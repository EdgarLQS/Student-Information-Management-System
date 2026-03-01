package com.student.view;

import com.student.controller.EnrollmentController;
import com.student.controller.CourseController;
import com.student.controller.ScoreController;
import com.student.model.Course;
import com.student.model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * 选课对话框
 */
public class EnrollmentDialog extends JDialog {
    private JComboBox<StudentComboItem> studentCombo;
    private JComboBox<CourseComboItem> courseCombo;
    private JTable enrollmentTable;
    private DefaultTableModel tableModel;

    private EnrollmentController enrollmentController;
    private CourseController courseController;
    private ScoreController scoreController;

    public EnrollmentDialog(Frame parent) {
        super(parent, "选课管理", true);
        this.enrollmentController = new EnrollmentController();
        this.courseController = new CourseController();
        this.scoreController = new ScoreController();
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setSize(700, 500);
        setLocationRelativeTo(getOwner());

        // 顶部选课面板
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("选课操作"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 选择学生
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        topPanel.add(new JLabel("学生："), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        studentCombo = new JComboBox<>();
        studentCombo.setPreferredSize(new Dimension(200, 25));
        studentCombo.addActionListener(e -> loadEnrollments());
        topPanel.add(studentCombo, gbc);

        // 选择课程
        gbc.gridx = 2;
        gbc.weightx = 0;
        topPanel.add(new JLabel("课程："), gbc);

        gbc.gridx = 3;
        gbc.weightx = 1;
        courseCombo = new JComboBox<>();
        courseCombo.setPreferredSize(new Dimension(200, 25));
        topPanel.add(courseCombo, gbc);

        // 选课按钮
        gbc.gridx = 4;
        gbc.weightx = 0;
        JButton enrollButton = new JButton("选课");
        enrollButton.addActionListener(e -> enrollCourse());
        topPanel.add(enrollButton, gbc);

        add(topPanel, BorderLayout.NORTH);

        // 已选课程表格
        createTable();
        JScrollPane scrollPane = new JScrollPane(enrollmentTable);
        add(scrollPane, BorderLayout.CENTER);

        // 底部按钮
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JButton dropButton = new JButton("退课");
        dropButton.addActionListener(e -> dropCourse());
        buttonPanel.add(dropButton);

        JButton closeButton = new JButton("关闭");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // 加载数据
        loadStudents();
        loadCourses();
    }

    private void createTable() {
        String[] columns = {"ID", "学号", "姓名", "课程号", "课程名称", "学分", "选课日期"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        enrollmentTable = new JTable(tableModel);
        enrollmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        enrollmentTable.setRowHeight(25);
    }

    private void loadStudents() {
        studentCombo.removeAllItems();
        studentCombo.addItem(new StudentComboItem(0, "-- 选择学生 --"));

        List<Student> students = scoreController.getAllStudents();
        for (Student student : students) {
            studentCombo.addItem(new StudentComboItem(student.getId(),
                student.getStudentId() + " - " + student.getName()));
        }
    }

    private void loadCourses() {
        courseCombo.removeAllItems();
        courseCombo.addItem(new CourseComboItem(0, "-- 选择课程 --"));

        List<Course> courses = courseController.getAllCourses();
        for (Course course : courses) {
            courseCombo.addItem(new CourseComboItem(course.getId(),
                course.getCourseId() + " - " + course.getCourseName()));
        }
    }

    private void loadEnrollments() {
        tableModel.setRowCount(0);

        StudentComboItem selectedStudent = (StudentComboItem) studentCombo.getSelectedItem();
        if (selectedStudent == null || selectedStudent.getId() == 0) {
            return;
        }

        List<com.student.model.Enrollment> enrollments = enrollmentController.getEnrollmentsByStudent(selectedStudent.getId());
        for (com.student.model.Enrollment enrollment : enrollments) {
            Object[] row = {
                enrollment.getId(),
                enrollment.getStudentName() != null ? enrollment.getStudentName() : "",
                enrollment.getCourseName() != null ? enrollment.getCourseName() : "",
                "", // 课程号需要额外查询
                enrollment.getCourseName() != null ? enrollment.getCourseName() : "",
                "", // 学分
                enrollment.getEnrollDate() != null ? enrollment.getEnrollDate() : ""
            };
            tableModel.addRow(row);
        }
    }

    private void enrollCourse() {
        StudentComboItem selectedStudent = (StudentComboItem) studentCombo.getSelectedItem();
        CourseComboItem selectedCourse = (CourseComboItem) courseCombo.getSelectedItem();

        if (selectedStudent == null || selectedStudent.getId() == 0) {
            JOptionPane.showMessageDialog(this, "请选择学生", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selectedCourse == null || selectedCourse.getId() == 0) {
            JOptionPane.showMessageDialog(this, "请选择课程", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (enrollmentController.isEnrolled(selectedStudent.getId(), selectedCourse.getId())) {
            JOptionPane.showMessageDialog(this, "该学生已选此课程", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (enrollmentController.enrollCourse(selectedStudent.getId(), selectedCourse.getId())) {
            JOptionPane.showMessageDialog(this, "选课成功", "提示", JOptionPane.INFORMATION_MESSAGE);
            loadEnrollments();
        } else {
            JOptionPane.showMessageDialog(this, "选课失败", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void dropCourse() {
        int selectedRow = enrollmentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请选择要退的课程", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer enrollmentId = (Integer) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this, "确定要退选该课程吗？", "确认退课",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (enrollmentController.dropCourse(enrollmentId)) {
                JOptionPane.showMessageDialog(this, "退课成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                loadEnrollments();
            } else {
                JOptionPane.showMessageDialog(this, "退课失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // 内部类：学生下拉选项
    static class StudentComboItem {
        private final Integer id;
        private final String displayName;

        StudentComboItem(Integer id, String displayName) {
            this.id = id;
            this.displayName = displayName;
        }

        Integer getId() {
            return id;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    // 内部类：课程下拉选项
    static class CourseComboItem {
        private final Integer id;
        private final String displayName;

        CourseComboItem(Integer id, String displayName) {
            this.id = id;
            this.displayName = displayName;
        }

        Integer getId() {
            return id;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }
}
