/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.services.impl;

import com.pbthnxl.dto.ReportMissingDTO;
import com.pbthnxl.pojo.Registration;
import com.pbthnxl.pojo.ReportMissing;
import com.pbthnxl.repositories.ReportMissingRepository;
import com.pbthnxl.services.ActivityParticipationTypeService;
import com.pbthnxl.services.ActivityService;
import com.pbthnxl.services.RegistrationService;
import com.pbthnxl.services.ReportMissingService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author DELL
 */
@Service
public class ReportMissingServiceImpl implements ReportMissingService{
    @Autowired
    private ReportMissingRepository reportMissingRepository;
    @Autowired
    private ActivityService acService;
    @Autowired
    private ActivityParticipationTypeService acPartTypeService;
    @Autowired
    private RegistrationService registrationService;

    @Override
    public List<ReportMissing> getReportMissings() {
        return this.reportMissingRepository.getReportMissings();
    }

    @Override
    public void confirmReportMissingById(int id) {
        this.reportMissingRepository.confirmReportMissingById(id);
    }

    @Override
    public void rejectReportMissingById(int id) {
        this.reportMissingRepository.rejectReportMissingById(id);
    }

    @Override
    public void save(ReportMissing r) {
        this.reportMissingRepository.save(r);
    }

    @Override
    public ReportMissingDTO convertToDTO(ReportMissing r) {
        ReportMissingDTO dto = new ReportMissingDTO();
        
        Registration registration = this.registrationService.findByStudentIdAndActivityParticipationTypeId(r.getStudentId().getId(), r.getActivityParticipationTypeId().getId());
        
        dto.setId(r.getId());
        if(registration != null){
            if(!r.getChecked()) dto.setStatus("Pending");
            else if(r.getChecked() && !registration.getParticipated()) dto.setStatus("Rejected");
            else if (r.getChecked() && registration.getParticipated()) dto.setStatus("Confirmed");
        } else dto.setStatus("Pending");
        
        dto.setProof(r.getProof());
        dto.setReportDate(r.getReportDate());
        dto.setActivityPartType(this.acPartTypeService.convertToDTO(r.getActivityParticipationTypeId(), true));
        
        return dto;
    }

    @Override
    public List<ReportMissingDTO> getStudentReportMissings(int studentId) {
        
        return this.reportMissingRepository.getStudentReportMissings(studentId).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public ReportMissing findByStudentIdAndActivityParticipationTypeId(int studentId, int activityParticipationTypeId) {
        return this.reportMissingRepository.findByStudentIdAndActivityParticipationTypeId(studentId, activityParticipationTypeId);
    }
    
    
}
