package com.itmk.web.book_borrow.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class LookBorrow {
    private String bookCode;
    private String barcode;
    private Long readerId;
    private Long borrowId;
    private Long bookId;
    private String bookName;
    private String bookPlaceNum;
    private String username;
    private String learnNum;
    private String className;
    private String phone;
    private String borrowStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date borrowTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date expectReturnTime;
    //剩余时间
    private int remainDays;  // 新加的
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date returnTime;
    private String applyStatus;
    private String returnStatus;
    private String excepionText;
    private String applyText;
    private String timeStatus;
}
