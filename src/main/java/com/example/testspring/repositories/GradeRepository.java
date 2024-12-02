package com.example.testspring.repositories;

import com.example.testspring.entities.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Integer> {
    List<Grade> findByTeacherIdAndCourseId(int teacherId, int classId);

    @Query("FROM Grade g WHERE g.studentId = :studentId")
    List<Grade> findGradesByStudentId(@Param("studentId") int studentId);

    @Query("FROM Grade g WHERE g.teacherId = :teacherId")
    List<Grade> findGradesByTeacherId(@Param("teacherId") int teacherId);
}

