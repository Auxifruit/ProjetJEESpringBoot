package com.example.testspring.servlets;


import com.example.testspring.entities.*;
import com.example.testspring.services.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/projetSB/GradeModificationController")
public class GradeModificationController {
    private final GradeService gradeService;
    private final UsersService usersService;
    private final CourseService courseService;
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final ClassesService classesService;

    @Autowired
    public GradeModificationController(GradeService gradeService, UsersService usersService, CourseService courseService, TeacherService teacherService, StudentService studentService, ClassesService classesService) {
        this.gradeService = gradeService;
        this.usersService = usersService;
        this.courseService = courseService;
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.classesService = classesService;
    }

    @GetMapping
    protected String showGradeModificationPage(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String gradeIdString = request.getParameter("gradeId");
        List<Course> courseList = courseService.getAllCourses();
        List<Student> students = studentService.getAllStudents();
        List<Teacher> teachers = teacherService.getAllTeachers();
        Map<Integer, Classes> mapStudentIdClass = new HashMap<>();
        Map<Integer, Users> mapStudentIdUsers = new HashMap<>();
        List<Users> studentList = new ArrayList<>();
        List<Users> teacherList = new ArrayList<>();
        Users gradeStudent = null;
        Users gradeTeacher = null;
        Course gradeCourse = null;
        Classes studentClass = null;

        if(gradeIdString == null || gradeIdString.isEmpty()) {
            request.setAttribute("error", "Erreur : Veuillez choisir une note.");
            return "redirect:/projetSB/GradeManagerController";
        }

        int gradeId = Integer.parseInt(gradeIdString);
        Grade grade = gradeService.getGradeById(gradeId);
        Student student = studentService.getStudentById(grade.getStudentId());
        Integer teacherGradeId = grade.getTeacherId();
        Integer courseGradeId = grade.getCourseId();
        Integer studentClassId = student.getClassId();

        gradeStudent = usersService.getUserById(student.getStudentId());

        if(teacherGradeId != null) {
            gradeTeacher = usersService.getUserById(teacherGradeId);
        }
        if(courseGradeId != null) {
            gradeCourse = courseService.getCourseById(courseGradeId);
        }
        if(studentClassId != null) {
            studentClass = classesService.getClasseById(studentClassId);
        }


        for(Student s : students) {
            Users studentUser = usersService.getUserById(s.getStudentId());
            if(studentUser != null) {
                studentList.add(studentUser);
                mapStudentIdUsers.put(s.getStudentId(), studentUser);
            }
            Integer sClasseInt = s.getClassId();
            if(sClasseInt != null) {
                Classes sClasse = classesService.getClasseById(sClasseInt);
                if(sClasse != null) {
                    mapStudentIdClass.put(s.getStudentId(), sClasse);
                }
            }

        }

        for(Teacher t : teachers) {
            Users teacherUser = usersService.getUserById(t.getTeacherId());
            if(teacherUser != null) {
                teacherList.add(teacherUser);
            }
        }

        request.setAttribute("grade", grade);
        request.setAttribute("courseList", courseList);
        request.setAttribute("studentList", studentList);
        request.setAttribute("teacherList", teacherList);
        request.setAttribute("gradeCourse", gradeCourse);
        request.setAttribute("gradeTeacher", gradeTeacher);
        request.setAttribute("gradeStudent", gradeStudent);
        request.setAttribute("studentClass", studentClass);
        request.setAttribute("mapStudentIdClass", mapStudentIdClass);
        request.setAttribute("mapStudentIdUsers", mapStudentIdUsers);

        return "gradeModification";
    }

    @PostMapping
    protected String handleGradeModification(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String gradeIdString = request.getParameter("gradeId");
        String gradeNewName = request.getParameter("gradeNewName");
        String gradeNewCourseIdString = request.getParameter("gradeNewCourseId");
        String gradeNewValueString = request.getParameter("gradeNewValue");
        String gradeNewCoefficientString = request.getParameter("gradeNewCoefficient");
        String gradeNewStudentIdString = request.getParameter("gradeNewStudentId");
        String gradeNewTeacherId = request.getParameter("gradeNewTeacherId");

        if(gradeIdString == null || gradeIdString.isEmpty()) {
            request.setAttribute("erreur", "Erreur : Veuillez choisir une note.");
            return "redirect:/projetSB/GradeManagerController";
        }

        if((gradeNewName == null || gradeNewName.isEmpty()) && (gradeNewCourseIdString == null || gradeNewCourseIdString.isEmpty()) && (gradeNewValueString == null || gradeNewValueString.isEmpty())
                && (gradeNewCoefficientString == null || gradeNewCoefficientString.isEmpty()) && (gradeNewStudentIdString == null || gradeNewStudentIdString.isEmpty()) && (gradeNewTeacherId == null || gradeNewTeacherId.isEmpty())) {
            request.setAttribute("erreur", "Erreur : Veuillez remplir au moins un champs.");
            return "redirect:/projetSB/GradeModificationController";
        }

        int gradeId = Integer.parseInt(gradeIdString);

        Grade grade = gradeService.getGradeById(gradeId);

        if(gradeNewName != null && !gradeNewName.isEmpty()) {
            grade.setGradeName(gradeNewName);
        }

        if(gradeNewValueString != null && !gradeNewValueString.isEmpty()) {
            double value = Double.parseDouble(gradeNewValueString);

            if(value < 0 || value > 200) {
                request.setAttribute("erreur", "Erreur : Veuillez saisir une note entre 0 et 20.");
                return "redirect:/projetSB/GradeModificationController";
            }

            grade.setGradeValue(value);
        }

        if(gradeNewCourseIdString != null && !gradeNewCourseIdString.isEmpty()) {
            int courseId = Integer.parseInt(gradeNewCourseIdString);
            grade.setCourseId(courseId);
        }

        if(gradeNewCoefficientString != null && !gradeNewCoefficientString.isEmpty()) {
            int coefficient = Integer.parseInt(gradeNewCoefficientString);

            if(coefficient < 0) {
                request.setAttribute("erreur", "Erreur : Veuillez saisir un coefficient supérieur à 0.");
                return "redirect:/projetSB/GradeModificationController";
            }

            grade.setGradeCoefficient(coefficient);
        }

        if(gradeNewStudentIdString != null && !gradeNewStudentIdString.isEmpty()) {
            int studentId = Integer.parseInt(gradeNewStudentIdString);

            grade.setStudentId(studentId);
        }

        if(gradeNewTeacherId != null && !gradeNewTeacherId.isEmpty()) {
            int teacherId = Integer.parseInt(gradeNewTeacherId);

            grade.setTeacherId(teacherId);
        }

        String error = gradeService.addGrade(grade);
        if(error == null) {
            return "redirect:/projetSB/GradeManagerController";
        }
        else {
            request.setAttribute("erreur", error);
            return "redirect:/projetSB/GradeModificationController";
        }
    }


}
