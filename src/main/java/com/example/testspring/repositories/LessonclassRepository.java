package com.example.testspring.repositories;

import com.example.testspring.entities.Classes;
import com.example.testspring.entities.Lessonclass;
import com.example.testspring.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LessonclassRepository extends JpaRepository<Lessonclass, Integer> {

    @Query("SELECT lc.classId FROM Lessonclass lc JOIN Classes c WHERE lc.classId = c.classId AND lc.lessonId = :lessonId")
    List<Integer> findClassesByLessonId(@Param("lessonId") Integer lessonId);

    @Query("""
        SELECT s
        FROM Student s
        WHERE s.classId = :classId
    """)
    List<Student> findStudentsByClassId(@Param("classId") Integer classId);

    @Query("""
        SELECT s
        FROM Student s
        JOIN Lessonclass lc ON lc.classId = s.classId
        WHERE lc.lessonId = :lessonId
    """)
    List<Student> findStudentsByLessonId(@Param("lessonId") Integer lessonId);

    @Query("""
        SELECT COUNT(lc)
        FROM Lessonclass lc
        JOIN Lesson l ON lc.lessonId = l.lessonId
        WHERE lc.classId = :classId
          AND l.lessonId != :excludedLessonId
          AND l.lessonStartDate < :endDate
          AND l.lessonEndDate > :startDate
    """)
    Long countConflictingClasses(
            @Param("classId") Integer classId,
            @Param("excludedLessonId") Integer excludedLessonId,
            @Param("startDate") java.sql.Timestamp startDate,
            @Param("endDate") java.sql.Timestamp endDate
    );
}