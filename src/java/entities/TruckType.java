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
@Table(name = "truck_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TruckType.findAll", query = "SELECT t FROM TruckType t"),
    @NamedQuery(name = "TruckType.findByTrucktypeID", query = "SELECT t FROM TruckType t WHERE t.trucktypeID = :trucktypeID"),
    @NamedQuery(name = "TruckType.findByYear", query = "SELECT t FROM TruckType t WHERE t.year = :year"),
    @NamedQuery(name = "TruckType.findByMake", query = "SELECT t FROM TruckType t WHERE t.make = :make"),
    @NamedQuery(name = "TruckType.findByModel", query = "SELECT t FROM TruckType t WHERE t.model = :model"),
    @NamedQuery(name = "TruckType.findByTrimStyle", query = "SELECT t FROM TruckType t WHERE t.trimStyle = :trimStyle")})
public class TruckType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "truck_type_ID")
    private Integer trucktypeID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "year")
    @Temporal(TemporalType.DATE)
    private Date year;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "make")
    private String make;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "model")
    private String model;
    @Size(max = 100)
    @Column(name = "trim_style")
    private String trimStyle;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "trucktypeID", fetch = FetchType.LAZY)
    private List<Truck> truckList;

    public TruckType() {
    }

    public TruckType(Integer trucktypeID) {
        this.trucktypeID = trucktypeID;
    }

    public TruckType(Integer trucktypeID, Date year, String make, String model) {
        this.trucktypeID = trucktypeID;
        this.year = year;
        this.make = make;
        this.model = model;
    }

    public Integer getTrucktypeID() {
        return trucktypeID;
    }

    public void setTrucktypeID(Integer trucktypeID) {
        this.trucktypeID = trucktypeID;
    }

    public Date getYear() {
        return year;
    }

    public void setYear(Date year) {
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTrimStyle() {
        return trimStyle;
    }

    public void setTrimStyle(String trimStyle) {
        this.trimStyle = trimStyle;
    }

    @XmlTransient
    public List<Truck> getTruckList() {
        return truckList;
    }

    public void setTruckList(List<Truck> truckList) {
        this.truckList = truckList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (trucktypeID != null ? trucktypeID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TruckType)) {
            return false;
        }
        TruckType other = (TruckType) object;
        if ((this.trucktypeID == null && other.trucktypeID != null) || (this.trucktypeID != null && !this.trucktypeID.equals(other.trucktypeID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.TruckType[ trucktypeID=" + trucktypeID + " ]";
    }
    
}
