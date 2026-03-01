package com.student.controller;

import com.student.dao.EnrollmentDAO;
import com.student.dao.EnrollmentDAOImpl;
import com.student.model.Enrollment;
import com.student.model.Student;

import java.time.LocalDate;
import java.util.List;

/**
 * 选课控制器 - 业务逻辑层
 */
public class EnrollmentController {
    private final EnrollmentDAO enrollmentDAO;

    public EnrollmentController() {
        this.enrollmentDAO = new EnrollmentDAOImpl();
    }

    /**
     * 获取所有选课记录
     */
    public List<Enrollment> getAllEnrollments() {
        return enrollmentDAO.getAll();
    }

    /**
     * 根据学生 ID 获取选课记录
     */
    public List<Enrollment> getEnrollmentsByStudent(Integer studentId) {
        return enrollmentDAO.getByStudentId(studentId);
    }

    /**
     * 根据课程 ID 获取选课记录
     */
    public List<Enrollment> getEnrollmentsByCourse(Integer courseId) {
        return enrollmentDAO.getByCourseId(courseId);
    }

    /**
     * 学生选课
     */
    public boolean enrollCourse(Integer studentId, Integer courseId) {
        // 检查是否已选
        if (enrollmentDAO.exists(studentId, courseId)) {
            return false;
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(studentId);
        enrollment.setCourseId(courseId);
        enrollment.setEnrollDate(LocalDate.now());
        enrollment.setStatus("active");

        return enrollmentDAO.insert(enrollment);
    }

    /**
     * 退课
     */
    public boolean dropCourse(Integer enrollmentId) {
        return enrollmentDAO.delete(enrollmentId);
    }

    /**
     * 检查学生是否已选该课程
     */
    public boolean isEnrolled(Integer studentId, Integer courseId) {
        return enrollmentDAO.exists(studentId, courseId);
    }
}
