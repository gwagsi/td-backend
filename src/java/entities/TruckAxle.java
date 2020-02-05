/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author erman
 */
@Entity
@Table(name = "truck_axle")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TruckAxle.findAll", query = "SELECT t FROM TruckAxle t"),
    @NamedQuery(name = "TruckAxle.findByTruckaxleID", query = "SELECT t FROM TruckAxle t WHERE t.truckaxleID = :truckaxleID"),
    @NamedQuery(name = "TruckAxle.findByTruckAxleName", query = "SELECT t FROM TruckAxle t WHERE t.truckAxleName = :truckAxleName"),
    @NamedQuery(name = "TruckAxle.findBySortField", query = "SELECT t FROM TruckAxle t WHERE t.sortField = :sortField"),
    @NamedQuery(name = "TruckAxle.findByDescription", query = "SELECT t FROM TruckAxle t WHERE t.description = :description")})
public class TruckAxle implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "truck_axle_ID")
    private Integer truckaxleID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "truck_axle_name")
    private String truckAxleName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sort_field")
    private int sortField;
    @Size(max = 100)
    @Column(name = "description")
    private String description;
    @OneToMany(mappedBy = "truckaxleID", fetch = FetchType.LAZY)
    private List<Truck> truckList;
    @OneToMany(mappedBy = "truckAxle", fetch = FetchType.LAZY)
    private List<Job> jobList;

    public TruckAxle() {
    }

    public TruckAxle(Integer truckaxleID) {
        this.truckaxleID = truckaxleID;
    }

    public TruckAxle(Integer truckaxleID, String truckAxleName, int sortField) {
        this.truckaxleID = truckaxleID;
        this.truckAxleName = truckAxleName;
        this.sortField = sortField;
    }

    public Integer getTruckaxleID() {
        return truckaxleID;
    }

    public void setTruckaxleID(Integer truckaxleID) {
        this.truckaxleID = truckaxleID;
    }

    public String getTruckAxleName() {
        return truckAxleName;
    }

    public void setTruckAxleName(String truckAxleName) {
        this.truckAxleName = truckAxleName;
    }

    public int getSortField() {
        return sortField;
    }

    public void setSortField(int sortField) {
        this.sortField = sortField;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public List<Truck> getTruckList() {
        return truckList;
    }

    public void setTruckList(List<Truck> truckList) {
        this.truckList = truckList;
    }

    @XmlTransient
    public List<Job> getJobList() {
        return jobList;
    }

    public void setJobList(List<Job> jobList) {
        this.jobList = jobList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (truckaxleID != null ? truckaxleID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TruckAxle)) {
            return false;
        }
        TruckAxle other = (TruckAxle) object;
        if ((this.truckaxleID == null && other.truckaxleID != null) || (this.truckaxleID != null && !this.truckaxleID.equals(other.truckaxleID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.TruckAxle[ truckaxleID=" + truckaxleID + " ]";
    }
    
}
