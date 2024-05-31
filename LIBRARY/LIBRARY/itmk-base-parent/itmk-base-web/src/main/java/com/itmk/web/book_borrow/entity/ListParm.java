package com.itmk.web.book_borrow.entity;

import lombok.Data;

@Data
public class ListParm {
    private Long userId;
    private Long currentPage;
    private Long pageSize;
    private String username;
    private String borrowStatus;
}
