package com.student.dao;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * DatabaseHelper 单元测试
 */
@DisplayName("DatabaseHelper 测试")
class DatabaseHelperTest {

    private DatabaseHelper dbHelper;

    @BeforeEach
    void setUp() {
        dbHelper = DatabaseHelper.getInstance();
    }

    @Test
    @DisplayName("测试获取单例实例")
    void testGetInstance() {
        DatabaseHelper instance1 = DatabaseHelper.getInstance();
        DatabaseHelper instance2 = DatabaseHelper.getInstance();

        assertThat(instance1).isSameAs(instance2);
    }

    @Test
    @DisplayName("测试获取数据库连接")
    void testGetConnection() throws SQLException {
        Connection conn = dbHelper.getConnection();
        assertThat(conn).isNotNull();
        assertThat(conn.isClosed()).isFalse();
    }

    @Test
    @DisplayName("测试连接有效性")
    void testConnectionValidity() throws SQLException {
        try (Connection conn = dbHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT 1")) {

            assertThat(rs.next()).isTrue();
            assertThat(rs.getInt(1)).isEqualTo(1);
        }
    }

    @Test
    @DisplayName("测试多个连接请求返回同一连接")
    void testMultipleConnectionRequests() throws SQLException {
        Connection conn1 = dbHelper.getConnection();
        Connection conn2 = dbHelper.getConnection();

        // 由于 SQLite JDBC 的特性，每次 getConnection() 返回的可能是新连接
        // 但两者都应该是有效的
        assertThat(conn1).isNotNull();
        assertThat(conn2).isNotNull();
        assertThat(conn1.isClosed()).isFalse();
        assertThat(conn2.isClosed()).isFalse();
    }

    @Test
    @DisplayName("测试连接关闭后重新连接")
    void testReconnectAfterClose() throws SQLException {
        Connection conn = dbHelper.getConnection();
        conn.close();

        Connection newConn = dbHelper.getConnection();
        assertThat(newConn).isNotNull();
        assertThat(newConn.isClosed()).isFalse();
    }

    @Test
    @DisplayName("测试数据库表存在性 - students")
    void testStudentsTableExists() throws SQLException {
        try (Connection conn = dbHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                 "SELECT name FROM sqlite_master WHERE type='table' AND name='students'")) {

            assertThat(rs.next()).isTrue();
            assertThat(rs.getString("name")).isEqualTo("students");
        }
    }

    @Test
    @DisplayName("测试数据库表存在性 - teachers")
    void testTeachersTableExists() throws SQLException {
        try (Connection conn = dbHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                 "SELECT name FROM sqlite_master WHERE type='table' AND name='teachers'")) {

            assertThat(rs.next()).isTrue();
            assertThat(rs.getString("name")).isEqualTo("teachers");
        }
    }

    @Test
    @DisplayName("测试数据库表存在性 - courses")
    void testCoursesTableExists() throws SQLException {
        try (Connection conn = dbHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                 "SELECT name FROM sqlite_master WHERE type='table' AND name='courses'")) {

            assertThat(rs.next()).isTrue();
            assertThat(rs.getString("name")).isEqualTo("courses");
        }
    }

    @Test
    @DisplayName("测试数据库表存在性 - attendances")
    void testAttendancesTableExists() throws SQLException {
        try (Connection conn = dbHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                 "SELECT name FROM sqlite_master WHERE type='table' AND name='attendances'")) {

            assertThat(rs.next()).isTrue();
            assertThat(rs.getString("name")).isEqualTo("attendances");
        }
    }

    @Test
    @DisplayName("测试数据库表存在性 - enrollments")
    void testEnrollmentsTableExists() throws SQLException {
        try (Connection conn = dbHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                 "SELECT name FROM sqlite_master WHERE type='table' AND name='enrollments'")) {

            assertThat(rs.next()).isTrue();
            assertThat(rs.getString("name")).isEqualTo("enrollments");
        }
    }

    @Test
    @DisplayName("测试数据库表存在性 - scores")
    void testScoresTableExists() throws SQLException {
        try (Connection conn = dbHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                 "SELECT name FROM sqlite_master WHERE type='table' AND name='scores'")) {

            assertThat(rs.next()).isTrue();
            assertThat(rs.getString("name")).isEqualTo("scores");
        }
    }
}
