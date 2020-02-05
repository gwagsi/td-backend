/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jobLogManagement.dao;

import accountManagement.dao.TypeOfUser;
import entities.Account;
import entities.DailyTicket;
import entities.JobLog;
import entities.JobResponse;
import entities.SolicitedTruck;
import entities.Truck;
import entities.WeeklyTicket;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import resultInfo.JobOfJobResponseInfo;
import toolsAndTransversalFunctionnalities.GestionnaireEntite;
import toolsAndTransversalFunctionnalities.Result;
import util.date.DateConversion;
import util.date.DateFunction;
import util.exception.EmployeeManagementException;
import util.exception.JobLogManagementException;

/**
 *
 * @author erman
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class JobLogManagementDao implements IJobLogManagementDaoLocal, IJobLogManagementDaoRemote {

    @EJB
    GestionnaireEntite ges;

     final static org.apache.logging.log4j.Logger logger = LogManager.getLogger("dump1");
    
    @Override
    public String addNewJobLog(String dateLog, String timeOnSite, String timeLeft, int numberOfLoad,
            String noteOnDay, int jobResponseID, String startTime, String endTime, String typeOfDirt,
            String fromWhere, String toWhere, int truckID, int driverAccountID) throws JobLogManagementException{

        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"addNewJobLog:  timeOnSite: " + timeOnSite + ",  "
                + "timeLeft: " + timeLeft + ",  "
                + "startTime: " + startTime + ",  "
                + "endTime: " + endTime + ",  "
                + "");
        
        String res = "";
        JobResponse jobResponse = null;
        JobLog jobLog;
        Account driverAccount;
        List<JobLog> jobLogs;
        WeeklyTicket weeklyTicket = null;
        DailyTicket dailyTicket = null;
        Truck truck = null;
        
        Date exDateLog = DateFunction.parseStringToDate(dateLog);
        Time tStartTime = null;
        Time tEndTime = null;
        
        try {
            tEndTime = DateConversion.convertStringToTime(endTime);
            tStartTime = DateConversion.convertStringToTime(startTime);
        } catch (ParseException parseException) {
            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"addNewJobLog:  Error = " + parseException.getMessage());
            res = "InvalidFormatTime";
            throw new JobLogManagementException(getClass()+"","addNewJobLog",1,res,res);
        
            //return res;
        }
        
        try {

            ges.creatEntityManager();
            jobResponse = (JobResponse) ges.getEntityManager().find(JobResponse.class, jobResponseID);
            truck = (Truck) ges.getEntityManager().find(Truck.class, truckID);
            driverAccount = (Account) ges.getEntityManager().find(Account.class, driverAccountID);

            if (jobResponse == null) {
                ges.closeEm();
                return "InvalidJobResponseID";
            }

            if (truck == null) {
                ges.closeEm();
                return "InvalidTruckID";
            }

            if (driverAccount == null) {
                ges.closeEm();
                return "InvalidAccountID";
            }

            //Recherche s'il y a des logs qui chevauche les dates de debut et de fin du log.
            Query query = ges.getEntityManager().createQuery("SELECT a FROM JobLog a WHERE a.deleted = false AND a.jobresponseID = :jobresponseID AND a.dateLog = :dateLog AND a.startTime < :endTime AND a.endTime > :startTime AND (a.truckID = :truckID OR a.truckID IS NULL )")
                    .setParameter("jobresponseID", jobResponse)
                    .setParameter("truckID", truck)
                    .setParameter("dateLog", dateLog)
                    .setParameter("startTime", tStartTime)
                    .setParameter("endTime", tEndTime);
            jobLogs = query.getResultList();

            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"addNewJobLog:  jobLogs = " + jobLogs);
            if (jobLogs != null && !jobLogs.isEmpty()) {
                return "overlapDate";
            }

            Date[] dates = DateFunction.getDateRangeOfWeek(exDateLog);
            Date weeklyStartDate = dates[0];
            Date weeklyEndDate = dates[1];
            try {

                weeklyTicket = (WeeklyTicket) ges.getEntityManager().createQuery("SELECT w FROM WeeklyTicket w WHERE w.deleted = FALSE AND w.weeklyStartDate = :weeklyStartDate AND w.weeklyEndDate = :weeklyEndDate AND w.jobresponseID = :jobresponseID AND (w.truckID = :truckID OR w.truckID IS NULL)")
                        .setParameter("jobresponseID", jobResponse)
                        .setParameter("weeklyStartDate", weeklyStartDate)
                        .setParameter("weeklyEndDate", weeklyEndDate)
                        .setParameter("truckID", truck)
                        .getSingleResult();

            } catch (Exception e) {
                logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"addNewJobLog: error --- " + e.getMessage());
            }
            
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"addNewJobLog:  weeklyTicket = " + weeklyTicket);
            
            if (weeklyTicket != null && (weeklyTicket.getPaid() || weeklyTicket.getPaymentRecieve()) ) {
                return "ticketPaid";
            } if (weeklyTicket != null) {
                ges.getEntityManager().merge(weeklyTicket);
                ges.closeEm();

            } else {

                weeklyTicket = new WeeklyTicket();

                weeklyTicket.setCreationDate(DateFunction.getGMTDate());
                weeklyTicket.setJobresponseID(jobResponse);
                weeklyTicket.setDeleted(false);
                weeklyTicket.setValidated(false);
                weeklyTicket.setTransactionFees(0.25);
                weeklyTicket.setSubmited(false);
                weeklyTicket.setWeeklyStartDate(new Date(weeklyStartDate.getTime()));
                weeklyTicket.setWeeklyEndDate(new Date(weeklyEndDate.getTime()));
                weeklyTicket.setTruckID(truck);

                ges.getEntityManager().persist(weeklyTicket);
                ges.closeEm();

            }

            try {

                dailyTicket = (DailyTicket) ges.getEntityManager().createQuery("SELECT a FROM DailyTicket a WHERE a.deleted = false AND a.jobresponseID = :jobresponseID AND a.ticketDate = :date AND a.truckID = :truckID")
                        .setParameter("jobresponseID", jobResponse)
                        .setParameter("date", exDateLog)
                        .setParameter("truckID", truck)
                        .getSingleResult();

            } catch (Exception e) {
                logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"addNewJobLog: error --- " + e.getMessage());
            }

            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"addNewJobLog:  dailyTicket = " + dailyTicket);

            if (dailyTicket != null) {
                dailyTicket.setViewTicket(false);
                dailyTicket.setState(false);
                ges.getEntityManager().merge(dailyTicket);
                ges.closeEm();
            } else {
                dailyTicket = new DailyTicket();
                
                dailyTicket.setCreationDate(DateFunction.getGMTDate());
                dailyTicket.setTicketDate(exDateLog);
                dailyTicket.setDeleted(false);
                dailyTicket.setViewTicket(false);
                dailyTicket.setState(false);
                dailyTicket.setJobresponseID(jobResponse);
                dailyTicket.setWeeklyticketID(weeklyTicket);
                dailyTicket.setTruckID(truck);

                ges.getEntityManager().persist(dailyTicket);
                ges.closeEm();
            }

            jobLog = new JobLog();
            jobLog.setExDateLog(exDateLog);
            jobLog.setCreationDate(DateFunction.getGMTDate());
            jobLog.setEditionDate(DateFunction.getGMTDate());
            jobLog.setTimeOnSite(timeOnSite);
            jobLog.setDateLog(dateLog);
            jobLog.setTimeLeft(timeLeft);
            
            try {
                jobLog.setStartTime(DateConversion.convertStringToTime(startTime));
                jobLog.setEndTime(DateConversion.convertStringToTime(endTime));
            } catch (ParseException parseException) {
                logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"addNewJobLog:  Error = " + parseException.getMessage());
                res = "InvalidFormatTime";
                 throw new JobLogManagementException(getClass()+"","addNewJobLog",1,res,parseException.getMessage());
        
              //  return res;
            }
            
            jobLog.setTypeOfDirt(typeOfDirt);
            jobLog.setToWhere(toWhere);
            jobLog.setFromWhere(fromWhere);
            jobLog.setNumberOfLoad(numberOfLoad);
            jobLog.setNoteOnDay(noteOnDay);
            jobLog.setJobresponseID(jobResponse);
            jobLog.setDeleted(false);
            jobLog.setClosed(false);
            jobLog.setDailyticketID(dailyTicket);
            jobLog.setTruckID(truck);
            jobLog.setAddaccountID(driverAccount);
            jobLog.setEditaccountID(driverAccount);

            ges.getEntityManager().persist(jobLog);
            ges.closeEm();

            jobResponse.setSubmitted(false);
            jobResponse.getJobLogList().add(jobLog);
            ges.getEntityManager().merge(jobResponse);
            ges.closeEm();
            res = "good";
            res += ";" + jobLog.getJoblogID();

            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"addNewJobLog: " + res);

        } catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"addNewJobLog: " + res);
             throw new JobLogManagementException(getClass()+"","addNewJobLog",1,res,res);
        
           // return res;
        }
        
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"addNewJobLog:  dailyTicket = " + dailyTicket);
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"addNewJobLog:  weeklyTicket = " + weeklyTicket);
        return res;
    }

    @Override
    public String editJobLog(int jobLogID, String dateLog, String timeOnSite, String timeLeft, int numberOfLoad, String noteOnDay, int jobResponseID,
            String startTime, String endTime, String typeOfDirt, String fromWhere, String toWhere, int truckID, int driverAccountID) throws JobLogManagementException{

        String res = "good";
        JobResponse jobResponse = null;
        JobLog jobLog;
        List<JobLog> jobLogs;
        WeeklyTicket weeklyTicket = null;
        DailyTicket dailyTicket = null;
        Account driverAccount;
        boolean persistentWeeklyTicket = false;
        boolean persistentDailyTicket = false;
        Truck truck = null;
        
        Date exDateLog = DateFunction.parseStringToDate(dateLog);
        Time tStartTime = null;
        Time tEndTime = null;
        
        try {
            tEndTime = DateConversion.convertStringToTime(endTime);
            tStartTime = DateConversion.convertStringToTime(startTime);
        } catch (ParseException parseException) {
            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editJobLog:  Error = " + parseException.getMessage());
            res = "InvalidFormatTime";
             throw new JobLogManagementException(getClass()+"","editJobLog",1,res,parseException.getMessage());
        
            //return res;
        }


        try {

            ges.creatEntityManager();
            jobLog = (JobLog) ges.getEntityManager().find(JobLog.class, jobLogID);
            jobResponse = (JobResponse) ges.getEntityManager().find(JobResponse.class, jobResponseID);
            truck = (Truck) ges.getEntityManager().find(Truck.class, truckID);
            driverAccount = (Account) ges.getEntityManager().find(Account.class, driverAccountID);


            if (jobLog == null) {
                ges.closeEm();
                return "InvalidJobLogID";
            }

            // si le log est fermé et n'est pas associé à un truck on peut toujours l'éditer
            // dans ce cas c'est un ancien log
            if (jobLog.getClosed() && jobLog.getTruckID() != null) {
                ges.closeEm();
                return "ClosedJobLogID";
            }

            if (jobResponse == null) {
                ges.closeEm();
                return "InvalidJobResponseID";
            }

            if (truck == null) {
                ges.closeEm();
                return "InvalidTruckID";
            }

            if (driverAccount == null) {
                ges.closeEm();
                return "InvalidAccountID";
            }

            Date[] dates = DateFunction.getDateRangeOfWeek(exDateLog);
            Date weeklyStartDate = dates[0];
            Date weeklyEndDate = dates[1];

            //Recherche s'il y a des logs qui chevauche les nouvelles dates de debut et de fin du log a editer.
            Query query = ges.getEntityManager().createQuery("SELECT a FROM JobLog a WHERE a.deleted = false AND a.jobresponseID = :jobresponseID AND a.dateLog = :dateLog AND a.startTime < :endTime AND a.endTime > :startTime AND (a.truckID = :truckID OR a.truckID IS NULL ) AND a != :jobLog")
                    .setParameter("jobresponseID", jobResponse)
                    .setParameter("truckID", truck)
                    .setParameter("dateLog", dateLog)
                    .setParameter("startTime", tStartTime)
                    .setParameter("endTime", tEndTime)
                    .setParameter("jobLog", jobLog);
            jobLogs = query.getResultList();

            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editJobLog:  jobLogs = " + jobLogs);
            if (jobLogs != null && !jobLogs.isEmpty()) {
                    return "overlapDate";
            }

            // Debut de mise a jour des dailyTickets.
            try {

                //Rechercher un eventuelle daily ticket auquel le nouveau log pourra appartenir
                dailyTicket = (DailyTicket) ges.getEntityManager().createQuery("SELECT a FROM DailyTicket a WHERE a.deleted = false AND a.jobresponseID = :jobresponseID AND a.ticketDate = :date AND ( a.truckID = :truckID OR a.truckID IS NULL) ")
                        .setParameter("jobresponseID", jobResponse)
                        .setParameter("date", exDateLog)
                        .setParameter("truckID", truck)
                        .getSingleResult();

            } catch (Exception e) {
                logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editJobLog: error --- " + e.getMessage());
            }
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editJobLog:  dailyTicket = " + dailyTicket);

            
            if (dailyTicket != null && dailyTicket.equals(jobLog.getDailyticketID())) {                
                weeklyTicket = dailyTicket.getWeeklyticketID();

            } else {

                DailyTicket oldDailyTicket = jobLog.getDailyticketID();
                WeeklyTicket oldWeeklyTicket = null;

                if (dailyTicket != null) {
                    weeklyTicket = dailyTicket.getWeeklyticketID();
                    persistentDailyTicket = false;
                    persistentWeeklyTicket = false;
                } else {

                    try {
                        //On recherche s'il ya un eventuel weeklyTicket prevus pour la nouvelle semaine auquelle pour pourait appartenir le Log
                        weeklyTicket = (WeeklyTicket) ges.getEntityManager().createQuery("SELECT w FROM WeeklyTicket w WHERE w.deleted = FALSE AND w.weeklyStartDate = :weeklyStartDate AND w.weeklyEndDate = :weeklyEndDate AND w.jobresponseID = :jobresponseID AND (w.truckID = :truckID OR w.truckID IS NULL)")
                                .setParameter("jobresponseID", jobResponse)
                                .setParameter("weeklyStartDate", weeklyStartDate)
                                .setParameter("weeklyEndDate", weeklyEndDate)
                                .setParameter("truckID", truck)
                                .getSingleResult();
                    } catch (Exception e) {
                        logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editJobLog: error --- " + e.getMessage());
                    }
                    logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editJobLog:  weeklyTicket = " + weeklyTicket);

                    dailyTicket = new DailyTicket();
                    dailyTicket.setCreationDate(DateFunction.getGMTDate());
                    dailyTicket.setTicketDate(exDateLog);
                    dailyTicket.setJobresponseID(jobResponse);
                    dailyTicket.setViewTicket(false);
                    
                    persistentDailyTicket = true;
                    oldWeeklyTicket = jobLog.getDailyticketID().getWeeklyticketID();
                    if (weeklyTicket != null) {
                        persistentWeeklyTicket = false;
                        oldWeeklyTicket = (oldWeeklyTicket.equals(weeklyTicket) ? null : oldWeeklyTicket);
                    } else {
                        // Dans ce cas on aura forcement un nouveau weeklyTicket donc
                        // celui associé au jobLog est forcement un anncien.
                        weeklyTicket = new WeeklyTicket();
                        weeklyTicket.setCreationDate(DateFunction.getGMTDate());
                        weeklyTicket.setJobresponseID(jobResponse);
                        weeklyTicket.setTransactionFees(0.25);
                        weeklyTicket.setWeeklyStartDate(weeklyStartDate);
                        weeklyTicket.setWeeklyEndDate(weeklyEndDate);
                        weeklyTicket.setSubmited(false);

                        persistentWeeklyTicket = true;

                    }
                    
                    logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editJobLog: oldDailyTicket: " + oldDailyTicket);
                    logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editJobLog: oldWeeklyTicket: " + oldWeeklyTicket);
                    
                    long nbreLogPrDaily = (long) ges.getEntityManager().createQuery("SELECT COUNT(j) FROM JobLog j "
                            + "WHERE j.deleted = FALSE AND j.dailyticketID = :oldDailyTicket")
                    .setParameter("oldDailyTicket", oldDailyTicket)
                    .getSingleResult();
                    
                    long nbreLogPrWeekly = (long) ges.getEntityManager().createQuery("SELECT COUNT(d) FROM DailyTicket d "
                            + "WHERE d.deleted = FALSE AND d.weeklyticketID = :oldWeeklyTicket")
                    .setParameter("oldWeeklyTicket", oldWeeklyTicket)
                    .getSingleResult();
                    
                    //Suppression des oldDailyTicket et oldWeelyTicket si necessaire
                    int updateCount = ges.getEntityManager().createQuery("UPDATE DailyTicket AS d SET d.deleted = TRUE "
                            + "WHERE d.deleted = FALSE AND d = :oldDailyTicket AND 1 >= (SELECT COUNT(j) FROM JobLog j "
                            + "WHERE j.deleted = FALSE AND j.dailyticketID = :oldDailyTicket)")
                            .setParameter("oldDailyTicket", oldDailyTicket)
                            .executeUpdate();
                    logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editJobLog: Nombre de DailyTicket supprimé: " + updateCount + ""
                            + "  -  Nombre Log restant est de nbreLogPrDaily: " + nbreLogPrDaily);

                    updateCount = ges.getEntityManager().createQuery("UPDATE WeeklyTicket AS w SET w.deleted = TRUE "
                            + "WHERE w.deleted = FALSE AND w = :oldWeeklyTicket AND 1 > (SELECT COUNT(d) FROM DailyTicket d "
                            + "WHERE d.deleted = FALSE AND d.weeklyticketID = :oldWeeklyTicket)")
                            .setParameter("oldWeeklyTicket", oldWeeklyTicket)
                            .executeUpdate();
                    logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editJobLog: Nombre de WeeklyTicket supprimé: " + updateCount + ""
                            + "  -  Nombre Log restant est de nbreLogPrWeekly: " + nbreLogPrWeekly);

                }

            }

            weeklyTicket.setDeleted(false);
            weeklyTicket.setValidated(false);
            weeklyTicket.setTruckID(truck);
            if (persistentWeeklyTicket) {
                ges.getEntityManager().persist(weeklyTicket);
                ges.closeEm();
            } else {
                ges.getEntityManager().merge(weeklyTicket);
                ges.closeEm();
            }

            dailyTicket.setDeleted(false);
            dailyTicket.setState(false);
            dailyTicket.setTruckID(truck);
            dailyTicket.setWeeklyticketID(weeklyTicket);
            if (persistentDailyTicket) {
                ges.getEntityManager().persist(dailyTicket);
                ges.closeEm();
            } else {
                ges.getEntityManager().merge(dailyTicket);
                ges.closeEm();
            }

            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editJobLog:  dailyTicket = " + dailyTicket);
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editJobLog:  weeklyTicket = " + weeklyTicket);
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editJobLog:  TruckID = " + truck);
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editJobLog:  jobLog = " + jobLog);

            //Fin de mise a jour des dailyTickets et debut de mise a jour du Log en question.
            jobLog.setTimeOnSite(timeOnSite);
            jobLog.setDateLog(dateLog);
            jobLog.setExDateLog(DateFunction.parseStringToDate(dateLog));
            jobLog.setTimeLeft(timeLeft);
            
            try {
                jobLog.setStartTime(DateConversion.convertStringToTime(startTime));
                jobLog.setEndTime(DateConversion.convertStringToTime(endTime));
            } catch (ParseException parseException) {
                logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editJobLog:  Error = " + parseException.getMessage());
                res = "InvalidFormatTime";
                throw new JobLogManagementException(getClass()+"","editJobLog",1,res,parseException.getMessage());
        
               // return res;
            }
            
            jobLog.setTypeOfDirt(typeOfDirt);
            jobLog.setToWhere(toWhere);
            jobLog.setFromWhere(fromWhere);
            jobLog.setNumberOfLoad(numberOfLoad);
            jobLog.setNoteOnDay(noteOnDay);
            jobLog.setJobresponseID(jobResponse);
            jobLog.setClosed(false);
            jobLog.setEditionDate(DateFunction.getGMTDate());
            jobLog.setTruckID(truck);
            jobLog.setJobresponseID(jobResponse);
            jobLog.setDailyticketID(dailyTicket);
            jobLog.setEditaccountID(driverAccount);

            ges.getEntityManager().merge(jobLog);
            ges.closeEm();

            jobResponse.setSubmitted(false);
            jobResponse.getJobLogList().add(jobLog);
            ges.getEntityManager().merge(jobResponse);
            ges.closeEm();

            res += ";" + jobLog.getJoblogID();
        } catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editJobLog: Error: " + res);
            throw new JobLogManagementException(getClass()+"","editJobLog",1,res,res);
        
            //return res;
        }

        return res;
    }

    @Override
    public String deleteJobLog(int jobLogID, int driverAccountID) throws JobLogManagementException{

        String res = "";
        JobLog jobLog = null;
        try {

            ges.creatEntityManager();
            jobLog = (JobLog) ges.getEntityManager().find(JobLog.class, jobLogID);

            if (jobLog == null) {
                ges.closeEm();
                return "InvalidJobLogID";
            }

            if (jobLog.getClosed()) {
                ges.closeEm();
                return "ClosedJobLogID";
            }

            if (jobLog.getDeleted()) {
                ges.closeEm();
                return "AlReadyDeleted";
            }

            DailyTicket dailyTicket = jobLog.getDailyticketID();
            WeeklyTicket weeklyTicket = dailyTicket.getWeeklyticketID();

            dailyTicket.setState(false);
            ges.getEntityManager().merge(dailyTicket);
            ges.closeEm();

            weeklyTicket.setValidated(false);
            ges.getEntityManager().merge(weeklyTicket);
            ges.closeEm();

            //Suppression des dailyTicket et weelyTicket s'il aucun log n'est plus associé
            int updateCount = ges.getEntityManager().createQuery("UPDATE DailyTicket AS d SET d.deleted = TRUE "
                    + "WHERE d.deleted = FALSE AND d = :dailyTicket AND 1 >= (SELECT COUNT(j) FROM JobLog j "
                    + "WHERE j.deleted = FALSE AND j.dailyticketID = :dailyTicket)")
                    .setParameter("dailyTicket", dailyTicket)
                    .executeUpdate();
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"deleteJobLog: Nombre de DailyTicket supprimé: " + updateCount);

            updateCount = ges.getEntityManager().createQuery("UPDATE WeeklyTicket AS w SET w.deleted = TRUE "
                    + "WHERE w.deleted = FALSE AND w = :weeklyTicket AND 1 >= (SELECT COUNT(d) FROM DailyTicket d "
                    + "WHERE d.deleted = FALSE AND d.weeklyticketID = :weeklyTicket)")
                    .setParameter("weeklyTicket", weeklyTicket)
                    .executeUpdate();
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"deleteJobLog: Nombre de WeeklyTicket supprimé: " + updateCount);

            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"deleteJobLog:  dailyTicket = " + dailyTicket);

            jobLog.setDeleted(true);
            jobLog.getJobresponseID().setSubmitted(false);
            ges.getEntityManager().merge(jobLog.getJobresponseID());
            ges.getEntityManager().merge(jobLog);
            ges.closeEm();

            res = "good";

        } catch (Throwable th) {
            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"deleteJobLog:  Error: " + th.getMessage());
            res = th.getMessage();
            throw new JobLogManagementException(getClass()+"","editJobLog",1,res,res);
        
        }

        return res;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfJobLogByJobResponseID(int jobResponseID, int index, int nombreMaxResult) throws JobLogManagementException{

        String res;
        Result result = new Result();
        List<JobLog> jobLogs = null;
        int numberOfElts = 0;
        List<String> listResult = new ArrayList<>();
        JobResponse jobResponse;
        result.setMsg("good");
        try {

            ges.creatEntityManager();
            jobResponse = (JobResponse) ges.getEntityManager().find(JobResponse.class, jobResponseID);

            if (jobResponse == null) {
                result.setMsg("InvaLidJobResponseID");
                return result;
            }

            Query query = ges.getEntityManager().createQuery("SELECT a FROM JobLog a WHERE a.deleted = false AND a.jobresponseID = :jobresponseID  ORDER BY a.creationDate DESC ");

            query.setParameter("jobresponseID", jobResponse);
            numberOfElts = query.getResultList().size();

            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResult);
            jobLogs = query.getResultList();

        } catch (Throwable th) {
            result.setMsg(th.getMessage());
        }

        if (jobLogs != null && !jobLogs.isEmpty()) {

            for (JobLog jobLog : jobLogs) {
                
                String joblogCreateOwnerName = (jobLog.getAddaccountID().getSocialstatusID().getSocialstatusID() == TypeOfUser.SOCIAL_STATUS_DRIVER_ID ? 
                        jobLog.getAddaccountID().getDriver().getName() : jobLog.getAddaccountID().getUser().getName());
                String joblogCreateOwnerSurname = (jobLog.getAddaccountID().getSocialstatusID().getSocialstatusID() == TypeOfUser.SOCIAL_STATUS_DRIVER_ID ? 
                        jobLog.getAddaccountID().getDriver().getSurname(): jobLog.getAddaccountID().getUser().getSurname());
                
                String joblogEditOwnerName = (jobLog.getEditaccountID().getSocialstatusID().getSocialstatusID() == TypeOfUser.SOCIAL_STATUS_DRIVER_ID ? 
                        jobLog.getEditaccountID().getDriver().getName() : jobLog.getEditaccountID().getUser().getName());
                String joblogEditOwnerSurname = (jobLog.getEditaccountID().getSocialstatusID().getSocialstatusID() == TypeOfUser.SOCIAL_STATUS_DRIVER_ID ? 
                        jobLog.getEditaccountID().getDriver().getSurname() : jobLog.getEditaccountID().getUser().getSurname());
                
                Integer joblogID = jobLog.getJoblogID();
                String dateLog = jobLog.getDateLog();
                String noteOnDay = jobLog.getNoteOnDay();
                int numberOfLoad = jobLog.getNumberOfLoad();
                String timeLeft = jobLog.getTimeLeft();
                String timeOnSite = jobLog.getTimeOnSite();
                Date creationDate = jobLog.getCreationDate();
                boolean submitted = jobLog.getJobresponseID().getSubmitted();
                boolean closed = jobLog.getClosed();
                Date startTime = jobLog.getStartTime();
                Date endTime = jobLog.getEndTime();
                String typeOfDirt = jobLog.getTypeOfDirt();
                String fromWhere = jobLog.getFromWhere();
                String toWhere = jobLog.getToWhere();
                Integer truckID = (jobLog.getTruckID() == null ? 0 : jobLog.getTruckID().getTruckID());
                String truckNumber = (jobLog.getTruckID() == null ? "" : jobLog.getTruckID().getTruckNumber());
                boolean dailyTicketState = jobLog.getDailyticketID().getState();
                Date editionDate = jobLog.getEditionDate();
                
                JobOfJobResponseInfo JobOfJobResponseInfo = new JobOfJobResponseInfo(joblogCreateOwnerName, joblogEditOwnerName, joblogCreateOwnerSurname, joblogEditOwnerSurname, joblogID, dateLog,
                        noteOnDay, numberOfLoad, timeLeft, timeOnSite, creationDate, submitted, closed, startTime, endTime, typeOfDirt, fromWhere, toWhere, truckID, truckNumber, dailyTicketState, editionDate);
                
                res = JobOfJobResponseInfo.getStringObj();
                
                listResult.add(res);

            }
        }

        listResult.add("" + numberOfElts);
        result.setObjectList(listResult);
        result.afficherResult("getARangeOfJobLogByJobResponseID");
        return result;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getJobLogByID(int jobLogID) throws JobLogManagementException{
        Result result = new Result();
        JobLog jobLog = null;
        try {

            ges.creatEntityManager();
            jobLog = (JobLog) ges.getEntityManager().find(JobLog.class, jobLogID);

            if (jobLog == null) {
                ges.closeEm();
                result.setMsg("InvalidAvailabilityID\n");
                result.afficherResult("getJobLogByID");
                return result;
            }

            ges.closeEm();
            result.setMsg("good");
            result.setObject(jobLog.getJoblogID() + "##"//00
                    + jobLog.getDateLog() + "##"
                    + jobLog.getTimeLeft() + "##"
                    + jobLog.getNumberOfLoad() + "##"
                    + jobLog.getNoteOnDay() + "##"
                    + jobLog.getJobresponseID().getJobresponseID() + "##"//05
                    + jobLog.getTimeOnSite() + "##"
                    + DateConversion.getTimeOfDate(jobLog.getStartTime()) + "##"
                    + DateConversion.getTimeOfDate(jobLog.getEndTime()) + "##"
                    + jobLog.getTypeOfDirt() + "##"
                    + jobLog.getFromWhere() + "##"//10
                    + jobLog.getToWhere() + "##"
                    + (jobLog.getTruckID() == null ? "" : jobLog.getTruckID().getTruckID()) + "##"//12
                    + (jobLog.getTruckID() == null ? "" : jobLog.getTruckID().getTruckNumber()) + "##" //13
                    + "null"
            );

        } catch (Throwable th) {
            result.setMsg(th.getMessage());
        }
        
        result.afficherResult("getJobLogByID");
        return result;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getNumberOfJobLogPerDay(int jobResponseID, String dateLog) throws JobLogManagementException{
        Result res = new Result();
        JobResponse jobresponse = null;
        List<JobLog> jobLog;
        try {
            ges.creatEntityManager();
            jobresponse = (JobResponse) ges.getEntityManager().find(JobResponse.class, jobResponseID);

            if (jobresponse == null) {
                ges.closeEm();
                res.setMsg("InvalidJobResponseID\n");
                return res;
            }

            Query query = ges.getEntityManager().createQuery("SELECT job FROM JobLog job where job.jobresponseID=:jobResponseID AND job.dateLog = :dateLog");
            query.setParameter("jobResponseID", jobresponse);
            query.setParameter("dateLog", dateLog);
            jobLog = query.getResultList();

            ges.closeEm();
            res.setMsg("good");
            res.setObject(jobLog.size() + "");

        } catch (Throwable th) {
            res.setMsg(th.getMessage());
        }

        return res;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getAllDateLog() throws JobLogManagementException{

        Result result = new Result();
        result.setMsg("good");
        try {

            ges.creatEntityManager();
            result.setObjectList(ges.getEntityManager().createQuery("SELECT DISTINCT a.exDateLog FROM JobLog a WHERE a.deleted = FALSE").getResultList());

        } catch (Throwable th) {
            result.setMsg(th.getMessage());
        }

        return result;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfJobLogForDailyTicket(int accountID, int jobResponseID, long ticketDate, int index, int nombreMaxResult) throws JobLogManagementException{

        String res;
        Result result = new Result();
        List<JobLog> jobLogs = null;
        int numberOfElts = 0;
        List<String> listResult = new ArrayList<>();
        JobResponse jobResponse;
        result.setMsg("good");
        try {

            ges.creatEntityManager();
            jobResponse = (JobResponse) ges.getEntityManager().find(JobResponse.class, jobResponseID);

            if (jobResponse == null) {
                result.setMsg("InvaLidJobResponseID");
                return result;
            }
            
            Query query = ges.getEntityManager().createQuery("SELECT a FROM JobLog a WHERE a.deleted = false AND a.jobresponseID = :jobresponseID AND a.exDateLog = :dateLog ORDER BY a.creationDate DESC")
                    .setParameter("jobresponseID", jobResponse)
                    .setParameter("dateLog", new Date(ticketDate));
            
            numberOfElts = query.getResultList().size();

            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResult);
            jobLogs = query.getResultList();

        } catch (Throwable th) {
            result.setMsg("" + th.getMessage());
        }

        if (jobLogs != null && !jobLogs.isEmpty()) {

            for (JobLog jobLog : jobLogs) {
                res = "" + jobLog.getJoblogID() + "##"
                        + jobLog.getDateLog() + "##"
                        + jobLog.getNoteOnDay() + "##"
                        + jobLog.getNumberOfLoad() + "##"
                        + jobLog.getTimeLeft() + "##"
                        + jobLog.getTimeOnSite() + "##"
                        + jobLog.getCreationDate().getTime() + "##"
                        + jobLog.getJobresponseID().getSubmitted() + "##"
                        + jobLog.getClosed() + "##"
                        + DateConversion.getTimeOfDate(jobLog.getStartTime()) + "##"
                        + DateConversion.getTimeOfDate(jobLog.getEndTime()) + "##"
                        + jobLog.getTypeOfDirt() + "##"
                        + jobLog.getFromWhere() + "##"
                        + jobLog.getToWhere() + "##"
                        + (jobLog.getTruckID() == null ? "" : jobLog.getTruckID().getTruckID()) + "##"
                        + (jobLog.getTruckID() == null ? "" : jobLog.getTruckID().getTruckNumber()) + "##"
                        + "null";
                listResult.add(res);
            }
        }

        listResult.add("" + numberOfElts);
        result.setObjectList(listResult);
        result.afficherResult("getARangeOfJobLogForDailyTicket");
        return result;
    }

    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getTruckForJobLog(int accountID, int jobResponseID)throws JobLogManagementException{

        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getTruckForJobLog: accountID  = " + accountID + " ---> jobResponseID = " + jobResponseID);
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
                result.afficherResult("getTruckForJobLog");
                return result;
            }

            Query query;

            query = ges.getEntityManager().createQuery("SELECT s FROM SolicitedTruck s WHERE s.deleted = FALSE AND s.jobresponseID = :jobresponseID AND s.truckID.deleted = FALSE AND s.truckAvailable = TRUE ORDER BY s.creationDate DESC");

            query.setParameter("jobresponseID", jobResponse);
            solicitedTruckList = query.getResultList();

        } catch (Throwable th) {
            System.err.println("getTruckForJobLog: Error  = " + th.getMessage());
            result.setMsg(th.getMessage());
        }

        if (solicitedTruckList != null && !solicitedTruckList.isEmpty()) {
            String res = "";
            for (SolicitedTruck solicitedTruck : solicitedTruckList) {

                Truck truck = solicitedTruck.getTruckID();
                res = truck.getTruckID() + ";"//0
                        + truck.getTruckNumber() + ";"
                        + truck.getAvailable() + ";"
                        + truck.getCreationDate().getTime() + ";"
                        + truck.getLocationPrice() + ";"
                        + truck.getLenghtofbedID().getLenghtofbedID() + ";"
                        + truck.getDistance() + ";"
                        + truck.getPhoneNumber() + ";"
                        + truck.getTruckZipCode() + ";"
                        + truck.getDOTNumber() + ";"
                        + truck.getGeneralLiability() + ";"
                        + truck.getInsuranceLiability() + ";"
                        + truck.getYear() + ";"
                        + truck.getTruckaxleID().getTruckaxleID() + ";"
                        + truck.getTruckDescription() + ";"
                        + "null";
                listResult.add(res);

            }

            result.setObjectList(listResult);
        }

        result.afficherResult("getTruckForJobLog");
        return result;
    }
   
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) 
    @Override
    public Result getTruckForDailyTicket(int accountID, int jobResponseID, long ticketDate)throws JobLogManagementException{

        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getTruckForDailyTicket: accountID  = " + accountID + " ---> jobResponseID = " + jobResponseID + " ---> ticketDate = " + new Date(ticketDate));
        Result result = new Result();
        List<String> listResult = new ArrayList<>();
        List<Truck> truckList = null;
        JobResponse jobResponse;

        result.setMsg("good");
        try {

            ges.creatEntityManager();
            jobResponse = (JobResponse) ges.getEntityManager().find(JobResponse.class, jobResponseID);

            if (jobResponse == null) {
                result.setMsg("InvaLidJobResponseID");
                result.afficherResult("getTruckForDailyTicket");
                return result;
            }

            Query query = ges.getEntityManager().createQuery("SELECT DISTINCT j.truckID FROM JobLog j WHERE j.deleted = FALSE AND j.jobresponseID = :jobresponseID AND j.exDateLog = :exDateLog AND j.truckID IS NOT NULL")
                    .setParameter("exDateLog", new Date(ticketDate))
                    .setParameter("jobresponseID", jobResponse)
                    ;
            
            truckList = (List<Truck>)query.getResultList();

        } catch (Throwable th) {
            System.err.println("getTruckForDailyTicket: Error  = " + th.getMessage());
            result.setMsg(th.getMessage());
        }

        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getTruckForDailyTicket:  truckList = " + truckList);
        if (truckList != null && !truckList.isEmpty()) {
            String res = "";
            for (Truck truck : truckList) {
                
                res = truck.getTruckID() + ";"//0
                        + truck.getTruckNumber() + ";"
                        + truck.getAvailable() + ";"
                        + truck.getCreationDate().getTime() + ";"
                        + truck.getLocationPrice() + ";"
                        + (truck.getLenghtofbedID() == null ? "" : truck.getLenghtofbedID().getLenghtofbedID()) + ";"
                        + truck.getDistance() + ";"
                        + truck.getPhoneNumber() + ";"
                        + truck.getTruckZipCode() + ";"
                        + truck.getDOTNumber() + ";"
                        + truck.getGeneralLiability() + ";"
                        + truck.getInsuranceLiability() + ";"
                        + truck.getYear() + ";"
                        + (truck.getTruckaxleID() == null ? "" : truck.getTruckaxleID().getTruckaxleID()) + ";"
                        + truck.getTruckDescription() + ";"
                        + (truck.getPicture() == null ? "" : truck.getPicture().getPathName()) + ";"
                        + "null";
                listResult.add(res);
            }

            result.setObjectList(listResult);
        }

        result.afficherResult("getTruckForDailyTicket");
        return result;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getTruckForWeeklyTicket(int accountID, int jobResponseID, long weeklyStartDate, long weeklyEndDate)throws JobLogManagementException{

        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getTruckForWeeklyTicket: accountID  = " + accountID + " ---> jobResponseID = " + jobResponseID + " ---> weeklyStartDate = " + new Date(weeklyStartDate) + " ---> weeklyEndDate = " + new Date(weeklyEndDate));
        Result result = new Result();
        List<String> listResult = new ArrayList<>();
        List<Truck> truckList = null;
        JobResponse jobResponse;

        result.setMsg("good");
        try {

            ges.creatEntityManager();
            jobResponse = (JobResponse) ges.getEntityManager().find(JobResponse.class, jobResponseID);

            if (jobResponse == null) {
                result.setMsg("InvaLidJobResponseID");
                result.afficherResult("getTruckForWeeklyTicket");
                return result;
            }

            Query query = ges.getEntityManager().createQuery("SELECT DISTINCT j.truckID FROM JobLog j WHERE j.deleted = FALSE AND j.jobresponseID = :jobresponseID AND j.exDateLog BETWEEN :weeklyStartDate AND :weeklyEndDate AND j.truckID IS NOT NULL")
                    .setParameter("weeklyStartDate", new Date(weeklyStartDate))
                    .setParameter("weeklyEndDate", new Date(weeklyEndDate))
                    .setParameter("jobresponseID", jobResponse)
                    ;
            
            truckList = query.getResultList();

        } catch (Throwable th) {
            System.err.println("getTruckForWeeklyTicket: Error  = " + th.getMessage());
            result.setMsg(th.getMessage());
        }

        if (truckList != null && !truckList.isEmpty()) {
            String res = "";
            for (Truck truck : truckList) {

                res = truck.getTruckID() + ";"//0
                        + truck.getTruckNumber() + ";"
                        + truck.getAvailable() + ";"
                        + truck.getCreationDate().getTime() + ";"
                        + truck.getLocationPrice() + ";"
                        + (truck.getLenghtofbedID() == null ? "" : truck.getLenghtofbedID().getLenghtofbedID()) + ";"
                        + truck.getDistance() + ";"
                        + truck.getPhoneNumber() + ";"
                        + truck.getTruckZipCode() + ";"
                        + truck.getDOTNumber() + ";"
                        + truck.getGeneralLiability() + ";"
                        + truck.getInsuranceLiability() + ";"
                        + truck.getYear() + ";"
                        + (truck.getTruckaxleID() == null ? "" : truck.getTruckaxleID().getTruckaxleID()) + ";"
                        + truck.getTruckDescription() + ";"
                        + (truck.getPicture() == null ? "" : truck.getPicture().getPathName()) + ";"
                        + "null";
                listResult.add(res);

            }

            result.setObjectList(listResult);
        }

        result.afficherResult("getTruckForWeeklyTicket");
        return result;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfJobLogForDailyTicketPerTruck(int accountID, int jobResponseID, long ticketDate, int truckID, int index, int nombreMaxResult) throws JobLogManagementException{

        String res;
        Result result = new Result();
        List<JobLog> jobLogs = null;
        int numberOfElts = 0;
        List<String> listResult = new ArrayList<>();
        JobResponse jobResponse;
        result.setMsg("good");
        try {

            ges.creatEntityManager();
            jobResponse = (JobResponse) ges.getEntityManager().find(JobResponse.class, jobResponseID);

            if (jobResponse == null) {
                result.setMsg("InvaLidJobResponseID");
                return result;
            }
            
            Query query = ges.getEntityManager().createQuery("SELECT a FROM JobLog a WHERE a.deleted = false AND a.jobresponseID = :jobresponseID AND a.exDateLog = :dateLog AND a.truckID.truckID = :truckID ORDER BY a.creationDate DESC")
                    .setParameter("jobresponseID", jobResponse)
                    .setParameter("truckID", truckID)
                    .setParameter("dateLog", new Date(ticketDate));
            
            numberOfElts = query.getResultList().size();

            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResult);
            jobLogs = query.getResultList();

        } catch (Throwable th) {
            result.setMsg("" + th.getMessage());
        }

        if (jobLogs != null && !jobLogs.isEmpty()) {

            for (JobLog jobLog : jobLogs) {
                res = "" + jobLog.getJoblogID() + "##"
                        + jobLog.getDateLog() + "##"
                        + jobLog.getNoteOnDay() + "##"
                        + jobLog.getNumberOfLoad() + "##"
                        + jobLog.getTimeLeft() + "##"
                        + jobLog.getTimeOnSite() + "##"
                        + jobLog.getCreationDate().getTime() + "##"
                        + jobLog.getJobresponseID().getSubmitted() + "##"
                        + jobLog.getClosed() + "##"
                        + DateConversion.getTimeOfDate(jobLog.getStartTime()) + "##"
                        + DateConversion.getTimeOfDate(jobLog.getEndTime()) + "##"
                        + jobLog.getTypeOfDirt() + "##"
                        + jobLog.getFromWhere() + "##"
                        + jobLog.getToWhere() + "##"
                        + (jobLog.getTruckID() == null ? "" : jobLog.getTruckID().getTruckID()) + "##"
                        + (jobLog.getTruckID() == null ? "" : jobLog.getTruckID().getTruckNumber()) + "##"
                        + "null";
                listResult.add(res);
            }
        }

        listResult.add("" + numberOfElts);
        result.setObjectList(listResult);
        result.afficherResult("getARangeOfJobLogForDailyTicket");
        return result;
    }

    
}
