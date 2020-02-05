/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package driverManagement.metier;

import toolsAndTransversalFunctionnalities.Result;

/**
 *
 * @author erman
 */
public interface IDriverManagementWs {
    
    /**
     *
     * @param name
     * @param surname
     * @param cardNumber
     * @param email
     * @param telephone
     * @param accountID
     * @param picture
     * @param address
     * @param license
     * @return
     */
    public String addNewDriver(String name, String surname, String cardNumber, String email, String telephone, int accountID, String picture, String address, String license);
    
    /**
     *
     * @param accountID
     * @param index
     * @param nombreMaxResultat
     * @return
     */
    public Result getARangeOfDriverByUser(int accountID, int index, int nombreMaxResultat);
    
    /**
     *
     * @param driverID
     * @param name
     * @param surname
     * @param cardNumber
     * @param email
     * @param telephone
     * @param accountID
     * @param picture
     * @param address
     * @param license
     * @return
     */
    public String modifyDriverInfo(int driverID, String name, String surname, String cardNumber, String email, String telephone, int accountID, String picture, String address, String license);
   
    /**
     *
     * @param driverID
     * @return
     */
    public String deleteDriver(int driverID);
}
