/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package admin.jobManagement.metier;

import toolsAndTransversalFunctionnalities.Result;

/**
 *
 * @author erman
 */
public interface IAdminJobManagementWs {

    /**
     *
     * @param accountID
     * @param admin
     * @param actionID
     * @param message
     * @return
     */
    //public String applyActionToUser(int accountID, String admin, int actionID, String message);
    
    /**
     * Permet de recherche un ensemble de jobs selon la categorie.
     * 
     * Exemple: si l'on souhaite avoir tous les jobs qui ont été supprimé en BD:
      getARangeOfCategorieJobs("DELETED", true, 2, 0, 0)
      
      si l'on souhaite avoir tous les truckOwners non supprimé en BD:
      getARangeOfCategorieJobs("DELETED", false, 2, 0, 0)
     * 
     *
     * @param categorie prend ses valeur dans l'ensemble [POSTED, ACTIF, CLOSED, DELETED, PASSED, ALL]
     * @param categorieValue est un booleen (TRUE ou FALSE)
     * @param index l'indice à partir duquel on pourra retourner le premier resultat(Sert a la pargination)
     * @param nombreMaxResult nombre max de resultat a retourné
     * @return
     */
    public Result getARangeOfCategorieJobs(String categorie, boolean categorieValue, int index, int nombreMaxResult);
    /**
     * @param statDate
     * @param endDate
     * @return Les informations rétournées sont concaténée par '##' et dans l'ordre suivant:
     *          numberOfPostedJob: Nombre de de posted job
     *          numberOfClosedJob: Nombre de closed job
     *          numberOfDeletedJob: nombre de job supprimé
     *          numberOfStartedJob: nombre de job actif ou started job
     *          numberOfPassedJob: nombre de passed job
     *          numberOfAllJob: nombre de tous les job existant en BD
     * 
     * Description: Ce service retourne les informations sur le nombres de jobs créer dans une plage de temps.
     * 
     * Exemple:
     *      pour avoir les stats sur tous les jobs: getJobsStatisticsInDateRange(0, 0)
     *      pour avoir les stats sur les jobs créer entre '2011-12-22 13:16:04' et le '2015-02-21 23:02': getJobsStatisticsInDateRange(1324559764039, 1424559764039)
     *      pour avoir les stats sur les jobs créer à partir du '2011-12-22 13:16:04': getJobsStatisticsInDateRange(1324559764039, 0)
     *      pour avoir les stats sur les jobs créer avant le '2015-02-21 23:02': getJobsStatisticsInDateRange(0, 1424559764039)
     * 
     */
    public String getJobsStatisticsInDateRange(long statDate, long endDate);
    
    /**
     * Permet d'obtenir le nombre d'utilisateur par type d'utilisateur. 
     *
     * @param categorie
     * @param categorieValue
     * @return retourne une chaine de caractère contenat "##" comme separateur des élements statistiques.
     */
    //public String  getCurrentJobsStatistics(String categorie, boolean categorieValue);
    
    /**
     *
     * @param statDate
     * @param endDate
     * @param isPaid
     * @param index
     * @param nombreMaxResult
     * @return
     */
    //public Result getARangeOfUserInDateRange(long statDate, long endDate, boolean isPaid, int index, int nombreMaxResult);
    
}
