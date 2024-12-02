package com.example.testspring.services;

import com.example.testspring.entities.Course;
import com.example.testspring.entities.Grade;
import com.example.testspring.repositories.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    // Récupérer toutes les notes
    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }

    // Récupérer une note par son ID
    public Grade getGradeById(int gradeId) {
        return gradeRepository.findById(gradeId).orElse(null);
    }

    public String addGrade(Grade grade) {
        try {
            gradeRepository.save(grade);
        } catch (Exception e) {
            return "Erreur : La note existe déjà ou une autre erreur est survenue.";
        }
        return null;
    }

    public Grade modifyGrade(int gradeId, Grade grade) {
        // Vérifier si la note existe avant de la modifier
        if (!gradeRepository.existsById(gradeId)) {
            throw new IllegalArgumentException("Grade with ID " + gradeId + " does not exist");
        }
        grade.setGradeId(gradeId);  // Assurer que l'ID correspond à celui passé
        return gradeRepository.save(grade);
    }

    // Supprimer une note par ID
    @Transactional
    public boolean deleteGrade(int gradeId) {
        if (gradeRepository.existsById(gradeId)) {
            gradeRepository.deleteById(gradeId);
            return true;
        }
        return false;
    }

    // Récupérer les notes par enseignant et classe
    public List<Grade> getGradesByTeacherAndClass(int teacherId, int classId) {
        return gradeRepository.findByTeacherIdAndCourseId(teacherId, classId);
    }

    // Méthode d'exemple pour récupérer une note spécifique et gérer les erreurs
    public Grade getGradeOrThrow(int gradeId) {
        return gradeRepository.findById(gradeId)
                .orElseThrow(() -> new IllegalArgumentException("Grade with ID " + gradeId + " not found"));
    }

    public List<Grade> getGradeByStudentId(int studentId) {
        if (studentId <= 0) {
            throw new IllegalArgumentException("studentId ne peut pas être inférieur ou égal à 0");
        }

        try {
            return gradeRepository.findGradesByStudentId(studentId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération des notes pour studentID: " + studentId, e);
        }
    }

    public List<Grade> getGradesByTeacherId(int teacherId) {
        if (teacherId <= 0) {
            throw new IllegalArgumentException("teacherId can't be less or equal to 0");
        }

        try {
            return gradeRepository.findGradesByTeacherId(teacherId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur de récupération des notes pour teacherId: " + teacherId, e);
        }
    }
}
