/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author DELL
 */
public class ActivityParticipationTypeDTO {
    private Integer id;
    private Integer activityId;
    private int point;
    private String activity;
    private String participationType;
    private String location;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "UTC")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDateTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "UTC")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDateTime;
    private String participant;
    private String article;
    private String faculty;

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the activityId
     */
    public Integer getActivityId() {
        return activityId;
    }

    /**
     * @param activityId the activityId to set
     */
    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    /**
     * @return the point
     */
    public int getPoint() {
        return point;
    }

    /**
     * @param point the point to set
     */
    public void setPoint(int point) {
        this.point = point;
    }

    /**
     * @return the activity
     */
    public String getActivity() {
        return activity;
    }

    /**
     * @param activity the activity to set
     */
    public void setActivity(String activity) {
        this.activity = activity;
    }

    /**
     * @return the participationType
     */
    public String getParticipationType() {
        return participationType;
    }

    /**
     * @param participationType the participationType to set
     */
    public void setParticipationType(String participationType) {
        this.participationType = participationType;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param activityLocation the location to set
     */
    public void setLocation(String activityLocation) {
        this.location = activityLocation;
    }

    /**
     * @return the startDateTime
     */
    public Date getStartDateTime() {
        return startDateTime;
    }

    /**
     * @param startDateTime the startDateTime to set
     */
    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     * @return the endDateTime
     */
    public Date getEndDateTime() {
        return endDateTime;
    }

    /**
     * @param endDateTime the endDateTime to set
     */
    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    /**
     * @return the participant
     */
    public String getParticipant() {
        return participant;
    }

    /**
     * @param participant the participant to set
     */
    public void setParticipant(String participant) {
        this.participant = participant;
    }

    /**
     * @return the article
     */
    public String getArticle() {
        return article;
    }

    /**
     * @param article the article to set
     */
    public void setArticle(String article) {
        this.article = article;
    }

    /**
     * @return the faculty
     */
    public String getFaculty() {
        return faculty;
    }

    /**
     * @param faculty the faculty to set
     */
    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }
}
