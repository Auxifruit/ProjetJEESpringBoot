package com.example.testspring.servlets;

import com.example.testspring.entities.Course;
import com.example.testspring.entities.Lesson;
import com.example.testspring.entities.Users;
import com.example.testspring.services.CourseService;
import com.example.testspring.services.LessonService;
import com.example.testspring.services.UsersService;
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
@RequestMapping("/projetSB/LessonManagerController")
public class LessonManagerController {
    private final LessonService lessonService;
    private final UsersService usersService;
    private final CourseService courseService;

    @Autowired
    public LessonManagerController(LessonService lessonService, UsersService usersService, CourseService courseService) {
        this.lessonService = lessonService;
        this.usersService = usersService;
        this.courseService = courseService;
    }

    @GetMapping
    protected String showLessonManagerController(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Lesson> lessonList = lessonService.getAllLessons();
        Map<Integer, Users> mapLessonIdTeacher = new HashMap<>();
        Map<Integer, Course> mapLessonIdCourse = new HashMap<>();

        for(Lesson lesson : lessonList) {
            Integer teacherId = lesson.getTeacherId();
            Integer courseId = lesson.getCourseId();

            if(teacherId != null) {
                Users user = usersService.getUserById(teacherId);
                if(user != null) {
                    mapLessonIdTeacher.put(lesson.getLessonId(), user);
                }
            }

            if(courseId != null) {
                Course course = courseService.getCourseById(courseId);
                if(course != null) {
                    mapLessonIdCourse.put(lesson.getLessonId(), course);
                }
            }
        }

        request.setAttribute("lessons", lessonList);
        request.setAttribute("mapLessonIdTeacher", mapLessonIdTeacher);
        request.setAttribute("mapLessonIdCourse", mapLessonIdCourse);

        return "lessonManager";
    }

}
