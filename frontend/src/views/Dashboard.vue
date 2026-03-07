<template>
  <div class="dashboard">
    <h2 class="page-title">系统概览</h2>
    
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stat-cards">
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon student">
              <el-icon :size="40"><User /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ overview.studentCount }}</div>
              <div class="stat-label">学生总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon teacher">
              <el-icon :size="40"><UserFilled /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ overview.teacherCount }}</div>
              <div class="stat-label">教师总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon course">
              <el-icon :size="40"><Reading /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ overview.courseCount }}</div>
              <div class="stat-label">课程总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon score">
              <el-icon :size="40"><TrendCharts /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ overview.scoredStudentCount }}</div>
              <div class="stat-label">已评分学生</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="charts-row">
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <span>班级人数分布</span>
          </template>
          <div ref="classChartRef" class="chart"></div>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <span>课程平均分</span>
          </template>
          <div ref="scoreChartRef" class="chart"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { User, UserFilled, Reading, TrendCharts } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { getOverview, getStudentCountByClass, getCourseAverageScores } from '@/api/statistics'

const overview = ref({
  studentCount: 0,
  teacherCount: 0,
  courseCount: 0,
  scoredStudentCount: 0
})

const classChartRef = ref(null)
const scoreChartRef = ref(null)

let classChart = null
let scoreChart = null

async function fetchOverview() {
  try {
    const res = await getOverview()
    overview.value = res.data
  } catch (error) {
    console.error('获取概览数据失败:', error)
  }
}

async function initClassChart() {
  try {
    const res = await getStudentCountByClass()
    const data = res.data
    
    classChart = echarts.init(classChartRef.value)
    classChart.setOption({
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        }
      },
      xAxis: {
        type: 'category',
        data: Object.keys(data)
      },
      yAxis: {
        type: 'value',
        name: '人数'
      },
      series: [{
        data: Object.values(data),
        type: 'bar',
        itemStyle: {
          color: '#409EFF'
        }
      }]
    })
  } catch (error) {
    console.error('获取班级人数数据失败:', error)
  }
}

async function initScoreChart() {
  try {
    const res = await getCourseAverageScores()
    const data = res.data
    
    scoreChart = echarts.init(scoreChartRef.value)
    scoreChart.setOption({
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        }
      },
      xAxis: {
        type: 'category',
        data: Object.keys(data)
      },
      yAxis: {
        type: 'value',
        name: '平均分',
        min: 0,
        max: 100
      },
      series: [{
        data: Object.values(data),
        type: 'bar',
        itemStyle: {
          color: '#67C23A'
        }
      }]
    })
  } catch (error) {
    console.error('获取课程平均分数据失败:', error)
  }
}

onMounted(() => {
  fetchOverview()
  initClassChart()
  initScoreChart()
  
  window.addEventListener('resize', () => {
    classChart?.resize()
    scoreChart?.resize()
  })
})
</script>

<style lang="scss" scoped>
.dashboard {
  .page-title {
    margin-bottom: 20px;
    color: #303133;
  }
  
  .stat-cards {
    margin-bottom: 20px;
  }
  
  .stat-card {
    .stat-content {
      display: flex;
      align-items: center;
      
      .stat-icon {
        width: 80px;
        height: 80px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        color: #fff;
        
        &.student {
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        
        &.teacher {
          background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }
        
        &.course {
          background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }
        
        &.score {
          background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
        }
      }
      
      .stat-info {
        margin-left: 20px;
        
        .stat-value {
          font-size: 32px;
          font-weight: bold;
          color: #303133;
        }
        
        .stat-label {
          margin-top: 5px;
          font-size: 14px;
          color: #909399;
        }
      }
    }
  }
  
  .charts-row {
    .chart-card {
      .chart {
        height: 300px;
      }
    }
  }
}
</style>
