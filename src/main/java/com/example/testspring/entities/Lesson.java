package com.example.testspring.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.units.qual.N;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Component
@Table(name="lesson", schema = "cydatabasesb")
public class Lesson {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "lesson_id")
    private int lessonId;
    @Basic
    @Column(name = "lesson_start_date", nullable = false)
    @NotNull
    private Timestamp lessonStartDate;
    @Basic
    @Column(name = "lesson_end_date", nullable = false)
    @NotNull
    private Timestamp lessonEndDate;
    @Basic
    @Column(name = "course_id")
    private Integer courseId;
    @Basic
    @Column(name = "teacher_id")
    private Integer teacherId;

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public Timestamp getLessonStartDate() {
        return lessonStartDate;
    }

    public void setLessonStartDate(Timestamp lessonStartDate) {
        this.lessonStartDate = lessonStartDate;
    }

    public Timestamp getLessonEndDate() {
        return lessonEndDate;
    }

    public void setLessonEndDate(Timestamp lessonEndDate) {
        this.lessonEndDate = lessonEndDate;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return lessonId == lesson.lessonId && Objects.equals(lessonStartDate, lesson.lessonStartDate) && Objects.equals(lessonEndDate, lesson.lessonEndDate) && Objects.equals(courseId, lesson.courseId) && Objects.equals(teacherId, lesson.teacherId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lessonId, lessonStartDate, lessonEndDate, courseId, teacherId);
    }
}
