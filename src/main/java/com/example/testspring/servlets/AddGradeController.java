package com.example.testspring.servlets;

import com.example.testspring.entities.*;
import com.example.testspring.services.GradeService;
import com.example.testspring.util.GMailer;
import com.example.testspring.services.ClassesService;
import com.example.testspring.services.CourseService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/projetSB/AddGradeController")
public class AddGradeController {
    private final GradeService gradeService;
    private final UsersService usersService;

    @Autowired
    public AddGradeController(UsersService usersService, GradeService gradeService) {
        this.usersService = usersService;
        this.gradeService = gradeService;
    }

    @PostMapping
    protected String handleAddGrade(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int teacherId = Integer.parseInt(request.getParameter("teacherID"));
        // get the data from the form
        Map<String, String[]> parameterMap = request.getParameterMap();
        // create a list of grade for easy manipulation
        List<Grade> grades = new ArrayList<>();
        GMailer gMailer = null;

        try {
            gMailer = new GMailer();
            // global parameters
            String courseIdParam = request.getParameter("courseId");
            String gradeName = request.getParameter("gradeName");
            String gradeCoefficientParam = request.getParameter("gradeCoefficient");

            if (courseIdParam == null || gradeName == null || gradeCoefficientParam == null) {
                throw new IllegalArgumentException("Les paramètres (courseId, gradeName, gradeCoefficient) n'existent pas.");
            }

            int courseId = Integer.parseInt(courseIdParam);
            int gradeCoefficient = Integer.parseInt(gradeCoefficientParam);

            // create each grade element
            for (String paramName : parameterMap.keySet()) {
                if (paramName.startsWith("grade_")) {
                    try {
                        int studentId = Integer.parseInt(paramName.split("_")[1]);
                        double gradeValue = Double.parseDouble(parameterMap.get(paramName)[0]);

                        // create the grade object
                        Grade grade = new Grade();
                        grade.setGradeName(gradeName);
                        grade.setGradeValue(gradeValue);
                        grade.setGradeCoefficient(gradeCoefficient);
                        grade.setStudentId(studentId);
                        grade.setCourseId(courseId);
                        grade.setTeacherId(teacherId); // teacher's ID

                        grades.add(grade);  // Add the grade to the list
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }

            // insert grades in the database and send emails
            for (Grade grade : grades) {
                if (gradeService.addGrade(grade) != null) {
                    // Get the student's email
                    String studentEmail = usersService.getUserById(grade.getStudentId()).getUserEmail();

                    // Send email to the student
                    String subject = "Nouvelle Note ajoutée";
                    String message = "Bonjour,\n\nUne nouvelle note a été ajoutée à votre dossier pour le cours " +
                            grade.getGradeName() + ".\nNote : " + grade.getGradeValue() + "\nCordialement,\nL'équipe pédagogique.";

                    // Envoi du mail
                    gMailer.sendMail(subject, message, studentEmail);
                } else {
                    System.out.println("Erreur d'insertion de la note pour l'étudiant ID: " + grade.getStudentId());
                }
            }

            // set back the ID for another use
            request.setAttribute("teacherID", teacherId);
            // forward to a success page
            return "addGradeDone";

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "error_insert_grade" + e.getMessage());
        }
        return "redirect:/projetSB/EntryNoteController";
    }
}
