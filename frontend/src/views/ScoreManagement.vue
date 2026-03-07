<template>
  <div class="score-management">
    <el-card>
      <div class="search-bar">
        <el-select v-model="filterType" placeholder="筛选类型" style="width: 150px;">
          <el-option label="按学生" value="student" />
          <el-option label="按课程" value="course" />
        </el-select>
        <el-select v-model="filterValue" :placeholder="`请选择${filterType === 'student' ? '学生' : '课程'}`" style="width: 200px;" clearable>
          <el-option v-for="item in filterOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button type="success" @click="handleEntry">录入成绩</el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" border stripe style="width: 100%; margin-top: 20px;">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="studentName" label="学生姓名" width="120" />
        <el-table-column prop="courseName" label="课程名称" width="150" />
        <el-table-column prop="score" label="成绩" width="100">
          <template #default="{ row }">
            <el-tag :type="getScoreType(row.score)">{{ row.score }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="gradedAt" label="录入时间" width="180" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">修改</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="entryDialogVisible" title="录入成绩" width="600px">
      <el-table :data="enrollmentData" border>
        <el-table-column prop="studentName" label="学生" />
        <el-table-column prop="courseName" label="课程" />
        <el-table-column label="成绩">
          <template #default="{ row }">
            <el-input-number v-model="row.score" :min="0" :max="100" controls-position="right" />
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <el-button @click="entryDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitScores">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAllScores, saveScore, deleteScore, getAllStudents, getEnrollmentsByStudent } from '@/api/score'
import { getAllCourses } from '@/api/course'

const loading = ref(false)
const tableData = ref([])
const filterType = ref('student')
const filterValue = ref('')
const entryDialogVisible = ref(false)
const enrollmentData = ref([])

const filterOptions = computed(() => {
  return []
})

const loadData = async () => {
  loading.value = true
  try {
    const res = await getAllScores()
    tableData.value = res.data || []
  } catch (error) {
    console.error('加载数据失败', error)
  } finally {
    loading.value = false
  }
}

const getScoreType = (score) => {
  if (score >= 90) return 'success'
  if (score >= 80) return ''
  if (score >= 60) return 'warning'
  return 'danger'
}

const handleSearch = () => { loadData() }
const handleEntry = async () => {
  try {
    const [studentsRes, coursesRes] = await Promise.all([getAllStudents(), getAllCourses()])
    enrollmentData.value = studentsRes.data.map(s => ({
      studentId: s.id,
      studentName: s.name,
      courseId: coursesRes.data[0]?.id,
      courseName: coursesRes.data[0]?.courseName,
      score: 0
    }))
    entryDialogVisible.value = true
  } catch (error) {
    console.error('加载数据失败', error)
  }
}

const handleEdit = (row) => {
  ElMessageBox.prompt('请输入成绩', '修改成绩', {
    inputValue: row.score,
    inputPattern: /^[0-9]+(\.[0-9]+)?$/,
    inputErrorMessage: '请输入有效数字'
  }).then(async ({ value }) => {
    await saveScore({ enrollmentId: row.enrollmentId, score: parseFloat(value) })
    ElMessage.success('更新成功')
    loadData()
  }).catch(() => {})
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定删除该成绩吗？', '提示', { type: 'warning' })
    await deleteScore(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') console.error('删除失败', error)
  }
}

const handleSubmitScores = async () => {
  try {
    for (const item of enrollmentData.value) {
      if (item.score > 0) {
        await saveScore({ enrollmentId: item.enrollmentId || 1, score: item.score })
      }
    }
    ElMessage.success('保存成功')
    entryDialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('保存失败', error)
  }
}

onMounted(() => { loadData() })
</script>

<style scoped>
.search-bar { display: flex; gap: 10px; align-items: center; }
</style>
