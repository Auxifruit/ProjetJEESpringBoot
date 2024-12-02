package com.example.testspring.servlets;

import com.example.testspring.entities.Userstovalidate;
import com.example.testspring.services.*;
import com.example.testspring.util.GMailer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/projetSB/RefuseUserController")
public class RefuseUserController {

    private final UsersToValidateService usersToValidateService;


    @Autowired
    public RefuseUserController(UsersToValidateService usersToValidateService) {
        this.usersToValidateService = usersToValidateService;
    }

    @PostMapping
    protected String handleRefuseUser(HttpServletRequest request, HttpServletResponse response) {
        String userToValidateIdString = request.getParameter("userToValidateId");

        if(userToValidateIdString == null || userToValidateIdString.isEmpty()) {
            request.setAttribute("erreur", "Erreur : Veuillez choisir un utilisateur.");
            return "redirect:/projetSB/UserToValidateManagerController";
        }

        int userToValidateId = Integer.parseInt(userToValidateIdString);
        Userstovalidate user = usersToValidateService.getUserToValidateById(userToValidateId);

        if (user != null) {
            String subject = "Votre inscription a été refusée";
            String body = "Bonjour " + user.getUserToValidateName() + ",\n\n" +
                    "Nous vous informons que votre inscription a été refusée. Si vous avez des questions, vous pouvez contacter notre équipe.\n\n" +
                    "Cordialement,\nL'équipe pédagogique";
            String email = user.getUserToValidateEmail();

            try {
                GMailer gmailer = new GMailer();
                gmailer.sendMail(subject, body, email);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("erreur", "Erreur : Impossible d'envoyer l'email de notification.");
            }

            boolean deletionSuccess = usersToValidateService.deleteUserToValidate(userToValidateId);

            if (deletionSuccess) {
                return "redirect:/projetSB/UserToValidateManagerController";
            } else {
                request.setAttribute("erreur", "Erreur : Impossible de refuser l'utilisateur.");
                return "redirect:/projetSB/UserToValidateManagerController";
            }
        } else {
            request.setAttribute("erreur", "Erreur : Utilisateur introuvable.");
            return "redirect:/projetSB/UserToValidateManagerController";
        }
    }
}
