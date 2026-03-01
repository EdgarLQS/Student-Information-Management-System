package com.student.controller;

import com.student.model.Score;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ScoreController 单元测试
 */
@DisplayName("ScoreController 测试")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ScoreControllerTest {

    private ScoreController controller;
    private CourseController courseController;

    @BeforeEach
    void setUp() {
        controller = new ScoreController();
        courseController = new CourseController();
    }

    @Test
    @Order(1)
    @DisplayName("测试获取所有成绩")
    void testGetAllScores() {
        List<Score> scores = controller.getAllScores();
        assertThat(scores).isNotNull();
    }

    @Test
    @Order(2)
    @DisplayName("测试获取所有学生")
    void testGetAllStudents() {
        List<com.student.model.Student> students = controller.getAllStudents();
        assertThat(students).isNotNull();
        assertThat(students.size()).isGreaterThan(0);
    }

    @Test
    @Order(3)
    @DisplayName("测试获取所有课程")
    void testGetAllCourses() {
        List<com.student.model.Course> courses = courseController.getAllCourses();
        assertThat(courses).isNotNull();
        assertThat(courses.size()).isGreaterThan(0);
    }

    @Test
    @Order(4)
    @DisplayName("测试根据学生 ID 获取成绩")
    void testGetScoresByStudent() {
        // 获取第一个学生
        List<com.student.model.Student> students = controller.getAllStudents();
        if (!students.isEmpty()) {
            Integer studentId = students.get(0).getId();
            List<Score> scores = controller.getScoresByStudent(studentId);
            assertThat(scores).isNotNull();
        }
    }

    @Test
    @Order(5)
    @DisplayName("测试根据课程 ID 获取成绩")
    void testGetScoresByCourse() {
        // 获取第一个课程
        List<com.student.model.Course> courses = courseController.getAllCourses();
        if (!courses.isEmpty()) {
            Integer courseId = courses.get(0).getId();
            List<Score> scores = controller.getScoresByCourse(courseId);
            assertThat(scores).isNotNull();
        }
    }

    @Test
    @Order(6)
    @DisplayName("测试获取课程统计信息")
    void testGetCourseStatistics() {
        // 获取第一个课程
        List<com.student.model.Course> courses = courseController.getAllCourses();
        if (!courses.isEmpty()) {
            Integer courseId = courses.get(0).getId();
            java.util.Map<String, Object> stats = controller.getCourseStatistics(courseId);
            assertThat(stats).isNotNull();
        }
    }

    @Test
    @Order(7)
    @DisplayName("测试保存成绩 - 有效分数")
    void testSaveScore_Valid() {
        // 获取第一条选课记录
        List<com.student.model.Enrollment> enrollments = controller.getEnrollmentsByStudent(1);
        if (!enrollments.isEmpty()) {
            Integer enrollmentId = enrollments.get(0).getId();
            boolean result = controller.saveScore(enrollmentId, 85.0);
            assertThat(result).isTrue();
        }
    }

    @Test
    @Order(8)
    @DisplayName("测试保存成绩 - 无效分数")
    void testSaveScore_Invalid() {
        List<com.student.model.Enrollment> enrollments = controller.getEnrollmentsByStudent(1);
        if (!enrollments.isEmpty()) {
            Integer enrollmentId = enrollments.get(0).getId();
            boolean result = controller.saveScore(enrollmentId, 150.0);
            assertThat(result).isFalse();
        }
    }

    @Test
    @Order(9)
    @DisplayName("测试获取成绩分布")
    void testGetScoreDistribution() {
        // 获取第一个课程
        List<com.student.model.Course> courses = courseController.getAllCourses();
        if (!courses.isEmpty()) {
            Integer courseId = courses.get(0).getId();
            java.util.Map<String, Integer> dist = controller.getScoreDistribution(courseId);
            assertThat(dist).isNotNull();
        }
    }

    @Test
    @Order(10)
    @DisplayName("测试获取学生选课记录")
    void testGetEnrollmentsByStudent() {
        List<com.student.model.Enrollment> enrollments = controller.getEnrollmentsByStudent(1);
        assertThat(enrollments).isNotNull();
    }

    @Test
    @Order(11)
    @DisplayName("测试获取课程平均分")
    void testGetCourseAverageScores() {
        java.util.Map<String, Double> averages = controller.getCourseAverageScores();
        assertThat(averages).isNotNull();
    }

    @Test
    @Order(12)
    @DisplayName("测试获取学生成绩雷达图数据")
    void testGetStudentScores() {
        java.util.Map<String, Double> scores = controller.getStudentScores(1);
        assertThat(scores).isNotNull();
    }

    @Test
    @Order(13)
    @DisplayName("测试验证成绩 - 有效")
    void testValidateScore_Valid() {
        assertThat(controller.validateScore(85.0)).isTrue();
        assertThat(controller.validateScore(0.0)).isTrue();
        assertThat(controller.validateScore(100.0)).isTrue();
    }

    @Test
    @Order(14)
    @DisplayName("测试验证成绩 - 无效")
    void testValidateScore_Invalid() {
        assertThat(controller.validateScore(-1.0)).isFalse();
        assertThat(controller.validateScore(100.1)).isFalse();
        assertThat(controller.validateScore(null)).isFalse();
    }
}
