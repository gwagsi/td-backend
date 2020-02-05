/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jobManagement.dao;

import accountManagement.dao.TypeOfUser;
import util.metier.function.JobFonction;
import entities.Account;
import entities.DirtType;
import entities.Document;
import entities.Job;
import entities.JobDocument;
import entities.JobLog;
import entities.JobResponse;
import entities.LenghtOfBed;
import entities.PaymentType;
import entities.SolicitedTruck;
import entities.Truck;
import entities.TruckAxle;
import entities.User;
import entities.Validation;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;
import javax.persistence.PessimisticLockException;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import toolsAndTransversalFunctionnalities.GestionnaireEntite;
import toolsAndTransversalFunctionnalities.Result;
import toolsAndTransversalFunctionnalities.ResultBackend;
import util.date.DateFunction;
import util.exception.JobManagementException;
import util.metier.function.TruckFunction;
import util.query.sql.JobManagementQuery;

/**
 *
 * @author erman
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class JobManagementDao implements IJobManagementDaoLocal, IJobManagementDaoRemote {

    @EJB
    GestionnaireEntite ges;
    final static org.apache.logging.log4j.Logger logger = LogManager.getLogger("dump1");
    
    @Override
    public ResultBackend addNewJob(long startDate, long endDate, int hourPerDay, int numberOfTruck,
            String companyRate, String rate, int excavatorID, int lenghtofbedID, int truckaxleID,
            String jobLocation, String jobNumber, int typeOfDirtyID, String weight, String startTime,
            String dotNumber, int generalLiability, int truckLiability, String proofOfTruckLiability,
            String directDeposit, String timeSheet, String automatedBooking, String jobDescription, String jobInstruction,
            String documentsID, float billingPrice, int paymentTypeID, int timeZone, String timeZoneID, float latitude, float longitude) throws JobManagementException{

        String res = "good";
        ResultBackend result = new ResultBackend();
        List<Object[]> listResult;
        Account account = null;
        Job job;
        JobDocument jobDocument;
        Document document = null;
        List<JobDocument> jobDocumentList;
        PaymentType paymentType;
        TruckAxle truckAxle;
        LenghtOfBed lenghtOfBed;
        DirtType dirtType;

        try {

            ges.creatEntityManager();
            account = (Account) ges.getEntityManager().find(Account.class, excavatorID, LockModeType.PESSIMISTIC_FORCE_INCREMENT);//Pour le jobNumber
            paymentType = (PaymentType) ges.getEntityManager().find(PaymentType.class, paymentTypeID);
            lenghtOfBed = (LenghtOfBed) ges.getEntityManager().find(LenghtOfBed.class, lenghtofbedID);
            truckAxle = (TruckAxle) ges.getEntityManager().find(TruckAxle.class, truckaxleID);
            dirtType = (DirtType) ges.getEntityManager().find(DirtType.class, typeOfDirtyID);

            if (account == null) {
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

            if (truckAxle == null) {
                ges.closeEm();
                result.setMsg("InvalidTruckAxleID");
                return result;
            }

            if (lenghtOfBed == null) {
                ges.closeEm();
                result.setMsg("InvalidLenghtOfBedID");
                return result;
            }

            if (dirtType == null) {
                ges.closeEm();
                result.setMsg("InvalidDirtTypeID");
                return result;
            }

            jobNumber = "" + (account.getUser().getJobNumber() + 1);
            job = new Job();
            job.setStartDate((new Date(startDate)));
            job.setEndDate((new Date(endDate)));
            job.setWeight(weight);
            job.setStartTime(startTime);
            job.setHourPerDay(hourPerDay);
            job.setNumberOfTruck(numberOfTruck);
            job.setRate(0);
            job.setJobCreationType(0);
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

            job.setDotNumber(dotNumber);
            job.setGeneralLiability(generalLiability);
            job.setTruckLiability(truckLiability);
            job.setProofOfTruckLiability(proofOfTruckLiability);
            job.setDirectDeposit(directDeposit);
            job.setTimeSheet(timeSheet);
            job.setAutomatedBooking(automatedBooking);
            job.setJobDescription(jobDescription);
            job.setJobInstruction(jobInstruction);
            job.setTimeZone(timeZone);

            job.setTruckAxle(truckAxle);
            job.setLenghtOfBed(lenghtOfBed);
            job.setExcavatorID(account.getUser());
            account.getUser().getJobList().add(job);
            account.getUser().setJobNumber(Integer.parseInt(jobNumber));
            ges.getEntityManager().persist(job);
            ges.getEntityManager().merge(account);
            ges.getEntityManager().merge(account.getUser());
            ges.closeEm();
            res += ";" + job.getJobID() + ";" + job.getJobNumber();

            String[] documentList = documentsID.split("#");
            jobDocumentList = new ArrayList<>();
            
            String emailExcavator = job.getExcavatorID().getAccountID().getEmail();
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"addNewJobDAO - job: " + job);

            String[] excavatorInfo = {
                emailExcavator,
                job.getExcavatorID().getName(),
                job.getExcavatorID().getSurname(),
                job.getExcavatorID().getTelephone(),
                job.getExcavatorID().getCellPhone()            
            };
            
            Float[] GPSCoordinates = {
                job.getLatitude(), 
                job.getLongitude()   
            };
            
            String[] jobResponseInfos = {
                DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(job.getStartDate(), job.getTimezoneID()),//0
                DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(job.getEndDate(), job.getTimezoneID()),//1
                job.getJobLocation(),//2
                job.getJobNumber(),
                String.valueOf(job.getHourPerDay()),
                String.valueOf(job.getNumberOfTruck()),//5
                job.getJobDescription(),
                job.getJobInstruction(),
                job.getDotNumber(),//8
                String.valueOf(job.getGeneralLiability()),//9
                String.valueOf(job.getTruckLiability()),//10
                job.getProofOfTruckLiability(),
                job.getLenghtOfBed() == null ? null : job.getLenghtOfBed().getName(),//12
                job.getTruckAxle() == null ? null : job.getTruckAxle().getTruckAxleName(),//13
                String.valueOf(job.getJobCreationType()),//14
            };
            
            listResult = new ArrayList<>();
            listResult.add(excavatorInfo);
            listResult.add(jobResponseInfos);
            listResult.add(GPSCoordinates);
            result.setResultArraysList(listResult);
            
            for (String documentID : documentList) {
                try {

                    jobDocument = new JobDocument();

                    document = ges.getEntityManager().find(Document.class, Integer.parseInt(documentID));
                    if (document != null) {

                        document.setDescription("JobDocument");
                        jobDocument.setCreationDate(DateFunction.getGMTDate());
                        jobDocument.setDeleted(false);
                        jobDocument.setType("jobDocument");
                        jobDocument.setDocumentID(document);
                        jobDocument.setJobID(job);
                        ges.getEntityManager().persist(jobDocument);
                        ges.closeEm();

                        jobDocumentList.add(jobDocument);

                        job.getJobDocumentList().add(jobDocument);

                        document.getJobDocumentList().add(jobDocument);

                        ges.getEntityManager().merge(job);
                        ges.getEntityManager().merge(document);

                        ges.closeEm();

                    } else {
                        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"[addNewJob] invalidDocumentID:" + documentID);
                    }

                } catch (NumberFormatException err) {
                    logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"[addNewJob] badDocumentID:" + documentID);
                    logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+err.getMessage());
                } catch (NullPointerException er) {
                    if (job.getJobDocumentList() == null || job.getJobDocumentList().isEmpty()) {
                        job.setJobDocumentList(jobDocumentList);
                    }
                    if (document != null && (document.getJobDocumentList() == null || document.getJobDocumentList().isEmpty())) {
                        document.setJobDocumentList(jobDocumentList);
                    }
                }
            }

        } catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            result.setMsg(res);
            result.afficherResult("addNewJob");
            return result;
        }
        
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"addNewJob ~ res: " + res);
        result.setMsg(res);
        return result;
    }

    // Il est a noter que lors de l'ajout d'un jobResponse un ligne dans la table validation est automatiquement creer
    // Ceci permettant au truckOwner d'initier un contrat avec l'excavator
    @Override
    public ResultBackend addNewJobResponse(long startDate, long endDate, int hourPerDay, String companyRate,
            String rate, int truckOwnerID, int jobID, float billingPrice, int paymentTypeID, String trucksID, int timeZone) throws JobManagementException{

        String res = "";
        Account account = null;
        Job job;
        JobResponse jobResponse;
        PaymentType paymentType = null;
        Validation validation;
        String etat = "";
        List<SolicitedTruck> solicitedTruckList;
        SolicitedTruck solicitedTruck;
        Truck truck = null;
        ResultBackend result = new ResultBackend();
        List<Object[]> listResult;

        try {

            ges.creatEntityManager();
            account = (Account) ges.getEntityManager().find(Account.class, truckOwnerID);
            job = (Job) ges.getEntityManager().find(Job.class, jobID);
            paymentType = (PaymentType) ges.getEntityManager().find(PaymentType.class, paymentTypeID);

            if (job == null) {
                ges.closeEm();
                result.setMsg("InvalidJobID");
                return result;
            }

            if (job.getClose()) {
                ges.closeEm();
                result.setMsg("ClosingJobID");
                return result;
            }

            if (account == null) {
                ges.closeEm();
                result.setMsg("InvalidAccountID");
                return result;
            }

            if (paymentType == null) {
                ges.closeEm();
                result.setMsg("InvalidPaymentTypeID");
                return result;
            }

            etat += "\nFin verification des infomations ...\n";

            //creation du jobResponse
            jobResponse = new JobResponse();
            jobResponse.setStartDate(new Date(startDate));
            jobResponse.setEndDate(new Date(endDate));
            jobResponse.setDeleted(false);
            jobResponse.setPaymenttypeID(paymentType);
            jobResponse.setBillingPrice(billingPrice);
            jobResponse.setSubmitReview(0);
            jobResponse.setPaymentStatus(false);
            jobResponse.setSubmitted(false);
            jobResponse.setCreationDate(DateFunction.getGMTDate());
            jobResponse.setLastModifiedDate(DateFunction.getGMTDate());
            jobResponse.setLastEditEndDate(DateFunction.getGMTDate());
            jobResponse.setHourPerDay(hourPerDay);
            jobResponse.setTruckownerID(account.getUser());
            jobResponse.setJobID(job);
            jobResponse.setNumberOfTruck(0);
            jobResponse.setTimeZone(timeZone);
            ges.getEntityManager().persist(jobResponse);
            ges.closeEm();

            etat += "\nFin d'ajout du jobResponse ...\n";

            job.getJobResponseList().add(jobResponse);
            account.getUser().getJobResponseList().add(jobResponse);

            //prise en compte de la validation
            validation = new Validation();
            validation.setClientValidation(0);
            validation.setTruckOwnerValidation(1);
            validation.setDeleted(false);
            validation.setJobID(job);
            validation.setJobresponseID(jobResponse);
            validation.setTruckOwnerValidationDate(DateFunction.getGMTDate());
            validation.setCreationDate(DateFunction.getGMTDate());
            ges.getEntityManager().persist(validation);
            ges.closeEm();

            etat += "\nFin d'ajout de la validation ...\n";

            ges.getEntityManager().merge(jobResponse);
            ges.getEntityManager().merge(job);
            ges.getEntityManager().merge(account);
            ges.getEntityManager().merge(account.getUser());
            ges.closeEm();

            etat += "\nFin de la transaction ...\n";

            res += ";" + jobResponse.getJobresponseID()
                    + ";" + validation.getValidationID() 
                    + ";" + validation.getJobID().getJobID()
                    + ";" + validation.getJobresponseID().getJobresponseID();
            String emailTruckOwner = jobResponse.getTruckownerID().getAccountID().getEmail();

            String[] truckOwnerInfo = {
                emailTruckOwner,
                jobResponse.getTruckownerID().getName(),
                jobResponse.getTruckownerID().getSurname(),
                jobResponse.getTruckownerID().getTelephone(),
                jobResponse.getTruckownerID().getCellPhone()
            };
            String emailExcavator = job.getExcavatorID().getAccountID().getEmail();
            
            String[] excavatorInfo = {
                emailExcavator,
                job.getExcavatorID().getName(),
                job.getExcavatorID().getSurname(),
                job.getExcavatorID().getTelephone(),
                job.getExcavatorID().getCellPhone()            
            };
            
            
            String[] trucksIDList = trucksID.split("#");
            solicitedTruckList = new ArrayList<>();

            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"--------- TrucksID = " + trucksID);

            for (String truckID : trucksIDList) {
                try {

                    solicitedTruck = new SolicitedTruck();

                    truck = ges.getEntityManager().find(Truck.class, Integer.parseInt(truckID));
                    if (truck != null) {
                        
                        solicitedTruck.setStartDate(new Date(startDate));
                        solicitedTruck.setEndDate(new Date(endDate));
                        solicitedTruck.setCreationDate(DateFunction.getGMTDate());
                        solicitedTruck.setDeleted(false);
                        solicitedTruck.setTruckAvailable(false);
                        solicitedTruck.setTruckID(truck);
                        solicitedTruck.setJobresponseID(jobResponse);
                        ges.getEntityManager().persist(solicitedTruck);
                        ges.closeEm();

                        solicitedTruckList.add(solicitedTruck);

                        jobResponse.getSolicitedTruckList().add(solicitedTruck);

                        truck.getSolicitedTruckList().add(solicitedTruck);
                        jobResponse.setNumberOfTruck(jobResponse.getNumberOfTruck() + 1);
                        ges.getEntityManager().merge(jobResponse);
                        ges.getEntityManager().merge(truck);

                        ges.closeEm();

                    } else {
                        res += ";invalidTruckID=" + truckID;
                    }

                } catch (NumberFormatException err) {
                    res += ";badTruckID=" + truckID;
                    logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+err.getMessage() + " \t" + res);
                } catch (NullPointerException er) {
                    if (jobResponse.getSolicitedTruckList() == null || jobResponse.getSolicitedTruckList().isEmpty()) {
                        jobResponse.setSolicitedTruckList(solicitedTruckList);
                    }
                    if (truck != null && (truck.getSolicitedTruckList() == null || truck.getSolicitedTruckList().isEmpty())) {
                        truck.setSolicitedTruckList(solicitedTruckList);
                    }
                    logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"Error catch " + er.getMessage());
                }

            }

            int timezone = jobResponse.getTimeZone();
            String timeZoneID = job.getTimezoneID();
            
            String[] jobResponseInfos = {
                DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(jobResponse.getJobID().getStartDate(), timeZoneID),//0
                DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(jobResponse.getJobID().getEndDate(), timeZoneID),//1
                jobResponse.getJobID().getJobLocation(),//2
                jobResponse.getJobID().getJobNumber(),
                String.valueOf(jobResponse.getHourPerDay()),
                String.valueOf(jobResponse.getNumberOfTruck()),//5
                jobResponse.getJobID().getJobDescription(),
                jobResponse.getJobID().getJobInstruction(),
                jobResponse.getJobID().getDotNumber(),//8
                String.valueOf(jobResponse.getJobID().getGeneralLiability()),//9
                String.valueOf(jobResponse.getJobID().getTruckLiability()),//10
                jobResponse.getJobID().getProofOfTruckLiability(),
                (jobResponse.getJobID().getLenghtOfBed() == null ? null : jobResponse.getJobID().getLenghtOfBed().getName()),//12
                (jobResponse.getJobID().getTruckAxle() == null ? "" : jobResponse.getJobID().getTruckAxle().getTruckAxleName()),//13
                String.valueOf(jobResponse.getJobID().getJobCreationType()),//14
                (timezone < 0 ? "":"+") + timezone/(3600000),//15
            };
            
            listResult = new ArrayList<>();
            listResult.add(excavatorInfo);
            listResult.add(truckOwnerInfo);
            listResult.add(jobResponseInfos);
            result.setResultArraysList(listResult);
            
            result.setMsg("good");

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
            result.setMsg(etat + e.getMessage() + "\n" + res);
            return result;
        }
        
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"addNewJobResponse ~ res: " + res);
        return result;
    }

    @Override
    public ResultBackend editJob(int jobID, long startDate, long endDate, int hourPerDay, int numberOfTruck,
            String companyRate, String rate, int excavatorID, int lenghtofbedID, int truckaxleID, String jobLocation,
            String jobNumber, int typeOfDirtyID, String weight, String startTime,
            String dotNumber, int generalLiability, int truckLiability, String proofOfTruckLiability,
            String directDeposit, String timeSheet, String automatedBooking, String jobDescription, String jobInstruction, 
            float billingPrice, int paymentTypeID, String timeZoneID, float latitude, float longitude)throws JobManagementException {

        String res = "good";
        ResultBackend result = new ResultBackend();
        List<Object[]> listResult;
        Account account = null;
        Job job = null;
        PaymentType paymentType;
        TruckAxle truckAxle;
        LenghtOfBed lenghtOfBed;
        DirtType dirtType;

        try {

            ges.creatEntityManager();
            job = (Job) ges.getEntityManager().find(Job.class, jobID);
            account = (Account) ges.getEntityManager().find(Account.class, excavatorID);
            paymentType = (PaymentType) ges.getEntityManager().find(PaymentType.class, paymentTypeID);
            lenghtOfBed = (LenghtOfBed) ges.getEntityManager().find(LenghtOfBed.class, lenghtofbedID);
            truckAxle = (TruckAxle) ges.getEntityManager().find(TruckAxle.class, truckaxleID);
            dirtType = (DirtType) ges.getEntityManager().find(DirtType.class, typeOfDirtyID);

            if (job == null || job.getDeleted()) {
                ges.closeEm();
                result.setMsg("InvalidJobID");
                return result;
            }

            if (account == null) {
                ges.closeEm();
                result.setMsg("InvalidAccountID");
                return result;
            }

            if (paymentType == null) {
                ges.closeEm();
                result.setMsg("InvalidPaymentTypeID");
                return result;
            }
            
            if (truckAxle == null && job.getJobCreationType() != 1) {
                ges.closeEm();
                result.setMsg("InvalidTruckAxleID");
                return result;
            }
            
            if (lenghtOfBed == null && job.getJobCreationType() != 1) {
                ges.closeEm();
                result.setMsg("InvalidLenghtOfBedID");
                return result;
            }
            
            if (dirtType == null) {
                ges.closeEm();
                result.setMsg("InvalidDirtTypeID");
                return result;
            }
            
            String oldJobLocation = job.getJobLocation();
            String oldStartDate = DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(job.getStartDate(), timeZoneID);
            String oldEndDate = DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(job.getEndDate(), timeZoneID);
            String oldNbTruck = "" + job.getNumberOfTruck();
            String oldDescription = job.getJobDescription();
            String oldInstruction = job.getJobInstruction();
            String oldProofOfTruckLiability = job.getProofOfTruckLiability();
            String oldDOT_Number = job.getDotNumber();
            String oldGeneralLiability = "" + job.getGeneralLiability();
            String oldTruckLiability = "" + job.getTruckLiability();
            String oldLenghtOfBed = (job.getLenghtOfBed() == null ? "" : job.getLenghtOfBed().getName()) ;
            String oldTruckAxle = (job.getTruckAxle() == null ? "" : job.getTruckAxle().getTruckAxleName());
            
            Date newJobEndate = new Date(endDate);
            Date newJobStartDate = new Date(startDate);
            job.setLastModifiedDate(DateFunction.getGMTDate());
            
            if (job.getEndDate().compareTo(newJobEndate) != 0) {
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editJob ~  -->   oldJobEndate: " + job.getEndDate() + "   -->    newJobEndate: " + newJobEndate);
                job.setLastEditEndDate(DateFunction.getGMTDate());
            }
            
            job.setStartDate(newJobStartDate);
            job.setEndDate(newJobEndate);
            job.setWeight(weight);
            job.setStartTime(startTime);
            job.setHourPerDay(hourPerDay);
            job.setNumberOfTruck(numberOfTruck);
            job.setBillingPrice(billingPrice);
            job.setPaymenttypeID(paymentType);

            //Ceci est ignore parce que le rating du job doit etre calcule.
            //job.setRate(rate);
            job.setTypeofdirtID(dirtType);
            job.setJobLocation(jobLocation);
            job.setLatitude(latitude);
            job.setLongitude(longitude);
            job.setTimezoneID(timeZoneID);
            job.setJobNumber(jobNumber);
            job.setCompanyRate(companyRate);

            job.setDotNumber(dotNumber);
            job.setGeneralLiability(generalLiability);
            job.setTruckLiability(truckLiability);
            job.setProofOfTruckLiability(proofOfTruckLiability);
            job.setDirectDeposit(directDeposit);
            job.setTimeSheet(timeSheet);
            job.setAutomatedBooking(automatedBooking);
            job.setJobDescription(jobDescription);
            job.setJobInstruction(jobInstruction);
            
            job.setTruckAxle(truckAxle);
            job.setLenghtOfBed(lenghtOfBed);
            job.setExcavatorID(account.getUser());

            ges.getEntityManager().merge(job);
            ges.closeEm();
            res += ";" + job.getJobID();
            String emailExcavator = job.getExcavatorID().getAccountID().getEmail();

            String[] excavatorInfo = {
                emailExcavator,
                job.getExcavatorID().getName(),
                job.getExcavatorID().getSurname(),
                job.getExcavatorID().getTelephone(),
                job.getExcavatorID().getCellPhone()            
            };
            
            Float[] GPSCoordinates = {
                job.getLatitude(), 
                job.getLongitude()   
            };
            
            String[][] jobResponseInfos = {
                {DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(job.getStartDate(), job.getTimezoneID()), oldStartDate},//0
                {DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(job.getEndDate(), job.getTimezoneID()), oldEndDate},//1
                {job.getJobLocation() , oldJobLocation},//2
                {job.getJobNumber()},
                {String.valueOf(job.getHourPerDay())},
                {String.valueOf(job.getNumberOfTruck()), oldNbTruck},//5
                {job.getJobDescription()},
                {job.getJobInstruction()},
                {job.getDotNumber(), oldDOT_Number},//8
                {String.valueOf(job.getGeneralLiability()), oldGeneralLiability},//9
                {String.valueOf(job.getTruckLiability()), oldTruckLiability},//10
                {job.getProofOfTruckLiability(), oldProofOfTruckLiability},
                {job.getLenghtOfBed() == null ? null : job.getLenghtOfBed().getName(), oldLenghtOfBed},//12
                {job.getTruckAxle() == null ? null : job.getTruckAxle().getTruckAxleName(), oldTruckAxle},//13
                {String.valueOf(job.getJobCreationType())},//14
            };
            
            listResult = new ArrayList<>();
            listResult.add(excavatorInfo);
            listResult.add(jobResponseInfos);
            listResult.add(GPSCoordinates);
            result.setResultArraysList(listResult);
        } catch (Throwable th) {
            result.setMsg("Error" + th.getMessage());
            return result;
        }

        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editJob ~ res: " + res);
        result.setMsg(res);
        return result;
    }

    @Override
    public String editResponseJob(int jobResponseID, long startDate, long endDate, int hourPerDay, int numberOfTruck, String companyRate, String rate, int truckOwnerID, float billingPrice, int paymentTypeID) throws JobManagementException{

        String res = "good";
        Account account = null;
        JobResponse jobResponse = null;
        PaymentType paymentType;
        Job job;

        try {

            ges.creatEntityManager();

            jobResponse = (JobResponse) ges.getEntityManager().find(JobResponse.class, jobResponseID);
            account = (Account) ges.getEntityManager().find(Account.class, truckOwnerID);
            paymentType = (PaymentType) ges.getEntityManager().find(PaymentType.class, paymentTypeID);

            if (jobResponse == null) {
                ges.closeEm();
                return "InvalidJobResponseID";
            }

            if (jobResponse.getJobID().getClose()) {
                ges.closeEm();
                return "ClosingJobID";
            }

            if (account == null) {
                ges.closeEm();
                return "InvalidAccountID";
            }

            if (paymentType == null) {
                ges.closeEm();
                return "InvalidPaymentTypeID";
            }

            //creation d'un jobResponse dans le cas ou le truckOnwer n'a jamais eu a modifier ce job
            job = jobResponse.getJobID();

            jobResponse.setStartDate(new Date(startDate));
            jobResponse.setEndDate(new Date(endDate));
            jobResponse.setLastModifiedDate(DateFunction.getGMTDate());
            jobResponse.setDeleted(false);
            jobResponse.setHourPerDay(hourPerDay);
            jobResponse.setPaymenttypeID(paymentType);
            jobResponse.setNumberOfTruck(numberOfTruck);
            jobResponse.setBillingPrice(billingPrice);
            jobResponse.setJobID(job);

            ges.getEntityManager().merge(jobResponse);
            ges.closeEm();

            ges.getEntityManager().merge(job);
            ges.getEntityManager().merge(account.getUser());
            ges.closeEm();
            res += ";" + jobResponse.getJobresponseID();

        } catch (Throwable th) {
             throw new JobManagementException(getClass()+"","editEmployeeInfo",1,th.getMessage(),th.getMessage());
        
            //return th.getMessage();
        }
        return res;
    }

    @Override
    public String editJobResponsEndDate(int jobResponseID, long endDate, int truckOwnerID) throws JobManagementException{

        String res = "good";
        Account account = null;
        JobResponse jobResponse = null;
        Job job;

        try {

            ges.creatEntityManager();

            jobResponse = (JobResponse) ges.getEntityManager().find(JobResponse.class, jobResponseID);
            account = (Account) ges.getEntityManager().find(Account.class, truckOwnerID);

            if (jobResponse == null) {
                ges.closeEm();
                return "InvalidJobResponseID";
            }

            if (account == null) {
                ges.closeEm();
                return "InvalidAccountID";
            }

            //creation d'un jobResponse dans le cas ou le truckOnwer n'a jamais eu a modifier ce job
            job = jobResponse.getJobID();
            
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editJobResponsEndDate ~  oldJobResponseEndate: " + jobResponse.getEndDate() + "   -->    newJobResponseEndate: " + new Date(endDate));
            jobResponse.setEndDate(new Date(endDate));
            jobResponse.setLastModifiedDate(DateFunction.getGMTDate());
            jobResponse.setLastEditEndDate(DateFunction.getGMTDate());
            jobResponse.setDeleted(false);
            jobResponse.setJobID(job);

            ges.getEntityManager().merge(jobResponse);
            ges.closeEm();

            ges.getEntityManager().merge(job);
            ges.getEntityManager().merge(account.getUser());
            ges.closeEm();
            res += ";" + jobResponse.getJobresponseID();

        } catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
             throw new JobManagementException(getClass()+"","editEmployeeInfo",1,res,th.getMessage());
        
            //return res;
        }
        
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editJobResponsEndDate ~ res: " + res);
        return res;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfByExcavator(String typeOfQuery, int excavatorID, int index, int nombreMaxResult) throws JobManagementException{

        String res;
        Result result = new Result();
        List<Job> jobs = null;
        Account account;
        int numberOfElts = 0;
        List<String> listResult = new ArrayList<>();
        result.setMsg("good");
        try {

            account = (Account) ges.getEntityManager().find(Account.class, excavatorID);

            if (account == null) {
                result.setMsg("InvaLidExcavatorID");
                return result;
            }

            Query query = JobManagementQuery.getQueryForSearchJobByExacavator(typeOfQuery, ges, account.getUser());
            //Query query = ges.getEntityManager().createQuery();
            
            //query.setParameter("excavatorID", account.getUser());
            //query.setParameter("currentDate", DateFunction.getGMTDateMidnight());
            numberOfElts = query.getResultList().size();
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResult);

            jobs = (List<Job>) query.getResultList();
            
        } catch (Throwable th) {
            result.setMsg(th.getMessage());
        }

        if (jobs != null && !jobs.isEmpty()) {

            for (Job job : jobs) {

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
                long numberOfTruck = jf.jobRemainingTruck(job, ges);

                res = "" + job.getJobID() + ";"
                        + job.getStartDate().getTime() + ";"
                        + job.getEndDate().getTime() + ";"
                        + job.getHourPerDay() + ";"
                        + job.getNumberOfTruck() + ";"
                        + job.getCompanyRate() + ";"
                        + (job.getExcavatorID().getRate()) + ";"

                        + (job.getLenghtOfBed() == null ? "" : job.getLenghtOfBed().getLenghtofbedID()) + ";"
                        + (job.getTruckAxle() == null ? "" : job.getTruckAxle().getTruckaxleID()) + ";"
                        + job.getJobLocation() + ";"
                        + job.getJobNumber() + ";"
                        + job.getClose() + ";"
                        + (job.getTypeofdirtID() == null ? "" : job.getTypeofdirtID().getLabel())+ ";"//12
                        + job.getWeight() + ";"
                        + job.getStartTime() + ";"
                        + job.getDotNumber() + ";"
                        + job.getGeneralLiability() + ";"//16
                        + job.getTruckLiability() + ";"//17
                        + job.getProofOfTruckLiability() + ";"
                        + job.getDirectDeposit() + ";"
                        + job.getTimeSheet() + ";"//20
                        + job.getAutomatedBooking() + ";"//21
                        + job.getJobDescription() + ";"
                        + job.getJobInstruction() + ";"
                        + jobDocumentsID + ";"
                        + jobDocumentsPATH + ";"
                        + numberOfTruck + ";"//26
                        + job.getExcavatorID().getTelephone() + ";"
                        + job.getJobCreationType() + ";"//28
                        + (job.getNumberOfTruck() - numberOfTruck) + ";"//29
                        + job.getTimezoneID() + ";"//30
                        + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(job.getStartDate(), job.getTimezoneID()) + ";"//31
                        + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(job.getEndDate(), job.getTimezoneID()) + ";"//32
                        + "null";

                listResult.add(res);
            }
        }

        listResult.add("" + numberOfElts);
        result.setObjectList(listResult);
        //result.afficherResult(typeOfQuery);
        return result;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfActifJobByExcavator(int excavatorID, int index, int nombreMaxResult)throws JobManagementException {

        
        long debut = System.currentTimeMillis();
        String res;
        Result result = new Result();
        List<Job> jobs = null;
        Account account = null;
        int numberOfElts = 0;
        List<String> listResult = new ArrayList<>();
        result.setMsg("good");
        try {

            ges.creatEntityManager();
            account = (Account) ges.getEntityManager().find(Account.class, excavatorID);

            if (account == null) {
                result.setMsg("InvaLidExcavatorID");
                return result;
            }

            Query query = JobManagementQuery.getQueryForSearchJobByExacavator("getARangeOfActifJobByExcavator", ges, account.getUser());

            //query.setParameter("excavatorID", account.getUser());
            //query.setParameter("firstCurrentDate", DateFunction.getGMTLastDateOfDay());
            //query.setParameter("lastCurrentDate", DateFunction.getGMTDateMidnight());
            numberOfElts = query.getResultList().size();
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResult);

            jobs = (List<Job>) query.getResultList();

        } catch (Throwable th) {
            result.setMsg(th.getMessage());
        }
        
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getARangeOfActifJobByExcavator: account = " + account);
        if (jobs != null && !jobs.isEmpty()) {

            for (Job job : jobs) {

                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getARangeOfActifJobByExcavator: job = " + job);
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
                long numberOfTruck = jf.jobRemainingTruck(job, ges);;

                res = "" + job.getJobID() + ";"
                        + job.getStartDate().getTime() + ";"
                        + job.getEndDate().getTime() + ";"
                        + job.getHourPerDay() + ";"
                        + job.getNumberOfTruck() + ";"
                        + job.getCompanyRate() + ";"
                        + (job.getExcavatorID().getRate()) + ";"

                        + (job.getLenghtOfBed() == null ? "" : job.getLenghtOfBed().getLenghtofbedID()) + ";"
                        + (job.getTruckAxle() == null ? "" : job.getTruckAxle().getTruckaxleID()) + ";"
                        + job.getJobLocation() + ";"
                        + job.getJobNumber() + ";"
                        + job.getClose() + ";"
                        + (job.getTypeofdirtID() == null ? "" : job.getTypeofdirtID().getLabel()) + ";"//12
                        + job.getWeight() + ";"
                        + job.getStartTime() + ";"
                        + job.getDotNumber() + ";"
                        + job.getGeneralLiability() + ";"//16
                        + job.getTruckLiability() + ";"//17
                        + job.getProofOfTruckLiability() + ";"
                        + job.getDirectDeposit() + ";"
                        + job.getTimeSheet() + ";"//20
                        + job.getAutomatedBooking() + ";"//21
                        + job.getJobDescription() + ";"
                        + job.getJobInstruction() + ";"
                        + jobDocumentsID + ";"
                        + jobDocumentsPATH + ";"
                        + numberOfTruck + ";"//26
                        + job.getExcavatorID().getTelephone() + ";"
                        + job.getJobCreationType() + ";"
                        + (job.getNumberOfTruck() - numberOfTruck) + ";"//29
                        + job.getTimezoneID() + ";"//30
                        + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(job.getStartDate(), job.getTimezoneID()) + ";"//31
                        + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(job.getEndDate(), job.getTimezoneID()) + ";"//32
                        + "null";

                listResult.add(res);
            }
        }

        listResult.add("" + numberOfElts);
        result.setObjectList(listResult);
        //result.afficherResult("getARangeOfActifJobByExcavator");
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+" Temps d'excution(en ms): " + (System.currentTimeMillis() - debut ));
        return result;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfJobResponse(int index, int nombreMaxResult) throws JobManagementException{

        String res;
        Result result = new Result();
        List<JobResponse> jobResponses = null;
        int numberOfElts = 0;
        List<String> listResult = new ArrayList<>();
        result.setMsg("good");
        try {

            ges.creatEntityManager();

            Query query = ges.getEntityManager().createQuery("SELECT a FROM JobResponse a WHERE a.deleted = false ORDER BY a.creationDate DESC");

            numberOfElts = query.getResultList().size();
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResult);

            jobResponses = query.getResultList();

        } catch (Throwable th) {
            result.setMsg(th.getMessage());
        }

        if (jobResponses != null && !jobResponses.isEmpty()) {

            for (JobResponse jobResponse : jobResponses) {
                String emailTruckowner = jobResponse.getTruckownerID().getAccountID().getEmail();

                res = "" + jobResponse.getJobresponseID() + ";"
                        + jobResponse.getStartDate().getTime() + ";"
                        + jobResponse.getEndDate().getTime() + ";"
                        + jobResponse.getHourPerDay() + ";"
                        + jobResponse.getNumberOfTruck() + ";"
                        + emailTruckowner + ";"
                        + jobResponse.getSubmitted() + ";"
                        + jobResponse.getBillingPrice() + ";"
                        + jobResponse.getTruckownerID().getTelephone() + ";"
                        + jobResponse.getTruckownerID().getCellPhone() + ";"
                        + jobResponse.getTruckownerID().getAddress() + ";"
                        + (jobResponse.getPaymenttypeID() == null ? -1 : jobResponse.getPaymenttypeID().getPaymenttypeID()) + ";"
                        + "null";

                listResult.add(res);
            }
        }

        listResult.add("" + numberOfElts);
        result.setObjectList(listResult);
        return result;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfJob(int index, int nombreMaxResult) throws JobManagementException{

        String res;
        Result result = new Result();
        List<Job> jobs = null;
        int numberOfElts = 0;
        List<String> listResult = new ArrayList<>();
        result.setMsg("good");
        try {

            ges.creatEntityManager();

            //Query query =  ges.getEntityManager().createQuery("SELECT a FROM  Job a WHERE a.deleted = FALSE AND a.close = FALSE AND a NOT IN  (SELECT b.jobID FROM Validation b WHERE b.deleted = TRUE OR b.clientValidation = 1 )");
            Query query = ges.getEntityManager().createQuery("SELECT a FROM  Job a WHERE a.deleted = FALSE AND a.close = FALSE ORDER BY a.creationDate DESC");

            numberOfElts = query.getResultList().size();
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResult);

            jobs = query.getResultList();

        } catch (Throwable th) {
            result.setMsg(th.getMessage());
        }

        if (jobs != null && !jobs.isEmpty()) {

            for (Job job : jobs) {

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
                long numberOfTruck = jf.jobRemainingTruck(job, ges);
                String emailExcavator = job.getExcavatorID().getAccountID().getEmail();

                res = "" + job.getJobID() + ";"//00
                        + job.getStartDate().getTime() + ";"
                        + job.getEndDate().getTime() + ";"
                        + job.getHourPerDay() + ";"
                        + job.getNumberOfTruck() + ";"
                        + job.getCompanyRate() + ";"//05
                        + (job.getExcavatorID().getRate()) + ";"
                        + (job.getLenghtOfBed() == null ? "" : job.getLenghtOfBed().getLenghtofbedID()) + ";"
                        + (job.getTruckAxle() == null ? "" : job.getTruckAxle().getTruckaxleID()) + ";"
                        + emailExcavator + ";"
                        + job.getExcavatorID().getName() + ";"//10
                        + job.getExcavatorID().getSurname() + ";"
                        + job.getJobLocation() + ";"
                        + job.getJobNumber() + ";"
                        + (job.getTypeofdirtID() == null ? "" : job.getTypeofdirtID().getLabel()) + ";"
                        + job.getExcavatorID().getAddress() + ";"//15
                        + job.getWeight() + ";"
                        + job.getStartTime() + ";"
                        + job.getDotNumber() + ";"
                        + job.getGeneralLiability() + ";"
                        + job.getTruckLiability() + ";"//20
                        + job.getProofOfTruckLiability() + ";"
                        + job.getDirectDeposit() + ";"
                        + job.getTimeSheet() + ";"
                        + job.getAutomatedBooking() + ";"
                        + job.getJobDescription() + ";"//25
                        + job.getJobInstruction() + ";"
                        + jobDocumentsID + ";"
                        + jobDocumentsPATH + ";"
                        + numberOfTruck + ";"
                        + job.getExcavatorID().getTelephone() + ";"//30
                        + job.getJobCreationType() + ";"//31
                        + job.getTimezoneID()+ ";"//32
                        + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(job.getStartDate(), job.getTimezoneID()) + ";"//33
                        + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(job.getEndDate(), job.getTimezoneID()) + ";"//34
                        + "null";

                listResult.add(res);
            }
        }

        listResult.add("" + numberOfElts);
        result.setObjectList(listResult);
        return result;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfJobResponseByTruckOwner(int truckOwnerID, int index, int nombreMaxResult)throws JobManagementException {

        String res;
        Result result = new Result();
        List<Validation> validationList = null;
        int numberOfElts = 0;
        List<String> listResult = new ArrayList<>();
        Account account;
        result.setMsg("good");
        try {

            ges.creatEntityManager();
            account = (Account) ges.getEntityManager().find(Account.class, truckOwnerID);

            if (account == null) {
                result.setMsg("InvaLidTruckOwnerID");
                return result;
            }

            //Query query = ges.getEntityManager().createQuery("SELECT a FROM JobResponse a WHERE a.deleted = false AND a.truckownerID = :truckOwnerID  AND a IN  (SELECT b.jobresponseID FROM Validation b WHERE b.deleted = FALSE AND b.clientValidation = 0 ) ");

            Query query =  ges.getEntityManager().createQuery("SELECT b FROM Validation b WHERE b.jobID.deleted = FALSE AND b.clientValidation = 0 AND b.truckOwnerValidation = 1 AND b.jobID.endDate > :currentDate AND b.jobresponseID.truckownerID = :truckOwnerID  AND b.jobresponseID.deleted = FALSE ORDER BY b.creationDate DESC");
            query.setParameter("truckOwnerID", account.getUser());
            query.setParameter("currentDate", DateFunction.getGMTDate());
            numberOfElts = query.getResultList().size();
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResult);

            validationList = query.getResultList();

        } catch (Throwable th) {
            result.setMsg( "getARangeOfJobResponseByTruckOwner:  Error" + th.getMessage());
            result.afficherResult("getARangeOfJobResponseByTruckOwner");
        }

        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getARangeOfJobResponseByTruckOwner:  jobResponses = " + validationList);
        if (validationList != null && !validationList.isEmpty()) {

            for (Validation validation : validationList) {
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getARangeOfJobResponseByTruckOwner:  validation = " + validation);
                
                JobResponse jobResponse = validation.getJobresponseID();
                
                res = "" + jobResponse.getJobresponseID() + ";"//0
                        + jobResponse.getJobID().getJobID() + ";"
                        + jobResponse.getStartDate().getTime() + ";"
                        + jobResponse.getEndDate().getTime() + ";"
                        + jobResponse.getHourPerDay() + ";"
                        + jobResponse.getNumberOfTruck() + ";"//5
                        + jobResponse.getJobID().getJobLocation() + ";"
                        + jobResponse.getJobID().getJobNumber() + ";"
                        + jobResponse.getCreationDate().getTime() + ";"
                        + jobResponse.getJobID().getClose() + ";"
                        + jobResponse.getJobID().getExcavatorID().getName() + ";"//10
                        + jobResponse.getJobID().getExcavatorID().getSurname() + ";"
                        + jobResponse.getJobID().getExcavatorID().getAccountID().getAccountID() + ";"
                        + jobResponse.getJobID().getClose() + ";"
                        + jobResponse.getJobID().getExcavatorID().getAccountID().getEmail() + ";"
                        + jobResponse.getSubmitted() + ";"//15
                        + jobResponse.getBillingPrice() + ";"
                        + jobResponse.getTruckownerID().getTelephone() + ";"
                        + jobResponse.getTruckownerID().getCellPhone() + ";"
                        + jobResponse.getTruckownerID().getAddress() + ";"
                        + (jobResponse.getPaymenttypeID() == null ? -1 : jobResponse.getPaymenttypeID().getPaymenttypeID()) + ";"//20
                        + validation.getTruckOwnerValidation() + ";"
                        + (jobResponse.getJobID().getLenghtOfBed() == null ? "" : jobResponse.getJobID().getLenghtOfBed().getLenghtofbedID()) + ";"
                        + (jobResponse.getJobID().getTruckAxle() == null ? "" : jobResponse.getJobID().getTruckAxle().getTruckaxleID()) + ";"
                        + jobResponse.getJobID().getDotNumber() + ";"
                        + jobResponse.getJobID().getTruckLiability()+ ";"//25
                        + jobResponse.getJobID().getGeneralLiability()+ ";"
                        + jobResponse.getJobID().getTimeSheet()+ ";"
                        + jobResponse.getJobID().getJobDescription()+ ";"
                        + jobResponse.getJobID().getJobInstruction()+ ";"
                        + jobResponse.getJobID().getExcavatorID().getTelephone()+ ";"//30
                        + jobResponse.getJobID().getProofOfTruckLiability()+ ";"
                        + jobResponse.getJobID().getDirectDeposit()+ ";"
                        + jobResponse.getJobID().getRate()+ ";"//33
                        + jobResponse.getJobID().getExcavatorID().getCellPhone()+ ";"//34
                        + (jobResponse.getJobID().getTypeofdirtID() == null ? "" : jobResponse.getJobID().getTypeofdirtID().getLabel()) + ";"//35
                        + jobResponse.getJobID().getTimezoneID()+ ";"//36
                        + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(jobResponse.getStartDate(), jobResponse.getJobID().getTimezoneID())+ ";"//37
                        + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(jobResponse.getEndDate(), jobResponse.getJobID().getTimezoneID()) + ";"//38
                        + "null";

                listResult.add(res);

            }
        }

        listResult.add("" + numberOfElts);
        result.setObjectList(listResult);
        //result.afficherResult("getARangeOfJobResponseByTruckOwner");
        return result;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfJobResponseByJobID(int jobID, int index, int nombreMaxResult)throws JobManagementException {

        String res;
        Result result = new Result();
        List<Validation> validationList = null;
        int numberOfElts = 0;
        List<String> listResult = new ArrayList<>();
        Job job;
        result.setMsg("good");
        try {

            ges.creatEntityManager();
            job = (Job) ges.getEntityManager().find(Job.class, jobID);

            if (job == null) {
                result.setMsg("InvaLidJobID");
                return result;
            }

            //Query query = ges.getEntityManager().createQuery("SELECT a FROM JobResponse a WHERE a.deleted = false AND a.jobID = :jobID");
            Query query = ges.getEntityManager().createQuery("SELECT a FROM Validation a WHERE a.deleted = false AND a.jobID = :jobID AND a.jobresponseID.deleted = false ORDER BY a.creationDate DESC");

            query.setParameter("jobID", job);
            numberOfElts = query.getResultList().size();
            query.setFirstResult(index);
            validationList = query.getResultList();

            //query = ges.getEntityManager().createQuery("SELECT a FROM  JobResponse a WHERE a.deleted = FALSE AND a IN (SELECT DISTINCT b.jobresponseID FROM Validation b WHERE b.deleted = FALSE AND b.clientValidation = 1   )");
            //jobResponsValides = query.getResultList();

        } catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getARangeOfJobResponseByJobID: res: " + res);
            result.setMsg("InvalidQuery");
        }

        if (validationList != null && !validationList.isEmpty()) {

            for (Validation validation : validationList) {
                
                JobResponse jobResponse = validation.getJobresponseID();
                JobFonction jf = new JobFonction();
                long numberOfTruck = jf.jobRemainingTruck(jobResponse.getJobID(), ges);

                String valid = (validation.getClientValidation() == 1 ? "" + 1 : "" + 0);
                
                String reviewComment = jobResponse.getCommentFromExcavator();
                res = "" + jobResponse.getJobresponseID() + ";"//0
                        + jobResponse.getStartDate().getTime() + ";"
                        + jobResponse.getEndDate().getTime() + ";"
                        + jobResponse.getHourPerDay() + ";"
                        + jobResponse.getNumberOfTruck() + ";"//4
                        + jobResponse.getTruckownerID().getName() + ";"//5
                        + jobResponse.getTruckownerID().getSurname() + ";"
                        + jobResponse.getTruckownerID().getAccountID().getEmail() + ";"
                        + jobResponse.getCreationDate().getTime() + ";"
                        + jobResponse.getJobID().getDeleted() + ";"//9
                        + jobResponse.getJobresponseID() + ";"
                        + jobResponse.getJobID().getJobID() + ";"
                        + jobResponse.getTruckownerID().getAccountID().getAccountID() + ";"
                        + valid + ";"
                        + jobResponse.getSubmitted() + ";"
                        + jobResponse.getBillingPrice() + ";"//15
                        + jobResponse.getJobID().getNumberOfTruck() + ";"//16
                        + (jobResponse.getJobID().getNumberOfTruck() - numberOfTruck) + ";"
                        + jobResponse.getReviewFromExcavator() + ";"
                        + (reviewComment == null ? "" : reviewComment.replaceAll(";", "%%%")) + ";"
                        + jobResponse.getTruckownerID().getRate() + ";"
                        + jobResponse.getTruckownerID().getTelephone() + ";"//21
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
    public Result getJobByID(int jobID) throws JobManagementException{

        String res = "";
        Result result = new Result();
        Job job = null;
        result.setMsg("good");
        try {

            ges.creatEntityManager();
            job = (Job) ges.getEntityManager().find(Job.class, jobID);

        } catch (Throwable th) {
            result.setMsg(th.getMessage());
        }

        if (job == null /*|| job.getDeleted()*/) {
            result.setObject("null");
            return result;
        }
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
        long numberOfTruck = jf.jobRemainingTruck(job, ges);

        res = "" + job.getJobID() + ";"//00
                + job.getStartDate().getTime() + ";"
                + job.getEndDate().getTime() + ";"
                + job.getHourPerDay() + ";"
                + job.getNumberOfTruck() + ";"
                + job.getCompanyRate() + ";"
                + (job.getExcavatorID().getRate()) + ";"
                + (job.getLenghtOfBed() == null ? "" : job.getLenghtOfBed().getLenghtofbedID()) + ";"
                + (job.getTruckAxle() == null ? "" : job.getTruckAxle().getTruckaxleID()) + ";"
                + job.getJobNumber() + ";"//09
                + job.getExcavatorID().getUserID() + ";"
                + job.getJobLocation() + ";"
                + (job.getTypeofdirtID() == null ? "" : job.getTypeofdirtID().getLabel()) + ";"
                + job.getWeight() + ";"
                + job.getStartTime() + ";"
                + job.getDotNumber() + ";"
                + job.getGeneralLiability() + ";"
                + job.getTruckLiability() + ";"
                + job.getProofOfTruckLiability() + ";"
                + job.getDirectDeposit() + ";"//19
                + job.getTimeSheet() + ";"
                + job.getAutomatedBooking() + ";"
                + job.getJobDescription() + ";"
                + job.getJobInstruction() + ";"
                + jobDocumentsID + ";"
                + jobDocumentsPATH + ";"
                + numberOfTruck + ";"
                + job.getClose() + ";"//27
                + job.getExcavatorID().getTelephone() + ";"//28
                + job.getJobCreationType() + ";"//29
                + job.getBillingPrice() + ";"//30
                + job.getPaymenttypeID().getPaymenttypeID() + ";"
                + job.getPaymenttypeID().getLibel() + ";"
                + job.getExcavatorID().getCellPhone() + ";"//33
                + job.getTimezoneID() + ";"//34
                + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(job.getStartDate(), job.getTimezoneID()) + ";"//35
                + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(job.getEndDate(), job.getTimezoneID()) + ";"//36
                + job.getLatitude()+ ";"//37
                + job.getLongitude()+ ";"//38
                + job.getExcavatorID().getName()+ ";"//39
                + job.getExcavatorID().getSurname()+ ";"//40
                + job.getExcavatorID().getAccountID().getEmail()+ ";"//41
                + "null";

        result.setObject(res);
        result.afficherResult("getJobByID");
        return result;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getJobResponseByID(int jobResponseID) throws JobManagementException{

        String res = "";
        Result result = new Result();
        JobResponse jobResponse = null;
        result.setMsg("good");
        try {

            ges.creatEntityManager();
            jobResponse = (JobResponse) ges.getEntityManager().find(JobResponse.class, jobResponseID);

        } catch (Throwable th) {
            result.setMsg(th.getMessage());
        }

        if (jobResponse != null && !jobResponse.getDeleted()) {
            res = "" + jobResponse.getJobresponseID() + ";"
                    + jobResponse.getJobID().getJobID() + ";"
                    + jobResponse.getStartDate().getTime() + ";"
                    + jobResponse.getHourPerDay() + ";"
                    + jobResponse.getEndDate().getTime() + ";"
                    + jobResponse.getNumberOfTruck() + ";"
                    + jobResponse.getCreationDate().getTime() + ";"
                    + jobResponse.getTruckownerID().getUserID() + ";"
                    + jobResponse.getSubmitted() + ";"
                    + jobResponse.getBillingPrice() + ";"
                    + jobResponse.getTruckownerID().getTelephone() + ";"
                    + jobResponse.getTruckownerID().getCellPhone() + ";"
                    + jobResponse.getTruckownerID().getAddress() + ";"
                    + (jobResponse.getPaymenttypeID() == null ? -1 : jobResponse.getPaymenttypeID().getPaymenttypeID()) + ";"
                    + "null";

            result.setObject(res);
        } else {
            result.setObject("null");
        }
        return result;
    }

    @Override
    public Result deleteJob(int jobID, String deletedReason) throws JobManagementException{

        String res = "";
        Result result = new Result();
        Job job = null;
        List<String> listResult = new ArrayList<>();
        List<Validation> validationList;

        try {

            ges.creatEntityManager();
            job = (Job) ges.getEntityManager().find(Job.class, jobID, LockModeType.OPTIMISTIC_FORCE_INCREMENT);

            if (job == null) {
                ges.closeEm();
                result.setMsg("InvalidJobID\n");
                return result;
            }
            
            if (job.getDeleted()) {
                ges.closeEm();
                result.setMsg("AlReadyDeleted\n");
                return result;
            }
            
            job.setDeleted(true);
            job.setDeletedReason(deletedReason);
            ges.getEntityManager().merge(job);
            ges.closeEm();

            String excavatorInfo = "" + job.getExcavatorID().getAccountID().getEmail();
            excavatorInfo += ";" + job.getExcavatorID().getName();
            excavatorInfo += ";" + job.getExcavatorID().getSurname();
            excavatorInfo += ";" + job.getExcavatorID().getTelephone();
            excavatorInfo += ";" + job.getExcavatorID().getCellPhone();
            excavatorInfo += ";" + "null";
            
            //validationList = (List<Validation>)ges.getEntityManager().createQuery("SELECT v FROM Validation v WHERE v.deleted = FALSE AND v.clientValidation = 1 AND v.jobID = :jobID").setParameter("jobID", job).getResultList();
            validationList = (List<Validation>) ges.getEntityManager().createQuery("SELECT v FROM Validation v WHERE v.deleted = FALSE AND v.jobID = :jobID")
                    .setParameter("jobID", job).getResultList();

            for (Validation validation : validationList) {
                JobResponse jobResponse = validation.getJobresponseID();

                String truckOwnerInfo = "" + jobResponse.getTruckownerID().getAccountID().getEmail();
                truckOwnerInfo += ";" + jobResponse.getTruckownerID().getName();
                truckOwnerInfo += ";" + jobResponse.getTruckownerID().getSurname();
                truckOwnerInfo += ";" + jobResponse.getTruckownerID().getTelephone();
                truckOwnerInfo += ";" + jobResponse.getTruckownerID().getCellPhone();
                truckOwnerInfo += ";" + "null";

                String jobResponseInfos = "" + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(job.getStartDate(), job.getTimezoneID());//0
                jobResponseInfos += ";" + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(job.getEndDate(), job.getTimezoneID());
                jobResponseInfos += ";" + job.getJobLocation();
                jobResponseInfos += ";" + job.getJobNumber();//3
                jobResponseInfos += ";" + jobResponse.getHourPerDay();//4
                jobResponseInfos += ";" + jobResponse.getNumberOfTruck();
                jobResponseInfos += ";" + jobResponse.getJobID().getJobDescription();
                jobResponseInfos += ";" + jobResponse.getJobID().getJobInstruction();
                jobResponseInfos += ";" + jobResponse.getJobID().getDotNumber();
                jobResponseInfos += ";" + jobResponse.getJobID().getGeneralLiability();
                jobResponseInfos += ";" + jobResponse.getJobID().getTruckLiability();
                jobResponseInfos += ";" + jobResponse.getJobID().getProofOfTruckLiability();
                jobResponseInfos += ";" + (jobResponse.getJobID().getLenghtOfBed() == null ? "" : jobResponse.getJobID().getLenghtOfBed().getName());
                jobResponseInfos += ";" + (jobResponse.getJobID().getTruckAxle() == null ? "" : jobResponse.getJobID().getTruckAxle().getTruckAxleName());
                jobResponseInfos += ";" + jobResponse.getJobID().getJobCreationType();
                jobResponseInfos += ";" + "null";

                listResult.add(truckOwnerInfo + "##" + jobResponseInfos);
                if (validation.getClientValidation() == 0) {

                    jobResponse.setDeleted(true);
                    ges.getEntityManager().merge(jobResponse);

                    validation.setDeleted(true);
                    ges.getEntityManager().merge(validation);
                    ges.closeEm();

                }
            }
            
            result.setObject(excavatorInfo);
            result.setObjectList(listResult);
            res = "good";
            result.setMsg(res);
        } catch (Throwable th) {
            res = th.getMessage();
            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+res);
        }

        return result;
    }

    @Override
    public String closeJob(int jobID) throws JobManagementException{

        String res = "";
        Job job = null;
        try {

            ges.creatEntityManager();
            job = (Job) ges.getEntityManager().find(Job.class, jobID);

            if (job == null) {
                ges.closeEm();
                return "InvalidJobID\n";
            }

            if (job.getClose()) {
                ges.closeEm();
                return "AlReadyClose\n";
            }

            job.setClose(true);
            ges.getEntityManager().merge(job);
            ges.closeEm();
            res = "good";

        } catch (Throwable th) {
            res = th.getMessage();
             throw new JobManagementException(getClass()+"","editEmployeeInfo",1,th.getMessage(),th.getMessage());
        
        }

        return res;
    }

    @Override
    public Result deleteJobResponse(int jobResponseID) throws JobManagementException{

        String res = "";
        Result result = new Result();
        JobResponse jobResponse = null;
        try {

            ges.creatEntityManager();
            jobResponse = (JobResponse) ges.getEntityManager().find(JobResponse.class, jobResponseID);

            if (jobResponse == null) {
                ges.closeEm();
                result.setMsg("InvalidJobResponseID\n");
                return result;
            }

            if (jobResponse.getDeleted()) {
                ges.closeEm();
                result.setMsg("AlReadyDeleted\n");
                return result;
            }

            jobResponse.setDeleted(true);
            
            ges.getEntityManager().merge(jobResponse);
            Job job = jobResponse.getJobID();
            ges.getEntityManager().merge(job);
            ges.closeEm();

            for (SolicitedTruck solicitTruck : jobResponse.getSolicitedTruckList()) {
                solicitTruck.setDeleted(true);
                ges.getEntityManager().merge(solicitTruck);
                ges.closeEm();
            }

            res = "good";
            result.setMsg(res);

            String truckOwnerInfo = "" + jobResponse.getTruckownerID().getAccountID().getEmail();
            truckOwnerInfo += ";" + jobResponse.getTruckownerID().getName();
            truckOwnerInfo += ";" + jobResponse.getTruckownerID().getSurname();
            truckOwnerInfo += ";" + jobResponse.getTruckownerID().getTelephone();
            truckOwnerInfo += ";" + jobResponse.getTruckownerID().getCellPhone();
            truckOwnerInfo += ";" + "null";

            String excavatorInfo = "" + job.getExcavatorID().getAccountID().getEmail();
            excavatorInfo += ";" + job.getExcavatorID().getName();
            excavatorInfo += ";" + job.getExcavatorID().getSurname();
            excavatorInfo += ";" + job.getExcavatorID().getTelephone();
            excavatorInfo += ";" + job.getExcavatorID().getCellPhone();
            excavatorInfo += ";" + "null";

            String jobResponseInfos = "" + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(job.getStartDate(), job.getTimezoneID());
            jobResponseInfos += ";" + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(job.getEndDate(), job.getTimezoneID());
            jobResponseInfos += ";" + job.getJobLocation();
            jobResponseInfos += ";" + job.getJobNumber();//3
            jobResponseInfos += ";" + jobResponse.getHourPerDay();//4
            jobResponseInfos += ";" + jobResponse.getNumberOfTruck();
            jobResponseInfos += ";" + job.getJobDescription();
            jobResponseInfos += ";" + job.getJobInstruction();
            jobResponseInfos += ";" + job.getDotNumber();
            jobResponseInfos += ";" + job.getGeneralLiability();
            jobResponseInfos += ";" + job.getTruckLiability();
            jobResponseInfos += ";" + job.getProofOfTruckLiability();
            jobResponseInfos += ";" + (job.getLenghtOfBed() == null ? "" : job.getLenghtOfBed().getName());
            jobResponseInfos += ";" + (job.getTruckAxle() == null ? "" : job.getTruckAxle().getTruckAxleName());
            jobResponseInfos += ";" + job.getJobCreationType();
            jobResponseInfos += ";" + "null";

            result.setObject(excavatorInfo + "##" + truckOwnerInfo + "##" + jobResponseInfos);

        } catch (Throwable th) {
            res = th.getMessage();
            result.setMsg(res);
        }

        return result;
    }

    @Override
    public Result deleteJobResponseByTruckOwner(int jobResponseID, String deletedReason) throws JobManagementException{

        String res = "";
        Result result = new Result();
        JobResponse jobResponse = null;
        List<Validation> validationList;

        try {

            ges.creatEntityManager();
            jobResponse = (JobResponse) ges.getEntityManager().find(JobResponse.class, jobResponseID, LockModeType.OPTIMISTIC_FORCE_INCREMENT);

            if (jobResponse == null) {
                ges.closeEm();
                result.setMsg("InvalidJobResponseID\n");
                result.afficherResult("deleteJobResponseByTruckOwner");
                return result;
            }

            if (jobResponse.getDeleted()) {
                ges.closeEm();
                result.setMsg("AlReadyDeleted\n");
                result.afficherResult("deleteJobResponseByTruckOwner");
                return result;
            }

            jobResponse.setDeleted(true);
            jobResponse.setDeletedReason(deletedReason);
            ges.getEntityManager().merge(jobResponse);
            ges.closeEm();

            for (SolicitedTruck solicitTruck : jobResponse.getSolicitedTruckList()) {
                solicitTruck.setDeleted(true);
                ges.getEntityManager().merge(solicitTruck);
                ges.closeEm();
            }

            validationList = (List<Validation>) ges.getEntityManager().createQuery("SELECT v FROM Validation v WHERE v.deleted = FALSE AND v.clientValidation = 1 AND v.jobresponseID = :jobResponseID").setParameter("jobResponseID", jobResponse).getResultList();

            //Le couple (job, jobResponse) est unique dans la table validation.
            if (validationList != null && !validationList.isEmpty()) {
                Validation validation = validationList.get(0);
                Job job = validation.getJobID();

                if (job.getClose()) {
                    job.setClose(false);
                    ges.getEntityManager().merge(job);
                }

                String excavatorInfo = "" + job.getExcavatorID().getAccountID().getEmail();
                excavatorInfo += ";" + job.getExcavatorID().getName();
                excavatorInfo += ";" + job.getExcavatorID().getSurname();
                excavatorInfo += ";" + jobResponse.getJobID().getExcavatorID().getTelephone();
                excavatorInfo += ";" + jobResponse.getJobID().getExcavatorID().getCellPhone();
                excavatorInfo += ";" + "null";

                String truckOwnerInfo = "" + jobResponse.getTruckownerID().getAccountID().getEmail();
                truckOwnerInfo += ";" + jobResponse.getTruckownerID().getName();
                truckOwnerInfo += ";" + jobResponse.getTruckownerID().getSurname();
                truckOwnerInfo += ";" + jobResponse.getTruckownerID().getTelephone();
                truckOwnerInfo += ";" + jobResponse.getTruckownerID().getCellPhone();
                truckOwnerInfo += ";" + "null";

                String jobResponseInfos = "" + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(jobResponse.getStartDate(), job.getTimezoneID());
                jobResponseInfos += ";" + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(jobResponse.getEndDate(), job.getTimezoneID());
                jobResponseInfos += ";" + jobResponse.getJobID().getJobLocation();
                jobResponseInfos += ";" + jobResponse.getJobID().getJobNumber();
                jobResponseInfos += ";" + jobResponse.getHourPerDay();//4
                jobResponseInfos += ";" + jobResponse.getNumberOfTruck();
                jobResponseInfos += ";" + jobResponse.getJobID().getJobDescription();
                jobResponseInfos += ";" + jobResponse.getJobID().getJobInstruction();
                jobResponseInfos += ";" + jobResponse.getJobID().getDotNumber();
                jobResponseInfos += ";" + jobResponse.getJobID().getGeneralLiability();
                jobResponseInfos += ";" + jobResponse.getJobID().getTruckLiability();
                jobResponseInfos += ";" + jobResponse.getJobID().getProofOfTruckLiability();
                jobResponseInfos += ";" + (jobResponse.getJobID().getLenghtOfBed() == null ? "" : jobResponse.getJobID().getLenghtOfBed().getName());
                jobResponseInfos += ";" + (jobResponse.getJobID().getTruckAxle() == null ? "" : jobResponse.getJobID().getTruckAxle().getTruckAxleName());
                jobResponseInfos += ";" + jobResponse.getJobID().getJobCreationType();
                jobResponseInfos += ";" + "null";

                result.setObject(excavatorInfo + "##" + truckOwnerInfo + "##" + jobResponseInfos);

                validation.setDeleted(true);
                ges.getEntityManager().merge(validation);
                ges.closeEm();
            } else {
            }

            res = "good";
            result.setMsg(res);

        } catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            result.setMsg(res);
        }

        result.afficherResult("deleteJobResponseByTruckOwner");
        return result;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfJobByTruckOwner(int truckOwnerID, int index, int nombreMaxResult) throws JobManagementException{

        String res;
        Result result = new Result();
        List<Job> jobs = null;
        int numberOfElts = 0;
        Account account;
        List<String> listResult = new ArrayList<>();
        result.setMsg("good");
        try {

            ges.creatEntityManager();
            account = (Account) ges.getEntityManager().find(Account.class, truckOwnerID);

            if (account == null) {
                result.setMsg("InvaLidTruckOwnerID");
                return result;
            }

            Date currentDate = DateFunction.getGMTDate();
            Query query = ges.getEntityManager().createQuery("SELECT a FROM  Job a WHERE a.deleted = FALSE AND a.close = FALSE AND a.jobCreationType = 0 AND a.endDate > :currentDate ORDER BY a.creationDate DESC");

            query.setParameter("currentDate", currentDate);
            numberOfElts = query.getResultList().size();
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResult);

            jobs = query.getResultList();

        } catch (Throwable th) {
            result.setMsg(th.getMessage());
        }

        if (jobs != null && !jobs.isEmpty()) {

            for (Job job : jobs) {

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
                long numberOfTruck = jf.jobRemainingTruck(job, ges);

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
                        + (job.getTypeofdirtID() == null ? "" : job.getTypeofdirtID().getLabel()) + ";"
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
                        + numberOfTruck + ";"
                        + job.getExcavatorID().getTelephone() + ";"
                        + job.getJobCreationType() + ";"//30
                        + job.getBillingPrice()+ ";"
                        + job.getExcavatorID().getCellPhone()+ ";"//32
                        + job.getTimezoneID()+ ";"//33
                        + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(job.getStartDate(), job.getTimezoneID()) + ";"//34
                        + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(job.getEndDate(), job.getTimezoneID()) + ";"//35
                        + "null";

                listResult.add(res);
            }
        }

        listResult.add("" + numberOfElts);
        result.setObjectList(listResult);
        result.afficherResult("getARangeOfJobByTruckOwner");
        return result;
    }

    @Override
    public Result validateAJobByTruckOwner(int accountID, int jobResponseID, float price, int paymentTypeID) throws JobManagementException{

        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"validateAJobByTruckOwner:  userID = " + accountID + ",  jobResponseID = " + jobResponseID );
        Result result = new Result();
        Account account;
        JobResponse jobResponse;
        Job job;
        PaymentType paymentType;
        Validation validation = null;
        List<SolicitedTruck> solicitedTruckList = null;
        List<SolicitedTruck> solicitedTruckListUnAvail = null;
        String truckNumbers = "";

        try {

            ges.creatEntityManager();

            jobResponse = (JobResponse) ges.getEntityManager().find(JobResponse.class, jobResponseID, LockModeType.PESSIMISTIC_FORCE_INCREMENT);
            account = (Account) ges.getEntityManager().find(Account.class, accountID);
            paymentType = (PaymentType) ges.getEntityManager().find(PaymentType.class, paymentTypeID);
         
            if (jobResponse == null) {
                ges.closeEm();
                result.setMsg("InvalidJobResponseID");
                result.afficherResult("validateAJobByTruckOwner");
                return result;
            }

            if (account == null) {
                ges.closeEm();
                result.setMsg("InvalidAccountID");
                result.afficherResult("validateAJobByTruckOwner");
                return result;
            }

            if (paymentType == null) {
                ges.closeEm();
                result.setMsg("InvalidPaymentTypeID\n");
                result.afficherResult("validateAJobByTruckOwner");
                return result;
            }
            
            job = jobResponse.getJobID();

            Query query = ges.getEntityManager().createQuery("SELECT v FROM  Validation v WHERE v.deleted = FALSE AND v.jobID = :jobID AND v.jobresponseID = :jobResponseID ")
                    .setParameter("jobID", job)
                    .setParameter("jobResponseID", jobResponse)
                    .setLockMode(LockModeType.PESSIMISTIC_FORCE_INCREMENT);

            validation = (Validation) query.getSingleResult();

            if (validation.getTruckOwnerValidation() == 1) {
                ges.closeEm();
                result.setMsg("AllReadyValidate;" + validation.getTruckOwnerValidation() 
                        + ";" + validation.getTruckOwnerValidationDate());
                result.afficherResult("validateAJobByTruckOwner");
                return result;
            }

            //Avant la validation, on verification du nombre de camion restant et fermeture eventuelle du job
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"Verification du nombre de truck requis ...");
            JobFonction jf = new JobFonction();
            ges.getEntityManager().lock(job, LockModeType.PESSIMISTIC_FORCE_INCREMENT);
            long numberOfTruck = jf.jobRemainingTruck(job, ges);

            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"le nombre de truck restant du job = " + numberOfTruck);
            if ((numberOfTruck == jobResponse.getNumberOfTruck())) {
                String resul = this.closeJob(job.getJobID());
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"Resultat de la fermeture du job = " + resul);
            } else if ((jobResponse.getNumberOfTruck() > numberOfTruck)) {
                result.setMsg("tooMuchTruck");
                logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"-------------- There is too much truck  ------");
                result.afficherResult("validateAJobByTruckOwner");
                return result;
            }

            solicitedTruckList = ges.getEntityManager().createQuery("SELECT s FROM SolicitedTruck s WHERE s.deleted = FALSE AND s.jobresponseID = :jobresponseID")
                    .setParameter("jobresponseID", jobResponse)
                    .getResultList();

            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"validateAJobByTruckOwner: solicitedTruckList = " + solicitedTruckList);
            solicitedTruckListUnAvail = new ArrayList<>();

            //On recuperère tous les solicitedTrucks qui ont été validé et qui chevauchent le truck en cour de validation
            for (SolicitedTruck solicitedTruck : solicitedTruckList) {

                //s.endDate < :startDate 
                //                       OR      ==============> truck libre
                //s.startDate > :endDate
                
                Query myQuery = ges.getEntityManager().createQuery("SELECT s FROM SolicitedTruck s WHERE s.deleted = FALSE AND s.truckAvailable = TRUE AND s.truckID = :truckID AND s.jobresponseID != :jobresponseID AND s.startDate <= :endDate AND s.endDate >= :startDate ")
                        .setParameter("startDate", solicitedTruck.getStartDate())
                        .setParameter("endDate", solicitedTruck.getEndDate())
                        .setParameter("truckID", solicitedTruck.getTruckID())
                        .setParameter("jobresponseID", jobResponse)
                        ;
                
                solicitedTruckListUnAvail.addAll(myQuery.getResultList());

                if (!solicitedTruckListUnAvail.isEmpty()) {
                    logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+" solicitedTruckListUnAvail = " + solicitedTruckListUnAvail);
                    result.setMsg("unAvailableTruck");
                    result.afficherResult("validateAJobByTruckOwner");
                    return result;
                } else {
                    truckNumbers += ", " + solicitedTruck.getTruckID().getTruckNumber();
                }
            }

            jobResponse.setBillingPrice(price);
            jobResponse.setPaymenttypeID(paymentType);
            validation.setTruckOwnerValidation(1);
            validation.setTruckOwnerValidationDate(DateFunction.getGMTDate());
            ges.getEntityManager().merge(validation);
            ges.getEntityManager().merge(jobResponse);

            ges.getEntityManager().merge(jobResponse);
            ges.getEntityManager().merge(job);
            ges.getEntityManager().merge(account.getUser());
            ges.closeEm();

            String truckOwnerInfo = "" + jobResponse.getTruckownerID().getAccountID().getEmail();
            truckOwnerInfo += ";" + jobResponse.getTruckownerID().getName();
            truckOwnerInfo += ";" + jobResponse.getTruckownerID().getSurname();
            truckOwnerInfo += ";" + jobResponse.getTruckownerID().getTelephone();
            truckOwnerInfo += ";" + jobResponse.getTruckownerID().getCellPhone();
            truckOwnerInfo += ";" + "null";

            String excavatorInfo = "" + job.getExcavatorID().getAccountID().getEmail();
            excavatorInfo += ";" + job.getExcavatorID().getName();
            excavatorInfo += ";" + job.getExcavatorID().getSurname();
            excavatorInfo += ";" + job.getExcavatorID().getTelephone();
            excavatorInfo += ";" + job.getExcavatorID().getCellPhone();
            excavatorInfo += ";" + "null";

            String jobResponseInfos = "" + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(job.getStartDate(), job.getTimezoneID());
            jobResponseInfos += ";" + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(job.getEndDate(), job.getTimezoneID());
            jobResponseInfos += ";" + job.getJobLocation();
            jobResponseInfos += ";" + job.getJobNumber();//3
            jobResponseInfos += ";" + jobResponse.getHourPerDay();//4
            jobResponseInfos += ";" + jobResponse.getNumberOfTruck();
            jobResponseInfos += ";" + job.getJobDescription();
            jobResponseInfos += ";" + job.getJobInstruction();
            jobResponseInfos += ";" + job.getDotNumber();
            jobResponseInfos += ";" + job.getGeneralLiability();
            jobResponseInfos += ";" + job.getTruckLiability();
            jobResponseInfos += ";" + job.getProofOfTruckLiability();
            jobResponseInfos += ";" + (job.getLenghtOfBed() == null ? "" : job.getLenghtOfBed().getName());
            jobResponseInfos += ";" + (job.getTruckAxle() == null ? "" : job.getTruckAxle().getTruckAxleName());
            jobResponseInfos += ";" + job.getJobCreationType();
            jobResponseInfos += ";" + "null";

            result.setObject(excavatorInfo + "##" + truckOwnerInfo + "##" + jobResponseInfos + "##" + truckNumbers.substring(1));
            result.setMsg("good;" + numberOfTruck);

        } catch (OptimisticLockException th) {
            throw new OptimisticLockException(th);
        } catch (PessimisticLockException th) {
            throw new PessimisticLockException(th);
        } catch (Throwable th) {

            if (validation == null) {
                ges.closeEm();
                result.setMsg("NotExitingValidation");
                result.afficherResult("validateAJobByTruckOwner");
                return result;
            }
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            String res = string.toString();
            result.setMsg("validationError\n" + th.getMessage() + res);
            result.afficherResult("validateAJobByTruckOwner");
            return result;
        }
        
        result.afficherResult("validateAJobByTruckOwner");
        return result;
    }

    
    @Override
    public Result validateAJobByExcavator(int accountID, int jobResponseID, List<String> truckIDList) throws JobManagementException{

        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"validateAJobByExcavator:  accountID = " + accountID + ",  jobResponseID = " + jobResponseID  + ",  truckIDList = " + truckIDList );
        Result result = new Result();
        Account account;
        JobResponse jobResponse;
        Job job;
        Validation validation = null;
        String truckNumbers = "";

        try {

            ges.creatEntityManager();

            jobResponse = (JobResponse) ges.getEntityManager().find(JobResponse.class, jobResponseID, LockModeType.PESSIMISTIC_FORCE_INCREMENT);
            account = (Account) ges.getEntityManager().find(Account.class, accountID);
            
            if (jobResponse == null || jobResponse.getDeleted()) {
                ges.closeEm();
                result.setMsg("InvalidJobResponseID");
                result.afficherResult("validateAJobByExcavator");
                return result;
            }

            if (account == null || account.getDeleted()) {
                ges.closeEm();
                result.setMsg("InvalidAccountID");
                result.afficherResult("validateAJobByExcavator");
                return result;
            }
            
            //job = (Job) ges.getEntityManager().find(Job.class, jobResponse.getJobID(), LockModeType.PESSIMISTIC_FORCE_INCREMENT);
            job = jobResponse.getJobID();
            ges.getEntityManager().lock(job, LockModeType.PESSIMISTIC_FORCE_INCREMENT);

            Query query = ges.getEntityManager().createQuery("SELECT v FROM  Validation v WHERE v.deleted = FALSE AND v.jobID = :jobID AND v.jobresponseID = :jobResponseID ")
                    .setParameter("jobID", job)
                    .setParameter("jobResponseID", jobResponse)
                    .setLockMode(LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            
            validation = (Validation) query.getSingleResult();
            
            if (validation.getTruckOwnerValidation() != 1) {
                ges.closeEm();
                result.setMsg("NotValidatedByTruckOwner");
                result.afficherResult("validateAJobByExcavator");
                return result;
            }
            
            if (truckIDList.contains("-1")) {
                truckIDList = (List<String>) ges.getEntityManager().createQuery("SELECT DISTINCT s.truckID.truckID FROM SolicitedTruck s WHERE s.deleted = FALSE AND s.truckAvailable = FALSE AND s.jobresponseID = :jobresponseID")
                        .setParameter("jobresponseID", jobResponse)
                        .getResultList();
            }
            
            if (truckIDList == null || truckIDList.isEmpty()) {
                result.setMsg("NoTruckAvailable");
                result.afficherResult("validateAJobByExcavator");
                return result;
            }
            
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"validateAJobByExcavator: Liste des trucks à proposé:  " + truckIDList);
            
            //Avant la validation, on verification du nombre de camion restant et fermeture eventuelle du job
            JobFonction jf = new JobFonction();
            long numberOfRemainingTruck = jf.jobRemainingTruck(job, ges);

            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"validateAJobByExcavator: le nombre de truck restant du job = " + numberOfRemainingTruck);
            
            if (numberOfRemainingTruck == 0) {
                result.setMsg("tooMuchTruck");
                result.afficherResult("validateAJobByExcavator");
                return result;
            }
            
            if (validation.getClientValidation() == 1 && numberOfRemainingTruck == 0) {
                ges.closeEm();
                result.setMsg("AllReadyValidate;" + validation.getClientValidation() + ";" + validation.getClientValidationDate());
                result.afficherResult("validateAJobByExcavator");
                return result;
            }

            List<String> validatedTruckList = new ArrayList<>();
            //On recuperère tous les solicitedTrucks qui ont été validé et qui chevauchent le truck en cour de validation
            for (Object truckID : truckIDList) {

                SolicitedTruck solicitedTruck = (SolicitedTruck) ges.getEntityManager().createQuery("SELECT s FROM SolicitedTruck s WHERE s.deleted = FALSE AND s.truckID.truckID = :truckID AND s.jobresponseID = :jobresponseID")
                        .setParameter("truckID", Integer.parseInt(String.valueOf(truckID)))
                        .setParameter("jobresponseID", jobResponse)
                        .getSingleResult();
                
                if (solicitedTruck.getTruckAvailable()) {
                    result.setMsg("unAvailableTruck;" + solicitedTruck.getTruckID().getTruckID() + ";" + solicitedTruck.getTruckID().getTruckNumber());
                    result.afficherResult("validateAJobByExcavator");
                    ges.rollbackTransaction();
                }
                //s.endDate < :startDate 
                //                       OR      ==============> truck libre
                //s.startDate > :endDate
                
                List<SolicitedTruck> solicitedTruckListUnAvail = ges.getEntityManager().createQuery("SELECT s FROM SolicitedTruck s WHERE s.deleted = FALSE AND s.truckAvailable = TRUE AND s.truckID.truckID = :truckID AND s != :solicitedTruck  AND s.startDate <= :endDate AND s.endDate >= :startDate")
                        .setParameter("startDate", solicitedTruck.getStartDate())
                        .setParameter("endDate", solicitedTruck.getEndDate())
                        .setParameter("truckID", Integer.parseInt(String.valueOf(truckID)))
                        .setParameter("solicitedTruck", solicitedTruck)
                        .getResultList();

                //Si le camion proposé est occupé pour un autre job, on annule la transaction
                if (!solicitedTruckListUnAvail.isEmpty()) {
                    logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+" solicitedTruckListUnAvail = " + solicitedTruckListUnAvail + "  --  truckID = " + truckID);
                    result.setMsg("unAvailableTruck;" + solicitedTruck.getTruckID().getTruckID() + ";" + solicitedTruck.getTruckID().getTruckNumber());
                    result.afficherResult("validateAJobByExcavator");
                    ges.rollbackTransaction();
                } else {
                    truckNumbers += ", " + solicitedTruck.getTruckID().getTruckNumber();
                    solicitedTruck.setTruckAvailable(true);
                    ges.getEntityManager().merge(solicitedTruck);
                    ges.closeEm();
                    validatedTruckList.add("" + truckID);
                }
                
                //Si on a proposé plus de Camion que ce qu'il faillait alors, on annule la transaction
                if (validatedTruckList.size() > numberOfRemainingTruck) {
                    result.setMsg("tooMuchTruck");
                    result.afficherResult("validateAJobByExcavator");
                    ges.rollbackTransaction();
                }
            }
            
            if ((numberOfRemainingTruck == validatedTruckList.size())) {
                job.setClose(true);
            }
            
            //if (validateTruckList.size() == jobResponse.getNumberOfTruck()) {
                validation.setClientValidation(1);
            //}
            
            validation.setClientValidationDate(DateFunction.getGMTDate());
            ges.getEntityManager().merge(validation);
            ges.getEntityManager().merge(jobResponse);
            ges.getEntityManager().merge(job);
            ges.getEntityManager().merge(account.getUser());
            ges.closeEm();

            String truckOwnerInfo = "" + jobResponse.getTruckownerID().getAccountID().getEmail();
            truckOwnerInfo += ";" + jobResponse.getTruckownerID().getName();
            truckOwnerInfo += ";" + jobResponse.getTruckownerID().getSurname();
            truckOwnerInfo += ";" + jobResponse.getTruckownerID().getTelephone();
            truckOwnerInfo += ";" + jobResponse.getTruckownerID().getCellPhone();
            truckOwnerInfo += ";" + "null";

            String excavatorInfo = "" + jobResponse.getJobID().getExcavatorID().getAccountID().getEmail();
            excavatorInfo += ";" + jobResponse.getJobID().getExcavatorID().getName();
            excavatorInfo += ";" + jobResponse.getJobID().getExcavatorID().getSurname();
            excavatorInfo += ";" + jobResponse.getJobID().getExcavatorID().getTelephone();
            excavatorInfo += ";" + jobResponse.getJobID().getExcavatorID().getCellPhone();
            excavatorInfo += ";" + "null";

            String timeZoneID = jobResponse.getJobID().getTimezoneID();
            String jobResponseInfos = "" + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(jobResponse.getStartDate(), timeZoneID);
            jobResponseInfos += ";" + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(jobResponse.getEndDate(), timeZoneID);
            jobResponseInfos += ";" + jobResponse.getJobID().getJobLocation();
            jobResponseInfos += ";" + jobResponse.getJobID().getJobNumber();//3
            jobResponseInfos += ";" + jobResponse.getHourPerDay();//4
            jobResponseInfos += ";" + validatedTruckList.size();//5
            jobResponseInfos += ";" + jobResponse.getJobID().getJobDescription();
            jobResponseInfos += ";" + jobResponse.getJobID().getJobInstruction();
            jobResponseInfos += ";" + jobResponse.getJobID().getDotNumber();
            jobResponseInfos += ";" + jobResponse.getJobID().getGeneralLiability();
            jobResponseInfos += ";" + jobResponse.getJobID().getTruckLiability();//10
            jobResponseInfos += ";" + jobResponse.getJobID().getProofOfTruckLiability();
            jobResponseInfos += ";" + (jobResponse.getJobID().getLenghtOfBed() == null ? "" : jobResponse.getJobID().getLenghtOfBed().getName());
            jobResponseInfos += ";" + (jobResponse.getJobID().getTruckAxle() == null ? "" : jobResponse.getJobID().getTruckAxle().getTruckAxleName());
            jobResponseInfos += ";" + jobResponse.getJobID().getJobCreationType();
            jobResponseInfos += ";" + (jobResponse.getTimeZone() < 0 ? "":"+") + jobResponse.getTimeZone()/(3600000);//15
            jobResponseInfos += ";" + "null";

            result.setObject(excavatorInfo + "##" + truckOwnerInfo + "##" + jobResponseInfos + "##" + truckNumbers.substring(1));
            result.setMsg("good");

        } catch (OptimisticLockException th) {
            throw new OptimisticLockException(th);
        } catch (Throwable th) {

            if (validation == null) {
                ges.closeEm();
                result.setMsg("NotExitingValidation");
                result.afficherResult("validateAJobByTruckOwner");
                return result;
            }
            if ((result.getMsg() == null || !result.getMsg().contains("unAvailableTruck") 
                    && !result.getMsg().contains("tooMuchTruck") && !result.getMsg().contains("ValidatedTruck"))) {
                StringWriter string = new StringWriter();
                PrintWriter str = new PrintWriter(string);
                th.printStackTrace(str);
                String res = string.toString();
                result.setMsg("validationError;" + th.getMessage() + res);
            }
            
            result.afficherResult("validateAJobByExcavator");
            return result;
        }

        result.afficherResult("validateAJobByExcavator");
        return result;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfValidJobResponseTruckOwner(int truckOwnerID, boolean includDeletedJob, int index, int nombreMaxResult) throws JobManagementException{

        String res;
        Result result = new Result();
        List<JobResponse> jobResponses = null;
        int numberOfElts = 0;
        Account account;
        List<String> listResult = new ArrayList<>();
        result.setMsg("good");
        try {

            ges.creatEntityManager();
            account = (Account) ges.getEntityManager().find(Account.class, truckOwnerID);

            if (account == null) {
                result.setMsg("InvaLidTruckOwnerID");
                return result;
            }

            Query query;
            
            /* IncludeDeletedJob pour des besoin d'historique*/
            if (!includDeletedJob) {
                query = ges.getEntityManager().createQuery("SELECT a FROM JobResponse a WHERE a.deleted = FALSE AND a.truckownerID = :truckOwnerID AND a IN (SELECT DISTINCT b.jobresponseID FROM Validation b WHERE b.deleted = FALSE AND b.clientValidation = 1 AND b.truckOwnerValidation = 1   ) ORDER BY a.creationDate DESC");
            } else {
                query = ges.getEntityManager().createQuery("SELECT a FROM JobResponse a WHERE a.deleted = FALSE AND a.truckownerID = :truckOwnerID AND a IN (SELECT DISTINCT b.jobresponseID FROM Validation b WHERE b.deleted = FALSE AND b.jobID.deleted = FALSE AND b.clientValidation = 1 AND b.truckOwnerValidation = 1   ) ORDER BY a.creationDate DESC");
            }

            query.setParameter("truckOwnerID", account.getUser());
            numberOfElts = query.getResultList().size();
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResult);

            jobResponses = query.getResultList();

        } catch (Throwable th) {
            result.setMsg(th.getMessage());
        }

        if (jobResponses != null && !jobResponses.isEmpty()) {

            for (JobResponse jobResponse : jobResponses) {

                String reviewComment = jobResponse.getReviewComment();
                Date jobResponseStartDate = jobResponse.getStartDate();
                Date jobResponseEndDate = jobResponse.getEndDate();
                long jobResponseStartTime = jobResponseStartDate.getTime();
                long jobResponseEndTime = jobResponseEndDate.getTime();

                res = "" + jobResponse.getJobresponseID() + ";"//0
                        + jobResponse.getJobID().getJobID() + ";"
                        + jobResponseStartTime + ";"
                        + jobResponseEndTime + ";"
                        + jobResponse.getHourPerDay() + ";"
                        + jobResponse.getNumberOfTruck() + ";"//5
                        + jobResponse.getJobID().getJobLocation() + ";"
                        + jobResponse.getJobID().getJobNumber() + ";"	
                        + jobResponse.getCreationDate().getTime() + ";"
                        + jobResponse.getDeleted() + ";"
                        + jobResponse.getJobID().getExcavatorID().getName() + ";"//10
                        + jobResponse.getJobID().getExcavatorID().getSurname() + ";"
                        + jobResponse.getJobID().getExcavatorID().getUserID() + ";"
                        + jobResponse.getJobID().getClose() + ";"
                        + jobResponse.getJobID().getExcavatorID().getAccountID().getEmail() + ";"
                        + jobResponse.getSubmitted() + ";"//15
                        + jobResponse.getBillingPrice() + ";"
                        + jobResponse.getSubmitReview() + ";"
                        + (reviewComment == null ? "" : reviewComment.replaceAll(";", "%%%")) + ";"
                        + jobResponse.getTruckownerID().getTelephone() + ";"
                        + jobResponse.getTruckownerID().getCellPhone() + ";"//20
                        + jobResponse.getTruckownerID().getAddress() + ";"
                        + (jobResponse.getPaymenttypeID() == null ? -1 : jobResponse.getPaymenttypeID().getPaymenttypeID()) + ";"
                        + jobResponse.getJobID().getExcavatorID().getTelephone()+";"
                        + jobResponse.getJobID().getExcavatorID().getCellPhone()+";"//24
                        + (jobResponse.getJobID().getLenghtOfBed() == null ? "" : jobResponse.getJobID().getLenghtOfBed().getLenghtofbedID()) + ";"
                        + (jobResponse.getJobID().getTruckAxle() == null ? "" : jobResponse.getJobID().getTruckAxle().getTruckaxleID()) + ";"
                        + jobResponse.getJobID().getDotNumber() + ";"//27
                        + jobResponse.getJobID().getTruckLiability()+ ";"
                        + jobResponse.getJobID().getGeneralLiability()+ ";"
                        + jobResponse.getJobID().getTimeSheet()+ ";"//30
                        + jobResponse.getJobID().getJobDescription()+ ";"
                        + jobResponse.getJobID().getJobInstruction()+ ";"
                        + jobResponse.getJobID().getExcavatorID().getTelephone()+ ";"
                        + jobResponse.getJobID().getProofOfTruckLiability()+ ";"
                        + jobResponse.getJobID().getDirectDeposit()+ ";"//35
                        + jobResponse.getJobID().getRate()+ ";"
                        + jobResponse.getJobID().getNumberOfTruck()+ ";"
                        + (jobResponse.getJobID().getTypeofdirtID() == null ? "" : jobResponse.getJobID().getTypeofdirtID().getLabel()) + ";"//38
                        + (jobResponseStartTime + jobResponse.getTimeZone()) + ";"//39
                        + (jobResponseEndTime + jobResponse.getTimeZone()) + ";"//40
                        + jobResponse.getJobID().getTimezoneID()+ ";"//41
                        + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(jobResponseStartDate, jobResponse.getJobID().getTimezoneID()) + ";"//42
                        + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(jobResponseEndDate, jobResponse.getJobID().getTimezoneID()) + ";"//43
                        + jobResponse.getLastEditEndDate().before(jobResponse.getJobID().getLastEditEndDate()) + ";"//44
                        + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(jobResponse.getJobID().getEndDate(), jobResponse.getJobID().getTimezoneID()) + ";"//45
                        + "null";
                
                listResult.add(res);
            }
        }

        listResult.add("" + numberOfElts);
        result.setObjectList(listResult);
        result.afficherResult("getARangeOfValidJobResponseTruckOwner");
        return result;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfCloseJobExcavator(int excavatorID, int index, int nombreMaxResult) throws JobManagementException{

        String res;
        Result result = new Result();
        List<Job> jobs = null;
        int numberOfElts = 0;
        Account account;
        List<String> listResult = new ArrayList<>();
        result.setMsg("good");
        try {

            ges.creatEntityManager();
            account = (Account) ges.getEntityManager().find(Account.class, excavatorID);

            if (account == null) {
                result.setMsg("InvaLidExcavatorID");
                return result;
            }

            Query query = ges.getEntityManager().createQuery("SELECT a FROM  Job a WHERE a.deleted = FALSE AND a.excavatorID =:excavatorID AND a.close = TRUE ORDER BY a.creationDate DESC");

            query.setParameter("excavatorID", account.getUser());
            numberOfElts = query.getResultList().size();
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResult);

            jobs = query.getResultList();

        } catch (Throwable th) {
            result.setMsg(th.getMessage());
        }

        if (jobs != null && !jobs.isEmpty()) {

            for (Job job : jobs) {

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
                long numberOfTruck = jf.jobRemainingTruck(job, ges);

                res = "" + job.getJobID() + ";"//0
                        + job.getStartDate().getTime() + ";"
                        + job.getEndDate().getTime() + ";"
                        + job.getHourPerDay() + ";"
                        + job.getNumberOfTruck() + ";"
                        + job.getCompanyRate() + ";"//5
                        + (job.getExcavatorID().getRate()) + ";"

                        + (job.getLenghtOfBed() == null ? "" : job.getLenghtOfBed().getLenghtofbedID()) + ";"
                        + (job.getTruckAxle() == null ? "" : job.getTruckAxle().getTruckaxleID()) + ";"
                        + job.getJobNumber() + ";"
                        + job.getJobLocation() + ";"
                        + (job.getTypeofdirtID() == null ? "" : job.getTypeofdirtID().getLabel()) + ";"
                        + job.getWeight() + ";"//12
                        + job.getStartTime() + ";"
                        + job.getDotNumber() + ";"
                        + job.getGeneralLiability() + ";"
                        + job.getTruckLiability() + ";"
                        + job.getProofOfTruckLiability() + ";"
                        + job.getDirectDeposit() + ";"
                        + job.getTimeSheet() + ";"
                        + job.getAutomatedBooking() + ";"//20
                        + job.getJobDescription() + ";"
                        + job.getJobInstruction() + ";"//22
                        + jobDocumentsID + ";"
                        + jobDocumentsPATH + ";"
                        + numberOfTruck + ";"
                        + job.getExcavatorID().getTelephone() + ";"//26                        
                        + job.getExcavatorID().getCellPhone()+ ";"//27
                        + (job.getNumberOfTruck() - numberOfTruck) + ";"//28
                        + job.getTimezoneID() + ";"//29
                        + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(job.getStartDate(), job.getTimezoneID()) + ";"//30
                        + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(job.getEndDate(), job.getTimezoneID()) + ";"//31
                        + "null";

                listResult.add(res);
            }
        }

        listResult.add("" + numberOfElts);
        result.setObjectList(listResult);
        return result;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getJobNumberByExcavator(int excavatorID) throws JobManagementException{

        Account account = null;
        Result result = new Result();
        result.setMsg("good");

        try {
            ges.creatEntityManager();
            account = (Account) ges.getEntityManager().find(Account.class, excavatorID);

        } catch (Throwable e) {
        }
        if (account == null) {
            result.setMsg("InvalidAccountID");
            return result;
        }

        result.setObject("" + (account.getUser().getJobNumber() + 1));
        return result;

    }

    @Override
    public String submitJob(int jobResponseID)throws JobManagementException {

        String res = "";
        JobResponse jobResponse = null;
        try {

            ges.creatEntityManager();
            jobResponse = (JobResponse) ges.getEntityManager().find(JobResponse.class, jobResponseID);

            if (jobResponse == null) {
                ges.closeEm();
                return "InvalidJobLogID\n";
            }

            if (jobResponse.getJobLogList() != null && !jobResponse.getJobLogList().isEmpty()) {

                for (JobLog jobLog : jobResponse.getJobLogList()) {

                    jobLog.setClosed(true);
                    ges.getEntityManager().merge(jobResponse);
                    ges.closeEm();
                }
                jobResponse.setSubmitted(true);
                ges.getEntityManager().merge(jobResponse);
                ges.closeEm();
                res = "good";
            } else {
                res = "NotExistingJobLogForJobResponse";
            }

        } catch (Throwable th) {
            res = th.getMessage();
             throw new JobManagementException(getClass()+"","editEmployeeInfo",1,th.getMessage(),th.getMessage());
        
        }

        return res;
    }

    @Override
    public Result submitJobLogExt(int jobResponseID) throws JobManagementException{

        String res;
        Result result = new Result();
        List<JobLog> jobLogs = null;
        String InfoExcavator = "";
        String InfoTruckOwner = "";
        List<String> listResult = new ArrayList<>();
        JobResponse jobResponse;
        result.setMsg("unknown");
        try {

            ges.creatEntityManager();
            jobResponse = (JobResponse) ges.getEntityManager().find(JobResponse.class, jobResponseID);

            if (jobResponse == null) {
                result.setMsg("InvaLidJobResponseID");
                return result;
            }

            if (jobResponse.getSubmitted()) {
                result.setMsg("AlReadySubmitted");
                return result;
            }

            Query query = ges.getEntityManager().createQuery("SELECT a FROM JobLog a WHERE a.deleted = false AND a.closed = FALSE AND a.jobresponseID = :jobresponseID  ");
            query.setParameter("jobresponseID", jobResponse);
            jobLogs = query.getResultList();

            if (jobLogs != null && !jobLogs.isEmpty()) {

                jobResponse.setSubmitted(true);
                ges.getEntityManager().merge(jobResponse);
                ges.closeEm();

                InfoExcavator = "" + jobResponse.getJobID().getExcavatorID().getUserID();
                InfoExcavator += ";" + jobResponse.getJobID().getExcavatorID().getAccountID().getEmail();
                InfoExcavator += ";" + jobResponse.getJobID().getExcavatorID().getName();
                InfoExcavator += ";" + jobResponse.getJobID().getExcavatorID().getSurname();

                InfoTruckOwner = "" + jobResponse.getTruckownerID().getName();
                InfoTruckOwner += ";" + jobResponse.getTruckownerID().getSurname();
                InfoTruckOwner += ";" + jobResponse.getJobID().getJobNumber();
                
                result.setMsg("good");
                listResult.add(InfoExcavator + "##" + InfoTruckOwner);
                result.setObjectList(listResult);
            }

        } catch (Throwable th) {
            result.setMsg(th.getMessage());
        }

        return result;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfUnSumittedJob(int truckOwnerID, int index, int nombreMaxResult)throws JobManagementException {

        String res;
        Result result = new Result();
        List<JobResponse> jobResponses = null;
        int numberOfElts = 0;
        List<String> listResult = new ArrayList<>();
        Account account;
        result.setMsg("good");
        try {

            ges.creatEntityManager();
            account = (Account) ges.getEntityManager().find(Account.class, truckOwnerID);

            if (account == null) {
                result.setMsg("InvaLidTruckOwnerID");
                return result;
            }

            Query query = ges.getEntityManager().createQuery("SELECT a FROM JobResponse a WHERE a.deleted = false AND a.submitted = false AND a.truckownerID = :truckOwnerID  AND a.jobID IN  (SELECT b.jobID FROM Validation b WHERE b.deleted = FALSE AND b.clientValidation = 1 ) ORDER BY a.creationDate DESC");

            query.setParameter("truckOwnerID", account.getUser());
            numberOfElts = query.getResultList().size();
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResult);

            jobResponses = query.getResultList();

        } catch (Throwable th) {
            result.setMsg(th.getMessage());
        }

        if (jobResponses != null && !jobResponses.isEmpty()) {

            for (JobResponse jobResponse : jobResponses) {

                res = "" + jobResponse.getJobresponseID() + ";"
                        + jobResponse.getJobID().getJobID() + ";"
                        + jobResponse.getStartDate().getTime() + ";"
                        + jobResponse.getEndDate().getTime() + ";"
                        + jobResponse.getHourPerDay() + ";"
                        + jobResponse.getNumberOfTruck() + ";"
                        + jobResponse.getJobID().getJobLocation() + ";"
                        + jobResponse.getJobID().getJobNumber() + ";"
                        + jobResponse.getCreationDate().getTime() + ";"
                        + jobResponse.getJobID().getClose() + ";"
                        + jobResponse.getJobID().getExcavatorID().getName() + ";"
                        + jobResponse.getJobID().getExcavatorID().getSurname() + ";"
                        + jobResponse.getJobID().getExcavatorID().getAccountID().getAccountID() + ";"
                        + jobResponse.getJobID().getClose() + ";"
                        + jobResponse.getJobID().getExcavatorID().getTelephone() + ";"
                        + "null";

                listResult.add(res);
            }
        }

        listResult.add("" + numberOfElts);
        result.setObjectList(listResult);
        return result;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfJobHistory(int truckOwnerID, int index, int nombreMaxResult) throws JobManagementException{

        String res;
        Result result = new Result();
        List<JobResponse> jobResponses = null;
        int numberOfElts = 0;
        List<String> listResult = new ArrayList<>();
        Account account;
        result.setMsg("good");
        try {

            ges.creatEntityManager();
            account = (Account) ges.getEntityManager().find(Account.class, truckOwnerID);

            if (account == null) {
                result.setMsg("InvaLidTruckOwnerID");
                return result;
            }

            Query query = ges.getEntityManager().createQuery("SELECT a FROM JobResponse a WHERE a.deleted = false AND a.submitted = TRUE AND a.truckownerID = :truckOwnerID  AND a.jobID IN  (SELECT b.jobID FROM Validation b WHERE b.deleted = FALSE AND b.clientValidation = 1 ) ORDER BY a.creationDate DESC");

            query.setParameter("truckOwnerID", account.getUser());
            numberOfElts = query.getResultList().size();
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResult);

            jobResponses = query.getResultList();

        } catch (Throwable th) {
            result.setMsg(th.getMessage());
        }

        if (jobResponses != null && !jobResponses.isEmpty()) {

            for (JobResponse jobResponse : jobResponses) {

                res = "" + jobResponse.getJobresponseID() + ";"
                        + jobResponse.getJobID().getJobID() + ";"
                        + jobResponse.getStartDate().getTime() + ";"
                        + jobResponse.getEndDate().getTime() + ";"
                        + jobResponse.getHourPerDay() + ";"
                        + jobResponse.getNumberOfTruck() + ";"
                        + jobResponse.getJobID().getJobLocation() + ";"
                        + jobResponse.getJobID().getJobNumber() + ";"
                        + jobResponse.getCreationDate().getTime() + ";"
                        + jobResponse.getJobID().getClose() + ";"
                        + jobResponse.getJobID().getExcavatorID().getName() + ";"
                        + jobResponse.getJobID().getExcavatorID().getSurname() + ";"
                        + jobResponse.getJobID().getExcavatorID().getAccountID().getAccountID() + ";"
                        + jobResponse.getJobID().getClose() + ";"
                        + jobResponse.getSubmitReview() + ";"
                        + jobResponse.getBillingPrice() + ";"
                        + jobResponse.getBillingPrice() + ";"
                        + jobResponse.getTruckownerID().getTelephone() + ";"
                        + jobResponse.getTruckownerID().getCellPhone() + ";"
                        + jobResponse.getTruckownerID().getAddress() + ";"
                        + (jobResponse.getPaymenttypeID() == null ? -1 : jobResponse.getPaymenttypeID().getPaymenttypeID()) + ";"
                        + "null";

                listResult.add(res);
            }
        }

        listResult.add("" + numberOfElts);
        result.setObjectList(listResult);
        return result;
    }

    @Override
    public String editSubmitReview(int jobResponseID, int submitReview) throws JobManagementException{

        String res = "";
        JobResponse jobResponse = null;
        boolean firstReview;

        try {

            ges.creatEntityManager();
            jobResponse = (JobResponse) ges.getEntityManager().find(JobResponse.class, jobResponseID, LockModeType.OPTIMISTIC_FORCE_INCREMENT);

            if (jobResponse == null) {
                ges.closeEm();
                return "InvalidJobResponseID\n";
            }

            firstReview = (jobResponse.getSubmitReview() == 0);
            int lastReview = jobResponse.getSubmitReview();

            jobResponse.setSubmitReview(submitReview);
            ges.getEntityManager().merge(jobResponse);

            Job job = jobResponse.getJobID();
            ges.getEntityManager().lock(job, LockModeType.PESSIMISTIC_FORCE_INCREMENT);
            
            ges.getEntityManager().merge(job);
            ges.closeEm();
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editSubmitReview: jobResponse: " + jobResponse + " Rating: " + jobResponse.getSubmitReview());

            // Mise a jour du rating du job
            JobFonction jf = new JobFonction();
            float rate = 0;
            int numberOfRate = 0;
            float jobLastReview = job.getRate();

            if (firstReview) {
                numberOfRate = job.getNumberOfRate();
                rate = (job.getRate() * numberOfRate + jobResponse.getSubmitReview()) / (numberOfRate + 1);
                job.setNumberOfRate(numberOfRate + 1);
                job.setRate(rate);
            } else {
                numberOfRate = job.getNumberOfRate();
                rate = (job.getRate() * numberOfRate + jobResponse.getSubmitReview() - lastReview) / (numberOfRate);
                job.setRate(rate);
            }

            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editSubmitReview: job: " + job + " Rating: " + job.getRate());
            job.setRate(rate);
            ges.getEntityManager().merge(job);
            ges.closeEm();

            User user = jobResponse.getJobID().getExcavatorID();

            float excavatorReview = 0;
            //rate2 = jf.evaluateExcavatorRate(user);
            int excavatorNumberOfRate = 0;

            if (firstReview) {

                excavatorNumberOfRate = user.getNumberOfReview();
                excavatorReview = (user.getRate() * excavatorNumberOfRate + jobResponse.getSubmitReview()) / (excavatorNumberOfRate + 1);
                user.setNumberOfReview(excavatorNumberOfRate + 1);
                user.setRate(excavatorReview);

            } else {

                excavatorNumberOfRate = user.getNumberOfReview();
                excavatorReview = (user.getRate() * excavatorNumberOfRate + jobResponse.getSubmitReview() - lastReview) / (excavatorNumberOfRate);
                user.setRate(excavatorReview);

            }

            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editSubmitReview: user: " + user + "  Rating: " + user.getRate());

            ges.getEntityManager().merge(user);
            ges.closeEm();

            res = "good";

        } catch (Throwable th) {
            res = th.getMessage();
            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editSubmitReview: Error: " + res);
        }

        return res;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<JobResponse> getARangeOfValidatJobResponseByJob(int jobID, int index, int nombreMaxResult) throws JobManagementException{

        Job job = null;
        List<JobResponse> jobResponses;

        try {

            job = (Job) ges.getEntityManager().find(Job.class, jobID);

            //Query query =  ges.getEntityManager().createQuery("SELECT a.jobresponseID FROM Validation a WHERE a.deleted = FALSE AND a.clientValidation = 1 AND a.truckOwnerValidation = 1 AND a.jobID = :jobID");
            //Query query =  ges.getEntityManager().createQuery("SELECT a.jobresponseID FROM Validation a WHERE a.deleted = FALSE AND a.jobresponseID.deleted = FALSE AND a.clientValidation = 1 AND a.truckOwnerValidation = 1 AND a.jobID = :jobID");
            Query query = ges.getEntityManager().createQuery("SELECT a FROM  JobResponse a WHERE a.deleted = FALSE AND a.jobID = :jobID AND a IN (SELECT DISTINCT b.jobresponseID FROM Validation b WHERE b.deleted = FALSE AND b.clientValidation = 1  AND b.truckOwnerValidation = 1 ) ORDER BY a.creationDate DESC");

            query.setParameter("jobID", job);
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResult);

            jobResponses = (List<JobResponse>) query.getResultList();

        } catch (Throwable th) {
            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"Error during the call of method getARangeOfValidatJobResponseByJob");
            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+th.getMessage());
            return null;
        }

        return jobResponses;

    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<JobResponse> getJobResponseForAutomatedBook(int jobID) throws JobManagementException{

        String res;
        Result result = new Result();
        List<JobResponse> jobResponses = null;

        Job job = null;
        result.setMsg("good");
        try {

            ges.creatEntityManager();
            job = (Job) ges.getEntityManager().find(Job.class, jobID);

            if (job == null) {
                logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+" --------  InvaLidJobID------  ");
                return null;
            }

            Query query = ges.getEntityManager().createQuery("SELECT a FROM JobResponse a WHERE a.deleted = false AND a.jobID = :jobID ORDER BY a.numberOfTruck DESC");

            query.setParameter("jobID", job);
            jobResponses = query.getResultList();
            ges.closeEm();

        } catch (Throwable th) {
            result.setMsg(th.getMessage());
        }

        return jobResponses;
    }

    @Override
    public String editReviewComment(int jobResponseID, String reviewComment) throws JobManagementException{

        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editReviewComment: jobResponseID  = " + jobResponseID + " ---> reviewComment = " + reviewComment);
        String res = "";
        JobResponse jobResponse = null;
        try {

            ges.creatEntityManager();
            jobResponse = (JobResponse) ges.getEntityManager().find(JobResponse.class, jobResponseID);

            if (jobResponse == null) {
                ges.closeEm();
                return "InvalidJobResponseID\n";
            }

            jobResponse.setReviewComment(reviewComment);
            ges.getEntityManager().merge(jobResponse);
            ges.closeEm();

            res = "good";

        } catch (Throwable th) {
            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editReviewComment: Error  = " + th.getMessage());
            res = th.getMessage();
             throw new JobManagementException(getClass()+"","editEmployeeInfo",1,th.getMessage(),th.getMessage());
        
        }

        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editReviewComment: res  = " + res);
        return res;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfExcavatorReviewComment(int jobID, int index, int nombreMaxResult) throws JobManagementException{

        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getARangeOfExcavatorReviewComment: jobID  = " + jobID + " ---> index = " + index + " ---> nombreMaxResult = " + nombreMaxResult);
        Result result = new Result();
        int numberOfElts = 0;
        List<String> listResult = new ArrayList<>();
        Job job;
        Iterator resultat = null;

        result.setMsg("good");
        try {

            ges.creatEntityManager();
            job = (Job) ges.getEntityManager().find(Job.class, jobID);

            if (job == null) {
                result.setMsg("InvaLidJobID");
                return result;
            }

            Query query;

            //query =  ges.getEntityManager().createQuery("SELECT a.submitReview, a.reviewComment FROM  JobResponse a WHERE a.deleted = FALSE AND a.jobID.excavatorID = :excavatorID AND a IN (SELECT DISTINCT b.jobresponseID FROM Validation b WHERE b.deleted = FALSE AND b.clientValidation = 1   )");
            query = ges.getEntityManager().createQuery("SELECT a.submitReview, a.reviewComment FROM  JobResponse a WHERE a.deleted = FALSE AND a.jobID.excavatorID = :excavatorID AND a.submitReview <> 0 ORDER BY a.creationDate DESC");

            query.setParameter("excavatorID", job.getExcavatorID());

            numberOfElts = query.getResultList().size();
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResult);

            resultat = query.getResultList().iterator();

        } catch (Throwable th) {
            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getARangeOfExcavatorReviewComment: Error  = " + th.getMessage());
            result.setMsg(th.getMessage());
        }
        if (resultat != null) {

            while (resultat.hasNext()) {
                Object[] tuple = (Object[]) resultat.next();
                int submitReview = (int) tuple[0];
                String reviewComment = (String) tuple[1];

                listResult.add(submitReview + "###" + reviewComment);
            }

        }

        listResult.add("" + numberOfElts);
        result.setObjectList(listResult);
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getARangeOfExcavatorReviewComment: listResult  = " + listResult);
        return result;
    }

    @Override
    public String editReviewFromExcvator(int jobResponseID, int submitReview) throws JobManagementException{

        String res = "";
        JobResponse jobResponse = null;
        boolean firstReview;

        try {

            ges.creatEntityManager();
            jobResponse = (JobResponse) ges.getEntityManager().find(JobResponse.class, jobResponseID);

            if (jobResponse == null) {
                ges.closeEm();
                return "InvalidJobResponseID\n";
            }

            firstReview = (jobResponse.getReviewFromExcavator() == 0);
            int lastReview = jobResponse.getReviewFromExcavator();

            jobResponse.setReviewFromExcavator(submitReview);
            ges.getEntityManager().merge(jobResponse);

            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editReviewFromExcvator:  jobResponse: " + jobResponse + "  Rating: " + jobResponse.getSubmitReview());

            User user = jobResponse.getTruckownerID();

            float truckOwnerReview = 0;
            int truckOwnerNumberOfRate = 0;

            if (firstReview) {

                truckOwnerNumberOfRate = user.getNumberOfReview();
                truckOwnerReview = (user.getRate() * truckOwnerNumberOfRate + jobResponse.getReviewFromExcavator()) / (truckOwnerNumberOfRate + 1);
                user.setNumberOfReview(truckOwnerNumberOfRate + 1);
                user.setRate(truckOwnerReview);

            } else {

                truckOwnerNumberOfRate = user.getNumberOfReview();
                truckOwnerReview = (user.getRate() * truckOwnerNumberOfRate + jobResponse.getReviewFromExcavator() - lastReview) / (truckOwnerNumberOfRate);
                user.setRate(truckOwnerReview);

            }

            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editReviewFromExcvator:  user: " + user + "  Rating: " + user.getRate());

            ges.getEntityManager().merge(user);
            ges.closeEm();

            res = "good";

        } catch (Throwable th) {
            res = th.getMessage();
            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editReviewFromExcvator: Error: " + res);
             throw new JobManagementException(getClass()+"","editEmployeeInfo",1,th.getMessage(),th.getMessage());
        
        }

        return res;
    }

    @Override
    public String editReviewCommentFromExcvator(int jobResponseID, String reviewComment)throws JobManagementException {

        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editReviewCommentFromExcvator: jobResponseID  = " + jobResponseID + " ---> reviewComment = " + reviewComment);
        String res = "";
        JobResponse jobResponse = null;
        try {

            ges.creatEntityManager();
            jobResponse = (JobResponse) ges.getEntityManager().find(JobResponse.class, jobResponseID);

            if (jobResponse == null) {
                ges.closeEm();
                return "InvalidJobResponseID\n";
            }

            jobResponse.setCommentFromExcavator(reviewComment);
            ges.getEntityManager().merge(jobResponse);
            ges.closeEm();

            res = "good";

        } catch (Throwable th) {
            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editReviewCommentFromExcvator: Error  = " + th.getMessage());
            res = th.getMessage();
             throw new JobManagementException(getClass()+"","editEmployeeInfo",1,th.getMessage(),th.getMessage());
        
        }

        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editReviewCommentFromExcvator: res  = " + res);
        return res;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfTruckOwnerReviewComment(int jobResponseID, int index, int nombreMaxResult) throws JobManagementException{

        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getARangeOfTruckOwnerReviewComment: jobResponseID  = " + jobResponseID + " ---> index = " + index + " ---> nombreMaxResult = " + nombreMaxResult);
        Result result = new Result();
        int numberOfElts = 0;
        List<String> listResult = new ArrayList<>();
        JobResponse jobResponse;
        Iterator resultat = null;

        result.setMsg("good");
        try {

            ges.creatEntityManager();
            jobResponse = (JobResponse) ges.getEntityManager().find(JobResponse.class, jobResponseID);

            if (jobResponse == null) {
                result.setMsg("InvaLidJobResponseID");
                return result;
            }

            Query query;

            //query =  ges.getEntityManager().createQuery("SELECT a.submitReview, a.reviewComment FROM  JobResponse a WHERE a.deleted = FALSE AND a.jobID.excavatorID = :excavatorID AND a IN (SELECT DISTINCT b.jobresponseID FROM Validation b WHERE b.deleted = FALSE AND b.clientValidation = 1   )");
            query = ges.getEntityManager().createQuery("SELECT a.reviewFromExcavator, a.commentFromExcavator FROM  JobResponse a WHERE a.deleted = FALSE AND a.truckownerID = :truckownerID AND a.reviewFromExcavator <> 0 ORDER BY a.creationDate DESC");

            query.setParameter("truckownerID", jobResponse.getTruckownerID());

            numberOfElts = query.getResultList().size();
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResult);

            resultat = query.getResultList().iterator();

        } catch (Throwable th) {
            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getARangeOfExcavatorReviewComment: Error  = " + th.getMessage());
            result.setMsg(th.getMessage());
        }
        if (resultat != null) {

            while (resultat.hasNext()) {
                Object[] tuple = (Object[]) resultat.next();
                int submitReview = (int) tuple[0];
                String reviewComment = (String) tuple[1];

                listResult.add(submitReview + "###" + reviewComment);
            }

        }

        listResult.add("" + numberOfElts);
        result.setObjectList(listResult);
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getARangeOfTruckOwnerReviewComment: listResult  = " + listResult);
        return result;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getTruckOwnerAndTruckInfo(int accountID, int jobResponseID)throws JobManagementException{

        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getJobResponseAndTruckInfo: accountID  = " + accountID + " ---> jobResponseID = " + jobResponseID);
        Result result = new Result();
        List<String> listResult = new ArrayList<>();
        List<SolicitedTruck> solicitedTruckList = new ArrayList<>();
        JobResponse jobResponse = null;

        result.setMsg("good");
        try {

            ges.creatEntityManager();
            jobResponse = (JobResponse) ges.getEntityManager().find(JobResponse.class, jobResponseID);

            if (jobResponse == null) {
                result.setMsg("InvaLidJobResponseID");
                result.afficherResult("getJobResponseAndTruckInfo");
                return result;
            }

            Query query = ges.getEntityManager().createQuery("SELECT s FROM SolicitedTruck s WHERE s.deleted = FALSE AND s.jobresponseID = :jobresponseID ORDER BY s.creationDate DESC")
                    .setParameter("jobresponseID", jobResponse);
            solicitedTruckList = query.getResultList();

        } catch (Throwable th) {
            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getTruckOwnerAndTruckInfo: Error  = " + th.getMessage());
            result.setMsg(th.getMessage());
        }

        if (jobResponse == null) {
            result.setMsg("InvaLidJobResponseID");
            result.afficherResult("getJobResponseAndTruckInfo");
            return result;
        }

        User user = jobResponse.getTruckownerID();
        String truckOwnerInfo = "" + user.getAccountID().getAccountID()+ ";" 
                                +user.getName()+";"
                                +user.getSurname()+";"
                                +user.getAddress()+";"
                                +user.getAccountID().getEmail()+";"
                                +user.getTelephone()+";"
                                +user.getCellPhone()+";"
                                +user.getGpsCoordinate()+";"
                                +"null"
         ;

        listResult.add(truckOwnerInfo);

        if (solicitedTruckList != null && !solicitedTruckList.isEmpty()) {
            String res = "";
            for (SolicitedTruck solicitedTruck : solicitedTruckList) {

                Truck truck = solicitedTruck.getTruckID();
                Calendar c = Calendar.getInstance();
                c.setTime(truck.getTrucktypeID().getYear());
                /*
                String picturePathName = (truck.getPicture() == null ? "" : truck.getPicture().getPathName());
                String picturePathNameID = (truck.getPicture() == null ? "" : "" + truck.getPicture().getDocumentID());
                
                String insurancePathName = (truck.getPictureInsurance() == null ? "" : truck.getPictureInsurance().getPathName());
                String insurancePathNameID = (truck.getPictureInsurance() == null ? "" : ""+truck.getPictureInsurance().getDocumentID());
                */
                String picturePathName = TruckFunction.getDocumentForTruck(truck)[0];
                String picturePathNameID = TruckFunction.getDocumentForTruck(truck)[1];
                
                String insurancePathName = TruckFunction.getDocumentForTruck(truck)[2];
                String insurancePathNameID = TruckFunction.getDocumentForTruck(truck)[3];

                String driverInfo = "-1";
                
                res = truck.getTruckID() + ";"//0
                        + truck.getTruckNumber() + ";"
                        + picturePathName + ";"
                        + truck.getAvailable() + ";"
                        + truck.getCreationDate().getTime() + ";"
                        + truck.getLocationPrice() + ";"//5
                        + truck.getLenghtofbedID().getLenghtofbedID() + ";"
                        + truck.getDistance() + ";"
                        + truck.getPhoneNumber() + ";"
                        + truck.getTruckZipCode() + ";"
                        + truck.getDOTNumber() + ";"//10
                        + truck.getGeneralLiability() + ";"
                        + truck.getInsuranceLiability() + ";"
                        + insurancePathName + ";"
                        + truck.getYear() + ";"
                        + truck.getTruckaxleID().getTruckaxleID() + ";"//15
                        + picturePathNameID + ";"
                        + insurancePathNameID + ";"
                        + truck.getTruckDescription() + ";"
                        + driverInfo + ";"//19
                        + solicitedTruck.getTruckAvailable() + ";"//20
                        + "null";
                listResult.add(res);

            }

            result.setObjectList(listResult);
        }

        result.afficherResult("getJobResponseAndTruckInfo");
        return result;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Object[]> searchTruckOwnerToNotify(float jobLatitude, float jobLongitude) throws JobManagementException{

        List<Object[]> userInformation = null;
        
        try {
            ges.creatEntityManager();
            
            Query query = ges.getEntityManager().createNativeQuery(JobManagementQuery.getQueryToFindUserToNotify())
                    .setParameter(1, jobLatitude)
                    .setParameter(2, jobLongitude)
                    .setParameter(3, TypeOfUser.SOCIAL_STATUS_TRUCK_OWNER_ID)
                    ;
            userInformation = query.getResultList();

        } catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"Error: " + string.toString());
        }
        
        //logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"searchTruckOwnerToNotify - userInformation: " + userInformation);
        if (userInformation == null || userInformation.isEmpty()) {
            return null;
        }
        
        return userInformation;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Object[]> searchTruckOwnerToNotify(int jobID, float jobLatitude, float jobLongitude) throws JobManagementException{

        List<Object[]> userInformation = null;
        
        try {
            ges.creatEntityManager();
            
            Query query = ges.getEntityManager().createNativeQuery(JobManagementQuery.getQueryToFindUserForEditJob())
                    .setParameter(1, jobLatitude)
                    .setParameter(2, jobLongitude)
                    .setParameter(3, TypeOfUser.SOCIAL_STATUS_TRUCK_OWNER_ID)
                    .setParameter(4, jobID)
                    ;
            userInformation = query.getResultList();

        } catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"Error: " + string.toString());
        }
        
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"searchTruckOwnerToNotify - userInformation: " + userInformation);
        if (userInformation == null || userInformation.isEmpty()) {
            return null;
        }
        
        return userInformation;
    }


}
