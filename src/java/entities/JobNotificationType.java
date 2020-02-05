/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
 * @author camertronix
 */
@Entity
@Table(name = "job_notification_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "JobNotificationType.findAll", query = "SELECT j FROM JobNotificationType j"),
    @NamedQuery(name = "JobNotificationType.findByJobnotificationtypeID", query = "SELECT j FROM JobNotificationType j WHERE j.jobnotificationtypeID = :jobnotificationtypeID"),
    @NamedQuery(name = "JobNotificationType.findByNotifiacationType", query = "SELECT j FROM JobNotificationType j WHERE j.notifiacationType = :notifiacationType"),
    @NamedQuery(name = "JobNotificationType.findByDescription", query = "SELECT j FROM JobNotificationType j WHERE j.description = :description")})
public class JobNotificationType implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "jobnotificationtypeID", fetch = FetchType.LAZY)
    private List<JobNotification> jobNotificationList;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "job_notification_type_ID")
    private Integer jobnotificationtypeID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "notifiacation_type")
    private String notifiacationType;
    @Size(max = 200)
    @Column(name = "description")
    private String description;

    public JobNotificationType() {
    }

    public JobNotificationType(Integer jobnotificationtypeID) {
        this.jobnotificationtypeID = jobnotificationtypeID;
    }

    public JobNotificationType(Integer jobnotificationtypeID, String notifiacationType) {
        this.jobnotificationtypeID = jobnotificationtypeID;
        this.notifiacationType = notifiacationType;
    }

    public Integer getJobnotificationtypeID() {
        return jobnotificationtypeID;
    }

    public void setJobnotificationtypeID(Integer jobnotificationtypeID) {
        this.jobnotificationtypeID = jobnotificationtypeID;
    }

    public String getNotifiacationType() {
        return notifiacationType;
    }

    public void setNotifiacationType(String notifiacationType) {
        this.notifiacationType = notifiacationType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (jobnotificationtypeID != null ? jobnotificationtypeID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JobNotificationType)) {
            return false;
        }
        JobNotificationType other = (JobNotificationType) object;
        if ((this.jobnotificationtypeID == null && other.jobnotificationtypeID != null) || (this.jobnotificationtypeID != null && !this.jobnotificationtypeID.equals(other.jobnotificationtypeID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.JobNotificationType[ jobnotificationtypeID=" + jobnotificationtypeID + " ]";
    }

    @XmlTransient
    public List<JobNotification> getJobNotificationList() {
        return jobNotificationList;
    }

    public void setJobNotificationList(List<JobNotification> jobNotificationList) {
        this.jobNotificationList = jobNotificationList;
    }
    
}
