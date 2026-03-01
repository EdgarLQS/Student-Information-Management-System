package com.student.view;

import com.student.controller.TeacherController;
import com.student.model.Teacher;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * 教师管理面板
 */
public class TeacherManagementPanel extends JPanel {
    private TeacherController controller;

    private JTable teacherTable;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;

    private JTextField searchField;
    private List<Teacher> currentTeachers;

    public TeacherManagementPanel() {
        this.controller = new TeacherController();
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 顶部操作面板
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        topPanel.add(new JLabel("搜索："));
        searchField = new JTextField(15);
        topPanel.add(searchField);

        JButton searchButton = new JButton("搜索");
        searchButton.addActionListener(e -> searchTeachers());
        topPanel.add(searchButton);

        JButton refreshButton = new JButton("刷新");
        refreshButton.addActionListener(e -> loadData());
        topPanel.add(refreshButton);

        JButton addButton = new JButton("添加");
        addButton.addActionListener(e -> addTeacher());
        topPanel.add(addButton);

        JButton editButton = new JButton("修改");
        editButton.addActionListener(e -> editTeacher());
        topPanel.add(editButton);

        JButton deleteButton = new JButton("删除");
        deleteButton.addActionListener(e -> deleteTeacher());
        topPanel.add(deleteButton);

        add(topPanel, BorderLayout.NORTH);

        // 表格
        createTable();
        JScrollPane scrollPane = new JScrollPane(teacherTable);
        add(scrollPane, BorderLayout.CENTER);

        // 底部状态栏
        statusLabel = new JLabel("  共 0 条记录");
        statusLabel.setBorder(BorderFactory.createEtchedBorder());
        add(statusLabel, BorderLayout.SOUTH);
    }

    private void createTable() {
        String[] columns = {"ID", "工号", "姓名", "性别", "职称", "所属院系", "电话", "邮箱"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        teacherTable = new JTable(tableModel);
        TableUtils.applyStyle(teacherTable);
        TableUtils.setCenterRenderer(teacherTable, 0, 3);
        TableUtils.setColumnWidths(teacherTable, 40, 100, 80, 50, 80, 120, 120, 150);

        // 表格行点击事件
        teacherTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = teacherTable.getSelectedRow();
                // 可以在这里启用/禁用编辑删除按钮
            }
        });
    }

    private void loadData() {
        currentTeachers = controller.getAllTeachers();
        refreshTable();
        searchField.setText("");
    }

    private void refreshTable() {
        tableModel.setRowCount(0);

        if (currentTeachers == null || currentTeachers.isEmpty()) {
            statusLabel.setText("  共 0 条记录");
            return;
        }

        for (Teacher teacher : currentTeachers) {
            Object[] row = {
                    teacher.getId(),
                    teacher.getTeacherId(),
                    teacher.getName(),
                    teacher.getGender() != null ? teacher.getGender() : "",
                    teacher.getTitle() != null ? teacher.getTitle() : "",
                    teacher.getDepartment() != null ? teacher.getDepartment() : "",
                    teacher.getPhone() != null ? teacher.getPhone() : "",
                    teacher.getEmail() != null ? teacher.getEmail() : ""
            };
            tableModel.addRow(row);
        }

        statusLabel.setText("  共 " + currentTeachers.size() + " 条记录");
    }

    private void searchTeachers() {
        String keyword = searchField.getText().trim();
        currentTeachers = controller.searchTeachers(keyword);
        refreshTable();
    }

    private void addTeacher() {
        TeacherFormDialog dialog = new TeacherFormDialog(null, "添加教师");
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Teacher teacher = dialog.getTeacher();
            if (controller.addTeacher(teacher)) {
                JOptionPane.showMessageDialog(this, "添加成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "添加失败，工号可能已存在", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editTeacher() {
        int selectedRow = teacherTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请先选择要修改的教师", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer id = (Integer) tableModel.getValueAt(selectedRow, 0);
        Teacher teacher = controller.getTeacherById(id);

        if (teacher != null) {
            TeacherFormDialog dialog = new TeacherFormDialog(null, "修改教师", teacher);
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                Teacher updatedTeacher = dialog.getTeacher();
                updatedTeacher.setId(id);

                if (controller.updateTeacher(updatedTeacher)) {
                    JOptionPane.showMessageDialog(this, "修改成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "修改失败", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void deleteTeacher() {
        int selectedRow = teacherTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请先选择要删除的教师", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer id = (Integer) tableModel.getValueAt(selectedRow, 0);
        Teacher teacher = controller.getTeacherById(id);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "确定要删除教师 \"" + (teacher != null ? teacher.getName() : "") + "\" 吗？",
                "确认删除",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.deleteTeacher(id)) {
                JOptionPane.showMessageDialog(this, "删除成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "删除失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void refresh() {
        loadData();
    }
}
