package com.example.testspring.repositories;

import com.example.testspring.entities.Userstovalidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersToValidateRepository extends JpaRepository<Userstovalidate, Integer> {
}
