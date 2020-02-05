/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package truckBookingManagement.metier;

import truckBookingManagement.dao.ITruckBookingManagementDaoLocal;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.ejb.Stateless;
import mailing.mailSending.MailToContactTruckOwner;
import org.apache.logging.log4j.LogManager;
import toolsAndTransversalFunctionnalities.Result;
import util.exception.TruckBookingManagementException;

/**
 *
 * @author erman
 */
@WebService(serviceName = "TruckBookingManagementWs")
@Stateless()
public class TruckBookingManagementWs implements ITruckBookingManagementWs{

    
    @EJB
    private ITruckBookingManagementDaoLocal dao;
     final static org.apache.logging.log4j.Logger logger = LogManager.getLogger("dump1");
    
    @WebMethod(operationName = "bookNewTruck")
    @Override
    public String bookNewTruck(String bookingDate, int truckID, int basketID, int clientID) {
        
        String res;
        try{
            
            res = dao.bookNewTruck(bookingDate, truckID, basketID, clientID);
          }catch(TruckBookingManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"bookNewTruck"+"\n"
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
                            +"Fct   ws- : "+"bookNewTruck"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
                return res;
        }
        
        return res;
    }
    
    @WebMethod(operationName = "editBookingDate")
    @Override
    public String editBookingDate(int truckBookingID, String bookingDate, int truckID, int basketID, int clientID) {
        
        String res;
        try{
            
            res = dao.editBookingDate(truckBookingID, bookingDate, truckID, basketID, clientID);
           }catch(TruckBookingManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"editBookingDate"+"\n"
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
                            +"Fct   ws- : "+"editBookingDate"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
                return res;
        }
        
        return res;
    }

