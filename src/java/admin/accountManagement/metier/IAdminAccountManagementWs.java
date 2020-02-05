/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package admin.accountManagement.metier;

import toolsAndTransversalFunctionnalities.Result;

/**
 *
 * @author erman
 */
public interface IAdminAccountManagementWs {

    /**
     * Cette methode permet d'eefectuer une action sur un utilisateur, comme par exemple
     * suspendre(SUSPEND) son compte, le rendre actif (ACTIVATE) ou supprimé son compte (DELETED).
     *
     * @param accountID identifiant de l'utilisateur dont on souhaite changer l'état.
     * @param admin identifiant de l'administateur qui change l'état de l'utilisateur
     * @param actionID identifiant de l'action a réaliser qui prend sa valeur dans l'ensemble{3-DELETED, 2-ACTIVATE, 1-SUSPENDED}
     * @param message  La raison selon laquelle l'on réalise l'action.
     * @return
     */
    public String applyActionToUser(int accountID, String admin, int actionID, String message);
    
    /**
     * Permet de recherche un ensemble d'utilisateur selon la categorie (une catégorie peut être Suspendu, connecté, supprimé ou actif)
     * et le type d'utilisateur (truckOwenr, Excavator, Simple User, ...)
     * Exemple: si l'on souhaite avoir tous les truckOwners supprimé en BD:
     *      getARangeOfCategorieUser("DELETED", true, 2, 0, 0)
     *      
     *      si l'on souhaite avoir tous les truckOwners non supprimé en BD:
     *      getARangeOfCategorieUser("DELETED", false, 2, 0, 0)
     * 
     *
     * @param categorie prend ses valeur dans l'ensemble [SUSPEND, ACTIF, CONNECTED, DELETED]
     * @param categorieValue est un booleen (TRUE ou FALSE)
     * @param typeOfUser correspond ici à l'idendifiant du status social (socialStatusID)
     * @param index l'indice à partir duquel on pourra retourner le premier resultat(Sert a la pargination)
     * @param nombreMaxResult nombre max de resultat a retourné
     * @return
     */
    public Result getARangeOfCategorieUser(String categorie, boolean categorieValue, int typeOfUser, int index, int nombreMaxResult);
    
    /**
     * Permet d'obtenir le nombre d'utilisateur:
     *  - ayant créé le compte au courant de la semaine
     *  - ayant eu des compte actif dans la semaine en cours
     *  - supprimé au courant de la semaine
     *
     * @param statDate
     * @param endDate
     * @return retourne une chaine de caractère contenant "##" comme separateur. Le premier élément de la chaine 
     * correspond au nombre de compte créé, le second correspond au nombre de ayant des compte actif et le troisieme
     * correspond au nombre d'utilisateur supprimé au cour de l'année.
     */
    public String getUsersStatisticsInDateRange(long statDate, long endDate);
    
    /**
     * Permet d'obtenir le nombre d'utilisateur par type d'utilisateur. 
     *
     * @param categorie
     * @param categorieValue
     * @return retourne une chaine de caractère contenat "##" comme separateur des élements statistiques.
     */
    public String  getCurrentUsersStatistics(String categorie, boolean categorieValue);
    
    /**
     *
     * @param statDate
     * @param endDate
     * @param isPaid
     * @param index
     * @param nombreMaxResult
     * @return
     */
    public Result getARangeOfUserInDateRange(long statDate, long endDate, boolean isPaid, int index, int nombreMaxResult);
    
}
