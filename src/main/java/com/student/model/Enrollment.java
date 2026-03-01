package com.student.model;

import java.time.LocalDate;

/**
 * 选课实体类
 */
public class Enrollment {
    private Integer id;
    private Integer studentId;
    private Integer courseId;
    private String studentName;   // 关联查询字段
    private String courseName;    // 关联查询字段
    private LocalDate enrollDate;
    private String status;

    public Enrollment() {
    }

    public Enrollment(Integer studentId, Integer courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public LocalDate getEnrollDate() {
        return enrollDate;
    }

    public void setEnrollDate(LocalDate enrollDate) {
        this.enrollDate = enrollDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "id=" + id +
                ", studentId=" + studentId +
                ", courseId=" + courseId +
                ", studentName='" + studentName + '\'' +
                ", courseName='" + courseName + '\'' +
                ", enrollDate=" + enrollDate +
                ", status='" + status + '\'' +
                '}';
    }
}
