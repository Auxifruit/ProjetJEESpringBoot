package com.example.testspring.repositories;

import com.example.testspring.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    @Query("SELECT c FROM Course c WHERE c.subjectId = :subjectId")
    List<Course> findBySubjectId(int subjectId);

    @Query("""
            SELECT DISTINCT c 
            FROM Course c 
            JOIN Lesson l ON c.courseId = l.courseId 
            WHERE l.teacherId = :teacherId
           """)
    List<Course> findCoursesByTeacherId(int teacherId);

    @Query("SELECT c FROM Course c WHERE c.subjectId = :subjectId")
    List<Course> findCoursesBySubjectId(@Param("subjectId") int subjectId);
}