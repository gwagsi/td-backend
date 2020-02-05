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
@Table(name = "job_document")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "JobDocument.findAll", query = "SELECT j FROM JobDocument j"),
    @NamedQuery(name = "JobDocument.findByJobdocumentID", query = "SELECT j FROM JobDocument j WHERE j.jobdocumentID = :jobdocumentID"),
    @NamedQuery(name = "JobDocument.findByType", query = "SELECT j FROM JobDocument j WHERE j.type = :type"),
    @NamedQuery(name = "JobDocument.findByDeleted", query = "SELECT j FROM JobDocument j WHERE j.deleted = :deleted"),
    @NamedQuery(name = "JobDocument.findByCreationDate", query = "SELECT j FROM JobDocument j WHERE j.creationDate = :creationDate")})
public class JobDocument implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "job_document_ID")
    private Integer jobdocumentID;
    @Size(max = 100)
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
    @JoinColumn(name = "Job_ID", referencedColumnName = "job_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Job jobID;
    @JoinColumn(name = "document_ID", referencedColumnName = "document_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Document documentID;

    public JobDocument() {
    }

    public JobDocument(Integer jobdocumentID) {
        this.jobdocumentID = jobdocumentID;
    }

    public JobDocument(Integer jobdocumentID, boolean deleted, Date creationDate) {
        this.jobdocumentID = jobdocumentID;
        this.deleted = deleted;
        this.creationDate = creationDate;
    }

    public Integer getJobdocumentID() {
        return jobdocumentID;
    }

    public void setJobdocumentID(Integer jobdocumentID) {
        this.jobdocumentID = jobdocumentID;
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

    public Job getJobID() {
        return jobID;
    }

    public void setJobID(Job jobID) {
        this.jobID = jobID;
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
        hash += (jobdocumentID != null ? jobdocumentID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JobDocument)) {
            return false;
        }
        JobDocument other = (JobDocument) object;
        if ((this.jobdocumentID == null && other.jobdocumentID != null) || (this.jobdocumentID != null && !this.jobdocumentID.equals(other.jobdocumentID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.JobDocument[ jobdocumentID=" + jobdocumentID + " ]";
    }
    
}
