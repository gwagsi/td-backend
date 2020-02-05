/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
 * @author camertronix
 */
@Entity
@Table(name = "job_notification")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "JobNotification.findAll", query = "SELECT j FROM JobNotification j"),
    @NamedQuery(name = "JobNotification.findByJobnotificationID", query = "SELECT j FROM JobNotification j WHERE j.jobnotificationID = :jobnotificationID"),
    @NamedQuery(name = "JobNotification.findByCreationDate", query = "SELECT j FROM JobNotification j WHERE j.creationDate = :creationDate"),
    @NamedQuery(name = "JobNotification.findByDeleted", query = "SELECT j FROM JobNotification j WHERE j.deleted = :deleted"),
    @NamedQuery(name = "JobNotification.findByState", query = "SELECT j FROM JobNotification j WHERE j.state = :state"),
    @NamedQuery(name = "JobNotification.findByValue", query = "SELECT j FROM JobNotification j WHERE j.value = :value")})
public class JobNotification implements Serializable {
    @JoinColumn(name = "job_notification_type_ID", referencedColumnName = "job_notification_type_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private JobNotificationType jobnotificationtypeID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "deleted")
    private boolean deleted;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "job_notification_ID")
    private Integer jobnotificationID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "state")
    private boolean state;
    @Size(max = 500)
    @Column(name = "value")
    private String value;
    @JoinColumn(name = "account_ID", referencedColumnName = "account_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Account accountID;
    @JoinColumn(name = "job_ID", referencedColumnName = "job_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Job jobID;

    public JobNotification() {
    }

    public JobNotification(Integer jobnotificationID) {
        this.jobnotificationID = jobnotificationID;
    }

    public JobNotification(Integer jobnotificationID, Date creationDate, boolean deleted, boolean state) {
        this.jobnotificationID = jobnotificationID;
        this.creationDate = creationDate;
        this.deleted = deleted;
        this.state = state;
    }

    public Integer getJobnotificationID() {
        return jobnotificationID;
    }

    public void setJobnotificationID(Integer jobnotificationID) {
        this.jobnotificationID = jobnotificationID;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }


    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Account getAccountID() {
        return accountID;
    }

    public void setAccountID(Account accountID) {
        this.accountID = accountID;
    }

    public Job getJobID() {
        return jobID;
    }

    public void setJobID(Job jobID) {
        this.jobID = jobID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (jobnotificationID != null ? jobnotificationID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JobNotification)) {
            return false;
        }
        JobNotification other = (JobNotification) object;
        if ((this.jobnotificationID == null && other.jobnotificationID != null) || (this.jobnotificationID != null && !this.jobnotificationID.equals(other.jobnotificationID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.JobNotification[ jobnotificationID=" + jobnotificationID + " ]";
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public JobNotificationType getJobnotificationtypeID() {
        return jobnotificationtypeID;
    }

    public void setJobnotificationtypeID(JobNotificationType jobnotificationtypeID) {
        this.jobnotificationtypeID = jobnotificationtypeID;
    }
    
}
