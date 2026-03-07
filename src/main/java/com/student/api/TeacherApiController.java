package com.student.api;

import com.student.common.ApiResponse;
import com.student.controller.TeacherController;
import com.student.model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 教师管理 REST API 控制器
 */
@RestController
@RequestMapping("/api/teachers")
@CrossOrigin(origins = "*")
public class TeacherApiController {

    @Autowired
    private TeacherController teacherController;

    /**
     * 获取所有教师
     * GET /api/teachers
     */
    @GetMapping
    public ApiResponse<List<Teacher>> getAllTeachers() {
        List<Teacher> teachers = teacherController.getAllTeachers();
        return ApiResponse.success(teachers);
    }

    /**
     * 根据 ID 获取教师
     * GET /api/teachers/{id}
     */
    @GetMapping("/{id}")
    public ApiResponse<Teacher> getTeacherById(@PathVariable Integer id) {
        Teacher teacher = teacherController.getTeacherById(id);
        if (teacher == null) {
            return ApiResponse.error(404, "教师不存在");
        }
        return ApiResponse.success(teacher);
    }

    /**
     * 根据工号获取教师
     * GET /api/teachers/by-id/{teacherId}
     */
    @GetMapping("/by-id/{teacherId}")
    public ApiResponse<Teacher> getTeacherByTeacherId(@PathVariable String teacherId) {
        Teacher teacher = teacherController.getTeacherByTeacherId(teacherId);
        if (teacher == null) {
            return ApiResponse.error(404, "教师不存在");
        }
        return ApiResponse.success(teacher);
    }

    /**
     * 搜索教师
     * GET /api/teachers/search?keyword=xxx
     */
    @GetMapping("/search")
    public ApiResponse<List<Teacher>> searchTeachers(@RequestParam String keyword) {
        List<Teacher> teachers = teacherController.searchTeachers(keyword);
        return ApiResponse.success(teachers);
    }

    /**
     * 分页获取教师
     * GET /api/teachers?page=1&size=10
     */
    @GetMapping("/page")
    public ApiResponse<Map<String, Object>> getTeachersByPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Teacher> teachers = teacherController.getTeachersByPage(page, size);
        int total = teacherController.getTotalCount();

        Map<String, Object> result = new HashMap<>();
        result.put("list", teachers);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        result.put("totalPages", (total + size - 1) / size);

        return ApiResponse.success(result);
    }

    /**
     * 添加教师
     * POST /api/teachers
     */
    @PostMapping
    public ApiResponse<Boolean> addTeacher(@RequestBody Teacher teacher) {
        if (!teacherController.validateTeacher(teacher)) {
            return ApiResponse.error(400, "教师数据验证失败");
        }

        boolean success = teacherController.addTeacher(teacher);
        if (!success) {
            return ApiResponse.error(400, "工号已存在");
        }
        return ApiResponse.success("教师添加成功", true);
    }

    /**
     * 更新教师
     * PUT /api/teachers
     */
    @PutMapping
    public ApiResponse<Boolean> updateTeacher(@RequestBody Teacher teacher) {
        if (!teacherController.validateTeacher(teacher)) {
            return ApiResponse.error(400, "教师数据验证失败");
        }

        boolean success = teacherController.updateTeacher(teacher);
        if (!success) {
            return ApiResponse.error(400, "教师更新失败");
        }
        return ApiResponse.success("教师更新成功", true);
    }

    /**
     * 删除教师
     * DELETE /api/teachers/{id}
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteTeacher(@PathVariable Integer id) {
        boolean success = teacherController.deleteTeacher(id);
        if (!success) {
            return ApiResponse.error(400, "教师删除失败");
        }
        return ApiResponse.success("教师删除成功", true);
    }
}
