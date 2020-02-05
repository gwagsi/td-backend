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
@Table(name = "validation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Validation.findAll", query = "SELECT v FROM Validation v"),
    @NamedQuery(name = "Validation.findByValidationID", query = "SELECT v FROM Validation v WHERE v.validationID = :validationID"),
    @NamedQuery(name = "Validation.findByTruckOwnerValidationDate", query = "SELECT v FROM Validation v WHERE v.truckOwnerValidationDate = :truckOwnerValidationDate"),
    @NamedQuery(name = "Validation.findByClientValidationDate", query = "SELECT v FROM Validation v WHERE v.clientValidationDate = :clientValidationDate"),
    @NamedQuery(name = "Validation.findByCreationDate", query = "SELECT v FROM Validation v WHERE v.creationDate = :creationDate"),
    @NamedQuery(name = "Validation.findByDeleted", query = "SELECT v FROM Validation v WHERE v.deleted = :deleted"),
    @NamedQuery(name = "Validation.findByClientValidation", query = "SELECT v FROM Validation v WHERE v.clientValidation = :clientValidation"),
    @NamedQuery(name = "Validation.findByTruckOwnerValidation", query = "SELECT v FROM Validation v WHERE v.truckOwnerValidation = :truckOwnerValidation")})
public class Validation implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "version")
    @Version
    private int version;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "validation_ID")
    private Integer validationID;
    @Column(name = "truck_owner_validation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date truckOwnerValidationDate;
    @Column(name = "client_validation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date clientValidationDate;
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
    @Column(name = "client_validation")
    private int clientValidation;
    @Basic(optional = false)
    @NotNull
    @Column(name = "truck_owner_validation")
    private int truckOwnerValidation;
    @JoinColumn(name = "job_ID", referencedColumnName = "job_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Job jobID;
    @JoinColumn(name = "job_response_ID", referencedColumnName = "job_response_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private JobResponse jobresponseID;

    public Validation() {
    }

    public Validation(Integer validationID) {
        this.validationID = validationID;
    }

    public Validation(Integer validationID, Date creationDate, boolean deleted, int clientValidation, int truckOwnerValidation) {
        this.validationID = validationID;
        this.creationDate = creationDate;
        this.deleted = deleted;
        this.clientValidation = clientValidation;
        this.truckOwnerValidation = truckOwnerValidation;
    }

    public Integer getValidationID() {
        return validationID;
    }

    public void setValidationID(Integer validationID) {
        this.validationID = validationID;
    }

    public Date getTruckOwnerValidationDate() {
        return truckOwnerValidationDate;
    }

    public void setTruckOwnerValidationDate(Date truckOwnerValidationDate) {
        this.truckOwnerValidationDate = truckOwnerValidationDate;
    }

    public Date getClientValidationDate() {
        return clientValidationDate;
    }

    public void setClientValidationDate(Date clientValidationDate) {
        this.clientValidationDate = clientValidationDate;
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

    public int getClientValidation() {
        return clientValidation;
    }

    public void setClientValidation(int clientValidation) {
        this.clientValidation = clientValidation;
    }

    public int getTruckOwnerValidation() {
        return truckOwnerValidation;
    }

    public void setTruckOwnerValidation(int truckOwnerValidation) {
        this.truckOwnerValidation = truckOwnerValidation;
    }

    public Job getJobID() {
        return jobID;
    }

    public void setJobID(Job jobID) {
        this.jobID = jobID;
    }

    public JobResponse getJobresponseID() {
        return jobresponseID;
    }

    public void setJobresponseID(JobResponse jobresponseID) {
        this.jobresponseID = jobresponseID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (validationID != null ? validationID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Validation)) {
            return false;
        }
        Validation other = (Validation) object;
        if ((this.validationID == null && other.validationID != null) || (this.validationID != null && !this.validationID.equals(other.validationID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Validation[ validationID=" + validationID + " ]";
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
    
}
