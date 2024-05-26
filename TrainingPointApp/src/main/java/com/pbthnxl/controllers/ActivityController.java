/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.controllers;

import com.pbthnxl.pojo.Activity;
import com.pbthnxl.pojo.User;
import com.pbthnxl.services.ActivityParticipationTypeService;
import com.pbthnxl.services.ActivityService;
import com.pbthnxl.services.ArticleService;
import com.pbthnxl.services.FacultyService;
import com.pbthnxl.services.ParticipantService;
import com.pbthnxl.services.ParticipationTypeService;
import com.pbthnxl.services.UserService;
import java.security.Principal;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author DELL
 */
@Controller
public class ActivityController {
    @Autowired
    private ActivityService activityService;
    
    @Autowired
    private ArticleService articleService;
    
    @Autowired
    private FacultyService facultyService;
    
    @Autowired
    private ParticipantService participantService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ParticipationTypeService participantTypeService;
    
    @Autowired
    private ActivityParticipationTypeService activityParticipationTypeService;
    
    @ModelAttribute
    public void commonAttr(Model model, Principal principal) {
        model.addAttribute("articles", this.articleService.getArticles());
        model.addAttribute("faculties", this.facultyService.getFaculties());
        model.addAttribute("participants", this.participantService.getParticipants());
        model.addAttribute("participantTypes", this.participantTypeService.getParticipationTypes());
        
        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        model.addAttribute("userId", user.getId());
    }
    
    @GetMapping("/activities")
    public String createView(Model model) {
        model.addAttribute("activity", new Activity());
        return "activities";
    }
    
    @PostMapping("/activities")
    public String createActivity(@ModelAttribute(value = "activity") @Valid Activity a,
            BindingResult rs) {
        if (!rs.hasErrors()) {
            try {
                this.activityService.addOrUpdate(a);

                return "redirect:/";
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }
        return "activities";
    }
    
    @GetMapping("/activities/{activityId}")
    public String updateView(Model model, @PathVariable(value = "activityId") int id) {
        model.addAttribute("activity", this.activityService.getActivityById(id));
        
        return "activities";
    }
    
    @GetMapping("/activities/{activityId}/participation-type")
    public String addParticipationType(Model model, @PathVariable(value = "activityId") int id) {
        System.out.println(this.activityParticipationTypeService.getActivityParticipationTypesByActivityId(id));
        model.addAttribute("activitiesParticipationType", this.activityParticipationTypeService.getActivityParticipationTypesByActivityId(id));
        return "activitiesParticipationType";
    }
}
