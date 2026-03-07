package com.student;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 学生信息管理系统 - Spring Boot Web 应用入口
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class StudentWebApp {

    public static void main(String[] args) {
        // 初始化数据库（使用原有的 DatabaseHelper）
        try {
            com.student.dao.DatabaseHelper.getInstance();
            System.out.println("数据库初始化成功");
        } catch (Exception e) {
            System.err.println("数据库初始化失败：" + e.getMessage());
            e.printStackTrace();
        }

        // 启动 Spring Boot 应用
        SpringApplication.run(StudentWebApp.class, args);
        System.out.println("================================");
        System.out.println("学生信息管理系统启动成功！");
        System.out.println("API 地址：http://localhost:8080/api");
        System.out.println("================================");
    }
}
