package com.student.dao;

import com.student.model.Score;

import java.util.List;

/**
 * 成绩数据访问接口
 */
public interface ScoreDAO {

    /**
     * 获取所有成绩记录
     */
    List<Score> getAll();

    /**
     * 根据学生 ID 获取成绩
     */
    List<Score> getByStudentId(Integer studentId);

    /**
     * 根据课程 ID 获取成绩
     */
    List<Score> getByCourseId(Integer courseId);

    /**
     * 根据选课 ID 获取成绩
     */
    Score getByEnrollmentId(Integer enrollmentId);

    /**
     * 获取学生成绩（含课程信息）
     */
    List<Score> getWithCourseInfo(Integer studentId);

    /**
     * 获取课程成绩（含学生信息）
     */
    List<Score> getWithStudentInfo(Integer courseId);

    /**
     * 插入或更新成绩
     */
    boolean insertOrUpdate(Integer enrollmentId, Double score);

    /**
     * 删除成绩记录
     */
    boolean delete(Integer id);

    /**
     * 检查成绩是否存在
     */
    boolean exists(Integer enrollmentId);
}
