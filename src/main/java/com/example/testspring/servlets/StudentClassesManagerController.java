package com.example.testspring.servlets;


import com.example.testspring.entities.Classes;
import com.example.testspring.entities.Major;
import com.example.testspring.entities.Student;
import com.example.testspring.entities.Users;
import com.example.testspring.services.ClassesService;
import com.example.testspring.services.MajorService;
import com.example.testspring.services.StudentService;
import com.example.testspring.services.UsersService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/projetSB/StudentClassesManagerController")
public class StudentClassesManagerController {
    private final ClassesService classesService;
    private final StudentService studentService;
    private final UsersService usersService;
    private final MajorService majorService;

    @Autowired
    public StudentClassesManagerController(ClassesService classesService, StudentService studentService, UsersService usersService, MajorService majorService) {
        this.classesService = classesService;
        this.studentService = studentService;
        this.usersService = usersService;
        this.majorService = majorService;
    }

    @GetMapping
    protected String showStudentClassesManagerPage(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        String classIdString = request.getParameter("classesId");
        List<Student> availableStudentList;
        List<Student> participatingStudentList;
        Map<Integer, Users> mapStudentIdUser = new HashMap<>();
        Map<Integer, Major> mapStudentIdMajor = new HashMap<>();
        Map<Integer, Classes> mapStudentIdClass = new HashMap<>();

        if(classIdString == null || classIdString.isEmpty()) {
            request.setAttribute("erreur", "Erreur : Veuillez choisir une classe.");
            return "redirect:/projetSB/ClassesManagerController";
        }

        int classId = Integer.parseInt(classIdString);
        Classes classe = classesService.getClasseById(classId);

        if(classe == null) {
            request.setAttribute("erreur", "Erreur : Veuillez choisir une classe.");
            return "redirect:/projetSB/ClassesManagerController";
        }

        availableStudentList = studentService.getStudentsWithoutClasses();
        participatingStudentList = studentService.getStudentsFromClassId(classe.getClassId());

        for(Student availableStudent : availableStudentList) {
            Users studentUser = usersService.getUserById(availableStudent.getStudentId());
            Integer availableStudentClassId = availableStudent.getClassId();
            Integer availableStudentMajorId = availableStudent.getMajorId();

            mapStudentIdUser.put(availableStudent.getStudentId(), studentUser);

            if(availableStudentClassId != null) {
                Classes availableStudentClass = classesService.getClasseById(availableStudentClassId);
                mapStudentIdClass.put(availableStudent.getStudentId(), availableStudentClass);
            }
            if(availableStudentMajorId != null) {
                Major availableStudentMajor = majorService.getMajorById(availableStudentMajorId);
                mapStudentIdMajor.put(availableStudent.getStudentId(), availableStudentMajor);
            }
        }

        for(Student participatingStudent : participatingStudentList) {
            Users studentUser = usersService.getUserById(participatingStudent.getStudentId());
            Integer participatingStudentClassId = participatingStudent.getClassId();
            Integer participatingStudentMajorId = participatingStudent.getMajorId();

            mapStudentIdUser.put(participatingStudent.getStudentId(), studentUser);

            if(participatingStudentClassId != null) {
                Classes participatingStudentClass = classesService.getClasseById(participatingStudentClassId);
                mapStudentIdClass.put(participatingStudent.getStudentId(), participatingStudentClass);
            }
            if(participatingStudentMajorId != null) {
                Major participatingStudentMajor = majorService.getMajorById(participatingStudentMajorId);
                mapStudentIdMajor.put(participatingStudent.getStudentId(), participatingStudentMajor);
            }
        }

        request.setAttribute("classe", classe);
        request.setAttribute("availableStudents", availableStudentList);
        request.setAttribute("participatingStudentList", participatingStudentList);
        request.setAttribute("mapStudentIdUser", mapStudentIdUser);
        request.setAttribute("mapStudentIdMajor", mapStudentIdMajor);
        request.setAttribute("mapStudentIdClass", mapStudentIdClass);

        return "studentClassesManager";
    }
}