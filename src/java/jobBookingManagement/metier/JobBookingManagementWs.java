/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jobBookingManagement.metier;

import java.io.PrintWriter;
import java.io.StringWriter;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.ejb.Stateless;
import jobBookingManagement.dao.IJobBookingManagementDaoLocal;
import mailing.mailSending.MailFunction;
import mailing.mailSending.MailSendingNewJobBooking;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import toolsAndTransversalFunctionnalities.Result;

/**
 *
 * @author erman
 */
@WebService(serviceName = "JobBookingManagementWs")
@Stateless()
public class JobBookingManagementWs implements IJobBookingManagementWs{

    
    @EJB
    IJobBookingManagementDaoLocal dao;
    final static Logger logger = LogManager.getLogger("dump1");
    
    @Override
    @WebMethod(operationName = "addNewJobBooking")
    public String addNewJobBooking(long startDate, long endDate, int hourPerDay, String companyRate, String rate, int accountID, String jobLocation, String jobNumber, 
            int typeOfDirtyID, String weight, String startTime, String directDeposit, String timeSheet, String automatedBooking, String jobDescription,
            String jobInstruction, String documentsID, String trucksID, float billingPrice, int paymentTypeID, int timeZone, 
            String timeZoneID, float latitude, float longitude) {
        
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"addNewJobBooking: startDate = " + startDate + " -- endDate = " + endDate + " -- hourPerDay = " + hourPerDay + ""
                + " -- companyRate = " + companyRate + " -- rate = " + rate + " -- accountID = " + accountID + " -- jobLocation = " + jobLocation + ""
                + " -- jobNumber = " + jobNumber + " -- typeOfDirty = " + typeOfDirtyID + "\n -- weight = " + weight + " -- startTime = " + startTime + " -- directDeposit = "
                + directDeposit + " -- timeSheet = " + timeSheet + " -- automatedBooking = " + automatedBooking + " -- jobDescription = " + jobDescription 
                + " -- jobInstruction = " + jobInstruction + " -- documentsID = " + documentsID + " -- trucksID = " + trucksID + " -- billingPrice = " + billingPrice + " -- paymentTypeID = " + paymentTypeID);
        
        String res;
        Result result;
        rate = "null";
        MailFunction mf = new MailFunction();
        
        try {
            
            if(trucksID == null || trucksID.equals("")){
                return "NoTruckSelected";
            }

            if(documentsID == null || documentsID.equals("")){
                documentsID = ""+0;
            }
            
            result = dao.addNewJobBooking(startDate, endDate, hourPerDay, companyRate, rate, accountID, jobLocation, jobNumber, typeOfDirtyID,
                    weight, startTime, directDeposit, timeSheet, automatedBooking, jobDescription, jobInstruction, documentsID, trucksID, 
                    billingPrice, paymentTypeID, timeZone, timeZoneID, latitude, longitude);
            
            
            if(result == null){
                return "ErrorForaddNewJobResponse";
            }
            
            res = result.getMsg();
            
            if ( !res.contains("good")) {
                return result.getMsg();
            } 
            
            MailSendingNewJobBooking jobMsg = new MailSendingNewJobBooking(result);
            Thread mail = new Thread(jobMsg);
            mail.start();
            res = result.getMsg();

            
            
        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
              logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"addNewJobBooking"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"addNewJobBooking: return: res = " + res);
        return res;
        
    }

    
    @WebMethod(operationName = "getARangeOfJobToConfirmByTruckOwner")
    @Override
    public Result getARangeOfJobToConfirmByTruckOwner(int truckOwnerID, int index, int nombreMaxResult) {

        Result result = new Result();
        try {

            result = dao.getARangeOfJobToConfirmByTruckOwner(truckOwnerID, index, nombreMaxResult);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfJobToConfirmByTruckOwner"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }
    
    @WebMethod(operationName = "getARangeOfTruckRequestedByJobResponseID")
    @Override
    public Result getARangeOfTruckRequestedByJobResponseID(int accountID, int jobResponseID, int index, int nombreMaxResultat) {

        Result result = new Result();
        try {

            result = dao.getARangeOfTruckRequestedByJobResponseID(accountID, jobResponseID, index, nombreMaxResultat);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfTruckRequestedByJobResponseID"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }
    
    @WebMethod(operationName = "getARangeOfTruckRequestedByJobID")
    @Override
    public Result getARangeOfTruckRequestedByJobID(int accountID, int jobID, int index, int nombreMaxResultat){

        Result result = new Result();
        try {

            result = dao.getARangeOfTruckRequestedByJobID(accountID, jobID, index, nombreMaxResultat);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfTruckRequestedByJobID"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }
    
    @WebMethod(operationName = "getARangeOfJobResponseByJobID")
    @Override
    public Result getARangeOfJobResponseByJobID(int accountID, int jobID, int index, int nombreMaxResultat){

        Result result = new Result();
        try {

            result = dao.getARangeOfJobResponseByJobID(accountID, jobID, index, nombreMaxResultat);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
              logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfJobResponseByJobID"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }
    
    @WebMethod(operationName = "getARangeOfJobForTruckByExcavatorID")
    @Override
    public Result getARangeOfJobForTruckByExcavatorID(int excavatorID, int index, int nombreMaxResult){

        Result result = new Result();
        try {

            result = dao.getARangeOfJobForTruckByExcavatorID(excavatorID, index, nombreMaxResult);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfJobForTruckByExcavatorID"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }
    
    @WebMethod(operationName = "getARangeOfJobToConfirmByExcavatorID")
    @Override
    public Result getARangeOfJobToConfirmByExcavatorID(int excavatorID, int index, int nombreMaxResult){

        Result result = new Result();
        try {

            result = dao.getARangeOfJobToConfirmByExcavatorID(excavatorID, index, nombreMaxResult);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfJobToConfirmByExcavatorID"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }

    
}
