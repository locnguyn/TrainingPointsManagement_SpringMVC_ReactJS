/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.validator.impl;

import com.pbthnxl.pojo.ActivityParticipationType;
import com.pbthnxl.services.ActivityParticipationTypeService;
import com.pbthnxl.validator.UniqueActivityParticipationType;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author DELL
 */
public class UniqueActivityParticipationTypeValidator implements ConstraintValidator<UniqueActivityParticipationType, ActivityParticipationType> {
    @Autowired
    private ActivityParticipationTypeService activityParticipationTypeService;
    @Override
    public void initialize(UniqueActivityParticipationType constraintAnnotation) {
    }

     @Override
    public boolean isValid(ActivityParticipationType value, ConstraintValidatorContext context) {

        // Ensure Activity and ParticipationType are not null
        if (value.getActivityId() == null || value.getParticipationTypeId() == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Activity and Participation Type must not be null")
                   .addConstraintViolation();
            return false;
        }

        // Check for unique combination
        boolean exists = activityParticipationTypeService.existsByActivityIdAndParticipationTypeId(
                value.getActivityId().getId(), value.getParticipationTypeId().getId());
        if (exists && value.getId() == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("The combination of Activity and Participation Type must be unique")
                   .addConstraintViolation();

            return false;
        }

        return true;
    }
}
