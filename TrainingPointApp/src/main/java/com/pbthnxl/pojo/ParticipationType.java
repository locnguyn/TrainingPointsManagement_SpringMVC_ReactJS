/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.pojo;

import com.pbthnxl.validator.UniqueParticipationTypeName;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author DELL
 */
@Entity
@Table(name = "participation_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ParticipationType.findAll", query = "SELECT p FROM ParticipationType p"),
    @NamedQuery(name = "ParticipationType.findById", query = "SELECT p FROM ParticipationType p WHERE p.id = :id"),
    @NamedQuery(name = "ParticipationType.findByName", query = "SELECT p FROM ParticipationType p WHERE p.name = :name")})
public class ParticipationType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull(message = "{participationType.name.nullErr}")
    @Size(max = 20, message = "{participationType.name.sizeErr}")
    @Column(name = "name")
    @UniqueParticipationTypeName(message = "{participationType.UniqueName.message}")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "participationTypeId")
    private Set<ActivityParticipationType> activityParticipationTypeSet;

    public ParticipationType() {
    }

    public ParticipationType(Integer id) {
        this.id = id;
    }

    public ParticipationType(Integer id, String name) {
        this.id = id;
        this.name = name;
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

    @XmlTransient
    public Set<ActivityParticipationType> getActivityParticipationTypeSet() {
        return activityParticipationTypeSet;
    }

    public void setActivityParticipationTypeSet(Set<ActivityParticipationType> activityParticipationTypeSet) {
        this.activityParticipationTypeSet = activityParticipationTypeSet;
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
        if (!(object instanceof ParticipationType)) {
            return false;
        }
        ParticipationType other = (ParticipationType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pbthnxl.pojo.ParticipationType[ id=" + id + " ]";
    }
    
}
