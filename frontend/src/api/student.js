import request from '@/utils/request'

/**
 * 获取所有学生
 */
export function getAllStudents() {
  return request({
    url: '/students',
    method: 'get'
  })
}

/**
 * 根据 ID 获取学生
 */
export function getStudentById(id) {
  return request({
    url: `/students/${id}`,
    method: 'get'
  })
}

/**
 * 根据学号获取学生
 */
export function getStudentByStudentId(studentId) {
  return request({
    url: `/students/by-id/${studentId}`,
    method: 'get'
  })
}

/**
 * 搜索学生
 */
export function searchStudents(keyword) {
  return request({
    url: '/students/search',
    method: 'get',
    params: { keyword }
  })
}

/**
 * 分页获取学生
 */
export function getStudentsByPage(page, size) {
  return request({
    url: '/students/page',
    method: 'get',
    params: { page, size }
  })
}

/**
 * 添加学生
 */
export function addStudent(data) {
  return request({
    url: '/students',
    method: 'post',
    data
  })
}

/**
 * 更新学生
 */
export function updateStudent(data) {
  return request({
    url: '/students',
    method: 'put',
    data
  })
}

/**
 * 删除学生
 */
export function deleteStudent(id) {
  return request({
    url: `/students/${id}`,
    method: 'delete'
  })
}
