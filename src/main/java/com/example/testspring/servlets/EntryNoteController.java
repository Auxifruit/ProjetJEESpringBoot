package com.example.testspring.servlets;

import com.example.testspring.entities.*;
import com.example.testspring.services.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/projetSB/EntryNoteController")
public class EntryNoteController {
    private final CourseService courseService;
    private final UsersService usersService;
    private final ClassesService classesService;

    @Autowired
    public EntryNoteController(CourseService courseService , UsersService usersService, ClassesService classesService) {
        this.courseService = courseService;
        this.usersService = usersService;
        this.classesService = classesService;
    }

    @GetMapping
    protected String showAddGradePage(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        Users connectedUser = (Users) session.getAttribute("connectedUser");

        if(!connectedUser.getUserRole().equals(Role.teacher)) {
            return "index";
        }

        setCommonAttributes(request, connectedUser.getUserId());
        request.setAttribute("teacherID", connectedUser.getUserId());
        return "addGrade";
    }

    @PostMapping
    protected String handleAddGrade(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String teacherIdString = request.getParameter("teacherID");
        String courseIdString = request.getParameter("courseId");
        String classIdString = request.getParameter("classId");

        if((courseIdString == null || courseIdString.isEmpty()) || (classIdString == null || classIdString.isEmpty())) {
            return "redirect:/projetSB/EntryNoteController";
        }

        int teacherID = Integer.parseInt(teacherIdString);
        int courseId = Integer.parseInt(courseIdString);
        int classId = Integer.parseInt(classIdString);

        System.out.println("ID Discipline: " + courseId + " ; ID Class: " + classId);

        // Validate the criteria
        List<Users> students = usersService.getStudentsByDisciplineAndClass(courseId, classId, teacherID);

        // If no valid criteria, set an empty list or null
        request.setAttribute("students", students);
        // set the CourseId, with the CourseName(Discipline) to sent it in parameters when we create a grade
        request.setAttribute("courseId", courseId);
        // reset the communes attributes before going back to the jsp
        setCommonAttributes(request,teacherID);
        // set back teacherID
        request.setAttribute("teacherID", teacherID);
        // Forward the request to the JSP page to display the results
        return "addGrade";
    }

    private void setCommonAttributes(HttpServletRequest request, int teacherID) {
        // get disciplines and classes and set them as attributes
        List<Course> disciplines = courseService.getAllTeacherCoursesByTeacherId(teacherID);
        List<Classes> classes = classesService.getAllTeacherClassByTeacherId(teacherID);

        request.setAttribute("disciplines", disciplines);
        request.setAttribute("classes", classes);
    }

}
