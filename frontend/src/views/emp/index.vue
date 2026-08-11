<script setup>
import { onMounted, ref, watch } from 'vue';
import { queryPageApi } from '@/api/emp';
import { queryAllApi as queryAllDeptApi } from '@/api/dept';
import { ElMessage,ElMessageBox } from 'element-plus';
import { addApi, deleteApi, queryByIdApi, updateApi } from '../../api/emp';
//变量
//token
const token = ref('');
//被选中的行
const selectedIds = ref([])
//员工列表数据
const empList = ref([
  
]);
// 控制弹窗
const dialogVisible = ref(false)
const dialogTitle = ref('')
//分页相关
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(10);
//搜索表单对象
const searchEmp = ref({
  name:'',
  gender:'',
  date:[],
  begin:'',
  end:''
});
const background = ref(true);
//表单校验规则
const rules = ref({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 20, message: '用户名长度应在2到20个字符之间', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    { min: 2, max: 10, message: '姓名长度应在2到10个字符之间', trigger: 'blur' }
  ],
  gender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入有效的手机号', trigger: 'blur' }
  ]
});
//表单引用
const empFormRef = ref();

//删除工作经历
const delExprItem = (index) =>{
  employee.value.exprList.splice(index,1);
}
//添加工作经历
const addExprItem = () =>{
  employee.value.exprList.push({company:'',job:'',begin:'',end:'',exprDate:[]});
}
//元数据
//职位列表数据
const jobs = ref([{ name: '班主任', value: 1 },{ name: '讲师', value: 2 },{ name: '学工主管', value: 3 },{ name: '教研主管', value: 4 },{ name: '咨询师', value: 5 },{ name: '其他', value: 6 }])
//性别列表数据
const genders = ref([{ name: '男', value: 1 }, { name: '女', value: 2 }])
//部门列表数据
const depts = ref([])
//新增/修改表单
const employee = ref({
  username: '',
  name: '',
  gender: '',
  phone: '',
  job: '',
  salary: '',
  deptId: '',
  entryDate: '',
  image: '',
  exprList: []
});
//==============================================================================
//自定义函数
//查询所有部门
const queryAllDepts = async () =>{
  const result = await queryAllDeptApi();
  if(result.code){
    depts.value = result.data;
  }
}
//添加员工
const addEmp = () => {
  dialogTitle.value = '新增员工'
  employee.value = {username: '',name: '',gender: '',phone: '',job: '',salary: '',deptId: '',entryDate: '',image: '',exprList: []};
  dialogVisible.value = true; 
  resetForm();
}
//根据id删除员工
const deleteById = async (id) =>{
  ElMessageBox.confirm(
    '确定删除选中员工吗？',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ) //确定按钮
    .then(async () => {
      
        const result = await deleteApi(id);
        if(result.code){
          ElMessage({
          type: 'success',
          message: '删除成功',
          })
          query();
        }else{
          ElMessage({
            type:'error',
            message:result.msg
          })
        
      
      }
    })
    //取消按钮
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '操作取消',
      })
    })
} 
//批量删除员工
const deleteEmp = () =>{
  if(selectedIds.value && selectedIds.value.length > 0){
    ElMessageBox.confirm(
    '确定删除选中员工吗？',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ) //确定按钮
    .then(async () => {
      
        const result = await deleteApi(selectedIds.value);
        if(result.code){
          ElMessage({
          type: 'success',
          message: '删除成功',
          })
          query();
        }else{
          ElMessage({
            type:'error',
            message:result.msg
          })
        
      
      }
    })
    //取消按钮
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '操作取消',
      })
    })
  }else{
    ElMessage({
            type:'error',
            message:'您还未选择任何数据'
    })
  }
  
}
 
//文件上传
// 图片上传成功后触发
const handleAvatarSuccess = (response) => {
  employee.value.image = response.data;
}
// 文件上传之前触发
const beforeAvatarUpload = (rawFile) => {
  if (rawFile.type !== 'image/jpeg' && rawFile.type !== 'image/png') {
    ElMessage.error('只支持上传图片')
    return false
  } else if (rawFile.size / 1024 / 1024 > 10) {
    ElMessage.error('只能上传10M以内图片')
    return false
  }
  return true
}
//查询按钮
const query = async () =>{
  
  const result = await queryPageApi(searchEmp.value.name,searchEmp.value.gender,searchEmp.value.begin,searchEmp.value.end,currentPage.value,pageSize.value);
  
  if(result.code){
    empList.value = result.data.rows;
    total.value = result.data.total;
  }
  
};
//清空按钮
const clear = () => {
  searchEmp.value = {name:'',gender:'',date:[],begin:'',end:''};
  query();
};




