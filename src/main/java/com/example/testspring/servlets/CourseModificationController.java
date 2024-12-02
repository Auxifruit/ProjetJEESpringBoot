package com.example.testspring.servlets;

import com.example.testspring.entities.Course;
import com.example.testspring.entities.Subjects;
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
import java.util.List;

@Controller
@RequestMapping("/projetSB/CourseModificationController")
public class CourseModificationController {

    private final CourseService courseService;
    private final SubjectService subjectService;

    @Autowired
    public CourseModificationController(CourseService courseService, SubjectService subjectService) {
        this.courseService = courseService;
        this.subjectService = subjectService;
    }

    @GetMapping
    protected String showCourseModificationPage(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String courseIdString = request.getParameter("courseId");
        List<Subjects> subjectList = subjectService.getAllSubjects();

        if(courseIdString == null || courseIdString.isEmpty()) {
            request.setAttribute("erreur", "Erreur : Veuillez choisir un cours.");
            return "redirect:/projetSB/CourseManagerController";
        }

        int courseId = Integer.parseInt(courseIdString);
        Course course = courseService.getCourseById(courseId);
        Subjects courseSubject = null;
        if(course.getSubjectId() != null) {
            courseSubject = subjectService.getSubjectById(course.getSubjectId());
        }

        request.setAttribute("course", course);
        request.setAttribute("courseSubject", courseSubject);
        request.setAttribute("subjects", subjectList);
        return "courseModification";
    }

    @PostMapping
    protected String handleCourseModification(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String courseIdString = request.getParameter("courseId");
        String newCourseSubjectIdString = request.getParameter("newCourseSubjectId");
        String newCourseName = request.getParameter("newCourseName");

        if(courseIdString == null || courseIdString.isEmpty()) {
            request.setAttribute("erreur", "Erreur : Veuillez choisir un cours.");
            return "redirect:/projetSB/CourseManagerController";
        }

        if((newCourseSubjectIdString == null || newCourseSubjectIdString.isEmpty()) && (newCourseName == null || newCourseName.isEmpty())) {
            request.setAttribute("erreur", "Erreur : Veuillez modifier au moins un champs.");
            return "redirect:/projetSB/CourseModificationController";
        }

        int courseId = Integer.parseInt(courseIdString);
        Course course = courseService.getCourseById(courseId);

        if(newCourseName == null || newCourseName.isEmpty()) {
            newCourseName = course.getCourseName();
        }

        int newCourseSubjectId;

        if(newCourseSubjectIdString == null || newCourseSubjectIdString.isEmpty()) {
            newCourseSubjectId = course.getSubjectId();
        }
        else {
            newCourseSubjectId = Integer.parseInt(newCourseSubjectIdString);
        }

        course.setCourseName(newCourseName);
        course.setSubjectId(newCourseSubjectId);

        String error = courseService.modifyCourse(course);
        if(error == null) {
            return "redirect:/projetSB/CourseManagerController";
        }
        else {
            request.setAttribute("erreur", error);
            return "redirect:/projetSB/CourseModificationController";
        }

    }


}
