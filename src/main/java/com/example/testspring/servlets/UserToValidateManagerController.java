package com.example.testspring.servlets;

import com.example.testspring.entities.Classes;
import com.example.testspring.entities.Major;
import com.example.testspring.entities.Userstovalidate;
import com.example.testspring.services.ClassesService;
import com.example.testspring.services.MajorService;
import com.example.testspring.services.UsersToValidateService;
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
@RequestMapping("/projetSB/UserToValidateManagerController")
public class UserToValidateManagerController extends HttpServlet {

    private final UsersToValidateService usersToValidateService;
    private final MajorService majorService;

    @Autowired
    public UserToValidateManagerController(UsersToValidateService usersToValidateService, MajorService majorService) {
        this.usersToValidateService = usersToValidateService;
        this.majorService = majorService;
    }

    @GetMapping
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Userstovalidate> userstovalidateList = usersToValidateService.getAllUsersToValidate();

        Map<Integer, Major> mapUserToValidateIdMajor = new HashMap<>();

        for(Userstovalidate userstovalidate : userstovalidateList) {
            Integer majorId = userstovalidate.getUserToValidateMajorId();
            if(majorId != null) {
                Major major = majorService.getMajorById(majorId);
                mapUserToValidateIdMajor.put(userstovalidate.getUserToValidateId(), major);
            }
        }

        request.setAttribute("usersToValidate", userstovalidateList);
        request.setAttribute("mapUserToValidateIdMajor", mapUserToValidateIdMajor);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/projetSB/userToValidateManager");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