//每页记录数变化时触发
const handleSizeChange = (val) => {
  query();
};
//页码变化时触发
const handleCurrentChange = (val) => {
  query();
};

//对话框保存按钮
const save = async ()=>{
  //表单校验
  if(!empFormRef.value) return;
  empFormRef.value.validate(async (valid) => {
    if(valid){
      let result;
      //如果有id
      if(employee.value.id){
        result = await updateApi(employee.value);
      }else{
        result = await addApi(employee.value);
      }
      if(result.code){
        ElMessage.success('操作成功');
        dialogVisible.value = false;
        query();
      
      }else{
        ElMessage.error(result.msg);
      }
      
    }else{
      ElMessage.error('表单校验不通过');
    }
  })

  

  
}
//重置表单警告
const resetForm = () => {
  if(empFormRef.value){
    empFormRef.value.resetFields();
  }
}
//编辑按钮
const edit = async (id) =>{
  
  const result = await queryByIdApi(id);
  if(result.code){
    dialogVisible.value = true;
    dialogTitle.value = '修改员工';
    employee.value = result.data; //会触发watch
    
    //处理工作经历日期
    let exprList = employee.value.exprList;
    if(exprList && exprList.length > 0){
      exprList.forEach((expr)=>{
        expr.exprDate = [expr.begin,expr.end];
      })
    }
    
  }else{
    ElMessage.error(result.msg);
  }
}
//处理多选框
const handleSelectionChange = (selection) =>{
  selectedIds.value = selection.map((item)=>item.id);
  
}
//获取token
const getToken = () =>{
  const loginUser = JSON.parse(localStorage.getItem('loginUser'));
  if(loginUser && loginUser.token){
    token.value = loginUser.token;
  }
}

//组件函数======================================================================
watch(()=>searchEmp.value.date,(newVal,oldVal)=>{
  if(newVal.length == 2){
    searchEmp.value.begin = newVal[0];
    searchEmp.value.end = newVal[1];
  }else{
    searchEmp.value.begin = '';
    searchEmp.value.end = '';
  }
});
//监听exprList
watch(()=>employee.value.exprList,
 ()=>{
  
  if(employee.value.exprList && employee.value.exprList.length > 0){
    employee.value.exprList.forEach((expr)=>{
      expr.begin = expr.exprDate[0];
      expr.end = expr.exprDate[1];
    })
  }
},{deep:true})//深度侦听，因为不深度侦听只有exprList的引用发生变化才会触发，往里添加元素相当于push，不会触发
onMounted(()=>{
  query(); //刷新时查询
  queryAllDepts(); //根据部门id查询部门名称
  getToken(); //刷新时获取token
})
</script>

