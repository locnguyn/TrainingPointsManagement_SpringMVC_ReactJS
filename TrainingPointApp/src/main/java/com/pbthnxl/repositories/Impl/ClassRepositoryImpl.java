/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.repositories.Impl;

import com.pbthnxl.pojo.Class;
import com.pbthnxl.repositories.ClassRepository;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
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
public class ClassRepositoryImpl implements ClassRepository {
    @Autowired
    private LocalSessionFactoryBean factory;
    
    @Override
    public List<Class> getClasses() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("Class.findAll");
        
        return q.getResultList();
    }

    @Override
    public Class getClassById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Class.class, id);
    }
    
}
