package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pojo.OperateLog;
import com.example.pojo.PageResult;
import com.example.pojo.Result;
import com.example.service.OperateLogService;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/log")
public class OperateLogController {
    @Autowired
    private OperateLogService operateLogService;
    

    @GetMapping("/page")
    public Result getLogResult(
        @RequestParam(defaultValue = "1") Integer page,
        @RequestParam(defaultValue = "10") Integer pageSize
    ){
        PageResult<OperateLog> pageResult = operateLogService.getLogResult(page,pageSize);
        return Result.success(pageResult);
    }
}
