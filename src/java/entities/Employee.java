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
@Table(name = "employee")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Employee.findAll", query = "SELECT e FROM Employee e"),
    @NamedQuery(name = "Employee.findByEmployeeID", query = "SELECT e FROM Employee e WHERE e.employeeID = :employeeID"),
    @NamedQuery(name = "Employee.findByEmpName", query = "SELECT e FROM Employee e WHERE e.empName = :empName"),
    @NamedQuery(name = "Employee.findByEmpCode", query = "SELECT e FROM Employee e WHERE e.empCode = :empCode"),
    @NamedQuery(name = "Employee.findByEmpEmail", query = "SELECT e FROM Employee e WHERE e.empEmail = :empEmail"),
    @NamedQuery(name = "Employee.findByEmpStatus", query = "SELECT e FROM Employee e WHERE e.empStatus = :empStatus"),
    @NamedQuery(name = "Employee.findByPhoneNumber", query = "SELECT e FROM Employee e WHERE e.phoneNumber = :phoneNumber"),
    @NamedQuery(name = "Employee.findByDeleted", query = "SELECT e FROM Employee e WHERE e.deleted = :deleted"),
    @NamedQuery(name = "Employee.findByCreationDate", query = "SELECT e FROM Employee e WHERE e.creationDate = :creationDate")})
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "employee_ID")
    private Integer employeeID;
    @Size(max = 30)
    @Column(name = "emp_name")
    private String empName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "emp_code")
    private String empCode;
    @Size(max = 40)
    @Column(name = "emp_email")
    private String empEmail;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "emp_status")
    private String empStatus;
    @Size(max = 20)
    @Column(name = "phone_number")
    private String phoneNumber;
    @Lob
    @Size(max = 65535)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "deleted")
    private boolean deleted;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @OneToMany(mappedBy = "employeeID", fetch = FetchType.LAZY)
    private List<WeeklyTicket> weeklyTicketList;
    @OneToMany(mappedBy = "employeeID", fetch = FetchType.LAZY)
    private List<DailyTicket> dailyTicketList;
    @JoinColumn(name = "excavator_ID", referencedColumnName = "user_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User excavatorID;

    public Employee() {
    }

    public Employee(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public Employee(Integer employeeID, String empCode, String empStatus, boolean deleted, Date creationDate) {
        this.employeeID = employeeID;
        this.empCode = empCode;
        this.empStatus = empStatus;
        this.deleted = deleted;
        this.creationDate = creationDate;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getEmpEmail() {
        return empEmail;
    }

    public void setEmpEmail(String empEmail) {
        this.empEmail = empEmail;
    }

    public String getEmpStatus() {
        return empStatus;
    }

    public void setEmpStatus(String empStatus) {
        this.empStatus = empStatus;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @XmlTransient
    public List<WeeklyTicket> getWeeklyTicketList() {
        return weeklyTicketList;
    }

    public void setWeeklyTicketList(List<WeeklyTicket> weeklyTicketList) {
        this.weeklyTicketList = weeklyTicketList;
    }

    @XmlTransient
    public List<DailyTicket> getDailyTicketList() {
        return dailyTicketList;
    }

    public void setDailyTicketList(List<DailyTicket> dailyTicketList) {
        this.dailyTicketList = dailyTicketList;
    }

    public User getExcavatorID() {
        return excavatorID;
    }

    public void setExcavatorID(User excavatorID) {
        this.excavatorID = excavatorID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeeID != null ? employeeID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.employeeID == null && other.employeeID != null) || (this.employeeID != null && !this.employeeID.equals(other.employeeID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Employee[ employeeID=" + employeeID + " ]";
    }
    
}
