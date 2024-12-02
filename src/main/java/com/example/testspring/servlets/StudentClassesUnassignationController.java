package com.example.testspring.servlets;

import com.example.testspring.entities.Student;
import com.example.testspring.services.StudentService;
import com.example.testspring.services.UsersService;
import com.example.testspring.util.GMailer;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/projetSB/StudentClassesUnassignationController")
public class StudentClassesUnassignationController {

    private final StudentService studentService;
    private final UsersService usersService;

    @Autowired
    public StudentClassesUnassignationController(StudentService studentService, UsersService usersService) {
        this.studentService = studentService;
        this.usersService = usersService;
    }

    @PostMapping
    protected String handleStudentClassesUnassignation(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        String classeIdString = request.getParameter("classesId");
        String studentIdString = request.getParameter("studentId");

        int classeId = Integer.parseInt(classeIdString);

        if(studentIdString == null || studentIdString.isEmpty()) {
            request.setAttribute("erreur", "Erreur : Veuillez choisir un étudiant.");
            return "redirect:/projetSB/StudentClassesManagerController?classesId="+classeId;
        }

        int studentId = Integer.parseInt(studentIdString);
        Student student = studentService.getStudentById(studentId);
        student.setClassId(null);

        if(studentService.addOrUpdateStudent(student) == true) {
            // Si la désaffectation réussie, envoyer un email de notification
            String subject = "Désaffectation de classe";
            String body = "Bonjour " + usersService.getUserById(student.getStudentId()).getUserName() + ",\n\n"
                    + "Nous avons le regret de vous informer que vous n'êtes plus affecté(e) à la classe suivante :\n"
                    + "Classe ID : " + classeId + "\n\n"
                    + "Cordialement,\nL'équipe pédagogique";

            String email = usersService.getUserById(student.getStudentId()).getUserEmail();  // Accéder à l'email de l'étudiant

            try {
                // Envoyer l'email de notification
                GMailer gmailer = new GMailer();
                gmailer.sendMail(subject, body, email);  // Subject, Body, Email
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("erreur", "Erreur : Impossible d'envoyer l'email de notification.");
            }
        }
        else {
            // Si la désaffectation a échoué, afficher une erreur
            request.setAttribute("erreur", "Erreur : Erreur lors de la désassignation");
        }

        return "redirect:/projetSB/StudentClassesManagerController?classesId="+classeId;
    }
}

