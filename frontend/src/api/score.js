import request from '@/utils/request'

export function getAllScores() {
  return request({
    url: '/scores',
    method: 'get'
  })
}

export function getScoresByStudentId(studentId) {
  return request({
    url: `/scores/student/${studentId}`,
    method: 'get'
  })
}

export function getScoresByCourseId(courseId) {
  return request({
    url: `/scores/course/${courseId}`,
    method: 'get'
  })
}

export function addScore(data) {
  return request({
    url: '/scores',
    method: 'post',
    data
  })
}

export function updateScore(data) {
  return request({
    url: '/scores',
    method: 'put',
    data
  })
}

export function deleteScore(id) {
  return request({
    url: `/scores/${id}`,
    method: 'delete'
  })
}

// 保存成绩（兼容旧的调用方式）
export const saveScore = addScore

// 引入学生和选课 API（兼容旧的调用方式）
export function getAllStudents() {
  return request({
    url: '/scores/students',
    method: 'get'
  })
}

export function getEnrollmentsByStudent(studentId) {
  return request({
    url: `/scores/enrollments/${studentId}`,
    method: 'get'
  })
}
