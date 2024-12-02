package com.example.testspring.entities;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Entity
@Component
@Table(name="users_to_validate", schema = "cydatabasesb")
public class Userstovalidate {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_to_validate_id")
    private int userToValidateId;
    @Basic
    @Column(name = "user_to_validate_password")
    private String userToValidatePassword;
    @Basic
    @Column(name = "user_to_validate_lastname")
    private String userToValidateLastName;
    @Basic
    @Column(name = "user_to_validate_name")
    private String userToValidateName;
    @Basic
    @Column(name = "user_to_validate_email")
    private String userToValidateEmail;
    @Basic
    @Column(name = "user_to_validate_birthdate")
    private String userToValidateBirthdate;
    @Basic
    @Column(name = "user_to_validate_role")
    @Enumerated(EnumType.STRING)
    private Role userToValidateRole;
    @Basic
    @Column(name = "user_to_validate_major_id")
    private Integer userToValidateMajorId;

    public int getUserToValidateId() {
        return userToValidateId;
    }

    public void setUserToValidateId(int userToValidateId) {
        this.userToValidateId = userToValidateId;
    }

    public String getUserToValidatePassword() {
        return userToValidatePassword;
    }

    public void setUserToValidatePassword(String userToValidatePassword) {
        this.userToValidatePassword = userToValidatePassword;
    }

    public String getUserToValidateLastName() {
        return userToValidateLastName;
    }

    public void setUserToValidateLastName(String userToValidateLastName) {
        this.userToValidateLastName = userToValidateLastName;
    }

    public String getUserToValidateName() {
        return userToValidateName;
    }

    public void setUserToValidateName(String userToValidateName) {
        this.userToValidateName = userToValidateName;
    }

    public String getUserToValidateEmail() {
        return userToValidateEmail;
    }

    public void setUserToValidateEmail(String userToValidateEmail) {
        this.userToValidateEmail = userToValidateEmail;
    }

    public String getUserToValidateBirthdate() {
        return userToValidateBirthdate;
    }

    public void setUserToValidateBirthdate(String userToValidateBirthdate) {
        this.userToValidateBirthdate = userToValidateBirthdate;
    }

    public Role getUserToValidateRole() {
        return userToValidateRole;
    }

    public void setUserToValidateRole(Role userToValidateRole) {
        this.userToValidateRole = userToValidateRole;
    }

    public Integer getUserToValidateMajorId() {
        return userToValidateMajorId;
    }

    public void setUserToValidateMajorId(Integer userToValidateMajorId) {
        this.userToValidateMajorId = userToValidateMajorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Userstovalidate that = (Userstovalidate) o;
        return userToValidateId == that.userToValidateId && Objects.equals(userToValidatePassword, that.userToValidatePassword) && Objects.equals(userToValidateLastName, that.userToValidateLastName) && Objects.equals(userToValidateName, that.userToValidateName) && Objects.equals(userToValidateEmail, that.userToValidateEmail) && Objects.equals(userToValidateBirthdate, that.userToValidateBirthdate) && Objects.equals(userToValidateRole, that.userToValidateRole) && Objects.equals(userToValidateMajorId, that.userToValidateMajorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userToValidateId, userToValidatePassword, userToValidateLastName, userToValidateName, userToValidateEmail, userToValidateBirthdate, userToValidateRole, userToValidateMajorId);
    }
}
