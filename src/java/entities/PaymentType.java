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
@Table(name = "payment_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PaymentType.findAll", query = "SELECT p FROM PaymentType p"),
    @NamedQuery(name = "PaymentType.findByPaymenttypeID", query = "SELECT p FROM PaymentType p WHERE p.paymenttypeID = :paymenttypeID"),
    @NamedQuery(name = "PaymentType.findByLibel", query = "SELECT p FROM PaymentType p WHERE p.libel = :libel"),
    @NamedQuery(name = "PaymentType.findByDescription", query = "SELECT p FROM PaymentType p WHERE p.description = :description")})
public class PaymentType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "payment_type_ID")
    private Integer paymenttypeID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "libel")
    private String libel;
    @Size(max = 100)
    @Column(name = "description")
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paymenttypeID", fetch = FetchType.LAZY)
    private List<Job> jobList;
    @OneToMany(mappedBy = "paymenttypeID", fetch = FetchType.LAZY)
    private List<JobResponse> jobResponseList;

    public PaymentType() {
    }

    public PaymentType(Integer paymenttypeID) {
        this.paymenttypeID = paymenttypeID;
    }

    public PaymentType(Integer paymenttypeID, String libel) {
        this.paymenttypeID = paymenttypeID;
        this.libel = libel;
    }

    public Integer getPaymenttypeID() {
        return paymenttypeID;
    }

    public void setPaymenttypeID(Integer paymenttypeID) {
        this.paymenttypeID = paymenttypeID;
    }

    public String getLibel() {
        return libel;
    }

    public void setLibel(String libel) {
        this.libel = libel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public List<Job> getJobList() {
        return jobList;
    }

    public void setJobList(List<Job> jobList) {
        this.jobList = jobList;
    }

    @XmlTransient
    public List<JobResponse> getJobResponseList() {
        return jobResponseList;
    }

    public void setJobResponseList(List<JobResponse> jobResponseList) {
        this.jobResponseList = jobResponseList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (paymenttypeID != null ? paymenttypeID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PaymentType)) {
            return false;
        }
        PaymentType other = (PaymentType) object;
        if ((this.paymenttypeID == null && other.paymenttypeID != null) || (this.paymenttypeID != null && !this.paymenttypeID.equals(other.paymenttypeID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PaymentType[ paymenttypeID=" + paymenttypeID + " ]";
    }
    
}
