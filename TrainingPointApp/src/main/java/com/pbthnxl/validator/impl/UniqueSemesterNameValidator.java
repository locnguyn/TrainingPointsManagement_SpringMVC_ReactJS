package com.pbthnxl.validator.impl;

import com.pbthnxl.services.SemesterService;
import com.pbthnxl.validator.UniqueSemesterName;
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
public class UniqueSemesterNameValidator implements ConstraintValidator<UniqueSemesterName, Integer>{
    @Autowired
    private SemesterService semesterService;

    @Override
    public void initialize(UniqueSemesterName constraintAnnotation) {
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return semesterService.findByName(value) == null;
    }
    
}
