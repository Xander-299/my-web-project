package com.tlias.service;

import java.util.List;

import com.tlias.pojo.Dept;

public interface DeptService {
    //查询操作
    public List<Dept> findAll();
    //删除操作
    public void deleteById(Integer id);

    //添加操作
    public void add(Dept dept);

    //查询单个数据操作
    public Dept getById(Integer id);

    //修改单个数据操作
    public void modify(Dept dept);
}
