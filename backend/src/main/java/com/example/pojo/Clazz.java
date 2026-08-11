package com.example.pojo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Clazz {
    private Integer id;//id
    private String name;//班级名
    private String room;//班级教室
    private LocalDate beginDate;//开课时间
    private LocalDate endDate;//结课时间
    private Integer masterId;//班主任id
    private Integer subject;//学科
    private LocalDateTime createTime;//创建时间
    private LocalDateTime updateTime;//修改时间
    private String masterName;//班主任名字
    private String status;//状态


}
