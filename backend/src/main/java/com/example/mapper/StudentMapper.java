package com.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.pojo.Student;
import com.example.pojo.StudentQueryParam;
@Mapper
public interface StudentMapper {

    List<Student> queryPagination(StudentQueryParam studentQueryParam);

    void deleteStudents(List<Integer> ids);

    void addStudent(Student student);

    Student queryStudentByid(Integer id);

    void modifyStudent(Student student);

    void breachHandling(Integer id, Integer score);

}
