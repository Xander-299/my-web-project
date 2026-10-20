package com.tlias.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tlias.mapper.OperateLogMapper;
import com.tlias.pojo.OperateLog;
import com.tlias.utils.CurrentHolder;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
public class OperateLogAspect {

    @Autowired
    private OperateLogMapper operateLogMapper;

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Around("@annotation(com.tlias.anno.Log)")
    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable {
        //log.info("AOP启动了");
        // 1. 记录开始时间
        long start = System.currentTimeMillis();

        // 2. 执行目标方法
        Object result = joinPoint.proceed();

        // 3. 记录结束时间
        long end = System.currentTimeMillis();

        // 4. 封装日志对象
        OperateLog olog = new OperateLog();

        // 操作人ID（实际项目一般从JWT或ThreadLocal获取）
        olog.setOperateEmpId(CurrentHolder.getCurrentId());

        // 操作时间
        olog.setOperateTime(LocalDateTime.now());

        // 类名
        String className = joinPoint.getTarget().getClass().getName();
        olog.setClassName(className);

        // 方法名
        String methodName = joinPoint.getSignature().getName();
        olog.setMethodName(methodName);

        // 方法参数
        Object[] args = joinPoint.getArgs();
        String params = objectMapper.writeValueAsString(args);
        olog.setMethodParams(params);

        // 返回值
        String returnValue = objectMapper.writeValueAsString(result);
        olog.setReturnValue(returnValue);

        // 执行时间
        olog.setCostTime(end - start);


        //输出日志
        log.info("操作日志: {}",olog);

        // 5. 保存日志
        operateLogMapper.insert(olog);

        return result;
    }
}