package com.student.api;

import com.student.common.ApiResponse;
import com.student.controller.StatisticsController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 统计管理 REST API 控制器
 */
@RestController
@RequestMapping("/api/statistics")
@CrossOrigin(origins = "*")
public class StatisticsApiController {

    @Autowired
    private StatisticsController statisticsController;

    /**
     * 获取概览统计
     * GET /api/statistics/overview
     */
    @GetMapping("/overview")
    public ApiResponse<Map<String, Object>> getOverview() {
        Map<String, Object> overview = new HashMap<>();
        overview.put("studentCount", statisticsController.getTotalStudentCount());
        overview.put("teacherCount", getTeacherCount());
        overview.put("courseCount", statisticsController.getTotalCourseCount());
        overview.put("scoredStudentCount", statisticsController.getScoredStudentCount());
        return ApiResponse.success(overview);
    }

    /**
     * 获取教师总数（临时方法）
     */
    private int getTeacherCount() {
        try {
            com.student.controller.TeacherController teacherController = new com.student.controller.TeacherController();
            return teacherController.getAllTeachers().size();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 按班级统计学生人数
     * GET /api/statistics/student-count-by-class
     */
    @GetMapping("/student-count-by-class")
    public ApiResponse<Map<String, Integer>> getStudentCountByClass() {
        Map<String, Integer> stats = statisticsController.getStudentCountByClass();
        return ApiResponse.success(stats);
    }

    /**
     * 获取成绩分布
     * GET /api/statistics/score-distribution/{courseId}
     */
    @GetMapping("/score-distribution/{courseId}")
    public ApiResponse<Map<String, Integer>> getScoreDistribution(@PathVariable Integer courseId) {
        Map<String, Integer> distribution = statisticsController.getScoreDistribution(courseId);
        return ApiResponse.success(distribution);
    }

    /**
     * 获取各课程平均分
     * GET /api/statistics/course-average-scores
     */
    @GetMapping("/course-average-scores")
    public ApiResponse<Map<String, Double>> getCourseAverageScores() {
        Map<String, Double> averages = statisticsController.getCourseAverageScores();
        return ApiResponse.success(averages);
    }

    /**
     * 获取学生各科成绩
     * GET /api/statistics/student-scores/{studentId}
     */
    @GetMapping("/student-scores/{studentId}")
    public ApiResponse<Map<String, Double>> getStudentScores(@PathVariable Integer studentId) {
        Map<String, Double> scores = statisticsController.getStudentScores(studentId);
        return ApiResponse.success(scores);
    }

    /**
     * 获取课程统计信息
     * GET /api/statistics/course/{courseId}
     */
    @GetMapping("/course/{courseId}")
    public ApiResponse<Map<String, Object>> getCourseStatistics(@PathVariable Integer courseId) {
        Map<String, Object> stats = statisticsController.getCourseStatistics(courseId);
        return ApiResponse.success(stats);
    }
}
