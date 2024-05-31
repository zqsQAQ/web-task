package com.itmk.web.sys_bookcopies.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("sys_bookcopies")
public class SysBookCopies{
    @TableId(type = IdType.AUTO)
    private Long copyId;
    private String barcode; // ISBN+num
    private Long bookId;
    private Long copyNum;
    private String borrowStatus;
    //bookCode不属于数据库，用注解排除
    @TableField(exist = false)
    private  String bookCode;
    //bookStore不属于数据库，用注解排除
    @TableField(exist = false)
    private Integer bookStore;

    public SysBookCopies(String barcode, Long bookId, Long copyNum, String borrowStatus) {
        this.barcode = barcode;
        this.bookId = bookId;
        this.copyNum = copyNum;
        this.borrowStatus = borrowStatus;
    }
}
