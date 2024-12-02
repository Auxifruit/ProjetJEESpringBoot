package com.example.testspring.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Entity
@Component
@Table(name="grade", schema = "cydatabasesb")
public class Grade {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "grade_id")
    private int gradeId;
    @Basic
    @Column(name = "grade_name", nullable = false)
    @NotNull
    private String gradeName;
    @Basic
    @Column(name = "grade_value", nullable = false)
    @NotNull
    private Double gradeValue;
    @Basic
    @Column(name = "grade_coefficient", nullable = false)
    @NotNull
    private Integer gradeCoefficient;
    @Basic
    @Column(name = "student_id")
    private Integer studentId;
    @Basic
    @Column(name = "course_id")
    private Integer courseId;
    @Basic
    @Column(name = "teacher_id")
    private Integer teacherId;

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public Double getGradeValue() {
        return gradeValue;
    }

    public void setGradeValue(Double gradeValue) {
        this.gradeValue = gradeValue;
    }

    public Integer getGradeCoefficient() {
        return gradeCoefficient;
    }

    public void setGradeCoefficient(Integer gradeCoefficient) {
        this.gradeCoefficient = gradeCoefficient;
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
        Grade grade = (Grade) o;
        return gradeId == grade.gradeId && Objects.equals(gradeName, grade.gradeName) && Objects.equals(gradeValue, grade.gradeValue) && Objects.equals(gradeCoefficient, grade.gradeCoefficient) && Objects.equals(studentId, grade.studentId) && Objects.equals(courseId, grade.courseId) && Objects.equals(teacherId, grade.teacherId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gradeId, gradeName, gradeValue, gradeCoefficient, studentId, courseId, teacherId);
    }
}
