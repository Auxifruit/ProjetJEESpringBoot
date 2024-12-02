package com.example.testspring.servlets;

import com.example.testspring.entities.*;
import com.example.testspring.services.*;
import com.example.testspring.util.GMailer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/projetSB/GradeCreationController")
public class GradeCreationController {
    private final GradeService gradeService;
    private final UsersService usersService;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final ClassesService classesService;
    private final CourseService courseService;

    @Autowired
    public GradeCreationController(GradeService gradeService, UsersService usersService, StudentService studentService, TeacherService teacherService, ClassesService classesService, CourseService courseService) {
        this.gradeService = gradeService;
        this.usersService = usersService;
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.classesService = classesService;
        this.courseService = courseService;
    }

    @GetMapping
    protected String showGradeCreationPage(HttpServletRequest request, HttpServletResponse response) {
        List<Grade> gradeList = gradeService.getAllGrades();
        List<Course> courseList = courseService.getAllCourses();
        List<Student> studentList = studentService.getAllStudents();
        List<Teacher> teacherList = teacherService.getAllTeachers();

        Map<Integer, Course> mapGradeIdCourse = new HashMap<>();
        Map<Integer, Users> mapGradeIdStudent = new HashMap<>();
        Map<Integer, Users> mapStudentIdUsers = new HashMap<>();
        Map<Integer, Classes> mapStudentIdClass = new HashMap<>();
        Map<Integer, Users> mapGradeIdTeacher = new HashMap<>();
        Map<Integer, Users> mapTeacherIdUsers = new HashMap<>();

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

        for(Student student : studentList) {
            Users u = usersService.getUserById(student.getStudentId());

            if(u != null) {
                mapStudentIdUsers.put(student.getStudentId(), u);
            }
        }

        for(Teacher teacher : teacherList) {
            Users u = usersService.getUserById(teacher.getTeacherId());

            if(u != null) {
                mapTeacherIdUsers.put(teacher.getTeacherId(), u);
            }
        }

        request.setAttribute("grades", gradeList);
        request.setAttribute("courses", courseList);
        request.setAttribute("students", studentList);
        request.setAttribute("teachers", teacherList);
        request.setAttribute("mapGradeIdCourse", mapGradeIdCourse);
        request.setAttribute("mapGradeIdStudent", mapGradeIdStudent);
        request.setAttribute("mapStudentIdUsers", mapGradeIdStudent);
        request.setAttribute("mapStudentIdClass", mapStudentIdClass);
        request.setAttribute("mapGradeIdTeacher", mapGradeIdTeacher);
        request.setAttribute("mapTeacherIdUsers", mapGradeIdTeacher);

        return "gradeCreation";
    }
    @PostMapping
    protected String handleGradeCreationPage(HttpServletRequest request, HttpServletResponse response) {
        String newGradeName = request.getParameter("newGradeName");
        String newGradeCourseIdString = request.getParameter("newGradeCourseId");
        String newGradeValueString = request.getParameter("newGradeValue");
        String newGradeCoefficientString = request.getParameter("newGradeCoefficient");
        String newGradeStudentIdString = request.getParameter("newGradeStudentId");
        String newGradeTeacherIdString = request.getParameter("newGradeTeacherId");

        if((newGradeName == null || newGradeName.isEmpty()) || (newGradeCourseIdString == null || newGradeCourseIdString.isEmpty()) || (newGradeValueString == null || newGradeValueString.isEmpty())
                || (newGradeCoefficientString == null || newGradeCoefficientString.isEmpty()) || (newGradeStudentIdString == null || newGradeStudentIdString.isEmpty()) || (newGradeTeacherIdString == null || newGradeTeacherIdString.isEmpty())) {
            request.setAttribute("erreur", "Erreur : Veuillez remplir tous les champs.");
            return "redirect:/projetSB/GradeCreationController";
        }

        double value = Double.parseDouble(newGradeValueString);
        int coefficient = Integer.parseInt(newGradeCoefficientString);
        int studentId = Integer.parseInt(newGradeStudentIdString);
        int courseId = Integer.parseInt(newGradeCourseIdString);
        int teacherId = Integer.parseInt(newGradeTeacherIdString);

        if(value < 0 || value > 20) {
            request.setAttribute("erreur", "Erreur : Veuillez saisir une note entre 0 et 20.");
            return "redirect:/projetSB/GradeCreationController";
        }

        if(coefficient < 0) {
            request.setAttribute("erreur", "Erreur : Veuillez saisir un coefficient supérieur à 0.");
            return "redirect:/projetSB/GradeCreationController";
        }

        Grade grade = new Grade();
        grade.setGradeName(newGradeName);
        grade.setGradeValue(value);
        grade.setGradeCoefficient(coefficient);
        grade.setStudentId(studentId);
        grade.setCourseId(courseId);
        grade.setTeacherId(teacherId);

        String error = gradeService.addGrade(grade);
        if(error == null) {
            String studentEmail = usersService.getUserById(studentId).getUserEmail();

            // Vérifier si l'email a bien été trouvé
            if (studentEmail != null) {
                // Préparer le sujet et le corps de l'email
                String subject = "Nouvelle note reçue";
                String body = "Bonjour,\n\n"
                    + "Vous avez reçu une nouvelle note pour le cours : " + grade.getGradeName() + ".\n"
                    + "Note : " + grade.getGradeValue() + "/20\n"
                    + "Coefficient : " + grade.getGradeCoefficient() + "\n\n"
                    + "Cordialement,\nL'équipe pédagogique";

                // Envoi de l'email
                try {
                    GMailer gmailer = new GMailer();  // Instancier GMailer
                    gmailer.sendMail(subject, body, studentEmail);  // Envoyer l'email
                    return "redirect:/projetSB/GradeManagerController";
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("erreur", "Erreur lors de l'envoi de l'email.");
                }
            } else {
                request.setAttribute("erreur", "Utilisateur non trouvé pour l'étudiant.");
                return "redirect:/projetSB/GradeCreationController";
            }
        } else {
            return "redirect:/projetSB/GradeCreationController";
        }

        return "redirect:/projetSB/GradeManagerController";
    }

}
