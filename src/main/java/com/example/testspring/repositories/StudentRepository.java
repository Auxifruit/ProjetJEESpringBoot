package com.example.testspring.repositories;

import com.example.testspring.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    List<Student> findByClassIdIsNull();
    List<Student> findByClassId(int classId);
}