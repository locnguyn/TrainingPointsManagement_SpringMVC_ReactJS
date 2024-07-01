package com.pbthnxl.validator.impl;

import com.pbthnxl.services.FacultyService;
import com.pbthnxl.validator.UniqueFacultyName;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author DELL
 */
public class UniqueFacultyNameValidator implements ConstraintValidator<UniqueFacultyName, String>{
    @Autowired
    private FacultyService facultyService;

    @Override
    public void initialize(UniqueFacultyName constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return facultyService.findByName(value) == null;
    }
    
}
