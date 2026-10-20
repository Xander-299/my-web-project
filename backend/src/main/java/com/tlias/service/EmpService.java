package com.tlias.service;



import java.util.List;

import com.tlias.pojo.Emp;
import com.tlias.pojo.EmpQueryParam;
import com.tlias.pojo.PageResult;

public interface EmpService {
    //调用Mapper
    //PageResult<Emp> queryPageResult(String name,Integer gender,LocalDate begin,LocalDate end,Integer page,Integer pageSize);
    
    PageResult<Emp> queryPageResult(EmpQueryParam empQueryParam);

    void addEmpinfo(Emp empInfo);

    void deleteEmpinfo(List<Integer> ids);

    void modifyEmpinfo(Emp empInfo);

    Emp queryEmpinfoById(Integer id);

    List<Emp> queryAllEmpinfo();
} 
