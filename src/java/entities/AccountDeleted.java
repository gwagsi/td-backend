/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author sorelus
 */
@Entity
@Table(name = "account_deleted")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AccountDeleted.findAll", query = "SELECT a FROM AccountDeleted a"),
    @NamedQuery(name = "AccountDeleted.findByAccountdeletedID", query = "SELECT a FROM AccountDeleted a WHERE a.accountdeletedID = :accountdeletedID"),
    @NamedQuery(name = "AccountDeleted.findByEmail", query = "SELECT a FROM AccountDeleted a WHERE a.email = :email"),
    @NamedQuery(name = "AccountDeleted.findByLogin", query = "SELECT a FROM AccountDeleted a WHERE a.login = :login")})
public class AccountDeleted implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "account_deleted_ID")
    private Integer accountdeletedID;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "login")
    private String login;
    @JoinColumn(name = "account_ID", referencedColumnName = "account_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Account accountID;

    public AccountDeleted() {
    }

    public AccountDeleted(Integer accountdeletedID) {
        this.accountdeletedID = accountdeletedID;
    }

    public AccountDeleted(Integer accountdeletedID, String email, String login) {
        this.accountdeletedID = accountdeletedID;
        this.email = email;
        this.login = login;
    }

    public Integer getAccountdeletedID() {
        return accountdeletedID;
    }

    public void setAccountdeletedID(Integer accountdeletedID) {
        this.accountdeletedID = accountdeletedID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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
        hash += (accountdeletedID != null ? accountdeletedID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AccountDeleted)) {
            return false;
        }
        AccountDeleted other = (AccountDeleted) object;
        if ((this.accountdeletedID == null && other.accountdeletedID != null) || (this.accountdeletedID != null && !this.accountdeletedID.equals(other.accountdeletedID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.AccountDeleted[ accountdeletedID=" + accountdeletedID + " ]";
    }
    
}
