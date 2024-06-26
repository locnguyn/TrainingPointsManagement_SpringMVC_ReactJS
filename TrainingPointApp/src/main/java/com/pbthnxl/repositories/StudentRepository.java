/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pbthnxl.repositories;

import com.pbthnxl.pojo.Student;
import java.util.List;

/**
 *
 * @author DELL
 */
public interface StudentRepository {
    boolean isExistingStudentCode(String studentCode);
    void saveStudent(Student student);
    List<Student> findAllAssistants();
    public Student findByStudentCode(String studentCode);
    List<Student> getStudentList(int facultyId, int page);
    Student getStudentById(int id);
}
