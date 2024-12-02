package com.example.testspring.services;

import com.example.testspring.entities.Classes;
import com.example.testspring.entities.Lesson;
import com.example.testspring.entities.Lessonclass;
import com.example.testspring.entities.Student;
import com.example.testspring.repositories.LessonclassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonclassService {

    private LessonclassRepository lessonclassRepository;

    private LessonService lessonService;

    @Autowired
    public LessonclassService(LessonclassRepository lessonclassRepository, LessonService lessonService) {
        this.lessonclassRepository = lessonclassRepository;
        this.lessonService = lessonService;
    }

    public List<Lessonclass> getAllLessonClasses() {
        return lessonclassRepository.findAll();
    }

    public List<Integer> getClassesByLessonId(Integer lessonId) {
        return lessonclassRepository.findClassesByLessonId(lessonId);
    }

    public Lessonclass getLessonClassByLessonIdAndClassId(Integer lessonId, Integer classId) {
        return lessonclassRepository.findAll()
                .stream()
                .filter(lc -> lc.getLessonId().equals(lessonId) && lc.getClassId().equals(classId))
                .findFirst()
                .orElse(null);
    }

    public boolean addLessonClass(Lessonclass lessonclass) {
        try {
            lessonclassRepository.save(lessonclass);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteLessonClass(Integer lessonClassId) {
        if (lessonclassRepository.existsById(lessonClassId)) {
            lessonclassRepository.deleteById(lessonClassId);
            return true;
        }
        return false;
    }

    public boolean canClassParticipate(Integer classId, Integer lessonId) {
        Lesson lesson = lessonService.getLessonById(lessonId);
        if (lesson == null) return false;

        Long conflictingClasses = lessonclassRepository.countConflictingClasses(
                classId,
                lessonId,
                lesson.getLessonStartDate(),
                lesson.getLessonEndDate()
        );

        return conflictingClasses == 0;
    }

    public List<Student> getStudentsByClassId(Integer classId) {
        return lessonclassRepository.findStudentsByClassId(classId);
    }

    public List<Student> getStudentsByLessonId(Integer lessonId) {
        return lessonclassRepository.findStudentsByLessonId(lessonId);
    }
}