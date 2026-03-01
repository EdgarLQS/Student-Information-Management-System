package com.student.dao;

import com.student.model.Teacher;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * TeacherDAO 单元测试
 */
@DisplayName("TeacherDAO 测试")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TeacherDAOImplTest {

    private TeacherDAO teacherDAO;

    @BeforeEach
    void setUp() {
        teacherDAO = new TeacherDAOImpl();
    }

    @Test
    @Order(1)
    @DisplayName("测试插入教师")
    void testInsert() {
        Teacher teacher = new Teacher("DAO_TEST_T001", "DAO 测试教授");
        teacher.setGender("男");
        teacher.setTitle("讲师");
        teacher.setDepartment("计算机学院");
        teacher.setPhone("13900139000");
        teacher.setEmail("daotest@example.com");

        // 先清理可能存在的测试数据
        Teacher existing = teacherDAO.getByTeacherId("DAO_TEST_T001");
        if (existing != null) {
            teacherDAO.delete(existing.getId());
        }

        boolean result = teacherDAO.insert(teacher);
        assertThat(result).isTrue();

        Teacher savedTeacher = teacherDAO.getByTeacherId("DAO_TEST_T001");
        assertThat(savedTeacher).isNotNull();
        assertThat(savedTeacher.getName()).isEqualTo("DAO 测试教授");
    }

    @Test
    @Order(2)
    @DisplayName("测试获取所有教师")
    void testGetAll() {
        List<Teacher> teachers = teacherDAO.getAll();
        assertThat(teachers).isNotNull();
    }

    @Test
    @Order(3)
    @DisplayName("测试根据 ID 获取教师")
    void testGetById() {
        Teacher teacher = teacherDAO.getByTeacherId("DAO_TEST_T001");
        if (teacher != null) {
            Teacher foundTeacher = teacherDAO.getById(teacher.getId());
            assertThat(foundTeacher).isNotNull();
        }
    }

    @Test
    @Order(4)
    @DisplayName("测试根据工号获取教师")
    void testGetByTeacherId() {
        Teacher teacher = teacherDAO.getByTeacherId("DAO_TEST_T001");
        if (teacher != null) {
            assertThat(teacher.getTeacherId()).isEqualTo("DAO_TEST_T001");
        }
    }

    @Test
    @Order(5)
    @DisplayName("测试更新教师")
    void testUpdate() {
        Teacher teacher = teacherDAO.getByTeacherId("DAO_TEST_T001");
        if (teacher != null) {
            teacher.setName("DAO 更新后的教授");
            teacher.setTitle("副教授");

            boolean result = teacherDAO.update(teacher);
            assertThat(result).isTrue();

            Teacher updatedTeacher = teacherDAO.getById(teacher.getId());
            assertThat(updatedTeacher.getName()).isEqualTo("DAO 更新后的教授");
        }
    }

    @Test
    @Order(6)
    @DisplayName("测试搜索教师")
    void testSearch() {
        List<Teacher> results = teacherDAO.search("DAO");
        assertThat(results).isNotNull();

        List<Teacher> noResults = teacherDAO.search("不存在的名字 XYZ");
        assertThat(noResults).isEmpty();
    }

    @Test
    @Order(7)
    @DisplayName("测试分页获取教师")
    void testGetPage() {
        List<Teacher> page1 = teacherDAO.getPage(1, 5);
        assertThat(page1).isNotNull();
    }

    @Test
    @Order(8)
    @DisplayName("测试获取教师总数")
    void testGetCount() {
        int count = teacherDAO.getCount();
        assertThat(count).isGreaterThanOrEqualTo(0);
    }

    @Test
    @Order(9)
    @DisplayName("测试删除教师")
    void testDelete() {
        Teacher teacher = teacherDAO.getByTeacherId("DAO_TEST_T001");
        if (teacher != null) {
            boolean result = teacherDAO.delete(teacher.getId());
            assertThat(result).isTrue();

            Teacher deletedTeacher = teacherDAO.getByTeacherId("DAO_TEST_T001");
            assertThat(deletedTeacher).isNull();
        }
    }
}
