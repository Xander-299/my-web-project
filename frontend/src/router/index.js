import { createRouter, createWebHistory } from 'vue-router'
import ClazzView from '@/views/clazz/index.vue'
import DeptView from '@/views/dept/index.vue'
import EmpView from '@/views/emp/index.vue'
import IndexView from '@/views/index/index.vue'
import LogView from '@/views/log/index.vue'
import LoginView from '@/views/login/index.vue'
import EmpReportView from '@/views/report/emp/index.vue'
import StuReportView from '@/views/report/stu/index.vue'
import StuView from '@/views/stu/index.vue'
import LayoutView from '@/views/layout/index.vue'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    // {
    //   path: '/',
    //   name: 'home',
    //   component: HomeView
    // },
    // {
    //   path: '/about',
    //   name: 'about',
    //   component: () => import('../views/AboutView.vue')
    // }

    {path:"/",
      component:LayoutView,
      name:"layout",
      redirect:"/index", //重定向
      children:[
        {path:"index", name:"index,",  component:IndexView},
        {path:"clazz", name:"clazz", component:ClazzView},
        {path:"stu",name:"stu",component:StuView},
        {path:"dept",name:"dpet",component:DeptView},
        {path:"emp",name:"emp",component:EmpView},
        {path:"log",name:"log",component:LogView},
        {path:"report/emp",name:"report/emp",component:EmpReportView},
        {path:"report/stu",name:"report/stu",component:StuReportView}
      ]

    },
    {path:"/login",name:"login",component:LoginView}
  ]
})

export default router
