package com.itmk.web.sys_login.controller;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itmk.jwt.JwtUtils;
import com.itmk.utils.ResultUtils;
import com.itmk.utils.ResultVo;
import com.itmk.web.sys_login.entity.LoginParm;
import com.itmk.web.sys_login.entity.LoginResult;
import com.itmk.web.sys_login.entity.UserInfo;
import com.itmk.web.sys_menu.entity.MakeTree;
import com.itmk.web.sys_menu.entity.RouterVO;
import com.itmk.web.sys_menu.entity.SysMenu;
import com.itmk.web.sys_menu.service.SysMenuService;
import com.itmk.web.sys_reader.entity.SysReader;
import com.itmk.web.sys_reader.service.SysReaderService;
import com.itmk.web.sys_user.entity.SysUser;
import com.itmk.web.sys_user.service.SysUserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/system")
public class LoginController {
    @Autowired
    private SysReaderService sysReaderService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private SysMenuService sysMenuService;

    //登录
    @PostMapping("/login")
    public ResultVo login(@RequestBody LoginParm parm) {
        if (StringUtils.isEmpty(parm.getUsername()) || StringUtils.isEmpty(parm.getPassword()) || StringUtils.isEmpty(parm.getUserType())) {
            return ResultUtils.error("Username, password or user type cannot be empty!");
        }
        String s = DigestUtils.md5DigestAsHex(parm.getPassword().getBytes());
        //判断是读者还是管理员
        if (parm.getUserType().equals("0")) { //0 ：读者
            //根据用户名和密码查询
            QueryWrapper<SysReader> query = new QueryWrapper<>();
//            query.lambda().eq(SysReader::getUsername, parm.getUsername())
//                    .eq(SysReader::getPassword, DigestUtils.md5DigestAsHex(parm.getPassword().getBytes()));//密码加密
            query.lambda().eq(SysReader::getUsername, parm.getUsername())
                    .eq(SysReader::getPassword, parm.getPassword());
            SysReader one = sysReaderService.getOne(query);
            if(one == null){
                return ResultUtils.error("Wrong user name or password!");
            }
            //返回数据给前端
            LoginResult result = new LoginResult();
            result.setUserId(one.getReaderId());
            result.setToken(jwtUtils.generateToken(one.getUsername(),parm.getUserType()));
            return ResultUtils.success("Login successfully!", result);
        } else if (parm.getUserType().equals("1")) { // 1: 系统用户
            //根据用户名和密码查询
            QueryWrapper<SysUser> query = new QueryWrapper<>();
//            query.lambda().eq(SysUser::getUsername, parm.getUsername())
//                    .eq(SysUser::getPassword, DigestUtils.md5DigestAsHex(parm.getPassword().getBytes()));
            query.lambda().eq(SysUser::getUsername, parm.getUsername())
                    .eq(SysUser::getPassword, parm.getPassword());
            SysUser one = sysUserService.getOne(query);
            if(one == null){
                return ResultUtils.error("Wrong user name or password!");
            }
            //返回数据给前端
            LoginResult result = new LoginResult();
            result.setUserId(one.getUserId());
            result.setToken(jwtUtils.generateToken(one.getUsername(),parm.getUserType()));
            return ResultUtils.success("Login successfully!", result);
        } else {
            return ResultUtils.error("User type does not exist!");
        }
    }

    //获取用户权限字段
    @GetMapping("/getInfo")
    public ResultVo getInfo(Long userId, HttpServletRequest request){
        //从请求的头部获取token
        String token = request.getHeader("token");
        //从token里面解析用户的类型
        Claims claims = jwtUtils.getClaimsFromToken(token);
        Object userType = claims.get("userType");
        //定义用户信息类
        UserInfo userInfo = new UserInfo();
        if(userType.equals("0")){ //读者
            //根据id查询读者信息
            SysReader reader = sysReaderService.getById(userId);
            userInfo.setIntroduction(reader.getLearnNum());
            userInfo.setName(reader.getLearnNum());
            userInfo.setAvatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
            //权限字段查询与设置
            List<SysMenu> menuList = sysMenuService.getReaderMenuByUserId(userId);
            List<String> collect = menuList.stream().filter(item -> item != null && item.getCode() != null).map(item -> item.getCode()).collect(Collectors.toList());
            if(collect.size() ==0){
                return ResultUtils.error("No login permission, please contact the administrator!");
            }
            //转成数组
            String[] strings = collect.toArray(new String[collect.size()]);
            userInfo.setRoles(strings);
            return ResultUtils.success("search successful!",userInfo);
        }else if(userType.equals("1")){ //管理员
            SysUser user = sysUserService.getById(userId);
            userInfo.setIntroduction(user.getNickName());
            userInfo.setName(user.getNickName());
            userInfo.setAvatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
            //权限字段查询与设置
            List<SysMenu> menuList = sysMenuService.getMenuByUserId(userId);
            List<String> collect = menuList.stream().filter(item -> item != null && item.getCode() != null).map(item -> item.getCode()).collect(Collectors.toList());
            if(collect.size() ==0){
                return ResultUtils.error("No login permission, please contact the administrator!");
            }
            //转成数组
            String[] strings = collect.toArray(new String[collect.size()]);
            userInfo.setRoles(strings);
            return ResultUtils.success("search successful!",userInfo);
        }else{
            return ResultUtils.success("User type does not exist!",userInfo);
        }
    }
    //获取菜单
    @GetMapping("/getMenuList")
    public ResultVo getMenuList(HttpServletRequest request){
        //获取token
        String token = request.getHeader("token");
        //获取用户名
        String username = jwtUtils.getUsernameFromToken(token);
        //获取用户类型
        Claims claims = jwtUtils.getClaimsFromToken(token);
        Object userType = claims.get("userType");
        if(userType.equals("0")){  //读者
            //获取用户信息
            SysReader reader = sysReaderService.loadByUsername(username);
            //获取权限信息
            List<SysMenu> menuList = sysMenuService.getReaderMenuByUserId(reader.getReaderId());
            List<SysMenu> collect = menuList.stream().filter(item -> item != null && !item.getType().equals("2")).collect(Collectors.toList());
            if(collect.size() == 0){
                return ResultUtils.error("No login permission, please contact the administrator!");
            }
            List<RouterVO> routerVOS = MakeTree.makeRouter(collect, 0L);
            return ResultUtils.success("search successful!",routerVOS);

        }else if(userType.equals("1")){  // 管理员
            //获取用户信息
            SysUser reader = sysUserService.loadByUsername(username);
            //获取权限信息
            List<SysMenu> menuList = sysMenuService.getMenuByUserId(reader.getUserId());
            List<SysMenu> collect = menuList.stream().filter(item -> item != null && !item.getType().equals("2")).collect(Collectors.toList());
            if(collect.size() == 0){
                return ResultUtils.error("No login permission, please contact the administrator!");
            }
            List<RouterVO> routerVOS = MakeTree.makeRouter(collect, 0L);
            return ResultUtils.success("search successful!",routerVOS);
        }else {
            return ResultUtils.error("User type does not exist!");
        }
    }
}
