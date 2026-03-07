import request from '@/utils/request'

export function getAllEnrollments() {
  return request({
    url: '/enrollments',
    method: 'get'
  })
}

export function getEnrollmentsByStudentId(studentId) {
  return request({
    url: `/enrollments/student/${studentId}`,
    method: 'get'
  })
}

export function getEnrollmentsByCourseId(courseId) {
  return request({
    url: `/enrollments/course/${courseId}`,
    method: 'get'
  })
}

export function addEnrollment(data) {
  return request({
    url: '/enrollments',
    method: 'post',
    data
  })
}

export function deleteEnrollment(id) {
  return request({
    url: `/enrollments/${id}`,
    method: 'delete'
  })
}

// 别名函数，兼容旧的调用方式
export const getEnrollmentsByStudent = getEnrollmentsByStudentId
export const getEnrollmentsByCourse = getEnrollmentsByCourseId
export const enrollCourse = addEnrollment
export const dropCourse = deleteEnrollment
