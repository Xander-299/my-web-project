package com.tlias.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.tlias.pojo.EmpExpr;
/*
员工工作经历
*/
@Mapper
public interface EmpExprMapper {
    void addEmpExpr(List<EmpExpr> empExprs);

    void deleteEmpExprinfos(List<Integer> empIds);

    

    
}
