package com.example.testspring.entities;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Entity
@Component
@Table(name="classes", schema = "cydatabasesb")
public class Classes {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "class_id")
    private int classId;
    @Basic
    @Column(name = "class_name")
    private String className;

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Classes classes = (Classes) o;
        return classId == classes.classId && Objects.equals(className, classes.className);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classId, className);
    }
}
