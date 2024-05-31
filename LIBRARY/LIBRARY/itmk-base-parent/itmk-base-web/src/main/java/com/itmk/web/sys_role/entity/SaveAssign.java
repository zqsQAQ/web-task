package com.itmk.web.sys_role.entity;

import lombok.Data;

import java.util.List;

@Data
public class SaveAssign {
    private Long roleId;
    private List<Long> list; //接受前端传递过来的参数
}
