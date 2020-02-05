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
import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
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
@Table(name = "truck")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Truck.findAll", query = "SELECT t FROM Truck t"),
    @NamedQuery(name = "Truck.findByTruckID", query = "SELECT t FROM Truck t WHERE t.truckID = :truckID"),
    @NamedQuery(name = "Truck.findByAvailable", query = "SELECT t FROM Truck t WHERE t.available = :available"),
    @NamedQuery(name = "Truck.findByTruckNumber", query = "SELECT t FROM Truck t WHERE t.truckNumber = :truckNumber"),
    @NamedQuery(name = "Truck.findByTruckDescription", query = "SELECT t FROM Truck t WHERE t.truckDescription = :truckDescription"),
    @NamedQuery(name = "Truck.findByYear", query = "SELECT t FROM Truck t WHERE t.year = :year"),
    @NamedQuery(name = "Truck.findByLocationPrice", query = "SELECT t FROM Truck t WHERE t.locationPrice = :locationPrice"),
    @NamedQuery(name = "Truck.findByDeleted", query = "SELECT t FROM Truck t WHERE t.deleted = :deleted"),
    @NamedQuery(name = "Truck.findByRate", query = "SELECT t FROM Truck t WHERE t.rate = :rate"),
    @NamedQuery(name = "Truck.findByNumberOfRate", query = "SELECT t FROM Truck t WHERE t.numberOfRate = :numberOfRate"),
    @NamedQuery(name = "Truck.findByCreationDate", query = "SELECT t FROM Truck t WHERE t.creationDate = :creationDate"),
    @NamedQuery(name = "Truck.findByPhoneNumber", query = "SELECT t FROM Truck t WHERE t.phoneNumber = :phoneNumber"),
    @NamedQuery(name = "Truck.findByDistance", query = "SELECT t FROM Truck t WHERE t.distance = :distance"),
    @NamedQuery(name = "Truck.findByTruckZipCode", query = "SELECT t FROM Truck t WHERE t.truckZipCode = :truckZipCode"),
    @NamedQuery(name = "Truck.findByLatitude", query = "SELECT t FROM Truck t WHERE t.latitude = :latitude"),
    @NamedQuery(name = "Truck.findByLongitude", query = "SELECT t FROM Truck t WHERE t.longitude = :longitude"),
    @NamedQuery(name = "Truck.findByDOTNumber", query = "SELECT t FROM Truck t WHERE t.dOTNumber = :dOTNumber"),
    @NamedQuery(name = "Truck.findByGeneralLiability", query = "SELECT t FROM Truck t WHERE t.generalLiability = :generalLiability"),
    @NamedQuery(name = "Truck.findByInsuranceLiability", query = "SELECT t FROM Truck t WHERE t.insuranceLiability = :insuranceLiability")})

