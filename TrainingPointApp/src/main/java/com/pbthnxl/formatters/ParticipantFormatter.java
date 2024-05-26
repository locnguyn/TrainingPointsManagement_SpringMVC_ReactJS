/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.formatters;

import com.pbthnxl.pojo.Participant;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;

/**
 *
 * @author DELL
 */
public class ParticipantFormatter implements Formatter<Participant>{

    @Override
    public String print(Participant part, Locale locale) {
        return String.valueOf(part.getId());
    }

    @Override
    public Participant parse(String id, Locale locale) throws ParseException {
        Participant p = new Participant();
        p.setId(Integer.parseInt(id));
        
        return p;
    }
    
}
