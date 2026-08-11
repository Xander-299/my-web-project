import request from '@/utils/request';

//班级人数统计
const clazzCountApi = () => request.get('/report/studentCountData');
//学员学历统计
const stuDegreeApi = () => request.get('/report/studentDegreeData');

export{
    clazzCountApi,
    stuDegreeApi
}