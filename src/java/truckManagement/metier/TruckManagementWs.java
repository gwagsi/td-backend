/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package truckManagement.metier;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;
import mailing.mailSending.MailSendingTrucks;
import org.apache.logging.log4j.LogManager;
import toolsAndTransversalFunctionnalities.Result;
import truckManagement.dao.ITruckManagementDaoLocal;
import util.exception.TruckBookingManagementException;
import util.exception.TruckManagementException;
import util.metier.function.TruckFunction;
import util.metier.gps.GeoLocation;

/**
 *
 * @author erman
 */
@WebService(serviceName = "TruckManagementWs")
@Stateless()
public class TruckManagementWs implements ITruckManagementWs{
    
    @EJB
    private ITruckManagementDaoLocal dao;
    
    private MailSendingTrucks mailSendingTrucks;

     final static org.apache.logging.log4j.Logger logger = LogManager.getLogger("dump1");
    
    @WebMethod(operationName = "addNewTruck")
    @Override
    public String addNewTruck(String picture, String matricule, double locationPrice, 
            int accountID, String year, String make, String model, int truckAxleID, 
            int lenghtOfBedID, int distance, String phoneNumber, String truckZipCode,
            String dOTNumber, int generalLiability, int insuranceLiability, String pictureInsurance, 
            String truckDescription, float latitude, float longitude){
       
        String res = "";
        Result result;
        try{
            
            if(picture == null || picture.equals("")){
                picture = ""+0;
            }
            
            if(pictureInsurance == null || pictureInsurance.equals("")){
                pictureInsurance = ""+0;
            }
            
            result = dao.addNewTruck(picture, matricule, locationPrice, accountID, year, make, model, truckAxleID, lenghtOfBedID, distance, phoneNumber, truckZipCode, dOTNumber, generalLiability, insuranceLiability, pictureInsurance, truckDescription, latitude, longitude);
            
            if(res.contains("good") && result.getObjectList() != null && !result.getObjectList().isEmpty()){
                //Fabrique du message a envoyer
                String message = "<br>Dear user,<br><br>"
                        + "We inform you that your area <i><b>"+truckZipCode+"</b></i> is now covered by our system";
                
                mailSendingTrucks = new MailSendingTrucks();
                mailSendingTrucks.sendMail(result.getObjectList(), message);
            }
            res = result.getMsg();
        }catch(Throwable e){
                StringWriter string = new StringWriter();
                PrintWriter str = new PrintWriter(string);
                e.printStackTrace(str);
                res = string.toString();
                logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"addNewTruck"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
                return res;
        }
                return res;
    }

