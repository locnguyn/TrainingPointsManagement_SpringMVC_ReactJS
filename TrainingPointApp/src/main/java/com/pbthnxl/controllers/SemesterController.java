/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.controllers;

import com.pbthnxl.pojo.Semester;
import com.pbthnxl.pojo.User;
import com.pbthnxl.services.SemesterService;
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
@RequestMapping("/semester")
public class SemesterController {

    @Autowired
    private UserService userService;

    @Autowired
    private SemesterService semesterService;

    @RequestMapping("/list")
    String list(Model model, Principal p) {
        if (p == null) {
            return "redirect:/";
        }
        User user = userService.getUserByUsername(p.getName());
        if (user == null || user.getUserRole().equals("ROLE_USER") || user.getUserRole().equals("ROLE_ASSISTANT")) {
            return "redirect:/";
        }
        model.addAttribute("semesters", semesterService.getSemesters());
        return "semesters";
    }
    
    @GetMapping("/add")
    public String createView(Model model) {
        model.addAttribute("semester", new Semester());
        return "addSemester";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute(value = "semester") @Valid Semester s,
            BindingResult rs, Principal p) {
        if(p == null) return "redirect:/";
        User user = userService.getUserByUsername(p.getName());
        if(user == null || user.getUserRole().equals("ROLE_USER") || user.getUserRole().equals("ROLE_ASSISTANT")){
            return "redirect:/";
        }
        if (!rs.hasErrors()) {
            try {
                this.semesterService.addOrUpdate(s);

                return "redirect:/semester/list";
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }
        return "addSemester";
    }

    @GetMapping("/update/{semesterId}")
    public String updateView(Model model, @PathVariable(value = "semesterId") int id, Principal p) {
        if (p == null) {
            return "redirect:/";
        }
        User user = userService.getUserByUsername(p.getName());
        if (user == null || user.getUserRole().equals("ROLE_USER") || user.getUserRole().equals("ROLE_ASSISTANT")) {
            return "redirect:/";
        }
        model.addAttribute("semester", this.semesterService.getSemesterById(id));

        return "addSemester";
    }

    @GetMapping("/remove/{semesterId}")
    public String remove(Model model, @PathVariable(value = "semesterId") int id, Principal p) {
        if (p == null) {
            return "redirect:/";
        }
        User user = userService.getUserByUsername(p.getName());
        if (user == null || user.getUserRole().equals("ROLE_USER") || user.getUserRole().equals("ROLE_ASSISTANT")) {
            return "redirect:/";
        }

        this.semesterService.delete(this.semesterService.getSemesterById(id));
        return "redirect:/semester/list";
    }
}
