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
 * @author erman
 */
@Entity
@Table(name = "user_notification_params")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserNotificationParams.findAll", query = "SELECT u FROM UserNotificationParams u"),
    @NamedQuery(name = "UserNotificationParams.findByUsernotificationparamsID", query = "SELECT u FROM UserNotificationParams u WHERE u.usernotificationparamsID = :usernotificationparamsID"),
    @NamedQuery(name = "UserNotificationParams.findByEmail", query = "SELECT u FROM UserNotificationParams u WHERE u.email = :email"),
    @NamedQuery(name = "UserNotificationParams.findByLatitude", query = "SELECT u FROM UserNotificationParams u WHERE u.latitude = :latitude"),
    @NamedQuery(name = "UserNotificationParams.findByLongitude", query = "SELECT u FROM UserNotificationParams u WHERE u.longitude = :longitude"),
    @NamedQuery(name = "UserNotificationParams.findByZipCode", query = "SELECT u FROM UserNotificationParams u WHERE u.zipCode = :zipCode"),
    @NamedQuery(name = "UserNotificationParams.findByCreationDate", query = "SELECT u FROM UserNotificationParams u WHERE u.creationDate = :creationDate"),
    @NamedQuery(name = "UserNotificationParams.findByDeleted", query = "SELECT u FROM UserNotificationParams u WHERE u.deleted = :deleted")})
public class UserNotificationParams implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "raduis")
    private float raduis;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "user_notification_params_ID")
    private Integer usernotificationparamsID;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Column(name = "latitude")
    private float latitude;
    @Basic(optional = false)
    @NotNull
    @Column(name = "longitude")
    private float longitude;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "zip_code")
    private String zipCode;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "deleted")
    private boolean deleted;
    @JoinColumn(name = "user_ID", referencedColumnName = "user_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User userID;

    public UserNotificationParams() {
    }

    public UserNotificationParams(Integer usernotificationparamsID) {
        this.usernotificationparamsID = usernotificationparamsID;
    }

    public UserNotificationParams(Integer usernotificationparamsID, String email, float latitude, float longitude, String zipCode, Date creationDate, boolean deleted) {
        this.usernotificationparamsID = usernotificationparamsID;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
        this.zipCode = zipCode;
        this.creationDate = creationDate;
        this.deleted = deleted;
    }

    public Integer getUsernotificationparamsID() {
        return usernotificationparamsID;
    }

    public void setUsernotificationparamsID(Integer usernotificationparamsID) {
        this.usernotificationparamsID = usernotificationparamsID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
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

    public User getUserID() {
        return userID;
    }

    public void setUserID(User userID) {
        this.userID = userID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usernotificationparamsID != null ? usernotificationparamsID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserNotificationParams)) {
            return false;
        }
        UserNotificationParams other = (UserNotificationParams) object;
        if ((this.usernotificationparamsID == null && other.usernotificationparamsID != null) || (this.usernotificationparamsID != null && !this.usernotificationparamsID.equals(other.usernotificationparamsID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.UserNotificationParams[ usernotificationparamsID=" + usernotificationparamsID + " ]";
    }

    public float getRaduis() {
        return raduis;
    }

    public void setRaduis(float raduis) {
        this.raduis = raduis;
    }
    
}
