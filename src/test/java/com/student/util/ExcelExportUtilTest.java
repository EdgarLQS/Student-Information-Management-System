package com.student.util;

import com.student.controller.CourseController;
import com.student.controller.StudentController;
import com.student.model.Course;
import com.student.model.Student;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ExcelExportUtil 单元测试
 */
@DisplayName("ExcelExportUtil 测试")
class ExcelExportUtilTest {

    private static final String TEST_OUTPUT_DIR = "target/test-excel-output";
    private static File outputDir;

    @BeforeAll
    static void setUpAll() throws IOException {
        outputDir = new File(TEST_OUTPUT_DIR);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
    }

    @AfterAll
    static void tearDownAll() throws IOException {
        if (outputDir != null && outputDir.exists()) {
            for (File file : outputDir.listFiles()) {
                file.delete();
            }
            outputDir.delete();
        }
    }

    @Test
    @DisplayName("测试导出学生列表到 Excel")
    void testExportStudents() throws IOException {
        StudentController controller = new StudentController();
        List<Student> students = controller.getAllStudents();

        String filePath = TEST_OUTPUT_DIR + "/students.xlsx";
        ExcelExportUtil.exportStudents(students, filePath);

        File excelFile = new File(filePath);
        assertThat(excelFile).exists();
        assertThat(excelFile.length()).isGreaterThan(0);
    }

    @Test
    @DisplayName("测试导出学生列表到 Excel - 空列表")
    void testExportStudentsEmptyList() throws IOException {
        String filePath = TEST_OUTPUT_DIR + "/students_empty.xlsx";
        ExcelExportUtil.exportStudents(java.util.Collections.emptyList(), filePath);

        File excelFile = new File(filePath);
        assertThat(excelFile).exists();
        assertThat(excelFile.length()).isGreaterThan(0);
    }

    @Test
    @DisplayName("测试导出教师列表到 Excel")
    void testExportTeachers() throws IOException {
        com.student.controller.TeacherController controller = new com.student.controller.TeacherController();
        List<com.student.model.Teacher> teachers = controller.getAllTeachers();

        String filePath = TEST_OUTPUT_DIR + "/teachers.xlsx";
        ExcelExportUtil.exportTeachers(teachers, filePath);

        File excelFile = new File(filePath);
        assertThat(excelFile).exists();
        assertThat(excelFile.length()).isGreaterThan(0);
    }

    @Test
    @DisplayName("测试导出课程列表到 Excel")
    void testExportCourses() throws IOException {
        CourseController controller = new CourseController();
        List<Course> courses = controller.getAllCourses();

        String filePath = TEST_OUTPUT_DIR + "/courses.xlsx";
        ExcelExportUtil.exportCourses(courses, filePath);

        File excelFile = new File(filePath);
        assertThat(excelFile).exists();
        assertThat(excelFile.length()).isGreaterThan(0);
    }

    @Test
    @DisplayName("测试导出成绩列表到 Excel")
    void testExportScores() throws IOException {
        com.student.controller.ScoreController controller = new com.student.controller.ScoreController();
        List<com.student.model.Score> scores = controller.getAllScores();

        String filePath = TEST_OUTPUT_DIR + "/scores.xlsx";
        ExcelExportUtil.exportScores(scores, filePath);

        File excelFile = new File(filePath);
        assertThat(excelFile).exists();
        assertThat(excelFile.length()).isGreaterThan(0);
    }

    @Test
    @DisplayName("测试导出考勤列表到 Excel")
    void testExportAttendances() throws IOException {
        com.student.controller.AttendanceController controller = new com.student.controller.AttendanceController();
        List<com.student.model.Attendance> attendances = controller.getAllAttendances();

        String filePath = TEST_OUTPUT_DIR + "/attendances.xlsx";
        ExcelExportUtil.exportAttendances(attendances, filePath);

        File excelFile = new File(filePath);
        assertThat(excelFile).exists();
        assertThat(excelFile.length()).isGreaterThan(0);
    }

    @Test
    @DisplayName("测试导出文件内容验证 - 学生")
    void testExportStudentsContent() throws IOException {
        StudentController controller = new StudentController();
        List<Student> students = controller.getAllStudents();

        String filePath = TEST_OUTPUT_DIR + "/students_content.xlsx";
        ExcelExportUtil.exportStudents(students, filePath);

        // 使用 Apache POI 读取并验证内容
        try (org.apache.poi.xssf.usermodel.XSSFWorkbook workbook =
                     new org.apache.poi.xssf.usermodel.XSSFWorkbook(filePath)) {
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
            assertThat(sheet.getPhysicalNumberOfRows()).isGreaterThan(0);

            // 验证表头
            org.apache.poi.ss.usermodel.Row headerRow = sheet.getRow(0);
            assertThat(headerRow.getCell(0).getStringCellValue()).isEqualTo("ID");
            assertThat(headerRow.getCell(1).getStringCellValue()).isEqualTo("学号");
            assertThat(headerRow.getCell(2).getStringCellValue()).isEqualTo("姓名");
        }
    }
}
