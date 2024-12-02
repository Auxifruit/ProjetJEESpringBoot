package com.example.testspring.repositories;

import com.example.testspring.entities.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Administrator, Integer> {
}