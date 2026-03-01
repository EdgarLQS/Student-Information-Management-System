package com.student.dao;

import com.student.model.Score;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 成绩数据访问实现类
 */
public class ScoreDAOImpl implements ScoreDAO {
    private static final Logger logger = LoggerFactory.getLogger(ScoreDAOImpl.class);

    private final DatabaseHelper dbHelper;

    public ScoreDAOImpl() {
        this.dbHelper = DatabaseHelper.getInstance();
    }

    @Override
    public List<Score> getAll() {
        List<Score> scores = new ArrayList<>();
        String sql = "SELECT s.*, e.student_id, e.course_id, " +
                     "st.name as student_name, c.course_name " +
                     "FROM scores s " +
                     "JOIN enrollments e ON s.enrollment_id = e.id " +
                     "JOIN students st ON e.student_id = st.id " +
                     "JOIN courses c ON e.course_id = c.id " +
                     "ORDER BY s.id DESC";

        try (Connection conn = dbHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                scores.add(mapResultSetToScore(rs));
            }

        } catch (SQLException e) {
            logger.error("获取所有成绩记录失败", e);
        }

        return scores;
    }

    @Override
    public List<Score> getByStudentId(Integer studentId) {
        return getWithCourseInfo(studentId);
    }

    @Override
    public List<Score> getByCourseId(Integer courseId) {
        return getWithStudentInfo(courseId);
    }

    @Override
    public Score getByEnrollmentId(Integer enrollmentId) {
        String sql = "SELECT s.*, e.student_id, e.course_id, " +
                     "st.name as student_name, c.course_name " +
                     "FROM scores s " +
                     "JOIN enrollments e ON s.enrollment_id = e.id " +
                     "JOIN students st ON e.student_id = st.id " +
                     "JOIN courses c ON e.course_id = c.id " +
                     "WHERE s.enrollment_id = ?";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, enrollmentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToScore(rs);
                }
            }

        } catch (SQLException e) {
            logger.error("根据选课 ID 获取成绩失败，enrollmentId={}", enrollmentId, e);
        }

        return null;
    }

    @Override
    public List<Score> getWithCourseInfo(Integer studentId) {
        List<Score> scores = new ArrayList<>();
        String sql = "SELECT s.*, e.student_id, e.course_id, " +
                     "st.name as student_name, c.course_name " +
                     "FROM scores s " +
                     "JOIN enrollments e ON s.enrollment_id = e.id " +
                     "JOIN students st ON e.student_id = st.id " +
                     "JOIN courses c ON e.course_id = c.id " +
                     "WHERE e.student_id = ? " +
                     "ORDER BY c.course_id";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    scores.add(mapResultSetToScore(rs));
                }
            }

        } catch (SQLException e) {
            logger.error("获取学生成绩失败，studentId={}", studentId, e);
        }

        return scores;
    }

    @Override
    public List<Score> getWithStudentInfo(Integer courseId) {
        List<Score> scores = new ArrayList<>();
        String sql = "SELECT s.*, e.student_id, e.course_id, " +
                     "st.name as student_name, c.course_name " +
                     "FROM scores s " +
                     "JOIN enrollments e ON s.enrollment_id = e.id " +
                     "JOIN students st ON e.student_id = st.id " +
                     "JOIN courses c ON e.course_id = c.id " +
                     "WHERE e.course_id = ? " +
                     "ORDER BY st.name";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    scores.add(mapResultSetToScore(rs));
                }
            }

        } catch (SQLException e) {
            logger.error("获取课程成绩失败，courseId={}", courseId, e);
        }

        return scores;
    }

    @Override
    public boolean insertOrUpdate(Integer enrollmentId, Double score) {
        if (exists(enrollmentId)) {
            return updateScore(enrollmentId, score);
        } else {
            return insertScore(enrollmentId, score);
        }
    }

    private boolean insertScore(Integer enrollmentId, Double score) {
        String sql = "INSERT INTO scores (enrollment_id, score) VALUES (?, ?)";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, enrollmentId);
            pstmt.setDouble(2, score);

            int rows = pstmt.executeUpdate();
            logger.info("插入成绩成功，enrollmentId={}, score={}", enrollmentId, score);
            return rows > 0;

        } catch (SQLException e) {
            logger.error("插入成绩失败，enrollmentId={}", enrollmentId, e);
            return false;
        }
    }

    private boolean updateScore(Integer enrollmentId, Double score) {
        String sql = "UPDATE scores SET score = ?, graded_at = CURRENT_TIMESTAMP WHERE enrollment_id = ?";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, score);
            pstmt.setInt(2, enrollmentId);

            int rows = pstmt.executeUpdate();
            logger.info("更新成绩成功，enrollmentId={}, score={}", enrollmentId, score);
            return rows > 0;

        } catch (SQLException e) {
            logger.error("更新成绩失败，enrollmentId={}", enrollmentId, e);
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM scores WHERE id = ?";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int rows = pstmt.executeUpdate();
            logger.info("删除成绩成功，id={}", id);
            return rows > 0;

        } catch (SQLException e) {
            logger.error("删除成绩失败，id={}", id, e);
            return false;
        }
    }

    @Override
    public boolean exists(Integer enrollmentId) {
        String sql = "SELECT COUNT(*) FROM scores WHERE enrollment_id = ?";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, enrollmentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            logger.error("检查成绩是否存在失败", e);
        }

        return false;
    }

    /**
     * 将 ResultSet 映射为 Score 对象
     */
    private Score mapResultSetToScore(ResultSet rs) throws SQLException {
        Score score = new Score();
        score.setId(rs.getInt("id"));
        score.setEnrollmentId(rs.getInt("enrollment_id"));
        score.setScore(rs.getDouble("score"));

        Timestamp gradedAt = rs.getTimestamp("graded_at");
        if (gradedAt != null) {
            score.setGradedAt(new Date(gradedAt.getTime()));
        }

        score.setStudentId(rs.getString("student_id"));
        score.setStudentName(rs.getString("student_name"));
        score.setCourseId(rs.getString("course_id"));
        score.setCourseName(rs.getString("course_name"));

        return score;
    }
}
