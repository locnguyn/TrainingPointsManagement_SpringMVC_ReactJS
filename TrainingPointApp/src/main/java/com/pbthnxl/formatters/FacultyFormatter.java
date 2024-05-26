/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.formatters;

import com.pbthnxl.pojo.Faculty;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;

/**
 *
 * @author DELL
 */
public class FacultyFormatter implements Formatter<Faculty>{

    @Override
    public String print(Faculty fac, Locale locale) {
        return String.valueOf(fac.getId());
    }

    @Override
    public Faculty parse(String id, Locale locale) throws ParseException {
        Faculty f = new Faculty();
        f.setId(Integer.parseInt(id));
        
        return f;
    }
    
}
