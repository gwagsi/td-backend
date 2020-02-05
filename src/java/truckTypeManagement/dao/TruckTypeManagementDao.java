/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package truckTypeManagement.dao;

import entities.TruckType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;
import toolsAndTransversalFunctionnalities.GestionnaireEntite;
import toolsAndTransversalFunctionnalities.Result;

/**
 *
 * @author erman
 */

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class TruckTypeManagementDao implements ITruckTypeManagementDaoLocal, ITruckTypeManagementDaoRemote{

    @EJB
    GestionnaireEntite ges;
    
    Date transfomSttringToDate(String date){
        date = "01-01-"+date+" 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        Date year = null;
        try {
            year = sdf.parse(date);
        } catch (ParseException ex) {
            Logger.getLogger(TruckTypeManagementDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return year;
    }
    
    public Query queryByOneAttribut(String methodName, String attribut){
        
        Query query = null;
        switch(methodName){
            case "getARangeOfTruckTypeByModel":
                query = this.ges.getEntityManager().createNamedQuery("TruckType.findByModel");
                query.setParameter("model", attribut);
                break;
                
            case "getARangeOfTruckTypeByMake":
                query = this.ges.getEntityManager().createNamedQuery("TruckType.findByMake");
                query.setParameter("make", attribut);
                break;
             
            case "getARangeOfTruckTypeByYear":
                query = this.ges.getEntityManager().createNamedQuery("TruckType.findByYear");
                Date year = transfomSttringToDate(attribut);
                query.setParameter("year", year);
                break;
                
             default:
        }
         
         return query;
    }
    
    public Query queryByTwoAttribut(String methodName, String attribut1, String attribut2){
        
        Query query = null;
        Date year;
        switch(methodName){
            case "getARangeOfTruckTypeByModelY":
                query = this.ges.getEntityManager().createQuery("SELECT t FROM TruckType t WHERE t.model = :model"
                    + " AND t.year = :year");
                year = transfomSttringToDate(attribut2);
                query.setParameter("year", year);
                query.setParameter("model", attribut1);
                break;
                
            case "getARangeOfTruckTypeByMM":
                query = ges.getEntityManager().createQuery("SELECT t FROM TruckType t WHERE t.model = :model"
                    + " AND t.make = :make");
                query.setParameter("make", attribut1);
                query.setParameter("model", attribut2);
                break;
             
            case "getARangeOfTruckTypeByMakeY":
                query = this.ges.getEntityManager().createQuery("SELECT t FROM TruckType t WHERE t.make = :make"
                    + " AND t.year = :year");
                year = transfomSttringToDate(attribut2);
                query.setParameter("year", year);
                query.setParameter("make", attribut1);
                break;
             
             default:
        }
         
         return query;
    }
    
    public Query queryByThreeAttribut(String methodName, String attribut1, String attribut2, String attribut3){
        
        Query query = null;
        Date year = null;
        SimpleDateFormat sdf;
        switch(methodName){
            case "getARangeOfTruckTypeByMMY":
                query = this.ges.getEntityManager().createQuery("SELECT t FROM TruckType t WHERE t.model = :model"
                    + " AND t.year = :year AND t.make = :make");
                year = transfomSttringToDate(attribut3);
                
                query.setParameter("make", attribut1);
                query.setParameter("year", year);
                query.setParameter("model", attribut2);
                break;
                
             default:
        }
         
         return query;
    }
    
    @Override
    public Result getARangeOfTruckTypeByOneAttribut(String attribut, int index, int nombreMaxResultat, String methodName) {
        
        List<TruckType> lt = null;
        Result result = new Result();
        List<String> lts = new ArrayList<>();
        int size = 0;
        
        try{
            
            try{
                ges.creatEntityManager();
            }catch(Throwable th){
                result.setMsg("Could not create Entity Manager\n"+th.getMessage());
                return result;
            }
            
            Query query =  queryByOneAttribut(methodName, attribut);
            lt = (List<TruckType>)query.getResultList();
            size = lt.size();
            
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResultat);
            lt = (List<TruckType>)query.getResultList();
            ges.closeEm();
            result.setMsg("good");
        
        }catch(Throwable th){
            ges.closeEm();
            result.setMsg(th.getMessage());
        }
        LinkedHashMap<String,Object> o=new LinkedHashMap(); 
        if(lt != null && !lt.isEmpty()){
            String res = "";
            for(TruckType truckType: lt){
                Calendar c = Calendar.getInstance();
                c.setTime(truckType.getYear());
                
                res = truckType.getTrucktypeID()+";"
                        +c.get(Calendar.YEAR)+";"
                        +truckType.getModel()+";"
                        +truckType.getMake()+";"
                        +truckType.getTrimStyle();
                lts.add(res);
               
            }
            lts.add(""+size);
            result.setObjectList(lts);
        }
        
        return result;
    }

    @Override
    public Result getARangeOfTruckTypeByTwoAttribut(String attribut1, String attribut2, int index, int nombreMaxResultat, String methodName) {
        
        List<TruckType> lt = null;
        Result result = new Result();
        List<String> lts = new ArrayList<>();
        
        try{
            
            try{
                ges.creatEntityManager();
            }catch(Throwable th){
                result.setMsg("Could not create Entity Manager\n"+th.getMessage());
                return result;
            }
            
            Query query =  this.queryByTwoAttribut(methodName, attribut1, attribut2);

            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResultat);
            
            lt = (List<TruckType>)query.getResultList();
            ges.closeEm();
            result.setMsg("good");
        
        }catch(Throwable th){
            ges.closeEm();
            result.setMsg(th.getMessage());
        }
        
        if(lt != null && !lt.isEmpty()){
            String res = "";
            for(TruckType truckType: lt){
                Calendar c = Calendar.getInstance();
                c.setTime(truckType.getYear());
                
                res = truckType.getTrucktypeID()+";"
                        +c.get(Calendar.YEAR)+";"
                        +truckType.getModel()+";"
                        +truckType.getMake()+";"
                        +truckType.getTrimStyle();
                lts.add(res);
            }
            int size = lts.size();
            lts.add(""+size);
            result.setObjectList(lts);
        }
        return result;
    }
    
    @Override
    public Result getARangeOfTruckTypeByThreeAttribut(String attribut1, String attribut2, String attribut3, int index, int nombreMaxResultat, String methodName) {
        
        List<TruckType> lt = null;
        Result result = new Result();
        List<String> lts = new ArrayList<>();
        
        try{
            
            try{
                ges.creatEntityManager();
            }catch(Throwable th){
                result.setMsg("Could not create Entity Manager\n"+th.getMessage());
                return result;
            }
            
            Query query =  this.queryByThreeAttribut(methodName, attribut1, attribut2, attribut3);

            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResultat);
            
            lt = (List<TruckType>)query.getResultList();
            ges.closeEm();
            result.setMsg("good");
        
        }catch(Throwable th){
            ges.closeEm();
            result.setMsg(th.getMessage());
        }
        
        if(lt != null && !lt.isEmpty()){
            String res = "";
            for(TruckType truckType: lt){
                Calendar c = Calendar.getInstance();
                c.setTime(truckType.getYear());
                
                res = truckType.getTrucktypeID()+";"
                        +c.get(Calendar.YEAR)+";"
                        +truckType.getModel()+";"
                        +truckType.getMake()+";"
                        +truckType.getTrimStyle();
                lts.add(res);
            }
            int size = lts.size();
            lts.add(""+size);
            result.setObjectList(lts);
        }
        return result;
    }

    @Override
    public Result getYearAllOfTruckType() {
        List<Date> lt = null;
        Result result = new Result();
        List<String> lts = new ArrayList<>();
        
        try{
            
            try{
                ges.creatEntityManager();
            }catch(Throwable th){
                result.setMsg("Could not create Entity Manager\n"+th.getMessage());
                return result;
            }
            
            Query query = ges.getEntityManager().createQuery("SELECT DISTINCT t.year FROM TruckType t");

            
            lt = (List<Date>)query.getResultList();
            ges.closeEm();
            result.setMsg("good");
        
        }catch(Throwable th){
            ges.closeEm();
            result.setMsg(th.getMessage());
        }
        
        if(lt != null && !lt.isEmpty()){
            String res = "";
            for(Date truckType: lt){
                Calendar c = Calendar.getInstance();
                c.setTime(truckType);
                
                res = ""+c.get(Calendar.YEAR);
                
                lts.add(res);
            }
            result.setObjectList(lts);
        }
        return result;
    }

    @Override
    public Result getTruckTypeMakeByYear(String year) {
        List<String> lt = null;
        Result result = new Result();
        List<String> lts = new ArrayList<>();
        
        try{
            
            try{
                ges.creatEntityManager();
            }catch(Throwable th){
                result.setMsg("Could not create Entity Manager\n"+th.getMessage());
                return result;
            }
            
            Query query = ges.getEntityManager().createQuery("SELECT DISTINCT t.make FROM TruckType t"
                    + " WHERE t.year = :year");
            Date date = transfomSttringToDate(year);
            query.setParameter("year", date);
            
            lt = (List<String>)query.getResultList();
            ges.closeEm();
            result.setMsg("good");
        
        }catch(Throwable th){
            ges.closeEm();
            result.setMsg(th.getMessage());
        }
        
        if(lt != null && !lt.isEmpty()){
            String res = "";
            for(String truckType: lt){
               
                res =  truckType;
                
                lts.add(res);
            }
            result.setObjectList(lts);
        }
        return result;
    }

    @Override
    public Result getTruckTypeModel(String make, String year) {
        List<String> lt = null;
        Result result = new Result();
        List<String> lts = new ArrayList<>();
        
        try{
            
            try{
                ges.creatEntityManager();
            }catch(Throwable th){
                result.setMsg("Could not create Entity Manager\n"+th.getMessage());
                return result;
            }
            
            Query query = ges.getEntityManager().createQuery("SELECT t.model FROM TruckType t WHERE t.make = :make"
                    + " AND t.year = :year");
            Date date = transfomSttringToDate(year);
            query.setParameter("year", date);
            query.setParameter("make", make);
            
            lt = (List<String>)query.getResultList();
            ges.closeEm();
            result.setMsg("good");
        
        }catch(Throwable th){
            result.setMsg(th.getMessage());
        }
        
        if(lt != null && !lt.isEmpty()){
            String res;
            for(String truckType: lt){
               
                res =  truckType;
                
                lts.add(res);
            }
            result.setObjectList(lts);
        }
        return result;
    }
    
}
