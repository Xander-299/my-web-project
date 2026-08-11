package com.example.pojo;

import java.time.LocalDate;
import java.time.LocalDateTime;



import lombok.Data;
@Data
public class Student {
    private Integer id; //id
    private String name; //学生姓名
    private String no;//学号
    private Integer gender;//性别
    private String phone; //电话
    private String idCard; //身份证号
    private Integer isCollege; //是否来自院校
    private String address;//联系地址
    private Integer degree;//最高学历
    private LocalDate graduationDate;//毕业时间
    private Integer clazzId;//关联班级id
    private Integer violationCount;//违纪次数
    private Integer violationScore;//违纪扣分
    private LocalDateTime createTime;//创建时间
    private LocalDateTime updateTime;//修改时间

    private String clazzName;//班级名
    
}