<template>
  <h1>员工管理</h1>
  <!--搜索栏-->
  <div class="container">
    <el-form :inline="true" :model="searchEmp" class="demo-form-inline">
    <el-form-item label="姓名">
      <el-input v-model="searchEmp.name" placeholder="请输入员工姓名" clearable />
    </el-form-item>
    <el-form-item label="性别">
      <el-select
        v-model="searchEmp.gender"
        placeholder="请选择"
        
      >
        <el-option label="男" value="1" />
        <el-option label="女" value="2" />
      </el-select>
    </el-form-item>
    <el-form-item label="入职时间">
      <el-date-picker
        v-model="searchEmp.date"
        type="daterange"
        range-separator="到"
        start-placeholder="开始时间"
        end-placeholder="结束时间"
        value-format="YYYY-MM-DD"
      />
    </el-form-item>

    <el-form-item>
      <el-button type="primary" @click="query">查询</el-button>
      <el-button type="info" @click="clear">清空</el-button>
    </el-form-item>
  </el-form>
  </div>
  <div class="container">
    <el-button type="primary" @click="addEmp">+ 新增员工</el-button>
      <el-button type="danger" @click="deleteEmp(selectedIds.value)">- 批量删除</el-button>
  </div>
  <!--表格-->
  <div class="container">
    <el-table :data="empList" border style="width: 100%" @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="50" align="center"  />
    <el-table-column prop="name" label="姓名"  align="center" width="150" />
    <el-table-column prop="gender" label="性别"  align="center" width="100">
      <template #default="scope">
        {{ scope.row.gender == '1' ? '男':'女' }}
    </template>
    </el-table-column>
    
    <el-table-column prop="image" label="头像"  align="center" width="120">
      <template #default="scope">
        <img :src="scope.row.image" height="40px">
      </template>
    </el-table-column>
    <el-table-column prop="deptName" label="部门名称"  align="center" with="120"/>
    <el-table-column prop="job" label="职位" align="center" with="80" >
      <template #default="scope">
        <span v-if="scope.row.job == 1">班主任</span>
        <span v-else-if="scope.row.job == 2">讲师</span>
        <span v-else-if="scope.row.job == 3">学工主管</span>
        <span v-else-if="scope.row.job == 4">校验主管</span>
        <span v-else-if="scope.row.job == 5">其它</span>
      </template>
    </el-table-column>
    <el-table-column prop="entryDate" label="入职时间"  align="center" width="150" />
    <el-table-column prop="updateTime" label="最后操作时间"align="center" width="200" />
    <el-table-column  label="操作"  align="center">
      <template #default="scope">
        <el-button type="primary" size="small" @click="edit(scope.row.id)">编辑</el-button>
      <el-button type="danger" size="small" @click="deleteById(scope.row.id)" >删除</el-button>
      </template>
      
    </el-table-column>
    
  </el-table>
  </div>
  <!--分页栏-->
  <div class="container">
    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :page-sizes="[5, 10, 20, 30,50,75,100]"
      :background="background"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
  <!-- 新增/修改员工的对话框 -->
  <el-dialog v-model="dialogVisible" :title="dialogTitle">
    
      <el-form :model="employee" label-width="80px" :rules="rules" ref="empFormRef">
        <!-- 基本信息 -->
        <!-- 第一行 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="employee.username" placeholder="请输入员工用户名，2-20个字"></el-input>
            </el-form-item>
          </el-col>
           
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="employee.name" placeholder="请输入员工姓名，2-10个字"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
 
        <!-- 第二行 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-select v-model="employee.gender" placeholder="请选择性别" style="width: 100%;">
                <el-option v-for="g in genders" :key="g.value" :label="g.name" :value="g.value"></el-option>
                
              </el-select>
            </el-form-item>
          </el-col>
 
          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="employee.phone" placeholder="请输入员工手机号"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
 
        <!-- 第三行 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="职位">
              <el-select v-model="employee.job" placeholder="请选择职位" style="width: 100%;">
                <el-option v-for="j in jobs" :key="j.value" :label="j.name" :value="j.value"></el-option>
                
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="薪资">
              <el-input v-model="employee.salary" placeholder="请输入员工薪资"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
 
        <!-- 第四行 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属部门">
              <el-select v-model="employee.deptId" placeholder="请选择部门" style="width: 100%;">
                <el-option v-for="d in depts" :key="d.id" :label="d.name" :value="d.id"></el-option>
                
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入职日期">
              <el-date-picker v-model="employee.entryDate" type="date" style="width: 100%;" placeholder="选择日期" format="YYYY-MM-DD" value-format="YYYY-MM-DD"></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
 
        <!-- 第五行 -->
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="头像">
              <el-upload
                class="avatar-uploader"
                action="/api/upload"
                :headers="{'token':token}"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload"
                >
                <img v-if="employee.image" :src="employee.image" class="avatar" />
                <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
              </el-upload>
            </el-form-item>
          </el-col>
        </el-row>
         
 
        <!-- 工作经历 -->
        <!-- 第六行 -->
        <el-row :gutter="10">
          <el-col :span="24">
            <el-form-item label="工作经历">
              <el-button type="success" size="small" @click="addExprItem">+ 添加工作经历</el-button>
            </el-form-item>
          </el-col>
        </el-row>
         
        <!-- 第七行 ...  工作经历 -->
        <el-row :gutter="3" v-for="(expr,index) in employee.exprList" >
          <el-col :span="10">
            <el-form-item size="small" label="时间" label-width="80px">
              <el-date-picker v-model="expr.exprDate" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" format="YYYY-MM-DD" value-format="YYYY-MM-DD" ></el-date-picker>
            </el-form-item>
          </el-col>
 
          <el-col :span="6">
            <el-form-item size="small" label="公司" label-width="60px">
              <el-input placeholder="请输入公司名称" v-model="expr.company"></el-input>
            </el-form-item>
          </el-col>
 
          <el-col :span="6">
            <el-form-item size="small" label="职位" label-width="60px">
              <el-input placeholder="请输入职位" v-model="expr.job"></el-input>
            </el-form-item>
          </el-col>
 
          <el-col :span="2">
            <el-form-item size="small" label-width="0px">
              <el-button type="danger" @click="delExprItem(index)" >- 删除</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
       
      <!-- 底部按钮 -->
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="save">保存</el-button>
        </span>
      </template>
  </el-dialog>

</template>

<style scoped>
.container{
  margin: 10px 0px;
}
.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 78px;
  height: 78px;
  text-align: center;
  border-radius: 10px;
  /* 添加灰色的虚线边框 */
  border: 1px dashed var(--el-border-color);
}
</style>