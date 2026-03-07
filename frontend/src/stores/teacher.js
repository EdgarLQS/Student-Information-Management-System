import { defineStore } from 'pinia'
import { ref } from 'vue'
import * as teacherApi from '@/api/teacher'

export const useTeacherStore = defineStore('teacher', () => {
  const teachers = ref([])
  const total = ref(0)
  const loading = ref(false)

  async function fetchAllTeachers() {
    loading.value = true
    try {
      const res = await teacherApi.getAllTeachers()
      teachers.value = res.data
      total.value = res.data.length
      return res.data
    } catch (error) {
      console.error('获取教师列表失败:', error)
      return []
    } finally {
      loading.value = false
    }
  }

  async function fetchTeachersByPage(page, size) {
    loading.value = true
    try {
      const res = await teacherApi.getTeachersByPage(page, size)
      teachers.value = res.data.list
      total.value = res.data.total
      return res.data
    } catch (error) {
      console.error('获取教师列表失败:', error)
      return { list: [], total: 0 }
    } finally {
      loading.value = false
    }
  }

  async function searchTeachers(keyword) {
    loading.value = true
    try {
      const res = await teacherApi.searchTeachers(keyword)
      teachers.value = res.data
      total.value = res.data.length
      return res.data
    } catch (error) {
      console.error('搜索教师失败:', error)
      return []
    } finally {
      loading.value = false
    }
  }

  async function addTeacher(data) {
    return await teacherApi.addTeacher(data)
  }

  async function updateTeacher(data) {
    return await teacherApi.updateTeacher(data)
  }

  async function deleteTeacher(id) {
    return await teacherApi.deleteTeacher(id)
  }

  function clearTeachers() {
    teachers.value = []
    total.value = 0
  }

  return {
    teachers,
    total,
    loading,
    fetchAllTeachers,
    fetchTeachersByPage,
    searchTeachers,
    addTeacher,
    updateTeacher,
    deleteTeacher,
    clearTeachers
  }
})
