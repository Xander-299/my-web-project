import axios from "axios";
import { ElMessage } from "element-plus";
import router from '@/router/index';
//创建axios实例对象
const request = axios.create({
    baseURL:'/api',
    timeout:600000
});
//axios的请求拦截器，获取localStorage中的token，在请求头增加token请求头
request.interceptors.request.use(
    (config)=>{//成功回调
        
        const loginUser = JSON.parse(localStorage.getItem('loginUser'));//从本地存储获取loginUser
        if(loginUser && loginUser.token){
            config.headers.token = loginUser.token; //把JWT令牌赋值给请求头中的token属性
        }
        return config;
    },
    (error) =>{//失败回调
        return Promise.reject(error);
    }
)
//axios的响应response拦截器
request.interceptors.response.use(
    (response)=>{
        return response.data;
    },
    (error)=>{
        if(error.response.status === 401){
            //提示信息
           ElMessage({
            type:'error',
            message:'登录超时，请重新登录'
           });
           //跳转登录页面
           router.push('/login');
        } else{
            ElMessage({
                type:'error',
                message:'接口访问异常'
            })
        }
        return Promise.reject(error);
    }

);
export default request;