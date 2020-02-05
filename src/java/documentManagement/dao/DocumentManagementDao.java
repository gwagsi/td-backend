/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package documentManagement.dao;

import entities.Account;
import entities.Document;
import entities.Driver;
import entities.DriverDocument;
import entities.Job;
import entities.JobDocument;
import entities.Truck;
import entities.TruckDocument;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import toolsAndTransversalFunctionnalities.GestionnaireEntite;
import toolsAndTransversalFunctionnalities.Result;
import util.date.DateFunction;
import util.exception.DocumentManagementException;

/**
 *
 * @author erman
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class DocumentManagementDao implements IDocumentManagementDaoLocal, IDocumentManagementDaoRemote{

    
    @EJB
    GestionnaireEntite ges;
     final static org.apache.logging.log4j.Logger logger = LogManager.getLogger("dump1");
    
    
    @Override
    public String addNewDocument(List<String> pathNames, int accountID)throws DocumentManagementException {
        
        Account account;
        Document document;
        String res;
        
        try{
            
            ges.creatEntityManager();
            account = (Account)ges.getEntityManager().find(Account.class, accountID);
            
            
            if(account == null){
                ges.closeEm();
                return "InvalidAccountID\n";
            }
            
            res = "good";
            
            for(String pathName:pathNames){
                
                document = new Document();
                document.setPathName(pathName);
                document.setCreationDate(DateFunction.getGMTDate());
                document.setDeleted(false);
                document.setUserID(account.getUser());
                ges.getEntityManager().persist(document);
                
                account.getUser().getDocumentList().add(document);
                ges.getEntityManager().merge(account);
                ges.getEntityManager().merge(account.getUser());
                ges.closeEm();
                res += ";"+document.getDocumentID();
            }
            
            
        }catch(Throwable th){
            throw new DocumentManagementException(getClass()+"","addNewDocument",1,th.getMessage(),th.getMessage());
        
            //return th.getMessage();
        }
        return res;
    }
    
    
    @Override
    public String bindNewDocument(List<String> pathNamesID, int accountID, int bindingID, String bindingObject, String typeOfDocument) throws DocumentManagementException{
        
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+ "bindNewDocument: pathNamesID = " + pathNamesID + "   accountID =  " + accountID + "   bindingID =  " + bindingID + "   bindingObject =  " + bindingObject + "   typeOfDocument =  " + typeOfDocument);
        
        Account account;
        List<Document> documentList;
        String res;
        try{
            
            ges.creatEntityManager();
            account = (Account)ges.getEntityManager().find(Account.class, accountID);
            
            
            if(account == null){
                ges.closeEm();
                return "InvalidAccountID\n";
            }
            
            res = "good";
            documentList = new ArrayList<>();
            for (String pathName : pathNamesID) {
                try {
                    
                    int documentID = Integer.parseInt(pathName);
                    Document document = (Document) ges.getEntityManager().createQuery("SELECT d FROM Document d WHERE d.deleted = FALSE AND d.documentID = :documentID AND d.userID =:userID").setParameter("documentID", documentID).setParameter("userID", account.getUser()).getSingleResult();
                    
                    if(document != null){
                        
                        documentList.add(document);
                    
                    }

                }catch(NumberFormatException err){
                    logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+ "bindNewDocument: Error: " + err.getMessage());
                    res += ";badDocumentID="+pathName;
                }catch(NoResultException er){
                    logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+ "bindNewDocument: Error: " + er.getMessage());
                    res += ";NoResult="+pathName;
                }catch(Throwable err){
                    StringWriter string = new StringWriter();
                    PrintWriter str = new PrintWriter(string);
                    err.printStackTrace(str);
                    res = string.toString();
                    logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+ "bindNewDocument: Error: " + res);
                     throw new DocumentManagementException(getClass()+"","bindNewDocument",1,res,res);
        
                   // return res;
                }
            }
            
        }catch(Throwable th){
             logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+ "bindNewDocument: Error: " + th.getMessage());
              throw new DocumentManagementException(getClass()+"","bindNewDocument",1,th.getMessage(),th.getMessage());
        
           // return "" + th.getMessage();
        }
        
        switch(bindingObject){
            case "Truck":
                if (typeOfDocument.equals("pictureTruck")) {
                            
                    boolean isPresentPrincipalPicture = false;
                    
                    for (Document document : documentList) {

                        Truck truck = ges.getEntityManager().find(Truck.class, bindingID);

                        document.setDescription("TruckDocument");
                        if(!isPresentPrincipalPicture){
                            truck.setPicture(document);
                            isPresentPrincipalPicture = true;
                        }
                        
                        TruckDocument truckDocument = new TruckDocument();
                        truckDocument.setCreationDate(DateFunction.getGMTDate());
                        truckDocument.setDeleted(false);
                        truckDocument.setType("pictureTruck");
                        truckDocument.setDocumentID(document);
                        truckDocument.setTruckID(truck);
                        try {
                            
                            ges.getEntityManager().persist(truckDocument);
                            ges.closeEm();
                            
                            truck.getTruckDocumentList().add(truckDocument);
                            document.getTruckDocumentList().add(truckDocument);
                            
                            ges.getEntityManager().merge(truck);
                            ges.getEntityManager().merge(document);
                         
                            ges.closeEm();
                        } catch (Throwable th) {
                            StringWriter string = new StringWriter();
                            PrintWriter str = new PrintWriter(string);
                            th.printStackTrace(str);
                            res = string.toString();
                            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"bindNewDocument: Error: " + res);
                             throw new DocumentManagementException(getClass()+"","bindNewDocument",1,res,th.getMessage());
        
                           // return res;
                        }
                    }
                } else if(typeOfDocument.equals("pictureInsurance")) {
                    
                    boolean isPresentPrincipalInsurance = false;
                    for (Document document : documentList) {
                        
                        Truck truck = ges.getEntityManager().find(Truck.class, bindingID);

                        document.setDescription("TruckDocument");
                        if(!isPresentPrincipalInsurance ){
                            truck.setPictureInsurance(document);
                            isPresentPrincipalInsurance = true;
                        }
                        
                        TruckDocument truckDocument = new TruckDocument();
                        truckDocument.setCreationDate(DateFunction.getGMTDate());
                        truckDocument.setDeleted(false);
                        truckDocument.setType("pictureInsurance");
                        truckDocument.setDocumentID(document);
                        truckDocument.setTruckID(truck);
                        try {
                            
                            ges.getEntityManager().persist(truckDocument);
                            ges.closeEm();
                            
                            truck.getTruckDocumentList().add(truckDocument);
                            document.getTruckDocumentList().add(truckDocument);
                            
                            ges.getEntityManager().merge(truck);
                            ges.getEntityManager().merge(document);
                            
                            ges.closeEm();
                            
                        } catch (Throwable th) {
                            StringWriter string = new StringWriter();
                            PrintWriter str = new PrintWriter(string);
                            th.printStackTrace(str);
                            res = string.toString();
                            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"bindNewDocument: Error: " + res);
                             throw new DocumentManagementException(getClass()+"","bindNewDocument",1,res,th.getMessage());
        
                           // return res;
                        }
                    }
                } else {
                    res = "unknowDocumentType";
                }
            break;
            case "Job":
                if (typeOfDocument.equals("jobDocument")) {
                    for (Document document : documentList) {

                        Job job = ges.getEntityManager().find(Job.class, bindingID);

                        document.setDescription("JobDocument");
                        JobDocument jobDocument = new JobDocument();
                        jobDocument.setCreationDate(DateFunction.getGMTDate());
                        jobDocument.setDeleted(false);
                        jobDocument.setType("jobDocument");
                        jobDocument.setDocumentID(document);
                        jobDocument.setJobID(job);
                        try {
                            
                            ges.getEntityManager().persist(jobDocument);
                            ges.closeEm();
                            
                            job.getJobDocumentList().add(jobDocument);
                            document.getJobDocumentList().add(jobDocument);
                            
                            ges.getEntityManager().merge(job);
                            ges.getEntityManager().merge(document);
                            
                            ges.closeEm();
                        }catch (Throwable th) {
                            StringWriter string = new StringWriter();
                            PrintWriter str = new PrintWriter(string);
                            th.printStackTrace(str);
                            res = string.toString();
                            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"bindNewDocument: Error: " + res);
                             throw new DocumentManagementException(getClass()+"","bindNewDocument",1,res,th.getMessage());
        
                           // return res;
                        }
                    }
                } else {
                    res = "unknowDocumentType";
                }
            break;
            case "Driver":
                if (typeOfDocument.equals("picture")) {
                    for (Document document : documentList) {

                        Driver driver = ges.getEntityManager().find(Driver.class, bindingID);

                        document.setDescription("DriverDocument");
                        DriverDocument driverDocument = new DriverDocument();
                        driverDocument.setCreationDate(DateFunction.getGMTDate());
                        driverDocument.setDeleted(false);
                        driverDocument.setType("picture");
                        driverDocument.setDocumentID(document);
                        driverDocument.setDriverID(driver);
                        try {
                            
                            ges.getEntityManager().persist(driverDocument);
                            ges.closeEm();
                            
                            driver.getDriverDocumentList().add(driverDocument);
                            document.getDriverDocumentList().add(driverDocument);
                            
                            ges.getEntityManager().merge(driver);
                            ges.getEntityManager().merge(document);
                            
                            ges.closeEm();
                        }catch (Throwable th) {
                            StringWriter string = new StringWriter();
                            PrintWriter str = new PrintWriter(string);
                            th.printStackTrace(str);
                            res = string.toString();
                            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"bindNewDocument: Error: " + res);
                             throw new DocumentManagementException(getClass()+"","bindNewDocument",1,res,th.getMessage());
        
                            //return res;
                        }
                    }
                } else if(typeOfDocument.equals("license")) {
                    for (Document document : documentList) {
                        
                        Driver truck = ges.getEntityManager().find(Driver.class, bindingID);

                        document.setDescription("DriverDocument");
                        DriverDocument driverDocument = new DriverDocument();
                        driverDocument.setCreationDate(DateFunction.getGMTDate());
                        driverDocument.setDeleted(false);
                        driverDocument.setType("license");
                        driverDocument.setDocumentID(document);
                        driverDocument.setDriverID(truck);
                        try {
                            
                            ges.getEntityManager().persist(driverDocument);
                            ges.closeEm();
                            
                            truck.getDriverDocumentList().add(driverDocument);
                            document.getDriverDocumentList().add(driverDocument);
                            
                            ges.getEntityManager().merge(truck);
                            ges.getEntityManager().merge(document);
                            
                            ges.closeEm();
                            
                        } catch (Throwable th) {
                            StringWriter string = new StringWriter();
                            PrintWriter str = new PrintWriter(string);
                            th.printStackTrace(str);
                            res = string.toString();
                            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"bindNewDocument: Error: " + res);
                             throw new DocumentManagementException(getClass()+"","bindNewDocument",1,res,th.getMessage());
        
                           // return res;
                        }
                    }
                } else {
                    res = "unknowDocumentType";
                }
            break;
        }
        
        
        
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+ "bindNewDocument: resultat res = " + res);
        
        return res;
    }
    
    @Override
    public String deletedDocument(List<String> pathNamesID, int accountID) throws DocumentManagementException{
        
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+ "deletedDocument: pathNamesID = " + pathNamesID + "   accountID =  " + accountID);
        
        Account account;
        Document document = null;
        String res;
        Query query;
        
        try {

            ges.creatEntityManager();
            account = (Account) ges.getEntityManager().find(Account.class, accountID);

            if (account == null) {
                ges.closeEm();
                return "InvalidAccountID\n";
            }

            res = "good";
            for (String pathName : pathNamesID) {
                try {

                    int documentID = Integer.parseInt(pathName);
                    document = (Document) ges.getEntityManager().createQuery("SELECT d FROM Document d WHERE d.deleted = FALSE AND d.documentID = :documentID AND d.userID =:userID").setParameter("documentID", documentID).setParameter("userID", account.getUser()).getSingleResult();

                } catch (NumberFormatException err) {
                    logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"deletedDocument: Error: " + err.getMessage());
                    res += ";badDocumentID:" + pathName;
                } catch (NoResultException er) {
                    logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"deletedDocument: Error: " + er.getMessage());
                    res += ";NoResult:" + pathName;
                } catch (Throwable err) {
                    StringWriter string = new StringWriter();
                    PrintWriter str = new PrintWriter(string);
                    err.printStackTrace(str);
                    res = string.toString();
                    logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"deletedDocument: Error: " + res);
                     throw new DocumentManagementException(getClass()+"","deletedDocument",1,res,res);
        
                   // return res;
                }

                if (document != null) {

                    //On teste si le document est lié a un objet
                    if (document.getDescription() == null) {
                        document.setDeleted(true);
                        ges.getEntityManager().remove(document);
                        ges.getEntityManager().merge(document);
                        ges.closeEm();
                    } else {
                        switch (document.getDescription()) {
                            case "TruckDocument":
                                query = ges.getEntityManager().createQuery("SELECT t FROM TruckDocument t WHERE t.deleted = FALSE AND t.documentID.deleted = FALSE AND t.documentID = :documentID").setParameter("documentID", document);
                                List<TruckDocument> truckDocumentList = query.getResultList();

                                if (truckDocumentList != null && !truckDocumentList.isEmpty()) {
                                    Truck truck = truckDocumentList.get(0).getTruckID();
                                    
                                    if (truck.getPicture() != null && truck.getPicture().equals(document)) {
                                        query = ges.getEntityManager().createQuery("SELECT t FROM TruckDocument t WHERE t.deleted = FALSE AND t.documentID.deleted = FALSE AND t.truckID = :truckID AND t.documentID != :documentID", TruckDocument.class).setParameter("truckID", truck).setParameter("documentID", document);
                                        truckDocumentList = query.getResultList();
                                        if (truckDocumentList != null && !truckDocumentList.isEmpty()) {
                                            truck.setPicture(truckDocumentList.get(0).getDocumentID());
                                        } else {
                                            truck.setPicture(null);
                                        }
                                    } else if (truck.getPictureInsurance() != null && truck.getPictureInsurance().equals(document)) {
                                        query = ges.getEntityManager().createQuery("SELECT t FROM TruckDocument t WHERE t.deleted = FALSE AND t.documentID.deleted = FALSE AND t.truckID = :truckID AND t.documentID != :documentID", TruckDocument.class).setParameter("truckID", truck).setParameter("documentID", document);
                                        truckDocumentList = query.getResultList();
                                        if (truckDocumentList != null && !truckDocumentList.isEmpty()) {
                                            truck.setPictureInsurance(truckDocumentList.get(0).getDocumentID());
                                        } else {
                                            truck.setPictureInsurance(null);
                                        }
                                    }

                                }
                                break;
                            case "JobDocument":
                                //On ne fait rien pour l'instant
                            case "DriverDocument":
                                //On ne fait rien pour l'instant
                            default:
                        }
                        document.setDeleted(true);
                        ges.getEntityManager().merge(document);
                        ges.closeEm();
                    }
                    
                }
            }

        } catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"deletedDocument: Error: " + res);
            throw new DocumentManagementException(getClass()+"","deletedDocument",1,res,res);
        
           // return res;
        }

        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"deletedDocument: resultat res = " + res);
        return res;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfDocumentByAccountID(int accountID, int index, int nombreMaxResultat) throws DocumentManagementException{

        String res;
        Result result = new Result();
        List<Document> documents = null;
        Account account;
        int numberOfElts = 0;
        List<String> listResult = new ArrayList<>();
        result.setMsg("good");
        
        try{
            
            ges.creatEntityManager();
            account = (Account)ges.getEntityManager().find(Account.class, accountID);
            
            if(account == null){
                result.setMsg("InvaLidAccountID");
                return result;
            }
            
            Query query =  ges.getEntityManager().createQuery("SELECT a FROM Document a WHERE a.deleted = FALSE AND  a.userID  = :userID  ");
            
            query.setParameter("userID", account.getUser());
            numberOfElts = query.getResultList().size();
            
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResultat);
            
            documents = (List<Document>)query.getResultList();
            
        }catch(Throwable th){
            result.setMsg(th.getMessage());
        }
        
        if(documents != null && !documents.isEmpty()){
            
            for(Document document: documents ){
                
                res = ""+document.getDocumentID()+";"
                    +document.getPathName()+";"
                    +document.getCreationDate().getTime()
                        ;
                
                listResult.add(res);
            }
        }
        
            listResult.add(""+numberOfElts);
            result.setObjectList(listResult);
        return result;
    }

}
