package com.itmk.web.book_borrow.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ReturnBook {
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
    private int remainDays;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date returnTime;
}
