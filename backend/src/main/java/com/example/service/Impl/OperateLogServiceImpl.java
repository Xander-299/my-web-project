package com.example.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mapper.OperateLogMapper;
import com.example.pojo.OperateLog;
import com.example.pojo.PageResult;
import com.example.service.OperateLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
@Service
public class OperateLogServiceImpl implements OperateLogService{
    @Autowired
    private OperateLogMapper operateLogMapper;

    @Override
    public PageResult<OperateLog> getLogResult(Integer page,Integer pageSize) {
        Page<OperateLog> p = PageHelper.startPage(page,pageSize);
        List<OperateLog> operateLogList = operateLogMapper.getLogResult();
        return new PageResult<OperateLog>(p.getTotal(), operateLogList);
    }

}
