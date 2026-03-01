package com.student.dao;

import com.student.model.Teacher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 教师数据访问实现类
 */
public class TeacherDAOImpl implements TeacherDAO {
    private static final Logger logger = LoggerFactory.getLogger(TeacherDAOImpl.class);

    private final DatabaseHelper dbHelper;

    public TeacherDAOImpl() {
        this.dbHelper = DatabaseHelper.getInstance();
    }

    @Override
    public List<Teacher> getAll() {
        List<Teacher> teachers = new ArrayList<>();
        String sql = "SELECT * FROM teachers ORDER BY id DESC";

        try (Connection conn = dbHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                teachers.add(mapResultSetToTeacher(rs));
            }

        } catch (SQLException e) {
            logger.error("获取所有教师失败", e);
        }

        return teachers;
    }

    @Override
    public Teacher getById(Integer id) {
        String sql = "SELECT * FROM teachers WHERE id = ?";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTeacher(rs);
                }
            }

        } catch (SQLException e) {
            logger.error("根据 ID 获取教师失败，id={}", id, e);
        }

        return null;
    }

    @Override
    public Teacher getByTeacherId(String teacherId) {
        String sql = "SELECT * FROM teachers WHERE teacher_id = ?";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, teacherId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTeacher(rs);
                }
            }

        } catch (SQLException e) {
            logger.error("根据工号获取教师失败，teacherId={}", teacherId, e);
        }

        return null;
    }

    @Override
    public boolean insert(Teacher teacher) {
        String sql = "INSERT INTO teachers (teacher_id, name, gender, title, department, phone, email) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, teacher.getTeacherId());
            pstmt.setString(2, teacher.getName());
            pstmt.setString(3, teacher.getGender());
            pstmt.setString(4, teacher.getTitle());
            pstmt.setString(5, teacher.getDepartment());
            pstmt.setString(6, teacher.getPhone());
            pstmt.setString(7, teacher.getEmail());

            int rows = pstmt.executeUpdate();
            logger.info("插入教师成功，teacherId={}", teacher.getTeacherId());
            return rows > 0;

        } catch (SQLException e) {
            logger.error("插入教师失败，teacherId={}", teacher.getTeacherId(), e);
            return false;
        }
    }

    @Override
    public boolean update(Teacher teacher) {
        String sql = "UPDATE teachers SET " +
                     "teacher_id = ?, name = ?, gender = ?, title = ?, " +
                     "department = ?, phone = ?, email = ?, " +
                     "updated_at = CURRENT_TIMESTAMP " +
                     "WHERE id = ?";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, teacher.getTeacherId());
            pstmt.setString(2, teacher.getName());
            pstmt.setString(3, teacher.getGender());
            pstmt.setString(4, teacher.getTitle());
            pstmt.setString(5, teacher.getDepartment());
            pstmt.setString(6, teacher.getPhone());
            pstmt.setString(7, teacher.getEmail());
            pstmt.setInt(8, teacher.getId());

            int rows = pstmt.executeUpdate();
            logger.info("更新教师成功，id={}", teacher.getId());
            return rows > 0;

        } catch (SQLException e) {
            logger.error("更新教师失败，id={}", teacher.getId(), e);
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM teachers WHERE id = ?";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int rows = pstmt.executeUpdate();
            logger.info("删除教师成功，id={}", id);
            return rows > 0;

        } catch (SQLException e) {
            logger.error("删除教师失败，id={}", id, e);
            return false;
        }
    }

    @Override
    public List<Teacher> search(String keyword) {
        List<Teacher> teachers = new ArrayList<>();
        String sql = "SELECT * FROM teachers WHERE teacher_id LIKE ? OR name LIKE ? ORDER BY id DESC";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    teachers.add(mapResultSetToTeacher(rs));
                }
            }

        } catch (SQLException e) {
            logger.error("搜索教师失败，keyword={}", keyword, e);
        }

        return teachers;
    }

    @Override
    public List<Teacher> getPage(int page, int pageSize) {
        List<Teacher> teachers = new ArrayList<>();
        String sql = "SELECT * FROM teachers ORDER BY id DESC LIMIT ? OFFSET ?";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, pageSize);
            pstmt.setInt(2, (page - 1) * pageSize);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    teachers.add(mapResultSetToTeacher(rs));
                }
            }

        } catch (SQLException e) {
            logger.error("分页获取教师失败，page={}, pageSize={}", page, pageSize, e);
        }

        return teachers;
    }

    @Override
    public int getCount() {
        String sql = "SELECT COUNT(*) FROM teachers";

        try (Connection conn = dbHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            logger.error("获取教师总数失败", e);
        }

        return 0;
    }

    /**
     * 将 ResultSet 映射为 Teacher 对象
     */
    private Teacher mapResultSetToTeacher(ResultSet rs) throws SQLException {
        Teacher teacher = new Teacher();
        teacher.setId(rs.getInt("id"));
        teacher.setTeacherId(rs.getString("teacher_id"));
        teacher.setName(rs.getString("name"));
        teacher.setGender(rs.getString("gender"));
        teacher.setTitle(rs.getString("title"));
        teacher.setDepartment(rs.getString("department"));
        teacher.setPhone(rs.getString("phone"));
        teacher.setEmail(rs.getString("email"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            teacher.setCreatedAt(new Date(createdAt.getTime()));
        }

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            teacher.setUpdatedAt(new Date(updatedAt.getTime()));
        }

        return teacher;
    }
}
