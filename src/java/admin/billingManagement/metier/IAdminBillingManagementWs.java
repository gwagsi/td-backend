/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package admin.billingManagement.metier;
import toolsAndTransversalFunctionnalities.Result;

/**
 *
 * @author Sorelus
 */
public interface IAdminBillingManagementWs {

  
    
     /**
     *
     * Permet d'avoir le nombre de personne ayant payé ou pas; cela dependra du status value sur une periode compris entre plageStart et plageEnd
     * si c'est true, c'est le bombre d'utilisateurs ayant payés si c'est false, c'est le contraire.
     * 
     * @param statusValue
     * @param statDate
     * @param endDate
     * @return 
     * 
     * Description:ce service retourne le nombre d'utilisateur ayant payé ou pas sur une pariode
     *      
     */
    
    public Result getNumberUserBilling( boolean statusValue,long statDate, long endDate);
    
    
      /**
     *
     * Permet d'avoir le nombre de personne ayant payé ou pas; cela dependra du status value sur une periode compris entre statDate et plageEnd
     * si c'est true, c'est le bombre d'utilisateurs ayant payés si c'est false, c'est le contraire.
     * 
     * @param statusValue
     * @param statDate
     * @param endDate
     * @param index
     * @param nombreMaxResult
     * @return 
     * 
     * Description:ce service retourne le nombre d'utilisateur ayant payé ou pas sur une pariode
     *      
     */
    public Result getListUserBilling( boolean statusValue,long statDate, long endDate, int index, int nombreMaxResult);
    
}


