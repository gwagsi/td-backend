/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author erman
 */
@Entity
@Table(name = "billing_receiver")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BillingReceiver.findAll", query = "SELECT b FROM BillingReceiver b"),
    @NamedQuery(name = "BillingReceiver.findByBillingreceiverID", query = "SELECT b FROM BillingReceiver b WHERE b.billingreceiverID = :billingreceiverID"),
    @NamedQuery(name = "BillingReceiver.findByName", query = "SELECT b FROM BillingReceiver b WHERE b.name = :name"),
    @NamedQuery(name = "BillingReceiver.findBySurname", query = "SELECT b FROM BillingReceiver b WHERE b.surname = :surname"),
    @NamedQuery(name = "BillingReceiver.findByAddress", query = "SELECT b FROM BillingReceiver b WHERE b.address = :address"),
    @NamedQuery(name = "BillingReceiver.findByEmail", query = "SELECT b FROM BillingReceiver b WHERE b.email = :email"),
    @NamedQuery(name = "BillingReceiver.findBySecurityCode", query = "SELECT b FROM BillingReceiver b WHERE b.securityCode = :securityCode"),
    @NamedQuery(name = "BillingReceiver.findByTelephone", query = "SELECT b FROM BillingReceiver b WHERE b.telephone = :telephone"),
    @NamedQuery(name = "BillingReceiver.findByCellPhone", query = "SELECT b FROM BillingReceiver b WHERE b.cellPhone = :cellPhone"),
    @NamedQuery(name = "BillingReceiver.findByCreationDate", query = "SELECT b FROM BillingReceiver b WHERE b.creationDate = :creationDate"),
    @NamedQuery(name = "BillingReceiver.findByMonthlyCost", query = "SELECT b FROM BillingReceiver b WHERE b.monthlyCost = :monthlyCost"),
    @NamedQuery(name = "BillingReceiver.findByAjustedCost", query = "SELECT b FROM BillingReceiver b WHERE b.ajustedCost = :ajustedCost"),
    @NamedQuery(name = "BillingReceiver.findByBillingCycle", query = "SELECT b FROM BillingReceiver b WHERE b.billingCycle = :billingCycle"),
    @NamedQuery(name = "BillingReceiver.findByEndOfBillingCycle", query = "SELECT b FROM BillingReceiver b WHERE b.endOfBillingCycle = :endOfBillingCycle"),
    @NamedQuery(name = "BillingReceiver.findBySameAsOnwer", query = "SELECT b FROM BillingReceiver b WHERE b.sameAsOnwer = :sameAsOnwer"),
    @NamedQuery(name = "BillingReceiver.findByCreditCardNumber", query = "SELECT b FROM BillingReceiver b WHERE b.creditCardNumber = :creditCardNumber"),
    @NamedQuery(name = "BillingReceiver.findByCreditCardExpiration", query = "SELECT b FROM BillingReceiver b WHERE b.creditCardExpiration = :creditCardExpiration"),
    @NamedQuery(name = "BillingReceiver.findByPaymentType", query = "SELECT b FROM BillingReceiver b WHERE b.paymentType = :paymentType"),
    @NamedQuery(name = "BillingReceiver.findByTypeOfCreditCard", query = "SELECT b FROM BillingReceiver b WHERE b.typeOfCreditCard = :typeOfCreditCard"),
    @NamedQuery(name = "BillingReceiver.findByApicustumID", query = "SELECT b FROM BillingReceiver b WHERE b.apicustumID = :apicustumID"),
    @NamedQuery(name = "BillingReceiver.findByDefaultpaymentmethodID", query = "SELECT b FROM BillingReceiver b WHERE b.defaultpaymentmethodID = :defaultpaymentmethodID"),
    @NamedQuery(name = "BillingReceiver.findByNameOnCreditCard", query = "SELECT b FROM BillingReceiver b WHERE b.nameOnCreditCard = :nameOnCreditCard")})
