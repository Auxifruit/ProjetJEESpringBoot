package com.example.testspring.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.units.qual.N;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Entity
@Component
@Table(name="course", schema = "cydatabasesb")
public class Course {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "course_id")
    private int courseId;
    @Basic
    @Column(name = "course_name", nullable = false)
    @NotNull
    private String courseName;
    @Basic
    @Column(name = "subject_id")
    private Integer subjectId;

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return courseId == course.courseId && Objects.equals(courseName, course.courseName) && Objects.equals(subjectId, course.subjectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, courseName, subjectId);
    }
}
