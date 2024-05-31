/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pbthnxl.repositories;

import java.util.List;
import com.pbthnxl.pojo.Class;

/**
 *
 * @author DELL
 */
public interface ClassRepository {
    List<Class> getClasses();
    Class getClassById(int id);
}
