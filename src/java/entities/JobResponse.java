/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author erman
 */
@Entity
@Table(name = "job_response")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "JobResponse.findAll", query = "SELECT j FROM JobResponse j"),
    @NamedQuery(name = "JobResponse.findByJobresponseID", query = "SELECT j FROM JobResponse j WHERE j.jobresponseID = :jobresponseID"),
    @NamedQuery(name = "JobResponse.findByStartDate", query = "SELECT j FROM JobResponse j WHERE j.startDate = :startDate"),
    @NamedQuery(name = "JobResponse.findByEndDate", query = "SELECT j FROM JobResponse j WHERE j.endDate = :endDate"),
    @NamedQuery(name = "JobResponse.findByTimeZone", query = "SELECT j FROM JobResponse j WHERE j.timeZone = :timeZone"),
    @NamedQuery(name = "JobResponse.findByHourPerDay", query = "SELECT j FROM JobResponse j WHERE j.hourPerDay = :hourPerDay"),
    @NamedQuery(name = "JobResponse.findByNumberOfTruck", query = "SELECT j FROM JobResponse j WHERE j.numberOfTruck = :numberOfTruck"),
    @NamedQuery(name = "JobResponse.findByBillingPrice", query = "SELECT j FROM JobResponse j WHERE j.billingPrice = :billingPrice"),
    @NamedQuery(name = "JobResponse.findBySubmitReview", query = "SELECT j FROM JobResponse j WHERE j.submitReview = :submitReview"),
    @NamedQuery(name = "JobResponse.findByReviewFromExcavator", query = "SELECT j FROM JobResponse j WHERE j.reviewFromExcavator = :reviewFromExcavator"),
    @NamedQuery(name = "JobResponse.findByPaymentStatus", query = "SELECT j FROM JobResponse j WHERE j.paymentStatus = :paymentStatus"),
    @NamedQuery(name = "JobResponse.findByDeleted", query = "SELECT j FROM JobResponse j WHERE j.deleted = :deleted"),
    @NamedQuery(name = "JobResponse.findBySubmitted", query = "SELECT j FROM JobResponse j WHERE j.submitted = :submitted"),
    @NamedQuery(name = "JobResponse.findByCreationDate", query = "SELECT j FROM JobResponse j WHERE j.creationDate = :creationDate"),
    @NamedQuery(name = "JobResponse.findByLastWeeklyDate", query = "SELECT j FROM JobResponse j WHERE j.lastWeeklyDate = :lastWeeklyDate")})
