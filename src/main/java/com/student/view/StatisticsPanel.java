package com.student.view;

import com.student.controller.StatisticsController;
import com.student.controller.CourseController;
import com.student.controller.ScoreController;
import com.student.model.Course;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * 统计图表面板
 */
public class StatisticsPanel extends JPanel {
    private StatisticsController statisticsController;
    private CourseController courseController;
    private ScoreController scoreController;

    private JComboBox<String> chartTypeCombo;
    private JComboBox<CourseComboItem> courseCombo;
    private JPanel chartPanel;

    public StatisticsPanel() {
        this.statisticsController = new StatisticsController();
        this.courseController = new CourseController();
        this.scoreController = new ScoreController();
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 顶部控制面板
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBorder(BorderFactory.createTitledBorder("图表控制"));

        topPanel.add(new JLabel("图表类型："));
        chartTypeCombo = new JComboBox<>(new String[]{
            "班级人数统计",
            "成绩分布统计",
            "课程平均分对比"
        });
        chartTypeCombo.addActionListener(e -> generateChart());
        topPanel.add(chartTypeCombo);

        topPanel.add(new JLabel("  "));

        topPanel.add(new JLabel("课程："));
        courseCombo = new JComboBox<>();
        courseCombo.setPreferredSize(new Dimension(250, 25));
        courseCombo.addActionListener(e -> generateChart());
        topPanel.add(courseCombo);

        JButton generateButton = new JButton("刷新图表");
        generateButton.addActionListener(e -> generateChart());
        topPanel.add(generateButton);

        add(topPanel, BorderLayout.NORTH);

        // 图表显示区域
        chartPanel = new JPanel();
        chartPanel.setLayout(new BorderLayout());
        chartPanel.setBorder(BorderFactory.createEtchedBorder());
        chartPanel.setBackground(Color.WHITE);

        JLabel welcomeLabel = new JLabel("请选择图表类型生成统计图表", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));
        chartPanel.add(welcomeLabel, BorderLayout.CENTER);

        add(chartPanel, BorderLayout.CENTER);

        // 加载课程列表
        loadCourses();

        // 生成初始图表
        generateChart();
    }

    private void loadCourses() {
        courseCombo.removeAllItems();
        courseCombo.addItem(new CourseComboItem(0, "全部课程"));

        for (Course course : courseController.getAllCourses()) {
            courseCombo.addItem(new CourseComboItem(course.getId(),
                course.getCourseId() + " - " + course.getCourseName()));
        }
    }

    private void generateChart() {
        chartPanel.removeAll();

        String chartType = (String) chartTypeCombo.getSelectedItem();

        if ("班级人数统计".equals(chartType)) {
            showClassStatsChart();
        } else if ("成绩分布统计".equals(chartType)) {
            showScoreDistributionChart();
        } else if ("课程平均分对比".equals(chartType)) {
            showCourseAverageChart();
        }

        chartPanel.revalidate();
        chartPanel.repaint();
    }

    private void showClassStatsChart() {
        Map<String, Integer> data = statisticsController.getStudentCountByClass();

        if (data.isEmpty()) {
            chartPanel.add(new JLabel("暂无数据", SwingConstants.CENTER), BorderLayout.CENTER);
            return;
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            dataset.addValue(entry.getValue(), "学生人数", entry.getKey());
        }

        JFreeChart chart = ChartFactory.createBarChart(
            "班级人数统计图",
            "班级",
            "人数",
            dataset,
            org.jfree.chart.plot.PlotOrientation.VERTICAL,
            false, true, false
        );

        styleChart(chart);

        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(600, 400));
        chartPanel.add(panel, BorderLayout.CENTER);
    }

    private void showScoreDistributionChart() {
        CourseComboItem selectedCourse = (CourseComboItem) courseCombo.getSelectedItem();
        Integer courseId = (selectedCourse != null && selectedCourse.getId() != 0)
            ? selectedCourse.getId() : null;

        Map<String, Integer> data = statisticsController.getScoreDistribution(courseId);

        if (data.isEmpty() || data.values().stream().allMatch(v -> v == 0)) {
            chartPanel.add(new JLabel("暂无成绩数据", SwingConstants.CENTER), BorderLayout.CENTER);
            return;
        }

        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("90-100 分 (优秀)", data.getOrDefault("90-100", 0));
        dataset.setValue("80-89 分 (良好)", data.getOrDefault("80-89", 0));
        dataset.setValue("70-79 分 (中等)", data.getOrDefault("70-79", 0));
        dataset.setValue("60-69 分 (及格)", data.getOrDefault("60-69", 0));
        dataset.setValue("<60 分 (不及格)", data.getOrDefault("<60", 0));

        JFreeChart chart = ChartFactory.createPieChart(
            "成绩分布图" + (courseId != null ? " - " + selectedCourse.toString() : ""),
            dataset,
            true, true, false
        );

        styleChart(chart);

        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(600, 400));
        chartPanel.add(panel, BorderLayout.CENTER);
    }

    private void showCourseAverageChart() {
        Map<String, Double> data = statisticsController.getCourseAverageScores();

        if (data.isEmpty()) {
            chartPanel.add(new JLabel("暂无成绩数据", SwingConstants.CENTER), BorderLayout.CENTER);
            return;
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Double> entry : data.entrySet()) {
            dataset.addValue(entry.getValue(), "平均分", entry.getKey());
        }

        JFreeChart chart = ChartFactory.createBarChart(
            "课程平均分对比图",
            "课程",
            "平均分",
            dataset,
            org.jfree.chart.plot.PlotOrientation.VERTICAL,
            false, true, false
        );

        styleChart(chart);

        // 设置 Y 轴范围 0-100
        org.jfree.chart.plot.CategoryPlot plot = chart.getCategoryPlot();
        org.jfree.chart.axis.ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setRange(0, 100);

        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(600, 400));
        chartPanel.add(panel, BorderLayout.CENTER);
    }

    private void styleChart(JFreeChart chart) {
        chart.getTitle().setFont(new Font("Microsoft YaHei", Font.BOLD, 16));

        org.jfree.chart.plot.CategoryPlot plot = chart.getCategoryPlot();
        if (plot != null) {
            plot.getDomainAxis().setLabelFont(new Font("Microsoft YaHei", Font.PLAIN, 12));
            plot.getRangeAxis().setLabelFont(new Font("Microsoft YaHei", Font.PLAIN, 12));
        }

        org.jfree.chart.plot.Plot chartPlot = chart.getPlot();
        if (chartPlot instanceof org.jfree.chart.plot.PiePlot) {
            ((org.jfree.chart.plot.PiePlot) chartPlot).setLabelFont(new Font("Microsoft YaHei", Font.PLAIN, 12));
        }
    }

    // 内部类
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
