<script setup>
import * as echarts from 'echarts';
import { onMounted, ref } from 'vue';
import { clazzCountApi, stuDegreeApi } from '../../../api/stuAndClazzStat';

const clazzRef = ref(null);
const stuDegreeRef = ref(null);
let tableData = ref({
  clazzChartInstance:null,
  stuChartInstance:null,
})
const option = ref({
  clazz:{
    title: {
    text: '班级人数统计',
    left: 'center',   // left / center / right / 像素
    top: 0 
    },
    xAxis: {
      data: []
    },
    yAxis: {},
    series: [
      {
        type: 'bar',
        data: []
      }
    ]
  },
    stu:{
      title: {
      text: '学员学历统计',
      left: 'center',   // left / center / right / 像素
      top: 0 
      },
      tooltip: {
        trigger: 'item',
        
      },
      legend: {
        orient: 'horizontal',
        top:'50',
        left:'center',
        data: []
      },
      series: [
        {
          type: 'pie',
          radius: ['50%', '70%'],
          avoidLabelOverlap: false,
          label: {
            show: false,
            position: 'center'
          },
          labelLine: {
            show: false
          },
          itemStyle: {
            borderColor: '#fff',   // 间隙颜色
            borderWidth: 2,         // 间隙大小（关键）
            borderRadius: 10
          },
          emphasis: {
            label: {
              show: true,
              fontSize: '30',
              fontWeight: 'bold'
            }
          },
          data: []
        }
      ]
    }
});
//圆环图
const initStuChart =async ()=>{
  if(!stuDegreeRef.value)return;
  else{
    tableData.value.stuChartInstance = echarts.init(stuDegreeRef.value);
    
    const result = await stuDegreeApi();
    if(result.code){
      option.value.stu.series[0].data = result.data;
      option.value.stu.legend.data = result.data.map((item)=>item.name);
    }
    tableData.value.stuChartInstance.setOption(option.value.stu);
  }
}

//柱状图
const initClazzChart =async ()=>{
  if(!clazzRef.value)return;
  else{
    tableData.value.clazzChartInstance = echarts.init(clazzRef.value);
    
    const result = await clazzCountApi();
    if(result.code){
      console.log(result.data.clazzList);
      option.value.clazz.xAxis.data = result.data.clazzList;
      option.value.clazz.series[0].data = result.data.dataList;
    }
    tableData.value.clazzChartInstance.setOption(option.value.clazz);
  }
}
onMounted(()=>{
  initClazzChart();
  initStuChart();
})
</script>

<template>
  <div class="container " style="width: 100%;height:100%;">
    <el-row style="width: 100%;height:100%;">
  <el-col :span="12">
    <div ref="clazzRef" id="clazz" style="width: 100%;height:100%;"></div>
  </el-col>
  <el-col :span="12">
    <div ref="stuDegreeRef" id="stu" style="width: 100%;height:100%;"></div>
  </el-col>
</el-row>
  

</div>
</template>

<style scoped>

</style>