/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package accountManagement.metier;

import accountManagement.dao.IAccountManagementDaoLocal;
import easencrypdecryp.SpecialCharToAsciiHex;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.ejb.Stateless;
import mailing.mailSending.MailSendingForAccountCreation;
import mailing.mailSending.MailToContactUs;
import mailing.mailSending.MailToRetrieveUserCode;
import mailing.mailSending.PwdRecover;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import toolsAndTransversalFunctionnalities.Result;
import util.exception.AccountManagementException;

/**
 *
 * @author erman
 */
@WebService(serviceName = "AccountManagementWs")
@Stateless()
public class AccountManagementWs implements IAccountManagementWs{

    @EJB
    private IAccountManagementDaoLocal dao;

    private MailSendingForAccountCreation mail;
   
    private PwdRecover pwdMail;
    
     final static Logger logger = LogManager.getLogger("dump1");
    
    @WebMethod(operationName = "addNewUser")
    @Override
    public String addNewUser(String name, String surname, String address, String email, String telephone, String gpsCoordinate, String cardNumber, String login, String password, String servicesID, int socialStatusID, String compagnyName){
        System.out.println("Service call");
        String res;
        try{
            
            if (login.trim().contains("@")) {
                return "invalidLogin";
            }
            try{
                
            res = dao.addNewUser(name, surname, address, email, telephone, gpsCoordinate, cardNumber, login.trim(), password.trim(), servicesID, socialStatusID, compagnyName);
           
            }catch(AccountManagementException error){
              res=error.getSmallMessage();
                logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"modifyPasswordByLogin"+"\n"
                            +"Class dao : "+error.getClasse()+"\n"
                            +"Fct   dao : "+error.getFonction()+"\n"
                            +"Info error "+error.getSmallMessage()+"\n"
                            +"Erreur   : "+"\n"
                            +error.getFullMessage());
              // logger.error(error.getFonction()+"---"+error.getSmallMessage()+"---"+error.getFullMessage());
            }
              System.out.println("Service ok 0");  
            if(res!=null && res.contains("good")){
           
                String[] result=res.split(";");
                int accountID=(new Integer(result[1]));
                 String libelle;
                System.out.println("Service ok 1");  
               try{
                 libelle = dao.assignPromotionCode(accountID, 1);
                 
               }catch(AccountManagementException error){
                   
              libelle=error.getSmallMessage();
               logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"modifyPasswordByLogin"+"\n"
                            +"Class dao : "+error.getClasse()+"\n"
                            +"Fct   dao : "+error.getFonction()+"\n"
                            +"Info error "+error.getSmallMessage()+"\n"
                            +"Erreur   : "+"\n"
                            +error.getFullMessage());
              //logger.error(error.getFonction()+"---"+error.getSmallMessage()+"---"+error.getFullMessage());
            
            }
                
                int typeOfUser = Integer.parseInt(res.split(";")[4]);
                String userCode = res.split(";")[5];
                
                String validationCode = (userCode == null || userCode.equals("") || userCode.equals("null") ? "" :
                        "<li>&nbsp;&nbsp;Use this code: <b><i>"+ userCode +"</i></b> to validate Jobs.</li><br>"
                        + "<span style=\"color: red\"><b> <i>Please, keep your code in secure location!!</i>"
                        + " </b></span>" );
                
                String promotionDescription = libelle.split(";")[5];
                mail = new MailSendingForAccountCreation();
                  System.out.println("Service d'envoi");
                mail.sendAccountCreationMsg(email, name, surname, login, password, accountID, typeOfUser, validationCode, promotionDescription);
                res="good"+";"+libelle;
            }
    //* 
            }catch(Throwable e){
                System.out.println(e.getMessage());
                StringWriter string = new StringWriter();
                PrintWriter str = new PrintWriter(string);
                e.printStackTrace(str);
                res = string.toString();
                logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"addNewUser"+"\n"
                            +"Erreur   : "+"\n"
                            +res);
            
                return res;
        }
      //*/
            
        return res;
    }
    
    @WebMethod(operationName = "modifyInfo")
    @Override
    public String modifyInfo(int accountID, String name, String surname,
            String address, String email, String telephone, String gpsCoordinate, 
            String cardNumber, String compagnyName, String cellPhone, String routineNumber) {
        
        String res;
        try{
            
            res = dao.modifyInfo(accountID, name, surname, address, email, telephone, gpsCoordinate, cardNumber, compagnyName, cellPhone, routineNumber);
            
        }catch(AccountManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"modifyInfo"+"\n"
                            +"Class dao : "+error.getClasse()+"\n"
                            +"Fct   dao : "+error.getFonction()+"\n"
                            +"Info error "+error.getSmallMessage()+"\n"
                            +"Erreur   : "+"\n"
                            +error.getFullMessage());
            
        }
        catch(Throwable e){
                StringWriter string = new StringWriter();
                PrintWriter str = new PrintWriter(string);
                e.printStackTrace(str);
                res = string.toString();
                 logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"modifyInfo"+"\n"
                            +"Erreur   : "+"\n"
                            +res);
                return res;
        }
        
        return res;
    }
    
    @WebMethod(operationName = "activate")
    @Override
    public String activate(String encryData) {

        String res;
        try {
             String chaine = encryData.replaceAll("\\w", "");
            for(char t: chaine.toCharArray()){
                encryData = encryData.replace(t+"", SpecialCharToAsciiHex.convertSpetialChar(t));
            }
         
            res = dao.activate(encryData);
            
         }catch(AccountManagementException error){
            res=error.getSmallMessage();   
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"modifyPasswordByLogin"+"\n"
                            +"Class dao : "+error.getClasse()+"\n"
                            +"Fct   dao : "+error.getFonction()+"\n"
                            +"Info error "+error.getSmallMessage()+"\n"
                            +"Erreur   : "+"\n"
                            +error.getFullMessage());
        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"activate"+"\n"
                            +"Erreur   : "+"\n"
                            +res);
            return res;
        }

        return res;
    }

    @WebMethod(operationName = "modifyPasswordByLogin")
    @Override
    public String modifyPasswordByLogin(int accountID, String login, String encienPassword, String newPassword) {
        String res;

        try {

            if (login.trim().contains("@")) {
                return "invalidLogin";
            }

            res = dao.modifyPasswordByLogin(accountID, login.trim(), encienPassword.trim(), newPassword.trim());
             
        }
        catch(AccountManagementException error){
            res=error.getSmallMessage();
             logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"modifyPasswordByLogin"+"\n"
                            +"Class dao : "+error.getClasse()+"\n"
                            +"Fct   dao : "+error.getFonction()+"\n"
                            +"Info error "+error.getSmallMessage()+"\n"
                            +"Erreur   : "+"\n"
                            +error.getFullMessage());
            
        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
            logger.error(    "\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"modifyPasswordByLogin"+"\n"
                            +"Erreur   : "+"\n"
                            +res);
            return res;
        }
        return res;
    }

    @WebMethod(operationName = "modifyPassword")
    @Override
    public String modifyPassword(int accountID, String encienPassword, String newPassword) {
        String res;

        try {
            
            res = dao.modifyPassword(accountID, encienPassword, newPassword);
            
             }catch(AccountManagementException error){
            res=error.getSmallMessage();
             logger.error(   "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"modifyPassword"+"\n"
                            +"Class dao : "+error.getClasse()+"\n"
                            +"Fct   dao : "+error.getFonction()+"\n"
                            +"Info error "+error.getSmallMessage()+"\n"
                            +"Erreur   : "+"\n"
                            +error.getFullMessage());      
        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
            logger.error(    "\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"modifyPassword"+"\n"
                            +"Erreur   : "+"\n"
                            +res);
            return res;
        }
        return res;
    }

    @WebMethod(operationName = "login")
    @Override
    public String login(String login, String password, int serviceID){
        
        String res="";
        
        try{
            
           res = dao.login(login.trim(), password.trim(), serviceID);
           
        }catch(AccountManagementException error){
            
            res=error.getSmallMessage(); 
           logger.error(   "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"login"+"\n"
                            +"Class dao : "+error.getClasse()+"\n"
                            +"Fct   dao : "+error.getFonction()+"\n"
                            +"Info error "+error.getSmallMessage()+"\n"
                            +"Erreur   : "+"\n"
                            +error.getFullMessage());    
        }catch(Throwable e){
                StringWriter string = new StringWriter();
                PrintWriter str = new PrintWriter(string);
                e.printStackTrace(str);
                res = string.toString();
                 logger.error(    "\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"login"+"\n"
                            +"Erreur   : "+"\n"
                            +res);
               // System.out.println(res);
                return res;
        }      
        return res;
    }
    
    @WebMethod(operationName = "logout")
    @Override
    public String logout(int accountID, String accountHistID, String phoneID){
         
        String res="";
        try{
            
            res = dao.logout(accountID, accountHistID, phoneID);
          }
               catch(AccountManagementException error){
               logger.error(   "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"logout"+"\n"
                            +"Class dao : "+error.getClasse()+"\n"
                            +"Fct   dao : "+error.getFonction()+"\n"
                            +"Info error "+error.getSmallMessage()+"\n"
                            +"Erreur   : "+"\n"
                            +error.getFullMessage());  
            res=error.getSmallMessage();
        }catch(Throwable e){
                StringWriter string = new StringWriter();
                PrintWriter str = new PrintWriter(string);
                e.printStackTrace(str);
                res = string.toString();
                logger.error(    "\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"logout"+"\n"
                            +"Erreur   : "+"\n"
                            +res);
                return res;
        }      
        return res;
    }
    
    @WebMethod(operationName = "recovePassword")
    @Override
    public Result recovePassword(String email) {

        Result resultat = new Result();
        try {

            resultat = dao.recoverPasswd(email);

            if (resultat != null && resultat.getMsg().contains("good")) {

                String name = resultat.getObjectList().get(0);
                String firstName = resultat.getObjectList().get(1);
                String login = resultat.getObjectList().get(2);
                String pwd = resultat.getObjectList().get(3);

                pwdMail = new PwdRecover();
                pwdMail.sendMail(email, name, firstName, login, pwd);
            }

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            String res = string.toString();
            logger.error(    "\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"recovePassword"+"\n"
                            +"Erreur   : "+"\n"
                            +res);
            //System.out.println("" + res);
            resultat.setMsg(res);
            return resultat;
        }

        return resultat;
    }

    @WebMethod(operationName = "getUserByAccountID")
    @Override
    public Result getUserByAccountID(int accountID) {
        
        Result resultat = new Result();
        String res;
        
         try{
             
             resultat = dao.getUserByAccountID(accountID);
            
         }catch(Throwable e){
             StringWriter string = new StringWriter();
             PrintWriter str = new PrintWriter(string);
             e.printStackTrace(str);
             res = string.toString();
             resultat.setMsg(res);
                 logger.error(    "\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getUserByAccountID"+"\n"
                            +"Erreur   : "+"\n"
                            +res);
             return resultat;
         }
         
         return resultat;
    }

    @WebMethod(operationName = "activateService")
    @Override
    public String activateService(int accountID, int serviceID) {
        
        String res;
        try{
            
            res = dao.activateService(accountID, serviceID);
            }
        
        catch(AccountManagementException error){
            
            res=error.getSmallMessage(); 
            logger.error(   "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"activateService"+"\n"
                            +"Class dao : "+error.getClasse()+"\n"
                            +"Fct   dao : "+error.getFonction()+"\n"
                            +"Info error "+error.getSmallMessage()+"\n"
                            +"Erreur   : "+"\n"
                            +error.getFullMessage()); 
            
        }catch(Throwable e){
                StringWriter string = new StringWriter();
                PrintWriter str = new PrintWriter(string);
                e.printStackTrace(str);
                res = string.toString();
                logger.error(    "\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"activateService"+"\n"
                            +"Erreur   : "+"\n"
                            +res);
                return res;
        }
        
        return res;
    }
    
    @WebMethod(operationName = "modifyBillingInfo")
    @Override
    public String modifyBillingInfo(int accountID, String name, String surname, String securityCode,
            String address, String email, String telephone, String cellPhone, String creditCard, 
            String cardExpiration, String bankName, String accountNumber, String accountName, int accountTypeID,
            int promotionCodeID, double monthlyCost, String billingCycle, double ajustedCost, String typeOfCreditCard, 
            String paymentType, String routingNumber, String nameOnCreditCard){  
        String res;
        try{
            
            res = dao.modifyBillingInfo(accountID, name, surname, securityCode, address, email, telephone, cellPhone, creditCard, cardExpiration, bankName, accountNumber, accountName, accountTypeID, promotionCodeID, billingCycle, typeOfCreditCard, paymentType, routingNumber, nameOnCreditCard);
             }
        catch(AccountManagementException error){
            
            res=error.getSmallMessage();
        }catch(Throwable e){
                StringWriter string = new StringWriter();
                PrintWriter str = new PrintWriter(string);
                e.printStackTrace(str);
                res = string.toString();
                logger.error(    "\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"modifyBillingInfo"+"\n"
                            +"Erreur   : "+"\n"
                            +res);
                return res;
        }
        
        return res;
    }

    @WebMethod(operationName = "getPromotionCodeByID")
    @Override
    public Result getPromotionCodeByID(int promotionCodeID) {
        
        Result resultat = new Result();
        String res;
        
         try{
             
             resultat = dao.getPromotionCodeByID(promotionCodeID);
            
         }catch(Throwable e){
             StringWriter string = new StringWriter();
             PrintWriter str = new PrintWriter(string);
             e.printStackTrace(str);
             res = string.toString();
             resultat.setMsg(res);
              logger.error(    "\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getPromotionCodeByID"+"\n"
                            +"Erreur   : "+"\n"
                            +res);
             return resultat;
         }
         
         return resultat;
    }

    @WebMethod(operationName = "applyPromotionCode")
    @Override
    public Result applyPromotionCode(int accountID, String promotionCodeLibel, int accountTypeID) {
        
        Result resultat = new Result();
        String res;
        
         try{
             
             resultat = dao.applyPromotionCode(accountID, promotionCodeLibel, accountTypeID);
            
         }catch(Throwable e){
             StringWriter string = new StringWriter();
             PrintWriter str = new PrintWriter(string);
             e.printStackTrace(str);
             res = string.toString();
             resultat.setMsg(res);
                logger.error(    "\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"applyPromotionCode"+"\n"
                            +"Erreur   : "+"\n"
                            +res);
             return resultat;
         }
         
         return resultat;
    }

    @WebMethod(operationName = "assignPromotionCode")
    @Override
    public String assignPromotionCode(int accountID, int promotionCodeID) {
        
        String res;
        try{
            
            res = dao.assignPromotionCode(accountID, promotionCodeID);
          }
        catch(AccountManagementException error){
        res=error.getSmallMessage();  
         logger.error(   "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"assignPromotionCode"+"\n"
                            +"Class dao : "+error.getClasse()+"\n"
                            +"Fct   dao : "+error.getFonction()+"\n"
                            +"Info error "+error.getSmallMessage()+"\n"
                            +"Erreur   : "+"\n"
                            +error.getFullMessage());    
        
        }catch(Throwable e){
                StringWriter string = new StringWriter();
                PrintWriter str = new PrintWriter(string);
                e.printStackTrace(str);
                res = string.toString();
                logger.error(    "\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"assignPromotionCode"+"\n"
                            +"Erreur   : "+"\n"
                            +res);
                return res;
        }
        
        return res;
    }

    @WebMethod(operationName = "checkUserInfo")
    @Override
    public String checkUserInfo(int accountID, String accountHEncrypt, int accountNumber) {
        String res="";
        
        try{
            
           res = dao.checkUserInfo(accountID, accountHEncrypt, accountNumber);
          }
        catch(AccountManagementException error){
            
              res=error.getSmallMessage();  
               logger.error(   "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"checkUserInfo"+"\n"
                            +"Class dao : "+error.getClasse()+"\n"
                            +"Fct   dao : "+error.getFonction()+"\n"
                            +"Info error "+error.getSmallMessage()+"\n"
                            +"Erreur   : "+"\n"
                            +error.getFullMessage()); 
        }catch(Throwable e){
                StringWriter string = new StringWriter();
                PrintWriter str = new PrintWriter(string);
                e.printStackTrace(str);
                res = string.toString();
                //System.out.println(res);
                 logger.error(    "\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"checkUserInfo"+"\n"
                            +"Erreur   : "+"\n"
                            +res);
                return res;
            
        } 
        logger.info(getClass() +" --- " +"[checkUserInfo(WS)] res  --- "+res);
        //System.out.println("[checkUserInfo(WS)] res: " + res);
        return res;
    }
    
    @WebMethod(operationName = "retrieveUserCode")
    @Override
    public String retrieveUserCode(int accountID, String login, String password, boolean generate) {
        
        String result = "";
        String res="good";
         try{
            try{ 
             res = dao.retrieveUserCode(accountID, login, password, generate);
              
            }catch(AccountManagementException error){
            res=error.getSmallMessage();
           logger.error(   "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"retrieveUserCode"+"\n"
                            +"Class dao : "+error.getClasse()+"\n"
                            +"Fct   dao : "+error.getFonction()+"\n"
                            +"Info error "+error.getSmallMessage()+"\n"
                            +"Erreur   : "+"\n"
                            +error.getFullMessage());    
              }
             if(!res.contains("good")){
                 return res;
             }

             result = res.split(";")[0];
             String receiver = res.split(";")[1];
             String name = res.split(";")[2];
             String firstName = res.split(";")[3];
             String userCode = "<i><b>" + res.split(";")[4] + "</b></i>";
             
             MailToRetrieveUserCode jobMsg = new MailToRetrieveUserCode();
             jobMsg.sendMailToRetrieveUserCode(receiver, name, firstName, userCode);
             

         } catch (Throwable e) {
             StringWriter string = new StringWriter();
             PrintWriter str = new PrintWriter(string);
             e.printStackTrace(str);
             res = string.toString();
            logger.error(    "\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"retrieveUserCode"+"\n"
                            +"Erreur   : "+"\n"
                            +res);
             //System.out.println("retrieveUserCode: Error: "+res);
             return res;
         }
         
         return result;
    }
     
    
    @WebMethod(operationName = "checkExcavatorUserCode")
    @Override
    public String checkExcavatorUserCode(int accountID, String userCode) {
        
        String res;
         try{
             
             res = dao.checkExcavatorUserCode(accountID, userCode);
           
         }catch(AccountManagementException error){
            res=error.getSmallMessage();
            logger.error(   "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"checkExcavatorUserCode"+"\n"
                            +"Class dao : "+error.getClasse()+"\n"
                            +"Fct   dao : "+error.getFonction()+"\n"
                            +"Info error "+error.getSmallMessage()+"\n"
                            +"Erreur   : "+"\n"
                            +error.getFullMessage());      
             
         } catch (Throwable e) {
             StringWriter string = new StringWriter();
             PrintWriter str = new PrintWriter(string);
             e.printStackTrace(str);
             res = string.toString();
              logger.error(    "\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"checkExcavatorUserCode"+"\n"
                            +"Erreur   : "+"\n"
                            +res);
            // System.out.println("retrieveUserCode: Error: "+res);
             return res;
         }
         
         return res;
    }
     
    @WebMethod(operationName = "contactUs")
    @Override
    public String contactUs(String message, String subject, String name, String email) {
        MailToContactUs mailToContactUser = new MailToContactUs();
        String sendMsgResult;
        String receiver = "support@trucksanddirt.com";

        String msg = "from <I>" + email + "</I> send us this message: <br><br>" + message;

        sendMsgResult = mailToContactUser.sendMailToContactUs(receiver, "Dear " + name + ",<br>" + msg, "Question from a user: " + subject);

        logger.info(getClass() +" --- " +"contactUs --- "+sendMsgResult);
       // System.out.println("Status du message=" + sendMsgResult);

        if (sendMsgResult.equals("envoyé")) {
            sendMsgResult = "good";
        } else {
            sendMsgResult = "bad";
        }

        return sendMsgResult;
    }

    @WebMethod(operationName = "getUserStatistics")
    @Override
    public Result getUserStatistics(int accountID) {

        Result resultat = new Result();
        String res;

        try {

            resultat = dao.getUserStatistics(accountID);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
            resultat.setMsg(res);
             logger.error(    "\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getUserStatistics"+"\n"
                            +"Erreur   : "+"\n"
                            +res);
            return resultat;
        }

        return resultat;
    }


}
