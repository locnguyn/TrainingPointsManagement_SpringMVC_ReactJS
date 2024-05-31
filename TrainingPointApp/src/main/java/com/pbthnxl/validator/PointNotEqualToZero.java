/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pbthnxl.validator;

import com.pbthnxl.validator.impl.PointNotEqualToZeroValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 *
 * @author DELL
 */
@Constraint(validatedBy = PointNotEqualToZeroValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PointNotEqualToZero {
    String message() default "{activityParticipationType.PointNotEqualToZero.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