    @WebMethod(operationName = "getARangeOfTruckByAccountID")
    @Override
    public Result getARangeOfTruckByAccountID(int accountID, int index, int nombreMaxResultat) {
        
        Result result = new Result();
        
        try{
            
            result = dao.getARangeOfTruckByAccountID(accountID, index, nombreMaxResultat);
            
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfTruckByAccountID"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
        return result;
        
    }

    @WebMethod(operationName = "getAllTruckByAccountID")
    @Override
    public Result getAllTruckByAccountID(int accountID) {
        Result result = new Result();
        
        try{
            
            result = dao.getAllTruckByAccountID(accountID);
            
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getAllTruckByAccountID"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
        return result;
    }

    @WebMethod(operationName = "getTruckDetail")
    @Override
    public Result getTruckDetail(int accountID, int truckID) {
        Result result = new Result();
        
        try{
            
            result = dao.getTruckDetail(accountID, truckID);
            
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getTruckDetail"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
        return result;
    }
    
    @WebMethod(operationName = "modifyTruckInfo")
    @Override
    public String modifyTruckInfo(int truckID, String picture, String matricule, double locationPrice, 
            int accountID, String year, String make, String model, int truckAxleID, 
            int lenghtOfBedID, int distance, String phoneNumber, String truckZipCode,
            String dOTNumber, int generalLiability, int insuranceLiability, String pictureInsurance, 
            String truckDescription, float latitude, float longitude){
       
        String res = "";
        Result result;
        try{
            
            result = dao.modifyTruckInfo(truckID, picture, matricule, locationPrice, accountID, year, make, model, truckAxleID, lenghtOfBedID, distance, phoneNumber, truckZipCode, dOTNumber, generalLiability, insuranceLiability, pictureInsurance, truckDescription, latitude, longitude);
            
            if(res.contains("good") && result.getObjectList() != null && !result.getObjectList().isEmpty()){
                //Fabrique du message a envoyer
                String message = "<br>Dear user,<br><br>"
                        + "We inform you that your area <i><b>"+truckZipCode+"</b></i> is now covered by our system";
                
                mailSendingTrucks = new MailSendingTrucks();
                mailSendingTrucks.sendMail(result.getObjectList(), message);
            }
            res = result.getMsg();
            
        }catch(Throwable e){
                StringWriter string = new StringWriter();
                PrintWriter str = new PrintWriter(string);
                e.printStackTrace(str);
                res = string.toString();
                     logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"modifyTruckInfo"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
                return res;
        }
                return res;
    }
    
    @WebMethod(operationName = "deleteTruck")
    @Override
    public String deleteTruck(int truckID){
        String res;
        try{
            
            res = dao.deleteTruck(truckID);
           }catch(TruckManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"deleteTruck"+"\n"
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
                            +"Fct   ws- : "+"deleteTruck"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
                return res;
        }
                return res;
    }

    @WebMethod(operationName = "getAllTruckAxleName")
    @Override
    public Result getAllTruckAxleName() {
        Result result = new Result();
        
        try{
            
            result = dao.getAllTruckAxleName();
            
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
               logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getAllTruckAxleName"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
        return result;
    }

    @WebMethod(operationName = "getTruckInfoByAccountID")
    @Override
    public Result getTruckInfoByAccountID(int accountID, int truckID) {
        Result result = new Result();
        
        try{
            
            result = dao.getTruckInfoByAccountID(accountID, truckID);
            
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getTruckInfoByAccountID"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
        return result;
    }
    
    @WebMethod(operationName = "getTruckNumberByTruckOwner")
    @Override
    public Result getTruckNumberByTruckOwner(int excavatorID) {
        Result result = new Result();
        
        try{
            
            result = dao.getTruckNumberByTruckOwner(excavatorID);
            
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
               logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getTruckNumberByTruckOwner"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
        return result;
    }
    
    @WebMethod(operationName = "countAllTrucks")
    @Override
    public Result countAllTrucks() {
       Result result = new Result();
       
       try{
            
            result = dao.countAllTrucks();
            
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"countAllTrucks"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
        return result;
       
    }
    
    @WebMethod(operationName = "addNewAreaNotCovered")
    @Override
    public String addNewAreaNotCovered(String email, String zipCode, String date){
        String res;
        try{
            
            res = dao.addNewAreaNotCovered(email, zipCode, date);
            }catch(TruckManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"addNewAreaNotCovered"+"\n"
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
                            +"Fct   ws- : "+"addNewAreaNotCovered"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
                return res;
        }
                return res;
    }
    
    @WebMethod(operationName = "getTruckByJobCharacteristics")
    @Override
    public Result getTruckByJobCharacteristics(int jobID){
        Result result = new Result();
       
       try{
            
            result = dao.getTruckByJobCharacteristics(jobID);
            
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getTruckByJobCharacteristics"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
        return result;
    }
    
    @WebMethod(operationName = "getUserTruckByJobCharacteristics")
    @Override
    public Result getUserTruckByJobCharacteristics(int accountID, int jobID){
        Result result = new Result();
       
       try{
            
            result = dao.getUserTruckByJobCharacteristics(accountID, jobID);
            
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getUserTruckByJobCharacteristics"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
        return result;
    }

    
    @WebMethod(operationName = "getDetailOfTruck")
    @Override
    public String getDetailOfTruck(int truckID) {
        String res;
        try{
            
            res = dao.getDetailOfTruck(truckID);
           }catch(TruckManagementException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"getDetailOfTruck"+"\n"
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
                            +"Fct   ws- : "+"getDetailOfTruck"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
                return res;
        }
                return res;
    }

    @WebMethod(operationName = "getListOfInsuranceLiability")
    @Override
    public Result getListOfInsuranceLiability() {
        Result result = new Result();
        try{
            
            result = dao.getListOfInsuranceLiability();
           
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
              logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getListOfInsuranceLiability"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
      return result;
    }
    
    @WebMethod(operationName = "getListOfGeneralLiability")
    @Override
    public Result getListOfGeneralLiability() {
        Result result = new Result();
        try{
            
            result = dao.getListOfGeneralLiability();
           
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
              logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getListOfGeneralLiability"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
      return result;
    }

    
    @WebMethod(operationName = "getARangeOfAllAvaillabilityTrucks")
    @Override
    public Result getARangeOfAllAvaillabilityTrucks(long lStartDate, long lEndDate, String truckZipCode, int distance_Within, 
            boolean excludeWeekend, int sortParam, int dotNumber, float latitude, float longitude, int index, 
            int nombreMaxResult, String attribut_session, int ORDER) {
         Result result = new Result();
         
        try{
            
            Date[] dates = TruckFunction.getDateForSearchTruck(lStartDate, lEndDate);
            Date startDate = dates[0];
            Date endDate = dates[1];
            
            if (distance_Within == 0 && latitude == 0 && longitude == 0) {
                result = dao.getARangeOfAllAvaillabilityTrucksWithoutDistance(startDate, endDate, truckZipCode, excludeWeekend, sortParam, dotNumber, index, nombreMaxResult, attribut_session, ORDER, 0);
            } else {
                
                truckZipCode = (truckZipCode != null ? truckZipCode : "");
                
                GeoLocation object = new GeoLocation();
                object.setBoundingCoordinates(latitude, longitude, distance_Within);
                float latMin = object.getMinLat();
                float latMax = object.getMaxLat();
                float lonMin = object.getMinLong();
                float lonMax = object.getMaxLong();
                
                result = dao.getARangeOfAllAvaillabilityTrucks(startDate, endDate, truckZipCode, distance_Within, excludeWeekend, sortParam, dotNumber,
                        latitude, longitude, latMin, latMax, lonMin, lonMax, index, nombreMaxResult, attribut_session, ORDER, 0);
                
            }
        }catch(Throwable e){
                StringWriter string = new StringWriter();
                PrintWriter str = new PrintWriter(string);
                e.printStackTrace(str);
                result.setMsg(string.toString());
                 logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfAllAvaillabilityTrucks"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
                return result;
        }
        
        return result;
    }
    
    
    @WebMethod(operationName = "getARangeOfAvailabilityTrucksFiltered")
    @Override
    public Result getARangeOfAvailabilityTrucksFiltered(long lStartDate, long lEndDate, String truckZipCode, int distance_Within,
            boolean excludeWeekend, int sortParam, int dotNumber, float latitude, float longitude, int truckAxleID, 
            int generalLiability, int insuranceLiability, int index, int nombreMaxResult, String attribut_session, int ORDER) {
         Result result = new Result();
         
        try{
            
            Date[] dates = TruckFunction.getDateForSearchTruck(lStartDate, lEndDate);
            Date startDate = dates[0];
            Date endDate = dates[1];
            if (distance_Within == 0 && latitude == 0 && longitude == 0) {
                result = dao.getARangeOfAvailabilityTrucksFilteredWithoutDistance(startDate, endDate, truckZipCode, excludeWeekend, sortParam, dotNumber, truckAxleID, generalLiability, insuranceLiability, index, nombreMaxResult, attribut_session, ORDER, 0);
            } else {
                
                truckZipCode = (truckZipCode != null ? truckZipCode : "");
                
                GeoLocation object = new GeoLocation();
                object.setBoundingCoordinates(latitude, longitude, distance_Within);
                float latMin = object.getMinLat();
                float latMax = object.getMaxLat();
                float lonMin = object.getMinLong();
                float lonMax = object.getMaxLong();
                
                result = dao.getARangeOfAvailabilityTrucksFiltered(startDate, endDate, truckZipCode, distance_Within, excludeWeekend,
                        sortParam, dotNumber, latitude, longitude, latMin, latMax, lonMin, lonMax, truckAxleID, generalLiability,
                        insuranceLiability, index, nombreMaxResult, attribut_session, ORDER, 0);
                
            }
        }catch(Throwable e){
                StringWriter string = new StringWriter();
                PrintWriter str = new PrintWriter(string);
                e.printStackTrace(str);
                result.setMsg(string.toString());
                logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfAvailabilityTrucksFiltered"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
                return result;
        }
        
        return result;
    }
    
    
    
    @WebMethod(operationName = "getARangeOfAllAvaillableTrucks")
    @Override
    public Result getARangeOfAllAvaillableTrucks(long lStartDate, long lEndDate, String truckZipCode, int distance_Within, 
            boolean excludeWeekend, int sortParam, int dotNumber, float latitude, float longitude, int index, 
            int nombreMaxResult, int ORDER) {
         Result result = new Result();
         
        try{
            
            Date[] dates = TruckFunction.getDateForSearchTruck(lStartDate, lEndDate);
            Date startDate = dates[0];
            Date endDate = dates[1];
            
            if (distance_Within == 0 && latitude == 0 && longitude == 0) {
                result = dao.getARangeOfAllAvaillabilityTrucksWithoutDistance(startDate, endDate, truckZipCode, excludeWeekend, sortParam, dotNumber, index, nombreMaxResult, "", ORDER, 1);
            } else {
                
                truckZipCode = (truckZipCode != null ? truckZipCode : "");
                
                GeoLocation object = new GeoLocation();
                object.setBoundingCoordinates(latitude, longitude, distance_Within);
                float latMin = object.getMinLat();
                float latMax = object.getMaxLat();
                float lonMin = object.getMinLong();
                float lonMax = object.getMaxLong();
                
                result = dao.getARangeOfAllAvaillabilityTrucks(startDate, endDate, truckZipCode, distance_Within, excludeWeekend, sortParam, dotNumber,
                        latitude, longitude, latMin, latMax, lonMin, lonMax, index, nombreMaxResult, "", ORDER, 1);
                
            }
        }catch(Throwable e){
                StringWriter string = new StringWriter();
                PrintWriter str = new PrintWriter(string);
                e.printStackTrace(str);
                result.setMsg(string.toString());
                 logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfAllAvaillableTrucks"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
                return result;
        }
        
        return result;
    }
    
    
    @WebMethod(operationName = "getARangeOfAvailableTrucksFiltered")
    @Override
    public Result getARangeOfAvailableTrucksFiltered(long lStartDate, long lEndDate, String truckZipCode, int distance_Within,
            boolean excludeWeekend, int sortParam, int dotNumber, float latitude, float longitude, int truckAxleID, 
            int generalLiability, int insuranceLiability, int index, int nombreMaxResult, int ORDER) {
         Result result = new Result();
         
        try{
            
            Date[] dates = TruckFunction.getDateForSearchTruck(lStartDate, lEndDate);
            Date startDate = dates[0];
            Date endDate = dates[1];
            if (distance_Within == 0 && latitude == 0 && longitude == 0) {
                result = dao.getARangeOfAvailabilityTrucksFilteredWithoutDistance(startDate, endDate, truckZipCode, excludeWeekend, sortParam, dotNumber, truckAxleID, generalLiability, insuranceLiability, index, nombreMaxResult, "", ORDER, 1);
            } else {
                
                truckZipCode = (truckZipCode != null ? truckZipCode : "");
                
                GeoLocation object = new GeoLocation();
                object.setBoundingCoordinates(latitude, longitude, distance_Within);
                float latMin = object.getMinLat();
                float latMax = object.getMaxLat();
                float lonMin = object.getMinLong();
                float lonMax = object.getMaxLong();
                
                result = dao.getARangeOfAvailabilityTrucksFiltered(startDate, endDate, truckZipCode, distance_Within, excludeWeekend,
                        sortParam, dotNumber, latitude, longitude, latMin, latMax, lonMin, lonMax, truckAxleID, generalLiability,
                        insuranceLiability, index, nombreMaxResult, "", ORDER, 1);
                
            }
        }catch(Throwable e){
                StringWriter string = new StringWriter();
                PrintWriter str = new PrintWriter(string);
                e.printStackTrace(str);
                result.setMsg(string.toString());
                 logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfAvailableTrucksFiltered"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
                return result;
        }
        
        return result;
    }
    
    
    @WebMethod(operationName = "getUnavailabilityDatesByTruckID")
    @Override
    public Result getUnavailabilityDatesByTruckID(int truckID) {

        Result result = new Result();
        try {

            result = dao.getUnavailabilityDatesByTruckID(truckID);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getUnavailabilityDatesByTruckID"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }

        return result;
    }

    @WebMethod(operationName = "getTruckDetailFrontEnd")
    @Override
    public Result getTruckDetailFrontEnd(int accountID, int truckID, int index, int nombreMaxResult) {
        Result result = new Result();
        
        try{
            
            result = dao.getTruckDetailFrontEnd(accountID, truckID, index, nombreMaxResult);
            
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getTruckDetailFrontEnd"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
        return result;
    }

    
    
}
