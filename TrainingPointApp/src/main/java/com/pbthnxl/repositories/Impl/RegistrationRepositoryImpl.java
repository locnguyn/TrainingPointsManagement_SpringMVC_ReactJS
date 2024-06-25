/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.repositories.Impl;

import com.pbthnxl.pojo.ActivityParticipationType;
import com.pbthnxl.pojo.Registration;
import com.pbthnxl.pojo.ReportMissing;
import com.pbthnxl.pojo.Semester;
import com.pbthnxl.pojo.Student;
import com.pbthnxl.repositories.ActivityParticipationTypeRepository;
import com.pbthnxl.repositories.RegistrationRepository;
import com.pbthnxl.repositories.ReportMissingRepository;
import com.pbthnxl.repositories.SemesterRepository;
import com.pbthnxl.repositories.StudentRepository;
import com.pbthnxl.services.SemesterService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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

    @Autowired
    private ReportMissingRepository reportRepo;
    
    @Autowired
    private SemesterService semesterService;

    @Autowired
    private SemesterRepository semesterRepo;
    
    @Override
    public void save(Registration registration) {
        Session s = factory.getObject().getCurrentSession();

        s.saveOrUpdate(registration);
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
                        Registration registration = this.findByStudentIdAndActivityParticipationTypeId(student.getId(), activityParticipationTypeId);

                        ReportMissing report = this.reportRepo.findByStudentIdAndActivityParticipationTypeId(student.getId(), activityParticipationTypeId);

                        // Kiểm tra xem đã tồn tại registration cho student và activityParticipationType này chưa
                        if (report != null) {
                            report.setChecked(true);

                            this.reportRepo.save(report);
                        }
                        if (registration == null) {
                            registration = new Registration();
                            registration.setRegistrationDate(currentDate);
                            registration.setParticipated(true);
                            registration.setActivityParticipationTypeId(this.activityParticipationTypeRepository.getActivityParticipationTypeById(activityParticipationTypeId));
                            registration.setStudentId(student);
                            this.save(registration);
                        } else {
                            registration.setParticipated(true);

                            this.save(registration);
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
        Query query = s.createQuery("SELECT r FROM Registration r "
                + "JOIN FETCH r.studentId s "
                + "JOIN FETCH s.facultyId f "
                + "WHERE f.id = :facultyId AND r.participated = true", Registration.class);
        query.setParameter("facultyId", facultyId);

        return query.getResultList();
    }

    @Override
    public List<Registration> findRegistrationsByStudentId(int id, Map<String, String> params) {
        Session s = factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Registration> q = b.createQuery(Registration.class);
        Root<Registration> r = q.from(Registration.class);

        q.select(r);

        List<Predicate> predicates = new ArrayList();
        
        predicates.add(b.equal(r.get("studentId"), id));

        //kiem tra dang ky  nam trong hk nao
        String date = params.get("currentDate");
        if (date != null && !date.isEmpty()) {
            List<Semester> semesters = this.semesterRepo.getSemesters();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

            Date currentDate;
            try {
                currentDate = formatter.parse(params.get("currentDate"));
                System.out.println(currentDate);

                for (Semester sem : semesters) {
                    if (currentDate.after(sem.getStartDate()) && currentDate.before(sem.getEndDate())) {
                        predicates.add(b.and(
                                b.greaterThanOrEqualTo(r.get("registrationDate"), sem.getStartDate()),
                                b.lessThanOrEqualTo(r.get("registrationDate"), sem.getEndDate())
                        ));

                    }
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
            }

        }

        q.where(predicates.toArray(new Predicate[0]));
        q.orderBy(b.desc(r.get("registrationDate")));

        Query query = s.createQuery(q);

        return query.getResultList();
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
        if (r != null) {
            s.delete(r);
        }
    }

    @Override
    public Registration findByStudentIdAndActivityParticipationTypeId(Integer studentId, int activityParticipationTypeId) {
        Session s = factory.getObject().getCurrentSession();
        Query<Registration> query = s.createQuery(
                "SELECT r FROM Registration r WHERE r.studentId.id = :studentId AND r.activityParticipationTypeId.id = :activityParticipationTypeId",
                Registration.class
        );
        query.setParameter("studentId", studentId);
        query.setParameter("activityParticipationTypeId", activityParticipationTypeId);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
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

    @Override
    public List<Registration> filterRegistrationsBySemester(List<Registration> registrations, int semesterId) {
        if (semesterId == 0) {
            return registrations;
        }

        Semester semester = semesterService.getSemesterById(semesterId);
        if (semester == null) {
            return registrations;
        }

        Date startDate = semester.getStartDate();
        Date endDate = semester.getEndDate();
        return registrations.stream()
                .filter(r -> r.getRegistrationDate().getTime() > startDate.getTime() && r.getRegistrationDate().getTime() < endDate.getTime())
                .collect(Collectors.toList());
    }

    @Override
    public List<Registration> getRegistrationsByClassId(int classId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query query = s.createQuery("SELECT r FROM Registration r "
                + "JOIN FETCH r.studentId s "
                + "JOIN FETCH s.classId c "
                + "WHERE c.id = :classId AND r.participated = true", Registration.class);
        query.setParameter("classId", classId);
        return query.getResultList();
    }

}
