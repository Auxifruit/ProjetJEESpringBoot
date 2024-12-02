package com.example.testspring.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/projetSB/LogoutController")
public class LogoutController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @GetMapping
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getSession().invalidate();

        RequestDispatcher dispatcher = request.getRequestDispatcher("index");
        dispatcher.forward(request, response);
    }

}
