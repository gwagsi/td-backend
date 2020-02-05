/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package notificationParam.metier;

import java.io.PrintWriter;
import java.io.StringWriter;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import notificationParam.dao.IUserNotificationParamsDaoLocal;
import org.apache.logging.log4j.LogManager;
import toolsAndTransversalFunctionnalities.Result;
import util.exception.UserNotificationParamsException;

/**
 *
 * @author erman
 */
@WebService(serviceName = "UserNotificationParamsWs")
@Stateless()
public class UserNotificationParamsWs implements IUserNotificationParamsWs{
    
    @EJB
    private IUserNotificationParamsDaoLocal dao;
    
    final static org.apache.logging.log4j.Logger logger = LogManager.getLogger("dump1");
    
    @WebMethod(operationName = "addNewNotificationParams")
    @Override
    public String addNewNotificationParams (int accountID, String email, float latitude, float longitude, String zipCode, float raduis) {
        String res;
        try {

            res = dao.addNewNotificationParams(accountID, email, latitude, longitude, zipCode, raduis);
             }catch(UserNotificationParamsException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"addNewNotificationParams"+"\n"
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
                            +"Fct   ws- : "+"addNewNotificationParams"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    }
    
    @WebMethod(operationName = "editNotificationParams")
    @Override
    public String editNotificationParams(int accountID, int notificationParamID, String email, float latitude, float longitude, String zipCode, float raduis) {
        String res;
        try {

            res = dao.editNotificationParams(accountID, notificationParamID, email, latitude, longitude, zipCode, raduis);
            }catch(UserNotificationParamsException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"editNotificationParams"+"\n"
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
                            +"Fct   ws- : "+"editNotificationParams"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    }
    
    @WebMethod(operationName = "deleteNotificationParams")
    @Override
    public String deleteNotificationParams(int accountID, int notificationParamID) {
        String res;
        try {

            res = dao.deleteNotificationParams(accountID, notificationParamID);
}catch(UserNotificationParamsException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"deleteNotificationParams"+"\n"
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
                            +"Fct   ws- : "+"deleteNotificationParams"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    }

    @WebMethod(operationName = "getARangeOfUserNotificationParam")
    @Override
    public Result getARangeOfUserNotificationParam(int accountID, int index, int nombreMaxResultat) {
        Result result = new Result();
        try {

            result = dao.getARangeOfUserNotificationParam(accountID, index, nombreMaxResultat);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfUserNotificationParam"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }

}
