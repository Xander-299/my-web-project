package com.example.pojo;

import lombok.Data;

@Data
public class StudentQueryParam {
    private String name;//姓名
    private Integer degree;//学历
    private Integer clazzId;//班级ID
    private Integer page = 1;//页码
    private Integer pageSize = 10;//每页记录数
}
