package com.example.service.Impl;



import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mapper.ReportMapper;
import com.example.pojo.JobOption;

import com.example.pojo.StudentStat;
import com.example.service.ReportService;


@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportMapper reportMapper;

    @Override
    public List<Map<String,Object>> getEmpGenderData() {
        

        return reportMapper.getEmpGenderData();
        
    }

    @Override
    public List<Map<String, Object>> getStudentDegreeData() {
        
        return reportMapper.getStudentDegreeData();
    }

    @Override
    public StudentStat<String, Long> getStudentCountData() {
        List<Map<String,Object>> studentCountList = reportMapper.getStudentCountData();

        List<String> clazzList = studentCountList.stream().map((hashMap)->(String)hashMap.get("name")).toList();
        List<Long> dataList = studentCountList.stream().map((hashMap)->(Long)hashMap.get("population")).toList();
        return new StudentStat<>(clazzList, dataList);
    }

    @Override
    public JobOption<String,Long> getEmpJobData() {
        List<Map<String,Object>> list = reportMapper.getEmpJobData();
        //封装
        List<String> jobList = list.stream().map((hashMap)->(String)hashMap.get("pos")).toList();
        List<Long> dataList = list.stream().map((hashmap)->(Long)hashmap.get("num")).toList();

        return new JobOption<String,Long>(jobList, dataList);
    }

    
}
