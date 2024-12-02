package com.example.testspring.entities;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Entity
@Component
@Table(name="student", schema = "cydatabasesb")
public class Student {
    @Id
    @Column(name = "student_id")
    private int studentId;
    @Basic
    @Column(name = "class_id")
    private Integer classId;
    @Basic
    @Column(name = "major_id")
    private Integer majorId;

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public Integer getMajorId() {
        return majorId;
    }

    public void setMajorId(Integer majorId) {
        this.majorId = majorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return studentId == student.studentId && Objects.equals(classId, student.classId) && Objects.equals(majorId, student.majorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, classId, majorId);
    }
}
