package com.itmk.web.sys_bookcopies.entity;

import lombok.Data;

@Data
public class ExceptionReParm {
    private String barcode;
    //表示还书状态（异常0 or 丢失1）
    private String type;
    //异常还书备注
    private String exceptionText;
}
