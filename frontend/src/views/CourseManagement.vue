<template>
  <div class="course-management">
    <el-card>
      <div class="search-bar">
        <el-input
          v-model="searchKeyword"
          placeholder="请输入课程号或课程名称搜索"
          clearable
          style="width: 300px;"
          @keyup.enter="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="handleReset">重置</el-button>
        <el-button type="success" @click="handleAdd">添加课程</el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" border stripe style="width: 100%; margin-top: 20px;">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="courseId" label="课程号" width="150" />
        <el-table-column prop="courseName" label="课程名称" width="200" />
        <el-table-column prop="credit" label="学分" width="80" />
        <el-table-column prop="teacher" label="授课教师" width="120" />
        <el-table-column prop="classHours" label="学时" width="80" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑课程' : '添加课程'" width="500px" @close="handleDialogClose">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="80px">
        <el-form-item label="课程号" prop="courseId">
          <el-input v-model="formData.courseId" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="课程名称" prop="courseName">
          <el-input v-model="formData.courseName" />
        </el-form-item>
        <el-form-item label="学分" prop="credit">
          <el-input-number v-model="formData.credit" :min="0" :max="10" :step="0.5" />
        </el-form-item>
        <el-form-item label="授课教师" prop="teacher">
          <el-input v-model="formData.teacher" />
        </el-form-item>
        <el-form-item label="学时" prop="classHours">
          <el-input-number v-model="formData.classHours" :min="1" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAllCourses, addCourse, updateCourse, deleteCourse, searchCourses } from '@/api/course'

const loading = ref(false)
const submitting = ref(false)
const tableData = ref([])
const searchKeyword = ref('')
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const formData = reactive({
  id: null,
  courseId: '',
  courseName: '',
  credit: 2,
  teacher: '',
  classHours: 32
})

const formRules = {
  courseId: [{ required: true, message: '请输入课程号', trigger: 'blur' }],
  courseName: [{ required: true, message: '请输入课程名称', trigger: 'blur' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getAllCourses()
    tableData.value = res.data || []
  } catch (error) {
    console.error('加载数据失败', error)
  } finally {
    loading.value = false
  }
}

const handleSearch = async () => {
  if (!searchKeyword.value.trim()) { loadData(); return }
  loading.value = true
  try {
    const res = await searchCourses(searchKeyword.value)
    tableData.value = res.data || []
  } catch (error) {
    console.error('搜索失败', error)
  } finally {
    loading.value = false
  }
}

const handleReset = () => { searchKeyword.value = ''; loadData() }
const handleAdd = () => { isEdit.value = false; dialogVisible.value = true }

const handleEdit = (row) => {
  isEdit.value = true
  formData.id = row.id
  formData.courseId = row.courseId
  formData.courseName = row.courseName
  formData.credit = row.credit
  formData.teacher = row.teacher
  formData.classHours = row.classHours
  dialogVisible.value = true
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要删除课程"${row.courseName}"吗？`, '提示', { type: 'warning' })
    await deleteCourse(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') console.error('删除失败', error)
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    submitting.value = true
    try {
      if (isEdit.value) {
        await updateCourse({ ...formData })
        ElMessage.success('更新成功')
      } else {
        await addCourse({ ...formData })
        ElMessage.success('添加成功')
      }
      dialogVisible.value = false
      loadData()
    } catch (error) {
      console.error('提交失败', error)
    } finally {
      submitting.value = false
    }
  })
}

const handleDialogClose = () => { if (formRef.value) formRef.value.resetFields() }

onMounted(() => { loadData() })
</script>

<style scoped>
.search-bar { display: flex; gap: 10px; align-items: center; }
</style>
