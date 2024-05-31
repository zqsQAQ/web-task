package com.itmk.web.sys_books.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itmk.utils.ResultUtils;
import com.itmk.utils.ResultVo;
import com.itmk.web.sys_bookcopies.entity.SysBookCopies;
import com.itmk.web.sys_bookcopies.service.CopyService;
import com.itmk.web.sys_books.entity.ListParm;
import com.itmk.web.sys_books.entity.SysBooks;
import com.itmk.web.sys_books.service.SysBooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class SysBooksController {
    @Autowired
    private SysBooksService sysBooksService;

    @Autowired
    private CopyService copyService;

    //新增
    @PostMapping
    public ResultVo add(@RequestBody SysBooks books){
        boolean save = sysBooksService.save(books);
        String bookCode = books.getBookCode();
        Long bookId = books.getBookId();
        for(int i=1; i<=books.getBookStore(); i++)
        {
            String barcode = bookCode + "-" + i;
            copyService.save(new SysBookCopies(barcode, bookId, (long)i,"0"));
        }
        if(save){
            return ResultUtils.success("Add book successfully!");
        }
        return ResultUtils.error("Failed to add book!");
    }

    //编辑
    @PutMapping
    public ResultVo edit(@RequestBody SysBooks books){
        String bookCode = books.getBookCode();
        Long bookId = books.getBookId();
        int oldNum = copyService.getCopyNum(bookId);
        int newNum = books.getBookStore();
        int unreturnNum = sysBooksService.checkUnreturned(bookId);
        if(unreturnNum > newNum)
            return ResultUtils.error("Having books which are not returned!");
        boolean save = sysBooksService.updateById(books);
        if(save){
            if(newNum>oldNum)
            {
                for(int i=oldNum+1; i<=newNum; i++)
                {
                    String barcode = bookCode + "-" + i;
                    copyService.save(new SysBookCopies(barcode, bookId, (long)i,"0"));
                }
            }
            else if(newNum<oldNum)
            {
                int count = oldNum-newNum;
                for(int i=oldNum; i>0; i--)
                {
                    String barcode = bookCode + "-" + i;
                    if(!copyService.getStatus(barcode).equals("1"))
                    {
                        copyService.deleteBookCopyByBar(barcode);
                        count--;
                    }
                    if(count == 0) break;
                }
            }
            return ResultUtils.success("Edit book successfully!");
        }
        return ResultUtils.error("Failed to edit book!");
    }

    //删除
    @DeleteMapping("/{bookId}")
    public ResultVo delete(@PathVariable("bookId") Long bookId){
        int unreturnNum = sysBooksService.checkUnreturned(bookId);
        if(unreturnNum > 0)
            return ResultUtils.error("Having books not returned!");
        boolean remove = sysBooksService.removeById(bookId);
        if(remove){
            copyService.deleteBookCopy(bookId);
            return ResultUtils.success("Delete book successfully!");
        }
        return ResultUtils.error("Failed to delete book!");
    }

    //列表
    @GetMapping("/list")
    public ResultVo getList(ListParm parm){
        IPage<SysBooks> list = sysBooksService.getList(parm);
        return ResultUtils.success("Search successfully!",list);
    }

    //列表
    @GetMapping("/getBookNum")
    public int getBookNum(){
        return sysBooksService.checkBookNum();
    }
}
