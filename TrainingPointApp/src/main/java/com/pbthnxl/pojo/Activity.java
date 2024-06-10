/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.pbthnxl.validator.EndDateAfterStartDate;

/**
 *
 * @author DELL
 */
@EndDateAfterStartDate(message = "{activity.endDateAfterStartDate.message}")
@Entity
@Table(name = "activity")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Activity.findAll", query = "SELECT a FROM Activity a"),
    @NamedQuery(name = "Activity.findById", query = "SELECT a FROM Activity a WHERE a.id = :id"),
    @NamedQuery(name = "Activity.findByName", query = "SELECT a FROM Activity a WHERE a.name = :name"),
    @NamedQuery(name = "Activity.findByStartDateTime", query = "SELECT a FROM Activity a WHERE a.startDateTime = :startDateTime"),
    @NamedQuery(name = "Activity.findByEndDate", query = "SELECT a FROM Activity a WHERE a.endDate = :endDate"),
    @NamedQuery(name = "Activity.findByLocation", query = "SELECT a FROM Activity a WHERE a.location = :location")})
public class Activity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Size(min = 1, max = 100, message = "{activity.name.minMaxErr}")
    @NotNull(message = "{activity.name.nullErr}")
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull(message = "{activity.startDateTime.nullErr}")
    @Column(name = "start_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDateTime;
    @Basic(optional = false)
    @NotNull(message = "{activity.endDate.nullErr}")
    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Size(max = 45, message = "{activity.location.maxErr}")
    @Column(name = "location")
    private String location;
    @JoinColumn(name = "article_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @JsonIgnore
    private Article articleId;
    @JoinColumn(name = "faculty_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @JsonIgnore
    private Faculty facultyId;
    @JoinColumn(name = "participant_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @JsonIgnore
    private Participant participantId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @JsonIgnore
    private User userId;
    @OneToMany(mappedBy = "activityId")
    @JsonIgnore
    private Set<Interaction> interactionSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "activityId")
    @JsonIgnore
    private Set<ActivityParticipationType> activityParticipationTypeSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "activityId")
    @JsonIgnore
    private Set<Comment> commentSet;

    public Activity() {
    }

    public Activity(Integer id) {
        this.id = id;
    }

    public Activity(Integer id, String name, Date startDateTime, Date endDate) {
        this.id = id;
        this.name = name;
        this.startDateTime = startDateTime;
        this.endDate = endDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Article getArticleId() {
        return articleId;
    }

    public void setArticleId(Article articleId) {
        this.articleId = articleId;
    }

    public Faculty getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Faculty facultyId) {
        this.facultyId = facultyId;
    }

    public Participant getParticipantId() {
        return participantId;
    }

    public void setParticipantId(Participant participantId) {
        this.participantId = participantId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    @XmlTransient
    public Set<Interaction> getInteractionSet() {
        return interactionSet;
    }

    public void setInteractionSet(Set<Interaction> interactionSet) {
        this.interactionSet = interactionSet;
    }

    @XmlTransient
    public Set<ActivityParticipationType> getActivityParticipationTypeSet() {
        return activityParticipationTypeSet;
    }

    public void setActivityParticipationTypeSet(Set<ActivityParticipationType> activityParticipationTypeSet) {
        this.activityParticipationTypeSet = activityParticipationTypeSet;
    }

    @XmlTransient
    public Set<Comment> getCommentSet() {
        return commentSet;
    }

    public void setCommentSet(Set<Comment> commentSet) {
        this.commentSet = commentSet;
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
        if (!(object instanceof Activity)) {
            return false;
        }
        Activity other = (Activity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pbthnxl.pojo.Activity[ id=" + id + " ]";
    }
    
}
