/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.controllers;

import com.pbthnxl.pojo.Activity;
import com.pbthnxl.pojo.Article;
import com.pbthnxl.pojo.Faculty;
import com.pbthnxl.pojo.User;
import com.pbthnxl.services.ActivityParticipationTypeService;
import com.pbthnxl.services.ActivityService;
import com.pbthnxl.services.ArticleService;
import com.pbthnxl.services.FacultyService;
import com.pbthnxl.services.SemesterService;
import com.pbthnxl.services.UserService;
import java.security.Principal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author admin
 */
@Controller
@ControllerAdvice
public class IndexController {

    @Autowired
    private ActivityParticipationTypeService activityParticipationTypeService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private ArticleService articleService;
    
    @Autowired
    private SemesterService semesterService;
    
    @Autowired
    private UserService userService;

    @ModelAttribute
    public void commonAttr(Model model, Principal principal) {
        model.addAttribute("faculties", this.facultyService.getFaculties());
        model.addAttribute("semesters", this.semesterService.getSemesters());
        if(principal != null)
            model.addAttribute("user", userService.getUserByUsername(principal.getName()));
    }
    
    @GetMapping("/")
    public String index(Model model,
            @RequestParam Map<String, String> params) {
        model.addAttribute("activities", this.activityService.findFilteredActivities(params));
        model.addAttribute("articles", this.articleService.getArticles());
        return "index";
    }
    
    @GetMapping("/stats")
    public String stats(Principal p){
        if(p == null) return "redirect:/";
        User user = userService.getUserByUsername(p.getName());
        if(user == null || user.getUserRole().equals("ROLE_USER")){
            return "redirect:/";
        }
        return "stats";
    }
}
