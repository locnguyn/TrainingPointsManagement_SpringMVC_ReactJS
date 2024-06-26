/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.services.impl;

import com.pbthnxl.dto.RegistrationDTO;
import com.pbthnxl.pojo.Registration;
import com.pbthnxl.repositories.ActivityParticipationTypeRepository;
import com.pbthnxl.repositories.ActivityRepository;
import com.pbthnxl.repositories.RegistrationRepository;
import com.pbthnxl.repositories.StudentRepository;
import com.pbthnxl.services.ActivityService;
import com.pbthnxl.services.RegistrationService;
import com.pbthnxl.services.SemesterService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private ActivityRepository activityRepo;

    @Autowired
    private ActivityParticipationTypeRepository acPartTypeRepo;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private SemesterService semesterService;

    @Override
    public void save(Registration registration) {
        this.registrationRepository.save(registration);
    }

    @Override
    public void processCSV(MultipartFile file, int activityParticipationTypeId) {
        this.registrationRepository.processCSV(file, activityParticipationTypeId);
    }

    @Override
    public List<Registration> findRegistrationsByStudentId(int id, Map<String, String> params) {
        return this.registrationRepository.findRegistrationsByStudentId(id, params);
    }

    @Override
    public List<RegistrationDTO> findRegistrationsByStudentIdDTO(int id, Map<String, String> params) {
        List<Registration> list = this.registrationRepository.findRegistrationsByStudentId(id, params);

        return list.stream().map(this::convertToDTO).collect(Collectors.toList());

    }

    @Override
    public RegistrationDTO convertToDTO(Registration r) {
        RegistrationDTO dto = new RegistrationDTO();

        dto.setId(r.getId());
        dto.setAcPartTypeId(r.getActivityParticipationTypeId().getId());
        dto.setCreatedAt(r.getRegistrationDate());
        dto.setParticipationTypeName(r.getActivityParticipationTypeId().getParticipationTypeId().getName());
        dto.setParticipated(r.getParticipated());
        dto.setPoint(r.getActivityParticipationTypeId().getPoint());
//        dto.setActivity(this.acPartTypeRepo.getActivityParticipationTypeById(r.getActivityParticipationTypeId()));
        dto.setActivity(this.activityService.convertToRegistrationActivityDTO(r.getActivityParticipationTypeId().getActivityId()));

        return dto;
    }

    @Override
    public Registration findRegistrationById(int id) {
        return this.registrationRepository.findRegistrationById(id);
    }

    @Override
    public void delete(int id) {
        this.registrationRepository.delete(id);
    }

    @Override
    public Registration findByStudentIdAndActivityParticipationTypeId(Integer studentId, int activityParticipationTypeId) {
        return this.registrationRepository.findByStudentIdAndActivityParticipationTypeId(studentId, activityParticipationTypeId);
    }

    @Override

    public List<Registration> filterRegistrationsBySemester(List<Registration> registrations, int semesterId) {
        return this.registrationRepository.filterRegistrationsBySemester(registrations, semesterId);

    }

    @Override
    public Registration findRegistrationOwner(int studentId, int registrationId) {
        return this.registrationRepository.findRegistrationOwner(studentId, registrationId);

    }

}
