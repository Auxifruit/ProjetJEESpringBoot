package com.example.testspring.servlets;


import com.example.testspring.entities.Major;
import com.example.testspring.services.MajorService;
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
@RequestMapping("/projetSB/MajorModificationController")
public class MajorModificationController {
    private final MajorService majorService;

    @Autowired
    public MajorModificationController(MajorService majorService) {
        this.majorService = majorService;
    }

    @GetMapping
    protected String showMajorModificationPage(HttpServletRequest request, HttpServletResponse response) {
        String majorIdString = request.getParameter("majorId");

        if(majorIdString == null || majorIdString.isEmpty()) {
            request.setAttribute("erreur", "Erreur : Veuillez choisir une filière.");
            return "redirect:/projetSB/MajorManagerController";
        }

        int majorIdId = Integer.parseInt(majorIdString);
        Major major = majorService.getMajorById(majorIdId);

        request.setAttribute("major", major);
        return "majorModification";
    }

    @PostMapping
    protected String handleMajorModification(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String majorIdString = request.getParameter("majorId");
        String majorNewName = request.getParameter("majorNewName");

        if(majorIdString == null || majorIdString.isEmpty()) {
            request.setAttribute("erreur", "Erreur : Veuillez choisir une filière.");
            return "redirect:/projetSB/MajorManagerController";
        }

        if(majorNewName == null || majorNewName.isEmpty()) {
            request.setAttribute("erreur", "Erreur : Veuillez choisir un nouveau nom.");
            return "redirect:/projetSB/MajorModificationController";
        }

        int majorId = Integer.parseInt(majorIdString);
        Major major = majorService.getMajorById(majorId);

        if(major == null) {
            request.setAttribute("erreur", "Erreur : La filière n'existe pas.");
            return "redirect:/projetSB/MajorModificationController";
        }

        if(major.getMajorName().equals(majorNewName)) {
            request.setAttribute("erreur", "Erreur : Veuillez choisir un nom différent.");
            return "redirect:/projetSB/MajorModificationController";
        }

        major.setMajorName(majorNewName);

        String error = majorService.addMajor(major);
        if(error == null) {
            return "redirect:/projetSB/MajorManagerController";
        }
        else {
            request.setAttribute("erreur", error);
            return "redirect:/projetSB/MajorModificationController";
        }

    }


}
