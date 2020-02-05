/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pushNotificationParam.metier;

import toolsAndTransversalFunctionnalities.Result;

/**
 *
 * @author erman
 */
public interface IPushNotificationWs {
    
     
    /**
     * Cette méthode permet de definir les paramètres de notification d'un utilisateur.
     *
     * @param accountID: identifiant de l'utilisateur qui doit recevoir les notifications
     * @param phoneID: Identifiant du téléphone
     * @param registrationID: Identifiant de l'utilisateur généré par sa plateforme(ANDROID, IOS, ...)
     * @param platteformID: Identifiant de la platteforme
     * @return
     */
    public String addPhoneNotificationParam (int accountID, String phoneID, String registrationID, int platteformID);
    
    /**
     * Changer le status d'une notification pour job(de non lu à lu).
     *
     * @param accountID: identifiant du compte
     * @param jobNotificationID: identifiant des informations de notification
     * @return
     */
    public String updateJobNotificationStatus(int accountID, int jobNotificationID);
    
    /**
     *
     * @param accountID
     * @param index
     * @param nombreMaxResultat
     * @return
     */
    public Result getARangeOfJobNotification(int accountID, int index, int nombreMaxResultat);
    
}
