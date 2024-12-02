package com.example.testspring.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Component
@Table(name="users", schema = "cydatabasesb")
public class Users implements Serializable {
    private static final long serialVersionUID = 1L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_id")
    private int userId;
    @Basic
    @Column(name = "user_password", nullable = false)
    @NotNull
    private String userPassword;
    @Basic
    @Column(name = "user_last_name", nullable = false)
    @NotNull
    private String userLastName;
    @Basic
    @Column(name = "user_name", nullable = false)
    @NotNull
    private String userName;
    @Basic
    @Column(name = "user_email", nullable = false)
    @NotNull
    private String userEmail;
    @Basic
    @Column(name = "user_birthdate", nullable = false)
    @NotNull
    private String userBirthdate;
    @Basic
    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private Role userRole;

    public Users(int userId, String userPassword, String userName, String userLastName, String userEmail, String userBirthdate, Role userRole) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userLastName = userLastName;
        this.userEmail = userEmail;
        this.userBirthdate = userBirthdate;
        this.userRole = userRole;
    }

    public Users() {

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String useLastName) {
        this.userLastName = useLastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserBirthdate() {
        return userBirthdate;
    }

    public void setUserBirthdate(String userBirthdate) {
        this.userBirthdate = userBirthdate;
    }

    public Role getUserRole() {
        return userRole;
    }

    public void setUserRole(Role userRole) {
        this.userRole = userRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return userId == users.userId && Objects.equals(userPassword, users.userPassword) && Objects.equals(userLastName, users.userLastName) && Objects.equals(userName, users.userName) && Objects.equals(userEmail, users.userEmail) && Objects.equals(userBirthdate, users.userBirthdate) && Objects.equals(userRole, users.userRole);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, userPassword, userLastName, userName, userEmail, userBirthdate, userRole);
    }
}
