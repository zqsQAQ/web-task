package com.itmk.web.sys_fines.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itmk.web.sys_fines.entity.ListParm;
import com.itmk.web.sys_fines.entity.SysFines;
import com.itmk.web.sys_fines.mapper.SysFinesMapper;
import com.itmk.web.sys_fines.service.SysFinesService;
import org.springframework.stereotype.Service;

@Service
public class SysFinesServiceImpl extends ServiceImpl<SysFinesMapper, SysFines> implements SysFinesService {
    @Override
    public IPage<SysFines> getFineList(ListParm parm) {
        //构造分页对象
        Page<SysFines> page = new Page<>();
        page.setCurrent(parm.getCurrentPage());
        page.setSize(parm.getPageSize());
        return this.baseMapper.getFineList(page, parm);
    }

    @Override
    public IPage<SysFines> getReaderFineList(ListParm parm) {
        //构造分页对象
        Page<SysFines> page = new Page<>();
        page.setCurrent(parm.getCurrentPage());
        page.setSize(parm.getPageSize());
        return this.baseMapper.getReaderFineList(page, parm);
    }

    @Override
    public int payFine(Long fineId) {
        return this.baseMapper.payFine(fineId);
    }

    @Override
    public int getUnpaidNum() {
        return this.baseMapper.getUnpaidNum();
    }

    @Override
    public int getPaidNum() {
        return this.baseMapper.getPaidNum();
    }

    @Override
    public float getUnpaidFee() {
        return this.baseMapper.getUnpaidFee();
    }

    @Override
    public float getPaidFee() {
        return this.baseMapper.getPaidFee();
    }

    @Override
    public float getReaderUnpaidFee(Long readerId) {
        return this.baseMapper.getReaderUnpaidFee(readerId);
    }

    @Override
    public float getReaderTotalFee(Long readerId) {
        return this.baseMapper.getReaderTotalFee(readerId);
    }

}
