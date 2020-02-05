/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package accountManagement.dao;

import toolsAndTransversalFunctionnalities.Result;
import util.exception.AccountManagementException;

/**
 *
 * @author erman
 */
public interface IAccountManagementDao {

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
     * @throws util.exception.AccountManagementException
     */
    public String addNewUser(String name, String surname, String address, String email, String telephone, String gpsCoordinate, String cardNumber, String login, String password, String servicesID, int socialStatusID, String compagnyName)throws AccountManagementException;
    
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
     * @param cellPhone
     * @param compagnyName
     * @param routineNumber
     * @return
     * @throws util.exception.AccountManagementException
     */
    public String modifyInfo(int accountID, String name, String surname,
            String address, String email, String telephone, String gpsCoordinate, 
            String cardNumber, String compagnyName, String cellPhone, String routineNumber)throws AccountManagementException;    
    /**
     *
     * @param encryData
     * @return
     * @throws util.exception.AccountManagementException
     */
    public String activate(String encryData)throws AccountManagementException;
    
    /**
     *
     * @param accountID
     * @param login
     * @param encienPassword
     * @param newPassword
     * @return
     * @throws util.exception.AccountManagementException
     */
    public String modifyPasswordByLogin(int accountID, String login, String encienPassword, String newPassword)throws AccountManagementException;
    
    /**
     *
     * @param accountID
     * @param encienPassword
     * @param newPassword
     * @return
     * @throws util.exception.AccountManagementException
     */
    public String modifyPassword(int accountID, String encienPassword, String newPassword)throws AccountManagementException;
    
    /**
     *
     * @param login
     * @param password
     * @param serviceID
     * @return
     * @throws util.exception.AccountManagementException
     */
    public String login(String login, String password, int serviceID)throws AccountManagementException;

    /**
     *
     * @param accountID
     * @param sessionID
     * @param phoneID
     * @return
     * @throws util.exception.AccountManagementException
     */
    public String logout(int accountID, String sessionID, String phoneID)throws AccountManagementException;

    /**
     *
     * @param accountID
     * @return
     * @throws util.exception.AccountManagementException
     */
    public Result getUserByAccountID(int accountID)throws AccountManagementException;
    
    /**
     *
     * @param email
     * @return
     * @throws util.exception.AccountManagementException
     */
    public Result recoverPasswd(String email)throws AccountManagementException;
    
    /**
     * @throws util.exception.AccountManagementException
     * @Description : permet d'activer un service pour un client dont l'identifiant est
     *  passer en parametre
     * @param accountID identifiant du client dont on veut activer le services
     * @param serviceID identifant du service a activer
     * @return
     */
    public String activateService(int accountID, int serviceID)throws AccountManagementException;
    
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
     * @param expirationDate
     * @param bankName
     * @param accountNumber
     * @param accountName
     * @param accountTypeID
     * @param promotionCodeID
     * @param billingCycle
     * @param typeOfCreditCard
     * @param paymentType
     * @param routingNumber
     * @param nameOnCreditCard
     * @return
     * @throws util.exception.AccountManagementException
     */
    public String modifyBillingInfo(int accountID, String name, String surname, String securityCode,
            String address, String email, String telephone, String cellPhone, String creditCard, 
            String expirationDate, String bankName, String accountNumber, String accountName, int accountTypeID,
            int promotionCodeID, String billingCycle, String typeOfCreditCard, 
            String paymentType, String routingNumber, String nameOnCreditCard)throws AccountManagementException;
    
    /**
     *
     * @param promotionCodeID
     * @return
     * @throws util.exception.AccountManagementException
     */
    public Result getPromotionCodeByID(int promotionCodeID)throws AccountManagementException;

    /**
     *
     * @param accountID
     * @param promotionCodeLibel
     * @param accountTypeID
     * @return
     * @throws util.exception.AccountManagementException
     */
    public Result applyPromotionCode(int accountID, String promotionCodeLibel, int accountTypeID)throws AccountManagementException;

    /**
     *
     * @param accountID
     * @param promotionCodeID
     * @return
     * @throws util.exception.AccountManagementException
     */
    public String assignPromotionCode(int accountID, int promotionCodeID)throws AccountManagementException;

    /**
     *
     * @param accountID
     * @param accountHEncrypt
     * @param accountNumber
     * @return
     * @throws util.exception.AccountManagementException
     */
    public String checkUserInfo(int accountID, String accountHEncrypt, int accountNumber)throws AccountManagementException;
    
    /**
     * Cette fonction permet de recuperer ou de generer le userCode d'un excavator.
     *
     * @param accountID identifiant de l'utilisateur connecté
     * @param login login de l'utilisateur
     * @param password mot de passe de l'utilisateur.
     * @param generate vaut True si le user code doit etre regénerer.
     * @return
     * @throws util.exception.AccountManagementException
     */
    public String retrieveUserCode(int accountID, String login, String password, boolean generate)throws AccountManagementException;
    
    /**
     * Cette fonction permet de verifier si le code l'utilisateur Excavator est valide.
     *
     * @param accountID: account_ID de l'excavator
     * @param userCode: User code de l'utilisateur
     * @return 'good' si tout se passe bien
     * @throws util.exception.AccountManagementException
     */
    public String checkExcavatorUserCode(int accountID, String userCode)throws AccountManagementException;
    
    /**
     *
     * @param accountID
     * @return
     * @throws util.exception.AccountManagementException
     */
    public Result getUserStatistics(int accountID)throws AccountManagementException;
    
}
