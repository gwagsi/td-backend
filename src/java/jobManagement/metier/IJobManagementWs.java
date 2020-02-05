/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jobManagement.metier;

import toolsAndTransversalFunctionnalities.Result;


/**
 *
 * @author erman
 */
public interface IJobManagementWs {
    
    /**
     * Cette permet d'enregistrer un job et implique aussi
     * l'enregistrement d'un jobResponse devant associer a chaque jobResponse
     * un truck owner repondant a un certains nombre der critere.
     *
     * @param startDate
     * @param endDate
     * @param hourPerDay
     * @param numberOfTruck
     * @param companyRate
     * @param rate
     * @param excavatorID
     * @param lenghtofbedID
     * @param truckaxleID
     * @param jobLocation
     * @param jobNumber
     * @param typeOfDirtyID
     * @param weight
     * @param startTime
     * @param dotNumber
     * @param generalLiability
     * @param truckLiability
     * @param proofOfTruckLiability
     * @param directDeposit
     * @param timeSheet
     * @param automatedBooking
     * @param jobDescription
     * @param jobInstruction
     * @param documentsID
     * @param billingPrice
     * @param paymentTypeID
     * @param timeZone
     * @param timeZoneID
     * @param latitude
     * @param longitude
     * @return"good" ou "InvalidTruckAxleID" ou "InvalidAccountID" ou "InvalidLenghtOfBedID" ou message d'erreur
     * 
     */
    public String addNewJob(long startDate, long endDate, int hourPerDay, int numberOfTruck,
            String companyRate, String rate, int excavatorID, int lenghtofbedID, int truckaxleID, 
            String jobLocation, String jobNumber, int typeOfDirtyID, String weight, String startTime,
            String dotNumber, int generalLiability, int truckLiability, String proofOfTruckLiability,
            String directDeposit, String timeSheet, String automatedBooking, String jobDescription, String jobInstruction,
            String documentsID, float billingPrice, int paymentTypeID, int timeZone, String timeZoneID, 
            float latitude, float longitude);
        
    /**
     *
     * @param startDate
     * @param endDate
     * @param hourPerDay
     * @param numberOfTruck
     * @param companyRate
     * @param rate
     * @param truckOwnerID
     * @param jobID
     * @param billingPrice
     * @param paymentTypeID
     * @param trucksID
     * @param timeZone
     * @return
     */
    public String addNewJobRespons(long startDate, long endDate, int hourPerDay, int numberOfTruck, String companyRate,
            String rate, int truckOwnerID, int jobID, float billingPrice, int paymentTypeID, String trucksID, int timeZone);
    
    
    /**
     *
     * @param jobID
     * @param startDate
     * @param endDate
     * @param hourPerDay
     * @param numberOfTruck
     * @param companyRate
     * @param rate
     * @param excavatorID
     * @param lenghtofbedID
     * @param truckaxleID
     * @param jobLocation
     * @param jobNumber
     * @param typeOfDirtyID
     * @param weight
     * @param startTime
     * @param dotNumber
     * @param generalLiability
     * @param truckLiability
     * @param proofOfTruckLiability
     * @param directDeposit
     * @param timeSheet
     * @param automatedBooking
     * @param jobDescription
     * @param jobInstruction
     * @param billingPrice
     * @param paymentTypeID
     * @param timeZoneID
     * @param latitude
     * @param longitude
     * @param modifiedField: Liste des clé de champ qui ont été modifié
     * @return "good" ou "InvalidJobID" ou "InvalidTruckAxleID" ou "InvalidAccountID" ou "InvalidLenghtOfBedID" ou message d'erreur
     */
    public String editJob(int jobID, long startDate, long endDate, int hourPerDay, int numberOfTruck,
            String companyRate, String rate, int excavatorID, int lenghtofbedID, int truckaxleID, String jobLocation,
            String jobNumber, int typeOfDirtyID, String weight, String startTime,
            String dotNumber, int generalLiability, int truckLiability, String proofOfTruckLiability,
            String directDeposit, String timeSheet, String automatedBooking, String jobDescription, String jobInstruction, 
            float billingPrice, int paymentTypeID, String timeZoneID, float latitude, float longitude, String modifiedField);

