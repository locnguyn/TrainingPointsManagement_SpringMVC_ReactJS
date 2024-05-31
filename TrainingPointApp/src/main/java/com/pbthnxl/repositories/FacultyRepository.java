/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pbthnxl.repositories;

import com.pbthnxl.pojo.Faculty;
import java.util.List;

/**
 *
 * @author DELL
 */
public interface FacultyRepository {
    List<Faculty> getFaculties();
    Faculty getFacultyById(int id);
}
