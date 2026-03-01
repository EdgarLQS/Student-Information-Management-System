package com.student.controller;

import com.student.dao.StudentDAO;
import com.student.dao.StudentDAOImpl;
import com.student.model.Student;

import java.util.List;

/**
 * 学生控制器 - 业务逻辑层
 */
public class StudentController {
    private final StudentDAO studentDAO;

    public StudentController() {
        this.studentDAO = new StudentDAOImpl();
    }

    /**
     * 获取所有学生
     */
    public List<Student> getAllStudents() {
        return studentDAO.getAll();
    }

    /**
     * 根据 ID 获取学生
     */
    public Student getStudentById(Integer id) {
        return studentDAO.getById(id);
    }

    /**
     * 根据学号获取学生
     */
    public Student getStudentByStudentId(String studentId) {
        return studentDAO.getByStudentId(studentId);
    }

    /**
     * 添加学生
     */
    public boolean addStudent(Student student) {
        // 验证学号是否已存在
        Student existing = studentDAO.getByStudentId(student.getStudentId());
        if (existing != null) {
            return false;
        }
        return studentDAO.insert(student);
    }

    /**
     * 更新学生
     */
    public boolean updateStudent(Student student) {
        return studentDAO.update(student);
    }

    /**
     * 删除学生
     */
    public boolean deleteStudent(Integer id) {
        return studentDAO.delete(id);
    }

    /**
     * 搜索学生
     */
    public List<Student> searchStudents(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllStudents();
        }
        return studentDAO.search(keyword);
    }

    /**
     * 分页获取学生
     */
    public List<Student> getStudentsByPage(int page, int pageSize) {
        return studentDAO.getPage(page, pageSize);
    }

    /**
     * 获取学生总数
     */
    public int getTotalCount() {
        return studentDAO.getCount();
    }

    /**
     * 验证学生数据
     */
    public boolean validateStudent(Student student) {
        if (student == null) {
            return false;
        }

        // 学号不能为空
        if (student.getStudentId() == null || student.getStudentId().trim().isEmpty()) {
            return false;
        }

        // 姓名不能为空
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            return false;
        }

        // 年龄验证
        if (student.getAge() != null && (student.getAge() < 1 || student.getAge() > 150)) {
            return false;
        }

        return true;
    }
}
