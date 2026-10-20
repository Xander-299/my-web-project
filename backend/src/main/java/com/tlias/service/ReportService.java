package com.tlias.service;

import java.util.List;
import java.util.Map;

import com.tlias.pojo.JobOption;

import com.tlias.pojo.StudentStat;



public interface ReportService {

    List<Map<String,Object>> getEmpGenderData();

    JobOption<String,Long> getEmpJobData();

    List<Map<String, Object>> getStudentDegreeData();

    StudentStat<String, Long> getStudentCountData();

    

}
