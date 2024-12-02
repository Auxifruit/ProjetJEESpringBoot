package com.example.testspring.servlets;

import com.example.testspring.entities.Major;
import com.example.testspring.services.MajorService;
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
@RequestMapping("/projetSB/MajorDeletionController")
public class MajorDeletionController extends HttpServlet {

    private final MajorService majorService;

    @Autowired
    public MajorDeletionController(MajorService majorService) {
        this.majorService = majorService;
    }

    @GetMapping
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @PostMapping
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String majorIdString = request.getParameter("majorId");

        if(majorIdString == null || majorIdString.isEmpty()) {
            request.setAttribute("erreur", "Erreur : Veuillez choisir une filière.");
            request.getRequestDispatcher("/projetSB/MajorManagerController").forward(request, response);
            return;
        }

        int majorId = Integer.parseInt(majorIdString);
        Major major = majorService.getMajorById(majorId);

        if(major == null) {
            request.setAttribute("erreur", "Erreur : La filière n'existe pas.");
            request.getRequestDispatcher("/projetSB/MajorManagerController").forward(request, response);
            return;
        }

        if(majorService.deleteMajorById(majorId) == true) {
            request.getRequestDispatcher("/projetSB/MajorManagerController").forward(request, response);
        }
        else {
            request.setAttribute("erreur", "Erreur : Erreur lors de la suppression de la filière.");
            request.getRequestDispatcher("/projetSB/MajorManagerController").forward(request, response);
        }

    }
}
