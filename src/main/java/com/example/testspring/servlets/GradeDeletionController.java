package com.example.testspring.servlets;

import com.example.testspring.services.GradeService;
import com.example.testspring.services.LessonService;
import com.example.testspring.services.LessonclassService;
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

@Controller
@RequestMapping("/projetSB/GradeDeletionController")
public class GradeDeletionController extends HttpServlet {

    private final GradeService gradeService;

    @Autowired
    public GradeDeletionController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @GetMapping
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @PostMapping
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String gradeIdString = request.getParameter("gradeId");

        if(gradeIdString == null || gradeIdString.isEmpty()) {
            request.setAttribute("erreur", "Erreur : Veuillez choisir une note.");
            request.getRequestDispatcher("/projetSB/GradeManagerController").forward(request, response);
            return;
        }

        int gradeId = Integer.parseInt(gradeIdString);

        if(gradeService.getGradeById(gradeId) == null) {
            request.setAttribute("erreur", "Erreur : La note n'existe pas.");
            request.getRequestDispatcher("/projetSB/GradeManagerController").forward(request, response);
            return;
        }

        if(gradeService.deleteGrade(gradeId) == true) {
            request.getRequestDispatcher("/projetSB/GradeManagerController").forward(request, response);
        }
        else {
            request.setAttribute("erreur", "Erreur : Erreur lors de la suppression de la note.");
            request.getRequestDispatcher("/projetSB/GradeManagerController").forward(request, response);
        }

    }
}
