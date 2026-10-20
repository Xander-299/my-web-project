package com.tlias.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;





@Mapper
public interface ReportMapper {

    
    List<Map<String, Object>> getEmpGenderData();

	List<Map<String, Object>> getEmpJobData();

    List<Map<String, Object>> getStudentDegreeData();

    List<Map<String, Object>> getStudentCountData();
    
}
