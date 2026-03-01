package com.student.dao;

import com.student.model.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 课程数据访问实现类
 */
public class CourseDAOImpl implements CourseDAO {
    private static final Logger logger = LoggerFactory.getLogger(CourseDAOImpl.class);

    private final DatabaseHelper dbHelper;

    public CourseDAOImpl() {
        this.dbHelper = DatabaseHelper.getInstance();
    }

    @Override
    public List<Course> getAll() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses ORDER BY id DESC";

        try (Connection conn = dbHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                courses.add(mapResultSetToCourse(rs));
            }

        } catch (SQLException e) {
            logger.error("获取所有课程失败", e);
        }

        return courses;
    }

    @Override
    public Course getById(Integer id) {
        String sql = "SELECT * FROM courses WHERE id = ?";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCourse(rs);
                }
            }

        } catch (SQLException e) {
            logger.error("根据 ID 获取课程失败，id={}", id, e);
        }

        return null;
    }

    @Override
    public Course getByCourseId(String courseId) {
        String sql = "SELECT * FROM courses WHERE course_id = ?";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, courseId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCourse(rs);
                }
            }

        } catch (SQLException e) {
            logger.error("根据课程号获取课程失败，courseId={}", courseId, e);
        }

        return null;
    }

    @Override
    public boolean insert(Course course) {
        String sql = "INSERT INTO courses (course_id, course_name, credit, teacher, class_hours) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, course.getCourseId());
            pstmt.setString(2, course.getCourseName());
            pstmt.setDouble(3, course.getCredit() != null ? course.getCredit() : 0.0);
            pstmt.setString(4, course.getTeacher());
            pstmt.setInt(5, course.getClassHours() != null ? course.getClassHours() : 0);

            int rows = pstmt.executeUpdate();
            logger.info("插入课程成功，courseId={}", course.getCourseId());
            return rows > 0;

        } catch (SQLException e) {
            logger.error("插入课程失败，courseId={}", course.getCourseId(), e);
            return false;
        }
    }

    @Override
    public boolean update(Course course) {
        String sql = "UPDATE courses SET " +
                     "course_id = ?, course_name = ?, credit = ?, " +
                     "teacher = ?, class_hours = ?, " +
                     "created_at = CURRENT_TIMESTAMP " +
                     "WHERE id = ?";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, course.getCourseId());
            pstmt.setString(2, course.getCourseName());
            pstmt.setDouble(3, course.getCredit() != null ? course.getCredit() : 0.0);
            pstmt.setString(4, course.getTeacher());
            pstmt.setInt(5, course.getClassHours() != null ? course.getClassHours() : 0);
            pstmt.setInt(6, course.getId());

            int rows = pstmt.executeUpdate();
            logger.info("更新课程成功，id={}", course.getId());
            return rows > 0;

        } catch (SQLException e) {
            logger.error("更新课程失败，id={}", course.getId(), e);
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM courses WHERE id = ?";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int rows = pstmt.executeUpdate();
            logger.info("删除课程成功，id={}", id);
            return rows > 0;

        } catch (SQLException e) {
            logger.error("删除课程失败，id={}", id, e);
            return false;
        }
    }

    @Override
    public List<Course> search(String keyword) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses WHERE course_id LIKE ? OR course_name LIKE ? ORDER BY id DESC";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    courses.add(mapResultSetToCourse(rs));
                }
            }

        } catch (SQLException e) {
            logger.error("搜索课程失败，keyword={}", keyword, e);
        }

        return courses;
    }

    @Override
    public boolean exists(String courseId) {
        String sql = "SELECT COUNT(*) FROM courses WHERE course_id = ?";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, courseId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            logger.error("检查课程号是否存在失败", e);
        }

        return false;
    }

    @Override
    public boolean hasEnrollments(Integer courseId) {
        String sql = "SELECT COUNT(*) FROM enrollments WHERE course_id = ?";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            logger.error("检查课程是否有选课失败", e);
        }

        return false;
    }

    /**
     * 将 ResultSet 映射为 Course 对象
     */
    private Course mapResultSetToCourse(ResultSet rs) throws SQLException {
        Course course = new Course();
        course.setId(rs.getInt("id"));
        course.setCourseId(rs.getString("course_id"));
        course.setCourseName(rs.getString("course_name"));
        course.setCredit(rs.getDouble("credit"));
        course.setTeacher(rs.getString("teacher"));
        course.setClassHours(rs.getInt("class_hours"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            course.setCreatedAt(new Date(createdAt.getTime()));
        }

        return course;
    }
}
