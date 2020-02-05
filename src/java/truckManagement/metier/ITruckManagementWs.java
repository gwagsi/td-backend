/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package truckManagement.metier;

import toolsAndTransversalFunctionnalities.Result;

/**
 *
 * @author erman
 */
public interface ITruckManagementWs {
    
    /**
     *
     * @param picture
     * @param matricule
     * @param locationPrice
     * @param accountID
     * @param year
     * @param make
     * @param model
     * @param truckAxleID
     * @param lenghtOfBedID
     * @param distance
     * @param phoneNumber
     * @param truckZipCode
     * @param dOTNumber
     * @param generalLiability
     * @param insuranceLiability
     * @param pictureInsurance
     * @param truckDescription
     * @param latitude
     * @param longitude
     * @return
     */
    public String addNewTruck(String picture, String matricule, double locationPrice, 
            int accountID, String year, String make, String model, int truckAxleID, 
            int lenghtOfBedID, int distance, String phoneNumber, String truckZipCode,
            String dOTNumber, int generalLiability, int insuranceLiability, String pictureInsurance, 
            String truckDescription, float latitude, float longitude);
       
    /**
     *
     * @param accountID
     * @param index
     * @param nombreMaxResultat
     * @return
     */
    public Result getARangeOfTruckByAccountID(int accountID, int index, int nombreMaxResultat);
    
    /**
     *
     * @param accountID
     * @return
     */
    public Result getAllTruckByAccountID(int accountID);
    
    /**
     *
     * @param accountID
     * @param truckID
     * @return
     */
    public Result getTruckDetail(int accountID, int truckID);

    /**
     *
     * @param truckID
     * @param picture
     * @param matricule
     * @param locationPrice
     * @param accountID
     * @param year
     * @param make
     * @param model
     * @param truckAxleID
     * @param lenghtOfBedID
     * @param distance
     * @param phoneNumber
     * @param truckZipCode
     * @param dOTNumber
     * @param generalLiability
     * @param insuranceLiability
     * @param pictureInsurance
     * @param truckDescription
     * @param latitude
     * @param longitude
     * @return
     */
    public String modifyTruckInfo(int truckID, String picture, String matricule, double locationPrice, 
            int accountID, String year, String make, String model, int truckAxleID, 
            int lenghtOfBedID, int distance, String phoneNumber, String truckZipCode,
            String dOTNumber, int generalLiability, int insuranceLiability, String pictureInsurance, 
            String truckDescription, float latitude, float longitude);
     
    
    /**
     *
     * @param truckID
     * @return
     */
    public String deleteTruck(int truckID);
    
    /**
     *
     * @return
     */
    public Result getAllTruckAxleName();
    
    /**
     *
     * @param accountID
     * @param truckID
     * @return
     */
    public Result getTruckInfoByAccountID(int accountID, int truckID);
    
    /**
     *
     * @param truckOwnerID
     * @return
     */
    public Result getTruckNumberByTruckOwner(int truckOwnerID);
    
    /**
     *
     * @return
     */
    public Result countAllTrucks();
    
    /**
     *
     * @param email
     * @param zipCode
     * @param date
     * @return
     */
    public String addNewAreaNotCovered(String email, String zipCode, String date);
    
    /**
     *
     * @param jobID
     * @return
     */
    public Result getTruckByJobCharacteristics(int jobID);
    
    
    /**
     *
     * @param jobID
     * @param accountID
     * @return
     */
    public Result getUserTruckByJobCharacteristics(int jobID, int accountID);

    /**
     *
     * @param truckID
     * @return
     */
    public String getDetailOfTruck(int truckID);
    
    /**
     *
     * @return
     */
    public Result getListOfInsuranceLiability();
    
    /**
     *
     * @return
     */
    public Result getListOfGeneralLiability();
     
    /**
     *
     * @param startDate
     * @param endDate
     * @param truckZipCode
     * @param distance_Within
     * @param excludeWeekend
     * @param sortParam
     * @param dotNumber
     * @param latitude
     * @param longitude
     * @param index
     * @param nombreMaxResult
     * @param attribut_session
     * @param ORDER
     * @return
     */
    public Result getARangeOfAllAvaillabilityTrucks(long startDate, long endDate, String truckZipCode, int distance_Within, 
            boolean excludeWeekend, int sortParam, int dotNumber, float latitude, float longitude, int index, 
            int nombreMaxResult, String attribut_session, int ORDER);

    /**
     *
     * @param startDate
     * @param endDate
     * @param truckZipCode
     * @param distance_Within
     * @param excludeWeekend
     * @param sortParam
     * @param truckAxleID
     * @param latitude
     * @param longitude
     * @param generalLiability
     * @param insuranceLiability
     * @param dotNumber
     * @param index
     * @param nombreMaxResult
     * @param attribut_session
     * @param ORDER
     * @return
     */
    public Result getARangeOfAvailabilityTrucksFiltered(long startDate, long endDate, String truckZipCode, int distance_Within,
            boolean excludeWeekend, int sortParam, int dotNumber, float latitude, float longitude, int truckAxleID, 
            int generalLiability, int insuranceLiability, int index, int nombreMaxResult, String attribut_session, int ORDER);
    
    /**
     *
     * @param lStartDate
     * @param lEndDate
     * @param truckZipCode
     * @param distance_Within
     * @param excludeWeekend
     * @param sortParam
     * @param dotNumber
     * @param latitude
     * @param longitude
     * @param index
     * @param nombreMaxResult
     * @param ORDER
     * @return
     */
    public Result getARangeOfAllAvaillableTrucks(long lStartDate, long lEndDate, String truckZipCode, int distance_Within, 
            boolean excludeWeekend, int sortParam, int dotNumber, float latitude, float longitude, int index, 
            int nombreMaxResult, int ORDER);
    
    /**
     *
     * @param lStartDate
     * @param lEndDate
     * @param truckZipCode
     * @param distance_Within
     * @param excludeWeekend
     * @param sortParam
     * @param dotNumber
     * @param latitude
     * @param longitude
     * @param truckAxleID
     * @param generalLiability
     * @param insuranceLiability
     * @param index
     * @param nombreMaxResult
     * @param ORDER
     * @return
     */
    public Result getARangeOfAvailableTrucksFiltered(long lStartDate, long lEndDate, String truckZipCode, int distance_Within,
            boolean excludeWeekend, int sortParam, int dotNumber, float latitude, float longitude, int truckAxleID, 
            int generalLiability, int insuranceLiability, int index, int nombreMaxResult, int ORDER);
    
    /**
     *
     * @param truckID
     * @return
     */
    public Result getUnavailabilityDatesByTruckID(int truckID);

    /**
     *
     * @param accountID
     * @param truckID
     * @param index
     * @param nombreMaxResult
     * @return
     */
    public Result getTruckDetailFrontEnd(int accountID, int truckID, int index, int nombreMaxResult);
    
}
