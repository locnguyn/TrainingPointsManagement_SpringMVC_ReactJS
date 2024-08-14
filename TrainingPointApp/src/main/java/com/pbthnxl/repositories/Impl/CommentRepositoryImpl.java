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
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author hieu
 */
@Repository
@Transactional
@PropertySource("classpath:paginations.properties")
public class CommentRepositoryImpl implements CommentRepository {

    @Autowired
    private LocalSessionFactoryBean factory;
    
    @Autowired
    private Environment env;

    @Override
    public List<Comment> getCommentsByActivityId(int activityId, int page) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("Comment.findByActivityId", Comment.class);
        q.setParameter("activityId", activityId);
        
        if (page != 0) {
            int pageSize = Integer.parseInt(env.getProperty("comments.pageSize").toString());
            int start = (page - 1) * pageSize;
            q.setFirstResult(start);
            q.setMaxResults(pageSize);
        }

        return q.getResultList();
    }

    @Override
    public void saveOrUpdate(Comment comment) {
        Session s = this.factory.getObject().getCurrentSession();
        s.saveOrUpdate(comment);
    }

    @Override
    public void delete(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        Comment c = s.get(Comment.class, id);

        if (c != null) {
            s.delete(c);
        }
    }

    @Override
    public Comment getCommentById(int id) {
        Session s = this.factory.getObject().getCurrentSession();

        return s.get(Comment.class, id);
    }

    @Override
    public int countLikes(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        org.hibernate.query.Query<Long> query = session.createNamedQuery("Comment.countInteractions", Long.class);
        query.setParameter("commentId", id);

        Long count = query.getSingleResult();
        return count != null ? count.intValue() : 0;
    }

    @Override
    public boolean isUserLikedComment(Integer commentId, String username) {
        Session s = this.factory.getObject().getCurrentSession();
        String query = "SELECT COUNT(i) FROM Interaction i WHERE i.commentId.id = :commentId AND i.userId.username = :username";
        Long count = s.createQuery(query, Long.class)
                .setParameter("commentId", commentId)
                .setParameter("username", username)
                .getSingleResult();
        return count > 0;
    }

}
