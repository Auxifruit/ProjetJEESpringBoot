package com.example.testspring.repositories;

import com.example.testspring.entities.Subjects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subjects, Integer> {
    @Query("""
        SELECT DISTINCT s
        FROM Subjects s
        JOIN Course c ON c.subjectId = s.subjectId
        JOIN Grade g ON g.courseId = c.courseId
        WHERE g.studentId = :studentId
    """)
    List<Subjects> findSubjectsByStudentId(@Param("studentId") int studentId);
}
