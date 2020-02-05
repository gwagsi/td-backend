/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package resultInfo;

/**
 *
 * @author erman
 */
public class LoginInfo {
    
    protected int accountID;
    protected boolean actif;
    protected String accHisID;
    protected String name;
    protected String surname;
    protected int socialStatusID;
    protected String socialStatusName;
    protected int accountNumber;
    protected String billingInfo;//"ok" or "bad"
    protected int accountType;
    protected long endOfBillingCycle;
    protected int userAccountID;

    public LoginInfo(int accountID, boolean actif, String accHisID, String name, String surname, int socialStatusID, String socialStatusName, int accountNumber, String billingInfo, int accountType, long endOfBillingCycle, int userAccountID) {
        this.accountID = accountID;
        this.actif = actif;
        this.accHisID = accHisID;
        this.name = name;
        this.surname = surname;
        this.socialStatusID = socialStatusID;
        this.socialStatusName = socialStatusName;
        this.accountNumber = accountNumber;
        this.billingInfo = billingInfo;
        this.accountType = accountType;
        this.endOfBillingCycle = endOfBillingCycle;
        this.userAccountID = userAccountID;
    }

    public String getStringObj(){
        return  + accountID
                + ";" + actif
                + ";" + accHisID
                + ";" + name + " " + surname
                + ";" + socialStatusID
                + ";" + socialStatusName
                + ";" + accountNumber
                + ";" + billingInfo
                + ";" + accountType
                + ";" + endOfBillingCycle
                + ";" + userAccountID
                + ";" + "null"
                ;
    }

    public String getStringObj(String res) {
        return res + ";" + getStringObj();
    }
    
}
