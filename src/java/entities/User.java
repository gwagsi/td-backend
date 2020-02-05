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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name = "user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findByUserID", query = "SELECT u FROM User u WHERE u.userID = :userID"),
    @NamedQuery(name = "User.findByVersion", query = "SELECT u FROM User u WHERE u.version = :version"),
    @NamedQuery(name = "User.findByName", query = "SELECT u FROM User u WHERE u.name = :name"),
    @NamedQuery(name = "User.findBySurname", query = "SELECT u FROM User u WHERE u.surname = :surname"),
    @NamedQuery(name = "User.findByAddress", query = "SELECT u FROM User u WHERE u.address = :address"),
    @NamedQuery(name = "User.findByUserCode", query = "SELECT u FROM User u WHERE u.userCode = :userCode"),
    @NamedQuery(name = "User.findByCompagnyName", query = "SELECT u FROM User u WHERE u.compagnyName = :compagnyName"),
    @NamedQuery(name = "User.findByTelephone", query = "SELECT u FROM User u WHERE u.telephone = :telephone"),
    @NamedQuery(name = "User.findByCellPhone", query = "SELECT u FROM User u WHERE u.cellPhone = :cellPhone"),
    @NamedQuery(name = "User.findByGpsCoordinate", query = "SELECT u FROM User u WHERE u.gpsCoordinate = :gpsCoordinate"),
    @NamedQuery(name = "User.findByCreationDate", query = "SELECT u FROM User u WHERE u.creationDate = :creationDate"),
    @NamedQuery(name = "User.findByDeleted", query = "SELECT u FROM User u WHERE u.deleted = :deleted"),
    @NamedQuery(name = "User.findByJobNumber", query = "SELECT u FROM User u WHERE u.jobNumber = :jobNumber"),
    @NamedQuery(name = "User.findByTruckNumber", query = "SELECT u FROM User u WHERE u.truckNumber = :truckNumber"),
    @NamedQuery(name = "User.findByBasketNumber", query = "SELECT u FROM User u WHERE u.basketNumber = :basketNumber"),
    @NamedQuery(name = "User.findByRate", query = "SELECT u FROM User u WHERE u.rate = :rate"),
    @NamedQuery(name = "User.findByNumberOfReview", query = "SELECT u FROM User u WHERE u.numberOfReview = :numberOfReview"),
    @NamedQuery(name = "User.findByCardNumber", query = "SELECT u FROM User u WHERE u.cardNumber = :cardNumber"),
    @NamedQuery(name = "User.findByRoutineNumber", query = "SELECT u FROM User u WHERE u.routineNumber = :routineNumber"),
    @NamedQuery(name = "User.findByBankName", query = "SELECT u FROM User u WHERE u.bankName = :bankName"),
    @NamedQuery(name = "User.findByAccountNumber", query = "SELECT u FROM User u WHERE u.accountNumber = :accountNumber"),
    @NamedQuery(name = "User.findByAccountName", query = "SELECT u FROM User u WHERE u.accountName = :accountName")})
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "user_ID")
    private Integer userID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "version")
    @Version
    private int version;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "name")
    private String name;
    @Size(max = 100)
    @Column(name = "surname")
    private String surname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "address")
    private String address;
    @Size(max = 100)
    @Column(name = "user_code")
    private String userCode;
    @Size(max = 100)
    @Column(name = "compagny_name")
    private String compagnyName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "telephone")
    private String telephone;
    @Size(max = 100)
    @Column(name = "cell_phone")
    private String cellPhone;
    @Size(max = 100)
    @Column(name = "gps_coordinate")
    private String gpsCoordinate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "deleted")
    private boolean deleted;
    @Column(name = "job_number")
    private Integer jobNumber;
    @Column(name = "truck_number")
    private Integer truckNumber;
    @Basic(optional = false)
    @NotNull
    @Column(name = "basket_number")
    private int basketNumber;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rate")
    private float rate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "number_of_review")
    private int numberOfReview;
    @Size(max = 100)
    @Column(name = "card_number")
    private String cardNumber;
    @Size(max = 100)
    @Column(name = "routine_number")
    private String routineNumber;
    @Size(max = 100)
    @Column(name = "bank_name")
    private String bankName;
    @Size(max = 100)
    @Column(name = "account_number")
    private String accountNumber;
    @Size(max = 100)
    @Column(name = "account_name")
    private String accountName;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userID", fetch = FetchType.LAZY)
    private List<DirtyBooking> dirtyBookingList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userID", fetch = FetchType.LAZY)
    private List<Truck> truckList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userID", fetch = FetchType.LAZY)
    private List<Document> documentList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userID", fetch = FetchType.LAZY)
    private List<Drive> driveList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userID", fetch = FetchType.LAZY)
    private List<Dirty> dirtyList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userID", fetch = FetchType.LAZY)
    private List<Driver> driverList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "excavatorID", fetch = FetchType.LAZY)
    private List<Employee> employeeList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clientID", fetch = FetchType.LAZY)
    private List<TruckBooking> truckBookingList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "truckownerID", fetch = FetchType.LAZY)
    private List<TruckBooking> truckBookingList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "truckownerID", fetch = FetchType.LAZY)
    private List<Availability> availabilityList;
    @OneToMany(mappedBy = "excavatorID", fetch = FetchType.LAZY)
    private List<Job> jobList;
    @OneToMany(mappedBy = "truckownerID", fetch = FetchType.LAZY)
    private List<JobResponse> jobResponseList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userID", fetch = FetchType.LAZY)
    private List<UserNotificationParams> userNotificationParamsList;
    @JoinColumn(name = "account_ID", referencedColumnName = "account_ID")
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Account accountID;

    public User() {
    }

    public User(Integer userID) {
        this.userID = userID;
    }

    public User(Integer userID, String name, String address, String telephone, Date creationDate, boolean deleted, int basketNumber, float rate, int numberOfReview) {
        this.userID = userID;
        this.name = name;
        this.address = address;
        this.telephone = telephone;
        this.creationDate = creationDate;
        this.deleted = deleted;
        this.basketNumber = basketNumber;
        this.rate = rate;
        this.numberOfReview = numberOfReview;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getCompagnyName() {
        return compagnyName;
    }

    public void setCompagnyName(String compagnyName) {
        this.compagnyName = compagnyName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getGpsCoordinate() {
        return gpsCoordinate;
    }

    public void setGpsCoordinate(String gpsCoordinate) {
        this.gpsCoordinate = gpsCoordinate;
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

    public Integer getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(Integer jobNumber) {
        this.jobNumber = jobNumber;
    }

    public Integer getTruckNumber() {
        return truckNumber;
    }

    public void setTruckNumber(Integer truckNumber) {
        this.truckNumber = truckNumber;
    }

    public int getBasketNumber() {
        return basketNumber;
    }

    public void setBasketNumber(int basketNumber) {
        this.basketNumber = basketNumber;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public int getNumberOfReview() {
        return numberOfReview;
    }

    public void setNumberOfReview(int numberOfReview) {
        this.numberOfReview = numberOfReview;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getRoutineNumber() {
        return routineNumber;
    }

    public void setRoutineNumber(String routineNumber) {
        this.routineNumber = routineNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    @XmlTransient
    public List<DirtyBooking> getDirtyBookingList() {
        return dirtyBookingList;
    }

    public void setDirtyBookingList(List<DirtyBooking> dirtyBookingList) {
        this.dirtyBookingList = dirtyBookingList;
    }

    @XmlTransient
    public List<Truck> getTruckList() {
        return truckList;
    }

    public void setTruckList(List<Truck> truckList) {
        this.truckList = truckList;
    }

    @XmlTransient
    public List<Document> getDocumentList() {
        return documentList;
    }

    public void setDocumentList(List<Document> documentList) {
        this.documentList = documentList;
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
    public List<Driver> getDriverList() {
        return driverList;
    }

    public void setDriverList(List<Driver> driverList) {
        this.driverList = driverList;
    }

    @XmlTransient
    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    @XmlTransient
    public List<TruckBooking> getTruckBookingList() {
        return truckBookingList;
    }

    public void setTruckBookingList(List<TruckBooking> truckBookingList) {
        this.truckBookingList = truckBookingList;
    }

    @XmlTransient
    public List<TruckBooking> getTruckBookingList1() {
        return truckBookingList1;
    }

    public void setTruckBookingList1(List<TruckBooking> truckBookingList1) {
        this.truckBookingList1 = truckBookingList1;
    }

    @XmlTransient
    public List<Availability> getAvailabilityList() {
        return availabilityList;
    }

    public void setAvailabilityList(List<Availability> availabilityList) {
        this.availabilityList = availabilityList;
    }

    @XmlTransient
    public List<Job> getJobList() {
        return jobList;
    }

    public void setJobList(List<Job> jobList) {
        this.jobList = jobList;
    }

    @XmlTransient
    public List<JobResponse> getJobResponseList() {
        return jobResponseList;
    }

    public void setJobResponseList(List<JobResponse> jobResponseList) {
        this.jobResponseList = jobResponseList;
    }

    @XmlTransient
    public List<UserNotificationParams> getUserNotificationParamsList() {
        return userNotificationParamsList;
    }

    public void setUserNotificationParamsList(List<UserNotificationParams> userNotificationParamsList) {
        this.userNotificationParamsList = userNotificationParamsList;
    }

    public Account getAccountID() {
        return accountID;
    }

    public void setAccountID(Account accountID) {
        this.accountID = accountID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userID != null ? userID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.userID == null && other.userID != null) || (this.userID != null && !this.userID.equals(other.userID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.User[ userID=" + userID + " ]";
    }
    
}
