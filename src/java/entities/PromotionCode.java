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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "promotion_code")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PromotionCode.findAll", query = "SELECT p FROM PromotionCode p"),
    @NamedQuery(name = "PromotionCode.findByPromotioncodeID", query = "SELECT p FROM PromotionCode p WHERE p.promotioncodeID = :promotioncodeID"),
    @NamedQuery(name = "PromotionCode.findByLibelle", query = "SELECT p FROM PromotionCode p WHERE p.libelle = :libelle"),
    @NamedQuery(name = "PromotionCode.findByPercent", query = "SELECT p FROM PromotionCode p WHERE p.percent = :percent"),
    @NamedQuery(name = "PromotionCode.findByDescription", query = "SELECT p FROM PromotionCode p WHERE p.description = :description"),
    @NamedQuery(name = "PromotionCode.findByDeleted", query = "SELECT p FROM PromotionCode p WHERE p.deleted = :deleted"),
    @NamedQuery(name = "PromotionCode.findByCreationDate", query = "SELECT p FROM PromotionCode p WHERE p.creationDate = :creationDate")})
public class PromotionCode implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "promotion_code_ID")
    private Integer promotioncodeID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "libelle")
    private String libelle;
    @Basic(optional = false)
    @NotNull
    @Column(name = "percent")
    private int percent;
    @Size(max = 100)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "deleted")
    private boolean deleted;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "promotioncodeID", fetch = FetchType.LAZY)
    private List<ApplyPromotionCode> applyPromotionCodeList;

    public PromotionCode() {
    }

    public PromotionCode(Integer promotioncodeID) {
        this.promotioncodeID = promotioncodeID;
    }

    public PromotionCode(Integer promotioncodeID, String libelle, int percent, boolean deleted, Date creationDate) {
        this.promotioncodeID = promotioncodeID;
        this.libelle = libelle;
        this.percent = percent;
        this.deleted = deleted;
        this.creationDate = creationDate;
    }

    public Integer getPromotioncodeID() {
        return promotioncodeID;
    }

    public void setPromotioncodeID(Integer promotioncodeID) {
        this.promotioncodeID = promotioncodeID;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @XmlTransient
    public List<ApplyPromotionCode> getApplyPromotionCodeList() {
        return applyPromotionCodeList;
    }

    public void setApplyPromotionCodeList(List<ApplyPromotionCode> applyPromotionCodeList) {
        this.applyPromotionCodeList = applyPromotionCodeList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (promotioncodeID != null ? promotioncodeID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PromotionCode)) {
            return false;
        }
        PromotionCode other = (PromotionCode) object;
        if ((this.promotioncodeID == null && other.promotioncodeID != null) || (this.promotioncodeID != null && !this.promotioncodeID.equals(other.promotioncodeID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PromotionCode[ promotioncodeID=" + promotioncodeID + " ]";
    }
    
}
