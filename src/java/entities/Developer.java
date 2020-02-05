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
@Table(name = "developer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Developer.findAll", query = "SELECT d FROM Developer d"),
    @NamedQuery(name = "Developer.findByDeveloperID", query = "SELECT d FROM Developer d WHERE d.developerID = :developerID"),
    @NamedQuery(name = "Developer.findByFirstName", query = "SELECT d FROM Developer d WHERE d.firstName = :firstName"),
    @NamedQuery(name = "Developer.findByLastName", query = "SELECT d FROM Developer d WHERE d.lastName = :lastName"),
    @NamedQuery(name = "Developer.findByDeleted", query = "SELECT d FROM Developer d WHERE d.deleted = :deleted"),
    @NamedQuery(name = "Developer.findByCreationDate", query = "SELECT d FROM Developer d WHERE d.creationDate = :creationDate"),
    @NamedQuery(name = "Developer.findByTimezoneID", query = "SELECT d FROM Developer d WHERE d.timezoneID = :timezoneID")})
public class Developer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "developer_ID")
    private Integer developerID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "first_name")
    private String firstName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "last_name")
    private String lastName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "deleted")
    private boolean deleted;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Size(max = 30)
    @Column(name = "timezoneID")
    private String timezoneID;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "developerID")
    private List<ResolveIssue> resolveIssueList;
    @JoinColumn(name = "users_ID", referencedColumnName = "USERNAME")
    @ManyToOne(optional = false)
    private Users usersID;

    public Developer() {
    }

    public Developer(Integer developerID) {
        this.developerID = developerID;
    }

    public Developer(Integer developerID, String firstName, String lastName, boolean deleted, Date creationDate) {
        this.developerID = developerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.deleted = deleted;
        this.creationDate = creationDate;
    }

    public Integer getDeveloperID() {
        return developerID;
    }

    public void setDeveloperID(Integer developerID) {
        this.developerID = developerID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getTimezoneID() {
        return timezoneID;
    }

    public void setTimezoneID(String timezoneID) {
        this.timezoneID = timezoneID;
    }

    @XmlTransient
    public List<ResolveIssue> getResolveIssueList() {
        return resolveIssueList;
    }

    public void setResolveIssueList(List<ResolveIssue> resolveIssueList) {
        this.resolveIssueList = resolveIssueList;
    }

    public Users getUsersID() {
        return usersID;
    }

    public void setUsersID(Users usersID) {
        this.usersID = usersID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (developerID != null ? developerID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Developer)) {
            return false;
        }
        Developer other = (Developer) object;
        if ((this.developerID == null && other.developerID != null) || (this.developerID != null && !this.developerID.equals(other.developerID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Developer[ developerID=" + developerID + " ]";
    }
    
}
