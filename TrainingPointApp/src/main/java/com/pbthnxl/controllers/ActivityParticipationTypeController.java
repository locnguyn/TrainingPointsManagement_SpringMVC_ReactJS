/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.controllers;

import com.pbthnxl.pojo.ActivityParticipationType;
import com.pbthnxl.services.ActivityParticipationTypeService;
import com.pbthnxl.services.ActivityService;
import com.pbthnxl.services.ParticipationTypeService;
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
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author DELL
 */
@Controller
public class ActivityParticipationTypeController {
    @Autowired
    private ActivityService activityService;
    
    @Autowired
    private ParticipationTypeService participationTypeService;
    
    @Autowired
    private ActivityParticipationTypeService activityParticipationTypeService;
    
    @ModelAttribute
    public void commonAttr(Model model, Principal principal) {
        model.addAttribute("activities", this.activityService.getActivities()); 
        model.addAttribute("participationTypes", this.participationTypeService.getParticipationTypes());
    }
    
    @GetMapping("/add-activity-participation-type")
    public String createView(@RequestParam(value = "activityId", required = false) Integer activityId, Model model) {
        if(activityId != null) {
            model.addAttribute("activityId", activityId);
        }
        model.addAttribute("activityParticipationType", new ActivityParticipationType());
        return "addActivityParticipationType";
    }

    @GetMapping("/activities-participation-type")
    public String listView(Model model) {
        model.addAttribute("activitiesParticipationType", this.activityParticipationTypeService.getActivityParticipationType());
        return "activitiesParticipationType";
    }
    
    @PostMapping("/add-activity-participation-type")
    public String createActivityParticipationType(@ModelAttribute(value = "activityParticipationType") @Valid ActivityParticipationType a,
            BindingResult rs) {
        if (!rs.hasErrors()) {
            try {
                this.activityParticipationTypeService.addOrUpdate(a);
                return "redirect:/activities-participation-type";
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }
        return "activitiesParticipationType";
    }
    
    @GetMapping("/activities-participation-type/{activityParticipationType}")
    public String updateView(Model model, @PathVariable(value = "activityParticipationType") int id) {
        model.addAttribute("activityParticipationType", this.activityParticipationTypeService.getActivityParticipationTypeById(id));
        
        return "addActivityParticipationType";
    }
}
