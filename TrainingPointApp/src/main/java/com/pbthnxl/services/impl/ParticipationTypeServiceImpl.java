/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.services.impl;

import com.pbthnxl.pojo.ParticipationType;
import com.pbthnxl.repositories.ParticipationTypeRepository;
import com.pbthnxl.services.ParticipationTypeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author DELL
 */
@Service
public class ParticipationTypeServiceImpl implements ParticipationTypeService {
    @Autowired
    private ParticipationTypeRepository participationTypeRepository;

    @Override
    public List<ParticipationType> getParticipationTypes() {
        return this.participationTypeRepository.getParticipationTypes();
    }
    
}
