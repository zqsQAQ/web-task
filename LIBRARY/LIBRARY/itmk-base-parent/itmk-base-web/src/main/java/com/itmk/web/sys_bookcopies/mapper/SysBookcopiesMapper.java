package com.itmk.web.sys_bookcopies.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itmk.web.sys_bookcopies.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysBookcopiesMapper extends BaseMapper<SysBookCopies> {
    //写这里的getCopyList时，要做多表查询（sys_books sys_category）所以要自定义查询语句(sql)
    //这里方法的名字要与xml文件里select标签的id属性的值保持相同(getList)
    List<SysBookCopies> getCopyList(@Param("parm") ListParm parm);

    //更新复印本状态
    int borrowBook(@Param("barcode") String barcode);
    //更新复印本状态
    int returnBook(@Param("barcode")String barcode);
    //更新复印本状态
    int exceptionBook(@Param("barcode")String barcode);

    //获取barcode对应的借阅状态
    String getStatus(@Param("barcode") String barcode);
    //删除指定的复印本
    int deleteBookCopyByBar(@Param("barcode")String barcode);
    //删除某一本书的所有复印本记录
    int deleteBookCopy(@Param("bookId")Long bookId);
    //获取一本书的复印本数量（包括已借和未借）
    int getCopyNum(@Param("bookId")Long bookId);

    //减库存
    int subBook(@Param("bookId") Long bookId);
    //加库存
    int addBook(@Param("bookId") Long bookId);

    //根据book_code获取book_id
    Long getBookIdByCode(@Param("bookCode")String bookCode);
    //根据barcode获取borrow_id（如果不存在则返回null）
    Long getBorrowIdByCode(@Param("barcode")String barcode);
    //根据username获取reader_id
    Long getReaderId(@Param("username")String username);
    //添加借书记录
    int insertBorrow(@Param("param")BorrowBoParam param);
    //更新还书记录
    int updateReturn(@Param("borrowId")Long borrowId);
    //更新还书记录
    int updateException(@Param("param") ExceptionParm param);

    //根据borrowId获取bookId
    Long getBookIdByBorrowId(@Param("borrowId")Long borrowId);
    //计算距离还书的天数
    int countDays(@Param("borrowId")Long borrowId);
    //获取指定书的价格
    Float getPrice(@Param("bookId")Long bookId);
    //插入罚款信息（过期）
    int insertFineDue(@Param("parm") FineParm parm);
    //插入罚款信息（异常）
    int insertFineException(@Param("parm")FineParm parm);


    //所有复印本的数量
    int getAllCopyNum();

}