@SqlResultSetMapping(name = "TruckSearchResults",
        entities = {
            @EntityResult(entityClass = Truck.class)},
        columns = {
            @ColumnResult(name = "distanceWithin")}
)
public class Truck implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "version")
    @Version
    private int version;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "truck_ID")
    private Integer truckID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "available")
    private boolean available;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "truck_number")
    private String truckNumber;
    @Size(max = 500)
    @Column(name = "truck_description")
    private String truckDescription;
    @Size(max = 4)
    @Column(name = "year")
    private String year;
    @Basic(optional = false)
    @NotNull
    @Column(name = "location_price")
    private float locationPrice;
    @Basic(optional = false)
    @NotNull
    @Column(name = "deleted")
    private boolean deleted;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rate")
    private float rate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "number_of_rate")
    private int numberOfRate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Size(max = 100)
    @Column(name = "phone_number")
    private String phoneNumber;
    @Basic(optional = false)
    @NotNull
    @Column(name = "distance")
    private int distance;
    @Size(max = 100)
    @Column(name = "truck_zip_code")
    private String truckZipCode;
    @Basic(optional = false)
    @NotNull
    @Column(name = "latitude")
    private float latitude;
    @Basic(optional = false)
    @NotNull
    @Column(name = "longitude")
    private float longitude;
    @Size(max = 100)
    @Column(name = "d_o_t_number")
    private String dOTNumber;
    @Basic(optional = false)
    @NotNull
    @Column(name = "general_liability")
    private int generalLiability;
    @Basic(optional = false)
    @NotNull
    @Column(name = "insurance_liability")
    private int insuranceLiability;
    @JoinColumn(name = "picture_insurance", referencedColumnName = "document_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Document pictureInsurance;
    @JoinColumn(name = "picture", referencedColumnName = "document_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Document picture;
    @JoinColumn(name = "lenght_of_bed_ID", referencedColumnName = "lenght_of_bed_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private LenghtOfBed lenghtofbedID;
    @JoinColumn(name = "truck_axle_ID", referencedColumnName = "truck_axle_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private TruckAxle truckaxleID;
    @JoinColumn(name = "truck_type_ID", referencedColumnName = "truck_type_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TruckType trucktypeID;
    @JoinColumn(name = "user_ID", referencedColumnName = "user_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User userID;
    @OneToMany(mappedBy = "truckID", fetch = FetchType.LAZY)
    private List<WeeklyTicket> weeklyTicketList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "truckID", fetch = FetchType.LAZY)
    private List<Drive> driveList;
    @OneToMany(mappedBy = "truckID", fetch = FetchType.LAZY)
    private List<Dirty> dirtyList;
    @OneToMany(mappedBy = "truckID", fetch = FetchType.LAZY)
    private List<DailyTicket> dailyTicketList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "truckID", fetch = FetchType.LAZY)
    private List<TruckBooking> truckBookingList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "truckID", fetch = FetchType.LAZY)
    private List<Availability> availabilityList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "truckID", fetch = FetchType.LAZY)
    private List<TruckDocument> truckDocumentList;
    @OneToMany(mappedBy = "truckID", fetch = FetchType.LAZY)
    private List<JobLog> jobLogList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "truckID", fetch = FetchType.LAZY)
    private List<SolicitedTruck> solicitedTruckList;

    public Truck() {
    }

    public Truck(Integer truckID) {
        this.truckID = truckID;
    }

    public Truck(Integer truckID, boolean available, String truckNumber, float locationPrice, boolean deleted, float rate, int numberOfRate, Date creationDate, int distance, float latitude, float longitude, int generalLiability, int insuranceLiability) {
        this.truckID = truckID;
        this.available = available;
        this.truckNumber = truckNumber;
        this.locationPrice = locationPrice;
        this.deleted = deleted;
        this.rate = rate;
        this.numberOfRate = numberOfRate;
        this.creationDate = creationDate;
        this.distance = distance;
        this.latitude = latitude;
        this.longitude = longitude;
        this.generalLiability = generalLiability;
        this.insuranceLiability = insuranceLiability;
    }

    public Integer getTruckID() {
        return truckID;
    }

    public void setTruckID(Integer truckID) {
        this.truckID = truckID;
    }

    public boolean getAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getTruckNumber() {
        return truckNumber;
    }

    public void setTruckNumber(String truckNumber) {
        this.truckNumber = truckNumber;
    }

    public String getTruckDescription() {
        return truckDescription;
    }

    public void setTruckDescription(String truckDescription) {
        this.truckDescription = truckDescription;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public float getLocationPrice() {
        return locationPrice;
    }

    public void setLocationPrice(float locationPrice) {
        this.locationPrice = locationPrice;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public int getNumberOfRate() {
        return numberOfRate;
    }

    public void setNumberOfRate(int numberOfRate) {
        this.numberOfRate = numberOfRate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getTruckZipCode() {
        return truckZipCode;
    }

    public void setTruckZipCode(String truckZipCode) {
        this.truckZipCode = truckZipCode;
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

    public String getDOTNumber() {
        return dOTNumber;
    }

    public void setDOTNumber(String dOTNumber) {
        this.dOTNumber = dOTNumber;
    }

    public int getGeneralLiability() {
        return generalLiability;
    }

    public void setGeneralLiability(int generalLiability) {
        this.generalLiability = generalLiability;
    }

    public int getInsuranceLiability() {
        return insuranceLiability;
    }

    public void setInsuranceLiability(int insuranceLiability) {
        this.insuranceLiability = insuranceLiability;
    }

    public Document getPictureInsurance() {
        return pictureInsurance;
    }

    public void setPictureInsurance(Document pictureInsurance) {
        this.pictureInsurance = pictureInsurance;
    }

    public Document getPicture() {
        return picture;
    }

    public void setPicture(Document picture) {
        this.picture = picture;
    }

    public LenghtOfBed getLenghtofbedID() {
        return lenghtofbedID;
    }

    public void setLenghtofbedID(LenghtOfBed lenghtofbedID) {
        this.lenghtofbedID = lenghtofbedID;
    }

    public TruckAxle getTruckaxleID() {
        return truckaxleID;
    }

    public void setTruckaxleID(TruckAxle truckaxleID) {
        this.truckaxleID = truckaxleID;
    }

    public TruckType getTrucktypeID() {
        return trucktypeID;
    }

    public void setTrucktypeID(TruckType trucktypeID) {
        this.trucktypeID = trucktypeID;
    }

    public User getUserID() {
        return userID;
    }

    public void setUserID(User userID) {
        this.userID = userID;
    }

    @XmlTransient
    public List<WeeklyTicket> getWeeklyTicketList() {
        return weeklyTicketList;
    }

    public void setWeeklyTicketList(List<WeeklyTicket> weeklyTicketList) {
        this.weeklyTicketList = weeklyTicketList;
    }

    @XmlTransient
    public List<Drive> getDriveList() {
        return driveList;
    }

    public void setDriveList(List<Drive> driveList) {
        this.driveList = driveList;
    }

    @XmlTransient
    public List<Dirty> getDirtyList() {
        return dirtyList;
    }

    public void setDirtyList(List<Dirty> dirtyList) {
        this.dirtyList = dirtyList;
    }

    @XmlTransient
    public List<DailyTicket> getDailyTicketList() {
        return dailyTicketList;
    }

    public void setDailyTicketList(List<DailyTicket> dailyTicketList) {
        this.dailyTicketList = dailyTicketList;
    }

    @XmlTransient
    public List<TruckBooking> getTruckBookingList() {
        return truckBookingList;
    }

    public void setTruckBookingList(List<TruckBooking> truckBookingList) {
        this.truckBookingList = truckBookingList;
    }

    @XmlTransient
    public List<Availability> getAvailabilityList() {
        return availabilityList;
    }

    public void setAvailabilityList(List<Availability> availabilityList) {
        this.availabilityList = availabilityList;
    }

    @XmlTransient
    public List<TruckDocument> getTruckDocumentList() {
        return truckDocumentList;
    }

    public void setTruckDocumentList(List<TruckDocument> truckDocumentList) {
        this.truckDocumentList = truckDocumentList;
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
        hash += (truckID != null ? truckID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Truck)) {
            return false;
        }
        Truck other = (Truck) object;
        if ((this.truckID == null && other.truckID != null) || (this.truckID != null && !this.truckID.equals(other.truckID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Truck[ truckID=" + truckID + " ]";
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
    
}
