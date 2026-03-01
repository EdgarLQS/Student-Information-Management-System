package com.student.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Course 实体类单元测试
 */
@DisplayName("Course 实体类测试")
class CourseTest {

    @Test
    @DisplayName("测试无参构造函数")
    void testNoArgsConstructor() {
        Course course = new Course();
        assertThat(course).isNotNull();
        assertThat(course.getCourseId()).isNull();
        assertThat(course.getCourseName()).isNull();
    }

    @Test
    @DisplayName("测试带参构造函数")
    void testParameterizedConstructor() {
        Course course = new Course("CS101", "Java 程序设计");
        assertThat(course.getCourseId()).isEqualTo("CS101");
        assertThat(course.getCourseName()).isEqualTo("Java 程序设计");
    }

    @Test
    @DisplayName("测试所有 getter 和 setter")
    void testGettersAndSetters() {
        Course course = new Course();

        course.setId(1);
        assertThat(course.getId()).isEqualTo(1);

        course.setCourseId("CS101");
        assertThat(course.getCourseId()).isEqualTo("CS101");

        course.setCourseName("Java 程序设计");
        assertThat(course.getCourseName()).isEqualTo("Java 程序设计");

        course.setCredit(4.0);
        assertThat(course.getCredit()).isEqualTo(4.0);

        course.setTeacher("王教授");
        assertThat(course.getTeacher()).isEqualTo("王教授");

        course.setClassHours(64);
        assertThat(course.getClassHours()).isEqualTo(64);

        java.util.Date date = new java.util.Date();
        course.setCreatedAt(date);
        assertThat(course.getCreatedAt()).isEqualTo(date);
    }
}
