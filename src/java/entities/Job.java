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
@Table(name = "job")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Job.findAll", query = "SELECT j FROM Job j"),
    @NamedQuery(name = "Job.findByJobID", query = "SELECT j FROM Job j WHERE j.jobID = :jobID"),
    @NamedQuery(name = "Job.findByStartDate", query = "SELECT j FROM Job j WHERE j.startDate = :startDate"),
    @NamedQuery(name = "Job.findByEndDate", query = "SELECT j FROM Job j WHERE j.endDate = :endDate"),
    @NamedQuery(name = "Job.findByTimeZone", query = "SELECT j FROM Job j WHERE j.timeZone = :timeZone"),
    @NamedQuery(name = "Job.findByStartTime", query = "SELECT j FROM Job j WHERE j.startTime = :startTime"),
    @NamedQuery(name = "Job.findByHourPerDay", query = "SELECT j FROM Job j WHERE j.hourPerDay = :hourPerDay"),
    @NamedQuery(name = "Job.findByNumberOfTruck", query = "SELECT j FROM Job j WHERE j.numberOfTruck = :numberOfTruck"),
    @NamedQuery(name = "Job.findByCompanyRate", query = "SELECT j FROM Job j WHERE j.companyRate = :companyRate"),
    @NamedQuery(name = "Job.findByRate", query = "SELECT j FROM Job j WHERE j.rate = :rate"),
    @NamedQuery(name = "Job.findByBillingPrice", query = "SELECT j FROM Job j WHERE j.billingPrice = :billingPrice"),
    @NamedQuery(name = "Job.findByNumberOfRate", query = "SELECT j FROM Job j WHERE j.numberOfRate = :numberOfRate"),
    @NamedQuery(name = "Job.findByJobLocation", query = "SELECT j FROM Job j WHERE j.jobLocation = :jobLocation"),
    @NamedQuery(name = "Job.findByJobNumber", query = "SELECT j FROM Job j WHERE j.jobNumber = :jobNumber"),
    @NamedQuery(name = "Job.findByDotNumber", query = "SELECT j FROM Job j WHERE j.dotNumber = :dotNumber"),
    @NamedQuery(name = "Job.findByGeneralLiability", query = "SELECT j FROM Job j WHERE j.generalLiability = :generalLiability"),
    @NamedQuery(name = "Job.findByTruckLiability", query = "SELECT j FROM Job j WHERE j.truckLiability = :truckLiability"),
    @NamedQuery(name = "Job.findByProofOfTruckLiability", query = "SELECT j FROM Job j WHERE j.proofOfTruckLiability = :proofOfTruckLiability"),
    @NamedQuery(name = "Job.findByDirectDeposit", query = "SELECT j FROM Job j WHERE j.directDeposit = :directDeposit"),
    @NamedQuery(name = "Job.findByTimeSheet", query = "SELECT j FROM Job j WHERE j.timeSheet = :timeSheet"),
    @NamedQuery(name = "Job.findByAutomatedBooking", query = "SELECT j FROM Job j WHERE j.automatedBooking = :automatedBooking"),
    @NamedQuery(name = "Job.findByWeight", query = "SELECT j FROM Job j WHERE j.weight = :weight"),
    @NamedQuery(name = "Job.findByDeleted", query = "SELECT j FROM Job j WHERE j.deleted = :deleted"),
    @NamedQuery(name = "Job.findByClose", query = "SELECT j FROM Job j WHERE j.close = :close"),
    @NamedQuery(name = "Job.findByCreationDate", query = "SELECT j FROM Job j WHERE j.creationDate = :creationDate"),
    @NamedQuery(name = "Job.findByJobCreationType", query = "SELECT j FROM Job j WHERE j.jobCreationType = :jobCreationType")})
