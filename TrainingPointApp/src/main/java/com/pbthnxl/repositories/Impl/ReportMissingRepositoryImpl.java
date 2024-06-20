/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.repositories.Impl;

import com.pbthnxl.pojo.Registration;
import com.pbthnxl.pojo.ReportMissing;
import com.pbthnxl.pojo.User;
import com.pbthnxl.repositories.ReportMissingRepository;
import com.pbthnxl.repositories.UserRepository;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author DELL
 */
@Repository
@Transactional
public class ReportMissingRepositoryImpl implements ReportMissingRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Autowired
    private UserRepository userRepo;

    @Override
    public List<ReportMissing> getReportMissings() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("ReportMissing.findByChecked");
        q.setParameter("checked", false);
        return q.getResultList();
    }

    @Override
    public void confirmReportMissingById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        ReportMissing reportMissing = session.get(ReportMissing.class, id);

        if (reportMissing != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();

            User currentUser = userRepo.getUserByUsername(username);

            reportMissing.setUserId(currentUser);
            

            Registration existingRegistration = findExistingRegistration(session, reportMissing);
            if (existingRegistration != null) {
                existingRegistration.setStudentId(reportMissing.getStudentId());
                existingRegistration.setActivityParticipationTypeId(reportMissing.getActivityParticipationTypeId());
                existingRegistration.setRegistrationDate(reportMissing.getReportDate());
                existingRegistration.setParticipated(true);
                session.update(existingRegistration);
            } else {
                Registration registration = new Registration();
                registration.setStudentId(reportMissing.getStudentId());
                registration.setActivityParticipationTypeId(reportMissing.getActivityParticipationTypeId());
                registration.setRegistrationDate(reportMissing.getReportDate());
                registration.setParticipated(true);
                session.save(registration);
            }

            reportMissing.setChecked(true);
            session.update(reportMissing);
        }
    }

    private Registration findExistingRegistration(Session session, ReportMissing reportMissing) {
        String hql = "FROM Registration WHERE studentId = :studentId AND activityParticipationTypeId = :activityParticipationTypeId";
        Query query = session.createQuery(hql);
        query.setParameter("studentId", reportMissing.getStudentId());
        query.setParameter("activityParticipationTypeId", reportMissing.getActivityParticipationTypeId());
        
        try {
            return (Registration) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
        
    }

    @Override
    public void rejectReportMissingById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        ReportMissing reportMissing = session.get(ReportMissing.class, id);

        if (reportMissing != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();

            User currentUser = userRepo.getUserByUsername(username);

            reportMissing.setUserId(currentUser);
            reportMissing.setChecked(true);
            session.update(reportMissing);
        }
    }

    @Override
    public void save(ReportMissing r) {
        Session s = this.factory.getObject().getCurrentSession();

        s.saveOrUpdate(r);
    }

    @Override
    public List<ReportMissing> getStudentReportMissings(int studentId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("ReportMissing.findByStudentId");
        q.setParameter("studentId", studentId);
        return q.getResultList();
    }

    @Override
    public ReportMissing findByStudentIdAndActivityParticipationTypeId(int studentId, int activityParticipationTypeId) {
        Session s = factory.getObject().getCurrentSession();
        
        String hql = "SELECT r FROM ReportMissing r WHERE r.studentId.id = :studentId AND r.activityParticipationTypeId.id = :activityParticipationTypeId";
        
        Query q = s.createQuery(hql);
        q.setParameter("studentId", studentId);
        q.setParameter("activityParticipationTypeId", activityParticipationTypeId);
        
        try {
            return (ReportMissing) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
        
    }

}
