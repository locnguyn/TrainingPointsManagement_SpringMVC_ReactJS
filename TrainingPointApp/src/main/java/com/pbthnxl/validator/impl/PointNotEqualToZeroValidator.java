/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.validator.impl;

import com.pbthnxl.validator.PointNotEqualToZero;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author DELL
 */
public class PointNotEqualToZeroValidator implements ConstraintValidator<PointNotEqualToZero, Integer> {
    @Override
    public void initialize(PointNotEqualToZero constraintAnnotation) {
    }

    @Override
    public boolean isValid(Integer point, ConstraintValidatorContext context) {
        return point != null && point != 0;
    }
}
