# 学生信息管理系统 - Vue 3 前端

基于 Vue 3 + Element Plus 的学生信息管理系统前端界面。

## 技术栈

- **框架**: Vue 3 (Composition API)
- **UI 组件库**: Element Plus
- **状态管理**: Pinia
- **路由管理**: Vue Router 4
- **HTTP 客户端**: Axios
- **图表库**: ECharts
- **构建工具**: Vite
- **CSS 预处理器**: Sass

## 项目结构

```
frontend/
├── public/                 # 静态资源
├── src/
│   ├── api/               # API 接口模块
│   │   ├── student.js     # 学生管理 API
│   │   ├── teacher.js     # 教师管理 API
│   │   ├── course.js      # 课程管理 API
│   │   ├── score.js       # 成绩管理 API
│   │   ├── attendance.js  # 考勤管理 API
│   │   ├── enrollment.js  # 选课管理 API
│   │   └── statistics.js  # 统计 API
│   ├── assets/            # 静态资源
│   ├── components/        # 公共组件
│   ├── views/             # 页面视图
│   │   ├── Dashboard.vue           # 概览页面
│   │   ├── StudentManagement.vue   # 学生管理
│   │   ├── TeacherManagement.vue   # 教师管理
│   │   ├── CourseManagement.vue    # 课程管理
│   │   ├── ScoreManagement.vue     # 成绩管理
│   │   ├── AttendanceManagement.vue # 考勤管理
│   │   ├── EnrollmentManagement.vue # 选课管理
│   │   └── Statistics.vue          # 统计分析
│   ├── stores/            # Pinia 状态管理
│   │   ├── student.js
│   │   ├── teacher.js
│   │   └── course.js
│   ├── router/            # 路由配置
│   │   └── index.js
│   ├── utils/             # 工具函数
│   │   └── request.js     # Axios 封装
│   ├── layout/            # 布局组件
│   │   └── MainLayout.vue
│   ├── App.vue            # 根组件
│   └── main.js            # 入口文件
├── package.json           # 项目依赖
├── vite.config.js         # Vite 配置
└── index.html             # HTML 模板
```

## 快速开始

### 1. 安装依赖

```bash
cd frontend
npm install
```

### 2. 启动后端服务

确保后端 Spring Boot 应用已启动，运行在 `http://localhost:8080`

```bash
# 在项目根目录
mvn spring-boot:run
```

### 3. 启动前端开发服务器

```bash
cd frontend
npm run dev
```

访问 http://localhost:5173

## 构建

```bash
npm run build
```

构建产物将输出到 `dist` 目录。

## 功能模块

| 模块 | 路由 | 功能描述 |
|------|------|----------|
| 概览 | /dashboard | 数据统计概览，图表展示 |
| 学生管理 | /students | 学生信息 CRUD |
| 教师管理 | /teachers | 教师信息 CRUD |
| 课程管理 | /courses | 课程信息 CRUD |
| 成绩管理 | /scores | 成绩录入与查询 |
| 考勤管理 | /attendance | 考勤记录管理 |
| 选课管理 | /enrollments | 学生选课管理 |
| 统计分析 | /statistics | 数据统计图表 |

## API 代理配置

开发环境下，通过 Vite 代理将 `/api` 请求转发到后端：

```js
// vite.config.js
server: {
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true
    }
  }
}
```

## 注意事项

1. 确保后端服务已启动并可访问
2. 数据库文件位于 `data/student.db`
3. 如遇跨域问题，检查后端 CORS 配置
4. 端口占用时 Vite 会自动使用下一个可用端口（5174, 5175...）

---

## 相关文档

| 文档 | 说明 |
|------|------|
| [../IMPLEMENTATION_SUMMARY.md](../IMPLEMENTATION_SUMMARY.md) | **实施总结（2026-03-07）** - 完整实施记录 |
| [../README.md](../README.md) | 项目主文档 |
| [../README_WEB.md](../README_WEB.md) | Web 版详细文档 |

---

## 更新日志

| 日期 | 版本 | 更新内容 |
|------|------|----------|
| 2026-03-07 | 1.0.0 | Vue 前端正式上线，8 个页面组件全部完成 |
| 2026-03-07 | 0.1.0 | 前端项目初始化完成 |
