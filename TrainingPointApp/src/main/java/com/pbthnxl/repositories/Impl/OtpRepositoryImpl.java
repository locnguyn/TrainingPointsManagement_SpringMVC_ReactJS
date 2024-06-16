/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.repositories.Impl;

import com.pbthnxl.pojo.Otp;
import com.pbthnxl.repositories.OtpRepository;
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
public class OtpRepositoryImpl implements OtpRepository{
    @Autowired LocalSessionFactoryBean factory;
    
    @Override
    public void save(Otp otp) {
        Session s = factory.getObject().getCurrentSession();
        
        if(otp.getId() != null){
            s.update(otp);
        } else{
            s.save(otp);
        }
    }

    @Override
    public Otp findByEmail(String email) {
        Session s = factory.getObject().getCurrentSession();
        
        Query q = s.createNamedQuery("Otp.findByEmail");
        
        q.setParameter("email", email);
        
        List<Otp> results = q.getResultList();
        if(results.isEmpty()){
            return null;
        } else{
            return results.get(0);
        }

//        return (Otp)q.getSingleResult();
        
    }
    
}
