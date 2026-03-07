<template>
  <div class="enrollment-management">
    <el-card>
      <div class="search-bar">
        <el-select v-model="filterStudentId" placeholder="选择学生" clearable style="width: 200px;" @change="handleFilterChange">
          <el-option v-for="s in students" :key="s.id" :label="s.name" :value="s.id" />
        </el-select>
        <el-select v-model="filterCourseId" placeholder="选择课程" clearable style="width: 200px;" @change="handleFilterChange">
          <el-option v-for="c in courses" :key="c.id" :label="c.courseName" :value="c.id" />
        </el-select>
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button type="success" @click="handleEnroll">学生选课</el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" border stripe style="width: 100%; margin-top: 20px;">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="studentName" label="学生" width="120" />
        <el-table-column prop="courseName" label="课程" width="150" />
        <el-table-column prop="enrollDate" label="选课日期" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'active' ? 'success' : 'info'">{{ row.status === 'active' ? '在选' : '已退' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button v-if="row.status === 'active'" type="danger" size="small" @click="handleDrop(row)">退课</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" title="学生选课" width="500px">
      <el-form :model="formData" label-width="80px">
        <el-form-item label="学生">
          <el-select v-model="formData.studentId" style="width: 100%;">
            <el-option v-for="s in students" :key="s.id" :label="s.name" :value="s.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="课程">
          <el-select v-model="formData.courseId" style="width: 100%;">
            <el-option v-for="c in courses" :key="c.id" :label="c.courseName" :value="c.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAllEnrollments, getEnrollmentsByStudent, getEnrollmentsByCourse, enrollCourse, dropCourse } from '@/api/enrollment'
import { getAllStudents } from '@/api/student'
import { getAllCourses } from '@/api/course'

const loading = ref(false)
const tableData = ref([])
const students = ref([])
const courses = ref([])
const filterStudentId = ref('')
const filterCourseId = ref('')
const dialogVisible = ref(false)

const formData = reactive({
  studentId: '',
  courseId: ''
})

const loadData = async () => {
  loading.value = true
  try {
    const res = await getAllEnrollments()
    tableData.value = res.data || []
  } catch (error) {
    console.error('加载数据失败', error)
  } finally {
    loading.value = false
  }
}

const loadOptions = async () => {
  const [studentsRes, coursesRes] = await Promise.all([getAllStudents(), getAllCourses()])
  students.value = studentsRes.data || []
  courses.value = coursesRes.data || []
}

const handleFilterChange = () => {
  handleSearch()
}

const handleSearch = async () => {
  loading.value = true
  try {
    let res
    if (filterStudentId.value) {
      res = await getEnrollmentsByStudent(filterStudentId.value)
    } else if (filterCourseId.value) {
      res = await getEnrollmentsByCourse(filterCourseId.value)
    } else {
      res = await getAllEnrollments()
    }
    tableData.value = res.data || []
  } catch (error) {
    console.error('查询失败', error)
  } finally {
    loading.value = false
  }
}

const handleEnroll = () => {
  formData.studentId = ''
  formData.courseId = ''
  dialogVisible.value = true
}

const handleDrop = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要让"${row.studentName}"退选"${row.courseName}"吗？`, '提示', { type: 'warning' })
    await dropCourse(row.id)
    ElMessage.success('退课成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') console.error('退课失败', error)
  }
}

const handleSubmit = async () => {
  if (!formData.studentId || !formData.courseId) {
    ElMessage.warning('请选择学生和课程')
    return
  }
  try {
    await enrollCourse({ studentId: formData.studentId, courseId: formData.courseId })
    ElMessage.success('选课成功')
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('选课失败', error)
    ElMessage.error('该学生已选此课程')
  }
}

onMounted(() => {
  loadData()
  loadOptions()
})
</script>

<style scoped>
.search-bar { display: flex; gap: 10px; align-items: center; }
</style>
