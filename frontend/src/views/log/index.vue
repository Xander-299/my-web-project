<script setup>
import { onMounted, ref } from 'vue';
import { queryLogApi } from '../../api/log';

const tableData = ref([])

const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

//分页查询
const queryPage = async () =>{
  const result = await queryLogApi(currentPage.value,pageSize.value);
  if(result.code){
    total.value = result.data.total;
    tableData.value = result.data.rows;
  }
}
const handleSizeChange = (val)=>{
  pageSize.value = val;
  queryPage();
}
const handleCurrentChange = (val)=>{
  currentPage.value = val;
  queryPage();
}

onMounted(()=>{
  queryPage();
})
</script>

<template>
  <h1>日志管理</h1>
  <!--表格-->
  <div class="container">
  <el-table :data="tableData" border style="width: 100%" size="small" >
    <el-table-column prop="operateEmpName" label="操作人" width="100" align="center" />
    <el-table-column prop="operateTime" label="操作时间" width="100" align="center" />
    <el-table-column prop="className" label="类名" align="center"  />
    <el-table-column prop="methodName" label="方法名" align="center" width="100" />
    <el-table-column prop="costTime" label="操作耗时(ms)" align="center" width="80" />
    <el-table-column prop="methodParams" label="请求参数" align="center" width="200" >
      <template #default="scope">
        <el-tooltip 
        popper-class="custom-tooltip" 
        :content="scope.row.methodParams" 
        placement="top" 
        effect="light"
        >
          <el-tag type="primary" class="tag-ellipsis">{{ scope.row.methodParams }}</el-tag>
      </el-tooltip>
      </template>
    </el-table-column>
    <el-table-column prop="returnValue" label="返回值" align="center" />
  </el-table>
</div>
<!--分页栏-->
<div class="container">
  <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :page-sizes="[10, 20, 50, 100]"
      
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
</div>
</template>

<style scoped>
.container{
  margin: 10px 0px;
}
.tag-ellipsis {
  display: inline-block;
  max-width: 180px; /* 最大宽度，根据需求调 */
  white-space: nowrap; /* 不换行 */
  overflow: hidden; /* 超出隐藏 */
  text-overflow: ellipsis; /* 显示省略号 */
}
::v-deep(.custom-tooltip) {
  max-width: 300px !important;
  white-space: normal !important;
  word-break: break-word !important;
}
</style>