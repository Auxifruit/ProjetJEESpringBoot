package com.example.testspring.services;

import com.example.testspring.entities.Administrator;
import com.example.testspring.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository){
        this.adminRepository = adminRepository;
    }

    public List<Administrator> getAllAdministrators() {
        return adminRepository.findAll();
    }

    public Administrator getAdministratorById(int administratorId) {
        return adminRepository.getReferenceById(administratorId);
    }

    public boolean addAdminInTable(Administrator administrator) {
        try {
            adminRepository.save(administrator);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAdmin(int administratorId) {
        try {
            adminRepository.deleteById(administratorId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}