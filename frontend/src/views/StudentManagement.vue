<template>
  <div class="student-management">
    <el-card>
      <!-- 搜索栏 -->
      <div class="search-bar">
        <el-input
          v-model="searchKeyword"
          placeholder="请输入学号或姓名搜索"
          clearable
          style="width: 300px;"
          @keyup.enter="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        <el-button type="primary" @click="handleSearch">
          <el-icon><Search /></el-icon>
          搜索
        </el-button>
        <el-button @click="handleReset">
          <el-icon><Refresh /></el-icon>
          重置
        </el-button>
        <el-button type="success" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          添加学生
        </el-button>
        <el-button type="warning" @click="handleExport">
          <el-icon><Download /></el-icon>
          导出 Excel
        </el-button>
      </div>

      <!-- 表格 -->
      <el-table
        :data="tableData"
        v-loading="loading"
        border
        stripe
        style="width: 100%; margin-top: 20px;"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="studentId" label="学号" width="150" />
        <el-table-column prop="name" label="姓名" width="120" />
        <el-table-column prop="gender" label="性别" width="80">
          <template #default="{ row }">
            <el-tag :type="row.gender === '男' ? 'primary' : 'danger'">
              {{ row.gender }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="age" label="年龄" width="80" />
        <el-table-column prop="className" label="班级" width="150" />
        <el-table-column prop="phone" label="电话" width="150" />
        <el-table-column prop="email" label="邮箱" min-width="200" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>

    <!-- 添加/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑学生' : '添加学生'"
      width="500px"
      @close="handleDialogClose"
    >
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="80px">
        <el-form-item label="学号" prop="studentId">
          <el-input v-model="formData.studentId" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="formData.name" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="formData.gender" style="width: 100%;">
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
          </el-select>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input-number v-model="formData.age" :min="1" :max="150" />
        </el-form-item>
        <el-form-item label="班级" prop="className">
          <el-input v-model="formData.className" />
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="formData.phone" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="formData.email" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAllStudents, addStudent, updateStudent, deleteStudent, searchStudents } from '@/api/student'

const loading = ref(false)
const submitting = ref(false)
const tableData = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const searchKeyword = ref('')
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const formData = reactive({
  id: null,
  studentId: '',
  name: '',
  gender: '男',
  age: 18,
  className: '',
  phone: '',
  email: ''
})

const formRules = {
  studentId: [{ required: true, message: '请输入学号', trigger: 'blur' }],
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  age: [{ required: true, message: '请输入年龄', trigger: 'blur' }],
  email: [{ type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getAllStudents()
    tableData.value = res.data || []
    total.value = tableData.value.length
  } catch (error) {
    console.error('加载数据失败', error)
  } finally {
    loading.value = false
  }
}

const handleSearch = async () => {
  if (!searchKeyword.value.trim()) {
    loadData()
    return
  }
  loading.value = true
  try {
    const res = await searchStudents(searchKeyword.value)
    tableData.value = res.data || []
    total.value = tableData.value.length
  } catch (error) {
    console.error('搜索失败', error)
  } finally {
    loading.value = false
  }
}

const handleReset = () => {
  searchKeyword.value = ''
  loadData()
}

const handleAdd = () => {
  isEdit.value = false
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  formData.id = row.id
  formData.studentId = row.studentId
  formData.name = row.name
  formData.gender = row.gender
  formData.age = row.age
  formData.className = row.className
  formData.phone = row.phone
  formData.email = row.email
  dialogVisible.value = true
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要删除学生"${row.name}"吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteStudent(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败', error)
    }
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (!valid) return

    submitting.value = true
    try {
      if (isEdit.value) {
        await updateStudent({ ...formData })
        ElMessage.success('更新成功')
      } else {
        await addStudent({ ...formData })
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

const handleDialogClose = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

const handleSizeChange = () => {
  loadData()
}

const handlePageChange = () => {
  loadData()
}

const handleExport = () => {
  ElMessage.info('导出功能开发中...')
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.student-management {
  padding: 0;
}

.search-bar {
  display: flex;
  gap: 10px;
  align-items: center;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
