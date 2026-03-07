package com.student.api;

import com.student.common.ApiResponse;
import com.student.controller.ScoreController;
import com.student.model.Enrollment;
import com.student.model.Score;
import com.student.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 成绩管理 REST API 控制器
 */
@RestController
@RequestMapping("/api/scores")
@CrossOrigin(origins = "*")
public class ScoreApiController {

    @Autowired
    private ScoreController scoreController;

    /**
     * 获取所有成绩记录
     * GET /api/scores
     */
    @GetMapping
    public ApiResponse<List<Score>> getAllScores() {
        List<Score> scores = scoreController.getAllScores();
        return ApiResponse.success(scores);
    }

    /**
     * 获取学生的所有成绩
     * GET /api/scores/student/{studentId}
     */
    @GetMapping("/student/{studentId}")
    public ApiResponse<List<Score>> getScoresByStudent(@PathVariable Integer studentId) {
        List<Score> scores = scoreController.getScoresByStudent(studentId);
        return ApiResponse.success(scores);
    }

    /**
     * 获取课程的所有成绩
     * GET /api/scores/course/{courseId}
     */
    @GetMapping("/course/{courseId}")
    public ApiResponse<List<Score>> getScoresByCourse(@PathVariable Integer courseId) {
        List<Score> scores = scoreController.getScoresByCourse(courseId);
        return ApiResponse.success(scores);
    }

    /**
     * 获取所有学生（用于成绩录入）
     * GET /api/scores/students
     */
    @GetMapping("/students")
    public ApiResponse<List<Student>> getAllStudents() {
        List<Student> students = scoreController.getAllStudents();
        return ApiResponse.success(students);
    }

    /**
     * 获取学生的选课记录
     * GET /api/scores/enrollments/{studentId}
     */
    @GetMapping("/enrollments/{studentId}")
    public ApiResponse<List<Enrollment>> getEnrollmentsByStudent(@PathVariable Integer studentId) {
        List<Enrollment> enrollments = scoreController.getEnrollmentsByStudent(studentId);
        return ApiResponse.success(enrollments);
    }

    /**
     * 录入或更新成绩
     * POST /api/scores
     */
    @PostMapping
    public ApiResponse<Boolean> saveScore(@RequestBody Map<String, Object> params) {
        Integer enrollmentId = (Integer) params.get("enrollmentId");
        Double score = (Double) params.get("score");

        if (enrollmentId == null || score == null) {
            return ApiResponse.error(400, "参数不完整");
        }

        if (!scoreController.validateScore(score)) {
            return ApiResponse.error(400, "成绩必须在 0-100 之间");
        }

        boolean success = scoreController.saveScore(enrollmentId, score);
        if (!success) {
            return ApiResponse.error(400, "成绩录入失败");
        }
        return ApiResponse.success("成绩录入成功", true);
    }

    /**
     * 删除成绩
     * DELETE /api/scores/{id}
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteScore(@PathVariable Integer id) {
        boolean success = scoreController.deleteScore(id);
        if (!success) {
            return ApiResponse.error(400, "成绩删除失败");
        }
        return ApiResponse.success("成绩删除成功", true);
    }

    /**
     * 获取课程成绩统计
     * GET /api/scores/statistics/course/{courseId}
     */
    @GetMapping("/statistics/course/{courseId}")
    public ApiResponse<Map<String, Object>> getCourseStatistics(@PathVariable Integer courseId) {
        Map<String, Object> stats = scoreController.getCourseStatistics(courseId);
        return ApiResponse.success(stats);
    }

    /**
     * 获取成绩分布
     * GET /api/scores/distribution/{courseId}
     */
    @GetMapping("/distribution/{courseId}")
    public ApiResponse<Map<String, Integer>> getScoreDistribution(@PathVariable Integer courseId) {
        Map<String, Integer> distribution = scoreController.getScoreDistribution(courseId);
        return ApiResponse.success(distribution);
    }

    /**
     * 获取各课程平均分
     * GET /api/scores/averages
     */
    @GetMapping("/averages")
    public ApiResponse<Map<String, Double>> getCourseAverageScores() {
        Map<String, Double> averages = scoreController.getCourseAverageScores();
        return ApiResponse.success(averages);
    }

    /**
     * 获取学生成绩（雷达图数据）
     * GET /api/scores/student-scores/{studentId}
     */
    @GetMapping("/student-scores/{studentId}")
    public ApiResponse<Map<String, Double>> getStudentScores(@PathVariable Integer studentId) {
        Map<String, Double> scores = scoreController.getStudentScores(studentId);
        return ApiResponse.success(scores);
    }
}
