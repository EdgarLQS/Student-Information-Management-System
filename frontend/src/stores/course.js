import { defineStore } from 'pinia'
import { ref } from 'vue'
import * as courseApi from '@/api/course'

export const useCourseStore = defineStore('course', () => {
  const courses = ref([])
  const total = ref(0)
  const loading = ref(false)

  async function fetchAllCourses() {
    loading.value = true
    try {
      const res = await courseApi.getAllCourses()
      courses.value = res.data
      total.value = res.data.length
      return res.data
    } catch (error) {
      console.error('获取课程列表失败:', error)
      return []
    } finally {
      loading.value = false
    }
  }

  async function fetchCoursesByPage(page, size) {
    loading.value = true
    try {
      const res = await courseApi.getCoursesByPage(page, size)
      courses.value = res.data.list
      total.value = res.data.total
      return res.data
    } catch (error) {
      console.error('获取课程列表失败:', error)
      return { list: [], total: 0 }
    } finally {
      loading.value = false
    }
  }

  async function searchCourses(keyword) {
    loading.value = true
    try {
      const res = await courseApi.searchCourses(keyword)
      courses.value = res.data
      total.value = res.data.length
      return res.data
    } catch (error) {
      console.error('搜索课程失败:', error)
      return []
    } finally {
      loading.value = false
    }
  }

  async function addCourse(data) {
    return await courseApi.addCourse(data)
  }

  async function updateCourse(data) {
    return await courseApi.updateCourse(data)
  }

  async function deleteCourse(id) {
    return await courseApi.deleteCourse(id)
  }

  function clearCourses() {
    courses.value = []
    total.value = 0
  }

  return {
    courses,
    total,
    loading,
    fetchAllCourses,
    fetchCoursesByPage,
    searchCourses,
    addCourse,
    updateCourse,
    deleteCourse,
    clearCourses
  }
})
