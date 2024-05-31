package com.itmk.web.sys_bookcopies.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itmk.web.sys_bookcopies.entity.*;
import com.itmk.web.sys_bookcopies.mapper.SysBookcopiesMapper;
import com.itmk.web.sys_bookcopies.service.CopyService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CopyServiceImpl extends ServiceImpl<SysBookcopiesMapper, SysBookCopies> implements CopyService {
    @Override
    public  List<SysBookCopies> getCopyList(ListParm parm) {
        List<SysBookCopies> list = new ArrayList<>();
        if(parm!= null)
            list = this.baseMapper.getCopyList(parm);
        return list;
    }

    @Override
    public int borrowBook(String barcode) {
        return this.baseMapper.borrowBook(barcode);
    }

    @Override
    public int returnBook(String barcode){
        return this.baseMapper.returnBook(barcode);
    }

    @Override
    public int exceptionBook(String barcode) {
        return this.baseMapper.exceptionBook(barcode);
    }

    @Override
    public int deleteBookCopyByBar(String barcode) {
        return this.baseMapper.deleteBookCopyByBar(barcode);
    }

    @Override
    public int deleteBookCopy(Long bookId) {
        return this.baseMapper.deleteBookCopy(bookId);
    }

    @Override
    public int getCopyNum(Long bookId) {
        return this.baseMapper.getCopyNum(bookId);
    }

    @Override
    public String getStatus(String barcode) {
        return this.baseMapper.getStatus(barcode);
    }

    @Override
    public String getBarcode(String bookCode) {
        List<SysBookCopies> list = this.baseMapper.getCopyList(new ListParm(bookCode));
        return list.get(list.size()-1).getBarcode();
    }

    @Override
    public String getBarcode(Long bookId) {
        List<SysBookCopies> list = this.baseMapper.getCopyList(new ListParm(bookId));
        return list.get(list.size()-1).getBarcode();
    }

    @Override
    public Long getBookIdByCode(String bookCode) {
        return this.baseMapper.getBookIdByCode(bookCode);
    }

    @Override
    public Long getBorrowIdByCode(String barcode) {
        return this.baseMapper.getBorrowIdByCode(barcode);
    }

    @Override
    public int insertBorrow(BorrowBoParam param) {
        return this.baseMapper.insertBorrow(param);
    }

    @Override
    public int updateReturn(Long borrowId) {
        return this.baseMapper.updateReturn(borrowId);
    }

    @Override
    public int updateException(ExceptionParm param) {
        return this.baseMapper.updateException(param);
    }

    @Override
    public int borrowByBar(BorrowParm parm) {
        String barcode = parm.getBarcode();
        String username = parm.getUsername();
        int index = barcode.indexOf('-');
        if(index == -1)
            return -1; // 条形码有误
        String status = this.baseMapper.getStatus(barcode);
        String bookCode = barcode.substring(0, index);
        Long bookId = this.baseMapper.getBookIdByCode(bookCode);
        if(bookId == null)
            return -2; // 书籍不存在
        if(!status.equals("0"))
            return -3; // 书籍复印本不存在或已经被借走
        Long readerId = this.baseMapper.getReaderId(username);
        if(readerId == null)
            return -4; // 用户不存在
        BorrowBoParam param = new BorrowBoParam();
        param.setBookId(bookId);
        param.setReaderId(readerId);
        param.setBarcode(barcode);
        param.setApplyStatus("1");
        param.setBorrowStatus("1");
        this.baseMapper.borrowBook(barcode);
        this.baseMapper.subBook(bookId);
        this.insertBorrow(param);
        return 0;
    }

    @Override
    public int returnByBar(String barcode) {
        int index = barcode.indexOf('-');
        if(index == -1)
            return -1; // 条形码有误
        String status = this.baseMapper.getStatus(barcode);
        String bookCode = barcode.substring(0, index);
        Long borrowId = this.baseMapper.getBorrowIdByCode(barcode);
        Long bookId = this.baseMapper.getBookIdByCode(bookCode);
        if(bookId == null)
            return -2; // 书籍不存在
        if(!status.equals("1") || borrowId == null)
            return -3; // 书籍复印本不存在或未借走
        // 判断是否有罚款
        int count = this.baseMapper.countDays(borrowId);
        FineParm fineParm = new FineParm();
        if(count > 0)
        {
            fineParm.setBorrowId(borrowId);
            fineParm.setFineFee((float)count);
            this.baseMapper.insertFineDue(fineParm);
        }
        this.baseMapper.addBook(bookId);
        this.baseMapper.returnBook(barcode);
        this.baseMapper.updateReturn(borrowId);
        return 0;
    }

    @Override
    public int exceptionByBar(ExceptionReParm parm) {
        String barcode = parm.getBarcode();
        String type = parm.getType();
        String returnStatus = "2";
        if(type == "1") returnStatus = "3";
        String exceptionText = parm.getExceptionText();

        int index = barcode.indexOf('-');
        System.out.println(">>>>>>> index " + index + "<<<<<<<<");
        if(index == -1)
            return -1; // 条形码有误

        String status = this.baseMapper.getStatus(barcode);
        String bookCode = barcode.substring(0, index);
        System.out.println(">>>>>>> bookCode " + bookCode + "<<<<<<<<");
        Long borrowId = this.baseMapper.getBorrowIdByCode(barcode);
        System.out.println(">>>>>>> borrowId " + borrowId + "<<<<<<<<");
        Long bookId = this.baseMapper.getBookIdByCode(bookCode);
        System.out.println(">>>>>>> bookId " + bookId + "<<<<<<<<");
        if(bookId == null)
            return -2; // 书籍不存在
        System.out.println(">>>>>>> status " + status + "<<<<<<<<");
        if(!status.equals("1") || borrowId == null)
            return -3; // 书籍复印本不存在或未借走

        //生成对应的罚款
        int fee = 0;
        int count = this.baseMapper.countDays(borrowId);
        if(count > 0)
            fee += count;
        fee += this.baseMapper.getPrice(bookId);
        FineParm fineParm = new FineParm();
        fineParm.setBorrowId(borrowId);
        fineParm.setFineFee((float)fee);
        this.baseMapper.insertFineException(fineParm);

        ExceptionParm param = new ExceptionParm();
        param.setBorrowId(borrowId);
        param.setReturnStatus(returnStatus);
        param.setExceptionText(exceptionText);
        this.baseMapper.updateException(param);
        this.baseMapper.exceptionBook(barcode);
        return 0;
    }

    @Override
    public int countDays(Long borrowId) {
        return this.baseMapper.countDays(borrowId);
    }

    @Override
    public int insertFineDue(Long borrowId) {
        int count = this.baseMapper.countDays(borrowId);
        FineParm parm = new FineParm();
        if(count <= 0)
            return 0;
        else
        {
            parm.setBorrowId(borrowId);
            parm.setFineFee((float)count);
            this.baseMapper.insertFineDue(parm);
            return count;
        }
    }

    @Override
    public int insertFineException(Long borrowId) {
        int fee = 0;
        int count = this.baseMapper.countDays(borrowId);
        Long bookId = this.baseMapper.getBookIdByBorrowId(borrowId);
        FineParm parm = new FineParm();
        if(count > 0)
            fee += count;
        fee += this.baseMapper.getPrice(bookId);
        parm.setBorrowId(borrowId);
        parm.setFineFee((float)fee);
        this.baseMapper.insertFineException(parm);
        return fee;
    }

    @Override
    public int getAllCopyNum() {
        return this.baseMapper.getAllCopyNum();
    }

}
