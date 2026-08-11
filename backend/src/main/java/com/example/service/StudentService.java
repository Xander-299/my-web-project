package com.example.service;

import java.util.List;

import com.example.pojo.PageResult;
import com.example.pojo.Student;
import com.example.pojo.StudentQueryParam;

public interface StudentService {

    PageResult<Student> queryPagination(StudentQueryParam studentQueryParam);

    void deleteStudents(List<Integer> ids);

    void addStudent(Student student);

    Student queryStudentById(Integer id);

    void modifyStudent(Student student);

    void breachHandling(Integer id, Integer score);

}
