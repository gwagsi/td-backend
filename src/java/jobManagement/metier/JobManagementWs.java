/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jobManagement.metier;

import entities.JobResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.ejb.Stateless;
import jobLogManagement.dao.IJobLogManagementDaoLocal;
import jobManagement.dao.IJobManagementDaoLocal;
import mailing.mailSending.MailSendingEditJob;
import mailing.mailSending.MailSendingJobCancelExcavator;
import mailing.mailSending.MailSendingJobCancelTruckOwner;
import mailing.mailSending.MailSendingJobConfirmation;
import mailing.mailSending.MailSendingJobRejection;
import mailing.mailSending.MailSendingNewJobResponse;
import mailing.mailSending.MailSendingTruckOwnerJobConfirmation;
import org.apache.logging.log4j.LogManager;
import toolsAndTransversalFunctionnalities.Result;
import toolsAndTransversalFunctionnalities.ResultBackend;
import util.exception.JobLogManagementException;
import util.exception.JobManagementException;

/**
 *
 * @author erman
 */
@WebService(serviceName = "JobManagementWs")
@Stateless()
public class JobManagementWs implements IJobManagementWs {

    @EJB
    IJobManagementDaoLocal jobDAO;
    
    @EJB
    IJobLogManagementDaoLocal jobLogDAO;
    
    @EJB
    JobSendNotificationAsyn jSNAsyn;
    
final static org.apache.logging.log4j.Logger logger = LogManager.getLogger("dump1");
    
