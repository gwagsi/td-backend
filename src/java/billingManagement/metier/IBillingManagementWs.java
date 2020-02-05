/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package billingManagement.metier;

import toolsAndTransversalFunctionnalities.Result;


/**
 *
 * @author erman
 */
public interface IBillingManagementWs {
    
    
    /**
     * @Description Retourne toutes les dates distinctes des differentes logs relatifs
     *      à un jobResponse
     *
     * @param jobResponseID
     * @return
     */
    public Result getAllDateOfLogsByJobResponseID(int jobResponseID);
    
    /**
     * @Description Retourne tous les type de paiement
     * @return
     */
    public Result getAllPaymentType();
    
    /**
     *
     * @param jobResponseID
     * @param date
     * @return
     */
    public Result generateDailyTicket(int jobResponseID, long date);
    
    /**
     *
     * @param jobResponseID
     * @return
     */
    public String editPaymentStatus(int jobResponseID);
    
    /**
     *
     * @param jobResponseID
     * @param ticketDate
     * @return
     */
    public String validateDailyTicketFromExcavator(int jobResponseID, long ticketDate);

    /**
     *
     * @param jobResponseID
     * @return
     */
    public Result getFinalBill(int jobResponseID);
    
    
    /**
     *
     * @param jobResponseID
     * @param ticketDate
     * @param excavatorCode
     * @return
     */
    public String validateDailyTicketFromTruckOwner(int jobResponseID, long ticketDate, String excavatorCode);
    
    /**
     *
     * @param jobResponseID
     * @return
     */
    public Result getWeeklyDateByJobResponseID(int jobResponseID);
    
    
    /**
     *
     * @param accountID
     * @param jobResponseID
     * @param startDate
     * @param endDate
     * @return
     */
    public Result generateWeeklyTicket(int accountID, int jobResponseID, long startDate, long endDate);
    
    /**
     *
     * @param truckOwnerID
     * @param jobResponseID
     * @param weeklyStartDate
     * @param weeklyEndDate
     * @param excavatorCode
     * @return
     */
    public String validateWeeklyTicketFromTruckOwner(int truckOwnerID, int jobResponseID, long weeklyStartDate, long weeklyEndDate, String excavatorCode);
    
    /**
     *
     * @param excavatorID
     * @param jobResponseID
     * @param weeklyStartDate
     * @param weeklyEndDate
     * @return
     */
    public String validateWeeklyTicketFromExcavator(int excavatorID, int jobResponseID, long weeklyStartDate, long weeklyEndDate);
    
    
    /**
     *
     * @param excavatorID
     * @param jobResponseID
     * @param weeklyStartDate
     * @param weeklyEndDate
     * @return
     */
    public String payWeeklyTicketByExcavator(int excavatorID, int jobResponseID, long weeklyStartDate, long weeklyEndDate);

    /**
     *
     * @param truckOwnerID
     * @param jobResponseID
     * @param weeklyStartDate
     * @param weeklyEndDate
     * @return
     */
    public String payWeeklyTicketByTruckOwner(int truckOwnerID, int jobResponseID, long weeklyStartDate, long weeklyEndDate);
    
    /**
     *
     * @param truckOwnerID
     * @param index
     * @param nombreMaxResult
     * @return
     */
    public Result getARangeOfTruckOwnerWeeklyTicketHistory(int truckOwnerID, int index, int nombreMaxResult);
    
    /**
     *
     * @param excavatorID
     * @param index
     * @param nombreMaxResult
     * @return
     */
    public Result getARangeOfExcavatorWeeklyTicketHistory(int excavatorID, int index, int nombreMaxResult);
    
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
     */
    public String submitDailyTicketByTruckOwner(int truckOwnerID, int jobResponseID, long ticketDate);
    
    
    /**
     * Ce cette methode permet au truckowner de soumettre un weekly ticket a l'excavator
     * et c'est a partir de cet instant que celui sera pour valider le ticket.
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
     */
    public String submitWeeklyTicketByTruckOwner(int truckOwnerID, int jobResponseID, long weeklyStartDate, long weeklyEndDate);
    
    /**
     *
     * @param accountID
     * @param monthlyStartDate
     * @param monthlyEndDate
     * @return
     */
    public Result loadMonthlyBill(int accountID, long monthlyStartDate, long monthlyEndDate);
    
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
     */
    public Result generateDailyTicketPerTruck(int accountID, int jobResponseID, int truckID, long date);
    
    /**
     *
     * @param accountID
     * @param dailyTicketID
     * @return
     */
    public String validateDailyTicketFromExcavatorPerTruck(int accountID, int dailyTicketID);
    
    /**
     *
     * @param accountID
     * @param dailyTicketID
     * @param excavatorCode
     * @return
     */
    public String validateDailyTicketFromTruckOwnerPerTruck(int accountID, int dailyTicketID, String excavatorCode);
    
    /**
     *
     * @param accountID
     * @param dailyTicketID
     * @return
     */
    public String submitDailyTicketByTruckOwnerPerTruck(int accountID, int dailyTicketID);
    
}
