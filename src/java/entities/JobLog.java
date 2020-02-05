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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author erman
 */
@Entity
@Table(name = "job_log")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "JobLog.findAll", query = "SELECT j FROM JobLog j"),
    @NamedQuery(name = "JobLog.findByJoblogID", query = "SELECT j FROM JobLog j WHERE j.joblogID = :joblogID"),
    @NamedQuery(name = "JobLog.findByDateLog", query = "SELECT j FROM JobLog j WHERE j.dateLog = :dateLog"),
    @NamedQuery(name = "JobLog.findByExDateLog", query = "SELECT j FROM JobLog j WHERE j.exDateLog = :exDateLog"),
    @NamedQuery(name = "JobLog.findByTimeLeft", query = "SELECT j FROM JobLog j WHERE j.timeLeft = :timeLeft"),
    @NamedQuery(name = "JobLog.findByTimeOnSite", query = "SELECT j FROM JobLog j WHERE j.timeOnSite = :timeOnSite"),
    @NamedQuery(name = "JobLog.findByNumberOfLoad", query = "SELECT j FROM JobLog j WHERE j.numberOfLoad = :numberOfLoad"),
    @NamedQuery(name = "JobLog.findByStartTime", query = "SELECT j FROM JobLog j WHERE j.startTime = :startTime"),
    @NamedQuery(name = "JobLog.findByEndTime", query = "SELECT j FROM JobLog j WHERE j.endTime = :endTime"),
    @NamedQuery(name = "JobLog.findByTypeOfDirt", query = "SELECT j FROM JobLog j WHERE j.typeOfDirt = :typeOfDirt"),
    @NamedQuery(name = "JobLog.findByFromWhere", query = "SELECT j FROM JobLog j WHERE j.fromWhere = :fromWhere"),
    @NamedQuery(name = "JobLog.findByToWhere", query = "SELECT j FROM JobLog j WHERE j.toWhere = :toWhere"),
    @NamedQuery(name = "JobLog.findByCreationDate", query = "SELECT j FROM JobLog j WHERE j.creationDate = :creationDate"),
    @NamedQuery(name = "JobLog.findByDeleted", query = "SELECT j FROM JobLog j WHERE j.deleted = :deleted"),
    @NamedQuery(name = "JobLog.findByClosed", query = "SELECT j FROM JobLog j WHERE j.closed = :closed")})
public class JobLog implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "edition_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date editionDate;
    @JoinColumn(name = "add_account_ID", referencedColumnName = "account_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Account addaccountID;
    @JoinColumn(name = "edit_account_ID", referencedColumnName = "account_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Account editaccountID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "version")
    @Version
    private int version;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "job_log_ID")
    private Integer joblogID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "date_log")
    private String dateLog;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ex_date_log")
    @Temporal(TemporalType.TIMESTAMP)
    private Date exDateLog;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "time_left")
    private String timeLeft;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "time_on_site")
    private String timeOnSite;
    @Basic(optional = false)
    @NotNull
    @Column(name = "number_of_load")
    private int numberOfLoad;
    @Column(name = "start_time")
    @Temporal(TemporalType.TIME)
    private Date startTime;
    @Column(name = "end_time")
    @Temporal(TemporalType.TIME)
    private Date endTime;
    @Size(max = 100)
    @Column(name = "type_of_dirt")
    private String typeOfDirt;
    @Size(max = 100)
    @Column(name = "from_where")
    private String fromWhere;
    @Size(max = 100)
    @Column(name = "to_where")
    private String toWhere;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "deleted")
    private boolean deleted;
    @Basic(optional = false)
    @NotNull
    @Column(name = "closed")
    private boolean closed;
    @Lob
    @Size(max = 65535)
    @Column(name = "note_on_day")
    private String noteOnDay;
    @JoinColumn(name = "truck_ID", referencedColumnName = "truck_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Truck truckID;
    @JoinColumn(name = "daily_ticket_ID", referencedColumnName = "daily_ticket_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DailyTicket dailyticketID;
    @JoinColumn(name = "job_response_ID", referencedColumnName = "job_response_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private JobResponse jobresponseID;

    public JobLog() {
    }

    public JobLog(Integer joblogID) {
        this.joblogID = joblogID;
    }

    public JobLog(Integer joblogID, String dateLog, Date exDateLog, String timeLeft, String timeOnSite, int numberOfLoad, Date creationDate, boolean deleted, boolean closed) {
        this.joblogID = joblogID;
        this.dateLog = dateLog;
        this.exDateLog = exDateLog;
        this.timeLeft = timeLeft;
        this.timeOnSite = timeOnSite;
        this.numberOfLoad = numberOfLoad;
        this.creationDate = creationDate;
        this.deleted = deleted;
        this.closed = closed;
    }

    public Integer getJoblogID() {
        return joblogID;
    }

    public void setJoblogID(Integer joblogID) {
        this.joblogID = joblogID;
    }

    public String getDateLog() {
        return dateLog;
    }

    public void setDateLog(String dateLog) {
        this.dateLog = dateLog;
    }

    public Date getExDateLog() {
        return exDateLog;
    }

    public void setExDateLog(Date exDateLog) {
        this.exDateLog = exDateLog;
    }

    public String getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(String timeLeft) {
        this.timeLeft = timeLeft;
    }

    public String getTimeOnSite() {
        return timeOnSite;
    }

    public void setTimeOnSite(String timeOnSite) {
        this.timeOnSite = timeOnSite;
    }

    public int getNumberOfLoad() {
        return numberOfLoad;
    }

    public void setNumberOfLoad(int numberOfLoad) {
        this.numberOfLoad = numberOfLoad;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getTypeOfDirt() {
        return typeOfDirt;
    }

    public void setTypeOfDirt(String typeOfDirt) {
        this.typeOfDirt = typeOfDirt;
    }

    public String getFromWhere() {
        return fromWhere;
    }

    public void setFromWhere(String fromWhere) {
        this.fromWhere = fromWhere;
    }

    public String getToWhere() {
        return toWhere;
    }

    public void setToWhere(String toWhere) {
        this.toWhere = toWhere;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean getClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public String getNoteOnDay() {
        return noteOnDay;
    }

    public void setNoteOnDay(String noteOnDay) {
        this.noteOnDay = noteOnDay;
    }

    public Truck getTruckID() {
        return truckID;
    }

    public void setTruckID(Truck truckID) {
        this.truckID = truckID;
    }

    public DailyTicket getDailyticketID() {
        return dailyticketID;
    }

    public void setDailyticketID(DailyTicket dailyticketID) {
        this.dailyticketID = dailyticketID;
    }

    public JobResponse getJobresponseID() {
        return jobresponseID;
    }

    public void setJobresponseID(JobResponse jobresponseID) {
        this.jobresponseID = jobresponseID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (joblogID != null ? joblogID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JobLog)) {
            return false;
        }
        JobLog other = (JobLog) object;
        if ((this.joblogID == null && other.joblogID != null) || (this.joblogID != null && !this.joblogID.equals(other.joblogID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.JobLog[ joblogID=" + joblogID + " ]";
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Account getAddaccountID() {
        return addaccountID;
    }

    public void setAddaccountID(Account addaccountID) {
        this.addaccountID = addaccountID;
    }

    public Account getEditaccountID() {
        return editaccountID;
    }

    public void setEditaccountID(Account editaccountID) {
        this.editaccountID = editaccountID;
    }

    public Date getEditionDate() {
        return editionDate;
    }

    public void setEditionDate(Date editionDate) {
        this.editionDate = editionDate;
    }
    
}
