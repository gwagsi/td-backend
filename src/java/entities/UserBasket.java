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
@Table(name = "user_basket")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserBasket.findAll", query = "SELECT u FROM UserBasket u"),
    @NamedQuery(name = "UserBasket.findByBasketID", query = "SELECT u FROM UserBasket u WHERE u.basketID = :basketID"),
    @NamedQuery(name = "UserBasket.findByBasketLabel", query = "SELECT u FROM UserBasket u WHERE u.basketLabel = :basketLabel"),
    @NamedQuery(name = "UserBasket.findByCreationDate", query = "SELECT u FROM UserBasket u WHERE u.creationDate = :creationDate"),
    @NamedQuery(name = "UserBasket.findByNumberOfTruck", query = "SELECT u FROM UserBasket u WHERE u.numberOfTruck = :numberOfTruck"),
    @NamedQuery(name = "UserBasket.findByConfirmed", query = "SELECT u FROM UserBasket u WHERE u.confirmed = :confirmed"),
    @NamedQuery(name = "UserBasket.findByComplet", query = "SELECT u FROM UserBasket u WHERE u.complet = :complet"),
    @NamedQuery(name = "UserBasket.findByDeleted", query = "SELECT u FROM UserBasket u WHERE u.deleted = :deleted")})
public class UserBasket implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "basket_ID")
    private Integer basketID;
    @Size(max = 100)
    @Column(name = "basket_label")
    private String basketLabel;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "number_of_truck")
    private int numberOfTruck;
    @Basic(optional = false)
    @NotNull
    @Column(name = "confirmed")
    private int confirmed;
    @Basic(optional = false)
    @NotNull
    @Column(name = "complet")
    private boolean complet;
    @Basic(optional = false)
    @NotNull
    @Column(name = "deleted")
    private boolean deleted;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "basketID", fetch = FetchType.LAZY)
    private List<TruckBooking> truckBookingList;

    public UserBasket() {
    }

    public UserBasket(Integer basketID) {
        this.basketID = basketID;
    }

    public UserBasket(Integer basketID, Date creationDate, int numberOfTruck, int confirmed, boolean complet, boolean deleted) {
        this.basketID = basketID;
        this.creationDate = creationDate;
        this.numberOfTruck = numberOfTruck;
        this.confirmed = confirmed;
        this.complet = complet;
        this.deleted = deleted;
    }

    public Integer getBasketID() {
        return basketID;
    }

    public void setBasketID(Integer basketID) {
        this.basketID = basketID;
    }

    public String getBasketLabel() {
        return basketLabel;
    }

    public void setBasketLabel(String basketLabel) {
        this.basketLabel = basketLabel;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public int getNumberOfTruck() {
        return numberOfTruck;
    }

    public void setNumberOfTruck(int numberOfTruck) {
        this.numberOfTruck = numberOfTruck;
    }

    public int getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
    }

    public boolean getComplet() {
        return complet;
    }

    public void setComplet(boolean complet) {
        this.complet = complet;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @XmlTransient
    public List<TruckBooking> getTruckBookingList() {
        return truckBookingList;
    }

    public void setTruckBookingList(List<TruckBooking> truckBookingList) {
        this.truckBookingList = truckBookingList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (basketID != null ? basketID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserBasket)) {
            return false;
        }
        UserBasket other = (UserBasket) object;
        if ((this.basketID == null && other.basketID != null) || (this.basketID != null && !this.basketID.equals(other.basketID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.UserBasket[ basketID=" + basketID + " ]";
    }
    
}
