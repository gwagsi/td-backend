/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package truckDriverBinding.dao;

import entities.Account;
import entities.User;
import entities.Drive;
import entities.Driver;
import entities.Truck;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;
import toolsAndTransversalFunctionnalities.GestionnaireEntite;
import util.date.DateFunction;
import util.exception.TruckDriverBindingException;

/**
 *
 * @author erict
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class TruckDriverBindingDao implements ITruckDriverBindingDaoLocal,ITruckDriverBindingDaoRemote {
    
    @EJB
    GestionnaireEntite ges;
    
    @Override
    public String bindTruckAndDriver(int accountID,int truckID, int driverID) throws TruckDriverBindingException{
        String res="good";
        Truck truck = null;
        Driver  driver = null;
        Drive drive = null;
        List<Drive> drives = null;
        Account account;
        User user = null;
        try{
            ges.creatEntityManager();
            
            try{
                account = (Account)ges.getEntityManager().find(Account.class, accountID);
                user = account.getUser();
                truck = (Truck)ges.getEntityManager().find(Truck.class, truckID);
                driver = (Driver)ges.getEntityManager().find(Driver.class, driverID);
                drives = (List<Drive>)ges.getEntityManager().createQuery("SELECT d FROM Drive d "
                        + "WHERE d.deleted = FALSE AND d.driverID = :driverID AND d.truckID = :truckID").setParameter("truckID", truck).setParameter("driverID", driver).getResultList();
                
                user.getUserID();
                driver.getDriverID();
                truck.getTruckID();
            
            }catch(Throwable th){
                if(user == null){
                    throw new TruckDriverBindingException(getClass()+"","bindTruckAndDriver",1,"notExistUser\n"+th.getMessage(),th.getMessage());
        
                   // return "notExistUser\n"+th.getMessage();
                }else if(driver == null){
                     throw new TruckDriverBindingException(getClass()+"","bindTruckAndDriver",1,"notExistingDriver\n"+th.getMessage(),th.getMessage());
        
                  //  return "notExistingDriver\n"+th.getMessage();
                }
                else if(truck == null){
                     throw new TruckDriverBindingException(getClass()+"","bindTruckAndDriver",1,"notExistingTruck\n"+th.getMessage(),th.getMessage());
        
                 //   return "notExistingTruck\n"+th.getMessage();
                }
                else if(drives != null && !drives.isEmpty()){
                     throw new TruckDriverBindingException(getClass()+"","bindTruckAndDriver",1,"AlreadyBind\n"+th.getMessage(),th.getMessage());
        
                   // return "AlreadyBind\n"+th.getMessage();
                }
                else 
                     throw new TruckDriverBindingException(getClass()+"","bindTruckAndDriver",1,""+th.getMessage(),th.getMessage());
        
                    //return th.getMessage();
            }
            
            if(drives != null && !drives.isEmpty()){
                return "AlreadyBind\n DriveID: "+drives.get(0).getDriveID();
            }
            
            if(driver.getUserID().equals(user) && truck.getUserID().equals(user)){
                drive = new Drive();
                drive.setUserID(user);
                drive.setDriverID(driver);
                drive.setTruckID(truck);
                drive.setDrivingDate(DateFunction.getGMTDate());
                drive.setDeleted(false);
                drive.setActif(true);
                
                ges.getEntityManager().persist(drive);
                ges.closeEm();
                drives.add(drive);
                user.setDriveList(drives);
                driver.setDriveList(drives);
                truck.setDriveList(drives);
                ges.getEntityManager().merge(user);
                ges.getEntityManager().merge(driver);
                ges.getEntityManager().merge(truck);
                ges.closeEm();
                
            }else{
                return "IncompatibilityBinding\n";
            }
        
        }catch(Throwable e){
              throw new TruckDriverBindingException(getClass()+"","bindTruckAndDriver",1,"Could not bind Truck and Driver\n"+e.getMessage(),e.getMessage());
        
         // return "Could not bind Truck and Driver\n"+e.getMessage();
        }
        return res;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Drive> getABindingRange(int accountID,int index, int maxtuple) throws TruckDriverBindingException{
       
        List<Drive> driveList;
        Account account;
        try{
            ges.creatEntityManager();
            account=(Account)ges.getEntityManager().find(Account.class, accountID);
            User user = account.getUser();
            Query query=ges.getEntityManager().createQuery("SELECT d FROM Drive d WHERE d.deleted = FALSE AND d.userID = :userID ORDER BY d.drivingDate DESC");
            
            query.setParameter("userID", user);
            query.setFirstResult(index);
            query.setMaxResults(maxtuple);
            driveList=query.getResultList();
            ges.closeEm();
        }catch(Throwable e){
            return null;
        }
        return driveList;
    }


    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public List<Drive> getAllBinding(int accountID) throws TruckDriverBindingException{
          List<Drive> driveList;
          Account account;
          User user;
        try{
            ges.creatEntityManager();
            account = (Account)ges.getEntityManager().find(Account.class, accountID);
            user = account.getUser();
            
            Query query = ges.getEntityManager().createQuery("SELECT d FROM Drive d WHERE d.deleted = FALSE AND d.userID = :userID");
            query.setParameter("userID", user);
            driveList = query.getResultList();
            ges.closeEm();
        }catch(Throwable e){
            return null;
        }
        return driveList;
    }

    @Override
    public String activateBinding(int bindingID) throws TruckDriverBindingException{
            String res = "good";
            Drive drive=null;
            try{
                try{
                    ges.creatEntityManager();
                    drive=(Drive)ges.getEntityManager().find(Drive.class, bindingID);
                    drive.setActif(true);
                    
                }catch(Throwable th){ 
                    //return "NotExistingBinding";
                     throw new TruckDriverBindingException(getClass()+"","activateBinding",1,"NotExistingBinding",th.getMessage());
        
                }
                
                ges.getEntityManager().merge(drive);
                ges.closeEm();
            }catch(Throwable e){
                StringWriter string = new StringWriter();
                PrintWriter str = new PrintWriter(string);
                e.printStackTrace(str);
                res = string.toString();
                throw new TruckDriverBindingException(getClass()+"","activateBinding",1,res,e.getMessage());
               // return res;
            }
            return res;
    }

    @Override
    public String deleteBinding(int bindingID) throws TruckDriverBindingException{
        
        String res="good";
        Drive drive=null;
        try{
            ges.creatEntityManager();
            drive=(Drive)ges.getEntityManager().find(Drive.class, bindingID);
            drive.setDeleted(true);

            ges.getEntityManager().merge(drive);

            ges.closeEm();
        
        }catch(Throwable th){
             throw new TruckDriverBindingException(getClass()+"","activateBinding",1,"NotExistingBinding\n"+th.getMessage(),th.getMessage());
              
            //return "NotExistingBinding\n"+th.getMessage();
        }
        
        return res;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Truck> getARangeOfTruckByUser(int accountID, int index, int nombreMaxResultat) throws TruckDriverBindingException{
        
        List<Truck> trucks;
        Account account;
        try{
            
            ges.creatEntityManager();
            account = (Account)ges.getEntityManager().find(Account.class, accountID);
            User user = account.getUser();
            
            Query query = ges.getEntityManager().createQuery(
                "SELECT t FROM Truck t WHERE t.deleted = FALSE AND t.userID = :userID ORDER BY t.creationDate DESC"
            );
        
            query.setParameter("userID", user);
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResultat);
        
            trucks = (List<Truck>)query.getResultList();
        
        }catch(Throwable th){
            return null;
        }finally{
            ges.closeEm();
        } 
            return trucks;
    }
    
    @Override
    public List<Truck> getAllTruckByUser(int accountID)throws TruckDriverBindingException{
         
        List<Truck> trucks;
        Account account;
        try{
            
            ges.creatEntityManager();
            
            Query query = ges.getEntityManager().createQuery(
                "SELECT v FROM Truck v WHERE v.deleted = FALSE AND v.userID = :userID "
            );
            
            account = (Account)ges.getEntityManager().find(Account.class, accountID);
            query.setParameter("userID", account.getUser());
        
            trucks = (List<Truck>)query.getResultList();
        
        }catch(Throwable th){
            return null;
        }finally{
            ges.closeEm();
        } 
            return trucks;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Driver> getARangeOfDriverByUser(int accountID, int index, int nombreMaxResultat) throws TruckDriverBindingException{
         
        List<Driver> drivers;
                
        try{
            
            ges.creatEntityManager();
            
            Query query = ges.getEntityManager().createQuery(
                "SELECT d FROM Driver d WHERE d.deleted = FALSE AND d.userID = :userID"
            );
            Account account = (Account)ges.getEntityManager().find(Account.class, accountID);
            
            query.setParameter("userID", account.getUser());
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResultat);
        
            drivers = (List<Driver>)query.getResultList();
        
        }catch(Throwable th){
            return null;
        }finally{
            ges.closeEm();
        }
            return drivers;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Driver> getAllDriverByUser(int accountID)throws TruckDriverBindingException{
          
        List<Driver> drivers;
                
        try{
            
            ges.creatEntityManager();
            
            Query query = ges.getEntityManager().createQuery(
                "SELECT d FROM Driver d WHERE d.deleted = FALSE AND d.userID = :userID"
            );
            Account account = (Account)ges.getEntityManager().find(Account.class, accountID);
            
            query.setParameter("userID", account.getUser());
        
            drivers = (List<Driver>)query.getResultList();
        
        }catch(Throwable th){
            return null;
        }finally{
            ges.closeEm();
        }
            return drivers;
    }
}
