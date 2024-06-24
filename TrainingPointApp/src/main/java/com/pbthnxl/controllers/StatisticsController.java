package com.pbthnxl.controllers;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import com.pbthnxl.services.StatisticService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private StatisticService statisticService;

    @GetMapping("/faculty-class")
    public ResponseEntity<Map<String, Object>> getStatisticsByFacultyAndClass(@RequestParam(name = "semesterId", defaultValue = "0") int semesterId) {
        Map<String, Object> stats = statisticService.getStatisticsByFacultyAndClass(semesterId);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
    
    @GetMapping("/faculty/{id}")
    public ResponseEntity<Map<String, Integer>> getStatisticsByFaculty(@PathVariable("id") int id, @RequestParam(name = "semesterId", defaultValue = "0") int semesterId) {
        Map<String, Integer> stats = statisticService.getStatisticsForFaculty(id, semesterId);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
    
    @GetMapping("/classification/{id}")
    public ResponseEntity<Map<String, Integer>> getStatisticsClassificationByFaculty(@PathVariable("id") int id, @RequestParam(name = "semesterId", defaultValue = "0") int semesterId) {
        Map<String, Integer> stats = statisticService.getClassificationStatistics(id, semesterId);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
    
    @GetMapping("/classification")
    public ResponseEntity<Map<String, Integer>> getStatisticsClassification(@RequestParam(name = "semesterId", defaultValue = "0") int semesterId) {
        Map<String, Integer> stats = statisticService.getClassificationStatistics(0, semesterId);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
}
