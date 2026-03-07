<template>
  <div class="attendance-management">
    <el-card>
      <div class="search-bar">
        <el-select v-model="filterStudentId" placeholder="选择学生" clearable style="width: 150px;">
          <el-option v-for="s in students" :key="s.id" :label="s.name" :value="s.id" />
        </el-select>
        <el-select v-model="filterCourseId" placeholder="选择课程" clearable style="width: 150px;">
          <el-option v-for="c in courses" :key="c.id" :label="c.courseName" :value="c.id" />
        </el-select>
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button type="success" @click="handleAdd">录入考勤</el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" border stripe style="width: 100%; margin-top: 20px;">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="studentName" label="学生" width="120" />
        <el-table-column prop="courseName" label="课程" width="150" />
        <el-table-column prop="attendanceDate" label="日期" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="150" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑考勤' : '录入考勤'" width="500px">
      <el-form ref="formRef" :model="formData" label-width="80px">
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
        <el-form-item label="日期">
          <el-date-picker v-model="formData.attendanceDate" type="date" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="formData.status" style="width: 100%;">
            <el-option label="出勤" value="present" />
            <el-option label="缺勤" value="absent" />
            <el-option label="迟到" value="late" />
            <el-option label="请假" value="leave" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="formData.remark" type="textarea" />
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
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAllAttendances, addAttendance, updateAttendance, deleteAttendance } from '@/api/attendance'
import { getAllStudents } from '@/api/student'
import { getAllCourses } from '@/api/course'

const loading = ref(false)
const tableData = ref([])
const students = ref([])
const courses = ref([])
const filterStudentId = ref('')
const filterCourseId = ref('')
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const formData = reactive({
  id: null,
  studentId: '',
  courseId: '',
  attendanceDate: new Date(),
  status: 'present',
  remark: ''
})

const loadData = async () => {
  loading.value = true
  try {
    const res = await getAllAttendances()
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

const handleSearch = () => { loadData() }

const handleAdd = () => {
  isEdit.value = false
  formData.id = null
  formData.studentId = ''
  formData.courseId = ''
  formData.attendanceDate = new Date()
  formData.status = 'present'
  formData.remark = ''
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  formData.id = row.id
  formData.studentId = row.studentId
  formData.courseId = row.courseId
  formData.attendanceDate = row.attendanceDate
  formData.status = row.status
  formData.remark = row.remark
  dialogVisible.value = true
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定删除该考勤记录吗？', '提示', { type: 'warning' })
    await deleteAttendance(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') console.error('删除失败', error)
  }
}

const handleSubmit = async () => {
  try {
    if (isEdit.value) {
      await updateAttendance({ ...formData })
      ElMessage.success('更新成功')
    } else {
      await addAttendance({ ...formData })
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('提交失败', error)
  }
}

const getStatusType = (status) => {
  if (status === 'present') return 'success'
  if (status === 'absent') return 'danger'
  if (status === 'late') return 'warning'
  return ''
}

onMounted(() => {
  loadData()
  loadOptions()
})
</script>

<style scoped>
.search-bar { display: flex; gap: 10px; align-items: center; }
</style>
