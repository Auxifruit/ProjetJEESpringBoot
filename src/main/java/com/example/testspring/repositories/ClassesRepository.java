package com.example.testspring.repositories;

import com.example.testspring.entities.Classes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassesRepository extends JpaRepository<Classes, Integer> {

    @Query("SELECT c FROM Classes c WHERE c.classId NOT IN (SELECT lc.classId FROM Lessonclass lc WHERE lc.lessonId = :lessonId)")
    List<Classes> findAvailableClassesForLesson(int lessonId);

    @Query("""
            SELECT DISTINCT c 
            FROM Classes c 
            JOIN Lessonclass lc ON c.classId = lc.lessonClassId 
            JOIN Lesson l ON lc.lessonClassId = l.lessonId 
            WHERE l.teacherId = :teacherId
            """)
    List<Classes> findAllByTeacherId(int teacherId);
}
