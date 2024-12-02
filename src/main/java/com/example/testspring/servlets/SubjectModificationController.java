package com.example.testspring.servlets;

import com.example.testspring.entities.Subjects;
import com.example.testspring.services.SubjectService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/projetSB/SubjectModificationController")
public class SubjectModificationController {
    private final SubjectService subjectService;

    @Autowired
    public SubjectModificationController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    protected String showSubjectModificationPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String subjectIdString = request.getParameter("subjectId");

        if(subjectIdString == null || subjectIdString.isEmpty()) {
            request.setAttribute("erreur", "Erreur : Veuillez choisir une matière.");
            return "redirect:/projetSB/SubjectManagerController";
        }

        int subjectId = Integer.parseInt(subjectIdString);
        Subjects subject = subjectService.getSubjectById(subjectId);

        request.setAttribute("subject", subject);
        return "subjectModification";
    }

    @PostMapping
    protected String handleSubjectModification(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String subjectIdString = request.getParameter("subjectId");
        String subjectNewName = request.getParameter("subjectNewName");

        if(subjectIdString == null || subjectIdString.isEmpty()) {
            request.setAttribute("erreur", "Erreur : Veuillez choisir une matière.");
            return "redirect:/projetSB/SubjectManagerController";
        }

        if(subjectNewName == null || subjectNewName.isEmpty()) {
            request.setAttribute("erreur", "Erreur : Veuillez choisir un nouveau nom.");
            return "redirect:/projetSB/SubjectManagerController";
        }

        int subjectId = Integer.parseInt(subjectIdString);

        Subjects subject = subjectService.getSubjectById(subjectId);

        if(subject == null) {
            request.setAttribute("erreur", "Erreur : La matière n'existe pas.");
            return "redirect:/projetSB/SubjectManagerController";
        }

        if(subject.getSubjectName().equals(subjectNewName)) {
            request.setAttribute("erreur", "Erreur : Veuillez choisir un nouveau différent.");
            return "redirect:/projetSB/SubjectModificationController";
        }

        subject.setSubjectName(subjectNewName);

        String error = subjectService.modifySubjectFromTable(subject);
        if(error == null) {
            return "redirect:/projetSB/SubjectManagerController";
        }
        else {
            request.setAttribute("erreur", error);
            return "redirect:/projetSB/SubjectModificationController";
        }

    }


}
