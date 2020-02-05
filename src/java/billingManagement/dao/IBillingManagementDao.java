/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package billingManagement.dao;


import toolsAndTransversalFunctionnalities.Result;
import toolsAndTransversalFunctionnalities.ResultBackend;
import util.exception.BillingManagementException;


/**
 *
 * @author erman
 */
public interface IBillingManagementDao {
    
    
    /**
     * @throws util.exception.BillingManagementException
     * @Description Retourne toutes les dates distinctes des differentes logs relatifs
     *      à un jobResponse
     *
     * @param jobResponseID
     * @return
     */
    public Result getAllDateOfLogsByJobResponseID(int jobResponseID)throws BillingManagementException;
    
    /**
     * @throws util.exception.BillingManagementException
     * @Description Retourne tous les type de paiement
     * @return
     */
    public Result getAllPaymentType()throws BillingManagementException;
    
    /**
     *
     * @param jobResponseID
     * @param date
     * @return
     * @throws util.exception.BillingManagementException
     */
    public Result generateDailyTicket(int jobResponseID, long date) throws BillingManagementException;
    
    /**
     *
     * @param jobResponseID
     * @return
     * @throws util.exception.BillingManagementException
     */
    public String editPaymentStatus(int jobResponseID) throws BillingManagementException;
    
    /**
     *
     * @param jobResponseID
     * @param ticketDate
     * @return
     * @throws util.exception.BillingManagementException
     */
    public String validateDailyTicketFromExcavator(int jobResponseID, long ticketDate) throws BillingManagementException;
    
    
    /**
     *
     * @param jobResponseID
     * @param ticketDate
     * @param excavatorCode
     * @return
     * @throws util.exception.BillingManagementException
     */
    public String validateDailyTicketFromTruckOwner(int jobResponseID, long ticketDate, String excavatorCode) throws BillingManagementException;
    
    /**
     *
     * @param jobResponseID
     * @return
     * @throws util.exception.BillingManagementException
     */
    public Result getFinalBill(int jobResponseID) throws BillingManagementException;
    
    /**
     *
     * @param jobResponseID
     * @return
     * @throws util.exception.BillingManagementException
     */
    public Result getWeeklyDateByJobResponseID(int jobResponseID) throws BillingManagementException;
    
    /**
     *
     * @param accountID
     * @param jobResponseID
     * @param startDate
     * @param endDate
     * @return
     * @throws util.exception.BillingManagementException
     */
    public Result generateWeeklyTicket(int accountID, int jobResponseID, long startDate, long endDate) throws BillingManagementException;
    
    /**
     *
     * @param excavatorID
     * @param jobResponseID
     * @param weeklyStartDate
     * @param weeklyEndDate
     * @return
     * @throws util.exception.BillingManagementException
     */
     public String validateWeeklyTicketFromExcavator(int excavatorID, int jobResponseID, long weeklyStartDate, long weeklyEndDate)throws BillingManagementException;
    
    /**
     *
     * @param truckOwnerID
     * @param jobResponseID
     * @param weeklyStartDate
     * @param weeklyEndDate
     * @param excavatorCode
     * @return
     * @throws util.exception.BillingManagementException
     */
    public String validateWeeklyTicketFromTruckOwner(int truckOwnerID, int jobResponseID, long weeklyStartDate, long weeklyEndDate, String excavatorCode)throws BillingManagementException;
    
     
    /**
     *
     * @param truckOwnerID
     * @param jobResponseID
     * @param weeklyStartDate
     * @param weeklyEndDate
     * @return
     * @throws util.exception.BillingManagementException
     */
    public String payWeeklyTicketByTruckOwner(int truckOwnerID, int jobResponseID, long weeklyStartDate, long weeklyEndDate)throws BillingManagementException;

    /**
     *
     * @param excavatorID
     * @param jobResponseID
     * @param weeklyStartDate
     * @param weeklyEndDate
     * @return
     * @throws util.exception.BillingManagementException
     */
    public ResultBackend payWeeklyTicketByExcavator(int excavatorID, int jobResponseID, long weeklyStartDate, long weeklyEndDate)throws BillingManagementException;
    
