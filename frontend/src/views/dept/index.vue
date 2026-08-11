<script setup>
import { onMounted, ref } from 'vue';
import {queryAllApi,addApi,queryByIdApi,updateApi,deleteApi} from '@/api/dept'
import { ElMessage,ElMessageBox } from 'element-plus'
onMounted(()=>{
  search();
})
//查询所有部门
const search = async () =>{
  const result = await queryAllApi();
  if(result.code){
    deptList.value = result.data;
  }
}
const deptList = ref([]);
//dialog
const dialogFormVisible = ref(false);
const dept = ref({name:''});
//提交按钮
const save = async ()=>{
  //表单校验
  if(!deptFormRef.value) return;
  deptFormRef.value.validate(async (valid) => {
    if(valid){
      let result;
      //如果有id执行修改，没有执行新增
      if(dept.value.id){
        result = await updateApi(dept.value)

      }
      else{
        result = await addApi(dept.value);
      }
      
      if(result.code){
        //提示信息
        ElMessage.success('操作成功');
        //关闭对话框
        dialogFormVisible.value = false;
        //查询
        search();
      }else{
        ElMessage.error(result.msg);
      }
    }else{
      ElMessage.error('表单校验不通过');
    }
  })
  
};
//重置表单提示
const resetForm = () => {
  if(deptFormRef.value){
    deptFormRef.value.resetFields();
  }
}
//新增部门按钮
const addDept = () =>{
  formTitle.value = '新增部门';
  dialogFormVisible.value = true;
  dept.value.name = '';
  resetForm();
  
};
//编辑按钮
const edit = async (id) => {
  const result = await queryByIdApi(id);
  if(result.code){
    
    formTitle.value = '修改部门';
    dept.value = result.data;
    dialogFormVisible.value = true;
    
    resetForm();
  }
}
//删除按钮
const deleteById = (id) => {
  //弹出确认框
  ElMessageBox.confirm(
    '您确认删除该部门吗?',
    '提示',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
  //确认按钮
    .then(async () => {
      const result = await deleteApi(id);
      if(result.code){
        search();
        ElMessage.success('删除成功');
      }else{
        ElMessage.error(result.msg);
      }
    })
    //取消按钮
    .catch(() => {
      ElMessage.info('您已取消删除')
    });
}
const formTitle = ref('');

//表单校验
const rules = ref({
  name:[
  { required: true, message: '部门名称是必填项', trigger: 'blur' },
    { min: 2, max: 10, message: '部门名称的长度需要在2-10位', trigger: 'blur' },
  ]
});
const deptFormRef = ref();

</script>

<template>
  <h1>部门管理</h1>
  <div class="container">
    <el-button type="primary" @click="addDept">+ 新增部门</el-button>
  </div>
  <div class="container">
    <el-table :data="deptList" border style="width: 100%">
    <el-table-column type="index" label="序号" width="100" align="center" />
    <el-table-column prop="name" label="部门名称" width="260" align="center" />
    <el-table-column prop="updateTime" label="最后操作时间" width="180" align="center" />
    <el-table-column  label="操作" align="center" >
      <template #default="scope">
        <el-button type="primary" size="small" @click="edit(scope.row.id)" ><el-icon><Edit /></el-icon>编辑</el-button>
        <el-button type="danger" size="small" @click="deleteById(scope.row.id)"><el-icon><Delete /></el-icon>删除</el-button>
      </template>
    </el-table-column>
    
  </el-table>
  </div>
  <div class="container">
    <el-dialog v-model="dialogFormVisible" :title="formTitle" width="500">
    <el-form :model="dept" :rules="rules" ref="deptFormRef">
      <el-form-item label="部门名称" :label-width="80" prop="name">
        <el-input v-model="dept.name" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="save">
          确定
        </el-button>
      </div>
    </template>
  </el-dialog>
  </div>
</template>

<style scoped>
.container{
  margin:10px 10px;
}
</style>
