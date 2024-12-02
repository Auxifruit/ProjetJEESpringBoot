package com.example.testspring.servlets;

import com.example.testspring.entities.Course;
import com.example.testspring.entities.Subjects;
import com.example.testspring.entities.Users;
import com.example.testspring.services.CourseService;
import com.example.testspring.services.SubjectService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
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
@RequestMapping("/projetSB/CourseManagerController")
public class CourseManagerController extends HttpServlet {
    private final CourseService courseService;
    private final SubjectService subjectService;

    @Autowired
    public CourseManagerController(CourseService courseService, SubjectService subjectService) {
        this.courseService = courseService;
        this.subjectService = subjectService;
    }

    @GetMapping
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Course> coursesList = courseService.getAllCourses();
        Map<Integer, Subjects> mapCourseIdSubject = new HashMap<>();

        for(Course course : coursesList) {
            Integer subjectId = course.getSubjectId();

            if(subjectId != null) {
                Subjects subject = subjectService.getSubjectById(subjectId);
                if(subject != null) {
                    mapCourseIdSubject.put(course.getCourseId(), subject);
                }
            }
        }

        request.setAttribute("courses", coursesList);
        request.setAttribute("mapCourseIdSubject", mapCourseIdSubject);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/projetSB/courseManager");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
