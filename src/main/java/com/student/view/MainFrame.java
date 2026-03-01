package com.student.view;

import com.student.controller.StudentController;
import com.student.model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * 主窗口
 */
public class MainFrame extends JFrame {
    private StudentController controller;

    private SearchPanel searchPanel;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;

    private List<Student> currentStudents;
    private int currentPage = 1;
    private final int PAGE_SIZE = 10;

    // 内容面板
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public MainFrame() {
        this.controller = new StudentController();
        initUI();
        loadData();
    }

    private void initUI() {
        setTitle("学生信息管理系统");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 创建菜单栏
        createMenuBar();

        setLayout(new BorderLayout(10, 10));

        // 顶部搜索和操作面板
        searchPanel = new SearchPanel();
        searchPanel.setOnSearch(this::searchStudents);
        searchPanel.setOnRefresh(this::loadData);
        searchPanel.setOnAdd(this::addStudent);
        searchPanel.setOnEdit(this::editStudent);
        searchPanel.setOnDelete(this::deleteStudent);
        searchPanel.setOnExport(this::exportData);

        add(searchPanel, BorderLayout.NORTH);

        // 内容区域（使用 CardLayout 切换不同面板）
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // 学生管理面板
        JPanel studentPanel = createStudentPanel();
        contentPanel.add(studentPanel, "student");

        // 课程管理面板
        CourseManagementPanel coursePanel = new CourseManagementPanel();
        contentPanel.add(coursePanel, "course");

        // 选课管理面板
        EnrollmentDialog enrollmentDialog = new EnrollmentDialog(this);
        // 选课功能通过对话框实现，这里放一个提示面板
        JPanel enrollmentPanel = createEnrollmentPanel(enrollmentDialog);
        contentPanel.add(enrollmentPanel, "enrollment");

        // 成绩管理面板
        ScoreManagementPanel scorePanel = new ScoreManagementPanel();
        contentPanel.add(scorePanel, "score");

        // 统计图表面板
        StatisticsPanel statsPanel = new StatisticsPanel();
        contentPanel.add(statsPanel, "statistics");

        add(contentPanel, BorderLayout.CENTER);

        // 状态栏
        statusLabel = new JLabel("  欢迎使用学生信息管理系统");
        statusLabel.setBorder(BorderFactory.createEtchedBorder());
        add(statusLabel, BorderLayout.SOUTH);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // 文件菜单
        JMenu fileMenu = new JMenu("文件 (F)");
        fileMenu.setMnemonic('F');

        JMenuItem exitItem = new JMenuItem("退出系统");
        exitItem.setMnemonic('X');
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        // 学生菜单
        JMenu studentMenu = new JMenu("学生 (S)");
        studentMenu.setMnemonic('S');

        JMenuItem studentItem = new JMenuItem("学生信息管理");
        studentItem.addActionListener(e -> cardLayout.show(contentPanel, "student"));
        studentMenu.add(studentItem);
        menuBar.add(studentMenu);

        // 课程菜单
        JMenu courseMenu = new JMenu("课程 (C)");
        courseMenu.setMnemonic('C');

        JMenuItem courseItem = new JMenuItem("课程信息管理");
        courseItem.addActionListener(e -> cardLayout.show(contentPanel, "course"));
        courseMenu.add(courseItem);

        JMenuItem enrollmentItem = new JMenuItem("选课管理");
        enrollmentItem.addActionListener(e -> {
            EnrollmentDialog dialog = new EnrollmentDialog(this);
            dialog.setVisible(true);
        });
        courseMenu.add(enrollmentItem);

        JMenuItem teacherItem = new JMenuItem("教师信息管理");
        teacherItem.addActionListener(e -> {
            TeacherManagementPanel teacherPanel = new TeacherManagementPanel();
            JDialog teacherDialog = new JDialog(this, "教师信息管理", true);
            teacherDialog.setSize(1000, 600);
            teacherDialog.setLocationRelativeTo(this);
            teacherDialog.add(teacherPanel);
            teacherDialog.setVisible(true);
        });
        courseMenu.add(teacherItem);

        menuBar.add(courseMenu);

        // 成绩菜单
        JMenu scoreMenu = new JMenu("成绩 (G)");
        scoreMenu.setMnemonic('G');

        JMenuItem scoreItem = new JMenuItem("成绩管理");
        scoreItem.addActionListener(e -> cardLayout.show(contentPanel, "score"));
        scoreMenu.add(scoreItem);

        JMenuItem scoreStatsItem = new JMenuItem("成绩统计");
        scoreStatsItem.addActionListener(e -> cardLayout.show(contentPanel, "statistics"));
        scoreMenu.add(scoreStatsItem);

        menuBar.add(scoreMenu);

        // 统计菜单
        JMenu statsMenu = new JMenu("统计 (T)");
        statsMenu.setMnemonic('T');

        JMenuItem classStatsItem = new JMenuItem("班级人数统计");
        classStatsItem.addActionListener(e -> cardLayout.show(contentPanel, "statistics"));
        statsMenu.add(classStatsItem);

        JMenuItem scoreDistItem = new JMenuItem("成绩分布统计");
        scoreDistItem.addActionListener(e -> cardLayout.show(contentPanel, "statistics"));
        statsMenu.add(scoreDistItem);

        menuBar.add(statsMenu);

        // 帮助菜单
        JMenu helpMenu = new JMenu("帮助 (H)");
        helpMenu.setMnemonic('H');

        JMenuItem aboutItem = new JMenuItem("关于");
        aboutItem.addActionListener(e -> showAbout());
        helpMenu.add(aboutItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private JPanel createStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // 中间表格
        createTable();
        JScrollPane scrollPane = new JScrollPane(studentTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // 底部状态栏和分页
        JPanel bottomPanel = new JPanel(new BorderLayout());

        statusLabel = new JLabel("  共 0 条记录");
        statusLabel.setBorder(BorderFactory.createEtchedBorder());
        bottomPanel.add(statusLabel, BorderLayout.WEST);

        JPanel pagePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        JButton prevButton = new JButton("上一页");
        prevButton.addActionListener(e -> prevPage());

        JLabel pageLabel = new JLabel("第 1 页");

        JButton nextButton = new JButton("下一页");
        nextButton.addActionListener(e -> nextPage());

        pagePanel.add(prevButton);
        pagePanel.add(pageLabel);
        pagePanel.add(nextButton);

        bottomPanel.add(pagePanel, BorderLayout.EAST);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        // 表格行点击事件
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = studentTable.getSelectedRow();
                if (selectedRow >= 0) {
                    searchPanel.getEditButton().setEnabled(true);
                    searchPanel.getDeleteButton().setEnabled(true);
                }
            }
        });

