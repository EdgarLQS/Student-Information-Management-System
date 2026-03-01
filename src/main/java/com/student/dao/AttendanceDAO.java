package com.student.dao;

import com.student.model.Attendance;

import java.util.List;

/**
 * 考勤数据访问接口
 */
public interface AttendanceDAO {

    /**
     * 获取所有考勤记录
     */
    List<Attendance> getAll();

    /**
     * 根据 ID 获取考勤记录
     */
    Attendance getById(Integer id);

    /**
     * 插入考勤记录
     */
    boolean insert(Attendance attendance);

    /**
     * 更新考勤记录
     */
    boolean update(Attendance attendance);

    /**
     * 删除考勤记录
     */
    boolean delete(Integer id);

    /**
     * 根据学生 ID 获取考勤记录
     */
    List<Attendance> getByStudentId(Integer studentId);

    /**
     * 根据课程 ID 获取考勤记录
     */
    List<Attendance> getByCourseId(Integer courseId);

    /**
     * 根据日期范围获取考勤记录
     */
    List<Attendance> getByDateRange(java.sql.Date startDate, java.sql.Date endDate);

    /**
     * 获取学生考勤统计
     */
    java.util.Map<String, Object> getStudentStatistics(Integer studentId);

    /**
     * 获取课程考勤统计
     */
    java.util.Map<String, Object> getCourseStatistics(Integer courseId);
}
