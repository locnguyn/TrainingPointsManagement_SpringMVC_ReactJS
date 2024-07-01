/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.repositories.Impl;

import com.pbthnxl.pojo.Student;
import com.pbthnxl.repositories.StudentRepository;
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
 * @author DELL
 */
@Repository
@Transactional
@PropertySource("classpath:configs.properties")
public class StudentRepositoryImpl implements StudentRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Autowired
    private Environment env;

    @Override
    public boolean isExistingStudentCode(String studentCode) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("Student.findByStudentCode");
        q.setParameter("studentCode", studentCode);
        List<Student> results = q.getResultList();
        return !results.isEmpty();
    }

    @Override
    public Student findByStudentCode(String studentCode) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("Student.findByStudentCode");
        q.setParameter("studentCode", studentCode);
        List<Student> results = q.getResultList();
        if (results.isEmpty()) {
            return null;  // Hoặc xử lý trường hợp không tìm thấy sinh viên theo cách khác
        }

        return results.get(0);
    }

    @Override
    public void saveStudent(Student student) {
        Session s = this.factory.getObject().getCurrentSession();
        if (student.getId() != null) {
            s.update(student);
        } else {
            s.save(student);
        }
    }

    @Override
    public List<Student> findAllAssistants() {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "SELECT s FROM Student s JOIN s.userId u WHERE u.userRole = :role";
        Query query = s.createQuery(hql, Student.class);
        query.setParameter("role", "ROLE_ASSISTANT");
        return query.getResultList();
    }

    @Override
    public List<Student> getStudentList(int facultyId, int page) {
        Session s = this.factory.getObject().getCurrentSession();
        if (facultyId == 0) {
            Query query = s.createNamedQuery("Student.findAll", Student.class);
            if (page != 0) {
                int pageSize = Integer.parseInt(env.getProperty("student.pageSize").toString());
                int start = (page - 1) * pageSize;
                query.setFirstResult(start);
                query.setMaxResults(pageSize);
            }
            return query.getResultList();
        }
        String hql = "SELECT s FROM Student s WHERE s.facultyId.id = :facultyId";
        Query query = s.createQuery(hql, Student.class);
        query.setParameter("facultyId", facultyId);
        if (page != 0) {
            int pageSize = Integer.parseInt(env.getProperty("student.pageSize").toString());
            int start = (page - 1) * pageSize;
            System.out.println(start + "_________________" + pageSize);
            query.setFirstResult(start);
            query.setMaxResults(pageSize);
        }
        return query.getResultList();
    }

    @Override
    public Student getStudentById(int id) {
        Session s = this.factory.getObject().getCurrentSession();

        return s.get(Student.class, id);
    }

}
