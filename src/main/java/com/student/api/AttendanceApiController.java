package com.student.api;

import com.student.common.ApiResponse;
import com.student.controller.AttendanceController;
import com.student.model.Attendance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 考勤管理 REST API 控制器
 */
@RestController
@RequestMapping("/api/attendances")
@CrossOrigin(origins = "*")
public class AttendanceApiController {

    @Autowired
    private AttendanceController attendanceController;

    /**
     * 获取所有考勤记录
     * GET /api/attendances
     */
    @GetMapping
    public ApiResponse<List<Attendance>> getAllAttendances() {
        List<Attendance> attendances = attendanceController.getAllAttendances();
        return ApiResponse.success(attendances);
    }

    /**
     * 根据 ID 获取考勤记录
     * GET /api/attendances/{id}
     */
    @GetMapping("/{id}")
    public ApiResponse<Attendance> getAttendanceById(@PathVariable Integer id) {
        Attendance attendance = attendanceController.getAttendanceById(id);
        if (attendance == null) {
            return ApiResponse.error(404, "考勤记录不存在");
        }
        return ApiResponse.success(attendance);
    }

    /**
     * 根据学生 ID 获取考勤记录
     * GET /api/attendances/student/{studentId}
     */
    @GetMapping("/student/{studentId}")
    public ApiResponse<List<Attendance>> getAttendancesByStudent(@PathVariable Integer studentId) {
        List<Attendance> attendances = attendanceController.getAttendancesByStudent(studentId);
        return ApiResponse.success(attendances);
    }

    /**
     * 根据课程 ID 获取考勤记录
     * GET /api/attendances/course/{courseId}
     */
    @GetMapping("/course/{courseId}")
    public ApiResponse<List<Attendance>> getAttendancesByCourse(@PathVariable Integer courseId) {
        List<Attendance> attendances = attendanceController.getAttendancesByCourse(courseId);
        return ApiResponse.success(attendances);
    }

    /**
     * 根据日期范围获取考勤记录
     * GET /api/attendances/date-range?startDate=2024-01-01&endDate=2024-12-31
     */
    @GetMapping("/date-range")
    public ApiResponse<List<Attendance>> getAttendancesByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") java.util.Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") java.util.Date endDate) {
        List<Attendance> attendances = attendanceController.getAttendancesByDateRange(
                new Date(startDate.getTime()), new Date(endDate.getTime()));
        return ApiResponse.success(attendances);
    }

    /**
     * 添加考勤记录
     * POST /api/attendances
     */
    @PostMapping
    public ApiResponse<Boolean> addAttendance(@RequestBody Attendance attendance) {
        boolean success = attendanceController.addAttendance(attendance);
        if (!success) {
            return ApiResponse.error(400, "考勤记录添加失败");
        }
        return ApiResponse.success("考勤记录添加成功", true);
    }

    /**
     * 更新考勤记录
     * PUT /api/attendances
     */
    @PutMapping
    public ApiResponse<Boolean> updateAttendance(@RequestBody Attendance attendance) {
        boolean success = attendanceController.updateAttendance(attendance);
        if (!success) {
            return ApiResponse.error(400, "考勤记录更新失败");
        }
        return ApiResponse.success("考勤记录更新成功", true);
    }

    /**
     * 删除考勤记录
     * DELETE /api/attendances/{id}
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteAttendance(@PathVariable Integer id) {
        boolean success = attendanceController.deleteAttendance(id);
        if (!success) {
            return ApiResponse.error(400, "考勤记录删除失败");
        }
        return ApiResponse.success("考勤记录删除成功", true);
    }

    /**
     * 获取学生考勤统计
     * GET /api/attendances/statistics/student/{studentId}
     */
    @GetMapping("/statistics/student/{studentId}")
    public ApiResponse<Map<String, Object>> getStudentStatistics(@PathVariable Integer studentId) {
        Map<String, Object> stats = attendanceController.getStudentStatistics(studentId);
        return ApiResponse.success(stats);
    }

    /**
     * 获取课程考勤统计
     * GET /api/attendances/statistics/course/{courseId}
     */
    @GetMapping("/statistics/course/{courseId}")
    public ApiResponse<Map<String, Object>> getCourseStatistics(@PathVariable Integer courseId) {
        Map<String, Object> stats = attendanceController.getCourseStatistics(courseId);
        return ApiResponse.success(stats);
    }
}
