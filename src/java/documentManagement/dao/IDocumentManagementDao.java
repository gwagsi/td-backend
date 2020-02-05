/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package documentManagement.dao;

import java.util.List;
import toolsAndTransversalFunctionnalities.Result;
import util.exception.DocumentManagementException;

/**
 *
 * @author erman
 */
public interface IDocumentManagementDao {

    /**
     *
     * @param pathNames
     * @param accountID
     * @return
     * @throws util.exception.DocumentManagementException
     */
    public String addNewDocument(List<String> pathNames, int accountID)throws DocumentManagementException;
    
    /**
     *
     * @param pathNamesID
     * @param accountID
     * @return
     * @throws util.exception.DocumentManagementException
     */
    public String deletedDocument(List<String> pathNamesID, int accountID)throws DocumentManagementException;
    
    /**
     *
     * @param accountID
     * @param index
     * @param nombreMaxResultat
     * @return
     * @throws util.exception.DocumentManagementException
     */
    public Result getARangeOfDocumentByAccountID(int accountID, int index, int nombreMaxResultat)throws DocumentManagementException;
    
    /**
     * Cette methode permettra de lier un ensemble de document existant a un objet et cet objet peut etre:
     * <ul>
     *       <li>Un Truck </li> et dans cas on deux possibilités, soit une image qui est l'image du camion ou soit 
     * un
     *       <li>Un Job </li>
     *       <li>Un Driver </li>
     * </ul>
     *
     * @param pathNamesID correspond a la liste d'identifiant de document à ajouter
     * @param accountID l'identifiant du propriétaire de l'image
     * @param bindingID l'identifiant de l'objet auquel l'on souhaite associé l'image
     * @param bindingObject nom de la classe de l'objet par exemple: Truck, Job, Driver ...
     * @param typeOfDocument type de document.
     * @return
     * @throws util.exception.DocumentManagementException
     */
    public String bindNewDocument(List<String> pathNamesID, int accountID, int bindingID, String bindingObject, String typeOfDocument)throws DocumentManagementException;
    
}
