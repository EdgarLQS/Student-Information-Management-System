package com.student.util;

import com.student.util.Validator.ValidationType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Validator 工具类单元测试
 */
@DisplayName("Validator 工具类测试")
class ValidatorTest {

    @Test
    @DisplayName("验证有效邮箱地址")
    void testIsValidEmail_ValidEmails() {
        assertThat(Validator.isValidEmail("test@example.com")).isTrue();
        assertThat(Validator.isValidEmail("user.name@domain.org")).isTrue();
        assertThat(Validator.isValidEmail("test123@test.cn")).isTrue();
        assertThat(Validator.isValidEmail("a+b@domain.co.uk")).isTrue();
    }

    @Test
    @DisplayName("验证无效邮箱地址")
    void testIsValidEmail_InvalidEmails() {
        assertThat(Validator.isValidEmail("invalid")).isFalse();
        assertThat(Validator.isValidEmail("invalid@")).isFalse();
        assertThat(Validator.isValidEmail("@domain.com")).isFalse();
        assertThat(Validator.isValidEmail("test@")).isFalse();
    }

    @Test
    @DisplayName("验证空邮箱地址返回 true")
    void testIsValidEmail_NullOrEmpty() {
        assertThat(Validator.isValidEmail(null)).isTrue();
        assertThat(Validator.isValidEmail("")).isTrue();
        assertThat(Validator.isValidEmail("   ")).isTrue();
    }

    @Test
    @DisplayName("验证有效电话号码")
    void testIsValidPhone_ValidPhones() {
        assertThat(Validator.isValidPhone("13800138000")).isTrue();
        assertThat(Validator.isValidPhone("19912345678")).isTrue();
        assertThat(Validator.isValidPhone("010-12345678")).isTrue();
        assertThat(Validator.isValidPhone("021-87654321")).isTrue();
        assertThat(Validator.isValidPhone("01012345678")).isTrue();
    }

    @Test
    @DisplayName("验证无效电话号码")
    void testIsValidPhone_InvalidPhones() {
        assertThat(Validator.isValidPhone("12345")).isFalse();
        assertThat(Validator.isValidPhone("1234567890123")).isFalse();
        assertThat(Validator.isValidPhone("abc")).isFalse();
    }

    @Test
    @DisplayName("验证空电话号码返回 true")
    void testIsValidPhone_NullOrEmpty() {
        assertThat(Validator.isValidPhone(null)).isTrue();
        assertThat(Validator.isValidPhone("")).isTrue();
        assertThat(Validator.isValidPhone("   ")).isTrue();
    }

    @Test
    @DisplayName("验证有效学号")
    void testIsValidStudentId_Valid() {
        assertThat(Validator.isValidStudentId("2024001")).isTrue();
        assertThat(Validator.isValidStudentId("20240001")).isTrue();
        assertThat(Validator.isValidStudentId("ABC123")).isTrue();
        assertThat(Validator.isValidStudentId("20240001A")).isTrue();
    }

    @Test
    @DisplayName("验证无效学号")
    void testIsValidStudentId_Invalid() {
        assertThat(Validator.isValidStudentId("12345")).isFalse();
        assertThat(Validator.isValidStudentId("123456789012345678901")).isFalse();
        assertThat(Validator.isValidStudentId("abc_def")).isFalse();
    }

    @Test
    @DisplayName("验证空学号返回 false")
    void testIsValidStudentId_NullOrEmpty() {
        assertThat(Validator.isValidStudentId(null)).isFalse();
        assertThat(Validator.isValidStudentId("")).isFalse();
        assertThat(Validator.isValidStudentId("   ")).isFalse();
    }

    @Test
    @DisplayName("验证有效工号")
    void testIsValidTeacherId_Valid() {
        assertThat(Validator.isValidTeacherId("T001")).isFalse(); // 太短
        assertThat(Validator.isValidTeacherId("T00001")).isTrue();
        assertThat(Validator.isValidTeacherId("20240001")).isTrue();
        assertThat(Validator.isValidTeacherId("ABC123")).isTrue();
    }

    @Test
    @DisplayName("验证无效工号")
    void testIsValidTeacherId_Invalid() {
        assertThat(Validator.isValidTeacherId("12345")).isFalse();
        assertThat(Validator.isValidTeacherId("123456789012345678901")).isFalse();
        assertThat(Validator.isValidTeacherId("abc_def")).isFalse();
    }

    @Test
    @DisplayName("验证有效课程号")
    void testIsValidCourseId_Valid() {
        assertThat(Validator.isValidCourseId("CS101")).isTrue();
        assertThat(Validator.isValidCourseId("MA1001")).isTrue();
        assertThat(Validator.isValidCourseId("ABCD")).isTrue();
    }

    @Test
    @DisplayName("验证无效课程号")
    void testIsValidCourseId_Invalid() {
        assertThat(Validator.isValidCourseId("CS1")).isFalse();
        assertThat(Validator.isValidCourseId("CS1234567890123")).isFalse();
        assertThat(Validator.isValidCourseId("abc_def")).isFalse();
    }

    @Test
    @DisplayName("验证有效年龄")
    void testIsValidAge_Valid() {
        assertThat(Validator.isValidAge(1)).isTrue();
        assertThat(Validator.isValidAge(18)).isTrue();
        assertThat(Validator.isValidAge(20)).isTrue();
        assertThat(Validator.isValidAge(150)).isTrue();
    }

