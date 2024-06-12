/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.Set;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author hieu
 */
public class ActivityDTO {
    private Integer id;
    private String name;
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
    private Set<CommentDTO> comments;
    private Set<ActivityParticipationTypeDTO> activityParticipationTypes;
    private Integer likes; 
    private Boolean liked;

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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
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

    /**
     * @return the comments
     */
    public Set<CommentDTO> getComments() {
        return comments;
    }

    /**
     * @param comments the comments to set
     */
    public void setComments(Set<CommentDTO> comments) {
        this.comments = comments;
    }

    /**
     * @return the activityParticipationTypes
     */
    public Set<ActivityParticipationTypeDTO> getActivityParticipationTypes() {
        return activityParticipationTypes;
    }

    /**
     * @param activityParticipationTypes the activityParticipationTypes to set
     */
    public void setActivityParticipationTypes(Set<ActivityParticipationTypeDTO> activityParticipationTypes) {
        this.activityParticipationTypes = activityParticipationTypes;
    }

    /**
     * @return the likes
     */
    public Integer getLikes() {
        return likes;
    }

    /**
     * @param likes the likes to set
     */
    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    /**
     * @return the liked
     */
    public Boolean getLiked() {
        return liked;
    }

    /**
     * @param liked the liked to set
     */
    public void setLiked(Boolean liked) {
        this.liked = liked;
    }
}
