package com.itmk.web.sys_reader.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itmk.web.sys_reader.entity.SysReader;
import org.apache.ibatis.annotations.Param;

public interface SysReaderMapper extends BaseMapper<SysReader> {

    int checkUnreturn(@Param("readerId") Long readerId);

    int checkUnpaid(@Param("readerId") Long readerId);

    int checkAllFines(@Param("readerId") Long readerId);

    int checkMemberNum();

    String checkLastBorrow(@Param("readerId") Long readerId);
    String checkLastReturn(@Param("readerId") Long readerId);

    int countUndeletableGraduates(@Param("entryYear") Integer entryYear);
    int deleteGraduates(@Param("entryYear") Integer entryYear);

    SysReader getReaderById(@Param("readerId") Long readerId);
}
