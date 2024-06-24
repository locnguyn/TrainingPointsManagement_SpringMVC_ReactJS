/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.repositories.Impl;

import com.pbthnxl.pojo.Semester;
import com.pbthnxl.repositories.SemesterRepository;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
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
public class SemesterRepositoryImpl implements SemesterRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Semester> getSemesters() {
        Session s = this.factory.getObject().getCurrentSession();

        Query q = s.createNamedQuery("Semester.findAll");

        return q.getResultList();
    }

    @Override
    public Semester getSemesterById(int id) {
        Session s = this.factory.getObject().getCurrentSession();

        return s.get(Semester.class, id);
    }

    @Override
    public void addOrUpdate(Semester s) {
        Session ss = this.factory.getObject().getCurrentSession();
        System.out.println("---------------------------------------------------------------------------------------------------------------------" + s.getId());
        if (s.getId() != null) {
            ss.update(s);
        } else {
            ss.save(s);
        }
    }

    @Override
    public void delete(Semester s) {
        Session ss = this.factory.getObject().getCurrentSession();

        ss.delete(s);
    }

}
