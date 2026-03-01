package com.student.view;

import com.student.model.Student;
import com.student.util.Validator;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * 学生表单对话框 - 用于添加和修改学生信息
 */
public class StudentFormDialog extends JDialog {
    private JTextField studentIdField;
    private JTextField nameField;
    private JComboBox<String> genderCombo;
    private JTextField ageField;
    private JTextField classNameField;
    private JTextField phoneField;
    private JTextField emailField;

    private Student student;
    private boolean isEditMode;
    private boolean confirmed = false;

    // 必填字段标记颜色
    private static final Color REQUIRED_COLOR = new Color(255, 100, 100);

    public StudentFormDialog(Frame parent, String title) {
        super(parent, title, true);
        this.isEditMode = false;
        initUI();
    }

    public StudentFormDialog(Frame parent, String title, Student student) {
        super(parent, title, true);
        this.isEditMode = true;
        this.student = student;
        initUI();
        fillData();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setSize(450, 400);
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

        // 学号
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        formPanel.add(createRequiredLabel("学号："), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        studentIdField = createTextField(!isEditMode);
        studentIdField.putClientProperty("Field", "学号");
        formPanel.add(studentIdField, gbc);

        // 姓名
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        formPanel.add(createRequiredLabel("姓名："), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        nameField = createTextField(true);
        nameField.putClientProperty("Field", "姓名");
        formPanel.add(nameField, gbc);

        // 性别
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        formPanel.add(new JLabel("性别："), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        genderCombo = new JComboBox<>(new String[]{"请选择", "男", "女"});
        genderCombo.setFont(Theme.FONT_DEFAULT);
        genderCombo.setPreferredSize(new Dimension(0, 30));
        formPanel.add(genderCombo, gbc);

        // 年龄
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        formPanel.add(new JLabel("年龄："), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        ageField = createTextField(true);
        ageField.putClientProperty("Field", "年龄");
        formPanel.add(ageField, gbc);

        // 班级
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        formPanel.add(new JLabel("班级："), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        classNameField = createTextField(true);
        classNameField.putClientProperty("Field", "班级");
        formPanel.add(classNameField, gbc);

        // 电话
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0;
        formPanel.add(new JLabel("电话："), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        phoneField = createTextField(true);
        phoneField.putClientProperty("Field", "电话");
        formPanel.add(phoneField, gbc);

        // 邮箱
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0;
        formPanel.add(new JLabel("邮箱："), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        emailField = createTextField(true);
        emailField.putClientProperty("Field", "邮箱");
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

    /**
     * 创建必填字段标签
     */
    private JLabel createRequiredLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(Theme.FONT_DEFAULT);
        label.setForeground(Theme.TEXT_PRIMARY);
        return label;
    }

    /**
     * 创建文本字段
     */
    private JTextField createTextField(boolean editable) {
        JTextField textField = new JTextField(20);
        textField.setFont(Theme.FONT_DEFAULT);
        textField.setPreferredSize(new Dimension(0, 30));
        textField.setEditable(editable);
        textField.setBorder(createTextFieldBorder());

        // 设置占位符颜色
        if (!editable) {
            textField.setBackground(Theme.BACKGROUND_SECONDARY);
        }

        return textField;
    }

    /**
     * 创建文本字段边框
     */
    private Border createTextFieldBorder() {
        return new CompoundBorder(
            new LineBorder(Theme.BORDER_COLOR, 1),
            new EmptyBorder(0, 8, 0, 8)
        );
    }

    /**
     * 创建按钮
     */
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
        if (student == null) return;

        studentIdField.setText(student.getStudentId());
        nameField.setText(student.getName());

        if (student.getGender() != null) {
            genderCombo.setSelectedItem(student.getGender());
        }

        if (student.getAge() != null) {
            ageField.setText(String.valueOf(student.getAge()));
        }

        if (student.getClassName() != null) {
            classNameField.setText(student.getClassName());
        }

        if (student.getPhone() != null) {
            phoneField.setText(student.getPhone());
        }

        if (student.getEmail() != null) {
            emailField.setText(student.getEmail());
        }
    }

    private void onConfirm() {
        // 验证必填字段 - 学号
        String studentId = studentIdField.getText().trim();
        if (Validator.isEmpty(studentId)) {
            JOptionPane.showMessageDialog(this, "学号不能为空", "输入错误", JOptionPane.ERROR_MESSAGE);
            studentIdField.requestFocus();
            return;
        }
        if (!Validator.isValidStudentId(studentId)) {
            JOptionPane.showMessageDialog(this, "学号格式不正确，应为 6-20 位字母或数字", "输入错误", JOptionPane.ERROR_MESSAGE);
            studentIdField.requestFocus();
            return;
        }

        // 验证必填字段 - 姓名
        String name = nameField.getText().trim();
        if (Validator.isEmpty(name)) {
            JOptionPane.showMessageDialog(this, "姓名不能为空", "输入错误", JOptionPane.ERROR_MESSAGE);
            nameField.requestFocus();
            return;
        }

        // 验证年龄
        String ageText = ageField.getText().trim();
        Integer age = null;
        if (!ageText.isEmpty()) {
            try {
                age = Integer.parseInt(ageText);
                if (!Validator.isValidAge(age)) {
                    JOptionPane.showMessageDialog(this, "年龄应在 1-150 之间", "输入错误", JOptionPane.ERROR_MESSAGE);
                    ageField.requestFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "年龄必须是数字", "输入错误", JOptionPane.ERROR_MESSAGE);
                ageField.requestFocus();
                return;
            }
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

        // 创建学生对象
        student = new Student();
        student.setStudentId(studentId);
        student.setName(name);

        String gender = (String) genderCombo.getSelectedItem();
        if (!"请选择".equals(gender)) {
            student.setGender(gender);
        }

        student.setAge(age);
        student.setClassName(classNameField.getText().trim());
        student.setPhone(phone);
        student.setEmail(email);

        confirmed = true;
        dispose();
    }

    private void onCancel() {
        confirmed = false;
        dispose();
    }

    public Student getStudent() {
        return student;
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
