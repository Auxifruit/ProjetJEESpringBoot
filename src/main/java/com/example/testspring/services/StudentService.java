package com.example.testspring.services;

import com.example.testspring.entities.Student;
import com.example.testspring.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(int studentId) {
        return studentRepository.findById(studentId).orElse(null);
    }

    public boolean addOrUpdateStudent(Student student) {
        try {
            studentRepository.save(student);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteStudent(int studentId) {
        try {
            studentRepository.deleteById(studentId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<Student> getStudentsWithoutClasses() {
        return studentRepository.findByClassIdIsNull();
    }

    public List<Student> getStudentsFromClassId(int classId) {
        return studentRepository.findByClassId(classId);
    }

}