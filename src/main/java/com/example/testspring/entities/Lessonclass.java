package com.example.testspring.entities;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Entity
@Component
@Table(name="lesson_class", schema = "cydatabasesb")
public class Lessonclass {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "lesson_class_id")
    private int lessonClassId;
    @Basic
    @Column(name = "lesson_id")
    private Integer lessonId;
    @Basic
    @Column(name = "class_id")
    private Integer classId;

    public int getLessonClassId() {
        return lessonClassId;
    }

    public void setLessonClassId(int lessonClassId) {
        this.lessonClassId = lessonClassId;
    }

    public Integer getLessonId() {
        return lessonId;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lessonclass that = (Lessonclass) o;
        return lessonClassId == that.lessonClassId && Objects.equals(lessonId, that.lessonId) && Objects.equals(classId, that.classId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lessonClassId, lessonId, classId);
    }
}
