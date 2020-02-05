/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jobManagement.dao;

import entities.JobResponse;
import java.util.List;
import toolsAndTransversalFunctionnalities.Result;
import toolsAndTransversalFunctionnalities.ResultBackend;
import util.exception.JobManagementException;


/**
 *
 * @author erman
 */
public interface IJobManagementDao {
    

    /**
     * 
     * @param latitude
     * @param longitude
     * @throws util.exception.JobManagementException
     * @Description cette permet d'enregistrer un job et implique aussi
     * l'enregistrement d'un jobResponse devant associer a chaque jobResponse
     * un truck owner repondant a un certains nombre der critere.
     * 
     * @param startDate
     * @param endDate
     * @param hourPerDay
     * @param numberOfTruck
     * @param companyRate
     * @param rate
     * @param jobLocation
     * @param jobNumber
     * @param excavatorID
     * @param lenghtofbedID
     * @param truckaxleID
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
     * @return "good" ou "InvalidTruckAxleID" ou "InvalidAccountID" ou "InvalidLenghtOfBedID" ou message d'erreur
     * 
     */
    
    public ResultBackend addNewJob(long startDate, long endDate, int hourPerDay, int numberOfTruck,
            String companyRate, String rate, int excavatorID, int lenghtofbedID, int truckaxleID, 
            String jobLocation, String jobNumber, int typeOfDirtyID, String weight, String startTime,
            String dotNumber, int generalLiability, int truckLiability, String proofOfTruckLiability,
            String directDeposit, String timeSheet, String automatedBooking, String jobDescription, 
            String jobInstruction, String documentsID, float billingPrice, int paymentTypeID, int timeZone, String timeZoneID, 
            float latitude, float longitude)throws JobManagementException;
    
    /**
     *
     * @param startDate
     * @param endDate
     * @param hourPerDay
     * @param companyRate
     * @param rate
     * @param truckOwnerID
     * @param jobID
     * @param billingPrice
     * @param paymentType
     * @param trucksID
     * @param timeZone
     * @return
     * @throws util.exception.JobManagementException
     */
    public ResultBackend addNewJobResponse(long startDate, long endDate, int hourPerDay, String companyRate, 
            String rate, int truckOwnerID, int jobID, float billingPrice, int paymentType, String trucksID, int timeZone)throws JobManagementException;
    
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
     * @return "good" ou "InvalidJobID" ou "InvalidTruckAxleID" ou "InvalidAccountID" ou "InvalidLenghtOfBedID" ou message d'erreur
     * @throws util.exception.JobManagementException
     */
    public ResultBackend editJob(int jobID, long startDate, long endDate, int hourPerDay, int numberOfTruck,
            String companyRate, String rate, int excavatorID, int lenghtofbedID, int truckaxleID, String jobLocation,
            String jobNumber, int typeOfDirtyID, String weight, String startTime,
            String dotNumber, int generalLiability, int truckLiability, String proofOfTruckLiability,
            String directDeposit, String timeSheet, String automatedBooking, String jobDescription, String jobInstruction, 
            float billingPrice, int paymentTypeID, String timeZoneID, float latitude, float longitude)throws JobManagementException;

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
     * @param paymentType
     * @return "good" ou "InvalidJobResponseID" ou "InvalidAccountID" ou message d'erreur
     * @throws util.exception.JobManagementException
     */
    public String editResponseJob(int jobResponseID, long startDate, long endDate, int hourPerDay, int numberOfTruck,
            String companyRate, String rate, int truckOwnerID, float billingPrice, int paymentType)throws JobManagementException;
    
    /**
     *
     * @param jobResponseID
     * @param endDate
     * @param truckOwnerID
     * @return
     * @throws util.exception.JobManagementException
     */
    public String editJobResponsEndDate(int jobResponseID, long endDate, int truckOwnerID)throws JobManagementException;

    /**
     *
     * @param typeOfJob
     * @param excavatorID correspond au numero de compte du excavator
     * @param index
     * @param nombreMaxResult
     * @return retourne tous les jobs d'un excavator
     * @throws util.exception.JobManagementException
     */
    public Result getARangeOfByExcavator(String typeOfJob, int excavatorID, int index, int nombreMaxResult)throws JobManagementException;
    
