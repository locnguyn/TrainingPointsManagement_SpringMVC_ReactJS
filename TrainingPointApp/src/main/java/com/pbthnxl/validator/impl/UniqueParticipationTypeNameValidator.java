package com.pbthnxl.validator.impl;

import com.pbthnxl.services.ParticipationTypeService;
import com.pbthnxl.validator.UniqueParticipationTypeName;
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
public class UniqueParticipationTypeNameValidator implements ConstraintValidator<UniqueParticipationTypeName, String>{
    @Autowired
    private ParticipationTypeService partTypeService;

    @Override
    public void initialize(UniqueParticipationTypeName constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return partTypeService.findByName(value) == null;
    }
    
}
