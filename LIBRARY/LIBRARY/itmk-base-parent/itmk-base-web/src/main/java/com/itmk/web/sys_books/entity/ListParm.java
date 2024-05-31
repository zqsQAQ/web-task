package com.itmk.web.sys_books.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ListParm {
    private Long currentPage;
    private Long pageSize;
    private String categoryId;
    private String bookName;
    private String bookPlaceNum;
    private String bookFloor;
    private String bookAuther;
}
