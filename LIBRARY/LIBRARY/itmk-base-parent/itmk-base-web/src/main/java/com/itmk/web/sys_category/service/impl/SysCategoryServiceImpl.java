package com.itmk.web.sys_category.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itmk.web.sys_category.entity.ListCategoryParm;
import com.itmk.web.sys_category.entity.SysCategory;
import com.itmk.web.sys_category.mapper.SysCategoryMapper;
import com.itmk.web.sys_category.service.SysCategoryService;
import org.springframework.stereotype.Service;

@Service
public class SysCategoryServiceImpl extends ServiceImpl<SysCategoryMapper, SysCategory> implements SysCategoryService {
    @Override
    public IPage<SysCategory> getList(ListCategoryParm parm) {
        //构造分页对象
        IPage<SysCategory> page = new Page<>();
        page.setSize(parm.getPageSize());
        page.setCurrent(parm.getCurrentPage());
        //构造查询条件
        QueryWrapper<SysCategory> query = new QueryWrapper<>();
        if(!StringUtils.isEmpty(parm.getCategoryName())){
            query.lambda().like(SysCategory::getCategoryName,parm.getCategoryName());
        }
        return this.baseMapper.selectPage(page,query);
    }
}
