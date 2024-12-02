package com.example.testspring.servlets;

import com.example.testspring.entities.Users;
import com.example.testspring.services.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/projetSB")
public class LoginController {

    private final UsersService usersService;

    @Autowired
    public LoginController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/LoginController")
    public String handleLogin(@RequestParam("email") String email, @RequestParam("password") String password, Model model, HttpServletRequest request) {
        if (email == null || email.isEmpty()) {
            model.addAttribute("error", "Erreur : Veuillez saisir un email.");
            return "login";
        }

        if (password == null || password.isEmpty()) {
            model.addAttribute("error", "Erreur : Veuillez saisir un mot de passe.");
            return "login";
        }

        Integer userId = usersService.userConnection(email, password);
        if (userId == null) {
            model.addAttribute("error", "Erreur : L'email ou le mot de passe est incorrect.");
            return "login";
        }

        Users connectedUser = usersService.getUserById(userId);
        request.getSession().setAttribute("connectedUser", connectedUser);

        return "index";
    }
}
