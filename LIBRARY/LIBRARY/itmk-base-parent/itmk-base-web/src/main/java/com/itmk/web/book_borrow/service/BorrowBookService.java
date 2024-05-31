package com.itmk.web.book_borrow.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itmk.web.book_borrow.entity.*;
import com.itmk.web.sys_fines.entity.FineParm;
import org.apache.ibatis.annotations.Param;


import java.util.List;

public interface BorrowBookService extends IService<BorrowBook> {
    //某一读者借书未还的总数量
    int getReaderBorrowNum(Long readerId);
    //某一读者历史借书的总数量
    int getReaderHistoryBorrowNum(Long readerId);
    //所有借书未还的总数量
    int getAllBorrowNum();
    //所有历史借书的总数量
    int getAllHistoryBorrowNum();
    //所有异常还书的总数量
    int getAllExceptionNum();
    //借书
    void borrow(BorrowParm parm,String userType);
    //还书列表
    IPage<ReturnBook> getBorrowList(ListParm parm);
    //还书
    void returnBook(List<ReturnParm> list);
    //异常还书
    void exceptionBook(ExceptionParm parm);
    //借阅查看列表
    IPage<LookBorrow> getLookBorrowList(LookParm parm);
    //某一读者借阅查看列表
    IPage<LookBorrow> getReaderLookBorrowList(LookParm parm);
    //某一读者借书表
    IPage<ReturnBook> getReaderBorrowList(ListParm parm);

    //根据借单返回barcode
    String getBarcodeByBorrow(Long borrowId);

    //根据书籍编号返回可借的最大barcode
    String getMaxBarcode(Long bookId);

    //计算距离还书的天数
    int countDays(Long borrowId);
    //获取指定书的价格
    Float getPrice(Long bookId);
    //插入罚款信息（过期）
    int insertFineDue(FineParm parm);
    //插入罚款信息（异常）
    int insertFineException(FineParm parm);
    //返回未支付罚款的数量
    int checkFine(Long readerId);

    //更新状态
    int borrowBookCopy( String barcode);
    //更新状态
    int returnBookCopy(String barcode);
    //更新状态
    int exceptionBookCopy(String barcode);

    //查询所有损毁的书的数量
    int getDamage();
    //查询所有丢失的书的数量
    int getLost();

    //查询预订的书
    int checkReserved(Long readerId);

}
