/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.controllers;

import com.pbthnxl.dto.RegistrationDTO;
import com.pbthnxl.pojo.Registration;
import com.pbthnxl.services.RegistrationService;
import com.pbthnxl.services.UserService;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author hieu
 */
@RestController
@RequestMapping("/api/registration")
public class ApiRegistrationController {
    @Autowired
    private RegistrationService regisService;
    @Autowired
    private UserService userService;
    
    
    @GetMapping(path="/user-registration/", produces = {
        MediaType.APPLICATION_JSON_VALUE
    })
    @CrossOrigin
    public ResponseEntity<List<RegistrationDTO>> list(Principal p){
        List<RegistrationDTO> r = this.regisService.findRegistrationsByStudentIdDTO(this.userService.getUserByUsername(p.getName()).getStudent().getId());
        
        return new ResponseEntity<>(r, HttpStatus.OK);
    }
    
    @CrossOrigin
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable(value = "id") int id, Principal p){
        Registration r = this.regisService.findRegistrationOwner(this.userService.getUserByUsername(p.getName()).getStudent().getId(), id);
        
        if(r != null){
            this.regisService.delete(id);
        }
        
    }
}
