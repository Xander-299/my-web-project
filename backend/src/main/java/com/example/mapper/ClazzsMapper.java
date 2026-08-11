package com.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.pojo.Clazz;
import com.example.pojo.ClazzsQueryParam;



@Mapper
public interface ClazzsMapper {
    List<Clazz> queryPageResult(ClazzsQueryParam clazzsQueryParam);


    void deleteClassById(Integer id);

    List<Clazz> queryAllClass();


    void addClass(Clazz clazz);

    Clazz queryClassById(Integer id);


    void modifyClass(Clazz clazz);
    
} 
