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
@Table(name = "issue")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Issue.findAll", query = "SELECT i FROM Issue i"),
    @NamedQuery(name = "Issue.findByIssueID", query = "SELECT i FROM Issue i WHERE i.issueID = :issueID"),
    @NamedQuery(name = "Issue.findByCategory", query = "SELECT i FROM Issue i WHERE i.category = :category"),
    @NamedQuery(name = "Issue.findBySubcategory", query = "SELECT i FROM Issue i WHERE i.subcategory = :subcategory"),
    @NamedQuery(name = "Issue.findByDescription", query = "SELECT i FROM Issue i WHERE i.description = :description"),
    @NamedQuery(name = "Issue.findByStatut", query = "SELECT i FROM Issue i WHERE i.statut = :statut"),
    @NamedQuery(name = "Issue.findByCreationDate", query = "SELECT i FROM Issue i WHERE i.creationDate = :creationDate"),
    @NamedQuery(name = "Issue.findByEditionDate", query = "SELECT i FROM Issue i WHERE i.editionDate = :editionDate"),
    @NamedQuery(name = "Issue.findByDeleted", query = "SELECT i FROM Issue i WHERE i.deleted = :deleted")})
public class Issue implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "issue_ID")
    private Integer issueID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "category")
    private String category;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "subcategory")
    private String subcategory;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 700)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "statut")
    private boolean statut;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "edition_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date editionDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "deleted")
    private boolean deleted;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "issueID")
    private List<ResolveIssue> resolveIssueList;
    @JoinColumn(name = "account_ID", referencedColumnName = "account_ID")
    @ManyToOne(optional = false)
    private Account accountID;

    public Issue() {
    }

    public Issue(Integer issueID) {
        this.issueID = issueID;
    }

    public Issue(Integer issueID, String category, String subcategory, String description, boolean statut, Date creationDate, Date editionDate, boolean deleted) {
        this.issueID = issueID;
        this.category = category;
        this.subcategory = subcategory;
        this.description = description;
        this.statut = statut;
        this.creationDate = creationDate;
        this.editionDate = editionDate;
        this.deleted = deleted;
    }

    public Integer getIssueID() {
        return issueID;
    }

    public void setIssueID(Integer issueID) {
        this.issueID = issueID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getStatut() {
        return statut;
    }

    public void setStatut(boolean statut) {
        this.statut = statut;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getEditionDate() {
        return editionDate;
    }

    public void setEditionDate(Date editionDate) {
        this.editionDate = editionDate;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @XmlTransient
    public List<ResolveIssue> getResolveIssueList() {
        return resolveIssueList;
    }

    public void setResolveIssueList(List<ResolveIssue> resolveIssueList) {
        this.resolveIssueList = resolveIssueList;
    }

    public Account getAccountID() {
        return accountID;
    }

    public void setAccountID(Account accountID) {
        this.accountID = accountID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (issueID != null ? issueID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Issue)) {
            return false;
        }
        Issue other = (Issue) object;
        if ((this.issueID == null && other.issueID != null) || (this.issueID != null && !this.issueID.equals(other.issueID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Issue[ issueID=" + issueID + " ]";
    }
    
}
