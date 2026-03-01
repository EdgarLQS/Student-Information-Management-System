package com.student.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

/**
 * 数据库帮助类 - 单例模式管理 SQLite 连接
 */
public class DatabaseHelper {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseHelper.class);

    private static final String DB_DIR = "data";
    private static final String DB_FILE = "student.db";
    private static DatabaseHelper instance;
    private Connection connection;

    private DatabaseHelper() {
        connect();
    }

    /**
     * 获取单例实例
     */
    public static synchronized DatabaseHelper getInstance() {
        if (instance == null) {
            instance = new DatabaseHelper();
        }
        return instance;
    }

    /**
     * 获取数据库连接
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connect();
        }
        return connection;
    }

    /**
     * 连接数据库
     */
    private void connect() {
        try {
            // 加载 SQLite 驱动
            Class.forName("org.sqlite.JDBC");

            // 确保数据目录存在
            File dbDir = new File(DB_DIR);
            if (!dbDir.exists()) {
                dbDir.mkdirs();
            }

            // 数据库文件路径
            String dbPath = DB_DIR + File.separator + DB_FILE;
            String url = "jdbc:sqlite:" + dbPath;

            connection = DriverManager.getConnection(url);
            connection.setAutoCommit(true);

            logger.info("数据库连接成功：{}", dbPath);

            // 初始化数据库表
            initTables();

            // 初始化示例数据
            initSampleData();

        } catch (ClassNotFoundException e) {
            logger.error("SQLite 驱动未找到", e);
            throw new RuntimeException("SQLite 驱动未找到", e);
        } catch (SQLException e) {
            logger.error("数据库连接失败", e);
            throw new RuntimeException("数据库连接失败", e);
        }
    }

    /**
     * 初始化数据库表
     */
    private void initTables() {
        try (Statement stmt = getConnection().createStatement()) {
            // 创建学生表
            stmt.execute("CREATE TABLE IF NOT EXISTS students (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "student_id VARCHAR(20) UNIQUE NOT NULL," +
                    "name VARCHAR(50) NOT NULL," +
                    "gender VARCHAR(10)," +
                    "age INTEGER," +
                    "class_name VARCHAR(50)," +
                    "phone VARCHAR(20)," +
                    "email VARCHAR(100)," +
                    "created_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "updated_at DATETIME DEFAULT CURRENT_TIMESTAMP" +
                    ")");

            // 创建课程表
            stmt.execute("CREATE TABLE IF NOT EXISTS courses (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "course_id VARCHAR(20) UNIQUE NOT NULL," +
                    "course_name VARCHAR(100) NOT NULL," +
                    "credit DECIMAL(3,1)," +
                    "teacher VARCHAR(50)," +
                    "class_hours INTEGER," +
                    "created_at DATETIME DEFAULT CURRENT_TIMESTAMP" +
                    ")");

            // 创建选课表
            stmt.execute("CREATE TABLE IF NOT EXISTS enrollments (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "student_id INTEGER NOT NULL," +
                    "course_id INTEGER NOT NULL," +
                    "enroll_date DATE DEFAULT CURRENT_DATE," +
                    "status VARCHAR(10) DEFAULT 'active'," +
                    "FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE," +
                    "FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE," +
                    "UNIQUE(student_id, course_id)" +
                    ")");

            // 创建成绩表
            stmt.execute("CREATE TABLE IF NOT EXISTS scores (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "enrollment_id INTEGER NOT NULL," +
                    "score DECIMAL(5,2)," +
                    "graded_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (enrollment_id) REFERENCES enrollments(id) ON DELETE CASCADE," +
                    "UNIQUE(enrollment_id)" +
                    ")");

            // 创建教师表
            stmt.execute("CREATE TABLE IF NOT EXISTS teachers (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "teacher_id VARCHAR(20) UNIQUE NOT NULL," +
                    "name VARCHAR(50) NOT NULL," +
                    "gender VARCHAR(10)," +
                    "title VARCHAR(50)," +
                    "department VARCHAR(100)," +
                    "phone VARCHAR(20)," +
                    "email VARCHAR(100)," +
                    "created_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "updated_at DATETIME DEFAULT CURRENT_TIMESTAMP" +
                    ")");

            // 创建考勤表
            stmt.execute("CREATE TABLE IF NOT EXISTS attendances (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "student_id INTEGER NOT NULL," +
                    "course_id INTEGER NOT NULL," +
                    "attendance_date DATE NOT NULL," +
                    "status VARCHAR(10) NOT NULL," +
                    "remark VARCHAR(200)," +
                    "created_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "updated_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE," +
                    "FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE," +
                    "UNIQUE(student_id, course_id, attendance_date)" +
                    ")");

            // 创建索引
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_student_id ON students(student_id)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_name ON students(name)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_course_id ON courses(course_id)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_enrollment_student ON enrollments(student_id)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_enrollment_course ON enrollments(course_id)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_score_enrollment ON scores(enrollment_id)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_teacher_id ON teachers(teacher_id)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_teacher_name ON teachers(name)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_attendance_student ON attendances(student_id)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_attendance_course ON attendances(course_id)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_attendance_date ON attendances(attendance_date)");

            logger.info("数据库表初始化完成");
        } catch (SQLException e) {
            logger.error("创建数据库表失败", e);
        }
    }

    /**
     * 初始化示例数据
     */
    private void initSampleData() {
        try {
            // 检查是否已有数据
            if (!isTableEmpty("students")) {
                logger.info("示例数据已存在，跳过初始化");
                return;
            }

            // 从资源文件加载初始化 SQL
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("db/init-data.sql");
            if (inputStream == null) {
                logger.warn("未找到初始化数据文件，跳过示例数据加载");
                return;
            }

            String sqlContent = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));
            inputStream.close();

            // 分割 SQL 语句并执行
            String[] sqlStatements = sqlContent.split(";");
            try (Statement stmt = getConnection().createStatement()) {
                for (String sql : sqlStatements) {
                    String trimmedSql = sql.trim();
                    if (!trimmedSql.isEmpty()) {
                        stmt.execute(trimmedSql);
                    }
                }
            }

            logger.info("示例数据初始化完成");
        } catch (Exception e) {
            logger.error("初始化示例数据失败", e);
        }
    }

    /**
     * 检查表是否为空
     */
    private boolean isTableEmpty(String tableName) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + tableName;
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        }
        return true;
    }

    /**
     * 关闭连接
     */
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.info("数据库连接已关闭");
            }
        } catch (SQLException e) {
            logger.error("关闭数据库连接失败", e);
        }
    }
}
