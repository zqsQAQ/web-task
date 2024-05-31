package com.itmk.web.book_borrow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itmk.exception.BusinessException;
import com.itmk.exception_advice.BusinessExceptionEnum;
import com.itmk.web.book_borrow.entity.*;
import com.itmk.web.book_borrow.mapper.BorrowBookMapper;
import com.itmk.web.book_borrow.service.BorrowBookService;
import com.itmk.web.sys_bookcopies.entity.SysBookCopies;
import com.itmk.web.sys_bookcopies.service.CopyService;
import com.itmk.web.sys_bookcopies.service.impl.CopyServiceImpl;
import com.itmk.web.sys_books.entity.SysBooks;
import com.itmk.web.sys_books.service.SysBooksService;
import com.itmk.web.sys_fines.entity.FineParm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
public class BorrowBookServiceImpl extends ServiceImpl<BorrowBookMapper, BorrowBook> implements BorrowBookService {

    @Autowired
    private SysBooksService sysBooksService;

    private Lock lock = new ReentrantLock();

    @Override
    public int getReaderBorrowNum(Long readerId) {
        return this.baseMapper.getReaderBorrowNum(readerId);
    }

    @Override
    public int getReaderHistoryBorrowNum(Long readerId) {
        return this.baseMapper.getReaderHistoryBorrowNum(readerId);
    }

    @Override
    public int getAllBorrowNum() {
        return this.baseMapper.getAllBorrowNum();
    }

    @Override
    public int getAllHistoryBorrowNum() {
        return this.baseMapper.getAllHistoryBorrowNum();
    }

    @Override
    public int getAllExceptionNum() {
        return this.baseMapper.getAllExceptionNum();
    }

    @Override
    public String getBarcodeByBorrow(Long borrowId) {
        return this.baseMapper.getBarcodeByBorrow(borrowId);
    }

    @Override
    public String getMaxBarcode(Long bookId) {
        return this.baseMapper.concatBarcode(bookId);
    }

    @Override
    public int countDays(Long borrowId) { return this.baseMapper.countDays(borrowId); }

    @Override
    public Float getPrice(Long bookId) {
        return this.baseMapper.getPrice(bookId);
    }

    @Override
    public int insertFineDue(FineParm parm) {
        return this.baseMapper.insertFineDue(parm);
    }

    @Override
    public int insertFineException(FineParm parm) {
        return this.baseMapper.insertFineException(parm);
    }

    @Override
    public int checkFine(Long readerId) {
        return this.baseMapper.checkFine(readerId);
    }

    @Override
    @Transactional
    public void borrow(BorrowParm parm,String userType) {
            //构造查询条件  list : BookId在BookIds中的书
            QueryWrapper<SysBooks> query = new QueryWrapper<>();
            query.lambda().in(SysBooks::getBookId,parm.getBookIds());
            List<SysBooks> list = sysBooksService.list(query);
            //查询该书的库存(可能并发，但是不会太大，使用锁)  collect : 库存不足的书
            List<SysBooks> collect = list.stream().filter(item -> item.getBookStore().longValue() < 1L).collect(Collectors.toList());
            if(collect.size() > 0){
                //提示哪本书库存不足 stringList : 库存不足的书的bookName
                List<String> stringList = collect.stream().map(SysBooks::getBookName).collect(Collectors.toList());
                throw new BusinessException(BusinessExceptionEnum.NO_STOCK.getCode(),
                       stringList + BusinessExceptionEnum.NO_STOCK.getMessage());
            }
            //如果库存充足 : 减库存  插入借书表中
            List<Long> bookIds = parm.getBookIds();
            for (int i = 0; i < bookIds.size(); i++){
                Long bookId = bookIds.get(i);
                //减库存
                int res = sysBooksService.subBook(bookId);
                //插入借书表
                if(res > 0){
                    BorrowBook borrowBook = new BorrowBook();
                    borrowBook.setBookId(bookId);
//                  String barcode = copyService.getBarcode(bookId);
//                  borrowBook.setBarcode(barcode);
                    borrowBook.setReaderId(parm.getReaderId());
                    borrowBook.setReturnTime(parm.getReturnTime());
                    borrowBook.setBarcode(this.baseMapper.concatBarcode(bookId));

                    if(userType.equals("0")){  //0读者
                        borrowBook.setApplyStatus("0");
                        borrowBook.setBorrowStatus("0");
                    }else if(userType.equals("1")){ //1管理员
                        borrowBook.setApplyStatus("1");
                        borrowBook.setBorrowStatus("1");
                    }else{
                        throw new BusinessException(500,"User type does not exist, can not borrow book!");
                    }
                    borrowBook.setApplyStatus("1");
                    borrowBook.setBorrowStatus("4");
                    borrowBook.setBorrowTime(new Date());
                    borrowBook.setExpectReturnTime(new Date());
                    //插入借书表
                    this.baseMapper.borrowBooks(borrowBook);
                }
            }
    }

    @Override
    public IPage<ReturnBook> getBorrowList(ListParm parm) {
        //构造分页对象
        Page<ReturnBook> page = new Page<>();
        page.setCurrent(parm.getCurrentPage());
        page.setSize(parm.getPageSize());
        return this.baseMapper.getBorrowList(page,parm);
    }

    @Override
    @Transactional
    public void returnBook(List<ReturnParm> list) {
        //加库存 变更借书表里的借书状态
        for(int i = 0; i < list.size(); i++){
            //加库存
            int res = sysBooksService.addBook(list.get(i).getBookId());
            //加库存成功 则变更借书状态
            if(res > 0){
                this.baseMapper.returnBooks(list.get(i));
            }
        }
    }

    @Override
    public void exceptionBook(ExceptionParm parm) {
        //type:   0损坏   1丢失
        String type = parm.getType();
        //书籍损坏
        if(type.equals("0")){
             this.baseMapper.exceptionBooks0(parm);
        }
        //书籍丢失
        else {
            this.baseMapper.exceptionBooks1(parm);
        }
    }

    @Override
    public IPage<LookBorrow> getLookBorrowList(LookParm parm) {
        //构造分页对象
        Page<LookBorrow> page = new Page<>();
        page.setSize(parm.getPageSize());
        page.setCurrent(parm.getCurrentPage());
        return this.baseMapper.getLookBorrowList(page,parm);
    }

    @Override
    public IPage<LookBorrow> getReaderLookBorrowList(LookParm parm) {
        //构造分页对象
        Page<LookBorrow> page = new Page<>();
        page.setSize(parm.getPageSize());
        page.setCurrent(parm.getCurrentPage());
        return this.baseMapper.getReaderLookBorrowList(page,parm);
    }

    @Override
    public IPage<ReturnBook> getReaderBorrowList(ListParm parm) {
        //构造分页对象
        Page<ReturnBook> page = new Page<>();
        page.setCurrent(parm.getCurrentPage());
        page.setSize(parm.getPageSize());
        return this.baseMapper.getReaderBorrowList(page,parm);
    }

    @Override
    public int borrowBookCopy(String barcode) {
        return this.baseMapper.borrowBookCopy(barcode);
    }

    @Override
    public int returnBookCopy(String barcode) {
        return this.baseMapper.returnBookCopy(barcode);
    }

    @Override
    public int exceptionBookCopy(String barcode) {
        return this.baseMapper.exceptionBookCopy(barcode);
    }

    @Override
    public int getDamage() {
        return this.baseMapper.getDamage();
    }

    @Override
    public int getLost() {
        return this.baseMapper.getLost();
    }

    @Override
    public int checkReserved(Long readerId) {
        return this.baseMapper.checkReserved(readerId);
    }

}
