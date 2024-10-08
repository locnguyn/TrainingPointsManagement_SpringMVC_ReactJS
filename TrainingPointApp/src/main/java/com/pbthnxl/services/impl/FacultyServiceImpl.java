/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.services.impl;

import com.pbthnxl.pojo.Faculty;
import com.pbthnxl.repositories.FacultyRepository;
import com.pbthnxl.services.FacultyService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author DELL
 */
@Service
public class FacultyServiceImpl implements FacultyService {
    @Autowired
    private FacultyRepository facultyRepository;
    
    @Override
    public List<Faculty> getFaculties() {
        return this.facultyRepository.getFaculties();
    }

    @Override
    public Faculty getFacultyById(int id) {
        return this.facultyRepository.getFacultyById(id);
    }

    @Override
    public void addOrUpdate(Faculty c) {
        this.facultyRepository.addOrUpdate(c);
    }

    @Override
    public void delete(Faculty c) {
        this.facultyRepository.delete(c);
    }

    @Override
    public Faculty findByName(String name) {
        return this.facultyRepository.findByName(name);
    }
    
}
