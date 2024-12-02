package com.example.testspring.servlets;

import com.example.testspring.entities.*;
import com.example.testspring.util.GMailer;
import com.example.testspring.services.*;
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
@RequestMapping("/projetSB/LessonClassesAssignationController")
public class LessonClassesAssignationController {
    private final LessonService lessonService;
    private final LessonclassService lessonclassService;
    private final UsersService usersService;
    private final CourseService courseService;

    @Autowired
    public LessonClassesAssignationController(LessonService lessonService, LessonclassService lessonclassService, UsersService usersService, CourseService courseService) {
        this.lessonService = lessonService;
        this.lessonclassService = lessonclassService;
        this.usersService = usersService;
        this.courseService = courseService;
    }

    @GetMapping
    protected String doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<Lesson> lessonList = lessonService.getAllLessons();

        request.setAttribute("lessons", lessonList);

        return "redirect:/projetSB/LessonClassesManagerController";
    }

    @PostMapping
    protected String doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String classeIdString = request.getParameter("classId");
        String lessonIdString = request.getParameter("lessonId");

        int lessonId = Integer.parseInt(lessonIdString);

        if(classeIdString == null || classeIdString.isEmpty()) {
            request.setAttribute("erreur", "Erreur : Veuillez choisir une classe.");
            return "redirect:/projetSB/LessonClassesManagerController";
        }

        int classeId = Integer.parseInt(classeIdString);

        if(lessonclassService.canClassParticipate(classeId, lessonId) == false) {
            request.setAttribute("erreur", "Erreur : La classe a une séance à ces horaires.");
            return "redirect:/projetSB/LessonClassesManagerController?lessonId="+lessonId;
        }

        Lessonclass lessonclass = new Lessonclass();
        lessonclass.setLessonId(lessonId);
        lessonclass.setClassId(classeId);

        if(lessonclassService.addLessonClass(lessonclass) == true) {
            // Récupérer la liste des étudiants inscrits à cette classe
            List<Student> studentsInClass = lessonclassService.getStudentsByClassId(classeId);

            if(studentsInClass != null && !studentsInClass.isEmpty()) {
                Lesson lesson = lessonService.getLessonById(lessonId);
                Integer courseId = lesson.getCourseId();
                String lessonName = "Cours";
                if(courseId != null) {
                    lessonName = courseService.getCourseById(courseId).getCourseName();
                }


                // Pour chaque étudiant, envoyer un email pour lui notifier de la nouvelle séance
                for (Student student : studentsInClass) {
                    // Récupérer l'utilisateur associé à cet étudiant
                    Users user = usersService.getUserById(student.getStudentId());
                    String studentEmail = user != null ? user.getUserEmail() : null;

                    if (studentEmail != null) {
                        // Récupérer le nom de l'utilisateur
                        String studentUserName = user.getUserName(); // Utiliser le nom de l'utilisateur

                        // Préparer le sujet et le corps du message
                        String subject = "Nouvelle séance assignée : " + lessonName;
                        String body = "Bonjour " + (studentUserName != null ? studentUserName : "Étudiant")
                                + ",\n\n Une nouvelle séance a été assignée à votre classe pour la matière : " + lessonName + "" +
                                ".\n Veuillez vérifier l'horaire et les détails de la séance dans votre emploi du temps.\n\n" +
                                "Cordialement,\nL'équipe pédagogique";

                        // Envoi de l'email via la classe GMailer
                        try {
                            GMailer gmailer = new GMailer();  // Créer une instance de GMailer
                            gmailer.sendMail(subject, body, studentEmail);  // Envoyer l'email
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        else {
            request.setAttribute("erreur", "Erreur : Erreur lors de l'assignation de la classe.");
        }

        request.setAttribute("lessonId", lessonId);
        return "redirect:/projetSB/LessonClassesManagerController?lessonId="+lessonId;
    }
}

