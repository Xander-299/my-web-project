package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.interceptor.TokenInterceptor;

@Configuration

public class WebConfig implements WebMvcConfigurer {
    @Autowired
    // private DemoInterceptor demoInerceptor;
    private TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor).addPathPatterns("/**").excludePathPatterns("/login"); // 拦截除了/login以外的所有请求
    }

}
