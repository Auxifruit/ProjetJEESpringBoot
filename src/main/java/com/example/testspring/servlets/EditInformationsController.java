package com.example.testspring.servlets;

import com.example.testspring.entities.Users;
import com.example.testspring.util.HashPswdUtil;

import com.example.testspring.services.UsersService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/projetSB/EditInformationsController")
public class EditInformationsController {

    private final UsersService usersService;

    @Autowired
    public EditInformationsController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping
    public String handleEditInformations(HttpServletRequest request, HttpServletResponse response) {
        // Récupérer les informations du formulaire
        String email = request.getParameter("email");
        String lastName = request.getParameter("nom");
        String name = request.getParameter("prenom");
        String birthDate = request.getParameter("dateNaissance");
        String password = request.getParameter("motDePasse");
        String hashedPassword = HashPswdUtil.hashPassword(password);

        // ID de l'utilisateur connecté
        String userIdStr = request.getParameter("userId");
        int userId;

        try {
            userId = Integer.parseInt(userIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("message", "ID utilisateur invalide.");
            return "redirect:/projetSB/PersonalInformationController";
        }

        // Validation des champs obligatoires
        if (email == null || email.trim().isEmpty() || lastName == null || lastName.trim().isEmpty()) {
            request.setAttribute("message", "Certains champs obligatoires sont vides.");
            return "redirect:/projetSB/PersonalInformationController";
        }

        Users user = usersService.getUserById(userId);
        user.setUserPassword(hashedPassword);
        user.setUserLastName(lastName);
        user.setUserName(name);
        user.setUserEmail(email);
        user.setUserBirthdate(birthDate);

        String error = usersService.addUser(user);
        if (error != null) {
            Users updatedUser = usersService.getUserById(userId);
            HttpSession session = request.getSession(true);
            session.invalidate();
            session.setAttribute("connectedUser", updatedUser);

            request.setAttribute("message", "Les informations ont été mises à jour avec succès.");
        } else {
            request.setAttribute("message", "Erreur : il y a eu une erreur lors de la modification de vos informations.");
        }

        return "redirect:/projetSB/PersonalInformationController";
    }
}

