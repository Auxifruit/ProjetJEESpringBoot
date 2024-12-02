package com.example.testspring.entities;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Entity
@Component
@Table(name = "major", schema = "cydatabasesb")
public class Major {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "major_id")
    private int majorId;
    @Basic
    @Column(name = "major_name")
    private String majorName;

    public int getMajorId() {
        return majorId;
    }

    public void setMajorId(int majorId) {
        this.majorId = majorId;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Major major = (Major) o;
        return majorId == major.majorId && Objects.equals(majorName, major.majorName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(majorId, majorName);
    }
}
