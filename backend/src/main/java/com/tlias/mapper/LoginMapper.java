package com.tlias.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.tlias.pojo.Emp;



@Mapper
public interface LoginMapper {
    Emp loginSystem(Emp emp);
}
