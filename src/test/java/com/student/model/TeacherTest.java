package com.student.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Teacher 实体类单元测试
 */
@DisplayName("Teacher 实体类测试")
class TeacherTest {

    @Test
    @DisplayName("测试无参构造函数")
    void testNoArgsConstructor() {
        Teacher teacher = new Teacher();
        assertThat(teacher).isNotNull();
        assertThat(teacher.getTeacherId()).isNull();
        assertThat(teacher.getName()).isNull();
    }

    @Test
    @DisplayName("测试带参构造函数")
    void testParameterizedConstructor() {
        Teacher teacher = new Teacher("T001", "王教授");
        assertThat(teacher.getTeacherId()).isEqualTo("T001");
        assertThat(teacher.getName()).isEqualTo("王教授");
    }

    @Test
    @DisplayName("测试所有 getter 和 setter")
    void testGettersAndSetters() {
        Teacher teacher = new Teacher();

        teacher.setId(1);
        assertThat(teacher.getId()).isEqualTo(1);

        teacher.setTeacherId("T001");
        assertThat(teacher.getTeacherId()).isEqualTo("T001");

        teacher.setName("王教授");
        assertThat(teacher.getName()).isEqualTo("王教授");

        teacher.setGender("男");
        assertThat(teacher.getGender()).isEqualTo("男");

        teacher.setTitle("教授");
        assertThat(teacher.getTitle()).isEqualTo("教授");

        teacher.setDepartment("计算机学院");
        assertThat(teacher.getDepartment()).isEqualTo("计算机学院");

        teacher.setPhone("13900139000");
        assertThat(teacher.getPhone()).isEqualTo("13900139000");

        teacher.setEmail("wang@example.com");
        assertThat(teacher.getEmail()).isEqualTo("wang@example.com");

        java.util.Date date = new java.util.Date();
        teacher.setCreatedAt(date);
        assertThat(teacher.getCreatedAt()).isEqualTo(date);

        teacher.setUpdatedAt(date);
        assertThat(teacher.getUpdatedAt()).isEqualTo(date);
    }
}
