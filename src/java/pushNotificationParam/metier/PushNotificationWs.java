/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pushNotificationParam.metier;

import java.io.PrintWriter;
import java.io.StringWriter;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import org.apache.logging.log4j.LogManager;
import pushNotificationParam.dao.IPushNotificationDaoLocal;
import toolsAndTransversalFunctionnalities.Result;
import util.exception.PushNotificationException;

/**
 *
 * @author erman
 */
@WebService(serviceName = "PushNotificationWs")
@Stateless()
public class PushNotificationWs implements IPushNotificationWs{
    
    @EJB
    private IPushNotificationDaoLocal dao;
     final static org.apache.logging.log4j.Logger logger = LogManager.getLogger("dump1");
    
    @WebMethod(operationName = "addPhoneNotificationParam")
    @Override
    public String addPhoneNotificationParam (int accountID, String phoneID, String registrationID, int platteformID){
        String res;
        try {

            res = dao.addPhoneNotificationParam(accountID, phoneID, registrationID, platteformID);
            }catch(PushNotificationException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"addPhoneNotificationParam"+"\n"
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
                            +"Fct   ws- : "+"addPhoneNotificationParam"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    }
    
    @WebMethod(operationName = "updateJobNotificationStatus")
    @Override
    public String updateJobNotificationStatus(int accountID, int jobNotificationID) {
        String res;
        try {

            res = dao.updateJobNotificationStatus(accountID, jobNotificationID);
            }catch(PushNotificationException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"updateJobNotificationStatus"+"\n"
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
                            +"Fct   ws- : "+"updateJobNotificationStatus"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    }

    @WebMethod(operationName = "getARangeOfJobNotification")
    @Override
    public Result getARangeOfJobNotification(int accountID, int index, int nombreMaxResultat) {
        Result result = new Result();
        try {

            result = dao.getARangeOfJobNotification(accountID, index, nombreMaxResultat);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
               logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfJobNotification"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }

}
