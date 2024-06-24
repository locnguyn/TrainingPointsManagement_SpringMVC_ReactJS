/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.services.impl;

import com.pbthnxl.pojo.Semester;
import com.pbthnxl.repositories.SemesterRepository;
import com.pbthnxl.services.SemesterService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author hieu
 */
@Service
public class SemesterServiceImpl implements SemesterService {

    @Autowired
    private SemesterRepository semesterRepo;

    @Override
    public List<Semester> getSemesters() {
        return this.semesterRepo.getSemesters();
    }

    @Override
    public Semester getSemesterById(int id) {
        return this.semesterRepo.getSemesterById(id);
    }

    @Override
    public void addOrUpdate(Semester s) {
        this.semesterRepo.addOrUpdate(s);
    }

    @Override
    public void delete(Semester s) {
        this.semesterRepo.delete(s);
    }
}
