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
@Table(name = "dirt_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DirtType.findAll", query = "SELECT d FROM DirtType d"),
    @NamedQuery(name = "DirtType.findByDirttypeID", query = "SELECT d FROM DirtType d WHERE d.dirttypeID = :dirttypeID"),
    @NamedQuery(name = "DirtType.findByLabel", query = "SELECT d FROM DirtType d WHERE d.label = :label"),
    @NamedQuery(name = "DirtType.findByDescription", query = "SELECT d FROM DirtType d WHERE d.description = :description")})
public class DirtType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dirt_type_ID")
    private Integer dirttypeID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "label")
    private String label;
    @Size(max = 100)
    @Column(name = "description")
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "typeofdirtID", fetch = FetchType.LAZY)
    private List<Job> jobList;

    public DirtType() {
    }

    public DirtType(Integer dirttypeID) {
        this.dirttypeID = dirttypeID;
    }

    public DirtType(Integer dirttypeID, String label) {
        this.dirttypeID = dirttypeID;
        this.label = label;
    }

    public Integer getDirttypeID() {
        return dirttypeID;
    }

    public void setDirttypeID(Integer dirttypeID) {
        this.dirttypeID = dirttypeID;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dirttypeID != null ? dirttypeID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DirtType)) {
            return false;
        }
        DirtType other = (DirtType) object;
        if ((this.dirttypeID == null && other.dirttypeID != null) || (this.dirttypeID != null && !this.dirttypeID.equals(other.dirttypeID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.DirtType[ dirttypeID=" + dirttypeID + " ]";
    }
    
}
