package com.example.testspring.servlets;

import com.example.testspring.entities.Classes;
import com.example.testspring.services.ClassesService;
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
@RequestMapping("/projetSB/ClassesModificationController")
public class ClassesModificationController {
    private final ClassesService classesService;

    @Autowired
    public ClassesModificationController(ClassesService classesService) {
        this.classesService = classesService;
    }

    @GetMapping
    protected String showClassesModificationPage(HttpServletRequest request, HttpServletResponse response) {
        String classIdString = request.getParameter("classesId");

        if(classIdString == null || classIdString.isEmpty()) {
            request.setAttribute("erreur", "Erreur : Veuillez choisir une classe.");
            return "redirect:/projetSB/ClassesManagerController";
        }

        int classId = Integer.parseInt(classIdString);
        Classes classe = classesService.getClasseById(classId);

        request.setAttribute("classe", classe);
        return "classesModification";
    }

    @PostMapping
    protected String handleClassesModification(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String classesIdString = request.getParameter("classesId");
        String classesNewName = request.getParameter("classesNewName");

        if(classesIdString == null || classesIdString.isEmpty()) {
            request.setAttribute("erreur", "Erreur : Veuillez choisir une classe.");
            return "redirect:/projetSB/ClassesManagerController";
        }

        if(classesNewName == null || classesNewName.isEmpty()) {
            request.setAttribute("erreur", "Erreur : Veuillez choisir un nouveau nom.");
            return "redirect:/projetSB/ClassesModificationController";
        }

        int classesId = Integer.parseInt(classesIdString);
        Classes classe = classesService.getClasseById(classesId);

        if(classe == null) {
            request.setAttribute("erreur", "Erreur : La classe n'existe pas.");
            return "redirect:/projetSB/ClassesManagerController";
        }

        if(classe.getClassName().equals(classesNewName)) {
            request.setAttribute("erreur", "Erreur : Veuillez choisir un nom différent.");
            return "redirect:/projetSB/ClassesModificationController";
        }

        classe.setClassName(classesNewName);

        String error = classesService.modifyClasse(classe);
        if(error == null) {
            return "redirect:/projetSB/ClassesManagerController";
        }
        else {
            request.setAttribute("erreur", error);
            return "redirect:/projetSB/ClassesModificationController";
        }

    }


}