    @Test
    @DisplayName("验证无效年龄")
    void testIsValidAge_Invalid() {
        assertThat(Validator.isValidAge(0)).isFalse();
        assertThat(Validator.isValidAge(-1)).isFalse();
        assertThat(Validator.isValidAge(151)).isFalse();
        assertThat(Validator.isValidAge(200)).isFalse();
    }

    @Test
    @DisplayName("验证空年龄返回 true")
    void testIsValidAge_Null() {
        assertThat(Validator.isValidAge(null)).isTrue();
    }

    @Test
    @DisplayName("验证有效成绩")
    void testIsValidScore_Valid() {
        assertThat(Validator.isValidScore(0.0)).isTrue();
        assertThat(Validator.isValidScore(60.0)).isTrue();
        assertThat(Validator.isValidScore(85.5)).isTrue();
        assertThat(Validator.isValidScore(100.0)).isTrue();
    }

    @Test
    @DisplayName("验证无效成绩")
    void testIsValidScore_Invalid() {
        assertThat(Validator.isValidScore(-1.0)).isFalse();
        assertThat(Validator.isValidScore(100.1)).isFalse();
        assertThat(Validator.isValidScore(150.0)).isFalse();
    }

    @Test
    @DisplayName("验证空成绩返回 true")
    void testIsValidScore_Null() {
        assertThat(Validator.isValidScore(null)).isTrue();
    }

    @Test
    @DisplayName("验证有效学分")
    void testIsValidCredit_Valid() {
        assertThat(Validator.isValidCredit(0.0)).isTrue();
        assertThat(Validator.isValidCredit(3.0)).isTrue();
        assertThat(Validator.isValidCredit(4.5)).isTrue();
        assertThat(Validator.isValidCredit(20.0)).isTrue();
    }

    @Test
    @DisplayName("验证无效学分")
    void testIsValidCredit_Invalid() {
        assertThat(Validator.isValidCredit(-0.5)).isFalse();
        assertThat(Validator.isValidCredit(20.1)).isFalse();
        assertThat(Validator.isValidCredit(25.0)).isFalse();
    }

    @Test
    @DisplayName("验证空学分返回 true")
    void testIsValidCredit_Null() {
        assertThat(Validator.isValidCredit(null)).isTrue();
    }

    @Test
    @DisplayName("验证有效课时")
    void testIsValidClassHours_Valid() {
        assertThat(Validator.isValidClassHours(1)).isTrue();
        assertThat(Validator.isValidClassHours(32)).isTrue();
        assertThat(Validator.isValidClassHours(64)).isTrue();
        assertThat(Validator.isValidClassHours(200)).isTrue();
    }

    @Test
    @DisplayName("验证无效课时")
    void testIsValidClassHours_Invalid() {
        assertThat(Validator.isValidClassHours(0)).isFalse();
        assertThat(Validator.isValidClassHours(-1)).isFalse();
        assertThat(Validator.isValidClassHours(201)).isFalse();
    }

    @Test
    @DisplayName("验证空课时返回 true")
    void testIsValidClassHours_Null() {
        assertThat(Validator.isValidClassHours(null)).isTrue();
    }

    @Test
    @DisplayName("验证字符串不为空")
    void testIsNotEmpty() {
        assertThat(Validator.isNotEmpty("test")).isTrue();
        assertThat(Validator.isNotEmpty("  test  ")).isTrue();
        assertThat(Validator.isNotEmpty(null)).isFalse();
        assertThat(Validator.isNotEmpty("")).isFalse();
        assertThat(Validator.isNotEmpty("   ")).isFalse();
    }

    @Test
    @DisplayName("验证字符串为空")
    void testIsEmpty() {
        assertThat(Validator.isEmpty(null)).isTrue();
        assertThat(Validator.isEmpty("")).isTrue();
        assertThat(Validator.isEmpty("   ")).isTrue();
        assertThat(Validator.isEmpty("test")).isFalse();
    }

    @Test
    @DisplayName("测试获取验证错误信息")
    void testGetValidationError() {
        assertThat(Validator.getValidationError("邮箱", "test", ValidationType.EMAIL))
                .contains("邮箱", "格式不正确", "邮箱");
        assertThat(Validator.getValidationError("电话", "test", ValidationType.PHONE))
                .contains("电话", "格式不正确");
        assertThat(Validator.getValidationError("学号", "test", ValidationType.STUDENT_ID))
                .contains("学号", "6-20");
        assertThat(Validator.getValidationError("工号", "test", ValidationType.TEACHER_ID))
                .contains("工号", "6-20");
        assertThat(Validator.getValidationError("课程号", "test", ValidationType.COURSE_ID))
                .contains("课程号", "4-12");
        assertThat(Validator.getValidationError("年龄", "test", ValidationType.AGE))
                .contains("年龄", "1-150");
        assertThat(Validator.getValidationError("成绩", "test", ValidationType.SCORE))
                .contains("成绩", "0-100");
        assertThat(Validator.getValidationError("学分", "test", ValidationType.CREDIT))
                .contains("学分", "0-20");
        assertThat(Validator.getValidationError("姓名", "test", ValidationType.REQUIRED))
                .contains("姓名", "不能为空");
    }
}
