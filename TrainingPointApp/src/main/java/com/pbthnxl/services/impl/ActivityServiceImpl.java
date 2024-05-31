/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.services.impl;

import com.pbthnxl.pojo.Activity;
import com.pbthnxl.repositories.ActivityRepository;
import com.pbthnxl.services.ActivityService;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author DELL
 */
@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    ActivityRepository activityRepo;

    @Override
    public List<Activity> getActivities() {
        return activityRepo.getActivities();
    }

    @Override
    public Activity getActivityById(int id) {
        return activityRepo.getActivityById(id);
    }

    @Override
    public void addOrUpdate(Activity activity) {
        this.activityRepo.addOrUpdate(activity);
    }

    @Override
    public List<Activity> findFilteredActivities(Map<String, String> params) {
        return activityRepo.findFilteredActivities(params);
    }

    @Override
    public void deleteActivity(int id) {
        this.activityRepo.deleteActivity(id);
    }
}
