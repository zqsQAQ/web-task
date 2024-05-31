package com.itmk.web.sys_reader.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itmk.exception.BusinessException;
import com.itmk.utils.ResultUtils;
import com.itmk.web.sys_reader.service.SysReaderService;
import com.itmk.web.sys_reader.entity.ReaderParm;
import com.itmk.web.sys_reader.entity.SysReader;
import com.itmk.web.sys_reader.mapper.SysReaderMapper;
import com.itmk.web.sys_reader_role.entity.ReaderRole;
import com.itmk.web.sys_reader_role.service.ReaderRoleService;
import com.itmk.web.sys_role.entity.SysRole;
import com.itmk.web.sys_role.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SysReaderServiceImpl extends ServiceImpl<SysReaderMapper, SysReader> implements SysReaderService {
    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private ReaderRoleService readerRoleService;

    @Override
    public IPage<SysReader> getList(ReaderParm parm) {
        //构造查询条件
        QueryWrapper<SysReader> query = new QueryWrapper<>();
        if(!StringUtils.isEmpty(parm.getIdCard())){
            query.lambda().like(SysReader::getIdCard,parm.getIdCard());
        }
        if(!StringUtils.isEmpty(parm.getLearnNum())){
            query.lambda().like(SysReader::getLearnNum,parm.getLearnNum());
        }
        if(!StringUtils.isEmpty(parm.getPhone())){
            query.lambda().like(SysReader::getPhone,parm.getPhone());
        }
        if(!StringUtils.isEmpty(parm.getUsername())){
            query.lambda().like(SysReader::getUsername,parm.getUsername());
        }
        if(parm.getEntryYear() != null){
            query.lambda().like(SysReader::getEntryYear,parm.getEntryYear());
        }
        //构造分页对象
        IPage<SysReader> page = new Page<>();
        page.setCurrent(parm.getCurrentPage());
        page.setSize(parm.getPageSize());

        return this.baseMapper.selectPage(page, query);
    }

    @Override
    @Transactional
    public void saveReader(SysReader sysReader) {
        QueryWrapper<SysRole> query = new QueryWrapper<>();
        query.lambda().eq(SysRole::getRoleType,"2");
        SysRole one = sysRoleService.getOne(query);
        if(one == null){
            throw new BusinessException(500,"please new reader role, then new reader!");
        }
        //新增读者
        this.baseMapper.insert(sysReader);
        //设置读者角色
        ReaderRole readerRole = new ReaderRole();
        readerRole.setReaderId(sysReader.getReaderId());
        readerRole.setRoleId(one.getRoleId());
        readerRoleService.save(readerRole);
    }

    @Override
    @Transactional
    public void editReader(SysReader sysReader) {
        QueryWrapper<SysRole> query = new QueryWrapper<>();
        query.lambda().eq(SysRole::getRoleType,"2");
        SysRole one = sysRoleService.getOne(query);
        //编辑读者
        this.baseMapper.updateById(sysReader);
        //设置读者  先删除再设置
        QueryWrapper<ReaderRole> readerRole = new QueryWrapper<>();
        readerRole.lambda().eq(ReaderRole::getReaderId,sysReader.getReaderId());
            //先删除
        readerRoleService.remove(readerRole);
            //设置读者角色
        ReaderRole saveReaderRole = new ReaderRole();
        saveReaderRole.setReaderId(sysReader.getReaderId());
        saveReaderRole.setRoleId(one.getRoleId());
        readerRoleService.save(saveReaderRole);

    }

    @Override
    public SysReader loadByUsername(String username) {
        QueryWrapper<SysReader> query = new QueryWrapper<>();
        query.lambda().eq(SysReader::getUsername,username);
        return this.baseMapper.selectOne(query);
    }

    @Override
    public int checkUnreturn(Long readerId) {
        return this.baseMapper.checkUnreturn(readerId);
    }

    @Override
    public int checkUnpaid(Long readerId) {
        return this.baseMapper.checkUnpaid(readerId);
    }

    @Override
    public int checkAllFines(Long readerId) {
        return this.baseMapper.checkAllFines(readerId);
    }

    @Override
    public int checkMemberNum() {
        return this.baseMapper.checkMemberNum();
    }

    @Override
    public String checkLastBorrow(Long readerId) {
        return this.baseMapper.checkLastBorrow(readerId);
    }

    @Override
    public String checkLastReturn(Long readerId) {
        return this.baseMapper.checkLastReturn(readerId);
    }

    @Override
    public int countUndeletableGraduates(Integer entryYear) {
        return this.baseMapper.countUndeletableGraduates(entryYear);
    }

    @Override
    public int deleteGraduates(Integer entryYear) {
        return this.baseMapper.deleteGraduates(entryYear);
    }

    @Override
    public SysReader getReaderById(Long readerId) {
        return this.baseMapper.getReaderById(readerId);
    }
}
