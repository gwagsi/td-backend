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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author erman
 */
@Entity
@Table(name = "dirty")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dirty.findAll", query = "SELECT d FROM Dirty d"),
    @NamedQuery(name = "Dirty.findByDirtyID", query = "SELECT d FROM Dirty d WHERE d.dirtyID = :dirtyID"),
    @NamedQuery(name = "Dirty.findByLocation", query = "SELECT d FROM Dirty d WHERE d.location = :location"),
    @NamedQuery(name = "Dirty.findByDescription", query = "SELECT d FROM Dirty d WHERE d.description = :description"),
    @NamedQuery(name = "Dirty.findByCreationDate", query = "SELECT d FROM Dirty d WHERE d.creationDate = :creationDate"),
    @NamedQuery(name = "Dirty.findByType", query = "SELECT d FROM Dirty d WHERE d.type = :type"),
    @NamedQuery(name = "Dirty.findByQuantity", query = "SELECT d FROM Dirty d WHERE d.quantity = :quantity"),
    @NamedQuery(name = "Dirty.findByLogistic", query = "SELECT d FROM Dirty d WHERE d.logistic = :logistic")})
public class Dirty implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dirty_ID")
    private Integer dirtyID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "location")
    private String location;
    @Size(max = 100)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Size(max = 100)
    @Column(name = "type")
    private String type;
    @Size(max = 100)
    @Column(name = "quantity")
    private String quantity;
    @Size(max = 100)
    @Column(name = "logistic")
    private String logistic;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dirtyID", fetch = FetchType.LAZY)
    private List<DirtyBooking> dirtyBookingList;
    @JoinColumn(name = "truck_ID", referencedColumnName = "truck_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Truck truckID;
    @JoinColumn(name = "user_ID", referencedColumnName = "user_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User userID;

    public Dirty() {
    }

    public Dirty(Integer dirtyID) {
        this.dirtyID = dirtyID;
    }

    public Dirty(Integer dirtyID, String location, Date creationDate) {
        this.dirtyID = dirtyID;
        this.location = location;
        this.creationDate = creationDate;
    }

    public Integer getDirtyID() {
        return dirtyID;
    }

    public void setDirtyID(Integer dirtyID) {
        this.dirtyID = dirtyID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getLogistic() {
        return logistic;
    }

    public void setLogistic(String logistic) {
        this.logistic = logistic;
    }

    @XmlTransient
    public List<DirtyBooking> getDirtyBookingList() {
        return dirtyBookingList;
    }

    public void setDirtyBookingList(List<DirtyBooking> dirtyBookingList) {
        this.dirtyBookingList = dirtyBookingList;
    }

    public Truck getTruckID() {
        return truckID;
    }

    public void setTruckID(Truck truckID) {
        this.truckID = truckID;
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
        hash += (dirtyID != null ? dirtyID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dirty)) {
            return false;
        }
        Dirty other = (Dirty) object;
        if ((this.dirtyID == null && other.dirtyID != null) || (this.dirtyID != null && !this.dirtyID.equals(other.dirtyID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Dirty[ dirtyID=" + dirtyID + " ]";
    }
    
}
