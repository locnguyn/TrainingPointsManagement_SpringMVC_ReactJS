/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.controllers;

import com.pbthnxl.pojo.Activity;
import com.pbthnxl.services.ActivityService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author hieu
 */
@RestController
@RequestMapping("/api")
public class ApiActivityController {
    @Autowired
    private ActivityService acService;
    
    @CrossOrigin
    @RequestMapping("/activities/")
    public ResponseEntity<List<Activity>> List(){
        return new ResponseEntity<>(this.acService.getActivities(), HttpStatus.OK);
    }
}
