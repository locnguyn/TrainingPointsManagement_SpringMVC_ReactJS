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

            // Assuming you have a method to get User by username
            User currentUser = userRepo.getUserByUsername(username);

            reportMissing.setUserId(currentUser);

            Registration registration = new Registration();
            registration.setStudentId(reportMissing.getStudentId());
            registration.setActivityParticipationTypeId(reportMissing.getActivityParticipationTypeId());
            registration.setRegistrationDate(reportMissing.getReportDate());
            registration.setParticipated(true);

            session.save(registration);

            reportMissing.setChecked(true);
            session.update(reportMissing);
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

            // Assuming you have a method to get User by username
            User currentUser = userRepo.getUserByUsername(username);

            reportMissing.setUserId(currentUser);
            reportMissing.setChecked(true);
            session.update(reportMissing);
        }
    }

}
