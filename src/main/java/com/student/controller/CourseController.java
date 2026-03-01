package com.student.controller;

import com.student.dao.CourseDAO;
import com.student.dao.CourseDAOImpl;
import com.student.dao.EnrollmentDAO;
import com.student.dao.EnrollmentDAOImpl;
import com.student.model.Course;
import com.student.model.Enrollment;

import java.util.List;

/**
 * 课程控制器 - 业务逻辑层
 */
public class CourseController {
    private final CourseDAO courseDAO;
    private final EnrollmentDAO enrollmentDAO;

    public CourseController() {
        this.courseDAO = new CourseDAOImpl();
        this.enrollmentDAO = new EnrollmentDAOImpl();
    }

    /**
     * 获取所有课程
     */
    public List<Course> getAllCourses() {
        return courseDAO.getAll();
    }

    /**
     * 根据 ID 获取课程
     */
    public Course getCourseById(Integer id) {
        return courseDAO.getById(id);
    }

    /**
     * 根据课程号获取课程
     */
    public Course getCourseByCourseId(String courseId) {
        return courseDAO.getByCourseId(courseId);
    }

    /**
     * 添加课程
     */
    public boolean addCourse(Course course) {
        // 验证课程号是否已存在
        if (courseDAO.exists(course.getCourseId())) {
            return false;
        }
        return courseDAO.insert(course);
    }

    /**
     * 更新课程
     */
    public boolean updateCourse(Course course) {
        return courseDAO.update(course);
    }

    /**
     * 删除课程
     */
    public boolean deleteCourse(Integer id) {
        // 检查是否有学生选修
        if (courseDAO.hasEnrollments(id)) {
            return false; // 有选课时不能删除
        }
        return courseDAO.delete(id);
    }

    /**
     * 搜索课程
     */
    public List<Course> searchCourses(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllCourses();
        }
        return courseDAO.search(keyword);
    }

    /**
     * 验证课程数据
     */
    public boolean validateCourse(Course course) {
        if (course == null) {
            return false;
        }

        // 课程号不能为空
        if (course.getCourseId() == null || course.getCourseId().trim().isEmpty()) {
            return false;
        }

        // 课程名称不能为空
        if (course.getCourseName() == null || course.getCourseName().trim().isEmpty()) {
            return false;
        }

        // 学分验证
        if (course.getCredit() != null && (course.getCredit() < 0 || course.getCredit() > 10)) {
            return false;
        }

        return true;
    }

    /**
     * 获取课程的所有选课学生
     */
    public List<Enrollment> getEnrollmentsByCourse(Integer courseId) {
        return enrollmentDAO.getByCourseId(courseId);
    }
}
