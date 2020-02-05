/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package admin.developerManagement.metier;

import toolsAndTransversalFunctionnalities.Result;

/**
 *
 * @author erman
 */
public interface IDeveloperManagementWs {
    
    /**
     *
     * @param firstName: First name of the developper
     * @param lastName: Last name of the developper
     * @param login: Login of the developper
     * @param password: Password of the developper
     * @param email: email address of the developper. An email will be send to this address to inform the developper about his parameters.
     * @param timezoneID: Timezone ID of the developper, it's set to "Africa/Abidjan" by default.
     * @return a string containing the following information:
     *          good: When anything it's Ok
     *          ExistingUsername: When the login already exists in database
     *          ExistingEmail: When the developper email already exists in database
     *        Any developper information are concatenate with semi-colon as follow:
     *              developperID ";"//00
     *              developperFirstName + ";"
     *              developper.getLastName()+ ";"
     *              developperEmail + ";"//03
     *              developperCreationDate + ";"//04 This information is in long
     *              developperTimezoneID() + ";"//05: This information is a String refering to the timezoneID of the devolopper.
     *          + "null"; //06
     */
    public String addNewDeveloper(String firstName, String lastName, String login, String password, String email, String timezoneID);
    
    /**
     *
     * @param index
     * @param nombreMaxResultat
     * @return a Result object containing the folloqing information:
     *          good: When anything has done correctly and the list of developper's information. The can be empty depending on database result.
     *          InternalError: for any other error
     */
    public Result getARangeOfDeveloper(int index, int nombreMaxResultat);
    
    /**
     *
     * @param developerID: The developper ID
     * @param firstName: Developper's first name
     * @param lastName: developper's last name
     * @param email: new developper email address
     * @param timezoneID: Developper's timezoneID
     * @return a string containing the following information:
     *          good: if anything it's Ok
     *          NotExistingDeveloper: If developper ID doesn't match to any other developper ID
     */
    public String editDeveloper(int developerID, String firstName, String lastName, String email, String timezoneID);
   
    /**
     *
     * @param developerID: The developper ID
     * @return a string containing the following information:
     *          good: if anything it's Ok
     *          NotExistingDeveloper: If developper ID doesn't match to any other developper ID
     */
    public String deleteDeveloper(int developerID);
}
