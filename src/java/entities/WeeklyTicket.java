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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author erman
 */
@Entity
@Table(name = "weekly_ticket")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WeeklyTicket.findAll", query = "SELECT w FROM WeeklyTicket w"),
    @NamedQuery(name = "WeeklyTicket.findByWeeklyticketID", query = "SELECT w FROM WeeklyTicket w WHERE w.weeklyticketID = :weeklyticketID"),
    @NamedQuery(name = "WeeklyTicket.findByWeeklyStartDate", query = "SELECT w FROM WeeklyTicket w WHERE w.weeklyStartDate = :weeklyStartDate"),
    @NamedQuery(name = "WeeklyTicket.findByWeeklyEndDate", query = "SELECT w FROM WeeklyTicket w WHERE w.weeklyEndDate = :weeklyEndDate"),
    @NamedQuery(name = "WeeklyTicket.findByTransactionFees", query = "SELECT w FROM WeeklyTicket w WHERE w.transactionFees = :transactionFees"),
    @NamedQuery(name = "WeeklyTicket.findByValidated", query = "SELECT w FROM WeeklyTicket w WHERE w.validated = :validated"),
    @NamedQuery(name = "WeeklyTicket.findByValidationDate", query = "SELECT w FROM WeeklyTicket w WHERE w.validationDate = :validationDate"),
    @NamedQuery(name = "WeeklyTicket.findBySubmited", query = "SELECT w FROM WeeklyTicket w WHERE w.submited = :submited"),
    @NamedQuery(name = "WeeklyTicket.findByDeleted", query = "SELECT w FROM WeeklyTicket w WHERE w.deleted = :deleted"),
    @NamedQuery(name = "WeeklyTicket.findByCreationDate", query = "SELECT w FROM WeeklyTicket w WHERE w.creationDate = :creationDate"),
    @NamedQuery(name = "WeeklyTicket.findByPaid", query = "SELECT w FROM WeeklyTicket w WHERE w.paid = :paid"),
    @NamedQuery(name = "WeeklyTicket.findByPaymentDate", query = "SELECT w FROM WeeklyTicket w WHERE w.paymentDate = :paymentDate"),
    @NamedQuery(name = "WeeklyTicket.findByPaymentRecieve", query = "SELECT w FROM WeeklyTicket w WHERE w.paymentRecieve = :paymentRecieve"),
    @NamedQuery(name = "WeeklyTicket.findBySettled", query = "SELECT w FROM WeeklyTicket w WHERE w.settled = :settled")})
