/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.repositories.Impl;

import com.pbthnxl.pojo.Faculty;
import com.pbthnxl.repositories.FacultyRepository;
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
public class FacultyRepositoryImpl implements FacultyRepository {
    @Autowired
    private LocalSessionFactoryBean factory;
    
    @Override
    public List<Faculty> getFaculties() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("Faculty.findAll");
        return q.getResultList();
    }

    @Override
    public Faculty getFacultyById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Faculty.class, id);
    }

    @Override
    public void addOrUpdate(Faculty c) {
        Session s = this.factory.getObject().getCurrentSession();
        if (c.getId() != null) {
            s.update(c);
        } else {
            s.save(c);
        }
    }

    @Override
    public void delete(Faculty c) {
        Session s = this.factory.getObject().getCurrentSession();

        s.delete(c);
    }
    
}
