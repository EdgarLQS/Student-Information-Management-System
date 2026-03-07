import request from '@/utils/request'

export function getAllAttendances() {
  return request({
    url: '/attendances',
    method: 'get'
  })
}

export function getAttendancesByStudentId(studentId) {
  return request({
    url: `/attendances/student/${studentId}`,
    method: 'get'
  })
}

export function getAttendancesByDate(date) {
  return request({
    url: '/attendances/date',
    method: 'get',
    params: { date }
  })
}

export function addAttendance(data) {
  return request({
    url: '/attendances',
    method: 'post',
    data
  })
}

export function updateAttendance(data) {
  return request({
    url: '/attendances',
    method: 'put',
    data
  })
}

export function deleteAttendance(id) {
  return request({
    url: `/attendances/${id}`,
    method: 'delete'
  })
}
