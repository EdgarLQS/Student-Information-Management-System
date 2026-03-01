# 学生信息管理系统

基于 Java8 + SQLite + Swing 的学生信息管理系统

## 环境要求

- JDK 1.8 或更高版本
- Maven 3.6+

## 快速开始

### 方式一：使用 Maven 运行

```bash
# 编译项目
mvn clean compile

# 打包项目
mvn clean package

# 运行程序
mvn exec:java -Dexec.mainClass="com.student.StudentManagementApp"
```

### 方式二：直接运行 JAR

```bash
java -jar target/student-management-system-1.0.0.jar
```

## 功能特性

### 学生管理
- 学生列表展示（表格形式）
- 添加/修改/删除学生信息
- 搜索学生（按学号或姓名）
- 数据分页显示

### 教师管理
- 教师信息录入（工号、姓名、性别、职称、所属院系、电话、邮箱）
- 教师列表展示
- 教师信息修改、删除
- 按姓名/工号搜索

### 课程管理
- 课程信息录入
- 课程列表展示
- 课程信息修改、删除

### 成绩管理
- 成绩录入
- 成绩列表展示
- 成绩修改、删除

### 考勤管理
- 考勤记录录入（学生、课程、日期、状态、备注）
- 考勤状态：出勤、缺勤、迟到、请假
- 按学生/课程/日期范围查询
- 出勤率统计显示

### 数据导出
- 导出学生列表到 Excel
- 导出教师列表到 Excel
- 导出课程列表到 Excel
- 导出成绩列表到 Excel
- 导出考勤列表到 Excel

### 数据验证
- 学号/工号格式验证（6-20 位字母或数字）
- 邮箱格式验证
- 电话号码格式验证
- 年龄范围验证（1-150）
- 成绩范围验证（0-100）
- 学分范围验证（0-20）

## 技术栈

- **开发语言**: Java 8
- **数据库**: SQLite
- **界面框架**: Swing
- **项目结构**: Maven

## 项目结构

```
student-management-system/
├── pom.xml                    # Maven 配置
├── 设计规划.md                 # 设计文档
├── README.md                   # 说明文档
├── src/
│   ├── main/
│   │   ├── java/com/student/
│   │   │   ├── StudentManagementApp.java    # 主入口
│   │   │   ├── model/                       # 实体类
│   │   │   │   ├── Student.java
│   │   │   │   ├── Teacher.java
│   │   │   │   ├── Course.java
│   │   │   │   ├── Score.java
│   │   │   │   ├── Attendance.java
│   │   │   │   └── Enrollment.java
│   │   │   ├── dao/                         # 数据访问层
│   │   │   ├── controller/                  # 业务逻辑层
│   │   │   ├── view/                        # 界面层
│   │   │   └── util/                        # 工具类
│   │   └── resources/db/                    # 数据库脚本
│   └── test/
│       └── java/com/student/                # 单元测试
└── data/
    └── student.db                           # SQLite 数据库（运行时生成）
```

## 单元测试

项目包含 141 个单元测试，覆盖 Model、DAO、Controller 和 Util 层。

```bash
# 运行测试
mvn test

# 生成覆盖率报告
mvn test jacoco:report

# 完整构建（包含覆盖率检查）
mvn clean verify
```

覆盖率报告位置：`target/site/jacoco/index.html`

## 使用说明

### 访问各功能模块

1. **学生管理**: 菜单"学生 > 学生信息管理"
2. **教师管理**: 菜单"课程 > 教师信息管理"
3. **课程管理**: 菜单"课程 > 课程信息管理"
4. **成绩管理**: 菜单"成绩 > 成绩信息管理"
5. **考勤管理**: 菜单"考勤 > 考勤信息管理"

### 基本操作

1. 启动程序后，点击"添加"按钮录入新记录
2. 选中表格中的记录，点击"修改"可编辑信息
3. 选中表格中的记录，点击"删除"可删除记录
4. 在搜索框输入关键词，点击"搜索"进行查询
5. 点击"导出"按钮可将数据导出为 Excel 文件

## 数据库说明

数据库文件自动创建在项目目录的 `data/student.db` 路径下。

### 数据表结构

**学生表 (students)**:
- id, student_id, name, gender, age, class_name, phone, email

**教师表 (teachers)**:
- id, teacher_id, name, gender, title, department, phone, email

**课程表 (courses)**:
- id, course_id, course_name, teacher_id, teacher_name, credits, class_hours

**成绩表 (scores)**:
- id, student_id, student_name, course_id, course_name, score, term

**考勤表 (attendances)**:
- id, student_id, student_name, course_id, course_name, attendance_date, status, remark

**选课表 (enrollments)**:
- id, student_id, course_id, enroll_date, status
