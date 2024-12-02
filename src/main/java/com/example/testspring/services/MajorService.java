package com.example.testspring.services;

import com.example.testspring.entities.Major;
import com.example.testspring.repositories.MajorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MajorService {
    private final MajorRepository majorRepository;
    @Autowired
    public MajorService(MajorRepository majorRepository){
        this.majorRepository = majorRepository;
    }

    public List<Major> getMajors() {
        return this.majorRepository.findAll();
    }

    public Major getMajorById(Integer majorId) {
        return this.majorRepository.findById(majorId).orElse(null);
    }

    public String addMajor(Major major) {
        try {
            majorRepository.save(major);
        } catch (Exception e) {
            return "Erreur : La fillière existe déjà ou une autre erreur est survenue.";
        }
        return null;
    }

    public boolean deleteMajorById(Integer majorId) {
        try {
            majorRepository.deleteById(majorId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void updateMajor(Major major) {
        Major majorEntity = getMajorById(major.getMajorId());

        majorEntity = majorEntity;

        this.majorRepository.save(majorEntity);
    }
}
