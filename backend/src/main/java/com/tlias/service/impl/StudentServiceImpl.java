package com.tlias.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tlias.mapper.StudentMapper;
import com.tlias.pojo.PageResult;
import com.tlias.pojo.Student;
import com.tlias.pojo.StudentQueryParam;
import com.tlias.service.StudentService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public PageResult<Student> queryPagination(StudentQueryParam studentQueryParam) {
        Page<Student> p = PageHelper.startPage(studentQueryParam.getPage(),studentQueryParam.getPageSize());
        List<Student> studentList = studentMapper.queryPagination(studentQueryParam);
        return new PageResult<Student>(p.getTotal(), studentList);
    }

    @Override
    public void deleteStudents(List<Integer> ids) {
        studentMapper.deleteStudents(ids);
        
    }

    @Override
    public void addStudent(Student student) {
        student.setCreateTime(LocalDateTime.now());
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.addStudent(student);
        
    }

    @Override
    public Student queryStudentById(Integer id) {
        
        return studentMapper.queryStudentByid(id);
    }

    @Override
    public void modifyStudent(Student student) {
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.modifyStudent(student);
        
    }

    @Override
    public void breachHandling(Integer id, Integer score) {
        studentMapper.breachHandling(id,score);
        
    }

    
}
