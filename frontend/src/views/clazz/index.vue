<script setup>
import { onMounted, reactive, ref, watch } from 'vue';
import { queryAllClazzApi, addClazzApi,deleteClazzApi,queryClazzByIdApi,updateClazzApi } from '@/api/clazz';
import {queryAllEmpApi} from '@/api/emp'
import { ElMessage, ElMessageBox } from 'element-plus';
//变量
//表单对象
const ruleFormRef = ref();
//对话框可见性
const dialogFormVisible = ref(false);
//对话框标题
const diglogFormTitle = ref('');
//学科列表
const subjects = ref([
  {name:'java',value:1},
  {name:'前端',value:2},
  {name:'大数据',value:3},
  {name:'Python',value:4},
  {name:'Go',value:5},
  {name:'嵌入式',value:6}
])
//班主任列表
const masters = ref([
])

//表单校验规则
const rules = reactive({
  name: [
    { required: true, message: '班级名称为必填项', trigger: 'blur' },
    { min: 4, max: 30, message: '用户名长度为4-30个字', trigger: 'blur' },
  ],
  beginDate:[
    {
      type: 'date',
      required: true,
      message: '开课时间为必填项',
      trigger: 'change',
    },
  ],
  endDate:[
    {
      type: 'date',
      required: true,
      message: '结课时间为必填项',
      trigger: 'change',
    },
  ],
  subject: [
    {
      required: true,
      message: '学科为必填项',
      trigger: 'change',
    },
  ]
}
)
//对话框表单
const clazzForm = ref({
  id:'',
  name:'',
  room:'',
  beginDate:'',
  endDate:'',
  masterId:'',
  subject:''
})
//查询条件对象
const formInline = ref({
  clazzName:'',
  date:[],
  begin:'',
  end:''
})
//表格数据对象
const tableData = ref([])
//分页组件
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(100)
//=====================
//自定义函数
//查询所有员工
const queryAllEmp = async () =>{
  const result = await queryAllEmpApi();
  if(result.code){
    masters.value = result.data;
  }
}
//更改记录数
const handleSizeChange = (val) => {
  pageSize.value = val;
  
  queryAll();
}
//更改当前页
const handleCurrentChange = (val) => {
  currentPage.value = val;
  
  queryAll();
}
//查询按钮
const queryAll = async ()=>{
  const result = await queryAllClazzApi(formInline.value.clazzName,formInline.value.begin,formInline.value.end,currentPage.value,pageSize.value);
  if(result.code){
    tableData.value = result.data.rows;
    total.value = result.data.total;
  }
}
//清空按钮
const clear = () =>{
  formInline.value = {clazzName:'',date:[],begin:'',end:''};
  queryAll();
}
//新增班级按钮
const addClazz = (formEl) =>{
  resetForm(formEl);
  diglogFormTitle.value = '新增班级';
  clazzForm.value = {id:'',name:'',room:'',beginDate:'',endDate:'',masterId:undefined,subject:undefined};
  dialogFormVisible.value = true;
  
}
//清除提示
const resetForm = (formEl) =>{
  if (!formEl)return;
    
    formEl.resetFields();
  
  
}
//弹窗取消按钮
const cancel = () =>{
  dialogFormVisible.value = false;
  ElMessage({
    type:'info',
    message:'操作取消'
  })
  resetForm();
}
//弹窗保存按钮
const save = (formEl)=>{
  //表单提交校验
  if (!formEl) return;
  formEl.validate(async (valid) => {
    if (valid) {
      let result;
      if(clazzForm.value.id){
        //修改
        result = await updateClazzApi(clazzForm.value);
      }else{
        //添加
        result = await addClazzApi(clazzForm.value);
      }
      if(result.code){
          ElMessage({
            type:'success',
            message:'操作成功'
          })
          dialogFormVisible.value = false;
          queryAll();
        }else{
          ElMessage({
            type:'error',
            message:result.msg
          })
        }
    } else {
      ElMessage({
            type:'error',
            message:'表单验证失败'
          })
    }
  })

  
}
//编辑按钮
const edit = async (id) =>{
  const result = await queryClazzByIdApi(id);
  if(result.code){
    clazzForm.value = result.data;
    diglogFormTitle.value = '修改班级';
    dialogFormVisible.value = true;
  }
}
//删除按钮
const deleteById = (id) => {
  ElMessageBox.confirm(
    '您确认删除此数据吗?',
    '删除班级',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
  //确认按钮
    .then(async () => {
      const result = await deleteClazzApi(id);
      if(result.code){
        
        ElMessage.success('删除成功');
        queryAll();
      }else{
        ElMessage.error(result.msg);
      }
    })
    //取消按钮
    .catch(() => {
      ElMessage.info('您已取消删除')
    });
}
//====================
//组件函数
//监听日期组件
watch(
  ()=>formInline.value.date,
  ()=>{
    if(formInline.value.date && formInline.value.date.length == 2){
      formInline.value.begin = formInline.value.date[0];
      formInline.value.end = formInline.value.date[1];
    }
  }
)
//组件装配时
onMounted(()=>{
  queryAll();
  queryAllEmp();
})
</script>

<template>
  <h1>班级管理</h1>
  <!--查询条件-->
  <div class="container">
  <el-form :inline="true" :model="formInline" class="demo-form-inline">
    
    <el-form-item label="班级名称">
      <el-input v-model="formInline.clazzName" placeholder="请输入班级名称" clearable />
    </el-form-item>
    
    <el-form-item label="结课时间">
      <el-date-picker
        v-model="formInline.date"
        type="daterange"
        range-separator="至"
        start-placeholder="开始时间"
        end-placeholder="结束时间"
        format="YYYY-MM-DD"
        value-format="YYYY-MM-DD"
      />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="queryAll">查询</el-button>
      <el-button type="info" @click="clear">清空</el-button>
    </el-form-item>
    
  </el-form>
  
  </div>
  <!--新增按钮-->
  <div class="container">
    <el-button type="success" @click="addClazz(ruleFormRef)">+ 新增班级</el-button>
  </div>
  <!--表格-->
  <div class="container">
  <el-table :data="tableData" border style="width: 100%">
    <el-table-column type="index" width="80" label="序号" align="center"/>
    <el-table-column prop="name" label="班级名称" width="180"  align="center"/>
    <el-table-column prop="room" label="班级教室" width="100" align="center"/>
    <el-table-column prop="masterName" label="班主任" width="100" align="center"/>
    <el-table-column prop="beginDate" label="开课时间" width="150" align="center"/>
    <el-table-column prop="endDate" label="结课时间" width="150" align="center"/>
    <el-table-column prop="status" label="状态" width="100" align="center"/>
    <el-table-column prop="updateTime" label="最后修改时间" width="180" align="center"/>
    <el-table-column fixed="right" label="操作" min-width="100" align="center">
      <template #default="scope">
        <el-button type="primary" size="small" @click="edit(scope.row.id)">编辑</el-button>
        <el-button type="danger" size="small" @click="deleteById(scope.row.id)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>
  </div>
  <!--分页组件-->
  <div class="container">
    <el-pagination
      v-model:current-page="currentPage4"
      v-model:page-size="pageSize4"
      :page-sizes="[5, 10, 20, 50, 100]"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
  <!--弹窗组件-->
  <el-dialog v-model="dialogFormVisible" :title="diglogFormTitle" width="520">
    <el-form 
    ref="ruleFormRef"
    :rules="rules"
    :model="clazzForm"
    label-width="80px"
    
    >
    
    <el-row>
    <el-col :span="24">
      <el-form-item label="班级名称" prop="name"  >
      <el-input v-model="clazzForm.name" placeholder="请输入班级名称" />
    </el-form-item>
    </el-col>
  </el-row>
  <el-row>
    <el-col :span="24">
      <el-form-item label="班级教室" >
      <el-input v-model="clazzForm.room" placeholder="请输入班级教室"   />
    </el-form-item>
    </el-col>
  </el-row>
  <el-row>
    <el-col :span="24">
      <el-form-item label="开课时间"  prop="beginDate">
        <el-date-picker
        v-model="clazzForm.beginDate"
        type="date"
        placeholder="请选择开课时间"
        format="YYYY-MM-DD"
        value-format="YYYY-MM-DD"
        style="width: 100%"
      />
    </el-form-item>
    </el-col>
  </el-row>
  <el-row>
    <el-col :span="24">
      <el-form-item label="结课时间" prop="endDate" >
        <el-date-picker
        v-model="clazzForm.endDate"
        type="date"
        placeholder="请选择结课时间"
        format="YYYY-MM-DD"
        value-format="YYYY-MM-DD"
        style="width: 100%"
      />
    </el-form-item>
    </el-col>
  </el-row>
  <el-row>
    <el-col :span="24">
      <el-form-item label="班主任" >
        <el-select v-model="clazzForm.masterId" placeholder="请选择班主任" style="width: 100%">
          <el-option v-for="m in masters" :key="m.id" :label="m.name" :value="m.id"></el-option>
      </el-select>
    </el-form-item>
    </el-col>
  </el-row>
  <el-row>
    <el-col :span="24">
      <el-form-item label="学科" prop="subject">
        <el-select v-model="clazzForm.subject" placeholder="请选择学科" style="width: 100%">
        <el-option v-for="s in subjects" :key="s.value" :value="s.value" :label="s.name"></el-option>
        </el-select>
    </el-form-item>
    </el-col>
  </el-row>
      <el-form-item >
      </el-form-item>
      <el-form-item >
      </el-form-item>
  <el-row  justify="end">
    <el-col :span="16"></el-col>
    <el-col :span="4"><el-button bg @click="cancel">取消</el-button></el-col>
    <el-col :span="4"><el-button type="primary" @click="save(ruleFormRef)">保存</el-button></el-col>
  </el-row>
  </el-form>
  </el-dialog>
</template>

<style scoped>
.container{
  margin: 10px 0px;
}
</style>