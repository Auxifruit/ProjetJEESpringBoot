package com.example.testspring.servlets;

import com.example.testspring.entities.Subjects;
import com.example.testspring.services.SubjectService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/projetSB/SubjectManagerController")
public class SubjectManagerController extends HttpServlet {

    private final SubjectService subjectService;

    @Autowired
    public SubjectManagerController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    protected String showSubjectManagerPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Subjects> subjectsList = subjectService.getAllSubjects();

        request.setAttribute("subjects", subjectsList);

        return "subjectManager";
    }

}
