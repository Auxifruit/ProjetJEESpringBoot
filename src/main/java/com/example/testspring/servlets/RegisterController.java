package com.example.testspring.servlets;

import com.example.testspring.entities.*;
import com.example.testspring.services.MajorService;
import com.example.testspring.services.SubjectService;
import com.example.testspring.services.UsersService;
import com.example.testspring.services.UsersToValidateService;
import com.example.testspring.util.GMailer;
import com.example.testspring.util.HashPswdUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/projetSB/RegisterController")
public class RegisterController {

    private MajorService majorService;
    private UsersService usersService;
    private UsersToValidateService usersToValidateService;

    @Autowired
    public RegisterController(MajorService majorService, UsersService usersService, UsersToValidateService usersToValidateService) {
        this.majorService = majorService;
        this.usersService = usersService;
        this.usersToValidateService = usersToValidateService;
    }

    @GetMapping
    protected String showRegisterPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Major> majorList = majorService.getMajors();

        request.setAttribute("majors", majorList);

        return "register";
    }
    @PostMapping
    protected String handleRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String birthdate = request.getParameter("birthdate");
        Role role = Role.valueOf(request.getParameter("role"));
        String majorIdString = request.getParameter("major");

        if((firstName == null || firstName.isEmpty()) || (lastName == null || lastName.isEmpty()) || (email == null || email.isEmpty())
                || (password == null || password.isEmpty()) || (birthdate == null || birthdate.isEmpty()) || (role.equals(Role.student) && (majorIdString == null || majorIdString.isEmpty()))) {
            request.setAttribute("error", "Erreur : Veuillez remplir tous les champs.");
            return "redirect:/projetSB/RegisterController";
        }

        if(usersService.isEmailInTable(email) == true) {
            request.setAttribute("error", "Erreur : L'email est déjà utilisée.");
            return "redirect:/projetSB/RegisterController";
        }

        Userstovalidate user = new Userstovalidate();
        user.setUserToValidatePassword(HashPswdUtil.hashPassword(password));
        user.setUserToValidateLastName(lastName);
        user.setUserToValidateName(firstName);
        user.setUserToValidateEmail(email);
        user.setUserToValidateBirthdate(birthdate);
        user.setUserToValidateRole(role);

        if(majorIdString != null && !majorIdString.isEmpty()) {
            int majorId = Integer.parseInt(majorIdString);
            user.setUserToValidateMajorId(majorId);
        }
        else {
            user.setUserToValidateMajorId(null);
        }

        Boolean error = usersToValidateService.addUserToValidateInTable(user);
        if(error != true) {
            request.setAttribute("error", "Erreur : Erreur lors de l'inscription");
            return "redirect:/projetSB/RegisterController";
        }

        try {
            String subject = "Inscription en attente de validation";
            String body = "Bonjour " + firstName + ",\n\n Votre inscription à notre établissement est en attente de validation. Vous serez informé dès que votre compte sera activé.\n\nCordialement,\nL'équipe pédagogique";

            // Utilisation de GMailer pour envoyer l'email
            GMailer gmailer = new GMailer();
            gmailer.sendMail(subject, body, email);

            return "succes";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/projetSB/RegisterController";
        }
    }
}
