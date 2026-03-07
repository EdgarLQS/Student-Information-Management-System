import request from '@/utils/request'

export function getAllTeachers() {
  return request({
    url: '/teachers',
    method: 'get'
  })
}

export function getTeacherById(id) {
  return request({
    url: `/teachers/${id}`,
    method: 'get'
  })
}

export function getTeacherByTeacherId(teacherId) {
  return request({
    url: `/teachers/by-id/${teacherId}`,
    method: 'get'
  })
}

export function searchTeachers(keyword) {
  return request({
    url: '/teachers/search',
    method: 'get',
    params: { keyword }
  })
}

export function getTeachersByPage(page, size) {
  return request({
    url: '/teachers/page',
    method: 'get',
    params: { page, size }
  })
}

export function addTeacher(data) {
  return request({
    url: '/teachers',
    method: 'post',
    data
  })
}

export function updateTeacher(data) {
  return request({
    url: '/teachers',
    method: 'put',
    data
  })
}

export function deleteTeacher(id) {
  return request({
    url: `/teachers/${id}`,
    method: 'delete'
  })
}
