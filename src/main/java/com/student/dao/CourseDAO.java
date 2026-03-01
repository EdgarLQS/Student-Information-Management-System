package com.student.dao;

import com.student.model.Course;

import java.util.List;

/**
 * 课程数据访问接口
 */
public interface CourseDAO {

    /**
     * 获取所有课程
     */
    List<Course> getAll();

    /**
     * 根据 ID 获取课程
     */
    Course getById(Integer id);

    /**
     * 根据课程号获取课程
     */
    Course getByCourseId(String courseId);

    /**
     * 插入课程
     */
    boolean insert(Course course);

    /**
     * 更新课程
     */
    boolean update(Course course);

    /**
     * 删除课程
     */
    boolean delete(Integer id);

    /**
     * 搜索课程
     */
    List<Course> search(String keyword);

    /**
     * 检查课程号是否已存在
     */
    boolean exists(String courseId);

    /**
     * 检查课程是否有学生选修
     */
    boolean hasEnrollments(Integer courseId);
}