        return panel;
    }

    private JPanel createEnrollmentPanel(EnrollmentDialog dialog) {
        JPanel panel = new JPanel(new GridBagLayout());

        JLabel label = new JLabel("请点击菜单 课程 > 选课管理 打开选课对话框");
        label.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));

        JButton openButton = new JButton("打开选课管理");
        openButton.addActionListener(e -> {
            EnrollmentDialog enrollmentDialog = new EnrollmentDialog(this);
            enrollmentDialog.setVisible(true);
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridy = 0;
        panel.add(label, gbc);

        gbc.gridy = 1;
        panel.add(openButton, gbc);

        return panel;
    }

    private void createTable() {
        String[] columns = {"ID", "学号", "姓名", "性别", "年龄", "班级", "电话", "邮箱"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        studentTable = new JTable(tableModel);
        TableUtils.applyStyle(studentTable);
        TableUtils.setCenterRenderer(studentTable, 0, 3, 4);
        TableUtils.setColumnWidths(studentTable, 40, 120, 80, 50, 50, 100, 120, 150);
    }

    private void loadData() {
        currentStudents = controller.getAllStudents();
        refreshTable();
        searchPanel.clearSearch();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);

        if (currentStudents == null || currentStudents.isEmpty()) {
            statusLabel.setText("  共 0 条记录");
            return;
        }

        for (Student student : currentStudents) {
            Object[] row = {
                    student.getId(),
                    student.getStudentId(),
                    student.getName(),
                    student.getGender() != null ? student.getGender() : "",
                    student.getAge() != null ? student.getAge() : "",
                    student.getClassName() != null ? student.getClassName() : "",
                    student.getPhone() != null ? student.getPhone() : "",
                    student.getEmail() != null ? student.getEmail() : ""
            };
            tableModel.addRow(row);
        }

        statusLabel.setText("  共 " + currentStudents.size() + " 条记录");
    }

    private void searchStudents() {
        String keyword = searchPanel.getSearchKeyword();
        currentStudents = controller.searchStudents(keyword);
        refreshTable();
    }

    private void addStudent() {
        StudentFormDialog dialog = new StudentFormDialog(this, "添加学生");
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Student student = dialog.getStudent();
            if (controller.addStudent(student)) {
                JOptionPane.showMessageDialog(this, "添加成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "添加失败，学号可能已存在", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请先选择要修改的学生", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer id = (Integer) tableModel.getValueAt(selectedRow, 0);
        Student student = controller.getStudentById(id);

        if (student != null) {
            StudentFormDialog dialog = new StudentFormDialog(this, "修改学生", student);
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                Student updatedStudent = dialog.getStudent();
                updatedStudent.setId(id);

                if (controller.updateStudent(updatedStudent)) {
                    JOptionPane.showMessageDialog(this, "修改成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "修改失败", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void deleteStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请先选择要删除的学生", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer id = (Integer) tableModel.getValueAt(selectedRow, 0);
        Student student = controller.getStudentById(id);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "确定要删除学生 \"" + (student != null ? student.getName() : "") + "\" 吗？",
                "确认删除",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.deleteStudent(id)) {
                JOptionPane.showMessageDialog(this, "删除成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "删除失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportData() {
        if (currentStudents == null || currentStudents.isEmpty()) {
            JOptionPane.showMessageDialog(this, "没有数据可导出", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("选择保存位置");
        fileChooser.setSelectedFile(new java.io.File("students.csv"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();
            exportToCSV(file);
        }
    }

    private void exportToCSV(java.io.File file) {
        try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(file))) {
            writer.println("ID，学号，姓名，性别，年龄，班级，电话，邮箱");

            for (Student student : currentStudents) {
                writer.printf("%d,%s,%s,%s,%s,%s,%s,%s%n",
                        student.getId(),
                        escapeCsv(student.getStudentId()),
                        escapeCsv(student.getName()),
                        escapeCsv(student.getGender()),
                        student.getAge() != null ? student.getAge() : "",
                        escapeCsv(student.getClassName()),
                        escapeCsv(student.getPhone()),
                        escapeCsv(student.getEmail())
                );
            }

            JOptionPane.showMessageDialog(this, "导出成功：" + file.getAbsolutePath(), "提示", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "导出失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private void prevPage() {
        if (currentPage > 1) {
            currentPage--;
            currentStudents = controller.getStudentsByPage(currentPage, PAGE_SIZE);
            refreshTable();
        }
    }

    private void nextPage() {
        int total = controller.getTotalCount();
        int totalPages = (int) Math.ceil((double) total / PAGE_SIZE);

        if (currentPage < totalPages) {
            currentPage++;
            currentStudents = controller.getStudentsByPage(currentPage, PAGE_SIZE);
            refreshTable();
        }
    }

    private void showAbout() {
        JOptionPane.showMessageDialog(this,
            "学生信息管理系统 v2.0\n\n" +
            "功能：学生管理、课程管理、选课管理、成绩管理、统计图表\n\n" +
            "技术栈：Java 8 + SQLite + Swing + JFreeChart",
            "关于",
            JOptionPane.INFORMATION_MESSAGE);
    }

    public void refresh() {
        loadData();
    }
}
