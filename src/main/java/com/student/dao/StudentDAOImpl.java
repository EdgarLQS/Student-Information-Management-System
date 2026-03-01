package com.student.dao;

import com.student.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 学生数据访问实现类
 */
public class StudentDAOImpl implements StudentDAO {
    private static final Logger logger = LoggerFactory.getLogger(StudentDAOImpl.class);

    private final DatabaseHelper dbHelper;

    public StudentDAOImpl() {
        this.dbHelper = DatabaseHelper.getInstance();
    }

    @Override
    public List<Student> getAll() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY id DESC";

        try (Connection conn = dbHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }

        } catch (SQLException e) {
            logger.error("获取所有学生失败", e);
        }

        return students;
    }

    @Override
    public Student getById(Integer id) {
        String sql = "SELECT * FROM students WHERE id = ?";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToStudent(rs);
                }
            }

        } catch (SQLException e) {
            logger.error("根据 ID 获取学生失败，id={}", id, e);
        }

        return null;
    }

    @Override
    public Student getByStudentId(String studentId) {
        String sql = "SELECT * FROM students WHERE student_id = ?";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToStudent(rs);
                }
            }

        } catch (SQLException e) {
            logger.error("根据学号获取学生失败，studentId={}", studentId, e);
        }

        return null;
    }

    @Override
    public boolean insert(Student student) {
        String sql = "INSERT INTO students (student_id, name, gender, age, class_name, phone, email) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getStudentId());
            pstmt.setString(2, student.getName());
            pstmt.setString(3, student.getGender());
            pstmt.setInt(4, student.getAge());
            pstmt.setString(5, student.getClassName());
            pstmt.setString(6, student.getPhone());
            pstmt.setString(7, student.getEmail());

            int rows = pstmt.executeUpdate();
            logger.info("插入学生成功，studentId={}", student.getStudentId());
            return rows > 0;

        } catch (SQLException e) {
            logger.error("插入学生失败，studentId={}", student.getStudentId(), e);
            return false;
        }
    }

    @Override
    public boolean update(Student student) {
        String sql = "UPDATE students SET " +
                     "student_id = ?, name = ?, gender = ?, age = ?, " +
                     "class_name = ?, phone = ?, email = ?, " +
                     "updated_at = CURRENT_TIMESTAMP " +
                     "WHERE id = ?";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getStudentId());
            pstmt.setString(2, student.getName());
            pstmt.setString(3, student.getGender());
            pstmt.setInt(4, student.getAge());
            pstmt.setString(5, student.getClassName());
            pstmt.setString(6, student.getPhone());
            pstmt.setString(7, student.getEmail());
            pstmt.setInt(8, student.getId());

            int rows = pstmt.executeUpdate();
            logger.info("更新学生成功，id={}", student.getId());
            return rows > 0;

        } catch (SQLException e) {
            logger.error("更新学生失败，id={}", student.getId(), e);
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int rows = pstmt.executeUpdate();
            logger.info("删除学生成功，id={}", id);
            return rows > 0;

        } catch (SQLException e) {
            logger.error("删除学生失败，id={}", id, e);
            return false;
        }
    }

    @Override
    public List<Student> search(String keyword) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE student_id LIKE ? OR name LIKE ? ORDER BY id DESC";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    students.add(mapResultSetToStudent(rs));
                }
            }

        } catch (SQLException e) {
            logger.error("搜索学生失败，keyword={}", keyword, e);
        }

        return students;
    }

    @Override
    public List<Student> getPage(int page, int pageSize) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY id DESC LIMIT ? OFFSET ?";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, pageSize);
            pstmt.setInt(2, (page - 1) * pageSize);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    students.add(mapResultSetToStudent(rs));
                }
            }

        } catch (SQLException e) {
            logger.error("分页获取学生失败，page={}, pageSize={}", page, pageSize, e);
        }

        return students;
    }

    @Override
    public int getCount() {
        String sql = "SELECT COUNT(*) FROM students";

        try (Connection conn = dbHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            logger.error("获取学生总数失败", e);
        }

        return 0;
    }

    /**
     * 将 ResultSet 映射为 Student 对象
     */
    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setId(rs.getInt("id"));
        student.setStudentId(rs.getString("student_id"));
        student.setName(rs.getString("name"));
        student.setGender(rs.getString("gender"));
        student.setAge(rs.getInt("age"));
        student.setClassName(rs.getString("class_name"));
        student.setPhone(rs.getString("phone"));
        student.setEmail(rs.getString("email"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            student.setCreatedAt(new Date(createdAt.getTime()));
        }

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            student.setUpdatedAt(new Date(updatedAt.getTime()));
        }

        return student;
    }
}
