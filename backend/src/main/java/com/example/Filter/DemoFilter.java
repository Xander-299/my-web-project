package com.example.Filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import lombok.extern.slf4j.Slf4j;
@Slf4j
  // 拦截所有请求
public class DemoFilter implements Filter{

    // 销毁方法，只会在web服务器关闭时执行一次
    @Override
    public void destroy() {
        
        
        log.info("DemoFilter销毁");
    }
    //拦截到请求后，会执行多次
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
            
        log.info("拦截到了请求");
        //获取请求头
        
        //放行
        chain.doFilter(request, response);
        
    }

    // web初始化方法，只会在web服务器启动时执行一次
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        
        
        log.info("DemoFilter初始化了");
    }

}
