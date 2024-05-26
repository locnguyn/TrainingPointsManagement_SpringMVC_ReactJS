/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.repositories;

import com.pbthnxl.pojo.Activity;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author DELL
 */
public interface ActivityRepository {
    List<Activity> getActivities();
    Activity getActivityById(int id);
    void addOrUpdate(Activity activity);
    List<Activity> findFilteredActivities(Map<String, String> params);
}
