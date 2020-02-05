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
@Table(name = "document")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Document.findAll", query = "SELECT d FROM Document d"),
    @NamedQuery(name = "Document.findByDocumentID", query = "SELECT d FROM Document d WHERE d.documentID = :documentID"),
    @NamedQuery(name = "Document.findByPathName", query = "SELECT d FROM Document d WHERE d.pathName = :pathName"),
    @NamedQuery(name = "Document.findByTypeOfDocument", query = "SELECT d FROM Document d WHERE d.typeOfDocument = :typeOfDocument"),
    @NamedQuery(name = "Document.findByDescription", query = "SELECT d FROM Document d WHERE d.description = :description"),
    @NamedQuery(name = "Document.findByDeleted", query = "SELECT d FROM Document d WHERE d.deleted = :deleted"),
    @NamedQuery(name = "Document.findByCreationDate", query = "SELECT d FROM Document d WHERE d.creationDate = :creationDate")})
public class Document implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "document_ID")
    private Integer documentID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "path_name")
    private String pathName;
    @Size(max = 100)
    @Column(name = "type_of_document")
    private String typeOfDocument;
    @Size(max = 20)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "deleted")
    private boolean deleted;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @OneToMany(mappedBy = "pictureInsurance", fetch = FetchType.LAZY)
    private List<Truck> truckList;
    @OneToMany(mappedBy = "picture", fetch = FetchType.LAZY)
    private List<Truck> truckList1;
    @JoinColumn(name = "user_ID", referencedColumnName = "user_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User userID;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "documentID", fetch = FetchType.LAZY)
    private List<DriverDocument> driverDocumentList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "documentID", fetch = FetchType.LAZY)
    private List<JobDocument> jobDocumentList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "documentID", fetch = FetchType.LAZY)
    private List<TruckDocument> truckDocumentList;

    public Document() {
    }

    public Document(Integer documentID) {
        this.documentID = documentID;
    }

    public Document(Integer documentID, String pathName, boolean deleted, Date creationDate) {
        this.documentID = documentID;
        this.pathName = pathName;
        this.deleted = deleted;
        this.creationDate = creationDate;
    }

    public Integer getDocumentID() {
        return documentID;
    }

    public void setDocumentID(Integer documentID) {
        this.documentID = documentID;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public String getTypeOfDocument() {
        return typeOfDocument;
    }

    public void setTypeOfDocument(String typeOfDocument) {
        this.typeOfDocument = typeOfDocument;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @XmlTransient
    public List<Truck> getTruckList() {
        return truckList;
    }

    public void setTruckList(List<Truck> truckList) {
        this.truckList = truckList;
    }

    @XmlTransient
    public List<Truck> getTruckList1() {
        return truckList1;
    }

    public void setTruckList1(List<Truck> truckList1) {
        this.truckList1 = truckList1;
    }

    public User getUserID() {
        return userID;
    }

    public void setUserID(User userID) {
        this.userID = userID;
    }

    @XmlTransient
    public List<DriverDocument> getDriverDocumentList() {
        return driverDocumentList;
    }

    public void setDriverDocumentList(List<DriverDocument> driverDocumentList) {
        this.driverDocumentList = driverDocumentList;
    }

    @XmlTransient
    public List<JobDocument> getJobDocumentList() {
        return jobDocumentList;
    }

    public void setJobDocumentList(List<JobDocument> jobDocumentList) {
        this.jobDocumentList = jobDocumentList;
    }

    @XmlTransient
    public List<TruckDocument> getTruckDocumentList() {
        return truckDocumentList;
    }

    public void setTruckDocumentList(List<TruckDocument> truckDocumentList) {
        this.truckDocumentList = truckDocumentList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (documentID != null ? documentID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Document)) {
            return false;
        }
        Document other = (Document) object;
        if ((this.documentID == null && other.documentID != null) || (this.documentID != null && !this.documentID.equals(other.documentID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Document[ documentID=" + documentID + " ]";
    }
    
}
