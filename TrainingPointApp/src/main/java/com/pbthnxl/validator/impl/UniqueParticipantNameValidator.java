package com.pbthnxl.validator.impl;

import com.pbthnxl.services.ParticipantService;
import com.pbthnxl.validator.UniqueParticipantName;
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
public class UniqueParticipantNameValidator implements ConstraintValidator<UniqueParticipantName, String>{
    @Autowired
    private ParticipantService participantService;

    @Override
    public void initialize(UniqueParticipantName constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return participantService.findByName(value) == null;
    }
    
}
