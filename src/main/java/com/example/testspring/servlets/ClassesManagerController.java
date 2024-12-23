package com.example.testspring.servlets;

import com.example.testspring.entities.Classes;
import com.example.testspring.services.ClassesService;
import com.example.testspring.services.CourseService;
import com.example.testspring.services.LessonService;
import com.example.testspring.services.UsersService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
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
@RequestMapping("/projetSB/ClassesManagerController")
public class ClassesManagerController {

    private final ClassesService classesService;

    @Autowired
    public ClassesManagerController(ClassesService classesService) {
        this.classesService = classesService;
    }

    @GetMapping
    protected String showClassesManagerPage(HttpServletRequest request, HttpServletResponse response) {
        List<Classes> classesList = classesService.getAllClasses();

        request.setAttribute("classes", classesList);

        return "classesManager";
    }
}
