<script setup>
import * as echarts from 'echarts';
import { onMounted, reactive, ref } from 'vue';
import { empGenderCountApi, empJobCountApi } from '../../../api/empStat';
const empJobRef = ref(null);
const empGenderRef = ref(null);
let tableData = ref({
  jobChartInstance:null,
  genderChartInstance:null,
})
const option = ref({
  job:{
    title: {
    text: '员工职位统计',
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
    gender:{
      title: {
      text: '员工性别统计',
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
        data: ['男性员工', '女性员工']
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
const initGenderChart =async ()=>{
  if(!empGenderRef.value)return;
  else{
    tableData.value.genderChartInstance = echarts.init(empGenderRef.value);
    
    const result = await empGenderCountApi();
    if(result.code){
      option.value.gender.series[0].data = result.data;
      option.value.gender.legend.data = result.data.map((item)=>item.name);
    }
    tableData.value.genderChartInstance.setOption(option.value.gender);
  }
}

//柱状图
const initJobChart =async ()=>{
  if(!empJobRef.value)return;
  else{
    tableData.value.jobChartInstance = echarts.init(empJobRef.value);
    
    const result = await empJobCountApi();
    if(result.code){
      option.value.job.xAxis.data = result.data.jobList;
      option.value.job.series[0].data = result.data.dataList;
    }
    tableData.value.jobChartInstance.setOption(option.value.job);
  }
}
onMounted(()=>{
  initJobChart();
  initGenderChart();
})
</script>

<template>
  <div class="container " style="width: 100%;height:100%;">
    
      <el-row style="width: 100%;height:100%;">
    <el-col :span="12">
      <div ref="empJobRef" id="empJob" style="width: 100%;height:100%;"></div>
    </el-col>
    <el-col :span="12">
      <div ref="empGenderRef" id="empGender" style="width: 100%;height:100%;"></div>
    </el-col>
  </el-row>
    
  
</div>
</template>

<style scoped>

</style>