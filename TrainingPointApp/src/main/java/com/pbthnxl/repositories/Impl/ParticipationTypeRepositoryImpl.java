/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.repositories.Impl;

import com.pbthnxl.pojo.ParticipationType;
import com.pbthnxl.repositories.ParticipationTypeRepository;
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
public class ParticipationTypeRepositoryImpl implements ParticipationTypeRepository {
    @Autowired
    private LocalSessionFactoryBean factory;
    
    @Override
    public List<ParticipationType> getParticipationTypes() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("ParticipationType.findAll");
        return q.getResultList();
    }
    
}
