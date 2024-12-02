package com.example.testspring.services;

import com.example.testspring.entities.Lesson;
import com.example.testspring.repositories.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class LessonService {

    private LessonRepository lessonRepository;

    @Autowired
    public LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }

    public Lesson getLessonById(Integer lessonId) {
        return lessonRepository.findById(lessonId).orElse(null);
    }

    public String addLesson(Lesson lesson) {
        try {
            lessonRepository.save(lesson);
            return "Leçon ajoutée avec succès.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de l'ajout de la leçon.";
        }
    }

    public boolean deleteLesson(Integer lessonId) {
        if (lessonRepository.existsById(lessonId)) {
            lessonRepository.deleteById(lessonId);
            return true;
        }
        return false;
    }

    public String modifyLesson(Lesson lesson) {
        try {
            lessonRepository.save(lesson);
            return "Leçon modifiée avec succès.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de la modification de la leçon.";
        }
    }

    public List<Lesson> getLessonsByTeacherId(Integer teacherId) {
        return lessonRepository.findByTeacherId(teacherId);
    }

    public List<Lesson> getLessonsByStudentId(Integer studentId) {
        return lessonRepository.findStudentLessons(studentId);
    }

    public boolean isLessonPossible(Integer lessonId, int teacherId, Timestamp lesson_Start_Date, Timestamp lesson_End_Date) {
        Long conflictingLessons = lessonRepository.countConflictingLessons(lessonId, teacherId, lesson_Start_Date, lesson_End_Date);
        return conflictingLessons == 0;
    }
}