public class BillingReceiver implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "version")
    @Version
    private int version;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "billing_receiver_ID")
    private Integer billingreceiverID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "surname")
    private String surname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "address")
    private String address;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "email")
    private String email;
    @Size(max = 100)
    @Column(name = "security_code")
    private String securityCode;
    @Size(max = 30)
    @Column(name = "telephone")
    private String telephone;
    @Size(max = 30)
    @Column(name = "cell_phone")
    private String cellPhone;
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "monthly_cost")
    private BigDecimal monthlyCost;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ajusted_cost")
    private BigDecimal ajustedCost;
    @Size(max = 100)
    @Column(name = "billing_cycle")
    private String billingCycle;
    @Column(name = "end_of_billing_cycle")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endOfBillingCycle;
    @Basic(optional = false)
    @NotNull
    @Column(name = "same_as_onwer")
    private boolean sameAsOnwer;
    @Size(max = 100)
    @Column(name = "credit_card_number")
    private String creditCardNumber;
    @Size(max = 100)
    @Column(name = "credit_card_expiration")
    private String creditCardExpiration;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "payment_type")
    private String paymentType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "type_of_credit_card")
    private String typeOfCreditCard;
    @Size(max = 100)
    @Column(name = "api_custum_ID")
    private String apicustumID;
    @Size(max = 100)
    @Column(name = "default_payment_method_ID")
    private String defaultpaymentmethodID;
    @Size(max = 100)
    @Column(name = "name_on_credit_card")
    private String nameOnCreditCard;
    @OneToOne(mappedBy = "billingreceiverID", fetch = FetchType.LAZY)
    private Account account;

    public BillingReceiver() {
    }

    public BillingReceiver(Integer billingreceiverID) {
        this.billingreceiverID = billingreceiverID;
    }

    public BillingReceiver(Integer billingreceiverID, String name, String surname, String address, String email, BigDecimal monthlyCost, BigDecimal ajustedCost, boolean sameAsOnwer, String paymentType, String typeOfCreditCard) {
        this.billingreceiverID = billingreceiverID;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.email = email;
        this.monthlyCost = monthlyCost;
        this.ajustedCost = ajustedCost;
        this.sameAsOnwer = sameAsOnwer;
        this.paymentType = paymentType;
        this.typeOfCreditCard = typeOfCreditCard;
    }

    public Integer getBillingreceiverID() {
        return billingreceiverID;
    }

    public void setBillingreceiverID(Integer billingreceiverID) {
        this.billingreceiverID = billingreceiverID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public BigDecimal getMonthlyCost() {
        return monthlyCost;
    }

    public void setMonthlyCost(BigDecimal monthlyCost) {
        this.monthlyCost = monthlyCost;
    }

    public BigDecimal getAjustedCost() {
        return ajustedCost;
    }

    public void setAjustedCost(BigDecimal ajustedCost) {
        this.ajustedCost = ajustedCost;
    }

    public String getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(String billingCycle) {
        this.billingCycle = billingCycle;
    }

    public Date getEndOfBillingCycle() {
        return endOfBillingCycle;
    }

    public void setEndOfBillingCycle(Date endOfBillingCycle) {
        this.endOfBillingCycle = endOfBillingCycle;
    }

    public boolean getSameAsOnwer() {
        return sameAsOnwer;
    }

    public void setSameAsOnwer(boolean sameAsOnwer) {
        this.sameAsOnwer = sameAsOnwer;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getCreditCardExpiration() {
        return creditCardExpiration;
    }

    public void setCreditCardExpiration(String creditCardExpiration) {
        this.creditCardExpiration = creditCardExpiration;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getTypeOfCreditCard() {
        return typeOfCreditCard;
    }

    public void setTypeOfCreditCard(String typeOfCreditCard) {
        this.typeOfCreditCard = typeOfCreditCard;
    }

    public String getApicustumID() {
        return apicustumID;
    }

    public void setApicustumID(String apicustumID) {
        this.apicustumID = apicustumID;
    }

    public String getDefaultpaymentmethodID() {
        return defaultpaymentmethodID;
    }

    public void setDefaultpaymentmethodID(String defaultpaymentmethodID) {
        this.defaultpaymentmethodID = defaultpaymentmethodID;
    }

    public String getNameOnCreditCard() {
        return nameOnCreditCard;
    }

    public void setNameOnCreditCard(String nameOnCreditCard) {
        this.nameOnCreditCard = nameOnCreditCard;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (billingreceiverID != null ? billingreceiverID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BillingReceiver)) {
            return false;
        }
        BillingReceiver other = (BillingReceiver) object;
        if ((this.billingreceiverID == null && other.billingreceiverID != null) || (this.billingreceiverID != null && !this.billingreceiverID.equals(other.billingreceiverID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.BillingReceiver[ billingreceiverID=" + billingreceiverID + " ]";
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
    
}
