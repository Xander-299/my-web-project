<script setup>
import { onMounted, reactive, ref } from 'vue';
import { queryAllClazzListApi } from '../../api/clazz';
import { addStuApi, deleteStuApi, queryAllStuApi, queryStuByIdApi,updateStuApi, violateScoreApi } from '../../api/stu';
import { ElMessage, ElMessageBox } from 'element-plus';
//变量
//查询条件
const searchStu = ref({
  name:'',
  degree:'',
  clazzId:''
})
//被选中的行
const selectionIdArray = ref([]);
//弹窗表格对象
const ruleFormRef = ref()
//表单验证规则
const rules = reactive({
  name: [
    { required: true, message: '姓名为必填项', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度为2-20个字', trigger: 'blur' },
  ],
  no: [
    { required: true, message: '学号为必填项', trigger: 'blur' },
    { min: 10,max:10, message: '学号长度为10个字', trigger: 'blur' },
  ],
  phone: [
  { required: true, message: '手机号为必填项', trigger: 'blur' },
  { 
    validator: (rule, value, callback) => {
        
        
        // 1. 判断长度是否为 11 位
        if (value.length !== 11) {
          return callback(new Error('请输入合法的手机号'));
        }
        
        // 2. 正则匹配格式
        const reg = /^1[3-9]\d{9}$/;
        if (!reg.test(value)) {
          return callback(new Error('请输入合法的手机号'));
        }
        
        // 3. 校验通过
        callback();

      }, 
        trigger: 'blur' 
    },
    
  ],
  idCard: [
    { required: true, message: '身份证号为必填项', trigger: 'blur' },
    {validator: (rule, value, callback) =>{

      // 1. 判断长度是否为 18 位
      if (value.length !== 18) {
          return callback(new Error('请输入合法的身份证号'));
        }
      //正则
      const reg = /^\d{16}[X0-9]\d$/;
      if (!reg.test(value)) {
          return callback(new Error('请输入合法的身份证号'));
        }
      callback();
    },
    trigger: 'blur'
  },
    
    
  ],
  gender: [
    {
      required: true,
      message: '性别为必填项',
      trigger: 'change',
    },
  ],
  isCollege:[
    {
      required: true,
      message: '是否院校学员为必填项',
      trigger: 'change',
    },
  ],

})
//性别列表
const genders = ref([
  {name:'男',value:1},
  {name:'女',value:2}
])
//是否院校列表
const isColleges = ref([
  {name:'是',value:1},
  {name:'否',value:2},
])
//弹窗表格
const stuForm = ref({
  id:'',
  name:'',
  no:'',
  gender:undefined,
  phone:'',
  degree:undefined,
  clazzId:undefined,
  idCard:'',
  isCollege:undefined,
  address:'',
  graduationDate:''

})
//弹窗
const dialogFormVisible = ref(false)
const dialogFormTitle = ref('')
//分页
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
//学员列表
const stuList = ref([]);
//学历列表
const degrees = ref([
  {name:'初中',value:1},
  {name:'高中',value:2},
  {name:'大专',value:3},
  {name:'本科',value:4},
  {name:'硕士',value:5},
  {name:'博士',value:6}
])
//班级列表
const clazzs = ref([])
//自定义函数
//违纪按钮
const violate = (id) =>{
  ElMessageBox.prompt('请输入违纪扣分', '提示', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    inputPattern:
      /^[\d]+$/,
    inputErrorMessage: '非法字符,请输入数字',
  })
    .then(async ({ value }) => {
      await violateScoreApi(id,value)
      ElMessage({
        type: 'success',
        message: '处理成功',
      })
      query();
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '操作取消',
      })
    })
}
//保存按钮
const save = async (formEl) =>{
  if(!formEl)return;
  await formEl.validate(async (valid, fields) => {
    if (valid) {
      let result;
      if(stuForm.value.id){
        //编辑
        result = await updateStuApi(stuForm.value);
        if(result.code){
          ElMessage({
            type:'success',
            message:'操作成功'
          });
          dialogFormVisible.value = false;
          query();
        }else{
          ElMessage({
            type:'error',
            message:result.msg
          })
          
        }
      }else{
        //添加
        result = await addStuApi(stuForm.value);
        if(result.code){
          ElMessage({
            type:'success',
            message:'操作成功'
          });
          dialogFormVisible.value = false;
          query();
        }else{
          ElMessage({
            type:'error',
            message:result.msg
          })
          
        }
      }
    } else {
      ElMessage({
        type:'error',
        message:'表单校验未通过'
      })
    }
  })
  
}
//批量删除
const batchDeleteStu = (ids)=>{
  if(ids.length > 0){
    ElMessageBox.confirm(
    '确定删除选中的数据吗？',
    '删除学生',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ) //确定按钮
    .then(async () => {
      
        const result = await deleteStuApi(ids);
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
        message: '删除取消',
      })
    })
  }else{
    ElMessage({
      type:'error',
      message:'您还未选中任意数据'
    });
    return;
  }
  

    
}
//根据id删除学员
const deleteStuById = (id) =>{
  if(id === []){
    ElMessage({
      type:'error',
      message:'您还未选中任意数据'
    });
    return;
  }
  ElMessageBox.confirm(
    '确定删除此数据吗？',
    '删除学生',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ) //确定按钮
    .then(async () => {
      
        const result = await deleteStuApi(id);
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
        message: '删除取消',
      })
    })
}
//
//获取班级列表
const getClazzsList = async () =>{
  const result = await queryAllClazzListApi();
  if(result.code){
    clazzs.value = result.data;
  }
}
//记录数改变时
const handleSizeChange = (val) =>{
  pageSize.value = val;
  query();
}
//页数改变时
const handleCurrentChange = (val) =>{
  currentPage.value = val;
  query();
}
//勾选框获取id
const handleSelectionChange = (selection)=>{
  const ids = selection.map((item)=>item.id);
  selectionIdArray.value = ids;
  console.log(selectionIdArray.value);
}
//分页查询
const query = async ()=>{
  const result = await queryAllStuApi(searchStu.value.name,searchStu.value.degree,searchStu.value.clazzId,currentPage.value,pageSize.value);
  if(result.code){
    stuList.value = result.data.rows;
    total.value = result.data.total;
  }
}
//编辑按钮
const edit = async (id) =>{
  const result = await queryStuByIdApi(id);
  if(result.code){
    stuForm.value = result.data;
    dialogFormTitle.value = '修改学员';
    dialogFormVisible.value = true;

  }
}
//新增学员按钮
const addStu = (formEl) =>{
  resetForm(formEl);
  dialogFormTitle.value = '新增学员'
  stuForm.value = {name:'',no:'',gender:undefined,phone:'',degree:undefined,clazzId:undefined,idCard:'',isCollege:undefined,address:'',graduationDate:''};
  dialogFormVisible.value = true;
}
//重置
const resetForm = (formEl) => {
  if (!formEl) return
  formEl.resetFields()

}
//清空按钮
const clear = () =>{
  searchStu.value = {name:'',degree:'',clazzId:''};
  query();
}
//组件函数
onMounted(()=>{
  getClazzsList();
  query();
})
</script>

