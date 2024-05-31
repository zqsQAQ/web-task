package com.itmk.web.sys_menu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itmk.utils.ResultUtils;
import com.itmk.utils.ResultVo;
import com.itmk.web.sys_menu.service.SysMenuService;
import com.itmk.web.sys_menu.entity.SysMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class SysMenuController {
    @Autowired
    private SysMenuService sysMenuService;

    //新增
    @PostMapping
    public ResultVo addMenu(@RequestBody SysMenu menu){
        menu.setCreateTime(new Date());
        boolean save = sysMenuService.save(menu);
        if(save){
            return ResultUtils.success("add menu successfully!");
        }
        return ResultUtils.error("Failed to add menu!");
    }
    //编辑
    @PutMapping
    public ResultVo editMenu(@RequestBody SysMenu menu){
        menu.setUpdateTime(new Date());
        boolean save = sysMenuService.updateById(menu);
        if(save){
            return ResultUtils.success("edit menu successfully!");
        }
        return ResultUtils.error("Failed to edit menu!");
    }
    //删除
    @DeleteMapping("/{menuId}")
    public ResultVo deleteMenu(@PathVariable("menuId") Long menuId){
        //判断是否有下级，若有下级则不能删除
        QueryWrapper<SysMenu> query = new QueryWrapper<>();
        query.lambda().eq(SysMenu::getParentId,menuId);
        List<SysMenu> list = sysMenuService.list(query);
        if(list.size() > 0) {
            return ResultUtils.error("This menu has subordinates and cannot be deleted!");
        }
        boolean remove = sysMenuService.removeById(menuId);
        if(remove){
            return ResultUtils.success("Delete successfully!");
        }
        return ResultUtils.error("Failed to delete!");
    }
    //菜单列表
    @GetMapping("/list")
    public ResultVo getList(){
        List<SysMenu> list = sysMenuService.menuList();
        return ResultUtils.success("search successfully!", list);
    }
    //上级菜单列表
    @GetMapping("/parent")
    public ResultVo getParentList(){
        List<SysMenu> list = sysMenuService.parentList();
        return ResultUtils.success("search successfully!", list);
    }
}
