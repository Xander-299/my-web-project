package com.tlias.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tlias.utils.JwtUtil;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
//@WebFilter(urlPatterns = "/*")
public class TokenFilter implements Filter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filerChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //1.获取请求路径
        String requestURI = request.getRequestURI();
        //2.判断路径中是否存在/login，如果是，放行
        if (requestURI.contains("/login")) {
            log.info("登录请求, 放行");
            filerChain.doFilter(request, response);
            return;
        }
        //3.获取请求头中的token
        String token = request.getHeader("Token");
        //4.判断token是否存在，如果token不存在，说明用户没有登录过，返回错误信息(响应401错误码)
        if (token == null || token.isEmpty()) {
            log.info("令牌为空, 响应401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        //5.如果token存在，校验令牌，如果校验失败 -> 返回错误信息(响应401错误码)
        try {
            jwtUtil.parseToken(token);
        } catch (Exception e) {
            log.info("令牌校验失败, 响应401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        
        //6.校验通过，放行
        log.info("令牌校验通过, 放行");
        filerChain.doFilter(request, response);
    }

}
