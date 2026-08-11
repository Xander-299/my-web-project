package com.example.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pojo.Result;
import com.example.pojo.StudentStat;
import com.example.pojo.JobOption;

import com.example.service.ReportService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private ReportService reportService;

    //员工性别统计
    @GetMapping("/empGenderData")
    public Result getEmpGenderData(){
        List<Map<String,Object>> genderStatList = reportService.getEmpGenderData();
        log.info("得到的list列表: {}",genderStatList);
        return Result.success(genderStatList);
    }

    //员工职位统计
    @GetMapping("/empJobData")
    public Result getEmpJobData(){
        JobOption<String,Long> jobOption = reportService.getEmpJobData();
        log.info("获取到的数据:{}",jobOption);
        return Result.success(jobOption);
    }

    //学生学历统计
    @GetMapping("/studentDegreeData")
    public Result getStudentDegreeData(){
        List<Map<String,Object>> degreeStatList = reportService.getStudentDegreeData();
        log.info("获取到的数据: {}",degreeStatList);
        return Result.success(degreeStatList);
    }

    //班级人数统计
    @GetMapping("/studentCountData")
    public Result getStudentCountData(){
        StudentStat<String,Long> studentStat = reportService.getStudentCountData();
        log.info("获取到的数据: {}",studentStat);
        return Result.success(studentStat);
    }
}
