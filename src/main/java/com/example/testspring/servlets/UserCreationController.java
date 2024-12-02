package com.example.testspring.servlets;

import com.example.testspring.entities.*;
import com.example.testspring.services.*;
import com.example.testspring.util.GMailer;
import com.example.testspring.util.HashPswdUtil;
import jakarta.servlet.ServletException;
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
@RequestMapping("/projetSB/UserCreationController")
public class UserCreationController {
    private final UsersService usersService;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final AdminService adminService;

    @Autowired
    public UserCreationController(UsersService usersService, StudentService studentService, TeacherService teacherService, AdminService adminService) {
        this.usersService = usersService;
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.adminService = adminService;
    }

    @GetMapping
    protected String showUserCreationPage(HttpServletRequest request, HttpServletResponse response) {
        List<Users> usersList = usersService.getAllUsers();

        request.setAttribute("users", usersList);
        return "userCreation";
    }

    @PostMapping
    protected String handleUserCreation(HttpServletRequest request, HttpServletResponse response) {
        String newUserLastName = request.getParameter("newUserLastName");
        String newUserName = request.getParameter("newUserName");
        String newUserEmail = request.getParameter("newUserEmail");
        String newUserPassword = request.getParameter("newUserPassword");
        String newUserBirthdate = request.getParameter("newUserBirthdate");
        String newUserRole = request.getParameter("newUserRole");

        if((newUserLastName == null || newUserLastName.isEmpty()) || (newUserName == null || newUserName.isEmpty()) || (newUserEmail == null || newUserEmail.isEmpty())
        || (newUserPassword == null || newUserPassword.isEmpty()) || (newUserBirthdate == null || newUserBirthdate.isEmpty()) || (newUserRole == null || newUserRole.isEmpty())) {
            request.setAttribute("erreur", "Erreur : Veuillez remplir tous les champs.");
            return "redirect:/projetSB/UserCreationController";
        }

        Users user = new Users();
        user.setUserLastName(newUserLastName);
        user.setUserName(newUserName);
        user.setUserEmail(newUserEmail);
        user.setUserPassword(HashPswdUtil.hashPassword(newUserPassword));
        user.setUserBirthdate(newUserBirthdate);
        user.setUserRole(Role.valueOf(newUserRole));

        String error = usersService.addUser(user);
        if(error != null) {
            request.setAttribute("erreur", error);
            return "redirect:/projetSB/UserCreationController";
        }

        user = usersService.getUserByEmail(newUserEmail);

        switch (user.getUserRole()) {
            case student:
                Student student = new Student();
                student.setStudentId(user.getUserId());

                if(studentService.addOrUpdateStudent(student) == false) {
                    request.setAttribute("erreur", "Erreur : Erreur lors de l'ajout de l'utilisateur en tant que " + Role.student);
                    return "redirect:/projetSB/UserCreationController";
                }

                String subject = "Bienvenue à l'école";
                String body = "Bonjour " + usersService.getUserById(student.getStudentId()).getUserName() + ",\n\n"
                        + "Nous avons le plaisir de vous informer que vous avez été ajouté(e) à notre école en tant qu'étudiant(e).\n\n"
                        + "Cordialement,\nL'équipe pédagogique";

                String email = usersService.getUserById(student.getStudentId()).getUserEmail();  // Récupérer l'email de l'étudiant

                try {
                    // Envoyer l'email de notification
                    GMailer gmailer = new GMailer();
                    gmailer.sendMail(subject, body, email);  // Subject, Body, Email
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("erreur", "Erreur : Impossible d'envoyer l'email de bienvenue.");
                }

                break;
            case teacher:
                Teacher teacher = new Teacher();
                teacher.setTeacherId(user.getUserId());

                if(teacherService.addOrUpdateTeacher(teacher) == false) {
                    request.setAttribute("erreur", "Erreur : Erreur lors de l'ajout de l'utilisateur en tant que " + Role.teacher);
                    return "redirect:/projetSB/UserCreationController";
                }
                break;
            case administrator:
                Administrator administrator = new Administrator();
                administrator.setAdministratorId(user.getUserId());

                if(adminService.addAdminInTable(administrator) == false) {
                    request.setAttribute("erreur", "Erreur : Erreur lors de l'ajout de l'utilisateur en tant que " + Role.administrator);
                    return "redirect:/projetSB/UserCreationController";
                }
                break;
            default:
                request.setAttribute("erreur", "Erreur : Erreur lors de l'ajout de l'utilisateur en tant que " + user.getUserRole());
                return "redirect:/projetSB/UserCreationController";
        }

        return "redirect:/projetSB/UserManagerController?roleFilter="+user.getUserRole();
    }
}
