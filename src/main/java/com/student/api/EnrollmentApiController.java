package com.student.api;

import com.student.common.ApiResponse;
import com.student.controller.EnrollmentController;
import com.student.model.Enrollment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 选课管理 REST API 控制器
 */
@RestController
@RequestMapping("/api/enrollments")
@CrossOrigin(origins = "*")
public class EnrollmentApiController {

    @Autowired
    private EnrollmentController enrollmentController;

    /**
     * 获取所有选课记录
     * GET /api/enrollments
     */
    @GetMapping
    public ApiResponse<List<Enrollment>> getAllEnrollments() {
        List<Enrollment> enrollments = enrollmentController.getAllEnrollments();
        return ApiResponse.success(enrollments);
    }

    /**
     * 根据学生 ID 获取选课记录
     * GET /api/enrollments/student/{studentId}
     */
    @GetMapping("/student/{studentId}")
    public ApiResponse<List<Enrollment>> getEnrollmentsByStudent(@PathVariable Integer studentId) {
        List<Enrollment> enrollments = enrollmentController.getEnrollmentsByStudent(studentId);
        return ApiResponse.success(enrollments);
    }

    /**
     * 根据课程 ID 获取选课记录
     * GET /api/enrollments/course/{courseId}
     */
    @GetMapping("/course/{courseId}")
    public ApiResponse<List<Enrollment>> getEnrollmentsByCourse(@PathVariable Integer courseId) {
        List<Enrollment> enrollments = enrollmentController.getEnrollmentsByCourse(courseId);
        return ApiResponse.success(enrollments);
    }

    /**
     * 学生选课
     * POST /api/enrollments/enroll
     */
    @PostMapping("/enroll")
    public ApiResponse<Boolean> enrollCourse(@RequestBody Map<String, Integer> params) {
        Integer studentId = params.get("studentId");
        Integer courseId = params.get("courseId");

        if (studentId == null || courseId == null) {
            return ApiResponse.error(400, "参数不完整");
        }

        boolean success = enrollmentController.enrollCourse(studentId, courseId);
        if (!success) {
            return ApiResponse.error(400, "选课失败，可能已选过该课程");
        }
        return ApiResponse.success("选课成功", true);
    }

    /**
     * 退课
     * DELETE /api/enrollments/{enrollmentId}
     */
    @DeleteMapping("/{enrollmentId}")
    public ApiResponse<Boolean> dropCourse(@PathVariable Integer enrollmentId) {
        boolean success = enrollmentController.dropCourse(enrollmentId);
        if (!success) {
            return ApiResponse.error(400, "退课失败");
        }
        return ApiResponse.success("退课成功", true);
    }

    /**
     * 检查学生是否已选该课程
     * GET /api/enrollments/check/{studentId}/{courseId}
     */
    @GetMapping("/check/{studentId}/{courseId}")
    public ApiResponse<Map<String, Boolean>> isEnrolled(
            @PathVariable Integer studentId,
            @PathVariable Integer courseId) {
        boolean enrolled = enrollmentController.isEnrolled(studentId, courseId);

        Map<String, Boolean> result = new HashMap<>();
        result.put("enrolled", enrolled);

        return ApiResponse.success(result);
    }
}
