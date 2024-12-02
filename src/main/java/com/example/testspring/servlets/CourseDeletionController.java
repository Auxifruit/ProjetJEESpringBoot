package com.example.testspring.servlets;

import com.example.testspring.services.CourseService;
import com.example.testspring.services.GradeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
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
@RequestMapping("/projetSB/CourseDeletionController")
public class CourseDeletionController extends HttpServlet {

    private final CourseService courseService;

    @Autowired
    public CourseDeletionController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @PostMapping
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String courseIdString = request.getParameter("courseId");

        if(courseIdString == null || courseIdString.isEmpty()) {
            request.setAttribute("erreur", "Erreur : Veuillez choisir une matière.");
            request.getRequestDispatcher("/projetSB/CourseManagerController").forward(request, response);
            return;
        }

        int courseId = Integer.parseInt(courseIdString);

        if(courseService.getCourseById(courseId) == null) {
            request.setAttribute("erreur", "Erreur : Le cours n'existe pas.");
            request.getRequestDispatcher("/projetSB/CourseManagerController").forward(request, response);
            return;
        }

        if(courseService.deleteCourse(courseId) == true) {
            request.getRequestDispatcher("/projetSB/CourseManagerController").forward(request, response);
        }
        else {
            request.setAttribute("erreur", "Erreur : Erreur lors de la suppression du cours.");
            request.getRequestDispatcher("/projetSB/CourseManagerController").forward(request, response);
        }

    }
}