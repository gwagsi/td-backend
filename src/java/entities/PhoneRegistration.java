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
 * @author camertronix
 */
@Entity
@Table(name = "phone_registration")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PhoneRegistration.findAll", query = "SELECT p FROM PhoneRegistration p"),
    @NamedQuery(name = "PhoneRegistration.findByPhoneregistrationID", query = "SELECT p FROM PhoneRegistration p WHERE p.phoneregistrationID = :phoneregistrationID"),
    @NamedQuery(name = "PhoneRegistration.findByPhoneID", query = "SELECT p FROM PhoneRegistration p WHERE p.phoneID = :phoneID"),
    @NamedQuery(name = "PhoneRegistration.findByRegisteredID", query = "SELECT p FROM PhoneRegistration p WHERE p.registeredID = :registeredID"),
    @NamedQuery(name = "PhoneRegistration.findByCreationDate", query = "SELECT p FROM PhoneRegistration p WHERE p.creationDate = :creationDate"),
    @NamedQuery(name = "PhoneRegistration.findByLastModificationDate", query = "SELECT p FROM PhoneRegistration p WHERE p.lastModificationDate = :lastModificationDate"),
    @NamedQuery(name = "PhoneRegistration.findByDeleted", query = "SELECT p FROM PhoneRegistration p WHERE p.deleted = :deleted")})
public class PhoneRegistration implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "phone_ID")
    private String phoneID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "registered_ID")
    private String registeredID;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "phone_registration_ID")
    private Integer phoneregistrationID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "last_modification_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModificationDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "deleted")
    private boolean deleted;
    @JoinColumn(name = "plateform_ID", referencedColumnName = "phone_plateform_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PhonePlateform plateformID;
    @JoinColumn(name = "account_ID", referencedColumnName = "account_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Account accountID;

    public PhoneRegistration() {
    }

    public PhoneRegistration(Integer phoneregistrationID) {
        this.phoneregistrationID = phoneregistrationID;
    }

    public PhoneRegistration(Integer phoneregistrationID, String phoneID, String registeredID, Date creationDate, Date lastModificationDate, boolean deleted) {
        this.phoneregistrationID = phoneregistrationID;
        this.phoneID = phoneID;
        this.registeredID = registeredID;
        this.creationDate = creationDate;
        this.lastModificationDate = lastModificationDate;
        this.deleted = deleted;
    }

    public Integer getPhoneregistrationID() {
        return phoneregistrationID;
    }

    public void setPhoneregistrationID(Integer phoneregistrationID) {
        this.phoneregistrationID = phoneregistrationID;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(Date lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public PhonePlateform getPlateformID() {
        return plateformID;
    }

    public void setPlateformID(PhonePlateform plateformID) {
        this.plateformID = plateformID;
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
        hash += (phoneregistrationID != null ? phoneregistrationID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PhoneRegistration)) {
            return false;
        }
        PhoneRegistration other = (PhoneRegistration) object;
        if ((this.phoneregistrationID == null && other.phoneregistrationID != null) || (this.phoneregistrationID != null && !this.phoneregistrationID.equals(other.phoneregistrationID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PhoneRegistration[ phoneregistrationID=" + phoneregistrationID + " ]";
    }

    public String getPhoneID() {
        return phoneID;
    }

    public void setPhoneID(String phoneID) {
        this.phoneID = phoneID;
    }

    public String getRegisteredID() {
        return registeredID;
    }

    public void setRegisteredID(String registeredID) {
        this.registeredID = registeredID;
    }

}
