package com.example.testspring.util;

import com.example.testspring.entities.Role;
import com.example.testspring.entities.Users;
import jakarta.servlet.http.HttpSession;

public class UserUtils {
    public static boolean isAdmin(HttpSession session) {
        Users connectedUser = (Users) session.getAttribute("connectedUser");
        return connectedUser != null && Role.administrator.equals(connectedUser.getUserRole());
    }
}