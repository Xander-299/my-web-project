package com.tlias.service;

import com.tlias.pojo.OperateLog;
import com.tlias.pojo.PageResult;

public interface OperateLogService {

    PageResult<OperateLog> getLogResult(Integer page,Integer pageSize);

}
