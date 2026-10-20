package com.tlias.exception;

import lombok.Getter;

/**
 * 业务异常。
 * <p>
 * 用于表达可预期的业务失败（如登录失败），由 {@link GlobalExceptionHandler}
 * 统一转换为带有明确提示的响应，避免依赖空指针等底层异常来隐式表达失败。
 */
@Getter
public class BusinessException extends RuntimeException {

    private final String message;

    public BusinessException(String message) {
        super(message);
        this.message = message;
    }
}
