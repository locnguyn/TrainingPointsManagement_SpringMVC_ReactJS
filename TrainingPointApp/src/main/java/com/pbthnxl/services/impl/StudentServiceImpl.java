/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.services.impl;

import com.pbthnxl.pojo.Student;
import com.pbthnxl.repositories.StudentRepository;
import com.pbthnxl.services.StudentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author DELL
 */
@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepository studentRepository;
    @Override
    public boolean isExistingStudentCode(String studentCode) {
        return this.studentRepository.isExistingStudentCode(studentCode);
    }

    @Override
    public void saveStudent(Student student) {
        this.studentRepository.saveStudent(student);
    }

    @Override
    public List<Student> findAllAssistants() {
        return this.studentRepository.findAllAssistants();
    }

    @Override
    public List<Student> getStudentList(int facultyId, int page) {
        return this.studentRepository.getStudentList(facultyId, page);
    }

    @Override
    public Student getStudentById(int id) {
        return this.studentRepository.getStudentById(id);
    }
}
