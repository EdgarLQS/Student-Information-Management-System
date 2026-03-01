package com.student.view;

import com.student.controller.CourseController;
import com.student.controller.StudentController;
import com.student.model.Attendance;
import com.student.model.Course;
import com.student.model.Student;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.List;

/**
 * 考勤录入对话框
 */
public class AttendanceEntryDialog extends JDialog {
    private JComboBox<StudentComboItem> studentCombo;
    private JComboBox<CourseComboItem> courseCombo;
    private JSpinner dateSpinner;
    private JComboBox<String> statusCombo;
    private JTextField remarkField;

    private Attendance attendance;
    private boolean confirmed = false;

    public AttendanceEntryDialog(Frame parent) {
        super(parent, "录入考勤", true);
        initUI();
        loadOptions();
    }

    public AttendanceEntryDialog(Frame parent, Attendance attendance) {
        super(parent, "修改考勤", true);
        this.attendance = attendance;
        initUI();
        loadOptions();
        fillData();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setSize(400, 350);
        setLocationRelativeTo(getOwner());
        setResizable(false);
        getContentPane().setBackground(Theme.BACKGROUND_PRIMARY);

        // 表单面板
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Theme.BACKGROUND_PRIMARY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 学生
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        formPanel.add(new JLabel("学生："), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        studentCombo = new JComboBox<>();
        studentCombo.setPreferredSize(new Dimension(0, 30));
        formPanel.add(studentCombo, gbc);

        // 课程
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        formPanel.add(new JLabel("课程："), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        courseCombo = new JComboBox<>();
        courseCombo.setPreferredSize(new Dimension(0, 30));
        formPanel.add(courseCombo, gbc);

        // 考勤日期
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        formPanel.add(new JLabel("日期："), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        SpinnerDateModel dateModel = new SpinnerDateModel();
        dateSpinner = new JSpinner(dateModel);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));
        dateSpinner.setPreferredSize(new Dimension(0, 30));
        formPanel.add(dateSpinner, gbc);

        // 状态
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        formPanel.add(new JLabel("状态："), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        statusCombo = new JComboBox<>(new String[]{"出勤", "缺勤", "迟到", "请假"});
        statusCombo.setPreferredSize(new Dimension(0, 30));
        formPanel.add(statusCombo, gbc);

        // 备注
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        formPanel.add(new JLabel("备注："), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        remarkField = new JTextField(20);
        remarkField.setPreferredSize(new Dimension(0, 30));
        formPanel.add(remarkField, gbc);

        add(formPanel, BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        buttonPanel.setBackground(Theme.BACKGROUND_SECONDARY);

        JButton confirmButton = new JButton("确定");
        confirmButton.setPreferredSize(new Dimension(80, 32));
        confirmButton.addActionListener(e -> onConfirm());

        JButton cancelButton = new JButton("取消");
        cancelButton.setPreferredSize(new Dimension(80, 32));
        cancelButton.addActionListener(e -> onCancel());

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(confirmButton);
    }

    private void loadOptions() {
        // 加载学生列表
        studentCombo.removeAllItems();
        studentCombo.addItem(new StudentComboItem(0, "-- 选择学生 --"));
        List<Student> students = new StudentController().getAllStudents();
        for (Student student : students) {
            studentCombo.addItem(new StudentComboItem(student.getId(),
                student.getStudentId() + " - " + student.getName()));
        }

        // 加载课程列表
        courseCombo.removeAllItems();
        courseCombo.addItem(new CourseComboItem(0, "-- 选择课程 --"));
        List<Course> courses = new CourseController().getAllCourses();
        for (Course course : courses) {
            courseCombo.addItem(new CourseComboItem(course.getId(),
                course.getCourseId() + " - " + course.getCourseName()));
        }
    }

    private void fillData() {
        if (attendance == null) return;

        // 设置学生
        for (int i = 0; i < studentCombo.getItemCount(); i++) {
            StudentComboItem item = studentCombo.getItemAt(i);
            if (item.getId().equals(attendance.getStudentId())) {
                studentCombo.setSelectedItem(item);
                break;
            }
        }

        // 设置课程
        for (int i = 0; i < courseCombo.getItemCount(); i++) {
            CourseComboItem item = courseCombo.getItemAt(i);
            if (item.getId().equals(attendance.getCourseId())) {
                courseCombo.setSelectedItem(item);
                break;
            }
        }

        // 设置日期
        if (attendance.getAttendanceDate() != null) {
            dateSpinner.setValue(attendance.getAttendanceDate());
        }

        // 设置状态
        if (attendance.getStatus() != null) {
            statusCombo.setSelectedItem(attendance.getStatus());
        }

        // 设置备注
        if (attendance.getRemark() != null) {
            remarkField.setText(attendance.getRemark());
        }
    }

    private void onConfirm() {
        // 验证必填字段
        StudentComboItem selectedStudent = (StudentComboItem) studentCombo.getSelectedItem();
        if (selectedStudent == null || selectedStudent.getId() == 0) {
            JOptionPane.showMessageDialog(this, "请选择学生", "输入错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        CourseComboItem selectedCourse = (CourseComboItem) courseCombo.getSelectedItem();
        if (selectedCourse == null || selectedCourse.getId() == 0) {
            JOptionPane.showMessageDialog(this, "请选择课程", "输入错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 创建考勤对象
        if (attendance == null) {
            attendance = new Attendance();
        }

        attendance.setStudentId(selectedStudent.getId());
        attendance.setCourseId(selectedCourse.getId());
        attendance.setAttendanceDate((Date) dateSpinner.getValue());
        attendance.setStatus((String) statusCombo.getSelectedItem());
        attendance.setRemark(remarkField.getText().trim());

        confirmed = true;
        dispose();
    }

    private void onCancel() {
        confirmed = false;
        dispose();
    }

    public Attendance getAttendance() {
        return attendance;
    }

    public boolean isConfirmed() {
        return confirmed;
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
