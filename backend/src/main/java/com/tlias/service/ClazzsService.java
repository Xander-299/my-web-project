package com.tlias.service;

import java.util.List;

import com.tlias.pojo.Clazz;
import com.tlias.pojo.ClazzsQueryParam;
import com.tlias.pojo.PageResult;

public interface ClazzsService {

    PageResult<Clazz> queryPageResult(ClazzsQueryParam clazzsQueryParam);

    void deleteClassById(Integer id);

    List<Clazz> queryAllClass();

    void addClass(Clazz clazz);

    Clazz queryClassById(Integer id);

    void modifyClass(Clazz clazz);

    
} 
