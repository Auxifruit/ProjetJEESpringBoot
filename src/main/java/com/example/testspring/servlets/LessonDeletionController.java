package com.example.testspring.servlets;

import com.example.testspring.entities.Student;
import com.example.testspring.services.LessonService;
import com.example.testspring.services.LessonclassService;
import com.example.testspring.services.UsersService;
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
import java.util.List;

@Controller
@RequestMapping("/projetSB/LessonDeletionController")
public class LessonDeletionController {

    private final LessonService lessonService;
    private final LessonclassService lessonclassService;
    private final UsersService usersService;

    @Autowired
    public LessonDeletionController(LessonService lessonService, LessonclassService lessonclassService, UsersService usersService) {
        this.lessonService = lessonService;
        this.lessonclassService = lessonclassService;
        this.usersService = usersService;
    }

    @PostMapping
    protected String handleLessonDeletion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String lessonIdString = request.getParameter("lessonId");

        if(lessonIdString == null || lessonIdString.isEmpty()) {
            System.out.println("ERREUR 1");
            request.setAttribute("erreur", "Erreur : Veuillez choisir une séance.");
            return "redirect:/projetSB/LessonManagerController";
        }

        int lessonId = Integer.parseInt(lessonIdString);

        if(lessonService.getLessonById(lessonId) == null) {
            System.out.println("ERREUR 2");
            request.setAttribute("erreur", "Erreur : La séance n'existe pas.");
            return "redirect:/projetSB/LessonManagerController";
        }

        boolean isDeleted = lessonService.deleteLesson(lessonId);
        if(isDeleted == true) {
            // Récupérer tous les étudiants associés à la séance supprimée
            List<Student> studentsInLesson = lessonclassService.getStudentsByLessonId(lessonId);

            if (studentsInLesson != null && !studentsInLesson.isEmpty()) {
                // Pour chaque étudiant, envoyer un email pour l'informer de la suppression de la séance
                for (Student student : studentsInLesson) {
                    String studentEmail = usersService.getUserById(student.getStudentId()).getUserEmail();

                    if (studentEmail != null) {
                        // Sujet et corps du message
                        String subject = "Séance supprimée";
                        String body = "Bonjour,\n\n"
                                + "Nous vous informons que la séance pour la matière ID " + lessonId + " a été supprimée.\n"
                                + "Nous vous invitons à consulter votre emploi du temps pour toute mise à jour.\n\n"
                                + "Cordialement,\nL'équipe pédagogique";

                        // Envoi de l'email via la classe GMailer
                        try {
                            GMailer gmailer = new GMailer();
                            gmailer.sendMail(subject, body, studentEmail);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            System.out.println("ERREUR 3");
            request.setAttribute("erreur", "Erreur : Erreur lors de la suppression de la séance.");
        }

        return "redirect:/projetSB/LessonManagerController";
    }
}
