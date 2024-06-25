/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.repositories.Impl;

import com.pbthnxl.pojo.ParticipationType;
import com.pbthnxl.repositories.ParticipationTypeRepository;
import java.util.List;
import javax.persistence.NoResultException;
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

    @Override
    public ParticipationType findByName(String name) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("ParticipationType.findByName");
        q.setParameter("name", name);
        try {
            return (ParticipationType) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void addOrUpdate(ParticipationType c) {
        Session s = this.factory.getObject().getCurrentSession();
        if (c.getId() != null) {
            s.update(c);
        System.out.println("update__________________________________________" + c.getName());
        } else {
            s.save(c);
        System.out.println("add__________________________________________" + c.getName());
        }
    }

    @Override
    public ParticipationType getParticipationTypeById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(ParticipationType.class, id);
    }

    @Override
    public void delete(ParticipationType c) {
        Session s = this.factory.getObject().getCurrentSession();

        s.delete(c);
    }

}
