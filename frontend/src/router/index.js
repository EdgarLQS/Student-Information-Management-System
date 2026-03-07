import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    component: () => import('@/layout/MainLayout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '概览' }
      },
      {
        path: '/students',
        name: 'StudentManagement',
        component: () => import('@/views/StudentManagement.vue'),
        meta: { title: '学生管理' }
      },
      {
        path: '/teachers',
        name: 'TeacherManagement',
        component: () => import('@/views/TeacherManagement.vue'),
        meta: { title: '教师管理' }
      },
      {
        path: '/courses',
        name: 'CourseManagement',
        component: () => import('@/views/CourseManagement.vue'),
        meta: { title: '课程管理' }
      },
      {
        path: '/scores',
        name: 'ScoreManagement',
        component: () => import('@/views/ScoreManagement.vue'),
        meta: { title: '成绩管理' }
      },
      {
        path: '/attendance',
        name: 'AttendanceManagement',
        component: () => import('@/views/AttendanceManagement.vue'),
        meta: { title: '考勤管理' }
      },
      {
        path: '/enrollments',
        name: 'EnrollmentManagement',
        component: () => import('@/views/EnrollmentManagement.vue'),
        meta: { title: '选课管理' }
      },
      {
        path: '/statistics',
        name: 'Statistics',
        component: () => import('@/views/Statistics.vue'),
        meta: { title: '统计分析' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
