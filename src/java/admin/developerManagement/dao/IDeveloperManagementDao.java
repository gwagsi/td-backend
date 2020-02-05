/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package admin.developerManagement.dao;

import toolsAndTransversalFunctionnalities.Result;
import toolsAndTransversalFunctionnalities.ResultBackend;
import util.exception.DeveloperManagementException;

/**
 *
 * @author erman
 */
public interface IDeveloperManagementDao {
    
    /**
     *
     * @param firstName
     * @param lastName
     * @param login
     * @param password
     * @param email
     * @param timezoneID
     * @return  Message d'erreur lors de la création des driver:
     * ExistingLogin: par defaut c'est le numero de téléphone, donc ne peut pas être utilisé comme login
     * ExistingEmail: l'email existe deja
     * NotExistingSocialStatus: le social status n'existe pas en BD
     * InvalidAccountID: mauvais account ID du TruckOwner
     * @throws util.exception.DeveloperManagementException
     * 
     */
    public ResultBackend addNewDeveloper(String firstName, String lastName, String login, String password, String email, String timezoneID) throws DeveloperManagementException;
    
    /**
     *
     * @param developerID
     * @param firstName
     * @param lastName
     * @param timezoneID
     * @param email
     * @return Message d'erreur pour l'édition:
     * ExistingLogin: par defaut c'est le numero de téléphone, donc ne peut pas être utilisé comme login
     * ExistingEmail: l'email existe deja
     * NotExistingSocialStatus: le social status n'existe pas en BD
     * InvalidAccountID: mauvais account ID du TruckOwner
     * NotExistingDriver: L'ID du driver n'existe pas
     * @throws util.exception.DeveloperManagementException
     * 
     */
    public ResultBackend editDeveloper(int developerID, String firstName, String lastName, String email, String timezoneID) throws DeveloperManagementException;
    
    /**
     *
     * @param index
     * @param nombreMaxResultat
     * @return
     */
    public Result getARangeOfDeveloper(int index, int nombreMaxResultat);
    
    /**
     *
     * @param developerID
     * @return
     * @throws util.exception.DeveloperManagementException
     */
    public String deleteDeveloper(int developerID) throws DeveloperManagementException;
}
