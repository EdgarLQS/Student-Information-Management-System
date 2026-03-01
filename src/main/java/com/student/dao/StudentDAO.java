package com.student.dao;

import com.student.model.Student;

import java.util.List;

/**
 * 学生数据访问接口
 */
public interface StudentDAO {

    /**
     * 获取所有学生
     */
    List<Student> getAll();

    /**
     * 根据 ID 获取学生
     */
    Student getById(Integer id);

    /**
     * 根据学号获取学生
     */
    Student getByStudentId(String studentId);

    /**
     * 插入学生
     */
    boolean insert(Student student);

    /**
     * 更新学生
     */
    boolean update(Student student);

    /**
     * 删除学生
     */
    boolean delete(Integer id);

    /**
     * 搜索学生（按学号或姓名）
     */
    List<Student> search(String keyword);

    /**
     * 分页获取学生
     */
    List<Student> getPage(int page, int pageSize);

    /**
     * 获取学生总数
     */
    int getCount();
}
