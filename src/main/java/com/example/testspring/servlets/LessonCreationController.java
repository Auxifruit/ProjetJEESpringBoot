package com.example.testspring.servlets;

import com.example.testspring.entities.Course;
import com.example.testspring.entities.Lesson;
import com.example.testspring.entities.Teacher;
import com.example.testspring.entities.Users;
import com.example.testspring.services.CourseService;
import com.example.testspring.services.LessonService;
import com.example.testspring.services.TeacherService;
import com.example.testspring.services.UsersService;
import com.example.testspring.util.DateUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
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
@RequestMapping("/projetSB/LessonCreationController")
public class LessonCreationController extends HttpServlet {

    private final LessonService lessonService;
    private final CourseService courseService;
    private final TeacherService teacherService;
    private final UsersService usersService;

    @Autowired
    public LessonCreationController(LessonService lessonService, TeacherService teacherService, CourseService courseService, UsersService usersService) {
        this.lessonService = lessonService;
        this.teacherService = teacherService;
        this.courseService = courseService;
        this.usersService = usersService;
    }

    @GetMapping
    protected String showLessonCreationPage(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<Lesson> lessonList = lessonService.getAllLessons();
        List<Course> courseList = courseService.getAllCourses();
        List<Teacher> teacherList = teacherService.getAllTeachers();

        Map<Integer, Users> mapLessonIdTeacher = new HashMap<>();
        Map<Integer, Course> mapLessonIdCourse = new HashMap<>();
        Map<Integer, Users> mapTeacherIdUsers = new HashMap<>();

        for(Teacher teacher : teacherList) {
            Users user = usersService.getUserById(teacher.getTeacherId());
            if(user != null) {
                mapTeacherIdUsers.put(teacher.getTeacherId(), user);
            }
        }

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
        request.setAttribute("courses", courseList);
        request.setAttribute("teachers", teacherList);
        request.setAttribute("mapLessonIdTeacher", mapLessonIdTeacher);
        request.setAttribute("mapLessonIdCourse", mapLessonIdCourse);
        request.setAttribute("mapTeacherIdUsers", mapTeacherIdUsers);

        return "lessonCreation";
    }

    @PostMapping
    protected String handleLessonCreation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String courseIdString = request.getParameter("course");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String teacherIdString = request.getParameter("teacher");

        if(courseIdString == null || courseIdString.isEmpty()) {
            request.setAttribute("erreur", "Erreur : Veuillez choisir un cours.");
            return "redirect:/projetSB/LessonCreationController";
        }

        if(teacherIdString == null || teacherIdString.isEmpty()) {
            request.setAttribute("erreur", "Erreur : Veuillez choisir un enseignant.");
            return "redirect:/projetSB/LessonCreationController";
        }

        int courseId = Integer.parseInt(courseIdString);
        int teacherId = Integer.parseInt(teacherIdString);

        if((startDate == null || startDate.isEmpty()) || (endDate == null || endDate.isEmpty())) {
            request.setAttribute("erreur", "Erreur : Veuillez saisir les 2 dates nécessaire pour la création d'une séance.");
            return "redirect:/projetSB/LessonCreationController";
        }

        String erreur = DateUtil.areDatesValid(startDate, endDate);
        if(erreur != null) {
            request.setAttribute("erreur", erreur);
            return "redirect:/projetSB/LessonCreationController";
        }

        if(lessonService.isLessonPossible(null, teacherId, DateUtil.dateStringToTimestamp(startDate), DateUtil.dateStringToTimestamp(endDate)) == false) {
            request.setAttribute("erreur", "Erreur : Le professeur a déjà cours à ces dates.");
            return "redirect:/projetSB/LessonCreationController";
        }

        Lesson lesson = new Lesson();
        lesson.setLessonStartDate(DateUtil.dateStringToTimestamp(startDate));
        lesson.setLessonEndDate(DateUtil.dateStringToTimestamp(endDate));
        lesson.setCourseId(courseId);
        lesson.setTeacherId(teacherId);

        String error = lessonService.addLesson(lesson);
        if(error == null) {
            return "redirect:/projetSB/LessonManagerController";
        }
        else {
            request.setAttribute("erreur", error);
            return "redirect:/projetSB/LessonCreationController";
        }
    }


}
