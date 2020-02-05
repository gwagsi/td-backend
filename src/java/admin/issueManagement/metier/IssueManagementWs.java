/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package admin.issueManagement.metier;

import admin.issueManagement.dao.IIssueManagementDaoLocal;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.ejb.Stateless;
import toolsAndTransversalFunctionnalities.Result;
import toolsAndTransversalFunctionnalities.ResultBackend;
import util.exception.IssueManagementException;

/**
 *
 * @author erman
 */
@WebService(serviceName = "IssueManagementWs")
@Stateless()
public class IssueManagementWs implements IIssueManagementWs{
    
    @EJB
    private IIssueManagementDaoLocal dao;
    
    @EJB
    IssueSendNotificationAsyn iSNAsyn;
    
    @WebMethod(operationName = "addNewIssue")
    @Override
    public String addNewIssue(int accountID, String category, String subcategory, String description) {
        String res;
        
        try {
            
            ResultBackend result = dao.addNewIssue(accountID, category, subcategory, description);

            res = result.getMsg();
            
            if (res != null && res.contains("good") && result.getResultArraysList() != null && !result.getResultArraysList().isEmpty()) {
                iSNAsyn.sendIssueNotificationMail(result.getResultArraysList());
            }

        } catch (IssueManagementException e) {
            res = e.getSmallMessage();
            return res;
        }

        return res;
    }

    @WebMethod(operationName = "getARangeOfIssue")
    @Override
    public Result getARangeOfIssue(int index, int nombreMaxResultat) {
        Result result = new Result();
        try{
            
            result = dao.getARangeOfIssue(index, nombreMaxResultat);
            
        }catch(Throwable e){
                StringWriter string = new StringWriter();
                PrintWriter str = new PrintWriter(string);
                e.printStackTrace(str);
                result.setMsg(string.toString());
                return result;
        }
        
        return result;
    }

    @WebMethod(operationName = "getARangeOfIssueByAccountID")
    @Override
    public Result getARangeOfIssueByAccountID(int accountID, int index, int nombreMaxResultat) {
        Result result = new Result();
        try{
            
            result = dao.getARangeOfIssueByAccountID(accountID, index, nombreMaxResultat);
            
        }catch(Throwable e){
                StringWriter string = new StringWriter();
                PrintWriter str = new PrintWriter(string);
                e.printStackTrace(str);
                result.setMsg(string.toString());
                return result;
        }
        
        return result;
    }

    @WebMethod(operationName = "getIssueByID")
    @Override
    public Result getIssueByID(int accountID, int issueID) {
        Result result = new Result();
        try{
            
            result = dao.getIssueByID(accountID, issueID);
        
        }catch(Throwable e){
                StringWriter string = new StringWriter();
                PrintWriter str = new PrintWriter(string);
                e.printStackTrace(str);
                result.setMsg(string.toString());
                return result;
        }
        
        return result;
    }

    @WebMethod(operationName = "editIssue")
    @Override
    public String editIssue(int accountID, int issueID, String category, String subcategory, String description) {
        String res;
        ResultBackend result;
        try {

            result = dao.editIssue(accountID, issueID, category, subcategory, description);

            res = result.getMsg();
            
        }catch (IssueManagementException e) {
            res = e.getSmallMessage();
            return res;
        }

        return res;
    }

    @WebMethod(operationName = "deleteIssue")
    @Override
    public String deleteIssue(int accountID, int issueID){
        String res;
        try{
            
            res = dao.deleteIssue(accountID, issueID);
            
        }catch (IssueManagementException e) {
            res = e.getSmallMessage();
            return res;
        }
        
        return res;
    }
    
    @WebMethod(operationName = "resolveIssue")
    @Override
    public String resolveIssue(String usernameID, int issueID, String solution){
        String res;
        try{
            
            res = dao.resolveIssue(usernameID, issueID, solution);
            
        }catch (IssueManagementException e) {
            res = e.getSmallMessage();
            return res;
        }
        
        return res;
    }
    
    
}
