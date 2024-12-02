package com.example.testspring.servlets;

import com.example.testspring.entities.*;
import com.example.testspring.services.ClassesService;
import com.example.testspring.services.MajorService;
import com.example.testspring.services.StudentService;
import com.example.testspring.services.UsersService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/projetSB/UserModificationController")
public class UserModificationController {

    private final UsersService usersService;
    private final StudentService studentService;
    private final ClassesService classesService;
    private final MajorService majorService;

    @Autowired
    public UserModificationController(UsersService usersService, StudentService studentService, ClassesService classesService, MajorService majorService) {
        this.usersService = usersService;
        this.studentService = studentService;
        this.classesService = classesService;
        this.majorService = majorService;
    }

    @GetMapping
    protected String showUserModificationPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userIdString = request.getParameter("userId");
        List<Classes> classesList = classesService.getAllClasses();
        List<Major> majorList = majorService.getMajors();
        Classes studentClasse = null;
        Major studentMajor = null;

        if(userIdString == null || userIdString.isEmpty()) {
            request.setAttribute("erreur", "Erreur : Veuillez choisir un utilisateur.");
            return "redirect:/projetSB/UserManagerController?roleFilter=student";
        }

        int userId = Integer.parseInt(userIdString);
        Users userToModify = usersService.getUserById(userId);

        if(userToModify.getUserRole().equals(Role.student)) {
            Student student = studentService.getStudentById(userToModify.getUserId());
            Integer classesId = student.getClassId();
            Integer majorId = student.getMajorId();

            if(classesId != null) {
                studentClasse = classesService.getClasseById(classesId);
            }
            if(majorId != null) {
                studentMajor = majorService.getMajorById(majorId);
            }
        }

        request.setAttribute("userToModify", userToModify);
        request.setAttribute("classesList", classesList);
        request.setAttribute("majorList", majorList);
        request.setAttribute("studentClasse", studentClasse);
        request.setAttribute("studentMajor", studentMajor);
        return "userModification";
    }

    @PostMapping
    protected String handleUserModification(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userIdString = request.getParameter("userId");
        String userNewLastName = request.getParameter("userNewLastName");
        String userNewName = request.getParameter("userNewName");
        String userNewEmail = request.getParameter("userNewEmail");
        String userNewBirthdate = request.getParameter("userNewBirthdate");
        String userNewClassesIdString = request.getParameter("userNewClassesId");
        String userNewMajorIdString = request.getParameter("userNewMajorId");
        String userNewRole = request.getParameter("userNewRole");

        if(userIdString == null || userIdString.isEmpty()) {
            request.setAttribute("erreur", "Erreur : Veuillez choisir un utilisateur.");
            return "redirect:/projetSB/UserManagerController?roleFilter=student";
        }

        int userId = Integer.parseInt(userIdString);
        Users userToModify = usersService.getUserById(userId);

        if((userNewLastName == null || userNewLastName.isEmpty()) && (userNewName == null || userNewName.isEmpty()) && (userNewEmail == null || userNewEmail.isEmpty())
        && (userNewBirthdate == null || userNewBirthdate.isEmpty()) && (userNewRole == null || userNewRole.isEmpty())) {
            if(userToModify.getUserRole().equals(Role.student)){
                if((userNewClassesIdString == null || userNewClassesIdString.isEmpty()) && (userNewMajorIdString == null || userNewMajorIdString.isEmpty())) {
                    request.setAttribute("erreur", "Erreur : Veuillez remplir au moins un champ.");
                    return "redirect:/projetSB/UserModificationController";
                }
            } else {
                request.setAttribute("erreur", "Erreur : Veuillez remplir au moins un champ.");
                return "redirect:/projetSB/UserModificationController";
            }
        }

        if(userNewLastName != null && !userNewLastName.isEmpty()) {
            userToModify.setUserLastName(userNewLastName);
        }

        if(userNewName != null && !userNewName.isEmpty()) {
            userToModify.setUserName(userNewName);
        }

        if(userNewEmail != null && !userNewEmail.isEmpty()) {
            if(usersService.isEmailInTable(userNewEmail) == false) {
                userToModify.setUserEmail(userNewEmail);
            } else {
                request.setAttribute("erreur", "Erreur : l'email est déjà utilisée.");
                return "redirect:/projetSB/UserModificationController";
            }
        }

        if(userNewBirthdate != null && !userNewBirthdate.isEmpty()) {
            userToModify.setUserBirthdate(userNewBirthdate);
        }

        if(userNewRole != null && !userNewRole.isEmpty()) {
            Role userRole = Role.valueOf(userNewRole);
            if(usersService.modifyUserRole(userToModify, userToModify.getUserRole(), userRole) == false) {
                request.setAttribute("erreur", "Erreur lors de la modification du role.");
                return "redirect:/projetSB/UserModificationController";
            }
        } else {
            String errorModifyUser = usersService.addUser(userToModify);
            if (errorModifyUser != null) {
                request.setAttribute("erreur", errorModifyUser);
                return "redirect:/projetSB/UserModificationController";
            }
        }

        if(userToModify.getUserRole().equals(Role.student)) {
            Student student = studentService.getStudentById(userToModify.getUserId());

            if(userNewClassesIdString != null && !userNewClassesIdString.isEmpty()) {
                int userNewClassesId = Integer.parseInt(userNewClassesIdString);
                student.setClassId(userNewClassesId);
            }

            if(userNewMajorIdString != null && !userNewMajorIdString.isEmpty()) {
                int userNewMajorId = Integer.parseInt(userNewMajorIdString);
                student.setMajorId(userNewMajorId);
            }

            if(studentService.addOrUpdateStudent(student) != false) {
                request.setAttribute("erreur", "Erreur : Erreur lors de la modification d'un utilisateur");
                return "redirect:/projetSB/UserModificationController";
            }
        }

        return "redirect:/projetSB/UserManagerController?roleFilter="+userToModify.getUserRole();
    }
}
