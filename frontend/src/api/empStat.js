import request from '@/utils/request';

//职位人数统计
const empJobCountApi = () => request.get('/report/empJobData');
//性别统计
const empGenderCountApi = () => request.get('/report/empGenderData');

export{
    empGenderCountApi,
    empJobCountApi
}