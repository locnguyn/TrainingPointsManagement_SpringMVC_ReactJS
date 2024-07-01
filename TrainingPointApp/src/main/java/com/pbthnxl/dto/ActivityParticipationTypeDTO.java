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
    private int point;
    private String participationType;
    private RegistrationActivityDTO activity;
    private Boolean isParticipated;
    private Boolean isRegistered;

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
     * @return the isParticipated
     */
    public Boolean getIsParticipated() {
        return isParticipated;
    }

    /**
     * @param isParticipated the isParticipated to set
     */
    public void setIsParticipated(Boolean isParticipated) {
        this.isParticipated = isParticipated;
    }

    /**
     * @return the isRegistered
     */
    public Boolean getIsRegistered() {
        return isRegistered;
    }

    /**
     * @param isRegistered the isRegistered to set
     */
    public void setIsRegistered(Boolean isRegistered) {
        this.isRegistered = isRegistered;
    }

}
