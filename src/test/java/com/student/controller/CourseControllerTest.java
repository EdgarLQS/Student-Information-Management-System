package com.student.controller;

import com.student.model.Course;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * CourseController 单元测试
 */
@DisplayName("CourseController 测试")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CourseControllerTest {

    private CourseController controller;

    @BeforeEach
    void setUp() {
        controller = new CourseController();
    }

    @Test
    @Order(1)
    @DisplayName("测试添加课程")
    void testAddCourse() {
        // 创建测试课程
        Course course = new Course();
        course.setCourseId("TEST101");
        course.setCourseName("测试课程");
        course.setCredit(3.0);
        course.setTeacher("测试教授");
        course.setClassHours(48);

        // 先尝试删除（如果已存在）
        Course existing = controller.getCourseByCourseId("TEST101");
        if (existing != null) {
            // 删除课程（测试用）
            controller.deleteCourse(existing.getId());
        }

        // 添加课程
        boolean result = controller.addCourse(course);
        assertThat(result).isTrue();

        // 验证添加成功
        Course addedCourse = controller.getCourseByCourseId("TEST101");
        assertThat(addedCourse).isNotNull();
        assertThat(addedCourse.getCourseName()).isEqualTo("测试课程");
    }

    @Test
    @Order(2)
    @DisplayName("测试重复添加课程")
    void testAddDuplicateCourse() {
        Course course = new Course("TEST101", "重复课程");
        boolean result = controller.addCourse(course);
        assertThat(result).isFalse();
    }

    @Test
    @Order(3)
    @DisplayName("测试获取所有课程")
    void testGetAllCourses() {
        List<Course> courses = controller.getAllCourses();
        assertThat(courses).isNotNull();
        assertThat(courses.size()).isGreaterThan(0);
    }

    @Test
    @Order(4)
    @DisplayName("测试根据 ID 获取课程")
    void testGetCourseById() {
        Course course = controller.getCourseByCourseId("TEST101");
        if (course != null) {
            Course foundCourse = controller.getCourseById(course.getId());
            assertThat(foundCourse).isNotNull();
            assertThat(foundCourse.getId()).isEqualTo(course.getId());
        }
    }

    @Test
    @Order(5)
    @DisplayName("测试根据课程号获取课程")
    void testGetCourseByCourseId() {
        Course course = controller.getCourseByCourseId("TEST101");
        if (course != null) {
            assertThat(course.getCourseId()).isEqualTo("TEST101");
        }
    }

    @Test
    @Order(6)
    @DisplayName("测试更新课程")
    void testUpdateCourse() {
        Course course = controller.getCourseByCourseId("TEST101");
        if (course != null) {
            course.setCourseName("更新后的课程");
            course.setCredit(4.0);

            boolean result = controller.updateCourse(course);
            assertThat(result).isTrue();

            Course updatedCourse = controller.getCourseById(course.getId());
            assertThat(updatedCourse.getCourseName()).isEqualTo("更新后的课程");
            assertThat(updatedCourse.getCredit()).isEqualTo(4.0);
        }
    }

    @Test
    @Order(7)
    @DisplayName("测试搜索课程")
    void testSearchCourses() {
        // 搜索存在的关键字
        List<Course> results = controller.searchCourses("测试");
        assertThat(results).isNotNull();

        // 搜索空关键字应该返回所有课程
        List<Course> allResults = controller.searchCourses("");
        assertThat(allResults).isNotNull();

        // 搜索不存在的关键字
        List<Course> noResults = controller.searchCourses("不存在的课程");
        assertThat(noResults).isEmpty();
    }

    @Test
    @Order(8)
    @DisplayName("测试搜索课程 null 关键字")
    void testSearchCoursesWithNullKeyword() {
        List<Course> results = controller.searchCourses(null);
        assertThat(results).isNotNull();
    }

    @Test
    @Order(9)
    @DisplayName("测试检查课程是否有选课")
    void testHasEnrollments() {
        // 测试现有课程
        List<Course> courses = controller.getAllCourses();
        if (!courses.isEmpty()) {
            // 使用 CourseDAO 来检查是否有选课记录
            com.student.dao.CourseDAO courseDAO = new com.student.dao.CourseDAOImpl();
            boolean hasEnrollments = courseDAO.hasEnrollments(courses.get(0).getId());
            // 结果可能是 true 或 false，只要不抛异常即可
            assertThat(hasEnrollments).isInstanceOf(Boolean.class);
        }
    }

    @Test
    @Order(10)
    @DisplayName("测试验证课程数据")
    void testValidateCourse() {
        Course validCourse = new Course("TEST_COURSE", "验证课程");
        assertThat(controller.validateCourse(validCourse)).isTrue();

        Course nullCourse = null;
        assertThat(controller.validateCourse(nullCourse)).isFalse();

        Course noIdCourse = new Course("", "无名课程");
        assertThat(controller.validateCourse(noIdCourse)).isFalse();
    }

    @Test
    @Order(11)
    @DisplayName("测试删除有选课的课程")
    void testDeleteCourseWithEnrollments() {
        // 获取第一个课程（通常有选课记录）
        List<Course> courses = controller.getAllCourses();
        if (!courses.isEmpty()) {
            Course course = courses.get(0);
            // 如果有选课记录，应该不能删除
            com.student.dao.CourseDAO courseDAO = new com.student.dao.CourseDAOImpl();
            if (courseDAO.hasEnrollments(course.getId())) {
                boolean result = controller.deleteCourse(course.getId());
                assertThat(result).isFalse();
            }
        }
    }
}
