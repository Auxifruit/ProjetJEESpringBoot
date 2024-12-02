package com.example.testspring.services;

import com.example.testspring.entities.Userstovalidate;
import com.example.testspring.repositories.UsersToValidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersToValidateService {
    private final UsersToValidateRepository usersToValidateRepository;

    @Autowired
    public UsersToValidateService(UsersToValidateRepository usersToValidateRepository) {
        this.usersToValidateRepository = usersToValidateRepository;
    }

    public List<Userstovalidate> getAllUsersToValidate() {
        return usersToValidateRepository.findAll();
    }

    public Userstovalidate getUserToValidateById(int userId) {
        return usersToValidateRepository.getReferenceById(userId);
    }

    public boolean addUserToValidateInTable(Userstovalidate userstovalidate) {
        try {
            usersToValidateRepository.save(userstovalidate);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUserToValidate(int userstovalidateId) {
        try {
            usersToValidateRepository.deleteById(userstovalidateId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
