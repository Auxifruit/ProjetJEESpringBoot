package com.example.testspring.entities;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Entity
@Component
@Table(name="administrator", schema = "cydatabasesb")
public class Administrator {
    @Id
    @Column(name = "administrator_id")
    private int administratorId;

    public int getAdministratorId() {
        return administratorId;
    }

    public void setAdministratorId(int administratorId) {
        this.administratorId = administratorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Administrator that = (Administrator) o;
        return administratorId == that.administratorId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(administratorId);
    }
}
