<template>
  <div class="statistics">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>各课程平均分对比</span>
            </div>
          </template>
          <div ref="averageChartRef" class="chart"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>班级人数分布</span>
            </div>
          </template>
          <div ref="classChartRef" class="chart"></div>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>成绩分布统计</span>
            </div>
          </template>
          <div ref="scoreChartRef" class="chart"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import { getCourseAverageScores, getStudentCountByClass, getScoreDistribution } from '@/api/statistics'
import { getAllCourses } from '@/api/course'
import { getAllScores } from '@/api/score'

const averageChartRef = ref(null)
const classChartRef = ref(null)
const scoreChartRef = ref(null)

const loadAverageChart = async () => {
  try {
    const res = await getCourseAverageScores()
    const data = res.data
    const chart = echarts.init(averageChartRef.value)
    chart.setOption({
      tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
      xAxis: {
        type: 'category',
        data: Object.keys(data),
        axisLabel: { interval: 0, rotate: 30 }
      },
      yAxis: { type: 'value', name: '平均分', min: 0, max: 100 },
      series: [{
        data: Object.values(data),
        type: 'bar',
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#83bff6' },
            { offset: 1, color: '#188df0' }
          ])
        }
      }]
    })
    window.addEventListener('resize', () => chart.resize())
  } catch (error) {
    console.error('加载图表失败', error)
  }
}

const loadClassChart = async () => {
  try {
    const res = await getStudentCountByClass()
    const data = res.data
    const chart = echarts.init(classChartRef.value)
    chart.setOption({
      tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
      xAxis: {
        type: 'category',
        data: Object.keys(data),
        axisLabel: { interval: 0, rotate: 30 }
      },
      yAxis: { type: 'value', name: '人数' },
      series: [{
        data: Object.values(data),
        type: 'bar',
        itemStyle: { color: '#5470c6' }
      }]
    })
    window.addEventListener('resize', () => chart.resize())
  } catch (error) {
    console.error('加载图表失败', error)
  }
}

const loadScoreChart = async () => {
  try {
    const [coursesRes, scoresRes] = await Promise.all([getAllCourses(), getAllScores()])
    const courses = coursesRes.data || []
    const scores = scoresRes.data || []

    if (courses.length === 0) {
      console.warn('课程列表为空')
      return
    }

    // 找到有成绩的课程
    let courseId = null
    if (scores.length > 0) {
      // 从成绩数据中获取第一个课程 ID
      courseId = courses[0].id
    } else {
      courseId = courses[0].id
    }

    const res = await getScoreDistribution(courseId)
    const data = res.data

    if (!data || Object.keys(data).length === 0) {
      console.warn('成绩分布数据为空')
      // 显示空数据提示
      const chart = echarts.init(scoreChartRef.value)
      chart.setOption({
        title: {
          text: '暂无成绩数据',
          left: 'center',
          top: 'center'
        }
      })
      return
    }

    const chart = echarts.init(scoreChartRef.value)
    chart.setOption({
      tooltip: { trigger: 'item' },
      legend: { top: 'bottom' },
      series: [{
        type: 'pie',
        radius: '70%',
        data: Object.entries(data).map(([key, value]) => ({ name: key, value })),
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }]
    })
    window.addEventListener('resize', () => chart.resize())
  } catch (error) {
    console.error('加载成绩分布图表失败', error)
  }
}

onMounted(() => {
  setTimeout(() => {
    loadAverageChart()
    loadClassChart()
    loadScoreChart()
  }, 100)
})
</script>

<style scoped>
.statistics {
  padding: 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart {
  height: 350px;
  width: 100%;
}
</style>
