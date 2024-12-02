package com.example.testspring.servlets;

import com.example.testspring.entities.*;
import com.example.testspring.services.ClassesService;
import com.example.testspring.services.GradeService;
import com.example.testspring.services.StudentService;
import com.example.testspring.services.UsersService;
import com.example.testspring.util.GeneratorPdfGradeReport;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/projetSB/StudentGradeReportController")
public class StudentGradeReportController {
    private final UsersService usersService;
    private final StudentService studentService;
    private final ClassesService classesService;
    private final GradeService gradeService;
    private final GeneratorPdfGradeReport generatorPdfGradeReport;

    @Autowired
    public StudentGradeReportController(UsersService usersService, StudentService studentService, ClassesService classesService, GradeService gradeService, GeneratorPdfGradeReport generatorPdfGradeReport) {
        this.usersService = usersService;
        this.studentService = studentService;
        this.classesService = classesService;
        this.gradeService = gradeService;
        this.generatorPdfGradeReport = generatorPdfGradeReport;
    }

    @GetMapping
    protected String showStudentGradeReportPage(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        Users connectedUser = (Users) session.getAttribute("connectedUser");
        String userIdString = request.getParameter("userId");

        if(connectedUser == null || (userIdString == null || userIdString.isEmpty())) {
            System.out.println("Problème 1");
            return "index";
        }

        Integer userId = Integer.parseInt(userIdString);

        if(connectedUser.getUserRole().equals(Role.student) && connectedUser.getUserId() != userId) {
            System.out.println("Problème 2");
            return "index";
        }

        // if not a student go home
        if(usersService.getUserById(userId).getUserRole() != Role.student) {
            System.out.println("Problème 3");
            return "index";
        }

        // get the function initialy use for the PDF generator (had to set it public)
        request.setAttribute("subjectCourseGrades", generatorPdfGradeReport.fetchSubjectCourseGrades(userId));

        // set userId
        request.setAttribute("student", usersService.getUserById(userId));

        // set className
        Integer classId = studentService.getStudentById(userId).getClassId();
        if(classId == null) {
            request.setAttribute("className", "Aucune");
        } else {
            request.setAttribute("className", classesService.getClasseById(classId).getClassName());
        }

        // set mean (with the mean calculator of the PDF generator
        double overallMean = generatorPdfGradeReport.calculateAverage(gradeService.getGradeByStudentId(userId)); // get the mean
        String formattedMean = String.format("%.2f", overallMean); // set the format to only 2 number after the ','
        request.setAttribute("mean", formattedMean);

        return "StudentGradeReport";
    }

    @PostMapping
    protected void handleStudentReportGrade(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            // get the id and send it to the form
            int userId = Integer.parseInt(request.getParameter("userId"));
            System.out.println("userId: "+userId);

            // generate the PDF
            byte[] pdfContent = generatorPdfGradeReport.generatePDF(usersService.getUserById(userId));

            // verify if the PDF is empty (error)
            if (pdfContent.length == 0) {
                throw new RuntimeException("PDF FILE EMPTY");
            }

            // set the HTTP response for the download
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"Relevé_de_notes.pdf\"");

            // send the data
            response.getOutputStream().write(pdfContent);
            response.getOutputStream().flush();

            System.out.println("PDF WELL SEND");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "ERROR IN THE GENERATION OF THE PDF");
        }
    }
}
