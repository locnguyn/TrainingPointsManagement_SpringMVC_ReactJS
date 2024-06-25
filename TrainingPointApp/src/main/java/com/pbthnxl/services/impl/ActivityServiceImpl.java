/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.services.impl;

import com.pbthnxl.dto.ActivityDTO;
import com.pbthnxl.dto.ActivityParticipationTypeDTO;
import com.pbthnxl.dto.CommentDTO;
import com.pbthnxl.dto.RegistrationActivityDTO;
import com.pbthnxl.pojo.Activity;
import com.pbthnxl.pojo.ActivityParticipationType;
import com.pbthnxl.pojo.Comment;
import com.pbthnxl.repositories.ActivityParticipationTypeRepository;
import com.pbthnxl.repositories.ActivityRepository;
import com.pbthnxl.repositories.CommentRepository;
import com.pbthnxl.services.ActivityParticipationTypeService;
import com.pbthnxl.services.ActivityService;
import com.pbthnxl.services.CommentService;
import com.pbthnxl.services.RegistrationService;
import com.pbthnxl.services.UserService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
    @Autowired
    ActivityParticipationTypeRepository acPartTypeRepo;
    @Autowired
    private CommentRepository commentRepo;
    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityParticipationTypeService acPartTypeService;

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

    @Override
    public List<ActivityDTO> findFilteredActivitiesDTO(Map<String, String> params) {
        List<Activity> list = this.activityRepo.findFilteredActivities(params);
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private ActivityDTO convertToDTO(Activity a) {
        ActivityDTO dto = new ActivityDTO();

        dto.setId(a.getId());
        dto.setName(a.getName());
        dto.setLocation(a.getLocation());
        dto.setArticle(a.getArticleId().getName());
        dto.setFaculty(a.getFacultyId().getName());
        dto.setStartDateTime(a.getStartDateTime());
        dto.setEndDateTime(a.getEndDate());
        dto.setParticipant(a.getParticipantId().getName());
        return dto;
    }

    @Override
    public ActivityDTO getActivityByIdDTO(int id, String username) {
        Activity a = this.getActivityById(id);

        ActivityDTO dto = this.convertToDTO(a);

        int studentId = this.userService.getUserByUsername(username).getStudent().getId();

//        dto.setComments(this.commentRepo.getCommentsByActivityId(id).stream().map(c -> this.commentService.convertToDTO(c, username)).collect(Collectors.toSet()));
        dto.setActivityParticipationTypes(this.acPartTypeRepo.getActivityParticipationTypesByActivityId(id).stream().map((c) -> this.acPartTypeService.convertToDTO(c, false, studentId)).collect(Collectors.toSet()));
        dto.setLikes(this.countLikes(id));
        dto.setLiked(this.isUserLikedActivity(id, username));
        return dto;
    }

    private ActivityParticipationTypeDTO convertToDTO(ActivityParticipationType type) {
        ActivityParticipationTypeDTO dto = new ActivityParticipationTypeDTO();
        Activity a = type.getActivityId();
        dto.setId(type.getId());
        dto.setPoint(type.getPoint());
        dto.setParticipationType(type.getParticipationTypeId().getName());
        return dto;
    }

    @Override

    public int countLikes(int activityId) {
        return this.activityRepo.countLikes(activityId);
    }

    @Override
    public boolean isUserLikedActivity(Integer activityId, String username) {
        return this.activityRepo.isUserLikedActivity(activityId, username);
    }

    @Override
    public RegistrationActivityDTO convertToRegistrationActivityDTO(Activity a) {
        RegistrationActivityDTO dto = new RegistrationActivityDTO();

        dto.setActivityId(a.getId());
        dto.setName(a.getName());
        dto.setParticipant(a.getParticipantId().getName());
        dto.setStartDateTime(a.getStartDateTime());
        dto.setEndDateTime(a.getEndDate());
        dto.setLocation(a.getLocation());
        dto.setFaculty(a.getFacultyId().getName());
        dto.setArticle(a.getArticleId().getName());

        return dto;

    }
}
