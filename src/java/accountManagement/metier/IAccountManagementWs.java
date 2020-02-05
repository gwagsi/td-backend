/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package accountManagement.metier;

import toolsAndTransversalFunctionnalities.Result;

/**
 *
 * @author erman
 */
public interface IAccountManagementWs {

    /**
     *
     * @param name
     * @param surname
     * @param address
     * @param email
     * @param telephone
     * @param gpsCoordinate
     * @param cardNumber
     * @param login
     * @param password
     * @param servicesID
     * @param socialStatusID
     * @param compagnyName
     * @return
     */
    public String addNewUser(String name, String surname, String address, String email, String telephone, String gpsCoordinate, String cardNumber, String login, String password, String servicesID, int socialStatusID, String compagnyName);
    
    /**
     *
     * @param accountID
     * @param name
     * @param surname
     * @param address
     * @param email
     * @param telephone
     * @param gpsCoordinate
     * @param cardNumber
     * @param compagnyName
     * @param cellPhone
     * @param routineNumber
     * @return
     */
    public String modifyInfo(int accountID, String name, String surname,
            String address, String email, String telephone, String gpsCoordinate, 
            String cardNumber, String compagnyName, String cellPhone, String routineNumber);    
    
    /**
     *
     * @param encryData
     * @return
     */
    public String activate(String encryData);
    
    /**
     *
     * @param accountID
     * @param login
     * @param encienPassword
     * @param newPassword
     * @return
     */
    public String modifyPasswordByLogin(int accountID, String login, String encienPassword, String newPassword);
    
    /**
     *
     * @param accountID
     * @param encienPassword
     * @param newPassword
     * @return
     */
    public String modifyPassword(int accountID, String encienPassword, String newPassword);

    /**
     *
     * @param login
     * @param password
     * @param serviceID
     * @return
     */
    public String login(String login, String password, int serviceID);
    
    /**
     *
     * @param accountID
     * @param accountHistID
     * @param phoneID
     * @return
     */
    public String logout(int accountID, String accountHistID, String phoneID);
    
    /**
     *
     * @param email
     * @return
     */
    public Result recovePassword(String email);
    
    /**
     *
     * @param accountID
     * @return
     */
    public Result getUserByAccountID(int accountID);
    
    /**
     *
     * @param accountID
     * @param serviceID
     * @return
     */
    public String activateService(int accountID, int serviceID);
    
    /**
     *
     * @param accountID
     * @param name
     * @param surname
     * @param securityCode
     * @param address
     * @param email
     * @param telephone
     * @param cellPhone
     * @param creditCard
     * @param cardExpiration
     * @param bankName
     * @param accountNumber
     * @param accountName
     * @param accountTypeID
     * @param promotionCodeID
     * @param monthlyCost
     * @param billingCycle
     * @param ajustedCost
     * @param typeOfCreditCard
     * @param paymentType
     * @param routingNumber
     * @param nameOnCreditCard
     * @return
     */
    public String modifyBillingInfo(int accountID, String name, String surname, String securityCode,
            String address, String email, String telephone, String cellPhone, String creditCard, 
            String cardExpiration, String bankName, String accountNumber, String accountName, int accountTypeID,
            int promotionCodeID, double monthlyCost, String billingCycle, double ajustedCost, String typeOfCreditCard, 
            String paymentType, String routingNumber, String nameOnCreditCard);
    /**
     *
     * @param promotionCodeID
     * @return
     */
    public Result getPromotionCodeByID(int promotionCodeID);
    
    /**
     *
     * @param accountID
     * @param promotionCodeLibel
     * @param accountTypeID
     * @return
     */
    public Result applyPromotionCode(int accountID, String promotionCodeLibel, int accountTypeID);
    
    /**
     *
     * @param accountID
     * @param promotionCodeID
     * @return
     */
    public String assignPromotionCode(int accountID, int promotionCodeID);
    
    /**
     *
     * @param accountID
     * @param accountHEncrypt
     * @param accountNumber
     * @return
     */
    public String checkUserInfo(int accountID, String accountHEncrypt, int accountNumber);
    
    /**
     *
     * @param accountID
     * @param login
     * @param password
     * @param generate
     * @return
     */
    public String retrieveUserCode(int accountID, String login, String password, boolean generate);

    /**
     *
     * @param accountID
     * @param userCode
     * @return
     */
    public String checkExcavatorUserCode(int accountID, String userCode);
    
    /**
     *
     * @param message
     * @param subject
     * @param name
     * @param email
     * @return
     */
    public String contactUs(String message, String subject, String name, String email);
    
    /**
     *
     * @param accountID
     * @return
     */
    public Result getUserStatistics(int accountID);
    
}
