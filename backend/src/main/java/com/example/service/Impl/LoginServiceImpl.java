package com.example.service.Impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mapper.LoginMapper;
import com.example.pojo.Emp;
import com.example.pojo.LoginInfo;
import com.example.service.LoginService;
import com.example.utils.JwtUtil;
@Service
public class LoginServiceImpl implements LoginService{
    @Autowired
    private LoginMapper loginMapper;

    @Override
    public LoginInfo loginSystem(Emp emp) {
        Emp empInfo = loginMapper.loginSystem(emp);
        Integer id = empInfo.getId();
        String username = empInfo.getUsername();
        String name = empInfo.getName();
        Map<String,Object> claims = new HashMap<>();
        claims.put("id", id);
        claims.put("username", username);
        String token = JwtUtil.generateToken(id.toString(), claims);
        if (emp != null) {
            return new LoginInfo(id, username, name, token);
        }
        return null;
        
    }
}
