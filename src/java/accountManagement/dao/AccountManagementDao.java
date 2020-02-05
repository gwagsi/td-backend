/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package accountManagement.dao;

import entities.Account;
import entities.AccountHistory;
import entities.AccountType;
import entities.ApplyPromotionCode;
import entities.BillingReceiver;
import entities.MonthlyBill;
import entities.PhoneRegistration;
import entities.PromotionCode;
import entities.RealizeService;
import entities.Service;
import entities.SocialStatus;
import entities.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.apache.logging.log4j.LogManager;
import paysimple.MypaySimpleObj;
import paysimple.PaySimpleAP;
import resultInfo.LoginInfo;
import toolsAndTransversalFunctionnalities.CodeGenerate;
import toolsAndTransversalFunctionnalities.CredentialInformation;
import toolsAndTransversalFunctionnalities.GestionnaireEntite;
import toolsAndTransversalFunctionnalities.Result;
import toolsAndTransversalFunctionnalities.ResultBackend;
import util.date.DateFunction;
import util.exception.AccountManagementException;
import util.query.sql.StatisticsQuery;
import util.security.CodeDecodeAES;

/**
 *
 * @author erman
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class AccountManagementDao implements IAccountManagementDaoRemote , IAccountManagementDaoLocal {
     
    @EJB
    GestionnaireEntite ges;
    
    AccountFunction af;
    
    final static org.apache.logging.log4j.Logger logger = LogManager.getLogger("dump1");
    
    @Override
    public String addNewUser(String name, String surname, String address, String email, String telephone, String gpsCoordinate, String cardNumber, 
            String login, String password, String servicesID, int socialStatusID, String compagnyName) throws AccountManagementException{
                
        String res = "";
        Account account = null;
        User user = null;
        SocialStatus socialStatus = null;
        RealizeService  realizeService = null;
        List<Service> services = new ArrayList<Service>();
        int nombreService = (servicesID.split(";")).length;
        
        try{
            
            ges.creatEntityManager();
            String[] serviceID = servicesID.split(";");
            for(int i = 0; i<nombreService; i++){
                Service service = (Service)ges.getEntityManager().find(Service.class, Integer.parseInt(serviceID[i]) );
                services.add(service);
             }
            
            socialStatus = (SocialStatus)ges.getEntityManager().find(SocialStatus.class, socialStatusID);
            
            try{
                
                account = (Account)ges.getEntityManager().createNamedQuery("Account.findByEmail").setParameter("email", email).getSingleResult();
            
            }catch(Throwable th){ }
            
            if(account != null){
                ges.closeEm();
                return "ExistingEmail";
            }
            
            try{
                
                account = (Account)ges.getEntityManager().createNamedQuery("Account.findByLogin").setParameter("login", login).getSingleResult();
            
            }catch(Throwable th){ }
            
            if(account != null){
                ges.closeEm();
                return "ExistingLogin";
            }
            
            if(socialStatus == null){
                ges.closeEm();
                return "NotExistingSocialStatus";
            }
            if(services.isEmpty()){
                ges.closeEm();
                return "NotExistingService";
            }
            
            //int accountNumber = getAccountNumber();
            
            account = new Account();
            account.setLogin(login);
            account.setAccountNumber(0);
            account.setPassword(password);
            account.setActif(false);
            account.setDeleted(false);
            account.setConnected(false);
            Date gmtDate = DateFunction.getGMTDate();
            account.setCreationDate(gmtDate);
            account.setEmail(email);
            account.setSocialstatusID(socialStatus);
            ges.getEntityManager().persist(account);
            ges.closeEm();
            
            user = new User();
            user.setAddress(address);
            user.setCardNumber(cardNumber);
            user.setName(name);
            user.setSurname(surname);
            user.setGpsCoordinate(gpsCoordinate);
            user.setTelephone(telephone);
            user.setCellPhone("null");
            user.setDeleted(false);
            user.setCreationDate(gmtDate);
            user.setJobNumber(0);
            user.setTruckNumber(0);
            user.setBasketNumber(0);
            user.setRate(0);
            if(!compagnyName.equals("")){
                user.setCompagnyName(compagnyName);
            }
            user.setAccountID(account);
            ges.getEntityManager().persist(user);
            ges.closeEm();
            
            
            List<RealizeService> reas = new ArrayList<>();
            for(Service service:services){
                realizeService = new RealizeService();
                realizeService.setAccountID(account);
                realizeService.setServiceID(service);
                ges.getEntityManager().persist(realizeService);
                ges.closeEm();
                reas.add(realizeService);
            }
            
            account.setRealizeServiceList(reas);
            account.setUser(user);
            user.setAccountID(account);
            
            
            ges.getEntityManager().merge(user);
            ges.getEntityManager().merge(account);
            ges.closeEm();
            
            if (socialStatus.getSocialstatusID() == TypeOfUser.SOCIAL_STATUS_EXCAVATOR_ID
                    || socialStatus.getSocialstatusID() == TypeOfUser.SOCIAL_STATUS_GOV_ID
                    || socialStatus.getSocialstatusID() == TypeOfUser.SOCIAL_STATUS_SIMPLE_USER_ID) {
               
                logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"addNewUser"+"\n"
                            +"info      : "+ "Generation Code excavator");
           
                // System.out.println("Generation Code excavator");
                String userCode = CodeGenerate.generateCode((int) user.getUserID());
                
                user.setUserCode(userCode);
                ges.getEntityManager().merge(user);
                ges.closeEm();
                
            }
            
            res = "good";
            res += ";"+account.getAccountID() + ";"
                    +user.getAccountID().getAccountID() +";"
                    +user.getAccountID().getAccountNumber()+";"
                    +account.getSocialstatusID().getSocialstatusID()+";"
                    +(user.getUserCode() == null ? "null" : user.getUserCode() ) +""
                    ;
             logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"addNewUser"+"\n"
                            +"info      : "+ "res = "+res);
            // System.out.println("addNewUser: res = "+res);
        }catch(Throwable th){
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            throw new AccountManagementException("AccountManagementDao","addNewUser",1,res,res);
           
        }
        return res;
    }

    @Override
    public String activate(String encryData) throws AccountManagementException{

        String res;
        Account account = null;
        int accountID = 0;
        try {
            //System.out.println("----------------"+encryData+"----------------");
            accountID = Integer.parseInt(CodeDecodeAES.decrypt(encryData));
             
        } catch (NumberFormatException ex) {
            Logger.getLogger(AccountManagementDao.class.getName()).log(Level.SEVERE, null, ex);
             throw new AccountManagementException("AccountManagementDao"," activate ", 2, "InvalidAccountID",ex.getMessage());
          
            //return "InvalidAccountID";
        }

         logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"activate"+"\n"
                            +"info      : "+ "AccountID: " + accountID);
       // System.out.println("activate: AccountID: " + accountID);
        
        try {
            ges.creatEntityManager();
            account = (Account) ges.getEntityManager().find(Account.class, accountID);
            account.setActif(true);
            ges.getEntityManager().merge(account);
            ges.closeEm();
            res = "good";
        } catch (Throwable th) {
            if (account == null) {
                 throw new AccountManagementException("AccountManagementDao"," activate ",2,"NotExistingAccount ",th.getMessage());
                 //return "NotExistingAccount";
            }
              throw new AccountManagementException("AccountManagementDao"," activate ",2,"Indeterminate",th.getMessage());
               
           //  throw new AccountManagementException("AccountManagementDao"," activate : "+ th.getMessage());
            //return th.getMessage();
        }
        return res;
    }

    @Override
    public String modifyPasswordByLogin(int accountID, String login, String encienPassword, String newPassword)  throws AccountManagementException{

        String res = "good";
        Account account;
        try {

            ges.creatEntityManager();

            account = (Account) ges.getEntityManager().find(Account.class, accountID);

            if (account.getLogin().equals(login) && !account.
            getDeleted()) {
                if (account.getPassword().equals(encienPassword)) {

                    account.setPassword(newPassword);
                    ges.getEntityManager().merge(account);
                    ges.closeEm();

                } else {
                    res = "InvalidPassword";
                }
            } else {
                res = "InvalidLogin";
            }
        } catch (Throwable e) {
            res = e.getMessage();
             throw new AccountManagementException("AccountManagementDao"," modifyPasswordByLogin  ",3,res,e.getMessage());
        }
        return res;
    }

    @Override
    public String modifyPassword(int accountID, String encienPassword, String newPassword) throws AccountManagementException{

        String res = "good";
        Account account;
        try {

            ges.creatEntityManager();

            account = (Account) ges.getEntityManager().find(Account.class, accountID);

            if (account.getPassword().equals(encienPassword) && !account.getDeleted()) {

                account.setPassword(newPassword);
                ges.getEntityManager().merge(account);
                ges.closeEm();

            } else {
                res = "InvalidPassword";
            }

        } catch (Throwable e) {

            //res = e.getMessage();
             throw new AccountManagementException("AccountManagementDao","modifyPassword ",4,res, e.getMessage());
       
        }
        return res;
    }

    @Override
    public String login(String login, String password, int serviceID)throws AccountManagementException{

        long debut = System.currentTimeMillis();
        Account account = null;
        String res;

        Date gmtDate = DateFunction.getGMTDate();
        long endOfBillingCycle = 0;
        String name;
        String surname;

        try {
            ges.evictAll();
            //Recherche du compte correspondant au login
            if (!login.contains("@")) {
                try {

                    account = (Account) ges.getEntityManager().createQuery("SELECT a FROM Account a WHERE a.login = :login")
                            .setParameter("login", login)
                            .getSingleResult();

                } catch (NoResultException th) {
                    logger.error("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"login"+"\n"
                            +"error      : "+"Not User found by login: " + th.getMessage());
                    //System.out.println("[login] Not User found by login: " + th.getMessage());
                
                } catch (Throwable th) {
                    StringWriter string = new StringWriter();
                    PrintWriter str = new PrintWriter(string);
                    th.printStackTrace(str);
                    res = string.toString();
                   
                   // System.out.println(res);
                    //return "InternalError";
                     throw new AccountManagementException("AccountManagementDao","login ",5,"InternalError",res);
                }
            } else {
                try {

                    account = (Account) ges.getEntityManager().createQuery("SELECT a FROM Account a WHERE a.email = :email")
                            .setParameter("email", login)
                            .getSingleResult();

                } catch (NoResultException th) {
                    
                     logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"login"+"\n"
                            +"info      : "+ "Not User found by email: " + th.getMessage());
                    //System.out.println("[login] Not User found by email: " + th.getMessage());
                
                } catch (Throwable th) {
                    StringWriter string = new StringWriter();
                    PrintWriter str = new PrintWriter(string);
                    th.printStackTrace(str);
                    res = string.toString();
                    //System.out.println(res);
                    //return "InternalError";
                      throw new AccountManagementException("AccountManagementDao","login ",6,"InternalError",res);
                }
            }

            // Verification des informations et allocation eventuelle de la connexion
            if (account == null || account.getDeleted() || account.getSuspend()) {
                if (account == null) {
                    //ges.closeEm();
                    res = "notExistingUser";
                } else {
                    res = "suspendedOrDeletedUser";
                }
                //return res;
                  throw new AccountManagementException("AccountManagementDao","login",6,res," No determinate");
            }
            
             logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"login"+"\n"
                            +"info      : "+ "login: accountgetDeleted: " + account.getDeleted()
                    + " - accountgetSuspend: " + account.getSuspend()
                    + " - accountgetActif: " + account.getActif());
             
            /*
             System.out.println("login: accountgetDeleted: " + account.getDeleted()
                    + " - accountgetSuspend: " + account.getSuspend()
                    + " - accountgetActif: " + account.getActif()
            );
                */

            if (!account.getPassword().equals(password)) {
                //ges.closeEm();
                return "InvalidPassword";
                
            }

            if (!account.getActif()) {
                //ges.closeEm();
                return "ActivateAccount";
            }

            try {
                ges.getEntityManager().refresh(account);
            } catch (EntityNotFoundException ex) {
                 logger.error("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"login"+"\n"
                            +"error      : "+"Error to Refresh cash of account:" + account + " Error: " + ex.getMessage());
                   
               // System.out.println("login: Error to Refresh cash of account:" + account + " Error: " + ex.getMessage());
            }
            
            int sessionTimeOut = DateFunction.getSessionTimeOut();
            logger.error("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"login"+"\n"
                            +"error      : "+"sessionTimeOut: " + sessionTimeOut + " - account: " + account);
                   
            //System.out.println("login: sessionTimeOut: " + sessionTimeOut + " - account: " + account);
            
            account.setConnected(true);
            AccountHistory accountHistory = new AccountHistory();
            accountHistory.setDeleted(false);
            accountHistory.setStartConnection(gmtDate);
            accountHistory.setAccountID(account);
            accountHistory.setSessionTimeOut(sessionTimeOut);

            ges.getEntityManager().persist(accountHistory);
            ges.getEntityManager().merge(account);
            ges.closeEm();

            Account userAccount = account;
            if (account.getSocialstatusID().getSocialstatusID() == TypeOfUser.SOCIAL_STATUS_DRIVER_ID) {
                userAccount = account.getDriver().getUserID().getAccountID();
                name = account.getDriver().getName();
                surname = account.getDriver().getSurname();
            } else {
                name = account.getUser().getName();
                surname = account.getUser().getSurname();
            }

            MonthlyBill monthlyBill = null;
            try {
                monthlyBill = (MonthlyBill) ges.getEntityManager().createQuery("SELECT m FROM MonthlyBill m WHERE m.deleted = FALSE AND m.accountID = :accountID AND :currentDate BETWEEN m.monthlyStartDate AND m.monthlyEndDate")
                        .setParameter("currentDate", gmtDate)
                        .setParameter("accountID", userAccount)
                        .getSingleResult();
            } catch (Exception e) {
                  logger.error("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"login"+"\n"
                            +"error      : "+ e.getMessage());
                   
               // System.out.println("login: Error: " + e.getMessage());
            }

            if (monthlyBill == null) {
                Calendar c = Calendar.getInstance();
                c.setTime(gmtDate);
                monthlyBill = new MonthlyBill();
                monthlyBill.setAccountID(userAccount);
                monthlyBill.setDeleted(false);
                monthlyBill.setPaid(false);
                monthlyBill.setTaxe(BigDecimal.ZERO);
                monthlyBill.setCreationDate(gmtDate);
                monthlyBill.setMonthlyStartDate(DateFunction.getGMTDateMidnight(c.getTime()));
                c.add(Calendar.MONTH, 1);
                monthlyBill.setMonthlyEndDate(DateFunction.getGMTLastDateOfDay(c.getTime()));
                monthlyBill.setPaymentDate(null);
                monthlyBill.setAccounttypeID(account.getAccounttypeID());
                if (account.getBillingreceiverID() == null) {
                    monthlyBill.setSuscriptionAmount(BigDecimal.ZERO);
                } else {
                    monthlyBill.setSuscriptionAmount(account.getBillingreceiverID().getMonthlyCost());
                }
                ges.getEntityManager().persist(monthlyBill);
                ges.closeEm();
            } else if (!monthlyBill.getPaid() && monthlyBill.getMonthlyEndDate().before(gmtDate)) {
                     logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"login"+"\n"
                            +"info      : "+ "Un paid Monthly Bill " + monthlyBill);
                
                // System.out.println("Login: Un paid Monthly Bill " + monthlyBill);
                //return "UnPayBill";
            }

            int accountID = account.getAccountID();
            boolean actif = account.getActif();
            int accHisID = accountHistory.getAccounthistoryID();
            int socialStatusID = account.getSocialstatusID().getSocialstatusID();
            String socialStatusName = account.getSocialstatusID().getTypeName();
            int accountNumber = account.getAccountNumber();
            String billingAck = (userAccount.getBillingreceiverID() != null ? "ok" : "bad");
            int accountTypeID = (account.getAccounttypeID() == null ? -1 : account.getAccounttypeID().getAccounttypeID());
            endOfBillingCycle = monthlyBill.getMonthlyEndDate().getTime();
            int userAccountID = userAccount.getAccountID();

            LoginInfo logInfo = new LoginInfo(accountID, actif, CodeDecodeAES.encryptAccountHistory(accHisID), name, surname, socialStatusID,
                    socialStatusName, accountNumber, billingAck, accountTypeID, endOfBillingCycle, userAccountID);

            res = logInfo.getStringObj("good");

           // System.out.println("login: res = " + res);
           // System.out.println("Temps execution = " + (System.currentTimeMillis() - debut));
          
             logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"login"+"\n"
                            +"info      : "+ "res = " + res);
        } catch (EJBException e) {
            res = "";
            //@SuppressWarnings("ThrowableResultIgnored")
            Exception cause = e.getCausedByException();
            if (cause instanceof ConstraintViolationException) {
                @SuppressWarnings("ThrowableResultIgnored")
                ConstraintViolationException cve = (ConstraintViolationException) e.getCausedByException();
                for (Iterator<ConstraintViolation<?>> it = cve.getConstraintViolations().iterator(); it.hasNext();) {
                    ConstraintViolation<? extends Object> v = it.next();
                    
                    //System.err.println(v);
                    //System.err.println("==>>" + v.getMessage());
                    res += v.getMessage();
                    logger.error("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"login"+"\n"
                            +"error      : "+ "res = " + res);
                }
            } else {
                //System.err.println("==>>" + cause.getMessage());
                res = "ejb-exception";
                
                  logger.error("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"login"+"\n"
                            +"error      : "+ "res = " + res);
                  
            }
        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
           // System.out.println(" login: Error = " + res);
             throw new AccountManagementException("AccountManagementDao","login",7,res,e.getMessage());
        }

        return res;

    }

    @Override
    public String logout(int accountID, String sessionID, String phoneID) throws AccountManagementException{

        String res = "good";
        Account account;
        AccountHistory accountHist;
        PhoneRegistration phoneRegistration = null;
        
        int accountHistoryID;
        try {
            accountHistoryID = Integer.parseInt(CodeDecodeAES.decryptAccountHistory(sessionID));
        } catch (Exception ex) {
            Logger.getLogger(AccountManagementDao.class.getName()).log(Level.SEVERE, null, ex);
            //return "InvalidAccounthistoryID";
              throw new AccountManagementException("AccountManagementDao","logout",8,"InvalidAccounthistoryID",ex.getMessage());
        }
        
        try {
            phoneRegistration = (PhoneRegistration) ges.getEntityManager().createQuery("SELECT p FROM PhoneRegistration p WHERE p.deleted = FALSE AND p.phoneID = :phoneID")
                    .setParameter("phoneID", phoneID)
                    .getSingleResult();
        } catch (Exception ex) {
              logger.error("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"logout"+"\n"
                            +"error      : "+ "phoneID: " + phoneID);
            // System.out.println("[logout] phoneID: " + phoneID );
        }
        
        try {

            ges.creatEntityManager();

            account = (Account) ges.getEntityManager().find(Account.class, accountID);

            if (account == null || account.getDeleted()) {
                res = "InvalidAccountID";
                return res;

            }

            accountHist = (AccountHistory) ges.getEntityManager().find(AccountHistory.class, accountHistoryID);
            if (accountHist == null) {
                res = "InvalidAccounthistoryID";
                return res;
            }

            if (account.getConnected() && accountHist.getEndConnection() == null) {
                account.setConnected(false);
                accountHist.setEndConnection(DateFunction.getGMTDate());
                ges.getEntityManager().merge(accountHist);
                ges.getEntityManager().merge(account);
            } else {
                res = "AlreadyDeconnected";
            }
            
            if (phoneRegistration != null) {
                phoneRegistration.setRegisteredID("0");
                ges.getEntityManager().merge(phoneRegistration);
                ges.closeEm();
            }
            
        } catch (Throwable e) {
            res = e.getMessage();
                throw new AccountManagementException("AccountManagementDao","logout",8,res,res);
        } finally {
            //ges.closeEm();
        }
        return res;
    }

    @Override
    public Result recoverPasswd(String email) {

        Account account = null;
        Result result = new ResultBackend();
        String res = "good";
          logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"recoverPasswd"+"\n"
                            +"info      : "+ "email: " + email);
          
        //System.out.println("recoverPasswd: email: " + email);

        try {

            ges.creatEntityManager();

            account = (Account) ges.getEntityManager().createQuery("SELECT a FROM Account a WHERE a.deleted = FALSE AND a.actif = TRUE AND a.email = :email")
                    .setParameter("email", email)
                    .getSingleResult();

        } catch (Throwable th) {
               logger.error("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"recoverPasswd"+"\n"
                            +"error      : "+th.getMessage());
            
            //System.out.println("recoverPasswd: Error: " + th.getMessage());
            ges.closeEm();
        }

        if (account == null) {
            
               logger.error("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"recoverPasswd"+"\n"
                            +"error      : "+"NotExistingUser");
            //System.out.println("recoverPasswd: Error: NotExistingUser");
            result.setMsg("NotExistingUser");
            return result;
        }

        String name;
        String firstName;
        String login;
        String pwd;
        
           logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"recoverPasswd"+"\n"
                            +"info      : "+"recoverPasswd: accoun: " + account+"\n"
                            + "recoverPasswd: accoungetSocialstatusID: " + account.getSocialstatusID()+"\n"
                            +"recoverPasswd: accoungetSocialstatusIDgetSocialstatusID: " + account.getSocialstatusID().getSocialstatusID()
           );
           
           /*
        System.out.println("recoverPasswd: accoun: " + account);
        System.out.println("recoverPasswd: accoungetSocialstatusID: " + account.getSocialstatusID());
        System.out.println("recoverPasswd: accoungetSocialstatusIDgetSocialstatusID: " + account.getSocialstatusID().getSocialstatusID());
        
           */
           
           
           
           if (account.getSocialstatusID().getSocialstatusID() == TypeOfUser.SOCIAL_STATUS_DRIVER_ID) {
            name = account.getDriver().getName();
            firstName = account.getDriver().getSurname();
            login = account.getLogin();
            pwd = account.getPassword();
        } else {
            name = account.getUser().getName();
            firstName = account.getUser().getSurname();
            login = account.getLogin();
            pwd = account.getPassword();
        }

        ArrayList listResult = new ArrayList();
        listResult.add(name);//00
        listResult.add(firstName);//01
        listResult.add(login);//02
        listResult.add(pwd);//03

        //res += ";" + account.getAccountID();
        result.setMsg(res);
        result.setObjectList(listResult);

        return result;
    }

    @Override
    public String modifyInfo(int accountID, String name, String surname,
            String address, String email, String telephone, String gpsCoordinate, 
            String cardNumber, String compagnyName, String cellPhone, String routineNumber) throws AccountManagementException{
         
        String res = "good";
        Account account = null;
        Account exist_account = null;
        User user = null;
        String etatTransaction = "";
        
        try{
            
            ges.creatEntityManager();
            etatTransaction = "The begining \n";
            
            try{
                account = (Account)ges.getEntityManager().find(Account.class, accountID);
                exist_account = (Account)ges.getEntityManager().createNamedQuery("Account.findByEmail").setParameter("email", email).getSingleResult();
            }catch(Throwable th){ }
            
            if(account == null){
                //ges.closeEm();
                return "InvalidAccountID\n";
            }
            
            if(exist_account != null && !account.equals(exist_account)){
                //ges.closeEm();
                return "ExistingEmail\n";
            }
            
            etatTransaction += "Controle des attribut termine\n";
            
            user = account.getUser();
            user.setAddress(address);
            user.setCardNumber(cardNumber);
            user.setName(name);
            user.setRoutineNumber(routineNumber);
            user.setCellPhone(cellPhone);
            user.setSurname(surname);
            user.setGpsCoordinate(gpsCoordinate);
            user.setTelephone(telephone);
            if(!compagnyName.equals("")){
                user.setCompagnyName(compagnyName);
            }
            
            etatTransaction += "mise a jour des infos du user: ETAT = Termine!!!\n";
            account.setEmail(email);
            account.setUser(user);
            user.setAccountID(account);
            
            etatTransaction += "gestion des relations niveau entite. ETAT:Termine!!! \n";
            
            ges.getEntityManager().merge(user);
            ges.getEntityManager().merge(account);
            ges.closeEm();
            
            etatTransaction += "Fin des operations \n";
            res += ";"+account.getAccountID();
            
        }catch(Throwable th){
            if(ges == null){
                  throw new AccountManagementException("AccountManagementDao","modifyInfo",8,"Could not create Entities Manager",th.getMessage());
                //return "Could not create Entities Manager";
            }
              throw new AccountManagementException("AccountManagementDao","modifyInfo",8,etatTransaction+th.getLocalizedMessage(),th.getMessage());
             //return etatTransaction+th.getLocalizedMessage();
        }
        return res;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getUserByAccountID(int accountID)  throws AccountManagementException{
        
        Result result = new Result();
        String res = "good";
        Account account = null;
        
        try{
            
            ges.creatEntityManager();
            account = (Account)ges.getEntityManager().find(Account.class, accountID);
            ges.closeEm();
        }catch(Throwable th){
            res += th.getMessage();
        }
        
        if(account == null){
            result.setMsg("NotExistingUser\n"+res);
            return result;
        }
        
        result.setMsg(res);
        String services = "";
        for(RealizeService reas:account.getRealizeServiceList()){
            services += reas.getServiceID().getServiceID()+"#"; 
        }
        
        String billing = "null";
        if(account.getBillingreceiverID() != null){
            billing =  "" +account.getAccounttypeID().getAccounttypeID();
            billing += ";"+account.getBillingreceiverID().getBillingreceiverID();
            billing += ";"+account.getBillingreceiverID().getName();
            billing += ";"+account.getBillingreceiverID().getSurname();
            billing += ";"+account.getBillingreceiverID().getEmail();
            billing += ";"+account.getBillingreceiverID().getTelephone();
            billing += ";"+account.getBillingreceiverID().getCellPhone();
            billing += ";"+account.getBillingreceiverID().getAddress();
            billing += ";"+account.getBillingreceiverID().getCreditCardNumber();
            billing += ";"+account.getBillingreceiverID().getCreditCardExpiration();
            billing += ";"+account.getBillingreceiverID().getSecurityCode();
            billing += ";"+account.getUser().getBankName();
            billing += ";"+account.getUser().getAccountNumber();
            billing += ";"+account.getUser().getAccountName();
            billing += ";"+account.getUser().getRoutineNumber();
            billing += ";$"+account.getBillingreceiverID().getAjustedCost().round(new MathContext(3, RoundingMode.HALF_UP));
            billing += ";"+account.getBillingreceiverID().getBillingCycle();
            billing += ";$"+account.getBillingreceiverID().getMonthlyCost().round(new MathContext(3, RoundingMode.HALF_UP));
        }
        
        User user = account.getUser();
        if(user == null){
            result.setMsg("Incoherence de la base de donnees\n");
            user = (User)ges.getEntityManager().find(User.class, accountID);
            result.setObject(user.getAccountID().getEmail());
            return result;
        }
        res = account.getAccountID()+";"//0
                +user.getName()+";"
                +user.getSurname()+";"
                +user.getAddress()+";"
                +user.getAccountID().getEmail()+";"
                +user.getTelephone()+";"
                +user.getCellPhone()+";"
                +user.getGpsCoordinate()+";"
                +user.getCardNumber()+";"
                +account.getLogin()+";"
                +account.getPassword()+";"//10
                +services+";"
                +account.getSocialstatusID().getSocialstatusID()+";"
                +account.getUser().getCompagnyName()+";"
                +billing+";"
                +account.getAccountNumber()+";"
                +(account.getBillingreceiverID() == null ? "" : account.getBillingreceiverID().getNameOnCreditCard())+";"
                +(account.getBillingreceiverID() == null ? "" : account.getBillingreceiverID().getPaymentType())+";"
                +"null"
                ;
        
        result.setObject(res);
        return result;
        
    }

    @Override
    public String activateService(int accountID, int serviceID) throws AccountManagementException{
        
        String res = "good";
        Account account;
        Service service;
        RealizeService reas;
        List<RealizeService> reasAcc;
        List<RealizeService> reasSer;
        
        try{
            ges.creatEntityManager();
            account = (Account)ges.getEntityManager().find(Account.class, accountID);
            service = (Service)ges.getEntityManager().find(Service.class, serviceID);
            
        }catch(Throwable th){
            if(ges == null){
                  throw new AccountManagementException("AccountManagementDao","activateService",8,"Could not create Entities Manager",th.getMessage());
                //return "Could not create Entity Manager";
            }
              throw new AccountManagementException("AccountManagementDao","activateService",8,th.getMessage(),th.getMessage());
            //return th.getMessage();
        }
        if(account == null){
            return "NotExistingAccount";
        }
        if(service == null){
            return "NotExistingService";
        }
        
        reasAcc = account.getRealizeServiceList();
        reasSer = service.getRealizeServiceList();
        reas = new RealizeService();
        reas.setAccountID(account);
        reas.setServiceID(service);
        ges.getEntityManager().persist(reas);
        ges.closeEm();
        
        reasAcc.add(reas);
        reasSer.add(reas);
        account.setRealizeServiceList(reasAcc);
        service.setRealizeServiceList(reasSer);
        ges.getEntityManager().merge(account);
        ges.getEntityManager().merge(service);
        ges.closeEm();
        
        return res+";"+reas.getRealizeserviceID()
                +";"+reas.getAccountID().getAccountID()
                +";"+reas.getServiceID().getServiceID();
    }
    
    
    
    @Override
    public String modifyBillingInfo(int accountID, String name, String surname, String securityCode,
            String address, String email, String telephone, String cellPhone, String creditCard, 
            String cardExpiration, String bankName, String accountNumber, String accountName, int accountTypeID,
            int promotionCodeID, String billingCycle, String typeOfCreditCard, 
            String paymentType, String routingNumber, String nameOnCreditCard) throws AccountManagementException{
  
        String res = "good";
        Account account;
        AccountType accountType;
        BillingReceiver billingReceiver;
        User user;
        PromotionCode promotionCode;
        ApplyPromotionCode applyPromotionCode = null;
        Date gmtDate = DateFunction.getGMTDate();
        Date dateOfNextPayment = DateFunction.getGMTDate();
        MonthlyBill monthlyBill = null;
        double amountDue;
        //Setup output structure
        MypaySimpleObj usaepayresponse;
        final String CHECK = "check";
        String description = " Monthly Payment";

        CredentialInformation creInfo = new CredentialInformation();
        Properties props;
        try {
            props = creInfo.loadProperties();
        } catch (IOException ex) {
            
             logger.error("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"modifyBillingInfo"+"\n"
                            +"error      : "+ex.getMessage());
             
           // System.out.println("modifyBillingInfo: Error: " + ex.getMessage());
             
            Logger.getLogger(AccountManagementDao.class.getName()).log(Level.SEVERE, null, ex);
            //return "NotExistingFileName";
             throw new AccountManagementException("AccountManagementDao","activateService",8,"NotExistingFileName",ex.getMessage());
            
        }

        String endPoint = props.getProperty("endPoint");//"sandbox.usaepay.com";
        String sourceKey = props.getProperty("sourceKey");//"Bmy7457mqTfoOZAH9s51204z53Aa2v1Y";
        String sourcePin = props.getProperty("sourcePin");//"1234";
        String clientIpAddress = props.getProperty("clientIpAddress");//"127.0.0.1";
        String period = props.getProperty("period");//"Monthly";
        boolean enablePeriodicPayment = Boolean.parseBoolean(props.getProperty("enablePeriodicPayment"));//true;
        boolean sendReceipt = Boolean.parseBoolean(props.getProperty("sendReceipt"));//true;

        PaySimpleAP usaepayapi = new PaySimpleAP(endPoint, sourceKey, sourcePin, clientIpAddress, period, enablePeriodicPayment, sendReceipt);

        try {

            ges.creatEntityManager();

            account = (Account) ges.getEntityManager().find(Account.class, accountID);
            accountType = (AccountType) ges.getEntityManager().find(AccountType.class, accountTypeID);
            promotionCode = (PromotionCode) ges.getEntityManager().find(PromotionCode.class, promotionCodeID);
            description = TypeOfUser.getStringValue(account.getSocialstatusID().getSocialstatusID()) + description;
            
            monthlyBill = (MonthlyBill) ges.getEntityManager().createQuery("SELECT m FROM MonthlyBill m WHERE m.deleted = FALSE AND m.accountID = :accountID AND :currentDate BETWEEN m.monthlyStartDate AND m.monthlyEndDate")
                    .setParameter("currentDate", gmtDate)
                    .setParameter("accountID", account)
                    .getSingleResult();

            if (account == null) {
                ges.closeEm();
                return "InvalidAccountID\n";
            }

            if (accountType == null) {
                ges.closeEm();
                return "InvalidAccountTypeID\n";
            }
            
            af = new AccountFunction();
            double monthlyCost = accountType.getPrice().doubleValue();
            double ajustedCost = 0;
            
            if (promotionCode != null) {
                try {
                    applyPromotionCode = (ApplyPromotionCode) ges.getEntityManager().createQuery("SELECT a FROM ApplyPromotionCode a WHERE a.used = FALSE AND a.deleted = FALSE AND a.accountID = :accountID AND a.promotioncodeID = :promotioncodeID").setParameter("promotioncodeID", promotionCode).setParameter("accountID", account).getSingleResult();
                } catch (Throwable ex) {
                }

                if (applyPromotionCode == null) {
                    ges.closeEm();
                    return "InvalidPromotionCodeID\n";
                }

                if (applyPromotionCode.getExpirationTerm().before(gmtDate)) {
                    ges.closeEm();
                    return "ExpirationTerm\n";
                }

                applyPromotionCode.setUsed(true);
                applyPromotionCode.setDateOfUsed(gmtDate);

                ges.getEntityManager().merge(applyPromotionCode);

                //Calcul de la date du prochain paiement
                int ajustedPercent = applyPromotionCode.getPromotioncodeID().getPercent();

                Calendar gmtCalendar = Calendar.getInstance();
                gmtCalendar.setTime(account.getBillingreceiverID() == null ? gmtDate : account.getBillingreceiverID().getEndOfBillingCycle());
                gmtCalendar = af.addMonth(gmtCalendar, (double) ((double) ajustedPercent) / 100);
                dateOfNextPayment = gmtCalendar.getTime();
                ajustedCost = monthlyCost * ajustedPercent;
            }

            
            amountDue = monthlyCost - ajustedCost;
            amountDue = amountDue <= 0 ? 0 : amountDue;
            user = account.getUser();

            boolean exist = true;
            if (account.getBillingreceiverID() == null) {
                billingReceiver = new BillingReceiver();

                //C'est a ce niveau qu'il faudra faire la souscription avec usaepay
                String firstName = name;
                String lastName = (surname.split("#").length < 2 ? "" : surname.split("#")[1]);
                String company = account.getUser().getCompagnyName();
                String street = address.split("#")[0];//"342 Main Street";//address
                String city = address.split("#")[1];//"yaounde";
                String state = address.split("#")[2];//"CA";//address
                String zipCode = address.split("#")[3];//"91920";//address
                String strDateOfNextPayment = af.parseDateToString(dateOfNextPayment);
                
                usaepayresponse = usaepayapi.addNewCustomerToOurPaySimpleAccount(firstName,lastName, company, street, city, state, zipCode, "US"/*country*/,  email, ""/*fax*/, telephone.split("#")[0], monthlyCost/*amount*/, strDateOfNextPayment,description);
               logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"modifyBillingInfo"+"\n"
                            +"info       : "+"custumer ID:" + usaepayresponse.custnum+"\n"
                            +"Method ID:" + usaepayresponse.paymentmethodid+"\n"
                            +"Message:" + usaepayresponse.message);
                
                /*
                System.out.println("custumer ID:" + usaepayresponse.custnum);
                System.out.println("Method ID:" + usaepayresponse.paymentmethodid);
                System.out.println("Message:" + usaepayresponse.message);
                */
                if (!usaepayresponse.message.equalsIgnoreCase("good")) {
                    if (applyPromotionCode != null) {

                        applyPromotionCode.setUsed(false);
                        applyPromotionCode.setDateOfUsed(null);
                        ges.getEntityManager().merge(applyPromotionCode);
                        ges.closeEm();

                    }
                    return "InvalidBillingInfomation";
                }
                
                if (!paymentType.equals(CHECK)) {
                    usaepayresponse = usaepayapi.addAPaymentMethodeInCreateMode(usaepayresponse.custnum/*custnum*/, "", "", ""/*cardCode*/, "", "", "", "ACH"/*CreditCard Or ACH*/, accountNumber, routingNumber, false/*set this payment methode as default*/, true/*activate creditCard verification.*/ );
                   
                    logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"modifyBillingInfo"+"\n"
                            +"info       : "+"custumer ID:" + usaepayresponse.custnum+"\n"
                            +"Method ID:" + usaepayresponse.paymentmethodid+"\n"
                            +"Message:" + usaepayresponse.message);
                    /*
                    System.out.println("custumer ID:" + usaepayresponse.custnum);
                    System.out.println("Method ID:" + usaepayresponse.paymentmethodid);
                    System.out.println("Message:" + usaepayresponse.message);
                    */
                    if (!usaepayresponse.message.equalsIgnoreCase("good")) {
                        //throw new Exception();
                        if (applyPromotionCode != null) {

                            applyPromotionCode.setUsed(false);
                            applyPromotionCode.setDateOfUsed(null);
                            ges.getEntityManager().merge(applyPromotionCode);
                            ges.closeEm();

                        }
                        return "InvalidBillingInfomationBankAccount";
                    }
                }

                monthlyBill.setAccounttypeID(accountType);
                monthlyBill.setSuscriptionAmount(new BigDecimal(amountDue));
                ges.getEntityManager().merge(monthlyBill);
                ges.closeEm();
            } else {
                exist = false;
                billingReceiver = account.getBillingreceiverID();
                String firstName = name;
                String lastName = surname.split("#")[1];
                String company = account.getUser().getCompagnyName();
                String street = address.split("#")[0];//"342 Main Street";//address
                String city = address.split("#")[1];//"yaounde";
                String state = address.split("#")[2];//"CA";//address
                String zipCode = address.split("#")[3];//"91920";//address
                String strDateOfNextPayment = af.parseDateToString(dateOfNextPayment);

                usaepayresponse = usaepayapi.modifyCustomerToOurPaySimpleAccount(billingReceiver.getApicustumID(), firstName, lastName, company, street, city, state, zipCode, "US"/*country*/, email, ""/*fax*/, telephone.split("#")[0], monthlyCost/*amount*/, strDateOfNextPayment,description);
                
                logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"modifyBillingInfo"+"\n"
                            +"info       : "+"custumer ID:" + usaepayresponse.custnum+"\n"
                            +"Method ID:" + usaepayresponse.paymentmethodid+"\n"
                            +"Message:" + usaepayresponse.message);
                /*
                System.out.println("custumer ID:" + usaepayresponse.custnum);
                System.out.println("Method ID:" + usaepayresponse.paymentmethodid);
                System.out.println("Message:" + usaepayresponse.message);
                */
                if (!usaepayresponse.message.equalsIgnoreCase("good")) {
                    //throw new Exception();
                    if (applyPromotionCode != null) {

                        applyPromotionCode.setUsed(false);
                        applyPromotionCode.setDateOfUsed(null);
                        ges.getEntityManager().merge(applyPromotionCode);
                        ges.closeEm();

                    }
                    return "InvalidBillingInfomation";
                }

                //if (account.getUser().getSocialstatusID().getSocialstatusID() == TRUCK_OWNER) {
                if (!paymentType.equals(CHECK)) {
                    usaepayresponse = usaepayapi.addAPaymentMethodeInModificationMode(usaepayresponse.custnum/*custnum*/, "", "", ""/*cardCode*/, 
                            "", "", "", "ACH"/*CreditCard Or ACH*/, accountNumber, routingNumber, 
                            false/*set this payment methode as default*/, true/*activate creditCard verification.*/
                    );
                    logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"modifyBillingInfo"+"\n"
                            +"info       : "+"custumer ID:" + usaepayresponse.custnum+"\n"
                            +"Method ID:" + usaepayresponse.paymentmethodid+"\n"
                            +"Message:" + usaepayresponse.message);
                    /*
                    System.out.println("custumer ID:" + usaepayresponse.custnum);
                    System.out.println("Method ID:" + usaepayresponse.paymentmethodid);
                    System.out.println("Message:" + usaepayresponse.message);
                        */ 
                    
                    if (!usaepayresponse.message.equalsIgnoreCase("good")) {
                        //throw new Exception();
                        if (applyPromotionCode != null) {

                            applyPromotionCode.setUsed(false);
                            applyPromotionCode.setDateOfUsed(null);
                            ges.getEntityManager().merge(applyPromotionCode);
                            ges.closeEm();

                        }
                        return "InvalidBillingInfomationBankAccount";
                    }
                }
                
                if (applyPromotionCode != null) {
                    monthlyBill.setSuscriptionAmount(new BigDecimal(amountDue));
                    ges.getEntityManager().merge(monthlyBill);
                    ges.closeEm();
                }
                
            }

            billingReceiver.setAddress(address);
            billingReceiver.setEmail(email);
            billingReceiver.setName(name);
            billingReceiver.setBillingCycle(billingCycle);
            billingReceiver.setMonthlyCost(new BigDecimal(monthlyCost));
            billingReceiver.setAjustedCost(new BigDecimal(ajustedCost));
            billingReceiver.setCellPhone(cellPhone);
            billingReceiver.setSurname(surname);
            billingReceiver.setTelephone(telephone);
            //billingReceiver.setSecurityCode( "xxx" + securityCode.substring(securityCode.length()/2, securityCode.length()));
            billingReceiver.setSecurityCode(securityCode);
            billingReceiver.setCreditCardExpiration(cardExpiration);
            //billingReceiver.setCreditCardNumber( "xxxxxxx" + creditCard.substring(creditCard.length()/2, creditCard.length()));
            billingReceiver.setCreditCardNumber(creditCard);
            billingReceiver.setTypeOfCreditCard(typeOfCreditCard);
            billingReceiver.setNameOnCreditCard(nameOnCreditCard);
            billingReceiver.setPaymentType(paymentType);
            billingReceiver.setEndOfBillingCycle(dateOfNextPayment);
            billingReceiver.setApicustumID(usaepayresponse.custnum);
            billingReceiver.setDefaultpaymentmethodID(usaepayresponse.paymentmethodid);

            user.setBankName(bankName);
            user.setAccountName(accountName);
            user.setAccountNumber(accountNumber);

            billingReceiver.setAccount(account);

            if (!exist) {
                ges.getEntityManager().merge(billingReceiver);
            } else {
                ges.getEntityManager().persist(billingReceiver);
            }

            account.setBillingreceiverID(billingReceiver);
            account.setUser(user);
            account.setAccounttypeID(accountType);
            user.setAccountID(account);
            billingReceiver.setAccount(account);

            ges.getEntityManager().merge(account);
            ges.getEntityManager().merge(billingReceiver);
            ges.getEntityManager().merge(user);
            ges.closeEm();
            res += ";" + account.getAccountID();

        } catch (Throwable th) {
            if (applyPromotionCode != null) {

                applyPromotionCode.setUsed(false);
                applyPromotionCode.setDateOfUsed(null);
                ges.getEntityManager().merge(applyPromotionCode);
                ges.closeEm();

            }
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
              logger.error("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"modifyBillingInfo"+"\n"
                            +"error       : "+"custumer ID:" + res);
              
            //System.err.println("modifyBillingInfo: Error: " + res);
             throw new AccountManagementException("AccountManagementDao","activateService",8,res,th.getMessage());
            
        }
        return res;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getPromotionCodeByID(int promotionCodeID) {
        
        Result result = new Result();
        String res = "good";
        PromotionCode promotionCode= null;
        
        try{
            
            ges.creatEntityManager();
            promotionCode = (PromotionCode)ges.getEntityManager().find(PromotionCode.class, promotionCodeID);
            ges.closeEm();
        }catch(Throwable th){
            res += th.getMessage();
        }
        
        if(promotionCode == null){
            result.setMsg("NotExistingPromotionCode\n"+res);
            return result;
        }
        
        result.setMsg(res);
        
        res = promotionCode.getPromotioncodeID()+";"
                + promotionCode.getLibelle()+";"
                + promotionCode.getPercent()+";"
                +promotionCode.getDescription()
                ;
        
        result.setObject(res);
        return result;
        
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result applyPromotionCode(int accountID, String promotionCodeLibel, int accountTypeID) {
        
        Result result = new Result();
        String res = "";
        Account account = null;
        PromotionCode promotionCode= null;
        ApplyPromotionCode applyPromotionCode = null;
        AccountType accountType = null;
        
        try{
            
            ges.creatEntityManager();
            account = (Account)ges.getEntityManager().find(Account.class, accountID);
            accountType = (AccountType) ges.getEntityManager().find(AccountType.class, accountTypeID);
            promotionCode = (PromotionCode)ges.getEntityManager().createQuery("SELECT p FROM PromotionCode p WHERE p.deleted = FALSE AND p.libelle = :promotionCodeLibel").setParameter("promotionCodeLibel", promotionCodeLibel).getSingleResult();
            ges.closeEm();
        }catch(Throwable th){
            res += th.getMessage();
        }
        
        if(account == null){
            result.setMsg("InvalidAccountID");
            return result;
        }
        
        if(promotionCode == null){
            result.setMsg("InvalidPromotionCode");
            return result;
        }
        
        if(accountType == null){
            result.setMsg("InvalidAccountTypeID");
            return result;
        }
        
        try{
                applyPromotionCode = (ApplyPromotionCode)ges.getEntityManager().createQuery("SELECT a FROM ApplyPromotionCode a WHERE a.deleted = FALSE AND a.accountID = :accountID AND a.promotioncodeID = :promotioncodeID").setParameter("promotioncodeID", promotionCode).setParameter("accountID", account).getSingleResult();
        }catch(Throwable ex){ }

        if(applyPromotionCode == null){
            result.setMsg("InvalidApplyPromotion");
            ges.closeEm();
            return result;
        }

        if (applyPromotionCode.getUsed()) {
            result.setMsg("AlReadyUsedPromotionCode");

            res = promotionCode.getPromotioncodeID() + ";"
                    + promotionCode.getLibelle() + ";"
                    + promotionCode.getPercent() + ";"
                    + promotionCode.getDescription();

            result.setObject(res);
            ges.closeEm();
            return result;
        }

        /*
        if(applyPromotionCode.getExpirationTerm().before(DateFunction.getDate())){
            result.setMsg("ExpirationTerm\n"+res);
            ges.closeEm();
            return result;
        }
        */
        res = "good";    
        result.setMsg(res);
        
        res = promotionCode.getPromotioncodeID()+";"
                + promotionCode.getLibelle()+";"
                + promotionCode.getPercent()+";"
                + promotionCode.getDescription()+";"
                + accountType.getPrice()+";"
                + accountType.getPrice().multiply(new BigDecimal(promotionCode.getPercent())) +";"
                ;
        
        result.setObject(res);
        return result;
        
    }

    @Override
    public String assignPromotionCode(int accountID, int promotionCodeID) {
        String res = "";
        Account account;
        PromotionCode promotionCode = null;
        ApplyPromotionCode applyPromotionCode = null;
        
        try{
            ges.creatEntityManager();
            account = (Account)ges.getEntityManager().find(Account.class, accountID);
            promotionCode = (PromotionCode)ges.getEntityManager().find(PromotionCode.class, promotionCodeID);
            
        }catch(Throwable th){
            if(ges == null){
                return "Could not create Entity Manager";
            }
            return th.getMessage();
        }
        if(account == null){
            return "NotExistingAccount";
        }
        if(promotionCode == null){
            return "NotExistingPromotionCode";
        }
        
        Calendar c = Calendar.getInstance();
        c.setTime(DateFunction.getGMTDate());
        applyPromotionCode = new ApplyPromotionCode();
        applyPromotionCode.setAccountID(account);
        applyPromotionCode.setPromotioncodeID(promotionCode);
        applyPromotionCode.setAttributionDate(c.getTime());
        applyPromotionCode.setUsed(false);
        applyPromotionCode.setDeleted(false);
        c.add(Calendar.MONTH, 1);
        applyPromotionCode.setExpirationTerm(c.getTime());
        
        ges.getEntityManager().persist(applyPromotionCode);
        ges.getEntityManager().merge(account);
        ges.getEntityManager().merge(promotionCode);
        ges.closeEm();
        res = "good";
        res += ";"+applyPromotionCode.getApplypromotioncodeID()
                +";"+applyPromotionCode.getExpirationTerm()
                +";"+applyPromotionCode.getPromotioncodeID().getLibelle()
                +";"+applyPromotionCode.getPromotioncodeID().getPercent()
                +";"+applyPromotionCode.getPromotioncodeID().getDescription()
                ;
        
        return res;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public String checkUserInfo(int accountID, String accountHEncrypt, int accountNumber) throws AccountManagementException{
        
        logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"checkUserInfo"+"\n"
                            +"info       : "+"accountID: " + accountID + ", accountHistID: " + accountHEncrypt + ""
                + ", accountNumber: " + accountNumber);
        /*
        System.out.println("[checkUserInfo] accountID: " + accountID + ", accountHistID: " + accountHEncrypt + ""
                + ", accountNumber: " + accountNumber);
        */
        
        Account account;
        AccountHistory accountH;
        //SocialStatus socialStatus;
        String res = "good";
        String name;
        String surname;
        int accountHistoryID = 0;
        
        try {
            accountHistoryID = Integer.parseInt(CodeDecodeAES.decryptAccountHistory(accountHEncrypt));
        } catch (Exception ex) {
            Logger.getLogger(AccountManagementDao.class.getName()).log(Level.SEVERE, null, ex);
             throw new AccountManagementException("AccountManagementDao","activateService",8,"InvalidAccountHistoryID",ex.getMessage());
            //return "InvalidAccountHistoryID";
        }
        
        try {

            ges.creatEntityManager();

            accountH = (AccountHistory) ges.getEntityManager().find(AccountHistory.class, accountHistoryID);
            account = (Account) ges.getEntityManager().find(Account.class, accountID);
            //socialStatus = (SocialStatus) ges.getEntityManager().find(SocialStatus.class, socialStatusID);

        } catch (Throwable th) {
            ges.closeEm();
              throw new AccountManagementException("AccountManagementDao","activateService",8,th.getMessage(),th.getMessage());
            //return th.getMessage();
        }

        if (account == null || account.getDeleted()) {
            return "InvalidAccountID";
        }
        
        if (accountH == null || accountH.getDeleted()) {
            return "InvalidAccountHistoryID";
        }
        
        long diffInMillies = DateFunction.getGMTDate().getTime() - accountH.getStartConnection().getTime();
        if (diffInMillies > accountH.getSessionTimeOut()) {
            account.setConnected(false);
            accountH.setEndConnection(DateFunction.getGMTDate());
            ges.getEntityManager().merge(account);
            ges.getEntityManager().merge(accountH);
            ges.closeEm();
            return "SessionTimeOut";
        }

        if (!accountH.getAccountID().equals(account)) {
            return "badInformation";
        }

        if (!account.getConnected()) {
            return "notConnected";
        }
        
        logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"checkUserInfo"+"\n"
                            +"info       : "+"\n\n**Données final dans le backend: accountID = " + accountID + ", accountHistID = " + accountHistoryID + ""
                + ", socialStatusID = " + account.getSocialstatusID().getSocialstatusID() + ","
                + " accountNumber = " + account.getAccountNumber() + ", paymentInfo = " + (account.getBillingreceiverID() == null ? "bad" : "ok"));
        
       /* 
        System.out.println("\n\n**Données final dans le backend: accountID = " + accountID + ", accountHistID = " + accountHistoryID + ""
                + ", socialStatusID = " + account.getSocialstatusID().getSocialstatusID() + ","
                + " accountNumber = " + account.getAccountNumber() + ", paymentInfo = " + (account.getBillingreceiverID() == null ? "bad" : "ok"));
        */
        
        if (account.getAccountNumber() != accountNumber) {
            return "InvalidAccountNumber";
        }

        Account userAccount = account;
        if (account.getSocialstatusID().getSocialstatusID() == TypeOfUser.SOCIAL_STATUS_DRIVER_ID) {
            userAccount = account.getDriver().getUserID().getAccountID();
            name = account.getDriver().getName();
            surname = account.getDriver().getSurname();
        } else {
            name = account.getUser().getName();
            surname = account.getUser().getSurname();
        }

        MonthlyBill monthlyBill = null;
        try {
            monthlyBill = (MonthlyBill) ges.getEntityManager().createQuery("SELECT m FROM MonthlyBill m WHERE m.deleted = FALSE AND m.accountID = :accountID AND :currentDate BETWEEN m.monthlyStartDate AND m.monthlyEndDate")
                    .setParameter("currentDate", DateFunction.getGMTDate())
                    .setParameter("accountID", userAccount)
                    .getSingleResult();
        } catch (Exception e) {
             logger.error("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"checkUserInfo"+"\n"
                            +"error       : "+e.getMessage());
        
           // System.out.println("login: Error: " + e.getMessage());
        }
        
        
        //int accountID = account.getAccountID();
        boolean actif = account.getActif();
        int accHisID = accountH.getAccounthistoryID();
        int socialStatusID = account.getSocialstatusID().getSocialstatusID();
        String socialStatusName = account.getSocialstatusID().getTypeName();
        //int accountNumber = account.getAccountNumber();
        String billingAck = (userAccount.getBillingreceiverID() != null ? "ok" : "bad");
        int accountTypeID = (account.getAccounttypeID() == null ? -1 : account.getAccounttypeID().getAccounttypeID());
        long endOfBillingCycle = (monthlyBill == null ? 0 : monthlyBill.getMonthlyEndDate().getTime());
        int userAccountID = userAccount.getAccountID();
        String encryptAccHistID = accountHEncrypt;
        /*try {
        encryptAccHistID = CodeDecodeAES.encryptAccountHistory(accHisID);
        } catch (Exception ex) {
        Logger.getLogger(AccountManagementDao.class.getName()).log(Level.SEVERE, null, ex);
        }*/

        LoginInfo logInfo = new LoginInfo(accountID, actif, encryptAccHistID, name, surname, socialStatusID,
                socialStatusName, accountNumber, billingAck, accountTypeID, endOfBillingCycle, userAccountID);

        res = logInfo.getStringObj(res);
        
        logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"checkUserInfo"+"\n"
                            +"info       : "+res);
        
        //System.out.println("checkUserInfo: res = " + res);
        return res;
    }
    
    @Override
    public String retrieveUserCode(int accountID, String login, String password, boolean generate) throws AccountManagementException{

        User user = null;
        Account account = null;
        Account connectedAccount = null;

        try {

            ges.creatEntityManager();

            connectedAccount = (Account) ges.getEntityManager().find(Account.class, accountID);

            if (connectedAccount == null) {
                logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"retrieveUserCode"+"\n"
                            +"info       : "+"InvalidAccountID");
                //System.out.println("retrieveUserCode: InvalidAccountID ");
                return "InvalidAccountID";
            }
            if (!login.contains("@")) {
                
                try {
                    
                    account = (Account) ges.getEntityManager().createQuery("SELECT a FROM Account a WHERE a.deleted = FALSE AND a.login = :login AND a.password = :password")
                            .setParameter("login", login)
                            .setParameter("password", password).getSingleResult();
                    
                } catch (Throwable th) {
                      logger.error("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"retrieveUserCode"+"\n"
                            +"error       : "+th.getMessage());
                      
                   // System.out.println("retrieveUserCode: Error: " + th.getMessage());
                }
            } else {
                
                try {
                    
                    account = (Account) ges.getEntityManager().createQuery("SELECT a FROM Account a WHERE a.deleted = FALSE AND a.email = :email AND a.password = :password")
                            .setParameter("email", login)
                            .setParameter("password", password).getSingleResult();
                    
                } catch (Throwable th) {
                    
                     logger.error("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"retrieveUserCode"+"\n"
                            +"error       : "+th.getMessage());
                     
                    //System.out.println("retrieveUserCode: Error: " + th.getMessage());
                }
            }
            if (account == null) {
               return "invalidLogin";
            } 
            user = account.getUser();
            if(!connectedAccount.getUser().equals(user)){
                
                logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"retrieveUserCode"+"\n"
                            +"info       : "+"retrieveUserCode: The user logged on is different from the requesting user of the word.");
                
                //System.out.println("retrieveUserCode: The user logged on is different from the requesting user of the word.");
                return "differenceBetweenLoginUserAndConnectedUser";
            }

            if (generate) {
                
                String userCode = CodeGenerate.generateCode((int) user.getUserID());
                
                user.setUserCode(userCode);
                ges.getEntityManager().merge(user);
                ges.closeEm();
                
            }
            
        } catch (Throwable th) {
             logger.error("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"retrieveUserCode"+"\n"
                            +"error       : "+th.getMessage());
             
            //System.out.println("retrieveUserCode: Error: " + th.getMessage());
             throw new AccountManagementException("AccountManagementDao","retrieveUserCode",8,th.getMessage(),th.getMessage());
            //return "" +  th.getMessage();
        }

        String res =  "good;" + user.getAccountID().getEmail() + ";"
                + user.getName()+ ";" 
                + user.getSurname()+ ";" 
                + user.getUserCode()+ ";" 
                + user.getAccountID().getAccountID()+ ";"
                ;
        
         logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"retrieveUserCode"+"\n"
                            +"info       : "+res);
                
        //System.out.println("retrieveUserCode: return: " + res);
        return res;
    }

    @Override
    public String checkExcavatorUserCode(int accountID, String userCode) throws AccountManagementException{

        User user = null;
        Account account = null;

        try {

            ges.creatEntityManager();

            account = (Account) ges.getEntityManager().find(Account.class, accountID);

            if (account == null || account.getSocialstatusID().getSocialstatusID() != TypeOfUser.SOCIAL_STATUS_EXCAVATOR_ID) {
               return "InvalidAccountID";
            }
            
            user = account.getUser();
            if(!user.getUserCode().equals(userCode)){
                return "InvalidUserCode";
            }
            
        } catch (Throwable th) {
            logger.error("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"retrieveUserCode"+"\n"
                            +"error       : "+th.getMessage());
            // System.out.println("retrieveUserCode: Error: " + th.getMessage());
             throw new AccountManagementException("AccountManagementDao","retrieveUserCode",8,th.getMessage(),th.getMessage());
            
            //return "" +  th.getMessage();
        }

        String res =  "good;" + user.getAccountID().getEmail() + ";"
                + user.getName()+ ";" 
                + user.getSurname()+ ";" 
                + user.getUserCode()+ ";" 
                + user.getAccountID().getAccountID()+ ";"
                ;
        
        logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"retrieveUserCode"+"\n"
                            +"info       : "+res);
        
        //System.out.println("retrieveUserCode: return: " + res);
        return "good";
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getUserStatistics(int accountID) {
        
        logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"getUserStatistics"+"\n"
                            +"info       : "+accountID);
       // System.out.println("[getUserStatistics] accountID: " + accountID);
        Result result = new Result();
        String res = "good";
        Account account = null;
        List<String> listResult = new ArrayList();
        
        try{
            
            ges.creatEntityManager();
            account  = (Account)ges.getEntityManager().find(Account.class, accountID);
            ges.closeEm();
        }catch(Throwable th){
            res += th.getMessage();
        }
        
        if (account == null) {
            result.setMsg("InvalidAccountID");
            return result;
        }

        int statusID = account.getSocialstatusID().getSocialstatusID();
        
        if (statusID == TypeOfUser.SOCIAL_STATUS_TRUCK_OWNER_ID) {

            Query query = StatisticsQuery.getQueryForSearchTruckOwnerStatistics(ges, account.getUser());
            Iterator queryResult = query.getResultList().iterator();
            if (queryResult.hasNext()) {
                Object[] object = (Object[]) queryResult.next();
                listResult.add(String.valueOf(object[0]));//TotalOfNonConfirmJobByTruckOwner
                listResult.add(String.valueOf(object[1]));//ToTalOfUnSendTicketByTruckOwner
                listResult.add(String.valueOf(object[2]));//TotalOfNonConfirmTruckByTruckOwner
            }

        } else if (statusID == TypeOfUser.SOCIAL_STATUS_EXCAVATOR_ID
                || statusID == TypeOfUser.SOCIAL_STATUS_GOV_ID
                || statusID == TypeOfUser.SOCIAL_STATUS_SIMPLE_USER_ID) {

            Query query = StatisticsQuery.getQueryForSearchExcavatorStatistics(ges, account.getUser());
            Iterator queryResult = query.getResultList().iterator();
            if (queryResult.hasNext()) {
                Object[] object = (Object[]) queryResult.next();
                listResult.add(String.valueOf(object[0]));//TotalOfNonConfirmJobByExcavator
                listResult.add(String.valueOf(object[1]));//TotalOfUnvalidateTicketForExcavator
                listResult.add(String.valueOf(object[2]));//TotalOfUnPaidTicketByExcavator
            }

        }
        logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"getUserStatistics"+"\n"
                            +"info       : "+"++++user.getUserID() = " + account.getUser()+"\n"
                            +"++++listResult = " + listResult);
        
        //System.out.println("++++user.getUserID() = " + account.getUser());
        //System.out.println("++++listResult = " + listResult);

        result.setMsg(res);
        
        result.setObjectList(listResult);
        return result;
        
    }

    
}
