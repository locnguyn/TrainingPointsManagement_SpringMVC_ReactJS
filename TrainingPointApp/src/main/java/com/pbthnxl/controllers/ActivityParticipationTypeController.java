/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.controllers;

import com.pbthnxl.pojo.ActivityParticipationType;
import com.pbthnxl.services.ActivityParticipationTypeService;
import com.pbthnxl.services.ActivityService;
import com.pbthnxl.services.ParticipationTypeService;
import com.pbthnxl.services.RegistrationService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author DELL
 */
@Controller
@RequestMapping("/activity-participation-type")
public class ActivityParticipationTypeController {
    @Autowired
    private ActivityService activityService;
    
    @Autowired
    private ParticipationTypeService participationTypeService;
    
    @Autowired
    private ActivityParticipationTypeService activityParticipationTypeService;
    
    @Autowired
    private RegistrationService registrationService;
    
    @ModelAttribute
    public void commonAttr(Model model, Principal principal) {
        model.addAttribute("activities", this.activityService.getActivities()); 
        model.addAttribute("participationTypes", this.participationTypeService.getParticipationTypes());
    }
    
    @GetMapping("/add")
    public String createView(@RequestParam(value = "activityId", required = false) Integer activityId, Model model) {
        if(activityId != null) {
            model.addAttribute("activityId", activityId);
        }
        model.addAttribute("activityParticipationType", new ActivityParticipationType());
        return "addActivityParticipationType";
    }

    @GetMapping("/list")
    public String listView(Model model) {
        model.addAttribute("activitiesParticipationType", this.activityParticipationTypeService.getActivityParticipationType());
        return "activitiesParticipationType";
    }
    
    @PostMapping("/add")
    public String createActivityParticipationType(@ModelAttribute(value = "activityParticipationType") @Valid ActivityParticipationType a,
            BindingResult rs) {
        if (!rs.hasErrors()) {
            try {
                ActivityParticipationType a1 = 
                        this.activityParticipationTypeService
                            .getActivityParticipationTypeByActivityIdAndParticipationTypeId(
                    a.getActivityId().getId(), a.getParticipationTypeId().getId());
                if(a1 != null){
                    a1.setPoint(a.getPoint());
                    this.activityParticipationTypeService.addOrUpdate(a1);
                }
                else
                    this.activityParticipationTypeService.addOrUpdate(a);
                return "redirect:/activity-participation-type/list";
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }
        return "addActivityParticipationType";
    }
    
    @GetMapping("/update/{activityParticipationType}")
    public String updateView(Model model, @PathVariable(value = "activityParticipationType") int id) {
        model.addAttribute("activityParticipationType", this.activityParticipationTypeService.getActivityParticipationTypeById(id));
        
        return "addActivityParticipationType";
    }
    
    @PostMapping("/update/{activityParticipationTypeId}/upload-csv")
    public String uploadFile(@PathVariable("activityParticipationTypeId") int activityParticipationTypeId,
                            @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            registrationService.processCSV(file, activityParticipationTypeId);
            redirectAttributes.addFlashAttribute("message", "File uploaded successfully.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "An error occurred while processing the file." + e.getMessage());
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/activity-participation-type/list";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteActivityParticipationType(@PathVariable("id") int id) {
        this.activityParticipationTypeService.deleteActivityParticipationType(id);
        return "redirect:/activity-participation-type/list";
    }
}
