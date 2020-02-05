/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package admin.developerManagement.metier;

import admin.developerManagement.dao.IDeveloperManagementDaoLocal;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.ejb.Stateless;
import mailing.mailSending.MailSendingForDeveloperAccountCreation;
import toolsAndTransversalFunctionnalities.Result;
import toolsAndTransversalFunctionnalities.ResultBackend;
import util.exception.DeveloperManagementException;

/**
 *
 * @author erman
 */
@WebService(serviceName = "DeveloperManagementWs")
@Stateless()
public class DeveloperManagementWs implements IDeveloperManagementWs{
    
    @EJB
    private IDeveloperManagementDaoLocal dao;
    
    @WebMethod(operationName = "addNewDeveloper")
    @Override
    public String addNewDeveloper(String firstName, String lastName, String login, String password, String email, String timezoneID) {
        String res;
        ResultBackend result;
        
        try {

            if (timezoneID.equals("")) {
                timezoneID = null;
            }

            result = dao.addNewDeveloper(firstName, lastName, login, password, email, timezoneID);

            res = result.getMsg();
            
            /*
            */
            if (res != null && res.contains("good")) {

                String[] developerMailInfo = (String[]) result.getResultArraysList().get(0);
                MailSendingForDeveloperAccountCreation sendMsgAddNewJob = new MailSendingForDeveloperAccountCreation();
                sendMsgAddNewJob.sendAccountCreationMail(developerMailInfo);

            }

        } catch (DeveloperManagementException e) {
            res = e.getSmallMessage();
            return res;
        }

        return res;
    }

    @WebMethod(operationName = "getARangeOfDeveloper")
    @Override
    public Result getARangeOfDeveloper(int index, int nombreMaxResultat) {
        Result result = new Result();
        try{
            
            result = dao.getARangeOfDeveloper(index, nombreMaxResultat);
            
        }catch(Throwable e){
                StringWriter string = new StringWriter();
                PrintWriter str = new PrintWriter(string);
                e.printStackTrace(str);
                result.setMsg(string.toString());
                return result;
        }
        
        result.afficherResult("getARangeOfDriverByUser");
        return result;
    }

    @WebMethod(operationName = "editDeveloper")
    @Override
    public String editDeveloper(int developerID, String firstName, String lastName, String email, String timezoneID) {
        String res;
        ResultBackend result;
        try {

            result = dao.editDeveloper(developerID, firstName, lastName, email, timezoneID);

            res = result.getMsg();
            
        }catch (DeveloperManagementException e) {
            res = e.getSmallMessage();
            return res;
        }

        return res;
    }

    @WebMethod(operationName = "deleteDeveloper")
    @Override
    public String deleteDeveloper(int developerID){
        String res;
        try{
            
            res = dao.deleteDeveloper(developerID);
            
        }catch (DeveloperManagementException e) {
            res = e.getSmallMessage();
            return res;
        }
        
        return res;
    }
    
    
}
