/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package truckBookingManagement.dao;

import entities.Account;
import entities.BillingReceiver;
import entities.Driver;
import entities.DriverDocument;
import entities.Truck;
import entities.TruckBooking;
import entities.TruckDocument;
import entities.User;
import entities.UserBasket;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import paysimple.MypaySimpleObj;
import paysimple.PaySimpleAP;
import toolsAndTransversalFunctionnalities.CredentialInformation;
import toolsAndTransversalFunctionnalities.GestionnaireEntite;
import toolsAndTransversalFunctionnalities.Result;
import util.date.DateFunction;
import util.exception.TruckBookingManagementException;
import util.metier.function.DriverFunction;

/**
 *
 * @author erman
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class TruckBookingManagementDao implements ITruckBookingManagementDaoLocal, ITruckBookingManagementDaoRemote{
    
     
    @EJB
    GestionnaireEntite ges;
     final static org.apache.logging.log4j.Logger logger = LogManager.getLogger("dump1");
    
    TruckBookingFunction tbf;
    
    @Override
    public String bookNewTruck(String bookingDate, int truckID, int basketID, int clientID) throws TruckBookingManagementException{
        String res = "";
        
        TruckBooking truckBooking;
        Truck truck;
        Account account;
        UserBasket basket;
        
        try{
            
            ges.creatEntityManager();
            
            basket = (UserBasket)ges.getEntityManager().find(UserBasket.class, basketID);
            
            truck = (Truck)ges.getEntityManager().find(Truck.class, truckID);
            
            account = (Account)ges.getEntityManager().find(Account.class, clientID);
            
            
            if(truck == null){
                ges.closeEm();
                return "NotExistingTruck\n";
            }
            
            User truckOwner = truck.getUserID();
            User client = account.getUser();
            
            if(basket == null){
                basket = new UserBasket();
                basket.setBasketLabel((client.getBasketNumber() + 1)+"/"+(DateFunction.getGMTDate())+"/"+client.getName());
                basket.setConfirmed(0);
                basket.setComplet(false);
                basket.setCreationDate(DateFunction.getGMTDate());
                basket.setNumberOfTruck(1);
                basket.setDeleted(false);
                
                client.setBasketNumber((client.getBasketNumber() ));
                ges.getEntityManager().persist(basket);
                ges.getEntityManager().merge(truckOwner);
                ges.closeEm();
                
            } else {
                basket.setNumberOfTruck(basket.getNumberOfTruck()+1);
                ges.getEntityManager().merge(basket);
                ges.closeEm();
            }
            truckBooking = new TruckBooking();
            
            truckBooking.setBookingDate(bookingDate);
            truckBooking.setCreationDate(DateFunction.getGMTDate());
            truckBooking.setBasketID(basket);
            truckBooking.setClientValidation(0);
            truckBooking.setDeleted(false);
            truckBooking.setValidated(false);
            truckBooking.setRejected(false);
            truckBooking.setTruckOwnerValidation(0);
            truckBooking.setTruckID(truck);
            truckBooking.setTruckownerID(truck.getUserID());
            truckBooking.setClientID(account.getUser());
            truckBooking.setBookingPrice(0);
            
            ges.getEntityManager().persist(truckBooking);
            ges.closeEm();
            
            res = "good;"+basket.getBasketID()+";"+truckBooking.getTruckbookingID()+";"+basket.getBasketLabel();
            
        }catch(Throwable th){
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
             throw new TruckBookingManagementException(getClass()+"","bookNewTruck",1,res,res);
        
            //return res;
        }
        return res;
    }
    
    
    @Override
    public String editBookingDate(int truckBookingID, String bookingDate, int truckID, int basketID, int clientID) throws TruckBookingManagementException{
        String res = "";
        
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"Param Server: truckBookingID = "+truckBookingID +" ---  bookingDate = "+ bookingDate +""
                + "--- truckID = " + truckID +" ---  basketID = "+ basketID  +" ---  clientID = "+ clientID );
        
        TruckBooking truckBooking;
        Truck truck;
        Account account;
        UserBasket basket;
        
        try{
            
            ges.creatEntityManager();
            
            basket = (UserBasket)ges.getEntityManager().find(UserBasket.class, basketID);
            
            truck = (Truck)ges.getEntityManager().find(Truck.class, truckID);
            
            truckBooking = (TruckBooking)ges.getEntityManager().find(TruckBooking.class, truckBookingID);
            
            account = (Account)ges.getEntityManager().find(Account.class, clientID);
            
            
            if(truckBooking == null){
                ges.closeEm();
                return "NotExistingTruckBooking\n";
            }
            
            if(truck == null){
                ges.closeEm();
                return "NotExistingTruck\n";
            }
            
            if(basket == null){
                ges.closeEm();
                return "NotExistingBasket\n";
            }
            
            User truckOwner = truck.getUserID();
            
            ges.getEntityManager().merge(basket);
            ges.closeEm();
            
            truckBooking.setBookingDate(bookingDate);
            truckBooking.setBasketID(basket);
            truckBooking.setTruckID(truck);
            truckBooking.setTruckownerID(truckOwner);
            truckBooking.setClientID(account.getUser());
            
            ges.getEntityManager().merge(truckBooking);
            ges.closeEm();
            
            res = "good;"+basket.getBasketID()+";"+truckBooking.getTruckbookingID()+";"+basket.getBasketLabel();
            
        }catch(Throwable th){
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
             throw new TruckBookingManagementException(getClass()+"","editBookingDate",1,res,res);
        
           // return res;
        }
        
         logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"Result Server: res = "+res );
        
        return res;
    }
   
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfBookingTruck(int accountID, int index, int nombreMaxResultat) throws TruckBookingManagementException{
        List<Truck> lt = null;
        Result result = new Result();
        List<String> lts = new ArrayList<>();
        int size = 0;
        Account account;
        
        try{
            ges.creatEntityManager();
            
            account = ges.getEntityManager().find(Account.class, accountID);
            
            if(account == null){
                ges.closeEm();
                result.setMsg("InvalidAccountID");
                return result;
            }
            
            Query query =  ges.getEntityManager().createQuery("SELECT DISTINCT t.truckID FROM TruckBooking t WHERE t.deleted = false AND t.validated = true AND t.truckownerID = :truckOwnerID AND t.rejected = FALSE AND t.basketID IN (SELECT b FROM UserBasket b WHERE b.deleted = FALSE ) ORDER BY t.creationDate DESC");

            query.setParameter("truckOwnerID", account.getUser());
            lt = (List<Truck>)query.getResultList();
            size = lt.size();
            
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResultat);
            
            lt = (List<Truck>)query.getResultList();
            result.setMsg("good");
        
        }catch(Throwable th){
            if(ges == null){
                result.setMsg("Could not create Entity Manager\n"+th.getMessage());
                return result;
            }
            result.setMsg(th.getMessage());
        }
        
        
        if(lt != null && !lt.isEmpty()){
            String res = "";
            for(Truck truck: lt){
                if(!truck.getDeleted()){
                    Calendar c = Calendar.getInstance();
                    c.setTime(truck.getTrucktypeID().getYear());
                    
                    String driverInfo = DriverFunction.getDriverForTruck(truck);
                    
                    String pictureTrucksID = "";
                    String pictureTrucksPATH = "";
                    
                    String pictureInsurancesID = "";
                    String pictureInsurancesPATH = "";
                    
                    List<TruckDocument> truckDocumentList = ges.getEntityManager().createQuery("SELECT t FROM TruckDocument t WHERE t.deleted = FALSE AND t.truckID = :truckID AND t.documentID NOT IN (SELECT d FROM Document d WHERE d.deleted = TRUE) ").setParameter("truckID", truck).getResultList();
                    
                    for(TruckDocument truckDocument: truckDocumentList){
                        if(truckDocument.getType().equals("pictureTruck")) {
                            pictureTrucksID += "#"+truckDocument.getDocumentID().getDocumentID();
                            pictureTrucksPATH += "#"+truckDocument.getDocumentID().getPathName();
                        } else {
                            pictureInsurancesID += "#"+truckDocument.getDocumentID().getDocumentID();
                            pictureInsurancesPATH += "#"+truckDocument.getDocumentID().getPathName();
                        }
                        
                    }
                    try {
                        
                        pictureTrucksID = pictureTrucksID.substring(1, pictureTrucksID.length());
                        pictureTrucksPATH = pictureTrucksPATH.substring(1, pictureTrucksPATH.length());
                    } catch (Exception e) {
                        
                    }try {
                        
                        pictureInsurancesID = pictureInsurancesID.substring(1, pictureInsurancesID.length());
                        pictureInsurancesPATH = pictureInsurancesPATH.substring(1, pictureInsurancesPATH.length());
                        
                    } catch (Exception e) {
                    }
                    res = truck.getTruckID()+";"
                        +truck.getTruckNumber()+";"
                        +pictureTrucksPATH+";"
                        +truck.getAvailable()+";"
                        +truck.getCreationDate().getTime()+";"
                        +truck.getLocationPrice()+";"
                        
                        +truck.getLenghtofbedID().getLenghtofbedID()+";"
                        +truck.getDistance()+";"
                        +truck.getPhoneNumber()+";"
                        +truck.getTruckZipCode()+";"
                        +truck.getDOTNumber()+";"
                        +truck.getGeneralLiability()+";"
                        +truck.getInsuranceLiability()+";"
                        +pictureInsurancesPATH+";"
                            
                        +""+";"
                        +truck.getYear()+";"
                        +""+";"
                        +""+";"
                        +""+";"
                        +truck.getTruckaxleID().getTruckaxleID()+";"
                        +driverInfo+";"
                        +pictureTrucksID+";"
                        +pictureInsurancesID+";"
                        +"null"
                            ;
                    lts.add(res);
                }
                
            } 
            lts.add(""+size);
            result.setObjectList(lts);
        }
        
        return result;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfNotBookingTruck(int accountID, int index, int nombreMaxResultat) throws TruckBookingManagementException{
        List<Truck> lt = null;
        Result result = new Result();
        List<String> lts = new ArrayList<>();
        int size = 0;
        Account account;
        
        try{
            ges.creatEntityManager();
            
            account = ges.getEntityManager().find(Account.class, accountID);
            
            if(account == null){
                ges.closeEm();
                result.setMsg("InvalidAccountID");
                return result;
            }
            
            Query query =  ges.getEntityManager().createQuery("SELECT DISTINCT t.truckID FROM TruckBooking t WHERE t.deleted = false AND t.validated = FALSE AND t.truckownerID = :truckOwnerID AND t.rejected = FALSE AND t.basketID IN (SELECT b FROM UserBasket b WHERE b.deleted = FALSE ) ORDER BY t.creationDate DESC");

            query.setParameter("truckOwnerID", account.getUser());
            lt = (List<Truck>)query.getResultList();
            size = lt.size();
            
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResultat);
            
            lt = (List<Truck>)query.getResultList();
            result.setMsg("good");
        
        }catch(Throwable th){
            if(ges == null){
                result.setMsg("Could not create Entity Manager\n"+th.getMessage());
                return result;
            }
            result.setMsg(th.getMessage());
        }
        
        
        if(lt != null && !lt.isEmpty()){
            String res = "";
            for(Truck truck: lt){
                if(!truck.getDeleted()){
                    Calendar c = Calendar.getInstance();
                    c.setTime(truck.getTrucktypeID().getYear());
                    
                    String driverInfo = DriverFunction.getDriverForTruck(truck);
                    
                    String pictureTrucksID = "";
                    String pictureTrucksPATH = "";
                    
                    String pictureInsurancesID = "";
                    String pictureInsurancesPATH = "";
                    
                    List<TruckDocument> truckDocumentList = ges.getEntityManager().createQuery("SELECT t FROM TruckDocument t WHERE t.deleted = FALSE AND t.truckID = :truckID AND t.documentID NOT IN (SELECT d FROM Document d WHERE d.deleted = TRUE) ").setParameter("truckID", truck).getResultList();
                    
                    for(TruckDocument truckDocument: truckDocumentList){
                        if(truckDocument.getType().equals("pictureTruck")) {
                            pictureTrucksID += "#"+truckDocument.getDocumentID().getDocumentID();
                            pictureTrucksPATH += "#"+truckDocument.getDocumentID().getPathName();
                        } else {
                            pictureInsurancesID += "#"+truckDocument.getDocumentID().getDocumentID();
                            pictureInsurancesPATH += "#"+truckDocument.getDocumentID().getPathName();
                        }
                        
                    }
                    try {
                        
                        pictureTrucksID = pictureTrucksID.substring(1, pictureTrucksID.length());
                        pictureTrucksPATH = pictureTrucksPATH.substring(1, pictureTrucksPATH.length());
                    } catch (Exception e) {
                        
                    }try {
                        
                        pictureInsurancesID = pictureInsurancesID.substring(1, pictureInsurancesID.length());
                        pictureInsurancesPATH = pictureInsurancesPATH.substring(1, pictureInsurancesPATH.length());
                        
                    } catch (Exception e) {
                    }
                    res = truck.getTruckID()+";"
                        +truck.getTruckNumber()+";"
                        +pictureTrucksPATH+";"
                        +truck.getAvailable()+";"
                        +truck.getCreationDate().getTime()+";"
                        +truck.getLocationPrice()+";"
                        
                        +truck.getLenghtofbedID().getLenghtofbedID()+";"
                        +truck.getDistance()+";"
                        +truck.getPhoneNumber()+";"
                        +truck.getTruckZipCode()+";"
                        +truck.getDOTNumber()+";"
                        +truck.getGeneralLiability()+";"
                        +truck.getInsuranceLiability()+";"
                        +pictureInsurancesPATH+";"
                            
                        +""+";"
                        +truck.getYear()+";"
                        +""+";"
                        +""+";"
                        +""+";"
                        +truck.getTruckaxleID().getTruckaxleID()+";"
                        +driverInfo+";"
                        +pictureTrucksID+";"
                        +pictureInsurancesID+";"
                        +"null"
                            ;
                    lts.add(res);
                }
                
            } 
            lts.add(""+size);
            result.setObjectList(lts);
        }
        
        return result;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfValidatTruckBook(int accountID, int index, int nombreMaxResultat) throws TruckBookingManagementException{
        List<Truck> lt = null;
        Result result = new Result();
        List<String> lts = new ArrayList<>();
        int size = 0;
        Account account;
        
        try{
            ges.creatEntityManager();
            
            account = ges.getEntityManager().find(Account.class, accountID);
            
            if(account == null){
                ges.closeEm();
                result.setMsg("InvalidAccountID");
                return result;
            }
            
            Query query =  ges.getEntityManager().createQuery("SELECT DISTINCT t.truckID FROM TruckBooking t WHERE t.deleted = false AND t.truckOwnerValidation = 1 AND t.validated = true AND t.truckownerID = :truckOwnerID AND t.rejected = FALSE AND t.basketID IN (SELECT b FROM UserBasket b WHERE b.deleted = FALSE ) ORDER BY t.creationDate DESC");

            query.setParameter("truckOwnerID", account.getUser());
            lt = (List<Truck>)query.getResultList();
            size = lt.size();
            
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResultat);
            
            lt = (List<Truck>)query.getResultList();
            result.setMsg("good");
        
        }catch(Throwable th){
            if(ges == null){
                result.setMsg("Could not create Entity Manager\n"+th.getMessage());
                return result;
            }
            result.setMsg(th.getMessage());
        }
        
        
        if(lt != null && !lt.isEmpty()){
            String res = "";
            for(Truck truck: lt){
                if(!truck.getDeleted()){
                    Calendar c = Calendar.getInstance();
                    c.setTime(truck.getTrucktypeID().getYear());
                    
                    String driverInfo = DriverFunction.getDriverForTruck(truck);
                    
                    String pictureTrucksID = "";
                    String pictureTrucksPATH = "";
                    
                    String pictureInsurancesID = "";
                    String pictureInsurancesPATH = "";
                    
                    List<TruckDocument> truckDocumentList = ges.getEntityManager().createQuery("SELECT t FROM TruckDocument t WHERE t.deleted = FALSE AND t.truckID = :truckID AND t.documentID NOT IN (SELECT d FROM Document d WHERE d.deleted = TRUE) ").setParameter("truckID", truck).getResultList();
                    
                    for(TruckDocument truckDocument: truckDocumentList){
                        if(truckDocument.getType().equals("pictureTruck")) {
                            pictureTrucksID += "#"+truckDocument.getDocumentID().getDocumentID();
                            pictureTrucksPATH += "#"+truckDocument.getDocumentID().getPathName();
                        } else {
                            pictureInsurancesID += "#"+truckDocument.getDocumentID().getDocumentID();
                            pictureInsurancesPATH += "#"+truckDocument.getDocumentID().getPathName();
                        }
                        
                    }
                    try {
                        
                        pictureTrucksID = pictureTrucksID.substring(1, pictureTrucksID.length());
                        pictureTrucksPATH = pictureTrucksPATH.substring(1, pictureTrucksPATH.length());
                    } catch (Exception e) {
                        
                    }try {
                        
                        pictureInsurancesID = pictureInsurancesID.substring(1, pictureInsurancesID.length());
                        pictureInsurancesPATH = pictureInsurancesPATH.substring(1, pictureInsurancesPATH.length());
                        
                    } catch (Exception e) {
                    }
                    res = truck.getTruckID()+";"
                        +truck.getTruckNumber()+";"
                        +pictureTrucksPATH+";"
                        +truck.getAvailable()+";"
                        +truck.getCreationDate().getTime()+";"
                        +truck.getLocationPrice()+";"
                        
                        +truck.getLenghtofbedID().getLenghtofbedID()+";"
                        +truck.getDistance()+";"
                        +truck.getPhoneNumber()+";"
                        +truck.getTruckZipCode()+";"
                        +truck.getDOTNumber()+";"
                        +truck.getGeneralLiability()+";"
                        +truck.getInsuranceLiability()+";"
                        +pictureInsurancesPATH+";"
                            
                        +""+";"
                        +truck.getYear()+";"
                        +""+";"
                        +""+";"
                        +""+";"
                        +truck.getTruckaxleID().getTruckaxleID()+";"
                        +driverInfo+";"
                        +pictureTrucksID+";"
                        +pictureInsurancesID+";"
                        +"null"
                            ;
                    lts.add(res);
                }
                
            } 
            lts.add(""+size);
            result.setObjectList(lts);
        }
        
        return result;
    }
    
    
    @Override
    public String removeTruckBooking(int truckBookingID) throws TruckBookingManagementException{
        String res = "";
        
        TruckBooking truckBooking;
        
        UserBasket basket;
        
        try{
            
            ges.creatEntityManager();
            
            truckBooking = (TruckBooking)ges.getEntityManager().find(TruckBooking.class, truckBookingID);
            
            if(truckBooking == null){
                ges.closeEm();
                return "NotExistingTruckBooking\n";
            }
            
            basket = truckBooking.getBasketID();
            
            truckBooking.setDeleted(true);
            basket.setNumberOfTruck(basket.getNumberOfTruck() - 1);
            /*
            if(basket.getNumberOfTruck() == 0){
            this.deletedBasket(basket.getBasketID());
            }
            */
            ges.getEntityManager().merge(basket);
            ges.getEntityManager().merge(truckBooking);
            ges.closeEm();
            
            res = "good;"+basket.getBasketID()+";"+truckBooking;
            
        }catch(Throwable th){
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            throw new TruckBookingManagementException(getClass()+"","removeTruckBooking",1,res,res);
        
           // return res;
        }
        return res;
    }
    
    @Override
    public String deletedBasket(int basketID) throws TruckBookingManagementException{
        String res = "";

        UserBasket basket;

        List<TruckBooking> truckBookingList;

        try {

            ges.creatEntityManager();

            basket = (UserBasket) ges.getEntityManager().find(UserBasket.class, basketID);

            if (basket == null) {
                ges.closeEm();
                return "NotExistingBasket\n";
            }

            /*
             truckBookingList = (List<TruckBooking>)ges.getEntityManager().createQuery("SELECT t FROM TruckBooking t WHERE t.basketID = :basketID").setParameter("basketID", basket).getResultList();
            
             truckBookingList = (truckBookingList.isEmpty() ? basket.getTruckBookingList(): truckBookingList);
            
             logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"deletedBasket: Avant la suppression: truckBookingList = "+ truckBookingList);
            
             logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"deletedBasket:  basket = "+ basket +" AND basket.getDeleted = "+ basket.getDeleted());
             for(TruckBooking truckBooking: truckBookingList){
             logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"deletedBasket: truckBooking = "+truckBooking+" AND truckBooking.getDeleted = "+ truckBooking.getDeleted());
             }
            
             for(TruckBooking truckBooking: truckBookingList){
             logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"deletedBasket: truckBooking = "+ truckBooking);
             truckBooking.setDeleted(true);
             truckBooking.setBasketID(basket);
             ges.getEntityManager().merge(truckBooking);
            
             }
             */
            basket.setDeleted(true);
            ges.getEntityManager().merge(basket);
            ges.closeEm();

            res = "good;" + basket.getBasketID();
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"deletedBasket: res = " + res);
            /*
             logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"deletedBasket: Apres la suppression:  basket = "+ basket +" AND  basket.getDeleted = "+ basket.getDeleted());
             for(TruckBooking truckBooking: basket.getTruckBookingList()){
             logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"deletedBasket: truckBooking = "+truckBooking+" AND truckBooking.getDeleted = "+ truckBooking.getDeleted());
             }
             */
        } catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
             throw new TruckBookingManagementException(getClass()+"","deletedBasket",1,res,res);
        
            //return res;
        }
        return res;
    }

    
    @Override
    public String confimedBasket(int basketID) throws TruckBookingManagementException{
        String res = "";
        
        UserBasket basket;
        
         List<TruckBooking> truckBookingList;
        
        try{
            
            ges.creatEntityManager();
            
            basket = (UserBasket)ges.getEntityManager().find(UserBasket.class, basketID);
            
            if(basket == null){
                ges.closeEm();
                return "NotExistingBasket\n";
            }
            
            
            truckBookingList = (List<TruckBooking>)ges.getEntityManager().createQuery("SELECT t FROM TruckBooking t WHERE t.deleted = FALSE AND t.basketID = :basketID").setParameter("basketID", basket).getResultList();
            
            truckBookingList = (truckBookingList.isEmpty() ? basket.getTruckBookingList(): truckBookingList);
            
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"ConfimedBasket: Avant la confirmation: truckBookingList = "+ truckBookingList);
            
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"confimedBasket:  basket = "+ basket +" AND basket.getConfirmed = "+ basket.getConfirmed());
            for(TruckBooking truckBooking: truckBookingList){
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"confimedBasket: truckBooking = "+truckBooking+" AND truckBooking.getValidated = "+ truckBooking.getValidated());
            }
            
            for(TruckBooking truckBooking: truckBookingList){
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"confimedBasket: truckBooking = "+ truckBooking);
                truckBooking.setValidated(true);
                truckBooking.setBasketID(basket);
                ges.getEntityManager().merge(truckBooking);
                
            }
            
            
            basket.setConfirmed(1);
            ges.getEntityManager().merge(basket);
            ges.closeEm();
            
            res = "good;"+basket.getBasketID();
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"confimedBasket: res = "+ res);
            
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"confimedBasket: Apres la confirmation:  basket = "+ basket +" AND  basket.getConfirmed = "+ basket.getConfirmed());
            for(TruckBooking truckBooking: basket.getTruckBookingList()){
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"confimedBasket: truckBooking = "+truckBooking+" AND truckBooking.getValidated = "+ truckBooking.getValidated());
            }
        }catch(Throwable th){
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
              throw new TruckBookingManagementException(getClass()+"","confimedBasket",1,res,res);
        
            //return res;
        }
        return res;
    }
    
    
    @Override
    public String completBasket(int basketID) throws TruckBookingManagementException{
        String res = "";
        
        UserBasket basket;
        
         List<TruckBooking> truckBookingList;
        
        try{
            
            ges.creatEntityManager();
            
            basket = (UserBasket)ges.getEntityManager().find(UserBasket.class, basketID);
            
            if(basket == null){
                ges.closeEm();
                return "NotExistingBasket\n";
            }
            
            
            truckBookingList = (List<TruckBooking>)ges.getEntityManager().createQuery("SELECT t FROM TruckBooking t WHERE t.deleted = FALSE AND t.rejected = FALSE AND t.validated = TRUE AND t.basketID = :basketID").setParameter("basketID", basket).getResultList();
            
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+" --- truckBookingList " + truckBookingList + " ...");
            
            //Ajout des dates d'indisponibilités du truck.
            for(TruckBooking truckBooking : truckBookingList){
                 
                if (truckBooking.getTruckOwnerValidation() == 1) {
                    /*Truck truck = truckBooking.getTruckID();
                    
                    logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+" Mise a jour des indisponibilités du truck = "+truck+" se trouvant dans le truckBookingList: " + truckBookingList);
                    
                    tbf = new TruckBookingFunction();
                    
                    List<String> unAvailabilityDateList = tbf.getUnAvailabilityDateByBookingDate(truckBooking.getBookingDate());
                    for (String unAvailabilityDate : unAvailabilityDateList) {
                    
                    Availability availability = null;
                    
                    Date year = tbf.parseYearToDate(unAvailabilityDate.split("#")[0]);
                    try {
                    
                    availability = (Availability) ges.getEntityManager().createQuery("SELECT a FROM Availability a WHERE a.deleted = FALSE AND a.truckID = :truckID AND a.year = :year").setParameter("truckID", truck).setParameter("year", year).getSingleResult();
                    
                    } catch (Exception e) {
                    logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+" ---- " + e.getMessage());
                    }
                    
                    logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"truck = " + truck + " availability  =  " + availability);
                    if (availability != null) {
                    availability.setUnavailableDates(availability.getUnavailableDates() + "#" + unAvailabilityDate.substring(5, unAvailabilityDate.length()));
                    ges.getEntityManager().merge(availability);
                    } else {
                    availability = new Availability();
                    
                    availability.setYear(new Date(year.getTime()));
                    availability.setUnavailableDates(unAvailabilityDate.substring(5, unAvailabilityDate.length()));
                    availability.setDeleted(false);
                    availability.setTruckID(truck);
                    availability.setTruckownerID(truck.getUserID());
                    ges.getEntityManager().persist(availability);
                    ges.closeEm();
                    
                    }
                    
                    }*/
                    truckBooking.setClientValidation(1);
                    ges.getEntityManager().merge(truckBooking);
                    ges.closeEm();
                } else {
                    logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"Reject: Truck =  " + truckBooking.getTruckID() + " - --- TruckBooking = " + truckBooking);
                    truckBooking.setRejected(true);
                    ges.getEntityManager().merge(truckBooking);
                    ges.closeEm();
                }

            }
            
            basket.setComplet(true);
            ges.getEntityManager().merge(basket);
            ges.closeEm();
            
            res = "good;"+basket.getBasketID();
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"completBasket: res = "+ res);
            
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"completBasket: after complet:  basket = "+ basket +" AND  basket.getComplet = "+ basket.getComplet());
            for(TruckBooking truckBooking: basket.getTruckBookingList()){
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"completBasket: truckBooking = "+truckBooking+" AND truckBooking.getValidated = "+ truckBooking.getRejected());
            }
        }catch(Throwable th){
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            throw new TruckBookingManagementException(getClass()+"","completBasket",1,res,res);
        
           // return res;
        }
        return res;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfBookingInfoByTruckID(int truckID, int index, int nombreMaxResultat) throws TruckBookingManagementException{
        List<TruckBooking> lt = null;
        Result result = new Result();
        List<String> lts = new ArrayList<>();
        int size = 0;
        Truck truck;
        
        try{
            ges.creatEntityManager();
            
            truck = ges.getEntityManager().find(Truck.class, truckID);
            
            if(truck == null){
                ges.closeEm();
                result.setMsg("InvalidTruckID");
                return result;
            }
            
            Query query =  ges.getEntityManager().createQuery("SELECT t FROM TruckBooking t WHERE t.deleted = false AND t.validated = true AND t.truckID = :truckID AND t.rejected = FALSE AND t.basketID IN (SELECT b FROM UserBasket b WHERE b.deleted = FALSE ) ORDER BY t.creationDate DESC");

            query.setParameter("truckID", truck);
            lt = (List<TruckBooking>)query.getResultList();
            size = lt.size();
            
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResultat);
            
            lt = (List<TruckBooking>)query.getResultList();
            result.setMsg("good");
        
        }catch(Throwable th){
            if(ges == null){
                result.setMsg("Could not create Entity Manager\n"+th.getMessage());
                return result;
            }
            result.setMsg(th.getMessage());
        }
        
        
        if(lt != null && !lt.isEmpty()){
            String res = "";
            for(TruckBooking truckBooking: lt){
                
                res = truckBooking.getTruckbookingID()+";"
                        +truckBooking.getCreationDate().getTime()+";"
                        +truckBooking.getBookingDate()+";"
                        +truckBooking.getClientID().getName()+";"
                        +truckBooking.getClientID().getSurname()+";"
                        +truckBooking.getClientID().getAccountID().getEmail()+";"
                        +truckBooking.getClientValidation()+";"
                        +truckBooking.getTruckOwnerValidation()+";"
                        +truckBooking.getValidated()+";"
                        +truckBooking.getPaid()+";"
                        +truckBooking.getPaymentRecieve()+";"
                        +truckBooking.getSettled()+";"
                        +truckBooking.getBookingPrice()+";"
                        ;
                lts.add(res);
            }
            
            lts.add(""+size);
            result.setObjectList(lts);
        }
        
        return result;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfValidatedBookingByTruckID(int truckID, int index, int nombreMaxResultat) throws TruckBookingManagementException{
        List<TruckBooking> lt = null;
        Result result = new Result();
        List<String> lts = new ArrayList<>();
        int size = 0;
        Truck truck;
        
        try{
            ges.creatEntityManager();
            
            truck = ges.getEntityManager().find(Truck.class, truckID);
            
            if(truck == null){
                ges.closeEm();
                result.setMsg("InvalidTruckID");
                return result;
            }
            
            Query query =  ges.getEntityManager().createQuery("SELECT t FROM TruckBooking t WHERE t.deleted = false AND t.clientValidation = 1 AND t.truckOwnerValidation = 1 AND t.validated = true AND t.truckID = :truckID AND t.rejected = FALSE AND t.basketID IN (SELECT b FROM UserBasket b WHERE b.deleted = FALSE ) ORDER BY t.creationDate DESC");

            query.setParameter("truckID", truck);
            lt = (List<TruckBooking>)query.getResultList();
            size = lt.size();
            
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResultat);
            
            lt = (List<TruckBooking>)query.getResultList();
            result.setMsg("good");
        
        }catch(Throwable th){
            if(ges == null){
                result.setMsg("Could not create Entity Manager\n"+th.getMessage());
                return result;
            }
            result.setMsg(th.getMessage());
        }
        
        
        if(lt != null && !lt.isEmpty()){
            String res = "";
            for(TruckBooking truckBooking: lt){
                
                res = truckBooking.getTruckbookingID()+";"
                        +truckBooking.getCreationDate().getTime()+";"
                        +truckBooking.getBookingDate()+";"
                        +truckBooking.getClientID().getName()+";"
                        +truckBooking.getClientID().getSurname()+";"
                        +truckBooking.getClientID().getAccountID().getEmail()+";"
                        +truckBooking.getClientValidation()+";"
                        +truckBooking.getTruckOwnerValidation()+";"
                        +truckBooking.getValidated()+";"
                        +truckBooking.getPaid()+";"
                        +truckBooking.getPaymentRecieve()+";"
                        +truckBooking.getSettled()+";"
                        +truckBooking.getBookingPrice()+";"
                        ;
                lts.add(res);
            }
            
            lts.add(""+size);
            result.setObjectList(lts);
        }
        
        return result;
    }
    
    
    //Recherche toutes les dates d'invalidités d'un truck à partir de la date du jour.
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getUnavailabilityDatesByTruckID(int truckID) throws TruckBookingManagementException{
        
        List<Date> availabilityList = null;
        Iterator solicitedTruckList = null;
        //List<TruckBooking> truckBookingList = null;
        
        List<String> listResult = null;

        Result result = new Result();
        Truck truck = null;
        tbf = new TruckBookingFunction();
        Date currentDate = DateFunction.getGMTDate();
        
        tbf = new TruckBookingFunction();
        
        try {

            ges.creatEntityManager();
            truck = (Truck) ges.getEntityManager().find(Truck.class, truckID);

        } catch (Throwable th) {
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getUnavailabilityDatesByTruckID: Error " + th.getMessage());
            result.setMsg(th.getMessage());
        }

        if (truck == null) {
            result.setMsg("InvalidTruckID");
            ges.closeEm();
            return result;
        } else if(truck.getDeleted()) {
            result.setMsg("InvalidTruckID");
            return result;
        }

        try {
            
            Query query;
            query = ges.getEntityManager().createQuery("SELECT DISTINCT a.availbilityDate FROM Availability a WHERE a.deleted = false AND a.state = 1 AND a.availbilityDate BETWEEN :firstDateOfYear AND :endDateOfYear AND a.truckID = :truckID");

            query.setParameter("truckID", truck);
            query.setParameter("firstDateOfYear", DateFunction.getDateRangeOfYear()[0]);
            query.setParameter("endDateOfYear", DateFunction.getDateRangeOfYear()[1]);
            availabilityList = (List<Date>) query.getResultList();
            
            query = ges.getEntityManager().createQuery("SELECT MIN(a.startDate), MAX(a.endDate) FROM SolicitedTruck a WHERE a.deleted = false AND a.truckAvailable = TRUE AND a.endDate >= :year AND a.truckID = :truckID");

            query.setParameter("truckID", truck);
            query.setParameter("year", currentDate);
            solicitedTruckList = query.getResultList().iterator();

            result.setMsg("good");
            ges.closeEm();

        } catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getUnavailabilityDatesByTruckID: Error: \n" + th.getMessage() + "\n" + string.toString());
            result.setMsg(th.getMessage() + "\n" + string.toString());
        }

        //***********
        String availabilityDate = "";
        listResult = new ArrayList<>();

        if (availabilityList != null && !availabilityList.isEmpty()) {
            for (Date availability : availabilityList) {
                
                availabilityDate += "#" + availability.getTime();
                
            }
            
            availabilityDate = availabilityDate.substring(1);
        }
        
        listResult.add(availabilityDate);
        
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getUnavailabilityDatesByTruckID: availabilityDate = " + availabilityDate);

        //*************
        String solicitedTruckDate = "";
        if (solicitedTruckList != null ) {
            while (solicitedTruckList.hasNext()) {
                Object[] objects = (Object[])solicitedTruckList.next();
                
                tbf = new TruckBookingFunction();
                
                Date startDate = currentDate;

                Date endDate = (Date) objects[1];
                
                solicitedTruckDate = tbf.getUnAvailabilityDate(startDate, endDate);
                
            }

        }
        
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getUnavailabilityDatesByTruckID: solicitedTruckDate = " + solicitedTruckDate);
        listResult.add(solicitedTruckDate);
       
        listResult.add("");
        
        result.setMsg("good");
        result.setObjectList(listResult);
        return result;
        
        
    }
    
    @Override
    public String confirmTruckBooking(int truckBookingID) throws TruckBookingManagementException{
        String res = "";
        
        TruckBooking truckBooking;
        
        try{
            
            ges.creatEntityManager();
            
            truckBooking = (TruckBooking)ges.getEntityManager().find(TruckBooking.class, truckBookingID);
            
            if(truckBooking == null){
                ges.closeEm();
                return "NotExistingTruckBooking\n";
            }
            
            truckBooking.setTruckOwnerValidation(1);
            
            ges.getEntityManager().merge(truckBooking);
            ges.closeEm();
            
            res = "good;"+truckBooking.getTruckbookingID();
            
        }catch(Throwable th){
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();  
            throw new TruckBookingManagementException(getClass()+"","confirmTruckBooking",1,res,res);
        
            
            //return res;
        }
        return res;
    }

    @Override
    public String rejectBookingForTruck(int truckBookingID) throws TruckBookingManagementException{
        String res = "";
        
        TruckBooking truckBooking;
        
        try{
            
            ges.creatEntityManager();
            
            truckBooking = (TruckBooking)ges.getEntityManager().find(TruckBooking.class, truckBookingID);
            
            if(truckBooking == null){
                ges.closeEm();
                return "NotExistingTruckBooking\n";
            }
            
            truckBooking.setTruckOwnerValidation(2);
            
            ges.getEntityManager().merge(truckBooking);
            ges.closeEm();
            
            res = "good;"+truckBooking.getTruckbookingID();
            
        }catch(Throwable th){
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            throw new TruckBookingManagementException(getClass()+"","rejectBookingForTruck",1,res,res);
        
            //return res;
        }
        return res;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfBasketByAccountID(int accountID, int index, int nombreMaxResultat)throws TruckBookingManagementException {
        List<UserBasket> lt = null;
        Result result = new Result();
        List<String> lts = new ArrayList<>();
        int size = 0;
        Account account;
        
        try{
            ges.creatEntityManager();
            
            account = ges.getEntityManager().find(Account.class, accountID);
            
            if(account == null){
                ges.closeEm();
                result.setMsg("InvalidAccountID");
                return result;
            }
            
            Query query =  ges.getEntityManager().createQuery("SELECT DISTINCT t.basketID FROM TruckBooking t WHERE t.deleted = FALSE AND t.validated = TRUE AND t.clientID = :clientID  AND t.rejected = FALSE AND t.basketID IN (SELECT b FROM UserBasket b WHERE b.deleted = FALSE ) ORDER BY t.creationDate DESC");

            query.setParameter("clientID", account.getUser());
            lt = (List<UserBasket>)query.getResultList();
            size = lt.size();
            
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResultat);
            
            lt = (List<UserBasket>)query.getResultList();
            result.setMsg("good");
        
        }catch(Throwable th){
            if(ges == null){
                result.setMsg("Could not create Entity Manager\n"+th.getMessage());
                return result;
            }
            result.setMsg(th.getMessage());
        }
        
        
        if(lt != null && !lt.isEmpty()){
            String res = "";
            for(UserBasket userBasket: lt){
                if(!userBasket.getDeleted()){
                    
                    res = userBasket.getBasketID()+";"
                        +userBasket.getBasketLabel()+";"
                        +userBasket.getNumberOfTruck()+";"
                        +userBasket.getComplet()+";"
                            
                        +"null"
                            ;
                    lts.add(res);
                }
                
            } 
            lts.add(""+size);
            result.setObjectList(lts);
        }
        
        return result;
        
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfValidBasketByAccountID(int accountID, int index, int nombreMaxResultat) throws TruckBookingManagementException{
        List<UserBasket> lt = null;
        Result result = new Result();
        List<String> lts = new ArrayList<>();
        int size = 0;
        Account account;
        
        try{
            ges.creatEntityManager();
            
            account = ges.getEntityManager().find(Account.class, accountID);
            
            if(account == null){
                ges.closeEm();
                result.setMsg("InvalidAccountID");
                return result;
            }
             Query query =  ges.getEntityManager().createQuery("SELECT DISTINCT t.basketID FROM TruckBooking t WHERE t.deleted = false AND t.clientID = :clientID AND t.basketID IN (SELECT b FROM UserBasket b WHERE b.deleted = FALSE AND b.confirmed = 1) "
                    + "ORDER BY t.creationDate DESC");
             
            query.setParameter("clientID", account.getUser());
            lt = (List<UserBasket>)query.getResultList();
            size = lt.size();
            
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResultat);
            
            lt = (List<UserBasket>)query.getResultList();
            result.setMsg("good");
        
        }catch(Throwable th){
            if(ges == null){
                result.setMsg("Could not create Entity Manager\n"+th.getMessage());
                return result;
            }
            result.setMsg(th.getMessage());
        }
        
        
        if(lt != null && !lt.isEmpty()){
            String res = "";
            for(UserBasket userBasket: lt){
                if(!userBasket.getDeleted()){
                    
                    res = userBasket.getBasketID()+";"
                        +userBasket.getBasketLabel()+";"
                        +userBasket.getNumberOfTruck()+";"
                            
                        +"null"
                            ;
                    lts.add(res);
                }
                
            } 
            lts.add(""+size);
            result.setObjectList(lts);
        }
        
        return result;
        
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfUnValidBasketByAccountID(int accountID, int index, int nombreMaxResultat) throws TruckBookingManagementException{
        List<UserBasket> lt = null;
        Result result = new Result();
        List<String> lts = new ArrayList<>();
        int size = 0;
        Account account;
        
        try{
            ges.creatEntityManager();
            
            account = ges.getEntityManager().find(Account.class, accountID);
            
            if(account == null){
                ges.closeEm();
                result.setMsg("InvalidAccountID");
                return result;
            }
            
            Query query =  ges.getEntityManager().createQuery("SELECT DISTINCT t.basketID FROM TruckBooking t WHERE t.deleted = false AND t.clientID = :clientID AND t.basketID IN (SELECT b FROM UserBasket b WHERE b.deleted = FALSE AND b.confirmed = 0) "
                    + "ORDER BY t.creationDate DESC");

            query.setParameter("clientID", account.getUser());
            lt = (List<UserBasket>)query.getResultList();
            size = lt.size();
            
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResultat);
            
            lt = (List<UserBasket>)query.getResultList();
            result.setMsg("good");
        
        }catch(Throwable th){
            if(ges == null){
                result.setMsg("Could not create Entity Manager\n"+th.getMessage());
                return result;
            }
            result.setMsg(th.getMessage());
        }
        
        
        if(lt != null && !lt.isEmpty()){
            String res = "";
            for(UserBasket userBasket: lt){
                if(!userBasket.getDeleted()){
                    
                    res = userBasket.getBasketID()+";"
                        +userBasket.getBasketLabel()+";"
                        +userBasket.getNumberOfTruck()+";"
                            
                        +"null"
                            ;
                    lts.add(res);
                }
                
            } 
            lts.add(""+size);
            result.setObjectList(lts);
        }
        
        return result;
        
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfConfirmTruckByBasketID(int accountID, int basketID, int index, int nombreMaxResultat) throws TruckBookingManagementException{
        List<TruckBooking> lt = null;
        Result result = new Result();
        List<String> lts = new ArrayList<>();
        int size = 0;
        Account account;
        UserBasket basket;
        
        try{
            ges.creatEntityManager();
            
            account = ges.getEntityManager().find(Account.class, accountID);
            
            basket = ges.getEntityManager().find(UserBasket.class, basketID);
            
            if(account == null){
                ges.closeEm();
                result.setMsg("InvalidAccountID");
                return result;
            }
            
            if(basket == null){
                ges.closeEm();
                result.setMsg("InvalidBasketID");
                return result;
            }
            
            Query query =  ges.getEntityManager().createQuery("SELECT DISTINCT t FROM TruckBooking t WHERE t.deleted = false AND t.validated = true AND t.clientID = :clientID AND t.basketID = :basketID ");

            query.setParameter("clientID", account.getUser());
            query.setParameter("basketID", basket);
            lt = (List<TruckBooking>)query.getResultList();
            size = lt.size();
            
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResultat);
            
            lt = (List<TruckBooking>)query.getResultList();
            result.setMsg("good");
        
        }catch(Throwable th){
            if(ges == null){
                result.setMsg("Could not create Entity Manager\n"+th.getMessage());
                return result;
            }
            result.setMsg(th.getMessage());
        }
        
        
        if(lt != null && !lt.isEmpty()){
            String res = "";
            for(TruckBooking truckBooking: lt){
                
                Truck truck = truckBooking.getTruckID();
                
                if(!truck.getDeleted()){
                    Calendar c = Calendar.getInstance();
                    c.setTime(truck.getTrucktypeID().getYear());
                    
                    String driverInfo = DriverFunction.getDriverForTruck(truck);
                    
                    String pictureTrucksID = "";
                    String pictureTrucksPATH = "";
                    
                    String pictureInsurancesID = "";
                    String pictureInsurancesPATH = "";
                    
                    List<TruckDocument> truckDocumentList = ges.getEntityManager().createQuery("SELECT t FROM TruckDocument t WHERE t.deleted = FALSE AND t.truckID = :truckID AND t.documentID NOT IN (SELECT d FROM Document d WHERE d.deleted = TRUE) ").setParameter("truckID", truck).getResultList();
                    
                    for(TruckDocument truckDocument: truckDocumentList){
                        if(truckDocument.getType().equals("pictureTruck")) {
                            pictureTrucksID += "#"+truckDocument.getDocumentID().getDocumentID();
                            pictureTrucksPATH += "#"+truckDocument.getDocumentID().getPathName();
                        } else {
                            pictureInsurancesID += "#"+truckDocument.getDocumentID().getDocumentID();
                            pictureInsurancesPATH += "#"+truckDocument.getDocumentID().getPathName();
                        }
                        
                    }
                    try {
                        
                        pictureTrucksID = pictureTrucksID.substring(1, pictureTrucksID.length());
                        pictureTrucksPATH = pictureTrucksPATH.substring(1, pictureTrucksPATH.length());
                    } catch (Exception e) {
                        
                    }try {
                        
                        pictureInsurancesID = pictureInsurancesID.substring(1, pictureInsurancesID.length());
                        pictureInsurancesPATH = pictureInsurancesPATH.substring(1, pictureInsurancesPATH.length());
                        
                    } catch (Exception e) {
                    }
                    res = truck.getTruckID()+";"
                        +truck.getTruckNumber()+";"
                        +pictureTrucksPATH+";"
                        +truck.getAvailable()+";"
                        +truck.getCreationDate().getTime()+";"
                        +truck.getLocationPrice()+";"
                        
                        +truck.getLenghtofbedID().getLenghtofbedID()+";"
                        +truck.getDistance()+";"
                        +truck.getPhoneNumber()+";"
                        +truck.getTruckZipCode()+";"
                        +truck.getDOTNumber()+";"
                        +truck.getGeneralLiability()+";"
                        +truck.getInsuranceLiability()+";"
                        +pictureInsurancesPATH+";"
                            
                        +""+";"
                        +truck.getYear()+";"
                        +""+";"
                        +""+";"
                        +""+";"
                        +truck.getTruckaxleID().getTruckaxleID()+";"
                        +driverInfo+";"
                        +pictureTrucksID+";"
                        +pictureInsurancesID+";"
                        +truckBooking.getTruckbookingID()+";"
                        +truckBooking.getValidated()+";"
                        +truckBooking.getRejected()+";"
                        +truckBooking.getTruckOwnerValidation()+";"
                        +truckBooking.getPaid()+";"
                        +truckBooking.getPaymentRecieve()+";"
                        +truckBooking.getSettled()+";"
                        +truckBooking.getBookingPrice()+";"
                        +"null"
                            ;
                    lts.add(res);
                }
                
            } 
            lts.add(""+size);
            result.setObjectList(lts);
        }
        
        return result;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfConfirmTruckByBasketIDFrontEnd(int accountID, int basketID, int index, int nombreMaxResultat) throws TruckBookingManagementException{
        List<TruckBooking> lt = null;
        Result result = new Result();
        List<String> lts = new ArrayList<>();
        int size = 0;
        Account account;
        UserBasket basket;
        
        try{
            ges.creatEntityManager();
            
            account = ges.getEntityManager().find(Account.class, accountID);
            
            basket = ges.getEntityManager().find(UserBasket.class, basketID);
            
            if(account == null){
                ges.closeEm();
                result.setMsg("InvalidAccountID");
                return result;
            }
            
            if(basket == null){
                ges.closeEm();
                result.setMsg("InvalidBasketID");
                return result;
            }
            
            Query query =  ges.getEntityManager().createQuery("SELECT DISTINCT t FROM TruckBooking t WHERE t.deleted = false AND t.validated = true AND t.clientID = :clientID AND t.basketID = :basketID ");

            query.setParameter("clientID", account.getUser());
            query.setParameter("basketID", basket);
            lt = (List<TruckBooking>)query.getResultList();
            size = lt.size();
            
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResultat);
            
            lt = (List<TruckBooking>)query.getResultList();
            result.setMsg("good");
        
        }catch(Throwable th){
            if(ges == null){
                result.setMsg("Could not create Entity Manager\n"+th.getMessage());
                return result;
            }
            result.setMsg(th.getMessage());
        }
        
        
        if(lt != null && !lt.isEmpty()){
            String res = "";
            for(TruckBooking truckBooking: lt){
                
                Truck truck = truckBooking.getTruckID();
                
                if(!truck.getDeleted()){
                    Calendar c = Calendar.getInstance();
                    c.setTime(truck.getTrucktypeID().getYear());
                    
                    String pictureTrucksID = "";
                    String pictureTrucksPATH = "";
                    
                    String pictureInsurancesID = "";
                    String pictureInsurancesPATH = "";
                    
                    List<TruckDocument> truckDocumentList = ges.getEntityManager().createQuery("SELECT t FROM TruckDocument t WHERE t.deleted = FALSE AND t.truckID = :truckID AND t.documentID NOT IN (SELECT d FROM Document d WHERE d.deleted = TRUE) ").setParameter("truckID", truck).getResultList();
                    
                    for(TruckDocument truckDocument: truckDocumentList){
                        if(truckDocument.getType().equals("pictureTruck")) {
                            pictureTrucksID += "#"+truckDocument.getDocumentID().getDocumentID();
                            pictureTrucksPATH += "#"+truckDocument.getDocumentID().getPathName();
                        } else {
                            pictureInsurancesID += "#"+truckDocument.getDocumentID().getDocumentID();
                            pictureInsurancesPATH += "#"+truckDocument.getDocumentID().getPathName();
                        }
                        
                    }
                    try {
                        
                        pictureTrucksID = pictureTrucksID.substring(1, pictureTrucksID.length());
                        pictureTrucksPATH = pictureTrucksPATH.substring(1, pictureTrucksPATH.length());
                    } catch (Exception e) {
                        
                    }try {
                        
                        pictureInsurancesID = pictureInsurancesID.substring(1, pictureInsurancesID.length());
                        pictureInsurancesPATH = pictureInsurancesPATH.substring(1, pictureInsurancesPATH.length());
                        
                    } catch (Exception e) {
                    }
                    res = truck.getTruckID()+";"
                        +truck.getTruckNumber()+";"
                        +pictureTrucksPATH+";"
                        +truck.getAvailable()+";"
                        +truck.getCreationDate().getTime()+";"
                        +truck.getLocationPrice()+";"
                        
                        +truck.getLenghtofbedID().getLenghtofbedID()+";"
                        +truck.getDistance()+";"
                        +truck.getPhoneNumber()+";"
                        +truck.getTruckZipCode()+";"
                        +truck.getDOTNumber()+";"
                        +truck.getGeneralLiability()+";"
                        +truck.getInsuranceLiability()+";"
                        +pictureInsurancesPATH+";"
                            
                        +truck.getYear()+";"
                        +truck.getTruckaxleID().getTruckaxleID()+";"
                        +pictureTrucksID+";"
                        +pictureInsurancesID+";"
                        +truckBooking.getTruckbookingID()+";"
                        +truckBooking.getValidated()+";"
                        +truckBooking.getRejected()+";"
                        +truckBooking.getTruckOwnerValidation()+";"
                        +truckBooking.getBasketID().getBasketID()+";"
                        +truckBooking.getBasketID().getConfirmed()+";"
                        +truckBooking.getBasketID().getComplet()+";"
                        +truckBooking.getPaid()+";"
                        +truckBooking.getPaymentRecieve()+";"
                        +truckBooking.getSettled()+";"
                        +truckBooking.getBookingPrice()+";"
                        +"null"
                            ;
                    lts.add(res);
                }
                
            } 
            lts.add(""+size);
            result.setObjectList(lts);
        }
        
        return result;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfNotConfirmTruckByBasketID(int accountID, int basketID, int index, int nombreMaxResultat) throws TruckBookingManagementException{
        List<Truck> lt = null;
        Result result = new Result();
        List<String> lts = new ArrayList<>();
        int size = 0;
        Account account;
        UserBasket basket;
        
        try{
            ges.creatEntityManager();
            
            account = ges.getEntityManager().find(Account.class, accountID);
            
            basket = ges.getEntityManager().find(UserBasket.class, basketID);
            
            if(account == null){
                ges.closeEm();
                result.setMsg("InvalidAccountID");
                return result;
            }
            
            if(basket == null){
                ges.closeEm();
                result.setMsg("InvalidBasketID");
                return result;
            }
            
            Query query =  ges.getEntityManager().createQuery("SELECT DISTINCT t.truckID FROM TruckBooking t WHERE t.deleted = false AND t.validated = true AND t.clientID = :clientID AND t.basketID = :basketID ");

            query.setParameter("clientID", account.getUser());
            query.setParameter("basketID", basket);
            lt = (List<Truck>)query.getResultList();
            size = lt.size();
            
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResultat);
            
            lt = (List<Truck>)query.getResultList();
            result.setMsg("good");
        
        }catch(Throwable th){
            if(ges == null){
                result.setMsg("Could not create Entity Manager\n"+th.getMessage());
                return result;
            }
            result.setMsg(th.getMessage());
        }
        
        
        if(lt != null && !lt.isEmpty()){
            String res = "";
            for(Truck truck: lt){
                if(!truck.getDeleted()){
                    Calendar c = Calendar.getInstance();
                    c.setTime(truck.getTrucktypeID().getYear());
                    
                    String driverInfo = DriverFunction.getDriverForTruck(truck);
                    
                    String pictureTrucksID = "";
                    String pictureTrucksPATH = "";
                    
                    String pictureInsurancesID = "";
                    String pictureInsurancesPATH = "";
                    
                    List<TruckDocument> truckDocumentList = ges.getEntityManager().createQuery("SELECT t FROM TruckDocument t WHERE t.deleted = FALSE AND t.truckID = :truckID AND t.documentID NOT IN (SELECT d FROM Document d WHERE d.deleted = TRUE) ").setParameter("truckID", truck).getResultList();
                    
                    for(TruckDocument truckDocument: truckDocumentList){
                        if(truckDocument.getType().equals("pictureTruck")) {
                            pictureTrucksID += "#"+truckDocument.getDocumentID().getDocumentID();
                            pictureTrucksPATH += "#"+truckDocument.getDocumentID().getPathName();
                        } else {
                            pictureInsurancesID += "#"+truckDocument.getDocumentID().getDocumentID();
                            pictureInsurancesPATH += "#"+truckDocument.getDocumentID().getPathName();
                        }
                        
                    }
                    try {
                        
                        pictureTrucksID = pictureTrucksID.substring(1, pictureTrucksID.length());
                        pictureTrucksPATH = pictureTrucksPATH.substring(1, pictureTrucksPATH.length());
                    } catch (Exception e) {
                        
                    }try {
                        
                        pictureInsurancesID = pictureInsurancesID.substring(1, pictureInsurancesID.length());
                        pictureInsurancesPATH = pictureInsurancesPATH.substring(1, pictureInsurancesPATH.length());
                        
                    } catch (Exception e) {
                    }
                    res = truck.getTruckID()+";"
                        +truck.getTruckNumber()+";"
                        +pictureTrucksPATH+";"
                        +truck.getAvailable()+";"
                        +truck.getCreationDate().getTime()+";"
                        +truck.getLocationPrice()+";"
                        
                        +truck.getLenghtofbedID().getLenghtofbedID()+";"
                        +truck.getDistance()+";"
                        +truck.getPhoneNumber()+";"
                        +truck.getTruckZipCode()+";"
                        +truck.getDOTNumber()+";"
                        +truck.getGeneralLiability()+";"
                        +truck.getInsuranceLiability()+";"
                        +pictureInsurancesPATH+";"
                            
                        +""+";"
                        +truck.getYear()+";"
                        +""+";"
                        +""+";"
                        +""+";"
                        +truck.getTruckaxleID().getTruckaxleID()+";"
                        +driverInfo+";"
                        +pictureTrucksID+";"
                        +pictureInsurancesID+";"
                        +"null"
                            ;
                    lts.add(res);
                }
                
            } 
            lts.add(""+size);
            result.setObjectList(lts);
        }
        
        return result;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfTruckByBasketID(int accountID, int basketID, int index, int nombreMaxResultat)throws TruckBookingManagementException {
        List<TruckBooking> lt = null;
        Result result = new Result();
        List<String> lts = new ArrayList<>();
        int size = 0;
        Account account;
        UserBasket basket;
        
        try{
            ges.creatEntityManager();
            
            account = ges.getEntityManager().find(Account.class, accountID);
            
            basket = ges.getEntityManager().find(UserBasket.class, basketID);
            
            if(account == null){
                ges.closeEm();
                result.setMsg("InvalidAccountID");
                return result;
            }
            
            if(basket == null){
                ges.closeEm();
                result.setMsg("InvalidBasketID");
                return result;
            }
            
            Query query =  ges.getEntityManager().createQuery("SELECT DISTINCT t FROM TruckBooking t WHERE t.deleted = false AND t.clientID = :clientID AND t.basketID = :basketID ");

            query.setParameter("clientID", account.getUser());
            query.setParameter("basketID", basket);
            lt = (List<TruckBooking>)query.getResultList();
            size = lt.size();
            
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResultat);
            
            lt = (List<TruckBooking>)query.getResultList();
            result.setMsg("good");
        
        }catch(Throwable th){
            if(ges == null){
                result.setMsg("Could not create Entity Manager\n"+th.getMessage());
                return result;
            }
            result.setMsg(th.getMessage());
        }
        
        
        if(lt != null && !lt.isEmpty()){
            String res = "";
            for(TruckBooking truckBooking: lt){
                
                Truck truck = truckBooking.getTruckID();
                
                if(!truck.getDeleted()){
                    Calendar c = Calendar.getInstance();
                    c.setTime(truck.getTrucktypeID().getYear());
                    
                    String driverInfo = DriverFunction.getDriverForTruck(truck);
                    
                    String pictureTrucksID = "";
                    String pictureTrucksPATH = "";
                    
                    String pictureInsurancesID = "";
                    String pictureInsurancesPATH = "";
                    
                    List<TruckDocument> truckDocumentList = ges.getEntityManager().createQuery("SELECT t FROM TruckDocument t WHERE t.deleted = FALSE AND t.truckID = :truckID AND t.documentID NOT IN (SELECT d FROM Document d WHERE d.deleted = TRUE) ").setParameter("truckID", truck).getResultList();
                    
                    for(TruckDocument truckDocument: truckDocumentList){
                        if(truckDocument.getType().equals("pictureTruck")) {
                            pictureTrucksID += "#"+truckDocument.getDocumentID().getDocumentID();
                            pictureTrucksPATH += "#"+truckDocument.getDocumentID().getPathName();
                        } else {
                            pictureInsurancesID += "#"+truckDocument.getDocumentID().getDocumentID();
                            pictureInsurancesPATH += "#"+truckDocument.getDocumentID().getPathName();
                        }
                        
                    }
                    try {
                        
                        pictureTrucksID = pictureTrucksID.substring(1, pictureTrucksID.length());
                        pictureTrucksPATH = pictureTrucksPATH.substring(1, pictureTrucksPATH.length());
                    } catch (Exception e) {
                        
                    }try {
                        
                        pictureInsurancesID = pictureInsurancesID.substring(1, pictureInsurancesID.length());
                        pictureInsurancesPATH = pictureInsurancesPATH.substring(1, pictureInsurancesPATH.length());
                        
                    } catch (Exception e) {
                    }
                    res = truck.getTruckID()+";"
                        +truck.getTruckNumber()+";"
                        +pictureTrucksPATH+";"
                        +truck.getAvailable()+";"
                        +truck.getCreationDate().getTime()+";"
                        +truck.getLocationPrice()+";"
                        
                        +truck.getLenghtofbedID().getLenghtofbedID()+";"
                        +truck.getDistance()+";"
                        +truck.getPhoneNumber()+";"
                        +truck.getTruckZipCode()+";"
                        +truck.getDOTNumber()+";"
                        +truck.getGeneralLiability()+";"
                        +truck.getInsuranceLiability()+";"
                        +pictureInsurancesPATH+";"
                            
                        +""+";"
                        +truck.getYear()+";"
                        +""+";"
                        +""+";"
                        +""+";"
                        +truck.getTruckaxleID().getTruckaxleID()+";"
                        +driverInfo+";"
                        +pictureTrucksID+";"
                        +pictureInsurancesID+";"
                        +truckBooking.getTruckbookingID()+";"
                        +truckBooking.getValidated()+";"
                        +truckBooking.getRejected()+";"
                        +truckBooking.getTruckOwnerValidation()+";"
                        +truckBooking.getPaid()+";"
                        +truckBooking.getPaymentRecieve()+";"
                        +truckBooking.getSettled()+";"
                        +truckBooking.getBookingPrice()+";"
                        +"null"
                            ;
                    lts.add(res);
                }
                
            } 
            lts.add(""+size);
            result.setObjectList(lts);
        }
        
        return result;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfTruckByBasketIDFrontEnd(int accountID, int basketID, int index, int nombreMaxResultat) throws TruckBookingManagementException{
        List<TruckBooking> lt = null;
        Result result = new Result();
        List<String> lts = new ArrayList<>();
        int size = 0;
        Account account;
        UserBasket basket;
        
        try{
            ges.creatEntityManager();
            
            account = ges.getEntityManager().find(Account.class, accountID);
            
            basket = ges.getEntityManager().find(UserBasket.class, basketID);
            
            if(account == null){
                ges.closeEm();
                result.setMsg("InvalidAccountID");
                return result;
            }
            
            if(basket == null){
                ges.closeEm();
                result.setMsg("InvalidBasketID");
                return result;
            }
            
            Query query =  ges.getEntityManager().createQuery("SELECT DISTINCT t FROM TruckBooking t WHERE t.deleted = false AND t.clientID = :clientID AND t.basketID = :basketID ");

            query.setParameter("clientID", account.getUser());
            query.setParameter("basketID", basket);
            lt = (List<TruckBooking>)query.getResultList();
            size = lt.size();
            
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResultat);
            
            lt = (List<TruckBooking>)query.getResultList();
            result.setMsg("good");
        
        }catch(Throwable th){
            if(ges == null){
                result.setMsg("Could not create Entity Manager\n"+th.getMessage());
                return result;
            }
            result.setMsg(th.getMessage());
        }
        
        
        if(lt != null && !lt.isEmpty()){
            String res = "";
            for(TruckBooking truckBooking: lt){
                
                Truck truck = truckBooking.getTruckID();
                
                if(!truck.getDeleted()){
                    Calendar c = Calendar.getInstance();
                    c.setTime(truck.getTrucktypeID().getYear());
                    
                    String pictureTrucksID = "";
                    String pictureTrucksPATH = "";
                    
                    String pictureInsurancesID = "";
                    String pictureInsurancesPATH = "";
                    
                    List<TruckDocument> truckDocumentList = ges.getEntityManager().createQuery("SELECT t FROM TruckDocument t WHERE t.deleted = FALSE AND t.truckID = :truckID AND t.documentID NOT IN (SELECT d FROM Document d WHERE d.deleted = TRUE) ").setParameter("truckID", truck).getResultList();
                    
                    for(TruckDocument truckDocument: truckDocumentList){
                        if(truckDocument.getType().equals("pictureTruck")) {
                            pictureTrucksID += "#"+truckDocument.getDocumentID().getDocumentID();
                            pictureTrucksPATH += "#"+truckDocument.getDocumentID().getPathName();
                        } else {
                            pictureInsurancesID += "#"+truckDocument.getDocumentID().getDocumentID();
                            pictureInsurancesPATH += "#"+truckDocument.getDocumentID().getPathName();
                        }
                        
                    }
                    try {
                        
                        pictureTrucksID = pictureTrucksID.substring(1, pictureTrucksID.length());
                        pictureTrucksPATH = pictureTrucksPATH.substring(1, pictureTrucksPATH.length());
                    } catch (Exception e) {
                        
                    }try {
                        
                        pictureInsurancesID = pictureInsurancesID.substring(1, pictureInsurancesID.length());
                        pictureInsurancesPATH = pictureInsurancesPATH.substring(1, pictureInsurancesPATH.length());
                        
                    } catch (Exception e) {
                    }
                    res = truck.getTruckID()+";"
                        +truck.getTruckNumber()+";"
                        +pictureTrucksPATH+";"
                        +truck.getAvailable()+";"
                        +truck.getCreationDate().getTime()+";"
                        +truck.getLocationPrice()+";"
                        
                        +truck.getLenghtofbedID().getLenghtofbedID()+";"
                        +truck.getDistance()+";"
                        +truck.getPhoneNumber()+";"
                        +truck.getTruckZipCode()+";"
                        +truck.getDOTNumber()+";"
                        +truck.getGeneralLiability()+";"
                        +truck.getInsuranceLiability()+";"
                        +pictureInsurancesPATH+";"
                        +truck.getYear()+";"
                        +truck.getTruckaxleID().getTruckaxleID()+";"
                        +pictureTrucksID+";"
                        +pictureInsurancesID+";"
                        +truckBooking.getTruckbookingID()+";"
                        +truckBooking.getValidated()+";"
                        +truckBooking.getRejected()+";"
                        +truckBooking.getTruckOwnerValidation()+";"
                        +truckBooking.getPaid()+";"
                        +truckBooking.getPaymentRecieve()+";"
                        +truckBooking.getSettled()+";"
                        +truckBooking.getBookingPrice()+";"
                            ;
                    lts.add(res);
                }
                
            } 
            lts.add(""+size);
            result.setObjectList(lts);
        }
        
        return result;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getBookingDateByID(int truckBookingID) throws TruckBookingManagementException{
        
        Result result = new Result();
        String res = "";
        TruckBooking truckBooking = null;
        
        try{
            ges.creatEntityManager();
            
            truckBooking = ges.getEntityManager().find(TruckBooking.class, truckBookingID);
            
            if(truckBooking == null){
                ges.closeEm();
                result.setMsg("InvalidTruckBookingID");
                return result;
            }
            result.setMsg("good");
        
        }catch(Throwable th){
            if(ges == null){
                result.setMsg("Could not create Entity Manager\n"+th.getMessage());
                return result;
            }
            result.setMsg(th.getMessage());
        }
        
        res = truckBooking.getTruckbookingID()+";"
                +truckBooking.getBookingDate()+";"
                ;
        result.setObject(res);
            
        
        return result;
    }
    
    
    @Override
    public String editSubmitReview(int truckBookingID, int submitReview) throws TruckBookingManagementException{
        
        String res = "";
        TruckBooking truckBooking = null;
        boolean firstReview;
        
        try{
            
            ges.creatEntityManager();
            truckBooking = (TruckBooking)ges.getEntityManager().find(TruckBooking.class, truckBookingID);
            
            if(truckBooking == null){
                ges.closeEm();
                return "InvalidJobResponseID\n";
            }
            
            firstReview = (truckBooking.getSubmitReview() == 0);
            int lastReview = truckBooking.getSubmitReview();
            
            truckBooking.setSubmitReview(submitReview);
            ges.getEntityManager().merge(truckBooking);
            
            Truck truck = truckBooking.getTruckID();
            ges.getEntityManager().merge(truck);
            ges.closeEm();
            
            // Mise a jour du rating du truck
            tbf = new TruckBookingFunction();
            float rate = 0;
            int numberOfRate = 0;
            
            if (firstReview) {
                numberOfRate = truck.getNumberOfRate();
                rate = (truck.getRate() * numberOfRate + truckBooking.getSubmitReview() )/(numberOfRate + 1);
                truck.setNumberOfRate(numberOfRate + 1);
                truck.setRate(rate);
            } else {
                numberOfRate = truck.getNumberOfRate();
                rate = (truck.getRate() * numberOfRate + truckBooking.getSubmitReview() - lastReview)/(numberOfRate);
                truck.setRate(rate);
            }
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editSubmitReview: Mise a jour du rating du truck en BD");
            truck.setRate(rate);
            ges.getEntityManager().merge(truck);
            ges.closeEm();
            /*
            User user = truckBooking.getTruckID().getUserID();
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editSubmitReview: Evaluation du rating du truckOwner  ...");
            float rate2 = tbf.evaluateTruckOwnerRate(user);
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editSubmitReview: Mise a jour du rating du truckOwner en BD");
            user.setRate(rate2);
            
            ges.getEntityManager().merge(user);
            ges.closeEm();
            */
            res = "good";
            
        }catch(Throwable th){
            res = th.getMessage();
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editSubmitReview: Error: " + res);
            throw new TruckBookingManagementException(getClass()+"","editSubmitReview",1,res,res);
        
        }
        
        return res;
    }
    
    
    
    @Override
    public String editReviewComment(int truckBookingID, String reviewComment) throws TruckBookingManagementException{
        
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editReviewComment: truckBookingID  = " + truckBookingID + " ---> reviewComment = " + reviewComment);
        String res = "";
        TruckBooking truckBooking = null;
        try{
            
            ges.creatEntityManager();
            truckBooking = (TruckBooking)ges.getEntityManager().find(TruckBooking.class, truckBookingID);
            
            if(truckBooking == null){
                ges.closeEm();
                return "InvalidTruckBookingID\n";
            }
            
            truckBooking.setReviewComment(reviewComment);
            ges.getEntityManager().merge(truckBooking);
            ges.closeEm();
            
            res = "good";
            
        }catch(Throwable th){
            System.err.println("editReviewComment: Error  = " + th.getMessage());
            res = th.getMessage();
             throw new TruckBookingManagementException(getClass()+"","editReviewComment",1,res,res);
        
        }
        
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editReviewComment: res  = " + res);
        return res;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfTruckOwnerReviewComment(int truckID, int index, int nombreMaxResult)throws TruckBookingManagementException{
        
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getARangeOfTruckOwnerReviewComment: truckID  = " + truckID + " ---> index = " + index + " ---> nombreMaxResult = " + nombreMaxResult);
        Result result = new Result();        
        int numberOfElts = 0;
        List<String> listResult = new ArrayList<>();
        Truck truck;
        Iterator resultat = null;
        
        result.setMsg("good");
        try{
            
            ges.creatEntityManager();
            truck = (Truck)ges.getEntityManager().find(Truck.class, truckID);
            
            if(truck == null){
                result.setMsg("InvaLidJobID");
                return result;
            }
            
            Query query;
            
            query =  ges.getEntityManager().createQuery("SELECT a.submitReview, a.reviewComment FROM  TruckBooking a WHERE a.deleted = FALSE AND a.truckID.userID = :userID AND a.submitReview <> 0");
            
            query.setParameter("userID", truck.getUserID());
            
            numberOfElts = query.getResultList().size();
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResult);
            
            resultat = query.getResultList().iterator();
            
        }catch(Throwable th){
            System.err.println("getARangeOfTruckOwnerReviewComment: Error  = " + th.getMessage());
            result.setMsg(th.getMessage());
        }
        if (resultat != null) {
            
            while (resultat.hasNext()) {
                Object[] tuple = (Object[]) resultat.next();
                int submitReview = (int) tuple[0];
                String reviewComment = (String) tuple[1];
                
                listResult.add(submitReview + "###" + reviewComment);
            }
            
        }
        
        listResult.add("" + numberOfElts);
        result.setObjectList(listResult);
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getARangeOfTruckOwnerReviewComment: listResult  = " + listResult);
        return result;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfTruckBook(int accountID, int index, int nombreMaxResultat) throws TruckBookingManagementException{
        List<TruckBooking> lt = null;
        Result result = new Result();
        List<String> lts = new ArrayList<>();
        int size = 0;
        Account account;
        
        try{
            ges.creatEntityManager();
            
            account = ges.getEntityManager().find(Account.class, accountID);
            
            if(account == null){
                ges.closeEm();
                result.setMsg("InvalidAccountID");
                return result;
            }
            
            Query query =  ges.getEntityManager().createQuery("SELECT DISTINCT t FROM TruckBooking t WHERE t.deleted = false AND t.truckOwnerValidation = 1 AND t.clientValidation = 1 AND t.validated = true AND t.clientID = :clientID AND t.rejected = FALSE AND t.basketID IN (SELECT b FROM UserBasket b WHERE b.deleted = FALSE AND b.complet = TRUE ) ORDER BY t.creationDate DESC");

            query.setParameter("clientID", account.getUser());
            lt = (List<TruckBooking>)query.getResultList();
            size = lt.size();
            
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResultat);
            
            lt = (List<TruckBooking>)query.getResultList();
            result.setMsg("good");
        
        }catch(Throwable th){
            if(ges == null){
                result.setMsg("Could not create Entity Manager\n"+th.getMessage());
                return result;
            }
            result.setMsg(th.getMessage());
        }
        
        
        if(lt != null && !lt.isEmpty()){
            String res = "";
            for(TruckBooking truckBooking: lt){
                
                Truck truck = truckBooking.getTruckID();
                if(!truck.getDeleted()){
                    Calendar c = Calendar.getInstance();
                    c.setTime(truck.getTrucktypeID().getYear());
                    
                    String driverInfo = DriverFunction.getDriverForTruck(truck);
                    
                    String pictureTrucksID = "";
                    String pictureTrucksPATH = "";
                    
                    String pictureInsurancesID = "";
                    String pictureInsurancesPATH = "";
                    
                    List<TruckDocument> truckDocumentList = ges.getEntityManager().createQuery("SELECT t FROM TruckDocument t WHERE t.deleted = FALSE AND t.truckID = :truckID AND t.documentID NOT IN (SELECT d FROM Document d WHERE d.deleted = TRUE) ").setParameter("truckID", truck).getResultList();
                    
                    for(TruckDocument truckDocument: truckDocumentList){
                        if(truckDocument.getType().equals("pictureTruck")) {
                            pictureTrucksID += "#"+truckDocument.getDocumentID().getDocumentID();
                            pictureTrucksPATH += "#"+truckDocument.getDocumentID().getPathName();
                        } else {
                            pictureInsurancesID += "#"+truckDocument.getDocumentID().getDocumentID();
                            pictureInsurancesPATH += "#"+truckDocument.getDocumentID().getPathName();
                        }
                        
                    }
                    try {
                        
                        pictureTrucksID = pictureTrucksID.substring(1, pictureTrucksID.length());
                        pictureTrucksPATH = pictureTrucksPATH.substring(1, pictureTrucksPATH.length());
                    } catch (Exception e) {
                        
                    }try {
                        
                        pictureInsurancesID = pictureInsurancesID.substring(1, pictureInsurancesID.length());
                        pictureInsurancesPATH = pictureInsurancesPATH.substring(1, pictureInsurancesPATH.length());
                        
                    } catch (Exception e) {
                    }
                    res = truck.getTruckID()+";"
                        +truck.getTruckNumber()+";"
                        +pictureTrucksPATH+";"
                        +truck.getAvailable()+";"
                        +truck.getCreationDate().getTime()+";"
                        +truck.getLocationPrice()+";"
                        
                        +truck.getLenghtofbedID().getLenghtofbedID()+";"
                        +truck.getDistance()+";"
                        +truck.getPhoneNumber()+";"
                        +truck.getTruckZipCode()+";"
                        +truck.getDOTNumber()+";"
                        +truck.getGeneralLiability()+";"
                        +truck.getInsuranceLiability()+";"
                        +pictureInsurancesPATH+";"
                            
                        +truckBooking.getBasketID().getBasketLabel()+";"
                        +truck.getYear()+";"
                        +truckBooking.getCreationDate().getTime()+";"
                        +truckBooking.getTruckbookingID()+";"
                        +truckBooking.getSubmitReview()+";"
                        +truck.getTruckaxleID().getTruckaxleID()+";"
                        +driverInfo+";"
                        +pictureTrucksID+";"
                        +pictureInsurancesID+";"
                        +truckBooking.getReviewComment()+";"
                        +truckBooking.getPaid()+";"
                        +truckBooking.getPaymentRecieve()+";"
                        +truckBooking.getSettled()+";"
                        +truckBooking.getBookingPrice()+";"
                        +"null"
                            ;
                    lts.add(res);
                }
                
            } 
            lts.add(""+size);
            result.setObjectList(lts);
        }
        
        return result;
    }
    
    
    @Override
    public String editBookingPrice(int truckBookingID, float bookingPrice) throws TruckBookingManagementException{
        String res = "";
        
        TruckBooking truckBooking;
        
        try{
            
            ges.creatEntityManager();
            
            truckBooking = (TruckBooking)ges.getEntityManager().find(TruckBooking.class, truckBookingID);
            
            if(truckBooking == null){
                ges.closeEm();
                return "NotExistingTruckBooking\n";
            }
            
            truckBooking.setBookingPrice(bookingPrice);
            
            ges.getEntityManager().merge(truckBooking);
            ges.closeEm();
            
            res = "good;"+truckBooking.getTruckbookingID();
            
        }catch(Throwable th){
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
             throw new TruckBookingManagementException(getClass()+"","editBookingPrice",1,res,res);
        
            //return res;
        }
        return res;
    }

    @Override
    public String contactTruckOwner(String message, int truckID, String subject, String name, String email)throws TruckBookingManagementException{
        ges.creatEntityManager();
        String res;
        Truck truck = null;
        User user = null;
        
        try{
            truck = (Truck)ges.getEntityManager().find(Truck.class, truckID);
            
            if(truck == null)
                return "InvalidTruckID";

            Query query =  ges.getEntityManager().createQuery("select t.userID from Truck t where t.truckID=:truckID");
            query.setParameter("truckID", truckID);
            try{
               user = (User)query.getSingleResult();
            }
            catch(Exception e){
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+e.getMessage());
            }
            
            if(user==null)
                return "InvalidTruckOwner";
        }
        catch(Throwable th){
            
        }
        res = "good;"
              +user.getAccountID().getEmail()+";"
              +user.getName()+";"
              +user.getSurname()+";"
              +truck.getTruckNumber()+";"
              +truck.getTruckDescription()+";"
              +truck.getCreationDate().getTime()+";"
                ;
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"Résultat: "+res);
        return res;
    }
    
    @Override
    public String payTruckBookingByClient(int clientID, int truckBookingID)throws TruckBookingManagementException {
        String res = "";
        
        TruckBooking truckBooking = null;
        Account account = null;
        
        try{
            
            ges.creatEntityManager();
            account = (Account)ges.getEntityManager().find(Account.class, clientID);
            
            if(account == null){
                ges.closeEm();
                return "InvalidAccountID";
            }
            
            try {
                
                truckBooking = (TruckBooking) ges.getEntityManager().createQuery("SELECT t FROM TruckBooking t WHERE t.deleted = FALSE AND t.truckbookingID = :truckbookingID").setParameter("truckbookingID", truckBookingID).getSingleResult();
                
            } catch (Exception e) {
            }
            
            if(truckBooking == null){
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"payTruckBookingByClient: return:  NotExistingTruckBooking");
                return "NotExistingTruckBooking";
            }
            
            //logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"payTruckBookingByClient: truckBooking.getPaid: " + truckBooking.getPaid());
            if(truckBooking.getPaid()){
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"payTruckBookingByClient: return:  AlReadyPaid");
                return "AlReadyPaid";
            }
            
            BillingReceiver clientBillingInfo = account.getBillingreceiverID();
            
            String customerID = clientBillingInfo.getApicustumID();// qui est l'id de celui qui paie dans notre plateforme sandbox
            String custCreditCardNum = clientBillingInfo.getCreditCardNumber();
            String custCardCode = clientBillingInfo.getSecurityCode();
            String custCardExpDate = clientBillingInfo.getCreditCardExpiration();
            
            //String paymentCustomerMethodeID = excavatorBillingInfo.getDefaultpaymentmethodID();// qui est une des ids des methodes de paiement du customer enregistre dans notre BD et du le sandbox, cela peut etre un creditCard ou check
            double amount = truckBooking.getBookingPrice();// montant a transferer
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"payWeeklyTicketByExcavator: getTruckownerID " + truckBooking.getTruckownerID() );
            
            BillingReceiver truckOwnerBillingInfo = truckBooking.getTruckownerID().getAccountID().getBillingreceiverID();

            String sellerID = truckOwnerBillingInfo.getApicustumID();// qui est l'id de celui qui recois dans notre plateforme sandbox
            String sellerAccountNumber = truckOwnerBillingInfo.getAccount().getUser().getAccountNumber();
            String sellerRoutingNumber = truckOwnerBillingInfo.getAccount().getUser().getRoutineNumber();
            
            if (truckOwnerBillingInfo.getAccount().getUser().getAccountNumber() == null || truckOwnerBillingInfo.getAccount().getUser().getAccountNumber().equalsIgnoreCase("null")) {
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"payWeeklyTicketByExcavator: return:  accountNumberNotYetDefined");
                return "accountNumberNotYetDefined";
            }

            if (truckOwnerBillingInfo.getAccount().getUser().getRoutineNumber() == null || truckOwnerBillingInfo.getAccount().getUser().getRoutineNumber().equalsIgnoreCase("null")) {
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"payWeeklyTicketByExcavator: return:  routineNumberNotYetDefined");
                return "routineNumberNotYetDefined";
            }
            
            
            //String paymentSellerMethodeID = truckOwnerBillingInfo.getDefaultpaymentmethodID();// qui est une des ids des methodes de paiement du customer enregistre dans notre BD et du le sandbox, cela peut etre un creditCard ou check
            
            tbf = new TruckBookingFunction();
            String truckOwnerName = tbf.getTotalName(truckBooking.getTruckownerID().getName(), truckBooking.getTruckownerID().getSurname());
            String clientName = tbf.getTotalName(account.getUser().getName(), account.getUser().getSurname());
            String truckNumber = truckBooking.getTruckID().getTruckNumber();
            
            String paymentdescription = " Payment for the truck booking of " + truckOwnerName + ", for the truck number " + truckNumber + ", demanded by " + clientName + ", On TRUCKS AND DIRT System"
                    ; // qui est la la raison du paiement.
            
            CredentialInformation creInfo = new CredentialInformation();
            Properties props = null;
            try {
                props = creInfo.loadProperties();
            } catch (IOException ex) {
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"modifyBillingInfo: Error: " + ex.getMessage());
                Logger.getLogger(TruckBookingManagementDao.class.getName()).log(Level.SEVERE, null, ex);
                throw new TruckBookingManagementException(getClass()+"","modifyBillingInfo",1,"NotExistingFileName",ex.getMessage());
        
                
                //return "NotExistingFileName";
            }
            String endPoint = props.getProperty("endPoint");//"sandbox.usaepay.com";
            String sourceKey = props.getProperty("sourceKey");//"Bmy7457mqTfoOZAH9s51204z53Aa2v1Y";
            String sourcePin = props.getProperty("sourcePin");//"1234";
            String clientIpAddress = props.getProperty("clientIpAddress");//"127.0.0.1";
            String period = props.getProperty("period");//"Monthly";
            boolean enablePeriodicPayment = Boolean.parseBoolean(props.getProperty("enablePeriodicPayment"));//true;
            boolean sendReceipt = Boolean.parseBoolean(props.getProperty("sendReceipt"));//true;
            
            PaySimpleAP paySimple = new PaySimpleAP(endPoint, sourceKey, sourcePin, clientIpAddress, period, enablePeriodicPayment, sendReceipt);

            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"payWeeklyTicketByExcavator: customerID = " + customerID + " --- custCreditCardNum = " + custCreditCardNum + " --- custCardCode = " + custCardCode + " --- custCardExpDate = " + custCardExpDate + " --- amount = " + amount + " --- sellerID = " + sellerID + " --- sellerAccountNumber = " + sellerAccountNumber + " --- sellerRoutingNumber = " + sellerRoutingNumber + "    paymentdescription = " + paymentdescription);
            MypaySimpleObj res2 = paySimple.runPayment(customerID, custCreditCardNum, custCardCode, custCardExpDate, amount, sellerID, sellerAccountNumber, sellerRoutingNumber, paymentdescription);

            res = res2.message;
            if (res.equalsIgnoreCase("good")) {

                truckBooking.setPaid(true);
                ges.getEntityManager().merge(truckBooking);
                ges.closeEm();
                res = "good";
                
            }
            
        }catch(Throwable th){
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"payWeeklyTicketByExcavator: Error:  res: " + res);
            throw new TruckBookingManagementException(getClass()+"","modifyBillingInfo",1,res,res);
        
           // return res;
        }
        
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"payWeeklyTicketByExcavator: return:  res: " + res);
        return res;
    }

    @Override
    public String payTruckBookingByTruckOwner(int truckOwnerID, int truckBookingID) throws TruckBookingManagementException{
        String res = "";
        
        TruckBooking truckBooking = null;
        
        try{
            
            ges.creatEntityManager();
            try {
                
                truckBooking = (TruckBooking) ges.getEntityManager().createQuery("SELECT t FROM TruckBooking t WHERE t.deleted = FALSE AND t.truckbookingID = :truckbookingID").setParameter("truckbookingID", truckBookingID).getSingleResult();
                
            } catch (Exception e) {
            }
            
            if(truckBooking == null){
                ges.closeEm();
                return "NotExistingTruckBooking\n";
            }
            
            truckBooking.setPaymentRecieve(true);
            truckBooking.setSettled(true);
            
            ges.getEntityManager().merge(truckBooking);
            ges.closeEm();
            
            res = "good;"+truckBooking.getTruckbookingID();
            
        }catch(Throwable th){

            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
             throw new TruckBookingManagementException(getClass()+"","payTruckBookingByTruckOwner",1,res,res);
        
           // return res;
        }

        return res;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfTruckBookHistory(int accountID, int index, int nombreMaxResultat) throws TruckBookingManagementException{
        List<TruckBooking> lt = null;
        Result result = new Result();
        List<String> lts = new ArrayList<>();
        int size = 0;
        Account account;
        
        try{
            ges.creatEntityManager();
            
            account = ges.getEntityManager().find(Account.class, accountID);
            
            if(account == null){
                ges.closeEm();
                result.setMsg("InvalidAccountID");
                return result;
            }
            
            Query query =  ges.getEntityManager().createQuery("SELECT DISTINCT t FROM TruckBooking t WHERE t.deleted = false AND t.truckOwnerValidation = 1 AND t.clientValidation = 1 AND t.validated = true AND t.truckownerID = :truckownerID AND t.rejected = FALSE AND t.basketID IN (SELECT b FROM UserBasket b WHERE b.deleted = FALSE AND b.complet = TRUE ) ORDER BY t.creationDate DESC");

            query.setParameter("truckownerID", account.getUser());
            lt = (List<TruckBooking>)query.getResultList();
            size = lt.size();
            
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResultat);
            
            lt = (List<TruckBooking>)query.getResultList();
            result.setMsg("good");
        
        }catch(Throwable th){
            if(ges == null){
                result.setMsg("Could not create Entity Manager\n"+th.getMessage());
                return result;
            }
            result.setMsg(th.getMessage());
        }
        
        
        if(lt != null && !lt.isEmpty()){
            String res = "";
            for(TruckBooking truckBooking: lt){
                
                Truck truck = truckBooking.getTruckID();
                if (!truck.getDeleted()) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(truck.getTrucktypeID().getYear());

                    String driverInfo = DriverFunction.getDriverForTruck(truck);
                    
                    String pictureTrucksID = "";
                    String pictureTrucksPATH = "";
                    
                    String pictureInsurancesID = "";
                    String pictureInsurancesPATH = "";
                    
                    List<TruckDocument> truckDocumentList = ges.getEntityManager().createQuery("SELECT t FROM TruckDocument t WHERE t.deleted = FALSE AND t.truckID = :truckID AND t.documentID NOT IN (SELECT d FROM Document d WHERE d.deleted = TRUE) ").setParameter("truckID", truck).getResultList();
                    
                    for(TruckDocument truckDocument: truckDocumentList){
                        if(truckDocument.getType().equals("pictureTruck")) {
                            pictureTrucksID += "#"+truckDocument.getDocumentID().getDocumentID();
                            pictureTrucksPATH += "#"+truckDocument.getDocumentID().getPathName();
                        } else {
                            pictureInsurancesID += "#"+truckDocument.getDocumentID().getDocumentID();
                            pictureInsurancesPATH += "#"+truckDocument.getDocumentID().getPathName();
                        }
                        
                    }
                    try {
                        
                        pictureTrucksID = pictureTrucksID.substring(1, pictureTrucksID.length());
                        pictureTrucksPATH = pictureTrucksPATH.substring(1, pictureTrucksPATH.length());
                    } catch (Exception e) {
                        
                    }try {
                        
                        pictureInsurancesID = pictureInsurancesID.substring(1, pictureInsurancesID.length());
                        pictureInsurancesPATH = pictureInsurancesPATH.substring(1, pictureInsurancesPATH.length());
                        
                    } catch (Exception e) {
                    }
                    res = truck.getTruckID()+";"
                        +truck.getTruckNumber()+";"
                        +pictureTrucksPATH+";"
                        +truck.getAvailable()+";"
                        +truck.getCreationDate().getTime()+";"
                        +truck.getLocationPrice()+";"
                        
                        +truck.getLenghtofbedID().getLenghtofbedID()+";"
                        +truck.getDistance()+";"
                        +truck.getPhoneNumber()+";"
                        +truck.getTruckZipCode()+";"
                        +truck.getDOTNumber()+";"
                        +truck.getGeneralLiability()+";"
                        +truck.getInsuranceLiability()+";"
                        +pictureInsurancesPATH+";"
                            
                        +truckBooking.getBasketID().getBasketLabel()+";"
                        +truck.getYear()+";"
                        +truckBooking.getCreationDate().getTime()+";"
                        +truckBooking.getTruckbookingID()+";"
                        +truckBooking.getSubmitReview()+";"
                        +truck.getTruckaxleID().getTruckaxleID()+";"
                        +driverInfo+";"
                        +pictureTrucksID+";"
                        +pictureInsurancesID+";"
                        +truckBooking.getReviewComment()+";"
                        +truckBooking.getPaid()+";"
                        +truckBooking.getPaymentRecieve()+";"
                        +truckBooking.getSettled()+";"
                        +truckBooking.getBookingPrice()+";"
                        +"null"
                            ;
                    lts.add(res);
                }
                
            } 
            lts.add(""+size);
            result.setObjectList(lts);
        }
        
        return result;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfTruckReviewComment(int truckID, int index, int nombreMaxResult)throws TruckBookingManagementException{
        
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getARangeOfTruckReviewComment: truckID  = " + truckID + " ---> index = " + index + " ---> nombreMaxResult = " + nombreMaxResult);
        Result result = new Result();        
        int numberOfElts = 0;
        List<String> listResult = new ArrayList<>();
        Truck truck;
        Iterator resultat = null;
        
        result.setMsg("good");
        try {

            ges.creatEntityManager();
            truck = (Truck) ges.getEntityManager().find(Truck.class, truckID);

            if (truck == null) {
                result.setMsg("InvaLidJobID");
                return result;
            }

            String driverLicenses = "";
            String driverPictures = "";

            List<Driver> truckDriverList = (List<Driver>) ges.getEntityManager().createQuery("SELECT d.driverID FROM Drive d WHERE d.deleted = FALSE AND d.truckID = :truckID").setParameter("truckID", truck).getResultList();
            if (truckDriverList != null && !truckDriverList.isEmpty()) {

                Driver driver = truck.getDriveList().get(0).getDriverID();
                List<DriverDocument> driverDocumentList = ges.getEntityManager().createQuery("SELECT d FROM DriverDocument d WHERE d.deleted = FALSE AND d.driverID = :driverID AND d.documentID NOT IN (SELECT d FROM Document d WHERE d.deleted = TRUE) ").setParameter("driverID", driver).getResultList();

                if (driverDocumentList != null && !driverDocumentList.isEmpty()) {

                    driverLicenses = "";
                    driverPictures = "";

                    for (DriverDocument driverDocument : driverDocumentList) {
                        if (driverDocument.getType().equals("license")) {

                            driverLicenses += "#" + driverDocument.getDocumentID().getPathName();

                        } else if (driverDocument.getType().equals("picture")) {

                            driverPictures += "#" + driverDocument.getDocumentID().getPathName();

                        }
                    }

                    driverLicenses = (driverLicenses.equals("") ? "" : driverLicenses.substring(1, driverLicenses.length()));
                    driverPictures = (driverPictures.equals("") ? "" : driverPictures.substring(1, driverPictures.length()));
                }
            }

            Calendar c = Calendar.getInstance();
            c.setTime(truck.getTrucktypeID().getYear());

            String pictureTrucksID = "";
            String pictureTrucksPATH = "";

            String pictureInsurancesID = "";
            String pictureInsurancesPATH = "";

            List<TruckDocument> truckDocumentList = ges.getEntityManager().createQuery("SELECT t FROM TruckDocument t WHERE t.deleted = FALSE AND t.truckID = :truckID AND t.documentID NOT IN (SELECT d FROM Document d WHERE d.deleted = TRUE) ").setParameter("truckID", truck).getResultList();

            for (TruckDocument truckDocument : truckDocumentList) {
                if (truckDocument.getType().equals("pictureTruck")) {
                    pictureTrucksID += "#" + truckDocument.getDocumentID().getDocumentID();
                    pictureTrucksPATH += "#" + truckDocument.getDocumentID().getPathName();
                } else {
                    pictureInsurancesID += "#" + truckDocument.getDocumentID().getDocumentID();
                    pictureInsurancesPATH += "#" + truckDocument.getDocumentID().getPathName();
                }

            }
            try {

                pictureTrucksID = pictureTrucksID.substring(1, pictureTrucksID.length());
                pictureTrucksPATH = pictureTrucksPATH.substring(1, pictureTrucksPATH.length());
            } catch (Exception e) {

            }
            try {

                pictureInsurancesID = pictureInsurancesID.substring(1, pictureInsurancesID.length());
                pictureInsurancesPATH = pictureInsurancesPATH.substring(1, pictureInsurancesPATH.length());

            } catch (Exception e) {
            }
            String res = truck.getTruckID() + ";"
                    + truck.getTruckNumber() + ";"
                    + pictureTrucksPATH + ";"
                    + truck.getAvailable() + ";"
                    + truck.getCreationDate().getTime() + ";"
                    + truck.getLocationPrice() + ";"
                    + truck.getLenghtofbedID().getLenghtofbedID() + ";"
                    + truck.getDistance() + ";"
                    + truck.getPhoneNumber() + ";"
                    + truck.getTruckZipCode() + ";"
                    + truck.getDOTNumber() + ";"
                    + truck.getGeneralLiability() + ";"
                    + truck.getInsuranceLiability() + ";"
                    + pictureInsurancesPATH + ";"
                    + truck.getTruckaxleID().getTruckaxleID() + ";"
                    + pictureTrucksID + ";"
                    + pictureInsurancesID + ";"
                    + truck.getYear() + ";"
                    + truck.getTruckDescription() + ";"
                    + truck.getNumberOfRate() + ";"
                    + truck.getRate() + ";"
                    + driverLicenses+ ";"
                    + driverPictures + ";"
                    + "null";
            listResult.add(res);

            Query query;

            query = ges.getEntityManager().createQuery("SELECT a.submitReview, a.reviewComment FROM  TruckBooking a WHERE a.deleted = FALSE AND a.truckID = :truckID AND a.submitReview <> 0");

            query.setParameter("truckID", truck);

            numberOfElts = query.getResultList().size();
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResult);

            resultat = query.getResultList().iterator();

        } catch (Throwable th) {
            System.err.println("getARangeOfTruckReviewComment: Error  = " + th.getMessage());
            result.setMsg(th.getMessage());
        }
        if (resultat != null) {
            
            while (resultat.hasNext()) {
                Object[] tuple = (Object[]) resultat.next();
                int submitReview = (int) tuple[0];
                String reviewComment = (String) tuple[1];
                
                listResult.add(submitReview + "###" + reviewComment);
            }
            
        }
        
        listResult.add("" + numberOfElts);
        result.setObjectList(listResult);
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getARangeOfTruckReviewComment: listResult  = " + listResult);
        return result;
    }
    
    
}
