package com.tlias.exception;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tlias.pojo.Result;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常：可预期的失败，直接把提示信息返回给前端。
     */
    @ExceptionHandler(BusinessException.class)
    public Result handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());
        return Result.error(e.getMessage());
    }

    /**
     * 唯一约束冲突：翻译成「xxx已存在」这类可读提示。
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public Result handleDuplicateException(DuplicateKeyException e) {
        log.error("唯一约束冲突", e);
        String causeMsg = e.getMessage();
        int i = causeMsg.indexOf("Duplicate entry");
        if (i < 0) {
            return Result.error("数据已存在");
        }
        String errorMsg = causeMsg.substring(i);
        String[] parts = errorMsg.split(" ");
        if (parts.length < 3) {
            return Result.error("数据已存在");
        }
        return Result.error(parts[2] + "已经存在");
    }

    /**
     * 兜底异常：记录完整堆栈，但不把内部细节暴露给前端。
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        log.error("程序出错啦~", e);
        return Result.error("出错了,请联系管理员~");
    }
}
