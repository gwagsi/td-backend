/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package truckTypeManagement.dao;

import toolsAndTransversalFunctionnalities.Result;
import util.exception.TruckTypeManagementException;

/**
 *
 * @author erman
 */
public interface ITruckTypeManagementDao {

    /**
     *
     * @param attribut
     * @param index
     * @param nombreMaxResultat
     * @param methodName
     * @return
     * @throws util.exception.TruckTypeManagementException
     */
    public Result getARangeOfTruckTypeByOneAttribut(String attribut, int index, int nombreMaxResultat, String methodName)throws TruckTypeManagementException;
    
    /**
     *
     * @param attribut1
     * @param attribut2
     * @param index
     * @param nombreMaxResultat
     * @param methodName
     * @return
     * @throws util.exception.TruckTypeManagementException
     */
    public Result getARangeOfTruckTypeByTwoAttribut(String attribut1, String attribut2, int index, int nombreMaxResultat, String methodName)throws TruckTypeManagementException;
    
    /**
     *
     * @param attribut1
     * @param attribut2
     * @param attribut3
     * @param index
     * @param nombreMaxResultat
     * @param methodName
     * @return
     * @throws util.exception.TruckTypeManagementException
     */
    public Result getARangeOfTruckTypeByThreeAttribut(String attribut1, String attribut2, String attribut3, int index, int nombreMaxResultat, String methodName)throws TruckTypeManagementException;
    
    /**
     *
     * @return
     * @throws util.exception.TruckTypeManagementException
     */
    public Result getYearAllOfTruckType()throws TruckTypeManagementException;
    
    /**
     *
     * @param year
     * @return
     * @throws util.exception.TruckTypeManagementException
     */
    public Result getTruckTypeMakeByYear(String year)throws TruckTypeManagementException;
    
    /**
     *
     * @param make
     * @param year
     * @return
     * @throws util.exception.TruckTypeManagementException
     */
    public Result getTruckTypeModel(String make, String year)throws TruckTypeManagementException;
            
}
