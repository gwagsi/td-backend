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
@Table(name = "dirty_booking")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DirtyBooking.findAll", query = "SELECT d FROM DirtyBooking d"),
    @NamedQuery(name = "DirtyBooking.findByDirtybookingID", query = "SELECT d FROM DirtyBooking d WHERE d.dirtybookingID = :dirtybookingID"),
    @NamedQuery(name = "DirtyBooking.findByBookingStartDate", query = "SELECT d FROM DirtyBooking d WHERE d.bookingStartDate = :bookingStartDate"),
    @NamedQuery(name = "DirtyBooking.findByBookingEndDate", query = "SELECT d FROM DirtyBooking d WHERE d.bookingEndDate = :bookingEndDate"),
    @NamedQuery(name = "DirtyBooking.findByIsBook", query = "SELECT d FROM DirtyBooking d WHERE d.isBook = :isBook")})
public class DirtyBooking implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dirty_booking_ID")
    private Integer dirtybookingID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "booking_start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date bookingStartDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "booking_end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date bookingEndDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "is_book")
    private boolean isBook;
    @JoinColumn(name = "user_ID", referencedColumnName = "user_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User userID;
    @JoinColumn(name = "dirty_ID", referencedColumnName = "dirty_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Dirty dirtyID;

    public DirtyBooking() {
    }

    public DirtyBooking(Integer dirtybookingID) {
        this.dirtybookingID = dirtybookingID;
    }

    public DirtyBooking(Integer dirtybookingID, Date bookingStartDate, Date bookingEndDate, boolean isBook) {
        this.dirtybookingID = dirtybookingID;
        this.bookingStartDate = bookingStartDate;
        this.bookingEndDate = bookingEndDate;
        this.isBook = isBook;
    }

    public Integer getDirtybookingID() {
        return dirtybookingID;
    }

    public void setDirtybookingID(Integer dirtybookingID) {
        this.dirtybookingID = dirtybookingID;
    }

    public Date getBookingStartDate() {
        return bookingStartDate;
    }

    public void setBookingStartDate(Date bookingStartDate) {
        this.bookingStartDate = bookingStartDate;
    }

    public Date getBookingEndDate() {
        return bookingEndDate;
    }

    public void setBookingEndDate(Date bookingEndDate) {
        this.bookingEndDate = bookingEndDate;
    }

    public boolean getIsBook() {
        return isBook;
    }

    public void setIsBook(boolean isBook) {
        this.isBook = isBook;
    }

    public User getUserID() {
        return userID;
    }

    public void setUserID(User userID) {
        this.userID = userID;
    }

    public Dirty getDirtyID() {
        return dirtyID;
    }

    public void setDirtyID(Dirty dirtyID) {
        this.dirtyID = dirtyID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dirtybookingID != null ? dirtybookingID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DirtyBooking)) {
            return false;
        }
        DirtyBooking other = (DirtyBooking) object;
        if ((this.dirtybookingID == null && other.dirtybookingID != null) || (this.dirtybookingID != null && !this.dirtybookingID.equals(other.dirtybookingID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.DirtyBooking[ dirtybookingID=" + dirtybookingID + " ]";
    }
    
}
