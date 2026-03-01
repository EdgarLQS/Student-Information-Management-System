package com.student.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Enrollment 实体类单元测试
 */
@DisplayName("Enrollment 实体类测试")
class EnrollmentTest {

    @Test
    @DisplayName("测试无参构造函数")
    void testNoArgsConstructor() {
        Enrollment enrollment = new Enrollment();
        assertThat(enrollment).isNotNull();
        assertThat(enrollment.getStudentId()).isNull();
    }

    @Test
    @DisplayName("测试带参构造函数")
    void testParameterizedConstructor() {
        Enrollment enrollment = new Enrollment(1, 1);
        assertThat(enrollment.getStudentId()).isEqualTo(1);
        assertThat(enrollment.getCourseId()).isEqualTo(1);
    }

    @Test
    @DisplayName("测试所有 getter 和 setter")
    void testGettersAndSetters() {
        Enrollment enrollment = new Enrollment();

        enrollment.setId(1);
        assertThat(enrollment.getId()).isEqualTo(1);

        enrollment.setStudentId(1);
        assertThat(enrollment.getStudentId()).isEqualTo(1);

        enrollment.setCourseId(1);
        assertThat(enrollment.getCourseId()).isEqualTo(1);

        enrollment.setStudentName("张三");
        assertThat(enrollment.getStudentName()).isEqualTo("张三");

        enrollment.setCourseName("Java 程序设计");
        assertThat(enrollment.getCourseName()).isEqualTo("Java 程序设计");

        LocalDate date = LocalDate.now();
        enrollment.setEnrollDate(date);
        assertThat(enrollment.getEnrollDate()).isEqualTo(date);

        enrollment.setStatus("active");
        assertThat(enrollment.getStatus()).isEqualTo("active");
    }
}
