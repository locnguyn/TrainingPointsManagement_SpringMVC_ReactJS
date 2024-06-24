/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.services.impl;

import com.pbthnxl.pojo.Registration;
import com.pbthnxl.pojo.Semester;
import com.pbthnxl.repositories.RegistrationRepository;
import com.pbthnxl.services.SemesterService;
import com.pbthnxl.services.StatisticService;
import java.util.Date;
import java.util.HashMap;
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
public class StatisticServiceImpl implements StatisticService {

    @Autowired
    private RegistrationRepository registrationRepository;
    
    @Autowired
    private SemesterService semesterService;

    @Override
    public Map<String, Object> getStatisticsByFacultyAndClass() {
        List<Registration> registrations = registrationRepository.getRegistrations();

        Map<String, Map<String, Integer>> facultyClassStats = new HashMap<>();

        for (Registration registration : registrations) {
            if (!registration.getParticipated()) {
                continue;
            }
            String facultyName = registration.getStudentId().getFacultyId().getName();
            String className = registration.getStudentId().getClassId().getName();
            int points = registration.getActivityParticipationTypeId().getPoint();

            facultyClassStats.putIfAbsent(facultyName, new HashMap<>());
            Map<String, Integer> classStats = facultyClassStats.get(facultyName);
            classStats.put(className, classStats.getOrDefault(className, 0) + points);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("facultyClassStats", facultyClassStats);
        return result;
    }

    @Override
    public Map<String, Object> getStatisticsForAllFaculties() {
        Map<String, Object> data = new HashMap<>();

        return data;
    }

    @Override
    public Map<String, Integer> getStatisticsForFaculty(int id) {
        Map<String, Integer> data = new HashMap<>();
        List<Registration> registrations = registrationRepository.getRegistrationsByFacultyId(id);
        for (Registration registration : registrations) {
            if (!registration.getParticipated()) {
                continue;
            }
            String className = registration.getStudentId().getClassId().getName();
            int points = registration.getActivityParticipationTypeId().getPoint();

            data.put(className, data.getOrDefault(className, 0) + points);
        }
        return data;
    }

    @Override
    public Map<String, Integer> getClassificationStatistics(int facultyId) {
        List<Registration> registrations;
        if(facultyId == 0)
            registrations = registrationRepository.getRegistrations();
        else
            registrations = registrationRepository.getRegistrationsByFacultyId(facultyId);

        Map<Integer, Integer> studentTotalPoints = new HashMap<>();
        for (Registration registration : registrations) {
            if (registration.getParticipated()) {
                int studentId = registration.getStudentId().getId();
                int points = registration.getActivityParticipationTypeId().getPoint();

                studentTotalPoints.put(studentId, studentTotalPoints.getOrDefault(studentId, 0) + points);
            }
        }

        Map<String, Integer> classificationStats = new HashMap<>();
        classificationStats.put("Xuất sắc", 0);
        classificationStats.put("Tốt", 0);
        classificationStats.put("Khá", 0);
        classificationStats.put("Trung bình", 0);
        classificationStats.put("Yếu", 0);
        classificationStats.put("Kém", 0);

        for (int totalPoints : studentTotalPoints.values()) {
            if (totalPoints >= 90) {
                classificationStats.put("Xuất sắc", classificationStats.get("Xuất sắc") + 1);
            } else if (totalPoints >= 80) {
                classificationStats.put("Tốt", classificationStats.get("Tốt") + 1);
            } else if (totalPoints >= 65) {
                classificationStats.put("Khá", classificationStats.get("Khá") + 1);
            } else if (totalPoints >= 50) {
                classificationStats.put("Trung bình", classificationStats.get("Trung bình") + 1);
            } else if (totalPoints >= 35) {
                classificationStats.put("Yếu", classificationStats.get("Yếu") + 1);
            } else {
                classificationStats.put("Kém", classificationStats.get("Kém") + 1);
            }
        }

        return classificationStats;
    }

    @Override
    public Map<String, Object> getStatisticsByFacultyAndClass(int semesterId) {
        List<Registration> registrations = registrationRepository.getRegistrations();
        registrations = this.registrationRepository.filterRegistrationsBySemester(registrations, semesterId);

        Map<String, Map<String, Integer>> facultyClassStats = new HashMap<>();

        for (Registration registration : registrations) {
            if (!registration.getParticipated()) {
                continue;
            }
            String facultyName = registration.getStudentId().getFacultyId().getName();
            String className = registration.getStudentId().getClassId().getName();
            int points = registration.getActivityParticipationTypeId().getPoint();

            facultyClassStats.putIfAbsent(facultyName, new HashMap<>());
            Map<String, Integer> classStats = facultyClassStats.get(facultyName);
            classStats.put(className, classStats.getOrDefault(className, 0) + points);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("facultyClassStats", facultyClassStats);
        return result;
    }

    @Override
    public Map<String, Integer> getStatisticsForFaculty(int id, int semesterId) {
        Map<String, Integer> data = new HashMap<>();
        List<Registration> registrations = registrationRepository.getRegistrationsByFacultyId(id);
        registrations = this.registrationRepository.filterRegistrationsBySemester(registrations, semesterId);

        for (Registration registration : registrations) {
            if (!registration.getParticipated()) {
                continue;
            }
            String className = registration.getStudentId().getClassId().getName();
            int points = registration.getActivityParticipationTypeId().getPoint();

            data.put(className, data.getOrDefault(className, 0) + points);
        }
        return data;
    }

    @Override
    public Map<String, Integer> getClassificationStatistics(int facultyId, int semesterId) {
        List<Registration> registrations;
        if(facultyId == 0)
            registrations = registrationRepository.getRegistrations();
        else
            registrations = registrationRepository.getRegistrationsByFacultyId(facultyId);
        
        registrations = this.registrationRepository.filterRegistrationsBySemester(registrations, semesterId);

        Map<Integer, Integer> studentTotalPoints = new HashMap<>();
        for (Registration registration : registrations) {
            if (registration.getParticipated()) {
                int studentId = registration.getStudentId().getId();
                int points = registration.getActivityParticipationTypeId().getPoint();

                studentTotalPoints.put(studentId, studentTotalPoints.getOrDefault(studentId, 0) + points);
            }
        }

        Map<String, Integer> classificationStats = new HashMap<>();
        classificationStats.put("Xuất sắc", 0);
        classificationStats.put("Tốt", 0);
        classificationStats.put("Khá", 0);
        classificationStats.put("Trung bình", 0);
        classificationStats.put("Yếu", 0);
        classificationStats.put("Kém", 0);

        for (int totalPoints : studentTotalPoints.values()) {
            if (totalPoints >= 90) {
                classificationStats.put("Xuất sắc", classificationStats.get("Xuất sắc") + 1);
            } else if (totalPoints >= 80) {
                classificationStats.put("Tốt", classificationStats.get("Tốt") + 1);
            } else if (totalPoints >= 65) {
                classificationStats.put("Khá", classificationStats.get("Khá") + 1);
            } else if (totalPoints >= 50) {
                classificationStats.put("Trung bình", classificationStats.get("Trung bình") + 1);
            } else if (totalPoints >= 35) {
                classificationStats.put("Yếu", classificationStats.get("Yếu") + 1);
            } else {
                classificationStats.put("Kém", classificationStats.get("Kém") + 1);
            }
        }

        return classificationStats;
    }

}
