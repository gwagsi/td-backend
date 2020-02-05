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
@Table(name = "admin_action")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AdminAction.findAll", query = "SELECT a FROM AdminAction a"),
    @NamedQuery(name = "AdminAction.findByAdminactionID", query = "SELECT a FROM AdminAction a WHERE a.adminactionID = :adminactionID"),
    @NamedQuery(name = "AdminAction.findByLibelAction", query = "SELECT a FROM AdminAction a WHERE a.libelAction = :libelAction"),
    @NamedQuery(name = "AdminAction.findByDescription", query = "SELECT a FROM AdminAction a WHERE a.description = :description")})
public class AdminAction implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "admin_action_ID")
    private Integer adminactionID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "libel_action")
    private String libelAction;
    @Size(max = 200)
    @Column(name = "description")
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "action", fetch = FetchType.LAZY)
    private List<AccountAdministration> accountAdministrationList;

    public AdminAction() {
    }

    public AdminAction(Integer adminactionID) {
        this.adminactionID = adminactionID;
    }

    public AdminAction(Integer adminactionID, String libelAction) {
        this.adminactionID = adminactionID;
        this.libelAction = libelAction;
    }

    public Integer getAdminactionID() {
        return adminactionID;
    }

    public void setAdminactionID(Integer adminactionID) {
        this.adminactionID = adminactionID;
    }

    public String getLibelAction() {
        return libelAction;
    }

    public void setLibelAction(String libelAction) {
        this.libelAction = libelAction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public List<AccountAdministration> getAccountAdministrationList() {
        return accountAdministrationList;
    }

    public void setAccountAdministrationList(List<AccountAdministration> accountAdministrationList) {
        this.accountAdministrationList = accountAdministrationList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (adminactionID != null ? adminactionID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AdminAction)) {
            return false;
        }
        AdminAction other = (AdminAction) object;
        if ((this.adminactionID == null && other.adminactionID != null) || (this.adminactionID != null && !this.adminactionID.equals(other.adminactionID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.AdminAction[ adminactionID=" + adminactionID + " ]";
    }
    
}
