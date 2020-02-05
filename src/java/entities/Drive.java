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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author erman
 */
@Entity
@Table(name = "drive")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Drive.findAll", query = "SELECT d FROM Drive d"),
    @NamedQuery(name = "Drive.findByDriveID", query = "SELECT d FROM Drive d WHERE d.driveID = :driveID"),
    @NamedQuery(name = "Drive.findByDrivingDate", query = "SELECT d FROM Drive d WHERE d.drivingDate = :drivingDate"),
    @NamedQuery(name = "Drive.findByActif", query = "SELECT d FROM Drive d WHERE d.actif = :actif"),
    @NamedQuery(name = "Drive.findByDeleted", query = "SELECT d FROM Drive d WHERE d.deleted = :deleted")})
public class Drive implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "drive_ID")
    private Integer driveID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "driving_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date drivingDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "actif")
    private boolean actif;
    @Basic(optional = false)
    @NotNull
    @Column(name = "deleted")
    private boolean deleted;
    @JoinColumn(name = "user_ID", referencedColumnName = "user_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User userID;
    @JoinColumn(name = "truck_ID", referencedColumnName = "truck_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Truck truckID;
    @JoinColumn(name = "driver_ID", referencedColumnName = "driver_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Driver driverID;

    public Drive() {
    }

    public Drive(Integer driveID) {
        this.driveID = driveID;
    }

    public Drive(Integer driveID, Date drivingDate, boolean actif, boolean deleted) {
        this.driveID = driveID;
        this.drivingDate = drivingDate;
        this.actif = actif;
        this.deleted = deleted;
    }

    public Integer getDriveID() {
        return driveID;
    }

    public void setDriveID(Integer driveID) {
        this.driveID = driveID;
    }

    public Date getDrivingDate() {
        return drivingDate;
    }

    public void setDrivingDate(Date drivingDate) {
        this.drivingDate = drivingDate;
    }

    public boolean getActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
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

    public Truck getTruckID() {
        return truckID;
    }

    public void setTruckID(Truck truckID) {
        this.truckID = truckID;
    }

    public Driver getDriverID() {
        return driverID;
    }

    public void setDriverID(Driver driverID) {
        this.driverID = driverID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (driveID != null ? driveID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Drive)) {
            return false;
        }
        Drive other = (Drive) object;
        if ((this.driveID == null && other.driveID != null) || (this.driveID != null && !this.driveID.equals(other.driveID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Drive[ driveID=" + driveID + " ]";
    }
    
}
