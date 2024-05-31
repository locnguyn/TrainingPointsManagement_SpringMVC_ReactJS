/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.controllers;

import com.pbthnxl.services.StatisticService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author DELL
 */
@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticService statisticService;

    @GetMapping("/faculty-class")
    public ResponseEntity<Map<String, Object>> getStatisticsByFacultyAndClass() {
        Map<String, Object> stats = statisticService.getStatisticsByFacultyAndClass();
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
    
    @GetMapping("/faculty/{id}")
    public ResponseEntity<Map<String, Integer>> getStatisticsByFaculty(@PathVariable("id") int id) {
        Map<String, Integer> stats = statisticService.getStatisticsForFaculty(id);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
    
    @GetMapping("/classification/{id}")
    public ResponseEntity<Map<String, Integer>> getStatisticsClassificationByFaculty(@PathVariable("id") int id) {
        Map<String, Integer> stats = statisticService.getClassificationStatistics(id);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
    
    @GetMapping("/classification")
    public ResponseEntity<Map<String, Integer>> getStatisticsClassification() {
        Map<String, Integer> stats = statisticService.getClassificationStatistics(0);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
}

