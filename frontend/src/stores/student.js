import { defineStore } from 'pinia'
import { ref } from 'vue'
import * as studentApi from '@/api/student'

export const useStudentStore = defineStore('student', () => {
  const students = ref([])
  const total = ref(0)
  const loading = ref(false)

  async function fetchAllStudents() {
    loading.value = true
    try {
      const res = await studentApi.getAllStudents()
      students.value = res.data
      total.value = res.data.length
      return res.data
    } catch (error) {
      console.error('获取学生列表失败:', error)
      return []
    } finally {
      loading.value = false
    }
  }

  async function fetchStudentsByPage(page, size) {
    loading.value = true
    try {
      const res = await studentApi.getStudentsByPage(page, size)
      students.value = res.data.list
      total.value = res.data.total
      return res.data
    } catch (error) {
      console.error('获取学生列表失败:', error)
      return { list: [], total: 0 }
    } finally {
      loading.value = false
    }
  }

  async function searchStudents(keyword) {
    loading.value = true
    try {
      const res = await studentApi.searchStudents(keyword)
      students.value = res.data
      total.value = res.data.length
      return res.data
    } catch (error) {
      console.error('搜索学生失败:', error)
      return []
    } finally {
      loading.value = false
    }
  }

  async function addStudent(data) {
    return await studentApi.addStudent(data)
  }

  async function updateStudent(data) {
    return await studentApi.updateStudent(data)
  }

  async function deleteStudent(id) {
    return await studentApi.deleteStudent(id)
  }

  function clearStudents() {
    students.value = []
    total.value = 0
  }

  return {
    students,
    total,
    loading,
    fetchAllStudents,
    fetchStudentsByPage,
    searchStudents,
    addStudent,
    updateStudent,
    deleteStudent,
    clearStudents
  }
})
