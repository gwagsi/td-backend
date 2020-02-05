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
@Table(name = "solicited_truck")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SolicitedTruck.findAll", query = "SELECT s FROM SolicitedTruck s"),
    @NamedQuery(name = "SolicitedTruck.findBySolicitedtruckID", query = "SELECT s FROM SolicitedTruck s WHERE s.solicitedtruckID = :solicitedtruckID"),
    @NamedQuery(name = "SolicitedTruck.findByStartDate", query = "SELECT s FROM SolicitedTruck s WHERE s.startDate = :startDate"),
    @NamedQuery(name = "SolicitedTruck.findByEndDate", query = "SELECT s FROM SolicitedTruck s WHERE s.endDate = :endDate"),
    @NamedQuery(name = "SolicitedTruck.findByTruckAvailable", query = "SELECT s FROM SolicitedTruck s WHERE s.truckAvailable = :truckAvailable"),
    @NamedQuery(name = "SolicitedTruck.findByCreationDate", query = "SELECT s FROM SolicitedTruck s WHERE s.creationDate = :creationDate"),
    @NamedQuery(name = "SolicitedTruck.findByDeleted", query = "SELECT s FROM SolicitedTruck s WHERE s.deleted = :deleted")})
public class SolicitedTruck implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "version")
    @Version
    private int version;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "solicited_truck_ID")
    private Integer solicitedtruckID;
    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "truck_available")
    private boolean truckAvailable;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "deleted")
    private boolean deleted;
    @JoinColumn(name = "job_response_ID", referencedColumnName = "job_response_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private JobResponse jobresponseID;
    @JoinColumn(name = "truck_ID", referencedColumnName = "truck_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Truck truckID;

    public SolicitedTruck() {
    }

    public SolicitedTruck(Integer solicitedtruckID) {
        this.solicitedtruckID = solicitedtruckID;
    }

    public SolicitedTruck(Integer solicitedtruckID, boolean truckAvailable, Date creationDate, boolean deleted) {
        this.solicitedtruckID = solicitedtruckID;
        this.truckAvailable = truckAvailable;
        this.creationDate = creationDate;
        this.deleted = deleted;
    }

    public Integer getSolicitedtruckID() {
        return solicitedtruckID;
    }

    public void setSolicitedtruckID(Integer solicitedtruckID) {
        this.solicitedtruckID = solicitedtruckID;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean getTruckAvailable() {
        return truckAvailable;
    }

    public void setTruckAvailable(boolean truckAvailable) {
        this.truckAvailable = truckAvailable;
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

    public JobResponse getJobresponseID() {
        return jobresponseID;
    }

    public void setJobresponseID(JobResponse jobresponseID) {
        this.jobresponseID = jobresponseID;
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
        hash += (solicitedtruckID != null ? solicitedtruckID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SolicitedTruck)) {
            return false;
        }
        SolicitedTruck other = (SolicitedTruck) object;
        if ((this.solicitedtruckID == null && other.solicitedtruckID != null) || (this.solicitedtruckID != null && !this.solicitedtruckID.equals(other.solicitedtruckID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.SolicitedTruck[ solicitedtruckID=" + solicitedtruckID + " ]";
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

}
