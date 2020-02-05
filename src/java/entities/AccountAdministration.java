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
import javax.persistence.Lob;
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
@Table(name = "account_administration")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AccountAdministration.findAll", query = "SELECT a FROM AccountAdministration a"),
    @NamedQuery(name = "AccountAdministration.findById", query = "SELECT a FROM AccountAdministration a WHERE a.id = :id"),
    @NamedQuery(name = "AccountAdministration.findByActionDate", query = "SELECT a FROM AccountAdministration a WHERE a.actionDate = :actionDate")})
public class AccountAdministration implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "actionDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actionDate;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "message")
    private String message;
    @JoinColumn(name = "action", referencedColumnName = "admin_action_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private AdminAction action;
    @JoinColumn(name = "admin", referencedColumnName = "USERNAME")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Users admin;
    @JoinColumn(name = "accountID", referencedColumnName = "account_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Account accountID;

    public AccountAdministration() {
    }

    public AccountAdministration(Long id) {
        this.id = id;
    }

    public AccountAdministration(Long id, Date actionDate, String message) {
        this.id = id;
        this.actionDate = actionDate;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AdminAction getAction() {
        return action;
    }

    public void setAction(AdminAction action) {
        this.action = action;
    }

    public Users getAdmin() {
        return admin;
    }

    public void setAdmin(Users admin) {
        this.admin = admin;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AccountAdministration)) {
            return false;
        }
        AccountAdministration other = (AccountAdministration) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.AccountAdministration[ id=" + id + " ]";
    }
    
}
