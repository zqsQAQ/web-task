package com.itmk.web.sys_fines.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itmk.web.sys_fines.entity.FineParm;
import com.itmk.web.sys_fines.entity.ListParm;
import com.itmk.web.sys_fines.entity.SysFines;
import org.apache.ibatis.annotations.Param;

public interface SysFinesMapper extends BaseMapper<SysFines> {

    IPage<SysFines> getFineList(Page<SysFines> page, @Param("parm") ListParm parm);

    IPage<SysFines> getReaderFineList(Page<SysFines> page, @Param("parm") ListParm parm);

    int payFine(@Param("fineId")Long fineId);

    int getUnpaidNum();
    int getPaidNum();

    float getUnpaidFee();
    float getPaidFee();

    float getReaderUnpaidFee(@Param("readerId")Long readerId);
    float getReaderTotalFee(@Param("readerId")Long readerId);
}
