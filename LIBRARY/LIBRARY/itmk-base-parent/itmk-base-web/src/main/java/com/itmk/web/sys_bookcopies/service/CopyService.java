package com.itmk.web.sys_bookcopies.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itmk.web.sys_bookcopies.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CopyService extends IService<SysBookCopies> {

    List<SysBookCopies> getCopyList(ListParm parm);
    //更新状态
    int borrowBook(String barcode);
    //更新状态
    int returnBook(String barcode);
    //更新状态
    int exceptionBook(String barcode);

    //删除指定的复印本
    int deleteBookCopyByBar(String barcode);
    //删除某一本书的所有复印本记录
    int deleteBookCopy(Long bookId);
    //获取一本书的复印本数量
    int getCopyNum(Long bookId);
    //获取barcode对应的借阅状态
    String getStatus(String barcode);

    //返回当前可借的barcode
    String getBarcode(String bookCode);
    //返回当前可借的barcode
    String getBarcode(Long bookId);

    //根据book_code获取book_id
    Long getBookIdByCode(String bookCode);
    //根据barcode获取borrow_id（如果不存在则返回null）
    Long getBorrowIdByCode(String barcode);
    //添加借书记录
    int insertBorrow(BorrowBoParam param);
    //更新还书记录
    int updateReturn(Long borrowId);
    //更新还书记录
    int updateException(ExceptionParm param);

    //通过barcode借书
    int borrowByBar(BorrowParm parm);
    //通过barcode还书
    int returnByBar(String barcode);
    //通过barcode还书
    int exceptionByBar(ExceptionReParm parm);

    //计算距离还书的天数
    int countDays(Long borrowId);
    //插入罚款信息（过期）
    int insertFineDue(Long borrowId);
    //插入罚款信息（异常）
    int insertFineException(Long borrowId);

    //所有复印本的数量
    int getAllCopyNum();

}
