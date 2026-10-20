package com.tlias.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tlias.pojo.Emp;
import com.tlias.pojo.LoginInfo;
import com.tlias.pojo.Result;
import com.tlias.service.LoginService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private LoginService loginService;
    
    @PostMapping
    public Result loginSystem(@RequestBody Emp emp){
        LoginInfo loginInfo = loginService.loginSystem(emp);
        log.info("响应数据: {}",loginInfo);
        return Result.success(loginInfo);
        
    }
}
