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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author erman
 */
@Entity
@Table(name = "realize_service")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RealizeService.findAll", query = "SELECT r FROM RealizeService r"),
    @NamedQuery(name = "RealizeService.findByRealizeserviceID", query = "SELECT r FROM RealizeService r WHERE r.realizeserviceID = :realizeserviceID")})
public class RealizeService implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "realize_service_ID")
    private Integer realizeserviceID;
    @JoinColumn(name = "account_ID", referencedColumnName = "account_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Account accountID;
    @JoinColumn(name = "service_ID", referencedColumnName = "service_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Service serviceID;

    public RealizeService() {
    }

    public RealizeService(Integer realizeserviceID) {
        this.realizeserviceID = realizeserviceID;
    }

    public Integer getRealizeserviceID() {
        return realizeserviceID;
    }

    public void setRealizeserviceID(Integer realizeserviceID) {
        this.realizeserviceID = realizeserviceID;
    }

    public Account getAccountID() {
        return accountID;
    }

    public void setAccountID(Account accountID) {
        this.accountID = accountID;
    }

    public Service getServiceID() {
        return serviceID;
    }

    public void setServiceID(Service serviceID) {
        this.serviceID = serviceID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (realizeserviceID != null ? realizeserviceID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RealizeService)) {
            return false;
        }
        RealizeService other = (RealizeService) object;
        if ((this.realizeserviceID == null && other.realizeserviceID != null) || (this.realizeserviceID != null && !this.realizeserviceID.equals(other.realizeserviceID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.RealizeService[ realizeserviceID=" + realizeserviceID + " ]";
    }
    
}
