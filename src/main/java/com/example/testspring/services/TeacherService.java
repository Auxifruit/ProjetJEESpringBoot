package com.example.testspring.services;

import com.example.testspring.entities.Teacher;
import com.example.testspring.repositories.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;

    @Autowired
    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public boolean addOrUpdateTeacher(Teacher teacher) {
        try {
            teacherRepository.save(teacher);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteTeacher(int teacherId) {
        Optional<Teacher> teacher = teacherRepository.findById(teacherId);
        if (teacher.isPresent()) {
            teacherRepository.delete(teacher.get());
            return true;
        }
        return false;
    }

    public Optional<Teacher> getTeacherById(int teacherId) {
        return teacherRepository.findById(teacherId);
    }
}

