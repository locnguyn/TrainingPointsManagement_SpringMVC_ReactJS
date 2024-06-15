/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.controllers;

import com.pbthnxl.pojo.Article;
import com.pbthnxl.pojo.Faculty;
import com.pbthnxl.pojo.Class;
import com.pbthnxl.services.ArticleService;
import com.pbthnxl.services.ClassService;
import com.pbthnxl.services.FacultyService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author hieu
 */
@RestController
@RequestMapping("/api/common")
public class ApiCommonController {
    @Autowired
    private FacultyService faService;
    @Autowired
    private ArticleService arService;
    @Autowired
    private ClassService classService;
    
    @CrossOrigin
    @GetMapping("/faculty-list/")
    public ResponseEntity<List<Faculty>> FacultyList(){
        return new ResponseEntity<>(this.faService.getFaculties(), HttpStatus.OK);
    }
    
    @CrossOrigin
    @GetMapping("/article-list/")
    public ResponseEntity<List<Article>> ArticleList(){
        return new ResponseEntity<>(this.arService.getArticles(), HttpStatus.OK);
    }
    
    @CrossOrigin
    @GetMapping("/class-list")
    public ResponseEntity<List<Class>> ClassList(){
        return new ResponseEntity<>(this.classService.getClasses(), HttpStatus.OK);
    }
}
