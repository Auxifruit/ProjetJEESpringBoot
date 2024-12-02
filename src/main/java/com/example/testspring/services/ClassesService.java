package com.example.testspring.services;

import com.example.testspring.entities.Classes;
import com.example.testspring.repositories.ClassesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassesService {

    private final ClassesRepository classesRepository;

    @Autowired
    public ClassesService(ClassesRepository classesRepository) {
        this.classesRepository = classesRepository;
    }

    public List<Classes> getAllClasses() {
        return classesRepository.findAll();
    }

    public Classes getClasseById(int classId) {
        return classesRepository.findById(classId).orElse(null);
    }

    public String addClasse(Classes classe) {
        try {
            classesRepository.save(classe);
        } catch (Exception e) {
            return "Erreur : La classe existe déjà ou une autre erreur est survenue.";
        }
        return null;
    }

    public boolean deleteClasse(int classId) {
        try {
            classesRepository.deleteById(classId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String modifyClasse(Classes classe) {
        try {
            classesRepository.save(classe);
            return "Classe modifiée avec succès.";
        } catch (Exception e) {
            return "Erreur : Impossible de modifier la classe.";
        }
    }

    public List<Classes> getAvailableClassesForLesson(int lessonId) {
        return classesRepository.findAvailableClassesForLesson(lessonId);
    }

    public List<Classes> getAllTeacherClassByTeacherId(int teacherId) {
        return classesRepository.findAllByTeacherId(teacherId);
    }
}
