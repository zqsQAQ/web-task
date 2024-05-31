package com.itmk.web.sys_bookcopies.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ListParm {
    private Long bookId;
    private String bookCode;
    private String barcode;

    public ListParm(String bookCode)
    {
        this.bookCode = bookCode;
    }

    public ListParm(Long bookId)
    {
        this.bookId = bookId;
    }
}
