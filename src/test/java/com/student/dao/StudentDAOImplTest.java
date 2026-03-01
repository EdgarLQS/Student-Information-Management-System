package com.student.dao;

import com.student.model.Student;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * StudentDAO 单元测试
 * 使用真实数据库进行测试
 */
@DisplayName("StudentDAO 测试")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudentDAOImplTest {

    private StudentDAO studentDAO;

    @BeforeEach
    void setUp() {
        studentDAO = new StudentDAOImpl();
    }

    @Test
    @Order(1)
    @DisplayName("测试插入学生")
    void testInsert() {
        Student student = new Student("DAO_TEST_001", "DAO 测试学生");
        student.setGender("男");
        student.setAge(20);
        student.setClassName("测试班");
        student.setPhone("13800138000");
        student.setEmail("daotest@example.com");

        // 先清理可能存在的测试数据
        Student existing = studentDAO.getByStudentId("DAO_TEST_001");
        if (existing != null) {
            studentDAO.delete(existing.getId());
        }

        boolean result = studentDAO.insert(student);
        assertThat(result).isTrue();

        Student savedStudent = studentDAO.getByStudentId("DAO_TEST_001");
        assertThat(savedStudent).isNotNull();
        assertThat(savedStudent.getName()).isEqualTo("DAO 测试学生");
    }

    @Test
    @Order(2)
    @DisplayName("测试获取所有学生")
    void testGetAll() {
        List<Student> students = studentDAO.getAll();
        assertThat(students).isNotNull();
        assertThat(students.size()).isGreaterThan(0);
    }

    @Test
    @Order(3)
    @DisplayName("测试根据 ID 获取学生")
    void testGetById() {
        Student student = studentDAO.getByStudentId("DAO_TEST_001");
        if (student != null) {
            Student foundStudent = studentDAO.getById(student.getId());
            assertThat(foundStudent).isNotNull();
            assertThat(foundStudent.getId()).isEqualTo(student.getId());
        }
    }

    @Test
    @Order(4)
    @DisplayName("测试根据学号获取学生")
    void testGetByStudentId() {
        Student student = studentDAO.getByStudentId("DAO_TEST_001");
        if (student != null) {
            assertThat(student.getStudentId()).isEqualTo("DAO_TEST_001");
            assertThat(student.getName()).isEqualTo("DAO 测试学生");
        }
    }

    @Test
    @Order(5)
    @DisplayName("测试更新学生")
    void testUpdate() {
        Student student = studentDAO.getByStudentId("DAO_TEST_001");
        if (student != null) {
            student.setName("DAO 更新后的学生");
            student.setAge(21);

            boolean result = studentDAO.update(student);
            assertThat(result).isTrue();

            Student updatedStudent = studentDAO.getById(student.getId());
            assertThat(updatedStudent.getName()).isEqualTo("DAO 更新后的学生");
            assertThat(updatedStudent.getAge()).isEqualTo(21);
        }
    }

    @Test
    @Order(6)
    @DisplayName("测试搜索学生")
    void testSearch() {
        // 搜索存在的关键字
        List<Student> results = studentDAO.search("DAO");
        assertThat(results).isNotNull();
        assertThat(results.size()).isGreaterThan(0);

        // 搜索不存在的关键字
        List<Student> noResults = studentDAO.search("不存在的名字 XYZ");
        assertThat(noResults).isEmpty();
    }

    @Test
    @Order(7)
    @DisplayName("测试分页获取学生")
    void testGetPage() {
        List<Student> page1 = studentDAO.getPage(1, 5);
        assertThat(page1).isNotNull();
        assertThat(page1.size()).isLessThanOrEqualTo(5);

        List<Student> page2 = studentDAO.getPage(2, 5);
        assertThat(page2).isNotNull();
    }

    @Test
    @Order(8)
    @DisplayName("测试获取学生总数")
    void testGetCount() {
        int count = studentDAO.getCount();
        assertThat(count).isGreaterThan(0);
    }

    @Test
    @Order(9)
    @DisplayName("测试删除学生")
    void testDelete() {
        Student student = studentDAO.getByStudentId("DAO_TEST_001");
        if (student != null) {
            boolean result = studentDAO.delete(student.getId());
            assertThat(result).isTrue();

            Student deletedStudent = studentDAO.getByStudentId("DAO_TEST_001");
            assertThat(deletedStudent).isNull();
        }
    }
}
