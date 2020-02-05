/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pushNotificationParam.dao;

import entities.Account;
import entities.JobNotification;
import entities.PhonePlateform;
import entities.PhoneRegistration;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;
import mailing.mailSending.MailFunction;
import org.apache.logging.log4j.LogManager;
import toolsAndTransversalFunctionnalities.GestionnaireEntite;
import toolsAndTransversalFunctionnalities.Result;
import util.date.DateFunction;
import util.exception.PushNotificationException;

/**
 *
 * @author erman
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PushNotificationDao implements IPushNotificationDaoLocal, IPushNotificationDaoRemote {
    
    @EJB
    GestionnaireEntite ges;
    final static org.apache.logging.log4j.Logger logger = LogManager.getLogger("dump1");
    
    @Override
    public String addPhoneNotificationParam (int accountID, String phoneID, String registrationID, int platteformID) throws PushNotificationException{
        
        String res = "good";
        Account account;
        
        PhoneRegistration phoneRegister = null;
        PhonePlateform plateform;
        
        try{
            
            ges.creatEntityManager();
            
            account = (Account) ges.getEntityManager().find(Account.class, accountID);
            plateform = (PhonePlateform) ges.getEntityManager().find(PhonePlateform.class, platteformID);

            if (account == null) {
                ges.closeEm();
                res = "InvalidAccountID";
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"addPushNotificationParams: res: " + res);
                return res;
            }

            if (plateform == null) {
                ges.closeEm();
                res = "InvalidPlateformID";
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"addPushNotificationParams: res: " + res);
                return res;
            }
            try {
                
                phoneRegister = (PhoneRegistration) ges.getEntityManager().createQuery("SELECT p FROM PhoneRegistration p WHERE p.phoneID = :phoneID")
                        .setParameter("phoneID", phoneID)
                        .getSingleResult();
            } catch (Exception e) {
                logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"addPushNotificationParams: phoneRegister: " + phoneRegister);
            }
           
            if (phoneRegister == null) {
                phoneRegister = new PhoneRegistration();
                phoneRegister.setPhoneID(phoneID);
                phoneRegister.setAccountID(account);
                phoneRegister.setRegisteredID(registrationID);
                phoneRegister.setPlateformID(plateform);
                phoneRegister.setDeleted(false);
                phoneRegister.setCreationDate(DateFunction.getGMTDate());
                phoneRegister.setLastModificationDate(DateFunction.getGMTDate());

                ges.getEntityManager().persist(phoneRegister);
            } else {
                phoneRegister.setAccountID(account);
                phoneRegister.setRegisteredID(registrationID);
                phoneRegister.setPlateformID(plateform);
                phoneRegister.setLastModificationDate(DateFunction.getGMTDate());

                ges.getEntityManager().merge(phoneRegister);
            }


            account.getPhoneRegistrationList().add(phoneRegister);
            ges.getEntityManager().merge(account);
            ges.closeEm();
            
            res += ";" + phoneRegister.getPhoneregistrationID();
            
        } catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            throw new PushNotificationException(getClass()+"","addPhoneNotificationParam",1,res,res);
        
        }
        
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"addNewNotificationParams: res: " + res);
        return res;
    }
    
    @Override
    public String updateJobNotificationStatus(int accountID, int jobNotificationID) throws PushNotificationException{
        
        String res = "good";
        Account account;
        
        JobNotification jobNotification;
       
        try{
            
            ges.creatEntityManager();
            
            account = (Account) ges.getEntityManager().find(Account.class, accountID);
            
            jobNotification = (JobNotification) ges.getEntityManager().find(JobNotification.class, jobNotificationID);
            
            if (account == null) {
                ges.closeEm();
                res = "InvalidAccountID";
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"updateJobNotificationStatus: res: " + res);
                return res;
            }
            
            if (jobNotification == null) {
                ges.closeEm();
                res = "InvalidNotificationParamID";
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"updateJobNotificationStatus: res: " + res);
                return res;
            }
            
            jobNotification.setState(true);
            
            ges.getEntityManager().merge(jobNotification);
            ges.closeEm();
            
            res += ";" + jobNotification.getJobnotificationID();
            
        } catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            throw new PushNotificationException(getClass()+"","updateJobNotificationStatus",1,res,res);
        
        }
        
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"updateJobNotificationStatus: res: " + res);
        return res;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfJobNotification(int accountID, int index, int nombreMaxResultat) throws PushNotificationException{
        
        List<JobNotification> jobNotificationList;
        Result result = new Result();
        List<String> listResult;
        int numberOfElts = 0;
        Account account;
        String res = "";
        
        try{
            ges.creatEntityManager();
            account = (Account) ges.getEntityManager().find(Account.class, accountID);
            
            if (account == null) {
                ges.closeEm();
                result.setMsg("InvalidAccountID");
                result.afficherResult("getARangeOfUserNotificationParam");
                return result;
            }
            
            Query query =  ges.getEntityManager().createQuery("SELECT u FROM JobNotification u WHERE u.deleted = FALSE AND u.accountID = :accountID ORDER BY u.creationDate DESC")
                    .setParameter("accountID", account);
            
            numberOfElts = query.getResultList().size();
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResultat);
            jobNotificationList = (List<JobNotification>)query.getResultList();
            result.setMsg("good");

        } catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            result.setMsg(res);
            result.afficherResult("getARangeOfUserNotificationParam");
            return result;
        }

        listResult = new ArrayList<>();

        for (JobNotification jobNotification : jobNotificationList) {
            String name = jobNotification.getJobID().getExcavatorID().getName();
            String surname = jobNotification.getJobID().getExcavatorID().getSurname();
            res = ""+ jobNotification.getJobnotificationID() + ";"//0
                    + jobNotification.getValue()+ ";"//Message de la notification
                    + jobNotification.getState()+ ";"//2
                    + jobNotification.getCreationDate().getTime()+ ";"//3
                    + jobNotification.getAccountID().getAccountID()+ ";"//4
                    + jobNotification.getJobnotificationtypeID().getJobnotificationtypeID()+ ";"//5
                    + jobNotification.getJobnotificationtypeID().getNotifiacationType()+ ";"//6
                    + MailFunction.getTotalName(name, surname) + ";"//7
                    + jobNotification.getJobID().getJobLocation() + ";"//8
                    + "null";
            listResult.add(res);
        }
        
        listResult.add("" + numberOfElts);
        result.setObjectList(listResult);
        result.afficherResult("getARangeOfUserNotificationParam");
        return result;
    }


}
