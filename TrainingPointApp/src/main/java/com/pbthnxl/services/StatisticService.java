/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pbthnxl.services;

import java.util.Map;

/**
 *
 * @author DELL
 */
public interface StatisticService {

    Map<String, Object> getStatisticsForAllFaculties();

    Map<String, Object> getStatisticsByFacultyAndClass();

    Map<String, Integer> getStatisticsForFaculty(int id);

    Map<String, Integer> getClassificationStatistics(int facultyId);

    Map<String, Object> getStatisticsByFacultyAndClass(int semesterId);

    Map<String, Integer> getStatisticsForFaculty(int id, int semesterId);

    Map<String, Integer> getClassificationStatistics(int facultyId, int semesterId);
    
    Map<String, Object> getStatisticsForClass(int classId, int semesterId);
}
