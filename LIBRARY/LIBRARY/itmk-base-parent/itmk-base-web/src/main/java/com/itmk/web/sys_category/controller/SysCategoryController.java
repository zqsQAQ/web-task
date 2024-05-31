package com.itmk.web.sys_category.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itmk.utils.ResultUtils;
import com.itmk.utils.ResultVo;
import com.itmk.web.sys_category.entity.ListCategoryParm;
import com.itmk.web.sys_category.entity.SysCategory;
import com.itmk.web.sys_category.service.SysCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;

@RestController
@RequestMapping("/api/category")
public class SysCategoryController {
    @Autowired
    private SysCategoryService sysCategoryService;

    //新增
    @PostMapping
    public ResultVo add(@RequestBody SysCategory category){
        boolean save = sysCategoryService.save(category);
        if(save){
            return ResultUtils.success("Add successfully!");
        }
        return ResultUtils.error("Failed to add!");
    }
    //编辑
    @PutMapping
    public ResultVo edit(@RequestBody SysCategory category){
        boolean save = sysCategoryService.updateById(category);
        if(save){
            return ResultUtils.success("Edit successfully!");
        }
        return ResultUtils.error("Failed to edit!");
    }
    //删除
    @DeleteMapping("/{categoryId}")
    public ResultVo delete(@PathVariable("categoryId") Long categoryId){
        boolean remove = sysCategoryService.removeById(categoryId);
        if(remove){
            return ResultUtils.success("Delete successfully!");
        }
        return ResultUtils.error("Failed to delete!");
    }
    //列表
    @GetMapping("/list")
    public ResultVo getList(ListCategoryParm parm){
        IPage<SysCategory> list = sysCategoryService.getList(parm);
        return ResultUtils.success("Search successfully!",list);
    }

    //图书类别
    @GetMapping("/cateList")
    public ResultVo getCateList(){
        List<SysCategory> list = sysCategoryService.list();
        return ResultUtils.success("Search succcessfully!", list);
    }

}
