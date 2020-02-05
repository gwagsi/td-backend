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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author erman
 */
@Entity
@Table(name = "account")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a"),
    @NamedQuery(name = "Account.findByAccountID", query = "SELECT a FROM Account a WHERE a.accountID = :accountID"),
    @NamedQuery(name = "Account.findByLogin", query = "SELECT a FROM Account a WHERE a.login = :login"),
    @NamedQuery(name = "Account.findByPassword", query = "SELECT a FROM Account a WHERE a.password = :password"),
    @NamedQuery(name = "Account.findByActif", query = "SELECT a FROM Account a WHERE a.actif = :actif"),
    @NamedQuery(name = "Account.findBySuspend", query = "SELECT a FROM Account a WHERE a.suspend = :suspend"),
    @NamedQuery(name = "Account.findByConnected", query = "SELECT a FROM Account a WHERE a.connected = :connected"),
    @NamedQuery(name = "Account.findByAccountNumber", query = "SELECT a FROM Account a WHERE a.accountNumber = :accountNumber"),
    @NamedQuery(name = "Account.findByDeleted", query = "SELECT a FROM Account a WHERE a.deleted = :deleted"),
    @NamedQuery(name = "Account.findByCreationDate", query = "SELECT a FROM Account a WHERE a.creationDate = :creationDate"),
    @NamedQuery(name = "Account.findByEmail", query = "SELECT a FROM Account a WHERE a.email = :email")})
