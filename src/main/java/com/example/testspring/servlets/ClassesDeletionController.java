package com.example.testspring.servlets;

import com.example.testspring.services.ClassesService;
import com.example.testspring.services.CourseService;
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
@RequestMapping("/projetSB/ClassesDeletionController")
public class ClassesDeletionController {

    private final ClassesService classesService;

    @Autowired
    public ClassesDeletionController(ClassesService classesService) {
        this.classesService = classesService;
    }

    @PostMapping
    protected String handleClassesDeletion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String classIdString = request.getParameter("classesId");

        if(classIdString == null || classIdString.isEmpty()) {
            request.setAttribute("erreur", "Erreur : Veuillez choisir une classe.");
            return "redirect:/projetSB/ClassesManagerController";
        }

        int classId = Integer.parseInt(classIdString);

        if(classesService.getClasseById(classId) == null) {
            request.setAttribute("erreur", "Erreur : La filière n'existe pas.");
            return "redirect:/projetSB/ClassesManagerController";
        }

        if(classesService.deleteClasse(classId) == true) {
            return "redirect:/projetSB/ClassesManagerController";
        }
        else {
            request.setAttribute("erreur", "Erreur : Erreur lors de la suppression de la classe.");
            return "redirect:/projetSB/ClassesManagerController";
        }
    }
}
