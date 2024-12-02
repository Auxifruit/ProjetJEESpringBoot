package com.example.testspring.entities;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Entity
@Component
@Table(name="teacher", schema = "cydatabasesb")
public class Teacher {
    @Id
    @Column(name = "teacher_id")
    private int teacherId;

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return teacherId == teacher.teacherId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(teacherId);
    }
}