public class WeeklyTicket implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "version")
    @Version
    private int version;
    @Column(name = "senderREF")
    private Integer senderREF;
    @Column(name = "receiverREF")
    private Integer receiverREF;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "weekly_ticket_ID")
    private Integer weeklyticketID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "weekly_start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date weeklyStartDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "weekly_end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date weeklyEndDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "transaction_fees")
    private double transactionFees;
    @Basic(optional = false)
    @NotNull
    @Column(name = "validated")
    private boolean validated;
    @Column(name = "validation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date validationDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "submited")
    private boolean submited;
    @Basic(optional = false)
    @NotNull
    @Column(name = "deleted")
    private boolean deleted;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "paid")
    private boolean paid;
    @Column(name = "payment_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "payment_recieve")
    private boolean paymentRecieve;
    @Basic(optional = false)
    @NotNull
    @Column(name = "settled")
    private boolean settled;
    @JoinColumn(name = "employee_ID", referencedColumnName = "employee_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Employee employeeID;
    @JoinColumn(name = "truck_owner_monthly_bill_ID", referencedColumnName = "monthly_bill_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MonthlyBill truckownermonthlybillID;
    @JoinColumn(name = "excavator_monthly_bill_ID", referencedColumnName = "monthly_bill_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MonthlyBill excavatormonthlybillID;
    @JoinColumn(name = "truck_ID", referencedColumnName = "truck_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Truck truckID;
    @JoinColumn(name = "job_response_ID", referencedColumnName = "job_response_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private JobResponse jobresponseID;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "weeklyticketID", fetch = FetchType.LAZY)
    private List<DailyTicket> dailyTicketList;

    public WeeklyTicket() {
    }

    public WeeklyTicket(Integer weeklyticketID) {
        this.weeklyticketID = weeklyticketID;
    }

    public WeeklyTicket(Integer weeklyticketID, Date weeklyStartDate, Date weeklyEndDate, double transactionFees, boolean validated, boolean submited, boolean deleted, Date creationDate, boolean paid, boolean paymentRecieve, boolean settled) {
        this.weeklyticketID = weeklyticketID;
        this.weeklyStartDate = weeklyStartDate;
        this.weeklyEndDate = weeklyEndDate;
        this.transactionFees = transactionFees;
        this.validated = validated;
        this.submited = submited;
        this.deleted = deleted;
        this.creationDate = creationDate;
        this.paid = paid;
        this.paymentRecieve = paymentRecieve;
        this.settled = settled;
    }

    public Integer getWeeklyticketID() {
        return weeklyticketID;
    }

    public void setWeeklyticketID(Integer weeklyticketID) {
        this.weeklyticketID = weeklyticketID;
    }

    public Date getWeeklyStartDate() {
        return weeklyStartDate;
    }

    public void setWeeklyStartDate(Date weeklyStartDate) {
        this.weeklyStartDate = weeklyStartDate;
    }

    public Date getWeeklyEndDate() {
        return weeklyEndDate;
    }

    public void setWeeklyEndDate(Date weeklyEndDate) {
        this.weeklyEndDate = weeklyEndDate;
    }

    public double getTransactionFees() {
        return transactionFees;
    }

    public void setTransactionFees(double transactionFees) {
        this.transactionFees = transactionFees;
    }

    public boolean getValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public Date getValidationDate() {
        return validationDate;
    }

    public void setValidationDate(Date validationDate) {
        this.validationDate = validationDate;
    }

    public boolean getSubmited() {
        return submited;
    }

    public void setSubmited(boolean submited) {
        this.submited = submited;
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

    public boolean getPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
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

    public Employee getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Employee employeeID) {
        this.employeeID = employeeID;
    }

    public MonthlyBill getTruckownermonthlybillID() {
        return truckownermonthlybillID;
    }

    public void setTruckownermonthlybillID(MonthlyBill truckownermonthlybillID) {
        this.truckownermonthlybillID = truckownermonthlybillID;
    }

    public MonthlyBill getExcavatormonthlybillID() {
        return excavatormonthlybillID;
    }

    public void setExcavatormonthlybillID(MonthlyBill excavatormonthlybillID) {
        this.excavatormonthlybillID = excavatormonthlybillID;
    }

    public Truck getTruckID() {
        return truckID;
    }

    public void setTruckID(Truck truckID) {
        this.truckID = truckID;
    }

    public JobResponse getJobresponseID() {
        return jobresponseID;
    }

    public void setJobresponseID(JobResponse jobresponseID) {
        this.jobresponseID = jobresponseID;
    }

    @XmlTransient
    public List<DailyTicket> getDailyTicketList() {
        return dailyTicketList;
    }

    public void setDailyTicketList(List<DailyTicket> dailyTicketList) {
        this.dailyTicketList = dailyTicketList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (weeklyticketID != null ? weeklyticketID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WeeklyTicket)) {
            return false;
        }
        WeeklyTicket other = (WeeklyTicket) object;
        if ((this.weeklyticketID == null && other.weeklyticketID != null) || (this.weeklyticketID != null && !this.weeklyticketID.equals(other.weeklyticketID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.WeeklyTicket[ weeklyticketID=" + weeklyticketID + " ]";
    }

    public Integer getSenderREF() {
        return senderREF;
    }

    public void setSenderREF(Integer senderREF) {
        this.senderREF = senderREF;
    }

    public Integer getReceiverREF() {
        return receiverREF;
    }

    public void setReceiverREF(Integer receiverREF) {
        this.receiverREF = receiverREF;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
    
}
