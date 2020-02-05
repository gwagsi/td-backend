/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package admin.accountManagement.metier;

import admin.accountManagement.dao.IAdminAccountManagementDaoLocal;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import toolsAndTransversalFunctionnalities.Result;
import util.exception.AdminAccountManagementException;

/**
 *
 * @author erman
 */
@WebService(serviceName = "AdminAccountManagementWs")
@Stateless()
public class AdminAccountManagementWs implements IAdminAccountManagementWs{

    @EJB
    private IAdminAccountManagementDaoLocal dao;
    
    final static Logger logger = LogManager.getLogger("dump1");
    @WebMethod(operationName = "applyActionToUser")
    @Override
    public String applyActionToUser(int accountID, String admin, int actionID, String message){
        
        String res;
        try{
            
            res = dao.applyActionToUser(accountID, admin, actionID, message);
            
        }
        catch(AdminAccountManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"applyActionToUser"+"\n"
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
                  logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"applyActionToUser"+"\n"
                            +"Erreur   : "+"\n"
                            +res);
                return res;
        }
        
        return res;
    }
    
    @WebMethod(operationName = "getARangeOfCategorieUser")
    @Override
    public Result getARangeOfCategorieUser(String categorie, boolean categorieValue, int typeOfUser, int index, int nombreMaxResult) {
        
        Result resultat = new Result();
        String res;
        
         try{
             
             resultat = dao.getARangeOfCategorieUser(categorie, categorieValue, typeOfUser, index, nombreMaxResult);
            
         }catch(Throwable e){
             StringWriter string = new StringWriter();
             PrintWriter str = new PrintWriter(string);
             e.printStackTrace(str);
             res = string.toString();
             resultat.setMsg(res);
              logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfCategorieUser"+"\n"
                            +"Erreur   : "+"\n"
                            +res);
             return resultat;
         }
         
         return resultat;
    }
    
    @WebMethod(operationName = "getUsersStatisticsInDateRange")
    @Override
    public String getUsersStatisticsInDateRange(long statDate, long endDate){
        
        String res;
        try{
            
            res = dao.getUsersStatisticsInDateRange(statDate, endDate);
          }
        catch(AdminAccountManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"getUsersStatisticsInDateRange"+"\n"
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
                 logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+" getUsersStatisticsInDateRange"+"\n"
                            +"Erreur   : "+"\n"
                            +res);
                return res;
        }
        
        return res;
    }
    
    @WebMethod(operationName = "getCurrentUsersStatistics")
    @Override
    public String getCurrentUsersStatistics(String categorie, boolean categorieValue){
        
        String res;
        try{
            
            res = dao.getCurrentUsersStatistics(categorie, categorieValue);
           }
          catch(AdminAccountManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"getCurrentUsersStatistics"+"\n"
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
                 logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+" getCurrentUsersStatistics"+"\n"
                            +"Erreur   : "+"\n"
                            +res);
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
                logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+" getARangeOfUserInDateRange"+"\n"
                            +"Erreur   : "+"\n"
                            +string);
                return result;
        }
        
        return result;
    }
}
