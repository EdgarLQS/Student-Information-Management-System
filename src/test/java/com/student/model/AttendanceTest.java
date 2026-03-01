package com.student.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Attendance 实体类单元测试
 */
@DisplayName("Attendance 实体类测试")
class AttendanceTest {

    @Test
    @DisplayName("测试无参构造函数")
    void testNoArgsConstructor() {
        Attendance attendance = new Attendance();
        assertThat(attendance).isNotNull();
        assertThat(attendance.getStudentId()).isNull();
    }

    @Test
    @DisplayName("测试带参构造函数")
    void testParameterizedConstructor() {
        java.util.Date date = new java.util.Date();
        Attendance attendance = new Attendance(1, 1, date, "出勤");
        assertThat(attendance.getStudentId()).isEqualTo(1);
        assertThat(attendance.getCourseId()).isEqualTo(1);
        assertThat(attendance.getStatus()).isEqualTo("出勤");
    }

    @Test
    @DisplayName("测试所有 getter 和 setter")
    void testGettersAndSetters() {
        Attendance attendance = new Attendance();

        attendance.setId(1);
        assertThat(attendance.getId()).isEqualTo(1);

        attendance.setStudentId(1);
        assertThat(attendance.getStudentId()).isEqualTo(1);

        attendance.setStudentName("张三");
        assertThat(attendance.getStudentName()).isEqualTo("张三");

        attendance.setCourseId(1);
        assertThat(attendance.getCourseId()).isEqualTo(1);

        attendance.setCourseName("Java 程序设计");
        assertThat(attendance.getCourseName()).isEqualTo("Java 程序设计");

        java.util.Date date = new java.util.Date();
        attendance.setAttendanceDate(date);
        assertThat(attendance.getAttendanceDate()).isEqualTo(date);

        attendance.setStatus("出勤");
        assertThat(attendance.getStatus()).isEqualTo("出勤");

        attendance.setRemark("正常");
        assertThat(attendance.getRemark()).isEqualTo("正常");

        attendance.setCreatedAt(date);
        assertThat(attendance.getCreatedAt()).isEqualTo(date);

        attendance.setUpdatedAt(date);
        assertThat(attendance.getUpdatedAt()).isEqualTo(date);
    }
}
