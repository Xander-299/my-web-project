import request from '@/utils/request'

//分页查询
const queryAllStuApi = (name,degree,clazzId,page,pageSize) => request.get(`/students?name=${name}&degree=${degree}&clazzId=${clazzId}&page=${page}&pageSize=${pageSize}`);

//删除
const deleteStuApi = (ids) => request.delete(`/students/${ids}`);
//添加
const addStuApi = (stuInfo) => request.post('/students',stuInfo);
//根据id查询
const queryStuByIdApi = (id) => request.get(`/students/${id}`);
//修改
const updateStuApi = (stuInfo) => request.put('/students',stuInfo);
//违纪处理
const violateScoreApi = (id,score) => request.put(`/students/violation/${id}/${score}`);
export{
    queryAllStuApi,
    deleteStuApi,
    queryStuByIdApi,
    addStuApi,
    updateStuApi,
    violateScoreApi
}