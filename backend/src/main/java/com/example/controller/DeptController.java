package com.example.controller;

import java.util.List;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.anno.Log;
import com.example.pojo.Dept;
import com.example.pojo.Result;
import com.example.service.DeptService;

import lombok.extern.slf4j.Slf4j;





@Slf4j
@RestController
@RequestMapping("/depts")
public class DeptController {
    @Autowired
    private DeptService deptService;
    
    //private static final Logger log = LoggerFactory.getLogger(DeptController.class);
    @GetMapping
    public Result list(){

        //System.out.println("查询全部部门数据");
        log.info("查询全部部门数据");
        List<Dept> depts = deptService.findAll();
        return Result.success(depts);
    }

    @DeleteMapping
    @Log
    public Result deleteDept(Integer id){
        
        //System.out.println("根据id删除数据："+ id);
        log.info("根据id删除数据: {}",id);
        deptService.deleteById(id);
        return Result.success();
    }

    @PostMapping
    @Log
    public Result addDept(@RequestBody Dept dept){
        //System.out.println(("新增部门:" + dept));
        log.info("新增部门数据: id={}, name={}",dept.getId(),dept.getName());
        deptService.add(dept);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id){
        //System.out.println("根据ID查询部门：" + id);
        log.info("根据ID查询部门: {}",id);
        Dept dept = deptService.getById(id);
        return Result.success(dept);
    }
    @PutMapping
    @Log
    public Result updateDept(@RequestBody Dept dept){
        log.info("将ID为{}的部门的部门名修改为: {}",dept.getId(),dept.getName());
        deptService.modify(dept);
        return Result.success();
    }
    
    
}
