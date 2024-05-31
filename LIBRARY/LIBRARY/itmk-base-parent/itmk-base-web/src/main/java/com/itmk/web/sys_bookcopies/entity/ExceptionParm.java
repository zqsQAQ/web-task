package com.itmk.web.sys_bookcopies.entity;

import lombok.Data;

@Data
public class ExceptionParm {

    private Long borrowId;
    //表示还书状态（异常2 or 丢失3）
    private String returnStatus;
    //异常还书备注
    private String exceptionText;
}