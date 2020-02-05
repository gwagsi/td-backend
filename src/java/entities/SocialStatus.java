/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author erman
 */
@Entity
@Table(name = "social_status")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SocialStatus.findAll", query = "SELECT s FROM SocialStatus s"),
    @NamedQuery(name = "SocialStatus.findBySocialstatusID", query = "SELECT s FROM SocialStatus s WHERE s.socialstatusID = :socialstatusID"),
    @NamedQuery(name = "SocialStatus.findByTypeName", query = "SELECT s FROM SocialStatus s WHERE s.typeName = :typeName"),
    @NamedQuery(name = "SocialStatus.findByDescription", query = "SELECT s FROM SocialStatus s WHERE s.description = :description"),
    @NamedQuery(name = "SocialStatus.findByDeleted", query = "SELECT s FROM SocialStatus s WHERE s.deleted = :deleted")})
public class SocialStatus implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "social_status_ID")
    private Integer socialstatusID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "type_name")
    private String typeName;
    @Size(max = 100)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "deleted")
    private boolean deleted;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "socialstatusID", fetch = FetchType.LAZY)
    private List<Account> accountList;

    public SocialStatus() {
    }

    public SocialStatus(Integer socialstatusID) {
        this.socialstatusID = socialstatusID;
    }

    public SocialStatus(Integer socialstatusID, String typeName, boolean deleted) {
        this.socialstatusID = socialstatusID;
        this.typeName = typeName;
        this.deleted = deleted;
    }

    public Integer getSocialstatusID() {
        return socialstatusID;
    }

    public void setSocialstatusID(Integer socialstatusID) {
        this.socialstatusID = socialstatusID;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
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

    @XmlTransient
    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (socialstatusID != null ? socialstatusID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SocialStatus)) {
            return false;
        }
        SocialStatus other = (SocialStatus) object;
        if ((this.socialstatusID == null && other.socialstatusID != null) || (this.socialstatusID != null && !this.socialstatusID.equals(other.socialstatusID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.SocialStatus[ socialstatusID=" + socialstatusID + " ]";
    }
    
}
