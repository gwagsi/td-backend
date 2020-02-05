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
import javax.persistence.Lob;
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
 * @author erman
 */
@Entity
@Table(name = "truck_booking")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TruckBooking.findAll", query = "SELECT t FROM TruckBooking t"),
    @NamedQuery(name = "TruckBooking.findByTruckbookingID", query = "SELECT t FROM TruckBooking t WHERE t.truckbookingID = :truckbookingID"),
    @NamedQuery(name = "TruckBooking.findByCreationDate", query = "SELECT t FROM TruckBooking t WHERE t.creationDate = :creationDate"),
    @NamedQuery(name = "TruckBooking.findByDeleted", query = "SELECT t FROM TruckBooking t WHERE t.deleted = :deleted"),
    @NamedQuery(name = "TruckBooking.findByValidated", query = "SELECT t FROM TruckBooking t WHERE t.validated = :validated"),
    @NamedQuery(name = "TruckBooking.findByRejected", query = "SELECT t FROM TruckBooking t WHERE t.rejected = :rejected"),
    @NamedQuery(name = "TruckBooking.findByPaid", query = "SELECT t FROM TruckBooking t WHERE t.paid = :paid"),
    @NamedQuery(name = "TruckBooking.findByPaymentRecieve", query = "SELECT t FROM TruckBooking t WHERE t.paymentRecieve = :paymentRecieve"),
    @NamedQuery(name = "TruckBooking.findBySettled", query = "SELECT t FROM TruckBooking t WHERE t.settled = :settled"),
    @NamedQuery(name = "TruckBooking.findBySubmitReview", query = "SELECT t FROM TruckBooking t WHERE t.submitReview = :submitReview"),
    @NamedQuery(name = "TruckBooking.findByBookingPrice", query = "SELECT t FROM TruckBooking t WHERE t.bookingPrice = :bookingPrice"),
    @NamedQuery(name = "TruckBooking.findByClientValidation", query = "SELECT t FROM TruckBooking t WHERE t.clientValidation = :clientValidation"),
    @NamedQuery(name = "TruckBooking.findByTruckOwnerValidation", query = "SELECT t FROM TruckBooking t WHERE t.truckOwnerValidation = :truckOwnerValidation"),
    @NamedQuery(name = "TruckBooking.findByClientValidationDate", query = "SELECT t FROM TruckBooking t WHERE t.clientValidationDate = :clientValidationDate"),
    @NamedQuery(name = "TruckBooking.findByTruckOwnerValidationDate", query = "SELECT t FROM TruckBooking t WHERE t.truckOwnerValidationDate = :truckOwnerValidationDate")})
