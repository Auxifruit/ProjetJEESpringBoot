package com.example.testspring.entities;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Entity
@Component
@Table(name="subjects", schema = "cydatabasesb")
public class Subjects {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "subject_id")
    private int subjectId;
    @Basic
    @Column(name = "subject_name")
    private String subjectName;

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subjects subjects = (Subjects) o;
        return subjectId == subjects.subjectId && Objects.equals(subjectName, subjects.subjectName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subjectId, subjectName);
    }
}
