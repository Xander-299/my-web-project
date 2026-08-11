package com.example.service;

import java.util.List;

import com.example.pojo.Clazz;
import com.example.pojo.ClazzsQueryParam;
import com.example.pojo.PageResult;

public interface ClazzsService {

    PageResult<Clazz> queryPageResult(ClazzsQueryParam clazzsQueryParam);

    void deleteClassById(Integer id);

    List<Clazz> queryAllClass();

    void addClass(Clazz clazz);

    Clazz queryClassById(Integer id);

    void modifyClass(Clazz clazz);

    
} 
