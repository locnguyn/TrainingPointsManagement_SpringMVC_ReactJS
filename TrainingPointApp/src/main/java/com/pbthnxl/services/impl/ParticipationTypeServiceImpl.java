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

    @Override
    public ParticipationType findByName(String name) {
        return this.participationTypeRepository.findByName(name);
    }

    @Override
    public void addOrUpdate(ParticipationType p) {
        this.participationTypeRepository.addOrUpdate(p);
    }

    @Override
    public ParticipationType getParticipationTypeById(int id) {
        return this.participationTypeRepository.getParticipationTypeById(id);
    }

    @Override
    public void delete(ParticipationType c) {
        this.participationTypeRepository.delete(c);
    }
    
}
