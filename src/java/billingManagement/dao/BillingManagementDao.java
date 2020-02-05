/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package billingManagement.dao;

import accountManagement.dao.TypeOfUser;
import employeeManagement.dao.EmployeeStatus;
import entities.Account;
import entities.BillingReceiver;
import entities.DailyTicket;
import entities.Employee;
import entities.JobResponse;
import entities.MonthlyBill;
import entities.PaymentType;
import entities.User;
import entities.WeeklyTicket;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import mailing.mailSending.MailFunction;
import org.apache.logging.log4j.LogManager;
import paysimple.MypaySimpleObj;
import paysimple.PaySimpleAP;
import toolsAndTransversalFunctionnalities.CredentialInformation;
import toolsAndTransversalFunctionnalities.GestionnaireEntite;
import toolsAndTransversalFunctionnalities.Result;
import toolsAndTransversalFunctionnalities.ResultBackend;
import util.date.DateConversion;
import util.date.DateFunction;
import util.exception.BillingManagementException;
import util.metier.function.NumberProcessing;
import util.metier.function.TypeOfPayment;

/**
 *
 * @author erman
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class BillingManagementDao implements IBillingManagementDaoLocal, IBillingManagementDaoRemote{

    @EJB
    GestionnaireEntite ges;
    
    BillingFonction billFonction;
    
     final static org.apache.logging.log4j.Logger logger = LogManager.getLogger("dump1");
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getAllDateOfLogsByJobResponseID(int jobResponseID) throws BillingManagementException{
         
        Result result = new Result();
        
        List<String> listResult = new ArrayList<>();
        List<Date> dateLogList;
        JobResponse jobResponse;
        result.setMsg("unknown");
        try{
            
            ges.creatEntityManager();
            jobResponse = (JobResponse)ges.getEntityManager().find(JobResponse.class, jobResponseID);
            
            if(jobResponse == null){
                result.setMsg("InvaLidJobResponseID");
                return result;
            }
            
            
            Query query =  ges.getEntityManager().createQuery("SELECT DISTINCT a.exDateLog FROM JobLog a WHERE a.deleted = false AND a.jobresponseID = :jobresponseID ORDER BY a.exDateLog DESC ");
            query.setParameter("jobresponseID", jobResponse);
            dateLogList = query.getResultList();
            for (Date dateLog : dateLogList) {
                //On retourne la date dans le fusaux horaire du job.
                listResult.add(String.valueOf(dateLog.getTime() + ";" 
                        + jobResponse.getJobID().getTimezoneID() + ";"//1
                        + DateFunction.formatDateToStringYMMMD(dateLog) + ";"
                ) );
            }
            result.setMsg("good");
            result.setObjectList(listResult);
        
        }catch(Throwable th){
            result.setMsg(th.getMessage());
        }
        
        result.afficherResult("getAllDateOfLogsByJobResponseID");
        return result;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getAllPaymentType() {
         
        String res;
        Result result = new Result();
        
        List<String> listResult = new ArrayList<>();
        List<PaymentType> paymentTypes;
        
        result.setMsg("unknown");
        try{
            
            ges.creatEntityManager();
            paymentTypes = (List<PaymentType>)ges.getEntityManager().createNamedQuery("PaymentType.findAll").getResultList();
            
            if(paymentTypes != null && !paymentTypes.isEmpty()){
                for(PaymentType paymentType: paymentTypes){
                    res = ""+paymentType.getPaymenttypeID();
                    res += ";"+paymentType.getLibel();
                    
                    listResult.add(res);
                }
            }
            
            result.setMsg("good");
            result.setObjectList(listResult);
        
        }catch(Throwable th){
            result.setMsg(th.getMessage());
        }
        
        return result;
    }
    
    @Override
    public String editPaymentStatus(int jobResponseID) throws BillingManagementException{
        
        String res = "";
        JobResponse jobResponse = null;
        try{
            
            ges.creatEntityManager();
            jobResponse = (JobResponse)ges.getEntityManager().find(JobResponse.class, jobResponseID);
            
            if(jobResponse == null){
                ges.closeEm();
                return "InvalidJobResponseID\n";
            }
            
            if(jobResponse.getPaymentStatus()){
                ges.closeEm();
                return "AllReadyValidate\n";
            }
            
            jobResponse.setPaymentStatus(true);
            ges.getEntityManager().merge(jobResponse);
            ges.closeEm();
            res = "good";
            
        }catch(Throwable th){
            res = th.getMessage();
              throw new BillingManagementException(getClass()+"","editPaymentStatus",1,res,res);
           
        }
        
        return res;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getFinalBill(int jobResponseID) {

        String res;
        Result result = new Result();
        List<String> listResult = new ArrayList<>();

        List<DailyTicket> dailyTickets = null;
        JobResponse jobResponse = null;
        result.setMsg("good");

        try {

            ges.creatEntityManager();
            jobResponse = (JobResponse) ges.getEntityManager().find(JobResponse.class, jobResponseID);

            if (jobResponse == null) {
                result.setMsg("InvaLidJobResponseID");
                return result;
            }

            Query query = ges.getEntityManager().createQuery("SELECT a FROM DailyTicket a WHERE a.deleted = FALSE AND a.jobresponseID = :jobresponseID ");

            query.setParameter("jobresponseID", jobResponse);

            dailyTickets = query.getResultList();

        } catch (Throwable th) {
            result.setMsg(th.getMessage());
        }

        if (dailyTickets == null || dailyTickets.isEmpty()) {
            result.setMsg("NotExistingDailyTicket");
            return result;
        }

        String excavatorInfo = "" + jobResponse.getJobresponseID() + ";"//0
                + jobResponse.getJobID().getExcavatorID().getName() + ";"//1
                + jobResponse.getJobID().getExcavatorID().getSurname() + ";"//2
                + jobResponse.getJobID().getExcavatorID().getAddress() + ";"
                + jobResponse.getJobID().getExcavatorID().getCellPhone() + ";"
                + jobResponse.getJobID().getExcavatorID().getAccountID().getEmail() + ";"//5
                ;

        String otherInfo = "" + jobResponse.getTruckownerID().getName() + ";"//6
                + jobResponse.getTruckownerID().getSurname() + ";"
                + jobResponse.getTruckownerID().getAddress() + ";"
                + jobResponse.getTruckownerID().getCellPhone() + ";"
                + jobResponse.getTruckownerID().getAccountID().getEmail() + ";"
                + (jobResponse.getJobID().getTypeofdirtID() == null ? "" : jobResponse.getJobID().getTypeofdirtID().getLabel()) + ";"
                + jobResponse.getJobID().getJobNumber() + ";"
                + jobResponse.getJobID().getJobLocation() + ";"//13
                + jobResponse.getJobID().getStartDate().getTime() + ";"
                + jobResponse.getJobID().getEndDate().getTime() + ";"
                + jobResponse.getJobID().getExcavatorID().getCompagnyName() + ";"
                + jobResponse.getJobID().getExcavatorID().getTelephone() + ";"
                + jobResponse.getTruckownerID().getCompagnyName() + ";"//18
                + jobResponse.getTruckownerID().getTelephone()//19
                ;

        listResult.add(excavatorInfo + otherInfo);
        int totalLoad = 0;
        float totalHour = 0;
        float totalAmount = 0;

        for (DailyTicket dailyTicket : dailyTickets) {

            Object[] resultQuery;
            try {
                resultQuery = (Object[]) ges.getEntityManager().createQuery("SELECT MIN(j.startTime), MAX(j.endTime), SUM(j.numberOfLoad), (SUM( SQL('TIME_TO_SEC(?)', j.endTime) -  SQL('TIME_TO_SEC(?)', j.startTime))/3600), COUNT(DISTINCT j) FROM JobLog j WHERE j.deleted = false AND j.dailyticketID = :dailyTicket ")
                        .setParameter("dailyTicket", dailyTicket)
                        .getSingleResult();
            } catch (Exception e) {
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getFinalBill: error1 --- " + e.getMessage());
                continue;
            }

            Date dailyStartTime = (Date) resultQuery[0];
            Date dailyEndTime = (Date) resultQuery[1];
            int dailyTotalLoad = Integer.parseInt(String.valueOf(resultQuery[2]));
            float dailyTotalHours = Float.valueOf(String.valueOf(resultQuery[3]));
            int totalJobLog = Integer.parseInt(String.valueOf(resultQuery[2]));
            double dailyTotalAmount = getTicketTotalAmount(jobResponse, dailyTotalHours, dailyTotalLoad, totalJobLog);

            res = "" + dailyTicket.getDailyticketID() + ";"
                    + dailyTicket.getTicketDate().getTime() + ";"
                    + DateConversion.formatDateToTimeAMPM(dailyStartTime) + ";"
                    + DateConversion.formatDateToTimeAMPM(dailyEndTime) + ";"
                    + dailyTotalLoad + ";"
                    + NumberProcessing.round(dailyTotalHours, 2) + ";"
                    + NumberProcessing.round(dailyTotalAmount, 2) + ";"
                    + dailyTicket.getJobresponseID().getJobID().getJobLocation() + ";"
                    + dailyTicket.getJobresponseID().getJobID().getJobNumber() + ";"
                    + dailyTicket.getViewTicket() + ";"
                    + dailyTicket.getWeeklyticketID().getPaid() + ";"
                    + dailyTicket.getWeeklyticketID().getPaymentRecieve() + ";"
                    + (dailyTicket.getTruckID() == null ? "" : dailyTicket.getTruckID().getTruckID()) + ";"
                    + (dailyTicket.getTruckID() == null ? "" : dailyTicket.getTruckID().getTruckNumber()) + ";"
                    + "null";

            totalLoad += dailyTotalLoad;
            totalHour += dailyTotalHours;
            totalAmount += dailyTotalAmount;

            listResult.add(res);
        }

        listResult.set(0, listResult.get(0) + ";" 
                + totalLoad /*20*/ + ";" 
                + NumberProcessing.round(totalHour, 2)/*21*/ + ";" 
                + NumberProcessing.round(totalAmount, 2)/*22*/ + ";" 
                + jobResponse.getPaymentStatus()/*23*/ + ";" 
                + jobResponse.getJobID().getTimezoneID()/*24*/ + ";" 
                + jobResponse.getJobID().getTimeZone()/*25*/ + ";" 
                + jobResponse.getTimeZone()/*26*/
        );
        result.setObjectList(listResult);
        return result;
    }

    @Override
    public String validateDailyTicketFromExcavator(int jobResponseID, long ticketDate) throws BillingManagementException{

        String res = "";
        JobResponse jobResponse = null;
        try {

            ges.creatEntityManager();

            try {

                jobResponse = (JobResponse) ges.getEntityManager().createQuery("SELECT a FROM JobResponse a WHERE a.deleted = FALSE AND a.jobresponseID = :jobresponseID").setParameter("jobresponseID", jobResponseID).getSingleResult();

            } catch (Exception e) {
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"validateDailyTicketFromExcavator: Error: " + e.getMessage());
            }

            if (jobResponse == null) {
                //ges.closeEm();
                return "invalidJobResponseID";
            }

            int updateCount = ges.getEntityManager().createQuery("UPDATE DailyTicket AS d SET d.state = TRUE WHERE d.deleted = FALSE AND d.jobresponseID = :jobresponseID AND d.ticketDate = :ticketDate")
                    .setParameter("jobresponseID", jobResponse)
                    .setParameter("ticketDate", new Date(ticketDate))
                    .executeUpdate();
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"validateDailyTicketFromExcavator: Nombre de ticket modifié est : " + updateCount);

            ges.closeEm();
            res = "good";

        } catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"validateDailyTicketFromExcavator: Error: " + res);
            throw new BillingManagementException(getClass()+"","validateDailyTicketFromExcavator",1,res,res);
           
            //return res;
        }

        return res;
    }

    @Override
    public String validateDailyTicketFromTruckOwner(int jobResponseID, long ticketDate, String excavatorCode) throws BillingManagementException{
        
        String res = "";
        JobResponse jobResponse = null;
        Employee employee = null;
        try{
            
            ges.creatEntityManager();
            jobResponse = (JobResponse) ges.getEntityManager().find(JobResponse.class, jobResponseID);                
            
            if(jobResponse == null){
                return "invalidJobResponseID";
            }
            
            try {
                employee = (Employee) ges.getEntityManager().createQuery("SELECT e FROM Employee e WHERE e.deleted = FALSE AND e.empCode = :empCode AND e.excavatorID = :excavatorID")
                        .setParameter("empCode", excavatorCode)
                        .setParameter("excavatorID", jobResponse.getJobID().getExcavatorID())
                        .getSingleResult();
            } catch (Exception e) {
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"validateDailyTicketFromTruckOwner: Error: "+e.getMessage());
            }
            
            if (jobResponse.getJobID().getExcavatorID().getUserCode().equals(excavatorCode)
                    || (employee != null && employee.getEmpStatus().equals(EmployeeStatus.ACTIF_EMP) )  ) {
                
                int updateCount = ges.getEntityManager().createQuery( "UPDATE DailyTicket AS d SET d.state = TRUE, d.viewTicket = TRUE, d.validationDate = :validationDate, d.employeeID = :employeeID WHERE d.deleted = FALSE AND d.jobresponseID = :jobresponseID AND d.ticketDate = :ticketDate")
                        .setParameter("jobresponseID", jobResponse)
                        .setParameter("ticketDate", new Date(ticketDate))
                        .setParameter("validationDate", DateFunction.getGMTDate())
                        .setParameter("employeeID", employee)
                        .executeUpdate();
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"validateDailyTicketFromTruckOwner: Nombre de ticket modifié est : " + updateCount);
                
                ges.closeEm();
                res = "good";
                
            } else {
                res = "invalidCode";
            }
        }catch(Throwable th){
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"validateDailyTicketFromTruckOwner: Error: "+res);
            throw new BillingManagementException(getClass()+"","validateDailyTicketFromTruckOwner",1,res,res);
           
        }
        
        return res;
    }

    @Override
    public Result generateDailyTicket(int jobResponseID, long date) {

        String res;
        Result result = new Result();
        List<String> listResult = new ArrayList<>();
        List<DailyTicket> dailyTickets = null;
        JobResponse jobResponse = null;
        result.setMsg("good");
        Date ticketDate = new Date(date);

        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"generateDailyTicket:   jobResponseID = " + jobResponseID + "  *** new Date(date) = " + ticketDate);
        try {

            ges.creatEntityManager();
            jobResponse = (JobResponse) ges.getEntityManager().find(JobResponse.class, jobResponseID);

            if (jobResponse == null) {
                result.setMsg("InvaLidJobResponseID");
                return result;
            }

            Query query = ges.getEntityManager().createQuery("SELECT a FROM DailyTicket a WHERE a.deleted = false AND a.jobresponseID = :jobresponseID AND a.ticketDate = :date ");

            query.setParameter("jobresponseID", jobResponse);
            query.setParameter("date", ticketDate);

            dailyTickets = query.getResultList();

        } catch (Throwable th) {
            result.setMsg("" + th.getMessage());
        }
        
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"generateDailyTicket: dailyTickets = " + dailyTickets);
        if (dailyTickets != null && !dailyTickets.isEmpty()) {
            listResult.add("0");
            DailyTicket dailyTicket = dailyTickets.get(0);
            int totalLoads = 0;
            float totalHours = 0;
            Date startTime = null;
            Date endTime = null;
            boolean status = true;
            boolean statusView = true;

            for (DailyTicket dailyTicket1 : dailyTickets) {
                
                Object[] resultQuery;
                try {
                    resultQuery = (Object[]) ges.getEntityManager().createQuery("SELECT MIN(j.startTime), MAX(j.endTime), SUM(j.numberOfLoad), (SUM( SQL('TIME_TO_SEC(?)', j.endTime) -  SQL('TIME_TO_SEC(?)', j.startTime))/3600), COUNT(DISTINCT j) FROM JobLog j WHERE j.deleted = false AND j.dailyticketID = :dailyTicket ")
                            .setParameter("dailyTicket", dailyTicket1)
                            .getSingleResult();
                } catch (Exception e) {
                    logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"generateDailyTicket: error1 --- " + e.getMessage());
                    continue;
                }

                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"generateDailyTicket: error1 --- "
                        +" \n resultQuery[0] = " + resultQuery[0]
                        +" \n resultQuery[1] = " + resultQuery[1]
                        +" \n resultQuery[2] = " + resultQuery[2]
                        +" \n resultQuery[3] = " + resultQuery[3]
                        +" \n resultQuery[4] = " + resultQuery[4]
                );
                Date dailyStartTime = (Date) resultQuery[0];
                Date dailyEndTime = (Date) resultQuery[1];
                int dailyTotalLoad = Integer.parseInt(String.valueOf(resultQuery[2]));
                //float dailyTotalHours = Float.parseFloat(String.valueOf(resultQuery[3]))/3600;
                float dailyTotalHours = Float.parseFloat(String.valueOf(resultQuery[3]));
                
                totalLoads += dailyTotalLoad;
                totalHours += dailyTotalHours;
                startTime = ((startTime != null && startTime.before(dailyStartTime)) ? startTime : dailyStartTime);
                endTime = ((endTime != null && endTime.after(dailyEndTime)) ? endTime : dailyEndTime);
                
                status &= dailyTicket1.getState();
                statusView &= dailyTicket1.getViewTicket();

                res = "" + (dailyTicket1.getTruckID() == null ? "" : dailyTicket1.getTruckID().getTruckNumber());
                res += ";" + DateConversion.formatDateToTimeAMPM(dailyStartTime);
                res += ";" + DateConversion.formatDateToTimeAMPM(dailyEndTime);
                res += ";" + dailyTotalLoad;
                res += ";" + dailyTotalHours;
                res += ";" + dailyTicket1.getDailyticketID();
                res += ";" + dailyTicket1.getState();
                res += ";" + dailyTicket1.getViewTicket();
                res += ";" + (dailyTicket1.getTruckID() == null ? "" : dailyTicket1.getTruckID().getTruckID());
                res += ";" + "null";
                listResult.add(res);
            }
            
            Date jobStartDate = dailyTicket.getJobresponseID().getJobID().getStartDate();
            Date jobEndDate = dailyTicket.getJobresponseID().getJobID().getEndDate();
            String timezoneID = dailyTicket.getJobresponseID().getJobID().getTimezoneID();
            
            User excavator = dailyTicket.getJobresponseID().getJobID().getExcavatorID();
            User truckowner = dailyTicket.getJobresponseID().getTruckownerID();
            
            res = "" + dailyTicket.getJobresponseID().getJobresponseID();//0
            res += ";" + excavator.getName();
            res += ";" + excavator.getSurname();
            res += ";" + excavator.getAddress();
            res += ";" + excavator.getCellPhone();
            res += ";" + excavator.getAccountID().getEmail();//5

            res += ";" + truckowner.getName();
            res += ";" + truckowner.getSurname();
            res += ";" + truckowner.getAddress();
            res += ";" + truckowner.getCellPhone();
            res += ";" + truckowner.getAccountID().getEmail();//10
            res += ";" + (dailyTicket.getJobresponseID().getJobID().getTypeofdirtID() == null ? "" :  dailyTicket.getJobresponseID().getJobID().getTypeofdirtID().getLabel()) ;
            res += ";" + dailyTicket.getJobresponseID().getJobID().getJobNumber();
            res += ";" + dailyTicket.getJobresponseID().getJobID().getJobLocation();

            res += ";" + totalLoads;
            res += ";" + totalHours;//15
            res += ";" + status;
            res += ";" + excavator.getCompagnyName();
            res += ";" + excavator.getTelephone();
            res += ";" + truckowner.getCompagnyName();
            res += ";" + truckowner.getTelephone();//20
            res += ";" + jobStartDate.getTime();
            res += ";" + jobEndDate.getTime();
            res += ";" + dailyTicket.getTicketDate().getTime();
            res += ";" + statusView;
            res += ";" + dailyTicket.getJobresponseID().getPaymenttypeID().getLibel();//25
            res += ";" + dailyTicket.getJobresponseID().getBillingPrice();
            res += ";" + timezoneID;//27
            res += ";" + DateFunction.formatDateToStringYMMMD(dailyTicket.getTicketDate());
            res += ";" + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(jobStartDate, timezoneID);
            res += ";" + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(jobEndDate, timezoneID);//30
            res += ";" + (!dailyTicket.getState() ?  "null" : 
                        (dailyTicket.getEmployeeID() == null ? MailFunction.getTotalName(excavator.getName(), excavator.getSurname()) :
                        dailyTicket.getEmployeeID().getEmpName() )
                    );//31
            res += ";" + DateConversion.formatDateToTimeAMPM(startTime);//32
            res += ";" + DateConversion.formatDateToTimeAMPM(endTime);//33
        
            listResult.set(0, res);
        }

        result.setObjectList(listResult);
        result.afficherResult("generateDailyTicket");
        return result;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getWeeklyDateByJobResponseID(int jobResponseID) {

        Result result = new Result();

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

            Query query;

            Date currentDate = DateFunction.getGMTDateMidnight();
            //query = ges.getEntityManager().createQuery("SELECT DISTINCT a.weeklyStartDate, a.weeklyEndDate FROM WeeklyTicket a WHERE a.deleted = FALSE AND a.weeklyEndDate <= :currentDate AND a.jobresponseID = :jobresponseID  ");
            query = ges.getEntityManager().createQuery("SELECT DISTINCT a.weeklyStartDate, a.weeklyEndDate FROM WeeklyTicket a WHERE a.deleted = FALSE AND a.jobresponseID = :jobresponseID AND ( a.weeklyEndDate <= :currentDate OR a.jobresponseID.endDate <= :currentDate ) ")
                    .setParameter("jobresponseID", jobResponse)
                    .setParameter("currentDate", currentDate)
                    ;
            
            billFonction = new BillingFonction();            
            Iterator dateList = query.getResultList().iterator();

            while (dateList.hasNext()) {
                Object[] tuple = (Object[]) dateList.next();
                Date startDate = (Date) tuple[0];
                Date endDate = (Date) tuple[1];
                String date = String.valueOf(startDate.getTime()) + "##" + String.valueOf(endDate.getTime());
                String timezoneID = jobResponse.getJobID().getTimezoneID();
                //String date = billFonction.parseDateToString(startDate) + "##" + billFonction.parseDateToString(endDate);
                listResult.add(date +
                        ";" + jobResponse.getJobID().getTimezoneID() + 
                        ";" + DateFunction.formatDateToStringYMMMD(startDate) + "##" + DateFunction.formatDateToStringYMMMD(endDate)
                );
            }

            result.setObjectList(listResult);
            result.setMsg("good");

        } catch (Throwable th) {
            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"" + th.getMessage());
            result.setMsg("" + th.getMessage());
        }

        return result;
    }

    @Override
    public Result generateWeeklyTicket(int accountID, int jobResponseID, long startDate, long endDate) {

        
        Result result = new Result();

        List<DailyTicket> dailyTickets = null;

        List<String> listResult = new ArrayList<>();
        
        JobResponse jobResponse = null;
        Date startDateD = new Date(startDate);
        Date endDateD = new Date(endDate);
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"generateWeeklyTicket:  startDate = " + startDateD + "  endDate = " + endDateD);
        int totalLoad = 0;
        float totalHour = 0;
        float totalAmount = 0;

        try {

            ges.creatEntityManager();
            jobResponse = (JobResponse) ges.getEntityManager().find(JobResponse.class, jobResponseID);
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"generateWeeklyTicket: jobResponse: " + jobResponse);
            
            if (jobResponse == null) {
                result.setMsg("InvaLidJobResponseID");
                return result;
            }

            Object[] totalInfo = (Object[]) ges.getEntityManager().createQuery("SELECT SUM(j.numberOfLoad), (SUM( SQL('TIME_TO_SEC(?)', j.endTime) -  SQL('TIME_TO_SEC(?)', j.startTime))/3600), COUNT(DISTINCT j) FROM JobLog j WHERE j.deleted = false AND j.jobresponseID = :jobresponseID AND j.exDateLog BETWEEN :weeklyStartDate AND :weeklyEndDate")
                    .setParameter("jobresponseID", jobResponse)
                    .setParameter("weeklyStartDate", startDateD)
                    .setParameter("weeklyEndDate", endDateD)
                    .getSingleResult();
            
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"generateWeeklyTicket: "
                        +" \n totalLoad = " + totalInfo[0]
                        +" \n totalHour = " + totalInfo[1]
                        +" \n totalJobLog = " + totalInfo[2]
                );
            totalLoad = Integer.parseInt(String.valueOf(totalInfo[0]));
            totalHour = Float.parseFloat(String.valueOf(totalInfo[1]));
            int totalJobLog = Integer.parseInt(String.valueOf(totalInfo[2]));
            totalAmount = getTicketTotalAmount(jobResponse, totalHour, totalLoad, totalJobLog);
            
        } catch (Throwable th) {
           logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"generateWeeklyTicket: Error: " + th.getMessage());
        }

        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"generateWeeklyTicket:  totalAmount = " + totalAmount + " -- totalHour = " + totalHour + " -- totalLoad = " + totalLoad);
       
        String dailyTicketyInfo;

        try {

            dailyTickets = (List<DailyTicket>) ges.getEntityManager().createQuery("SELECT d FROM DailyTicket d WHERE d.deleted = FALSE AND d.ticketDate BETWEEN :weeklyStartDate AND :weeklyEndDate AND d.jobresponseID = :jobresponseID AND d.weeklyticketID.deleted = FALSE")
                    .setParameter("jobresponseID", jobResponse)
                    .setParameter("weeklyStartDate", startDateD)
                    .setParameter("weeklyEndDate", endDateD)
                    .getResultList();

        } catch (Exception e) {
            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"generateWeeklyTicket: Error: " + e.getMessage());
        }

        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"generateWeeklyTicket:  dailyTickets = " + dailyTickets);

        if (dailyTickets == null || dailyTickets.isEmpty()) {
            result.setMsg("NotExistingDailyTicket");
            result.afficherResult("generateWeeklyTicket");
            return result;
        }

        String res;
        for (DailyTicket dailyTicket : dailyTickets) {
            
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"generateWeeklyTicket:  Weeklyticket = " + dailyTicket.getWeeklyticketID());
            Object[] resultQuery;
            try {
                resultQuery = (Object[]) ges.getEntityManager().createQuery("SELECT MIN(j.startTime), MAX(j.endTime), SUM(j.numberOfLoad), (SUM( SQL('TIME_TO_SEC(?)', j.endTime) -  SQL('TIME_TO_SEC(?)', j.startTime))/3600), COUNT(DISTINCT j) FROM JobLog j WHERE j.deleted = false AND j.dailyticketID = :dailyTicket ")
                        .setParameter("dailyTicket", dailyTicket)
                        .getSingleResult();
            } catch (Exception e) {
                logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"generateWeeklyTicket: error --- " + e.getMessage());
                continue;
            }

            Date dailyStartTime = (Date) resultQuery[0];
            Date dailyEndTime = (Date) resultQuery[1];
            int dailyTotalLoad = Integer.parseInt(String.valueOf(resultQuery[2]));
            float dailyTotalHours = Float.parseFloat(String.valueOf(resultQuery[3]));
            int dailyTotalJobLog = Integer.parseInt(String.valueOf(resultQuery[4]));
            float dailyTotalAmount = getTicketTotalAmount(dailyTicket.getJobresponseID(), dailyTotalHours, dailyTotalLoad, dailyTotalJobLog);

            dailyTicketyInfo = "" + dailyTicket.getDailyticketID();
            dailyTicketyInfo += ";" + dailyTicket.getTicketDate().getTime();
            dailyTicketyInfo += ";" + DateConversion.formatDateToTimeAMPM(dailyStartTime);
            dailyTicketyInfo += ";" + DateConversion.formatDateToTimeAMPM(dailyEndTime);
            dailyTicketyInfo += ";" + dailyTotalLoad;
            dailyTicketyInfo += ";" + NumberProcessing.round(dailyTotalHours, 2);
            dailyTicketyInfo += ";" + dailyTicket.getState();
            dailyTicketyInfo += ";" + dailyTotalAmount;
            dailyTicketyInfo += ";" + (dailyTicket.getTruckID() == null ? "" : dailyTicket.getTruckID().getTruckID());
            dailyTicketyInfo += ";" + (dailyTicket.getTruckID() == null ? "" : dailyTicket.getTruckID().getTruckNumber());
            dailyTicketyInfo += ";" + "null";
            listResult.add(dailyTicketyInfo);

        }
        
        WeeklyTicket weeklyTicket = dailyTickets.get(0).getWeeklyticketID();
        double transactionFees = weeklyTicket.getTransactionFees();//pourcentage des frais de transaction

        double excavatorAmount = totalAmount * (1 + transactionFees / 100);
        double truckOwnerAmount = totalAmount * (1 - transactionFees / 100);
        
        String timezoneID = weeklyTicket.getJobresponseID().getJobID().getTimezoneID();
        Date jobStartDate = weeklyTicket.getJobresponseID().getJobID().getStartDate();
        Date jobEndDate = weeklyTicket.getJobresponseID().getJobID().getEndDate();
        
        res = "" + weeklyTicket.getJobresponseID().getJobresponseID();//0
        res += ";" + weeklyTicket.getJobresponseID().getJobID().getExcavatorID().getName();//1
        res += ";" + weeklyTicket.getJobresponseID().getJobID().getExcavatorID().getSurname();
        res += ";" + weeklyTicket.getJobresponseID().getJobID().getExcavatorID().getAddress();
        res += ";" + weeklyTicket.getJobresponseID().getJobID().getExcavatorID().getCellPhone();
        res += ";" + weeklyTicket.getJobresponseID().getJobID().getExcavatorID().getAccountID().getEmail();//5
        res += ";" + weeklyTicket.getJobresponseID().getTruckownerID().getName();
        res += ";" + weeklyTicket.getJobresponseID().getTruckownerID().getSurname();
        res += ";" + weeklyTicket.getJobresponseID().getTruckownerID().getAddress();
        res += ";" + weeklyTicket.getJobresponseID().getTruckownerID().getCellPhone();
        res += ";" + weeklyTicket.getJobresponseID().getTruckownerID().getAccountID().getEmail();//10

        res += ";" + (weeklyTicket.getJobresponseID().getJobID().getTypeofdirtID() == null ? "" : weeklyTicket.getJobresponseID().getJobID().getTypeofdirtID().getLabel());
        res += ";" + weeklyTicket.getJobresponseID().getJobID().getJobNumber();
        res += ";" + weeklyTicket.getJobresponseID().getJobID().getJobLocation();
        
        res += ";" + totalLoad;
        res += ";" + NumberProcessing.round(totalHour, 2);//15
        res += ";" + NumberProcessing.round(totalAmount, 2);

        res += ";" + weeklyTicket.getValidated();

        res += ";" + weeklyTicket.getWeeklyStartDate().getTime();
        res += ";" + weeklyTicket.getWeeklyEndDate().getTime();
        res += ";" + weeklyTicket.getSubmited();//20
        res += ";" + weeklyTicket.getJobresponseID().getJobID().getExcavatorID().getCompagnyName();
        res += ";" + weeklyTicket.getJobresponseID().getJobID().getExcavatorID().getTelephone();
        res += ";" + weeklyTicket.getJobresponseID().getTruckownerID().getCompagnyName();
        res += ";" + weeklyTicket.getJobresponseID().getTruckownerID().getTelephone();

        res += ";" + jobStartDate.getTime();//25
        res += ";" + jobEndDate.getTime();
        res += ";" + weeklyTicket.getWeeklyticketID();
        res += ";" + weeklyTicket.getPaid();
        res += ";" + weeklyTicket.getPaymentRecieve();//29
        res += ";" + NumberProcessing.round(excavatorAmount, 2);//30
        res += ";" + NumberProcessing.round(truckOwnerAmount, 2);//31
        res += ";" + weeklyTicket.getJobresponseID().getPaymenttypeID().getLibel();
        res += ";" + weeklyTicket.getJobresponseID().getBillingPrice();
        res += ";" + weeklyTicket.getSubmited();//34
        res += ";" + timezoneID;//35
        res += ";" + DateFunction.formatDateToStringYMMMD(weeklyTicket.getWeeklyStartDate());
        res += ";" + DateFunction.formatDateToStringYMMMD(weeklyTicket.getWeeklyEndDate());
        res += ";" + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(jobStartDate, timezoneID);
        res += ";" + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(jobEndDate, timezoneID);//39
        
        listResult.add(0, res);

        result.setMsg("good");
        result.setObjectList(listResult);
        result.afficherResult("generateWeeklyTicket");

        return result;
    }

    private float getTicketTotalAmount(JobResponse jobresponse, float ticketTotalHours, int ticketTotalLoad, int totalJobLog) {
        
        Integer paymenttypeID = jobresponse.getPaymenttypeID().getPaymenttypeID();
        float billingPrice = jobresponse.getBillingPrice();        
        float totalAmount; 
        
        switch(paymenttypeID){
            case TypeOfPayment.PAYMENT_TYPE_PER_HOUR:
                totalAmount = ticketTotalHours * billingPrice; break;
                
            case TypeOfPayment.PAYMENT_TYPE_PER_LOAD:
                totalAmount = ticketTotalLoad  *  billingPrice; break;
                
            case TypeOfPayment.PAYMENT_TYPE_PER_TON_PER_YD:
                totalAmount = billingPrice * totalJobLog; break;
            
            default: totalAmount = billingPrice * totalJobLog;
        }
        
        return totalAmount;
    }

    
    
    @Override
    public String validateWeeklyTicketFromExcavator(int excavatorID, int jobResponseID, long weeklyStartDate, long weeklyEndDate) throws BillingManagementException{
        
        String res = "";
        JobResponse jobResponse = null;
        try{
            
            ges.creatEntityManager();
            jobResponse = (JobResponse)ges.getEntityManager().find(JobResponse.class, jobResponseID);
            
            if(jobResponse == null){
                ges.closeEm();
                return "InvalidJobResponseID\n";
            }
            
            int updateCount = ges.getEntityManager().createQuery("UPDATE WeeklyTicket AS w SET w.validated = TRUE WHERE w.deleted = FALSE AND w.jobresponseID = :jobresponseID AND w.weeklyStartDate = :weeklyStartDate AND w.weeklyEndDate = :weeklyEndDate")
                    .setParameter("jobresponseID", jobResponse)
                    .setParameter("weeklyStartDate", new Date(weeklyStartDate))
                    .setParameter("weeklyEndDate", new Date(weeklyEndDate))
                    .executeUpdate();
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"validateWeeklyTicketFromExcavator: Nombre de WeeklyTicket modifié est : " + updateCount);
            
            res = "good";
            
        }catch (Throwable th) {
            res = th.getMessage();
            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"validateWeeklyTicketFromExcavator: Error: " + res);
            
             throw new BillingManagementException(getClass()+"","validateWeeklyTicketFromExcavator",1,res,res);
           
        }
        
        return res;
    }
    
    @Override
    public String validateWeeklyTicketFromTruckOwner(int truckOwnerID, int jobResponseID, long weeklyStartDate, long weeklyEndDate, String excavatorCode) throws BillingManagementException{
        
        String res = "";
        JobResponse jobResponse = null;
        try{
            
            ges.creatEntityManager();
            jobResponse = (JobResponse)ges.getEntityManager().find(JobResponse.class, jobResponseID);
            
            if(jobResponse == null){
                ges.closeEm();
                return "InvalidJobResponseID\n";
            }
            
            if (jobResponse.getJobID().getExcavatorID().getUserCode().equals(excavatorCode)) {
                int updateCount = ges.getEntityManager().createQuery("UPDATE WeeklyTicket AS w SET w.validated = TRUE WHERE w.deleted = FALSE AND w.jobresponseID = :jobresponseID AND w.weeklyStartDate = :weeklyStartDate AND w.weeklyEndDate = :weeklyEndDate")
                    .setParameter("jobresponseID", jobResponse)
                    .setParameter("weeklyStartDate", new Date(weeklyStartDate))
                    .setParameter("weeklyEndDate", new Date(weeklyEndDate))
                    .executeUpdate();
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"validateWeeklyTicketFromTruckOwner: Nombre de WeeklyTicket modifié est : " + updateCount);
                res = "good";
                
            } else {
                res = "invalidCode";
            }
        }catch(Throwable th){
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"validateWeeklyTicketFromTruckOwner: Error: "+res);
              throw new BillingManagementException(getClass()+"","validateWeeklyTicketFromTruckOwner",1,res,res);
           
        }
        
        return res;
    }

    
    @Override
    public String payWeeklyTicketByTruckOwner(int truckOwnerID, int jobResponseID, long weeklyStartDate, long weeklyEndDate) throws BillingManagementException{
        
        String res = "";
        JobResponse jobResponse = null;
        try{
            
            ges.creatEntityManager();
            jobResponse = (JobResponse)ges.getEntityManager().find(JobResponse.class, jobResponseID);
            
            if(jobResponse == null){
                ges.closeEm();
                return "InvalidJobResponseID\n";
            }
            
            
            int updateCount = ges.getEntityManager().createQuery("UPDATE DailyTicket AS d SET d.state = TRUE WHERE d.deleted = FALSE AND d.ticketDate >= :weeklyStartDate AND d.ticketDate <= :weeklyEndDate AND d.jobresponseID = :jobresponseID AND d.weeklyticketID.deleted = FALSE")
                    .setParameter("jobresponseID", jobResponse)
                    .setParameter("weeklyStartDate", new Date(weeklyStartDate))
                    .setParameter("weeklyEndDate", new Date(weeklyEndDate))
                    .executeUpdate();
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"payWeeklyTicketByTruckOwner: Nombre de DailyTicket validé(Par Camion) : " + updateCount);

            updateCount = ges.getEntityManager().createQuery("UPDATE WeeklyTicket AS w SET w.paymentRecieve = TRUE, w.settled = TRUE WHERE w.deleted = FALSE AND w.jobresponseID = :jobresponseID AND w.weeklyStartDate = :weeklyStartDate AND w.weeklyEndDate = :weeklyEndDate")
                    .setParameter("jobresponseID", jobResponse)
                    .setParameter("weeklyStartDate", new Date(weeklyStartDate))
                    .setParameter("weeklyEndDate", new Date(weeklyEndDate))
                    .executeUpdate();
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"payWeeklyTicketByTruckOwner: Nombre de WeeklyTicket considéré comme 'payé'(Par Camion) : " + updateCount);

            updateCount = ges.getEntityManager().createQuery("UPDATE JobLog AS j SET j.closed = TRUE WHERE j.deleted = FALSE AND j.exDateLog BETWEEN :weeklyStartDate AND :weeklyEndDate AND j.jobresponseID = :jobresponseID AND j.dailyticketID.deleted = FALSE ")
                    .setParameter("jobresponseID", jobResponse)
                    .setParameter("weeklyStartDate", new Date(weeklyStartDate))
                    .setParameter("weeklyEndDate", new Date(weeklyEndDate))
                    .executeUpdate();
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"payWeeklyTicketByTruckOwner: Nombre de logs fermé(Par Camion) : " + updateCount);

            ges.closeEm();
            res = "good";

        } catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"payWeeklyTicketByTruckOwner: res = " + res);
            
             throw new BillingManagementException(getClass()+"","payWeeklyTicketByTruckOwner",1,res,res);
        
            //return res;
        }
        
        return res;
    }

    
    @Override
    public ResultBackend payWeeklyTicketByExcavator(int excavatorID, int jobResponseID, long weeklyStartDate, long weeklyEndDate) {
        Date gmtDate = DateFunction.getGMTDate();
        
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"payWeeklyTicketByExcavator: Date d'appel: " + gmtDate +" || excavatorID = " + excavatorID + "     jobResponseID = " + jobResponseID+ "     weeklyStartDate = " + weeklyStartDate + "     weeklyEndDate = " + weeklyEndDate);
        
        String res = "";
        Account account;
        WeeklyTicket weeklyTicket = null;
        JobResponse jobResponse = null;
        ResultBackend result = new ResultBackend();
        
        try{
            
            ges.creatEntityManager();
            account = (Account)ges.getEntityManager().find(Account.class, excavatorID);
            
            if(account == null){
                ges.closeEm();
                result.setMsg("InvalidAccountID");
                return result;
            }
            
            jobResponse = (JobResponse)ges.getEntityManager().find(JobResponse.class, jobResponseID);
            
            if(jobResponse == null){
                ges.closeEm();
                result.setMsg("InvalidJobResponseID");
                return result;
            }
            
            float totalAmount;
            double transactionFees;
            
            Object[] totalInfo;
            
            try {
                totalInfo = (Object[]) ges.getEntityManager().createQuery("SELECT MAX(j.dailyticketID.weeklyticketID.transactionFees), (SUM( SQL('TIME_TO_SEC(?)', j.endTime) -  SQL('TIME_TO_SEC(?)', j.startTime))/3600), SUM(j.numberOfLoad), j.dailyticketID.weeklyticketID, COUNT(DISTINCT j) FROM JobLog j WHERE j.dailyticketID.weeklyticketID.paid = FALSE AND j.dailyticketID.weeklyticketID.submited = TRUE AND j.deleted = FALSE AND j.dailyticketID.weeklyticketID.deleted = FALSE AND j.jobresponseID = :jobresponseID AND j.dailyticketID.weeklyticketID.weeklyStartDate = :weeklyStartDate AND j.dailyticketID.weeklyticketID.weeklyEndDate = :weeklyEndDate")
                        .setParameter("jobresponseID", jobResponse)
                        .setParameter("weeklyStartDate", new Date(weeklyStartDate))
                        .setParameter("weeklyEndDate", new Date(weeklyEndDate))
                        .getSingleResult();

                transactionFees = Double.parseDouble(String.valueOf(totalInfo[0]));
                //logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"payWeeklyTicketByExcavator: transactionFees : " + transactionFees);
                float totalHour = Float.parseFloat(String.valueOf(totalInfo[1]));
                //logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"payWeeklyTicketByExcavator: totalHour : " + totalHour);
                int totalLoad = Integer.parseInt(String.valueOf(totalInfo[2]));
                //logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"payWeeklyTicketByExcavator: totalLoad : " + totalLoad);
                int totalJobLog = Integer.parseInt(String.valueOf(totalInfo[4]));
                //logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"payWeeklyTicketByExcavator: totalJobLog : " + totalJobLog);
                totalAmount = getTicketTotalAmount(jobResponse, totalHour, totalLoad, totalJobLog);
                //logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"payWeeklyTicketByExcavator: totalAmount : " + totalAmount);
                weeklyTicket = (WeeklyTicket) totalInfo[3];
                
            } catch (NoResultException ex) {
                logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"payWeeklyTicketByExcavator: Error : " + ex.getMessage());
                result.setMsg("BadQuery");
                return result;
            } catch (NumberFormatException ex) {
                logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"payWeeklyTicketByExcavator: Error : " + ex.getMessage());
                result.setMsg("NotYetSumited");
                return result;
            }

            if (totalInfo == null || totalInfo[0] == null || totalInfo[1] == null || totalInfo[2] == null) {
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"payWeeklyTicketByExcavator: return1:  NotYetSumited");
                result.setMsg("NotYetSumited");
                return result;
            }
            
            if(!weeklyTicket.getSubmited() || weeklyTicket.getTruckownermonthlybillID() == null || weeklyTicket.getExcavatormonthlybillID() == null){
                weeklyTicket.setSubmited(false);
                ges.getEntityManager().merge(weeklyTicket);
                ges.closeEm();
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"payWeeklyTicketByExcavator: return:  NotYetSumited");
                result.setMsg("NotYetSumited");
                return result;
            }
            
            double custToCamerAmount = totalAmount * (1 + transactionFees/100);
            double camerToCustAmount = totalAmount * (1 - transactionFees/100);
            
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"payWeeklyTicketByExcavator: weeklyTicket = " + weeklyTicket + " -- totalInfo[0] = " + totalInfo[0] + " --  totalInfo[1] = " + totalInfo[1] + " --  totalInfo[2] = " + totalInfo[2]);
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"payWeeklyTicketByExcavator: amount = " + totalAmount + " -- transactionFees = " + transactionFees);
            
            BillingReceiver excavatorBillingInfo = account.getBillingreceiverID();
            BillingReceiver truckOwnerBillingInfo = weeklyTicket.getJobresponseID().getTruckownerID().getAccountID().getBillingreceiverID();
            
            if(truckOwnerBillingInfo.getAccount().getUser().getAccountNumber() == null || truckOwnerBillingInfo.getAccount().getUser().getAccountNumber().equalsIgnoreCase("null")){
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"payWeeklyTicketByExcavator: return:  accountNumberNotYetDefined");
                result.setMsg("accountNumberNotYetDefined");
                return result;
            }
            
            if(truckOwnerBillingInfo.getAccount().getUser().getRoutineNumber() == null || truckOwnerBillingInfo.getAccount().getUser().getRoutineNumber().equalsIgnoreCase("null")){
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"payWeeklyTicketByExcavator: return:  routineNumberNotYetDefined");
                result.setMsg("routineNumberNotYetDefined");
                return result;
            }
            
            String customerID = excavatorBillingInfo.getApicustumID();// qui est l'id de celui qui paie dans notre plateforme sandbox
            String custAccountNumber = excavatorBillingInfo.getAccount().getUser().getAccountNumber();
            String custRoutingNumber = excavatorBillingInfo.getAccount().getUser().getRoutineNumber();
            
            String sellerID = truckOwnerBillingInfo.getApicustumID();// qui est l'id de celui qui recois dans notre plateforme sandbox
            String sellerAccountNumber = truckOwnerBillingInfo.getAccount().getUser().getAccountNumber();
            String sellerRoutingNumber = truckOwnerBillingInfo.getAccount().getUser().getRoutineNumber();
            
            billFonction = new BillingFonction();
            String weeklyStartDateString = billFonction.parseDateToString2(weeklyTicket.getWeeklyStartDate());
            String weeklyEndDateString = billFonction.parseDateToString2(weeklyTicket.getWeeklyEndDate());
            String jobNumber = weeklyTicket.getJobresponseID().getJobID().getJobNumber();
            String location = weeklyTicket.getJobresponseID().getJobID().getJobLocation();
            String truckOwnerName = billFonction.getTotalName(weeklyTicket.getJobresponseID().getTruckownerID().getName(), weeklyTicket.getJobresponseID().getTruckownerID().getSurname());
            String excavatorName = billFonction.getTotalName(weeklyTicket.getJobresponseID().getJobID().getExcavatorID().getName(), weeklyTicket.getJobresponseID().getJobID().getExcavatorID().getSurname());
            
            String custPaymentdescription = " Payment of the weekly ticket  going from " + weeklyStartDateString
                    + " to " + weeklyEndDateString + " for the job number " + jobNumber + " located at " + location +", executed by " + truckOwnerName
                    + " and published by " + excavatorName
                    + " On TRUCKS AND DIRT System"; // qui est la la raison du paiement.
            
            
            String sellPaymentdescription = " Payment of the weekly ticket  going from " + weeklyStartDateString
                    + " to " + weeklyEndDateString + " for the job number " + jobNumber + " located at " + location +", executed by " + truckOwnerName
                    + " and published by " + excavatorName
                    + " On TRUCKS AND DIRT System"; // qui est la la raison du paiement.
            
            
            CredentialInformation creInfo = new CredentialInformation();
            Properties props = null;
            try {
                props = creInfo.loadProperties();
            } catch (IOException ex) {
                logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"payWeeklyTicketByExcavator: Error: " + ex.getMessage());
                Logger.getLogger(BillingManagementDao.class.getName()).log(Level.SEVERE, null, ex);
                result.setMsg("NotExistingFileName");
                return result;
            }
            
            String endPoint = props.getProperty("endPoint");//"sandbox.usaepay.com";
            String sourceKey = props.getProperty("sourceKey");//"Bmy7457mqTfoOZAH9s51204z53Aa2v1Y";
            String sourcePin = props.getProperty("sourcePin");//"1234";
            String clientIpAddress = props.getProperty("clientIpAddress");//"127.0.0.1";
            String period = props.getProperty("period");//"Monthly";
            boolean enablePeriodicPayment = Boolean.parseBoolean(props.getProperty("enablePeriodicPayment"));//true;
            boolean sendReceipt = Boolean.parseBoolean(props.getProperty("sendReceipt"));//true;
            
            PaySimpleAP paySimple = new PaySimpleAP(endPoint, sourceKey, sourcePin, clientIpAddress, period, enablePeriodicPayment, sendReceipt);
            
            
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"payWeeklyTicketByExcavator: customerID = " + customerID + " --- custAccountNumber = " + custAccountNumber + " --- custRoutingNumber = " + custRoutingNumber + " --- custToCamerAmount = " + custToCamerAmount
                    + " --- camerToCustAmount = " + camerToCustAmount + " --- sellerID = " + sellerID + " --- sellerAccountNumber = " + sellerAccountNumber + " --- sellerRoutingNumber = " + sellerRoutingNumber 
                    + "    \n custPaymentdescription = " + custPaymentdescription+ "    \n sellPaymentdescription = " + sellPaymentdescription);
            
            MypaySimpleObj resPayMsg = paySimple.runPayment(customerID, custAccountNumber, custRoutingNumber, custToCamerAmount, camerToCustAmount, sellerID, sellerAccountNumber, sellerRoutingNumber, custPaymentdescription, sellPaymentdescription);
            res = resPayMsg.message;
            
            MonthlyBill truckOwnerMonthlyBill = weeklyTicket.getTruckownermonthlybillID();
            MonthlyBill excavatorMonthlyBill = weeklyTicket.getExcavatormonthlybillID();
            
            List<Object[]> resultArraysList = new ArrayList<>();
            if (res != null && res.equalsIgnoreCase("good")) {
                
                int senderREF = resPayMsg.sendRef;
                int receiverREF = resPayMsg.payRef;

                if (truckOwnerMonthlyBill.getSuscriptionAmount().doubleValue() == 0 && truckOwnerMonthlyBill.getMonthlyEndDate().after(gmtDate)) {
                    truckOwnerMonthlyBill.setPaid(true);
                    truckOwnerMonthlyBill.setPaymentDate(gmtDate);
                }
                
                if (excavatorMonthlyBill.getSuscriptionAmount().doubleValue() == 0 && excavatorMonthlyBill.getMonthlyEndDate().after(gmtDate)) {
                    excavatorMonthlyBill.setPaid(true);
                    excavatorMonthlyBill.setPaymentDate(gmtDate);
                }
                
                ges.getEntityManager().merge(truckOwnerMonthlyBill);
                ges.getEntityManager().merge(excavatorMonthlyBill);
                ges.closeEm();
                
                int updateCount = ges.getEntityManager().createQuery("UPDATE DailyTicket AS d SET d.state = TRUE WHERE d.deleted = FALSE AND d.ticketDate >= :weeklyStartDate AND d.ticketDate <= :weeklyEndDate AND d.jobresponseID = :jobresponseID AND d.weeklyticketID.deleted = FALSE")
                        .setParameter("jobresponseID", jobResponse)
                        .setParameter("weeklyStartDate", new Date(weeklyStartDate))
                        .setParameter("weeklyEndDate", new Date(weeklyEndDate))
                        .executeUpdate();
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"payWeeklyTicketByExcavator: Nombre de DailyTicket validé (Par Camion) : " + updateCount);

                updateCount = ges.getEntityManager().createQuery("UPDATE WeeklyTicket AS w SET w.paid = TRUE, w.senderREF = :senderREF, w.receiverREF = :receiverREF  WHERE w.deleted = FALSE AND w.jobresponseID = :jobresponseID AND w.weeklyStartDate = :weeklyStartDate AND w.weeklyEndDate = :weeklyEndDate")
                        .setParameter("jobresponseID", jobResponse)
                        .setParameter("senderREF", senderREF)
                        .setParameter("receiverREF", receiverREF)
                        .setParameter("weeklyStartDate", new Date(weeklyStartDate))
                        .setParameter("weeklyEndDate", new Date(weeklyEndDate))
                        .executeUpdate();
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"payWeeklyTicketByExcavator: Nombre de WeeklyTicket confirmé marqué à 'payé' est (Par Camion) : " + updateCount);
                
                updateCount = ges.getEntityManager().createQuery("UPDATE JobLog AS j SET j.closed = TRUE WHERE j.deleted = FALSE AND j.exDateLog BETWEEN :weeklyStartDate AND :weeklyEndDate AND j.jobresponseID = :jobresponseID AND j.dailyticketID.deleted = FALSE ")
                        .setParameter("jobresponseID", jobResponse)
                        .setParameter("weeklyStartDate", new Date(weeklyStartDate))
                        .setParameter("weeklyEndDate", new Date(weeklyEndDate))
                        .executeUpdate();
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"payWeeklyTicketByExcavator: Nombre de logs fermé (Par Camion) : " + updateCount);

                res = "good"; 
            } else if (res != null && (res.contains("IncorrectBillingInformation_sender") || res.contains("NotExistRoutingNumber_sender")) ){
                
                String[] excavatorInfo = {
                    excavatorMonthlyBill.getAccountID().getUser().getName(),
                    excavatorMonthlyBill.getAccountID().getUser().getSurname(),
                    excavatorMonthlyBill.getAccountID().getEmail(),
                    String.valueOf(excavatorMonthlyBill.getAccountID().getAccountNumber()),          
                };

                resultArraysList.add(excavatorInfo);
                result.setResultArraysList(resultArraysList);
            } else if (res != null && (res.contains("IncorrectBillingInformation_receiver") || res.contains("NotExistRoutingNumber_receiver")) ){
                
                String[] truckOwnerInfo = {
                    truckOwnerMonthlyBill.getAccountID().getUser().getName(),
                    truckOwnerMonthlyBill.getAccountID().getUser().getSurname(),
                    truckOwnerMonthlyBill.getAccountID().getEmail(),          
                    String.valueOf(truckOwnerMonthlyBill.getAccountID().getAccountNumber()),          
                };
                
                String[] msgContentData = {
                    truckOwnerMonthlyBill.getAccountID().getUser().getCompagnyName(),            
                };
                
                resultArraysList.add(truckOwnerInfo);
                resultArraysList.add(msgContentData);
                result.setResultArraysList(resultArraysList);
            }
            
            result.setMsg(res);
        } catch (Throwable th) {
            ges.rollbackTransaction();
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"payWeeklyTicketByExcavator: Error: " + res);
            result.setMsg(res);
            return result;
        }
        
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"payWeeklyTicketByExcavator: return:  res: " + res);
        return result;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfTruckOwnerWeeklyTicketHistory(int truckOwnerID, int index, int nombreMaxResult) {
        
        Result result = new Result();

        List<WeeklyTicket> weeklyTicketList = null;
        Account account = null;
        int numberOfElts = 0;
        List<String> listResult = new ArrayList<>();
        result.setMsg("good");
        
        try{
            
            ges.creatEntityManager();
            
            account = ges.getEntityManager().find(Account.class, truckOwnerID);
            
            if(account == null){
                result.setMsg("invalidAccountID");
                return result;
            }
            
            Query query = ges.getEntityManager().createQuery("SELECT w FROM WeeklyTicket w WHERE w.deleted = FALSE AND  w.jobresponseID.truckownerID = :truckownerID ORDER BY w.creationDate DESC")
                    .setParameter("truckownerID", account.getUser());
            
            numberOfElts = query.getResultList().size();
            
            weeklyTicketList = (List<WeeklyTicket>) query.setFirstResult(index)
                    .setMaxResults(nombreMaxResult)
                    .getResultList();
            
        }catch(Throwable th){
           //logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getARangeOfTruckOwnerWeeklyTicketHistory: Error: " + th.getMessage());
            logger.error("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"getARangeOfExcavatorWeeklyTicketHistory"+"\n"
                            +"error      : "+"getARangeOfTruckOwnerWeeklyTicketHistory: Error: " + th.getMessage());
            
             
        
        }
        
       // logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getARangeOfTruckOwnerWeeklyTicketHistory: weeklyTicketList = " + weeklyTicketList);
        logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"getARangeOfExcavatorWeeklyTicketHistory"+"\n"
                            +"info      : "+"getARangeOfTruckOwnerWeeklyTicketHistory: weeklyTicketList = " + weeklyTicketList);
            
              
        if (weeklyTicketList == null || weeklyTicketList.isEmpty()) {
            result.setMsg("NotGenerated");
            return result;
        }

        for (WeeklyTicket weeklyTicket : weeklyTicketList) {
            
            int totalLoad;
            float totalHour;
            float totalAmount;
            Object[] totalInfo;
            try {
                totalInfo = (Object[]) ges.getEntityManager().createQuery("SELECT SUM(j.numberOfLoad), (SUM( SQL('TIME_TO_SEC(?)', j.endTime) -  SQL('TIME_TO_SEC(?)', j.startTime))/3600), COUNT(DISTINCT j) FROM JobLog j WHERE j.deleted = false AND j.dailyticketID.weeklyticketID.deleted = FALSE AND j.dailyticketID.weeklyticketID = :weeklyTicket")
                        .setParameter("weeklyTicket", weeklyTicket)
                        .getSingleResult();
                /*
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getARangeOfTruckOwnerWeeklyTicketHistory: "
                        + " \n weeklyTicket = " + weeklyTicket
                        + " \n weeklyTicket.DELETED = " + weeklyTicket.getDeleted()
                        + " \n totalInfo[0] = " + totalInfo[0]
                        + " \n totalInfo[1] = " + totalInfo[1]
                );
                */
                 logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"getARangeOfExcavatorWeeklyTicketHistory"+"\n"
                            +"error      : "+"getARangeOfTruckOwnerWeeklyTicketHistory: "
                        + " \n weeklyTicket = " + weeklyTicket
                        + " \n weeklyTicket.DELETED = " + weeklyTicket.getDeleted()
                        + " \n totalInfo[0] = " + totalInfo[0]
                        + " \n totalInfo[1] = " + totalInfo[1]
               );
            
              
                totalLoad = Integer.parseInt(String.valueOf(totalInfo[0]));
                totalHour = Float.parseFloat(String.valueOf(totalInfo[1]));
                int totalJobLog = Integer.parseInt(String.valueOf(totalInfo[2]));
                totalAmount = getTicketTotalAmount(weeklyTicket.getJobresponseID(), totalHour, totalLoad, totalJobLog);

            } catch (NumberFormatException e) {
               // logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getARangeOfTruckOwnerWeeklyTicketHistory: Error: " + e.getMessage());
               
                 logger.error("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"getARangeOfExcavatorWeeklyTicketHistory"+"\n"
                            +"error      : "+"getARangeOfTruckOwnerWeeklyTicketHistory: Error: " + e.getMessage());
            
              
                
                continue;
            }
            String res;
            Date jobStartDate = weeklyTicket.getJobresponseID().getJobID().getStartDate();
            Date jobEndDate = weeklyTicket.getJobresponseID().getJobID().getEndDate();
            String timezoneID = weeklyTicket.getJobresponseID().getJobID().getTimezoneID();
            
            res = "" + weeklyTicket.getJobresponseID().getJobresponseID();//0
            res += ";" + weeklyTicket.getJobresponseID().getJobID().getExcavatorID().getName();//******
            res += ";" + weeklyTicket.getJobresponseID().getJobID().getExcavatorID().getSurname();//******
            res += ";" + weeklyTicket.getJobresponseID().getJobID().getExcavatorID().getAddress();
            res += ";" + weeklyTicket.getJobresponseID().getJobID().getExcavatorID().getCellPhone();
            res += ";" + weeklyTicket.getJobresponseID().getJobID().getExcavatorID().getAccountID().getEmail();//5
            res += ";" + weeklyTicket.getJobresponseID().getTruckownerID().getName();
            res += ";" + weeklyTicket.getJobresponseID().getTruckownerID().getSurname();
            res += ";" + weeklyTicket.getJobresponseID().getTruckownerID().getAddress();
            res += ";" + weeklyTicket.getJobresponseID().getTruckownerID().getCellPhone();
            res += ";" + weeklyTicket.getJobresponseID().getTruckownerID().getAccountID().getEmail();//10
            
            res += ";" + (weeklyTicket.getJobresponseID().getJobID().getTypeofdirtID() == null ? "" : weeklyTicket.getJobresponseID().getJobID().getTypeofdirtID().getLabel());
            res += ";" + weeklyTicket.getJobresponseID().getJobID().getJobNumber();//******
            res += ";" + weeklyTicket.getJobresponseID().getJobID().getJobLocation();//*******
            res += ";" + totalLoad;//******
            res += ";" + totalHour;//15
            res += ";" + totalAmount;//******
            res += ";" + weeklyTicket.getValidated();
            
            res += ";" + weeklyTicket.getWeeklyStartDate().getTime();//******
            res += ";" + weeklyTicket.getWeeklyEndDate().getTime();//******
            res += ";" + weeklyTicket.getSubmited();//20
            res += ";" + weeklyTicket.getJobresponseID().getJobID().getExcavatorID().getCompagnyName();
            res += ";" + weeklyTicket.getJobresponseID().getJobID().getExcavatorID().getTelephone();
            res += ";" + weeklyTicket.getJobresponseID().getTruckownerID().getCompagnyName();
            res += ";" + weeklyTicket.getJobresponseID().getTruckownerID().getTelephone();
            
            res += ";" + jobStartDate.getTime();//25
            res += ";" + jobEndDate.getTime();//26
            res += ";" + weeklyTicket.getWeeklyticketID();//******
            res += ";" + weeklyTicket.getPaid();//******
            res += ";" + weeklyTicket.getPaymentRecieve();//******
            res += ";" + weeklyTicket.getSettled();//30
            res += ";" + weeklyTicket.getJobresponseID().getCreationDate().getTime();
            res += ";" + timezoneID;//32
            res += ";" + DateFunction.formatDateToStringYMMMD(weeklyTicket.getWeeklyStartDate());
            res += ";" + DateFunction.formatDateToStringYMMMD(weeklyTicket.getWeeklyEndDate());
            res += ";" + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(jobStartDate, timezoneID);//35
            res += ";" + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(jobEndDate, timezoneID);//36
            
            //logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getARangeOfTruckOwnerWeeklyTicketHistory: result: res = " + res);
            logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"getARangeOfExcavatorWeeklyTicketHistory"+"\n"
                            +"info      : "+"getARangeOfTruckOwnerWeeklyTicketHistory: result: res = " + res);
            
                
            
            listResult.add(res);
            
        }
        
        listResult.add("" + numberOfElts);
        result.setMsg("good");
        result.setObjectList(listResult);

        return result;
        
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfExcavatorWeeklyTicketHistory(int excavatorID, int index, int nombreMaxResult) {
        
        Result result = new Result();

        List<WeeklyTicket> weeklyTicketList = null;
        Account account = null;
        int numberOfElts = 0;
        List<String> listResult = new ArrayList<>();
        result.setMsg("good");
        
        try{
            
            ges.creatEntityManager();
            
            account = ges.getEntityManager().find(Account.class, excavatorID);
            
            if(account == null){
                result.setMsg("invalidAccountID");
                return result;
            }
            
            Query query = ges.getEntityManager().createQuery("SELECT w FROM WeeklyTicket w WHERE w.deleted = FALSE AND  w.jobresponseID.jobID.excavatorID = :excavatorID ORDER BY w.creationDate DESC ")
                    .setParameter("excavatorID", account.getUser());
            
            numberOfElts = query.getResultList().size();
            weeklyTicketList = (List<WeeklyTicket>) query.setFirstResult(index)
                    .setMaxResults(nombreMaxResult)
                    .getResultList();

        } catch (Throwable th){
            //System.err.println("getARangeOfExcavatorWeeklyTicketHistory: Error: " + th.getMessage());
          logger.error("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"getARangeOfExcavatorWeeklyTicketHistory"+"\n"
                            +"error      : "+"getARangeOfExcavatorWeeklyTicketHistory: Error: " + th.getMessage());
            
                
        
        }
        
       // logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getARangeOfExcavatorWeeklyTicketHistory: weeklyTicketList = " + weeklyTicketList);
        
        logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"getARangeOfExcavatorWeeklyTicketHistory"+"\n"
                            +"info      : "+ "getARangeOfExcavatorWeeklyTicketHistory: weeklyTicketList = " + weeklyTicketList);
            
                
        if (weeklyTicketList == null || weeklyTicketList.isEmpty()) {
            result.setMsg("NotGenerated");
            return result;
        }

        for (WeeklyTicket weeklyTicket : weeklyTicketList) {
            
            String res;
            Date jobStartDate = weeklyTicket.getJobresponseID().getJobID().getStartDate();
            Date jobEndDate = weeklyTicket.getJobresponseID().getJobID().getEndDate();
            String timezoneID = weeklyTicket.getJobresponseID().getJobID().getTimezoneID();
            
            int totalLoad;
            float totalHour;
            float totalAmount;
            Object[] totalInfo;
            try {
                totalInfo = (Object[]) ges.getEntityManager().createQuery("SELECT SUM(j.numberOfLoad), (SUM( SQL('TIME_TO_SEC(?)', j.endTime) -  SQL('TIME_TO_SEC(?)', j.startTime))/3600), COUNT(DISTINCT j) FROM JobLog j WHERE j.deleted = false AND j.dailyticketID.weeklyticketID.deleted = FALSE AND j.dailyticketID.weeklyticketID = :weeklyTicket")
                        .setParameter("weeklyTicket", weeklyTicket)
                        .getSingleResult();
                /*
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getARangeOfExcavatorWeeklyTicketHistory: "
                        + " \n weeklyTicket = " + weeklyTicket
                        + " \n weeklyTicket.DELETED = " + weeklyTicket.getDeleted()
                        + " \n totalInfo[0] = " + totalInfo[0]
                        + " \n totalInfo[1] = " + totalInfo[1]
                );
                */
                 logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"getARangeOfExcavatorWeeklyTicketHistory"+"\n"
                            +"info     : "+"getARangeOfExcavatorWeeklyTicketHistory: "
                        + " \n weeklyTicket = " + weeklyTicket
                        + " \n weeklyTicket.DELETED = " + weeklyTicket.getDeleted()
                        + " \n totalInfo[0] = " + totalInfo[0]
                        + " \n totalInfo[1] = " + totalInfo[1]);
            
                
                totalLoad = Integer.parseInt(String.valueOf(totalInfo[0]));
                totalHour = Float.parseFloat(String.valueOf(totalInfo[1]));
                int totalJobLog = Integer.parseInt(String.valueOf(totalInfo[2]));
                totalAmount = getTicketTotalAmount(weeklyTicket.getJobresponseID(), totalHour, totalLoad, totalJobLog);

            } catch (NumberFormatException e) {
              //  logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getARangeOfExcavatorWeeklyTicketHistory: Error: " + e.getMessage());
                logger.error("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"getARangeOfExcavatorWeeklyTicketHistory"+"\n"
                            +"error      : "+ "getARangeOfExcavatorWeeklyTicketHistory: Error: " + e.getMessage());
            
                
                continue;
            }
            
            res = "" + weeklyTicket.getJobresponseID().getJobresponseID();//0
            res += ";" + weeklyTicket.getJobresponseID().getJobID().getExcavatorID().getName();
            res += ";" + weeklyTicket.getJobresponseID().getJobID().getExcavatorID().getSurname();
            res += ";" + weeklyTicket.getJobresponseID().getJobID().getExcavatorID().getAddress();
            res += ";" + weeklyTicket.getJobresponseID().getJobID().getExcavatorID().getCellPhone();
            res += ";" + weeklyTicket.getJobresponseID().getJobID().getExcavatorID().getAccountID().getEmail();//5
            res += ";" + weeklyTicket.getJobresponseID().getTruckownerID().getName();//******
            res += ";" + weeklyTicket.getJobresponseID().getTruckownerID().getSurname();//******
            res += ";" + weeklyTicket.getJobresponseID().getTruckownerID().getAddress();
            res += ";" + weeklyTicket.getJobresponseID().getTruckownerID().getCellPhone();
            res += ";" + weeklyTicket.getJobresponseID().getTruckownerID().getAccountID().getEmail();//10
            
            res += ";" + (weeklyTicket.getJobresponseID().getJobID().getTypeofdirtID() == null ? "" : weeklyTicket.getJobresponseID().getJobID().getTypeofdirtID().getLabel());
            res += ";" + weeklyTicket.getJobresponseID().getJobID().getJobNumber();//******
            res += ";" + weeklyTicket.getJobresponseID().getJobID().getJobLocation();//*******
            res += ";" + totalLoad;//******
            res += ";" + totalHour;//15
            res += ";" + totalAmount;//******
            res += ";" + weeklyTicket.getValidated();
            
            res += ";" + weeklyTicket.getWeeklyStartDate().getTime();//******
            res += ";" + weeklyTicket.getWeeklyEndDate().getTime();//******
            res += ";" + weeklyTicket.getSubmited();//20
            res += ";" + weeklyTicket.getJobresponseID().getJobID().getExcavatorID().getCompagnyName();
            res += ";" + weeklyTicket.getJobresponseID().getJobID().getExcavatorID().getTelephone();
            res += ";" + weeklyTicket.getJobresponseID().getTruckownerID().getCompagnyName();
            res += ";" + weeklyTicket.getJobresponseID().getTruckownerID().getTelephone();
            
            res += ";" + jobStartDate.getTime();//25
            res += ";" + jobEndDate.getTime();//******
            res += ";" + weeklyTicket.getWeeklyticketID();//******
            res += ";" + weeklyTicket.getPaid();//******
            res += ";" + weeklyTicket.getPaymentRecieve();//******
            res += ";" + weeklyTicket.getSettled();//******
            res += ";" + weeklyTicket.getJobresponseID().getJobID().getCreationDate().getTime();//31
            res += ";" + timezoneID;//32
            res += ";" + DateFunction.formatDateToStringYMMMD(weeklyTicket.getWeeklyStartDate());
            res += ";" + DateFunction.formatDateToStringYMMMD(weeklyTicket.getWeeklyEndDate());
            res += ";" + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(jobStartDate, timezoneID);//35
            res += ";" + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(jobEndDate, timezoneID);//36
            
           // logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getARangeOfExcavatorWeeklyTicketHistory: result: res = " + res);
             logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"getARangeOfExcavatorWeeklyTicketHistory"+"\n"
                            +"info      : "+ "getARangeOfExcavatorWeeklyTicketHistory: result: res = " + res);
            
            
            listResult.add(res);
            
        }
        result.setMsg("good");
        listResult.add("" + numberOfElts);
        result.setObjectList(listResult);

        return result;
        
    }

    
    
    @Override
    public String submitDailyTicketByTruckOwner(int truckOwnerID, int jobResponseID, long ticketDate) throws BillingManagementException{
        
        String res = "";
        JobResponse jobResponse = null;
        try{
            
            ges.creatEntityManager();
            jobResponse = (JobResponse)ges.getEntityManager().find(JobResponse.class, jobResponseID);
            
            if(jobResponse == null){
                ges.closeEm();
                return "InvalidJobResponseID\n";
            }
            
           // logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"submitDailyTicketByTruckOwner: TicketDate : " + new Date(ticketDate));
            
             logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"submitWeeklyTicketByTruckOwner"+"\n"
                            +"info      : "+ "submitDailyTicketByTruckOwner: TicketDate : " + new Date(ticketDate));
            
            int updateCount = ges.getEntityManager().createQuery("UPDATE DailyTicket AS d SET d.viewTicket = TRUE WHERE d.deleted = FALSE AND d.jobresponseID = :jobresponseID AND d.ticketDate = :ticketDate")
                    .setParameter("jobresponseID", jobResponse)
                    .setParameter("ticketDate", new Date(ticketDate))
                    .executeUpdate();
           // logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"submitDailyTicketByTruckOwner: Nombre de ticket modifié est : " + updateCount);
            
             logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"submitWeeklyTicketByTruckOwner"+"\n"
                            +"info      : "+ "submitDailyTicketByTruckOwner: Nombre de ticket modifié est : " + updateCount);
           
            res = (updateCount <= 0 ? "InvalidDateTicket" : "good");
            
        }catch(Throwable th){
            res = "" + th.getMessage();
             throw new BillingManagementException(getClass()+"","submitDailyTicketByTruckOwner",1,res,res);
        
        }
        
        //logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"submitDailyTicketByTruckOwner: res = : " + res);
       
          logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"submitWeeklyTicketByTruckOwner"+"\n"
                            +"info      : "+ "submitDailyTicketByTruckOwner: res = : " + res);
           
        return res;
    }

    
    @Override
    public String submitWeeklyTicketByTruckOwner(int truckOwnerID, int jobResponseID, long weeklyStartDate, long weeklyEndDate) throws BillingManagementException{
        
        String res = "";
        JobResponse jobResponse;
        Account account;
        try{
            
            ges.creatEntityManager();
            jobResponse = (JobResponse)ges.getEntityManager().find(JobResponse.class, jobResponseID);
            account = (Account)ges.getEntityManager().find(Account.class, truckOwnerID);
            
            if(jobResponse == null){
                ges.closeEm();
                return "InvalidJobResponseID";
            }
            
            if(account == null){
                ges.closeEm();
                return "InvalidTruckOwnerID";
            }
            MonthlyBill truckOwnerMonthlyBill = null;
            try {
                truckOwnerMonthlyBill = (MonthlyBill) ges.getEntityManager().createQuery("SELECT m FROM MonthlyBill m WHERE m.deleted = FALSE AND m.accountID = :accountID AND :currentDate BETWEEN m.monthlyStartDate AND m.monthlyEndDate")
                        .setParameter("currentDate", DateFunction.getGMTDate())
                        .setParameter("accountID", account)
                        .getSingleResult();
            } catch (Exception e) {
            }
            
            MonthlyBill excavatorMonthlyBill = null;
            try {
                excavatorMonthlyBill = (MonthlyBill) ges.getEntityManager().createQuery("SELECT m FROM MonthlyBill m WHERE m.deleted = FALSE AND m.accountID = :accountID AND :currentDate BETWEEN m.monthlyStartDate AND m.monthlyEndDate")
                        .setParameter("currentDate", DateFunction.getGMTDate())
                        .setParameter("accountID", jobResponse.getJobID().getExcavatorID().getAccountID())
                        .getSingleResult();
            } catch (Exception e) {
            }
            
            if (truckOwnerMonthlyBill == null) {
                return "NotExistingTruckownerMonthlyBill";
            }
            
            if (excavatorMonthlyBill == null) {
                return "NotExistingExcavatorMonthlyBill";
            }
            ges.closeEm();

            int updateCount = ges.getEntityManager().createQuery("UPDATE WeeklyTicket AS w SET w.submited = TRUE, w.validated = TRUE, w.excavatormonthlybillID = :excavatormonthlybillID, w.truckownermonthlybillID = :truckownermonthlybillID WHERE w.submited = FALSE AND w.deleted = FALSE AND w.jobresponseID = :jobresponseID AND w.weeklyStartDate = :weeklyStartDate AND w.weeklyEndDate = :weeklyEndDate")
                    .setParameter("jobresponseID", jobResponse)
                    .setParameter("excavatormonthlybillID", excavatorMonthlyBill)
                    .setParameter("truckownermonthlybillID", truckOwnerMonthlyBill)
                    .setParameter("weeklyStartDate", new Date(weeklyStartDate))
                    .setParameter("weeklyEndDate", new Date(weeklyEndDate))
                    .executeUpdate();
           // logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"submitWeeklyTicketByTruckOwner: Nombre de ticket modifié est : " + updateCount);
              logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"submitWeeklyTicketByTruckOwner"+"\n"
                            +"info      : "+ "submitWeeklyTicketByTruckOwner: Nombre de ticket modifié est : " + updateCount);
           
            if (updateCount == 0) {
                res = "AlReadySubmited";
            } else {
                res = "good";
            }
            
        }catch(Throwable th){
            th.printStackTrace();
            ges.rollbackTransaction();
            res = "" + th.getMessage();
             throw new BillingManagementException(getClass()+"","submitWeeklyTicketByTruckOwner",1,res,res);
        
        }
        
       // logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"submitWeeklyTicketByTruckOwner: res = " + res);
        
          logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"submitWeeklyTicketByTruckOwner"+"\n"
                            +"info      : "+ "submitWeeklyTicketByTruckOwner: res = " + res);
           
        return res;
    }

    
    @Override
    public Result loadMonthlyBill(int accountID, long monthlyStartDate, long monthlyEndDate) {

        Result result = new Result();
        List<String> listResult = new ArrayList<>();
        List<WeeklyTicket> weeklyTickets = new ArrayList<>();
        
        Account account = null;
        Date monthlyStartDateD = new Date(monthlyStartDate);
        Date monthlyEndDateD = new Date(monthlyEndDate);
       // logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"loadMonthlyBill:  monthlyStartDate = " + monthlyStartDateD + "  monthlyEndDate = " + monthlyEndDateD);
        
         logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"loadMonthlyBill"+"\n"
                            +"info      : "+ "loadMonthlyBill:  monthlyStartDate = " + monthlyStartDateD + "  monthlyEndDate = " + monthlyEndDateD);
           
        MonthlyBill monthlyBill = null;
        int socialstatusID = 0;

        try {

            ges.creatEntityManager();
            account = (Account) ges.getEntityManager().find(Account.class, accountID);
           // logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"loadMonthlyBill: jobResponse: " + account);
            
             logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"loadMonthlyBill"+"\n"
                            +"info      : "+ "loadMonthlyBill: jobResponse: " + account);
           
            if (account == null) {
                result.setMsg("InvalidAccountID");
                return result;
            }
            monthlyBill = (MonthlyBill) ges.getEntityManager().createQuery("SELECT m FROM MonthlyBill m WHERE m.deleted = FALSE AND m.accountID = :accountID AND m.monthlyStartDate = :monthlyStartDate AND m.monthlyEndDate = :monthlyEndDate")
                    .setParameter("monthlyStartDate", monthlyStartDateD)
                    .setParameter("monthlyEndDate", monthlyEndDateD)
                    .setParameter("accountID", account)
                    .getSingleResult();
            
            socialstatusID = account.getSocialstatusID().getSocialstatusID();
            if (socialstatusID == TypeOfUser.SOCIAL_STATUS_TRUCK_OWNER_ID) {
                weeklyTickets = (List<WeeklyTicket>) ges.getEntityManager().createQuery("SELECT a FROM WeeklyTicket a WHERE a.deleted = false AND a.truckownermonthlybillID = :truckownermonthlybillID")
                        .setParameter("truckownermonthlybillID", monthlyBill)
                        .getResultList();
            } else if (socialstatusID == TypeOfUser.SOCIAL_STATUS_EXCAVATOR_ID || socialstatusID == TypeOfUser.SOCIAL_STATUS_SIMPLE_USER_ID){
                
                weeklyTickets = (List<WeeklyTicket>) ges.getEntityManager().createQuery("SELECT a FROM WeeklyTicket a WHERE a.deleted = false AND a.excavatormonthlybillID = :excavatormonthlybillID")
                        .setParameter("excavatormonthlybillID", monthlyBill)
                        .getResultList();
            }

        } catch (Throwable th) {
            //System.err.println("loadMonthlyBill: Error: " + th.getMessage());
              logger.error("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"loadMonthlyBill"+"\n"
                            +"error      : "+ th.getMessage());
           
        }

        //logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"loadMonthlyBill:  weeklyTickets = " + weeklyTickets);
          logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"loadMonthlyBill"+"\n"
                            +"info      : "+ "loadMonthlyBill:  weeklyTickets = " + weeklyTickets);
           
        if (monthlyBill == null) {
            result.setMsg("NotExistingMonthlyBill");
            result.afficherResult("loadMonthlyBill");
            return result;
        }
        /*
        if (weeklyTickets == null || weeklyTickets.isEmpty()) {
        result.setMsg("NotExistingWeeklyTicket");
        result.afficherResult("loadMonthlyBill");
        return result;
        }
        */
        String weeklyTicketyInfo;
        String res;
        double totalAmount = 0;
        listResult.add("0");
        if (socialstatusID == TypeOfUser.SOCIAL_STATUS_TRUCK_OWNER_ID) {
            for (WeeklyTicket weeklyTicket : weeklyTickets) {

                Object[] totalInfo = (Object[]) ges.getEntityManager().createQuery("SELECT  (SUM( SQL('TIME_TO_SEC(?)', a.endTime) -  SQL('TIME_TO_SEC(?)', a.startTime))/3600), SUM(a.numberOfLoad), COUNT(DISTINCT a) FROM JobLog a WHERE a.deleted = false AND a.jobresponseID = :jobresponseID AND a.dailyticketID.weeklyticketID = :weeklyTicket ")
                        .setParameter("jobresponseID", weeklyTicket.getJobresponseID())
                        .setParameter("weeklyTicket", weeklyTicket)
                        .getSingleResult();

                double transactionFees = weeklyTicket.getTransactionFees();//pourcentage des frais de transaction
                float totalHour = Float.parseFloat(String.valueOf(totalInfo[0]));
                int totalLoad = Integer.parseInt(String.valueOf(totalInfo[1]));
                int totalJobLog = Integer.parseInt(String.valueOf(totalInfo[2]));
                double truckOwnerAmount = getTicketTotalAmount(weeklyTicket.getJobresponseID(), totalHour, totalLoad, totalJobLog) * (transactionFees / 100);

                totalAmount += truckOwnerAmount;
                Date jobRespStartDate = weeklyTicket.getJobresponseID().getStartDate();
                Date jobRespEndDate = weeklyTicket.getJobresponseID().getEndDate();
                String jobTimeZoneID = weeklyTicket.getJobresponseID().getJobID().getTimezoneID();

                weeklyTicketyInfo = "" + jobRespStartDate.getTime();//00
                weeklyTicketyInfo += ";" + jobRespEndDate.getTime();//01
                weeklyTicketyInfo += ";" + weeklyTicket.getJobresponseID().getJobID().getJobNumber();//02
                weeklyTicketyInfo += ";" + weeklyTicket.getJobresponseID().getJobID().getJobLocation();
                weeklyTicketyInfo += ";" + weeklyTicket.getWeeklyStartDate().getTime();
                weeklyTicketyInfo += ";" + weeklyTicket.getWeeklyEndDate().getTime();//05
                weeklyTicketyInfo += ";" + NumberProcessing.round(truckOwnerAmount, 2);
                weeklyTicketyInfo += ";" + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(jobRespStartDate, jobTimeZoneID);//07
                weeklyTicketyInfo += ";" + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(jobRespEndDate, jobTimeZoneID);//08
                weeklyTicketyInfo += ";" + DateFunction.formatDateToStringYMMMD(weeklyTicket.getWeeklyStartDate());//09
                weeklyTicketyInfo += ";" + DateFunction.formatDateToStringYMMMD(weeklyTicket.getWeeklyEndDate());//10
                weeklyTicketyInfo += ";" + "null";
                listResult.add(weeklyTicketyInfo);
                
            }
        } else if (socialstatusID == TypeOfUser.SOCIAL_STATUS_EXCAVATOR_ID || socialstatusID == TypeOfUser.SOCIAL_STATUS_SIMPLE_USER_ID) {
            for (WeeklyTicket weeklyTicket : weeklyTickets) {
                
                Object[] totalInfo = (Object[]) ges.getEntityManager().createQuery("SELECT  (SUM( SQL('TIME_TO_SEC(?)', a.endTime) -  SQL('TIME_TO_SEC(?)', a.startTime))/3600), SUM(a.numberOfLoad), COUNT(DISTINCT a) FROM JobLog a WHERE a.deleted = false AND a.jobresponseID = :jobresponseID AND a.dailyticketID.weeklyticketID = :weeklyTicket ")
                        .setParameter("jobresponseID", weeklyTicket.getJobresponseID())
                        .setParameter("weeklyTicket", weeklyTicket)
                        .getSingleResult();

                double transactionFees = weeklyTicket.getTransactionFees();//pourcentage des frais de transaction
                float totalHour = Float.parseFloat(String.valueOf(totalInfo[0]));
                int totalLoad = Integer.parseInt(String.valueOf(totalInfo[1]));
                int totalJobLog = Integer.parseInt(String.valueOf(totalInfo[2]));
                double excavatorAmount = getTicketTotalAmount(weeklyTicket.getJobresponseID(), totalHour, totalLoad, totalJobLog) * (transactionFees / 100);
                
                totalAmount += excavatorAmount;
                Date jobStartDate = weeklyTicket.getJobresponseID().getJobID().getStartDate();
                Date jobEndDate = weeklyTicket.getJobresponseID().getJobID().getEndDate();
                String jobTimeZoneID = weeklyTicket.getJobresponseID().getJobID().getTimezoneID();
                
                weeklyTicketyInfo = "" + jobStartDate.getTime();//00
                weeklyTicketyInfo += ";" + jobEndDate.getTime();//01
                weeklyTicketyInfo += ";" + weeklyTicket.getJobresponseID().getJobID().getJobNumber();
                weeklyTicketyInfo += ";" + weeklyTicket.getJobresponseID().getJobID().getJobLocation();
                weeklyTicketyInfo += ";" + weeklyTicket.getWeeklyStartDate().getTime();
                weeklyTicketyInfo += ";" + weeklyTicket.getWeeklyEndDate().getTime();//05
                weeklyTicketyInfo += ";" + NumberProcessing.round(excavatorAmount, 2);
                weeklyTicketyInfo += ";" + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(jobStartDate, jobTimeZoneID);//07
                weeklyTicketyInfo += ";" + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(jobEndDate, jobTimeZoneID);//08
                weeklyTicketyInfo += ";" + DateFunction.formatDateToStringYMMMD(weeklyTicket.getWeeklyStartDate());//09
                weeklyTicketyInfo += ";" + DateFunction.formatDateToStringYMMMD(weeklyTicket.getWeeklyEndDate());//10
                weeklyTicketyInfo += ";" + (weeklyTicket.getPaid() || weeklyTicket.getPaymentRecieve());//11
                weeklyTicketyInfo += ";" + "null";
                listResult.add(weeklyTicketyInfo);
                
            }
        }
        
        double amountDue = totalAmount + monthlyBill.getSuscriptionAmount().doubleValue();
        
        res = "" + monthlyBill.getMonthlybillID();//0
        res += ";" + monthlyBill.getAccountID().getUser().getName();
        res += ";" + monthlyBill.getAccountID().getUser().getSurname();
        res += ";" + monthlyBill.getAccountID().getUser().getAddress();
        res += ";" + monthlyBill.getAccountID().getUser().getCellPhone();
        res += ";" + monthlyBill.getAccountID().getEmail();//5
        res += ";" + monthlyBill.getAccountID().getUser().getCompagnyName();
        res += ";" + monthlyBill.getAccountID().getUser().getTelephone();
        res += ";" + NumberProcessing.round(amountDue, 2);//8
        res += ";" + NumberProcessing.round(totalAmount, 2);//9
        res += ";" + NumberProcessing.round(monthlyBill.getSuscriptionAmount(), 2);//10
        res += ";" + monthlyBill.getAccountID().getAccountNumber();

        listResult.set(0, res);

        result.setMsg("good");
        result.setObjectList(listResult);
        result.afficherResult("loadMonthlyBill");

        return result;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getMonthyDateForPersonnalBilling(int accountID) {

        Result result = new Result();

        List<String> listResult = new ArrayList<>();

        Account account;
        result.setMsg("unknown");

        try {

            ges.creatEntityManager();
            account = (Account) ges.getEntityManager().find(Account.class, accountID);

            if (account == null) {
                result.setMsg("InvaLidAccountID");
                return result;
            }

            Query query;
            
            query = ges.getEntityManager().createQuery("SELECT DISTINCT a.monthlyStartDate, a.monthlyEndDate FROM MonthlyBill a WHERE a.deleted = FALSE AND a.accountID = :accountID ORDER BY a.creationDate DESC ")
                    .setParameter("accountID", account);
            
            Iterator dateList = query.getResultList().iterator();

            while (dateList.hasNext()) {
                Object[] tuple = (Object[]) dateList.next();
                Date monthlyStartDate = (Date) tuple[0];
                Date monthlyEndDate = (Date) tuple[1];
                String date = String.valueOf(monthlyStartDate.getTime()) + "##" + String.valueOf(monthlyEndDate.getTime()
                        + ";" + DateFunction.formatDateToStringYMMMD(monthlyStartDate) + "##" + DateFunction.formatDateToStringYMMMD(monthlyEndDate)
                );
                listResult.add(date);
            }

            result.setObjectList(listResult);
            result.setMsg("good");

        } catch (Throwable th) {
           // logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"" + th.getMessage());
              logger.error("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"getMonthyDateForPersonnalBilling"+"\n"
                            +"error      : "+ th.getMessage());
           
            result.setMsg("" + th.getMessage());
        }
        
        result.afficherResult("getMonthyDateForPersonnalBilling");
        return result;
    }

    
    @Override
    public Result generateDailyTicketPerTruck(int accountID, int jobResponseID, int truckID, long date) {

        String res;
        Result result = new Result();
        List<String> listResult = new ArrayList<>();
        List<DailyTicket> dailyTickets = null;
        JobResponse jobResponse = null;
        result.setMsg("good");

        //logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"generateDailyTicketPerTruck:   jobResponseID = " + jobResponseID + "  *** new Date(date) = " + new Date(date));
        
         logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"getARangeOfCategorieJobs"+"\n"
                            +"info      : "+ "generateDailyTicketPerTruck:   jobResponseID = " + jobResponseID + "  *** new Date(date) = " + new Date(date));
           
        try {

            ges.creatEntityManager();
            jobResponse = (JobResponse) ges.getEntityManager().find(JobResponse.class, jobResponseID);

            if (jobResponse == null) {
                result.setMsg("InvaLidJobResponseID");
                return result;
            }

            Query query = ges.getEntityManager().createQuery("SELECT a FROM DailyTicket a WHERE a.deleted = false AND a.jobresponseID = :jobresponseID AND a.ticketDate = :date AND a.truckID.truckID = :truckID ")
                    .setParameter("jobresponseID", jobResponse)
                    .setParameter("truckID", truckID)
                    .setParameter("date", new Date(date));

            dailyTickets = query.getResultList();

        } catch (Throwable th) {
            result.setMsg("" + th.getMessage());
        }
        
        //logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"generateDailyTicketPerTruck: dailyTickets = " + dailyTickets);
           logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"generateDailyTicketPerTruck"+"\n"
                            +"info      : "+ "generateDailyTicketPerTruck: dailyTickets = " + dailyTickets);
           
            
        
        if (dailyTickets == null || dailyTickets.isEmpty()) {
            result.setMsg("NotExistingDailyTicket");
            return result;
        }

        DailyTicket dailyTicket = dailyTickets.get(0);
        listResult.add("0");

        Object[] resultQuery;
        try {
            resultQuery = (Object[]) ges.getEntityManager().createQuery("SELECT MIN(j.startTime), MAX(j.endTime), SUM(j.numberOfLoad), (SUM( SQL('TIME_TO_SEC(?)', j.endTime) -  SQL('TIME_TO_SEC(?)', j.startTime))/3600) FROM JobLog j WHERE j.deleted = false AND j.dailyticketID = :dailyTicket ")
                    .setParameter("dailyTicket", dailyTicket)
                    .getSingleResult();
        } catch (Exception e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
           // logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"generateDailyTicketPerTruck: res = " + res);
                  logger.error("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"generateDailyTicketPerTruck"+"\n"
                            +"error      : "+ "generateDailyTicketPerTruck: res = " + res);
           
            
            result.setMsg("BadQuery");
            return result;
        }

        Date dailyStartTime = (Date) resultQuery[0];
        Date dailyEndTime = (Date) resultQuery[1];
        int dailyTotalLoad = Integer.parseInt(String.valueOf(resultQuery[2]));
        float dailyTotalHours = Float.parseFloat(String.valueOf(resultQuery[3]));
        
        int totalLoads = dailyTotalLoad;
        float totalHours = dailyTotalHours;
        boolean status = dailyTicket.getState();

        res = "" + (dailyTicket.getTruckID() == null ? "" : dailyTicket.getTruckID().getTruckNumber());//0
        res += ";" + DateConversion.formatDateToTimeAMPM((dailyStartTime));//1
        res += ";" + DateConversion.formatDateToTimeAMPM(dailyEndTime);//2
        res += ";" + dailyTotalLoad;//3
        res += ";" + NumberProcessing.round(dailyTotalHours, 2);//4
        res += ";" + dailyTicket.getDailyticketID();//5
        res += ";" + dailyTicket.getState();
        res += ";" + dailyTicket.getViewTicket();
        res += ";" + (dailyTicket.getTruckID() == null ? "" : dailyTicket.getTruckID().getTruckID());
        res += ";" + "null";
        listResult.add(res);


        Date jobStartDate = dailyTicket.getJobresponseID().getJobID().getStartDate();
        Date jobEndDate = dailyTicket.getJobresponseID().getJobID().getEndDate();
        String timezoneID = dailyTicket.getJobresponseID().getJobID().getTimezoneID();

        User excavator = dailyTicket.getJobresponseID().getJobID().getExcavatorID();
        User truckowner = dailyTicket.getJobresponseID().getTruckownerID();
        
        res = "" + dailyTicket.getJobresponseID().getJobresponseID();//0
        res += ";" + excavator.getName();//1
        res += ";" + excavator.getSurname();
        res += ";" + excavator.getAddress();
        res += ";" + excavator.getCellPhone();
        res += ";" + excavator.getAccountID().getEmail();//5

        res += ";" + truckowner.getName();
        res += ";" + truckowner.getSurname();
        res += ";" + truckowner.getAddress();
        res += ";" + truckowner.getCellPhone();
        res += ";" + truckowner.getAccountID().getEmail();//10
        res += ";" + (dailyTicket.getJobresponseID().getJobID().getTypeofdirtID() == null ? "" : dailyTicket.getJobresponseID().getJobID().getTypeofdirtID().getLabel());
        res += ";" + dailyTicket.getJobresponseID().getJobID().getJobNumber();
        res += ";" + dailyTicket.getJobresponseID().getJobID().getJobLocation();

        res += ";" + totalLoads;
        res += ";" + NumberProcessing.round(totalHours, 2);//15
        res += ";" + status;//16
        res += ";" + excavator.getCompagnyName();
        res += ";" + excavator.getTelephone();
        res += ";" + truckowner.getCompagnyName();
        res += ";" + truckowner.getTelephone();//20
        res += ";" + jobStartDate.getTime();
        res += ";" + jobEndDate.getTime();
        res += ";" + dailyTicket.getTicketDate().getTime();
        res += ";" + dailyTicket.getViewTicket();//24
        res += ";" + dailyTicket.getJobresponseID().getPaymenttypeID().getLibel();//25
        res += ";" + dailyTicket.getJobresponseID().getBillingPrice();//26
        res += ";" + timezoneID;//27
        res += ";" + DateFunction.formatDateToStringYMMMD(dailyTicket.getTicketDate());
        res += ";" + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(jobStartDate, timezoneID);
        res += ";" + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(jobEndDate, timezoneID);//30
        res += ";" + (!dailyTicket.getState() ?  "null" : 
                        (dailyTicket.getEmployeeID() == null ? MailFunction.getTotalName(excavator.getName(), excavator.getSurname()) :
                        dailyTicket.getEmployeeID().getEmpName() )
                    );//31
        
        listResult.set(0, res);

        result.setObjectList(listResult);
        result.afficherResult("generateDailyTicketPerTruck");
        return result;
    }
    
    @Override
    public String validateDailyTicketFromExcavatorPerTruck(int accountID, int dailyTicketID) throws BillingManagementException{

        String res = "";
        DailyTicket dailyTicket = null;
        try {

            ges.creatEntityManager();
            
            dailyTicket = (DailyTicket) ges.getEntityManager().find(DailyTicket.class, dailyTicketID);
            
            if (dailyTicket == null) {
                return "invalidDailyTicketID";
            }
            
            dailyTicket.setState(true);
            ges.getEntityManager().merge(dailyTicket);
            ges.closeEm();
            res = "good";

        } catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            //logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"validateDailyTicketFromExcavatorPerTruck: Error: " + res);
        throw new BillingManagementException(getClass()+"","validateDailyTicketFromExcavatorPerTruck",1,res,res);
        
        }

        return res;
    }

    @Override
    public String validateDailyTicketFromTruckOwnerPerTruck(int accountID, int dailyTicketID, String excavatorCode) throws BillingManagementException{
        
        String res = "";
        DailyTicket dailyTicket = null;
        Employee employee = null;
        
        try {

            ges.creatEntityManager();
            
            dailyTicket = (DailyTicket) ges.getEntityManager().find(DailyTicket.class, dailyTicketID);
            
            if (dailyTicket == null) {
                return "invalidDailyTicketID";
            }
            
            try {
                employee = (Employee) ges.getEntityManager().createQuery("SELECT e FROM Employee e WHERE e.deleted = FALSE AND e.empCode = :empCode AND e.excavatorID = :excavatorID")
                        .setParameter("empCode", excavatorCode)
                        .setParameter("excavatorID", dailyTicket.getJobresponseID().getJobID().getExcavatorID())
                        .getSingleResult();
            } catch (Exception e) {
                //logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"validateDailyTicketFromTruckOwnerPerTruck: Error: "+e.getMessage());
            
                  logger.error("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"validateDailyTicketFromTruckOwnerPerTruck"+"\n"
                            +"error      : "+ "validateDailyTicketFromTruckOwnerPerTruck: Error: "+e.getMessage());
            }
            
            if (dailyTicket.getJobresponseID().getJobID().getExcavatorID().getUserCode().equals(excavatorCode)
                    || (employee != null && employee.getEmpStatus().equals(EmployeeStatus.ACTIF_EMP) )) {

                dailyTicket.setState(true);
                dailyTicket.setViewTicket(true);
                dailyTicket.setValidationDate(DateFunction.getGMTDate());
                dailyTicket.setEmployeeID(employee);
                ges.getEntityManager().merge(dailyTicket);
                ges.closeEm();
                res = "good";
            } else {
                res = "invalidCode";
            }
        } catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            //logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"validateDailyTicketFromTruckOwnerPerTruck: Error: "+res);
            throw new BillingManagementException(getClass()+"","validateDailyTicketFromTruckOwnerPerTruck",1,res,res);
        
        
        }
        
        return res;
    }

    @Override
    public String submitDailyTicketByTruckOwnerPerTruck(int accountID, int dailyTicketID) throws BillingManagementException{
        
        String res = "";
        DailyTicket dailyTicket = null;
        try {

            ges.creatEntityManager();

            dailyTicket = (DailyTicket) ges.getEntityManager().find(DailyTicket.class, dailyTicketID);

            if (dailyTicket == null) {
                return "invalidDailyTicketID";
            }

            if (dailyTicket.getViewTicket()) {
                return "AlReadySubmited";
            }
            
            dailyTicket.setViewTicket(true);
            ges.getEntityManager().merge(dailyTicket);
            ges.closeEm();
            res = "good";

        } catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
           // logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"submitDailyTicketByTruckOwnerPerTruck: Error: "+res);
             throw new BillingManagementException(getClass()+"","submitDailyTicketByTruckOwnerPerTruck",1,res,res);
        
        }
        
        return res;
    }

    
}