    /**
     *
     * @param truckOwnerID
     * @param index
     * @param nombreMaxResult
     * @return
     * @throws util.exception.BillingManagementException
     */
    public Result getARangeOfTruckOwnerWeeklyTicketHistory(int truckOwnerID, int index, int nombreMaxResult)throws BillingManagementException;
    
    /**
     *
     * @param excavatorID
     * @param index
     * @param nombreMaxResult
     * @return
     * @throws util.exception.BillingManagementException
     */
    public Result getARangeOfExcavatorWeeklyTicketHistory(int excavatorID, int index, int nombreMaxResult)throws BillingManagementException;
    
    /**
     * Cette methode permet au truckowner de soumettre un daily ticket à l'excvator
     * et c'est à partir de cet instant que celui-ci pourra valider.
     *
     * @param truckOwnerID identifiant de compte du truckowner
     * @param jobResponseID
     * @param ticketDate
     * @return retoutne 
     *    "good" si tout c'est bien passé 
     *    "AlReadySubmited" si le ticket a déjà été soumit.
     *    "InvalidDailyTicketID" si l'identifiant du ticket est n'exsite pas en base de données.
     *    et en cas d'erreur innatendu, on retourne message d'erreur concerné.
     * @throws util.exception.BillingManagementException
     */
    public String submitDailyTicketByTruckOwner(int truckOwnerID, int jobResponseID, long ticketDate)throws BillingManagementException;
    
    
    /**
     * Cette methode permet au truckowner de soumettre un weekly ticket à l'excavator
     * et c'est à partir de cet instant, ce ticket est considéré comme valider donc celui-ci peut-être payé.
     *
     * @param truckOwnerID identifiant de compte du truckowner
     * @param jobResponseID
     * @param weeklyStartDate
     * @param weeklyEndDate
     * @return retoutne 
     *    "good" si tout c'est bien passé 
     *    "AlReadySubmited" si le ticket a déjà été soumit.
     *    "InvalidWeeklyTicketID" si l'identifiant du ticket est n'exsite pas en base de données.
     *    et en cas d'erreur innatendu, on retourne message d'erreur concerné.
     * @throws util.exception.BillingManagementException
     */
    public String submitWeeklyTicketByTruckOwner(int truckOwnerID, int jobResponseID, long weeklyStartDate, long weeklyEndDate)throws BillingManagementException;

    /**
     *
     * @param accountID
     * @param monthlyStartDate
     * @param monthlyEndDate
     * @return
     * @throws util.exception.BillingManagementException
     */
    public Result loadMonthlyBill(int accountID, long monthlyStartDate, long monthlyEndDate)throws BillingManagementException;
    
    /**
     *
     * @param accountID
     * @return
     */
    public Result getMonthyDateForPersonnalBilling(int accountID);
    
    /**
     * Permet de générer un dailyTicket pour un camion donné.
     *
     * @param accountID
     * @param jobResponseID
     * @param truckID
     * @param date
     * @return
     * @throws util.exception.BillingManagementException
     */
    public Result generateDailyTicketPerTruck(int accountID, int jobResponseID, int truckID, long date)throws BillingManagementException;
    
    /**
     *
     * @param accountID
     * @param dailyTicketID
     * @return
     * @throws util.exception.BillingManagementException
     */
    public String validateDailyTicketFromExcavatorPerTruck(int accountID, int dailyTicketID)throws BillingManagementException;
    
    /**
     *
     * @param accountID
     * @param dailyTicketID
     * @param excavatorCode
     * @return
     * @throws util.exception.BillingManagementException
     */
    public String validateDailyTicketFromTruckOwnerPerTruck(int accountID, int dailyTicketID, String excavatorCode)throws BillingManagementException;
    
    /**
     *
     * @param accountID
     * @param dailyTicketID
     * @return
     * @throws util.exception.BillingManagementException
     */
    public String submitDailyTicketByTruckOwnerPerTruck(int accountID, int dailyTicketID)throws BillingManagementException;
    
}
