package com.student.dao;

import com.student.model.Teacher;

import java.util.List;

/**
 * 教师数据访问接口
 */
public interface TeacherDAO {

    /**
     * 获取所有教师
     */
    List<Teacher> getAll();

    /**
     * 根据 ID 获取教师
     */
    Teacher getById(Integer id);

    /**
     * 根据工号获取教师
     */
    Teacher getByTeacherId(String teacherId);

    /**
     * 插入教师
     */
    boolean insert(Teacher teacher);

    /**
     * 更新教师
     */
    boolean update(Teacher teacher);

    /**
     * 删除教师
     */
    boolean delete(Integer id);

    /**
     * 搜索教师（按工号或姓名）
     */
    List<Teacher> search(String keyword);

    /**
     * 分页获取教师
     */
    List<Teacher> getPage(int page, int pageSize);

    /**
     * 获取教师总数
     */
    int getCount();
}
