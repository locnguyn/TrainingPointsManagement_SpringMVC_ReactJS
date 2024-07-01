package com.pbthnxl.validator.impl;

import com.pbthnxl.services.StudentService;
import com.pbthnxl.validator.UniqueStudentCode;
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
public class UniqueStudentCodeValidator implements ConstraintValidator<UniqueStudentCode, String>{
    @Autowired
    private StudentService studentService;

    @Override
    public void initialize(UniqueStudentCode constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !this.studentService.isExistingStudentCode(value);
    }
    
}
