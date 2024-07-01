package com.pbthnxl.validator.impl;

import com.pbthnxl.pojo.Semester;
import com.pbthnxl.validator.SemesterDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author DELL
 */
public class SemesterDateValidator implements ConstraintValidator<SemesterDate, Semester> {
    @Override
    public void initialize(SemesterDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(Semester semester, ConstraintValidatorContext context) {
        if (semester.getStartDate()== null || semester.getEndDate() == null) {
            return true;
        }
        return semester.getEndDate().after(semester.getStartDate());
    }
}
