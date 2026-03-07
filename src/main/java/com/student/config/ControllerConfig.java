package com.student.config;

import com.student.controller.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Controller 配置类 - 将现有 Controller 注册为 Spring Bean
 */
@Configuration
public class ControllerConfig {

    @Bean
    public StudentController studentController() {
        return new StudentController();
    }

    @Bean
    public TeacherController teacherController() {
        return new TeacherController();
    }

    @Bean
    public CourseController courseController() {
        return new CourseController();
    }

    @Bean
    public ScoreController scoreController() {
        return new ScoreController();
    }

    @Bean
    public EnrollmentController enrollmentController() {
        return new EnrollmentController();
    }

    @Bean
    public AttendanceController attendanceController() {
        return new AttendanceController();
    }

    @Bean
    public StatisticsController statisticsController() {
        return new StatisticsController();
    }
}
