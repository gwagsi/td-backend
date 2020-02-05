/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package driverManagement.dao;

import accountManagement.dao.TypeOfUser;
import entities.Account;
import entities.AccountDeleted;
import entities.Document;
import entities.Drive;
import entities.Driver;
import entities.DriverDocument;
import entities.SocialStatus;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import toolsAndTransversalFunctionnalities.CodeGenerate;
import toolsAndTransversalFunctionnalities.GestionnaireEntite;
import toolsAndTransversalFunctionnalities.Result;
import toolsAndTransversalFunctionnalities.ResultBackend;
import util.date.DateFunction;
import util.exception.DocumentManagementException;
import util.exception.DriverManagementException;

/**
 *
 * @author erman
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class DriverManagementDao implements IDriverManagementDaoLocal, IDriverManagementDaoRemote{
     final static org.apache.logging.log4j.Logger logger = LogManager.getLogger("dump1");
    
    @EJB
    GestionnaireEntite ges;

    @Override
    public ResultBackend addNewDriver(String name, String surname, String cardNumber, String email, String telephone, int accountID, String picture, String address, String license) throws DriverManagementException{
    
        String res = "";
        //String error = "";
        Account truckOwnerAccount = null;
        Account accountDriver = null;
        SocialStatus socialStatus = null;
        Driver driver;
        DriverDocument driverDocument;
        Document document = null;
        List<DriverDocument> driverDocumentList;
        boolean isTruckOwner = false;
        
        ResultBackend result = new ResultBackend();
       
        try{
            
            ges.creatEntityManager();
            try {
                
                accountDriver = (Account) ges.getEntityManager().createQuery("SELECT a FROM Account a WHERE a.deleted = FALSE AND a.login = :login")
                        .setParameter("login", telephone).getSingleResult();
            } catch (Exception e) {
            }
            
            if (accountDriver != null) {
                ges.closeEm();
                result.setMsg("ExistingLogin");
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"addNewDriver: error: ExistingLogin");
                return result;
            }
            
            try {
                
                accountDriver = (Account) ges.getEntityManager().createQuery("SELECT a FROM Account a WHERE a.deleted = FALSE AND a.email = :email")
                        .setParameter("email", email).getSingleResult();
            } catch (Exception e) {
            }
            
            if (accountDriver != null && accountDriver.getSocialstatusID().getSocialstatusID() == TypeOfUser.SOCIAL_STATUS_DRIVER_ID) {
                //ges.closeEm();
                result.setMsg("ExistingEmail");
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"addNewDriver: error: ExistingEmail");
                return result;
            } else  if (accountDriver != null) {
                isTruckOwner = true;
            }
            
            socialStatus = (SocialStatus)ges.getEntityManager().find(SocialStatus.class, TypeOfUser.SOCIAL_STATUS_DRIVER_ID);
            
            if(socialStatus == null){
                ges.closeEm();
                result.setMsg("NotExistingSocialStatus");
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"addNewDriver: error: NotExistingSocialStatus");
                return result;
            }
            
            truckOwnerAccount = (Account)ges.getEntityManager().find(Account.class, accountID);
            
            if(truckOwnerAccount == null){
                ges.closeEm();
                result.setMsg("InvalidAccountID");
                return result;
            }

            //int accountNumber = getAccountNumber().getAccountNumber()+1;
            if (!isTruckOwner) {
                accountDriver = new Account();
                accountDriver.setLogin(telephone);
                accountDriver.setPassword(CodeGenerate.generatePassword());
                accountDriver.setAccountNumber(0);
                accountDriver.setActif(true);
                accountDriver.setSuspend(false);
                accountDriver.setDeleted(false);
                accountDriver.setEmail(email);
                accountDriver.setConnected(false);
                accountDriver.setSocialstatusID(socialStatus);
                accountDriver.setCreationDate(DateFunction.getGMTDate());
                ges.getEntityManager().persist(accountDriver);
            }
            
            driver = new Driver();
            driver.setUserID(truckOwnerAccount.getUser());
            driver.setIdCardNumber(cardNumber);
            driver.setEmail(email);
            driver.setName(name);
            driver.setAddress(address);
            driver.setSurname(surname);
            driver.setPicture(picture);
            driver.setLicense(license);
            driver.setTelephone(telephone);
            driver.setDeleted(false);
            driver.setCreationDate(DateFunction.getGMTDate());
            driver.setAccountID(accountDriver);
            ges.getEntityManager().persist(driver);

            accountDriver.setDriver(driver);
            ges.getEntityManager().merge(accountDriver);

            truckOwnerAccount.setDriver(driver);
            ges.getEntityManager().merge(truckOwnerAccount);

            ges.closeEm();

            String[] documentList = picture.split("#");

            driverDocumentList = new ArrayList<>();
            for (String documentID : documentList) {
                try {

                    driverDocument = new DriverDocument();

                    document = ges.getEntityManager().find(Document.class, Integer.parseInt(documentID));
                    if (document != null) {

                        document.setDescription("DriverDocument");
                        driverDocument.setCreationDate(DateFunction.getGMTDate());
                        driverDocument.setDeleted(false);
                        driverDocument.setDocumentID(document);
                        driverDocument.setDriverID(driver);
                        driverDocument.setType("picture");
                        ges.getEntityManager().persist(driverDocument);
                        ges.closeEm();

                        driverDocumentList.add(driverDocument);

                        driver.getDriverDocumentList().add(driverDocument);
                        document.getDriverDocumentList().add(driverDocument);

                        ges.getEntityManager().merge(driver);
                        ges.getEntityManager().merge(document);

                        ges.closeEm();

                    } else {
                        res += ";invalidPictureID=" + documentID;
                    }

                } catch (NumberFormatException err) {
                    logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+err.getMessage());
                    res += ";badPictureID=" + documentID;
                } catch (NullPointerException er) {
                    if (driver.getDriverDocumentList() == null || driver.getDriverDocumentList().isEmpty()) {
                        driver.setDriverDocumentList(driverDocumentList);
                    }
                    if (document.getDriverDocumentList() == null || document.getDriverDocumentList().isEmpty()) {
                        document.setDriverDocumentList(driverDocumentList);
                    }
                }

            }

            String[] licenseList = license.split("#");

            driverDocumentList = new ArrayList<>();
            for (String documentID : licenseList) {
                try {

                    driverDocument = new DriverDocument();

                    document = ges.getEntityManager().find(Document.class, Integer.parseInt(documentID));
                    if (document != null) {

                        document.setDescription("DriverDocument");
                        driverDocument.setCreationDate(DateFunction.getGMTDate());
                        driverDocument.setDeleted(false);
                        driverDocument.setDocumentID(document);
                        driverDocument.setDriverID(driver);
                        driverDocument.setType("license");
                        ges.getEntityManager().persist(driverDocument);
                        ges.closeEm();

                        driverDocumentList.add(driverDocument);

                        driver.getDriverDocumentList().add(driverDocument);
                        document.getDriverDocumentList().add(driverDocument);

                        ges.getEntityManager().merge(driver);
                        ges.getEntityManager().merge(document);

                        ges.closeEm();

                    } else {
                        res += ";invalidLicenseID=" + documentID;
                    }

                } catch (NumberFormatException err) {
                    logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+err.getMessage());
                    res += ";badLicenseID=" + documentID;
                } catch (NullPointerException er) {
                    if (driver.getDriverDocumentList() == null || driver.getDriverDocumentList().isEmpty()) {
                        driver.setDriverDocumentList(driverDocumentList);
                    }

                    if (document.getDriverDocumentList() == null || document.getDriverDocumentList().isEmpty()) {
                        document.setDriverDocumentList(driverDocumentList);
                    }
                }

            }
            
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"addNewDriver: warning:" + res);
            
            String driverInfo = "" + driver.getDriverID() + ";"  + driver.getName();
            driverInfo += ";" + driver.getSurname();
            driverInfo += ";" + driver.getEmail();
            driverInfo += ";" + driver.getTelephone();
            driverInfo += ";" + driver.getLicense();
            driverInfo += ";" + driver.getUserID().getName();
            driverInfo += ";" + driver.getUserID().getSurname();
            
            List<Object[]> listResult;

            String[] driverMailInfo = {
                String.valueOf(driver.getAccountID().getAccountID()),//00
                driver.getName(),//01
                driver.getSurname(),
                driver.getEmail(),//03
                driver.getTelephone(),
                accountDriver.getEmail(),//05
                accountDriver.getLogin(),//06
                accountDriver.getPassword(),//07
                accountDriver.getDriver().getUserID().getAccountID().getEmail(),//08
                accountDriver.getDriver().getUserID().getName(),//09
                accountDriver.getDriver().getUserID().getSurname(),//10
                String.valueOf(accountDriver.getSocialstatusID().getSocialstatusID()),
            };
            
            result.setMsg("good");
            result.setObject(driverInfo);
            
            listResult = new ArrayList<>();
            listResult.add(driverMailInfo);
            result.setResultArraysList(listResult);

        } catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            result.setMsg(res + th.getMessage());
            result.afficherResult("addNewDriver");
            return result;
        }
        return result;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfDriverByUser(int accountID, int index, int nombreMaxResultat) throws DriverManagementException{

        List<Driver> driverList = null;
        Result result = new Result();
        result.setMsg("good");
        List<String> listResult = null;
        Account account = null;
        int numberOfElts = 0;

        try {

            ges.creatEntityManager();
            
            account = (Account) ges.getEntityManager().find(Account.class, accountID);

            if (account == null) {
                ges.closeEm();
                result.setMsg("InvalidAccountID");
                return result;
            }

            Query query = ges.getEntityManager().createQuery("SELECT d FROM Driver d WHERE d.deleted = FALSE AND d.userID.accountID = :accountID ORDER BY d.creationDate DESC");

            query.setParameter("accountID", account);
            driverList = (List<Driver>) query.getResultList();
            numberOfElts = driverList.size();

            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResultat);

            driverList = (List<Driver>) query.getResultList();

            ges.closeEm();

        } catch (Throwable th) {
            System.out.print("getARangeOfDriverByUser: error: ");
            th.printStackTrace();
            result.setMsg("error");
            return result;
        }

        if (driverList != null && !driverList.isEmpty()) {
            String res;
            listResult = new ArrayList<>();
            for (Driver driver : driverList) {
                String picturesID = "";
                String picturesPATH = "";

                String licensesID = "";
                String licensesPATH = "";

                List<DriverDocument> driverDocumentList = ges.getEntityManager().createQuery("SELECT d FROM DriverDocument d WHERE d.deleted = FALSE AND d.driverID = :driverID AND d.documentID NOT IN (SELECT d FROM Document d WHERE d.deleted = TRUE) ").setParameter("driverID", driver).getResultList();

                for (DriverDocument driverDocument : driverDocumentList) {
                    if (driverDocument.getType().equals("picture")) {
                        picturesID += "#" + driverDocument.getDocumentID().getDocumentID();
                        picturesPATH += "#" + driverDocument.getDocumentID().getPathName();
                    } else {
                        licensesID += "#" + driverDocument.getDocumentID().getDocumentID();
                        licensesPATH += "#" + driverDocument.getDocumentID().getPathName();
                    }

                }

                try {

                    picturesID = picturesID.substring(1, picturesID.length());
                    picturesPATH = picturesPATH.substring(1, picturesPATH.length());
                } catch (Exception e) {

                }
                try {

                    licensesID = licensesID.substring(1, licensesID.length());
                    licensesPATH = licensesPATH.substring(1, licensesPATH.length());

                } catch (Exception e) {
                }

                res = driver.getDriverID() + ";"//00
                        + driver.getName() + ";"
                        + driver.getSurname() + ";"
                        + (driver.getAccountID() == null ? driver.getEmail() : driver.getAccountID().getEmail()) + ";"//03
                        + driver.getCreationDate().getTime() + ";"//04
                        + driver.getIdCardNumber() + ";"
                        + picturesPATH + ";"
                        + driver.getTelephone() + ";"
                        + driver.getAddress() + ";"//08
                        + licensesPATH + ";"
                        + picturesID + ";"
                        + licensesID + ";"
                        + (driver.getAccountID() == null) + ";"//12
                        + "null";
                listResult.add(res);
            }
            listResult.add("" + numberOfElts);
            result.setObjectList(listResult);
        }

        return result;
    }

    @Override
    public ResultBackend modifyDriverInfo(int driverID, String name, String surname, String cardNumber, String email, String telephone, int accountID, String picture, String address, String license) throws DriverManagementException{

        String res = "good";
        String error = "";
        Account account = null;
        Account driverAccount = null;
        Driver driver = null;
        ResultBackend result = new ResultBackend();
        boolean sendEmail = false;

        try {

            ges.creatEntityManager();

            account = (Account) ges.getEntityManager().find(Account.class, accountID);

            // Rechercher le Driver en BD
            driver = (Driver) ges.getEntityManager().find(Driver.class, driverID);

            if (account == null) {
                ges.closeEm();
                result.setMsg("InvalidAccountID");
                return result;
            }

            if (driver == null || driver.getDeleted()) {
                ges.closeEm();
                result.setMsg("NotExistingDriver");
                return result;
            }

            try {

                driverAccount = (Account) ges.getEntityManager().createQuery("SELECT a FROM Account a WHERE a.deleted = FALSE AND a.email = :email")
                        .setParameter("email", email)
                        .getSingleResult();
            } catch (Exception e) {
            }

            if (driverAccount != null && (driverAccount.getSocialstatusID().getSocialstatusID() == TypeOfUser.SOCIAL_STATUS_DRIVER_ID 
                    && driverAccount.getDriver() != driver || driverAccount.getSocialstatusID().getSocialstatusID() != TypeOfUser.SOCIAL_STATUS_DRIVER_ID 
                    && driverAccount.getUser() != null)) {
                
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"modifyDriverInfo: ExistingEmail " + driverAccount.getAccountID() + " - " + driverAccount.getEmail());                    
                ges.closeEm();
                result.setMsg("ExistingEmail");
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"modifyDriverInfo: error: ExistingEmail \n" + error);
                return result;
            }

            // Si le driver ne possède pas de compte, il faudra en créer
            if (driver.getAccountID() == null) {

                try {

                    driverAccount = (Account) ges.getEntityManager().createQuery("SELECT a FROM Account a WHERE a.deleted = FALSE AND a.login = :login")
                            .setParameter("login", telephone)
                            .getSingleResult();
                } catch (Exception e) {
                }

                if (driverAccount != null) {
                    logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"modifyDriverInfo: ExistingLogin " + driverAccount.getAccountID() + " - " + driverAccount.getLogin());
                    ges.closeEm();
                    result.setMsg("ExistingLogin");
                    logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"modifyDriverInfo: error: ExistingLogin \n" + error);
                    return result;
                }

                SocialStatus socialStatus = (SocialStatus) ges.getEntityManager().find(SocialStatus.class, TypeOfUser.SOCIAL_STATUS_DRIVER_ID);

                if (socialStatus == null) {
                    ges.closeEm();
                    result.setMsg("NotExistingSocialStatus");
                    logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"modifyDriverInfo: error: NotExistingSocialStatus \n" + error);
                    return result;
                }

                driverAccount = new Account();
                driverAccount.setLogin(telephone);
                driverAccount.setPassword(CodeGenerate.generatePassword());
                driverAccount.setAccountNumber(0);
                driverAccount.setActif(true);
                driverAccount.setSuspend(false);
                driverAccount.setDeleted(false);
                driverAccount.setEmail(email);
                driverAccount.setConnected(false);
                driverAccount.setSocialstatusID(socialStatus);
                driverAccount.setCreationDate(DateFunction.getGMTDate());
                ges.getEntityManager().persist(driverAccount);
                sendEmail = true;

            }

            driver.setUserID(account.getUser());
            driver.setIdCardNumber(cardNumber);
            driver.setEmail(email);
            driver.setName(name);
            driver.setAddress(address);
            driver.setSurname(surname);
            driver.setPicture(picture);
            driver.setLicense(license);
            driver.setTelephone(telephone);
            driver.setAccountID(driverAccount);
            ges.getEntityManager().merge(driver);
            ges.closeEm();

        } catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"modifyDriverInfo: Error:" + res);
            ges.rollbackTransaction();
            result.setMsg(th.getMessage());
            return result;
        }

        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"modifyDriverInfo: warning(res):" + res);

        String driverInfo = "" + driver.getDriverID() + ";" + driver.getName();
        driverInfo += ";" + driver.getSurname();
        driverInfo += ";" + driver.getEmail();
        driverInfo += ";" + driver.getTelephone();
        driverInfo += ";" + driver.getLicense();
        driverInfo += ";" + driver.getUserID().getName();
        driverInfo += ";" + driver.getUserID().getSurname();

        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"modifyDriverInfo: driverInfo:" + driverInfo);
        if (sendEmail) {
            
            List<Object[]> listResult;
            
            String[] driverMailInfo = {
                String.valueOf(driverAccount.getAccountID()),//00
                driver.getName(),//01
                driver.getSurname(),
                driver.getEmail(),//03
                driver.getTelephone(),
                driverAccount.getEmail(),//05
                driverAccount.getLogin(),//06
                driverAccount.getPassword(),//07
                driver.getUserID().getAccountID().getEmail(),//08
                driver.getUserID().getName(),//09
                driver.getUserID().getSurname(),//10
                String.valueOf(driverAccount.getSocialstatusID().getSocialstatusID()),};
            
            listResult = new ArrayList<>();
            listResult.add(driverMailInfo);
            result.setResultArraysList(listResult);
        }
        result.setMsg("good");
        result.setObject(driverInfo);
        result.afficherResult("modifyDriverInfo");
        return result;
    }

    @Override
    public String deleteDriver(int driverID) throws DriverManagementException{

        String res = "good";
        Driver driver = null;
        List<Drive> drives;
        Account account;
        AccountDeleted accountDeleted;
        try {
            ges.creatEntityManager();
            driver = (Driver) ges.getEntityManager().find(Driver.class, driverID);
            driver.setDeleted(true);
            account = driver.getAccountID();
            if (account != null) {
                account.setDeleted(true);
                //j'ai ajouté ceci pour la gestion du backup
                accountDeleted = new AccountDeleted();
                accountDeleted.setAccountID(account);
                accountDeleted.setEmail(account.getEmail());
                accountDeleted.setLogin(account.getLogin());
                ges.getEntityManager().persist(accountDeleted);
                //account.setEmail( String.valueOf((account.getEmail()+ String.valueOf(DateFunction.getGMTDate())).hashCode()) );
                //account.setLogin( String.valueOf((account.getLogin()+ String.valueOf(DateFunction.getGMTDate())).hashCode()) );
                account.setEmail(String.valueOf(UUID.randomUUID()));
                account.setLogin(String.valueOf(UUID.randomUUID()));
                ges.getEntityManager().merge(account);

            }
            drives = (List<Drive>) ges.getEntityManager().createQuery("SELECT d FROM Drive d "
                    + "WHERE d.deleted = FALSE AND d.driverID = :driverID").setParameter("driverID", driver).getResultList();

            for (Drive drive : drives) {
                drive.setDeleted(true);
                ges.getEntityManager().merge(drive);
            }

            ges.getEntityManager().merge(driver);
            
            ges.closeEm();

        } catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"deleteDriver: NotExistingDriver " + res);
            throw new DriverManagementException(getClass()+"","deleteDriver",1,th.getMessage(),th.getMessage());
        
            //return "NotExistingDriver";
        }

        return res;
    }
    
}
