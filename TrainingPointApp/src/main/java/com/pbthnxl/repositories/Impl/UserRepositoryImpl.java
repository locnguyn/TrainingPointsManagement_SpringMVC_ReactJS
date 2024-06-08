/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.repositories.Impl;

import com.pbthnxl.pojo.User;
import com.pbthnxl.repositories.UserRepository;
import java.util.List;
import javax.persistence.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author DELL
 */
@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private LocalSessionFactoryBean factory;
    @Autowired
    private BCryptPasswordEncoder passEncoder;

    @Override
    public User getUserByUsername(String username) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM User WHERE username = :username");
        q.setParameter("username", username);

        List<User> results = q.getResultList();
        if (!results.isEmpty()) {
            return results.get(0);
        } else {
            return null; // hoặc throw một exception tùy vào yêu cầu của ứng dụng
        }
    }

    @Override
    public int getIdByUsername(String username) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM User WHERE username = :username");
        q.setParameter("username", username);

        return ((User) q.getSingleResult()).getId();
    }

    @Override
    public void saveUser(User user) {
        Session s = this.factory.getObject().getCurrentSession();
        if (user.getId() != null) {
            s.update(user);
        } else {
            s.save(user);
        }
    }
    
    @Override
    public void updateUserRole(int userId, String role) {
        Session s = this.factory.getObject().getCurrentSession();
        User user = s.get(User.class, userId);
        if (user != null) {
            user.setUserRole(role);
            s.update(user);
            System.out.println("update role successfully______________________________________________________________________________________");
        }
    }

    @Override
    public boolean authUser(String username, String password) {
        User  u = this.getUserByUsername(username);
        
        return this.passEncoder.matches(password, u.getPassword());
    }


}
