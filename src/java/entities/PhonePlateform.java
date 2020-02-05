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
 * @author camertronix
 */
@Entity
@Table(name = "phone_plateform")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PhonePlateform.findAll", query = "SELECT p FROM PhonePlateform p"),
    @NamedQuery(name = "PhonePlateform.findByPhoneplateformID", query = "SELECT p FROM PhonePlateform p WHERE p.phoneplateformID = :phoneplateformID"),
    @NamedQuery(name = "PhonePlateform.findByPlateformName", query = "SELECT p FROM PhonePlateform p WHERE p.plateformName = :plateformName"),
    @NamedQuery(name = "PhonePlateform.findByDescription", query = "SELECT p FROM PhonePlateform p WHERE p.description = :description")})
public class PhonePlateform implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "phone_plateform_ID")
    private Integer phoneplateformID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "plateform_name")
    private String plateformName;
    @Size(max = 200)
    @Column(name = "description")
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "plateformID", fetch = FetchType.LAZY)
    private List<PhoneRegistration> phoneRegistrationList;

    public PhonePlateform() {
    }

    public PhonePlateform(Integer phoneplateformID) {
        this.phoneplateformID = phoneplateformID;
    }

    public PhonePlateform(Integer phoneplateformID, String plateformName) {
        this.phoneplateformID = phoneplateformID;
        this.plateformName = plateformName;
    }

    public Integer getPhoneplateformID() {
        return phoneplateformID;
    }

    public void setPhoneplateformID(Integer phoneplateformID) {
        this.phoneplateformID = phoneplateformID;
    }

    public String getPlateformName() {
        return plateformName;
    }

    public void setPlateformName(String plateformName) {
        this.plateformName = plateformName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public List<PhoneRegistration> getPhoneRegistrationList() {
        return phoneRegistrationList;
    }

    public void setPhoneRegistrationList(List<PhoneRegistration> phoneRegistrationList) {
        this.phoneRegistrationList = phoneRegistrationList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (phoneplateformID != null ? phoneplateformID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PhonePlateform)) {
            return false;
        }
        PhonePlateform other = (PhonePlateform) object;
        if ((this.phoneplateformID == null && other.phoneplateformID != null) || (this.phoneplateformID != null && !this.phoneplateformID.equals(other.phoneplateformID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PhonePlateform[ phoneplateformID=" + phoneplateformID + " ]";
    }
    
}
