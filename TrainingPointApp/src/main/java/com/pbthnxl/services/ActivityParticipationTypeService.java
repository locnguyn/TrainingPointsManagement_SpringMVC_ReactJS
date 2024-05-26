/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.services;

import com.pbthnxl.pojo.ActivityParticipationType;
import java.util.List;

/**
 *
 * @author DELL
 */
public interface ActivityParticipationTypeService {
    List<ActivityParticipationType> getActivityParticipationType();
    List<ActivityParticipationType> getActivityParticipationTypesByActivityId(int activityId);
    void addOrUpdate(ActivityParticipationType activityParticipationType);
    ActivityParticipationType getActivityParticipationTypeById(int id);
}
