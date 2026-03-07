package com.student.api;

import com.student.common.ApiResponse;
import com.student.controller.StudentController;
import com.student.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学生管理 REST API 控制器
 */
@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentApiController {

    @Autowired
    private StudentController studentController;

    /**
     * 获取所有学生
     * GET /api/students
     */
    @GetMapping
    public ApiResponse<List<Student>> getAllStudents() {
        List<Student> students = studentController.getAllStudents();
        return ApiResponse.success(students);
    }

    /**
     * 根据 ID 获取学生
     * GET /api/students/{id}
     */
    @GetMapping("/{id}")
    public ApiResponse<Student> getStudentById(@PathVariable Integer id) {
        Student student = studentController.getStudentById(id);
        if (student == null) {
            return ApiResponse.error(404, "学生不存在");
        }
        return ApiResponse.success(student);
    }

    /**
     * 根据学号获取学生
     * GET /api/students/by-id/{studentId}
     */
    @GetMapping("/by-id/{studentId}")
    public ApiResponse<Student> getStudentByStudentId(@PathVariable String studentId) {
        Student student = studentController.getStudentByStudentId(studentId);
        if (student == null) {
            return ApiResponse.error(404, "学生不存在");
        }
        return ApiResponse.success(student);
    }

    /**
     * 搜索学生
     * GET /api/students/search?keyword=xxx
     */
    @GetMapping("/search")
    public ApiResponse<List<Student>> searchStudents(@RequestParam String keyword) {
        List<Student> students = studentController.searchStudents(keyword);
        return ApiResponse.success(students);
    }

    /**
     * 分页获取学生
     * GET /api/students?page=1&size=10
     */
    @GetMapping("/page")
    public ApiResponse<Map<String, Object>> getStudentsByPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Student> students = studentController.getStudentsByPage(page, size);
        int total = studentController.getTotalCount();

        Map<String, Object> result = new HashMap<>();
        result.put("list", students);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        result.put("totalPages", (total + size - 1) / size);

        return ApiResponse.success(result);
    }

    /**
     * 添加学生
     * POST /api/students
     */
    @PostMapping
    public ApiResponse<Boolean> addStudent(@RequestBody Student student) {
        if (!studentController.validateStudent(student)) {
            return ApiResponse.error(400, "学生数据验证失败");
        }

        boolean success = studentController.addStudent(student);
        if (!success) {
            return ApiResponse.error(400, "学号已存在");
        }
        return ApiResponse.success("学生添加成功", true);
    }

    /**
     * 更新学生
     * PUT /api/students
     */
    @PutMapping
    public ApiResponse<Boolean> updateStudent(@RequestBody Student student) {
        if (!studentController.validateStudent(student)) {
            return ApiResponse.error(400, "学生数据验证失败");
        }

        boolean success = studentController.updateStudent(student);
        if (!success) {
            return ApiResponse.error(400, "学生更新失败");
        }
        return ApiResponse.success("学生更新成功", true);
    }

    /**
     * 删除学生
     * DELETE /api/students/{id}
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteStudent(@PathVariable Integer id) {
        boolean success = studentController.deleteStudent(id);
        if (!success) {
            return ApiResponse.error(400, "学生删除失败");
        }
        return ApiResponse.success("学生删除成功", true);
    }
}
