package com.student.controller;

import com.student.dao.EnrollmentDAO;
import com.student.dao.EnrollmentDAOImpl;
import com.student.dao.ScoreDAO;
import com.student.dao.ScoreDAOImpl;
import com.student.dao.StatisticsDAO;
import com.student.dao.StudentDAO;
import com.student.dao.StudentDAOImpl;
import com.student.model.Enrollment;
import com.student.model.Score;
import com.student.model.Student;

import java.util.List;
import java.util.Map;

/**
 * 成绩控制器 - 业务逻辑层
 */
public class ScoreController {
    private final ScoreDAO scoreDAO;
    private final EnrollmentDAO enrollmentDAO;
    private final StudentDAO studentDAO;
    private final StatisticsDAO statisticsDAO;

    public ScoreController() {
        this.scoreDAO = new ScoreDAOImpl();
        this.enrollmentDAO = new EnrollmentDAOImpl();
        this.studentDAO = new StudentDAOImpl();
        this.statisticsDAO = new StatisticsDAO();
    }

    /**
     * 获取学生的所有成绩
     */
    public List<Score> getScoresByStudent(Integer studentId) {
        return scoreDAO.getByStudentId(studentId);
    }

    /**
     * 获取课程的所有成绩
     */
    public List<Score> getScoresByCourse(Integer courseId) {
        return scoreDAO.getByCourseId(courseId);
    }

    /**
     * 获取所有成绩记录
     */
    public List<Score> getAllScores() {
        return scoreDAO.getAll();
    }

    /**
     * 录入或更新成绩
     */
    public boolean saveScore(Integer enrollmentId, Double score) {
        if (score < 0 || score > 100) {
            return false;
        }
        return scoreDAO.insertOrUpdate(enrollmentId, score);
    }

    /**
     * 删除成绩
     */
    public boolean deleteScore(Integer id) {
        return scoreDAO.delete(id);
    }

    /**
     * 获取成绩统计信息
     */
    public Map<String, Object> getCourseStatistics(Integer courseId) {
        return statisticsDAO.getCourseStatistics(courseId);
    }

    /**
     * 获取成绩分布
     */
    public Map<String, Integer> getScoreDistribution(Integer courseId) {
        return statisticsDAO.getScoreDistribution(courseId);
    }

    /**
     * 获取所有学生
     */
    public List<Student> getAllStudents() {
        return studentDAO.getAll();
    }

    /**
     * 获取学生的选课记录
     */
    public List<Enrollment> getEnrollmentsByStudent(Integer studentId) {
        return enrollmentDAO.getByStudentId(studentId);
    }

    /**
     * 获取课程平均分统计
     */
    public Map<String, Double> getCourseAverageScores() {
        return statisticsDAO.getCourseAverageScores();
    }

    /**
     * 获取学生成绩（用于雷达图）
     */
    public Map<String, Double> getStudentScores(Integer studentId) {
        return statisticsDAO.getStudentScores(studentId);
    }

    /**
     * 验证成绩
     */
    public boolean validateScore(Double score) {
        return score != null && score >= 0 && score <= 100;
    }
}