public class Account implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accountID")
    private List<Issue> issueList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "accountID")
    private AccountDeleted accountDeleted;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accountID", fetch = FetchType.LAZY)
    private List<JobNotification> jobNotificationList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accountID", fetch = FetchType.LAZY)
    private List<PhoneRegistration> phoneRegistrationList;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "account_ID")
    private Integer accountID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "version")
    @Version
    private int version;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "login")
    private String login;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @NotNull
    @Column(name = "actif")
    private boolean actif;
    @Basic(optional = false)
    @NotNull
    @Column(name = "suspend")
    private boolean suspend;
    @Basic(optional = false)
    @NotNull
    @Column(name = "connected")
    private boolean connected;
    @Basic(optional = false)
    @NotNull
    @Column(name = "account_number")
    private int accountNumber;
    @Basic(optional = false)
    @NotNull
    @Column(name = "deleted")
    private boolean deleted;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "email")
    private String email;
    @OneToOne(mappedBy = "accountID", fetch = FetchType.LAZY)
    private Driver driver;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accountID", fetch = FetchType.LAZY)
    private List<ApplyPromotionCode> applyPromotionCodeList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accountID", fetch = FetchType.LAZY)
    private List<AccountAdministration> accountAdministrationList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "editaccountID", fetch = FetchType.LAZY)
    private List<JobLog> jobLogList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "addaccountID", fetch = FetchType.LAZY)
    private List<JobLog> jobLogList1;
    @JoinColumn(name = "social_status_ID", referencedColumnName = "social_status_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private SocialStatus socialstatusID;
    @JoinColumn(name = "account_type_ID", referencedColumnName = "account_type_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private AccountType accounttypeID;
    @JoinColumn(name = "billing_receiver_ID", referencedColumnName = "billing_receiver_ID")
    @OneToOne(fetch = FetchType.LAZY)
    private BillingReceiver billingreceiverID;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accountID", fetch = FetchType.LAZY)
    private List<AccountHistory> accountHistoryList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accountID", fetch = FetchType.LAZY)
    private List<RealizeService> realizeServiceList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "accountID", fetch = FetchType.LAZY)
    private User user;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accountID", fetch = FetchType.LAZY)
    private List<MonthlyBill> monthlyBillList;

    public Account() {
    }

    public Account(Integer accountID) {
        this.accountID = accountID;
    }

    public Account(Integer accountID, String login, String password, boolean actif, boolean suspend, boolean connected, int accountNumber, boolean deleted, Date creationDate, String email) {
        this.accountID = accountID;
        this.login = login;
        this.password = password;
        this.actif = actif;
        this.suspend = suspend;
        this.connected = connected;
        this.accountNumber = accountNumber;
        this.deleted = deleted;
        this.creationDate = creationDate;
        this.email = email;
    }

    public Integer getAccountID() {
        return accountID;
    }

    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public boolean getSuspend() {
        return suspend;
    }

    public void setSuspend(boolean suspend) {
        this.suspend = suspend;
    }

    public boolean getConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    @XmlTransient
    public List<ApplyPromotionCode> getApplyPromotionCodeList() {
        return applyPromotionCodeList;
    }

    public void setApplyPromotionCodeList(List<ApplyPromotionCode> applyPromotionCodeList) {
        this.applyPromotionCodeList = applyPromotionCodeList;
    }

    @XmlTransient
    public List<AccountAdministration> getAccountAdministrationList() {
        return accountAdministrationList;
    }

    public void setAccountAdministrationList(List<AccountAdministration> accountAdministrationList) {
        this.accountAdministrationList = accountAdministrationList;
    }

    @XmlTransient
    public List<JobLog> getJobLogList() {
        return jobLogList;
    }

    public void setJobLogList(List<JobLog> jobLogList) {
        this.jobLogList = jobLogList;
    }

    @XmlTransient
    public List<JobLog> getJobLogList1() {
        return jobLogList1;
    }

    public void setJobLogList1(List<JobLog> jobLogList1) {
        this.jobLogList1 = jobLogList1;
    }

    public SocialStatus getSocialstatusID() {
        return socialstatusID;
    }

    public void setSocialstatusID(SocialStatus socialstatusID) {
        this.socialstatusID = socialstatusID;
    }

    public AccountType getAccounttypeID() {
        return accounttypeID;
    }

    public void setAccounttypeID(AccountType accounttypeID) {
        this.accounttypeID = accounttypeID;
    }

    public BillingReceiver getBillingreceiverID() {
        return billingreceiverID;
    }

    public void setBillingreceiverID(BillingReceiver billingreceiverID) {
        this.billingreceiverID = billingreceiverID;
    }

    @XmlTransient
    public List<AccountHistory> getAccountHistoryList() {
        return accountHistoryList;
    }

    public void setAccountHistoryList(List<AccountHistory> accountHistoryList) {
        this.accountHistoryList = accountHistoryList;
    }

    @XmlTransient
    public List<RealizeService> getRealizeServiceList() {
        return realizeServiceList;
    }

    public void setRealizeServiceList(List<RealizeService> realizeServiceList) {
        this.realizeServiceList = realizeServiceList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @XmlTransient
    public List<MonthlyBill> getMonthlyBillList() {
        return monthlyBillList;
    }

    public void setMonthlyBillList(List<MonthlyBill> monthlyBillList) {
        this.monthlyBillList = monthlyBillList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (accountID != null ? accountID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Account)) {
            return false;
        }
        Account other = (Account) object;
        if ((this.accountID == null && other.accountID != null) || (this.accountID != null && !this.accountID.equals(other.accountID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Account[ accountID=" + accountID + " ]";
    }

    @XmlTransient
    public List<JobNotification> getJobNotificationList() {
        return jobNotificationList;
    }

    public void setJobNotificationList(List<JobNotification> jobNotificationList) {
        this.jobNotificationList = jobNotificationList;
    }

    @XmlTransient
    public List<PhoneRegistration> getPhoneRegistrationList() {
        return phoneRegistrationList;
    }

    public void setPhoneRegistrationList(List<PhoneRegistration> phoneRegistrationList) {
        this.phoneRegistrationList = phoneRegistrationList;
    }

    public AccountDeleted getAccountDeleted() {
        return accountDeleted;
    }

    public void setAccountDeleted(AccountDeleted accountDeleted) {
        this.accountDeleted = accountDeleted;
    }

    @XmlTransient
    public List<Issue> getIssueList() {
        return issueList;
    }

    public void setIssueList(List<Issue> issueList) {
        this.issueList = issueList;
    }
    
}
