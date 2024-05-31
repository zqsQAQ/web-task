package com.itmk.web.sys_fines.entity;

import lombok.Data;

@Data
public class ListParm {
    private Long currentPage;
    private Long pageSize;
    private Long readerId;
    private String username;
    private Integer fineType;
    private String fineStatus;
}
