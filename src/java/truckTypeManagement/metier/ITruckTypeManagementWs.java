/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package truckTypeManagement.metier;

import toolsAndTransversalFunctionnalities.Result;

/**
 *
 * @author erman
 */
public interface ITruckTypeManagementWs {
    
    /**
     *
     * @param model
     * @param index
     * @param nombreMaxResultat
     * @return
     */
    public Result getARangeOfTruckTypeByModel(String model, int index, int nombreMaxResultat);
    
    /**
     *
     * @return
     */
    public Result getYearAllOfTruckType();
    
     /**
     *
     * @param make
     * @param year
     * @return
     */
    public Result getTruckTypeModel(String make, String year);
    
    /**
     *
     * @param year
     * @return
     */
    public Result getTruckTypeMakeByYear(String year);
    
    /**
     *
     * @param make
     * @param index
     * @param nombreMaxResultat
     * @return
     */
    public Result getARangeOfTruckTypeByMake(String make, int index, int nombreMaxResultat);
    
    /**
     *
     * @param year
     * @param index
     * @param nombreMaxResultat
     * @return
     */
    public Result getARangeOfTruckTypeByYear(String year, int index, int nombreMaxResultat);
    
    /**
     *
     * @param make
     * @param model
     * @param index
     * @param nombreMaxResultat
     * @return
     */
    public Result getARangeOfTruckTypeByMM(String make, String model, int index, int nombreMaxResultat);
    
    /**
     *
     * @param make
     * @param year
     * @param index
     * @param nombreMaxResultat
     * @return
     */
    public Result getARangeOfTruckTypeByMakeY(String make, String year, int index, int nombreMaxResultat);
    
    /**
     *
     * @param make
     * @param year
     * @param index
     * @param nombreMaxResultat
     * @return
     */
    public Result getARangeOfTruckTypeByModelY(String make, String year, int index, int nombreMaxResultat);
    
    /**
     *
     * @param make
     * @param model
     * @param year
     * @param index
     * @param nombreMaxResultat
     * @return
     */
    public Result getARangeOfTruckTypeByMMY(String make, String model, String year, int index, int nombreMaxResultat);
    
}
