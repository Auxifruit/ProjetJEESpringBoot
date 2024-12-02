package com.example.testspring.servlets;

import com.example.testspring.entities.Student;
import com.example.testspring.entities.Teacher;
import com.example.testspring.entities.Users;
import com.example.testspring.entities.Userstovalidate;
import com.example.testspring.services.*;
import com.example.testspring.util.GMailer;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/projetSB/ValidateUserController")
public class ValidateUserController extends HttpServlet {
    private final UsersToValidateService usersToValidateService;
    private final UsersService usersService;
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final MajorService majorService;

    @Autowired
    public ValidateUserController(UsersToValidateService usersToValidateService, UsersService usersService, TeacherService teacherService, StudentService studentService, MajorService majorService) {
        this.usersToValidateService = usersToValidateService;
        this.usersService = usersService;
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.majorService = majorService;
    }

    @PostMapping
    protected String handleValidateUsers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userToValidateIdString = request.getParameter("userToValidateId");

        if(userToValidateIdString == null || userToValidateIdString.isEmpty()) {
            request.setAttribute("erreur", "Erreur : Veuillez choisir un utilisateur.");
            return "redirect:/projetSB/UserToValidateManagerController";
        }

        int userToValidateId = Integer.parseInt(userToValidateIdString);

        Userstovalidate userstovalidate = usersToValidateService.getUserToValidateById(userToValidateId);
        if(userstovalidate == null) {
            request.setAttribute("erreur", "Erreur : L'utilisateur n'existe pas.");
            return "redirect:/projetSB/UserToValidateManagerController";
        }

        Users user = new Users();
        user.setUserPassword(userstovalidate.getUserToValidatePassword());
        user.setUserLastName(userstovalidate.getUserToValidateLastName());
        user.setUserName(userstovalidate.getUserToValidateName());
        user.setUserEmail(userstovalidate.getUserToValidateEmail());
        user.setUserBirthdate(userstovalidate.getUserToValidateBirthdate());
        user.setUserRole(userstovalidate.getUserToValidateRole());

        String error = usersService.addUser(user);
        if(error != null) {
            request.setAttribute("erreur", error);
            return "redirect:/projetSB/UserToValidateManagerController";
        }

        switch (user.getUserRole()) {
            case student:
                Student student = new Student();
                student.setStudentId(user.getUserId());
                student.setMajorId(userstovalidate.getUserToValidateMajorId());

                studentService.addOrUpdateStudent(student);
                break;
            case teacher:
                Teacher teacher = new Teacher();
                teacher.setTeacherId(user.getUserId());

                teacherService.addOrUpdateTeacher(teacher);
                break;
            default:
                request.setAttribute("erreur", "Erreur : Erreur dans le rôle de l'utilisateur");
                return "redirect:/projetSB/UserToValidateManagerController";
        }

        // L'utilisateur est maintenant validé, on peut envoyer l'email
        String subject = "Votre inscription a été validée";
        String body = "Bonjour " + user.getUserName() + ",\n\n" +
                "Nous avons le plaisir de vous informer que votre inscription a été validée.\n" +
                "Vous pouvez maintenant accéder à votre espace étudiant.\n\n" +
                "Cordialement,\nL'équipe pédagogique";
        String email = user.getUserEmail();

        // Envoi de l'email via GMailer
        try {
            GMailer gmailer = new GMailer();
            gmailer.sendMail(subject, body, email);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erreur", "Erreur : Impossible d'envoyer l'email de validation.");
            return "redirect:/projetSB/UserToValidateManagerController";
        }

        if(usersToValidateService.deleteUserToValidate(userToValidateId) == false) {
            request.setAttribute("erreur", "Erreur : Erreur lors du refus de l'utilisateur.");
            request.getRequestDispatcher("userToValidateManager-servlet").forward(request, response);
        }

        return "redirect:/projetSB/UserToValidateManagerController";
    }
}