    @WebMethod(operationName = "getARangeOfBookingTruck")
    @Override
    public Result getARangeOfBookingTruck(int accountID, int index, int nombreMaxResultat) {
        Result result = new Result();
        
        try{
            
            result = dao.getARangeOfBookingTruck(accountID, index, nombreMaxResultat);
            
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfBookingTruck"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
        return result;
    }

    @WebMethod(operationName = "getARangeOfNotBookingTruck")
    @Override
    public Result getARangeOfNotBookingTruck(int accountID, int index, int nombreMaxResultat) {
        Result result = new Result();
        
        try{
            
            result = dao.getARangeOfBookingTruck(accountID, index, nombreMaxResultat);
            
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfNotBookingTruck"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
        return result;
    }

    @WebMethod(operationName = "removeTruckBooking")
    @Override
    public String removeTruckBooking(int truckBookingID) {
        
        String res;
        try{
            
            res = dao.removeTruckBooking(truckBookingID);
          }catch(TruckBookingManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"removeTruckBooking"+"\n"
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
                            +"Fct   ws- : "+"removeTruckBooking"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
                return res;
        }
        
        return res;
    }
    

    @WebMethod(operationName = "deletedBasket")
    @Override
    public String deletedBasket(int basketID) {
        
        String res;
        try{
            
            res = dao.deletedBasket(basketID);
             }catch(TruckBookingManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"deletedBasket"+"\n"
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
                            +"Fct   ws- : "+"deletedBasket"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
                return res;
        }
        
        return res;
    }

    @WebMethod(operationName = "confimedBasket")
    @Override
    public String confimedBasket(int basketID) {
        
        String res;
        try{
            
            res = dao.confimedBasket(basketID);
           }catch(TruckBookingManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"confimedBasket"+"\n"
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
                            +"Fct   ws- : "+"confimedBasket"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
                return res;
        }
        
        return res;
    }

    @WebMethod(operationName = "completBasket")
    @Override
    public String completBasket(int basketID) {
        
        String res;
        try{
            
            res = dao.completBasket(basketID);
           }catch(TruckBookingManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"completBasket"+"\n"
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
                            +"Fct   ws- : "+"completBasket"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
                return res;
        }
        
        return res;
    }

    @WebMethod(operationName = "getARangeOfBookingInfoByTruckID")
    @Override
    public Result getARangeOfBookingInfoByTruckID(int truckID, int index, int nombreMaxResultat) {
        Result result = new Result();
        
        try{
            
            result = dao.getARangeOfBookingInfoByTruckID(truckID, index, nombreMaxResultat);
            
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
                 logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfBookingInfoByTruckID"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
        return result;
    }
    

    @WebMethod(operationName = "getARangeOfValidatedBookingByTruckID")
    @Override
    public Result getARangeOfValidatedBookingByTruckID(int truckID, int index, int nombreMaxResultat) {
        Result result = new Result();
        
        try{
            
            result = dao.getARangeOfValidatedBookingByTruckID(truckID, index, nombreMaxResultat);
            
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
                 logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfValidatedBookingByTruckID"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
        return result;
    }
    
    
    @WebMethod(operationName = "getUnavailabilityDatesByTruckID")
    @Override
    public Result getUnavailabilityDatesByTruckID(int truckID) {
        Result result = new Result();
        try{
            
            result = dao.getUnavailabilityDatesByTruckID(truckID);
            
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
                 logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getUnavailabilityDatesByTruckID"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
        
        return result;
    }

    @WebMethod(operationName = "confirmTruckBooking")
    @Override
    public String confirmTruckBooking(int truckBookingID) {
        
        String res;
        try{
            
            res = dao.confirmTruckBooking(truckBookingID);
             }catch(TruckBookingManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"confirmTruckBooking"+"\n"
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
                            +"Fct   ws- : "+"confirmTruckBooking"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
                return res;
        }
        
        return res;
    }

    @WebMethod(operationName = "rejectBookingForTruck")
    @Override
    public String rejectBookingForTruck(int truckBookingID) {
        
        String res;
        try{
            
            res = dao.rejectBookingForTruck(truckBookingID);
               }catch(TruckBookingManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"rejectBookingForTruck"+"\n"
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
                            +"Fct   ws- : "+"rejectBookingForTruck"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
                return res;
        }
        
        return res;
    }

    @WebMethod(operationName = "getARangeOfValidatTruckBook")
    @Override
    public Result getARangeOfValidatTruckBook(int accountID, int index, int nombreMaxResultat) {
        Result result = new Result();
        
        try{
            
            result = dao.getARangeOfValidatTruckBook(accountID, index, nombreMaxResultat);
            
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
                 logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfValidatTruckBook"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
        return result;
    }

    @WebMethod(operationName = "getARangeOfBasketByAccountID")
    @Override
    public Result getARangeOfBasketByAccountID(int accountID, int index, int nombreMaxResultat) {
        Result result = new Result();
        
        try{
            
            result = dao.getARangeOfBasketByAccountID(accountID, index, nombreMaxResultat);
            
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
                 logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfBasketByAccountID"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
        return result;
    }

    @WebMethod(operationName = "getARangeOfValidBasketByAccountID")
    @Override
    public Result getARangeOfValidBasketByAccountID(int accountID, int index, int nombreMaxResultat) {
         Result result = new Result();
        
        try{
            
            result = dao.getARangeOfValidBasketByAccountID(accountID, index, nombreMaxResultat);
            
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
                 logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfValidBasketByAccountID"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
        return result;
    }

    @WebMethod(operationName = "getARangeOfUnValidBasketByAccountID")
    @Override
    public Result getARangeOfUnValidBasketByAccountID(int accountID, int index, int nombreMaxResultat) {
         Result result = new Result();
        
        try{
            
            result = dao.getARangeOfUnValidBasketByAccountID(accountID, index, nombreMaxResultat);
            
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
                 logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfUnValidBasketByAccountID"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
        return result;
    }

    @WebMethod(operationName = "getARangeOfConfirmTruckByBasketID")
    @Override
    public Result getARangeOfConfirmTruckByBasketID(int accountID, int basketID, int index, int nombreMaxResultat) {
         
        Result result = new Result();
        
        try{
            
            result = dao.getARangeOfConfirmTruckByBasketID(accountID, basketID, index, nombreMaxResultat);
            
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
                 logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfConfirmTruckByBasketID"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
        return result;
    }

    @WebMethod(operationName = "getARangeOfConfirmTruckByBasketIDFrontEnd")
    @Override
    public Result getARangeOfConfirmTruckByBasketIDFrontEnd(int accountID, int basketID, int index, int nombreMaxResultat) {
         
        Result result = new Result();
        
        try{
            
            result = dao.getARangeOfConfirmTruckByBasketIDFrontEnd(accountID, basketID, index, nombreMaxResultat);
            
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
                 logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfConfirmTruckByBasketIDFrontEnd"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
        return result;
    }

    @WebMethod(operationName = "getARangeOfNotConfirmTruckByBasketID")
    @Override
    public Result getARangeOfNotConfirmTruckByBasketID(int accountID, int basketID, int index, int nombreMaxResultat) {
         
        Result result = new Result();
        
        try{
            
            result = dao.getARangeOfConfirmTruckByBasketID(accountID, basketID, index, nombreMaxResultat);
            
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
                 logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfNotConfirmTruckByBasketID"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
        return result;
    }
    

    @WebMethod(operationName = "getARangeOfTruckByBasketID")
    @Override
    public Result getARangeOfTruckByBasketID(int accountID, int basketID, int index, int nombreMaxResultat) {
         
        Result result = new Result();
        
        try{
            
            result = dao.getARangeOfTruckByBasketID(accountID, basketID, index, nombreMaxResultat);
            
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
                 logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfTruckByBasketID"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
        return result;
    }
    

    @WebMethod(operationName = "getARangeOfTruckByBasketIDFrontEnd")
    @Override
    public Result getARangeOfTruckByBasketIDFrontEnd(int accountID, int basketID, int index, int nombreMaxResultat) {
         
        Result result = new Result();
        
        try{
            
            result = dao.getARangeOfTruckByBasketIDFrontEnd(accountID, basketID, index, nombreMaxResultat);
            
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
                 logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfTruckByBasketIDFrontEnd"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
        return result;
    }

    @WebMethod(operationName = "getBookingDateByID")
    @Override
    public Result getBookingDateByID(int truckBookingID) {
         
        Result result = new Result();
        
        try{
            
            result = dao.getBookingDateByID(truckBookingID);
            
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
                 logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getBookingDateByID"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
        return result;
    }
    
    @WebMethod(operationName = "editSubmitReview")
    @Override
    public String editSubmitReview(int truckBookingID, int submitReview) {
        
        String res;
        try{
            
            res = dao.editSubmitReview(truckBookingID, submitReview);
             }catch(TruckBookingManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"editSubmitReview"+"\n"
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
                            +"Fct   ws- : "+"editSubmitReview"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
                return res;
        }
        
        return res;
    }

    
    @WebMethod(operationName = "editReviewComment")
    @Override
    public String editReviewComment(int truckBookingID, String reveiwComment) {
        String res;
        try {

            res = dao.editReviewComment(truckBookingID, reveiwComment);
                 }catch(TruckBookingManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"editReviewComment"+"\n"
                            +"Class dao : "+error.getClasse()+"\n"
                            +"Fct   dao : "+error.getFonction()+"\n"
                            +"Info error "+error.getSmallMessage()+"\n"
                            +"Erreur   : "+"\n"
                            +error.getFullMessage());   
        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
                 logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"editReviewComment"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }

        return res;
    }
    
    
    @WebMethod(operationName = "getARangeOfTruckOwnerReviewComment")
    @Override
    public Result getARangeOfTruckOwnerReviewComment(int truckID, int index, int nombreMaxResult) {

        Result result = new Result();
        
        try {

            result = dao.getARangeOfTruckOwnerReviewComment(truckID, index, nombreMaxResult);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
                 logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfTruckOwnerReviewComment"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }
    
    
    @WebMethod(operationName = "getARangeOfTruckBook")
    @Override
    public Result getARangeOfTruckBook(int accountID, int index, int nombreMaxResultat) {
        Result result = new Result();
        
        try{
            
            result = dao.getARangeOfTruckBook(accountID, index, nombreMaxResultat);
            
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
                 logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfTruckBook"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
        return result;
    }

    
    @WebMethod(operationName = "editBookingPrice")
    @Override
    public String editBookingPrice(int truckBookingID, float bookingPrice) {
        
        String res;
        try{
            
            res = dao.editBookingPrice(truckBookingID, bookingPrice);
                  }catch(TruckBookingManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"editBookingPrice"+"\n"
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
                            +"Fct   ws- : "+"editBookingPrice"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
                return res;
        }
        
        return res;
    }
    
    @WebMethod(operationName = "contactTruckOwner")
    @Override
    public String contactTruckOwner(String message, int truckID, String subject, String name, String email){
        MailToContactTruckOwner ctcTruckOwnerMsg = new MailToContactTruckOwner();
        String sendMsgResult = null;
        String res ;
        try{
            res = dao.contactTruckOwner(message, truckID, subject, name, email);
              }catch(TruckBookingManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"contactTruckOwner"+"\n"
                            +"Class dao : "+error.getClasse()+"\n"
                            +"Fct   dao : "+error.getFonction()+"\n"
                            +"Info error "+error.getSmallMessage()+"\n"
                            +"Erreur   : "+"\n"
                            +error.getFullMessage());   }
        String[] data =  res.split(";");
        //System.out.println("result="+res);
        
        if(data[0].equals("good")){
            String msg = "<br>"+
                    "<b>"+name+ "</b>  (<I>"+email+"</I>)  send you this message to ask you some question about your truck number  <b><I>"+
                    data[4].split("#")[0]+"</I></b> <br><br>"
                    +"Here is his message:<br></I>" + message + "</I>";
            sendMsgResult = ctcTruckOwnerMsg.sendMailToContactTruckOwner(data[1], data[2], data[3], msg, "Question from a user: "+subject, false,  "");
            
            System.out.println("Status du message="+sendMsgResult);
            
            if(sendMsgResult.equals("envoyé")){
                sendMsgResult = "good";
            }
            
        }
        else{
            sendMsgResult = "bad";
        }
        
        
        return sendMsgResult;
    }
    
    @WebMethod(operationName = "contactTruckOwnerNEW")
    @Override
    public String contactTruckOwnerNEW(String message, int truckID, String subject, String name, String email, boolean isCopy){
        
        System.out.println("contactTruckOwnerNEW: truckID: " + truckID + ",  name: " + name + ",  email: " + email + ",  isCopy: " + isCopy);
        
        MailToContactTruckOwner ctcTruckOwnerMsg = new MailToContactTruckOwner();
        String sendMsgResult = null;
        String res;
         try{
                res= dao.contactTruckOwner(message, truckID, subject, name, email);
         }catch(TruckBookingManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"contactTruckOwnerNEW"+"\n"
                            +"Class dao : "+error.getClasse()+"\n"
                            +"Fct   dao : "+error.getFonction()+"\n"
                            +"Info error "+error.getSmallMessage()+"\n"
                            +"Erreur   : "+"\n"
                            +error.getFullMessage());   }
        String[] data =  res.split(";");
        //System.out.println("result="+res);
        
        if(data[0].equals("good")){
            String msg = "<br>"+
                    "<b>"+name+ "</b>  (<I>"+email+"</I>)  send you this message to ask you some question about your truck number  <b><I>"+
                    data[4].split("#")[0]+"</I></b> <br><br>"
                    +"Here is his message:<br></I>" + message + "</I>";
            sendMsgResult = ctcTruckOwnerMsg.sendMailToContactTruckOwner(data[1], data[2], data[3], msg, "Question from a user: "+subject, isCopy, email);
            
            System.out.println("Status du message="+sendMsgResult);
            
            if(sendMsgResult.equals("envoyé")){
                sendMsgResult = "good";
            }
            
        }
        else{
            sendMsgResult = "bad";
        }
        
        
        return sendMsgResult;
    }

    
    @WebMethod(operationName = "payTruckBookingByClient")
    @Override
    public String payTruckBookingByClient(int clientID, int truckBookingID) {
        
        String res;
        try{
            
            res = dao.payTruckBookingByClient(clientID, truckBookingID);
             }catch(TruckBookingManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"payTruckBookingByClient"+"\n"
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
                            +"Fct   ws- : "+"payTruckBookingByClient"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
                return res;
        }
        
        return res;
    }
    
    
    @WebMethod(operationName = "payTruckBookingByTruckOwner")
    @Override
    public String payTruckBookingByTruckOwner(int truckOwnerID, int truckBookingID) {
        
        String res;
        try{
            
            res = dao.payTruckBookingByTruckOwner(truckOwnerID, truckBookingID);
              }catch(TruckBookingManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"payTruckBookingByTruckOwner"+"\n"
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
                            +"Fct   ws- : "+"payTruckBookingByTruckOwner"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
                return res;
        }
        
        return res;
    }

    
    @WebMethod(operationName = "getARangeOfTruckBookHistory")
    @Override
    public Result getARangeOfTruckBookHistory(int accountID, int index, int nombreMaxResultat){
        Result result = new Result();
        
        try{
            
            result = dao.getARangeOfTruckBookHistory(accountID, index, nombreMaxResultat);
            
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
                 logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfTruckBookHistory"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
        return result;
    }

    
    @WebMethod(operationName = "getARangeOfTruckReviewComment")
    @Override
    public Result getARangeOfTruckReviewComment(int truckID, int index, int nombreMaxResult) {

        Result result = new Result();
        
        try {

            result = dao.getARangeOfTruckReviewComment(truckID, index, nombreMaxResult);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfTruckReviewComment"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }
    
    
}
