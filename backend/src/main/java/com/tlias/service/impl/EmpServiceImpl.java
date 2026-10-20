package com.tlias.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.tlias.mapper.EmpExprMapper;
import com.tlias.mapper.EmpMapper;
import com.tlias.pojo.Emp;
import com.tlias.pojo.EmpExpr;

import com.tlias.pojo.EmpQueryParam;
import com.tlias.pojo.PageResult;
import com.tlias.service.EmpService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class EmpServiceImpl implements EmpService  {
    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private EmpExprMapper empExprMapper;

    
    @Override
    public PageResult<Emp> queryPageResult(EmpQueryParam empQueryParam) {
        PageHelper.startPage(empQueryParam.getPage(),empQueryParam.getPageSize());
        List<Emp> empList = empMapper.queryEveryPageEmps(empQueryParam);
        try (Page<Emp> p = (Page<Emp>) empList) {
            return new PageResult<>(p.getTotal(),p.getResult());
        }
    }

    
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void addEmpinfo(Emp empInfo) {
        Emp emp = empInfo;
        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());
        //先插父表
        empMapper.addEmpinfo(emp);


        List<EmpExpr> empExprs = emp.getExprList();
        if (!CollectionUtils.isEmpty(empExprs)) {
            //如果直接使用isEmpty,如果empExprs = null,即是empExprs.size() = 0, 则会报NullPointerException
            //如果工作经历不为空。则遍历List添加员工id
            Integer id = emp.getId();
            for (EmpExpr empExpr : empExprs) {
                empExpr.setEmpId(id);
            }
            //throw new RuntimeException("模拟插入经历失败");
            //再插子表
            empExprMapper.addEmpExpr(empExprs);
        }
        
    }

    
    @Override
    public List<Emp> queryAllEmpinfo() {
        
        return empMapper.queryAllEmpinfo();
    }


    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Emp queryEmpinfoById(Integer id) {
        return empMapper.queryEmpinfoById(id);
    }

    
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void modifyEmpinfo(Emp empInfo) {
        //先修改子表
        List<EmpExpr>empExprs = empInfo.getExprList();
        Integer empId = empInfo.getId();
        List<Integer> idList = List.of(empId);
        if (!CollectionUtils.isEmpty(empExprs)) {
            
            //根据员工id删除
            empExprMapper.deleteEmpExprinfos(idList);
            //根据工作经历列表添加数据
            for (EmpExpr empExpr : empExprs) {
                empExpr.setEmpId(empId);
            }
            empExprMapper.addEmpExpr(empExprs);
        }
        empInfo.setUpdateTime(LocalDateTime.now());
        //再修改父表
        empMapper.modifyEmpinfo(empInfo);
    }
    //需要多次修改数据库,添加@Transactional事务管理,@Transactional默认只对RuntimeException和Error回滚,
    // rollbackFor = {Exception.class}表示所有异常都回滚
    //没有@Transactional默认自动提交


    
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void deleteEmpinfo(List<Integer> ids) {
        if (!CollectionUtils.isEmpty(ids)) {
            //先删子表
            empExprMapper.deleteEmpExprinfos(ids);
            //再删父表
            empMapper.deleteEmpinfos(ids);
            
        }
        
        
    }


    //-------------原始的分页查询------------------
    /* 
    @Override
    public PageResult<Emp> queryPageResult(Integer currentPage,Integer pageSize) {
        
        Long total = empMapper.queryTotalPage();
        Integer start = (currentPage - 1) * pageSize;
        List<Emp> emps = empMapper.queryEveryPageEmps(start,pageSize);
        PageResult<Emp> pageResult = new PageResult<>(total,emps);
        
        return pageResult;
    }
    */
    /*
    @Override
    public PageResult<Emp> queryPageResult(Integer currentPage,Integer pageSize){
        //1.设置分页参数
        PageHelper.startPage(currentPage,pageSize);
        //2.执行查询
        List<Emp> empList = empMapper.queryEveryPageEmps();
        Page<Emp> p = (Page<Emp>) empList;
        //3.解析查询结果，封装数据
        return new PageResult<Emp>(p.getTotal(), p.getResult());
    }
    */
    /* 
    @Override
    public PageResult<Emp> queryPageResult(String name,Integer gender,LocalDate begin,LocalDate end,Integer page,Integer pageSize) {
        //1.设置分页参数
        PageHelper.startPage(page,pageSize);
        //2.执行查询
        List<Emp> empList = empMapper.queryEveryPageEmps(name,gender,begin,end);
        Page<Emp> pageResult = (Page<Emp>) empList;
        //3.解析查询结果，封装数据
        return new PageResult<Emp>(pageResult.getTotal(), pageResult.getResult());
    }
    */
   
}
