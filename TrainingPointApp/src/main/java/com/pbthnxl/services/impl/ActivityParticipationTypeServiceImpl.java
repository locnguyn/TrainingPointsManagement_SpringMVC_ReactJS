/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.services.impl;

import com.pbthnxl.dto.ActivityParticipationTypeDTO;
import com.pbthnxl.pojo.Activity;
import com.pbthnxl.pojo.ActivityParticipationType;
import com.pbthnxl.pojo.Registration;
import com.pbthnxl.repositories.ActivityParticipationTypeRepository;
import com.pbthnxl.services.ActivityParticipationTypeService;
import com.pbthnxl.services.ActivityService;
import com.pbthnxl.services.RegistrationService;
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
    private ActivityParticipationTypeRepository activityParticipationTypeRepo;
    @Autowired
    private ActivityService acService;
    
    @Autowired
    private RegistrationService registrationService;
    
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
        return list.stream().map((f) -> convertToDTO(f, false, 0)).collect(Collectors.toList());
    }

    @Override
    public ActivityParticipationTypeDTO convertToDTO(ActivityParticipationType type, Boolean isGetReport, int studentId) {
        ActivityParticipationTypeDTO dto = new ActivityParticipationTypeDTO();
        Activity a = type.getActivityId();
        
        if(studentId != 0){
            Registration r = this.registrationService.findByStudentIdAndActivityParticipationTypeId(studentId, type.getId());
            
            if(r != null){
                dto.setIsRegistered(true);
                
                if(r.getParticipated()){
                    dto.setIsParticipated(true);
                }
                else dto.setIsParticipated(false);
            } else dto.setIsRegistered(false);
        }
        dto.setId(type.getId());
        dto.setPoint(type.getPoint());
        dto.setParticipationType(type.getParticipationTypeId().getName());
        
        if(isGetReport){
            dto.setActivity(this.acService.convertToRegistrationActivityDTO(type.getActivityId()));
        }
        return dto;
    }
    
}
