package com.itmk.web.sys_user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itmk.utils.ResultUtils;
import com.itmk.utils.ResultVo;
import com.itmk.web.sys_role.entity.SysRole;
import com.itmk.web.sys_role.service.SysRoleService;
import com.itmk.web.sys_user_role.entity.UserRole;
import com.itmk.web.sys_user_role.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import com.itmk.web.sys_user.entity.PageParm;
import com.itmk.web.sys_user.entity.SysUser;
import com.itmk.web.sys_user.service.SysUserService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private UserRoleService userRoleService;

    //新增用户
    @PostMapping
    public ResultVo addUser(@RequestBody SysUser user){
        //判断账户是否被占用
        QueryWrapper<SysUser> query = new QueryWrapper<>();
        query.lambda().eq(SysUser::getUsername, user.getUsername());
        SysUser one = sysUserService.getOne(query);
        if(one != null){
            return ResultUtils.error("This username is existed");
        }
        //密码加密
        user.setPassword(user.getPassword());
        //设置是否是管理 管理员已经提前创建好，这里默认都不是管理员
        user.setIsAdmin("1");
        //设置创建时间
        user.setCreateTime(new Date());
        //放入数据库
        sysUserService.addUser(user);
        return ResultUtils.success("Add user successfully");
    }

    //编辑用户
    @PutMapping
    public ResultVo editUser(@RequestBody SysUser user){
        //判断账户是否被占用
        QueryWrapper<SysUser> query = new QueryWrapper<>();
        query.lambda().eq(SysUser::getUsername, user.getUsername());
        SysUser one = sysUserService.getOne(query);
        if(one != null && one.getUserId() != user.getUserId()){
            return ResultUtils.error("This username is existed");
        }
        //密码加密
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        //设置更新时间
        user.setUpdateTime(new Date());
        //放入数据库
        sysUserService.editUser(user);
        return ResultUtils.success("Edit user successfully");
    }

    //删除用户
    @DeleteMapping("/{userId}")
    public ResultVo deleteUser(@PathVariable("userId") Long userId){
        boolean remove = sysUserService.removeById(userId);
        if(remove){
            return ResultUtils.success("Delete user successfully");
        }
        return ResultUtils.error("Failed to delete user");
    }

    //列表查询
    @GetMapping("/list")
    public ResultVo getList(PageParm parm){
        IPage<SysUser> list = sysUserService.list(parm);
        //处理密码  不显示密码
        list.getRecords().stream().forEach(item ->{
            item.setPassword("");
        });
        return ResultUtils.success("search successfully",list);
    }

    //查询角色列表
    @GetMapping("/getRoleList")
    public ResultVo getRoleList(){
        List<SysRole> list = sysRoleService.list();
        return ResultUtils.success("search successfully",list);
    }

    //根据用户id查询角色id
    @GetMapping("/getRoleId")
    public ResultVo getRoleId(String userId){
        QueryWrapper<UserRole> query = new QueryWrapper<>();
        query.lambda().eq(UserRole::getUserId,userId);
        UserRole one = userRoleService.getOne(query);
        return ResultUtils.success("search successfully",one);
    }
}

