# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 常用命令

```bash
# 编译项目
mvn clean compile

# 打包项目（生成可执行 JAR）
mvn clean package

# 运行程序
mvn exec:java -Dexec.mainClass="com.student.StudentManagementApp"
# 或
java -jar target/student-management-system-1.0.0.jar

# 运行所有测试
mvn test

# 运行单个测试类
mvn test -Dtest=StudentControllerTest

# 生成覆盖率报告
mvn test jacoco:report

# 完整构建（包含覆盖率检查）
mvn clean verify
```

## 代码架构

### 分层架构
- **model** - 数据实体层（Student, Teacher, Course, Score, Attendance, Enrollment）
- **dao** - 数据访问层（接口 + 实现，使用 SQLite）
- **controller** - 业务逻辑层
- **view** - Swing 界面层（MainFrame, 各种 ManagementPanel 和 FormDialog）
- **util** - 工具类（Validator 数据验证，ExcelExportUtil Excel 导出）

### 核心组件
- `StudentManagementApp.java` - 应用入口
- `MainFrame.java` - 主窗口，包含菜单导航
- `DatabaseHelper.java` - 数据库连接管理（单例模式）
- `Validator.java` - 输入数据验证（学号、邮箱、电话等格式）

### 数据库
- SQLite 数据库位于 `data/student.db`
- 6 个核心表：students, teachers, courses, scores, attendances, enrollments
- 数据库脚本在 `src/main/resources/db/`

### 测试
- 测试框架：JUnit 5 + Mockito + AssertJ
- 测试目录：`src/test/java/com/student/`
- 141 个测试用例，覆盖 Model、DAO、Controller、Util 层
- 覆盖率要求：行 30%+, 分支 28%+（Swing GUI 代码除外）

## Git 提交

使用 `/commit 提交信息` 技能快速提交代码到 GitHub。
