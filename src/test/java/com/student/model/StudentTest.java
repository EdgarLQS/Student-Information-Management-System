package com.student.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Student 实体类单元测试
 */
@DisplayName("Student 实体类测试")
class StudentTest {

    @Test
    @DisplayName("测试无参构造函数")
    void testNoArgsConstructor() {
        Student student = new Student();
        assertThat(student).isNotNull();
        assertThat(student.getId()).isNull();
        assertThat(student.getStudentId()).isNull();
        assertThat(student.getName()).isNull();
    }

    @Test
    @DisplayName("测试带参构造函数")
    void testParameterizedConstructor() {
        Student student = new Student("2024001", "张三");
        assertThat(student.getStudentId()).isEqualTo("2024001");
        assertThat(student.getName()).isEqualTo("张三");
    }

    @Test
    @DisplayName("测试所有 getter 和 setter")
    void testGettersAndSetters() {
        Student student = new Student();

        student.setId(1);
        assertThat(student.getId()).isEqualTo(1);

        student.setStudentId("2024001");
        assertThat(student.getStudentId()).isEqualTo("2024001");

        student.setName("张三");
        assertThat(student.getName()).isEqualTo("张三");

        student.setGender("男");
        assertThat(student.getGender()).isEqualTo("男");

        student.setAge(20);
        assertThat(student.getAge()).isEqualTo(20);

        student.setClassName("计算机 1 班");
        assertThat(student.getClassName()).isEqualTo("计算机 1 班");

        student.setPhone("13800138000");
        assertThat(student.getPhone()).isEqualTo("13800138000");

        student.setEmail("zhangsan@example.com");
        assertThat(student.getEmail()).isEqualTo("zhangsan@example.com");

        java.util.Date date = new java.util.Date();
        student.setCreatedAt(date);
        assertThat(student.getCreatedAt()).isEqualTo(date);

        student.setUpdatedAt(date);
        assertThat(student.getUpdatedAt()).isEqualTo(date);
    }

    @Test
    @DisplayName("测试 null 值设置")
    void testNullValues() {
        Student student = new Student();
        student.setGender(null);
        student.setAge(null);
        student.setClassName(null);
        student.setPhone(null);
        student.setEmail(null);

        assertThat(student.getGender()).isNull();
        assertThat(student.getAge()).isNull();
        assertThat(student.getClassName()).isNull();
        assertThat(student.getPhone()).isNull();
        assertThat(student.getEmail()).isNull();
    }
}
