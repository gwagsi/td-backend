/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package truckDriverBinding.metier;

import entities.Drive;
import entities.Driver;
import entities.Truck;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import toolsAndTransversalFunctionnalities.Result;
import truckDriverBinding.dao.ITruckDriverBindingDaoLocal;
import util.exception.TruckDriverBindingException;

/**
 *
 * @author erict
 */
@WebService(serviceName = "TruckDriverBindingWs")
@Stateless()
public class TruckDriverBindingWs implements ITruckDriverBindingWs  {
    
    @EJB
    private ITruckDriverBindingDaoLocal dao;
    final static org.apache.logging.log4j.Logger logger = LogManager.getLogger("dump1");
    
    @WebMethod(operationName = "bindTruckAndDriver")
    @Override
    public String bindTruckAndDriver(int userID,int truckID, int driverID) {
        
        String res="good";
        try{
            
            res = dao.bindTruckAndDriver(userID, truckID, driverID);
        }catch(TruckDriverBindingException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"bindTruckAndDriver"+"\n"
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

    @WebMethod(operationName = "getABindingRange")
    @Override
    public Result getABindingRange(int userID, int index, int maxtuple) {
       Result res=new Result();
       res.setMsg("good");
       List<Drive> listDrive;
       int size = 0;
       try{
           listDrive = dao.getAllBinding(userID);
           
           if(listDrive != null && !listDrive.isEmpty()){
               size = listDrive.size();
           }
           
           listDrive = dao.getABindingRange(userID, index, maxtuple);
           List<String> list=new ArrayList<>();
           
           if(listDrive != null && !listDrive.isEmpty()){
                for(Drive drive: listDrive){
                    String driveInfo="";
                    Calendar c = Calendar.getInstance();
                    c.setTime(drive.getTruckID().getTrucktypeID().getYear());
                    
                    driveInfo+=drive.getDriveID()+";"
                          +drive.getDrivingDate()+";"
                          +drive.getTruckID().getTruckID()+";"
                          +drive.getTruckID().getTruckNumber()+";"
                          +c.get(Calendar.YEAR)+"-"
                          +drive.getTruckID().getTrucktypeID().getMake()+"-"
                          +drive.getTruckID().getTrucktypeID().getModel()+";"
                          +drive.getDriverID().getDriverID()+";"
                          +drive.getDriverID().getIdCardNumber()+";"
                          +drive.getDriverID().getName()+";"
                          +drive.getDriverID().getPicture()+";"
                          +drive.getTruckID().getPicture();
                    list.add(driveInfo);
                }
           }else{
               res.setMsg("empty");
           }
           list.add(""+size);
           res.setObjectList(list);
           res.setObject(null);
           
       }catch(Throwable e){
                StringWriter string = new StringWriter();
                PrintWriter str = new PrintWriter(string);
                e.printStackTrace(str);
                res.setObject(null);
                res.setObjectList(null);
                res.setMsg(string.toString());
                return res;
       }
       return res;
    }

    
    @WebMethod(operationName = "activateBinding")
    @Override
    public String activateBinding(int bindingID) {
        String res="good";
        try{
            res = dao.activateBinding(bindingID);
            }catch(TruckDriverBindingException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"activateBinding"+"\n"
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

    @WebMethod(operationName = "deleteBinding")
    @Override
    public String deleteBinding(int bindingID) {
        
        String res;
        try{
            
            res = dao.deleteBinding(bindingID);
            }catch(TruckDriverBindingException error){
            res=error.getSmallMessage();
            logger.error(    "\n"+"Class ws  : "+getClass()+"\n"
                            +"Fct   ws  : "+"deleteBinding"+"\n"
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

    @WebMethod(operationName = "getARangeOfTruckByUser")
    @Override
    public Result getARangeOfTruckByUser(int userID, int index, int nombreMaxResultat) {
         
        String res = "good";
        Result resultat = new Result();
        List<String> list = new ArrayList();
        List<Truck>  trucks;
        int size = 0;
         
         try{
             
             trucks = dao.getAllTruckByUser(userID);
             if(trucks != null && !trucks.isEmpty()){
                 size = trucks.size();
             }
             trucks = dao.getARangeOfTruckByUser(userID, index, nombreMaxResultat);
         
         }catch(Throwable e){
             
                StringWriter string = new StringWriter();
                PrintWriter str = new PrintWriter(string);
                e.printStackTrace(str);
                res = string.toString();
                resultat.setMsg(res);
                return resultat;
                
        }
         if(trucks != null && !trucks.isEmpty()){
             for(Truck truck:trucks){
                 String ht ="";
                 Calendar c = Calendar.getInstance();
                 c.setTime(truck.getTrucktypeID().getYear());
                 
                 ht += truck.getTruckID()+";"
                         +truck.getLocationPrice()+";"
                         +truck.getTruckNumber()+";"
                         +truck.getPicture()+";"
                         +c.get(Calendar.YEAR)+"-"+truck.getTrucktypeID().getMake()+"-"+truck.getTrucktypeID().getModel();
                 list.add(ht);
             }
         }else{
             res = "empty";
         }
         
         list.add(""+size);
         resultat.setObjectList(list);
         resultat.setMsg(res);
         return resultat;
    }

    @WebMethod(operationName = "getARangeOfDriverByUser")
    @Override
    public Result getARangeOfDriverByUser(int userID, int index, int nombreMaxResultat) {
         
        String res = "good";
        Result resultat = new Result();
        List<String> list = new ArrayList();
        List<Driver>  drivers;
        int size = 0;
         
         try{
             
             drivers = dao.getAllDriverByUser(userID);
             if(drivers != null && !drivers.isEmpty()){
                 size = drivers.size();
             }
             drivers = dao.getARangeOfDriverByUser(userID, index, nombreMaxResultat);
         
         }catch(Throwable e){
             
                StringWriter string = new StringWriter();
                PrintWriter str = new PrintWriter(string);
                e.printStackTrace(str);
                res = string.toString();
                resultat.setMsg(res);
                return resultat;
                
        }
         if(drivers != null && !drivers.isEmpty()){
             for(Driver driver:drivers){
                 String ht ="";
                 ht += driver.getDriverID()+";"
                         +driver.getIdCardNumber()+";"
                         +driver.getName()+";"
                         +driver.getTelephone()+";"
                         +driver.getPicture();
                 list.add(ht);
             }
             list.add(""+size);
         }else{
             res = "empty"; 
         list.add(""+size);
         } 
         
         resultat.setObjectList(list);
         resultat.setMsg(res);
         return resultat;
    }

}
