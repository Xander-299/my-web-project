package com.tlias.pojo;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
@Data
public class ClazzsQueryParam {
    private String name; //姓名
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate begin; //开始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end; //结束时间
    private Integer page = 1; //当前页码
    private Integer pageSize = 10;//每页记录数

}
