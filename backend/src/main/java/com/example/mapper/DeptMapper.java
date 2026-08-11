package com.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.pojo.Dept;
/*
部门信息
*/
@Mapper
public interface DeptMapper {
    
    @Select("SELECT id,name,create_time,update_time FROM dept ORDER BY update_time DESC;")
    List<Dept> findAll();

    @Delete("DELETE FROM dept where id = #{id}")
    void deleteDeptById(Integer id);

    @Insert("INSERT INTO dept (id,name,create_time,update_time) VALUES (#{id},#{name},#{createTime},#{updateTime})")
    void addData(Dept dept);
    @Select("SELECT * FROM dept where id = #{id}")
    Dept getDeptById(Integer id);
    @Update("UPDATE dept SET name = #{name}, update_time = #{updateTime} where id = #{id}")
    void modifyDept(Dept dept);
}
