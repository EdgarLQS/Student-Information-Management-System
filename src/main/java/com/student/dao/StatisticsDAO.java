package com.student.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 统计数据访问类
 */
public class StatisticsDAO {
    private static final Logger logger = LoggerFactory.getLogger(StatisticsDAO.class);

    private final DatabaseHelper dbHelper;

    public StatisticsDAO() {
        this.dbHelper = DatabaseHelper.getInstance();
    }

    /**
     * 按班级统计学生人数
     */
    public Map<String, Integer> getStudentCountByClass() {
        Map<String, Integer> result = new LinkedHashMap<>();
        String sql = "SELECT class_name, COUNT(*) as count FROM students GROUP BY class_name ORDER BY class_name";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                result.put(rs.getString("class_name"), rs.getInt("count"));
            }

        } catch (SQLException e) {
            logger.error("按班级统计学生人数失败", e);
        }

        return result;
    }

    /**
     * 获取成绩分布（分数段）
     */
    public Map<String, Integer> getScoreDistribution(Integer courseId) {
        Map<String, Integer> result = new LinkedHashMap<>();
        result.put("90-100", 0);
        result.put("80-89", 0);
        result.put("70-79", 0);
        result.put("60-69", 0);
        result.put("<60", 0);

        String sql;
        PreparedStatement pstmt;

        try (Connection conn = dbHelper.getConnection()) {
            if (courseId != null) {
                sql = "SELECT s.score FROM scores s " +
                      "JOIN enrollments e ON s.enrollment_id = e.id " +
                      "WHERE e.course_id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, courseId);
            } else {
                sql = "SELECT score FROM scores";
                pstmt = conn.prepareStatement(sql);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    double score = rs.getDouble("score");
                    if (score >= 90) {
                        result.put("90-100", result.get("90-100") + 1);
                    } else if (score >= 80) {
                        result.put("80-89", result.get("80-89") + 1);
                    } else if (score >= 70) {
                        result.put("70-79", result.get("70-79") + 1);
                    } else if (score >= 60) {
                        result.put("60-69", result.get("60-69") + 1);
                    } else {
                        result.put("<60", result.get("<60") + 1);
                    }
                }
            }

        } catch (SQLException e) {
            logger.error("获取成绩分布失败", e);
        }

        return result;
    }

    /**
     * 获取各课程平均分
     */
    public Map<String, Double> getCourseAverageScores() {
        Map<String, Double> result = new LinkedHashMap<>();
        String sql = "SELECT c.course_name, AVG(s.score) as avg_score " +
                     "FROM scores s " +
                     "JOIN enrollments e ON s.enrollment_id = e.id " +
                     "JOIN courses c ON e.course_id = c.id " +
                     "GROUP BY c.id, c.course_name " +
                     "ORDER BY c.course_id";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                result.put(rs.getString("course_name"), rs.getDouble("avg_score"));
            }

        } catch (SQLException e) {
            logger.error("获取课程平均分失败", e);
        }

        return result;
    }

    /**
     * 获取学生各科成绩（用于雷达图）
     */
    public Map<String, Double> getStudentScores(Integer studentId) {
        Map<String, Double> result = new LinkedHashMap<>();
        String sql = "SELECT c.course_name, s.score " +
                     "FROM scores s " +
                     "JOIN enrollments e ON s.enrollment_id = e.id " +
                     "JOIN courses c ON e.course_id = c.id " +
                     "WHERE e.student_id = ? " +
                     "ORDER BY c.course_id";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    result.put(rs.getString("course_name"), rs.getDouble("score"));
                }
            }

        } catch (SQLException e) {
            logger.error("获取学生成绩失败，studentId={}", studentId, e);
        }

        return result;
    }

    /**
     * 获取课程统计信息（平均分、最高分、最低分、及格率）
     */
    public Map<String, Object> getCourseStatistics(Integer courseId) {
        Map<String, Object> result = new HashMap<>();
        String sql = "SELECT AVG(s.score) as avg_score, MAX(s.score) as max_score, " +
                     "MIN(s.score) as min_score, COUNT(*) as total, " +
                     "SUM(CASE WHEN s.score >= 60 THEN 1 ELSE 0 END) as pass_count " +
                     "FROM scores s " +
                     "JOIN enrollments e ON s.enrollment_id = e.id " +
                     "WHERE e.course_id = ?";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    result.put("average", rs.getDouble("avg_score"));
                    result.put("highest", rs.getDouble("max_score"));
                    result.put("lowest", rs.getDouble("min_score"));
                    result.put("total", rs.getInt("total"));

                    int total = rs.getInt("total");
                    int passCount = rs.getInt("pass_count");
                    double passRate = total > 0 ? (passCount * 100.0 / total) : 0;
                    result.put("passRate", passRate);
                }
            }

        } catch (SQLException e) {
            logger.error("获取课程统计信息失败，courseId={}", courseId, e);
        }

        return result;
    }

    /**
     * 获取所有学生总数
     */
    public int getTotalStudentCount() {
        String sql = "SELECT COUNT(*) FROM students";
        return executeCountQuery(sql);
    }

    /**
     * 获取所有课程总数
     */
    public int getTotalCourseCount() {
        String sql = "SELECT COUNT(*) FROM courses";
        return executeCountQuery(sql);
    }

    /**
     * 获取有成绩的学生数
     */
    public int getScoredStudentCount() {
        String sql = "SELECT COUNT(DISTINCT e.student_id) FROM scores s JOIN enrollments e ON s.enrollment_id = e.id";
        return executeCountQuery(sql);
    }

    private int executeCountQuery(String sql) {
        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            logger.error("执行计数查询失败", e);
        }

        return 0;
    }
}
