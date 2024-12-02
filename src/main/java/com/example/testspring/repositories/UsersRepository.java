package com.example.testspring.repositories;

import com.example.testspring.entities.Role;
import com.example.testspring.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository  extends JpaRepository<Users, Integer> {
    List<Users> findByUserRole(Role userRole);

    @Query("SELECT u FROM Users u WHERE u.userRole = :userRole")
    List<Users> findAllByRole(@Param("userRole") Role role);


    Users findUserByUserEmail(String userEmail);

    boolean existsByUserEmail(String userEmail);

    Users findByUserEmailAndUserPassword(String userEmail, String userPassword);

    @Query("""
        SELECT new Users(u.userId, u.userPassword, u.userName, u.userLastName, u.userEmail, u.userBirthdate, u.userRole)
        FROM Users u 
        JOIN Student s ON u.userId = s.studentId 
        JOIN Classes c ON s.classId = c.classId 
        JOIN Lessonclass lc ON c.classId = lc.lessonClassId 
        JOIN Lesson l ON lc.lessonClassId = l.lessonId 
        JOIN Course co ON l.courseId = co.courseId 
        WHERE co.courseId = :courseId 
          AND c.classId = :classId 
          AND u.userRole = :userRole 
          AND l.teacherId = :teacherID
    """)
    List<Users> findStudentsByDisciplineAndClass(
            @Param("courseId") int courseId,
            @Param("classId") int classId,
            @Param("userRole") Role userRole,
            @Param("teacherID") int teacherID);
}
