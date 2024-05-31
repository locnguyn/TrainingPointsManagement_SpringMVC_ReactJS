/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.controllers;

import com.pbthnxl.dto.StudentUserForm;
import com.pbthnxl.pojo.Student;
import com.pbthnxl.pojo.User;
import com.pbthnxl.services.ClassService;
import com.pbthnxl.services.CloudinaryService;
import com.pbthnxl.services.FacultyService;
import com.pbthnxl.services.StudentService;
import com.pbthnxl.services.UserService;
import java.io.IOException;
import java.security.Principal;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
@RequestMapping("/assistant")
public class AssistantController {

    @Autowired
    private ClassService classService;

    @Autowired
    private FacultyService facultyService;
    
    @Autowired
    private CloudinaryService cloudinaryService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private BCryptPasswordEncoder passswordEncoder;

    @ModelAttribute
    public void commonAttr(Model model, Principal principal) {
        model.addAttribute("classes", this.classService.getClasses());
        model.addAttribute("faculties", this.facultyService.getFaculties());
    }


    @GetMapping("/list")
    public String assistantList(Model model) {
        model.addAttribute("assistants", studentService.findAllAssistants());
        return "assistants";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("studentUserForm", new StudentUserForm());
        model.addAttribute("classes", classService.getClasses());
        model.addAttribute("faculties", facultyService.getFaculties());
        return "addAssistant";
    }

    @PostMapping("/add")
    public String saveAssistant(@ModelAttribute("studentUserForm") @Valid StudentUserForm form,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("classes", classService.getClasses());
            model.addAttribute("faculties", facultyService.getFaculties());
            return "addAssistant";
        }

        // Validate password and confirm password
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.studentUserForm", "Mật khẩu nhập lại không khớp");
            model.addAttribute("classes", classService.getClasses());
            model.addAttribute("faculties", facultyService.getFaculties());
            return "addAssistant";
        }

        // Upload avatar to Cloudinary
        String avatarUrl = "";
        if (!form.getAvatar().isEmpty()) {
            avatarUrl = cloudinaryService.uploadFile(form.getAvatar());
        }

        // Create User
        User user = new User();
        user.setFirstName(form.getFirstName());
        user.setLastName(form.getLastName());
        user.setEmail(form.getEmail());
        user.setPhoneNumber(form.getPhoneNumber());
        user.setUsername(form.getUsername());
        user.setPassword(this.passswordEncoder.encode( form.getPassword())); // You should encrypt the password
        user.setUserRole("ROLE_ASSISTANT");
        user.setActive(true);
        user.setAvatar(avatarUrl);
        userService.saveUser(user);

        // Create Student
        Student student = new Student();
        student.setStudentCode(form.getStudentCode());
        student.setClassId(classService.getClassById(form.getClassId()));
        student.setFacultyId(facultyService.getFacultyById(form.getFacultyId()));
        student.setUserId(user);
        studentService.saveStudent(student);

        return "redirect:/assistant/list";
    }
    
    @GetMapping("/remove/{userId}")
    public String removeAssistant(@PathVariable(value = "userId") int userId) {
        userService.updateUserRole(userId, "ROLE_USER");
        return "redirect:/assistant/list";
    }
}
