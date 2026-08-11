package com.example.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.anno.Log;
import com.example.pojo.Emp;

import com.example.pojo.EmpQueryParam;
import com.example.pojo.PageResult;
import com.example.pojo.Result;
import com.example.service.EmpService;


import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/emps")
public class EmpController {
    @Autowired
    private EmpService empService;

 /*    @GetMapping
    public Result queryTotalEmps(
        @RequestParam(defaultValue = "") String name,
        @RequestParam(defaultValue = "") Integer gender,
        @RequestParam(defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
        @RequestParam(defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end,
        @RequestParam(defaultValue = "1")Integer page,
        @RequestParam(defaultValue = "10") Integer pageSize){
        log.info("分页查询:{},{},{},{},{},{}",page,pageSize,name,gender,begin,end);
        PageResult<Emp> pageResult = empService.queryPageResult(name,gender,begin,end,page,pageSize);
        return Result.success(pageResult);
    }
    */

    //分页查询
    @GetMapping
    public Result queryPagination(EmpQueryParam empQueryParam){
        log.info("分页查询:{}",empQueryParam);
        PageResult<Emp> pageResult = empService.queryPageResult(empQueryParam);
        return Result.success(pageResult);

    }

    //添加员工信息
    @PostMapping
    @Log
    public Result addEmpinfo(@RequestBody Emp empInfo){
        log.info("新增员工: {}",empInfo);
        empService.addEmpinfo(empInfo);
        return Result.success();
    }

    //根据id删除员工信息
    @DeleteMapping
    //如果要用List接数据，就得加@RequestParam,否则只能用Integer[]接
    @Log
    public Result deleteEmpinfo(@RequestParam List<Integer> ids){
        log.info("删除员工id为: {}",ids);
        empService.deleteEmpinfo(ids);
        return Result.success();
    }

    //根据id查询员工信息
    @GetMapping("/{id}")
    public Result getEmpinfoById(@PathVariable Integer id) {
        log.info("根据id: {}查询员工信息",id);
        Emp emp = empService.queryEmpinfoById(id);
        
        return Result.success(emp);

    }
    
    //根据员工id删除工作经历,再添加员工经历,再修改员工个人信息
     @PutMapping
     @Log
    public Result modifyEmpinfo(@RequestBody Emp empInfo){
        log.info("修改员工: {}",empInfo);
        empService.modifyEmpinfo(empInfo);
        return Result.success();
    }
    
    //查询所有员工信息
    @GetMapping("/list")
    public Result queryAllEmpinfo(){
        List<Emp> empList = empService.queryAllEmpinfo();
        return Result.success(empList);
    }
    
        
}
