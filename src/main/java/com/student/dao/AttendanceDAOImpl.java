package com.student.dao;

import com.student.model.Attendance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 考勤数据访问实现类
 */
public class AttendanceDAOImpl implements AttendanceDAO {
    private static final Logger logger = LoggerFactory.getLogger(AttendanceDAOImpl.class);

    private final DatabaseHelper dbHelper;

    public AttendanceDAOImpl() {
        this.dbHelper = DatabaseHelper.getInstance();
    }

    @Override
    public List<Attendance> getAll() {
        List<Attendance> attendances = new ArrayList<>();
        String sql = "SELECT a.*, s.name as student_name, c.course_name " +
                     "FROM attendances a " +
                     "LEFT JOIN students s ON a.student_id = s.id " +
                     "LEFT JOIN courses c ON a.course_id = c.id " +
                     "ORDER BY a.attendance_date DESC, a.id DESC";

        try (Connection conn = dbHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                attendances.add(mapResultSetToAttendance(rs));
            }

        } catch (SQLException e) {
            logger.error("获取所有考勤记录失败", e);
        }

        return attendances;
    }

    @Override
    public Attendance getById(Integer id) {
        String sql = "SELECT a.*, s.name as student_name, c.course_name " +
                     "FROM attendances a " +
                     "LEFT JOIN students s ON a.student_id = s.id " +
                     "LEFT JOIN courses c ON a.course_id = c.id " +
                     "WHERE a.id = ?";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAttendance(rs);
                }
            }

        } catch (SQLException e) {
            logger.error("根据 ID 获取考勤记录失败，id={}", id, e);
        }

        return null;
    }

    @Override
    public boolean insert(Attendance attendance) {
        String sql = "INSERT INTO attendances (student_id, course_id, attendance_date, status, remark) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, attendance.getStudentId());
            pstmt.setInt(2, attendance.getCourseId());
            pstmt.setDate(3, new java.sql.Date(attendance.getAttendanceDate().getTime()));
            pstmt.setString(4, attendance.getStatus());
            pstmt.setString(5, attendance.getRemark());

            int rows = pstmt.executeUpdate();
            logger.info("插入考勤记录成功");
            return rows > 0;

        } catch (SQLException e) {
            logger.error("插入考勤记录失败", e);
            return false;
        }
    }

    @Override
    public boolean update(Attendance attendance) {
        String sql = "UPDATE attendances SET " +
                     "student_id = ?, course_id = ?, attendance_date = ?, " +
                     "status = ?, remark = ?, updated_at = CURRENT_TIMESTAMP " +
                     "WHERE id = ?";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, attendance.getStudentId());
            pstmt.setInt(2, attendance.getCourseId());
            pstmt.setDate(3, new java.sql.Date(attendance.getAttendanceDate().getTime()));
            pstmt.setString(4, attendance.getStatus());
            pstmt.setString(5, attendance.getRemark());
            pstmt.setInt(6, attendance.getId());

            int rows = pstmt.executeUpdate();
            logger.info("更新考勤记录成功，id={}", attendance.getId());
            return rows > 0;

        } catch (SQLException e) {
            logger.error("更新考勤记录失败，id={}", attendance.getId(), e);
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM attendances WHERE id = ?";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int rows = pstmt.executeUpdate();
            logger.info("删除考勤记录成功，id={}", id);
            return rows > 0;

        } catch (SQLException e) {
            logger.error("删除考勤记录失败，id={}", id, e);
            return false;
        }
    }

    @Override
    public List<Attendance> getByStudentId(Integer studentId) {
        List<Attendance> attendances = new ArrayList<>();
        String sql = "SELECT a.*, s.name as student_name, c.course_name " +
                     "FROM attendances a " +
                     "LEFT JOIN students s ON a.student_id = s.id " +
                     "LEFT JOIN courses c ON a.course_id = c.id " +
                     "WHERE a.student_id = ? " +
                     "ORDER BY a.attendance_date DESC";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    attendances.add(mapResultSetToAttendance(rs));
                }
            }

        } catch (SQLException e) {
            logger.error("根据学生 ID 获取考勤记录失败，studentId={}", studentId, e);
        }

        return attendances;
    }

    @Override
    public List<Attendance> getByCourseId(Integer courseId) {
        List<Attendance> attendances = new ArrayList<>();
        String sql = "SELECT a.*, s.name as student_name, c.course_name " +
                     "FROM attendances a " +
                     "LEFT JOIN students s ON a.student_id = s.id " +
                     "LEFT JOIN courses c ON a.course_id = c.id " +
                     "WHERE a.course_id = ? " +
                     "ORDER BY a.attendance_date DESC";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    attendances.add(mapResultSetToAttendance(rs));
                }
            }

        } catch (SQLException e) {
            logger.error("根据课程 ID 获取考勤记录失败，courseId={}", courseId, e);
        }

        return attendances;
    }

    @Override
    public List<Attendance> getByDateRange(java.sql.Date startDate, java.sql.Date endDate) {
        List<Attendance> attendances = new ArrayList<>();
        String sql = "SELECT a.*, s.name as student_name, c.course_name " +
                     "FROM attendances a " +
                     "LEFT JOIN students s ON a.student_id = s.id " +
                     "LEFT JOIN courses c ON a.course_id = c.id " +
                     "WHERE a.attendance_date BETWEEN ? AND ? " +
                     "ORDER BY a.attendance_date DESC";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, startDate);
            pstmt.setDate(2, endDate);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    attendances.add(mapResultSetToAttendance(rs));
                }
            }

        } catch (SQLException e) {
            logger.error("根据日期范围获取考勤记录失败", e);
        }

        return attendances;
    }

    @Override
    public Map<String, Object> getStudentStatistics(Integer studentId) {
        Map<String, Object> stats = new HashMap<>();
        String sql = "SELECT " +
                     "COUNT(*) as total, " +
                     "SUM(CASE WHEN status = '出勤' THEN 1 ELSE 0 END) as present, " +
                     "SUM(CASE WHEN status = '缺勤' THEN 1 ELSE 0 END) as absent, " +
                     "SUM(CASE WHEN status = '迟到' THEN 1 ELSE 0 END) as late, " +
                     "SUM(CASE WHEN status = '请假' THEN 1 ELSE 0 END) as leave " +
                     "FROM attendances WHERE student_id = ?";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    stats.put("total", rs.getInt("total"));
                    stats.put("present", rs.getInt("present"));
                    stats.put("absent", rs.getInt("absent"));
                    stats.put("late", rs.getInt("late"));
                    stats.put("leave", rs.getInt("leave"));

                    int total = rs.getInt("total");
                    if (total > 0) {
                        stats.put("attendanceRate", (double) rs.getInt("present") / total * 100);
                    } else {
                        stats.put("attendanceRate", 0.0);
                    }
                }
            }

        } catch (SQLException e) {
            logger.error("获取学生考勤统计失败", e);
        }

        return stats;
    }

    @Override
    public Map<String, Object> getCourseStatistics(Integer courseId) {
        Map<String, Object> stats = new HashMap<>();
        String sql = "SELECT " +
                     "COUNT(*) as total, " +
                     "SUM(CASE WHEN status = '出勤' THEN 1 ELSE 0 END) as present, " +
                     "SUM(CASE WHEN status = '缺勤' THEN 1 ELSE 0 END) as absent " +
                     "FROM attendances WHERE course_id = ?";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    stats.put("total", rs.getInt("total"));
                    stats.put("present", rs.getInt("present"));
                    stats.put("absent", rs.getInt("absent"));

                    int total = rs.getInt("total");
                    if (total > 0) {
                        stats.put("attendanceRate", (double) rs.getInt("present") / total * 100);
                    } else {
                        stats.put("attendanceRate", 0.0);
                    }
                }
            }

        } catch (SQLException e) {
            logger.error("获取课程考勤统计失败", e);
        }

        return stats;
    }

    /**
     * 将 ResultSet 映射为 Attendance 对象
     */
    private Attendance mapResultSetToAttendance(ResultSet rs) throws SQLException {
        Attendance attendance = new Attendance();
        attendance.setId(rs.getInt("id"));
        attendance.setStudentId(rs.getInt("student_id"));
        attendance.setStudentName(rs.getString("student_name"));
        attendance.setCourseId(rs.getInt("course_id"));
        attendance.setCourseName(rs.getString("course_name"));

        java.sql.Date attendanceDate = rs.getDate("attendance_date");
        if (attendanceDate != null) {
            attendance.setAttendanceDate(new java.util.Date(attendanceDate.getTime()));
        }

        attendance.setStatus(rs.getString("status"));
        attendance.setRemark(rs.getString("remark"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            attendance.setCreatedAt(new java.util.Date(createdAt.getTime()));
        }

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            attendance.setUpdatedAt(new java.util.Date(updatedAt.getTime()));
        }

        return attendance;
    }
}
