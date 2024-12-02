package com.example.testspring.servlets;

import com.example.testspring.entities.Role;
import com.example.testspring.entities.Users;
import com.example.testspring.services.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/projetSB/UserDeletionController")
public class UserDeletionController extends HttpServlet {

    private final UsersService usersService;

    @Autowired
    public UserDeletionController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @PostMapping
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Users user = (Users) session.getAttribute("connectedUser");
        String userIdString = request.getParameter("userId");

        if(userIdString == null || userIdString.isEmpty()) {
            request.setAttribute("erreur", "Erreur : Veuillez choisir un utilisateur.");
            request.getRequestDispatcher("/projetSB/UserManagerController?roleFilter=student").forward(request, response);
            return;
        }

        int userId = Integer.parseInt(userIdString);
        Users userToDelete = usersService.getUserById(userId);
        Role role = user.getUserRole();

        if(user.getUserId() == userId) {
            request.setAttribute("erreur", "Erreur : Vous ne pouvez pas vous supprimer vous-même.");
            request.getRequestDispatcher("/projetSB/UserManagerController?roleFilter="+role).forward(request, response);
            return;
        }

        if(userToDelete == null) {
            request.setAttribute("erreur", "Erreur : L'utilisateur n'existe pas.");
            request.getRequestDispatcher("/projetSB/UserManagerController?roleFilter="+role).forward(request, response);
            return;
        }

        if(usersService.deleteUser(userToDelete.getUserId()) == true) {
            request.getRequestDispatcher("/projetSB/UserManagerController?roleFilter="+role).forward(request, response);
        }
        else {
            request.setAttribute("erreur", "Erreur : Erreur lors de la suppression de l'utilisateur.");
            request.getRequestDispatcher("/projetSB/UserManagerController?roleFilter="+role).forward(request, response);
        }

    }
}