    /**
     *
     * @param jobResponseID
     * @param startDate
     * @param endDate
     * @param hourPerDay
     * @param numberOfTruck
     * @param companyRate
     * @param rate
     * @param truckOwnerID
     * @param billingPrice
     * @param paymentTypeID
     * @return "good" ou "InvalidJobResponseID" ou "InvalidAccountID" ou message d'erreur
     */
    public String editResponseJob(int jobResponseID, long startDate, long endDate, int hourPerDay, int numberOfTruck,
            String companyRate, String rate, int truckOwnerID, float billingPrice, int paymentTypeID);

    /**
     *
     * @param jobResponseID
     * @param endDate
     * @param truckOwnerID
     * @return
     */
    public String editJobResponsEndDate(int jobResponseID, long endDate, int truckOwnerID);
    
    /**
     *
     * @param excavatorID correspond au numero de compte du excavator
     * @param index
     * @param nombreMaxResult
     * @return retourne tous les jobs d'un excavator
     */
    public Result getARangeOfNotStartedJobByExcavator(int excavatorID, int index, int nombreMaxResult);
    
    /**
     * @param index
     * @param nombreMaxResult
     * @Description Cette methode donne la possiblité a un truckOwner
     * de voire les reponses qu'il a deja adresser aux excavators
     *
     * @param truckOwnerID
     * @return retourne tous jobResponse d'un truckOwner
     */
    public Result getARangeOfJobResponseByTruckOwner(int truckOwnerID, int index, int nombreMaxResult);
    
    /**
     *
     * @param index
     * @param nombreMaxResult
     * @return retourne l'excavateur d'un job
     */
    public Result getARangeOfJobRespons(int index, int nombreMaxResult);
    
    /**
     *
     * @param index
     * @param nombreMaxResult
     * @return
     */
    public Result getARangeOfJob(int index, int nombreMaxResult);
    
    /**
     * @Description retourne un job a partir de l'identifiant fourni
     *
     * @param jobID
     * @return les informations sont retournées dans l'ordre suivant:
     *  0 - jobResponseID
     *  1 - jobID
     *  2 - startDate
     *  3 - hourPerDay
     *  4 - endDate
     *  5 - numberOfTruck
     *  6 - creationDate
     *  7 - truckOwnerID
     */
    public Result getJobByID(int jobID);
    
    /**
     * @Description retourne un jobResponse à partir de l'identifiant fourni
     *
     * @param jobResponseID
     * @return les informations sont retournées dans l'ordre suivant:
     * 0 - jobID
     * 1 - startDate
     * 2 - endDate
     * 3 - hourPerDay
     * 4 - numberOfTruck
     * 5 - companyRate
     * 6 - rate
     * 7 - lengthOfBed
     * 8 - truckAxleName
     * 9 - jobNumber
     * 10 - excavatorID
     * 11 - jobLocation
     */
    public Result getJobResponseByID(int jobResponseID);
    
    /**
     *
     * @param jobID
     * @param deletedReason
     * @return
     */
    public String deleteJob(int jobID, String deletedReason);
    
    /**
     *
     * @param jobResponseID
     * @return
     */
    public String deleteJobRespons(int jobResponseID);

    /**
     *
     * @param jobResponseID
     * @param deletedReason
     * @return
     */
    public String deleteJobResponseByTruckOwner(int jobResponseID, String deletedReason);
    
    
    /**
     * @Description retourne les reponses d'un à partir de l'identifiant fourni
     *
     * @param jobID identifiant du job
     * @param index 
     * @param nombreMaxResult
     * @return les informations sont retournées dans l'ordre suivant:
     * 1 - startDate
     * 2 - endDate
     * 3 - hourPerDay
     * 4 - numberOfTruck
     * 5 - truckName
     * 6 - truckSurname
     * 7 - creationDate
     * 8 - truckEmail
     * 9 - delete
     * 10 - jobResponseID
     * 11 - jobID
     * 12 - truckOwnerID
     * 
     */
    public Result getARangeOfJobResponseByJobID(int jobID, int index, int nombreMaxResult);
    
    /**
     * 
     * Retourne tous les Job auquel un truckOwner peut postuler
     *
     * @param truckOnwerID
     * @param index
     * @param nombreMaxResult
     * @return
     */
    public Result getARangeOfJobByTruckOwner(int truckOnwerID, int index, int nombreMaxResult);
    
