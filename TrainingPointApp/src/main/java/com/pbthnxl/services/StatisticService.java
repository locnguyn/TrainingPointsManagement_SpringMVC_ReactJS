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
    Map<String, Integer> getStatisticsForFaculty(int id);
    Map<String, Object> getStatisticsByFacultyAndClass();
    Map<String, Integer> getClassificationStatistics(int facultyId);
}
