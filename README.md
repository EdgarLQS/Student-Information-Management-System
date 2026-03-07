# 学生信息管理系统

一个功能完整的学生信息管理系统，支持 **Swing 桌面版** 和 **Web 版** 两种访问方式。

---

## 项目概述

本项目提供两种应用形态：

| 版本 | 技术栈 | 适用场景 |
|------|--------|----------|
| **Swing 桌面版** | Java 8 + SQLite + Swing | 单机桌面应用，无需额外配置 |
| **Web 版** | Spring Boot + Vue 3 + Element Plus | 前后端分离，支持浏览器访问 |

---

## 快速开始

### 方式一：Swing 桌面版（推荐新手）

```bash
# 编译项目
mvn clean compile

# 打包项目
mvn clean package

# 运行程序
mvn exec:java -Dexec.mainClass="com.student.StudentManagementApp"
# 或
java -jar target/student-management-system-1.0.0.jar
```

### 方式二：Web 版

#### 1. 启动后端（Spring Boot）
```bash
# 在项目根目录
mvn spring-boot:run
```
后端 API 地址：http://localhost:8080/api

#### 2. 启动前端（Vue 3）
```bash
cd frontend
npm install
npm run dev
```
前端访问地址：http://localhost:5173

---

## 环境要求

| 组件 | 要求 |
|------|------|
| JDK | 1.8 或更高版本 |
| Maven | 3.6+ |
| Node.js | 16+（仅 Web 版需要） |
| npm | 8+（仅 Web 版需要） |

---

## 功能模块

### 通用功能（两个版本都支持）

| 模块 | 功能描述 |
|------|----------|
| **学生管理** | 学生信息 CRUD、搜索、分页、数据导出 |
| **教师管理** | 教师信息 CRUD、搜索、数据导出 |
| **课程管理** | 课程信息 CRUD、搜索、数据导出 |
| **成绩管理** | 成绩录入、修改、删除、查询 |
| **考勤管理** | 考勤记录管理、状态标记、出勤率统计 |
| **选课管理** | 学生选课、退课、选课列表 |
| **统计分析** | 数据概览、图表展示（Web 版专属） |

### 版本差异

| 功能 | Swing 桌面版 | Web 版 |
|------|-------------|--------|
| 界面风格 | 经典 Swing | 现代化 Element Plus |
| 数据导出 | Excel 导出 | 开发中 |
| 统计图表 | JFreeChart | ECharts |
| 响应式布局 | 不支持 | 支持 |

---

## 技术架构

### Swing 桌面版

```
┌─────────────────────────────────────┐
│         View 层 (Swing UI)          │
│  - MainFrame                        │
│  - 各种 ManagementPanel             │
│  - FormDialog                       │
├─────────────────────────────────────┤
│       Controller 层 (业务逻辑)       │
│  - StudentController                │
│  - TeacherController                │
│  - CourseController                 │
│  - ScoreController                  │
│  - AttendanceController             │
│  - EnrollmentController             │
├─────────────────────────────────────┤
│         Model 层 (数据实体)          │
│  - Student, Teacher, Course         │
│  - Score, Attendance, Enrollment    │
├─────────────────────────────────────┤
│       DAO 层 (数据访问)              │
│  - DatabaseHelper (单例)            │
│  - 各种 DAO (接口 + 实现)            │
├─────────────────────────────────────┤
│       Util 层 (工具类)               │
│  - Validator (数据验证)              │
│  - ExcelExportUtil (Excel 导出)      │
└─────────────────────────────────────┘
```

### Web 版

```
┌─────────────────────────────────────┐
│         前端层 (Vue 3)              │
│  - Element Plus 组件                 │
│  - ECharts 图表                     │
│  - Pinia 状态管理                   │
│  - Vue Router 路由                  │
├─────────────────────────────────────┤
│         API 层 (Spring Boot)         │
│  - StudentApiController             │
│  - TeacherApiController             │
│  - CourseApiController              │
│  - ScoreApiController               │
│  - AttendanceApiController          │
│  - EnrollmentApiController          │
│  - StatisticsApiController          │
├─────────────────────────────────────┤
│         Controller 层 (复用)         │
│         DAO 层 (复用)                │
│         Model 层 (复用)              │
└─────────────────────────────────────┘
```

---

## API 端点（Web 版）

| 模块 | 端点前缀 | HTTP 方法 |
|------|----------|-----------|
| 学生管理 | `/api/students` | GET / POST / PUT / DELETE |
| 教师管理 | `/api/teachers` | GET / POST / PUT / DELETE |
| 课程管理 | `/api/courses` | GET / POST / PUT / DELETE |
| 成绩管理 | `/api/scores` | GET / POST / DELETE |
| 考勤管理 | `/api/attendances` | GET / POST / PUT / DELETE |
| 选课管理 | `/api/enrollments` | GET / POST / DELETE |
| 统计分析 | `/api/statistics` | GET |

