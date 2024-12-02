package com.example.testspring.servlets;

import com.example.testspring.entities.Course;
import com.example.testspring.entities.Subjects;
import com.example.testspring.services.ClassesService;
import com.example.testspring.services.CourseService;
import com.example.testspring.services.SubjectService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/projetSB/CourseCreationController")
public class CourseCreationController extends HttpServlet {

    private final CourseService courseService;
    private final SubjectService subjectService;

    @Autowired
    public CourseCreationController(CourseService courseService, SubjectService subjectService) {
        this.courseService = courseService;
        this.subjectService = subjectService;
    }

    @GetMapping
    protected String showCourseCreationPage(HttpServletRequest request, HttpServletResponse response) {
        List<Course> courseList = courseService.getAllCourses();
        List<Subjects> subjectList = subjectService.getAllSubjects();

        Map<Integer, Subjects> mapCourseIdSubject = new HashMap<>();

        for(Course course : courseList) {
            Integer subjectId = course.getSubjectId();

            if(subjectId != null) {
                Subjects subject = subjectService.getSubjectById(subjectId);
                if(subject != null) {
                    mapCourseIdSubject.put(course.getCourseId(), subject);
                }
            }
        }

        request.setAttribute("courses", courseList);
        request.setAttribute("subjects", subjectList);
        request.setAttribute("mapCourseIdSubject", mapCourseIdSubject);


        return "courseCreation";
    }

    @PostMapping
    protected String handleCourseCreation(HttpServletRequest request, HttpServletResponse response) {
        String subjectIdString = request.getParameter("courseSubjectId");
        String courseName = request.getParameter("courseName");

        if(subjectIdString == null || subjectIdString.isEmpty()) {
            request.setAttribute("erreur", "Erreur : Veuillez choisir une matière.");
            return "redirect:/projetSB/CourseCreationController";
        }

        if(courseName == null || courseName.isEmpty()) {
            request.setAttribute("erreur", "Erreur : Veuillez saisir le nom du nouveau cours.");
            return "redirect:/projetSB/CourseCreationController";
        }

        int subjectId = Integer.parseInt(subjectIdString);

        Course course = new Course();
        course.setCourseName(courseName);
        course.setSubjectId(subjectId);

        String error = courseService.addCourse(course);
        if(error == null) {
            return "redirect:/projetSB/CourseManagerController";
        }
        else {
            request.setAttribute("erreur", error);
            return "redirect:/projetSB/CourseCreationController";
        }
    }


}
