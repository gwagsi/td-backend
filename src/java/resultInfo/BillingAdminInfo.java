/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resultInfo;

import java.math.BigDecimal;

/**
 *
 * @author sorelus
 */
public class BillingAdminInfo {

    protected int accountID;
    protected String firstName;
    protected String lastName;
    protected int roleId;
    protected String email;
    protected String telephone;
    protected long nextDate;
    protected BigDecimal amountPaid;

    public BillingAdminInfo(int accountID,
            String firstName,
            String lastName,
            int roleId,
            String email,
            String telephone,
            long nextDate,
            BigDecimal amountPaid) {

        this.accountID = accountID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roleId = roleId;
        this.email = email;
        this.telephone = telephone;
        this.nextDate = nextDate;
        this.amountPaid = amountPaid;

    }

    @Override
    public String toString() {
        return getStringObj();

    }

    public String getStringObj() {

        return accountID + ";" + 
                firstName + ";"
                + lastName + ";"
                + roleId + ";"
                + email + ";"
                + telephone + ";"
                + nextDate + ";"
                + amountPaid + ";";
    }

}
