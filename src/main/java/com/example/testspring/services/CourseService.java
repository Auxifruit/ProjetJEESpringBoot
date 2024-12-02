package com.example.testspring.services;

import com.example.testspring.entities.Course;
import com.example.testspring.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(int courseId) {
        return courseRepository.findById(courseId).orElse(null);
    }

    public String addCourse(Course course) {
        try {
            courseRepository.save(course);
        } catch (Exception e) {
            return "Error: Could not add course. " + e.getMessage();
        }
        return null;
    }

    public boolean deleteCourse(int courseId) {
        try {
            courseRepository.deleteById(courseId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String modifyCourse(Course course) {
        try {
            courseRepository.save(course);
            return "Course modified successfully.";
        } catch (Exception e) {
            return "Error: Could not modify course. " + e.getMessage();
        }
    }

    public String getCourseName(int courseId) {
        return getCourseById(courseId).getCourseName();
    }

    public int getCourseSubjectId(int courseId) {
        return getCourseById(courseId).getSubjectId();
    }

    public List<Course> getCoursesBySubjectId(int subjectId) {
        if (subjectId <= 0) {
            throw new IllegalArgumentException("Subject ID must be greater than 0");
        }
        return courseRepository.findBySubjectId(subjectId);
    }

    public List<Course> getAllTeacherCoursesByTeacherId(int teacherId) {
        return courseRepository.findCoursesByTeacherId(teacherId);
    }

}