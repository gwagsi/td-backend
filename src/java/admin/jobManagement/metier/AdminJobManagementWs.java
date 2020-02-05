/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package admin.jobManagement.metier;

import admin.jobManagement.dao.IAdminJobManagementDaoLocal;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import toolsAndTransversalFunctionnalities.Result;
import util.exception.AdminJobManagementException;

/**
 *
 * @author erman
 */
@WebService(serviceName = "AdminJobManagementWs")
@Stateless()
public class AdminJobManagementWs implements IAdminJobManagementWs{

    @EJB
    private IAdminJobManagementDaoLocal dao;
    
    final static Logger logger = LogManager.getLogger("dump1");
    /*
    @WebMethod(operationName = "applyActionToUser")
    @Override
    public String applyActionToUser(int accountID, String admin, int actionID, String message){
        
        String res;
        try{
            
            res = dao.applyActionToUser(accountID, admin, actionID, message);
            
        }catch(Throwable e){
                StringWriter string = new StringWriter();
                PrintWriter str = new PrintWriter(string);
                e.printStackTrace(str);
                res = string.toString();
                return res;
        }
        
        return res;
    }
    */
    @WebMethod(operationName = "getARangeOfCategorieJobs")
    @Override
    public Result getARangeOfCategorieJobs(String categorie, boolean categorieValue, int index, int nombreMaxResult) {

        Result resultat = new Result();
        String res;

        try {

            resultat = dao.getARangeOfCategorieJobs(categorie, categorieValue, index, nombreMaxResult);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
            resultat.setMsg(res);
            return resultat;
        }

        return resultat;
    }

    @WebMethod(operationName = "getJobsStatisticsInDateRange")
    @Override
    public String getJobsStatisticsInDateRange(long statDate, long endDate){
        
        String res;
        try{
            
            res = dao.getJobsStatisticsInDateRange(statDate, endDate);
            }
        catch(AdminJobManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"getJobsStatisticsInDateRange"+"\n"
                            +"Class dao : "+error.getClasse()+"\n"
                            +"Fct   dao : "+error.getFonction()+"\n"
                            +"Info error "+error.getSmallMessage()+"\n"
                            +"Erreur   : "+"\n"
                            +error.getFullMessage());
           
        }catch(Throwable e){
                StringWriter string = new StringWriter();
                PrintWriter str = new PrintWriter(string);
                e.printStackTrace(str);
                res = string.toString();
                return res;
        }
        
        return res;
    }
    /*
    @WebMethod(operationName = "getCurrentJobsStatistics")
    @Override
    public String getCurrentJobsStatistics(String categorie, boolean categorieValue){
        
        String res;
        try{
            
            res = dao.getCurrentJobsStatistics(categorie, categorieValue);
            
        }catch(Throwable e){
                StringWriter string = new StringWriter();
                PrintWriter str = new PrintWriter(string);
                e.printStackTrace(str);
                res = string.toString();
                return res;
        }
        
        return res;
    }
    
    @WebMethod(operationName = "getARangeOfUserInDateRange")
    @Override
    public Result getARangeOfUserInDateRange(long statDate, long endDate, boolean isPaid, int index, int nombreMaxResult) {
        
        Result result;
        try{
            
            result = dao.getARangeOfUserInDateRange(statDate, endDate, isPaid, index, nombreMaxResult);
            
        }catch(Throwable e){
                StringWriter string = new StringWriter();
                PrintWriter str = new PrintWriter(string);
                e.printStackTrace(str);
                result =  new Result();
                result.setMsg(string.toString());
                return result;
        }
        
        return result;
    }
    */
}
