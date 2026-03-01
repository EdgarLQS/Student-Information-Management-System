package com.student.view;

import com.student.model.Course;

import javax.swing.*;
import java.awt.*;

/**
 * 课程表单对话框
 */
public class CourseFormDialog extends JDialog {
    private JTextField courseIdField;
    private JTextField courseNameField;
    private JTextField creditField;
    private JTextField teacherField;
    private JTextField classHoursField;

    private Course course;
    private boolean isEditMode;
    private boolean confirmed = false;

    public CourseFormDialog(Frame parent, String title) {
        super(parent, title, true);
        this.isEditMode = false;
        initUI();
    }

    public CourseFormDialog(Frame parent, String title, Course course) {
        super(parent, title, true);
        this.isEditMode = true;
        this.course = course;
        initUI();
        fillData();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setSize(350, 280);
        setLocationRelativeTo(getOwner());
        setResizable(false);

        // 表单面板
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 课程号
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        formPanel.add(new JLabel("课程号：*"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        courseIdField = new JTextField(15);
        courseIdField.setEnabled(!isEditMode);
        formPanel.add(courseIdField, gbc);

        // 课程名称
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        formPanel.add(new JLabel("课程名称：*"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        courseNameField = new JTextField(15);
        formPanel.add(courseNameField, gbc);

        // 学分
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        formPanel.add(new JLabel("学分："), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        creditField = new JTextField(15);
        formPanel.add(creditField, gbc);

        // 教师
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        formPanel.add(new JLabel("教师："), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        teacherField = new JTextField(15);
        formPanel.add(teacherField, gbc);

        // 学时
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        formPanel.add(new JLabel("学时："), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        classHoursField = new JTextField(15);
        formPanel.add(classHoursField, gbc);

        add(formPanel, BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JButton confirmButton = new JButton("确定");
        confirmButton.setPreferredSize(new Dimension(80, 30));
        confirmButton.addActionListener(e -> onConfirm());

        JButton cancelButton = new JButton("取消");
        cancelButton.setPreferredSize(new Dimension(80, 30));
        cancelButton.addActionListener(e -> onCancel());

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(confirmButton);
    }

    private void fillData() {
        if (course == null) return;

        courseIdField.setText(course.getCourseId());
        courseNameField.setText(course.getCourseName());

        if (course.getCredit() != null) {
            creditField.setText(String.valueOf(course.getCredit()));
        }

        if (course.getTeacher() != null) {
            teacherField.setText(course.getTeacher());
        }

        if (course.getClassHours() != null) {
            classHoursField.setText(String.valueOf(course.getClassHours()));
        }
    }

    private void onConfirm() {
        String courseId = courseIdField.getText().trim();
        String courseName = courseNameField.getText().trim();

        if (courseId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "课程号不能为空", "输入错误", JOptionPane.ERROR_MESSAGE);
            courseIdField.requestFocus();
            return;
        }

        if (courseName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "课程名称不能为空", "输入错误", JOptionPane.ERROR_MESSAGE);
            courseNameField.requestFocus();
            return;
        }

        course = new Course();
        course.setCourseId(courseId);
        course.setCourseName(courseName);

        String creditText = creditField.getText().trim();
        if (!creditText.isEmpty()) {
            try {
                course.setCredit(Double.parseDouble(creditText));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "学分必须是数字", "输入错误", JOptionPane.ERROR_MESSAGE);
                creditField.requestFocus();
                return;
            }
        }

        course.setTeacher(teacherField.getText().trim());

        String classHoursText = classHoursField.getText().trim();
        if (!classHoursText.isEmpty()) {
            try {
                course.setClassHours(Integer.parseInt(classHoursText));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "学时必须是整数", "输入错误", JOptionPane.ERROR_MESSAGE);
                classHoursField.requestFocus();
                return;
            }
        }

        confirmed = true;
        dispose();
    }

    private void onCancel() {
        confirmed = false;
        dispose();
    }

    public Course getCourse() {
        return course;
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