public class TruckBooking implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "truck_booking_ID")
    private Integer truckbookingID;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "booking_date")
    private String bookingDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "deleted")
    private boolean deleted;
    @Basic(optional = false)
    @NotNull
    @Column(name = "validated")
    private boolean validated;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rejected")
    private boolean rejected;
    @Basic(optional = false)
    @NotNull
    @Column(name = "paid")
    private boolean paid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "payment_recieve")
    private boolean paymentRecieve;
    @Basic(optional = false)
    @NotNull
    @Column(name = "settled")
    private boolean settled;
    @Basic(optional = false)
    @NotNull
    @Column(name = "submit_review")
    private int submitReview;
    @Lob
    @Size(max = 65535)
    @Column(name = "review_comment")
    private String reviewComment;
    @Basic(optional = false)
    @NotNull
    @Column(name = "booking_price")
    private float bookingPrice;
    @Basic(optional = false)
    @NotNull
    @Column(name = "client_validation")
    private int clientValidation;
    @Basic(optional = false)
    @NotNull
    @Column(name = "truck_owner_validation")
    private int truckOwnerValidation;
    @Column(name = "client_validation_date")
    @Temporal(TemporalType.DATE)
    private Date clientValidationDate;
    @Column(name = "truck_owner_validation_date")
    @Temporal(TemporalType.DATE)
    private Date truckOwnerValidationDate;
    @JoinColumn(name = "basket_ID", referencedColumnName = "basket_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private UserBasket basketID;
    @JoinColumn(name = "client_ID", referencedColumnName = "user_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User clientID;
    @JoinColumn(name = "truck_owner_ID", referencedColumnName = "user_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User truckownerID;
    @JoinColumn(name = "truck_ID", referencedColumnName = "truck_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Truck truckID;

    public TruckBooking() {
    }

    public TruckBooking(Integer truckbookingID) {
        this.truckbookingID = truckbookingID;
    }

    public TruckBooking(Integer truckbookingID, String bookingDate, Date creationDate, boolean deleted, boolean validated, boolean rejected, boolean paid, boolean paymentRecieve, boolean settled, int submitReview, float bookingPrice, int clientValidation, int truckOwnerValidation) {
        this.truckbookingID = truckbookingID;
        this.bookingDate = bookingDate;
        this.creationDate = creationDate;
        this.deleted = deleted;
        this.validated = validated;
        this.rejected = rejected;
        this.paid = paid;
        this.paymentRecieve = paymentRecieve;
        this.settled = settled;
        this.submitReview = submitReview;
        this.bookingPrice = bookingPrice;
        this.clientValidation = clientValidation;
        this.truckOwnerValidation = truckOwnerValidation;
    }

    public Integer getTruckbookingID() {
        return truckbookingID;
    }

    public void setTruckbookingID(Integer truckbookingID) {
        this.truckbookingID = truckbookingID;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
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

    public boolean getValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public boolean getRejected() {
        return rejected;
    }

    public void setRejected(boolean rejected) {
        this.rejected = rejected;
    }

    public boolean getPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public boolean getPaymentRecieve() {
        return paymentRecieve;
    }

    public void setPaymentRecieve(boolean paymentRecieve) {
        this.paymentRecieve = paymentRecieve;
    }

    public boolean getSettled() {
        return settled;
    }

    public void setSettled(boolean settled) {
        this.settled = settled;
    }

    public int getSubmitReview() {
        return submitReview;
    }

    public void setSubmitReview(int submitReview) {
        this.submitReview = submitReview;
    }

    public String getReviewComment() {
        return reviewComment;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }

    public float getBookingPrice() {
        return bookingPrice;
    }

    public void setBookingPrice(float bookingPrice) {
        this.bookingPrice = bookingPrice;
    }

    public int getClientValidation() {
        return clientValidation;
    }

    public void setClientValidation(int clientValidation) {
        this.clientValidation = clientValidation;
    }

    public int getTruckOwnerValidation() {
        return truckOwnerValidation;
    }

    public void setTruckOwnerValidation(int truckOwnerValidation) {
        this.truckOwnerValidation = truckOwnerValidation;
    }

    public Date getClientValidationDate() {
        return clientValidationDate;
    }

    public void setClientValidationDate(Date clientValidationDate) {
        this.clientValidationDate = clientValidationDate;
    }

    public Date getTruckOwnerValidationDate() {
        return truckOwnerValidationDate;
    }

    public void setTruckOwnerValidationDate(Date truckOwnerValidationDate) {
        this.truckOwnerValidationDate = truckOwnerValidationDate;
    }

    public UserBasket getBasketID() {
        return basketID;
    }

    public void setBasketID(UserBasket basketID) {
        this.basketID = basketID;
    }

    public User getClientID() {
        return clientID;
    }

    public void setClientID(User clientID) {
        this.clientID = clientID;
    }

    public User getTruckownerID() {
        return truckownerID;
    }

    public void setTruckownerID(User truckownerID) {
        this.truckownerID = truckownerID;
    }

    public Truck getTruckID() {
        return truckID;
    }

    public void setTruckID(Truck truckID) {
        this.truckID = truckID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (truckbookingID != null ? truckbookingID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TruckBooking)) {
            return false;
        }
        TruckBooking other = (TruckBooking) object;
        if ((this.truckbookingID == null && other.truckbookingID != null) || (this.truckbookingID != null && !this.truckbookingID.equals(other.truckbookingID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.TruckBooking[ truckbookingID=" + truckbookingID + " ]";
    }
    
}
