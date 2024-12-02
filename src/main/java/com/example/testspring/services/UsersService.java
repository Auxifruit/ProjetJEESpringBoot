package com.example.testspring.services;

import com.example.testspring.entities.*;
import com.example.testspring.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final AdminService adminService;
    private final StudentService studentService;
    private final TeacherService teacherService;

    @Autowired
    public UsersService(UsersRepository usersRepository, AdminService adminService, StudentService studentService, TeacherService teacherService){
        this.usersRepository = usersRepository;
        this.adminService = adminService;
        this.studentService = studentService;
        this.teacherService = teacherService;
    }

    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    public List<Users> getAllUsersByFilter(Role roleFilter) {
        if (roleFilter != null) {
            return usersRepository.findByUserRole(roleFilter);
        }
        return usersRepository.findAll();
    }

    public Users getUserById(int userId) {
        return usersRepository.getReferenceById(userId);
    }

    public String addUser(Users user) {
        try {
            usersRepository.save(user);
        } catch (Exception e) {
            return "Erreur : L'utilisateur existe déjà ou une autre erreur est survenue.";
        }
        return null;
    }

    public boolean deleteUser(int userId) {
        try {
            usersRepository.deleteById(userId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Users getUserByEmail(String userEmail) {
        return usersRepository.findUserByUserEmail(userEmail);
    }

    @Transactional
    public boolean modifyUserRole(Users user, Role oldUserRole, Role newUserRole) {
        try {
            user.setUserRole(newUserRole);
            usersRepository.save(user);

            switch (newUserRole) {
                case student:
                    Student student = new Student();
                    student.setStudentId(user.getUserId());
                    student.setClassId(null);
                    student.setMajorId(null);

                    studentService.addOrUpdateStudent(student);
                    break;
                case teacher:
                    Teacher teacher = new Teacher();
                    teacher.setTeacherId(user.getUserId());

                    teacherService.addOrUpdateTeacher(teacher);
                    break;
                case administrator:
                    Administrator administrator = new Administrator();
                    administrator.setAdministratorId(user.getUserId());

                    adminService.addAdminInTable(administrator);
                    break;
            }

            switch (oldUserRole) {
                case student:
                    studentService.deleteStudent(user.getUserId());

                    break;
                case teacher:
                    teacherService.deleteTeacher(user.getUserId());

                    break;
                case administrator:
                    adminService.deleteAdmin(user.getUserId());

                    break;
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isEmailInTable(String userEmail) {
        if (userEmail == null || userEmail.isEmpty()) {
            return false;
        }
        return usersRepository.existsByUserEmail(userEmail);
    }

    public Integer userConnection(String userEmail, String userPassword) {
        Users user = usersRepository.findByUserEmailAndUserPassword(userEmail, userPassword);

        if(user != null) {
            return user.getUserId();
        }

        return null;
    }

    public List<Users> getStudentsByDisciplineAndClass(int courseId, int classId, int teacherID) {
        return usersRepository.findStudentsByDisciplineAndClass(courseId, classId, Role.student, teacherID);
    }
}
