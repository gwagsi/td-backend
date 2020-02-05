/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jobBookingManagement.dao;

import toolsAndTransversalFunctionnalities.Result;
import util.exception.JobBookingManagementException;

/**
 *
 * @author erman
 */
public interface IJobBookingManagementDao {
    
    
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
     * @param jobLocation
     * @param jobNumber
     * @param accountID
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
     * @throws util.exception.JobBookingManagementException
     * 
     */
    
    public Result addNewJobBooking(long startDate, long endDate, int hourPerDay, String companyRate, String rate, 
            int accountID, String jobLocation, String jobNumber, int typeOfDirtyID, String weight, String startTime,
            String directDeposit, String timeSheet, String automatedBooking, String jobDescription, String jobInstruction,
            String documentsID, String trucksID, float billingPrice, int paymentTypeID, int timeZone, String timeZoneID,
            float latitude, float longitude) throws JobBookingManagementException;

    /**
     *
     * @param truckOwnerID
     * @param index
     * @param nombreMaxResult
     * @return
     * @throws util.exception.JobBookingManagementException
     */
    public Result getARangeOfJobToConfirmByTruckOwner(int truckOwnerID, int index, int nombreMaxResult)throws JobBookingManagementException;

    /**
     *
     * @param accountID
     * @param jobResponseID
     * @param index
     * @param nombreMaxResultat
     * @return
     * @throws util.exception.JobBookingManagementException
     */
    public Result getARangeOfTruckRequestedByJobResponseID(int accountID, int jobResponseID, int index, int nombreMaxResultat)throws JobBookingManagementException;
    
    /**
     *
     * @param accountID
     * @param jobID
     * @param index
     * @param nombreMaxResultat
     * @return
     * @throws util.exception.JobBookingManagementException
     */
    public Result getARangeOfTruckRequestedByJobID(int accountID, int jobID, int index, int nombreMaxResultat)throws JobBookingManagementException;
    
    /**
     *
     * @param accountID
     * @param jobID
     * @param index
     * @param nombreMaxResult
     * @return
     * @throws util.exception.JobBookingManagementException
     */
    public Result getARangeOfJobResponseByJobID(int accountID, int jobID, int index, int nombreMaxResult)throws JobBookingManagementException;
    
    /**
     * Liste des jobs FrontEnd qui attendent la confirmation du truckowner.
     *
     * @param excavatorID
     * @param index
     * @param nombreMaxResult
     * @return
     * @throws util.exception.JobBookingManagementException
     */
    public Result getARangeOfJobForTruckByExcavatorID(int excavatorID, int index, int nombreMaxResult)throws JobBookingManagementException;
    
    /**
     *
     * Liste des jobs frontend ou backend qui ont été validé par le truckowner et qui attendent la validation de l'excavator.
     * 
     * @param excavatorID
     * @param index
     * @param nombreMaxResult
     * @return
     * @throws util.exception.JobBookingManagementException
     */
    public Result getARangeOfJobToConfirmByExcavatorID(int excavatorID, int index, int nombreMaxResult)throws JobBookingManagementException;
    
    
}
