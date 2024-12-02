package com.example.testspring.servlets;

import com.example.testspring.entities.*;
import com.example.testspring.services.*;
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
import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/projetSB/UserScheduleController")
public class UserScheduleController extends HttpServlet {

    private final LessonService lessonService;
    private final UsersService usersService;
    private final CourseService courseService;
    private final ClassesService classesService;

    private final LessonclassService lessonclassService;

    @Autowired
    public UserScheduleController(LessonService lessonService, UsersService usersService, CourseService courseService, LessonclassService lessonclassService, ClassesService classesService) {
        this.lessonService = lessonService;
        this.usersService = usersService;
        this.courseService = courseService;
        this.lessonclassService = lessonclassService;
        this.classesService = classesService;
    }

    @PostMapping
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("SAVEG");
        doGet(request, response);
    }

    @GetMapping
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Users user = (Users) session.getAttribute("connectedUser");

        Map<Integer, Users> mapLessonIdTeacher = new HashMap<>();
        Map<Integer, Course> mapLessonIdCourse = new HashMap<>();
        Map<Integer, List<Classes>> mapLessonIdClassesList = new HashMap<>();

        if(user == null) {
            request.getRequestDispatcher("index").forward(request, response);
            return;
        }

        int userId = user.getUserId();

        String userIdForm = request.getParameter("userId");
        if(userIdForm != null) {
            userId = Integer.parseInt(userIdForm);
        }

        Map<LocalDate, List<Lesson>> lessonsByDay = new TreeMap<>();
        Role userRole = usersService.getUserById(userId).getUserRole();

        List<Lesson> lessonList;

        switch (userRole) {
            case student:
                lessonList = lessonService.getLessonsByStudentId(userId);
                break;
            case teacher:
                lessonList = lessonService.getLessonsByTeacherId(userId);
                break;
            default:
                System.out.println("Erreur : rôle non supporté.");
                request.getRequestDispatcher("index").forward(request, response);
                return;
        }

        for (Lesson lesson : lessonList) {
            LocalDate date = lesson.getLessonStartDate().toLocalDateTime().toLocalDate();
            lessonsByDay.computeIfAbsent(date, k -> new ArrayList<>()).add(lesson);
        }

        Map<LocalDate, List<Lesson>> pastLessons = new TreeMap<>();
        Map<LocalDate, List<Lesson>> futureLessons = new TreeMap<>();
        LocalDate today = LocalDate.now();

        for (Map.Entry<LocalDate, List<Lesson>> entry : lessonsByDay.entrySet()) {
            if (entry.getKey().isBefore(today)) {
                pastLessons.put(entry.getKey(), entry.getValue());
            } else {
                futureLessons.put(entry.getKey(), entry.getValue());
            }
        }

        for(Map.Entry<LocalDate, List<Lesson>> entry : lessonsByDay.entrySet()) {
            for(Lesson lesson : entry.getValue()) {
                Integer teacherId = lesson.getTeacherId();
                Integer courseId = lesson.getCourseId();

                if(teacherId != null) {
                    Users teacher = usersService.getUserById(teacherId);
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

                List<Integer> classesListFromLesson = lessonclassService.getClassesByLessonId(lesson.getLessonId());
                List<Classes> classesList = new ArrayList<>();
                for(Integer classId : classesListFromLesson) {
                    Classes classe = classesService.getClasseById(classId);

                    classesList.add(classe);
                }

                if(classesList != null) {
                    mapLessonIdClassesList.put(lesson.getLessonId(), classesList);
                }
            }
        }


        try {
            request.setAttribute("userIdForm", userId);
            request.setAttribute("lessonList", lessonsByDay);
            request.setAttribute("pastLessons", pastLessons);
            request.setAttribute("futureLessons", futureLessons);
            request.setAttribute("mapLessonIdTeacher", mapLessonIdTeacher);
            request.setAttribute("mapLessonIdCourse", mapLessonIdCourse);
            request.setAttribute("mapLessonIdClassesList", mapLessonIdClassesList);
            request.getRequestDispatcher("userSchedule").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
