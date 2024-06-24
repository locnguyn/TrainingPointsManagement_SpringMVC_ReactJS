/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.controllers;

import com.pbthnxl.pojo.Class;
import com.pbthnxl.pojo.User;
import com.pbthnxl.services.ClassService;
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
@RequestMapping("/class")
public class ClassController {

    @Autowired
    private UserService userService;

    @Autowired
    private ClassService classService;

    @RequestMapping("/list")
    String list(Model model, Principal p) {
        if (p == null) {
            return "redirect:/";
        }
        User user = userService.getUserByUsername(p.getName());
        if (user == null || user.getUserRole().equals("ROLE_USER") || user.getUserRole().equals("ROLE_ASSISTANT")) {
            return "redirect:/";
        }
        model.addAttribute("classes", classService.getClasses());
        return "classes";
    }
    
    @GetMapping("/add")
    public String createView(Model model) {
        model.addAttribute("clazz", new Class());
        return "addClass";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute(value = "clazz") @Valid Class s,
            BindingResult rs, Principal p) {
        if(p == null) return "redirect:/";
        User user = userService.getUserByUsername(p.getName());
        if(user == null || user.getUserRole().equals("ROLE_USER") || user.getUserRole().equals("ROLE_ASSISTANT")){
            return "redirect:/";
        }
        if (!rs.hasErrors()) {
            try {
                this.classService.addOrUpdate(s);

                return "redirect:/class/list";
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }
        return "addClass";
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
        model.addAttribute("clazz", this.classService.getClassById(id));

        return "addClass";
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

        this.classService.delete(this.classService.getClassById(id));
        return "redirect:/class/list";
    }
}
