package com.student.controller;

import com.student.model.Student;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * StudentController 单元测试
 * 使用真实数据库进行测试（内存数据库）
 */
@DisplayName("StudentController 测试")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudentControllerTest {

    private StudentController controller;
    private static boolean databaseInitialized = false;

    @BeforeEach
    void setUp() {
        controller = new StudentController();
    }

    @Test
    @Order(1)
    @DisplayName("测试添加学生")
    void testAddStudent() {
        // 创建测试学生
        Student student = new Student();
        student.setStudentId("TEST001");
        student.setName("测试学生");
        student.setGender("男");
        student.setAge(20);
        student.setClassName("测试班");
        student.setPhone("13800138000");
        student.setEmail("test@example.com");

        // 先尝试删除（如果已存在）
        Student existing = controller.getStudentByStudentId("TEST001");
        if (existing != null) {
            controller.deleteStudent(existing.getId());
        }

        // 添加学生
        boolean result = controller.addStudent(student);
        assertThat(result).isTrue();

        // 验证添加成功
        Student addedStudent = controller.getStudentByStudentId("TEST001");
        assertThat(addedStudent).isNotNull();
        assertThat(addedStudent.getName()).isEqualTo("测试学生");
    }

    @Test
    @Order(2)
    @DisplayName("测试重复添加学生")
    void testAddDuplicateStudent() {
        Student student = new Student("TEST001", "重复学生");
        boolean result = controller.addStudent(student);
        assertThat(result).isFalse();
    }

    @Test
    @Order(3)
    @DisplayName("测试获取所有学生")
    void testGetAllStudents() {
        List<Student> students = controller.getAllStudents();
        assertThat(students).isNotNull();
        assertThat(students.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    @Order(4)
    @DisplayName("测试根据 ID 获取学生")
    void testGetStudentById() {
        Student student = controller.getStudentByStudentId("TEST001");
        if (student != null) {
            Student foundStudent = controller.getStudentById(student.getId());
            assertThat(foundStudent).isNotNull();
            assertThat(foundStudent.getId()).isEqualTo(student.getId());
        }
    }

    @Test
    @Order(5)
    @DisplayName("测试根据学号获取学生")
    void testGetStudentByStudentId() {
        Student student = controller.getStudentByStudentId("TEST001");
        if (student != null) {
            assertThat(student.getStudentId()).isEqualTo("TEST001");
        }
    }

    @Test
    @Order(6)
    @DisplayName("测试更新学生")
    void testUpdateStudent() {
        Student student = controller.getStudentByStudentId("TEST001");
        if (student != null) {
            student.setName("更新后的学生");
            student.setAge(21);

            boolean result = controller.updateStudent(student);
            assertThat(result).isTrue();

            Student updatedStudent = controller.getStudentById(student.getId());
            assertThat(updatedStudent.getName()).isEqualTo("更新后的学生");
            assertThat(updatedStudent.getAge()).isEqualTo(21);
        }
    }

    @Test
    @Order(7)
    @DisplayName("测试搜索学生")
    void testSearchStudents() {
        // 搜索存在的学生
        List<Student> results = controller.searchStudents("测试");
        assertThat(results).isNotNull();

        // 搜索空关键字应该返回所有学生
        List<Student> allResults = controller.searchStudents("");
        assertThat(allResults).isNotNull();
        assertThat(allResults.size()).isGreaterThanOrEqualTo(results.size());

        // 搜索不存在的关键字
        List<Student> noResults = controller.searchStudents("不存在的名字");
        assertThat(noResults).isEmpty();
    }

    @Test
    @Order(8)
    @DisplayName("测试搜索学生 null 关键字")
    void testSearchStudentsWithNullKeyword() {
        List<Student> results = controller.searchStudents(null);
        assertThat(results).isNotNull();
    }

    @Test
    @Order(9)
    @DisplayName("测试分页获取学生")
    void testGetStudentsByPage() {
        List<Student> page1 = controller.getStudentsByPage(1, 5);
        assertThat(page1).isNotNull();
        assertThat(page1.size()).isLessThanOrEqualTo(5);
    }

    @Test
    @Order(10)
    @DisplayName("测试获取学生总数")
    void testGetTotalCount() {
        int count = controller.getTotalCount();
        assertThat(count).isGreaterThan(0);
    }

    @Test
    @Order(11)
    @DisplayName("测试验证学生数据")
    void testValidateStudent() {
        Student validStudent = new Student("TEST002", "验证学生");
        assertThat(controller.validateStudent(validStudent)).isTrue();

        Student nullStudent = null;
        assertThat(controller.validateStudent(nullStudent)).isFalse();

        Student noIdStudent = new Student("", "无名学生");
        assertThat(controller.validateStudent(noIdStudent)).isFalse();
    }

    @Test
    @Order(12)
    @DisplayName("测试删除学生")
    void testDeleteStudent() {
        Student student = controller.getStudentByStudentId("TEST001");
        if (student != null) {
            boolean result = controller.deleteStudent(student.getId());
            assertThat(result).isTrue();

            Student deletedStudent = controller.getStudentByStudentId("TEST001");
            assertThat(deletedStudent).isNull();
        }
    }
}