public class JobResponse implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "version")
    @Version
    private int version;
    @Basic(optional = false)
    @NotNull
    @Column(name = "last_modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "last_edit_end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastEditEndDate;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "job_response_ID")
    private Integer jobresponseID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "time_zone")
    private int timeZone;
    @Column(name = "hour_per_day")
    private Integer hourPerDay;
    @Column(name = "number_of_truck")
    private Integer numberOfTruck;
    @Basic(optional = false)
    @NotNull
    @Column(name = "billing_price")
    private float billingPrice;
    @Basic(optional = false)
    @NotNull
    @Column(name = "submit_review")
    private int submitReview;
    @Lob
    @Size(max = 65535)
    @Column(name = "review_comment")
    private String reviewComment;
    @Basic(optional = false)
    @NotNull
    @Column(name = "review_from_excavator")
    private int reviewFromExcavator;
    @Lob
    @Size(max = 65535)
    @Column(name = "comment_from_excavator")
    private String commentFromExcavator;
    @Basic(optional = false)
    @NotNull
    @Column(name = "payment_status")
    private boolean paymentStatus;
    @Basic(optional = false)
    @NotNull
    @Column(name = "deleted")
    private boolean deleted;
    @Lob
    @Size(max = 65535)
    @Column(name = "deleted_reason")
    private String deletedReason;
    @Basic(optional = false)
    @NotNull
    @Column(name = "submitted")
    private boolean submitted;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "last_weekly_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastWeeklyDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "jobresponseID", fetch = FetchType.LAZY)
    private List<WeeklyTicket> weeklyTicketList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "jobresponseID", fetch = FetchType.LAZY)
    private List<DailyTicket> dailyTicketList;
    @OneToMany(mappedBy = "jobresponseID", fetch = FetchType.LAZY)
    private List<Validation> validationList;
    @JoinColumn(name = "payment_type_ID", referencedColumnName = "payment_type_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private PaymentType paymenttypeID;
    @JoinColumn(name = "job_ID", referencedColumnName = "job_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Job jobID;
    @JoinColumn(name = "truck_owner_ID", referencedColumnName = "user_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private User truckownerID;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "jobresponseID", fetch = FetchType.LAZY)
    private List<JobLog> jobLogList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "jobresponseID", fetch = FetchType.LAZY)
    private List<SolicitedTruck> solicitedTruckList;

    public JobResponse() {
    }

    public JobResponse(Integer jobresponseID) {
        this.jobresponseID = jobresponseID;
    }

    public JobResponse(Integer jobresponseID, Date startDate, Date endDate, int timeZone, float billingPrice, int submitReview, int reviewFromExcavator, boolean paymentStatus, boolean deleted, boolean submitted, Date creationDate) {
        this.jobresponseID = jobresponseID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.timeZone = timeZone;
        this.billingPrice = billingPrice;
        this.submitReview = submitReview;
        this.reviewFromExcavator = reviewFromExcavator;
        this.paymentStatus = paymentStatus;
        this.deleted = deleted;
        this.submitted = submitted;
        this.creationDate = creationDate;
    }

    public Integer getJobresponseID() {
        return jobresponseID;
    }

    public void setJobresponseID(Integer jobresponseID) {
        this.jobresponseID = jobresponseID;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(int timeZone) {
        this.timeZone = timeZone;
    }

    public Integer getHourPerDay() {
        return hourPerDay;
    }

    public void setHourPerDay(Integer hourPerDay) {
        this.hourPerDay = hourPerDay;
    }

    public Integer getNumberOfTruck() {
        return numberOfTruck;
    }

    public void setNumberOfTruck(Integer numberOfTruck) {
        this.numberOfTruck = numberOfTruck;
    }

    public float getBillingPrice() {
        return billingPrice;
    }

    public void setBillingPrice(float billingPrice) {
        this.billingPrice = billingPrice;
    }

    public int getSubmitReview() {
        return submitReview;
    }

    public void setSubmitReview(int submitReview) {
        this.submitReview = submitReview;
    }

    public String getReviewComment() {
        return reviewComment;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }

    public int getReviewFromExcavator() {
        return reviewFromExcavator;
    }

    public void setReviewFromExcavator(int reviewFromExcavator) {
        this.reviewFromExcavator = reviewFromExcavator;
    }

    public String getCommentFromExcavator() {
        return commentFromExcavator;
    }

    public void setCommentFromExcavator(String commentFromExcavator) {
        this.commentFromExcavator = commentFromExcavator;
    }

    public boolean getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getDeletedReason() {
        return deletedReason;
    }

    public void setDeletedReason(String deletedReason) {
        this.deletedReason = deletedReason;
    }

    public boolean getSubmitted() {
        return submitted;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastWeeklyDate() {
        return lastWeeklyDate;
    }

    public void setLastWeeklyDate(Date lastWeeklyDate) {
        this.lastWeeklyDate = lastWeeklyDate;
    }

    @XmlTransient
    public List<WeeklyTicket> getWeeklyTicketList() {
        return weeklyTicketList;
    }

    public void setWeeklyTicketList(List<WeeklyTicket> weeklyTicketList) {
        this.weeklyTicketList = weeklyTicketList;
    }

    @XmlTransient
    public List<DailyTicket> getDailyTicketList() {
        return dailyTicketList;
    }

    public void setDailyTicketList(List<DailyTicket> dailyTicketList) {
        this.dailyTicketList = dailyTicketList;
    }

    @XmlTransient
    public List<Validation> getValidationList() {
        return validationList;
    }

    public void setValidationList(List<Validation> validationList) {
        this.validationList = validationList;
    }

    public PaymentType getPaymenttypeID() {
        return paymenttypeID;
    }

    public void setPaymenttypeID(PaymentType paymenttypeID) {
        this.paymenttypeID = paymenttypeID;
    }

    public Job getJobID() {
        return jobID;
    }

    public void setJobID(Job jobID) {
        this.jobID = jobID;
    }

    public User getTruckownerID() {
        return truckownerID;
    }

    public void setTruckownerID(User truckownerID) {
        this.truckownerID = truckownerID;
    }

    @XmlTransient
    public List<JobLog> getJobLogList() {
        return jobLogList;
    }

    public void setJobLogList(List<JobLog> jobLogList) {
        this.jobLogList = jobLogList;
    }

    @XmlTransient
    public List<SolicitedTruck> getSolicitedTruckList() {
        return solicitedTruckList;
    }

    public void setSolicitedTruckList(List<SolicitedTruck> solicitedTruckList) {
        this.solicitedTruckList = solicitedTruckList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (jobresponseID != null ? jobresponseID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JobResponse)) {
            return false;
        }
        JobResponse other = (JobResponse) object;
        if ((this.jobresponseID == null && other.jobresponseID != null) || (this.jobresponseID != null && !this.jobresponseID.equals(other.jobresponseID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.JobResponse[ jobresponseID=" + jobresponseID + " ]";
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Date getLastEditEndDate() {
        return lastEditEndDate;
    }

    public void setLastEditEndDate(Date lastEditEndDate) {
        this.lastEditEndDate = lastEditEndDate;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
    
}
