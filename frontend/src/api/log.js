import request from '@/utils/request';

//分页查询
const queryLogApi = (page,pageSize) => request.get(`/log/page?page=${page}&pageSize=${pageSize}`);


export{
    queryLogApi
}