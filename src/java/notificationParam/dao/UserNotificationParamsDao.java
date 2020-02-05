/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notificationParam.dao;

import entities.Account;
import entities.UserNotificationParams;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import toolsAndTransversalFunctionnalities.GestionnaireEntite;
import toolsAndTransversalFunctionnalities.Result;
import util.date.DateFunction;
import util.exception.UserNotificationParamsException;

/**
 *
 * @author erman
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UserNotificationParamsDao implements IUserNotificationParamsDaoLocal, IUserNotificationParamsDaoRemote {
    
    @EJB
    GestionnaireEntite ges;
     final static org.apache.logging.log4j.Logger logger = LogManager.getLogger("dump1");
    
    @Override
    public String addNewNotificationParams (int accountID, String email, float latitude, float longitude, String zipCode, float raduis) throws UserNotificationParamsException{
        
        String res = "good";
        Account account;
        
        UserNotificationParams userNotificationParam;
       
        try{
            
            ges.creatEntityManager();
            
            account = (Account) ges.getEntityManager().find(Account.class, accountID);

            if (account == null) {
                ges.closeEm();
                res = "InvalidAccountID";
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"addNewNotificationParams: res: " + res);
                return res;
            }
            
            userNotificationParam = new UserNotificationParams();
            userNotificationParam.setUserID(account.getUser());
            userNotificationParam.setEmail(email);
            userNotificationParam.setLatitude(latitude);
            userNotificationParam.setLongitude(longitude);
            userNotificationParam.setZipCode(zipCode);
            userNotificationParam.setRaduis(raduis);
            userNotificationParam.setDeleted(false);
            userNotificationParam.setCreationDate(DateFunction.getGMTDate());
            
            ges.getEntityManager().persist(userNotificationParam);
            ges.closeEm();
            
            res += ";" + userNotificationParam.getUsernotificationparamsID();
            
        } catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            throw new UserNotificationParamsException(getClass()+"","addNewNotificationParams",1,res,res);
        
        }
        
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"addNewNotificationParams: res: " + res);
        return res;
    }
    
    @Override
    public String editNotificationParams(int accountID, int notificationParamID, String email, float latitude, float longitude, String zipCode, float raduis) throws UserNotificationParamsException{
        
        String res = "good";
        Account account;
        
        UserNotificationParams userNotificationParam;
       
        try{
            
            ges.creatEntityManager();
            
            account = (Account) ges.getEntityManager().find(Account.class, accountID);
            
            userNotificationParam = (UserNotificationParams) ges.getEntityManager().find(UserNotificationParams.class, notificationParamID);
            
            if (account == null) {
                ges.closeEm();
                res = "InvalidAccountID";
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editNotificationParams: res: " + res);
                return res;
            }
            
            if (userNotificationParam == null || userNotificationParam.getDeleted()) {
                ges.closeEm();
                res = "InvalidNotificationParamID";
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editNotificationParams: res: " + res);
                return res;
            }
            
            userNotificationParam.setUserID(account.getUser());
            userNotificationParam.setEmail(email);
            userNotificationParam.setLatitude(latitude);
            userNotificationParam.setLongitude(longitude);
            userNotificationParam.setZipCode(zipCode);
            userNotificationParam.setRaduis(raduis);
            
            ges.getEntityManager().merge(userNotificationParam);
            ges.closeEm();
            
            res += ";" + userNotificationParam.getUsernotificationparamsID();
            
        } catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
             throw new UserNotificationParamsException(getClass()+"","editNotificationParams",1,res,res);
        
        }
        
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editNotificationParams: res: " + res);
        return res;
    }
    
    @Override
    public String deleteNotificationParams(int accountID, int notificationParamID) throws UserNotificationParamsException{
        
        String res = "good";
        Account account;
        
        UserNotificationParams userNotificationParam;
       
        try{
            
            ges.creatEntityManager();
            
            account = (Account) ges.getEntityManager().find(Account.class, accountID);
            
            userNotificationParam = (UserNotificationParams) ges.getEntityManager().find(UserNotificationParams.class, notificationParamID);
            
            if (account == null) {
                ges.closeEm();
                res = "InvalidAccountID";
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"deleteNotificationParams: res: " + res);
                return res;
            }
            
            if (userNotificationParam == null || userNotificationParam.getDeleted()) {
                ges.closeEm();
                res = "InvalidNotificationParamID";
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"deleteNotificationParams: res: " + res);
                return res;
            }
            
            userNotificationParam.setDeleted(true);
            
            ges.getEntityManager().merge(userNotificationParam);
            ges.closeEm();
            
            res += ";" + userNotificationParam.getUsernotificationparamsID();
            
        } catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            throw new UserNotificationParamsException(getClass()+"","deleteNotificationParams",1,res,res);
        
        }
        
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"deleteNotificationParams: res: " + res);
        return res;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfUserNotificationParam(int accountID, int index, int nombreMaxResultat) throws UserNotificationParamsException{
        
        List<UserNotificationParams> userNotificationParamList;
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
            
            Query query =  ges.getEntityManager().createQuery("SELECT u FROM UserNotificationParams u WHERE u.deleted = FALSE AND u.userID = :accountID")
                    .setParameter("accountID", account.getUser());
            
            numberOfElts = query.getResultList().size();
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResultat);
            userNotificationParamList = (List<UserNotificationParams>)query.getResultList();
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

        for (UserNotificationParams userNotificationParam : userNotificationParamList) {
            res = "" + userNotificationParam.getUsernotificationparamsID()+ ";"//0
                    + userNotificationParam.getEmail()+ ";"
                    + userNotificationParam.getZipCode()+ ";"
                    + userNotificationParam.getLatitude()+ ";"//4
                    + userNotificationParam.getLongitude()+ ";"//5
                    + userNotificationParam.getRaduis()+ ";"//6
                    + userNotificationParam.getUserID().getAccountID().getAccountID()+ ";"//7
                    + "null";
            listResult.add(res);
        }
        
        listResult.add("" + numberOfElts);
        result.setObjectList(listResult);
        result.afficherResult("getARangeOfUserNotificationParam");
        return result;
    }


}
