/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package driverManagement.metier;

import driverManagement.dao.IDriverManagementDaoLocal;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.ejb.Stateless;
import mailing.mailSending.MailSendingForDriverAccountCreation;
import org.apache.logging.log4j.LogManager;
import toolsAndTransversalFunctionnalities.Result;
import toolsAndTransversalFunctionnalities.ResultBackend;
import util.exception.BillingManagementException;
import util.exception.DriverManagementException;

/**
 *
 * @author erman
 */
@WebService(serviceName = "DriverManagementWs")
@Stateless()
public class DriverManagementWs implements IDriverManagementWs{
    
    @EJB
    private IDriverManagementDaoLocal dao;
     final static org.apache.logging.log4j.Logger logger = LogManager.getLogger("dump1");
    
    @WebMethod(operationName = "addNewDriver")
    @Override
    public String addNewDriver(String name, String surname, String cardNumber, String email, String telephone, int accountID, String picture, String address, String license) {
        String res = null;
        ResultBackend result;
        try {

            if (picture == null || picture.equals("")) {
                picture = "" + 0;
            }

            if (license == null || license.equals("")) {
                license = "" + 0;
            }

            result = dao.addNewDriver(name, surname, cardNumber, email, telephone, accountID, picture, address, license);

            res = result.getMsg();
            System.out.println("addNewDriver - res: " + res);
            System.out.println("addNewDriver - driverInfo: " + result.getObject());

            if (res != null && res.contains("good")) {

                String[] driverMailInfo = (String[]) result.getResultArraysList().get(0);
                MailSendingForDriverAccountCreation sendMsgAddNewJob = new MailSendingForDriverAccountCreation();
                sendMsgAddNewJob.sendAccountCreationMail(driverMailInfo);

            }

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+" addNewDriver"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    }

    @WebMethod(operationName = "getARangeOfDriverByUser")
    @Override
    public Result getARangeOfDriverByUser(int accountID, int index, int nombreMaxResultat) {
        Result result = new Result();
        try{
            
            result = dao.getARangeOfDriverByUser(accountID, index, nombreMaxResultat);
            
        }catch(Throwable e){
                StringWriter string = new StringWriter();
                PrintWriter str = new PrintWriter(string);
                e.printStackTrace(str);
                result.setMsg(string.toString());
                 logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+" getARangeOfDriverByUser"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
                return result;
        }
        
        result.afficherResult("getARangeOfDriverByUser");
        return result;
    }

    @WebMethod(operationName = "modifyDriverInfo")
    @Override
    public String modifyDriverInfo(int driverID, String name, String surname, String cardNumber, String email, String telephone, int accountID, String picture, String address, String license) {
        String res;
        ResultBackend result;
        try {

            result = dao.modifyDriverInfo(driverID, name, surname, cardNumber, email, telephone, accountID, picture, address, license);

            res = result.getMsg();
            System.out.println("modifyDriverInfo - res: " + res);
            System.out.println("modifyDriverInfo - driverInfo: " + result.getObject());
            if (res != null && res.contains("good") && result.getResultArraysList() != null) {

                String[] driverMailInfo = (String[]) result.getResultArraysList().get(0);
                MailSendingForDriverAccountCreation sendMsgAddNewJob = new MailSendingForDriverAccountCreation();
                sendMsgAddNewJob.sendAccountCreationMail(driverMailInfo);

            }
            result.afficherResult("modifyDriverInfo");
        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+" modifyDriverInfo"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    }

    @WebMethod(operationName = "deleteDriver")
    @Override
    public String deleteDriver(int driverID){
        String res;
        try{
            
            res = dao.deleteDriver(driverID);
          }
        catch(DriverManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"deleteDriver"+"\n"
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
                logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"deleteDriver"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
                return res;
        }
        
        return res;
    }
    
    
}
