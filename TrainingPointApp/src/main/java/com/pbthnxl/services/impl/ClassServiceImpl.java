/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.services.impl;

import com.pbthnxl.pojo.Class;
import com.pbthnxl.repositories.ClassRepository;
import com.pbthnxl.services.ClassService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author DELL
 */
@Service
public class ClassServiceImpl implements ClassService {
    @Autowired
    private ClassRepository classRepository;

    @Override
    public List<Class> getClasses() {
        return this.classRepository.getClasses();
    }

    @Override
    public Class getClassById(int id) {
        return this.classRepository.getClassById(id);
    }
    
}
