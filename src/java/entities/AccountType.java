/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
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
@Table(name = "account_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AccountType.findAll", query = "SELECT a FROM AccountType a"),
    @NamedQuery(name = "AccountType.findByAccounttypeID", query = "SELECT a FROM AccountType a WHERE a.accounttypeID = :accounttypeID"),
    @NamedQuery(name = "AccountType.findByPrice", query = "SELECT a FROM AccountType a WHERE a.price = :price"),
    @NamedQuery(name = "AccountType.findByName", query = "SELECT a FROM AccountType a WHERE a.name = :name")})
public class AccountType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "account_type_ID")
    private Integer accounttypeID;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "price")
    private BigDecimal price;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "description")
    private String description;
    @OneToMany(mappedBy = "accounttypeID", fetch = FetchType.LAZY)
    private List<Account> accountList;
    @OneToMany(mappedBy = "accounttypeID", fetch = FetchType.LAZY)
    private List<MonthlyBill> monthlyBillList;

    public AccountType() {
    }

    public AccountType(Integer accounttypeID) {
        this.accounttypeID = accounttypeID;
    }

    public AccountType(Integer accounttypeID, BigDecimal price, String name, String description) {
        this.accounttypeID = accounttypeID;
        this.price = price;
        this.name = name;
        this.description = description;
    }

    public Integer getAccounttypeID() {
        return accounttypeID;
    }

    public void setAccounttypeID(Integer accounttypeID) {
        this.accounttypeID = accounttypeID;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
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
        hash += (accounttypeID != null ? accounttypeID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AccountType)) {
            return false;
        }
        AccountType other = (AccountType) object;
        if ((this.accounttypeID == null && other.accounttypeID != null) || (this.accounttypeID != null && !this.accounttypeID.equals(other.accounttypeID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.AccountType[ accounttypeID=" + accounttypeID + " ]";
    }
    
}
