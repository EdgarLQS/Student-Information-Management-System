package com.student.controller;

import com.student.model.Teacher;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * TeacherController 单元测试
 */
@DisplayName("TeacherController 测试")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TeacherControllerTest {

    private TeacherController controller;

    @BeforeEach
    void setUp() {
        controller = new TeacherController();
    }

    @Test
    @Order(1)
    @DisplayName("测试添加教师")
    void testAddTeacher() {
        // 创建测试教师
        Teacher teacher = new Teacher();
        teacher.setTeacherId("TESTT001");
        teacher.setName("测试教授");
        teacher.setGender("男");
        teacher.setTitle("讲师");
        teacher.setDepartment("计算机学院");
        teacher.setPhone("13900139000");
        teacher.setEmail("testprof@example.com");

        // 先尝试删除（如果已存在）
        Teacher existing = controller.getTeacherByTeacherId("TESTT001");
        if (existing != null) {
            controller.deleteTeacher(existing.getId());
        }

        // 添加教师
        boolean result = controller.addTeacher(teacher);
        assertThat(result).isTrue();

        // 验证添加成功
        Teacher addedTeacher = controller.getTeacherByTeacherId("TESTT001");
        assertThat(addedTeacher).isNotNull();
        assertThat(addedTeacher.getName()).isEqualTo("测试教授");
    }

    @Test
    @Order(2)
    @DisplayName("测试重复添加教师")
    void testAddDuplicateTeacher() {
        Teacher teacher = new Teacher("TESTT001", "重复教授");
        boolean result = controller.addTeacher(teacher);
        assertThat(result).isFalse();
    }

    @Test
    @Order(3)
    @DisplayName("测试获取所有教师")
    void testGetAllTeachers() {
        List<Teacher> teachers = controller.getAllTeachers();
        assertThat(teachers).isNotNull();
    }

    @Test
    @Order(4)
    @DisplayName("测试根据 ID 获取教师")
    void testGetTeacherById() {
        Teacher teacher = controller.getTeacherByTeacherId("TESTT001");
        if (teacher != null) {
            Teacher foundTeacher = controller.getTeacherById(teacher.getId());
            assertThat(foundTeacher).isNotNull();
            assertThat(foundTeacher.getId()).isEqualTo(teacher.getId());
        }
    }

    @Test
    @Order(5)
    @DisplayName("测试根据工号获取教师")
    void testGetTeacherByTeacherId() {
        Teacher teacher = controller.getTeacherByTeacherId("TESTT001");
        if (teacher != null) {
            assertThat(teacher.getTeacherId()).isEqualTo("TESTT001");
        }
    }

    @Test
    @Order(6)
    @DisplayName("测试更新教师")
    void testUpdateTeacher() {
        Teacher teacher = controller.getTeacherByTeacherId("TESTT001");
        if (teacher != null) {
            teacher.setName("更新后的教授");
            teacher.setTitle("副教授");

            boolean result = controller.updateTeacher(teacher);
            assertThat(result).isTrue();

            Teacher updatedTeacher = controller.getTeacherById(teacher.getId());
            assertThat(updatedTeacher.getName()).isEqualTo("更新后的教授");
            assertThat(updatedTeacher.getTitle()).isEqualTo("副教授");
        }
    }

    @Test
    @Order(7)
    @DisplayName("测试搜索教师")
    void testSearchTeachers() {
        // 搜索存在的关键字
        List<Teacher> results = controller.searchTeachers("测试");
        assertThat(results).isNotNull();

        // 搜索空关键字应该返回所有教师
        List<Teacher> allResults = controller.searchTeachers("");
        assertThat(allResults).isNotNull();

        // 搜索不存在的关键字
        List<Teacher> noResults = controller.searchTeachers("不存在的名字");
        assertThat(noResults).isEmpty();
    }

    @Test
    @Order(8)
    @DisplayName("测试搜索教师 null 关键字")
    void testSearchTeachersWithNullKeyword() {
        List<Teacher> results = controller.searchTeachers(null);
        assertThat(results).isNotNull();
    }

    @Test
    @Order(9)
    @DisplayName("测试分页获取教师")
    void testGetTeachersByPage() {
        List<Teacher> page1 = controller.getTeachersByPage(1, 5);
        assertThat(page1).isNotNull();
    }

    @Test
    @Order(10)
    @DisplayName("测试获取教师总数")
    void testGetTotalCount() {
        int count = controller.getTotalCount();
        assertThat(count).isGreaterThanOrEqualTo(0);
    }

    @Test
    @Order(11)
    @DisplayName("测试验证教师数据")
    void testValidateTeacher() {
        Teacher validTeacher = new Teacher("TESTT002", "验证教授");
        assertThat(controller.validateTeacher(validTeacher)).isTrue();

        Teacher nullTeacher = null;
        assertThat(controller.validateTeacher(nullTeacher)).isFalse();

        Teacher noIdTeacher = new Teacher("", "无名教授");
        assertThat(controller.validateTeacher(noIdTeacher)).isFalse();
    }

    @Test
    @Order(12)
    @DisplayName("测试删除教师")
    void testDeleteTeacher() {
        Teacher teacher = controller.getTeacherByTeacherId("TESTT001");
        if (teacher != null) {
            boolean result = controller.deleteTeacher(teacher.getId());
            assertThat(result).isTrue();

            Teacher deletedTeacher = controller.getTeacherByTeacherId("TESTT001");
            assertThat(deletedTeacher).isNull();
        }
    }
}
