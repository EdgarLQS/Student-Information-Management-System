package com.student.api;

import com.student.common.ApiResponse;
import com.student.controller.CourseController;
import com.student.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课程管理 REST API 控制器
 */
@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*")
public class CourseApiController {

    @Autowired
    private CourseController courseController;

    /**
     * 获取所有课程
     * GET /api/courses
     */
    @GetMapping
    public ApiResponse<List<Course>> getAllCourses() {
        List<Course> courses = courseController.getAllCourses();
        return ApiResponse.success(courses);
    }

    /**
     * 根据 ID 获取课程
     * GET /api/courses/{id}
     */
    @GetMapping("/{id}")
    public ApiResponse<Course> getCourseById(@PathVariable Integer id) {
        Course course = courseController.getCourseById(id);
        if (course == null) {
            return ApiResponse.error(404, "课程不存在");
        }
        return ApiResponse.success(course);
    }

    /**
     * 根据课程号获取课程
     * GET /api/courses/by-id/{courseId}
     */
    @GetMapping("/by-id/{courseId}")
    public ApiResponse<Course> getCourseByCourseId(@PathVariable String courseId) {
        Course course = courseController.getCourseByCourseId(courseId);
        if (course == null) {
            return ApiResponse.error(404, "课程不存在");
        }
        return ApiResponse.success(course);
    }

    /**
     * 搜索课程
     * GET /api/courses/search?keyword=xxx
     */
    @GetMapping("/search")
    public ApiResponse<List<Course>> searchCourses(@RequestParam String keyword) {
        List<Course> courses = courseController.searchCourses(keyword);
        return ApiResponse.success(courses);
    }

    /**
     * 添加课程
     * POST /api/courses
     */
    @PostMapping
    public ApiResponse<Boolean> addCourse(@RequestBody Course course) {
        if (!courseController.validateCourse(course)) {
            return ApiResponse.error(400, "课程数据验证失败");
        }

        boolean success = courseController.addCourse(course);
        if (!success) {
            return ApiResponse.error(400, "课程号已存在");
        }
        return ApiResponse.success("课程添加成功", true);
    }

    /**
     * 更新课程
     * PUT /api/courses
     */
    @PutMapping
    public ApiResponse<Boolean> updateCourse(@RequestBody Course course) {
        if (!courseController.validateCourse(course)) {
            return ApiResponse.error(400, "课程数据验证失败");
        }

        boolean success = courseController.updateCourse(course);
        if (!success) {
            return ApiResponse.error(400, "课程更新失败");
        }
        return ApiResponse.success("课程更新成功", true);
    }

    /**
     * 删除课程
     * DELETE /api/courses/{id}
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteCourse(@PathVariable Integer id) {
        boolean success = courseController.deleteCourse(id);
        if (!success) {
            return ApiResponse.error(400, "课程删除失败，可能有学生已选此课程");
        }
        return ApiResponse.success("课程删除成功", true);
    }

    /**
     * 获取课程的选课学生
     * GET /api/courses/{id}/enrollments
     */
    @GetMapping("/{id}/enrollments")
    public ApiResponse<List<?>> getEnrollmentsByCourse(@PathVariable Integer id) {
        List<?> enrollments = courseController.getEnrollmentsByCourse(id);
        return ApiResponse.success(enrollments);
    }

    /**
     * 分页获取课程
     * GET /api/courses?page=1&size=10
     */
    @GetMapping("/page")
    public ApiResponse<Map<String, Object>> getCoursesByPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Course> courses = courseController.searchCourses("");
        int total = courses.size();
        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, total);

        List<Course> pageCourses = fromIndex < total ? courses.subList(fromIndex, toIndex) : courses;

        Map<String, Object> result = new HashMap<>();
        result.put("list", pageCourses);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        result.put("totalPages", (total + size - 1) / size);

        return ApiResponse.success(result);
    }
}
