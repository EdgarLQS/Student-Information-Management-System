package com.student.view;

import com.student.controller.CourseController;
import com.student.model.Course;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * 课程管理面板
 */
public class CourseManagementPanel extends JPanel {
    private CourseController controller;

    private JTable courseTable;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;

    private JTextField searchField;

    private List<Course> currentCourses;

    public CourseManagementPanel() {
        this.controller = new CourseController();
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
        searchButton.addActionListener(e -> searchCourses());
        topPanel.add(searchButton);

        JButton refreshButton = new JButton("刷新");
        refreshButton.addActionListener(e -> loadData());
        topPanel.add(refreshButton);

        JButton addButton = new JButton("添加");
        addButton.addActionListener(e -> addCourse());
        topPanel.add(addButton);

        JButton editButton = new JButton("修改");
        editButton.addActionListener(e -> editCourse());
        topPanel.add(editButton);

        JButton deleteButton = new JButton("删除");
        deleteButton.addActionListener(e -> deleteCourse());
        topPanel.add(deleteButton);

        add(topPanel, BorderLayout.NORTH);

        // 表格
        createTable();
        JScrollPane scrollPane = new JScrollPane(courseTable);
        add(scrollPane, BorderLayout.CENTER);

        // 底部状态栏
        statusLabel = new JLabel("  共 0 条记录");
        statusLabel.setBorder(BorderFactory.createEtchedBorder());
        add(statusLabel, BorderLayout.SOUTH);
    }

    private void createTable() {
        String[] columns = {"ID", "课程号", "课程名称", "学分", "教师", "学时", "创建时间"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        courseTable = new JTable(tableModel);
        courseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        courseTable.setRowHeight(25);
        courseTable.setAutoCreateRowSorter(true);

        courseTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        courseTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        courseTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        courseTable.getColumnModel().getColumn(3).setPreferredWidth(50);
        courseTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        courseTable.getColumnModel().getColumn(5).setPreferredWidth(50);
        courseTable.getColumnModel().getColumn(6).setPreferredWidth(120);
    }

    private void loadData() {
        currentCourses = controller.getAllCourses();
        refreshTable();
        searchField.setText("");
    }

    private void refreshTable() {
        tableModel.setRowCount(0);

        if (currentCourses == null || currentCourses.isEmpty()) {
            statusLabel.setText("  共 0 条记录");
            return;
        }

        for (Course course : currentCourses) {
            Object[] row = {
                    course.getId(),
                    course.getCourseId(),
                    course.getCourseName(),
                    course.getCredit() != null ? course.getCredit() : "",
                    course.getTeacher() != null ? course.getTeacher() : "",
                    course.getClassHours() != null ? course.getClassHours() : "",
                    course.getCreatedAt() != null ? course.getCreatedAt() : ""
            };
            tableModel.addRow(row);
        }

        statusLabel.setText("  共 " + currentCourses.size() + " 条记录");
    }

    private void searchCourses() {
        String keyword = searchField.getText().trim();
        currentCourses = controller.searchCourses(keyword);
        refreshTable();
    }

    private void addCourse() {
        CourseFormDialog dialog = new CourseFormDialog(null, "添加课程");
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Course course = dialog.getCourse();
            if (controller.addCourse(course)) {
                JOptionPane.showMessageDialog(this, "添加成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "添加失败，课程号可能已存在", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请先选择要修改的课程", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer id = (Integer) tableModel.getValueAt(selectedRow, 0);
        Course course = controller.getCourseById(id);

        if (course != null) {
            CourseFormDialog dialog = new CourseFormDialog(null, "修改课程", course);
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                Course updatedCourse = dialog.getCourse();
                updatedCourse.setId(id);

                if (controller.updateCourse(updatedCourse)) {
                    JOptionPane.showMessageDialog(this, "修改成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "修改失败", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void deleteCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请先选择要删除的课程", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer id = (Integer) tableModel.getValueAt(selectedRow, 0);
        Course course = controller.getCourseById(id);

        // 检查是否有学生选修
        if (!controller.getEnrollmentsByCourse(id).isEmpty()) {
            JOptionPane.showMessageDialog(this, "该课程已有学生选修，不能删除", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "确定要删除课程 \"" + (course != null ? course.getCourseName() : "") + "\" 吗？",
                "确认删除",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.deleteCourse(id)) {
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
