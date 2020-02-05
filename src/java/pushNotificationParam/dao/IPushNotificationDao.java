/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pushNotificationParam.dao;

import toolsAndTransversalFunctionnalities.Result;
import util.exception.PushNotificationException;


/**
 *
 * @author erman
 */
public interface IPushNotificationDao {
    
    /**
     * Cette méthode permet de definir les paramètres de notification d'un utilisateur.
     *
     * @param accountID: identifiant de l'utilisateur qui doit recevoir les notifications
     * @param phoneID: Identifiant du téléphone
     * @param registrationID: Identifiant de l'utilisateur généré par sa plateforme(ANDROID, IOS, ...)
     * @param platteformID: Identifiant de la platteforme
     * @return
     * @throws util.exception.PushNotificationException
     */
    public String addPhoneNotificationParam (int accountID, String phoneID, String registrationID, int platteformID) throws PushNotificationException;
    
    /**
     * Changer le status d'une notification pour job(de non lu à lu).
     *
     * @param accountID: identifiant du compte
     * @param notificationParamID: identifiant des informations de notification
     * @return
     * @throws util.exception.PushNotificationException
     */
    public String updateJobNotificationStatus(int accountID, int notificationParamID)throws PushNotificationException;
    
    /**
     *
     * @param accountID
     * @param index
     * @param nombreMaxResultat
     * @return
     * @throws util.exception.PushNotificationException
     */
    public Result getARangeOfJobNotification(int accountID, int index, int nombreMaxResultat)throws PushNotificationException;
    
}
