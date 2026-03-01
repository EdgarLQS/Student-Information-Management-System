package com.student.dao;

import com.student.model.Enrollment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 选课数据访问实现类
 */
public class EnrollmentDAOImpl implements EnrollmentDAO {
    private static final Logger logger = LoggerFactory.getLogger(EnrollmentDAOImpl.class);

    private final DatabaseHelper dbHelper;

    public EnrollmentDAOImpl() {
        this.dbHelper = DatabaseHelper.getInstance();
    }

    @Override
    public List<Enrollment> getAll() {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT e.*, s.name as student_name, c.course_name " +
                     "FROM enrollments e " +
                     "JOIN students s ON e.student_id = s.id " +
                     "JOIN courses c ON e.course_id = c.id " +
                     "ORDER BY e.id DESC";

        try (Connection conn = dbHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                enrollments.add(mapResultSetToEnrollment(rs));
            }

        } catch (SQLException e) {
            logger.error("获取所有选课记录失败", e);
        }

        return enrollments;
    }

    @Override
    public List<Enrollment> getByStudentId(Integer studentId) {
        return getWithCourseInfo(studentId);
    }

    @Override
    public List<Enrollment> getByCourseId(Integer courseId) {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT e.*, s.name as student_name, c.course_name " +
                     "FROM enrollments e " +
                     "JOIN students s ON e.student_id = s.id " +
                     "JOIN courses c ON e.course_id = c.id " +
                     "WHERE e.course_id = ? AND e.status = 'active' " +
                     "ORDER BY e.id DESC";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    enrollments.add(mapResultSetToEnrollment(rs));
                }
            }

        } catch (SQLException e) {
            logger.error("根据课程 ID 获取选课记录失败，courseId={}", courseId, e);
        }

        return enrollments;
    }

    @Override
    public List<Enrollment> getWithCourseInfo(Integer studentId) {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT e.*, s.name as student_name, c.course_name, c.credit " +
                     "FROM enrollments e " +
                     "JOIN students s ON e.student_id = s.id " +
                     "JOIN courses c ON e.course_id = c.id " +
                     "WHERE e.student_id = ? AND e.status = 'active' " +
                     "ORDER BY e.id DESC";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    enrollments.add(mapResultSetToEnrollment(rs));
                }
            }

        } catch (SQLException e) {
            logger.error("获取学生选课记录失败，studentId={}", studentId, e);
        }

        return enrollments;
    }

    @Override
    public boolean insert(Enrollment enrollment) {
        String sql = "INSERT INTO enrollments (student_id, course_id, enroll_date, status) " +
                     "VALUES (?, ?, ?, ?)";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, enrollment.getStudentId());
            pstmt.setInt(2, enrollment.getCourseId());
            pstmt.setString(3, enrollment.getEnrollDate() != null ?
                enrollment.getEnrollDate().toString() : LocalDate.now().toString());
            pstmt.setString(4, enrollment.getStatus() != null ? enrollment.getStatus() : "active");

            int rows = pstmt.executeUpdate();
            logger.info("插入选课记录成功，studentId={}, courseId={}",
                enrollment.getStudentId(), enrollment.getCourseId());
            return rows > 0;

        } catch (SQLException e) {
            logger.error("插入选课记录失败", e);
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM enrollments WHERE id = ?";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int rows = pstmt.executeUpdate();
            logger.info("删除选课记录成功，id={}", id);
            return rows > 0;

        } catch (SQLException e) {
            logger.error("删除选课记录失败，id={}", id, e);
            return false;
        }
    }

    @Override
    public boolean exists(Integer studentId, Integer courseId) {
        String sql = "SELECT COUNT(*) FROM enrollments WHERE student_id = ? AND course_id = ? AND status = 'active'";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            pstmt.setInt(2, courseId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            logger.error("检查选课记录是否存在失败", e);
        }

        return false;
    }

    /**
     * 将 ResultSet 映射为 Enrollment 对象
     */
    private Enrollment mapResultSetToEnrollment(ResultSet rs) throws SQLException {
        Enrollment enrollment = new Enrollment();
        enrollment.setId(rs.getInt("id"));
        enrollment.setStudentId(rs.getInt("student_id"));
        enrollment.setCourseId(rs.getInt("course_id"));
        enrollment.setStudentName(rs.getString("student_name"));
        enrollment.setCourseName(rs.getString("course_name"));
        enrollment.setStatus(rs.getString("status"));

        String enrollDateStr = rs.getString("enroll_date");
        if (enrollDateStr != null) {
            enrollment.setEnrollDate(LocalDate.parse(enrollDateStr));
        }

        return enrollment;
    }
}
