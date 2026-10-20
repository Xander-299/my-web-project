package com.tlias.service;

import java.util.List;

import com.tlias.pojo.PageResult;
import com.tlias.pojo.Student;
import com.tlias.pojo.StudentQueryParam;

public interface StudentService {

    PageResult<Student> queryPagination(StudentQueryParam studentQueryParam);

    void deleteStudents(List<Integer> ids);

    void addStudent(Student student);

    Student queryStudentById(Integer id);

    void modifyStudent(Student student);

    void breachHandling(Integer id, Integer score);

}
