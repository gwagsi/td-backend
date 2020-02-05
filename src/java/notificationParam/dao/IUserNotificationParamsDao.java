/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package notificationParam.dao;

import toolsAndTransversalFunctionnalities.Result;
import util.exception.UserNotificationParamsException;


/**
 *
 * @author erman
 */
public interface IUserNotificationParamsDao {
    
    /**
     * Cette méthode permet de definir les paramètres de notification d'un TruckOwner.
     *
     * @param accountID: identifiant de l'excavator
     * @param email: l'email du Truckowner
     * @param latitude: la latitude des coordonnées GPS du TruckOwner
     * @param longitude: la longitude des coordonnées GPS du TruckOwner
     * @param zipCode: le code zip du TruckOwner
     * @param raduis: Rayon maximal dans lequel l'utilisateur aimerai ếtre notifier
     * @return
     * @throws util.exception.UserNotificationParamsException
     */
    public String addNewNotificationParams (int accountID, String email, float latitude, float longitude, String zipCode, float raduis)throws UserNotificationParamsException /*throws UserNotificationParamsException*/;
    
    /**
     * Cette méthode permet de d'éditer les informations d'un employé ainsi que son code de validation.
     * 
     * @param accountID: identifiant de l'excavator
     * @param notificationParamID: identifiant de l'employé
     * @param email: l'email du Truckowner
     * @param latitude: la latitude des coordonnées GPS du TruckOwner
     * @param longitude: la longitude des coordonnées GPS du TruckOwner
     * @param zipCode: le code zip du TruckOwner
     * @param raduis: Rayon maximal dans lequel l'utilisateur aimerai ếtre notifier
     * @return
     * @throws util.exception.UserNotificationParamsException
     */
    public String editNotificationParams(int accountID, int notificationParamID, String email, float latitude, float longitude, String zipCode, float raduis)throws UserNotificationParamsException;
    
    /**
     * Supprimer les informations de notification du TruckOwner afin d'envoyer les mails à tous les nouveaux jobs ajoutés.
     *
     * @param accountID: identifiant du compte
     * @param notificationParamID: identifiant des informations de notification
     * @return
     * @throws util.exception.UserNotificationParamsException
     */
    public String deleteNotificationParams(int accountID, int notificationParamID)throws UserNotificationParamsException;
    
    /**
     *
     * @param accountID
     * @param index
     * @param nombreMaxResultat
     * @return
     * @throws util.exception.UserNotificationParamsException
     */
    public Result getARangeOfUserNotificationParam(int accountID, int index, int nombreMaxResultat)throws UserNotificationParamsException;
    
}
