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
import javax.persistence.ManyToOne;
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
@Table(name = "driver")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Driver.findAll", query = "SELECT d FROM Driver d"),
    @NamedQuery(name = "Driver.findByDriverID", query = "SELECT d FROM Driver d WHERE d.driverID = :driverID"),
    @NamedQuery(name = "Driver.findByName", query = "SELECT d FROM Driver d WHERE d.name = :name"),
    @NamedQuery(name = "Driver.findBySurname", query = "SELECT d FROM Driver d WHERE d.surname = :surname"),
    @NamedQuery(name = "Driver.findByPicture", query = "SELECT d FROM Driver d WHERE d.picture = :picture"),
    @NamedQuery(name = "Driver.findByAddress", query = "SELECT d FROM Driver d WHERE d.address = :address"),
    @NamedQuery(name = "Driver.findByIdCardNumber", query = "SELECT d FROM Driver d WHERE d.idCardNumber = :idCardNumber"),
    @NamedQuery(name = "Driver.findByTelephone", query = "SELECT d FROM Driver d WHERE d.telephone = :telephone"),
    @NamedQuery(name = "Driver.findByEmail", query = "SELECT d FROM Driver d WHERE d.email = :email"),
    @NamedQuery(name = "Driver.findByLicense", query = "SELECT d FROM Driver d WHERE d.license = :license"),
    @NamedQuery(name = "Driver.findByDeleted", query = "SELECT d FROM Driver d WHERE d.deleted = :deleted"),
    @NamedQuery(name = "Driver.findByCreationDate", query = "SELECT d FROM Driver d WHERE d.creationDate = :creationDate")})
public class Driver implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "driver_ID")
    private Integer driverID;
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
    @Size(max = 100)
    @Column(name = "picture")
    private String picture;
    @Size(max = 100)
    @Column(name = "address")
    private String address;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "id_card_number")
    private String idCardNumber;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "telephone")
    private String telephone;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "email")
    private String email;
    @Size(max = 100)
    @Column(name = "license")
    private String license;
    @Basic(optional = false)
    @NotNull
    @Column(name = "deleted")
    private boolean deleted;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "driverID", fetch = FetchType.LAZY)
    private List<DriverDocument> driverDocumentList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "driverID", fetch = FetchType.LAZY)
    private List<Drive> driveList;
    @JoinColumn(name = "account_ID", referencedColumnName = "account_ID")
    @OneToOne(fetch = FetchType.LAZY)
    private Account accountID;
    @JoinColumn(name = "user_ID", referencedColumnName = "user_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User userID;

    public Driver() {
    }

    public Driver(Integer driverID) {
        this.driverID = driverID;
    }

    public Driver(Integer driverID, String name, String idCardNumber, String telephone, String email, boolean deleted, Date creationDate) {
        this.driverID = driverID;
        this.name = name;
        this.idCardNumber = idCardNumber;
        this.telephone = telephone;
        this.email = email;
        this.deleted = deleted;
        this.creationDate = creationDate;
    }

    public Integer getDriverID() {
        return driverID;
    }

    public void setDriverID(Integer driverID) {
        this.driverID = driverID;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @XmlTransient
    public List<DriverDocument> getDriverDocumentList() {
        return driverDocumentList;
    }

    public void setDriverDocumentList(List<DriverDocument> driverDocumentList) {
        this.driverDocumentList = driverDocumentList;
    }

    @XmlTransient
    public List<Drive> getDriveList() {
        return driveList;
    }

    public void setDriveList(List<Drive> driveList) {
        this.driveList = driveList;
    }

    
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
    
    public Account getAccountID() {
        return accountID;
    }

    public void setAccountID(Account accountID) {
        this.accountID = accountID;
    }

    public User getUserID() {
        return userID;
    }

    public void setUserID(User userID) {
        this.userID = userID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (driverID != null ? driverID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Driver)) {
            return false;
        }
        Driver other = (Driver) object;
        if ((this.driverID == null && other.driverID != null) || (this.driverID != null && !this.driverID.equals(other.driverID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Driver[ driverID=" + driverID + " ]";
    }
    
}
