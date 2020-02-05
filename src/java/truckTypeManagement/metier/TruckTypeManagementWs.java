/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package truckTypeManagement.metier;

import java.io.PrintWriter;
import java.io.StringWriter;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import toolsAndTransversalFunctionnalities.Result;
import truckTypeManagement.dao.ITruckTypeManagementDaoLocal;

/**
 *
 * @author erman
 */
@WebService(serviceName = "TruckTypeManagementWs")
@Stateless()
public class TruckTypeManagementWs implements ITruckTypeManagementWs{

    @EJB
    private ITruckTypeManagementDaoLocal dao;
    
     final static org.apache.logging.log4j.Logger logger = LogManager.getLogger("dump1");
    
    @WebMethod(operationName = "getARangeOfTruckTypeByModel")
    @Override
    public Result getARangeOfTruckTypeByModel(String model, int index, int nombreMaxResultat){
        
        Result result = new Result();
        
        try{
            
            result = dao.getARangeOfTruckTypeByOneAttribut(model, index, nombreMaxResultat, "getARangeOfTruckTypeByModel");
            
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfTruckTypeByModel"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
        return result;
        
    }

    @WebMethod(operationName = "getARangeOfTruckTypeByMake")
    @Override
    public Result getARangeOfTruckTypeByMake(String make, int index, int nombreMaxResultat) {
        
        Result result = new Result();
        
        try{
            
            result = dao.getARangeOfTruckTypeByOneAttribut(make, index, nombreMaxResultat, "getARangeOfTruckTypeByMake");
            
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfTruckTypeByMake"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
        return result;
        
    }

    @WebMethod(operationName = "getARangeOfTruckTypeByYear")
    @Override
    public Result getARangeOfTruckTypeByYear(String year, int index, int nombreMaxResultat) {
        Result result = new Result();
        
        try{
            
            result = dao.getARangeOfTruckTypeByOneAttribut((year).toString(), index, nombreMaxResultat, "getARangeOfTruckTypeByYear");
            
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfTruckTypeByYear"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
        return result;
    }

    @WebMethod(operationName = "getARangeOfTruckTypeByMM")
    @Override
    public Result getARangeOfTruckTypeByMM(String make, String model, int index, int nombreMaxResultat) {
        Result result = new Result();
        
        try{
            
            result = dao.getARangeOfTruckTypeByTwoAttribut(make, model, index, nombreMaxResultat, "getARangeOfTruckTypeByMM");
            
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfTruckTypeByMM"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
        return result;
    }

    @WebMethod(operationName = "getARangeOfTruckTypeByMakeY")
    @Override
    public Result getARangeOfTruckTypeByMakeY(String make, String year, int index, int nombreMaxResultat) {
        Result result = new Result();
        
        try{
            
            result = dao.getARangeOfTruckTypeByTwoAttribut(make, year, index, nombreMaxResultat, "getARangeOfTruckTypeByMakeY");
            
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfTruckTypeByMakeY"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
        return result;
    }

    @WebMethod(operationName = "getARangeOfTruckTypeByModelY")
    @Override
    public Result getARangeOfTruckTypeByModelY(String make, String year, int index, int nombreMaxResultat) {
        Result result = new Result();
        
        try{
            
            result = dao.getARangeOfTruckTypeByTwoAttribut(make, year, index, nombreMaxResultat, "getARangeOfTruckTypeByModelY");
            
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfTruckTypeByModelY"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
        return result;
    }

    @WebMethod(operationName = "getARangeOfTruckTypeByMMY")
    @Override
    public Result getARangeOfTruckTypeByMMY(String make, String model, String year, int index, int nombreMaxResultat) {
        Result result = new Result();
        
        try{
            
            result = dao.getARangeOfTruckTypeByThreeAttribut(make, model, year, index, nombreMaxResultat, "getARangeOfTruckTypeByMMY");
            
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfTruckTypeByMMY"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
        return result;
    }

    @WebMethod(operationName = "getYearAllOfTruckType")
    @Override
    public Result getYearAllOfTruckType() {
         Result result = new Result();
        
        try{
            
            result = dao.getYearAllOfTruckType();
            
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getYearAllOfTruckType"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
        return result;
    }

    @WebMethod(operationName = "getTruckTypeModel")
    @Override
    public Result getTruckTypeModel(String make, String year) {
         Result result = new Result();
        
        try{
            
            result = dao.getTruckTypeModel(make, year);
            
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getTruckTypeModel"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
        return result;
    }

    @WebMethod(operationName = "getTruckTypeMakeByYear")
    @Override
    public Result getTruckTypeMakeByYear(String year) {
         Result result = new Result();
        
        try{
            
            result = dao.getTruckTypeMakeByYear(year);
            
        }catch(Throwable e){
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getTruckTypeMakeByYear"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            result.setMsg(res);
        }
        return result;
    }
}
