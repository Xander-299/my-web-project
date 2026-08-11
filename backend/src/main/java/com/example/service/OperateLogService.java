package com.example.service;

import com.example.pojo.OperateLog;
import com.example.pojo.PageResult;

public interface OperateLogService {

    PageResult<OperateLog> getLogResult(Integer page,Integer pageSize);

}
