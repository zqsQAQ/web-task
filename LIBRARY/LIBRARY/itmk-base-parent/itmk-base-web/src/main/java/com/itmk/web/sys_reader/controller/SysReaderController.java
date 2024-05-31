package com.itmk.web.sys_reader.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itmk.utils.ResultUtils;
import com.itmk.utils.ResultVo;
import com.itmk.web.sys_reader.service.SysReaderService;
import com.itmk.web.sys_reader.entity.ReaderParm;
import com.itmk.web.sys_reader.entity.SysReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reader")
public class SysReaderController {
    @Autowired
    private SysReaderService sysReaderService;
    //新增
    @PostMapping
    public ResultVo addReader(@RequestBody SysReader reader){
        //查询学号username是否已经被占用
        QueryWrapper<SysReader> query = new QueryWrapper<>();
        query.lambda().eq(SysReader::getUsername,reader.getUsername());
        SysReader one = sysReaderService.getOne(query);
        if(one != null){
            return ResultUtils.error("This student ID is already occupied!");
        }
//        reader.setPassword(DigestUtils.md5DigestAsHex(reader.getPassword().getBytes()));//密码加密
//        reader.setPassword(reader.getPassword());
        reader.setCheckStatus("1");
        reader.setUserStatus("1");
        sysReaderService.saveReader(reader);
        return ResultUtils.success("Add reader successfully!");
    }
    //编辑
    @PutMapping
    public ResultVo editReader(@RequestBody SysReader reader){
        sysReaderService.editReader(reader);
        return ResultUtils.success("Edit reader successfully!");
    }
    //删除
    @DeleteMapping("/{readerId}")
    public ResultVo deleteReader(@PathVariable("readerId") Long readerId){
        int count_unreturn = sysReaderService.checkUnreturn(readerId);
        int count_unpaid = sysReaderService.checkUnpaid(readerId);
        if(count_unpaid !=0)
            return ResultUtils.error("Having a fine not paid!");
        if(count_unreturn !=0)
            return ResultUtils.error("Having a book not returned!");

        boolean remove = sysReaderService.removeById(readerId);
        if(remove){
            return ResultUtils.success("Delete reader successfully!");
        }
        return ResultUtils.error("Failed to delete reader!");
    }
    //列表
    @GetMapping("/list")
    public ResultVo getList(ReaderParm parm){
        IPage<SysReader> list = sysReaderService.getList(parm);
        return ResultUtils.success("search successfully!",list);
    }
    //根据学号查询读者信息
    @GetMapping("/getByUserName")
    public ResultVo getByUserName( SysReader reader){
        //构造查询条件
        QueryWrapper<SysReader> query = new QueryWrapper<>();
        query.lambda().eq(SysReader::getUsername,reader.getUsername());
        SysReader one = sysReaderService.getOne(query);
        if (one != null){
            return ResultUtils.success("Search successfully!",one);
        }else {
            return ResultUtils.error("This student ID is not existed!");
        }
    }

    //根据readerId查询读者信息
    @GetMapping("/getByReaderId/{readerId}")
    public ResultVo getByReaderId(@PathVariable("readerId") Long readerId){
        SysReader reader = sysReaderService.getReaderById(readerId);
        if(reader == null) return ResultUtils.error("Member not exists!",readerId);
        return ResultUtils.success("Search successfully!",reader);
    }
    //读者注册
    @PostMapping("/register")
    public ResultVo register(@RequestBody SysReader reader) {
        //查询学号是否已经被占用
        QueryWrapper<SysReader> query = new QueryWrapper<>();
        query.lambda().eq(SysReader::getUsername, reader.getUsername());
        SysReader one = sysReaderService.getOne(query);
        if (one != null) {
            return ResultUtils.error("The student number is occupied!");
        }
        reader.setPassword(DigestUtils.md5DigestAsHex(reader.getPassword().getBytes()));
        reader.setCheckStatus("0");  //未审核
        reader.setUserStatus("1");
        sysReaderService.saveReader(reader);
        return ResultUtils.success("registration success!");
    }
    //读者审核
    @PutMapping("/applyReader")
    public ResultVo applyReader(@RequestBody SysReader reader) {
        reader.setCheckStatus("1");
        sysReaderService.updateById(reader);
        return ResultUtils.success("audit successfully!");
    }

    //读者数量
    @GetMapping("/getMemberNum")
    public int getMemberNum()
    {
        return sysReaderService.checkMemberNum();
    }

    //上次借的书
    @GetMapping("/checkLastBorrow/{readerId}")
    public String checkLastBorrow(@PathVariable("readerId") Long readerId)
    {
        return sysReaderService.checkLastBorrow(readerId);
    }

    //上次还的书
    @GetMapping("/checkLastReturn/{readerId}")
    public String checkLastReturn(@PathVariable("readerId") Long readerId)
    {
        return sysReaderService.checkLastReturn(readerId);
    }

    //检查还书通知
    @GetMapping("/UnreturnNotice/{readerId}")
    public ResultVo UnreturnNotice(@PathVariable("readerId") Long readerId)
    {
        int unreturnNum = sysReaderService.checkUnreturn(readerId);
        if(unreturnNum == 1)
            return ResultUtils.error("You have 1 book to return!");
        else if(unreturnNum > 1)
            return ResultUtils.error("You have " + unreturnNum +" books to return!");
        else
            return ResultUtils.success("You have no book to return!");
    }

    //检查罚款通知
    @GetMapping("/UnpaidNotice/{readerId}")
    public ResultVo UnpaidNotice(@PathVariable("readerId") Long readerId)
    {
        int unpaidNum = sysReaderService.checkUnpaid(readerId);
        if(unpaidNum == 1)
            return ResultUtils.error("You have 1 fine to pay!");
        else if(unpaidNum > 1)
            return ResultUtils.error("You have " + unpaidNum +" fines to pay!");
        else
            return ResultUtils.success("You have no fine to pay!");
    }


    //该用户所有未还的罚款数量
    @GetMapping("/checkUnpaid/{readerId}")
    public int checkUnpaid(@PathVariable("readerId")Long readerId) {
        return sysReaderService.checkUnpaid(readerId);
    }

    //该用户所有的罚款数量
    @GetMapping("/checkAllFines/{readerId}")
    public int checkAllFines(@PathVariable("readerId")Long readerId) {
        return sysReaderService.checkAllFines(readerId);
    }

    //删除某年份的所有毕业生
    @DeleteMapping("/deleteGraduates/{entryYear}")
    public ResultVo deleteGraduates(@PathVariable("entryYear") Integer entryYear)
    {
        int count = sysReaderService.countUndeletableGraduates(entryYear);
        sysReaderService.deleteGraduates(entryYear);
        if(count > 0)
            return ResultUtils.error("Having " + count + " graduates cannot be deleted!");
        else
            return ResultUtils.success("Delete Successfully!");
    }


}
