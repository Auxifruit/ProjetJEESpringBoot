package com.example.testspring.servlets;

import com.example.testspring.entities.*;
import com.example.testspring.services.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/projetSB/GradeManagerController")
public class GradeManagerController extends HttpServlet {

    private final GradeService gradeService;
    private final UsersService usersService;
    private final StudentService studentService;
    private final ClassesService classesService;
    private final CourseService courseService;

    @Autowired
    public GradeManagerController(GradeService gradeService, UsersService usersService, StudentService studentService, ClassesService classesService, CourseService courseService) {
        this.gradeService = gradeService;
        this.usersService = usersService;
        this.studentService = studentService;
        this.classesService = classesService;
        this.courseService = courseService;
    }

    @GetMapping
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Grade> gradeList = gradeService.getAllGrades();

        Map<Integer, Course> mapGradeIdCourse = new HashMap<>();
        Map<Integer, Users> mapGradeIdStudent = new HashMap<>();
        Map<Integer, Classes> mapStudentIdClass = new HashMap<>();
        Map<Integer, Users> mapGradeIdTeacher = new HashMap<>();

        for(Grade grade : gradeList) {
            Integer courseId = grade.getCourseId();
            if(courseId != null) {
                Course course = courseService.getCourseById(courseId);

                mapGradeIdCourse.put(grade.getGradeId(), course);
            }

            Integer studentId = grade.getStudentId();
            Users userStudent = usersService.getUserById(studentId);
            mapGradeIdStudent.put(grade.getGradeId(), userStudent);

            Student student = studentService.getStudentById(studentId);
            Integer classId = student.getClassId();
            if(classId != null) {
                Classes classe = classesService.getClasseById(classId);

                mapStudentIdClass.put(student.getStudentId(), classe);
            }

            Integer teacherId = grade.getTeacherId();
            if(teacherId != null) {
                Users teacher = usersService.getUserById(teacherId);

                mapGradeIdTeacher.put(grade.getGradeId(), teacher);
            }
        }

        request.setAttribute("grades", gradeList);
        request.setAttribute("mapGradeIdCourse", mapGradeIdCourse);
        request.setAttribute("mapGradeIdStudent", mapGradeIdStudent);
        request.setAttribute("mapStudentIdClass", mapStudentIdClass);
        request.setAttribute("mapGradeIdTeacher", mapGradeIdTeacher);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/projetSB/gradeManager");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
