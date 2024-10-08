/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pbthnxl.services;

import com.pbthnxl.pojo.ParticipationType;
import java.util.List;

/**
 *
 * @author DELL
 */
public interface ParticipationTypeService {
    List<ParticipationType> getParticipationTypes();
    ParticipationType findByName(String name);
    void addOrUpdate(ParticipationType p);
    ParticipationType getParticipationTypeById(int id);
    void delete(ParticipationType c);
}
