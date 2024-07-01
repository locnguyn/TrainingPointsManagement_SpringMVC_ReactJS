/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.services;

import com.pbthnxl.dto.ActivityDTO;
import com.pbthnxl.dto.RegistrationActivityDTO;
import com.pbthnxl.pojo.Activity;
import java.util.List;
import java.util.Map;

/**
 *
 * @author DELL
 */
public interface ActivityService {
    List<Activity> getActivities();
    Activity getActivityById(int id);
    void addOrUpdate(Activity activity);
    List<Activity> findFilteredActivities(Map<String, String> params);
    List<ActivityDTO> findFilteredActivitiesDTO(Map<String, String> params);
    void deleteActivity(int id);
    ActivityDTO getActivityByIdDTO(int id, String username);
    int countLikes(int activityId);
    boolean isUserLikedActivity(Integer activityId, String username);
    RegistrationActivityDTO convertToRegistrationActivityDTO(Activity a);
}
