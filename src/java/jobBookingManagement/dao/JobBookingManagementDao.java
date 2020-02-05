/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jobBookingManagement.dao;

import entities.Account;
import entities.DirtType;
import entities.Document;
import entities.Job;
import entities.JobDocument;
import entities.JobResponse;
import entities.PaymentType;
import entities.SolicitedTruck;
import entities.Truck;
import entities.TruckDocument;
import entities.User;
import entities.Validation;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import util.metier.function.JobFonction;
import toolsAndTransversalFunctionnalities.GestionnaireEntite;
import toolsAndTransversalFunctionnalities.Result;
import util.date.DateFunction;
import util.exception.JobBookingManagementException;
import util.metier.function.DriverFunction;

/**
 *
 * @author erman
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class JobBookingManagementDao implements IJobBookingManagementDaoLocal, IJobBookingManagementDaoRemote{

    @EJB
    GestionnaireEntite ges;
    
    
    JobFonction jf;
    
    final static org.apache.logging.log4j.Logger logger = LogManager.getLogger("dump1");
    
    @Override
    public Result addNewJobBooking(long startDate, long endDate, int hourPerDay, String companyRate, String rate, int accountID,
            String jobLocation, String jobNumber, int typeOfDirtyID, String weight, String startTime, String directDeposit, 
            String timeSheet, String automatedBooking, String jobDescription, String jobInstruction, String documentsID,
            String trucksID, float billingPrice, int paymentTypeID, int timeZone, String timeZoneID, float latitude, float longitude) throws JobBookingManagementException{
        
        
        String res = "good";
        String anotherRes = null;
        Account account = null;
        Job job;
        PaymentType paymentType = null;
        DirtType dirtType = null;
        JobDocument jobDocument;
        Document document = null;
        Result result = new Result();
        List<JobDocument> jobDocumentList;
        List<JobResponse> jobResponseList;
        List<User> truckOwnerList;
        
        List<SolicitedTruck> solicitedTruckList;
        List<String> listResult = new ArrayList<>();
        List<String> truckNumberList = new ArrayList<>();
        
        result.setMsg("bad");
        
        try{
            
            ges.creatEntityManager();
            account = (Account)ges.getEntityManager().find(Account.class, accountID, LockModeType.PESSIMISTIC_FORCE_INCREMENT);// Pour le JobNumber
            paymentType = (PaymentType) ges.getEntityManager().find(PaymentType.class, paymentTypeID);
            dirtType = (DirtType) ges.getEntityManager().find(DirtType.class, typeOfDirtyID);
            
            
            if(account == null){
                ges.closeEm();
                result.setMsg("InvalidAccountID");
                return result;
            }
            
            if (account.getBillingreceiverID() == null) {
                ges.closeEm();
                result.setMsg("NotSetPaymentParam");
                return result;
            }

            if (paymentType == null) {
                ges.closeEm();
                result.setMsg("InvalidPaymentTypeID");
                return result;
            }
            
            if (dirtType == null) {
                ges.closeEm();
                result.setMsg("InvalidDirtTypeID");
                return result;
            }
            
            
            String[] trucksIDList = trucksID.split("#");
            int numberOfTruck = trucksIDList.length;
            
            jobNumber = "" + (account.getUser().getJobNumber() + 1);
            job = new Job();
            job.setStartDate(new Date(startDate));
            job.setEndDate(new Date(endDate));
            job.setWeight(weight);
            job.setStartTime(startTime);
            job.setHourPerDay(hourPerDay);
            job.setNumberOfTruck(numberOfTruck);
            job.setRate(0);
            job.setJobCreationType(1);
            job.setTypeofdirtID(dirtType);
            job.setJobLocation(jobLocation);
            job.setLatitude(latitude);
            job.setLongitude(longitude);
            job.setTimezoneID(timeZoneID);
            job.setJobNumber(jobNumber);
            job.setDeleted(false);
            job.setClose(false);
            job.setCreationDate(DateFunction.getGMTDate());
            job.setLastModifiedDate(DateFunction.getGMTDate());
            job.setLastEditEndDate(DateFunction.getGMTDate());
            job.setCompanyRate(companyRate);
            job.setBillingPrice(billingPrice);
            job.setPaymenttypeID(paymentType);
            
            job.setDotNumber("");
            job.setGeneralLiability(0);
            job.setTruckLiability(0);
            job.setProofOfTruckLiability("");
            job.setDirectDeposit(directDeposit);
            job.setTimeSheet(timeSheet);
            job.setAutomatedBooking(automatedBooking);
            job.setJobDescription(jobDescription);
            job.setJobInstruction(jobInstruction);
            job.setTimeZone(timeZone);
            
            job.setExcavatorID(account.getUser());
            account.getUser().getJobList().add(job);
            account.getUser().setJobNumber(Integer.parseInt(jobNumber));
            ges.getEntityManager().persist(job);
            ges.getEntityManager().merge(account);
            ges.getEntityManager().merge(account.getUser());
            ges.closeEm();
            
            res += ";"+job.getJobID()+";"+job.getJobNumber();
            
            String[] documentList = documentsID.split("#");
            jobDocumentList = new ArrayList<>();
            for (String documentID : documentList) {
                try {
                    
                    jobDocument = new JobDocument();
                    
                    document = ges.getEntityManager().find(Document.class, Integer.parseInt(documentID));
                    if(document != null){
                        document.setDescription("JobDocument");
                        jobDocument.setCreationDate(DateFunction.getGMTDate());
                        jobDocument.setDeleted(false);
                        jobDocument.setType("jobDocument");
                        jobDocument.setDocumentID(document);
                        jobDocument.setJobID(job);
                        ges.getEntityManager().persist(jobDocument);
                        
                        jobDocumentList.add(jobDocument);
                        
                        job.getJobDocumentList().add(jobDocument);
                        
                        document.getJobDocumentList().add(jobDocument);
                        
                        ges.getEntityManager().merge(job);
                        ges.getEntityManager().merge(document);
                        
                    }else{
                        res += ";invalidDocumentID="+documentID;
                    }
                
                }catch(NumberFormatException err){
                    logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+err.getMessage());
                    res += ";badDocumentID="+documentID;
                }catch(NullPointerException er){
                    if(job.getJobDocumentList() == null || job.getJobDocumentList().isEmpty()){
                        job.setJobDocumentList(jobDocumentList);
                    }
                    if(document.getJobDocumentList() == null || document.getJobDocumentList().isEmpty()){
                        document.setJobDocumentList(jobDocumentList);
                    }
                }
            }

            String excavatorInfo = "" + job.getExcavatorID().getAccountID().getEmail();
            excavatorInfo += ";" + job.getExcavatorID().getName();
            excavatorInfo += ";" + job.getExcavatorID().getSurname();
            excavatorInfo += ";" + job.getExcavatorID().getTelephone();
            excavatorInfo += ";" + job.getExcavatorID().getCellPhone();
            excavatorInfo += ";" + "null";
            result.setObject(excavatorInfo);
            
            solicitedTruckList = new ArrayList<>();
            jobResponseList = new ArrayList<>();
            truckOwnerList = new ArrayList<>();
            
            for (String truckID : trucksIDList) {
                
                Validation validation = null;
                JobResponse jobResponse = null;
                
                try {

                    Truck truck = ges.getEntityManager().find(Truck.class, Integer.parseInt(truckID));
                    if (truck != null) {
                        
                        jf = new JobFonction();
                        User truckOwner = truck.getUserID();
                        
                        //Si c'est un nouveau truckOwner
                        if (!truckOwnerList.contains(truckOwner) ) {
                            truckOwnerList.add(truckOwner);

                            //Création des jobResponses assosié
                            jobResponse = new JobResponse();
                            jobResponse.setStartDate(new Date(startDate));
                            jobResponse.setEndDate(new Date(endDate));
                            jobResponse.setDeleted(false);
                            jobResponse.setTimeZone(timeZone);
                            jobResponse.setPaymenttypeID(paymentType);
                            jobResponse.setBillingPrice(billingPrice);
                            jobResponse.setSubmitReview(0);
                            jobResponse.setReviewFromExcavator(0);
                            jobResponse.setPaymentStatus(false);
                            jobResponse.setSubmitted(false);
                            jobResponse.setCreationDate(DateFunction.getGMTDate());
                            jobResponse.setLastModifiedDate(DateFunction.getGMTDate());
                            jobResponse.setLastEditEndDate(DateFunction.getGMTDate());
                            jobResponse.setHourPerDay(hourPerDay);
                            jobResponse.setTruckownerID(truckOwner);
                            jobResponse.setJobID(job);
                            jobResponse.setNumberOfTruck(1);
                            ges.getEntityManager().persist(jobResponse);
                            
                            jobResponseList.add(jobResponse);
                            truckNumberList.add(truck.getTruckNumber());
                            
                            if (job.getJobResponseList() != null) {
                                job.getJobResponseList().add(jobResponse);
                            } else {
                                job.setJobResponseList(new ArrayList<>(jobResponseList));
                            }
                            account.getUser().getJobResponseList().add(jobResponse);

                            //prise en compte de la validation
                            validation = new Validation();
                            validation.setClientValidation(0);
                            validation.setTruckOwnerValidation(0);
                            validation.setDeleted(false);
                            validation.setJobID(job);
                            validation.setJobresponseID(jobResponse);
                            validation.setClientValidationDate(DateFunction.getGMTDate());
                            validation.setCreationDate(DateFunction.getGMTDate());
                            ges.getEntityManager().persist(validation);
                           
                            // Information pour l'envoi des mails
                            anotherRes += ";\n" + jobResponse.getJobresponseID() + ";\n ValidationID = "
                                    + validation.getValidationID() + ";\n JobID = "
                                    + validation.getJobID().getJobID() + ";\n JobResponseID = "
                                    + validation.getJobresponseID().getJobresponseID();
                        } else {
                            
                            int position = truckOwnerList.indexOf(truckOwner);
                            
                            jobResponse = jobResponseList.get(position);
                            jobResponse.setNumberOfTruck(jobResponse.getNumberOfTruck() + 1);
                            
                            truckNumberList.add(position, truckNumberList.get(position) + ",  " + truck.getTruckNumber());

                            ges.getEntityManager().merge(jobResponse);
                            ges.getEntityManager().merge(job);
                            ges.getEntityManager().merge(account);
                            ges.getEntityManager().merge(account.getUser());

                        }
                        
                        SolicitedTruck solicitedTruck = new SolicitedTruck();
                        
                        solicitedTruck.setStartDate(new Date(startDate));
                        solicitedTruck.setEndDate(new Date(endDate));
                        solicitedTruck.setCreationDate(DateFunction.getGMTDate());
                        solicitedTruck.setDeleted(false);
                        solicitedTruck.setTruckAvailable(false);
                        solicitedTruck.setTruckID(truck);
                        solicitedTruck.setJobresponseID(jobResponse);
                        ges.getEntityManager().persist(solicitedTruck);
                        
                        solicitedTruckList.add(solicitedTruck);
                        if (jobResponse.getSolicitedTruckList() != null) {
                            jobResponse.getSolicitedTruckList().add(solicitedTruck);
                        } else {
                            jobResponse.setSolicitedTruckList(new ArrayList<>(solicitedTruckList));
                        }
                        if (truck.getSolicitedTruckList() != null) {
                            truck.getSolicitedTruckList().add(solicitedTruck);
                        } else {
                            truck.setSolicitedTruckList(new ArrayList<>(solicitedTruckList));
                        }
                        ges.getEntityManager().merge(jobResponse);
                        ges.getEntityManager().merge(truck);

                    } else {
                        anotherRes += ";invalidTruckID=" + truckID;
                    }

                } catch (NumberFormatException err) {
                    anotherRes += ";badTruckID=" + truckID;
                    System.err.println(err.getMessage() + " \t" + res);
                } 
                
                result.setObjectList(listResult);
                result.setMsg(res);
                ges.closeEm();

            }
            
            int position = 0;
            for (JobResponse jobResponse : jobResponseList) {
                String truckOwnerInfo = "" + jobResponse.getTruckownerID().getAccountID().getEmail();
                truckOwnerInfo += ";" + jobResponse.getTruckownerID().getName();
                truckOwnerInfo += ";" + jobResponse.getTruckownerID().getSurname();
                truckOwnerInfo += ";" + jobResponse.getTruckownerID().getTelephone();
                truckOwnerInfo += ";" + jobResponse.getTruckownerID().getCellPhone();
                truckOwnerInfo += ";" + "null";

                String jobResponseInfos = "" + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(jobResponse.getJobID().getStartDate(), timeZoneID);//0
                jobResponseInfos += ";" + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(jobResponse.getJobID().getEndDate(), timeZoneID);
                jobResponseInfos += ";" + jobResponse.getJobID().getJobLocation();
                jobResponseInfos += ";" + jobResponse.getJobID().getJobNumber();
                jobResponseInfos += ";" + jobResponse.getHourPerDay();//4
                jobResponseInfos += ";" + jobResponse.getNumberOfTruck();//5
                jobResponseInfos += ";" + jobResponse.getJobID().getJobDescription();//6
                jobResponseInfos += ";" + jobResponse.getJobID().getJobInstruction();
                jobResponseInfos += ";" + jobResponse.getJobID().getDotNumber();
                jobResponseInfos += ";" + jobResponse.getJobID().getGeneralLiability();
                jobResponseInfos += ";" + jobResponse.getJobID().getTruckLiability();
                jobResponseInfos += ";" + jobResponse.getJobID().getProofOfTruckLiability();
                jobResponseInfos += ";" + (jobResponse.getJobID().getLenghtOfBed() == null ? "" : jobResponse.getJobID().getLenghtOfBed().getName());
                jobResponseInfos += ";" + (jobResponse.getJobID().getTruckAxle() == null ? "" : jobResponse.getJobID().getTruckAxle().getTruckAxleName());
                jobResponseInfos += ";" + jobResponse.getJobID().getJobCreationType();
                jobResponseInfos += ";" + "null";
                listResult.add(truckOwnerInfo + "##" + jobResponseInfos + "##" + truckNumberList.get(position++));

            }
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+anotherRes);
            
        }catch(Throwable th){
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"addNewJobBooking: Error:  " + res);
            result.setMsg("Error: " + res);
            return result;
        }
        
        result.afficherResult("addNewJobBooking");
        return result;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfJobToConfirmByTruckOwner(int truckOwnerID, int index, int nombreMaxResult) throws JobBookingManagementException{
        
        String res;
        Result result = new Result();
        List<Validation> validationList = null;
        int numberOfElts = 0;
        Account account;
        List<String> listResult = new ArrayList<>();
        result.setMsg("good");
        try{
            
            ges.creatEntityManager();
            account = (Account)ges.getEntityManager().find(Account.class, truckOwnerID);
            
            if(account == null){
                result.setMsg("InvaLidTruckOwnerID");
                result.afficherResult("getARangeOfJobToConfirmByTruckOwner");
                return result;
            }
            
            Query query =  ges.getEntityManager().createQuery("SELECT v FROM Validation v WHERE v.jobID.endDate > :currentDate AND v.jobID.deleted = FALSE AND v.truckOwnerValidation = 0 AND v.jobID.jobCreationType = 1 AND v.jobresponseID.deleted = FALSE AND v.jobresponseID.truckownerID = :truckOwnerID ORDER BY v.creationDate DESC");
            
            query.setParameter("truckOwnerID", account.getUser());
            query.setParameter("currentDate", DateFunction.getGMTDate());
            numberOfElts = query.getResultList().size();
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResult);
            
            validationList = query.getResultList();
            
        }catch(Throwable th){
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            result.setMsg(string.toString());
            result.afficherResult("getARangeOfJobToConfirmByTruckOwner");
            return result;
        }
        
        if(validationList != null && !validationList.isEmpty()){
            
            for(Validation validation: validationList ){
                
                //Query takenTruckQuery =  ges.getEntityManager().createNativeQuery(TruckSearchQuery.getQueryForSearchTakenTrucksOfJobResponse(), SolicitedTruck.class)
                       // .setParameter(1, validation.getJobresponseID().getJobresponseID())
                       // ;
                
                //logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"takenTruckQuery.getResultList()  =  " + takenTruckQuery.getResultList());
                //if (!takenTruckQuery.getResultList().isEmpty()) {
                //    numberOfElts --;
                //} else {
                    Job job = validation.getJobID();
                    String jobDocumentsID = "";
                    String jobDocumentsPATH = "";
                    
                    List<JobDocument> jobDocumentList = ges.getEntityManager().createQuery("SELECT j FROM JobDocument j WHERE j.deleted = FALSE AND j.jobID = :jobID AND j.documentID NOT IN (SELECT d FROM Document d WHERE d.deleted = TRUE) ").setParameter("jobID", job).getResultList();
                    
                    for (JobDocument jobDocument : jobDocumentList) {
                        jobDocumentsID += "#" + jobDocument.getDocumentID().getDocumentID();
                        jobDocumentsPATH += "#" + jobDocument.getDocumentID().getPathName();
                    }
                    
                    try {
                        
                        jobDocumentsID = jobDocumentsID.substring(1, jobDocumentsID.length());
                        jobDocumentsPATH = jobDocumentsPATH.substring(1, jobDocumentsPATH.length());
                    } catch (Exception e) {
                        
                    }
                    
                    JobFonction jf = new JobFonction();
                    long numberTruck = job.getNumberOfTruck() - jf.jobRemainingTruck(job, ges);
                    
                    res = "" + job.getJobID() + ";"//0
                            + job.getStartDate().getTime() + ";"
                            + job.getEndDate().getTime() + ";"
                            + job.getHourPerDay() + ";"
                            + job.getNumberOfTruck() + ";"
                            + job.getCompanyRate() + ";"//5
                            + (job.getExcavatorID().getRate()) + ";"
                            + (job.getLenghtOfBed() == null ? "" : job.getLenghtOfBed().getLenghtofbedID()) + ";"
                            + (job.getTruckAxle() == null ? "" : job.getTruckAxle().getTruckaxleID()) + ";"
                            + job.getExcavatorID().getAccountID().getEmail() + ";"
                            + job.getExcavatorID().getName() + ";"//10
                            + job.getExcavatorID().getSurname() + ";"
                            + job.getJobLocation() + ";"
                            + job.getJobNumber() + ";"
                            + (job.getTypeofdirtID() == null ? "" : job.getTypeofdirtID().getLabel()) + ";"//14
                            + job.getWeight() + ";"//15
                            + job.getStartTime() + ";"
                            + job.getDotNumber() + ";"
                            + job.getGeneralLiability() + ";"
                            + job.getTruckLiability() + ";"
                            + job.getProofOfTruckLiability() + ";"//20
                            + job.getDirectDeposit() + ";"
                            + job.getTimeSheet() + ";"
                            + job.getAutomatedBooking() + ";"
                            + job.getJobDescription() + ";"
                            + job.getJobInstruction() + ";"//25
                            + jobDocumentsID + ";"
                            + jobDocumentsPATH + ";"
                            + numberTruck + ";"//number of remaining Truck for jobs
                            + job.getExcavatorID().getTelephone() + ";"
                            + validation.getJobresponseID().getJobresponseID() + ";"//30
                            + validation.getTruckOwnerValidation() + ";"
                            + validation.getJobresponseID().getBillingPrice() + ";"
                            + (validation.getJobresponseID().getPaymenttypeID() == null ? -1 : validation.getJobresponseID().getPaymenttypeID().getPaymenttypeID()) + ";"
                            + job.getBillingPrice() + ";"
                            + job.getPaymenttypeID().getLibel() + ";"//35
                            + job.getExcavatorID().getCellPhone()+ ";"//36
                            + job.getTimezoneID()+ ";"//37
                            + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(job.getStartDate(), job.getTimezoneID()) + ";"//38
                            + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(job.getEndDate(), job.getTimezoneID()) + ";"//39
                            + "null";
                    
                    listResult.add(res);
                //}
            }
        }

        listResult.add("" + numberOfElts);
        result.setObjectList(listResult);
        result.afficherResult("getARangeOfJobToConfirmByTruckOwner");
        return result;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfTruckRequestedByJobResponseID(int accountID, int jobResponseID, int index, int nombreMaxResultat) throws JobBookingManagementException{
        List<SolicitedTruck> solicitedTruckList = null;
        Result result = new Result();
        Account account;
        List<String> lts = new ArrayList<>();
        int size = 0;

        try {
            ges.creatEntityManager();

            ges.creatEntityManager();
            account = (Account) ges.getEntityManager().find(Account.class, accountID);

            if (account == null) {
                result.setMsg("InvaLidTruckOwnerID");
                result.afficherResult("getARangeOfTruckRequestedByJobResponseID");
                return result;
            }

            Query query = ges.getEntityManager().createQuery("SELECT s FROM SolicitedTruck s WHERE s.deleted = FALSE AND s.jobresponseID.jobresponseID = :jobresponseID AND s.jobresponseID.deleted = FALSE  ORDER BY s.creationDate DESC ")
                    .setParameter("jobresponseID", jobResponseID);
            solicitedTruckList = (List<SolicitedTruck>) query.getResultList();
            size = solicitedTruckList.size();

            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResultat);

            solicitedTruckList = (List<SolicitedTruck>) query.getResultList();
            result.setMsg("good");

        } catch (Throwable th) {
            if (ges == null) {
                result.setMsg("Could not create Entity Manager\n" + th.getMessage());
                result.afficherResult("getARangeOfTruckRequestedByJobResponseID");
                return result;
            }
            result.setMsg(th.getMessage());
        }

        if (solicitedTruckList != null && !solicitedTruckList.isEmpty()) {
            String res = "";
            for (SolicitedTruck solicitedTruck : solicitedTruckList) {

                Truck truck = solicitedTruck.getTruckID();

                Calendar c = Calendar.getInstance();
                c.setTime(truck.getTrucktypeID().getYear());

                String driverInfo = DriverFunction.getDriverForTruck(truck);
                
                res = truck.getTruckID() + ";"//0
                        + truck.getTruckNumber() + ";"
                        + (truck.getPicture()== null ? "" : truck.getPicture().getPathName()) + ";"
                        + truck.getAvailable() + ";"
                        + truck.getCreationDate().getTime() + ";"//4
                        + truck.getLocationPrice() + ";"
                        + truck.getLenghtofbedID().getLenghtofbedID() + ";"
                        + truck.getDistance() + ";"
                        + truck.getPhoneNumber() + ";"
                        + truck.getTruckZipCode() + ";"//9
                        + truck.getDOTNumber() + ";"
                        + truck.getGeneralLiability() + ";"
                        + truck.getInsuranceLiability() + ";"
                        + (truck.getPictureInsurance()== null ? "" : truck.getPictureInsurance().getPathName()) + ";"
                        + "" + ";"//14
                        + truck.getYear() + ";"//15
                        + "" + ";"
                        + "" + ";"
                        + "" + ";"
                        + truck.getTruckaxleID().getTruckaxleID() + ";"//19
                        + driverInfo + ";"//20 ~ 24
                        + (truck.getPicture()== null ? "" : truck.getPicture().getDocumentID()) + ";"
                        + (truck.getPictureInsurance()== null ? "" : truck.getPictureInsurance().getDocumentID()) + ";"
                        + truck.getTruckDescription() + ";"//23 ~ 27
                        + solicitedTruck.getTruckAvailable() + ";"//24 ~ 28
                        + "null";
                lts.add(res);

            }
            lts.add("" + size);
            result.setObjectList(lts);
        }

        result.afficherResult("getARangeOfTruckRequestedByJobResponseID");
        return result;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfTruckRequestedByJobID(int accountID, int jobID, int index, int nombreMaxResultat) throws JobBookingManagementException{
        List<SolicitedTruck> solicitedTruckList = null;
        Result result = new Result();
        Account account;
        List<String> lts = new ArrayList<>();
        int size = 0;
        
        try{
            ges.creatEntityManager();
            
            account = (Account)ges.getEntityManager().find(Account.class, accountID);
            
            if(account == null){
                result.setMsg("InvaLidAccountID");
                result.afficherResult("getARangeOfTruckRequestedByJobID");
                return result;
            }
            
            Query query =  ges.getEntityManager().createQuery("SELECT s FROM SolicitedTruck s WHERE s.deleted = FALSE AND s.jobresponseID IN (SELECT j FROM JobResponse j WHERE j.deleted = FALSE AND j.jobID.jobID = :jobID AND j.jobID.deleted = FALSE)  ORDER BY s.creationDate DESC ");

            query.setParameter("jobID", jobID);
            solicitedTruckList = (List<SolicitedTruck>)query.getResultList();
            size = solicitedTruckList.size();
            
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResultat);
            
            solicitedTruckList = (List<SolicitedTruck>)query.getResultList();
            result.setMsg("good");
        
        }catch(Throwable th){
            if(ges == null){
                result.setMsg("Could not create Entity Manager\n"+th.getMessage());
                result.afficherResult("getARangeOfTruckRequestedByJobID");
                return result;
            }
            result.setMsg(th.getMessage());
        }
        
        
        if(solicitedTruckList != null && !solicitedTruckList.isEmpty()){
            String res = "";
            for(SolicitedTruck solicitedTruck: solicitedTruckList){
                
                Truck truck = solicitedTruck.getTruckID();
                
                if(!truck.getDeleted()){
                    Calendar c = Calendar.getInstance();
                    c.setTime(truck.getTrucktypeID().getYear());
                    
                    String driverInfo = DriverFunction.getDriverForTruck(truck);
                    
                    String pictureTrucksID = "";
                    String pictureTrucksPATH = "";
                    
                    String pictureInsurancesID = "";
                    String pictureInsurancesPATH = "";
                    
                    List<TruckDocument> truckDocumentList = ges.getEntityManager().createQuery("SELECT t FROM TruckDocument t WHERE t.deleted = FALSE AND t.truckID = :truckID AND t.documentID NOT IN (SELECT d FROM Document d WHERE d.deleted = TRUE) ").setParameter("truckID", truck).getResultList();
                    
                    for(TruckDocument truckDocument: truckDocumentList){
                        if(truckDocument.getType().equals("pictureTruck")) {
                            pictureTrucksID += "#"+truckDocument.getDocumentID().getDocumentID();
                            pictureTrucksPATH += "#"+truckDocument.getDocumentID().getPathName();
                        } else {
                            pictureInsurancesID += "#"+truckDocument.getDocumentID().getDocumentID();
                            pictureInsurancesPATH += "#"+truckDocument.getDocumentID().getPathName();
                        }
                        
                    }
                    try {
                        
                        pictureTrucksID = pictureTrucksID.substring(1, pictureTrucksID.length());
                        pictureTrucksPATH = pictureTrucksPATH.substring(1, pictureTrucksPATH.length());
                    } catch (Exception e) {
                        
                    }try {
                        
                        pictureInsurancesID = pictureInsurancesID.substring(1, pictureInsurancesID.length());
                        pictureInsurancesPATH = pictureInsurancesPATH.substring(1, pictureInsurancesPATH.length());
                        
                    } catch (Exception e) { }
                    
                    res = truck.getTruckID()+";"
                        +truck.getTruckNumber()+";"
                        +pictureTrucksPATH+";"
                        +truck.getAvailable()+";"
                        +truck.getCreationDate().getTime()+";"
                        +truck.getLocationPrice()+";"
                        
                        +truck.getLenghtofbedID().getLenghtofbedID()+";"
                        +truck.getDistance()+";"
                        +truck.getPhoneNumber()+";"
                        +truck.getTruckZipCode()+";"
                        +truck.getDOTNumber()+";"
                        +truck.getGeneralLiability()+";"//11
                        +truck.getInsuranceLiability()+";"//12
                        +pictureInsurancesPATH+";"
                            
                        +""+";"
                        +truck.getYear()+";"
                        +""+";"
                        +""+";"
                        +""+";"
                        +truck.getTruckaxleID().getTruckaxleID()+";"
                        +driverInfo+";"
                        +pictureTrucksID+";"
                        +pictureInsurancesID+";"
                        +truck.getTruckDescription()+";"
                        +"null"
                            ;
                    lts.add(res);
                }
                
            } 
            lts.add(""+size);
            result.setObjectList(lts);
        }
        
        result.afficherResult("getARangeOfTruckRequestedByJobID");
        return result;
    }

    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfJobResponseByJobID(int accountID, int jobID, int index, int nombreMaxResult) throws JobBookingManagementException{
        
        String res;
        Result result = new Result();
        List<Validation> jobResponses = null;
        int numberOfElts = 0;
        List<String> listResult = new ArrayList<>();
        Job job;
        Account account;
        result.setMsg("good");
        try{
            
            ges.creatEntityManager();
            
            
            account = (Account)ges.getEntityManager().find(Account.class, accountID);
            
            if(account == null){
                result.setMsg("InvaLidAccountID");
                result.afficherResult("getARangeOfJobResponseByJobID");
                return result;
            }
            
            job = (Job)ges.getEntityManager().find(Job.class, jobID);
            
            if(job == null){
                result.setMsg("InvaLidJobID");
                result.afficherResult("getARangeOfJobResponseByJobID");
                return result;
            }
            
            //Query query =  ges.getEntityManager().createQuery("SELECT v FROM Validation v WHERE v.deleted = FALSE AND v.jobID.jobCreationType = 1 AND v.jobresponseID.deleted = FALSE AND v.jobID = :jobID ORDER BY v.creationDate DESC");
            Query query = ges.getEntityManager().createQuery("SELECT a FROM Validation a WHERE a.deleted = false AND a.jobID = :jobID AND a.jobresponseID.deleted = false ORDER BY a.creationDate DESC");
            
            query.setParameter("jobID", job);
            numberOfElts = query.getResultList().size();
            query.setFirstResult(index);
            jobResponses = query.getResultList();
            
        }catch(Throwable th){
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getARangeOfJobResponseByJobID: res: " + res);
            result.setMsg("InvalidQuery");
        }

        if (jobResponses != null && !jobResponses.isEmpty()) {

            for (Validation validation : jobResponses) {

                JobResponse jobResponse = validation.getJobresponseID();

                String reviewComment = jobResponse.getCommentFromExcavator();
                res = "" + jobResponse.getJobresponseID() + ";"//00
                        + jobResponse.getStartDate().getTime() + ";"
                        + jobResponse.getEndDate().getTime() + ";"
                        + jobResponse.getHourPerDay() + ";"
                        + jobResponse.getNumberOfTruck() + ";"
                        + jobResponse.getTruckownerID().getName() + ";"//05
                        + jobResponse.getTruckownerID().getSurname() + ";"
                        + jobResponse.getTruckownerID().getAccountID().getEmail() + ";"
                        + jobResponse.getCreationDate().getTime() + ";"
                        + jobResponse.getJobID().getDeleted() + ";"
                        + jobResponse.getJobresponseID() + ";"//10
                        + jobResponse.getJobID().getJobID() + ";"
                        + jobResponse.getTruckownerID().getAccountID().getAccountID() + ";"
                        + validation.getClientValidation() + ";"
                        + jobResponse.getSubmitted() + ";"
                        + jobResponse.getBillingPrice() + ";"//15
                        + jobResponse.getJobID().getNumberOfTruck() + ";"
                        + (0) + ";"
                        + jobResponse.getReviewFromExcavator() + ";"
                        + (reviewComment == null ? "" : reviewComment.replaceAll(";", "%%%")) + ";"
                        + jobResponse.getTruckownerID().getRate() + ";"//20
                        + jobResponse.getTruckownerID().getTelephone() + ";"
                        + jobResponse.getTruckownerID().getCellPhone() + ";"
                        + jobResponse.getTruckownerID().getAddress() + ";"
                        + (jobResponse.getPaymenttypeID() == null ? -1 : jobResponse.getPaymenttypeID().getPaymenttypeID()) + ";"
                        + validation.getTruckOwnerValidation() + ";"//25
                        + jobResponse.getJobID().getTimezoneID() + ";"//26
                        + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(jobResponse.getStartDate(), jobResponse.getJobID().getTimezoneID()) + ";"//27
                        + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(jobResponse.getEndDate(), jobResponse.getJobID().getTimezoneID()) + ";"//28
                        + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(jobResponse.getCreationDate(), jobResponse.getJobID().getTimezoneID()) + ";"//29
                        + "null";

                listResult.add(res);
            }
        }

        listResult.add("" + numberOfElts);
        result.setObjectList(listResult);
        result.afficherResult("getARangeOfJobResponseByJobID");
        return result;
    }
    
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfJobForTruckByExcavatorID(int excavatorID, int index, int nombreMaxResult) throws JobBookingManagementException{
        
        String res;
        Result result = new Result();
        List<Job> jobs = null;
        Account account;
        int numberOfElts = 0;
        List<String> listResult = new ArrayList<>();
        result.setMsg("good");
        try{
            
            ges.creatEntityManager();
            account = (Account)ges.getEntityManager().find(Account.class, excavatorID);
            
            if (account == null) {
                result.setMsg("InvaLidExcavatorID");
                result.afficherResult("getARangeOfJobForTruckByExcavatorID");
                return result;
            }
            
            //Query query =  ges.getEntityManager().createQuery("SELECT a FROM Job a WHERE a.deleted = FALSE AND a.excavatorID  = :excavatorID AND a.jobCreationType = 1  ORDER BY a.creationDate DESC");
            Query query =  ges.getEntityManager().createQuery("SELECT DISTINCT a.jobID FROM Validation a WHERE a.deleted = FALSE AND a.jobID.excavatorID  = :excavatorID AND a.jobID.jobCreationType = 1 AND a.truckOwnerValidation = 0  AND a.jobID.endDate >= :currentDate  ORDER BY a.creationDate DESC")
                    .setParameter("excavatorID", account.getUser())
                    .setParameter("currentDate", DateFunction.getGMTLastDateOfDay())
                    ;
            numberOfElts = query.getResultList().size();
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResult);
            
            jobs = (List<Job>)query.getResultList();
            
        }catch(Throwable th){
            result.setMsg(th.getMessage());
        }
        
        
        if(jobs != null && !jobs.isEmpty()){
            
            for(Job job: jobs ){
                
                
                String jobDocumentsID = "";
                String jobDocumentsPATH = "";
                
                List<JobDocument> jobDocumentList = ges.getEntityManager().createQuery("SELECT j FROM JobDocument j WHERE j.deleted = FALSE AND j.jobID = :jobID AND j.documentID NOT IN (SELECT d FROM Document d WHERE d.deleted = TRUE) ").setParameter("jobID", job).getResultList();
                
                for(JobDocument jobDocument: jobDocumentList){
                    jobDocumentsID += "#"+jobDocument.getDocumentID().getDocumentID();
                    jobDocumentsPATH += "#"+jobDocument.getDocumentID().getPathName();
                }
                
                try {
                        
                    jobDocumentsID = jobDocumentsID.substring(1, jobDocumentsID.length());
                    jobDocumentsPATH = jobDocumentsPATH.substring(1, jobDocumentsPATH.length());
                } catch (Exception e) {

                }

                JobFonction jf = new JobFonction();
                long numberTruck = job.getNumberOfTruck() - jf.jobRemainingTruck(job, ges);

                res = "" + job.getJobID() + ";"//00
                        + job.getStartDate().getTime() + ";"
                        + job.getEndDate().getTime() + ";"
                        + job.getHourPerDay() + ";"
                        + job.getNumberOfTruck() + ";"
                        + job.getCompanyRate() + ";"//05
                        + (job.getExcavatorID().getRate()) + ";"
                        + (job.getLenghtOfBed() == null ? "" : job.getLenghtOfBed().getLenghtofbedID()) + ";"
                        + (job.getTruckAxle() == null ? "" : job.getTruckAxle().getTruckaxleID()) + ";"
                        + job.getJobLocation() + ";"
                        + job.getJobNumber() + ";"//10
                        + job.getClose() + ";"
                        + (job.getTypeofdirtID() == null ? "" : job.getTypeofdirtID().getLabel()) + ";"
                        + job.getWeight() + ";"
                        + job.getStartTime() + ";"
                        + job.getDotNumber() + ";"//15
                        + job.getGeneralLiability() + ";"
                        + job.getTruckLiability() + ";"
                        + job.getProofOfTruckLiability() + ";"
                        + job.getDirectDeposit() + ";"
                        + job.getTimeSheet() + ";"//20
                        + job.getAutomatedBooking() + ";"
                        + job.getJobDescription() + ";"
                        + job.getJobInstruction() + ";"
                        + jobDocumentsID + ";"
                        + jobDocumentsPATH + ";"//25
                        + numberTruck + ";"
                        + job.getExcavatorID().getTelephone() + ";"//27
                        + job.getTimezoneID() + ";"//28
                        + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(job.getStartDate(), job.getTimezoneID()) + ";"//29
                        + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(job.getEndDate(), job.getTimezoneID()) + ";"//30
                        + "null";

                listResult.add(res);
            }
        }

        listResult.add("" + numberOfElts);
        result.setObjectList(listResult);
        result.afficherResult("getARangeOfJobForTruckByExcavatorID");
        return result;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfJobToConfirmByExcavatorID(int excavatorID, int index, int nombreMaxResult) throws JobBookingManagementException{
        
        String res;
        Result result = new Result();
        List<Job> jobs = null;
        //List<Validation> validationList = null;
        Account account;
        int numberOfElts = 0;
        List<String> listResult = new ArrayList<>();
        result.setMsg("good");
        try{
            
            ges.creatEntityManager();
            account = (Account)ges.getEntityManager().find(Account.class, excavatorID);
            
            if (account == null) {
                result.setMsg("InvaLidExcavatorID");
                result.afficherResult("getARangeOfJobForTruckByExcavatorID");
                return result;
            }
            
            Query query =  ges.getEntityManager().createQuery("SELECT DISTINCT a.jobID FROM Validation a WHERE a.deleted = FALSE AND a.jobID.excavatorID  = :excavatorID AND a.jobID.endDate >= :currentDate AND a.jobresponseID.deleted  = FALSE AND a.clientValidation = 0 AND a.truckOwnerValidation = 1  ORDER BY a.creationDate DESC")
                    .setParameter("excavatorID", account.getUser())
                    .setParameter("currentDate", DateFunction.getGMTLastDateOfDay())
                    ;
            numberOfElts = query.getResultList().size();
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResult);
            
            jobs = (List<Job>)query.getResultList();
            
        }catch(Throwable th){
            result.setMsg(th.getMessage());
        }
        
        
        if(jobs != null && !jobs.isEmpty()){
            
            for(Job job: jobs ){
                
                
                String jobDocumentsID = "";
                String jobDocumentsPATH = "";
                
                List<JobDocument> jobDocumentList = ges.getEntityManager().createQuery("SELECT j FROM JobDocument j WHERE j.deleted = FALSE AND j.jobID = :jobID AND j.documentID NOT IN (SELECT d FROM Document d WHERE d.deleted = TRUE) ").setParameter("jobID", job).getResultList();
                
                for(JobDocument jobDocument: jobDocumentList){
                    jobDocumentsID += "#"+jobDocument.getDocumentID().getDocumentID();
                    jobDocumentsPATH += "#"+jobDocument.getDocumentID().getPathName();
                }
                
                try {
                        
                    jobDocumentsID = jobDocumentsID.substring(1, jobDocumentsID.length());
                    jobDocumentsPATH = jobDocumentsPATH.substring(1, jobDocumentsPATH.length());
                } catch (Exception e) {

                }

                JobFonction jf = new JobFonction();
                long numberTruck = job.getNumberOfTruck() - jf.jobRemainingTruck(job, ges);

                res = "" + job.getJobID() + ";"//00
                        + job.getStartDate().getTime() + ";"
                        + job.getEndDate().getTime() + ";"
                        + job.getHourPerDay() + ";"
                        + job.getNumberOfTruck() + ";"//04
                        + job.getCompanyRate() + ";"
                        + (job.getExcavatorID().getRate()) + ";"
                        + (job.getLenghtOfBed() == null ? "" : job.getLenghtOfBed().getLenghtofbedID()) + ";"
                        + (job.getTruckAxle() == null ? "" : job.getTruckAxle().getTruckaxleID()) + ";"
                        + job.getJobLocation() + ";"
                        + job.getJobNumber() + ";"//10
                        + job.getClose() + ";"
                        + (job.getTypeofdirtID() == null ? "" : job.getTypeofdirtID().getLabel()) + ";"
                        + job.getWeight() + ";"
                        + job.getStartTime() + ";"
                        + job.getDotNumber() + ";"//15
                        + job.getGeneralLiability() + ";"
                        + job.getTruckLiability() + ";"
                        + job.getProofOfTruckLiability() + ";"
                        + job.getDirectDeposit() + ";"
                        + job.getTimeSheet() + ";"//20
                        + job.getAutomatedBooking() + ";"
                        + job.getJobDescription() + ";"
                        + job.getJobInstruction() + ";"
                        + jobDocumentsID + ";"
                        + jobDocumentsPATH + ";"//25
                        + numberTruck + ";"//26
                        + job.getExcavatorID().getTelephone() + ";"//27
                        + job.getTimezoneID() + ";"//28
                        + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(job.getStartDate(), job.getTimezoneID()) + ";"//29
                        + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(job.getEndDate(), job.getTimezoneID()) + ";"//30
                        + "null";

                listResult.add(res);
            }
        }

        listResult.add("" + numberOfElts);
        result.setObjectList(listResult);
        result.afficherResult("getARangeOfJobToConfirmByExcavatorID");
        return result;
    }


}
