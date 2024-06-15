/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.dto;

import java.util.Date;

/**
 *
 * @author hieu
 */
public class RegistrationDTO {
    private Integer id;
    private Integer acPartTypeId;
    private Date createdAt;
    private Boolean participated;
    private String participationTypeName;
    private Integer point;
    private RegistrationActivityDTO activity;

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
     * @return the createdAt
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt the createdAt to set
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return the participated
     */
    public Boolean getParticipated() {
        return participated;
    }

    /**
     * @param participated the participated to set
     */
    public void setParticipated(Boolean participated) {
        this.participated = participated;
    }

    /**
     * @return the participationTypeName
     */
    public String getParticipationTypeName() {
        return participationTypeName;
    }

    /**
     * @param participationTypeName the participationTypeName to set
     */
    public void setParticipationTypeName(String participationTypeName) {
        this.participationTypeName = participationTypeName;
    }

    /**
     * @return the activity
     */
    public RegistrationActivityDTO getActivity() {
        return activity;
    }

    /**
     * @param activity the activity to set
     */
    public void setActivity(RegistrationActivityDTO activity) {
        this.activity = activity;
    }

    /**
     * @return the point
     */
    public Integer getPoint() {
        return point;
    }

    /**
     * @param point the point to set
     */
    public void setPoint(Integer point) {
        this.point = point;
    }

    /**
     * @return the acPartTypeId
     */
    public Integer getAcPartTypeId() {
        return acPartTypeId;
    }

    /**
     * @param acPartTypeId the acPartTypeId to set
     */
    public void setAcPartTypeId(Integer acPartTypeId) {
        this.acPartTypeId = acPartTypeId;
    }
    
}
