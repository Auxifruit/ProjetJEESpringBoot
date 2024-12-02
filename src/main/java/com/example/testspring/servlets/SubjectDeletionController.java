package com.example.testspring.servlets;

import com.example.testspring.services.SubjectService;
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
@RequestMapping("/projetSB/SubjectDeletionController")
public class SubjectDeletionController {

    private final SubjectService subjectService;

    @Autowired
    public SubjectDeletionController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @PostMapping
    protected String handleSubjectDeletion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String subjectIdString = request.getParameter("subjectId");

        if(subjectIdString == null || subjectIdString.isEmpty()) {
            request.setAttribute("erreur", "Erreur : Veuillez choisir une matière.");
            return "redirect:/projetSB/SubjectManagerController";
        }

        int subjectId = Integer.parseInt(subjectIdString);

        if(subjectService.getSubjectById(subjectId) == null) {
            request.setAttribute("erreur", "Erreur : La matière n'existe pas.");
            return "redirect:/projetSB/SubjectManagerController";
        }


        if(subjectService.deleteSubjectFromTable(subjectId) == false) {
            request.setAttribute("erreur", "Erreur : Erreur lors de la suppression de la matière.");
        }

        return "redirect:/projetSB/SubjectManagerController";
    }
}
