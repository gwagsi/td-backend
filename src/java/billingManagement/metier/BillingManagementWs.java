/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package billingManagement.metier;

import billingManagement.dao.IBillingManagementDaoLocal;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.ejb.Stateless;
import mailing.mailSending.MailFailurePayment;
import mailing.mailSending.MailFailurePaymentExcavator;
import mailing.mailSending.MailFailurePaymentTruckOwner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import toolsAndTransversalFunctionnalities.Result;
import toolsAndTransversalFunctionnalities.ResultBackend;
import util.exception.AvaibilityManagementException;
import util.exception.BillingManagementException;

/**
 *
 * @author erman
 */

@WebService(serviceName = "BillingManagementWs")
@Stateless()
public class BillingManagementWs implements IBillingManagementWs{

    @EJB
    IBillingManagementDaoLocal dao;
    
    final static Logger logger = LogManager.getLogger("dump1");
    @WebMethod(operationName = "getAllDateOfLogsByJobResponseID")
    @Override
    public Result getAllDateOfLogsByJobResponseID(int jobResponseID) {
        Result result = new Result();
        try{
            
            result = dao.getAllDateOfLogsByJobResponseID(jobResponseID);
            
        }catch(Throwable e){
                StringWriter string = new StringWriter();
                PrintWriter str = new PrintWriter(string);
                e.printStackTrace(str);
                result.setMsg(string.toString());
                 logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getAllDateOfLogsByJobResponseID"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
                return result;
        }
        
        return result;
    }

    @WebMethod(operationName = "getAllPaymentType")
    @Override
    public Result getAllPaymentType() {
        Result result = new Result();
        try{
            
            result = dao.getAllPaymentType();
            
        }catch(Throwable e){
                StringWriter string = new StringWriter();
                PrintWriter str = new PrintWriter(string);
                e.printStackTrace(str);
                result.setMsg(string.toString());
                logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getAllPaymentType"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
                return result;
        }
        
        return result;
    }

    @WebMethod(operationName = "generateDailyTicket")
    @Override
    public Result generateDailyTicket(int jobResponseID, long date) {
        
        Result result = new Result();
        try{
            
            result = dao.generateDailyTicket(jobResponseID, date);
            
        }catch(Throwable e){
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"generateDailyTicket"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }
        
        return result;
    }

