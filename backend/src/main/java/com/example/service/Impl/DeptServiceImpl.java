package com.example.service.Impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mapper.DeptMapper;
import com.example.pojo.Dept;
import com.example.service.DeptService;

@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptMapper deptMapper;
    
    
    @Override
    public List<Dept> findAll() {
        
        //调用DeptMapper实现类的函数
        return deptMapper.findAll();
        
    }


    @Override
    public void deleteById(Integer id) {
        deptMapper.deleteDeptById(id);
        
    }


    @Override
    public void add(Dept dept) {
        dept.setCreateTime(LocalDateTime.now());
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.addData(dept);
        
    }


    @Override
    public Dept getById(Integer id) {
        return deptMapper.getDeptById(id);
        
    }


    @Override
    public void modify(Dept dept) {
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.modifyDept(dept);
    }

    
    
}
