/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package admin.issueManagement.metier;

import toolsAndTransversalFunctionnalities.Result;

/**
 *
 * @author erman
 */
public interface IIssueManagementWs {
    
    /**
     *
     * @param accountID: Account ID of user who is adding the issue(Truckowner, Excavator, Simple User, Gov Agency)
     * @param category: Issue's category
     * @param subcategory: Issue's subcategory
     * @param description: Issue's description
     * 
     * @return a string containing the following information:
     *          good: When anything it's Ok
     *          InvalidAccountID: When the accountID doesn't exist in database
     *          InternalError: For any another error
     */
    public String addNewIssue(int accountID, String category, String subcategory, String description);
    
    /**
     * This function retrieve all issue in the specified range in database
     *
     * @param index
     * @param nombreMaxResultat
     * @return a Result object containing the folloqing information:
     *          good: When anything has done correctly and the list of developper's information. The can be empty depending on database result.
     *          InternalError: For any other error
     *        Any issue information are concatenate with semi-colon as follow:
     *              IssueID ";"//00
     *              Category + ";"
     *              Subcategory()+ ";"
     *              Description + ";"//03
     *              issueCreationDate + ";"//04 This information is a long
     *              Status + ";"//05 This information is a boolean
     *              userEmail + ";"//06 The emal of the user who has created the issue
     *              userName + ";"//07 The name user who has created the issue
     *              userSurname + ";"//08 The surname user who has created the issue
     *              issueEditionDate + ";"//09 This information is a long
     */
    public Result getARangeOfIssue(int index, int nombreMaxResultat);
    
    
    /**
     *
     * @param accountID
     * @param index
     * @param nombreMaxResultat
     * @return a Result object containing the folloqing information:
     *          good: When anything has done correctly and the list of developper's information. The can be empty depending on database result.
     *          InvalidAccountID: When the accout doesn't exist in database
     *          InternalError: for any other error
     *        Any issue information are concatenate with semi-colon as follow:
     *              IssueID ";"//00
     *              Category + ";"
     *              Subcategory()+ ";"
     *              Description + ";"//03
     *              issueCreationDate + ";"//04 This information is a long
     *              Status + ";"//05 This information is a boolean
     *              userEmail + ";"//06 The emal of the user who has created the issue
     *              userName + ";"//07 The name user who has created the issue
     *              userSurname + ";"//08 The surname user who has created the issue
     *              issueEditionDate + ";"//09 This information is a long
     */
    public Result getARangeOfIssueByAccountID(int accountID, int index, int nombreMaxResultat);
    
    /**
     *
     * @param accountID
     * @param issueID
     * @return a Result object containing the folloqing information:
     *          good: When anything has done correctly and the list of developper's information. The can be empty depending on database result.
     *          InvalidAccountID: When the accout doesn't exist in database
     *          InvalidIssueID: when the issue doesn't exist in database
     *          InternalError: for any other error
     *        Any issue information are concatenate with semi-colon as follow:
     *              IssueID ";"//00
     *              Category + ";"
     *              Subcategory()+ ";"
     *              Description + ";"//03
     *              issueCreationDate + ";"//04 This information is a long
     *              Status + ";"//05 This information is a boolean
     *              userEmail + ";"//06 The emal of the user who has created the issue
     *              userName + ";"//07 The name user who has created the issue
     *              userSurname + ";"//08 The surname user who has created the issue
     *              issueEditionDate + ";"//09 This information is a long
     *              deleted + ";"//10 This information is a boolean
     */
    public Result getIssueByID(int accountID, int issueID);
    
    /**
     * @param accountID
     * @param issueID
     * @param category
     * @param subcategory
     * @param description
     * @return a string containing the following information:
     *          good: if anything it's Ok
     *          InvalidAccountID: When the accout doesn't exist in database
     *          InvalidIssueID: If issue doesn't exist
     */
    public String editIssue(int accountID, int issueID, String category, String subcategory, String description);
   
    /**
     *
     * @param accountID
     * @param issueID
     * @return a string containing the following information:
     *          good: if anything it's Ok
     *          InvalidAccountID: When the accout doesn't exist in database
     *          InvalidIssueID: If issue doesn't exist
     */
    public String deleteIssue(int accountID, int issueID);
    
    /**
     *
     * @param usernameID
     * @param issueID
     * @param solution
     * @return a string containing the following information:
     *          good: if anything it's Ok
     *          InvalidUsernameID: When the accout doesn't exist in database
     *          InvalidIssueID: If issue doesn't exist
     */
    public String resolveIssue(String usernameID, int issueID, String solution);
}
