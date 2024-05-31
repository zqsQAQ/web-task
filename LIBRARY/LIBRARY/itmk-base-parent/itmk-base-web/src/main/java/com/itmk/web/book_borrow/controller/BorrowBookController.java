package com.itmk.web.book_borrow.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itmk.jwt.JwtUtils;
import com.itmk.utils.ResultUtils;
import com.itmk.utils.ResultVo;
import com.itmk.web.book_borrow.entity.*;
import com.itmk.web.book_borrow.service.BorrowBookService;
import com.itmk.web.sys_bookcopies.service.CopyService;
import com.itmk.web.sys_books.service.SysBooksService;
import com.itmk.web.sys_fines.entity.FineParm;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/borrow")
public class BorrowBookController {
    @Autowired
    private BorrowBookService borrowBookService;

    @Autowired
    private SysBooksService sysBooksService;

    @Autowired
    private CopyService copyService;

    @Autowired
    private JwtUtils jwtUtils;

    //借书
    @PostMapping
    public ResultVo borrow(@RequestBody BorrowParm parm, HttpServletRequest request){
        String token = request.getHeader("token");
        Claims claims = jwtUtils.getClaimsFromToken(token);
        String userType = (String) claims.get("userType");
        List<Long> LL = parm.getBookIds();
        int fineNum = borrowBookService.checkFine(parm.getReaderId());
        if(fineNum > 0)
            return ResultUtils.error("Cannot reserve when fine not paid!");
        if(borrowBookService.getReaderBorrowNum(parm.getReaderId()) + LL.size() > 5)
            return ResultUtils.error("Cannot reserve more than 5 books!");
        borrowBookService.borrow(parm,userType);
        for(int i=0; i<LL.size(); i++)
        {
            String barcode = borrowBookService.getMaxBarcode(LL.get(i));
            borrowBookService.borrowBookCopy(barcode);
        }
        return ResultUtils.success("Reserve successfully!");
    }

    //还书列表
    @GetMapping("/getBorrowList")
    public ResultVo getBorrowList(ListParm parm,HttpServletRequest request){
        //获取token
        String token = request.getHeader("token");
        Claims claims = jwtUtils.getClaimsFromToken(token);
        String userType = (String) claims.get("userType");
        IPage<ReturnBook> borrowList = null;
        if(userType.equals("0")){ //0读者
            borrowList = borrowBookService.getReaderBorrowList(parm);
            return ResultUtils.success("Search successfully!",borrowList);
        }else if(userType.equals("1")){//1管理员
            borrowList = borrowBookService.getBorrowList(parm);
            return ResultUtils.success("Search successfully!",borrowList);
        }else {//如果类型不对  borrowList=null，即返回空表
            return ResultUtils.success("Search successfully!",borrowList);
        }
    }

    //还书
    @PostMapping("/returnBook")
    public ResultVo returnBook(@RequestBody List<ReturnParm> parm){
        for(int i=0; i< parm.size(); i++)
        {
            Long borrowId = parm.get(i).getBorrowId();
            int countDays = borrowBookService.countDays(borrowId);
            if(countDays > 0)
            {
                FineParm fineParm = new FineParm();
                fineParm.setBorrowId(borrowId);
                fineParm.setFineFee((float)countDays);
                borrowBookService.insertFineDue(fineParm);
            }
            String barcode =  borrowBookService.getBarcodeByBorrow(parm.get(i).getBorrowId());
            borrowBookService.returnBookCopy(barcode);
        }
        borrowBookService.returnBook(parm);
        return ResultUtils.success("Return book successfully!");
    }

    //异常还书
    @PostMapping("/exceptionReturnBook")
    public ResultVo exceptionReturnBook(@RequestBody ExceptionParm parm){
        Long borrowId = parm.getBorrowId();
        String barcode =  borrowBookService.getBarcodeByBorrow(parm.getBorrowId());
        FineParm fineParm = new FineParm();
        fineParm.setBorrowId(borrowId);
        fineParm.setFineFee(borrowBookService.getPrice(parm.getBookId()));
        borrowBookService.exceptionBookCopy(barcode);
        borrowBookService.exceptionBook(parm);
        borrowBookService.insertFineException(fineParm);
        return ResultUtils.success("Exception return book successfully!");
    }

