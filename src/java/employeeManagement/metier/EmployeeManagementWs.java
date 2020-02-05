/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package employeeManagement.metier;

import employeeManagement.dao.EmployeeStatus;
import employeeManagement.dao.IEmployeeManagementDaoLocal;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import mailing.mailSending.MailSendingEmployeCode;
import org.apache.logging.log4j.LogManager;
import toolsAndTransversalFunctionnalities.Result;
import util.exception.EmployeeManagementException;

/**
 *
 * @author erman
 */
@WebService(serviceName = "EmployeeManagementWs")
@Stateless()
public class EmployeeManagementWs implements IEmployeeManagementWs {

    @EJB
    private IEmployeeManagementDaoLocal dao;
    final static org.apache.logging.log4j.Logger logger = LogManager.getLogger("dump1");
    
    @WebMethod(operationName = "generateEmployeeCode")
    @Override
    public String generateEmployeeCode(int excavatorID, String employeeName, String employeePhoneNumber, String employeeEmail, String description) {
        Result result = new Result();
        String res;
        try {

            result = dao.generateEmployeeCode(excavatorID, employeeName, employeePhoneNumber, employeeEmail, description);
            res = result.getMsg();

            if (!res.split(";")[0].contains("good")) {
                return res;
            }

            String[] sendingInfo = result.getObject().split(";");

            String empName = sendingInfo[0];
            String empCode = sendingInfo[1];
            String empEmail = sendingInfo[2];
            String empPhone = sendingInfo[3];

            String excavatorEmail = sendingInfo[5];
            String excavatorName = sendingInfo[6];
            String excavatorSurname = sendingInfo[7];

            MailSendingEmployeCode mailSendingEmplCode = new MailSendingEmployeCode();

            mailSendingEmplCode.sendMailToContactTruckOwner(excavatorEmail, excavatorName, excavatorSurname, empName, empEmail, empPhone, empCode);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"generateEmployeeCode"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result.getMsg();
        }

        return res;
    }

    @WebMethod(operationName = "editEmployeeInfo")
    @Override
    public String editEmployeeInfo(int excavatorID, int employeesID, String employeeName, String employeePhoneNumber, String employeeEmail, String description, String employeeCode) {
        String res;
        try {

            res = dao.editEmployeeInfo(excavatorID, employeesID, employeeName, employeePhoneNumber, employeeEmail, description, employeeCode);
        }catch(EmployeeManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"editEmployeeInfo"+"\n"
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
                            +"Fct   ws- : "+"editEmployeeInfo"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    }

    @WebMethod(operationName = "getARangeOfEmployeeByExcavator")
    @Override
    public Result getARangeOfEmployeeByExcavator(int excavatorID, int index, int nombreMaxResultat) {
        Result result = new Result();
        try {

            result = dao.getARangeOfEmployeeByExcavator(excavatorID, index, nombreMaxResultat);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfEmployeeByExcavator"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }

    @WebMethod(operationName = "suspendEmployee")
    @Override
    public String suspendEmployee(int excavatorID, int employeesID) {
        String res;
        try {

            res = dao.modifyEmployeeStatus(excavatorID, employeesID, EmployeeStatus.SUSPEND_EMP);
            }catch(EmployeeManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"suspendEmployee"+"\n"
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
                            +"Fct   ws- : "+" suspendEmployee"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    }

    @WebMethod(operationName = "activateEmployee")
    @Override
    public String activateEmployee(int excavatorID, int employeesID) {
        String res;
        try {

            res = dao.modifyEmployeeStatus(excavatorID, employeesID, EmployeeStatus.ACTIF_EMP);
            }catch(EmployeeManagementException error){
                        res=error.getSmallMessage();
                        logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                                        +"Fct   ws  : "+"activateEmployee"+"\n"
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
                            +"Fct   ws- : "+" activateEmployee"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    }

    @WebMethod(operationName = "editEmployeeCode")
    @Override
    public String editEmployeeCode(int excavatorID, int employeeID, String code, boolean generate) {
        String res;
        try {

            res = dao.editEmployeeCode(excavatorID, employeeID, code, generate);
                        }catch(EmployeeManagementException error){
                        res=error.getSmallMessage();
                        logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                                        +"Fct   ws  : "+"editEmployeeCode"+"\n"
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
                            +"Fct   ws- : "+" editEmployeeCode"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    }

}
