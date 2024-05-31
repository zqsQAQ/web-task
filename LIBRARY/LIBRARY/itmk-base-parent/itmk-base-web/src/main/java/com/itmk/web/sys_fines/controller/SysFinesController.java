package com.itmk.web.sys_fines.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itmk.jwt.JwtUtils;
import com.itmk.utils.ResultUtils;
import com.itmk.utils.ResultVo;
import com.itmk.web.book_borrow.entity.ReturnParm;
import com.itmk.web.sys_fines.entity.FineParm;
import com.itmk.web.sys_fines.entity.ListParm;
import com.itmk.web.sys_fines.entity.SysFines;
import com.itmk.web.sys_fines.service.SysFinesService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/fine")
public class SysFinesController {

    @Autowired
    private SysFinesService sysFinesService;

    @Autowired
    private JwtUtils jwtUtils;

    //罚款列表
    @GetMapping("/getFineList")
    public ResultVo getFineList(ListParm parm, HttpServletRequest request){
        //获取token
        String token = request.getHeader("token");
        Claims claims = jwtUtils.getClaimsFromToken(token);
        String userType = (String) claims.get("userType");
        IPage<SysFines> fineList = null;
        if(userType.equals("0")){ //0读者
            fineList = sysFinesService.getReaderFineList(parm);
            return ResultUtils.success("Search successfully!",fineList);
        }else if(userType.equals("1")){//1管理员
            fineList = sysFinesService.getFineList(parm);
            return ResultUtils.success("Search successfully!",fineList);
        }else {//如果类型不对  borrowList=null，即返回空表
            return ResultUtils.success("Search successfully!",fineList);
        }
    }

    //支付罚款
    @GetMapping("/payFine")
    public ResultVo payFine(@RequestParam("fineId")Long fineId){
       sysFinesService.payFine(fineId);
       return ResultUtils.success("Pay Successfully!");
    }


    @GetMapping("/getUnpaidNum")
    public int getUnpaidNum() {
        return sysFinesService.getUnpaidNum();
    }

    @GetMapping("/getPaidNum")
    public int getPaidNum() {
        return sysFinesService.getPaidNum();
    }

    @GetMapping("/getUnpaidFee")
    public float getUnpaidFee() {
        return sysFinesService.getUnpaidFee();
    }

    @GetMapping("/getPaidFee")
    public float getPaidFee() {
        return sysFinesService.getPaidFee();
    }

    @GetMapping("/getReaderUnpaidFee/{readerId}")
    public float getReaderUnpaidFee(@PathVariable("readerId")Long readerId) {
        return sysFinesService.getReaderUnpaidFee(readerId);
    }

    @GetMapping("/getReaderTotalFee/{readerId}")
    public float getReaderTotalFee(@PathVariable("readerId")Long readerId) {
        return sysFinesService.getReaderTotalFee(readerId);
    }

}
