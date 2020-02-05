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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "invalid_solicited_truck")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvalidSolicitedTruck.findAll", query = "SELECT i FROM InvalidSolicitedTruck i"),
    @NamedQuery(name = "InvalidSolicitedTruck.findByInvalidsolicitedtruckID", query = "SELECT i FROM InvalidSolicitedTruck i WHERE i.invalidsolicitedtruckID = :invalidsolicitedtruckID"),
    @NamedQuery(name = "InvalidSolicitedTruck.findByCreationDate", query = "SELECT i FROM InvalidSolicitedTruck i WHERE i.creationDate = :creationDate"),
    @NamedQuery(name = "InvalidSolicitedTruck.findByDeleted", query = "SELECT i FROM InvalidSolicitedTruck i WHERE i.deleted = :deleted"),
    @NamedQuery(name = "InvalidSolicitedTruck.findByLevel", query = "SELECT i FROM InvalidSolicitedTruck i WHERE i.level = :level"),
    @NamedQuery(name = "InvalidSolicitedTruck.findByJobresponseID", query = "SELECT i FROM InvalidSolicitedTruck i WHERE i.jobresponseID = :jobresponseID"),
    @NamedQuery(name = "InvalidSolicitedTruck.findBySolicitedtruckID", query = "SELECT i FROM InvalidSolicitedTruck i WHERE i.solicitedtruckID = :solicitedtruckID")})
public class InvalidSolicitedTruck implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "invalid_solicited_truck_ID")
    private Integer invalidsolicitedtruckID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "deleted")
    private boolean deleted;
    @Basic(optional = false)
    @NotNull
    @Column(name = "level")
    private int level;
    @Column(name = "job_response_ID")
    private Integer jobresponseID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "solicited_truck_ID")
    private int solicitedtruckID;

    public InvalidSolicitedTruck() {
    }

    public InvalidSolicitedTruck(Integer invalidsolicitedtruckID) {
        this.invalidsolicitedtruckID = invalidsolicitedtruckID;
    }

    public InvalidSolicitedTruck(Integer invalidsolicitedtruckID, Date creationDate, boolean deleted, int level, int solicitedtruckID) {
        this.invalidsolicitedtruckID = invalidsolicitedtruckID;
        this.creationDate = creationDate;
        this.deleted = deleted;
        this.level = level;
        this.solicitedtruckID = solicitedtruckID;
    }

    public Integer getInvalidsolicitedtruckID() {
        return invalidsolicitedtruckID;
    }

    public void setInvalidsolicitedtruckID(Integer invalidsolicitedtruckID) {
        this.invalidsolicitedtruckID = invalidsolicitedtruckID;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Integer getJobresponseID() {
        return jobresponseID;
    }

    public void setJobresponseID(Integer jobresponseID) {
        this.jobresponseID = jobresponseID;
    }

    public int getSolicitedtruckID() {
        return solicitedtruckID;
    }

    public void setSolicitedtruckID(int solicitedtruckID) {
        this.solicitedtruckID = solicitedtruckID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (invalidsolicitedtruckID != null ? invalidsolicitedtruckID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvalidSolicitedTruck)) {
            return false;
        }
        InvalidSolicitedTruck other = (InvalidSolicitedTruck) object;
        if ((this.invalidsolicitedtruckID == null && other.invalidsolicitedtruckID != null) || (this.invalidsolicitedtruckID != null && !this.invalidsolicitedtruckID.equals(other.invalidsolicitedtruckID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.InvalidSolicitedTruck[ invalidsolicitedtruckID=" + invalidsolicitedtruckID + " ]";
    }
    
}
