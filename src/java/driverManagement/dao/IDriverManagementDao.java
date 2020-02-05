/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package driverManagement.dao;

import toolsAndTransversalFunctionnalities.Result;
import toolsAndTransversalFunctionnalities.ResultBackend;
import util.exception.DriverManagementException;

/**
 *
 * @author erman
 */
public interface IDriverManagementDao {
    
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
     * @return  Message d'erreur lors de la création des driver:
     * ExistingLogin: par defaut c'est le numero de téléphone, donc ne peut pas être utilisé comme login
     * ExistingEmail: l'email existe deja
     * NotExistingSocialStatus: le social status n'existe pas en BD
     * InvalidAccountID: mauvais account ID du TruckOwner
     * @throws util.exception.DriverManagementException
     * 
     */
    public ResultBackend addNewDriver(String name, String surname, String cardNumber, String email, String telephone, int accountID, String picture, String address, String license)throws DriverManagementException;
    
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
     * @return Message d'erreur pour l'édition:
     * ExistingLogin: par defaut c'est le numero de téléphone, donc ne peut pas être utilisé comme login
     * ExistingEmail: l'email existe deja
     * NotExistingSocialStatus: le social status n'existe pas en BD
     * InvalidAccountID: mauvais account ID du TruckOwner
     * NotExistingDriver: L'ID du driver n'existe pas
     * @throws util.exception.DriverManagementException
     * 
     */
    public ResultBackend modifyDriverInfo(int driverID, String name, String surname, String cardNumber, String email, String telephone, int accountID, String picture, String address, String license)throws DriverManagementException;
    
    /**
     *
     * @param accountID
     * @param index
     * @param nombreMaxResultat
     * @return
     * @throws util.exception.DriverManagementException
     */
    public Result getARangeOfDriverByUser(int accountID, int index, int nombreMaxResultat)throws DriverManagementException;
    
    /**
     *
     * @param driverID
     * @return
     * @throws util.exception.DriverManagementException
     */
    public String deleteDriver(int driverID) throws DriverManagementException;
}
