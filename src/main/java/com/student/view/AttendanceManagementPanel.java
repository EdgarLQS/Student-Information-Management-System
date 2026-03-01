package com.student.view;

import com.student.controller.AttendanceController;
import com.student.controller.CourseController;
import com.student.controller.StudentController;
import com.student.model.Attendance;
import com.student.model.Course;
import com.student.model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 考勤管理面板
 */
public class AttendanceManagementPanel extends JPanel {
    private AttendanceController attendanceController;
    private StudentController studentController;
    private CourseController courseController;

    private JTable attendanceTable;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;
    private JLabel statisticsLabel;

    private JComboBox<StudentComboItem> studentCombo;
    private JComboBox<CourseComboItem> courseCombo;
    private JSpinner startDateSpinner;
    private JSpinner endDateSpinner;

    private List<Attendance> currentAttendances;

    public AttendanceManagementPanel() {
        this.attendanceController = new AttendanceController();
        this.studentController = new StudentController();
        this.courseController = new CourseController();
        initUI();
        loadFilterOptions();
        loadAttendances();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 顶部筛选和操作面板
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        topPanel.add(new JLabel("学生："));
        studentCombo = new JComboBox<>();
        studentCombo.setPreferredSize(new Dimension(200, 25));
        studentCombo.addActionListener(e -> loadAttendances());
        topPanel.add(studentCombo);

        topPanel.add(new JLabel("  课程："));
        courseCombo = new JComboBox<>();
        courseCombo.setPreferredSize(new Dimension(200, 25));
        courseCombo.addActionListener(e -> loadAttendances());
        topPanel.add(courseCombo);

        topPanel.add(new JLabel("  从："));
        startDateSpinner = new JSpinner(new SpinnerDateModel());
        startDateSpinner.setPreferredSize(new Dimension(120, 25));
        topPanel.add(startDateSpinner);

        topPanel.add(new JLabel("  到："));
        endDateSpinner = new JSpinner(new SpinnerDateModel());
        endDateSpinner.setPreferredSize(new Dimension(120, 25));
        topPanel.add(endDateSpinner);

        JButton queryButton = new JButton("查询");
        queryButton.addActionListener(e -> loadAttendances());
        topPanel.add(queryButton);

        JButton addButton = new JButton("录入");
        addButton.addActionListener(e -> addAttendance());
        topPanel.add(addButton);

        JButton editButton = new JButton("修改");
        editButton.addActionListener(e -> editAttendance());
        topPanel.add(editButton);

        JButton deleteButton = new JButton("删除");
        deleteButton.addActionListener(e -> deleteAttendance());
        topPanel.add(deleteButton);

        add(topPanel, BorderLayout.NORTH);

        // 表格
        createTable();
        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        add(scrollPane, BorderLayout.CENTER);

        // 底部状态栏和统计
        JPanel bottomPanel = new JPanel(new BorderLayout());

        statusLabel = new JLabel("  共 0 条记录");
        statusLabel.setBorder(BorderFactory.createEtchedBorder());
        bottomPanel.add(statusLabel, BorderLayout.WEST);

        statisticsLabel = new JLabel("  出勤率：-");
        statisticsLabel.setBorder(BorderFactory.createEtchedBorder());
        bottomPanel.add(statisticsLabel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void createTable() {
        String[] columns = {"ID", "学生", "课程", "考勤日期", "状态", "备注"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        attendanceTable = new JTable(tableModel);
        TableUtils.applyStyle(attendanceTable);
        TableUtils.setCenterRenderer(attendanceTable, 0, 4);

        attendanceTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        attendanceTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        attendanceTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        attendanceTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        attendanceTable.getColumnModel().getColumn(4).setPreferredWidth(60);
        attendanceTable.getColumnModel().getColumn(5).setPreferredWidth(150);
    }

    private void loadFilterOptions() {
        // 加载学生列表
        studentCombo.removeAllItems();
        studentCombo.addItem(new StudentComboItem(0, "-- 全部学生 --"));
        List<Student> students = studentController.getAllStudents();
        for (Student student : students) {
            studentCombo.addItem(new StudentComboItem(student.getId(),
                student.getStudentId() + " - " + student.getName()));
        }

        // 加载课程列表
        courseCombo.removeAllItems();
        courseCombo.addItem(new CourseComboItem(0, "-- 全部课程 --"));
        List<Course> courses = courseController.getAllCourses();
        for (Course course : courses) {
            courseCombo.addItem(new CourseComboItem(course.getId(),
                course.getCourseId() + " - " + course.getCourseName()));
        }

        // 设置默认日期范围为最近一个月
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        startDateSpinner.setValue(calendar.getTime());
        endDateSpinner.setValue(new Date());
    }

    private void loadAttendances() {
        StudentComboItem selectedStudent = (StudentComboItem) studentCombo.getSelectedItem();
        CourseComboItem selectedCourse = (CourseComboItem) courseCombo.getSelectedItem();

        Date startDate = (Date) startDateSpinner.getValue();
        Date endDate = (Date) endDateSpinner.getValue();

        if (selectedStudent != null && selectedStudent.getId() != 0) {
            currentAttendances = attendanceController.getAttendancesByStudent(selectedStudent.getId());
        } else if (selectedCourse != null && selectedCourse.getId() != 0) {
            currentAttendances = attendanceController.getAttendancesByCourse(selectedCourse.getId());
        } else {
            currentAttendances = attendanceController.getAttendancesByDateRange(
                new java.sql.Date(startDate.getTime()),
                new java.sql.Date(endDate.getTime())
            );
        }

        refreshTable();
        updateStatistics();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);

        if (currentAttendances == null || currentAttendances.isEmpty()) {
            statusLabel.setText("  共 0 条记录");
            statisticsLabel.setText("  出勤率：-");
            return;
        }

        for (Attendance attendance : currentAttendances) {
            Object[] row = {
                attendance.getId(),
                attendance.getStudentName() != null ? attendance.getStudentName() : "",
                attendance.getCourseName() != null ? attendance.getCourseName() : "",
                attendance.getAttendanceDate() != null ?
                    new java.text.SimpleDateFormat("yyyy-MM-dd").format(attendance.getAttendanceDate()) : "",
                attendance.getStatus() != null ? attendance.getStatus() : "",
                attendance.getRemark() != null ? attendance.getRemark() : ""
            };
            tableModel.addRow(row);
        }

        statusLabel.setText("  共 " + currentAttendances.size() + " 条记录");
    }

    private void updateStatistics() {
        if (currentAttendances != null && !currentAttendances.isEmpty()) {
            long present = currentAttendances.stream()
                .filter(a -> "出勤".equals(a.getStatus()))
                .count();
            double rate = (double) present / currentAttendances.size() * 100;
            statisticsLabel.setText(String.format("  出勤率：%.1f%%", rate));
        } else {
            statisticsLabel.setText("  出勤率：-");
        }
    }

    private void addAttendance() {
        AttendanceEntryDialog dialog = new AttendanceEntryDialog(null);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            JOptionPane.showMessageDialog(this, "考勤录入成功", "提示", JOptionPane.INFORMATION_MESSAGE);
            loadAttendances();
        }
    }

    private void editAttendance() {
        int selectedRow = attendanceTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请先选择要修改的考勤记录", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer id = (Integer) tableModel.getValueAt(selectedRow, 0);
        Attendance attendance = attendanceController.getAttendanceById(id);

        if (attendance != null) {
            AttendanceEntryDialog dialog = new AttendanceEntryDialog(null, attendance);
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                JOptionPane.showMessageDialog(this, "考勤修改成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                loadAttendances();
            }
        }
    }

    private void deleteAttendance() {
        int selectedRow = attendanceTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请先选择要删除的考勤记录", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer id = (Integer) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "确定要删除这条考勤记录吗？",
                "确认删除",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (attendanceController.deleteAttendance(id)) {
                JOptionPane.showMessageDialog(this, "删除成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                loadAttendances();
            } else {
                JOptionPane.showMessageDialog(this, "删除失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // 内部类
    static class StudentComboItem {
        private final Integer id;
        private final String displayName;

        StudentComboItem(Integer id, String displayName) {
            this.id = id;
            this.displayName = displayName;
        }

        Integer getId() { return id; }

        @Override
        public String toString() { return displayName; }
    }

    static class CourseComboItem {
        private final Integer id;
        private final String displayName;

        CourseComboItem(Integer id, String displayName) {
            this.id = id;
            this.displayName = displayName;
        }

        Integer getId() { return id; }

        @Override
        public String toString() { return displayName; }
    }
}
