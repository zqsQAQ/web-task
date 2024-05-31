package com.itmk.web.sys_fines.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itmk.web.sys_fines.entity.ListParm;
import com.itmk.web.sys_fines.entity.SysFines;
import org.apache.ibatis.annotations.Param;

public interface SysFinesService extends IService<SysFines> {

    IPage<SysFines> getFineList(ListParm parm);

    IPage<SysFines> getReaderFineList(ListParm parm);

    int payFine(Long fineId);

    int getUnpaidNum();
    int getPaidNum();

    float getUnpaidFee();
    float getPaidFee();

    float getReaderUnpaidFee(Long readerId);
    float getReaderTotalFee(Long readerId);

}
