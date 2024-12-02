package com.example.testspring.servlets;

import com.example.testspring.entities.Lesson;
import com.example.testspring.entities.Lessonclass;
import com.example.testspring.entities.Student;
import com.example.testspring.entities.Users;
import com.example.testspring.util.GMailer;
import com.example.testspring.services.CourseService;
import com.example.testspring.services.LessonService;
import com.example.testspring.services.LessonclassService;
import com.example.testspring.services.UsersService;
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
@RequestMapping("/projetSB/LessonClassesUnassignationController")
public class LessonClassesUnassignationController {
    private final LessonService lessonService;
    private final LessonclassService lessonclassService;
    private final UsersService usersService;
    private final CourseService courseService;

    @Autowired
    public LessonClassesUnassignationController(LessonService lessonService, LessonclassService lessonclassService, UsersService usersService, CourseService courseService) {
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
    protected String handleClassesUnassignation(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        String classeIdString = request.getParameter("classId");
        String lessonIdString = request.getParameter("lessonId");

        int lessonId = Integer.parseInt(lessonIdString);

        if(classeIdString == null || classeIdString.isEmpty()) {
            request.setAttribute("erreur", "Erreur : Veuillez choisir une classe.");
            return "redirect:/projetSB/LessonClassesManagerController?lessonId="+lessonId;
        }

        int classeId = Integer.parseInt(classeIdString);
        Lesson lesson = lessonService.getLessonById(lessonId);
        Lessonclass lessonclass = lessonclassService.getLessonClassByLessonIdAndClassId(lessonId, classeId);

        if(lessonclassService.deleteLessonClass(lessonclass.getLessonClassId()) == true) {
            // Récupérer la liste des étudiants inscrits à cette classe
            List<Student> studentsInClass = lessonclassService.getStudentsByClassId(classeId);

            if(studentsInClass != null && !studentsInClass.isEmpty()) {
                Integer courseID = lesson.getCourseId();
                String lessonName = "Cours";

                if(courseID != null) {
                    String courseName = courseService.getCourseById(courseID).getCourseName();
                    if(courseName != null && !courseName.isEmpty()) {
                        lessonName = courseName;
                    }
                }

                // Pour chaque étudiant, envoyer un email pour lui notifier que la séance a été annulée
                for (Student student : studentsInClass) {
                    // Récupérer l'utilisateur associé à cet étudiant
                    Users user = usersService.getUserById(student.getStudentId());
                    String studentEmail = user != null ? user.getUserEmail() : null;

                    if (studentEmail != null) {
                        // Récupérer le nom de l'utilisateur
                        String studentUserName = user.getUserName(); // Utiliser le nom de l'utilisateur

                        // Préparer le sujet et le corps du message
                        String subject = "Séance annulée : " + lessonName;
                        String body = "Bonjour " + (studentUserName != null ? studentUserName : "Étudiant") + ",\n\n Nous vous informons que la séance assignée à votre classe pour la matière : " + lessonName + " a été annulée.\n Veuillez vérifier votre emploi du temps pour toute mise à jour.\n\n Cordialement,\nL'équipe pédagogique";
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
            request.setAttribute("erreur", "Erreur : Erreur lors de la désaffectation de la classe.");
        }

        request.setAttribute("lessonId", lessonId);
        return "redirect:/projetSB/LessonClassesManagerController?lessonId="+lessonId;
    }
}

