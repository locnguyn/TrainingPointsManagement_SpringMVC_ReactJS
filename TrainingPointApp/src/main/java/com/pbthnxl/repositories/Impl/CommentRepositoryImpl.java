/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.repositories.Impl;

import com.pbthnxl.pojo.Comment;
import com.pbthnxl.repositories.CommentRepository;
import java.util.List;
import javax.persistence.Query;
import org.hibernate.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author hieu
 */
@Repository
@Transactional
public class CommentRepositoryImpl implements CommentRepository{
    @Autowired
    private LocalSessionFactoryBean factory;
    
    @Override
    public List<Comment> getCommentsByActivityId(int activityId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("Comment.findByActivityId");
        q.setParameter("activityId", activityId);
        
        return q.getResultList();
    }

    @Override
    public void save(Comment comment) {
        Session s = this.factory.getObject().getCurrentSession();
        s.save(comment);
    }

    @Override
    public void delete(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        Comment c = s.get(Comment.class, id);
        
        if(c != null){
            s.delete(c);
        }
    }
    
}
