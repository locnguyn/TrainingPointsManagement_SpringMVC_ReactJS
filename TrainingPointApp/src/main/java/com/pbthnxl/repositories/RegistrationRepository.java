/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pbthnxl.repositories;

import com.pbthnxl.pojo.Registration;
import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author DELL
 */
public interface RegistrationRepository {
    void save(Registration registration);
    Registration findByStudentIdAndActivityParticipationTypeId(Integer studentId, int activityParticipationTypeId); 
    void processCSV(MultipartFile file, int activityParticipationTypeId);
    List<Registration> getRegistrations();
    List<Registration> getRegistrationsByFacultyId(int facultyId);
    List<Registration> findRegistrationsByStudentId(int id, Map<String, String> params);
    Registration findRegistrationById(int id);
    List<Registration> filterRegistrationsBySemester(List<Registration> registrations, int semesterId);
    Registration findRegistrationOwner(int studentId, int registrationId);
    void delete(int id);
}
