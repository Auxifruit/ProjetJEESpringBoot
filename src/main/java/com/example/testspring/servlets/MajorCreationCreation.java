package com.example.testspring.servlets;

import com.example.testspring.entities.Major;
import com.example.testspring.services.ClassesService;
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
import java.util.List;

@Controller
@RequestMapping("/projetSB/MajorCreationCreation")
public class MajorCreationCreation {
    private final MajorService majorService;

    @Autowired
    public MajorCreationCreation(MajorService majorService) {
        this.majorService = majorService;
    }

    @GetMapping
    protected String showMajorCreationPage(HttpServletRequest request, HttpServletResponse response) {
        List<Major> majorList = majorService.getMajors();

        request.setAttribute("majors", majorList);

        return "majorCreation";
    }

    @PostMapping
    protected String handleMajorCreation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String majorName = request.getParameter("newMajor");

        if(majorName == null || majorName.isEmpty()) {
            request.setAttribute("erreur", "Erreur : Veuillez saisir le nom de la nouvelle filière.");
            return "redirect:/projetSB/MajorCreationController";
        }

        Major major = new Major();
        major.setMajorName(majorName);

        String error = majorService.addMajor(major);
        if(error == null) {
            return "redirect:/projetSB/MajorManagerController";
        }
        else {
            request.setAttribute("erreur", error);
            return "redirect:/projetSB/MajorCreationController";
        }

    }
}
