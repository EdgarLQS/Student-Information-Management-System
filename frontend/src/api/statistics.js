import request from '@/utils/request'

export function getOverview() {
  return request({
    url: '/statistics/overview',
    method: 'get'
  })
}

export function getStudentCountByClass() {
  return request({
    url: '/statistics/student-count-by-class',
    method: 'get'
  })
}

export function getScoreDistribution(courseId) {
  return request({
    url: `/statistics/score-distribution/${courseId}`,
    method: 'get'
  })
}

export function getCourseAverageScores() {
  return request({
    url: '/statistics/course-average-scores',
    method: 'get'
  })
}

export function getStudentScores(studentId) {
  return request({
    url: `/statistics/student-scores/${studentId}`,
    method: 'get'
  })
}

export function getCourseStatistics(courseId) {
  return request({
    url: `/statistics/course/${courseId}`,
    method: 'get'
  })
}
