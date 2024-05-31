package com.itmk.web.book_borrow.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@TableName("borrow_book")
public class BorrowBook {
    @TableId(type = IdType.AUTO)
    private Long borrowId;
    //图书id
    private Long bookId;
    //图书条形码
    private String barcode;
    //借书人id
    private Long readerId;
    //借书时间
    private Date borrowTime;
    //预期还书时间
    private Date expectReturnTime;
    //剩余时间
    @TableField(exist = false)
    private int remainDays;
    //还书时间
    private Date returnTime;
    //0: 待审核 1： 已审核  2：拒绝
    private String applyStatus;
    //0:审核中 1:在借中  2：已还   3：拒绝
    private String borrowStatus;
    //1: 正常还书 2：异常还书
    private String returnStatus;
    //异常还书备注
    private String excepionText;
    //审核拒绝备注
    private String applyText;
}
