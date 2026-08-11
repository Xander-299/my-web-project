package com.example.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.pojo.Emp;



@Mapper
public interface LoginMapper {
    Emp loginSystem(Emp emp);
}
