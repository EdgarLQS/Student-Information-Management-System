package com.student.controller;

import com.student.dao.TeacherDAO;
import com.student.dao.TeacherDAOImpl;
import com.student.model.Teacher;

import java.util.List;

/**
 * 教师控制器 - 业务逻辑层
 */
public class TeacherController {
    private final TeacherDAO teacherDAO;

    public TeacherController() {
        this.teacherDAO = new TeacherDAOImpl();
    }

    /**
     * 获取所有教师
     */
    public List<Teacher> getAllTeachers() {
        return teacherDAO.getAll();
    }

    /**
     * 根据 ID 获取教师
     */
    public Teacher getTeacherById(Integer id) {
        return teacherDAO.getById(id);
    }

    /**
     * 根据工号获取教师
     */
    public Teacher getTeacherByTeacherId(String teacherId) {
        return teacherDAO.getByTeacherId(teacherId);
    }

    /**
     * 添加教师
     */
    public boolean addTeacher(Teacher teacher) {
        // 验证工号是否已存在
        Teacher existing = teacherDAO.getByTeacherId(teacher.getTeacherId());
        if (existing != null) {
            return false;
        }
        return teacherDAO.insert(teacher);
    }

    /**
     * 更新教师
     */
    public boolean updateTeacher(Teacher teacher) {
        return teacherDAO.update(teacher);
    }

    /**
     * 删除教师
     */
    public boolean deleteTeacher(Integer id) {
        return teacherDAO.delete(id);
    }

    /**
     * 搜索教师
     */
    public List<Teacher> searchTeachers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllTeachers();
        }
        return teacherDAO.search(keyword);
    }

    /**
     * 分页获取教师
     */
    public List<Teacher> getTeachersByPage(int page, int pageSize) {
        return teacherDAO.getPage(page, pageSize);
    }

    /**
     * 获取教师总数
     */
    public int getTotalCount() {
        return teacherDAO.getCount();
    }

    /**
     * 验证教师数据
     */
    public boolean validateTeacher(Teacher teacher) {
        if (teacher == null) {
            return false;
        }

        // 工号不能为空
        if (teacher.getTeacherId() == null || teacher.getTeacherId().trim().isEmpty()) {
            return false;
        }

        // 姓名不能为空
        if (teacher.getName() == null || teacher.getName().trim().isEmpty()) {
            return false;
        }

        return true;
    }
}