    /**
     *
     * @param excavatorID
     * @param jobResponseID
     * @return
     */
    public String validateAJob(int excavatorID, int jobResponseID);
    
    /**
     *
     * @param excavatorID
     * @param jobResponseID
     * @param truckIDList
     * @return
     */
    public String validateAJobByExcavator(int excavatorID, int jobResponseID, String truckIDList);
    
    /**
     *
     * @param truckOwnerID
     * @param jobResponseID
     * @param price
     * @param paymentTypeID
     * @return
     */
    public String validateAJobByTruckOwner(int truckOwnerID, int jobResponseID, float price, int paymentTypeID);
    
    /**
     *
     * @param jobID
     * @return
     */
    public String closeJob(int jobID);
    
    /**
     *
     * @param truckOwnerID
     * @param index
     * @param nombreMaxResult
     * @return
     */
    public Result getARangeOfValidJobResponseTruckOwner(int truckOwnerID, int index, int nombreMaxResult);
    
    /**
     *
     * @param excavatorID
     * @param index
     * @param nombreMaxResult
     * @return
     */
    public Result getARangeOfPassJobExcavator(int excavatorID, int index, int nombreMaxResult);
    
    /**
     *
     * @param excavatorID
     * @param index
     * @param nombreMaxResult
     * @return
     */
    public Result getARangeOfJobForBillByExcavator(int excavatorID, int index, int nombreMaxResult);
    
    /**
     *
     * @param excavatorID
     * @param index
     * @param nombreMaxResult
     * @return
     */
    public Result getARangeOfActifJobByExcavator(int excavatorID, int index, int nombreMaxResult);
    
    /**
     *
     * @param excavatorID
     * @return
     */
    public Result getJobNumberByExcavator(int excavatorID);
    
    /**
     *
     * @param excavatorID
     * @param index
     * @param nombreMaxResult
     * @return
     */
    public Result getARangeOfCloseJobExcavator(int excavatorID, int index, int nombreMaxResult);
    
    /**
     * Permet de ajouter un log sur une tache réaliser par un camion.
     *
     * @param dateLog date du jour de réalisation de la tache.
     * @param timeOnSite temps de debut du job
     * @param timeLeft temps de fin du job
     * @param numberOfLoad nombre de charge
     * @param noteOnDay note du jour
     * @param jobResponseID identifiant du job pour lequel on fait le log
     * @param startTime temps de debut du job.
     * @param endTime temps de fin de réalisation du job.
     * @param typeOfDirt type de dechet transporté.
     * @param fromWhere point de depart.
     * @param toWhere point de fin.
     * @param truckID camion ayant réaliser le job.
     * @param accountID
     * @return
     */
    public String addNewJobLog(String dateLog, String timeOnSite, String timeLeft, int numberOfLoad, String noteOnDay, int jobResponseID, 
            String startTime, String endTime, String typeOfDirt, String fromWhere, String toWhere, int truckID, int accountID);
 
    /**
     * Permet d'editer un log.
     *
     * @param jobLogID identifiant du log a modifié
     * @param dateLog date du jour de réalisation de la tache.
     * @param timeOnSite temps de debut du job
     * @param timeLeft temps de fin du job
     * @param numberOfLoad nombre de charge
     * @param noteOnDay note du jour
     * @param jobResponseID identifiant du job pour lequel on fait le log
     * @param startTime temps de debut du job.
     * @param endTime temps de fin de réalisation du job.
     * @param typeOfDirt type de dechet transporté.
     * @param fromWhere point de depart.
     * @param toWhere point de fin.
     * @param truckID camion ayant réaliser le job.
     * @param accountID
     * @return
     */
    public String editJobLog(int jobLogID, String dateLog, String timeOnSite, String timeLeft, int numberOfLoad, String noteOnDay,
            int jobResponseID, String startTime, String endTime, String typeOfDirt, String fromWhere, String toWhere, int truckID, int accountID);

    /**
     *
     * @param jobLogID
     * @param accountID
     * @return
     */
    public String deleteJobLog(int jobLogID, int accountID);
    
    /**
     *		
     * @param jobResponseID
     * @return
     */
    public String submitJob(int jobResponseID);
    
    /**
     * @description retourne tous les logs d'un jobs
     * @param jobResponseID
     * @param index
     * @param nombreMaxResult
     * @return
     */
    public Result getARangeOfJobLogByJobResponseID(int jobResponseID, int index, int nombreMaxResult);
    
