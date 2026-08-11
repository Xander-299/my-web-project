package com.example.interceptor;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class DemoInterceptor implements HandlerInterceptor{
    //在视图渲染之后运行
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            @Nullable Exception ex) throws Exception {
        log.info("afterCompletion---");
        
    }
    //目标资源运行之后运行
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            @Nullable ModelAndView modelAndView) throws Exception {
        log.info("postHandle--");
    }

    //目标资源运行前运行, 返回值为true则放行, 反之不放行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.info("preHandle--");
        return true;
    }

}
