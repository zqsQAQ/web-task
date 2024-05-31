package com.itmk.web.book_borrow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itmk.web.book_borrow.entity.*;
import com.itmk.web.sys_fines.entity.FineParm;
import org.apache.ibatis.annotations.Param;

public interface BorrowBookMapper extends BaseMapper<BorrowBook> {
    IPage<ReturnBook> getBorrowList(Page<ReturnBook> page, @Param("parm")ListParm parm);
    //借阅查看列表
    IPage<LookBorrow> getLookBorrowList(Page<LookBorrow> page, @Param("parm")LookParm parm);
    //某一读者借阅查看列表
    IPage<LookBorrow> getReaderLookBorrowList(Page<LookBorrow> page, @Param("parm") LookParm parm);
    //某一读者借书表
    IPage<ReturnBook> getReaderBorrowList(Page<ReturnBook> page, @Param("parm")ListParm parm);
    //某一读者借书未还的总数量
    int getReaderBorrowNum(@Param("readerId") Long readerId);
    //某一读者历史借书的总数量
    int getReaderHistoryBorrowNum(@Param("readerId") Long readerId);
    //所有借书未还的总数量
    int getAllBorrowNum();
    //所有历史借书的总数量
    int getAllHistoryBorrowNum();
    //所有异常还书的总数量
    int getAllExceptionNum();

    //计算距离还书的天数
    int countDays(@Param("borrowId")Long borrowId);
    //获取指定书的价格
    Float getPrice(@Param("bookId")Long bookId);
    //插入罚款信息（过期）
    int insertFineDue(@Param("parm")FineParm parm);
    //插入罚款信息（异常）
    int insertFineException(@Param("parm")FineParm parm);
    //返回未支付罚款的数量
    int checkFine(@Param("readerId")Long readerId);

    // 借书
    int borrowBooks(@Param("parm") BorrowBook param);
    // 正常还书
    int returnBooks(@Param("parm") ReturnParm param);
    // 异常还书
    int exceptionBooks0(@Param("parm") ExceptionParm param);
    // 丢失还书
    int exceptionBooks1(@Param("parm") ExceptionParm param);

    //根据书籍编号拼接出最大可借的barcode
    String concatBarcode(@Param("bookId") Long bookId);

    //根据借单返回barcode
    String getBarcodeByBorrow(@Param("borrowId") Long borrowId);

    //更新状态
    int borrowBookCopy(@Param("barcode") String barcode);
    //更新状态
    int returnBookCopy(@Param("barcode")String barcode);
    //更新状态
    int exceptionBookCopy(@Param("barcode")String barcode);

    //查询所有损毁的书的数量
    int getDamage();

    //查询所有丢失的书的数量
    int getLost();

    //查询预订的书
    int checkReserved(@Param("readerId")Long readerId);
}
