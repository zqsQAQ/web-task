package com.itmk.web.sys_bookcopies.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itmk.utils.ResultUtils;
import com.itmk.utils.ResultVo;
import com.itmk.web.sys_bookcopies.entity.*;
import com.itmk.web.sys_bookcopies.service.CopyService;
import com.itmk.web.sys_books.entity.SysBooks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/copies")
public class SysBookcopiesController {

    @Autowired
    private CopyService copyService;

    //列表
    @GetMapping("/list")
    public ResultVo getCopyList(@RequestParam(value = "bookCode", required = false) String bookCode){
        ListParm parm = new ListParm(bookCode);
        List<SysBookCopies> list = copyService.getCopyList(parm);
        return ResultUtils.success("Search successfully!",list);
    }

    //借书（根据barcode)
    @PostMapping("/borrowCopy")
    public ResultVo borrowCopy(@RequestBody BorrowParm parm){
        int result = copyService.borrowByBar(parm);
        if(result == 0)
            return ResultUtils.success("Borrow successfully!");
        else if(result == -1)
            return ResultUtils.error("Incorrect barcode!");
        else if(result == -2)
            return ResultUtils.error("Book not exists!");
        else if(result == -3)
            return ResultUtils.error("Book not exists or book borrowed!");
        else if(result == -4)
            return ResultUtils.error("Member not exists!");
        return ResultUtils.error("Borrow failed!");
    }

    //还书（根据barcode)
    @GetMapping("/returnCopy")
    public ResultVo returnCopy(@RequestParam("barcode") String barcode){
        int result = copyService.returnByBar(barcode);
        if(result == 0)
            return ResultUtils.success("Return successfully!");
        else if(result == -1)
            return ResultUtils.error("Incorrect barcode!");
        else if(result == -2)
            return ResultUtils.error("Book not exists!");
        else if(result == -3)
            return ResultUtils.error("Book not exists or not borrowed!");
        return ResultUtils.error("Return failed!");
    }

    //异常还书（根据barcode)
    @PostMapping("/exceptionCopy")
    public ResultVo exceptionCopy(@RequestBody ExceptionReParm parm){
        int result = copyService.exceptionByBar(parm);
        if(result == 0)
            return ResultUtils.success("Exception return successfully!");
        else if(result == -1)
            return ResultUtils.error("Incorrect barcode!");
        else if(result == -2)
            return ResultUtils.error("Book not exists!");
        else if(result == -3)
            return ResultUtils.error("Book not exists or not borrowed!");
        return ResultUtils.error("Exception return failed!");
    }

    //获取所有复印本数量
    @GetMapping("/getCopyNum")
    public int getCopyNum(){
        return copyService.getAllCopyNum();
    }

}
