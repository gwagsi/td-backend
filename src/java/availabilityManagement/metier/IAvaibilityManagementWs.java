/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package availabilityManagement.metier;

import java.util.List;
import toolsAndTransversalFunctionnalities.Result;

/**
 *
 * @author erman
 */
public interface IAvaibilityManagementWs {
    
    /**
     *
     * @param unavailable_dates
     * @param truckID
     * @param truckOwnerID
     * @return
     */
    public String addAvailability(List<Long> unavailable_dates, int truckID, int truckOwnerID);

    /**
     *
     * @param availabilityID
     * @param day
     * @param timeRange
     * @param accountID
     * @param truckID
     * @return
     */
    public String editAvailability(int availabilityID, String day,  String timeRange, int accountID, int truckID);
    
    /**
     *
     * @param availabilityID
     * @return
     */
    public String deleteAvailability(int availabilityID);

       
    /**
     *
     * @param truckID
     * @param year
     * @return
     */
    public String deleteAvailabilityByYear(int truckID, String year);

    
    /**
     *
     * @param availabilityByID
     * @return
     */
    public Result getAvailabilityByID(int availabilityByID);
    
    /**
     *
     * @param truckID
     * @param index
     * @param nombreMaxResultat
     * @return
     */
    public Result getARangeOfAvailabilityByTruckID(int truckID, int index, int nombreMaxResultat);
   
    /**
     *
     * @param truckOwnerID
     * @param year
     * @param truckID
     * @return
     */
    public Result getARangeOfAvailabilityByTruckOwnerID(int truckOwnerID, String year, int truckID);
    
    /**
     *
     * @param address
     * @return
     */
    public String verifyAddress(String address);
   
    /**
     *
     * @param truckID
     * @param year
     * @return
     */
    public Result getAllUnAvailabilityTruckDateByYear(int truckID, int year);
    
}
