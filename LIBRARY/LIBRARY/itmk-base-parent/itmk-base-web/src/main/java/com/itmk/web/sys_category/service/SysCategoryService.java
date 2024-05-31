package com.itmk.web.sys_category.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itmk.web.sys_category.entity.ListCategoryParm;
import com.itmk.web.sys_category.entity.SysCategory;
import org.springframework.stereotype.Service;

@Service
public interface SysCategoryService extends IService<SysCategory> {
    IPage<SysCategory> getList(ListCategoryParm parm);

}
