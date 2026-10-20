package com.tlias.interceptor;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.tlias.utils.CurrentHolder;
import com.tlias.utils.JwtUtil;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        /* 
        //1.获取请求路径
        String requestURI = request.getRequestURI();
        //2.判断路径中是否存在/login，如果是，放行
        if (requestURI.contains("/login")) {
            log.info("登录请求, 放行");
            
            return true;
        }
        */
        //3.获取请求头中的token
        String token = request.getHeader("Token");
        //4.判断token是否存在，如果token不存在，说明用户没有登录过，返回错误信息(响应401错误码)
        if (token == null || token.isEmpty()) {
            log.info("令牌为空, 响应401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        //5.如果token存在，校验令牌，如果校验失败 -> 返回错误信息(响应401错误码)
        try {
            Claims claims = JwtUtil.parseToken(token);
            Integer empId = Integer.valueOf(claims.get("id").toString());
            CurrentHolder.setCurrentId(empId);
            log.info("当前登录员工id:{}, 并将其存入ThreadLocal",empId);
        } catch (Exception e) {
            log.info("令牌校验失败, 响应401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        
        //6.校验通过，放行
        log.info("令牌校验通过, 放行");
        return true;
        
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            @Nullable ModelAndView modelAndView) throws Exception {
        CurrentHolder.remove();
    }

}
