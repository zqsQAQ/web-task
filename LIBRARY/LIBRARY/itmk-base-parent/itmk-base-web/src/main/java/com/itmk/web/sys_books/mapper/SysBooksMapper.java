package com.itmk.web.sys_books.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itmk.web.sys_books.entity.ListParm;
import com.itmk.web.sys_books.entity.SysBooks;
import org.apache.ibatis.annotations.Param;

public interface SysBooksMapper extends BaseMapper<SysBooks> {
    //写这里的getList时，要做多表查询（sys_books sys_category）所以要自定义查询语句(sql)
    //这里方法的名字要与xml文件里select标签的id属性的值保持相同(getList)
    IPage<SysBooks> getList(Page<SysBooks> page, @Param("parm") ListParm parm);

    //减库存
    int subBook(@Param("bookId") Long bookId);

    //加库存
    int addBook(@Param("bookId") Long bookId);

    //检查未归还的数量
    int checkUnreturned(@Param("bookId") Long bookId);

    //计算书的数量
    int checkBookNum();


}
