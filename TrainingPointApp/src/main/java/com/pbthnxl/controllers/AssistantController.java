/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.controllers;

import com.pbthnxl.dto.StudentUserForm;
import com.pbthnxl.pojo.Student;
import com.pbthnxl.pojo.User;
import com.pbthnxl.services.ArticleService;
import com.pbthnxl.services.ClassService;
import com.pbthnxl.services.CloudinaryService;
import com.pbthnxl.services.FacultyService;
import com.pbthnxl.services.RegistrationService;
import com.pbthnxl.services.ReportMissingService;
import com.pbthnxl.services.StudentService;
import com.pbthnxl.services.UserService;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author DELL
 */
@Controller
@RequestMapping("/user")
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
    
    @Autowired
    private RegistrationService registrationService;
    
    @Autowired
    private ArticleService articleService;
    
    @Autowired
    private ReportMissingService reportMissingService;

    @ModelAttribute
    public void commonAttr(Model model, Principal principal) {
        model.addAttribute("classes", this.classService.getClasses());
        model.addAttribute("faculties", this.facultyService.getFaculties());
    }

    @GetMapping("/list")
    public String assistantList(Model model, Principal p) {
        if (p == null) {
            return "redirect:/";
        }
        User user = userService.getUserByUsername(p.getName());
        if (user == null || user.getUserRole().equals("ROLE_USER") || user.getUserRole().equals("ROLE_ASSISTANT")) {
            return "redirect:/";
        }
        model.addAttribute("assistants", studentService.findAllAssistants());
        return "assistants";
    }

    @GetMapping("/students")
    public String studentList(Model model, Principal p, @RequestParam Map<String, String> params) {
        if (p == null) {
            return "redirect:/";
        }
        User user = userService.getUserByUsername(p.getName());
        if (user == null || user.getUserRole().equals("ROLE_USER")) {
            return "redirect:/";
        }
        int page = 1;
        if(params.containsKey("page")){
            page = Integer.parseInt(params.get("page"));
        }
        model.addAttribute("students", studentService.getStudentList(user.getUserRole().equals("ROLE_ASSISTANT")?user.getStudent().getFacultyId().getId():0, page));
        return "students";
    }

    @GetMapping("/add")
    public String showAddForm(Model model, Principal p) {
        if (p == null) {
            return "redirect:/";
        }
        User user = userService.getUserByUsername(p.getName());
        if (user == null || user.getUserRole().equals("ROLE_USER") || user.getUserRole().equals("ROLE_ASSISTANT")) {
            return "redirect:/";
        }
        model.addAttribute("studentUserForm", new StudentUserForm());
        model.addAttribute("classes", classService.getClasses());
        model.addAttribute("faculties", facultyService.getFaculties());
        return "addUser";
    }

    @PostMapping("/add")
    public String saveAssistant(@ModelAttribute("studentUserForm") @Valid StudentUserForm form,
            BindingResult result,
            Model model, Principal p) {
        if (p == null) {
            return "redirect:/";
        }
        User u = userService.getUserByUsername(p.getName());
        if (u == null || u.getUserRole().equals("ROLE_USER") || u.getUserRole().equals("ROLE_ASSISTANT")) {
            return "redirect:/";
        }
        if (result.hasErrors()) {
            model.addAttribute("classes", classService.getClasses());
            model.addAttribute("faculties", facultyService.getFaculties());
            return "addUser";
        }

        // Validate password and confirm password
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.studentUserForm", "Mật khẩu nhập lại không khớp");
            model.addAttribute("classes", classService.getClasses());
            model.addAttribute("faculties", facultyService.getFaculties());
            return "addUser";
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
        user.setPassword(this.passswordEncoder.encode(form.getPassword())); // You should encrypt the password
        user.setUserRole(form.getRole());
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

        return "redirect:/user/list";
    }

    @GetMapping("/remove-assistant/{userId}")
    public String removeAssistant(@PathVariable(value = "userId") int userId, Principal p) {
        if (p == null) {
            return "redirect:/";
        }
        User user = userService.getUserByUsername(p.getName());
        if (user == null || user.getUserRole().equals("ROLE_USER") || user.getUserRole().equals("ROLE_ASSISTANT")) {
            return "redirect:/";
        }
        userService.updateUserRole(userId, "ROLE_USER");
        return "redirect:/user/list";
    }
    
    @GetMapping("/add-assistant/{userId}")
    public String addStudentAssistant(@PathVariable(value = "userId") int userId, Principal p) {
        if (p == null) {
            return "redirect:/";
        }
        User user = userService.getUserByUsername(p.getName());
        if (user == null || user.getUserRole().equals("ROLE_USER") || user.getUserRole().equals("ROLE_ASSISTANT")) {
            return "redirect:/";
        }
        userService.updateUserRole(userId, "ROLE_ASSISTANT");
        return "redirect:/user/list";
    }
    
    @GetMapping("/students/{studentId}")
    public String details(@PathVariable(value = "studentId") int studentId,  Map<String, String> params, Principal p, Model model) {
        if (p == null) {
            return "redirect:/";
        }
        User user = userService.getUserByUsername(p.getName());
        if (user == null || user.getUserRole().equals("ROLE_USER")) {
            return "redirect:/";
        }
        Student s = this.studentService.getStudentById(studentId);
        model.addAttribute("student", s);
        model.addAttribute("registrations", this.registrationService.findRegistrationsByStudentIdDTO(studentId, new HashMap<>()));
        model.addAttribute("reports", this.reportMissingService.getStudentReportMissings(studentId, params));
        model.addAttribute("articles", this.articleService.getArticles());
        return "studentDetails";
    }
}
