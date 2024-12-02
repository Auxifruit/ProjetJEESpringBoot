package com.example.testspring.servlets;

import com.example.testspring.entities.*;
import com.example.testspring.services.*;
import com.example.testspring.util.GeneratorPdfGradeReport;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/projetSB/TeacherGradeController")
public class TeacherGradeController {

    private final StudentService studentService;
    private final ClassesService classesService;
    private final GradeService gradeService;
    private final CourseService courseService;
    private final UsersService usersService;

    @Autowired
    public TeacherGradeController(StudentService studentService, ClassesService classesService, GradeService gradeService, CourseService courseService, UsersService usersService) {
        this.studentService = studentService;
        this.classesService = classesService;
        this.gradeService = gradeService;
        this.courseService = courseService;
        this.usersService = usersService;
    }

    @GetMapping
    protected String showTeacherGradePage(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        Users connectedUser = (Users) session.getAttribute("connectedUser");

        if (connectedUser == null) {
            return "index";
        }

        int teacherId = connectedUser.getUserId();

        String userIdForm = request.getParameter("userId");
        if (userIdForm != null) {
            teacherId = Integer.parseInt(userIdForm);
        }

        List<Course> courseList = courseService.getAllCourses();
        List<Grade> teacherGradeList = gradeService.getGradesByTeacherId(teacherId);
        Map<Integer, Users> mapGradeIdUser = new HashMap<>();

        Map<Classes, List<Grade>> classGradeMap = new HashMap<>();

        for (Grade grade : teacherGradeList) {
            Classes classe = classesService.getClasseById(studentService.getStudentById(grade.getStudentId()).getClassId());
            Users u = usersService.getUserById(grade.getStudentId());

            classGradeMap.computeIfAbsent(classe, k -> new ArrayList<>()).add(grade);

            if (u != null) {
                mapGradeIdUser.put(grade.getGradeId(), u);
            }
        }

        // Construire la map cours -> classGradeMap
        Map<Course, Map<Classes, List<Grade>>> courseClassGradeMap = new HashMap<>();

        for (Course course : courseList) {
            // Filtrer les notes liées à ce cours
            Map<Classes, List<Grade>> filteredClassGradeMap = new HashMap<>();

            for (Map.Entry<Classes, List<Grade>> entry : classGradeMap.entrySet()) {
                Classes classe = entry.getKey();
                List<Grade> grades = entry.getValue();

                // Filtrer les notes de la classe associées au cours
                List<Grade> filteredGrades = new ArrayList<>();
                for (Grade grade : grades) {
                    if (grade.getCourseId() == course.getCourseId()) { // Vérifie si la note appartient au cours
                        filteredGrades.add(grade);
                    }
                }

                if (!filteredGrades.isEmpty()) {
                    filteredClassGradeMap.put(classe, filteredGrades);
                }
            }

            if (!filteredClassGradeMap.isEmpty()) {
                courseClassGradeMap.put(course, filteredClassGradeMap);
            }
        }

        request.setAttribute("userIdForm", teacherId);
        request.setAttribute("courseClassGradeMap", courseClassGradeMap);
        request.setAttribute("mapGradeIdUser", mapGradeIdUser);
        return "teacherGradeViewer";
    }
}