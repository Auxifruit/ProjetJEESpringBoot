package com.example.testspring.servlets;

import com.example.testspring.entities.Subjects;
import com.example.testspring.services.SubjectService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/projetSB/SubjectCreationController")
public class SubjectCreationController {

    private final SubjectService subjectService;

    @Autowired
    public SubjectCreationController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    protected String showSubjectCreationPage(HttpServletRequest request, HttpServletResponse response) {
        List<Subjects> subjectList = subjectService.getAllSubjects();

        request.setAttribute("subjects", subjectList);

        return "subjectCreation";
    }

    @PostMapping
    protected String handleSubjectCreation(HttpServletRequest request, HttpServletResponse response) {
        String subjectName = request.getParameter("newSubject");

        if(subjectName == null || subjectName.isEmpty()) {
            request.setAttribute("erreur", "Erreur : Veuillez saisir le nom de la nouvelle matière.");
            return "redirect:/projetSB/SubjectCreationController";
        }

        Subjects subject = new Subjects();
        subject.setSubjectName(subjectName);

        String error = subjectService.addSubjectInTable(subject);
        if(error == null) {
            return "redirect:/projetSB/SubjectManagerController";
        }
        else {
            request.setAttribute("erreur", error);
            return "redirect:/projetSB/SubjectCreationController";
        }

    }


}
