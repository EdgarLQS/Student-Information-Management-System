package com.student.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 * 数据验证工具类
 */
public class Validator {
    private static final Logger logger = LoggerFactory.getLogger(Validator.class);

    // 正则表达式常量
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
        "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^1[3-9]\\d{9}$|^0\\d{2,3}-?\\d{7,8}$|^\\d{3,4}-?\\d{7,8}$"
    );

    private static final Pattern STUDENT_ID_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9]{6,20}$"
    );

    private static final Pattern TEACHER_ID_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9]{6,20}$"
    );

    private static final Pattern COURSE_ID_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9]{4,12}$"
    );

    /**
     * 验证邮箱格式
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return true; // 邮箱为空是允许的
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * 验证电话号码格式
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return true; // 电话为空是允许的
        }
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    /**
     * 验证学号格式
     */
    public static boolean isValidStudentId(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            return false;
        }
        return STUDENT_ID_PATTERN.matcher(studentId.trim()).matches();
    }

    /**
     * 验证工号格式
     */
    public static boolean isValidTeacherId(String teacherId) {
        if (teacherId == null || teacherId.trim().isEmpty()) {
            return false;
        }
        return TEACHER_ID_PATTERN.matcher(teacherId.trim()).matches();
    }

    /**
     * 验证课程号格式
     */
    public static boolean isValidCourseId(String courseId) {
        if (courseId == null || courseId.trim().isEmpty()) {
            return false;
        }
        return COURSE_ID_PATTERN.matcher(courseId.trim()).matches();
    }

    /**
     * 验证年龄
     */
    public static boolean isValidAge(Integer age) {
        if (age == null) {
            return true; // 年龄为空是允许的
        }
        return age >= 1 && age <= 150;
    }

    /**
     * 验证成绩
     */
    public static boolean isValidScore(Double score) {
        if (score == null) {
            return true; // 成绩为空是允许的
        }
        return score >= 0 && score <= 100;
    }

    /**
     * 验证学分
     */
    public static boolean isValidCredit(Double credit) {
        if (credit == null) {
            return true; // 学分为空是允许的
        }
        return credit >= 0 && credit <= 20;
    }

    /**
     * 验证课时
     */
    public static boolean isValidClassHours(Integer hours) {
        if (hours == null) {
            return true; // 课时为空是允许的
        }
        return hours >= 1 && hours <= 200;
    }

    /**
     * 验证字符串不为空
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    /**
     * 验证字符串为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 获取验证错误信息
     */
    public static String getValidationError(String field, String value, ValidationType type) {
        switch (type) {
            case EMAIL:
                return field + "格式不正确，请输入有效的邮箱地址";
            case PHONE:
                return field + "格式不正确，请输入有效的电话号码（如：13800138000 或 010-12345678）";
            case STUDENT_ID:
                return field + "格式不正确，学号应为 6-20 位字母或数字";
            case TEACHER_ID:
                return field + "格式不正确，工号应为 6-20 位字母或数字";
            case COURSE_ID:
                return field + "格式不正确，课程号应为 4-12 位字母或数字";
            case AGE:
                return field + "格式不正确，年龄应在 1-150 之间";
            case SCORE:
                return field + "格式不正确，成绩应在 0-100 之间";
            case CREDIT:
                return field + "格式不正确，学分应在 0-20 之间";
            case REQUIRED:
                return field + "不能为空";
            default:
                return field + "验证失败";
        }
    }

    /**
     * 验证类型枚举
     */
    public enum ValidationType {
        REQUIRED,
        EMAIL,
        PHONE,
        STUDENT_ID,
        TEACHER_ID,
        COURSE_ID,
        AGE,
        SCORE,
        CREDIT
    }
}
