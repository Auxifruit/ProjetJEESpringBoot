package com.example.testspring.services;

import com.example.testspring.entities.Subjects;
import com.example.testspring.repositories.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;

    @Autowired
    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public List<Subjects> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public Subjects getSubjectById(int subjectId) {
        return subjectRepository.findById(subjectId).orElse(null);
    }

    public String addSubjectInTable(Subjects subject) {
        try {
            subjectRepository.save(subject);
            return null;
        } catch (Exception e) {
            return "Erreur : La matière existe déjà.";
        }
    }

    public boolean deleteSubjectFromTable(int subjectId) {
        try {
            subjectRepository.deleteById(subjectId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String modifySubjectFromTable(Subjects subject) {
        try {
            subjectRepository.save(subject);
            return null;
        } catch (Exception e) {
            return "Erreur : Erreur lors de la modification de la matière.";
        }
    }

    public List<Subjects> getSubjectsByStudentId(int studentId) {
        if (studentId <= 0) {
            throw new IllegalArgumentException("studentId can't be less or equal to 0");
        }

        try {
            return subjectRepository.findSubjectsByStudentId(studentId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur de récupération des matières pour studentID: " + studentId, e);
        }
    }
}

