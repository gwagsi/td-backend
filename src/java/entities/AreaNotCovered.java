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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "area_not_covered")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AreaNotCovered.findAll", query = "SELECT a FROM AreaNotCovered a"),
    @NamedQuery(name = "AreaNotCovered.findByAreaNotCovered", query = "SELECT a FROM AreaNotCovered a WHERE a.areaNotCovered = :areaNotCovered"),
    @NamedQuery(name = "AreaNotCovered.findByZipCode", query = "SELECT a FROM AreaNotCovered a WHERE a.zipCode = :zipCode"),
    @NamedQuery(name = "AreaNotCovered.findByEmail", query = "SELECT a FROM AreaNotCovered a WHERE a.email = :email"),
    @NamedQuery(name = "AreaNotCovered.findByStatus", query = "SELECT a FROM AreaNotCovered a WHERE a.status = :status"),
    @NamedQuery(name = "AreaNotCovered.findByCreationDate", query = "SELECT a FROM AreaNotCovered a WHERE a.creationDate = :creationDate"),
    @NamedQuery(name = "AreaNotCovered.findByDeleted", query = "SELECT a FROM AreaNotCovered a WHERE a.deleted = :deleted"),
    @NamedQuery(name = "AreaNotCovered.findByDate", query = "SELECT a FROM AreaNotCovered a WHERE a.date = :date")})
public class AreaNotCovered implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "area_not_covered")
    private Integer areaNotCovered;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "zip_code")
    private String zipCode;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private boolean status;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.DATE)
    private Date creationDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "deleted")
    private boolean deleted;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "date")
    private String date;

    public AreaNotCovered() {
    }

    public AreaNotCovered(Integer areaNotCovered) {
        this.areaNotCovered = areaNotCovered;
    }

    public AreaNotCovered(Integer areaNotCovered, String zipCode, String email, boolean status, Date creationDate, boolean deleted, String date) {
        this.areaNotCovered = areaNotCovered;
        this.zipCode = zipCode;
        this.email = email;
        this.status = status;
        this.creationDate = creationDate;
        this.deleted = deleted;
        this.date = date;
    }

    public Integer getAreaNotCovered() {
        return areaNotCovered;
    }

    public void setAreaNotCovered(Integer areaNotCovered) {
        this.areaNotCovered = areaNotCovered;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (areaNotCovered != null ? areaNotCovered.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AreaNotCovered)) {
            return false;
        }
        AreaNotCovered other = (AreaNotCovered) object;
        if ((this.areaNotCovered == null && other.areaNotCovered != null) || (this.areaNotCovered != null && !this.areaNotCovered.equals(other.areaNotCovered))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.AreaNotCovered[ areaNotCovered=" + areaNotCovered + " ]";
    }
    
}
