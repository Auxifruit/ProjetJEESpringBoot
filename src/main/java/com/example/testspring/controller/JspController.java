package com.example.testspring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class JspController {

    @GetMapping("/projetSB/classesManager")
    public String showClassesManagerPage(ModelMap model){
        return "classesManager";
    }

    @GetMapping("/projetSB/classesCreation")
    public String showClassesCreationPage(ModelMap model){
        return "classesCreation";
    }

    @GetMapping("/projetSB/courseManager")
    public String showCourseManagerPage(ModelMap model){
        return "courseManager";
    }

    @GetMapping("/projetSB/gradeManager")
    public String showGradeManagerPage(ModelMap model){
        return "gradeManager";
    }

    @GetMapping("/projetSB/lessonManager")
    public String showLessonManagerPage(ModelMap model){
        return "lessonManager";
    }

    @GetMapping("/projetSB/majorManager")
    public String showMajorManagerPage(ModelMap model){
        return "majorManager";
    }

    @GetMapping("/projetSB/subjectManager")
    public String showSubjectManagerPage(ModelMap model){
        return "subjectManager";
    }

    @GetMapping("/projetSB/userManager")
    public String showUserManagerPage(ModelMap model){
        return "userManager";
    }

    @GetMapping("/projetSB/userToValidateManager")
    public String showUserToValidateManagerPage(ModelMap model){
        return "userToValidateManager";
    }

    @GetMapping("/projetSB/userSchedule")
    public String showUserSchedulePage(ModelMap model){
        return "userSchedule";
    }

    @GetMapping("/projetSB/register")
    public String showRegisterPage(ModelMap model){
        return "register";
    }

    @GetMapping("/projetSB/success")
    public String showSuccessPage(ModelMap model){
        return "success";
    }

    @GetMapping("/projetSB/login")
    public String showLoginPage(ModelMap model){
        return "login";
    }

    @GetMapping("/projetSB/personalInformation")
    public String showPersonalInformationPage(ModelMap model){
        return "personalInformation";
    }

    @GetMapping("/projetSB/adminPage")
    public String showAdminPagePage(ModelMap model){
        return "adminPage";
    }

    @GetMapping(value = "/projetSB/")
    public String showIndexPage(ModelMap model){
        return "index";
    }

    @GetMapping("/projetSB/index")
    public String showIndexPageSecondWay(ModelMap model){
        return "index";
    }

}
