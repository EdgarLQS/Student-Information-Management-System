package com.student.controller;

import com.student.model.Attendance;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * AttendanceController 单元测试
 */
@DisplayName("AttendanceController 测试")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AttendanceControllerTest {

    private AttendanceController controller;

    @BeforeEach
    void setUp() {
        controller = new AttendanceController();
    }

    @Test
    @Order(1)
    @DisplayName("测试获取所有考勤记录")
    void testGetAllAttendances() {
        List<Attendance> attendances = controller.getAllAttendances();
        assertThat(attendances).isNotNull();
    }

    @Test
    @Order(2)
    @DisplayName("测试根据学生 ID 获取考勤记录")
    void testGetAttendancesByStudent() {
        // 获取第一个学生
        com.student.controller.StudentController studentController = new com.student.controller.StudentController();
        List<com.student.model.Student> students = studentController.getAllStudents();
        if (!students.isEmpty()) {
            Integer studentId = students.get(0).getId();
            List<Attendance> attendances = controller.getAttendancesByStudent(studentId);
            assertThat(attendances).isNotNull();
        }
    }

    @Test
    @Order(3)
    @DisplayName("测试根据课程 ID 获取考勤记录")
    void testGetAttendancesByCourse() {
        // 获取第一个课程
        com.student.controller.CourseController courseController = new com.student.controller.CourseController();
        List<com.student.model.Course> courses = courseController.getAllCourses();
        if (!courses.isEmpty()) {
            Integer courseId = courses.get(0).getId();
            List<Attendance> attendances = controller.getAttendancesByCourse(courseId);
            assertThat(attendances).isNotNull();
        }
    }

    @Test
    @Order(4)
    @DisplayName("测试根据日期范围获取考勤记录")
    void testGetAttendancesByDateRange() {
        java.sql.Date startDate = new java.sql.Date(System.currentTimeMillis() - 86400000L * 30);
        java.sql.Date endDate = new java.sql.Date(System.currentTimeMillis());
        List<Attendance> attendances = controller.getAttendancesByDateRange(startDate, endDate);
        assertThat(attendances).isNotNull();
    }

    @Test
    @Order(5)
    @DisplayName("测试获取学生考勤统计")
    void testGetStudentStatistics() {
        // 获取第一个学生
        com.student.controller.StudentController studentController = new com.student.controller.StudentController();
        List<com.student.model.Student> students = studentController.getAllStudents();
        if (!students.isEmpty()) {
            Integer studentId = students.get(0).getId();
            java.util.Map<String, Object> stats = controller.getStudentStatistics(studentId);
            assertThat(stats).isNotNull();
            // 验证统计信息包含必要的键
            assertThat(stats).containsKeys("total", "present", "absent", "late", "leave", "attendanceRate");
        }
    }

    @Test
    @Order(6)
    @DisplayName("测试获取课程考勤统计")
    void testGetCourseStatistics() {
        // 获取第一个课程
        com.student.controller.CourseController courseController = new com.student.controller.CourseController();
        List<com.student.model.Course> courses = courseController.getAllCourses();
        if (!courses.isEmpty()) {
            Integer courseId = courses.get(0).getId();
            java.util.Map<String, Object> stats = controller.getCourseStatistics(courseId);
            assertThat(stats).isNotNull();
            assertThat(stats).containsKeys("total", "present", "absent", "attendanceRate");
        }
    }

    @Test
    @Order(7)
    @DisplayName("测试添加考勤记录")
    void testAddAttendance() {
        // 获取第一个学生和课程
        com.student.controller.StudentController studentController = new com.student.controller.StudentController();
        com.student.controller.CourseController courseController = new com.student.controller.CourseController();
        List<com.student.model.Student> students = studentController.getAllStudents();
        List<com.student.model.Course> courses = courseController.getAllCourses();

        if (!students.isEmpty() && !courses.isEmpty()) {
            Attendance attendance = new Attendance();
            attendance.setStudentId(students.get(0).getId());
            attendance.setCourseId(courses.get(0).getId());
            attendance.setAttendanceDate(new Date());
            attendance.setStatus("出勤");
            attendance.setRemark("测试考勤");

            boolean result = controller.addAttendance(attendance);
            assertThat(result).isTrue();
        }
    }

    @Test
    @Order(8)
    @DisplayName("测试更新考勤记录")
    void testUpdateAttendance() {
        // 获取第一条考勤记录
        List<Attendance> attendances = controller.getAllAttendances();
        if (!attendances.isEmpty()) {
            Attendance attendance = attendances.get(0);
            attendance.setStatus("迟到");
            attendance.setRemark("更新后的备注");

            boolean result = controller.updateAttendance(attendance);
            assertThat(result).isTrue();

            // 恢复原始状态
            attendance.setStatus("出勤");
            attendance.setRemark("");
            controller.updateAttendance(attendance);
        }
    }

    @Test
    @Order(9)
    @DisplayName("测试删除考勤记录")
    void testDeleteAttendance() {
        // 先添加一条测试记录
        com.student.controller.StudentController studentController = new com.student.controller.StudentController();
        com.student.controller.CourseController courseController = new com.student.controller.CourseController();
        List<com.student.model.Student> students = studentController.getAllStudents();
        List<com.student.model.Course> courses = courseController.getAllCourses();

        if (!students.isEmpty() && !courses.isEmpty()) {
            Attendance attendance = new Attendance();
            attendance.setStudentId(students.get(0).getId());
            attendance.setCourseId(courses.get(0).getId());
            attendance.setAttendanceDate(new Date());
            attendance.setStatus("出勤");

            controller.addAttendance(attendance);

            // 获取刚添加的记录并删除
            List<Attendance> attendances = controller.getAllAttendances();
            Attendance toDelete = attendances.stream()
                    .filter(a -> "出勤".equals(a.getStatus()))
                    .findFirst()
                    .orElse(null);

            if (toDelete != null) {
                boolean result = controller.deleteAttendance(toDelete.getId());
                assertThat(result).isTrue();
            }
        }
    }
}