    /**
     * @param truckOwnerID
     * @param index
     * @param nombreMaxResult
     * @return
     * @description retourne tous les jobs en cours dont les logs n'ont pas été soumit à la compagnie
     * 
     */
    public Result getARangeOfUnSumittedJob(int truckOwnerID, int index, int nombreMaxResult);
    
    /**
     * @Description retourne tous les jobs en cours dont les logs ont été deja soumit a la compagnie
     * @param truckOwnerID
     * @param index
     * @param nombreMaxResult
     * @return
     */
    public Result getARangeOfJobHistory(int truckOwnerID, int index, int nombreMaxResult);
    
    /**
     *
     * @param JobLogID
     * @return
     */
    public Result getJobLogByID(int JobLogID);
    
    /**
     *
     * @param jobResponseID
     * @param dateLog
     * @return
     */
    public Result getNumberOfJobLogPerDay(int jobResponseID, String dateLog);
    
    /**
     *
     * @param jobResponseID
     * @param submitReview
     * @return
     */
    public String editSubmitReview(int jobResponseID, int submitReview);
    
    /**
     *
     * @return
     */
    public Result getAllDateLog();
    
    /**
     *
     * @param index
     * @param nombreMaxResult
     * @return
     */
    public Result getARangeofJob(int index, int nombreMaxResult);

    /**
     *
     * @param jobID
     * @return
     */
    public Result automatedBooking(int jobID);

    /**
     *
     * @param jobResponseID
     * @param reveiwComment
     * @return
     */
    public String editReviewComment(int jobResponseID, String reveiwComment);
    
    /**
     *
     * @param jobID
     * @param index
     * @param nombreMaxResult
     * @return
     */
    public Result getARangeOfExcavatorReviewComment(int jobID, int index, int nombreMaxResult);
    
    /**
     *
     * @param jobResponseID
     * @param submitReview
     * @return
     */
    public String editReviewFromExcvator(int jobResponseID, int submitReview);
    
    /**
     *
     * @param jobResponseID
     * @param reviewComment
     * @return
     */
    public String editReviewCommentFromExcvator(int jobResponseID, String reviewComment);
    
    /**
     *
     * @param jobResponseID
     * @param index
     * @param nombreMaxResult
     * @return
     */
    public Result getARangeOfTruckOwnerReviewComment(int jobResponseID, int index, int nombreMaxResult);
    
    /**
     *
     * @param truckOwnerID
     * @param index
     * @param nombreMaxResult
     * @return
     */
    public Result getARangeOfValidJobResponseByTruckOwner(int truckOwnerID, int index, int nombreMaxResult);
    
    /**
     *
     * @param accountID
     * @param jobResponseID
     * @return
     */
    public Result getTruckOwnerAndTruckInfo(int accountID, int jobResponseID);
    
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
     */
    public Result getARangeOfJobLogForDailyTicket(int accountID, int jobResponseID, long ticketDate, int index, int nombreMaxResult);
    
    /**
     * Permet de rechercher tous les camions qui ont été sollicité et validé pour un jobResponse, cela servira a faire
     * des logs sur le job.
     *
     * @param accountID
     * @param jobResponseID
     * @return
     */
    public Result getTruckForJobLog(int accountID, int jobResponseID);
    
    /**
     * Permet de rechercher des camions qui ont été utiliser pour faire des logs sur un job en cour de la journée
     * et servira a générer des dailyTickets.
     *
     * @param accountID
     * @param jobResponseID
     * @param ticketDate
     * @return
     */
    public Result getTruckForDailyTicket(int accountID, int jobResponseID, long ticketDate);
    
    /**
     * Permet de rechercher des camions qui ont été utiliser pour faire des logs sur un job en cour de la semaine
     * et servira a générer des weeklyTickets.
     * 
     * @param accountID
     * @param jobResponseID
     * @param weeklyStartDate
     * @param weeklyEndDate
     * @return
     */
    public Result getTruckForWeeklyTicket(int accountID, int jobResponseID, long weeklyStartDate, long weeklyEndDate);
   
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
     */
    public Result getARangeOfJobLogForDailyTicketPerTruck(int accountID, int jobResponseID, long ticketDate, int truckID, int index, int nombreMaxResult);
    
}
