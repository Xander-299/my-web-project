package com.tlias.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tlias.anno.Log;
import com.tlias.pojo.Clazz;
import com.tlias.pojo.ClazzsQueryParam;
import com.tlias.pojo.PageResult;
import com.tlias.pojo.Result;
import com.tlias.service.ClazzsService;

import lombok.extern.slf4j.Slf4j;




@Slf4j
@RestController
@RequestMapping("/clazzs")
public class ClazzsController {
    @Autowired
    private ClazzsService clazzsService;

    //分页查询
    @GetMapping
    public Result queryPagination(ClazzsQueryParam clazzsQueryParam){
        log.info("请求参数: {}",clazzsQueryParam);
        PageResult<Clazz> pageResult = clazzsService.queryPageResult(clazzsQueryParam);
        
        return Result.success(pageResult);
    }

    //删除班级
    @DeleteMapping("/{id}")
    @Log
    public Result deleteClassById(@PathVariable Integer id){
        log.info("删除的班级id: {}",id);
        clazzsService.deleteClassById(id);
        return Result.success();
    }
    
    //查询全部班级
    @GetMapping("/list")
    public Result queryAllClass() {
        
        List<Clazz> classList = clazzsService.queryAllClass();
        
        return Result.success(classList);
    }

    //添加班级
    @PostMapping
    @Log
    public Result addClass(@RequestBody Clazz clazz){
        log.info("添加班级: {}",clazz);
        clazzsService.addClass(clazz);
        return Result.success();
    }
    

    //根据Id查询
    @GetMapping("/{id}")
    public Result queryClassById(@PathVariable Integer id) {
        log.info("根据id: {}查询",id);
        Clazz clazz = clazzsService.queryClassById(id);
        return Result.success(clazz);
    }

    //修改班级
    @PutMapping
    @Log
    public Result modifyClass(@RequestBody Clazz clazz){
        log.info("传入的参数: {}",clazz);
        clazzsService.modifyClass(clazz);
        return Result.success();
    }
    
}
