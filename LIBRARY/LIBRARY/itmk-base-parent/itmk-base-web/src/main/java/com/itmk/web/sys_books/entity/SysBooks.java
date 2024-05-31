package com.itmk.web.sys_books.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("sys_books")
public class SysBooks {
    @TableId(type = IdType.AUTO)
    private Long bookId;
    private Long categoryId;

    //categoryName不属于数据库，用注解排除
    @TableField(exist = false)
    private String categoryName;

    private String bookName;
    private String bookCode;
    private String bookPlaceNum;
    private String bookAuther;
    private String bookProduct;
    private Float bookPrice;
    private Integer bookStore;
    private String bookFloor;
}
