/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.formatters;

import com.pbthnxl.pojo.Activity;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;

/**
 *
 * @author DELL
 */
public class ActivityFormatter implements Formatter<Activity>{

    @Override
    public String print(Activity activity, Locale locale) {
        return String.valueOf(activity.getId());
    }

    @Override
    public Activity parse(String id, Locale locale) throws ParseException {
        Activity a = new Activity();
        a.setId(Integer.parseInt(id));
        
        return a;
    }
    
}
