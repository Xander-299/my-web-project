package com.example.service.Impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mapper.ClazzsMapper;
import com.example.pojo.Clazz;
import com.example.pojo.ClazzsQueryParam;
import com.example.pojo.PageResult;
import com.example.service.ClazzsService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;


@Service
public class ClazzsServiceImpl implements ClazzsService{
    @Autowired
    private ClazzsMapper clazzsMapper;

    @Override
    public Clazz queryClassById(Integer id) {
        
        return clazzsMapper.queryClassById(id);
    }

    @Override
    public List<Clazz> queryAllClass() {
        
        return clazzsMapper.queryAllClass();
    }

    

    @Override
    public void modifyClass(Clazz clazz) {
        clazz.setUpdateTime(LocalDateTime.now());
        clazzsMapper.modifyClass(clazz);
        
    }

    @Override
    public void addClass(Clazz clazz) {
        clazz.setCreateTime(LocalDateTime.now());
        clazz.setUpdateTime(LocalDateTime.now());
        clazzsMapper.addClass(clazz);
        
    }

    @Override
    public void deleteClassById(Integer id) {

        clazzsMapper.deleteClassById(id);
        
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageResult<Clazz> queryPageResult(ClazzsQueryParam clazzsQueryParam) {
        Page<Clazz> p = PageHelper.startPage(clazzsQueryParam.getPage(),clazzsQueryParam.getPageSize());

        List<Clazz> clazzList = clazzsMapper.queryPageResult(clazzsQueryParam);
        LocalDate today = LocalDate.now();
        for (Clazz clazz : clazzList) {
            //结课时间
            LocalDate endDate = clazz.getEndDate();
            //开课时间
            LocalDate beginDate = clazz.getBeginDate();
            if(endDate.isBefore(today)){
                clazz.setStatus("已结课");
            }else if(beginDate.isBefore(today)){
                clazz.setStatus("已开班");
            }else if(beginDate.isAfter(today)){
                clazz.setStatus("未开班");
            }
        }
        return new PageResult<Clazz>(p.getTotal(), clazzList);
        
        
        
    }

}
