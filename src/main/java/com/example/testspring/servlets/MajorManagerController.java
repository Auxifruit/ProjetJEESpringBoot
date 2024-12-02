package com.example.testspring.servlets;

import com.example.testspring.entities.Major;
import com.example.testspring.services.MajorService;
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
@RequestMapping("/projetSB/MajorManagerController")
public class MajorManagerController {
    private final MajorService majorService;

    @Autowired
    public MajorManagerController(MajorService majorService) {
        this.majorService = majorService;
    }

    @GetMapping
    protected String showMajorManagerPage(HttpServletRequest request, HttpServletResponse response)  {
        List<Major> majorList = majorService.getMajors();

        request.setAttribute("majors", majorList);

        return "majorManager";
    }
}
