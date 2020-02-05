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
@Table(name = "driver_document")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DriverDocument.findAll", query = "SELECT d FROM DriverDocument d"),
    @NamedQuery(name = "DriverDocument.findByDriverdocumentID", query = "SELECT d FROM DriverDocument d WHERE d.driverdocumentID = :driverdocumentID"),
    @NamedQuery(name = "DriverDocument.findByType", query = "SELECT d FROM DriverDocument d WHERE d.type = :type"),
    @NamedQuery(name = "DriverDocument.findByDeleted", query = "SELECT d FROM DriverDocument d WHERE d.deleted = :deleted"),
    @NamedQuery(name = "DriverDocument.findByCreationDate", query = "SELECT d FROM DriverDocument d WHERE d.creationDate = :creationDate")})
public class DriverDocument implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "driver_document_ID")
    private Integer driverdocumentID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @NotNull
    @Column(name = "deleted")
    private boolean deleted;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @JoinColumn(name = "driver_ID", referencedColumnName = "driver_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Driver driverID;
    @JoinColumn(name = "document_ID", referencedColumnName = "document_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Document documentID;

    public DriverDocument() {
    }

    public DriverDocument(Integer driverdocumentID) {
        this.driverdocumentID = driverdocumentID;
    }

    public DriverDocument(Integer driverdocumentID, String type, boolean deleted, Date creationDate) {
        this.driverdocumentID = driverdocumentID;
        this.type = type;
        this.deleted = deleted;
        this.creationDate = creationDate;
    }

    public Integer getDriverdocumentID() {
        return driverdocumentID;
    }

    public void setDriverdocumentID(Integer driverdocumentID) {
        this.driverdocumentID = driverdocumentID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Driver getDriverID() {
        return driverID;
    }

    public void setDriverID(Driver driverID) {
        this.driverID = driverID;
    }

    public Document getDocumentID() {
        return documentID;
    }

    public void setDocumentID(Document documentID) {
        this.documentID = documentID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (driverdocumentID != null ? driverdocumentID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DriverDocument)) {
            return false;
        }
        DriverDocument other = (DriverDocument) object;
        if ((this.driverdocumentID == null && other.driverdocumentID != null) || (this.driverdocumentID != null && !this.driverdocumentID.equals(other.driverdocumentID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.DriverDocument[ driverdocumentID=" + driverdocumentID + " ]";
    }
    
}
