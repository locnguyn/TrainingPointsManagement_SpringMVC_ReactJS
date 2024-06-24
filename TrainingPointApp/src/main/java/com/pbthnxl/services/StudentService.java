/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pbthnxl.services;

import com.pbthnxl.pojo.Student;
import java.util.List;

/**
 *
 * @author DELL
 */
public interface StudentService {
    boolean isExistingStudentCode(String studentCode);
    void saveStudent(Student student);
    List<Student> findAllAssistants();
    List<Student> getStudentList(int facultyId);
    Student getStudentById(int id);
}
