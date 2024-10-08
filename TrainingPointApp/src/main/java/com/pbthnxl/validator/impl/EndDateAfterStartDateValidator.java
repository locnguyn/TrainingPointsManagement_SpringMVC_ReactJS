package com.pbthnxl.validator.impl;

import com.pbthnxl.pojo.Activity;
import com.pbthnxl.validator.EndDateAfterStartDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author DELL
 */
public class EndDateAfterStartDateValidator implements ConstraintValidator<EndDateAfterStartDate, Activity> {
    @Override
    public void initialize(EndDateAfterStartDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(Activity activity, ConstraintValidatorContext context) {
        if (activity.getStartDateTime() == null || activity.getEndDate() == null) {
            return true;
        }
        return activity.getEndDate().after(activity.getStartDateTime());
    }
}
