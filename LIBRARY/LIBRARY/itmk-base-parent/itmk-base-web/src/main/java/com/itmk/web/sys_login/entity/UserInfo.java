package com.itmk.web.sys_login.entity;

import lombok.Data;

/**
 * 返回前端的用户信息
 */
@Data
public class UserInfo {
    private String name;
    private String avatar;
    private String introduction;
    private Object[] roles;
}
