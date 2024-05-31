package com.itmk.web.sys_fines.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sys_fines")
public class SysFines {
    @TableId(type = IdType.AUTO)
    private Long fineId;
    private Long borrowId;
    @TableField(exist = false)
    private String barcode;
    @TableField(exist = false)
    private Long readerId;
    @TableField(exist = false)
    private String username;
    @TableField(exist = false)
    private Long bookId;
    @TableField(exist = false)
    private String bookName;
    private Integer fineType;       // 罚款类型 0:逾期未还；1:损毁或丢失
    private Float fineFee;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date payTime;
    private String fineStatus; // 支付状态 0:未支付；1:已支付
}