<template>
  <h1>学员管理</h1>
  <!--查询条件-->
  <div class="container">
    <el-form :inline="true" :model="searchStu" class="demo-form-inline">
    <el-form-item label="姓名">
      <el-input v-model="searchStu.name" placeholder="请输入学生姓名" clearable />
    </el-form-item>
    <el-form-item label="最高学历">
      <el-select
        v-model="searchStu.degree"
        placeholder="请选择"
      >
        <el-option v-for="degree in degrees" :key="degree.value" :label="degree.name" :value="degree.value"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="所属班级">
      <el-select
        v-model="searchStu.clazzId"
        placeholder="请选择"
      >
      <el-option v-for="clazz in clazzs" :key="clazz.id" :label="clazz.name" :value="clazz.id"></el-option>
    </el-select>
    </el-form-item>

    <el-form-item>
      <el-button type="primary" @click="query">查询</el-button>
      <el-button type="info" @click="clear">清空</el-button>
    </el-form-item>
  </el-form>
  </div>
  <!--新增/批量删除按钮-->
  <div class="container">
    <el-button type="success" @click="addStu(ruleFormRef)">+ 新增学员</el-button>
    <el-button type="danger" @click="batchDeleteStu(selectionIdArray)">- 批量删除</el-button>
  </div>
  <!--表格-->
  <div class="container">
    <el-table  :data="stuList" border style="width: 100%" @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="50" align="center"  />
    <el-table-column prop="name" label="姓名"  align="center" width="100" />
    <el-table-column prop="no" label="学号"  align="center" width="110" />
    <el-table-column prop="clazzName" label="班级"  align="center" width="150" />
    <el-table-column prop="gender" label="性别"  align="center" width="70" >
      <template #default="scope">
        {{ scope.row.gender == '1' ? '男':'女' }}
    </template>
    </el-table-column>
    
    <el-table-column prop="phone" label="手机号"  align="center" width="120" />
      
    <el-table-column prop="degree" label="最高学历" align="center" width="90" >
      <template #default="scope">
        <span v-if="scope.row.degree == 1">初中</span>
        <span v-else-if="scope.row.degree == 2">高中</span>
        <span v-else-if="scope.row.degree == 3">大专</span>
        <span v-else-if="scope.row.degree == 4">本科</span>
        <span v-else-if="scope.row.degree == 5">硕士</span>
        <span v-else-if="scope.row.degree == 6">博士</span>
      </template>
    </el-table-column>
    <el-table-column prop="violationCount" label="违纪次数"  align="center" width="90" />
    <el-table-column prop="violationScore" label="违纪扣分" align="center" width="90" />
    <el-table-column prop="updateTime" label="最后修改时间" align="center" width="160" />
    <el-table-column  label="操作"  align="center" min-width="150">
      <template #default="scope">
        <el-button type="primary" size="small" @click="edit(scope.row.id)">编辑</el-button>
        <el-button type="warning" size="small" @click="violate(scope.row.id)">违纪</el-button>
      <el-button type="danger" size="small" @click="deleteStuById(scope.row.id)" >删除</el-button>
      </template>
      
    </el-table-column>
    
  </el-table>
  </div>
  <!--分页栏-->
  <div class="container">
    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :page-sizes="[5, 10, 20,50,100]"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
  <!--弹窗-->
  <el-dialog v-model="dialogFormVisible" :title="dialogFormTitle" width="700">
    <el-form 
    :model="stuForm" 
    label-width="100px" 
    style="max-width: 100%" 
    ref="ruleFormRef"
    :rules="rules"
    >
      <el-row>
        <el-col :span="12">
          <el-form-item label="姓名" prop="name" >
      <el-input v-model="stuForm.name" placeholder="请输入学员姓名" />
    </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="学号" prop="no" >
      <el-input v-model="stuForm.no" placeholder="请输入学员学号" />
    </el-form-item>
        </el-col>
      </el-row>


      <el-row>
        <el-col :span="12">
          <el-form-item label="性别" prop="gender" >
      <el-select v-model="stuForm.gender" placeholder="请选择" style="width: 100%" >
        <el-option v-for="gender in genders" :key="gender.value"  :label="gender.name" :value="gender.value" />
        
      </el-select>
      </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="手机号" prop="phone">
      <el-input v-model="stuForm.phone" placeholder="请输入手机号" />
    </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="身份证号" prop="idCard">
      <el-input v-model="stuForm.idCard" placeholder="请输入身份证号" />
    </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否院校" prop="isCollege">
      <el-select v-model="stuForm.isCollege" placeholder="请选择" style="width: 100%">
        <el-option v-for="isCollege in isColleges" :key="isCollege.value" :label="isCollege.name" :value="isCollege.value" />
        
      </el-select>
      </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="联系地址">
      <el-input v-model="stuForm.address" placeholder="请输入联系地址" />
    </el-form-item>
  </el-col>
        <el-col :span="12">
          <el-form-item label="最高学历">
      <el-select v-model="stuForm.degree" placeholder="请选择" style="width: 100%">
        <el-option v-for="dg in degrees" :key="dg.value" :label="dg.name" :value="dg.value" />
        
      </el-select>
      </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="毕业时间">
          <el-date-picker
        v-model="stuForm.graduationDate"
        type="date"
        placeholder="请选择毕业时间"
        format="YYYY-MM-DD"
        value-format="YYYY-MM-DD"
        style="width: 100%"
      />
    </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="所属班级">
      <el-select v-model="stuForm.clazzId" placeholder="请选择" style="width: 100%">
        <el-option v-for="clazz in clazzs" :key="clazz.id" :label="clazz.name" :value="clazz.id" />
        
      </el-select>
      </el-form-item>
        </el-col>
      </el-row>
      <el-row> <el-col :span="24"><el-form-item></el-form-item></el-col></el-row>
      <el-row> <el-col :span="24"><el-form-item></el-form-item></el-col></el-row>
      <el-row>
        <el-col :span="16" justify="end"></el-col>
        <el-col :span="3"><el-button type="default" bg @click="dialogFormVisible = false">取消</el-button></el-col>
        <el-col :span="3"><el-button type="primary" @click="save(ruleFormRef)">保存</el-button></el-col>
      </el-row>
    </el-form>
  </el-dialog>
</template>

<style scoped>
.container{
  margin: 10px 0px;
}
</style>