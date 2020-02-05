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
@Table(name = "lenght_of_bed")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LenghtOfBed.findAll", query = "SELECT l FROM LenghtOfBed l"),
    @NamedQuery(name = "LenghtOfBed.findByLenghtofbedID", query = "SELECT l FROM LenghtOfBed l WHERE l.lenghtofbedID = :lenghtofbedID"),
    @NamedQuery(name = "LenghtOfBed.findByName", query = "SELECT l FROM LenghtOfBed l WHERE l.name = :name"),
    @NamedQuery(name = "LenghtOfBed.findBySortField", query = "SELECT l FROM LenghtOfBed l WHERE l.sortField = :sortField"),
    @NamedQuery(name = "LenghtOfBed.findByDescription", query = "SELECT l FROM LenghtOfBed l WHERE l.description = :description")})
public class LenghtOfBed implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "lenght_of_bed_ID")
    private Integer lenghtofbedID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sort_field")
    private int sortField;
    @Size(max = 100)
    @Column(name = "description")
    private String description;
    @OneToMany(mappedBy = "lenghtofbedID", fetch = FetchType.LAZY)
    private List<Truck> truckList;
    @OneToMany(mappedBy = "lenghtOfBed", fetch = FetchType.LAZY)
    private List<Job> jobList;

    public LenghtOfBed() {
    }

    public LenghtOfBed(Integer lenghtofbedID) {
        this.lenghtofbedID = lenghtofbedID;
    }

    public LenghtOfBed(Integer lenghtofbedID, String name, int sortField) {
        this.lenghtofbedID = lenghtofbedID;
        this.name = name;
        this.sortField = sortField;
    }

    public Integer getLenghtofbedID() {
        return lenghtofbedID;
    }

    public void setLenghtofbedID(Integer lenghtofbedID) {
        this.lenghtofbedID = lenghtofbedID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        hash += (lenghtofbedID != null ? lenghtofbedID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LenghtOfBed)) {
            return false;
        }
        LenghtOfBed other = (LenghtOfBed) object;
        if ((this.lenghtofbedID == null && other.lenghtofbedID != null) || (this.lenghtofbedID != null && !this.lenghtofbedID.equals(other.lenghtofbedID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.LenghtOfBed[ lenghtofbedID=" + lenghtofbedID + " ]";
    }
    
}
