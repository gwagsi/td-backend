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
@Table(name = "resolve_issue")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ResolveIssue.findAll", query = "SELECT r FROM ResolveIssue r"),
    @NamedQuery(name = "ResolveIssue.findByResolveissueID", query = "SELECT r FROM ResolveIssue r WHERE r.resolveissueID = :resolveissueID"),
    @NamedQuery(name = "ResolveIssue.findBySolution", query = "SELECT r FROM ResolveIssue r WHERE r.solution = :solution"),
    @NamedQuery(name = "ResolveIssue.findBySolutionDate", query = "SELECT r FROM ResolveIssue r WHERE r.solutionDate = :solutionDate"),
    @NamedQuery(name = "ResolveIssue.findByCreationDate", query = "SELECT r FROM ResolveIssue r WHERE r.creationDate = :creationDate"),
    @NamedQuery(name = "ResolveIssue.findByDeleted", query = "SELECT r FROM ResolveIssue r WHERE r.deleted = :deleted"),
    @NamedQuery(name = "ResolveIssue.findByEditionDate", query = "SELECT r FROM ResolveIssue r WHERE r.editionDate = :editionDate")})
public class ResolveIssue implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "resolve_issue_ID")
    private Integer resolveissueID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 700)
    @Column(name = "solution")
    private String solution;
    @Basic(optional = false)
    @NotNull
    @Column(name = "solution_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date solutionDate;
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
    @Column(name = "edition_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date editionDate;
    @JoinColumn(name = "developer_ID", referencedColumnName = "developer_ID")
    @ManyToOne(optional = false)
    private Developer developerID;
    @JoinColumn(name = "issue_ID", referencedColumnName = "issue_ID")
    @ManyToOne(optional = false)
    private Issue issueID;

    public ResolveIssue() {
    }

    public ResolveIssue(Integer resolveissueID) {
        this.resolveissueID = resolveissueID;
    }

    public ResolveIssue(Integer resolveissueID, String solution, Date solutionDate, Date creationDate, boolean deleted, Date editionDate) {
        this.resolveissueID = resolveissueID;
        this.solution = solution;
        this.solutionDate = solutionDate;
        this.creationDate = creationDate;
        this.deleted = deleted;
        this.editionDate = editionDate;
    }

    public Integer getResolveissueID() {
        return resolveissueID;
    }

    public void setResolveissueID(Integer resolveissueID) {
        this.resolveissueID = resolveissueID;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public Date getSolutionDate() {
        return solutionDate;
    }

    public void setSolutionDate(Date solutionDate) {
        this.solutionDate = solutionDate;
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

    public Date getEditionDate() {
        return editionDate;
    }

    public void setEditionDate(Date editionDate) {
        this.editionDate = editionDate;
    }

    public Developer getDeveloperID() {
        return developerID;
    }

    public void setDeveloperID(Developer developerID) {
        this.developerID = developerID;
    }

    public Issue getIssueID() {
        return issueID;
    }

    public void setIssueID(Issue issueID) {
        this.issueID = issueID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (resolveissueID != null ? resolveissueID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ResolveIssue)) {
            return false;
        }
        ResolveIssue other = (ResolveIssue) object;
        if ((this.resolveissueID == null && other.resolveissueID != null) || (this.resolveissueID != null && !this.resolveissueID.equals(other.resolveissueID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ResolveIssue[ resolveissueID=" + resolveissueID + " ]";
    }
    
}
