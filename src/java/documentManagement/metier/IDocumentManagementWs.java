/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package documentManagement.metier;

import java.util.List;
import toolsAndTransversalFunctionnalities.Result;

/**
 *
 * @author erman
 */
public interface IDocumentManagementWs {
    
    /**
     *
     * @param pathNames
     * @param accountID
     * @return
     */
    public String addNewDocument(List<String> pathNames, int accountID);
    
    /**
     *
     * @param pathNamesID
     * @param accountID
     * @return
     */
    public String deletedDocument(List<String> pathNamesID, int accountID);
    
    /**
     *
     * @param accountID
     * @param index
     * @param nombreMaxResultat
     * @return
     */
    public Result getARangeOfDocumentByAccountID(int accountID, int index, int nombreMaxResultat);
    
    /**
     *
     * @param pathNamesID
     * @param accountID
     * @param bindingID
     * @param bindingObject
     * @param typeOfDocument
     * @return
     */
    public String bindNewDocument(List<String> pathNamesID, int accountID, int bindingID, String bindingObject, String typeOfDocument);
    
    
}
