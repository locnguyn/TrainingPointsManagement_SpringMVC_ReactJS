/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.repositories.Impl;

import com.pbthnxl.pojo.ActivityParticipationType;
import com.pbthnxl.repositories.ActivityParticipationTypeRepository;
import java.util.List;
import javax.persistence.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author DELL
 */
@Repository
@Transactional
public class ActivityParticipationTypeRepositoryImpl implements ActivityParticipationTypeRepository {
    @Autowired
    private LocalSessionFactoryBean factory;
    
    @Override
    public List<ActivityParticipationType> getActivityParticipationType() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("ActivityParticipationType.findAll");
        return q.getResultList();
    }

    @Override
    public void addOrUpdate(ActivityParticipationType activityParticipationType) {
        Session s = this.factory.getObject().getCurrentSession();
        if (activityParticipationType.getId() != null){
            s.update(activityParticipationType);
        }
        else{
            s.save(activityParticipationType); 
        }
    }

    @Override
    public ActivityParticipationType getActivityParticipationTypeById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(ActivityParticipationType.class, id);
    }

    @Override
    public List<ActivityParticipationType> getActivityParticipationTypesByActivityId(int activityId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("ActivityParticipationType.findByActivityId");
        q.setParameter("activityId", activityId);
        
        return q.getResultList();
    }
    
}
