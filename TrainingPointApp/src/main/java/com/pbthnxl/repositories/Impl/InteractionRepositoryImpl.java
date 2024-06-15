/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.repositories.Impl;

import com.pbthnxl.pojo.Interaction;
import com.pbthnxl.repositories.InteractionRepository;
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
@Transactional
@Repository
public class InteractionRepositoryImpl implements InteractionRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public void save(Interaction i) {
        Session s = this.factory.getObject().getCurrentSession();
        
        s.save(i);
    }

    @Override
    public void delete(int id) {
        Session s = this.factory.getObject().getCurrentSession();

        Interaction i = s.get(Interaction.class, id);
        if(i != null){
            s.delete(i);
        }
    }

    @Override
    public Interaction getInteractionByUserIdAndActivityId(int userId, int activityId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("Interaction.findByUserIdAndActivityId");
        
        q.setParameter("userId", userId);
        q.setParameter("activityId", activityId);
        if(!q.getResultList().isEmpty())
            return (Interaction) q.getResultList().get(0);
        return null;
    }

    @Override
    public Interaction getInteractionByUserIdAndCommentId(int userId, int commentId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("Interaction.findByUserIdAndCommentId");
        
        q.setParameter("userId", userId);
        q.setParameter("commentId", commentId);
        if(!q.getResultList().isEmpty())
            return (Interaction) q.getResultList().get(0);
        return null;
    }
}
