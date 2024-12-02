package com.example.testspring.servlets;

import com.example.testspring.entities.*;
import com.example.testspring.services.ClassesService;
import com.example.testspring.services.MajorService;
import com.example.testspring.services.StudentService;
import com.example.testspring.services.UsersService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/projetSB/PersonalInformationController")
public class PersonalInformationController extends HttpServlet {
    private final UsersService usersService;
    private final StudentService studentService;
    private final ClassesService classesService;
    private final MajorService majorService;

    @Autowired
    public PersonalInformationController(UsersService usersService, StudentService studentService, ClassesService classesService, MajorService majorService) {
        this.usersService = usersService;
        this.studentService = studentService;
        this.classesService = classesService;
        this.majorService = majorService;
    }

    @GetMapping
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        doPost(request, response);
    }

    @PostMapping
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        Users connectedUser = (Users) session.getAttribute("connectedUser");

        Classes studentClass = null;
        Major studentMajor = null;

        if (connectedUser == null) {
            request.getRequestDispatcher("index");
            return;
        }

        if(connectedUser.getUserRole().equals(Role.student)) {
            Student student = studentService.getStudentById(connectedUser.getUserId());

            Integer classId = student.getClassId();
            Integer majorId = student.getMajorId();

            if(classId != null) {
                studentClass = classesService.getClasseById(classId);
            }
            if(majorId != null) {
                studentMajor = majorService.getMajorById(majorId);
            }
        }


        try {
            request.setAttribute("studentClass", studentClass);
            request.setAttribute("studentMajor", studentMajor);
            request.getRequestDispatcher("personalInformation").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
