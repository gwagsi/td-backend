/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package admin.issueManagement.dao;

import toolsAndTransversalFunctionnalities.Result;
import toolsAndTransversalFunctionnalities.ResultBackend;
import util.exception.IssueManagementException;

/**
 *
 * @author erman
 */
public interface IIssueManagementDao {
    
    /**
     *
     * @param accountID: Account ID of user who is adding the issue
     * @param category: Issue's category
     * @param subcategory: Issue's subcategory
     * @param description: Issue's description
     * 
     * @return 
     * @throws util.exception.IssueManagementException
     * 
     */
    public ResultBackend addNewIssue(int accountID, String category, String subcategory, String description) throws IssueManagementException;
    
    /**
     *
     * @param accountID
     * @param issueID
     * @param category
     * @param subcategory
     * @param description
     * @return Message d'erreur pour l'édition:
     * ExistingLogin: par defaut c'est le numero de téléphone, donc ne peut pas être utilisé comme login
     * ExistingEmail: l'email existe deja
     * NotExistingSocialStatus: le social status n'existe pas en BD
     * InvalidAccountID: mauvais account ID du TruckOwner
     * NotExistingDriver: L'ID du driver n'existe pas
     * @throws util.exception.IssueManagementException
     * 
     */
    public ResultBackend editIssue(int accountID, int issueID, String category, String subcategory, String description) throws IssueManagementException;
    
    /**
     *
     * @param index
     * @param nombreMaxResultat
     * @return
     */
    public Result getARangeOfIssue(int index, int nombreMaxResultat);
    
    /**
     *
     * @param accountID
     * @param index
     * @param nombreMaxResultat
     * @return
     */
    public Result getARangeOfIssueByAccountID(int accountID, int index, int nombreMaxResultat);
    
    /**
     *
     * @param accountID
     * @param issueID
     * @return
     */
    public Result getIssueByID(int accountID, int issueID);
    
    /**
     *
     * @param accountID: Account ID of the user who is deleted the issue
     * @param issueID
     * @return
     * @throws util.exception.IssueManagementException
     */
    public String deleteIssue(int accountID, int issueID) throws IssueManagementException;
    
    /**
     *
     * @param usernameID:  Account ID of the user who is deleted the issue
     * @param issueID
     * @param solution
     * @return
     * @throws util.exception.IssueManagementException
     */
    public String resolveIssue(String usernameID, int issueID, String solution) throws IssueManagementException;
}