public class Job implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "jobID", fetch = FetchType.LAZY)
    private List<JobNotification> jobNotificationList;
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
    @Column(name = "last_edit_end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastEditEndDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "latitude")
    private float latitude;
    @Basic(optional = false)
    @NotNull
    @Column(name = "longitude")
    private float longitude;
    @Size(max = 100)
    @Column(name = "time_zone_ID")
    private String timezoneID;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "job_ID")
    private Integer jobID;
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
    @Size(max = 100)
    @Column(name = "start_time")
    private String startTime;
    @Column(name = "hour_per_day")
    private Integer hourPerDay;
    @Column(name = "number_of_truck")
    private Integer numberOfTruck;
    @Size(max = 100)
    @Column(name = "company_rate")
    private String companyRate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rate")
    private float rate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "billing_price")
    private float billingPrice;
    @Basic(optional = false)
    @NotNull
    @Column(name = "number_of_rate")
    private int numberOfRate;
    @Size(max = 100)
    @Column(name = "job_location")
    private String jobLocation;
    @Size(max = 100)
    @Column(name = "job_number")
    private String jobNumber;
    @Size(max = 100)
    @Column(name = "dot_number")
    private String dotNumber;
    @Basic(optional = false)
    @NotNull
    @Column(name = "general_liability")
    private int generalLiability;
    @Basic(optional = false)
    @NotNull
    @Column(name = "truck_liability")
    private int truckLiability;
    @Size(max = 100)
    @Column(name = "proof_of_truck_liability")
    private String proofOfTruckLiability;
    @Size(max = 100)
    @Column(name = "direct_deposit")
    private String directDeposit;
    @Size(max = 100)
    @Column(name = "time_sheet")
    private String timeSheet;
    @Size(max = 100)
    @Column(name = "automated_booking")
    private String automatedBooking;
    @Lob
    @Size(max = 65535)
    @Column(name = "job_description")
    private String jobDescription;
    @Lob
    @Size(max = 65535)
    @Column(name = "job_instruction")
    private String jobInstruction;
    @Size(max = 100)
    @Column(name = "weight")
    private String weight;
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
    @Column(name = "close")
    private boolean close;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "job_creation_type")
    private int jobCreationType;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "jobID", fetch = FetchType.LAZY)
    private List<JobDocument> jobDocumentList;
    @OneToMany(mappedBy = "jobID", fetch = FetchType.LAZY)
    private List<Validation> validationList;
    @JoinColumn(name = "type_of_dirt_ID", referencedColumnName = "dirt_type_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DirtType typeofdirtID;
    @JoinColumn(name = "lenght_of_bed", referencedColumnName = "lenght_of_bed_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private LenghtOfBed lenghtOfBed;
    @JoinColumn(name = "truck_axle", referencedColumnName = "truck_axle_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private TruckAxle truckAxle;
    @JoinColumn(name = "payment_type_ID", referencedColumnName = "payment_type_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PaymentType paymenttypeID;
    @JoinColumn(name = "excavator_ID", referencedColumnName = "user_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private User excavatorID;
    @OneToMany(mappedBy = "jobID", fetch = FetchType.LAZY)
    private List<JobResponse> jobResponseList;

    public Job() {
    }

    public Job(Integer jobID) {
        this.jobID = jobID;
    }

    public Job(Integer jobID, Date startDate, Date endDate, int timeZone, float rate, float billingPrice, int numberOfRate, int generalLiability, int truckLiability, boolean deleted, boolean close, Date creationDate, int jobCreationType) {
        this.jobID = jobID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.timeZone = timeZone;
        this.rate = rate;
        this.billingPrice = billingPrice;
        this.numberOfRate = numberOfRate;
        this.generalLiability = generalLiability;
        this.truckLiability = truckLiability;
        this.deleted = deleted;
        this.close = close;
        this.creationDate = creationDate;
        this.jobCreationType = jobCreationType;
    }

    public Integer getJobID() {
        return jobID;
    }

    public void setJobID(Integer jobID) {
        this.jobID = jobID;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
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

    public String getCompanyRate() {
        return companyRate;
    }

    public void setCompanyRate(String companyRate) {
        this.companyRate = companyRate;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public float getBillingPrice() {
        return billingPrice;
    }

    public void setBillingPrice(float billingPrice) {
        this.billingPrice = billingPrice;
    }

    public int getNumberOfRate() {
        return numberOfRate;
    }

    public void setNumberOfRate(int numberOfRate) {
        this.numberOfRate = numberOfRate;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getDotNumber() {
        return dotNumber;
    }

    public void setDotNumber(String dotNumber) {
        this.dotNumber = dotNumber;
    }

    public int getGeneralLiability() {
        return generalLiability;
    }

    public void setGeneralLiability(int generalLiability) {
        this.generalLiability = generalLiability;
    }

    public int getTruckLiability() {
        return truckLiability;
    }

    public void setTruckLiability(int truckLiability) {
        this.truckLiability = truckLiability;
    }

    public String getProofOfTruckLiability() {
        return proofOfTruckLiability;
    }

    public void setProofOfTruckLiability(String proofOfTruckLiability) {
        this.proofOfTruckLiability = proofOfTruckLiability;
    }

    public String getDirectDeposit() {
        return directDeposit;
    }

    public void setDirectDeposit(String directDeposit) {
        this.directDeposit = directDeposit;
    }

    public String getTimeSheet() {
        return timeSheet;
    }

    public void setTimeSheet(String timeSheet) {
        this.timeSheet = timeSheet;
    }

    public String getAutomatedBooking() {
        return automatedBooking;
    }

    public void setAutomatedBooking(String automatedBooking) {
        this.automatedBooking = automatedBooking;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobInstruction() {
        return jobInstruction;
    }

    public void setJobInstruction(String jobInstruction) {
        this.jobInstruction = jobInstruction;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
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

    public boolean getClose() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public int getJobCreationType() {
        return jobCreationType;
    }

    public void setJobCreationType(int jobCreationType) {
        this.jobCreationType = jobCreationType;
    }

    @XmlTransient
    public List<JobDocument> getJobDocumentList() {
        return jobDocumentList;
    }

    public void setJobDocumentList(List<JobDocument> jobDocumentList) {
        this.jobDocumentList = jobDocumentList;
    }

    @XmlTransient
    public List<Validation> getValidationList() {
        return validationList;
    }

    public void setValidationList(List<Validation> validationList) {
        this.validationList = validationList;
    }

    public DirtType getTypeofdirtID() {
        return typeofdirtID;
    }

    public void setTypeofdirtID(DirtType typeofdirtID) {
        this.typeofdirtID = typeofdirtID;
    }

    public LenghtOfBed getLenghtOfBed() {
        return lenghtOfBed;
    }

    public void setLenghtOfBed(LenghtOfBed lenghtOfBed) {
        this.lenghtOfBed = lenghtOfBed;
    }

    public TruckAxle getTruckAxle() {
        return truckAxle;
    }

    public void setTruckAxle(TruckAxle truckAxle) {
        this.truckAxle = truckAxle;
    }

    public PaymentType getPaymenttypeID() {
        return paymenttypeID;
    }

    public void setPaymenttypeID(PaymentType paymenttypeID) {
        this.paymenttypeID = paymenttypeID;
    }

    public User getExcavatorID() {
        return excavatorID;
    }

    public void setExcavatorID(User excavatorID) {
        this.excavatorID = excavatorID;
    }

    @XmlTransient
    public List<JobResponse> getJobResponseList() {
        return jobResponseList;
    }

    public void setJobResponseList(List<JobResponse> jobResponseList) {
        this.jobResponseList = jobResponseList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (jobID != null ? jobID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Job)) {
            return false;
        }
        Job other = (Job) object;
        if ((this.jobID == null && other.jobID != null) || (this.jobID != null && !this.jobID.equals(other.jobID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Job[ jobID=" + jobID + " ]";
    }

    public String getTimezoneID() {
        return timezoneID;
    }

    public void setTimezoneID(String timezoneID) {
        this.timezoneID = timezoneID;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
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

    @XmlTransient
    public List<JobNotification> getJobNotificationList() {
        return jobNotificationList;
    }

    public void setJobNotificationList(List<JobNotification> jobNotificationList) {
        this.jobNotificationList = jobNotificationList;
    }
    
}
