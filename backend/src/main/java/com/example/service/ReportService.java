package com.example.service;

import java.util.List;
import java.util.Map;

import com.example.pojo.JobOption;

import com.example.pojo.StudentStat;



public interface ReportService {

    List<Map<String,Object>> getEmpGenderData();

    JobOption<String,Long> getEmpJobData();

    List<Map<String, Object>> getStudentDegreeData();

    StudentStat<String, Long> getStudentCountData();

    

}
