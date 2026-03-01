package com.student.dao;

import com.student.model.Enrollment;

import java.util.List;

/**
 * 选课数据访问接口
 */
public interface EnrollmentDAO {

    /**
     * 获取所有选课记录
     */
    List<Enrollment> getAll();

    /**
     * 根据学生 ID 获取选课记录
     */
    List<Enrollment> getByStudentId(Integer studentId);

    /**
     * 根据课程 ID 获取选课记录
     */
    List<Enrollment> getByCourseId(Integer courseId);

    /**
     * 获取学生的选课记录（含课程信息）
     */
    List<Enrollment> getWithCourseInfo(Integer studentId);

    /**
     * 插入选课记录
     */
    boolean insert(Enrollment enrollment);

    /**
     * 删除选课记录
     */
    boolean delete(Integer id);

    /**
     * 检查学生是否已选该课程
     */
    boolean exists(Integer studentId, Integer courseId);
}
