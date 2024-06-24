/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.controllers;

import com.pbthnxl.pojo.Faculty;
import com.pbthnxl.pojo.User;
import com.pbthnxl.services.FacultyService;
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
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author DELL
 */
@Controller
@RequestMapping("/faculty")
public class FacultyController {

    @Autowired
    private UserService userService;

    @Autowired
    private FacultyService facultyService;

    @RequestMapping("/list")
    String list(Model model, Principal p) {
        if (p == null) {
            return "redirect:/";
        }
        User user = userService.getUserByUsername(p.getName());
        if (user == null || user.getUserRole().equals("ROLE_USER") || user.getUserRole().equals("ROLE_ASSISTANT")) {
            return "redirect:/";
        }
        model.addAttribute("faculties", facultyService.getFaculties());
        return "faculties";
    }
    
    @GetMapping("/add")
    public String createView(Model model) {
        model.addAttribute("faculty", new Faculty());
        return "addFaculty";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute(value = "faculty") @Valid Faculty s,
            BindingResult rs, Principal p) {
        if(p == null) return "redirect:/";
        User user = userService.getUserByUsername(p.getName());
        if(user == null || user.getUserRole().equals("ROLE_USER") || user.getUserRole().equals("ROLE_ASSISTANT")){
            return "redirect:/";
        }
        if (!rs.hasErrors()) {
            try {
                this.facultyService.addOrUpdate(s);

                return "redirect:/faculty/list";
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }
        return "addFaculty";
    }

    @GetMapping("/update/{id}")
    public String updateView(Model model, @PathVariable(value = "id") int id, Principal p) {
        if (p == null) {
            return "redirect:/";
        }
        User user = userService.getUserByUsername(p.getName());
        if (user == null || user.getUserRole().equals("ROLE_USER") || user.getUserRole().equals("ROLE_ASSISTANT")) {
            return "redirect:/";
        }
        model.addAttribute("faculty", this.facultyService.getFacultyById(id));

        return "addFaculty";
    }

    @GetMapping("/remove/{id}")
    public String remove(Model model, @PathVariable(value = "id") int id, Principal p) {
        if (p == null) {
            return "redirect:/";
        }
        User user = userService.getUserByUsername(p.getName());
        if (user == null || user.getUserRole().equals("ROLE_USER") || user.getUserRole().equals("ROLE_ASSISTANT")) {
            return "redirect:/";
        }

        this.facultyService.delete(this.facultyService.getFacultyById(id));
        return "redirect:/faculty/list";
    }
}
