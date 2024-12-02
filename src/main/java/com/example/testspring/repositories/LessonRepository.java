package com.example.testspring.repositories;

import com.example.testspring.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {

    // Méthodes automatiques générées par Spring Data JPA
    List<Lesson> findByTeacherId(Integer teacherId);

    @Query("""
        SELECT l FROM Lesson l 
        JOIN Lessonclass lc ON l.lessonId = lc.lessonId
        JOIN Student s ON lc.classId = s.classId
        WHERE s.studentId = :studentId
    """)
    List<Lesson> findStudentLessons(@Param("studentId") Integer studentId);

    @Query("""
        SELECT COUNT(l) FROM Lesson l
        WHERE l.teacherId = :teacherId
        AND (:lessonId IS NULL OR l.lessonId != :lessonId)
        AND (
            (l.lessonStartDate < :lesson_End_Date AND l.lessonEndDate > :lesson_Start_Date) OR
            (l.lessonStartDate BETWEEN :lesson_Start_Date AND :lesson_End_Date) OR
            (l.lessonEndDate BETWEEN :lesson_Start_Date AND :lesson_End_Date)
        )
    """)
    Long countConflictingLessons(@Param("lessonId") Integer lessonId,
                                 @Param("teacherId") Integer teacherId,
                                 @Param("lesson_Start_Date") Timestamp lesson_Start_Date,
                                 @Param("lesson_End_Date") Timestamp lesson_End_Date);
}