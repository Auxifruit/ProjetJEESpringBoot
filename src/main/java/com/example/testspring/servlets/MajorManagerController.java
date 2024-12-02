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
public class MajorManagerController extends HttpServlet {
    private final MajorService majorService;

    @Autowired
    public MajorManagerController(MajorService majorService) {
        this.majorService = majorService;
    }

    @GetMapping
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Major> majorList = majorService.getMajors();

        request.setAttribute("majors", majorList);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/projetSB/majorManager");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
