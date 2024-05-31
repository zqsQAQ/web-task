package com.itmk.web.sys_bookcopies.entity;

import lombok.Data;

@Data
public class BorrowBoParam {
    private Long bookId;

    private String barcode;

    private Long readerId;

    private String applyStatus;

    private String borrowStatus;
}
