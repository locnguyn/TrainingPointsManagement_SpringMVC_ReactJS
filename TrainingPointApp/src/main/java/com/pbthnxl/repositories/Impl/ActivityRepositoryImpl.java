/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.repositories.Impl;

import com.pbthnxl.pojo.Activity;
import com.pbthnxl.repositories.ActivityRepository;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
public class ActivityRepositoryImpl implements ActivityRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Autowired
    private Environment env;

    @Override
    public List<Activity> getActivities() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("Activity.findAll");
        List<Activity> ac = q.getResultList();
        return ac;
    }

    @Override
    public Activity getActivityById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Activity.class, id);
    }

    @Override
    public void addOrUpdate(Activity activity) {
        Session s = this.factory.getObject().getCurrentSession();
        if (activity.getId() != null) {
            s.update(activity);
        } else {
            s.save(activity);
        }
    }

    @Override
    public List<Activity> findFilteredActivities(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Activity> q = b.createQuery(Activity.class);
        Root<Activity> r = q.from(Activity.class);
        q.select(r);

        List<Predicate> predicates = new ArrayList<>();
        
        String kw = params.get("kw");
        if (kw != null && !kw.isEmpty()) {
            predicates.add(b.like(r.get("name"), String.format("%%%s%%", kw)));
        }

        String facultyId = params.get("faculty");
        if (facultyId != null && !facultyId.isEmpty()) {
            predicates.add(b.equal(r.get("facultyId").get("id"), Integer.parseInt(facultyId)));
        }

        String article = params.get("article");
        if (article != null && !article.isEmpty()) {
            predicates.add(b.equal(r.get("articleId").get("id"), Integer.parseInt(article)));
        }

        String filterType = params.get("filterType");
        if (filterType != null && !filterType.isEmpty()) {
            Date startDate = null;
            Date endDate = null;
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            if ("week".equalsIgnoreCase(filterType)) {
                startDate = calendar.getTime();

                calendar.add(Calendar.DAY_OF_WEEK, 6); // Thêm 6 ngày để đến Chủ Nhật của tuần
                endDate = calendar.getTime();
            } else if ("month".equalsIgnoreCase(filterType)) {
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                startDate = calendar.getTime();

                calendar.add(Calendar.MONTH, 1);
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                endDate = calendar.getTime();
            }
            predicates.add(b.and(
                    b.greaterThanOrEqualTo(r.get("startDateTime"), startDate),
                    b.lessThanOrEqualTo(r.get("startDateTime"), endDate),
                    b.or(
                            b.greaterThanOrEqualTo(r.get("endDate"), startDate),
                            b.lessThanOrEqualTo(r.get("endDate"), endDate)
                    )
            ));
        }

        q.where(predicates.toArray(new Predicate[0]));
        q.orderBy(b.desc(r.get("startDateTime")));

        Query query = s.createQuery(q);

        String page = params.get("page");
        if (page != null && !page.isEmpty()) {
            int pageSize = Integer.parseInt(env.getProperty("activities.pageSize").toString());
            int pageNumber = Integer.parseInt(page);
            int start = (pageNumber - 1) * pageSize;
            query.setFirstResult(start);
            query.setMaxResults(pageSize);
        }

        return query.getResultList();
    }

    @Override
    public void deleteActivity(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        Activity activity = s.get(Activity.class, id);
        if (activity != null) {
            s.delete(activity);
        }
    }
}
