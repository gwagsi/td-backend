/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
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
@Table(name = "monthly_bill")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MonthlyBill.findAll", query = "SELECT m FROM MonthlyBill m"),
    @NamedQuery(name = "MonthlyBill.findByMonthlybillID", query = "SELECT m FROM MonthlyBill m WHERE m.monthlybillID = :monthlybillID"),
    @NamedQuery(name = "MonthlyBill.findBySuscriptionAmount", query = "SELECT m FROM MonthlyBill m WHERE m.suscriptionAmount = :suscriptionAmount"),
    @NamedQuery(name = "MonthlyBill.findByTaxe", query = "SELECT m FROM MonthlyBill m WHERE m.taxe = :taxe"),
    @NamedQuery(name = "MonthlyBill.findByDeleted", query = "SELECT m FROM MonthlyBill m WHERE m.deleted = :deleted"),
    @NamedQuery(name = "MonthlyBill.findByPaid", query = "SELECT m FROM MonthlyBill m WHERE m.paid = :paid"),
    @NamedQuery(name = "MonthlyBill.findByPaymentDate", query = "SELECT m FROM MonthlyBill m WHERE m.paymentDate = :paymentDate"),
    @NamedQuery(name = "MonthlyBill.findByCreationDate", query = "SELECT m FROM MonthlyBill m WHERE m.creationDate = :creationDate"),
    @NamedQuery(name = "MonthlyBill.findByMonthlyStartDate", query = "SELECT m FROM MonthlyBill m WHERE m.monthlyStartDate = :monthlyStartDate"),
    @NamedQuery(name = "MonthlyBill.findByMonthlyEndDate", query = "SELECT m FROM MonthlyBill m WHERE m.monthlyEndDate = :monthlyEndDate")})
public class MonthlyBill implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "version")
    @Version
    private int version;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "monthly_bill_ID")
    private Integer monthlybillID;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "suscriptionAmount")
    private BigDecimal suscriptionAmount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "taxe")
    private BigDecimal taxe;
    @Basic(optional = false)
    @NotNull
    @Column(name = "deleted")
    private boolean deleted;
    @Basic(optional = false)
    @NotNull
    @Column(name = "paid")
    private boolean paid;
    @Column(name = "payment_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "monthly_start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date monthlyStartDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "monthly_end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date monthlyEndDate;
    @OneToMany(mappedBy = "truckownermonthlybillID", fetch = FetchType.LAZY)
    private List<WeeklyTicket> weeklyTicketList;
    @OneToMany(mappedBy = "excavatormonthlybillID", fetch = FetchType.LAZY)
    private List<WeeklyTicket> weeklyTicketList1;
    @JoinColumn(name = "account_type_ID", referencedColumnName = "account_type_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private AccountType accounttypeID;
    @JoinColumn(name = "account_ID", referencedColumnName = "account_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Account accountID;

    public MonthlyBill() {
    }

    public MonthlyBill(Integer monthlybillID) {
        this.monthlybillID = monthlybillID;
    }

    public MonthlyBill(Integer monthlybillID, BigDecimal suscriptionAmount, BigDecimal taxe, boolean deleted, boolean paid, Date creationDate, Date monthlyStartDate, Date monthlyEndDate) {
        this.monthlybillID = monthlybillID;
        this.suscriptionAmount = suscriptionAmount;
        this.taxe = taxe;
        this.deleted = deleted;
        this.paid = paid;
        this.creationDate = creationDate;
        this.monthlyStartDate = monthlyStartDate;
        this.monthlyEndDate = monthlyEndDate;
    }

    public Integer getMonthlybillID() {
        return monthlybillID;
    }

    public void setMonthlybillID(Integer monthlybillID) {
        this.monthlybillID = monthlybillID;
    }

    public BigDecimal getSuscriptionAmount() {
        return suscriptionAmount;
    }

    public void setSuscriptionAmount(BigDecimal suscriptionAmount) {
        this.suscriptionAmount = suscriptionAmount;
    }

    public BigDecimal getTaxe() {
        return taxe;
    }

    public void setTaxe(BigDecimal taxe) {
        this.taxe = taxe;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getMonthlyStartDate() {
        return monthlyStartDate;
    }

    public void setMonthlyStartDate(Date monthlyStartDate) {
        this.monthlyStartDate = monthlyStartDate;
    }

    public Date getMonthlyEndDate() {
        return monthlyEndDate;
    }

    public void setMonthlyEndDate(Date monthlyEndDate) {
        this.monthlyEndDate = monthlyEndDate;
    }

    @XmlTransient
    public List<WeeklyTicket> getWeeklyTicketList() {
        return weeklyTicketList;
    }

    public void setWeeklyTicketList(List<WeeklyTicket> weeklyTicketList) {
        this.weeklyTicketList = weeklyTicketList;
    }

    @XmlTransient
    public List<WeeklyTicket> getWeeklyTicketList1() {
        return weeklyTicketList1;
    }

    public void setWeeklyTicketList1(List<WeeklyTicket> weeklyTicketList1) {
        this.weeklyTicketList1 = weeklyTicketList1;
    }

    public AccountType getAccounttypeID() {
        return accounttypeID;
    }

    public void setAccounttypeID(AccountType accounttypeID) {
        this.accounttypeID = accounttypeID;
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
        hash += (monthlybillID != null ? monthlybillID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MonthlyBill)) {
            return false;
        }
        MonthlyBill other = (MonthlyBill) object;
        if ((this.monthlybillID == null && other.monthlybillID != null) || (this.monthlybillID != null && !this.monthlybillID.equals(other.monthlybillID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.MonthlyBill[ monthlybillID=" + monthlybillID + " ]";
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
    
}
