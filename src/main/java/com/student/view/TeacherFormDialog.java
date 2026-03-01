package com.student.view;

import com.student.model.Teacher;
import com.student.util.Validator;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * 教师表单对话框 - 用于添加和修改教师信息
 */
public class TeacherFormDialog extends JDialog {
    private JTextField teacherIdField;
    private JTextField nameField;
    private JComboBox<String> genderCombo;
    private JTextField titleField;
    private JTextField departmentField;
    private JTextField phoneField;
    private JTextField emailField;

    private Teacher teacher;
    private boolean isEditMode;
    private boolean confirmed = false;

    public TeacherFormDialog(Frame parent, String title) {
        super(parent, title, true);
        this.isEditMode = false;
        initUI();
    }

    public TeacherFormDialog(Frame parent, String title, Teacher teacher) {
        super(parent, title, true);
        this.isEditMode = true;
        this.teacher = teacher;
        initUI();
        fillData();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setSize(450, 420);
        setLocationRelativeTo(getOwner());
        setResizable(false);
        getContentPane().setBackground(Theme.BACKGROUND_PRIMARY);

        // 表单面板
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Theme.BACKGROUND_PRIMARY);
        formPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 工号
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        formPanel.add(createLabel("工号："), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        teacherIdField = createTextField(!isEditMode);
        formPanel.add(teacherIdField, gbc);

        // 姓名
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        formPanel.add(createLabel("姓名："), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        nameField = createTextField(true);
        formPanel.add(nameField, gbc);

        // 性别
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        formPanel.add(createLabel("性别："), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        genderCombo = new JComboBox<>(new String[]{"请选择", "男", "女"});
        genderCombo.setFont(Theme.FONT_DEFAULT);
        genderCombo.setPreferredSize(new Dimension(0, 30));
        formPanel.add(genderCombo, gbc);

        // 职称
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        formPanel.add(createLabel("职称："), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        titleField = createTextField(true);
        formPanel.add(titleField, gbc);

        // 所属院系
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        formPanel.add(createLabel("所属院系："), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        departmentField = createTextField(true);
        formPanel.add(departmentField, gbc);

        // 电话
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0;
        formPanel.add(createLabel("电话："), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        phoneField = createTextField(true);
        formPanel.add(phoneField, gbc);

        // 邮箱
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0;
        formPanel.add(createLabel("邮箱："), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        emailField = createTextField(true);
        formPanel.add(emailField, gbc);

        add(formPanel, BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        buttonPanel.setBackground(Theme.BACKGROUND_SECONDARY);

        JButton confirmButton = createButton("确定", Theme.PRIMARY_COLOR);
        confirmButton.addActionListener(e -> onConfirm());

        JButton cancelButton = createButton("取消", Theme.BUTTON_DEFAULT_BG);
        cancelButton.addActionListener(e -> onCancel());

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // 回车键确认
        getRootPane().setDefaultButton(confirmButton);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(Theme.FONT_DEFAULT);
        label.setForeground(Theme.TEXT_PRIMARY);
        return label;
    }

    private JTextField createTextField(boolean editable) {
        JTextField textField = new JTextField(20);
        textField.setFont(Theme.FONT_DEFAULT);
        textField.setPreferredSize(new Dimension(0, 30));
        textField.setEditable(editable);
        textField.setBorder(createTextFieldBorder());

        if (!editable) {
            textField.setBackground(Theme.BACKGROUND_SECONDARY);
        }

        return textField;
    }

    private Border createTextFieldBorder() {
        return new CompoundBorder(
            new LineBorder(Theme.BORDER_COLOR, 1),
            new EmptyBorder(0, 8, 0, 8)
        );
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(Theme.FONT_DEFAULT);
        button.setPreferredSize(new Dimension(80, 32));
        button.setBackground(bgColor);
        button.setForeground(bgColor == Theme.BUTTON_DEFAULT_BG ? Theme.TEXT_PRIMARY : Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (bgColor == Theme.BUTTON_DEFAULT_BG) {
            button.setBorder(new LineBorder(Theme.BORDER_COLOR, 1));
        }

        return button;
    }

    private void fillData() {
        if (teacher == null) return;

        teacherIdField.setText(teacher.getTeacherId());
        nameField.setText(teacher.getName());

        if (teacher.getGender() != null) {
            genderCombo.setSelectedItem(teacher.getGender());
        }

        if (teacher.getTitle() != null) {
            titleField.setText(teacher.getTitle());
        }

        if (teacher.getDepartment() != null) {
            departmentField.setText(teacher.getDepartment());
        }

        if (teacher.getPhone() != null) {
            phoneField.setText(teacher.getPhone());
        }

        if (teacher.getEmail() != null) {
            emailField.setText(teacher.getEmail());
        }
    }

    private void onConfirm() {
        // 验证必填字段 - 工号
        String teacherId = teacherIdField.getText().trim();
        if (Validator.isEmpty(teacherId)) {
            JOptionPane.showMessageDialog(this, "工号不能为空", "输入错误", JOptionPane.ERROR_MESSAGE);
            teacherIdField.requestFocus();
            return;
        }
        if (!Validator.isValidTeacherId(teacherId)) {
            JOptionPane.showMessageDialog(this, "工号格式不正确，应为 6-20 位字母或数字", "输入错误", JOptionPane.ERROR_MESSAGE);
            teacherIdField.requestFocus();
            return;
        }

        // 验证必填字段 - 姓名
        String name = nameField.getText().trim();
        if (Validator.isEmpty(name)) {
            JOptionPane.showMessageDialog(this, "姓名不能为空", "输入错误", JOptionPane.ERROR_MESSAGE);
            nameField.requestFocus();
            return;
        }

        // 验证电话号码
        String phone = phoneField.getText().trim();
        if (!phone.isEmpty() && !Validator.isValidPhone(phone)) {
            JOptionPane.showMessageDialog(this, "电话号码格式不正确", "输入错误", JOptionPane.ERROR_MESSAGE);
            phoneField.requestFocus();
            return;
        }

        // 验证邮箱
        String email = emailField.getText().trim();
        if (!email.isEmpty() && !Validator.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "邮箱格式不正确", "输入错误", JOptionPane.ERROR_MESSAGE);
            emailField.requestFocus();
            return;
        }

        // 创建教师对象
        teacher = new Teacher();
        teacher.setTeacherId(teacherId);
        teacher.setName(name);

        String gender = (String) genderCombo.getSelectedItem();
        if (!"请选择".equals(gender)) {
            teacher.setGender(gender);
        }

        teacher.setTitle(titleField.getText().trim());
        teacher.setDepartment(departmentField.getText().trim());
        teacher.setPhone(phone);
        teacher.setEmail(email);

        confirmed = true;
        dispose();
    }

    private void onCancel() {
        confirmed = false;
        dispose();
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
