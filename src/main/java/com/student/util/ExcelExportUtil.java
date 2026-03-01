package com.student.util;

import com.student.model.Attendance;
import com.student.model.Course;
import com.student.model.Score;
import com.student.model.Student;
import com.student.model.Teacher;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Excel 导出工具类
 */
public class ExcelExportUtil {

    /**
     * 导出学生列表到 Excel
     */
    public static void exportStudents(List<Student> students, String filePath) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("学生列表");

            // 创建表头样式
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);

            // 创建表头
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "学号", "姓名", "性别", "年龄", "班级", "电话", "邮箱"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // 填充数据
            int rowNum = 1;
            for (Student student : students) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(student.getId());
                row.createCell(1).setCellValue(student.getStudentId());
                row.createCell(2).setCellValue(student.getName());
                row.createCell(3).setCellValue(student.getGender() != null ? student.getGender() : "");
                row.createCell(4).setCellValue(student.getAge() != null ? student.getAge() : 0);
                row.createCell(5).setCellValue(student.getClassName() != null ? student.getClassName() : "");
                row.createCell(6).setCellValue(student.getPhone() != null ? student.getPhone() : "");
                row.createCell(7).setCellValue(student.getEmail() != null ? student.getEmail() : "");

                for (int i = 0; i < headers.length; i++) {
                    row.getCell(i).setCellStyle(dataStyle);
                }
            }

            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 写入文件
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
        }
    }

    /**
     * 导出教师列表到 Excel
     */
    public static void exportTeachers(List<Teacher> teachers, String filePath) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("教师列表");

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);

            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "工号", "姓名", "性别", "职称", "所属院系", "电话", "邮箱"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (Teacher teacher : teachers) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(teacher.getId());
                row.createCell(1).setCellValue(teacher.getTeacherId());
                row.createCell(2).setCellValue(teacher.getName());
                row.createCell(3).setCellValue(teacher.getGender() != null ? teacher.getGender() : "");
                row.createCell(4).setCellValue(teacher.getTitle() != null ? teacher.getTitle() : "");
                row.createCell(5).setCellValue(teacher.getDepartment() != null ? teacher.getDepartment() : "");
                row.createCell(6).setCellValue(teacher.getPhone() != null ? teacher.getPhone() : "");
                row.createCell(7).setCellValue(teacher.getEmail() != null ? teacher.getEmail() : "");

                for (int i = 0; i < headers.length; i++) {
                    row.getCell(i).setCellStyle(dataStyle);
                }
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
        }
    }

    /**
     * 导出课程列表到 Excel
     */
    public static void exportCourses(List<Course> courses, String filePath) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("课程列表");

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);

            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "课程号", "课程名称", "学分", "教师", "学时", "创建时间"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (Course course : courses) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(course.getId());
                row.createCell(1).setCellValue(course.getCourseId());
                row.createCell(2).setCellValue(course.getCourseName());
                row.createCell(3).setCellValue(course.getCredit() != null ? course.getCredit() : 0);
                row.createCell(4).setCellValue(course.getTeacher() != null ? course.getTeacher() : "");
                row.createCell(5).setCellValue(course.getClassHours() != null ? course.getClassHours() : 0);
                row.createCell(6).setCellValue(course.getCreatedAt() != null ? course.getCreatedAt().toString() : "");

                for (int i = 0; i < headers.length; i++) {
                    row.getCell(i).setCellStyle(dataStyle);
                }
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
        }
    }

    /**
     * 导出成绩列表到 Excel
     */
    public static void exportScores(List<Score> scores, String filePath) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("成绩列表");

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);

            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "学号", "姓名", "课程号", "课程名称", "成绩", "录入时间"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (Score score : scores) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(score.getId());
                row.createCell(1).setCellValue(score.getStudentId() != null ? score.getStudentId() : "");
                row.createCell(2).setCellValue(score.getStudentName() != null ? score.getStudentName() : "");
                row.createCell(3).setCellValue(score.getCourseId() != null ? score.getCourseId() : "");
                row.createCell(4).setCellValue(score.getCourseName() != null ? score.getCourseName() : "");
                row.createCell(5).setCellValue(score.getScore() != null ? score.getScore() : 0);
                row.createCell(6).setCellValue(score.getGradedAt() != null ? score.getGradedAt().toString() : "");

                for (int i = 0; i < headers.length; i++) {
                    row.getCell(i).setCellStyle(dataStyle);
                }
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
        }
    }

    /**
     * 导出考勤列表到 Excel
     */
    public static void exportAttendances(List<Attendance> attendances, String filePath) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("考勤列表");

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);

            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "学生", "课程", "考勤日期", "状态", "备注"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (Attendance attendance : attendances) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(attendance.getId());
                row.createCell(1).setCellValue(attendance.getStudentName() != null ? attendance.getStudentName() : "");
                row.createCell(2).setCellValue(attendance.getCourseName() != null ? attendance.getCourseName() : "");
                row.createCell(3).setCellValue(attendance.getAttendanceDate() != null ?
                    new java.text.SimpleDateFormat("yyyy-MM-dd").format(attendance.getAttendanceDate()) : "");
                row.createCell(4).setCellValue(attendance.getStatus() != null ? attendance.getStatus() : "");
                row.createCell(5).setCellValue(attendance.getRemark() != null ? attendance.getRemark() : "");

                for (int i = 0; i < headers.length; i++) {
                    row.getCell(i).setCellStyle(dataStyle);
                }
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
        }
    }

    /**
     * 创建表头样式
     */
    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    /**
     * 创建数据样式
     */
    private static CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }
}