    @WebMethod(operationName = "editPaymentStatus")
    @Override
    public String editPaymentStatus(int jobResponseID) {
        
        String res;
        try {

            res = dao.editPaymentStatus(jobResponseID);
             }
        catch(BillingManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"editPaymentStatus"+"\n"
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
                            +"Fct   ws- : "+"editPaymentStatus"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    }

    @WebMethod(operationName = "getFinalBill")
    @Override
    public Result getFinalBill(int jobResponseID) {
        Result result = new Result();
        try{

            result = dao.getFinalBill(jobResponseID);

        }catch(Throwable e){
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getFinalBill"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }


    @WebMethod(operationName = "validateDailyTicketFromExcavator")
    @Override
    public String validateDailyTicketFromExcavator(int jobResponseID, long ticketDate){
        
        String res;
        try {

            res = dao.validateDailyTicketFromExcavator(jobResponseID, ticketDate);
            }
        catch(BillingManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"validateDailyTicketFromExcavator"+"\n"
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
                            +"Fct   ws- : "+"validateDailyTicketFromExcavator"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    } 
    
    
    @WebMethod(operationName = "validateDailyTicketFromTruckOwner")
    @Override
    public String validateDailyTicketFromTruckOwner(int jobResponseID, long ticketDate, String excavatorCode){
        
        String res;
        try {

            res = dao.validateDailyTicketFromTruckOwner(jobResponseID, ticketDate, excavatorCode.trim());
            }
        catch(BillingManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"validateDailyTicketFromTruckOwner"+"\n"
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
                            +"Fct   ws- : "+"validateDailyTicketFromTruckOwner"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    } 
    
    
    @WebMethod(operationName = "getWeeklyDateByJobResponseID")
    @Override
    public Result getWeeklyDateByJobResponseID(int jobResponseID) {
        Result result = new Result();
        try{
            
            result = dao.getWeeklyDateByJobResponseID(jobResponseID);
            
        }catch(Throwable e){
                StringWriter string = new StringWriter();
                PrintWriter str = new PrintWriter(string);
                e.printStackTrace(str);
                result.setMsg(string.toString());
                  logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getWeeklyDateByJobResponseID"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
                return result;
        }
        
        return result;
    }
    
    @WebMethod(operationName = "generateWeeklyTicket")
    @Override
    public Result generateWeeklyTicket(int accountID, int jobResponseID, long startDate, long endDate) {
        Result result;
        try {

            result = dao.generateWeeklyTicket(accountID, jobResponseID, startDate, endDate);
            
        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result = new Result();
            result.setMsg(string.toString());
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"generateWeeklyTicket"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }

    @WebMethod(operationName = "validateWeeklyTicketFromTruckOwner")
    @Override
    public String validateWeeklyTicketFromTruckOwner(int truckOwnerID, int jobResponseID, long weeklyStartDate, long weeklyEndDate, String excavatorCode){
        
        String res;
        try {

            res = dao.validateWeeklyTicketFromTruckOwner(truckOwnerID, jobResponseID, weeklyStartDate, weeklyEndDate, excavatorCode);
             }
        catch(BillingManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"validateWeeklyTicketFromTruckOwner"+"\n"
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
                            +"Fct   ws- : "+"validateWeeklyTicketFromTruckOwner"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    }
    
    @WebMethod(operationName = "validateWeeklyTicketFromExcavator")
    @Override
    public String validateWeeklyTicketFromExcavator(int excavatorID, int jobResponseID, long weeklyStartDate, long weeklyEndDate){

        String res;
        try {

            res = dao.validateWeeklyTicketFromExcavator(excavatorID, jobResponseID, weeklyStartDate, weeklyEndDate);
             }
        catch(BillingManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"validateWeeklyTicketFromExcavator"+"\n"
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
                            +"Fct   ws- : "+"validateWeeklyTicketFromExcavator"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    }
    
    
    
    @WebMethod(operationName = "payWeeklyTicketByExcavator")
    @Override
    public String payWeeklyTicketByExcavator(int excavatorID, int jobResponseID, long weeklyStartDate, long weeklyEndDate) {

        String res;
        ResultBackend result;

        try {

            result = dao.payWeeklyTicketByExcavator(excavatorID, jobResponseID, weeklyStartDate, weeklyEndDate);
            res = result.getMsg();
            System.out.println("payWeeklyTicketByExcavator (res): " + res);

            if (res != null && (res.contains("IncorrectBillingInformation_sender") || res.contains("NotExistRoutingNumber_sender")) ) {

                MailFailurePayment mfp;
                String subject = "Payment Failure on TrucksAndDirt.com";
                String systemMsg = "This is to inform you that Mr/Mrs ###TOTALNAME, having the account number"
                        + " ###ACCOUNT_NUMBER has an unrecognized Routing Number. No payment will be possible using it";

                String[] personnalData = (String[]) result.getResultArraysList().get(0);
                String[] msgContentData = null;
                mfp = new MailFailurePaymentExcavator(personnalData, msgContentData, subject, systemMsg);
                Thread mail = new Thread(mfp);
                mail.start();

            } else if (res != null && (res.contains("IncorrectBillingInformation_receiver") || res.contains("NotExistRoutingNumber_receiver")) ) {

                MailFailurePayment mfp;
                String subject = "Payment Failure on TrucksAndDirt.com";
                String systemMsg = "This is to inform you that Mr/Mrs ###TOTALNAME, having the account number"
                        + " ###ACCOUNT_NUMBER has an unrecognized Routing Number. No payment will be possible using it";

                String[] personnalData = (String[]) result.getResultArraysList().get(0);
                String[] msgContentData = (String[]) result.getResultArraysList().get(1);
                mfp = new MailFailurePaymentTruckOwner(personnalData, msgContentData, subject, systemMsg);
                Thread mail = new Thread(mfp);
                mail.start();
            }

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"payWeeklyTicketByExcavator"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    }
    
    @WebMethod(operationName = "payWeeklyTicketByTruckOwner")
    @Override
    public String payWeeklyTicketByTruckOwner(int truckOwnerID, int jobResponseID, long weeklyStartDate, long weeklyEndDate) {

        String res;
        try {

            res = dao.payWeeklyTicketByTruckOwner(truckOwnerID, jobResponseID, weeklyStartDate, weeklyEndDate);
            }
        catch(BillingManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"payWeeklyTicketByTruckOwner"+"\n"
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
                            +"Fct   ws- : "+"payWeeklyTicketByTruckOwner"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    }

    
    @WebMethod(operationName = "getARangeOfTruckOwnerWeeklyTicketHistory")
    @Override
        public Result getARangeOfTruckOwnerWeeklyTicketHistory(int truckOwnerID, int index, int nombreMaxResult){
        Result result = new Result();
        try{
            
            result = dao.getARangeOfTruckOwnerWeeklyTicketHistory(truckOwnerID, index, nombreMaxResult);
            
        }catch(Throwable e){
                StringWriter string = new StringWriter();
                PrintWriter str = new PrintWriter(string);
                e.printStackTrace(str);
                result.setMsg(string.toString());
                 logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfTruckOwnerWeeklyTicketHistory"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
                return result;
        }
        
        return result;
    }

    @WebMethod(operationName = "getARangeOfExcavatorWeeklyTicketHistory")
    @Override
    public Result getARangeOfExcavatorWeeklyTicketHistory(int excavatorID, int index, int nombreMaxResult) {
        Result result = new Result();
        try {

            result = dao.getARangeOfExcavatorWeeklyTicketHistory(excavatorID, index, nombreMaxResult);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfExcavatorWeeklyTicketHistory"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }

    
    @WebMethod(operationName = "submitDailyTicketByTruckOwner")
    @Override
    public String submitDailyTicketByTruckOwner(int truckOwnerID, int jobResponseID, long ticketDate) {

        String res;
        try {

            res = dao.submitDailyTicketByTruckOwner(truckOwnerID, jobResponseID, ticketDate);
             }
        catch(BillingManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"submitDailyTicketByTruckOwner"+"\n"
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
                            +"Fct   ws- : "+"submitDailyTicketByTruckOwner"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    }
    
    @WebMethod(operationName = "submitWeeklyTicketByTruckOwner")
    @Override
    public String submitWeeklyTicketByTruckOwner(int truckOwnerID, int jobResponseID, long weeklyStartDate, long weeklyEndDate) {

        String res;
        try {

            res = dao.submitWeeklyTicketByTruckOwner(truckOwnerID, jobResponseID, weeklyStartDate, weeklyEndDate);
            }
        catch(BillingManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"submitWeeklyTicketByTruckOwner"+"\n"
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
                            +"Fct   ws- : "+"submitWeeklyTicketByTruckOwner"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    }

    @WebMethod(operationName = "loadMonthlyBill")
    @Override
    public Result loadMonthlyBill(int accountID, long monthlyStartDate, long monthlyEndDate) {
        Result result = new Result();
        try {

            result = dao.loadMonthlyBill(accountID, monthlyStartDate, monthlyEndDate);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"loadMonthlyBill"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }

    @WebMethod(operationName = "getMonthyDateForPersonnalBilling")
    @Override
    public Result getMonthyDateForPersonnalBilling(int accountID) {
        Result result = new Result();
        try {

            result = dao.getMonthyDateForPersonnalBilling(accountID);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getMonthyDateForPersonnalBilling"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }

    
    @WebMethod(operationName = "generateDailyTicketPerTruck")
    @Override
    public Result generateDailyTicketPerTruck(int accountID, int jobResponseID, int truckID, long date) {
        
        Result result = new Result();
        try{
            
            result = dao.generateDailyTicketPerTruck(accountID, jobResponseID, truckID, date);
            
        }catch(Throwable e){
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"generateDailyTicketPerTruck"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }
        
        return result;
    }
    
    @WebMethod(operationName = "validateDailyTicketFromExcavatorPerTruck")
    @Override
    public String validateDailyTicketFromExcavatorPerTruck(int accountID, int dailyTicketID) {

        String res;
        try {

            res = dao.validateDailyTicketFromExcavatorPerTruck(accountID, dailyTicketID);
            }
        catch(BillingManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"validateDailyTicketFromExcavatorPerTruck"+"\n"
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
                            +"Fct   ws- : "+"validateDailyTicketFromExcavatorPerTruck"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    }
    
    @WebMethod(operationName = "validateDailyTicketFromTruckOwnerPerTruck")
    @Override
    public String validateDailyTicketFromTruckOwnerPerTruck(int accountID, int dailyTicketID, String excavatorCode) {

        String res;
        try {

            res = dao.validateDailyTicketFromTruckOwnerPerTruck(accountID, dailyTicketID, excavatorCode.trim());
             }
        catch(BillingManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"validateDailyTicketFromTruckOwnerPerTruck"+"\n"
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
                            +"Fct   ws- : "+"validateDailyTicketFromTruckOwnerPerTruck"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    }
    
    @WebMethod(operationName = "submitDailyTicketByTruckOwnerPerTruck")
    @Override
    public String submitDailyTicketByTruckOwnerPerTruck(int accountID, int dailyTicketID) {

        String res;
        try {

            res = dao.submitDailyTicketByTruckOwnerPerTruck(accountID, dailyTicketID);
            }
        catch(BillingManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"submitDailyTicketByTruckOwnerPerTruck"+"\n"
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
                            +"Fct   ws- : "+" submitDailyTicketByTruckOwnerPerTruck"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    }
    
    
}
