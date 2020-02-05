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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author erman
 */
@Entity
@Table(name = "account_history")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AccountHistory.findAll", query = "SELECT a FROM AccountHistory a"),
    @NamedQuery(name = "AccountHistory.findByAccounthistoryID", query = "SELECT a FROM AccountHistory a WHERE a.accounthistoryID = :accounthistoryID"),
    @NamedQuery(name = "AccountHistory.findByStartConnection", query = "SELECT a FROM AccountHistory a WHERE a.startConnection = :startConnection"),
    @NamedQuery(name = "AccountHistory.findByEndConnection", query = "SELECT a FROM AccountHistory a WHERE a.endConnection = :endConnection"),
    @NamedQuery(name = "AccountHistory.findBySessionTimeOut", query = "SELECT a FROM AccountHistory a WHERE a.sessionTimeOut = :sessionTimeOut"),
    @NamedQuery(name = "AccountHistory.findByDeleted", query = "SELECT a FROM AccountHistory a WHERE a.deleted = :deleted")})
public class AccountHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "account_history_ID")
    private Integer accounthistoryID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "start_connection")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startConnection;
    @Column(name = "end_connection")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endConnection;
    @Basic(optional = false)
    @NotNull
    @Column(name = "session_time_out")
    private int sessionTimeOut;
    @Basic(optional = false)
    @NotNull
    @Column(name = "deleted")
    private boolean deleted;
    @JoinColumn(name = "account_ID", referencedColumnName = "account_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Account accountID;

    public AccountHistory() {
    }

    public AccountHistory(Integer accounthistoryID) {
        this.accounthistoryID = accounthistoryID;
    }

    public AccountHistory(Integer accounthistoryID, Date startConnection, int sessionTimeOut, boolean deleted) {
        this.accounthistoryID = accounthistoryID;
        this.startConnection = startConnection;
        this.sessionTimeOut = sessionTimeOut;
        this.deleted = deleted;
    }

    public Integer getAccounthistoryID() {
        return accounthistoryID;
    }

    public void setAccounthistoryID(Integer accounthistoryID) {
        this.accounthistoryID = accounthistoryID;
    }

    public Date getStartConnection() {
        return startConnection;
    }

    public void setStartConnection(Date startConnection) {
        this.startConnection = startConnection;
    }

    public Date getEndConnection() {
        return endConnection;
    }

    public void setEndConnection(Date endConnection) {
        this.endConnection = endConnection;
    }

    public int getSessionTimeOut() {
        return sessionTimeOut;
    }

    public void setSessionTimeOut(int sessionTimeOut) {
        this.sessionTimeOut = sessionTimeOut;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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
        hash += (accounthistoryID != null ? accounthistoryID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AccountHistory)) {
            return false;
        }
        AccountHistory other = (AccountHistory) object;
        if ((this.accounthistoryID == null && other.accounthistoryID != null) || (this.accounthistoryID != null && !this.accounthistoryID.equals(other.accounthistoryID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.AccountHistory[ accounthistoryID=" + accounthistoryID + " ]";
    }
    
}
