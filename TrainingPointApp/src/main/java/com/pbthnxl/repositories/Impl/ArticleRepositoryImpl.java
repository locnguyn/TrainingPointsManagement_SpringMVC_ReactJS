/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.repositories.Impl;

import com.pbthnxl.pojo.Article;
import com.pbthnxl.repositories.ArticleRepository;
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
public class ArticleRepositoryImpl implements ArticleRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Article> getArticles() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("Article.findAll");
        return q.getResultList();
    }

    @Override
    public Article getArticleById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        
        return s.get(Article.class, id);
    }

    @Override
    public void addOrUpdate(Article c) {
        Session s = this.factory.getObject().getCurrentSession();
        if (c.getId() != null) {
            s.update(c);
        } else {
            s.save(c);
        }
    }

    @Override
    public void delete(Article c) {
        Session s = this.factory.getObject().getCurrentSession();

        s.delete(c);
    }

    @Override
    public Article findByName(String name) {
        Session s = this.factory.getObject().getCurrentSession();

        Query q = s.createNamedQuery("Article.findByName");
        q.setParameter("name", name);
        try {
            return (Article) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
}
