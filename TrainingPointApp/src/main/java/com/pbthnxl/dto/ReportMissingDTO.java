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
 * @author hieu
 */
public class ReportMissingDTO {
    private Integer id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "UTC")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportDate;
    private String proof;
    private String status;
    private ActivityParticipationTypeDTO activityPartType;

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
     * @return the reportDate
     */
    public Date getReportDate() {
        return reportDate;
    }

    /**
     * @param reportDate the reportDate to set
     */
    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    /**
     * @return the proof
     */
    public String getProof() {
        return proof;
    }

    /**
     * @param proof the proof to set
     */
    public void setProof(String proof) {
        this.proof = proof;
    }


    /**
     * @return the activityPartType
     */
    public ActivityParticipationTypeDTO getActivityPartType() {
        return activityPartType;
    }

    /**
     * @param activityPartType the activityPartType to set
     */
    public void setActivityPartType(ActivityParticipationTypeDTO activityPartType) {
        this.activityPartType = activityPartType;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
