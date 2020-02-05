/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package availabilityManagement.dao;

import java.util.List;
import toolsAndTransversalFunctionnalities.Result;
import util.exception.AvaibilityManagementException;

/**
 *
 * @author erman
 */
public interface IAvaibilityManagementDao {
    
    /**
     *
     * @param unavailable_dates
     * @param truckID
     * @param truckOwnerID
     * @return
     * @throws util.exception.AvaibilityManagementException
     */
    public String addAvailability(List<Long> unavailable_dates, int truckID, int truckOwnerID) throws AvaibilityManagementException;

    /**
     *
     * @param availabilityID
     * @param day
     * @param timeRange
     * @param accountID
     * @param truckID
     * @return
     * @throws util.exception.AvaibilityManagementException
     */
    public String editAvailability(int availabilityID, String day, String timeRange, int accountID, int truckID)throws AvaibilityManagementException;
    
    /**
     *
     * @param availabilityID
     * @return
     * @throws util.exception.AvaibilityManagementException
     */
    public String deleteAvailability(int availabilityID) throws AvaibilityManagementException;

    /**
     *
     * @param truckID
     * @param year
     * @return
     * @throws util.exception.AvaibilityManagementException
     */
    public String deleteAvailability(int truckID, String year) throws AvaibilityManagementException;

    /**
     *
     * @param availabilityByID
     * @return
     * @throws util.exception.AvaibilityManagementException
     */
    public Result getAvailabilityByID(int availabilityByID) throws AvaibilityManagementException;
    
    /**
     *
     * @param truckID
     * @param index
     * @param nombreMaxResultat
     * @return
     * @throws util.exception.AvaibilityManagementException
     */
    public Result getARangeOfAvailabilityByTruckID(int truckID, int index, int nombreMaxResultat)throws AvaibilityManagementException;

    /**
     *
     * @param truckOwnerID
     * @param year
     * @param truckID
     * @return
     * @throws util.exception.AvaibilityManagementException
     */
    public Result getARangeOfAvailabilityByTruckOwnerID(int truckOwnerID, String year, int truckID) throws AvaibilityManagementException;
    
    /**
     *
     * @param address
     * @return
     * @throws util.exception.AvaibilityManagementException
     */
    public String verifyAddress(String address) throws AvaibilityManagementException;
    
    /**
     *
     * @param truckID
     * @param year
     * @return
     * @throws util.exception.AvaibilityManagementException
     */
    public Result getAllUnAvailabilityTruckDateByYear(int truckID, int year) throws AvaibilityManagementException;
    
    
}
