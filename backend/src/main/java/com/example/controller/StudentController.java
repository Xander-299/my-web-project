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
import org.springframework.web.bind.annotation.RestController;

import com.example.anno.Log;
import com.example.pojo.PageResult;
import com.example.pojo.Result;
import com.example.pojo.Student;
import com.example.pojo.StudentQueryParam;
import com.example.service.StudentService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;
    
    //学员列表分页查询
    @GetMapping
    public Result queryPagination(StudentQueryParam studentQueryParam){
        log.info("请求参数: {}",studentQueryParam);
        PageResult<Student> pageResult = studentService.queryPagination(studentQueryParam);
        return Result.success(pageResult);
    }

    //根据id列表删除学员
    @DeleteMapping("/{ids}")
    @Log
    public Result deleteStudents(@PathVariable List<Integer> ids){
        log.info("需要删除的学生id列表: {}",ids);
        studentService.deleteStudents(ids);
        return Result.success();
    }

    //添加学员
    @PostMapping
    @Log
    public Result addStudent(@RequestBody Student student){
        log.info("请求体: {}",student);
        studentService.addStudent(student);
        return Result.success();
    }

    //根据ID查询
    @GetMapping("/{id}")
    public Result queryStudentById(@PathVariable Integer id){
        log.info("查询id为{}的学生",id);
        Student studentInfo = studentService.queryStudentById(id);
        return Result.success(studentInfo);
    }


    //修改学员
    @PutMapping
    @Log
    public Result modifyStudent(@RequestBody Student student){
        log.info("请求体:{}",student);
        studentService.modifyStudent(student);
        return Result.success();
    }

    //违纪处理
    @PutMapping("/violation/{id}/{score}")
    @Log
    public Result breachHandling(@PathVariable Integer id,@PathVariable Integer score){
        log.info("违纪id为: {}, 违纪分数为: {}",id,score);
        studentService.breachHandling(id,score);
        return Result.success();
    }
}
