/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package truckManagement.dao;

import java.util.Date;
import java.util.List;
import toolsAndTransversalFunctionnalities.Result;
import util.exception.TruckManagementException;

/**
 *
 * @author erman
 */
public interface ITruckManagementDao {

    /**
     *
     * @param picture
     * @param truckNumber
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
     * @throws util.exception.TruckManagementException
     */
    public Result addNewTruck(String picture, String truckNumber, double locationPrice, 
            int accountID, String year, String make, String model, int truckAxleID, 
            int lenghtOfBedID, int distance, String phoneNumber, String truckZipCode,
            String dOTNumber, int generalLiability, int insuranceLiability, String pictureInsurance, 
            String truckDescription, float latitude, float longitude)throws TruckManagementException;

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
     * @throws util.exception.TruckManagementException
     */
    public Result modifyTruckInfo(int truckID, String picture, String matricule, double locationPrice, 
            int accountID, String year, String make, String model, int truckAxleID, 
            int lenghtOfBedID, int distance, String phoneNumber, String truckZipCode,
            String dOTNumber, int generalLiability, int insuranceLiability, String pictureInsurance, 
            String truckDescription, float latitude, float longitude)throws TruckManagementException;
    
       
    /**
     *
     * @param accountID
     * @param index
     * @param nombreMaxResultat
     * @return
     * @throws util.exception.TruckManagementException
     */
    public Result getARangeOfTruckByAccountID(int accountID, int index, int nombreMaxResultat)throws TruckManagementException;
    
    /**
     *
     * @param accountID
     * @return
     * @throws util.exception.TruckManagementException
     */
    public Result getAllTruckByAccountID(int accountID)throws TruckManagementException;
    
    /**
     *
     * @param accountID
     * @param truckID
     * @return
     * @throws util.exception.TruckManagementException
     */
    public Result getTruckDetail(int accountID, int truckID)throws TruckManagementException;
    
    /**
     *
     * @param driverID
     * @return
     * @throws util.exception.TruckManagementException
     */
    public String deleteTruck(int driverID)throws TruckManagementException;
    
    /**
     *
     * @param accountID
     * @param truckID
     * @param documentsID
     * @return
     * @throws util.exception.TruckManagementException
     */
    public String addTruckDocument(int accountID, int truckID, List<String> documentsID)throws TruckManagementException;
    
    /**
     *
     * @return
     */
    public Result getAllTruckAxleName();
    
    /**
     *
     * @description permet de retourner les infomations d'un camion a partir de son identifiant
     * @param accountID l'identifiant du compte du proprietaire du camion
     * @param truckID l'identifiant du camion
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
     * @throws util.exception.TruckManagementException
     */
    public String addNewAreaNotCovered(String email, String zipCode, String date)throws TruckManagementException;

    /**
     *
     * @param truckID
     * @param documentID
     * @return
     * @throws util.exception.TruckManagementException
     */
    public String deleteTruckDocument(int truckID, int documentID)throws TruckManagementException;
    
    /**
     *
     * @param jobID
     * @return
     */
    public Result getTruckByJobCharacteristics(int jobID);
    
    
    /**
     *
     * @param accountID
     * @param jobID
     * @return
     */
    public Result getUserTruckByJobCharacteristics(int accountID, int jobID);
    
    /**
     *
     * @param truckID
     * @return
     * @throws util.exception.TruckManagementException
     */
    public String getDetailOfTruck(int truckID)throws TruckManagementException;
    
    /**
     *
     * @return
     */
    public Result getListOfGeneralLiability();
    
    /**
     *
     * @return
     */
    public Result getListOfInsuranceLiability();
    
    /**
     *
     * @param startDate
     * @param endDate
     * @param truckZipCode
     * @param distance_Within
     * @param excludeWeekend
     * @param sortParam
     * @param latitude
     * @param longitude
     * @param latMin
     * @param latMax
     * @param lonMin
     * @param lonMax
     * @param dotNumber
     * @param index
     * @param nombreMaxResult
     * @param ORDER
     * @param attribut_session
     * @param methodID
     * @return
     */
    public Result getARangeOfAllAvaillabilityTrucks(Date startDate, Date endDate, String truckZipCode, int distance_Within, 
            boolean excludeWeekend, int sortParam, int dotNumber, float latitude, float longitude, float latMin,  float latMax, 
             float lonMin, float lonMax, int index, int nombreMaxResult, String attribut_session, int ORDER, int methodID);
    
    /**
     *
     * @param startDate
     * @param endDate
     * @param truckZipCode
     * @param excludeWeekend
     * @param sortParam
     * @param dotNumber
     * @param index
     * @param nombreMaxResult
     * @param attribut_session
     * @param ORDER
     * @param methodID
     * @return
     */
    public Result getARangeOfAllAvaillabilityTrucksWithoutDistance(Date startDate, Date endDate, String truckZipCode, 
            boolean excludeWeekend, int sortParam, int dotNumber, int index, int nombreMaxResult, String attribut_session, 
            int ORDER, int methodID);


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
     * @param latMin
     * @param latMax
     * @param lonMin
     * @param lonMax
     * @param generalLiability
     * @param insuranceLiability
     * @param dotNumber
     * @param index
     * @param nombreMaxResult
     * @param attribut_session
     * @param ORDER
     * @param methodID
     * @return
     */
    public Result getARangeOfAvailabilityTrucksFiltered(Date startDate, Date endDate, String truckZipCode, int distance_Within, 
            boolean excludeWeekend, int sortParam, int dotNumber, float latitude, float longitude, float latMin,  float latMax, 
            float lonMin, float lonMax, int truckAxleID, int generalLiability, int insuranceLiability, int index, int nombreMaxResult,
            String attribut_session, int ORDER, int methodID);
    
    /**
     *
     * @param startDate
     * @param endDate
     * @param truckZipCode
     * @param excludeWeekend
     * @param sortParam
     * @param dotNumber
     * @param truckAxleID
     * @param generalLiability
     * @param insuranceLiability
     * @param index
     * @param nombreMaxResult
     * @param attribut_session
     * @param ORDER
     * @param methodID
     * @return
     */
    public Result getARangeOfAvailabilityTrucksFilteredWithoutDistance(Date startDate, Date endDate, String truckZipCode,
            boolean excludeWeekend, int sortParam, int dotNumber, int truckAxleID, int generalLiability, 
            int insuranceLiability, int index, int nombreMaxResult, String attribut_session, int ORDER, int methodID);
    
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
