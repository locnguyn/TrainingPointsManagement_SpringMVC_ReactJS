/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.repositories;

import com.pbthnxl.pojo.Semester;
import java.util.List;

/**
 *
 * @author hieu
 */
public interface SemesterRepository {
    List<Semester> getSemesters();
    Semester getSemesterById(int id);
    void addOrUpdate(Semester s);
    void delete(Semester s);
    Semester findByName(int name);
}
