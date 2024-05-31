/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pbthnxl.services;

import com.pbthnxl.pojo.Faculty;
import java.util.List;

/**
 *
 * @author DELL
 */
public interface FacultyService {
    List<Faculty> getFaculties();
    Faculty getFacultyById(int id);
}
