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
@Table(name = "truck_document")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TruckDocument.findAll", query = "SELECT t FROM TruckDocument t"),
    @NamedQuery(name = "TruckDocument.findByTruckdocumentID", query = "SELECT t FROM TruckDocument t WHERE t.truckdocumentID = :truckdocumentID"),
    @NamedQuery(name = "TruckDocument.findByType", query = "SELECT t FROM TruckDocument t WHERE t.type = :type"),
    @NamedQuery(name = "TruckDocument.findByDeleted", query = "SELECT t FROM TruckDocument t WHERE t.deleted = :deleted"),
    @NamedQuery(name = "TruckDocument.findByCreationDate", query = "SELECT t FROM TruckDocument t WHERE t.creationDate = :creationDate")})
public class TruckDocument implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "truck_document_ID")
    private Integer truckdocumentID;
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
    @JoinColumn(name = "truck_ID", referencedColumnName = "truck_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Truck truckID;
    @JoinColumn(name = "document_ID", referencedColumnName = "document_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Document documentID;

    public TruckDocument() {
    }

    public TruckDocument(Integer truckdocumentID) {
        this.truckdocumentID = truckdocumentID;
    }

    public TruckDocument(Integer truckdocumentID, String type, boolean deleted, Date creationDate) {
        this.truckdocumentID = truckdocumentID;
        this.type = type;
        this.deleted = deleted;
        this.creationDate = creationDate;
    }

    public Integer getTruckdocumentID() {
        return truckdocumentID;
    }

    public void setTruckdocumentID(Integer truckdocumentID) {
        this.truckdocumentID = truckdocumentID;
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

    public Truck getTruckID() {
        return truckID;
    }

    public void setTruckID(Truck truckID) {
        this.truckID = truckID;
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
        hash += (truckdocumentID != null ? truckdocumentID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TruckDocument)) {
            return false;
        }
        TruckDocument other = (TruckDocument) object;
        if ((this.truckdocumentID == null && other.truckdocumentID != null) || (this.truckdocumentID != null && !this.truckdocumentID.equals(other.truckdocumentID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.TruckDocument[ truckdocumentID=" + truckdocumentID + " ]";
    }
    
}
