package com.tlias.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tlias.exception.BusinessException;
import com.tlias.mapper.LoginMapper;
import com.tlias.pojo.Emp;
import com.tlias.pojo.LoginInfo;
import com.tlias.service.LoginService;
import com.tlias.utils.JwtUtil;
@Service
public class LoginServiceImpl implements LoginService{
    @Autowired
    private LoginMapper loginMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public LoginInfo loginSystem(Emp emp) {
        Emp empInfo = loginMapper.loginSystem(emp);
        if (empInfo == null) {
            throw new BusinessException("用户名或密码错误");
        }
        Integer id = empInfo.getId();
        String username = empInfo.getUsername();
        String name = empInfo.getName();
        Map<String,Object> claims = new HashMap<>();
        claims.put("id", id);
        claims.put("username", username);
        String token = jwtUtil.generateToken(id.toString(), claims);
        return new LoginInfo(id, username, name, token);

    }
}
