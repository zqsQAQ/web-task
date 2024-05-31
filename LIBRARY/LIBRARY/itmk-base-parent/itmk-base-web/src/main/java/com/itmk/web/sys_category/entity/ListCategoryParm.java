package com.itmk.web.sys_category.entity;

import lombok.Data;

@Data
public class ListCategoryParm {
    private Long currentPage;
    private Long pageSize;
    private String categoryName;
}
