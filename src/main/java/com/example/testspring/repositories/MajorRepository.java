package com.example.testspring.repositories;

import com.example.testspring.entities.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MajorRepository extends JpaRepository<Major, Integer> {
}
