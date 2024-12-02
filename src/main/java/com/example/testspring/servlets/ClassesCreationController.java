package com.example.testspring.servlets;

import com.example.testspring.entities.Classes;
import com.example.testspring.services.ClassesService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/projetSB/ClassesCreationController")
public class ClassesCreationController {

    private final ClassesService classesService;

    @Autowired
    public ClassesCreationController(ClassesService classesService) {
        this.classesService = classesService;
    }

    @GetMapping
    public String showClassesCreationPage(HttpServletRequest request) {
        List<Classes> classesList = classesService.getAllClasses();
        request.setAttribute("classes", classesList);
        return "classesCreation";
    }

    @PostMapping
    public String handleClassesCreation(HttpServletRequest request) {
        String classesName = request.getParameter("newClasses");

        if (classesName == null || classesName.isEmpty()) {
            request.setAttribute("erreur", "Erreur : Veuillez saisir le nom de la nouvelle classe.");
            return "redirect:/projetSB/ClassesCreationController";
        }

        Classes classe = new Classes();
        classe.setClassName(classesName);

        String error = classesService.addClasse(classe);
        if (error == null) {
            return "redirect:/projetSB/ClassesManagerController";
        } else {
            request.setAttribute("erreur", error);
            return "redirect:/projetSB/ClassesCreationController";
        }
    }
}
