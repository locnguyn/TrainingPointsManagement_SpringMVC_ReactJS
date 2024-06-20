/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.pojo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DELL
 */
@Entity
@Table(name = "report_missing")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReportMissing.findAll", query = "SELECT r FROM ReportMissing r"),
    @NamedQuery(name = "ReportMissing.findById", query = "SELECT r FROM ReportMissing r WHERE r.id = :id"),
    @NamedQuery(name = "ReportMissing.findByReportDate", query = "SELECT r FROM ReportMissing r WHERE r.reportDate = :reportDate"),
    @NamedQuery(name = "ReportMissing.findByProof", query = "SELECT r FROM ReportMissing r WHERE r.proof = :proof"),
    @NamedQuery(name = "ReportMissing.findByStudentId", query = "SELECT r FROM ReportMissing r WHERE r.studentId.id = :studentId"),
    @NamedQuery(name = "ReportMissing.findByChecked", query = "SELECT r FROM ReportMissing r WHERE r.checked = :checked")})
public class ReportMissing implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "report_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "proof")
    private String proof;
    @Basic(optional = false)
    @NotNull
    @Column(name = "checked")
    private boolean checked;
    @JoinColumn(name = "activity_participation_type_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ActivityParticipationType activityParticipationTypeId;
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Student studentId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private User userId;

    public ReportMissing() {
    }

    public ReportMissing(Integer id) {
        this.id = id;
    }

    public ReportMissing(Integer id, String proof, boolean checked) {
        this.id = id;
        this.proof = proof;
        this.checked = checked;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public String getProof() {
        return proof;
    }

    public void setProof(String proof) {
        this.proof = proof;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public ActivityParticipationType getActivityParticipationTypeId() {
        return activityParticipationTypeId;
    }

    public void setActivityParticipationTypeId(ActivityParticipationType activityParticipationTypeId) {
        this.activityParticipationTypeId = activityParticipationTypeId;
    }

    public Student getStudentId() {
        return studentId;
    }

    public void setStudentId(Student studentId) {
        this.studentId = studentId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReportMissing)) {
            return false;
        }
        ReportMissing other = (ReportMissing) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pbthnxl.pojo.ReportMissing[ id=" + id + " ]";
    }
    
}
