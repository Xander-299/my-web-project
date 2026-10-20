package com.tlias.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tlias.mapper.LoginMapper;
import com.tlias.pojo.Emp;
import com.tlias.pojo.LoginInfo;
import com.tlias.service.LoginService;
import com.tlias.utils.JwtUtil;
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
