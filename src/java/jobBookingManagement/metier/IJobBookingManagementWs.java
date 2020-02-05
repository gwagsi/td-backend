/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jobBookingManagement.metier;

import toolsAndTransversalFunctionnalities.Result;

/**
 *
 * @author erman
 */
public interface IJobBookingManagementWs {
    
    
    
    /**
     * cette permet d'enregistrer un job et implique aussi
     * l'enregistrement d'un jobResponse devant associer a chaque jobResponse
     * un truck owner repondant a un certains nombre der critere.
     * 
     * @param startDate
     * @param endDate
     * @param hourPerDay
     * @param companyRate
     * @param rate
     * @param accountID
     * @param jobLocation
     * @param jobNumber
     * @param typeOfDirtyID
     * @param weight
     * @param startTime
     * @param directDeposit
     * @param timeSheet
     * @param automatedBooking
     * @param jobDescription
     * @param jobInstruction 
     * @param documentsID 
     * @param trucksID
     * @param billingPrice
     * @param paymentTypeID
     * @param timeZone
     * @param timeZoneID
     * @param latitude
     * @param longitude
     * @return "good" ou "InvalidTruckAxleID" ou "InvalidAccountID" ou "InvalidLenghtOfBedID" ou message d'erreur
     * 
     */
    
    public String addNewJobBooking(long startDate, long endDate, int hourPerDay, String companyRate, String rate, 
            int accountID, String jobLocation, String jobNumber, int typeOfDirtyID, String weight, String startTime,
            String directDeposit, String timeSheet, String automatedBooking, String jobDescription, String jobInstruction,
            String documentsID, String trucksID, float billingPrice, int paymentTypeID, int timeZone, String timeZoneID, 
            float latitude, float longitude);

    /**
     *
     * @param truckOwnerID
     * @param index
     * @param nombreMaxResult
     * @return
     */
    public Result getARangeOfJobToConfirmByTruckOwner(int truckOwnerID, int index, int nombreMaxResult);
    
    /**
     *
     * @param accountID
     * @param jobResponseID
     * @param index
     * @param nombreMaxResultat
     * @return
     */
    public Result getARangeOfTruckRequestedByJobResponseID(int accountID, int jobResponseID, int index, int nombreMaxResultat);
    
    /**
     *
     * @param accountID
     * @param jobID
     * @param index
     * @param nombreMaxResultat
     * @return
     */
    public Result getARangeOfTruckRequestedByJobID(int accountID, int jobID, int index, int nombreMaxResultat);

    /**
     *
     * @param accountID
     * @param jobID
     * @param index
     * @param nombreMaxResultat
     * @return
     */
    public Result getARangeOfJobResponseByJobID(int accountID, int jobID, int index, int nombreMaxResultat);
    
    /**
     *
     * @param excavatorID
     * @param index
     * @param nombreMaxResult
     * @return
     */
    public Result getARangeOfJobForTruckByExcavatorID(int excavatorID, int index, int nombreMaxResult);
    
    /**
     *
     * @param excavatorID
     * @param index
     * @param nombreMaxResult
     * @return
     */
    public Result getARangeOfJobToConfirmByExcavatorID(int excavatorID, int index, int nombreMaxResult);
    
    
}
