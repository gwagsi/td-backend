/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jobLogManagement.dao;

import toolsAndTransversalFunctionnalities.Result;
import util.exception.JobLogManagementException;


/**
 *
 * @author erman
 */
public interface IJobLogManagementDao {
    
    /**
     *
     * @param dateLog
     * @param timeOnSite
     * @param timeLeft
     * @param numberOfLoad
     * @param noteOnDay
     * @param jobResponseID
     * @param startTime
     * @param endTime
     * @param typeOfDirt
     * @param fromWhere
     * @param toWhere
     * @param truckID
     * @param driverAccountID
     * @return
     * @throws util.exception.JobLogManagementException
     */
    public String addNewJobLog(String dateLog, String timeOnSite, String timeLeft, int numberOfLoad, String noteOnDay, int jobResponseID,
            String startTime, String endTime, String typeOfDirt, String fromWhere, String toWhere, int truckID, int driverAccountID)throws JobLogManagementException;
 
    /**
     *
     * @param jobLogID
     * @param dateLog
     * @param timeOnSite
     * @param timeLeft
     * @param numberOfLoad
     * @param noteOnDay
     * @param jobResponseID
     * @param startTime
     * @param endTime
     * @param typeOfDirt
     * @param fromWhere
     * @param toWhere
     * @param truckID
     * @param driverAccountID
     * @return
     * @throws util.exception.JobLogManagementException
     */
    public String editJobLog(int jobLogID, String dateLog, String timeOnSite, String timeLeft, int numberOfLoad, String noteOnDay,
            int jobResponseID, String startTime, String endTime, String typeOfDirt, String fromWhere, String toWhere, int truckID, int driverAccountID)throws JobLogManagementException;

    /**
     *
     * @param jobLogID
     * @param driverAccountID
     * @return
     * @throws util.exception.JobLogManagementException
     */
    public String deleteJobLog(int jobLogID, int driverAccountID)throws JobLogManagementException;

    /**
     * @throws util.exception.JobLogManagementException
     * @description retourne tous les logs d'un jobs
     * @param jobResponseID
     * @param index
     * @param nombreMaxResult
     * @return
     */
    public Result getARangeOfJobLogByJobResponseID(int jobResponseID, int index, int nombreMaxResult) throws JobLogManagementException;
    
    /**
     *
     * @param jobLogID
     * @return
     * @throws util.exception.JobLogManagementException
     */
    public Result getJobLogByID(int jobLogID)throws JobLogManagementException;
    
    /**
     *
     * @param jobResponseID
     * @param dateLog
     * @return
     * @throws util.exception.JobLogManagementException
     */
    public Result getNumberOfJobLogPerDay(int jobResponseID, String dateLog)throws JobLogManagementException;
    
    /**
     *
     * @return
     * @throws util.exception.JobLogManagementException
     */
    public Result getAllDateLog()throws JobLogManagementException;
    
    /**
     *
     * Recherche tous les logs d'une journée d'un job.
     * 
     * @param accountID numero de compte du client connecté.
     * @param jobResponseID identifiant du jobResponse
     * @param ticketDate date du log
     * @param index
     * @param nombreMaxResult
     * @return retourne un result
     * @throws util.exception.JobLogManagementException
     */
    public Result getARangeOfJobLogForDailyTicket(int accountID, int jobResponseID, long ticketDate, int index, int nombreMaxResult)throws JobLogManagementException;
    
    /**
     * Permet de rechercher tous les camions qui ont été sollicité et validé pour un jobResponse, cela servira a faire
     * des logs sur le job.
     *
     * @param accountID
     * @param jobResponseID
     * @return
     * @throws util.exception.JobLogManagementException
     */
    public Result getTruckForJobLog(int accountID, int jobResponseID) throws JobLogManagementException;
    
    /**
     * Permet de rechercher des camions qui ont été utiliser pour faire des logs sur un job en cour de la journée
     * et servira a générer des dailyTickets.
     *
     * @param accountID
     * @param jobResponseID
     * @param ticketDate
     * @return
     * @throws util.exception.JobLogManagementException
     */
    public Result getTruckForDailyTicket(int accountID, int jobResponseID, long ticketDate)throws JobLogManagementException;
    
    /**
     * Permet de rechercher des camions qui ont été utiliser pour faire des logs sur un job en cour de la semaine
     * et servira a générer des weeklyTickets.
     * 
     * @param accountID
     * @param jobResponseID
     * @param weeklyStartDate
     * @param weeklyEndDate
     * @return
     * @throws util.exception.JobLogManagementException
     */
    public Result getTruckForWeeklyTicket(int accountID, int jobResponseID, long weeklyStartDate, long weeklyEndDate)throws JobLogManagementException;
   
    /**
     *
     * Recherche tous les logs d'une journée d'un job.
     * 
     * @param accountID numero de compte du client connecté.
     * @param jobResponseID identifiant du jobResponse
     * @param ticketDate date du log
     * @param truckID
     * @param index
     * @param nombreMaxResult
     * @return retourne un result
     * @throws util.exception.JobLogManagementException
     */
    public Result getARangeOfJobLogForDailyTicketPerTruck(int accountID, int jobResponseID, long ticketDate, int truckID, int index, int nombreMaxResult)throws JobLogManagementException;

}
