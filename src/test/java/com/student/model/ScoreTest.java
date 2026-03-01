package com.student.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Score 实体类单元测试
 */
@DisplayName("Score 实体类测试")
class ScoreTest {

    @Test
    @DisplayName("测试无参构造函数")
    void testNoArgsConstructor() {
        Score score = new Score();
        assertThat(score).isNotNull();
        assertThat(score.getScore()).isNull();
    }

    @Test
    @DisplayName("测试带参构造函数")
    void testParameterizedConstructor() {
        Score score = new Score(1, 85.5);
        assertThat(score.getEnrollmentId()).isEqualTo(1);
        assertThat(score.getScore()).isEqualTo(85.5);
    }

    @Test
    @DisplayName("测试所有 getter 和 setter")
    void testGettersAndSetters() {
        Score score = new Score();

        score.setId(1);
        assertThat(score.getId()).isEqualTo(1);

        score.setEnrollmentId(1);
        assertThat(score.getEnrollmentId()).isEqualTo(1);

        score.setScore(85.5);
        assertThat(score.getScore()).isEqualTo(85.5);

        score.setStudentId("2024001");
        assertThat(score.getStudentId()).isEqualTo("2024001");

        score.setStudentName("张三");
        assertThat(score.getStudentName()).isEqualTo("张三");

        score.setCourseId("CS101");
        assertThat(score.getCourseId()).isEqualTo("CS101");

        score.setCourseName("Java 程序设计");
        assertThat(score.getCourseName()).isEqualTo("Java 程序设计");

        java.util.Date date = new java.util.Date();
        score.setGradedAt(date);
        assertThat(score.getGradedAt()).isEqualTo(date);
    }
}
