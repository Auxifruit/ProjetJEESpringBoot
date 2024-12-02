package com.example.testspring.servlets;

import com.example.testspring.entities.*;
import com.example.testspring.services.*;
import com.example.testspring.util.DateUtil;
import com.example.testspring.util.GMailer;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/projetSB/LessonModificationController")
public class LessonModificationController {
    private final LessonService lessonService;
    private final LessonclassService lessonclassService;
    private final UsersService usersService;
    private final CourseService courseService;
    private final TeacherService teacherService;

    @Autowired
    public LessonModificationController(LessonService lessonService, UsersService usersService, CourseService courseService, TeacherService teacherService, LessonclassService lessonclassService) {
        this.lessonService = lessonService;
        this.usersService = usersService;
        this.courseService = courseService;
        this.teacherService = teacherService;
        this.lessonclassService = lessonclassService;
    }

    @GetMapping
    protected String showModificationLessonPage(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String lessonIdString = request.getParameter("lessonId");
        List<Course> courseList = courseService.getAllCourses();
        List<Teacher> teachers = teacherService.getAllTeachers();
        List<Users> teacherList = new ArrayList<>();
        Users lessonTeacher = null;
        Course lessonCourse = null;

        if(lessonIdString == null || lessonIdString.isEmpty()) {
            request.setAttribute("erreur", "Erreur : Veuillez choisir une séance.");
            return "redirect:/projetSB/LessonManagerController";
        }

        int lessonId = Integer.parseInt(lessonIdString);
        Lesson lesson = lessonService.getLessonById(lessonId);

        Integer teacherId = lesson.getTeacherId();
        Integer courseId = lesson.getCourseId();

        if(teacherId != null) {
            lessonTeacher = usersService.getUserById(teacherId);
        }

        if(courseId != null) {
            lessonCourse = courseService.getCourseById(courseId);
        }

        for(Teacher teacher : teachers) {
            Users u = usersService.getUserById(teacher.getTeacherId());

            if(u != null) {
                teacherList.add(u);
            }
        }

        request.setAttribute("courses", courseList);
        request.setAttribute("teacherList", teacherList);
        request.setAttribute("lesson", lesson);
        request.setAttribute("lessonTeacher", lessonTeacher);
        request.setAttribute("lessonCourse", lessonCourse);

        return "lessonModification";
    }

    @PostMapping
    protected String handleModificationLesson(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String lessonIdString = request.getParameter("lessonId");
        String newStartDate = request.getParameter("newStartDate");
        String newEndDate = request.getParameter("newEndDate");
        String newCourseIdString = request.getParameter("newCourseId");
        String newTeacherIdString = request.getParameter("newTeacherId");
        boolean dateModified = false;
        boolean teacherModified = false;

        if(lessonIdString == null || lessonIdString.isEmpty()) {
            request.setAttribute("erreur", "Erreur : Veuillez choisir une séance.");
            return "redirect:/projetSB/LessonManagerController";
        }

        if((newStartDate == null || newStartDate.isEmpty()) && (newEndDate == null || newEndDate.isEmpty()) && (newCourseIdString == null || newCourseIdString.isEmpty()) &&  (newTeacherIdString == null || newTeacherIdString.isEmpty())) {
            request.setAttribute("erreur", "Erreur : Veuillez choisir au moins un champ à modifier.");
            return "redirect:/projetSB/LessonModificationController";
        }

        int lessonId = Integer.parseInt(lessonIdString);
        Lesson lesson = lessonService.getLessonById(lessonId);

        if(newStartDate == null || newStartDate.isEmpty()) {
            newStartDate = lesson.getLessonStartDate().toString();
            newStartDate = newStartDate.substring(0, 16);
            newStartDate = newStartDate.replace(" ", "T");
        }
        else {
            dateModified = true;
        }

        if(newEndDate == null || newEndDate.isEmpty()) {
            newEndDate = lesson.getLessonEndDate().toString();
            newEndDate = newEndDate.substring(0, 16);
            newEndDate = newEndDate.replace(" ", "T");
        }
        else {
            dateModified = true;
        }

        int newCourseId;

        if(newCourseIdString == null || newCourseIdString.isEmpty()) {
            newCourseId = lesson.getCourseId();
        }
        else {
            newCourseId = Integer.parseInt(newCourseIdString);
        }

        int newTeacherId;

        if(newTeacherIdString == null || newTeacherIdString.isEmpty()) {
            newTeacherId = lesson.getTeacherId();
        }
        else {
            newTeacherId = Integer.parseInt(newTeacherIdString);
            teacherModified = true;
        }

        if(dateModified == true) {
            String erreur = DateUtil.areDatesValid(newStartDate, newEndDate);
            if(erreur != null) {
                request.setAttribute("erreur", erreur);
                return "redirect:/projetSB/LessonModificationController";
            }
        }

        if((dateModified == true || teacherModified == true ) && lessonService.isLessonPossible(lessonId, newTeacherId, DateUtil.dateStringToTimestamp(newStartDate), DateUtil.dateStringToTimestamp(newEndDate)) == false) {
            request.setAttribute("erreur", "Erreur : Le professeur a déjà cours à ces dates.");
            return "redirect:/projetSB/LessonModificationController";
        }

        if(dateModified == true) {
            lesson.setLessonStartDate(DateUtil.dateStringToTimestamp(newStartDate));
            lesson.setLessonEndDate(DateUtil.dateStringToTimestamp(newEndDate));
        }
        lesson.setCourseId(newCourseId);
        lesson.setTeacherId(newTeacherId);

        String error = lessonService.modifyLesson(lesson);
        if(error == null) {
            // Récupérer les étudiants inscrits à la séance modifiée
            List<Student> studentsInLesson = lessonclassService.getStudentsByLessonId(lessonId);

            System.out.println("studentsInLesson size : " + studentsInLesson.size());

            if(studentsInLesson != null && !studentsInLesson.isEmpty()) {
                // Pour chaque étudiant, envoyer un email de notification
                for (Student student : studentsInLesson) {
                    String studentEmail = usersService.getUserById(student.getStudentId()).getUserEmail();

                    if (studentEmail != null) {
                        String subject = "Modification de votre séance";
                        String body = "Bonjour,\n\n"
                                + "Nous vous informons que la séance pour la matière " + lesson.getCourseId() + " a été modifiée.\n"
                                + "Nouvelle date : " + newStartDate + " - " + newEndDate + "\n\n"
                                + "Cordialement,\nL'équipe pédagogique";

                        try {
                            GMailer gmailer = new GMailer();
                            gmailer.sendMail(subject, body, studentEmail);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            // Rediriger vers la page de gestion des séances
            return "redirect:/projetSB/LessonManagerController";
        }
        else {
            return "redirect:/projetSB/LessonModificationController";
        }

    }


}
