/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.controllers;

import com.pbthnxl.components.JwtService;
import com.pbthnxl.pojo.User;
import com.pbthnxl.services.CloudinaryService;
import com.pbthnxl.services.UserService;
import java.security.Principal;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author hieu
 */
@RestController
@RequestMapping("/api")
public class ApiUserController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private CloudinaryService cloudinary;

    @PostMapping(path = "/register/", consumes = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.MULTIPART_FORM_DATA_VALUE
    })
    @CrossOrigin
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestParam Map<String, String> params, @RequestPart MultipartFile[] files) {
        String avatarUrl = "";

        User user = new User();
        user.setFirstName(params.get("firstName"));
        user.setLastName(params.get("lastName"));
        user.setUsername(params.get("username"));
        user.setPassword(this.passwordEncoder.encode(params.get("password")));
        user.setEmail(params.get("email"));
        user.setPhoneNumber(params.get("phone"));
        user.setUserRole("ROLE_USER");
        user.setActive(true);
        if (files.length > 0) {
            avatarUrl = this.cloudinary.uploadFile(files[0]);
            user.setAvatar(avatarUrl);
        }

        this.userService.saveUser(user);
    }

    @PostMapping("/login/")
    @CrossOrigin
    public ResponseEntity<String> login(@RequestBody User user) {
        if (this.userService.authUser(user.getUsername(), user.getPassword()) == true) {

            String token = this.jwtService.generateTokenLogin(user.getUsername());

            return new ResponseEntity<>(token, HttpStatus.OK);
        }
        return new ResponseEntity<>("error", HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "/current-user/", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<User> getCurrentUser(Principal p) {
        User u = this.userService.getUserByUsername(p.getName());
        return new ResponseEntity<>(u, HttpStatus.OK);
    }

    @PatchMapping(path = "/current-user/", consumes = {
        MediaType.MULTIPART_FORM_DATA_VALUE,
        MediaType.APPLICATION_JSON_VALUE,
    })
    @CrossOrigin
    public ResponseEntity<User> update(@RequestParam Map<String, String> params, @RequestPart(required = false) MultipartFile[] files, Principal p) {
        User user = this.userService.getUserByUsername(p.getName());
        if (user == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        if (params.containsKey("password")) {
            user.setPassword(this.passwordEncoder.encode(params.get("password")));
        }
        if (params.containsKey("firstName")) {
            user.setFirstName(params.get("firstName"));
        }
        if (params.containsKey("lastName")) {
            user.setLastName(params.get("lastName"));
        }
        if (params.containsKey("email")) {
            user.setEmail(params.get("email"));
        }
        if (params.containsKey("phone")) {
            user.setPhoneNumber(params.get("phone"));
        }
        if (files != null && files.length > 0) {
            String avatarUrl = this.cloudinary.uploadFile(files[0]);
            user.setAvatar(avatarUrl);
        }

        this.userService.saveUser(user);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    
    @PatchMapping(path = "/current-user/change-password/", consumes = {
        MediaType.MULTIPART_FORM_DATA_VALUE
    })
    @CrossOrigin
    public ResponseEntity<String> changePassword(@RequestParam Map<String, String> params, Principal p){
        User user = this.userService.getUserByUsername(p.getName());
        if (user == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        
        String oldPassword = "";
        if (params.containsKey("old_password") && params.containsKey("new_password")){
            oldPassword = params.get("old_password");
            System.out.println(oldPassword);
            System.out.println(user.getPassword());
            if(this.passwordEncoder.matches(oldPassword, user.getPassword())){
                 user.setPassword(this.passwordEncoder.encode(params.get("new_password")));
                 this.userService.saveUser(user);
                 return new ResponseEntity<>("Success", HttpStatus.OK);
            }
        } 
        
        return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
    }

}
