package com.student.view;

import com.student.controller.CourseController;
import com.student.controller.EnrollmentController;
import com.student.controller.ScoreController;
import com.student.model.Course;
import com.student.model.Enrollment;
import com.student.model.Score;
import com.student.model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * 成绩录入对话框
 */
public class ScoreEntryDialog extends JDialog {
    private JComboBox<StudentComboItem> studentCombo;
    private JComboBox<CourseComboItem> courseCombo;
    private JTable scoreTable;
    private DefaultTableModel tableModel;

    private ScoreController scoreController;
    private CourseController courseController;
    private EnrollmentController enrollmentController;

    private Score existingScore;
    private boolean confirmed = false;

    public ScoreEntryDialog(Frame parent) {
        super(parent, "成绩录入", true);
        init();
    }

    public ScoreEntryDialog(Frame parent, Score existingScore) {
        super(parent, "成绩修改", true);
        this.existingScore = existingScore;
        init();
    }

    private void init() {
        this.scoreController = new ScoreController();
        this.courseController = new CourseController();
        this.enrollmentController = new EnrollmentController();

        setLayout(new BorderLayout(10, 10));
        setSize(600, 450);
        setLocationRelativeTo(getOwner());

        // 顶部筛选面板
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBorder(BorderFactory.createTitledBorder("筛选"));

        topPanel.add(new JLabel("学生："));
        studentCombo = new JComboBox<>();
        studentCombo.setPreferredSize(new Dimension(200, 25));
        studentCombo.addActionListener(e -> loadScores());
        topPanel.add(studentCombo);

        topPanel.add(new JLabel("课程："));
        courseCombo = new JComboBox<>();
        courseCombo.setPreferredSize(new Dimension(200, 25));
        courseCombo.addActionListener(e -> loadScores());
        topPanel.add(courseCombo);

        add(topPanel, BorderLayout.NORTH);

        // 成绩表格
        createTable();
        JScrollPane scrollPane = new JScrollPane(scoreTable);
        add(scrollPane, BorderLayout.CENTER);

        // 底部按钮
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JButton saveButton = new JButton("保存");
        saveButton.addActionListener(e -> saveScores());
        buttonPanel.add(saveButton);

        JButton closeButton = new JButton("关闭");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // 加载数据
        loadStudents();
        loadCourses();
        loadScores();
    }

    private void createTable() {
        String[] columns = {"学号", "姓名", "课程名称", "当前成绩", "录入成绩"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        scoreTable = new JTable(tableModel);
        scoreTable.setRowHeight(25);
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

    private void loadScores() {
        tableModel.setRowCount(0);

        StudentComboItem selectedStudent = (StudentComboItem) studentCombo.getSelectedItem();
        CourseComboItem selectedCourse = (CourseComboItem) courseCombo.getSelectedItem();

        if (selectedStudent == null || selectedStudent.getId() == 0) {
            return;
        }

        List<Enrollment> enrollments = enrollmentController.getEnrollmentsByStudent(selectedStudent.getId());

        for (Enrollment enrollment : enrollments) {
            if (selectedCourse != null && selectedCourse.getId() != 0) {
                if (!enrollment.getCourseId().equals(selectedCourse.getId())) {
                    continue;
                }
            }

            List<Score> scores = scoreController.getScoresByStudent(enrollment.getStudentId());
            Score score = null;
            for (Score s : scores) {
                if (s.getEnrollmentId().equals(enrollment.getId())) {
                    score = s;
                    break;
                }
            }

            Object[] row = {
                enrollment.getStudentName() != null ? enrollment.getStudentName() : "",
                "",
                enrollment.getCourseName() != null ? enrollment.getCourseName() : "",
                score != null && score.getScore() != null ? String.format("%.1f", score.getScore()) : "-",
                score != null && score.getScore() != null ? String.format("%.1f", score.getScore()) : ""
            };
            tableModel.addRow(row);
        }
    }

    private void saveScores() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String scoreText = (String) tableModel.getValueAt(i, 4);
            if (scoreText != null && !scoreText.trim().isEmpty()) {
                try {
                    double score = Double.parseDouble(scoreText.trim());
                    if (score < 0 || score > 100) {
                        JOptionPane.showMessageDialog(this,
                            "成绩必须在 0-100 之间", "输入错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this,
                        "成绩必须是数字", "输入错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }

        confirmed = true;
        JOptionPane.showMessageDialog(this, "成绩保存成功", "提示", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

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
