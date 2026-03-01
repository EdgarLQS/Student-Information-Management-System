package com.student.controller;

import com.student.dao.AttendanceDAO;
import com.student.dao.AttendanceDAOImpl;
import com.student.model.Attendance;

import java.util.List;
import java.util.Map;

/**
 * 考勤控制器 - 业务逻辑层
 */
public class AttendanceController {
    private final AttendanceDAO attendanceDAO;

    public AttendanceController() {
        this.attendanceDAO = new AttendanceDAOImpl();
    }

    /**
     * 获取所有考勤记录
     */
    public List<Attendance> getAllAttendances() {
        return attendanceDAO.getAll();
    }

    /**
     * 根据 ID 获取考勤记录
     */
    public Attendance getAttendanceById(Integer id) {
        return attendanceDAO.getById(id);
    }

    /**
     * 添加考勤记录
     */
    public boolean addAttendance(Attendance attendance) {
        return attendanceDAO.insert(attendance);
    }

    /**
     * 更新考勤记录
     */
    public boolean updateAttendance(Attendance attendance) {
        return attendanceDAO.update(attendance);
    }

    /**
     * 删除考勤记录
     */
    public boolean deleteAttendance(Integer id) {
        return attendanceDAO.delete(id);
    }

    /**
     * 根据学生 ID 获取考勤记录
     */
    public List<Attendance> getAttendancesByStudent(Integer studentId) {
        return attendanceDAO.getByStudentId(studentId);
    }

    /**
     * 根据课程 ID 获取考勤记录
     */
    public List<Attendance> getAttendancesByCourse(Integer courseId) {
        return attendanceDAO.getByCourseId(courseId);
    }

    /**
     * 根据日期范围获取考勤记录
     */
    public List<Attendance> getAttendancesByDateRange(java.sql.Date startDate, java.sql.Date endDate) {
        return attendanceDAO.getByDateRange(startDate, endDate);
    }

    /**
     * 获取学生考勤统计
     */
    public Map<String, Object> getStudentStatistics(Integer studentId) {
        return attendanceDAO.getStudentStatistics(studentId);
    }

    /**
     * 获取课程考勤统计
     */
    public Map<String, Object> getCourseStatistics(Integer courseId) {
        return attendanceDAO.getCourseStatistics(courseId);
    }
}
