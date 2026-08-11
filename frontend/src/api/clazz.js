import request from '@/utils/request';

//分页查询
const queryAllClazzApi = (name,begin,end,page,pageSize) => request.get(`/clazzs?name=${name}&begin=${begin}&end=${end}&page=${page}&pageSize=${pageSize}`);
//删除
const deleteClazzApi = (id) => request.delete(`/clazzs/${id}`);
//添加
const addClazzApi = (clazzInfo) => request.post('/clazzs',clazzInfo);
//根据id查询
const queryClazzByIdApi = (id) => request.get(`/clazzs/${id}`);
//修改
const updateClazzApi = (clazzInfo) => request.put('/clazzs',clazzInfo);
//查询所有班级
const queryAllClazzListApi = () => request.get('/clazzs/list');
//导出
export{
    queryAllClazzApi,
    addClazzApi,
    deleteClazzApi,
    queryClazzByIdApi,
    updateClazzApi,
    queryAllClazzListApi
};