### API 响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": { ... },
  "timestamp": 1772885439136
}
```

---

## 项目结构

```
student-management-system/
├── pom.xml                         # Maven 配置
├── README.md                       # 说明文档（本文件）
├── README_WEB.md                   # Web 版详细文档
├── 设计规划.md                      # 设计文档
├── src/
│   ├── main/
│   │   ├── java/com/student/
│   │   │   ├── StudentManagementApp.java   # Swing 入口
│   │   │   ├── StudentWebApp.java          # Web 入口
│   │   │   ├── model/                      # 实体类 (6 个)
│   │   │   ├── dao/                        # 数据访问层
│   │   │   ├── controller/                 # 业务逻辑层 (7 个)
│   │   │   ├── view/                       # Swing 界面
│   │   │   ├── api/                        # REST API 控制器 (7 个)
│   │   │   ├── common/                     # 公共类
│   │   │   ├── config/                     # 配置类
│   │   │   └── util/                       # 工具类
│   │   └── resources/
│   │       ├── application.properties      # Spring Boot 配置
│   │       └── db/                         # 数据库脚本
│   └── test/
│       └── java/com/student/               # 单元测试 (141 个)
├── frontend/                               # Vue 前端项目
│   ├── src/
│   │   ├── api/               # API 模块 (7 个)
│   │   ├── views/             # 页面视图 (8 个)
│   │   ├── stores/            # Pinia stores (3 个)
│   │   ├── router/            # 路由配置
│   │   ├── utils/             # 工具函数
│   │   ├── layout/            # 布局组件
│   │   ├── App.vue
│   │   └── main.js
│   ├── package.json
│   ├── vite.config.js
│   └── index.html
└── data/
    └── student.db                 # SQLite 数据库
```

---

## 数据库设计

数据库文件：`data/student.db`

| 表名 | 说明 | 核心字段 |
|------|------|----------|
| `students` | 学生信息表 | id, student_id, name, gender, age, class_name, phone, email |
| `teachers` | 教师信息表 | id, teacher_id, name, gender, title, department, phone, email |
| `courses` | 课程表 | id, course_id, course_name, teacher_id, credits, class_hours |
| `scores` | 成绩表 | id, student_id, course_id, score, term |
| `attendances` | 考勤表 | id, student_id, course_id, attendance_date, status, remark |
| `enrollments` | 选课表 | id, student_id, course_id, enroll_date, status |

---

## 测试

项目包含 **141 个单元测试**，覆盖 Model、DAO、Controller 和 Util 层。

```bash
# 运行所有测试
mvn test

# 生成覆盖率报告
mvn test jacoco:report

# 完整构建（包含覆盖率检查）
mvn clean verify
```

覆盖率要求：行 30%+, 分支 28%+（Swing GUI 代码除外）

覆盖率报告位置：`target/site/jacoco/index.html`

---

## 构建打包

### 后端打包
```bash
mvn clean package
# 生成 target/student-management-system-1.0.0.jar
```

### 前端打包
```bash
cd frontend
npm run build
# 生成 dist/ 目录
```

---

## 配置说明

### application.properties

```properties
# Spring Boot 应用配置
server.port=8080
spring.application.name=student-management-system

# 日志配置
logging.level.com.student=INFO
logging.level.org.springframework.web=INFO

# JSON 配置
spring.jackson.date-format=yyyy-MM-dd HH:mm:ss
spring.jackson.time-zone=GMT+8
```

### Vite 代理配置

```javascript
// frontend/vite.config.js
server: {
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true
    }
  }
}
```

---

## 注意事项

1. **端口配置**: 后端默认端口 8080，前端默认端口 5173
2. **数据库路径**: 保持 `data/student.db` 不变
3. **编码格式**: 统一使用 UTF-8（POST 请求需设置 charset=utf-8）
4. **Java 版本**: Java 8
5. **跨域问题**: 已配置 CORS，开发环境无跨域限制

---

## 技术亮点

1. **渐进式重构**: 保留原有 Swing 代码，新增 Web 前端，平滑过渡
2. **代码复用**: 复用现有 Controller、DAO、Model 层
3. **现代化前端**: Vue 3 + Element Plus + ECharts
4. **统一规范**: 统一的 API 响应格式和错误处理机制
5. **完整测试**: 141 个单元测试，覆盖率达标

---

## 后续扩展建议

- [ ] 用户登录认证功能
- [ ] 权限控制（基于角色）
- [ ] 数据导入（Excel 批量导入）
- [ ] 前端单元测试
- [ ] E2E 测试
- [ ] 更多统计图表和报表
- [ ] 数据库备份/恢复功能

---

## 相关文档

| 文档 | 说明 |
|------|------|
| [IMPLEMENTATION_SUMMARY.md](IMPLEMENTATION_SUMMARY.md) | **实施总结（2026-03-07）** - Vue Web 版完整实施记录 |
| [README_WEB.md](README_WEB.md) | Web 版详细实施文档 |
| [设计规划.md](设计规划.md) | 项目设计规划文档 |
| [frontend/README.md](frontend/README.md) | 前端项目说明 |

---

## 更新日志

| 日期 | 版本 | 更新内容 |
|------|------|----------|
| 2026-03-07 | 1.0.0 | Vue Web 版正式上线，完成 7 个 API 控制器 + 8 个前端页面 |
| 2026-03-01 | 0.9.0 | Swing 桌面版完成，141 个单元测试通过 |