    //借阅查看
    @GetMapping("/getLookBorrowList")
    public ResultVo getLookBorrowList(LookParm parm,HttpServletRequest request){
        //获取token
        String token = request.getHeader("token");
        Claims claims = jwtUtils.getClaimsFromToken(token);
        String userType = (String) claims.get("userType");
        IPage<LookBorrow> borrowList = null;
        if(userType.equals("0")){ //0读者
            borrowList = borrowBookService.getReaderLookBorrowList(parm);
            return ResultUtils.success("Search successfully!",borrowList);
        }else if(userType.equals("1")){//1管理员
            borrowList = borrowBookService.getLookBorrowList(parm);
            return ResultUtils.success("Search successfully!",borrowList);
        }else {//如果类型不对  borrowList=null，即返回空表
            return ResultUtils.success("Search successfully!",borrowList);
        }
    }

    @GetMapping("/getReaderBorrowNum/{readerId}")
    public int getReaderBorrowNum(@PathVariable("readerId") Long readerId) {
        return borrowBookService.getReaderBorrowNum(readerId);
    }

    @GetMapping("/getReaderHistoryBorrowNum/{readerId}")
    public int getReaderHistoryBorrowNum(@PathVariable("readerId")Long readerId) {
        return borrowBookService.getReaderHistoryBorrowNum(readerId);
    }

    @GetMapping("/getAllBorrowNum")
    public int getAllBorrowNum() {
        return borrowBookService.getAllBorrowNum();
    }

    @GetMapping("/getAllHistoryBorrowNum")
    public int getAllHistoryBorrowNum() {
        return borrowBookService.getAllHistoryBorrowNum();
    }

    @GetMapping("/getAllExceptionNum")
    public int getAllExceptionNum() {
        return borrowBookService.getAllExceptionNum();
    }

    @GetMapping("/getDamage")
    public int getDamage() {
        return borrowBookService.getDamage();
    }

    @GetMapping("/getLost")
    public int getLost() {
        return borrowBookService.getLost();
    }

    @GetMapping("/getReserve/{borrowId}")
    public ResultVo getReserve(@PathVariable("borrowId")Long borrowId)
    {
        BorrowBook borrowBook = borrowBookService.getById(borrowId);
        if(borrowBookService.getReaderBorrowNum(borrowBook.getReaderId())  >= 5)
            return ResultUtils.error("Cannot borrow more than 5 books!");
        borrowBook.setBorrowStatus("1");
        borrowBookService.updateById(borrowBook);
        return ResultUtils.success("Get Reserved Book Successfully!");
    }

    @GetMapping("/cancelReserve/{borrowId}")
    public ResultVo cancelReserve(@PathVariable("borrowId")Long borrowId)
    {
        BorrowBook borrowBook = borrowBookService.getById(borrowId);
        String barcode = borrowBook.getBarcode();
        String bookcode = barcode.substring(0, barcode.indexOf('-'));
        Long bookId = copyService.getBookIdByCode(bookcode);
        if(borrowBookService.getReaderBorrowNum(borrowBook.getReaderId())  >= 5)
            return ResultUtils.error("Cannot borrow more than 5 books!");
        if(!borrowBook.getBorrowStatus().equals("4"))
            return ResultUtils.error("Book not reserved!");
        borrowBookService.returnBookCopy(barcode);
        ReturnParm returnParm = new ReturnParm();
        returnParm.setBookId(bookId);
        returnParm.setBorrowId(borrowId);
        List<ReturnParm> list = new ArrayList<>();
        list.add(returnParm);
        borrowBookService.returnBook(list);
        borrowBook.setBorrowStatus("5");
        borrowBookService.updateById(borrowBook);
        return ResultUtils.success("Cancel Reserved Book Successfully!");
    }

    @GetMapping("/reserveNotice/{readerId}")
    public ResultVo reserveNotice(@PathVariable("readerId")Long readerId)
    {
        int count = borrowBookService.checkReserved(readerId);
        if(count > 0)
            return ResultUtils.error("You have made " + count + " reservation(s)!");
        else
            return ResultUtils.success("You have no reservation!");
    }
}
