package com.student.view;

import com.student.controller.CourseController;
import com.student.controller.ScoreController;
import com.student.model.Course;
import com.student.model.Score;
import com.student.model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * 成绩管理面板
 */
public class ScoreManagementPanel extends JPanel {
    private ScoreController scoreController;
    private CourseController courseController;

    private JTable scoreTable;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;
    private JLabel statisticsLabel;

    private JComboBox<String> filterTypeCombo;
    private JComboBox<StudentComboItem> studentCombo;
    private JComboBox<CourseComboItem> courseCombo;

    private List<Score> currentScores;

    public ScoreManagementPanel() {
        this.scoreController = new ScoreController();
        this.courseController = new CourseController();
        initUI();
        loadFilterOptions();
        loadScores();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 顶部筛选和操作面板
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        topPanel.add(new JLabel("筛选："));

        filterTypeCombo = new JComboBox<>(new String[]{"按学生", "按课程"});
        filterTypeCombo.addActionListener(e -> onFilterTypeChanged());
        topPanel.add(filterTypeCombo);

        studentCombo = new JComboBox<>();
        studentCombo.setPreferredSize(new Dimension(200, 25));
        studentCombo.addActionListener(e -> loadScores());
        topPanel.add(studentCombo);

        courseCombo = new JComboBox<>();
        courseCombo.setPreferredSize(new Dimension(200, 25));
        courseCombo.addActionListener(e -> loadScores());
        topPanel.add(courseCombo);
        courseCombo.setVisible(false);

        JButton queryButton = new JButton("查询");
        queryButton.addActionListener(e -> loadScores());
        topPanel.add(queryButton);

        JButton enterScoreButton = new JButton("录入成绩");
        enterScoreButton.addActionListener(e -> enterScore());
        topPanel.add(enterScoreButton);

        JButton editButton = new JButton("修改");
        editButton.addActionListener(e -> editScore());
        topPanel.add(editButton);

        JButton exportButton = new JButton("导出");
        exportButton.addActionListener(e -> exportScores());
        topPanel.add(exportButton);

        add(topPanel, BorderLayout.NORTH);

        // 表格
        createTable();
        JScrollPane scrollPane = new JScrollPane(scoreTable);
        add(scrollPane, BorderLayout.CENTER);

        // 底部状态栏和统计
        JPanel bottomPanel = new JPanel(new BorderLayout());

        statusLabel = new JLabel("  共 0 条记录");
        statusLabel.setBorder(BorderFactory.createEtchedBorder());
        bottomPanel.add(statusLabel, BorderLayout.WEST);

        statisticsLabel = new JLabel("  平均分：-  最高分：-  最低分：-  及格率：-");
        statisticsLabel.setBorder(BorderFactory.createEtchedBorder());
        bottomPanel.add(statisticsLabel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void createTable() {
        String[] columns = {"ID", "学号", "姓名", "课程号", "课程名称", "成绩", "录入时间"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        scoreTable = new JTable(tableModel);
        scoreTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scoreTable.setRowHeight(25);
        scoreTable.setAutoCreateRowSorter(true);

        scoreTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        scoreTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        scoreTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        scoreTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        scoreTable.getColumnModel().getColumn(4).setPreferredWidth(120);
        scoreTable.getColumnModel().getColumn(5).setPreferredWidth(60);
        scoreTable.getColumnModel().getColumn(6).setPreferredWidth(120);
    }

    private void loadFilterOptions() {
        // 加载学生列表
        studentCombo.removeAllItems();
        studentCombo.addItem(new StudentComboItem(0, "-- 选择学生 --"));
        List<Student> students = scoreController.getAllStudents();
        for (Student student : students) {
            studentCombo.addItem(new StudentComboItem(student.getId(),
                student.getStudentId() + " - " + student.getName()));
        }

        // 加载课程列表
        courseCombo.removeAllItems();
        courseCombo.addItem(new CourseComboItem(0, "-- 选择课程 --"));
        List<Course> courses = courseController.getAllCourses();
        for (Course course : courses) {
            courseCombo.addItem(new CourseComboItem(course.getId(),
                course.getCourseId() + " - " + course.getCourseName()));
        }
    }

    private void onFilterTypeChanged() {
        String selectedType = (String) filterTypeCombo.getSelectedItem();
        if ("按学生".equals(selectedType)) {
            studentCombo.setVisible(true);
            courseCombo.setVisible(false);
        } else {
            studentCombo.setVisible(false);
            courseCombo.setVisible(true);
        }
        loadScores();
    }

    private void loadScores() {
        String selectedType = (String) filterTypeCombo.getSelectedItem();

        if ("按学生".equals(selectedType)) {
            StudentComboItem selectedStudent = (StudentComboItem) studentCombo.getSelectedItem();
            if (selectedStudent != null && selectedStudent.getId() != 0) {
                currentScores = scoreController.getScoresByStudent(selectedStudent.getId());
            } else {
                currentScores = scoreController.getAllScores();
            }
        } else {
            CourseComboItem selectedCourse = (CourseComboItem) courseCombo.getSelectedItem();
            if (selectedCourse != null && selectedCourse.getId() != 0) {
                currentScores = scoreController.getScoresByCourse(selectedCourse.getId());
                updateStatistics(selectedCourse.getId());
            } else {
                currentScores = scoreController.getAllScores();
                updateStatistics(null);
            }
        }

        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);

        if (currentScores == null || currentScores.isEmpty()) {
            statusLabel.setText("  共 0 条记录");
            statisticsLabel.setText("  平均分：-  最高分：-  最低分：-  及格率：-");
            return;
        }

        for (Score score : currentScores) {
            Object[] row = {
                score.getId(),
                score.getStudentId() != null ? score.getStudentId() : "",
                score.getStudentName() != null ? score.getStudentName() : "",
                score.getCourseId() != null ? score.getCourseId() : "",
                score.getCourseName() != null ? score.getCourseName() : "",
                score.getScore() != null ? score.getScore() : "",
                score.getGradedAt() != null ? score.getGradedAt() : ""
            };
            tableModel.addRow(row);
        }

        statusLabel.setText("  共 " + currentScores.size() + " 条记录");
    }

    private void updateStatistics(Integer courseId) {
        if (courseId != null) {
            java.util.Map<String, Object> stats = scoreController.getCourseStatistics(courseId);
            if (stats != null && !stats.isEmpty()) {
                String text = String.format("  平均分：%.1f  最高分：%.1f  最低分：%.1f  及格率：%.1f%%",
                    stats.getOrDefault("average", 0.0),
                    stats.getOrDefault("highest", 0.0),
                    stats.getOrDefault("lowest", 0.0),
                    stats.getOrDefault("passRate", 0.0));
                statisticsLabel.setText(text);
                return;
            }
        }
        statisticsLabel.setText("  平均分：-  最高分：-  最低分：-  及格率：-");
    }

    private void enterScore() {
        ScoreEntryDialog dialog = new ScoreEntryDialog(null);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            JOptionPane.showMessageDialog(this, "成绩录入成功", "提示", JOptionPane.INFORMATION_MESSAGE);
            loadScores();
        }
    }

    private void editScore() {
        int selectedRow = scoreTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请先选择要修改的成绩", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer id = (Integer) tableModel.getValueAt(selectedRow, 0);
        Score score = currentScores.stream()
            .filter(s -> s.getId().equals(id))
            .findFirst()
            .orElse(null);

        if (score != null) {
            ScoreEntryDialog dialog = new ScoreEntryDialog(null, score);
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                JOptionPane.showMessageDialog(this, "成绩修改成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                loadScores();
            }
        }
    }

    private void exportScores() {
        if (currentScores == null || currentScores.isEmpty()) {
            JOptionPane.showMessageDialog(this, "没有数据可导出", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("选择保存位置");
        fileChooser.setSelectedFile(new java.io.File("scores.csv"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();
            exportToCSV(file);
        }
    }

    private void exportToCSV(java.io.File file) {
        try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(file))) {
            writer.println("学号，姓名，课程号，课程名称，成绩");

            for (Score score : currentScores) {
                writer.printf("%s,%s,%s,%s,%s%n",
                    escapeCsv(score.getStudentId()),
                    escapeCsv(score.getStudentName()),
                    escapeCsv(score.getCourseId()),
                    escapeCsv(score.getCourseName()),
                    score.getScore() != null ? score.getScore() : "");
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