    @WebMethod(operationName = "addNewJob")
    @Override
    public String addNewJob(long startDate, long endDate, int hourPerDay, int numberOfTruck,
            String companyRate, String rate, int excavatorID, int lenghtofbedID, int truckaxleID, 
            String jobLocation, String jobNumber, int typeOfDirtyID, String weight, String startTime,
            String dotNumber, int generalLiability, int truckLiability, String proofOfTruckLiability,
            String directDeposit, String timeSheet, String automatedBooking, String jobDescription, String jobInstruction,
            String documentsID, float billingPrice, int paymentTypeID, int timeZone, String timeZoneID,
            float latitude, float longitude){
        
        String res;
        rate = "null";
        ResultBackend result;
        try {
            
            if(documentsID == null || documentsID.equals("")){
                documentsID = ""+0;
            }
            
            if(numberOfTruck <= 0){
                return "badNumberOfTruck";
            }

            result = jobDAO.addNewJob(startDate, endDate, hourPerDay, numberOfTruck, companyRate, rate, excavatorID,
                    lenghtofbedID, truckaxleID, jobLocation, jobNumber, typeOfDirtyID, weight, startTime, dotNumber,
                    generalLiability, truckLiability, proofOfTruckLiability, directDeposit, timeSheet, automatedBooking,
                    jobDescription, jobInstruction, documentsID, billingPrice, paymentTypeID, timeZone, timeZoneID,
                    latitude, longitude);
            res = (result != null ? result.getMsg() : "");
            if (res.contains("good")) {
                
                String[] excavatorInfo = (String[]) result.getResultArraysList().get(0);
                String[] jobResponseInfos = (String[]) result.getResultArraysList().get(1);
                Float[] GPSCoordinates = (Float[]) result.getResultArraysList().get(2);
                Float jobLatitude = GPSCoordinates[0];
                Float jobLongitude = GPSCoordinates[1];
                
                /*
                List<Object[]> userListInfo = jobDAO.searchTruckOwnerToNotify(latitude, longitude);
                if (userListInfo != null && !userListInfo.isEmpty()) {
                MailSendingNewJobNotification sendMsgAddNewJob = new MailSendingNewJobNotification(excavatorInfo, jobResponseInfos, userListInfo);
                Thread mail = new Thread(sendMsgAddNewJob);
                mail.start();
                }
                */
                int jobID = Integer.parseInt(String.valueOf(res.split(";")[1]));
                jSNAsyn.sendJobCreationNotificationMail(excavatorInfo, jobResponseInfos, jobID, latitude, longitude);
            }
            
        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"addNewJob"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        System.out.println("[addNewJob] res: " + res);
        return res;
    }

    @WebMethod(operationName = "addNewJobRespons")
    @Override
    public String addNewJobRespons(long startDate, long endDate, int hourPerDay, int numberOfTruck, String companyRate, String rate, int truckOwnerID, int jobID, float billingPrice, int paymentTypeID, String trucksID, int timeZone) {

        String res = "good";
        ResultBackend result;

        try {

            result = jobDAO.addNewJobResponse(startDate, endDate, hourPerDay, companyRate, rate, truckOwnerID, jobID, billingPrice, paymentTypeID, trucksID, timeZone);
            
            if(result == null){
                return "ErrorForaddNewJobResponse";
            }
            
            if ( !result.getMsg().contains("good")) {
                return result.getMsg();
            }
            
            //result.afficherResult("AddNewJobResponse");
            if (result.getResultArraysList() != null && !result.getResultArraysList().isEmpty()) {
                
                MailSendingNewJobResponse sendMsgAddNewBid = new MailSendingNewJobResponse(result);                
                Thread mail = new Thread(sendMsgAddNewBid);
                mail.start();
                
            }
            res = result.getMsg();
        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
            System.out.println(" res == "+res);
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"addNewJobRespons"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    }

    @WebMethod(operationName = "editJob")
    @Override
    public String editJob(int jobID, long startDate, long endDate, int hourPerDay, int numberOfTruck,
            String companyRate, String rate, int excavatorID, int lenghtofbedID, int truckaxleID, 
            String jobLocation, String jobNumber, int typeOfdirtyID, String weight, String startTime,
            String dotNumber, int generalLiability, int truckLiability, String proofOfTruckLiability,
            String directDeposit, String timeSheet, String automatedBooking, String jobDescription, String jobInstruction, 
            float billingPrice, int paymentTypeID, String timeZoneID, float latitude, float longitude, String modifiedField) {

        String res;
        ResultBackend result;
        rate = "null";
        
        try {

            
            if(numberOfTruck <= 0){
                return "badNumberOfTruck";
            }
            
            System.out.println("editJob ~ modifiedField(next): " + modifiedField);
            
            List<String> modifiedFieldList = (modifiedField == null ? null : Arrays.asList(modifiedField.trim().split("##")));
            
            System.out.println("editJob ~ modifiedFieldList: " + modifiedFieldList);
            result = jobDAO.editJob(jobID, startDate, endDate, hourPerDay, numberOfTruck, companyRate, rate, excavatorID, 
                    lenghtofbedID, truckaxleID, jobLocation, jobNumber, typeOfdirtyID, weight, startTime, dotNumber, 
                    generalLiability, truckLiability, proofOfTruckLiability, directDeposit, timeSheet, automatedBooking, 
                    jobDescription, jobInstruction, billingPrice, paymentTypeID, timeZoneID, latitude, longitude);

            if (modifiedFieldList != null && !modifiedFieldList.isEmpty() && result!= null && result.getMsg().contains("good")) {
                String[] excavatorInfo = (String[]) result.getResultArraysList().get(0);
                String[][] jobResponseInfos = (String[][]) result.getResultArraysList().get(1);
                Float[] GPSCoordinates = (Float[]) result.getResultArraysList().get(2);
                Float jobLatitude = GPSCoordinates[0];
                Float jobLongitude = GPSCoordinates[1];
                System.out.println("editJob - jobResponseInfos: " + jobResponseInfos);
                MailSendingEditJob sendMsgEditJob = new MailSendingEditJob(jobDAO, jobID, jobResponseInfos, excavatorInfo, 
                        jobLatitude, jobLongitude, modifiedFieldList);
                Thread mail = new Thread(sendMsgEditJob);
                mail.start();
            }
            
            res = result.getMsg();
            System.out.println("editJob ~ res: " + res);
        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"editJob"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    }

    @WebMethod(operationName = "editResponseJob")
    @Override
    public String editResponseJob(int jobResponseID, long startDate, long endDate, int hourPerDay, int numberOfTruck, String companyRate, String rate, int truckOwnerID, float billingPrice, int paymentTypeID) {

        String res;
        try {

            res = jobDAO.editResponseJob(jobResponseID, startDate, endDate, hourPerDay, numberOfTruck, companyRate, rate, truckOwnerID, billingPrice, paymentTypeID);
             }catch(JobManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"editResponseJob"+"\n"
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
                            +"Fct   ws- : "+"editResponseJob"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    }

    @WebMethod(operationName = "editJobResponsEndDate")
    @Override
    public String editJobResponsEndDate(int jobResponseID, long endDate, int truckOwnerID) {

        String res;
        try {
            
            res = jobDAO.editJobResponsEndDate(jobResponseID, endDate, truckOwnerID);
            System.out.println("editJobResponsEndDate(Ws) ~ res: " + res);
            }catch(JobManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"editJobResponsEndDate"+"\n"
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
                            +"Fct   ws- : "+"editJobResponsEndDate"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    }

    @WebMethod(operationName = "getARangeOfNotStartedJobByExcavator")
    @Override
    public Result getARangeOfNotStartedJobByExcavator(int excavatorID, int index, int nombreMaxResult) {

        Result result = new Result();
        try {

            result = jobDAO.getARangeOfByExcavator("getARangeOfNotStartedJobByExcavator", excavatorID, index, nombreMaxResult);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfNotStartedJobByExcavator"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }

    @WebMethod(operationName = "getARangeOfJobResponseByTruckOwner")
    @Override
    public Result getARangeOfJobResponseByTruckOwner(int truckOwnerID, int index, int nombreMaxResult) {

        Result result = new Result();
        try {

            result = jobDAO.getARangeOfJobResponseByTruckOwner(truckOwnerID, index, nombreMaxResult);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfJobResponseByTruckOwner"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }

    @WebMethod(operationName = "getARangeOfJobRespons")
    @Override
    public Result getARangeOfJobRespons(int index, int nombreMaxResult) {

        Result result = new Result();
        try {

            result = jobDAO.getARangeOfJobResponse(index, nombreMaxResult);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfJobRespons"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }

    @WebMethod(operationName = "getJobByID")
    @Override
    public Result getJobByID(int jobID) {

        Result result = new Result();
        try {

            result = jobDAO.getJobByID(jobID);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getJobByID"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }

    @WebMethod(operationName = "getJobResponseByID")
    @Override
    public Result getJobResponseByID(int jobResponseID) {

        Result result = new Result();
        try {

            result = jobDAO.getJobResponseByID(jobResponseID);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getJobResponseByID"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }

    @WebMethod(operationName = "deleteJob")
    @Override
    public String deleteJob(int jobID, String deletedReason) {

        String res;
        Result result;
        
        try {

            result = jobDAO.deleteJob(jobID, deletedReason);
            
            if(result == null ){
                return "ErrorForDeleteJob";
            }
            
            if( !result.getMsg().contains("good")){
                return result.getMsg();
            }
            
            result.afficherResult("deleteJob");
            
            MailSendingJobCancelExcavator jobCancelMsg = new MailSendingJobCancelExcavator(result);
            Thread mail = new Thread(jobCancelMsg);
            mail.start();
            res = result.getMsg();

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"deleteJob"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    }

    @WebMethod(operationName = "deleteJobRespons")
    @Override
    public String deleteJobRespons(int jobResponseID) {
        String res = null;
        Result result;
        try {

            result = jobDAO.deleteJobResponse(jobResponseID);
            
            if(result == null ){
                return "ErrorForDeleteJobResponse";
            }
            
            if( !result.getMsg().equalsIgnoreCase("good")){
                return result.getMsg();
            }
            
            result.afficherResult("deleteJobRespons");
            
            MailSendingJobRejection jobMsg = new MailSendingJobRejection(result);
            Thread mail = new Thread(jobMsg);
            mail.start();
            
            res = result.getMsg();

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"deleteJobRespons"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    }

    @WebMethod(operationName = "deleteJobResponseByTruckOwner")
    @Override
    public String deleteJobResponseByTruckOwner(int jobResponseID, String deletedReason) {
        String res = null;
        Result result;
        try {

            result = jobDAO.deleteJobResponseByTruckOwner(jobResponseID, deletedReason);
            
            if(result == null ){
                return "ErrorForDeleteJob";
            }
            
            if( !result.getMsg().contains("good") || result.getObject() == null){
                return result.getMsg();
            }
            
            MailSendingJobCancelTruckOwner jobMsg = new MailSendingJobCancelTruckOwner(result);
            Thread mail = new Thread(jobMsg);
            mail.start();
            
            res = result.getMsg();

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"deleteJobResponseByTruckOwner"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    }

    @WebMethod(operationName = "getARangeOfJobResponseByJobID")
    @Override
    public Result getARangeOfJobResponseByJobID(int jobID, int index, int nombreMaxResult) {

        Result result = new Result();
        try {

            result = jobDAO.getARangeOfJobResponseByJobID(jobID, index, nombreMaxResult);

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

    @WebMethod(operationName = "getARangeOfJobByTruckOwner")
    @Override
    public Result getARangeOfJobByTruckOwner(int truckOwnerID, int index, int nombreMaxResult) {

        Result result = new Result();
        try {

            result = jobDAO.getARangeOfJobByTruckOwner(truckOwnerID, index, nombreMaxResult);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfJobByTruckOwner"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }

    @WebMethod(operationName = "validateAJob")
    @Override
    public String validateAJob(int excavatorID, int jobResponseID) {

        String res;
        Result result;
        
        try {
            
            result = jobDAO.validateAJobByExcavator(excavatorID, jobResponseID, Arrays.asList("-1"));
            
            if(result == null){
                return "ErrorDuringValidation";
            } 
            
            if (!result.getMsg().contains("good") || result.getObject() == null) {
                return result.getMsg();
            }
            
            MailSendingJobConfirmation jobMsg = new MailSendingJobConfirmation(result);
            Thread mail = new Thread(jobMsg);
            mail.start();
            
            res = result.getMsg();

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
            System.out.println("res    =   " + res);
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"validateAJob"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }
        
        return res;
    }

    @WebMethod(operationName = "validateAJobByExcavator")
    @Override
    public String validateAJobByExcavator(int excavatorID, int jobResponseID, String truckIDList) {

        String res;
        Result result;
        
        List<String> asList = Arrays.asList(truckIDList.trim().split("##"));
        if (asList.isEmpty()) {
            return "NoTuckSeleted";
        }
        try {
            
            result = jobDAO.validateAJobByExcavator(excavatorID, jobResponseID, asList);
            
            if(result == null){
                return "ErrorDuringValidation";
            } 
            
            if (!result.getMsg().contains("good") || result.getObject() == null) {
                return result.getMsg();
            }
            
            MailSendingJobConfirmation jobMsg = new MailSendingJobConfirmation(result);
            Thread mail = new Thread(jobMsg);
            mail.start();
            
            res = result.getMsg();

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
            System.out.println("res    =   " + res);
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"validateAJobByExcavator"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }
        
        return res;
    }

    @WebMethod(operationName = "validateAJobByTruckOwner")
    @Override
    public String validateAJobByTruckOwner(int truckOwnerID, int jobResponseID, float price, int paymentTypeID) {

        String res;
        Result result;
        
        try {
            
            result = jobDAO.validateAJobByTruckOwner(truckOwnerID, jobResponseID, price, paymentTypeID);
            
            if(result == null){
                return "ErrorDuringValidation";
            } 
            
            if (!result.getMsg().contains("good")) {
                return result.getMsg();
            }
            
            MailSendingTruckOwnerJobConfirmation jobMsg = new MailSendingTruckOwnerJobConfirmation(result);
            Thread mail = new Thread(jobMsg);
            mail.start();
            
            res = result.getMsg();

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"validateAJobByTruckOwner"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    }

    @WebMethod(operationName = "closeJob")
    @Override
    public String closeJob(int jobID) {

        String res;
        try {

            res = jobDAO.closeJob(jobID);
            }catch(JobManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"closeJob"+"\n"
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
                            +"Fct   ws- : "+"closeJob"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    }

    @WebMethod(operationName = "getARangeOfValidJobResponseTruckOwner")
    @Override
    public Result getARangeOfValidJobResponseTruckOwner(int truckOwnerID, int index, int nombreMaxResult) {

        Result result = new Result();
        boolean considerDeletedJob = false;
        try {

            result = jobDAO.getARangeOfValidJobResponseTruckOwner(truckOwnerID, considerDeletedJob, index, nombreMaxResult);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfValidJobResponseTruckOwner"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }

    @WebMethod(operationName = "getARangeOfValidJobResponseByTruckOwner")
    @Override
    public Result getARangeOfValidJobResponseByTruckOwner(int truckOwnerID, int index, int nombreMaxResult) {

        Result result = new Result();
        boolean considerDeletedJob = true;
        try {

            result = jobDAO.getARangeOfValidJobResponseTruckOwner(truckOwnerID, considerDeletedJob, index, nombreMaxResult);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfValidJobResponseByTruckOwner"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }

    @WebMethod(operationName = "getARangeOfPassJobExcavator")
    @Override
    public Result getARangeOfPassJobExcavator(int excavatorID, int index, int nombreMaxResult) {

        Result result = new Result();
        try {

            result = jobDAO.getARangeOfByExcavator("getARangeOfPassJobExcavator", excavatorID, index, nombreMaxResult);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfPassJobExcavator"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }

    @WebMethod(operationName = "getARangeOfJobForBillByExcavator")
    @Override
    public Result getARangeOfJobForBillByExcavator(int excavatorID, int index, int nombreMaxResult) {

        Result result = new Result();
        try {

            result = jobDAO.getARangeOfByExcavator("getARangeOfJobForBillByExcavator", excavatorID, index, nombreMaxResult);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfJobForBillByExcavator"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }

    @WebMethod(operationName = "getARangeOfActifJobByExcavator")
    @Override
    public Result getARangeOfActifJobByExcavator(int excavatorID, int index, int nombreMaxResult) {

        Result result = new Result();
        try {

            result = jobDAO.getARangeOfActifJobByExcavator(excavatorID, index, nombreMaxResult);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfActifJobByExcavator"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }

    @WebMethod(operationName = "getJobNumberByExcavator")
    @Override
    public Result getJobNumberByExcavator(int excavatorID) {

        Result result = new Result();
        try {

            result = jobDAO.getJobNumberByExcavator(excavatorID);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getJobNumberByExcavator"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }

    @WebMethod(operationName = "getARangeOfCloseJobExcavator")
    @Override
    public Result getARangeOfCloseJobExcavator(int excavatorID, int index, int nombreMaxResult) {

        Result result = new Result();
        try {

            result = jobDAO.getARangeOfCloseJobExcavator(excavatorID, index, nombreMaxResult);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfCloseJobExcavator"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }

    @WebMethod(operationName = "addNewJobLog")
    @Override
    public String addNewJobLog(String dateLog, String timeOnSite, String timeLeft, int numberOfLoad, String noteOnDay, int jobResponseID, 
            String startTime, String endTime, String typeOfDirt, String fromWhere, String toWhere, int truckID, int accountID) {

        String res;
        try {

            res = jobLogDAO.addNewJobLog(dateLog, timeOnSite, timeLeft, numberOfLoad, noteOnDay, jobResponseID, startTime, endTime, typeOfDirt, fromWhere, toWhere, truckID, accountID);
            }catch(JobLogManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"addNewJobLog"+"\n"
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
                            +"Fct   ws- : "+"addNewJobLog"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    }

    @WebMethod(operationName = "editJobLog")
    @Override
    public String editJobLog(int jobLogID, String dateLog, String timeOnSite, String timeLeft, int numberOfLoad, String noteOnDay,
            int jobResponseID, String startTime, String endTime, String typeOfDirt, String fromWhere, String toWhere, int truckID, int accountID) {

        String res;
        try {

            res = jobLogDAO.editJobLog(jobLogID, dateLog, timeOnSite, timeLeft, numberOfLoad, noteOnDay, jobResponseID, startTime, endTime, typeOfDirt, fromWhere, toWhere, truckID, accountID);
             }catch(JobLogManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"editJobLog"+"\n"
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
                            +"Fct   ws- : "+"editJobLog"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    }

    @WebMethod(operationName = "deleteJobLog")
    @Override
    public String deleteJobLog(int jobLogID, int accountID) {

        String res;
        try {

            res = jobLogDAO.deleteJobLog(jobLogID, accountID);
              }catch(JobLogManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"deleteJobLog"+"\n"
                            +"Class dao : "+error.getClasse()+"\n"
                            +"Fct   dao : "+error.getFonction()+"\n"
                            +"Info error "+error.getSmallMessage()+"\n"
                            +"Erreur   : "+"\n"
                            +error.getFullMessage());  
         
        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"deleteJobLog"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            res = string.toString();
            return res;
        }

        return res;
    }

    @WebMethod(operationName = "submitJob")
    @Override
    public String submitJob(int jobResponseID) {

        String res;
        Result result;
        try {

            result = jobDAO.submitJobLogExt(jobResponseID);
            res = result.getMsg();
            
        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"submitJob"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    }

    @WebMethod(operationName = "getARangeOfJobLogByJobResponseID")
    @Override
    public Result getARangeOfJobLogByJobResponseID(int jobResponseID, int index, int nombreMaxResult) {

        Result result = new Result();
        try {

            result = jobLogDAO.getARangeOfJobLogByJobResponseID(jobResponseID, index, nombreMaxResult);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfJobLogByJobResponseID"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }

    @WebMethod(operationName = "getARangeOfUnSumittedJob")
    @Override
    public Result getARangeOfUnSumittedJob(int truckOwnerID, int index, int nombreMaxResult) {

        Result result = new Result();
        try {

            result = jobDAO.getARangeOfUnSumittedJob(truckOwnerID, index, nombreMaxResult);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfUnSumittedJob"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }

    @WebMethod(operationName = "getARangeOfJobHistory")
    @Override
    public Result getARangeOfJobHistory(int truckOwnerID, int index, int nombreMaxResult) {

        Result result = new Result();
        try {

            result = jobDAO.getARangeOfJobHistory(truckOwnerID, index, nombreMaxResult);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfJobHistory"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }

    @WebMethod(operationName = "getJobLogByID")
    @Override
    public Result getJobLogByID(int JobLogID) {

        Result result = new Result();
        try {

            result = jobLogDAO.getJobLogByID(JobLogID);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getJobLogByID"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }

    @WebMethod(operationName = "getNumberOfJobLogPerDay")
    @Override
    public Result getNumberOfJobLogPerDay(int jobResponseID, String dateLog) {
        Result result = new Result();
        try {

            result = jobLogDAO.getNumberOfJobLogPerDay(jobResponseID, dateLog);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getNumberOfJobLogPerDay"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }

    @WebMethod(operationName = "editSubmitReview")
    @Override
    public String editSubmitReview(int jobResponseID, int submitReview) {
        String res;
        try {

            res = jobDAO.editSubmitReview(jobResponseID, submitReview);
               }catch(JobManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"editSubmitReview"+"\n"
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
                            +"Fct   ws- : "+"editSubmitReview"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    }
    
    @WebMethod(operationName = "getAllDateLog")
    @Override
    public Result getAllDateLog(){
        Result result = new Result();
        try {

            result = jobLogDAO.getAllDateLog();

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getAllDateLog"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }
    
    @WebMethod(operationName = "getARangeofJob")
    @Override
    public Result getARangeofJob(int index, int nombreMaxResult){
        Result result = new Result();
        try {

            result = jobDAO.getARangeOfJob(index, nombreMaxResult);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeofJob"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }
    
   
    @WebMethod(operationName = "getARangeOfJob")
    @Override
    public Result getARangeOfJob(int index, int nombreMaxResult) {

        Result result = new Result();
        List<JobResponse> job;
        try {

            result = jobDAO.getARangeOfJob(index, nombreMaxResult);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfJob"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }
    
    @WebMethod(operationName = "automatedBooking")
    @Override
    public Result automatedBooking(int jobID) {

        Result result = new Result();
        List<JobResponse> jobResponses;
        String res = "good";
        
        try {

            jobResponses = jobDAO.getJobResponseForAutomatedBook(jobID);
            
            System.out.println("Affichage de la liste de Reçu dans les webService ...");
            for (JobResponse jobResponse : jobResponses) {

                System.out.println("  jobResponse = " + jobResponse + " Statut[valid = true/ rejet = false] " + jobResponse.getDeleted());

            }
            
            for (JobResponse jobResponse : jobResponses) {
                if(!jobResponse.getDeleted()){
                    
                    System.out.println("Debut de validation.. jobResponse = "+jobResponse);
                    //res = this.validateAJob(jobResponse.getJobID().getExcavatorID().getAccountID().getAccountID(), jobResponse.getJobresponseID());
                    System.out.println("Fin de validation du Job ... \n Message reçu = "+res);
                    
                } else {
                    
                    System.out.println("Debut de rejet.. jobResponse = "+jobResponse);
                    res = this.deleteJobRespons(jobResponse.getJobresponseID());
                    System.out.println("Fin de rejet du Job ... \n Message reçu = "+res);
                    
                }
            }
            result.setMsg("good");
            
        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"automatedBooking"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }
    
    
    @WebMethod(operationName = "editReviewComment")
    @Override
    public String editReviewComment(int jobResponseID, String reveiwComment) {
        String res;
        try {

            res = jobDAO.editReviewComment(jobResponseID, reveiwComment);
                }catch(JobManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"editReviewComment"+"\n"
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
                            +"Fct   ws- : "+"editReviewComment"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    }
    
    
    @WebMethod(operationName = "getARangeOfExcavatorReviewComment")
    @Override
    public Result getARangeOfExcavatorReviewComment(int jobID, int index, int nombreMaxResult) {

        Result result = new Result();
        
        try {

            result = jobDAO.getARangeOfExcavatorReviewComment(jobID, index, nombreMaxResult);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfExcavatorReviewComment"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }
    
    @WebMethod(operationName = "editReviewFromExcvator")
    @Override
    public String editReviewFromExcvator(int jobResponseID, int submitReview) {
        String res;
        try {

            res = jobDAO.editReviewFromExcvator(jobResponseID, submitReview);
              }catch(JobManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"editReviewFromExcvator"+"\n"
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
                            +"Fct   ws- : "+"editReviewFromExcvator"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    }
   
    @WebMethod(operationName = "editReviewCommentFromExcvator")
    @Override
    public String editReviewCommentFromExcvator(int jobResponseID, String reveiwComment) {
        String res;
        try {

            res = jobDAO.editReviewCommentFromExcvator(jobResponseID, reveiwComment);
              }catch(JobManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"editReviewCommentFromExcvator"+"\n"
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
                            +"Fct   ws- : "+"editReviewCommentFromExcvator"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    }
    
    
    @WebMethod(operationName = "getARangeOfTruckOwnerReviewComment")
    @Override
    public Result getARangeOfTruckOwnerReviewComment(int jobResponseID, int index, int nombreMaxResult) {

        Result result = new Result();
        
        try {

            result = jobDAO.getARangeOfTruckOwnerReviewComment(jobResponseID, index, nombreMaxResult);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfTruckOwnerReviewComment"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }
    
    
    @WebMethod(operationName = "getTruckOwnerAndTruckInfo")
    @Override
    public Result getTruckOwnerAndTruckInfo(int accountID, int jobResponseID){

        Result result = new Result();
        
        try {

            result = jobDAO.getTruckOwnerAndTruckInfo(accountID, jobResponseID);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getTruckOwnerAndTruckInfo"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }
    
     
    @WebMethod(operationName = "getARangeOfJobLogForDailyTicket")
    @Override
    public Result getARangeOfJobLogForDailyTicket(int accountID, int jobResponseID, long ticketDate, int index, int nombreMaxResult) {

        Result result = new Result();
        try {

            result = jobLogDAO.getARangeOfJobLogForDailyTicket(accountID, jobResponseID, ticketDate, index, nombreMaxResult);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfJobLogForDailyTicket"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }

    
    @WebMethod(operationName = "getTruckForJobLog")
    @Override
    public Result getTruckForJobLog(int accountID, int jobResponseID){

        Result result = new Result();
        
        try {

            result = jobLogDAO.getTruckForJobLog(accountID, jobResponseID);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getTruckForJobLog"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }
    
    
    @WebMethod(operationName = "getTruckForDailyTicket")
    @Override
    public Result getTruckForDailyTicket(int accountID, int jobResponseID, long ticketDate){

        Result result = new Result();
        
        try {

            result = jobLogDAO.getTruckForDailyTicket(accountID, jobResponseID, ticketDate);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getTruckForDailyTicket"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }
    
    
    @WebMethod(operationName = "getTruckForWeeklyTicket")
    @Override
    public Result getTruckForWeeklyTicket(int accountID, int jobResponseID, long weeklyStartDate, long weeklyEndDate){

        Result result = new Result();
        
        try {

            result = jobLogDAO.getTruckForWeeklyTicket(accountID, jobResponseID, weeklyStartDate, weeklyEndDate);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getTruckForWeeklyTicket"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }
    
    
    @WebMethod(operationName = "getARangeOfJobLogForDailyTicketPerTruck")
    @Override
    public Result getARangeOfJobLogForDailyTicketPerTruck(int accountID, int jobResponseID, long ticketDate, int truckID, int index, int nombreMaxResult) {

        Result result = new Result();
        try {

            result = jobLogDAO.getARangeOfJobLogForDailyTicketPerTruck(accountID, jobResponseID, ticketDate, truckID, index, nombreMaxResult);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfJobLogForDailyTicketPerTruck"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }
    
}
