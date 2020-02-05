                                   /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package availabilityManagement.dao;

import entities.Account;
import entities.Availability;
import entities.Truck;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import toolsAndTransversalFunctionnalities.GestionnaireEntite;
import toolsAndTransversalFunctionnalities.Result;
import util.date.DateFunction;
import util.exception.AvaibilityManagementException;

/**
 *
 * @author erman
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class AvaibilityManagementDao implements IAvaibilityManagementDaoRemote, IAvaibilityManagementDaoLocal{

    @EJB
    GestionnaireEntite ges;
    
    AvailabilityFunction af;
    
     final static org.apache.logging.log4j.Logger logger = LogManager.getLogger("dump1");
    
    @Override
    public String addAvailability(List<Long> unavailable_dates, int truckID, int truckOwnerID) throws AvaibilityManagementException{
        
        String res = "good";
        Truck truck = null;
        Account account = null;
        Availability availability = null;
        List<Availability> availabilityList = null;
        String etat = "\nDebut\n";
        
        af = new AvailabilityFunction();
        
        try{
            
            ges.creatEntityManager();
           
            truck = (Truck)ges.getEntityManager().find(Truck.class, truckID);
            account = (Account)ges.getEntityManager().find(Account.class, truckOwnerID);
            
            if(truck == null){
                ges.closeEm();
                return "InvalidTruckID\n";
            }
            
            if(account == null){
                ges.closeEm();
                return "InvalidAccountID\n";
            }
            
            Date aDateOfYear = new Date(unavailable_dates.get(0));
            Date[] datesOfYear = DateFunction.getDateRangeOfYear(aDateOfYear);
            
            //System.out.println("addAvailability <= <= <=: aDateOfYear " + aDateOfYear);
            logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"addAvailability"+"\n"
                            +"info      : "+ "addAvailability <= <= <=: aDateOfYear " + aDateOfYear);
            
            Query query = ges.getEntityManager().createQuery("SELECT d FROM Availability d WHERE d.deleted = FALSE AND d.truckID = :truckID AND d.truckownerID = :truckownerID AND d.availbilityDate BETWEEN :firstDateOfYear AND :lastDateOfYear ")
                    .setParameter("truckID", truck)
                    .setParameter("truckownerID", account.getUser())
                    .setParameter("firstDateOfYear", datesOfYear[0])
                    .setParameter("lastDateOfYear", datesOfYear[1])
                    ;
            availabilityList = query.getResultList();
            
            for (Availability avail : availabilityList) {
                avail.setDeleted(true);
                avail.setState(1);
                ges.getEntityManager().merge(avail);
                ges.closeEm();
            }
            
            //System.out.println("addAvailability <= <= <=:   datesOfYear[0] = " + datesOfYear[0] + "         datesOfYear[1] = " + datesOfYear[1]);
            
            logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"addAvailability"+"\n"
                            +"info      : "+ "addAvailability <= <= <=:   datesOfYear[0] = " + datesOfYear[0] + "         datesOfYear[1] = " + datesOfYear[1]);
            
            for (Long dateLong : unavailable_dates) {
                
                Date date = new Date(dateLong);
                
              //  System.out.println("addAvailability <= <= <=: Date(dateLong) " + date);
                
                logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"addAvailability"+"\n"
                            +"info      : "+ "addAvailability <= <= <=: Date(dateLong) " + date);
            
                availability = null;
                
                query = ges.getEntityManager().createQuery("SELECT s FROM SolicitedTruck s WHERE s.deleted = FALSE AND s.truckID = :truckID AND s.truckAvailable = TRUE AND s.startDate <= :currentDate AND s.endDate >= :currentDate")
                        .setParameter("currentDate", date)
                        .setParameter("truckID", truck)
                        ;
                if (query.getResultList().isEmpty()) {
                    
                    try {
                        
                        availability = (Availability) ges.getEntityManager().createQuery("SELECT a FROM Availability a WHERE a.availbilityDate = :availbilityDate AND a.truckID = :truckID AND a.truckownerID = :truckOwnerID")
                                .setParameter("availbilityDate", date)
                                .setParameter("truckID", truck)
                                .setParameter("truckOwnerID", account.getUser())
                                .getSingleResult();
                        
                    } catch (Throwable th) {
                        logger.error("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"addAvailability"+"\n"
                            +"error      : "+ "--------Error during getting availability\n" + th.getMessage());
            
                        
                       // System.out.println("--------Error during getting availability\n" + th.getMessage());
                    }
                    
                    if (availability != null) {

                        //System.out.println("*******addAvailability1 <= <= <=: availability1 " + availability + "        availability.getDeleted() = " + availability.getDeleted());
                        availability.setDeleted(false);
                        availability.setState(1);
                        ges.getEntityManager().merge(availability);
                        ges.closeEm();
                        //System.out.println("******addAvailability2 <= <= <=: availability2 " + availability + "        availability.getDeleted() = " + availability.getDeleted());
                        res += ";existingAv=" + availability.getAvailabilityID();
                    } else {
                        
                        availability = null;
                        etat += "Fin de verification des infos\n";
                        availability = new Availability();
                        
                        availability.setAvailbilityDate(date);
                        availability.setCreationDate(DateFunction.getGMTDate());
                        availability.setDeleted(false);
                        availability.setState(1);
                        availability.setTruckID(truck);
                        availability.setTruckownerID(account.getUser());
                        ges.getEntityManager().persist(availability);
                        
                        etat += "Fin de persistance des infos du availability\n";
                        truck.getAvailabilityList().add(availability);
                        account.getUser().getAvailabilityList().add(availability);
                        etat += "Phase de setting\n";
                        
                        ges.getEntityManager().merge(truck);
                        etat += "Fin de mise a jour du truck\n";
                        
                        ges.getEntityManager().merge(account);
                        etat += "Fin de mise a jour du account\n";
                        
                        ges.getEntityManager().merge(account.getUser());
                        etat += "Fin de mise a jour du user\n";
                        ges.closeEm();
                        res += ";" + availability.getAvailabilityID();
                    }
                } else {
                    res += ";" + dateLong;
                }
            }
        }catch(Throwable e){
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
             throw new AvaibilityManagementException(getClass()+"","addAvailability",1,res,res);
           
            //return etat+e.getMessage()+"\n"+res;
        }
        return res;
    }

    @Override
    public String editAvailability(int availabilityID, String day, String timeRange, int accountID, int truckID) throws AvaibilityManagementException{
        
        String res = "good";
        Truck truck = null;
        Account account = null;
        Availability availability = null;
        Availability availability2 = null;
       
        try{
            
            ges.creatEntityManager();
            
            availability = (Availability)ges.getEntityManager().find(Availability.class, availabilityID);
                
            account = (Account)ges.getEntityManager().find(Account.class, accountID);
                
            truck = (Truck)ges.getEntityManager().find(Truck.class, truckID);
                
           
            
            if(availability == null){
                ges.closeEm();
                return "InvalidAvailabilityID\n";
            }
            
            if(truck == null){
                ges.closeEm();
                return "InvalidTruckID\n";
            }
            
            if(account == null){
                ges.closeEm();
                return "InvalidAccountID\n";
            }
            
            try{
                availability2 = (Availability)ges.getEntityManager().createQuery("SELECT a FROM Availability a WHERE a.deleted = FALSE AND "
                        + "a.timeRange = :timeRange AND a.day = :day AND a.truckID = :truckID").setParameter("timeRange"
                                + "", timeRange).setParameter("day", day).setParameter("truckID", truck).getSingleResult();
            }catch(Throwable th){ }
            
            if(availability2 != null && !availability2.equals(availability)){
                ges.closeEm();
                return "availabiltyAllReadyExist\n";
            }
            
            availability.setDeleted(false);
            availability.setTruckID(truck);
            availability.setTruckownerID(account.getUser());
            ges.getEntityManager().merge(availability);
            ges.closeEm();
            
            truck.getAvailabilityList().add(availability);
            account.getUser().getAvailabilityList().add(availability);
            
            ges.getEntityManager().merge(truck);
            ges.getEntityManager().merge(account);
            ges.getEntityManager().merge(account.getUser());
            
            ges.closeEm();
            
        
        }catch(Throwable th){
            throw new AvaibilityManagementException(getClass()+"","editAvailability",1,th.getMessage(),res);
            //return th.getMessage();
        }
        return res;
    }

    @Override
    public String deleteAvailability(int availabilityID) throws AvaibilityManagementException{
        
        String res = "good";
        Availability availability = null;
       
        try{
            ges.creatEntityManager();
           
            try{
                availability = (Availability)ges.getEntityManager().find(Availability.class, availabilityID);
            }catch(Throwable th){ }
            
            if(availability == null){
                ges.closeEm();
                return "InvalidAvailabilityID\n";
            }
            
            availability.setDeleted(true);
            ges.getEntityManager().merge(availability);
            ges.closeEm();
            
        }catch(Throwable th){
            throw new AvaibilityManagementException(getClass()+"","deleteAvailability",1,th.getMessage(),res);
            
            //return th.getMessage();
        }
        return res;
    }

    @Override
    public String deleteAvailability(int truckID, String yearh) throws AvaibilityManagementException{
        
        //System.out.println(" deletedAvailability: truckID = " + truckID + "  And   year = " + yearh);
        
        logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"deleteAvailability"+"\n"
                            +"info      : "+ " deletedAvailability: truckID = " + truckID + "  And   year = " + yearh);
            
        String res = "good";
        Availability availability = null;
        Truck truck = null;
       
        
        af = new AvailabilityFunction();
        Date year = af.parseYearToDate(yearh);
        
        try{
            ges.creatEntityManager();
            
            truck = (Truck)ges.getEntityManager().find(Truck.class, truckID);
              
           // System.out.println(" deletedAvailability: truck = " + truck);
            
              logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"deleteAvailability"+"\n"
                            +"info      : "+ " deletedAvailability: truck = " + truck);
            
              
            if(truck == null){
                ges.closeEm();
                return "InvalidTruckID\n";
            }
            try {

                Date[] datesOfYear = DateFunction.getDateRangeOfYear(year);
                //availability = (Availability)ges.getEntityManager().createQuery("SELECT a FROM Availability a WHERE a.deleted = FALSE AND a.truckID = :truckID AND a.availbilityDate = :year")
                int numberOfRow = ges.getEntityManager().createQuery("UPDATE Availability a SET a.deleted = TRUE WHERE a.deleted = FALSE AND a.truckID = :truckID AND a.availbilityDate BETWEEN :firstDateOfYear AND :lastDateOfYear ")
                        .setParameter("truckID", truck)
                        .setParameter("firstDateOfYear", datesOfYear[0])
                        .setParameter("lastDateOfYear", datesOfYear[1])
                        .executeUpdate();
                
                 logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"deleteAvailability"+"\n"
                            +"info      : "+ "********Le nombre de ligne supprimé est numberOfRow  =  " + numberOfRow);
            
              
               // System.out.println("********Le nombre de ligne supprimé est numberOfRow  =  " + numberOfRow);
            } catch (Exception e) {
                StringWriter string = new StringWriter();
                PrintWriter str = new PrintWriter(string);
                e.printStackTrace(str);
                res = string.toString();
                 throw new AvaibilityManagementException(getClass()+"","deleteAvailability",1,res,res);
            
               // return res;
            }

            //System.out.println(" deletedAvailability: availability = " + availability);

            logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"deleteAvailability"+"\n"
                            +"info      : "+" deletedAvailability: availability = " + availability);
            
              
            if (availability == null) {
                //ges.closeEm();
                return "NotExistingAvailability\n";
            }
            availability.setDeleted(true);
            ges.getEntityManager().merge(availability);
            ges.closeEm();

        } catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            throw new AvaibilityManagementException(getClass()+"","deleteAvailability",1,res,res);
            
           // return res;
        }
        return res;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getAvailabilityByID(int availabilityID) throws AvaibilityManagementException{

        String res = "";
        Result result = new Result();
        Availability availability = null;
        result.setMsg("good");
        try{
            
            ges.creatEntityManager();
            availability = (Availability)ges.getEntityManager().find(Availability.class, availabilityID);
            
        }catch(Throwable th){
            result.setMsg(th.getMessage());
        }
        
        if(availability != null){
            
            Calendar c = Calendar.getInstance();
            c.setTime(availability.getAvailbilityDate());
            res = ""+availability.getAvailabilityID()+";"
                    +c.getTime().getTime()+";"
                    +c.get(Calendar.YEAR)+";"
                    //+availability.getTimeRange()+";"
                    +availability.getTruckID().getTruckID()+";"
                    +availability.getTruckownerID().getUserID();
            
            result.setObject(res);
        }else{
            result.setObject("null");
        }
        return result;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfAvailabilityByTruckID(int truckID, int index, int nombreMaxResultat) throws AvaibilityManagementException{
          
        List<Availability> availabilityList = null;
        Result result = new Result();
        List<String> listResult = null;
        int numberOfElts = 0;
        
        try{
            
            ges.creatEntityManager();
            
            Query query =  ges.getEntityManager().createQuery("SELECT a FROM Availability a WHERE a.deleted = false AND "
                    + "a.truckID IN (SELECT t FROM Truck t WHERE t.deleted = FALSE AND t.truckID = :truckID)");
            
            query.setParameter("truckID", truckID);
            
            availabilityList = (List<Availability>)query.getResultList();
            numberOfElts = availabilityList.size();
            
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResultat);
            
            availabilityList = (List<Availability>)query.getResultList();
            result.setMsg("good");
        
        }catch(Throwable th){
                StringWriter string = new StringWriter();
                PrintWriter str = new PrintWriter(string);
                th.printStackTrace(str);
                result.setMsg(th.getMessage()+"\n"+string.toString());
                
                 logger.error("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"getARangeOfAvailabilityByTruckID"+"\n"
                            +"erreur      : "+result.getMsg());
            
              
               // System.out.println(""+result.getMsg());
                
                
                // throw new AvaibilityManagementException(getClass()+"","deleteAvailability",1,th.getMessage(),res);
            
                return result;
        }
        
        
        if(availabilityList != null && !availabilityList.isEmpty()){
            String res = "";
            listResult = new ArrayList<>();
            for (Availability availability : availabilityList) {

                Calendar c = Calendar.getInstance();
                c.setTime(availability.getAvailbilityDate());
                res = "" + availability.getAvailabilityID() + ";"
                        + c.getTime().getTime() + ";"
                        + c.get(Calendar.YEAR) + ";"
                        + availability.getTruckID().getTruckID() + ";"
                        + availability.getTruckownerID().getUserID();

                listResult.add(res);
            }
            listResult.add("" + numberOfElts);
            result.setObjectList(listResult);
        }
        
        return result;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfAvailabilityByTruckOwnerID(int truckOwnerID, String yearh, int truckID) throws AvaibilityManagementException{
          
        List<Availability> availabilityList = null;
        Result result = new Result();
        List<String> listResult = null;
        int numberOfElts = 0;
        Account account = null;
        Truck truck = null;
        
        af = new AvailabilityFunction();
        Date year = af.parseYearToDate(yearh);
        
        try{
            
            ges.creatEntityManager();
            truck = (Truck)ges.getEntityManager().find(Truck.class, truckID);
            
        }catch(Throwable th){
            result.setMsg(th.getMessage());
        }
        
        try{
            
            ges.creatEntityManager();
            account = (Account)ges.getEntityManager().find(Account.class, truckOwnerID);
            
        }catch(Throwable th){
            result.setMsg(th.getMessage());
        }
        
        if(account==null){
            result.setMsg("InvalidTruckOwnerID");
            ges.closeEm();
            return result;
        }
        
        if(truck==null){
            result.setMsg("InvalidTruckID");
            ges.closeEm();
            return result;
        }
        
            try{
                Query query =  ges.getEntityManager().createQuery("SELECT a FROM Availability a WHERE a.deleted = false AND a.availbilityDate = :year AND a.truckownerID  IN (SELECT u FROM User u WHERE u.accountID =:truckOwnerID) AND a.truckID IN (SELECT t FROM Truck t where t.truckID=:truckID)");

                query.setParameter("truckOwnerID", account);
                query.setParameter("truckID", truckID);
                query.setParameter("year", year);

                availabilityList = (List<Availability>)query.getResultList();
                numberOfElts = availabilityList.size();


                availabilityList = (List<Availability>)query.getResultList();
                result.setMsg("good");
                ges.closeEm();

            }catch(Throwable th){
                StringWriter string = new StringWriter();
                PrintWriter str = new PrintWriter(string);
                th.printStackTrace(str);
                
                 logger.error("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"getARangeOfAvailabilityByTruckID"+"\n"
                            +"erreur      : "+th.getMessage()+"\n"+string.toString());
            
                 
               // System.out.println("Error \n"+th.getMessage()+"\n"+string.toString());
            result.setMsg(th.getMessage() + "\n" + string.toString());
        }

        if (availabilityList != null && !availabilityList.isEmpty()) {
            String res = "";
            listResult = new ArrayList<>();
            for (Availability availability : availabilityList) {

                Calendar c = Calendar.getInstance();
                c.setTime(availability.getAvailbilityDate());
                res = "" + availability.getAvailabilityID() + ";"
                        + c.getTime().getTime() + ";"
                        + c.get(Calendar.YEAR) + ";"
                        + availability.getTruckID().getTruckID() + ";"
                        + availability.getTruckownerID().getUserID();

                listResult.add(res);
            }
            listResult.add("" + numberOfElts);
            result.setObjectList(listResult);
        }

        return result;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public String verifyAddress(String address) throws AvaibilityManagementException{
        String result= "";
        List<Truck> trucks;
        if (address!=null){
            ges.creatEntityManager();
            Query query =  ges.getEntityManager().createQuery("SELECT t FROM Truck t WHERE t.deleted = FALSE AND t.truckZipCode LIKE :address" );
            
            query.setParameter("address", "%"+address+"%");
            trucks = query.getResultList();
            if(trucks.isEmpty()){
                result = "bad";
            }
            else
                result= "good";
         }
        return result;
    }

    //Recherche toutes les dates d'invalidités d'un truck pour une année
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getAllUnAvailabilityTruckDateByYear(int truckID, int year) throws AvaibilityManagementException{
        
        List<Date> availabilityList = null;
        Iterator solicitedTruckList = null;
        //List<TruckBooking> truckBookingList = null;
        
        List<String> listResult = null;

        Result result = new Result();
        Truck truck = null;
        Date currentDate = DateFunction.getADateForYear(year);
        Date firstDateOfYear = DateFunction.getDateRangeOfYear(currentDate)[0];
        Date endDateOfYear = DateFunction.getDateRangeOfYear(currentDate)[1];
        //System.out.println("getAllUnAvailabilityTruckDateByYear: firstDateOfYear = " + firstDateOfYear + " And endDateOfYear = " + endDateOfYear + "");
        
        
        try {

            ges.creatEntityManager();
            truck = (Truck) ges.getEntityManager().find(Truck.class, truckID);

        } catch (Throwable th) {
            //System.out.println("getAllUnAvailabilityTruckDateByYear: Error " + th.getMessage());
           
            logger.error("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"getAllUnAvailabilityTruckDateByYear"+"\n"
                            +"erreur      : "+"getAllUnAvailabilityTruckDateByYear: Error " + th.getMessage());
            
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
            query.setParameter("firstDateOfYear", firstDateOfYear);
            query.setParameter("endDateOfYear", endDateOfYear);
            availabilityList = (List<Date>) query.getResultList();
            
            query = ges.getEntityManager().createQuery("SELECT a.startDate, a.endDate "
                    + "FROM SolicitedTruck a WHERE a.deleted = false AND a.truckAvailable = TRUE AND "
                    + "( a.endDate BETWEEN :endDateOfYear AND :firstDateOfYear OR a.startDate BETWEEN :firstDateOfYear AND :endDateOfYear) AND "
                    + "a.truckID = :truckID");

            query.setParameter("truckID", truck);
            query.setParameter("firstDateOfYear", firstDateOfYear);
            query.setParameter("endDateOfYear", endDateOfYear);
            solicitedTruckList = query.getResultList().iterator();

            result.setMsg("good");
            ges.closeEm();

        } catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            
             logger.error("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"getAllUnAvailabilityTruckDateByYear"+"\n"
                            +"erreur      : "+"getAllUnAvailabilityTruckDateByYear: Error: \n" + th.getMessage() + "\n" + string.toString());
            
             
           // System.out.println("getAllUnAvailabilityTruckDateByYear: Error: \n" + th.getMessage() + "\n" + string.toString());
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
        
         logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"getAllUnAvailabilityTruckDateByYear"+"\n"
                            +"info      : "+"getAllUnAvailabilityTruckDateByYear: availabilityDate = " + availabilityDate);
            
             
       // System.out.println("getAllUnAvailabilityTruckDateByYear: availabilityDate = " + availabilityDate);

        //*************
        String solicitedTruckDate = "";
        af = new AvailabilityFunction();
        
        if (solicitedTruckList != null && solicitedTruckList.hasNext()) {
            while (solicitedTruckList.hasNext()) {
                Object[] objects = (Object[])solicitedTruckList.next();
                
                Date startDate = (Date) objects[0];

                Date endDate = (Date) objects[1];
                //System.out.println("getAllUnAvailabilityTruckDateByYear: objects[0] = " + objects[0] + "          objects[1] = " + objects[1]);
                solicitedTruckDate = af.getAllDayOfDateRangeConcate((startDate.after(firstDateOfYear) ? startDate : firstDateOfYear),
                                     (endDate.before(endDateOfYear) ? endDate : endDateOfYear), solicitedTruckDate );
                
            }
            
            solicitedTruckDate = solicitedTruckDate.substring(1);

        }
        
      //  System.out.println("getAllUnAvailabilityTruckDateByYear: solicitedTruckDate = " + solicitedTruckDate);
        
         logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"getAllUnAvailabilityTruckDateByYear"+"\n"
                            +"info      : "+"getAllUnAvailabilityTruckDateByYear: solicitedTruckDate = " + solicitedTruckDate);
            
             
        listResult.add(solicitedTruckDate);
       
        listResult.add("");
        
        result.setMsg("good");
        result.setObjectList(listResult);
        return result;
        
    }
    
    
}