    /**
     *
     * @param excavatorID
     * @param index
     * @param nombreMaxResult
     * @return
     * @throws util.exception.JobManagementException
     */
    public Result getARangeOfActifJobByExcavator(int excavatorID, int index, int nombreMaxResult)throws JobManagementException;
    
    /**
     * Retourne toutes propositions faites sur les jobs d'un truckOwner.
     *
     * @param index
     * @param nombreMaxResult
     * @param truckOwnerID
     * @return retourne tous jobResponse d'un truckOwner
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
     * @throws util.exception.JobManagementException
     * 
     */
    public Result getARangeOfJobResponseByTruckOwner(int truckOwnerID, int index, int nombreMaxResult)throws JobManagementException;
    
    /**
     *
     * @param index
     * @param nombreMaxResult
     * @return retourne l'excavateur d'un job
     * @throws util.exception.JobManagementException
     */
    public Result getARangeOfJobResponse(int index, int nombreMaxResult)throws JobManagementException;
    
    /**
     *
     * @param index
     * @param nombreMaxResult
     * @return
     * 
     * Retourne tous les jobs non clos du System
     * @throws util.exception.JobManagementException
     */
    public Result getARangeOfJob(int index, int nombreMaxResult)throws JobManagementException;
    
    /**
     * @throws util.exception.JobManagementException
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
    public Result getJobByID(int jobID)throws JobManagementException;
    
    /**
     * @throws util.exception.JobManagementException
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
    public Result getJobResponseByID(int jobResponseID)throws JobManagementException;
    
    /**
     *
     * @param jobID
     * @param deletedReason
     * @return
     * @throws util.exception.JobManagementException
     */
    public Result deleteJob(int jobID, String deletedReason)throws JobManagementException;
    
    
    /**
     *
     * @param jobID
     * @return
     * @throws util.exception.JobManagementException
     */
    public String closeJob(int jobID)throws JobManagementException;
    
    /**
     * Cette methode pour supprimer la reponse a un job
     * @param jobResponseID identifiant du job a supprimer
     * 
     * @return retourne good ou InvalidJobID dans le cas ou le job
     * preciser dans la base de données.
     * @throws util.exception.JobManagementException
     */
    public Result deleteJobResponse(int jobResponseID)throws JobManagementException;
    
    /**
     *
     * @param jobResponseID
     * @param deletedReason
     * @return
     */
    public Result deleteJobResponseByTruckOwner(int jobResponseID, String deletedReason)throws JobManagementException;
    
    /**
     * @throws util.exception.JobManagementException
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
    public Result getARangeOfJobResponseByJobID(int jobID, int index, int nombreMaxResult)throws JobManagementException;
    
    /**
     * 
     * Cette methode retourne tous les jobs Backend qui n'ont pas encore commencé
     *
     * @param truckOnwerID
     * @param index
     * @param nombreMaxResult
     * @return
     * @throws util.exception.JobManagementException
     */
    public Result getARangeOfJobByTruckOwner(int truckOnwerID, int index, int nombreMaxResult)throws JobManagementException;
    
    /**
     *
     * @param truckOwnerID
     * @param considerDeletedJob
     * @param index
     * @param nombreMaxResult
     * @return
     * @throws util.exception.JobManagementException
     */
    public Result getARangeOfValidJobResponseTruckOwner(int truckOwnerID, boolean considerDeletedJob, int index, int nombreMaxResult)throws JobManagementException;
    
    /**
     *
     * @param excavatorID
     * @return
     * @throws util.exception.JobManagementException
     */
    public Result getJobNumberByExcavator(int excavatorID)throws JobManagementException;
    
    /**
     *
     * @param accountID
     * @param jobResponseID
     * @param price
     * @param paymentTypeID
     * @return
     * @throws util.exception.JobManagementException
     */
    public Result validateAJobByTruckOwner(int accountID, int jobResponseID, float price, int paymentTypeID)throws JobManagementException;

    /**
     *
     * @param accountID
     * @param jobResponseID
     * @param truckIDList
     * @return
     * @throws util.exception.JobManagementException
     */
    public Result validateAJobByExcavator(int accountID, int jobResponseID, List<String> truckIDList)throws JobManagementException;
    
