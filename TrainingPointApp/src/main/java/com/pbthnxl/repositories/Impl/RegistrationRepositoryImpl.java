/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.repositories.Impl;

import com.pbthnxl.pojo.ActivityParticipationType;
import com.pbthnxl.pojo.Registration;
import com.pbthnxl.pojo.Student;
import com.pbthnxl.repositories.ActivityParticipationTypeRepository;
import com.pbthnxl.repositories.RegistrationRepository;
import com.pbthnxl.repositories.StudentRepository;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author DELL
 */
@Repository
@Transactional
public class RegistrationRepositoryImpl implements RegistrationRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ActivityParticipationTypeRepository activityParticipationTypeRepository;

    @Override
    public void save(Registration registration) {
        Session s = factory.getObject().getCurrentSession();
        s.save(registration);
    }

    @Override
    public boolean existsByStudentIdAndActivityParticipationTypeId(Integer studentId, int activityParticipationTypeId) {
        Session s = factory.getObject().getCurrentSession();
        Query<Long> query = s.createQuery(
                "SELECT COUNT(r) FROM Registration r WHERE r.studentId.id = :studentId AND r.activityParticipationTypeId.id = :activityParticipationTypeId",
                Long.class
        );
        query.setParameter("studentId", studentId);
        query.setParameter("activityParticipationTypeId", activityParticipationTypeId);
        return query.getSingleResult() > 0;
    }

    @Override
    public void processCSV(MultipartFile file, int activityParticipationTypeId) {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
            Date currentDate = new Date();

            for (CSVRecord record : records) {
                try {
                    String studentCode = record.get("MSSV");
                    Student student = studentRepository.findByStudentCode(studentCode);

                    if (student != null) {
                        // Kiểm tra xem đã tồn tại registration cho student và activityParticipationType này chưa
                        if (!this.existsByStudentIdAndActivityParticipationTypeId(student.getId(), activityParticipationTypeId)) {
                            Registration registration = new Registration();
                            registration.setRegistrationDate(currentDate);
                            registration.setParticipated(true);
                            registration.setActivityParticipationTypeId(this.activityParticipationTypeRepository.getActivityParticipationTypeById(activityParticipationTypeId));
                            registration.setStudentId(student);
                            this.save(registration);
                            System.out.println(registration + "____________________________________");
                        }
                    } else {
                        System.out.println("Student not found for MSSV: " + studentCode);
                    }
                } catch (Exception e) {
                    System.out.println("Error processing record: " + record);
                    e.printStackTrace();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(RegistrationRepositoryImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<Registration> getRegistrations() {
        Session s = factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("Registration.findAll");
        return q.getResultList();
    }

    @Override
    public List<Registration> getRegistrationsByFacultyId(int facultyId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query query = s.createQuery("SELECT r FROM Registration r " +
                                       "JOIN FETCH r.studentId s " +
                                       "JOIN FETCH s.facultyId f " +
                                       "WHERE f.id = :facultyId AND r.participated = true", Registration.class);
        query.setParameter("facultyId", facultyId);
        
        return query.getResultList();
    }

    @Override
    public List<Registration> findRegistrationsByStudentId(int id) {
         Session s = factory.getObject().getCurrentSession();
         Query q = s.createNamedQuery("Registration.findByStudentId");
         q.setParameter("studentId", id);
         
         return q.getResultList();
    }

    @Override
    public Registration findRegistrationById(int id) {
        Session s = factory.getObject().getCurrentSession();
        
        return s.get(Registration.class, id);
    }

    @Override
    public void delete(int id) {
        Session s = factory.getObject().getCurrentSession();
        
        Registration r = s.get(Registration.class, id);
        if(r != null){
            s.delete(r);
        }
    }

    @Override
    public Registration findRegistrationOwner(int studentId, int registrationId) {
        Session s = factory.getObject().getCurrentSession();
        
        Query<Registration> q = s.createQuery("SELECT r FROM Registration r " +
                                "WHERE r.id = :registrationId AND r.studentId.id = :studentId", Registration.class);
        
        q.setParameter("studentId", studentId);
        q.setParameter("registrationId", registrationId);
        
        return q.getSingleResult();
    }

}
