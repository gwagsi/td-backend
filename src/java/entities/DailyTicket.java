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
@Table(name = "daily_ticket")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DailyTicket.findAll", query = "SELECT d FROM DailyTicket d"),
    @NamedQuery(name = "DailyTicket.findByDailyticketID", query = "SELECT d FROM DailyTicket d WHERE d.dailyticketID = :dailyticketID"),
    @NamedQuery(name = "DailyTicket.findByTicketDate", query = "SELECT d FROM DailyTicket d WHERE d.ticketDate = :ticketDate"),
    @NamedQuery(name = "DailyTicket.findByState", query = "SELECT d FROM DailyTicket d WHERE d.state = :state"),
    @NamedQuery(name = "DailyTicket.findByValidationDate", query = "SELECT d FROM DailyTicket d WHERE d.validationDate = :validationDate"),
    @NamedQuery(name = "DailyTicket.findByViewTicket", query = "SELECT d FROM DailyTicket d WHERE d.viewTicket = :viewTicket"),
    @NamedQuery(name = "DailyTicket.findByDeleted", query = "SELECT d FROM DailyTicket d WHERE d.deleted = :deleted"),
    @NamedQuery(name = "DailyTicket.findByCreationDate", query = "SELECT d FROM DailyTicket d WHERE d.creationDate = :creationDate")})
public class DailyTicket implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "version")
    @Version
    private int version;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "daily_ticket_ID")
    private Integer dailyticketID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ticket_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ticketDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "state")
    private boolean state;
    @Column(name = "validation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date validationDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "view_ticket")
    private boolean viewTicket;
    @Basic(optional = false)
    @NotNull
    @Column(name = "deleted")
    private boolean deleted;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @JoinColumn(name = "employee_ID", referencedColumnName = "employee_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Employee employeeID;
    @JoinColumn(name = "truck_ID", referencedColumnName = "truck_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Truck truckID;
    @JoinColumn(name = "weekly_ticket_ID", referencedColumnName = "weekly_ticket_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private WeeklyTicket weeklyticketID;
    @JoinColumn(name = "job_response_ID", referencedColumnName = "job_response_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private JobResponse jobresponseID;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dailyticketID", fetch = FetchType.LAZY)
    private List<JobLog> jobLogList;

    public DailyTicket() {
    }

    public DailyTicket(Integer dailyticketID) {
        this.dailyticketID = dailyticketID;
    }

    public DailyTicket(Integer dailyticketID, Date ticketDate, boolean state, boolean viewTicket, boolean deleted, Date creationDate) {
        this.dailyticketID = dailyticketID;
        this.ticketDate = ticketDate;
        this.state = state;
        this.viewTicket = viewTicket;
        this.deleted = deleted;
        this.creationDate = creationDate;
    }

    public Integer getDailyticketID() {
        return dailyticketID;
    }

    public void setDailyticketID(Integer dailyticketID) {
        this.dailyticketID = dailyticketID;
    }

    public Date getTicketDate() {
        return ticketDate;
    }

    public void setTicketDate(Date ticketDate) {
        this.ticketDate = ticketDate;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public Date getValidationDate() {
        return validationDate;
    }

    public void setValidationDate(Date validationDate) {
        this.validationDate = validationDate;
    }

    public boolean getViewTicket() {
        return viewTicket;
    }

    public void setViewTicket(boolean viewTicket) {
        this.viewTicket = viewTicket;
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

    public Employee getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Employee employeeID) {
        this.employeeID = employeeID;
    }

    public Truck getTruckID() {
        return truckID;
    }

    public void setTruckID(Truck truckID) {
        this.truckID = truckID;
    }

    public WeeklyTicket getWeeklyticketID() {
        return weeklyticketID;
    }

    public void setWeeklyticketID(WeeklyTicket weeklyticketID) {
        this.weeklyticketID = weeklyticketID;
    }

    public JobResponse getJobresponseID() {
        return jobresponseID;
    }

    public void setJobresponseID(JobResponse jobresponseID) {
        this.jobresponseID = jobresponseID;
    }

    @XmlTransient
    public List<JobLog> getJobLogList() {
        return jobLogList;
    }

    public void setJobLogList(List<JobLog> jobLogList) {
        this.jobLogList = jobLogList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dailyticketID != null ? dailyticketID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DailyTicket)) {
            return false;
        }
        DailyTicket other = (DailyTicket) object;
        if ((this.dailyticketID == null && other.dailyticketID != null) || (this.dailyticketID != null && !this.dailyticketID.equals(other.dailyticketID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.DailyTicket[ dailyticketID=" + dailyticketID + " ]";
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
    
}
