package com.student.controller;

import com.student.dao.StatisticsDAO;

import java.util.Map;

/**
 * 统计控制器 - 业务逻辑层
 */
public class StatisticsController {
    private final StatisticsDAO statisticsDAO;

    public StatisticsController() {
        this.statisticsDAO = new StatisticsDAO();
    }

    /**
     * 按班级统计学生人数
     */
    public Map<String, Integer> getStudentCountByClass() {
        return statisticsDAO.getStudentCountByClass();
    }

    /**
     * 获取成绩分布
     */
    public Map<String, Integer> getScoreDistribution(Integer courseId) {
        return statisticsDAO.getScoreDistribution(courseId);
    }

    /**
     * 获取各课程平均分
     */
    public Map<String, Double> getCourseAverageScores() {
        return statisticsDAO.getCourseAverageScores();
    }

    /**
     * 获取学生各科成绩
     */
    public Map<String, Double> getStudentScores(Integer studentId) {
        return statisticsDAO.getStudentScores(studentId);
    }

    /**
     * 获取课程统计信息
     */
    public Map<String, Object> getCourseStatistics(Integer courseId) {
        return statisticsDAO.getCourseStatistics(courseId);
    }

    /**
     * 获取学生总数
     */
    public int getTotalStudentCount() {
        return statisticsDAO.getTotalStudentCount();
    }

    /**
     * 获取课程总数
     */
    public int getTotalCourseCount() {
        return statisticsDAO.getTotalCourseCount();
    }

    /**
     * 获取有成绩的学生数
     */
    public int getScoredStudentCount() {
        return statisticsDAO.getScoredStudentCount();
    }
}
