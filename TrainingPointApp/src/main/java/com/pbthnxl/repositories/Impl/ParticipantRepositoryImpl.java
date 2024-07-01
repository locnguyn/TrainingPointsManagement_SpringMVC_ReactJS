/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.repositories.Impl;

import com.pbthnxl.pojo.Participant;
import com.pbthnxl.repositories.ParticipantRepository;
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
public class ParticipantRepositoryImpl implements ParticipantRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Participant> getParticipants() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("Participant.findAll");
        return q.getResultList();
    }

    @Override
    public Participant findByName(String name) {
        Session s = this.factory.getObject().getCurrentSession();

        Query q = s.createNamedQuery("Participant.findByName");
        q.setParameter("name", name);
        try {
            return (Participant) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void addOrUpdate(Participant p) {
        Session s = this.factory.getObject().getCurrentSession();
        System.out.println("__________________________________________" + p.getId());
        if (p.getId() != null) {
            s.update(p);
        } else {
            s.save(p);
        }
    }

    @Override
    public Participant getParticipantById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Participant.class, id);
    }

    @Override
    public void delete(Participant c) {
        Session s = this.factory.getObject().getCurrentSession();

        s.delete(c);
    }

}
