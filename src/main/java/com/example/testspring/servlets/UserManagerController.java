package com.example.testspring.servlets;

import com.example.testspring.entities.*;
import com.example.testspring.services.ClassesService;
import com.example.testspring.services.MajorService;
import com.example.testspring.services.StudentService;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/projetSB/UserManagerController")
public class UserManagerController extends HttpServlet {

    private final UsersService usersService;
    private final StudentService studentService;
    private final ClassesService classesService;
    private final MajorService majorService;

    @Autowired
    public UserManagerController(UsersService usersService, StudentService studentService, ClassesService classesService, MajorService majorService) {
        this.usersService = usersService;
        this.studentService = studentService;
        this.classesService = classesService;
        this.majorService = majorService;
    }

    @GetMapping
    protected void doGet(@RequestParam("roleFilter") String roleFilter, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Role role = null;
        if(roleFilter != null && !roleFilter.isEmpty()) {
            role = Role.valueOf(roleFilter);
        }

        List<Users> usersList = usersService.getAllUsersByFilter(role);
        Map<Integer, Student> mapUserIdStudent = new HashMap<>();
        Map<Integer, Classes> mapStudentIdClass = new HashMap<>();
        Map<Integer, Major> mapStudentIdMajor = new HashMap<>();

        for(Users users : usersList) {
            if(users.getUserRole().equals(Role.student)) {
                Student student = studentService.getStudentById(users.getUserId());
                if(student != null) {
                    mapUserIdStudent.put(users.getUserId(), student);

                    Integer classId = student.getClassId();
                    Integer majorId = student.getMajorId();

                    if(classId != null) {
                        Classes classe = classesService.getClasseById(classId);

                        mapStudentIdClass.put(student.getStudentId(), classe);
                    }

                    if(majorId != null) {
                        Major major = majorService.getMajorById(majorId);

                        mapStudentIdMajor.put(student.getStudentId(), major);
                    }
                }
            }
        }

        request.setAttribute("users", usersList);
        request.setAttribute("mapUserIdStudent", mapUserIdStudent);
        request.setAttribute("mapStudentIdClass", mapStudentIdClass);
        request.setAttribute("mapStudentIdMajor", mapStudentIdMajor);
        request.setAttribute("roleFilter", roleFilter);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/projetSB/userManager");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