    /**
     *
     * @param excavatorID
     * @param index
     * @param nombreMaxResult
     * @return
     * @throws util.exception.JobManagementException
     */
    public Result getARangeOfCloseJobExcavator(int excavatorID, int index, int nombreMaxResult)throws JobManagementException;
    
    /**
     *
     * @param jobResponseID id du jobresponse
     * @param submitReview valeur du submit review
     * @return retourne 'good' lorsque la modification s'est bien deroulée
     * @throws util.exception.JobManagementException
     */
    public String editSubmitReview(int jobResponseID, int submitReview)throws JobManagementException;
    
    /**
     *
     * @param jobResponseID
     * @return
     * @throws util.exception.JobManagementException
     */
    public String submitJob(int jobResponseID)throws JobManagementException;
    
    /**
     *
     * @param jobResponseID
     * @return
     * @throws util.exception.JobManagementException
     */
    public Result submitJobLogExt(int jobResponseID)throws JobManagementException;
    
    /**
     * @throws util.exception.JobManagementException
     * @description retourne tous les jobs en cours dont les logs n'ont pas été soumit à la compagnie
     * @param truckOwnerID
     * @param index
     * @param nombreMaxResult
     * @return
     */
    public Result getARangeOfUnSumittedJob(int truckOwnerID, int index, int nombreMaxResult)throws JobManagementException;
    
    /**
     * @throws util.exception.JobManagementException
     * @Description retourne tous les jobs en cours dont les logs ont été deja soumit a la compagnie
     * @param truckOwnerID
     * @param index
     * @param nombreMaxResult
     * @return
     */
    public Result getARangeOfJobHistory(int truckOwnerID, int index, int nombreMaxResult)throws JobManagementException;
    
    /**
     *
     * @param jobID
     * @param index
     * @param nombreMaxResult
     * @return
     * 
     * Retourne tous les JobResponses validés par le client d'un job donnee
     * @throws util.exception.JobManagementException
     */
    public List<JobResponse> getARangeOfValidatJobResponseByJob(int jobID, int index, int nombreMaxResult)throws JobManagementException;
    
    /**
     *
     * @param jobLogID
     * @return
     * @throws util.exception.JobManagementException
     */
    public List<JobResponse> getJobResponseForAutomatedBook(int jobLogID)throws JobManagementException;
    
    /**
     *
     * @param jobResponseID
     * @param reveiwComment
     * @return
     * @throws util.exception.JobManagementException
     */
    public String editReviewComment(int jobResponseID, String reveiwComment)throws JobManagementException;
    
    /**
     *
     * @param jobID
     * @param index
     * @param nombreMaxResult
     * @return
     * @throws util.exception.JobManagementException
     */
    public Result getARangeOfExcavatorReviewComment(int jobID, int index, int nombreMaxResult)throws JobManagementException;
    
    /**
     *
     * @param jobResponseID
     * @param submitReview
     * @return
     * @throws util.exception.JobManagementException
     */
    public String editReviewFromExcvator(int jobResponseID, int submitReview)throws JobManagementException;
    
    /**
     *
     * @param jobResponseID
     * @param reviewComment
     * @return
     * @throws util.exception.JobManagementException
     */
    public String editReviewCommentFromExcvator(int jobResponseID, String reviewComment)throws JobManagementException;
    
    /**
     *
     * @param jobResponseID
     * @param index
     * @param nombreMaxResult
     * @return
     * @throws util.exception.JobManagementException
     */
    public Result getARangeOfTruckOwnerReviewComment(int jobResponseID, int index, int nombreMaxResult)throws JobManagementException;
    
    /**
     *
     * @param accountID
     * @param jobResponseID
     * @return
     * @throws util.exception.JobManagementException
     */
    public Result getTruckOwnerAndTruckInfo(int accountID, int jobResponseID)throws JobManagementException;
    
    /**
     *
     * @param jobLatitude
     * @param jobLongitude
     * @return
     * @throws util.exception.JobManagementException
     */
    public List<Object[]> searchTruckOwnerToNotify(float jobLatitude, float jobLongitude)throws JobManagementException;
    
    /**
     *
     * @param jobID
     * @param jobLatitude
     * @param jobLongitude
     * @return
     * @throws util.exception.JobManagementException
     */
    public List<Object[]> searchTruckOwnerToNotify(int jobID, float jobLatitude, float jobLongitude)throws JobManagementException;
    
}
