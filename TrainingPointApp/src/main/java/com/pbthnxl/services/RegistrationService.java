/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pbthnxl.services;

import com.pbthnxl.dto.RegistrationDTO;
import com.pbthnxl.pojo.Registration;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author DELL
 */
public interface RegistrationService {
    void save(Registration registration);
    Registration findByStudentIdAndActivityParticipationTypeId(Integer studentId, int activityParticipationTypeId);
    void processCSV(MultipartFile file, int activityParticipationTypeId);
    List<Registration> findRegistrationsByStudentId(int id);
    List<RegistrationDTO> findRegistrationsByStudentIdDTO(int id);
    Registration findRegistrationById(int id);
    void delete(int id);
    RegistrationDTO convertToDTO(Registration r);
    List<Registration> filterRegistrationsBySemester(List<Registration> registrations, int semesterId);
}
