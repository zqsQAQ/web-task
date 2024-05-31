package com.itmk.web.sys_role.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itmk.utils.ResultUtils;
import com.itmk.utils.ResultVo;
import com.itmk.web.sys_role.entity.*;
import com.itmk.web.sys_role.service.SysRoleService;
import com.itmk.web.sys_role_menu.service.RoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/role")
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private RoleMenuService roleMenuService;

    //新增
    @PostMapping
    public ResultVo addRole(@RequestBody SysRole role){
        if(role.getRoleType().equals("2")){
            QueryWrapper<SysRole> query = new QueryWrapper<>();
            query.lambda().eq(SysRole::getRoleType,"2");
            SysRole one = sysRoleService.getOne(query);
            if(one != null){
                return ResultUtils.error("Reader role is existed!");
            }
        }
        role.setCreateTime(new Date());
        boolean save = sysRoleService.save(role);
        if(save){
            return ResultUtils.success("Add role successfully!");
        }
        return ResultUtils.error("Failed to add role");
    }
    //编辑
    @PutMapping
    public ResultVo editRole(@RequestBody SysRole role){
        if(role.getRoleType().equals("2")){
            QueryWrapper<SysRole> query = new QueryWrapper<>();
            query.lambda().eq(SysRole::getRoleType,"2");
            SysRole one = sysRoleService.getOne(query);
            if(one != null && role.getRoleId() != one.getRoleId()){
                return ResultUtils.error("Reader role is existed!");
            }
        }
        role.setCreateTime(new Date());
        boolean save = sysRoleService.updateById(role);
        if(save){
            return ResultUtils.success("Edit role successfully!");
        }
        return ResultUtils.error("Failed to edit role");
    }
    //删除
    @DeleteMapping("/{roleId}")
    public ResultVo deleteRole(@PathVariable("roleId") Long roleId){
        boolean remove =  sysRoleService.removeById(roleId);
        if(remove){
            return ResultUtils.success("Delete role successfully!");
        }
        return ResultUtils.error("Failed to delete role");
    }
    //查询列表
    @GetMapping("/list")
    public ResultVo getList(RoleParm parm){
        IPage<SysRole> list = sysRoleService.list(parm);
        return ResultUtils.success("Search role successfully!", list);
    }
    //查询角色权限树的回显
    @GetMapping("/getAssingShow")
    public ResultVo getAssingShow(AssignParm parm){
        AssignVo show = sysRoleService.getAssignShow(parm);
        return ResultUtils.success("Search successfully!",show);
    }
    //角色分配权限保存
    @PostMapping("/assignSave")
    public ResultVo assignSave(@RequestBody SaveAssign parm){
        roleMenuService.assignSave(parm.getRoleId(),parm.getList());
        return ResultUtils.success("Assign successfully!");
    }

}
