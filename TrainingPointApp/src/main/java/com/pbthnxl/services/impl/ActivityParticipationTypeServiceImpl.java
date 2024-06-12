/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.services.impl;

import com.pbthnxl.dto.ActivityParticipationTypeDTO;
import com.pbthnxl.pojo.Activity;
import com.pbthnxl.pojo.ActivityParticipationType;
import com.pbthnxl.repositories.ActivityParticipationTypeRepository;
import com.pbthnxl.services.ActivityParticipationTypeService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author DELL
 */
@Service
public class ActivityParticipationTypeServiceImpl implements ActivityParticipationTypeService {
    @Autowired
    ActivityParticipationTypeRepository activityParticipationTypeRepo;
    
    @Override
    public List<ActivityParticipationType> getActivityParticipationType() {
        return this.activityParticipationTypeRepo.getActivityParticipationType();
    }

    @Override
    public void addOrUpdate(ActivityParticipationType activityParticipationType) {
        this.activityParticipationTypeRepo.addOrUpdate(activityParticipationType);
    }

    @Override
    public ActivityParticipationType getActivityParticipationTypeById(int id) {
        return this.activityParticipationTypeRepo.getActivityParticipationTypeById(id);
    }

    @Override
    public List<ActivityParticipationType> getActivityParticipationTypesByActivityId(int activityId) {
        return this.activityParticipationTypeRepo.getActivityParticipationTypesByActivityId(activityId);
    }

    @Override
    public boolean existsByActivityIdAndParticipationTypeId(int activityId, int participationTypeId) {
        return this.activityParticipationTypeRepo.existsByActivityIdAndParticipationTypeId(activityId, participationTypeId);
    }

    @Override
    public ActivityParticipationType getActivityParticipationTypeByActivityIdAndParticipationTypeId(int activityId, int participationTypeId) {
        return this.activityParticipationTypeRepo.getActivityParticipationTypeByActivityIdAndParticipationTypeId(activityId, participationTypeId);
    }

    @Override
    public void deleteActivityParticipationType(int id) {
        this.activityParticipationTypeRepo.deleteActivityParticipationType(id);
    }
    
    @Override
    public List<ActivityParticipationTypeDTO> getActivityParticipationTypeDTOs() {
        List<ActivityParticipationType> list = this.getActivityParticipationType();
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private ActivityParticipationTypeDTO convertToDTO(ActivityParticipationType type) {
        ActivityParticipationTypeDTO dto = new ActivityParticipationTypeDTO();
        Activity a = type.getActivityId();
        dto.setId(type.getId());
        dto.setPoint(type.getPoint());
        dto.setParticipationType(type.getParticipationTypeId().getName());
        return dto;
    }
    
}
