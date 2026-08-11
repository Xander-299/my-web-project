package com.example.exception;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.pojo.Result;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public Result handleException(Exception e){
        log.error("程序出错啦~",e);
        return Result.error("出错了,请联系管理员~");
    }

    @ExceptionHandler
    public Result handleDuplicateException(DuplicateKeyException e){
        log.error("程序出错啦~",e);
        String causeMsg = e.getMessage();
        int i = causeMsg.indexOf("Duplicate entry");
        String errorMsg = causeMsg.substring(i);
        String needMsg = errorMsg.split(" ")[2];
        return Result.error(needMsg + "已经存在");
    }
    @ExceptionHandler
    public Result handleNullPointerException(NullPointerException e){
        log.error("程序出错啦",e);
        return Result.error("用户名或密码错误！！");
    }
}
