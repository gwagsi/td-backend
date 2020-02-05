/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package availabilityManagement.metier;

import availabilityManagement.dao.IAvaibilityManagementDaoLocal;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import toolsAndTransversalFunctionnalities.Result;
import util.date.DateFunction;
import util.exception.AvaibilityManagementException;

/**
 *
 * @author erman
 */
@WebService(serviceName = "AvaibilityManagementWs")
@Stateless()
public class AvaibilityManagementWs implements IAvaibilityManagementWs{

    @EJB
    IAvaibilityManagementDaoLocal dao;
    
     final static Logger logger = LogManager.getLogger("dump1");
    @WebMethod(operationName = "addAvailability")
    @Override
    public String addAvailability(List<Long> unavailable_dates, int truckID, int truckOwnerID){

        String res;
        try{
            
            res = dao.addAvailability(unavailable_dates, truckID, truckOwnerID);
           }
        catch(AvaibilityManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"addAvailability"+"\n"
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
                            +"Fct   ws- : "+"addAvailability"+"\n"
                            +"Erreur   : "+"\n"
                            +res);
                return res;
        }
        
        return res;
    }

    @WebMethod(operationName = "editAvailability")
    @Override
    public String editAvailability(int availabilityID, String day, String timeRange, int accountID, int truckID){

        String res;
        try{
            
            res = dao.editAvailability(availabilityID, day, timeRange, accountID, truckID);
       }
        catch(AvaibilityManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"editAvailability"+"\n"
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
                            +"Fct   ws- : "+"editAvailability"+"\n"
                            +"Erreur   : "+"\n"
                            +res);
                return res;
        }
        
        return res;
    }

    @WebMethod(operationName = "deleteAvailability")
    @Override
    public String deleteAvailability(int availabilityID) {

        String res;
        try{
            
            res = dao.deleteAvailability(availabilityID);
         }
        catch(AvaibilityManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"deleteAvailability"+"\n"
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
                            +"Fct   ws- : "+"deleteAvailability"+"\n"
                            +"Erreur   : "+"\n"
                            +res);
                return res;
        }
        
        return res;
    }

    @WebMethod(operationName = "deleteAvailabilityByYear")
    @Override
    public String deleteAvailabilityByYear(int truckID, String year) {

        String res;
        try{
            
            res = dao.deleteAvailability(truckID, year);
        }
        catch(AvaibilityManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"deleteAvailabilityByYear"+"\n"
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
                            +"Fct   ws- : "+"deleteAvailabilityByYear"+"\n"
                            +"Erreur   : "+"\n"
                            +res);
                return res;
        }
        
        return res;
    }

    @WebMethod(operationName = "getAvailabilityByID")
    @Override
    public Result getAvailabilityByID(int availabilityByID) {
        
        Result result = new Result();
        try{
            
            result = dao.getAvailabilityByID(availabilityByID);
            
        }catch(Throwable e){
                StringWriter string = new StringWriter();
                PrintWriter str = new PrintWriter(string);
                e.printStackTrace(str);
                result.setMsg(string.toString());
                logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getAvailabilityByID"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
                return result;
        }
        
        return result;
    }

    @WebMethod(operationName = "getARangeOfAvailabilityByTruckID")
    @Override
    public Result getARangeOfAvailabilityByTruckID(int truckID, int index, int nombreMaxResultat) {

        Result result = new Result();
        try {

            result = dao.getARangeOfAvailabilityByTruckID(truckID, index, nombreMaxResultat);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfAvailabilityByTruckID"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }

    @WebMethod(operationName = "getARangeOfAvailabilityByTruckOwnerID")
    @Override
    public Result getARangeOfAvailabilityByTruckOwnerID(int truckOwnerID, String year, int truckID) {
        Result result = new Result();
       // System.out.println("Start :"+ DateFunction.getGMTDate());
        try{
            
            result = dao.getARangeOfAvailabilityByTruckOwnerID(truckOwnerID, year, truckID);
            
        }catch(Throwable e){
                StringWriter string = new StringWriter();
                PrintWriter str = new PrintWriter(string);
                e.printStackTrace(str);
                result.setMsg(string.toString());
                logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfAvailabilityByTruckOwnerID"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
                return result;
        }
        
        return result;
    }

    @WebMethod(operationName = "verifyAddress")
    @Override
    public String verifyAddress(String address) {
        String res;
        try{
            
            res = dao.verifyAddress(address);
         }
        catch(AvaibilityManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"verifyAddress"+"\n"
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
                            +"Fct   ws- : "+"verifyAddress"+"\n"
                            +"Erreur   : "+"\n"
                            +res);
                return res;
        }
        
        return res;
    }
    
    @WebMethod(operationName = "getAllUnAvailabilityTruckDateByYear")
    @Override
    public Result getAllUnAvailabilityTruckDateByYear(int truckID, int year) {

        Result result = new Result();
        try {

            result = dao.getAllUnAvailabilityTruckDateByYear(truckID, year);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"verifyAddress"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }
    
    
}
