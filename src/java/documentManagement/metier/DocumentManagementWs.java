/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package documentManagement.metier;

import documentManagement.dao.IDocumentManagementDaoLocal;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import toolsAndTransversalFunctionnalities.Result;

/**
 *
 * @author erman
 */
@WebService(serviceName = "DocumentManagementWs")
@Stateless()
public class DocumentManagementWs implements IDocumentManagementWs{

    @EJB
    IDocumentManagementDaoLocal dao;
     final static org.apache.logging.log4j.Logger logger = LogManager.getLogger("dump1");
    
    
    @WebMethod(operationName = "addNewDocument")
    @Override
    public String addNewDocument(List<String> pathNames, int accountID) {
        String res;
        try{
            
            res = dao.addNewDocument(pathNames, accountID);
            
        }catch(Throwable e){
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"addNewDocument"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }
        
        return res;
    }
    
    @WebMethod(operationName = "deleteDocument")
    @Override
    public String deletedDocument(List<String> pathNamesID, int accountID) {
        String res;
        try{
            
            res = dao.deletedDocument(pathNamesID, accountID);
            
        }catch(Throwable e){
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"deletedDocument"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }
        
        return res;
    }

    @WebMethod(operationName = "getARangeOfDocumentByAccountID")
    @Override
    public Result getARangeOfDocumentByAccountID(int accountID, int index, int nombreMaxResultat) {
        Result result = new Result();
        try{
            
            result = dao.getARangeOfDocumentByAccountID(accountID, index, nombreMaxResultat);
            
        }catch(Throwable e){
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            result.setMsg(string.toString());
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getARangeOfDocumentByAccountID"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return result;
        }
        
        return result;
    }

    @WebMethod(operationName = "bindNewDocument")
    @Override
    public String bindNewDocument(List<String> pathNamesID, int accountID, int bindingID, String bindingObject, String typeOfDocument) {
        String res;
        try{
            
            res = dao.bindNewDocument(pathNamesID, accountID, bindingID, bindingObject, typeOfDocument);
            
        }catch(Throwable e){
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"bindNewDocument"+"\n"
                            +"Erreur   : "+"\n"
                            +string.toString());
            return res;
        }
        
        return res;
    }
    
    
}
