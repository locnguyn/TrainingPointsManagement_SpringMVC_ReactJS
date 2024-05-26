/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.formatters;

import com.pbthnxl.pojo.ParticipationType;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;

/**
 *
 * @author DELL
 */
public class ParticipationTypeFormatter implements Formatter<ParticipationType> {

    @Override
    public String print(ParticipationType participationType, Locale locale) {
        return String.valueOf(participationType.getId());
    }

    @Override
    public ParticipationType parse(String id, Locale locale) throws ParseException {
        ParticipationType p = new ParticipationType();
        p.setId(Integer.parseInt(id));
        
        return p;
    }
    
}
