package com.example.testspring.servlets;

import com.example.testspring.entities.Classes;
import com.example.testspring.entities.Course;
import com.example.testspring.entities.Lesson;
import com.example.testspring.entities.Users;
import com.example.testspring.services.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/projetSB/LessonClassesManagerController")
public class LessonClassesManagerController {
    private final LessonService lessonService;
    private final LessonclassService lessonclassService;
    private final ClassesService classesService;
    private final CourseService courseService;
    private final UsersService usersService;

    @Autowired
    public LessonClassesManagerController(LessonService lessonService, LessonclassService lessonclassService, ClassesService classesService, CourseService courseService, UsersService usersService) {
        this.lessonService = lessonService;
        this.lessonclassService = lessonclassService;
        this.classesService = classesService;
        this.courseService = courseService;
        this.usersService = usersService;
    }

    @GetMapping
    protected String showLessenClassesManagerPage(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        String lessonIdString = request.getParameter("lessonId");
        List<Classes> availableClassesList = new ArrayList<>();
        List<Integer> participatingClassId = new ArrayList<>();
        List<Classes> participatingClass = new ArrayList<>();
        Course lessonCourse = null;
        Users lessonTeacher = null;

        if(lessonIdString == null || lessonIdString.isEmpty()) {
            request.setAttribute("erreur", "Erreur : Veuillez choisir une séance.");
            return "redirect:/projetSB/LessonManagerController";
        }

        int lessonId = Integer.parseInt(lessonIdString);

        if(lessonService.getLessonById(lessonId) == null) {
            request.setAttribute("erreur", "Erreur : Veuillez choisir une séance.");
            return "redirect:/projetSB/LessonManagerController";
        }

        Lesson lesson = lessonService.getLessonById(lessonId);
        availableClassesList = classesService.getAvailableClassesForLesson(lesson.getLessonId());
        participatingClassId = lessonclassService.getClassesByLessonId(lesson.getLessonId());
        Integer lessonCourseId = lesson.getCourseId();
        Integer lessonTeacherId = lesson.getTeacherId();

        if(lessonCourseId != null) {
            lessonCourse = courseService.getCourseById(lessonCourseId);
        }
        if(lessonTeacherId != null) {
            lessonTeacher = usersService.getUserById(lessonTeacherId);
        }
        for(Integer classId : participatingClassId) {
            Classes classe = classesService.getClasseById(classId);
            if(classe != null) {
                participatingClass.add(classe);
            }
        }

        request.setAttribute("lesson", lesson);
        request.setAttribute("availableClassesList", availableClassesList);
        request.setAttribute("participatingClass", participatingClass);
        request.setAttribute("lessonCourse", lessonCourse);
        request.setAttribute("lessonTeacher", lessonTeacher);

        return "lessonClassesManager";
    }

}