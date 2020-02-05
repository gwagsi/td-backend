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
@Table(name = "apply_promotion_code")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ApplyPromotionCode.findAll", query = "SELECT a FROM ApplyPromotionCode a"),
    @NamedQuery(name = "ApplyPromotionCode.findByApplypromotioncodeID", query = "SELECT a FROM ApplyPromotionCode a WHERE a.applypromotioncodeID = :applypromotioncodeID"),
    @NamedQuery(name = "ApplyPromotionCode.findByAttributionDate", query = "SELECT a FROM ApplyPromotionCode a WHERE a.attributionDate = :attributionDate"),
    @NamedQuery(name = "ApplyPromotionCode.findByExpirationTerm", query = "SELECT a FROM ApplyPromotionCode a WHERE a.expirationTerm = :expirationTerm"),
    @NamedQuery(name = "ApplyPromotionCode.findByDateOfUsed", query = "SELECT a FROM ApplyPromotionCode a WHERE a.dateOfUsed = :dateOfUsed"),
    @NamedQuery(name = "ApplyPromotionCode.findByUsed", query = "SELECT a FROM ApplyPromotionCode a WHERE a.used = :used"),
    @NamedQuery(name = "ApplyPromotionCode.findByDeleted", query = "SELECT a FROM ApplyPromotionCode a WHERE a.deleted = :deleted")})
public class ApplyPromotionCode implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "apply_promotion_code_ID")
    private Integer applypromotioncodeID;
    @Column(name = "attribution_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date attributionDate;
    @Column(name = "expiration_term")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationTerm;
    @Column(name = "date_of_used")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfUsed;
    @Basic(optional = false)
    @NotNull
    @Column(name = "used")
    private boolean used;
    @Basic(optional = false)
    @NotNull
    @Column(name = "deleted")
    private boolean deleted;
    @JoinColumn(name = "promotion_code_ID", referencedColumnName = "promotion_code_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PromotionCode promotioncodeID;
    @JoinColumn(name = "account_ID", referencedColumnName = "account_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Account accountID;

    public ApplyPromotionCode() {
    }

    public ApplyPromotionCode(Integer applypromotioncodeID) {
        this.applypromotioncodeID = applypromotioncodeID;
    }

    public ApplyPromotionCode(Integer applypromotioncodeID, boolean used, boolean deleted) {
        this.applypromotioncodeID = applypromotioncodeID;
        this.used = used;
        this.deleted = deleted;
    }

    public Integer getApplypromotioncodeID() {
        return applypromotioncodeID;
    }

    public void setApplypromotioncodeID(Integer applypromotioncodeID) {
        this.applypromotioncodeID = applypromotioncodeID;
    }

    public Date getAttributionDate() {
        return attributionDate;
    }

    public void setAttributionDate(Date attributionDate) {
        this.attributionDate = attributionDate;
    }

    public Date getExpirationTerm() {
        return expirationTerm;
    }

    public void setExpirationTerm(Date expirationTerm) {
        this.expirationTerm = expirationTerm;
    }

    public Date getDateOfUsed() {
        return dateOfUsed;
    }

    public void setDateOfUsed(Date dateOfUsed) {
        this.dateOfUsed = dateOfUsed;
    }

    public boolean getUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public PromotionCode getPromotioncodeID() {
        return promotioncodeID;
    }

    public void setPromotioncodeID(PromotionCode promotioncodeID) {
        this.promotioncodeID = promotioncodeID;
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
        hash += (applypromotioncodeID != null ? applypromotioncodeID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ApplyPromotionCode)) {
            return false;
        }
        ApplyPromotionCode other = (ApplyPromotionCode) object;
        if ((this.applypromotioncodeID == null && other.applypromotioncodeID != null) || (this.applypromotioncodeID != null && !this.applypromotioncodeID.equals(other.applypromotioncodeID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ApplyPromotionCode[ applypromotioncodeID=" + applypromotioncodeID + " ]";
    }
    
}
