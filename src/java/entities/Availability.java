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
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author erman
 */
@Entity
@Table(name = "availability")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Availability.findAll", query = "SELECT a FROM Availability a"),
    @NamedQuery(name = "Availability.findByAvailabilityID", query = "SELECT a FROM Availability a WHERE a.availabilityID = :availabilityID"),
    @NamedQuery(name = "Availability.findByAvailbilityDate", query = "SELECT a FROM Availability a WHERE a.availbilityDate = :availbilityDate"),
    @NamedQuery(name = "Availability.findByState", query = "SELECT a FROM Availability a WHERE a.state = :state"),
    @NamedQuery(name = "Availability.findByDeleted", query = "SELECT a FROM Availability a WHERE a.deleted = :deleted"),
    @NamedQuery(name = "Availability.findByCreationDate", query = "SELECT a FROM Availability a WHERE a.creationDate = :creationDate")})
public class Availability implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "version")
    @Version
    private int version;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "availability_ID")
    private Integer availabilityID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "availbility_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date availbilityDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "state")
    private int state;
    @Basic(optional = false)
    @NotNull
    @Column(name = "deleted")
    private boolean deleted;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @JoinColumn(name = "truck_owner_ID", referencedColumnName = "user_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User truckownerID;
    @JoinColumn(name = "truck_ID", referencedColumnName = "truck_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Truck truckID;

    public Availability() {
    }

    public Availability(Integer availabilityID) {
        this.availabilityID = availabilityID;
    }

    public Availability(Integer availabilityID, Date availbilityDate, int state, boolean deleted, Date creationDate) {
        this.availabilityID = availabilityID;
        this.availbilityDate = availbilityDate;
        this.state = state;
        this.deleted = deleted;
        this.creationDate = creationDate;
    }

    public Integer getAvailabilityID() {
        return availabilityID;
    }

    public void setAvailabilityID(Integer availabilityID) {
        this.availabilityID = availabilityID;
    }

    public Date getAvailbilityDate() {
        return availbilityDate;
    }

    public void setAvailbilityDate(Date availbilityDate) {
        this.availbilityDate = availbilityDate;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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

    public User getTruckownerID() {
        return truckownerID;
    }

    public void setTruckownerID(User truckownerID) {
        this.truckownerID = truckownerID;
    }

    public Truck getTruckID() {
        return truckID;
    }

    public void setTruckID(Truck truckID) {
        this.truckID = truckID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (availabilityID != null ? availabilityID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Availability)) {
            return false;
        }
        Availability other = (Availability) object;
        if ((this.availabilityID == null && other.availabilityID != null) || (this.availabilityID != null && !this.availabilityID.equals(other.availabilityID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Availability[ availabilityID=" + availabilityID + " ]";
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
    
}
