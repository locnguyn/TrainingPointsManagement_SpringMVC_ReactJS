/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.services.impl;

import com.pbthnxl.pojo.Registration;
import com.pbthnxl.repositories.RegistrationRepository;
import com.pbthnxl.services.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author DELL
 */
@Service
public class RegistrationServiceImpl implements RegistrationService {
    @Autowired
    private RegistrationRepository registrationRepository;

    @Override
    public void save(Registration registration) {
        this.registrationRepository.save(registration);
    }

    @Override
    public boolean existsByStudentIdAndActivityParticipationTypeId(Integer studentId, int activityParticipationTypeId) {
        return this.registrationRepository.existsByStudentIdAndActivityParticipationTypeId(studentId, activityParticipationTypeId);
    }

    @Override
    public void processCSV(MultipartFile file, int activityParticipationTypeId) {
        this.registrationRepository.processCSV(file, activityParticipationTypeId);
    }
    
}
