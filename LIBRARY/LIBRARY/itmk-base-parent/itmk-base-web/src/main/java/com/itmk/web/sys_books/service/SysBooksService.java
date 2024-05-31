package com.itmk.web.sys_books.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itmk.web.sys_books.entity.ListParm;
import com.itmk.web.sys_books.entity.SysBooks;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

public interface SysBooksService extends IService<SysBooks> {
    IPage<SysBooks> getList(ListParm parm);
    //减库存
    int subBook(Long bookId);
    //加库存
    int addBook(Long bookId);
    //根据bookId查询bookCode(ISBN)
    String checkBookCode(Long bookId);
    //检查未归还的数量
    int checkUnreturned(Long bookId);

    //计算书的数量
    int checkBookNum();
}
