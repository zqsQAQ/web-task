package com.itmk.web.book_borrow.entity;

import lombok.Data;

@Data
public class ExceptionParm {

    private Long borrowId;
    //图书id
    private Long bookId;
    //表示还书状态（异常0 or 丢失1）
    private String type;
    //异常还书备注
    private String exceptionText;
}
