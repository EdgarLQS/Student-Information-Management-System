import request from '@/utils/request'

export function getAllCourses() {
  return request({
    url: '/courses',
    method: 'get'
  })
}

export function getCourseById(id) {
  return request({
    url: `/courses/${id}`,
    method: 'get'
  })
}

export function getCoursesByPage(page, size) {
  return request({
    url: '/courses/page',
    method: 'get',
    params: { page, size }
  })
}

export function searchCourses(keyword) {
  return request({
    url: '/courses/search',
    method: 'get',
    params: { keyword }
  })
}

export function addCourse(data) {
  return request({
    url: '/courses',
    method: 'post',
    data
  })
}

export function updateCourse(data) {
  return request({
    url: '/courses',
    method: 'put',
    data
  })
}

export function deleteCourse(id) {
  return request({
    url: `/courses/${id}`,
    method: 'delete'
  })
}
