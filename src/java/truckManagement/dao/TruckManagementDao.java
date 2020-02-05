/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truckManagement.dao;

import entities.Account;
import entities.AreaNotCovered;
import entities.Document;
import entities.Drive;
import entities.Driver;
import entities.DriverDocument;
import entities.Job;
import entities.LenghtOfBed;
import entities.Truck;
import entities.TruckAxle;
import entities.TruckDocument;
import entities.TruckType;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import javax.persistence.TemporalType;
import org.apache.logging.log4j.LogManager;
import toolsAndTransversalFunctionnalities.GestionnaireEntite;
import toolsAndTransversalFunctionnalities.Result;
import util.date.DateFunction;
import util.exception.TruckManagementException;
import util.metier.function.DriverFunction;
import util.metier.function.FormatDataOfSearchTruckFilter;
import util.query.sql.searchTruckQuery.SearchAvailableTruck;
import util.query.sql.searchTruckQuery.SearchTruckFilter;
import util.query.sql.searchTruckQuery.TruckSearchQuery;

/**
 *
 * @author erman
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class TruckManagementDao implements ITruckManagementDaoLocal, ITruckManagementDaoRemote {

    @EJB
    GestionnaireEntite ges;
    final static org.apache.logging.log4j.Logger logger = LogManager.getLogger("dump1");
    
    TruckFunction tf;

    @Override
    public Result addNewTruck(String pictureTrucksID, String truckNumber, double locationPrice,
            int accountID, String year, String make, String model, int truckAxleID,
            int lenghtOfBedID, int distance, String phoneNumber, String truckZipCode,
            String dOTNumber, int generalLiability, int insuranceLiability, String pictureInsurancesID,
            String truckDescription, float latitude, float longitude) {

        String res = "";
        String date;
        Result result = new Result();
        TruckDocument truckDocument;
        Document document = null;
        List<TruckDocument> truckDocumentList;

        Truck truck = new Truck();
        List<Truck> trucks = null;
        TruckAxle truckAxle = null;
        LenghtOfBed lenghtOfBed = null;
        Account account = null;
        TruckType truckType = null;
        String error = "";
        List<AreaNotCovered> areaNotCoveredList = null;
        List<String> resultList = new ArrayList<>();

        try {

            ges.creatEntityManager();
            try {

                account = (Account) ges.getEntityManager().find(Account.class, accountID);

                date = "01-01-2013 00:00:00";
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                Date day = null;

                try {
                    day = sdf.parse(date);
                } catch (ParseException ex) {
                         logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+ex);
           
                   // Logger.getLogger(TruckTypeManagementDao.class.getName()).log(Level.SEVERE, null, ex);
                }

                String query = "SELECT t FROM TruckType t WHERE t.make = :make AND t.model = :model AND t.year = :year";
                truckType = (TruckType) ges.getEntityManager().createQuery(query).setParameter("make", "Toyota").setParameter("model", "Venza").setParameter("year", day).getSingleResult();
                truckAxle = (TruckAxle) ges.getEntityManager().find(TruckAxle.class, truckAxleID);
                lenghtOfBed = (LenghtOfBed) ges.getEntityManager().find(LenghtOfBed.class, lenghtOfBedID);
                trucks = (List<Truck>) ges.getEntityManager().createQuery(""
                        + "SELECT t FROM Truck t WHERE t.truckNumber = :truckNumber AND t.deleted = FALSE AND t.userID = :userID").setParameter("userID", account.getUser()).setParameter("truckNumber", truckNumber).getResultList();

            } catch (Throwable th) {
                error = th.getMessage();
                logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+error);
            }

            if (account == null) {
                ges.closeEm();
                result.setMsg("UnExistingAccount");
                result.afficherResult("addNewTruck");
                return result;
            }
            if (truckType == null) {
                ges.closeEm();
                result.setMsg("ExistingtruckType");
                result.afficherResult("addNewTruck");
                return result;
            }
            if (truckAxle == null) {
                ges.closeEm();
                result.setMsg("NotExistingTruckAxle");
                result.afficherResult("addNewTruck");
                return result;
            }
            if (lenghtOfBed == null) {
                ges.closeEm();
                result.setMsg("NotExistingLenghtOfBed");
                result.afficherResult("addNewTruck");
                return result;
            }
            if (trucks != null && !trucks.isEmpty()) {
                ges.closeEm();
                result.setMsg("ExistingTruckNumber" );
                result.afficherResult("addNewTruck");
                return result;
            }

            truck.setTruckNumber(truckNumber);
            truck.setTruckDescription(truckDescription);
            truck.setLocationPrice((float) locationPrice);
            truck.setCreationDate(DateFunction.getGMTDate());
            truck.setAvailable(true);
            truck.setDeleted(false);

            truck.setLenghtofbedID(lenghtOfBed);
            truck.setDistance(distance);
            truck.setPhoneNumber(phoneNumber);
            truck.setPicture(null);
            truck.setYear(year);
            truck.setTruckZipCode(truckZipCode);
            truck.setLatitude(latitude);
            truck.setLongitude(longitude);
            truck.setDOTNumber(dOTNumber);
            truck.setGeneralLiability(generalLiability);
            truck.setInsuranceLiability(insuranceLiability);
            truck.setPictureInsurance(null);

            truck.setTrucktypeID(truckType);
            truck.setTruckaxleID(truckAxle);
            truck.setUserID(account.getUser());

            ges.getEntityManager().persist(truck);
            account.getUser().getTruckList().add(truck);
            account.getUser().setTruckNumber(0);
            ges.getEntityManager().merge(account);
            ges.closeEm();

            res = "good";
            res += ";" + truck.getTruckID() + ";" + (truck.getUserID().getTruckNumber() + 1) + ";" + truck.getLocationPrice();
            result.setMsg(res);

        } catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            result.setMsg(res);
            result.afficherResult("addNewTruck");
            return result;
        }

        //Ajout des images pour assurance
        String[] documentList = pictureInsurancesID.split("#");

        truckDocumentList = new ArrayList<>();

        boolean isPresentPrincipalInsurance = false;

        for (String documentID : documentList) {
            try {

                truckDocument = new TruckDocument();

                document = ges.getEntityManager().find(Document.class, Integer.parseInt(documentID));
                if (document == null) {
                    res += ";invalidPictureInsuranceID=" + documentID;
                } else {
                    
                    truckDocument.setCreationDate(DateFunction.getGMTDate());
                    truckDocument.setDeleted(false);
                    truckDocument.setType("pictureInsurance");
                    truckDocument.setDocumentID(document);
                    truckDocument.setTruckID(truck);
                    ges.getEntityManager().persist(truckDocument);
                    ges.closeEm();
                    if (!isPresentPrincipalInsurance) {
                        truck.setPictureInsurance(document);
                        isPresentPrincipalInsurance = true;
                    }
                    truckDocumentList.add(truckDocument);
                    truck.getTruckDocumentList().add(truckDocument);
                    document.getTruckDocumentList().add(truckDocument);

                    ges.getEntityManager().merge(truck);
                    ges.getEntityManager().merge(document);

                    ges.closeEm();

                }

            } catch (NumberFormatException err) {
                logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+err.getMessage());
                res += ";badPictureInsuranceID=" + documentID;
            } catch (NullPointerException er) {
                if (truck.getTruckDocumentList() == null || truck.getTruckDocumentList().isEmpty()) {
                    truck.setTruckDocumentList(truckDocumentList);
                }
                if (document.getTruckDocumentList() == null || document.getTruckDocumentList().isEmpty()) {
                    document.setTruckDocumentList(truckDocumentList);
                }
                //res += ";NullPointerException";
            }

        }

        //Ajout des images pour camion
        String[] pictureTrucksList = pictureTrucksID.split("#");

        truckDocumentList = new ArrayList<>();

        boolean isPresentPrincipalPicture = false;

        for (String documentID : pictureTrucksList) {
            try {

                truckDocument = new TruckDocument();

                document = ges.getEntityManager().find(Document.class, Integer.parseInt(documentID));
                if (document != null) {

                    document.setDescription("TruckDocument");
                    truckDocument.setCreationDate(DateFunction.getGMTDate());
                    truckDocument.setDeleted(false);
                    truckDocument.setType("pictureTruck");
                    truckDocument.setDocumentID(document);
                    truckDocument.setTruckID(truck);
                    ges.getEntityManager().persist(truckDocument);
                    ges.closeEm();

                    if (!isPresentPrincipalPicture) {
                        truck.setPicture(document);
                        isPresentPrincipalPicture = true;
                    }

                    truckDocumentList.add(truckDocument);
                    truck.getTruckDocumentList().add(truckDocument);
                    document.getTruckDocumentList().add(truckDocument);

                    ges.getEntityManager().merge(truck);
                    ges.getEntityManager().merge(document);

                    ges.closeEm();

                } else {
                    res += ";invalidPictureTruckID=" + documentID;
                }

            } catch (NumberFormatException err) {
                logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+err.getMessage());
                res += ";badPictureTruckID=" + documentID;
            } catch (NullPointerException er) {
                if (truck.getTruckDocumentList() == null || truck.getTruckDocumentList().isEmpty()) {
                    truck.setTruckDocumentList(truckDocumentList);
                }
                if (document.getTruckDocumentList() == null || document.getTruckDocumentList().isEmpty()) {
                    document.setTruckDocumentList(truckDocumentList);
                }
                //res += ";NullPointerException";
            }

        }

        result.setMsg(res);

        //Verifier si ce truck se trouve dans une zone non couverte
        try {

            areaNotCoveredList = (List<AreaNotCovered>) ges.getEntityManager().createQuery(""
                    + "SELECT a FROM AreaNotCovered a WHERE a.zipCode = :zipCode AND a.status = FALSE"
                    + "").setParameter("zipCode", truckZipCode).getResultList();

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
            result.setMsg(res);
            result.afficherResult("addNewTruck");
            return result;
        }

        if (areaNotCoveredList != null && !areaNotCoveredList.isEmpty()) {
            for (AreaNotCovered areaNotCovered : areaNotCoveredList) {
                areaNotCovered.setStatus(true);
                ges.getEntityManager().merge(areaNotCovered);

                res = "" + areaNotCovered.getEmail();
                resultList.add(res);
            }
            result.setObjectList(resultList);
            ges.closeEm();
        }

        result.afficherResult("addNewTruck");
        return result;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfTruckByAccountID(int accountID, int index, int nombreMaxResultat) {
        List<Truck> lt = null;
        Result result = new Result();
        List<String> lts = new ArrayList<>();
        int size = 0;

        try {
            ges.creatEntityManager();

            Query query = ges.getEntityManager().createQuery("SELECT t FROM Truck t WHERE t.deleted = false AND "
                    + "t.userID IN (SELECT u FROM User u WHERE u.accountID IN ("
                    + " SELECT a FROM Account a WHERE a.accountID = :accountID)"
                    + ") ORDER BY t.creationDate DESC");

            query.setParameter("accountID", accountID);
            lt = (List<Truck>) query.getResultList();
            size = lt.size();

            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResultat);

            lt = (List<Truck>) query.getResultList();
            result.setMsg("good");

        } catch (Throwable th) {
            if (ges == null) {
                result.setMsg("Could not create Entity Manager\n" + th.getMessage());
                return result;
            }
            result.setMsg(th.getMessage());
        }

        if (lt != null && !lt.isEmpty()) {
            String res = "";
            for (Truck truck : lt) {
                if (!truck.getDeleted()) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(truck.getTrucktypeID().getYear());

                    String driverInfo;
                    driverInfo = DriverFunction.getDriverForTruck(truck);

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
                    res = truck.getTruckID() + ";"
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
                            + "" + ";"
                            + truck.getYear() + ";"
                            + "" + ";"
                            + "" + ";"
                            + "" + ";"
                            + truck.getTruckaxleID().getTruckaxleID() + ";"
                            + driverInfo + ";"
                            + pictureTrucksID + ";"
                            + pictureInsurancesID + ";"
                            + truck.getTruckDescription() + ";"
                            + "null";
                    lts.add(res);
                }

            }
            lts.add("" + size);
            result.setObjectList(lts);
        }

        return result;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getAllTruckByAccountID(int accountID) {
        List<Truck> lt = null;
        Result result = new Result();
        List<String> lts = new ArrayList<>();

        try {
            ges.creatEntityManager();
            Query query = ges.getEntityManager().createQuery("SELECT t FROM Truck t WHERE t.deleted = false AND "
                    + "t.userID IN (SELECT u FROM User u WHERE u.accountID IN ("
                    + " SELECT a FROM Account a WHERE a.accountID = :accountID)"
                    + ") ORDER BY t.creationDate DESC");

            query.setParameter("accountID", accountID);

            lt = (List<Truck>) query.getResultList();
            result.setMsg("good");

        } catch (Throwable th) {
            if (ges == null) {
                result.setMsg("Could not create Entity Manager\n" + th.getMessage());
                return result;
            }
            result.setMsg(th.getMessage());
        }

        if (lt != null && !lt.isEmpty()) {
            String res;
            for (Truck truck : lt) {
                Calendar c = Calendar.getInstance();
                c.setTime(truck.getTrucktypeID().getYear());

                String driverInfo = DriverFunction.getDriverForTruck(truck);

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

                res = truck.getTruckID() + ";"
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
                        + "" + ";"
                        + truck.getYear() + ";"
                        + "" + ";"
                        + "" + ";"
                        + "" + ";"
                        + truck.getTruckaxleID().getTruckaxleID() + ";"
                        + driverInfo + ";"
                        + pictureTrucksID + ";"
                        + pictureInsurancesID + ";"
                        + truck.getTruckDescription() + ";"
                        + "null";
                lts.add(res);
            }
            int size = lts.size();
            lts.add("" + size);
            result.setObjectList(lts);
        }

        return result;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getTruckDetail(int accountID, int truckID) {
        List<Truck> lt = null;
        Result result = new Result();
        List<String> lts = new ArrayList<>();

        try {

            ges.creatEntityManager();

            Query query = ges.getEntityManager().createQuery("SELECT t FROM Truck t WHERE  t.deleted = false AND "
                    + " t.truckID = :truckID AND t.userID IN ("
                    + " SELECT u FROM User u WHERE u.accountID IN ("
                    + " SELECT a FROM Account a WHERE a.accountID = :accountID)"
                    + ") ORDER BY t.creationDate DESC");

            query.setParameter("accountID", accountID);
            query.setParameter("truckID", truckID);

            lt = (List<Truck>) query.getResultList();
            result.setMsg("good");

        } catch (Throwable th) {
            if (ges == null) {
                result.setMsg("Could not create Entity Manager\n" + th.getMessage());
                return result;
            }
            result.setMsg(th.getMessage());
        }

        if (lt != null && !lt.isEmpty()) {
            String res = "";
            for (Truck truck : lt) {
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

                res = truck.getTruckID() + ";"
                        + truck.getLocationPrice() + ";"
                        + truck.getAvailable() + ";"
                        + pictureTrucksPATH + ";"
                        + truck.getLenghtofbedID().getLenghtofbedID() + ";"
                        + truck.getDistance() + ";"
                        + truck.getPhoneNumber() + ";"
                        + truck.getTruckZipCode() + ";"
                        + truck.getDOTNumber() + ";"
                        + truck.getGeneralLiability() + ";"
                        + truck.getInsuranceLiability() + ";"
                        + pictureInsurancesPATH + ";"
                        + truck.getYear() + ";"
                        + "" + ";"
                        + "" + ";"
                        + "" + ";"
                        + truck.getTruckaxleID().getTruckaxleID() + ";"
                        + pictureTrucksID + ";"
                        + pictureInsurancesID + ";"
                        + truck.getTruckDescription() + ";"
                        + "null";
                lts.add(res);
            }
            int size = lts.size();
            lts.add("" + size);
            result.setObjectList(lts);
        }

        return result;
    }

    @Override
    public Result modifyTruckInfo(int truckID, String pictureTrucksID, String truckNumber, double locationPrice,
            int accountID, String year, String make, String model, int truckAxleID,
            int lenghtOfBedID, int distance, String phoneNumber, String truckZipCode,
            String dOTNumber, int generalLiability, int insuranceLiability, String pictureInsurancesID, 
            String truckDescription, float latitude, float longitude) {

        String res = "good";
        Result result = new Result();
        Truck truck = null;
        TruckAxle truckAxle = null;
        Account account = null;
        TruckType truckType = null;
        String date;

        TruckDocument truckDocument;
        Document document = null;
        List<TruckDocument> truckDocumentList;
        List<Document> documentList;

        LenghtOfBed lenghtOfBed = null;
        String error = "";
        List<Truck> trucks = null;
        List<AreaNotCovered> areaNotCoveredList = null;
        List<String> resultList = new ArrayList<>();

        try {

            ges.creatEntityManager();

            try {

                account = (Account) ges.getEntityManager().find(Account.class, accountID);
                date = "01-01-2013 00:00:00";
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                Date day = null;
                day = sdf.parse(date);
                String query = "SELECT t FROM TruckType t WHERE t.make = :make AND t.model = :model AND t.year = :year";
                truckType = (TruckType) ges.getEntityManager().createQuery(query).setParameter("make", "Toyota").setParameter("model", "Venza").setParameter("year", day).getSingleResult();

                truck = (Truck) ges.getEntityManager().find(Truck.class, truckID);
                truckAxle = (TruckAxle) ges.getEntityManager().find(TruckAxle.class, truckAxleID);
                lenghtOfBed = (LenghtOfBed) ges.getEntityManager().find(LenghtOfBed.class, lenghtOfBedID);
                trucks = (List<Truck>) ges.getEntityManager().createQuery(""
                        + "SELECT t FROM Truck t WHERE t.truckNumber = :truckNumber AND t.deleted = FALSE").setParameter("truckNumber", truckNumber).getResultList();

            } catch (ParseException ex) {
                //Logger.getLogger(TruckTypeManagementDao.class.getName()).log(Level.SEVERE, null, ex);
                 logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+ex.getMessage());
               
            
            
            } catch (Throwable th) {
                error = th.getMessage();
            }

            if (truck == null) {
                ges.closeEm();
                result.setMsg("UnExistingTruckID\n\n" + error);
                return result;
            }
            if (account == null) {
                ges.closeEm();
                result.setMsg("UnExistingAccount\n\n" + error);
                return result;
            }
            if (truckType == null) {
                ges.closeEm();
                result.setMsg("ExistingtruckType\n\n" + error);
                return result;
            }
            if (truckAxle == null) {
                ges.closeEm();
                result.setMsg("NotExistingTruckAxle\n\n" + error);
                return result;
            }
            if (lenghtOfBed == null) {
                ges.closeEm();
                result.setMsg("NotExistingLenghtOfBed\n\n" + error);
                return result;
            }

            if (!truck.getTruckNumber().equals(truckNumber) && trucks != null && !trucks.isEmpty()) {
                ges.closeEm();
                result.setMsg("ExistingTruckNumber;" + error);
                return result;
            }

            truck.setTruckNumber(truckNumber);
            truck.setTruckDescription(truckDescription);
            truck.setLocationPrice((float) locationPrice);
            truck.setTrucktypeID(truckType);
            truck.setUserID(account.getUser());
            //truck.setPicture(pictureTrucksID);

            truck.setLenghtofbedID(lenghtOfBed);
            truck.setDistance(distance);
            truck.setPhoneNumber(phoneNumber);
            //truck.setPicture(pictureTrucksID);
            truck.setYear(year);
            truck.setTruckZipCode(truckZipCode);
            truck.setLatitude(latitude);
            truck.setLongitude(longitude);
            truck.setDOTNumber(dOTNumber);
            truck.setGeneralLiability(generalLiability);
            truck.setInsuranceLiability(insuranceLiability);
            //truck.setPictureInsurance(pictureInsurancesID);

            truck.setTruckaxleID(truckAxle);
            ges.getEntityManager().merge(truck);
            account.getUser().getTruckList().add(truck);
            ges.getEntityManager().merge(account);
            ges.closeEm();

            res = "good";
            res += ";" + truck.getTruckID() + ";" + truck.getLocationPrice() + ";"
                    + truck.getUserID().getName();

        } catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            result.setMsg(res);
            return result;
        }

        // Editions des documents du truck (TruckDocument)
        /**
         * Le principe est le suivant: + On supprime initiale tous les documents
         * du truck et ceci se fait en positionnant le champ deleted des
         * truckDocuments a True + Pour chaque document dont l'ID a ete passé en
         * paramatre: -- si le document appartenait deja au truck, on remet le
         * champ deleted du truckDocument a FALSE -- sinon on cree un nouveau
         * truckDocument et on positionne les parametres.
         *
         */
        documentList = new ArrayList<>();
        for (TruckDocument truckDoc : truck.getTruckDocumentList()) {
            truckDoc.setDeleted(true);
            ges.getEntityManager().merge(truckDoc);

            documentList.add(truckDoc.getDocumentID());

        }
        ges.closeEm();

        String[] documentIDList = (pictureInsurancesID + "#" + pictureTrucksID).split("#");

        boolean isPresentPrincipalPicture = (truck.getPicture() != null && pictureTrucksID.contains("" + truck.getPicture().getDocumentID()));
        boolean isPresentPrincipalInsurance = (truck.getPictureInsurance() != null && pictureInsurancesID.contains("" + truck.getPictureInsurance().getDocumentID()));

        truckDocumentList = new ArrayList<>();
        for (String documentID : documentIDList) {
            try {

                document = ges.getEntityManager().find(Document.class, Integer.parseInt(documentID));
                if (document != null) {
                    document.setDescription("TruckDocument");
                    if (documentList.contains(document)) {
                        truckDocument = truck.getTruckDocumentList().get(documentList.indexOf(document));
                        truckDocument.setDeleted(false);
                        document.setDeleted(false);

                        if (!isPresentPrincipalPicture && truckDocument.getType().equals("pictureTruck")) {
                            truck.setPicture(document);
                            isPresentPrincipalPicture = true;
                            ges.getEntityManager().merge(truck);
                        } else if (!isPresentPrincipalInsurance && truckDocument.getType().equals("pictureInsurance")) {
                            truck.setPicture(document);
                            isPresentPrincipalInsurance = true;
                            ges.getEntityManager().merge(truck);
                        }

                        ges.getEntityManager().merge(document);
                        ges.getEntityManager().merge(truckDocument);
                        ges.closeEm();

                    } else {
                        truckDocument = new TruckDocument();
                        truckDocument.setCreationDate(DateFunction.getGMTDate());
                        truckDocument.setDeleted(false);
                        if (pictureInsurancesID.contains(documentID)) {

                            truckDocument.setType("pictureInsurance");
                            if (!isPresentPrincipalInsurance) {
                                truck.setPictureInsurance(document);
                                isPresentPrincipalInsurance = true;
                            }
                        } else {

                            truckDocument.setType("pictureTruck");
                            if (!isPresentPrincipalPicture) {
                                truck.setPicture(document);
                                isPresentPrincipalPicture = true;
                            }

                        }
                        truckDocument.setDocumentID(document);
                        truckDocument.setTruckID(truck);
                        ges.getEntityManager().persist(truckDocument);
                        ges.closeEm();

                        truckDocumentList.add(truckDocument);
                        truck.getTruckDocumentList().add(truckDocument);
                        document.getTruckDocumentList().add(truckDocument);

                        ges.getEntityManager().merge(truck);
                        ges.getEntityManager().merge(document);
                        ges.closeEm();
                    }

                } else {
                    res += ";invalidDocumentID=" + documentID;
                }

            } catch (NumberFormatException err) {
                logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+err.getMessage());
                res += ";badDocumentID=" + documentID;
            } catch (NullPointerException er) {
                if (truck.getTruckDocumentList() == null || truck.getTruckDocumentList().isEmpty()) {
                    truck.setTruckDocumentList(truckDocumentList);
                }
                if (document.getTruckDocumentList() == null || document.getTruckDocumentList().isEmpty()) {
                    document.setTruckDocumentList(truckDocumentList);
                }
                //res += ";NullPointerException";
            }

        }
        result.setMsg(res);

        //Verifier si ce truck se trouve dans une zone non couverte
        try {

            areaNotCoveredList = (List<AreaNotCovered>) ges.getEntityManager().createQuery(""
                    + "SELECT a FROM AreaNotCovered a WHERE a.zipCode = :zipCode AND a.status = FALSE").setParameter("zipCode", truckZipCode).getResultList();

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
            result.setMsg(res);
            return result;
        }

        if (areaNotCoveredList != null && !areaNotCoveredList.isEmpty()) {
            for (AreaNotCovered areaNotCovered : areaNotCoveredList) {
                areaNotCovered.setStatus(true);
                ges.getEntityManager().merge(areaNotCovered);

                res = "" + areaNotCovered.getEmail();
                resultList.add(res);
            }
            result.setObjectList(resultList);
            ges.closeEm();
        }

        return result;
    }

    @Override
    public String deleteTruck(int truckID) throws TruckManagementException{

        String res = "good";
        Truck truck = null;
        List<Drive> drives;
        try {

            ges.creatEntityManager();
            truck = (Truck) ges.getEntityManager().find(Truck.class, truckID);
            truck.setDeleted(true);

            drives = (List<Drive>) ges.getEntityManager().createQuery("SELECT d FROM Drive d "
                    + "WHERE d.deleted = FALSE AND d.truckID = :truckID").setParameter("truckID", truck).getResultList();

            for (Drive drive : drives) {
                drive.setDeleted(true);
                ges.getEntityManager().merge(drive);
            }
            ges.getEntityManager().merge(truck);
            ges.closeEm();

        } catch (Throwable th) {
            //return "NotExistingTruck\n" + th.getMessage();
             throw new TruckManagementException(getClass()+"","deleteTruck",1,"NotExistingTruck\n" + th.getMessage(),th.getMessage());
        
        }

        return res;
    }

    @Override
    public String deleteTruckDocument(int truckID, int documentID) throws TruckManagementException{

        String res = "good";
        Truck truck = null;
        Document document;

        try {

            ges.creatEntityManager();
            truck = (Truck) ges.getEntityManager().find(Truck.class, truckID);

            document = (Document) ges.getEntityManager().find(Document.class, documentID);
            if (document != null) {

                for (TruckDocument truckDocument : document.getTruckDocumentList()) {
                    truckDocument.setDeleted(true);
                    ges.getEntityManager().merge(truckDocument);
                }

                document.setDeleted(true);
                ges.getEntityManager().merge(document);

            }
            ges.getEntityManager().merge(truck);
            ges.closeEm();

        } catch (Throwable th) {
              throw new TruckManagementException(getClass()+"","deleteTruckDocument",1,"NotExistingTruck\n" + th.getMessage(),th.getMessage());
        
            //return "NotExistingTruck\n" + th.getMessage();
        }

        return res;
    }

    @Override
    public String addTruckDocument(int accountID, int truckID, List<String> documentsID) throws TruckManagementException{

        String res = "good";
        Truck truck = null;
        List<Drive> drives;
        try {

            ges.creatEntityManager();
            truck = (Truck) ges.getEntityManager().find(Truck.class, truckID);
            truck.setDeleted(true);

            drives = (List<Drive>) ges.getEntityManager().createQuery("SELECT d FROM Drive d "
                    + "WHERE d.deleted = FALSE AND d.truckID = :truckID").setParameter("truckID", truck).getResultList();

            for (Drive drive : drives) {
                drive.setDeleted(true);
                ges.getEntityManager().merge(drive);
            }
            ges.getEntityManager().merge(truck);
            ges.closeEm();

        } catch (Throwable th) {
             throw new TruckManagementException(getClass()+"","addTruckDocument",1,"NotExistingTruck\n" + th.getMessage(),th.getMessage());
        
            //return "NotExistingTruck\n" + th.getMessage();
        }

        return res;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getAllTruckAxleName() {
        List<TruckAxle> lt = null;
        Result result = new Result();
        List<String> lts = new ArrayList<>();

        try {
            ges.creatEntityManager();
            lt = (List<TruckAxle>) ges.getEntityManager().createNamedQuery("TruckAxle.findAll").getResultList();
            result.setMsg("good");

        } catch (Throwable th) {
            if (ges == null) {
                result.setMsg("Could not create Entity Manager\n" + th.getMessage());
                return result;
            }
            result.setMsg(th.getMessage());
        }

        if (lt != null && !lt.isEmpty()) {
            String res = "";
            for (TruckAxle truckAxle : lt) {
                res = truckAxle.getTruckAxleName();
                lts.add(res);
            }
            result.setObjectList(lts);
        }
        return result;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getTruckInfoByAccountID(int accountID, int truckID) {
        List<Truck> lt = null;
        Result result = new Result();
        List<String> lts = new ArrayList<>();

        try {

            ges.creatEntityManager();

            Query query = ges.getEntityManager().createQuery("SELECT t FROM Truck t WHERE  t.deleted = false AND "
                    + " t.truckID = :truckID AND t.userID IN ("
                    + " SELECT u FROM User u WHERE u.accountID IN ("
                    + " SELECT a FROM Account a WHERE a.accountID = :accountID)"
                    + ") ORDER BY t.creationDate DESC");

            query.setParameter("accountID", accountID);
            query.setParameter("truckID", truckID);

            lt = (List<Truck>) query.getResultList();
            result.setMsg("good");

        } catch (Throwable th) {
            if (ges == null) {
                result.setMsg("Could not create Entity Manager\n" + th.getMessage());
                return result;
            }
            result.setMsg(th.getMessage());
        }

        if (lt != null && !lt.isEmpty()) {
            String res = "";
            for (Truck truck : lt) {
                Calendar c = Calendar.getInstance();
                c.setTime(truck.getTrucktypeID().getYear());
                String drive;
                if (truck.getDriveList() != null && !truck.getDriveList().isEmpty()) {
                    Drive driveBinding = truck.getDriveList().get(0);
                    if (!driveBinding.getDeleted()) {
                        drive = "" + driveBinding.getDriveID() + ";";
                        drive += "" + truck.getDriveList().get(0).getDriverID().getDriverID();
                    } else {
                        drive = "-1;-1";
                    }
                } else {
                    drive = "-1;-1";
                }

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

                res = truck.getTruckID() + ";"//0
                        + truck.getTruckNumber() + ";"
                        + truck.getLocationPrice() + ";"
                        + pictureTrucksPATH + ";"
                        + truck.getLenghtofbedID().getLenghtofbedID() + ";"
                        + truck.getDistance() + ";"//5
                        + truck.getPhoneNumber() + ";"
                        + truck.getTruckZipCode() + ";"
                        + truck.getDOTNumber() + ";"
                        + truck.getGeneralLiability() + ";"
                        + truck.getInsuranceLiability() + ";"//10
                        + pictureInsurancesPATH + ";"
                        + drive + ";"//12-13
                        + truck.getAvailable() + ";"//14
                        + truck.getYear() + "#"
                        + "" + "#"
                        + "" + ";"//15
                        + truck.getTruckaxleID().getTruckaxleID() + ";"
                        + pictureTrucksID + ";"
                        + pictureInsurancesID + ";"
                        + truck.getTruckDescription() + ";"
                        + truck.getLatitude()+ ";"//20
                        + truck.getLongitude()+ ";"
                        + "null";
                lts.add(res);
            }
            int size = lts.size();
            lts.add("" + size);
            result.setObjectList(lts);
        }

        return result;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getTruckNumberByTruckOwner(int truckOwnerID) {

        Account account = null;
        Result result = new Result();
        result.setMsg("good");

        try {
            ges.creatEntityManager();
            account = (Account) ges.getEntityManager().find(Account.class, truckOwnerID);

        } catch (Throwable e) {
        }

        if (account == null) {
            result.setMsg("InvalidAccountID");
            return result;
        }

        result.setObject("" + (account.getUser().getTruckNumber() + 1));
        return result;

    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result countAllTrucks() {
        Result result = new Result();
        try {
            ges.creatEntityManager();
            Query query = ges.getEntityManager().createQuery("SELECT t FROM Truck t where t.deleted = FALSE");
            result.setObject("" + query.getResultList().size());
            result.setMsg("good");
        } catch (Throwable e) {
            result.setMsg("bad");

        }

        return result;
    }

    @Override
    public String addNewAreaNotCovered(String email, String zipCode, String date)throws TruckManagementException {

        String res = "good";
        AreaNotCovered areaNotCovered = null;
        try {
            try {
                ges.creatEntityManager();
                areaNotCovered = (AreaNotCovered) ges.getEntityManager().createQuery("SELECT d FROM AreaNotCovered d  "
                        + "WHERE d.deleted = FALSE AND d.zipCode = :zipCode AND d.email = :email").setParameter("zipCode", zipCode).setParameter("email", email).getSingleResult();

            } catch (Throwable th) {
            }

            if (areaNotCovered != null) {
                return "alreadyExistAreaNotCovered\n";
            }

            areaNotCovered = new AreaNotCovered();
            areaNotCovered.setEmail(email);
            areaNotCovered.setZipCode(zipCode);
            areaNotCovered.setDate(date);
            areaNotCovered.setCreationDate(DateFunction.getGMTDate());
            areaNotCovered.setDeleted(false);
            areaNotCovered.setStatus(false);

            ges.getEntityManager().persist(areaNotCovered);
            ges.closeEm();

        } catch (Throwable th) {
             throw new TruckManagementException(getClass()+"","addNewAreaNotCovered",1,"NotExistingTruck\n" + th.getMessage(),th.getMessage());
        
            //return "NotExistingTruck\n" + th.getMessage();
        }

        return res;
    }

    /**
     * Cette fonction consiste a rechercher les trucks respectant les
     * caracteristiques passer en parametre.
     *
     * @param truckAxle
     * @param lenghtOfBed
     * @param generalLiability
     * @param insuranceLiability
     * @param startDate
     * @param endDate
     * @param accountID
     * @param ack
     * @return
     *
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Truck> searchTruck(TruckAxle truckAxle, LenghtOfBed lenghtOfBed, int generalLiability, int insuranceLiability, Date startDate, Date endDate, int accountID, StringBuilder ack) {

        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"searchTruck: truckAxleName = " + truckAxle + " -- lenghtOfBedName = " + lenghtOfBed + " -- generalLiability = " + generalLiability + " -- insuranceLiability = " + insuranceLiability + " -- startDate = " + startDate + " -- endDate = " + endDate);
        List<Truck> lt = null;
        List<Integer> truckListID;
        String sTruckListID = "";
        tf = new TruckFunction();

        try {

            ges.creatEntityManager();

            Query query;

            if (accountID == 0) {

                query = ges.getEntityManager().createQuery("SELECT t FROM Truck t WHERE  t.deleted = false AND t.lenghtofbedID IN (SELECT l FROM LenghtOfBed l WHERE l.sortField >= :slenghtOfBed) AND t.truckaxleID NOT IN (SELECT a FROM TruckAxle a WHERE a.sortField < :struckAxleName) ");
                query.setParameter("slenghtOfBed", lenghtOfBed.getSortField());
                query.setParameter("struckAxleName", truckAxle.getSortField());
                lt = (List<Truck>) query.getResultList();

            } else {

                Account account = ges.getEntityManager().find(Account.class, accountID);
                query = ges.getEntityManager().createQuery("SELECT t FROM Truck t WHERE  t.deleted = false AND t.userID = :userID AND t.lenghtofbedID IN (SELECT l FROM LenghtOfBed l WHERE l.sortField >= :slenghtOfBed) AND t.truckaxleID NOT IN (SELECT a FROM TruckAxle a WHERE a.sortField < :struckAxleName) ");
                query.setParameter("slenghtOfBed", lenghtOfBed.getSortField());
                query.setParameter("struckAxleName", truckAxle.getSortField());
                query.setParameter("userID", account.getUser());
                lt = (List<Truck>) query.getResultList();

            }

            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"searchTruck: Voici la liste de tous les trucks selectionnés");
            //logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"searchTruck: lt = "+lt);

            if (lt == null || lt.isEmpty()) {
                ack.append("##LenghtOfBedOrTruckType-No");
                return lt;
            }

            truckListID = new ArrayList<>();
            for (Truck truck : lt) {
                truckListID.add(truck.getTruckID());
            }

            lt = ges.getEntityManager().createQuery("SELECT t FROM Truck t WHERE t.deleted = FALSE AND t IN :truckListID AND t.generalLiability >= :generalLiability AND t.insuranceLiability >= :insuranceLiability ")
                    .setParameter("truckListID", truckListID)
                    .setParameter("insuranceLiability", insuranceLiability)
                    .setParameter("generalLiability", generalLiability)
                    .getResultList();

            if (lt == null || lt.isEmpty()) {
                ack.append("##TruckLiability-No");
                return lt;
            }

            for (Truck truck : lt) {
                sTruckListID += ", " + truck.getTruckID();
            }

            //Pour gérer le cas la liste des trucks 'lt' est vide
            sTruckListID = "(" + (sTruckListID.equals("") ? "0" : sTruckListID.substring(1)) + ")";

            //logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+" Avant   lt = " + lt);
            lt = ges.getEntityManager().createNativeQuery(TruckSearchQuery.getQueryForSearchAllAvailableTrucksINDateRange(sTruckListID), Truck.class)
                    .setParameter(1, startDate)
                    .setParameter(2, endDate)
                    .setParameter(3, startDate)
                    .setParameter(4, endDate)
                    .getResultList();

            //logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+" Soli lt = " + lt);
            if (lt == null || lt.isEmpty()) {
                ack.append("##startDateOrEndDate-No");
                return lt;
            }

            //logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"searchTruck: Voici la liste de tous les trucks selectionnés apres application des differents filtre ...");
            //logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"searchTruck: lt = "+lt);
        } catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            String res = string.toString();
            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+res);
        }

        return lt;

    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getTruckByJobCharacteristics(int jobID) {
        List<Truck> lt = null;
        Result result = new Result();
        List<String> lts = new ArrayList<>();
        Job job = null;

        try {
            ges.creatEntityManager();

            job = (Job) ges.getEntityManager().createQuery("SELECT j FROM Job j WHERE j.deleted = FALSE AND j.jobID = :jobID").setParameter("jobID", jobID).getSingleResult();

        } catch (Throwable th) {
            if (ges == null) {
                result.setMsg("Could not create Entity Manager\n" + th.getMessage());
                return result;
            }
            result.setMsg(th.getMessage());
        }

        if (job == null) {
            result.setMsg("InvalidJobID");
            return result;
        }

        TruckAxle truckAxleName = job.getTruckAxle();
        LenghtOfBed lenghtOfBedName = job.getLenghtOfBed();
        int generalLiability = job.getGeneralLiability();
        int insuranceLiability = job.getTruckLiability();
        Date startDate = job.getStartDate();
        Date endDate = job.getEndDate();
        StringBuilder ack = new StringBuilder("");

        lt = this.searchTruck(truckAxleName, lenghtOfBedName, generalLiability, insuranceLiability, startDate, endDate, 0, ack);

        result.setMsg("good");
        result.setObject((ack.toString().equals("") ? result.getMsg() : ack.toString()));

        if (lt != null && !lt.isEmpty()) {
            String res = "";
            for (Truck truck : lt) {
                if (!truck.getDeleted()) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(truck.getTrucktypeID().getYear());

                    String driverInfo = DriverFunction.getDriverForTruck(truck);
                    
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
                    res = truck.getTruckID() + ";"
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
                            + "" + ";"
                            + truck.getYear() + ";"
                            + "" + ";"
                            + "" + ";"
                            + "" + ";"
                            + truck.getTruckaxleID().getTruckaxleID() + ";"
                            + driverInfo + ";"
                            + pictureTrucksID + ";"
                            + pictureInsurancesID + ";"
                            + truck.getTruckDescription() + ";"
                            + "null";
                    lts.add(res);
                }

            }

            result.setObjectList(lts);
        }

        return result;

    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getUserTruckByJobCharacteristics(int accountID, int jobID) {
        List<Truck> lt = null;
        Result result = new Result();
        List<String> lts = new ArrayList<>();
        Job job = null;

        try {
            ges.creatEntityManager();

            job = (Job) ges.getEntityManager().createQuery("SELECT j FROM Job j WHERE j.deleted = FALSE AND j.jobID = :jobID").setParameter("jobID", jobID).getSingleResult();

        } catch (Throwable th) {
            if (ges == null) {
                result.setMsg("Could not create Entity Manager\n" + th.getMessage());
                return result;
            }
            result.setMsg(th.getMessage());
        }

        if (job == null) {
            result.setMsg("InvalidJobID");
            return result;
        }

        TruckAxle truckAxleName = job.getTruckAxle();
        LenghtOfBed lenghtOfBedName = job.getLenghtOfBed();
        int generalLiability = job.getGeneralLiability();
        int insuranceLiability = job.getTruckLiability();
        Date startDate = job.getStartDate();
        Date endDate = job.getEndDate();
        StringBuilder ack = new StringBuilder("");

        lt = this.searchTruck(truckAxleName, lenghtOfBedName, generalLiability, insuranceLiability, startDate, endDate, accountID, ack);

        result.setMsg("good");
        result.setObject((ack.toString().equals("") ? result.getMsg() : ack.toString()));

        if (lt != null && !lt.isEmpty()) {
            String res;
            for (Truck truck : lt) {
                if (!truck.getDeleted()) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(truck.getTrucktypeID().getYear());

                    String driverInfo = DriverFunction.getDriverForTruck(truck);

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
                    res = truck.getTruckID() + ";"
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
                            + "" + ";"
                            + truck.getYear() + ";"
                            + "" + ";"
                            + "" + ";"
                            + "" + ";"
                            + truck.getTruckaxleID().getTruckaxleID() + ";"
                            + driverInfo + ";"
                            + pictureTrucksID + ";"
                            + pictureInsurancesID + ";"
                            + truck.getTruckDescription() + ";"
                            + "null";
                    lts.add(res);
                }

            }

            result.setObjectList(lts);
        }

        return result;

    }

    @Deprecated
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public String getDetailOfTruck(int truckID)throws TruckManagementException {
        Truck truck = null;
        String res = "bad";
        try {

            ges.creatEntityManager();

            Query query = ges.getEntityManager().createQuery("SELECT t FROM Truck t WHERE t.truckID = :truckID AND t.deleted = FALSE");

            query.setParameter("truckID", truckID);

            truck = (Truck) query.getSingleResult();

        } catch (Throwable th) {
            if (ges == null) {
                 throw new TruckManagementException(getClass()+"","getDetailOfTruck",1,"bad",th.getMessage());
        
               // return "bad";
            }
        }

        String pictureTrucksID = "";
        String pictureTrucksPATH = "";

        if (truck != null) {

            List<TruckDocument> truckDocumentList = ges.getEntityManager().createQuery("SELECT t FROM TruckDocument t WHERE t.deleted = FALSE AND t.truckID = :truckID AND t.documentID NOT IN (SELECT d FROM Document d WHERE d.deleted = TRUE) ").setParameter("truckID", truck).getResultList();

            for (TruckDocument truckDocument : truckDocumentList) {
                if (truckDocument.getType().equals("pictureTruck")) {
                    pictureTrucksID += "#" + truckDocument.getDocumentID().getDocumentID();
                    pictureTrucksPATH += "#" + truckDocument.getDocumentID().getPathName();
                }
            }
            try {

                pictureTrucksID = pictureTrucksID.substring(1, pictureTrucksID.length());
                pictureTrucksPATH = pictureTrucksPATH.substring(1, pictureTrucksPATH.length());
            } catch (Exception e) {

            }

            res = truck.getTruckID() + ";"
                    + truck.getDOTNumber() + ";"
                    + truck.getDistance() + ";"
                    + truck.getGeneralLiability() + ";"
                    + truck.getInsuranceLiability() + ";"
                    + truck.getTruckDescription() + ";"
                    + truck.getYear() + ";"
                    + truck.getLenghtofbedID().getLenghtofbedID() + ";"
                    + truck.getTruckaxleID().getTruckaxleID() + ";"
                    + truck.getTruckNumber() + ";"
                    + truck.getInsuranceLiability() + ";"
                    + pictureTrucksPATH + ";"
                    + "null";
        }

        return res;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getListOfGeneralLiability() {
        List<String> generalLiability;
        ges.creatEntityManager();
        Result result = new Result();

        try {
            Query query = ges.getEntityManager().createQuery("SELECT DISTINCT t.generalLiability FROM Truck t WHERE t.deleted = FALSE ORDER BY t.generalLiability ASC");
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"Exécution de la requete terminée!");
            generalLiability = (List<String>) query.getResultList();
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"Récupération du résultat: " + generalLiability);
            result.setMsg("good");
            result.setObjectList(generalLiability);
            ges.closeEm();
        } catch (Exception e) {
            String res;
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
            result.setMsg(res);
        }

        return result;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getListOfInsuranceLiability() {
        List<String> insuranceLiability;

        Result result = new Result();
        ges.creatEntityManager();
        try {
            Query q = ges.getEntityManager().createQuery("SELECT DISTINCT t.insuranceLiability FROM Truck t WHERE t.deleted = FALSE ORDER BY t.insuranceLiability ASC");
            insuranceLiability = (List<String>) q.getResultList();
            result.setMsg("good");
            result.setObjectList(insuranceLiability);
            ges.closeEm();

        } catch (Exception e) {
            result.setMsg("bad");
        }

        return result;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfAllAvaillabilityTrucks(Date startDate, Date endDate, String truckZipCode, int distance_Within, 
            boolean excludeWeekend, int sortParam, int dotNumber, float latitude, float longitude, float latMin,  float latMax, 
             float lonMin, float lonMax, int index, int nombreMaxResult, String attribut_session, int ORDER, int methodID) {

        long debut = System.currentTimeMillis();
        tf = new TruckFunction();

        List<Object[]> informationList = null;
        Result result = new Result();
        List<String> listResult = new ArrayList<>();
        int numberOfElts = 0;

        try {

            ges.creatEntityManager();

            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getARangeOfAllAvaillabilityTrucks: startDate = " + startDate + "   ,  endDate = " + endDate + "   ,  "
                    + "latitude = " + latitude + "   ,  longitude = " + longitude + "   ,  distance_Within = " + distance_Within 
                    + " (Miles)"
            );

            Query query = ges.getEntityManager().createNativeQuery(SearchAvailableTruck.getQueryForSearchAllAvailableTrucks(sortParam, excludeWeekend, dotNumber, ORDER), "TruckSearchResults")
                    .setParameter(1, startDate, TemporalType.TIMESTAMP)
                    .setParameter(2, endDate, TemporalType.TIMESTAMP)
                    .setParameter(3, distance_Within)
                    .setParameter(4, latitude)
                    .setParameter(5, longitude)
                    .setParameter(6, latMin)
                    .setParameter(7, latMax)
                    .setParameter(8, lonMin)
                    .setParameter(9, lonMax);
            
            numberOfElts = query.getResultList().size();
            query.setFirstResult(index).setMaxResults(nombreMaxResult);
            informationList = (List<Object[]>) query.getResultList();
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"Temps execution requete sql (ms): " + (System.currentTimeMillis() - debut));

            result.setMsg("good");
            ges.closeEm();

        } catch (Throwable th) {

            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+string);
            result.setMsg(string.toString());
            result.afficherResult("getARangeOfAllAvaillabilityTrucks");
            return result;
        }
        
        String res = "";
        
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getARangeOfAllAvaillabilityTrucks: methodID = " + methodID );
        if (informationList != null && !informationList.isEmpty()) {
            //String informations, picture, dot;
            
            if (methodID == 0) {
                listResult = new ArrayList<>();
                res = FormatDataOfSearchTruckFilter.getHTMLDataOfAvailableTruck(informationList, attribut_session, res);
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getARangeOfAllAvaillabilityTrucks: res = " + res  );
                listResult.add(res);
            } else {
                listResult = FormatDataOfSearchTruckFilter.getOfAvailableTruckDataSList(informationList);
            }
        } else  if (methodID == 0){
            listResult.add(res);
        }
        
        listResult.add(""+numberOfElts);
        result.setObjectList(listResult);
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"Temps execution final de la méthode (ms): " + (System.currentTimeMillis() - debut));
       
        return result;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfAllAvaillabilityTrucksWithoutDistance(Date startDate, Date endDate, String truckZipCode, 
            boolean excludeWeekend, int sortParam, int dotNumber, int index, int nombreMaxResult, String attribut_session, 
            int ORDER, int methodID) {

        long debut = System.currentTimeMillis();
        tf = new TruckFunction();

        List<Object[]> informationList = null;
        Result result = new Result();
        List<String> listResult = new ArrayList<>();
        int numberOfElts = 0;

        try {

            ges.creatEntityManager();

            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getARangeOfAllAvaillabilityTrucksWithoutDistance: startDate = " + startDate + "   ,  endDate = " + endDate + " "
            );

            Query query = ges.getEntityManager().createNativeQuery(SearchAvailableTruck.getQueryForSearchTruckWithoutDistance(sortParam, excludeWeekend, dotNumber, ORDER), "TruckSearchResults")
                    .setParameter(1, startDate, TemporalType.TIMESTAMP)
                    .setParameter(2, endDate, TemporalType.TIMESTAMP)
                    ;
            
            numberOfElts = query.getResultList().size();
            query.setFirstResult(index).setMaxResults(nombreMaxResult);
            informationList = (List<Object[]>) query.getResultList();
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"Temps execution requete sql (ms): " + (System.currentTimeMillis() - debut));

            result.setMsg("good");
            ges.closeEm();

        } catch (Throwable th) {

            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+string);
            result.setMsg(string.toString());
            result.afficherResult("getARangeOfAllAvaillabilityTrucks");
            return result;
        }
        
        String res = "";
        
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getARangeOfAllAvaillabilityTrucksWithoutDistance: methodID = " + methodID );
        if (informationList != null && !informationList.isEmpty()) {
            //String informations, picture, dot;
            if (methodID == 0) {
                listResult = new ArrayList<>();
                res = FormatDataOfSearchTruckFilter.getHTMLDataOfAvailableTruckWithoutDistance(informationList, attribut_session, res);
                listResult.add(res);
            } else {
                listResult = FormatDataOfSearchTruckFilter.getOfAvailableTruckDataSList(informationList);
            }
        
        }else if (methodID == 0){
            listResult.add(res);
        }
        
        listResult.add(""+numberOfElts);
        result.setObjectList(listResult);
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"Temps execution final de la méthode (ms): " + (System.currentTimeMillis() - debut));
       
        return result;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfAvailabilityTrucksFiltered(Date startDate, Date endDate, String truckZipCode, int distance_Within,
            boolean excludeWeekend, int sortParam, int dotNumber, float latitude, float longitude, float latMin, float latMax,
            float lonMin, float lonMax, int truckAxleID, int generalLiability, int insuranceLiability, int index, int nombreMaxResult,
            String attribut_session, int ORDER, int methodID) {

        long debut = System.currentTimeMillis();
        tf = new TruckFunction();
        
        List<Object[]> informationList = null;
        Result result = new Result();
        List<String> listResult = new ArrayList<>();
        int numberOfElts = 0;

        try {

            ges.creatEntityManager();
            
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getARangeOfAvailabilityTrucksFiltered: startDate = " + startDate + "   ,  endDate = " + endDate + "   ,  "
                    + "latitude = " + latitude + "   ,  longitude = " + longitude + "   ,  distance_Within = " + distance_Within 
                    + " (Miles)" + "   ,  truckAxleID = " + truckAxleID  + "   ,  generalLiability = " + generalLiability  + ""
                    + "   , insuranceLiability = " + insuranceLiability 
            );

            
            Query query = ges.getEntityManager().createNativeQuery(SearchTruckFilter.getQueryForSearchAllAvailableTrucksWithFilter(sortParam, excludeWeekend, dotNumber, ORDER), "TruckSearchResults")
                    .setParameter(1, startDate, TemporalType.TIMESTAMP)
                    .setParameter(2, endDate, TemporalType.TIMESTAMP)
                    .setParameter(3, distance_Within)
                    .setParameter(4, latitude)
                    .setParameter(5, longitude)
                    .setParameter(6, latMin)
                    .setParameter(7, latMax)
                    .setParameter(8, lonMin)
                    .setParameter(9, lonMax)
                    .setParameter(10, truckAxleID)
                    .setParameter(11, generalLiability)
                    .setParameter(12, insuranceLiability);

            numberOfElts = query.getResultList().size();
            query.setFirstResult(index).setMaxResults(nombreMaxResult);
            informationList = (List<Object[]>) query.getResultList();
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"Temps execution requete sql (ms): " + (System.currentTimeMillis() - debut));

            result.setMsg("good");
            ges.closeEm();

        } catch (Throwable th) {

            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+string);
            result.setMsg(string.toString());
            return result;
        }

        String res = "";
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getARangeOfAllAvaillabilityTrucksWithoutDistance: methodID = " + methodID );
        if (informationList != null && !informationList.isEmpty()) {

            if (methodID == 0) {
                listResult = new ArrayList<>();
                res = FormatDataOfSearchTruckFilter.getHTMLDataOfAvailableTruckFiltered(informationList, attribut_session, res);
                listResult.add(res);
            } else {
                listResult = FormatDataOfSearchTruckFilter.getOfAvailableTruckDataSList(informationList);
            }
        
        }else if (methodID == 0){
            listResult.add(res);
        }
        
        listResult.add("" + numberOfElts);
        result.setObjectList(listResult);
        
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"Temps execution final de la méthode (ms): " + (System.currentTimeMillis() - debut));
        return result;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfAvailabilityTrucksFilteredWithoutDistance(Date startDate, Date endDate, String truckZipCode,
            boolean excludeWeekend, int sortParam, int dotNumber, int truckAxleID, int generalLiability, 
            int insuranceLiability, int index, int nombreMaxResult, String attribut_session, int ORDER, int methodID) {

        long debut = System.currentTimeMillis();
        tf = new TruckFunction();
        
        List<Object[]> informationList = null;
        Result result = new Result();
        List<String> listResult = new ArrayList<>();
        int numberOfElts = 0;

        try {

            ges.creatEntityManager();

            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getARangeOfAvailabilityTrucksFilteredWithoutDistance: startDate = " + startDate + "   ,  endDate = " + endDate + " "
                    + "" + "   ,  truckAxleID = " + truckAxleID  + "   ,  generalLiability = " + generalLiability  + ""
                    + "   , insuranceLiability = " + insuranceLiability 
            );

            
            Query query = ges.getEntityManager().createNativeQuery(SearchTruckFilter.getQueryForSearchTruckFilterWithoutDistance(sortParam, excludeWeekend, dotNumber, ORDER), "TruckSearchResults")
                    .setParameter(1, startDate, TemporalType.TIMESTAMP)
                    .setParameter(2, endDate, TemporalType.TIMESTAMP)
                    .setParameter(3, truckAxleID)
                    .setParameter(4, generalLiability)
                    .setParameter(5, insuranceLiability);

            numberOfElts = query.getResultList().size();
            query.setFirstResult(index).setMaxResults(nombreMaxResult);
            informationList = (List<Object[]>) query.getResultList();
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"Temps execution requete sql (ms): " + (System.currentTimeMillis() - debut));

            result.setMsg("good");
            ges.closeEm();

        } catch (Throwable th) {

            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+string);
            result.setMsg(string.toString());
            return result;
        }

        String res = "";
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getARangeOfAllAvaillabilityTrucksWithoutDistance: methodID = " + methodID );
        if (informationList != null && !informationList.isEmpty()) {

            if (methodID == 0) {
                listResult = new ArrayList<>();
                res = FormatDataOfSearchTruckFilter.getHTMLDataOfAvailableTruckFilteredWithoutDistance(informationList, attribut_session, res);
                listResult.add(res);
            } else {
                listResult = FormatDataOfSearchTruckFilter.getOfAvailableTruckDataSList(informationList);
            }
        
        }else if (methodID == 0){
            listResult.add(res);
        }
        
        listResult.add("" + numberOfElts);
        result.setObjectList(listResult);
        
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"Temps execution final de la méthode (ms): " + (System.currentTimeMillis() - debut));
        return result;
    }

    
    //Recherche toutes les dates d'invalidités d'un truck à partir de la date du jour.
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getUnavailabilityDatesByTruckID(int truckID) {

        List<Date> availabilityList = null;
        Iterator solicitedTruckList = null;
        //List<TruckBooking> truckBookingList = null;

        List<String> listResult = null;

        Result result = new Result();
        Truck truck = null;
        Date currentDate = DateFunction.getGMTDateMidnight();

        //logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getUnavailabilityDatesByTruckID: firstDateOfYear = " + firstDateOfYear + " And endDateOfYear = " + endDateOfYear + "");
        try {

            ges.creatEntityManager();
            truck = (Truck) ges.getEntityManager().find(Truck.class, truckID);

        } catch (Throwable th) {
            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getUnavailabilityDatesByTruckID: Error " + th.getMessage());
            result.setMsg(th.getMessage());
        }

        if (truck == null) {
            result.setMsg("InvalidTruckID");
            ges.closeEm();
            return result;
        } else if (truck.getDeleted()) {
            result.setMsg("InvalidTruckID");
            return result;
        }

        try {

            Query query;
            query = ges.getEntityManager().createQuery("SELECT DISTINCT a.availbilityDate FROM Availability a WHERE a.deleted = false AND a.state = 1 AND a.availbilityDate >= :currentDate AND a.truckID = :truckID");

            query.setParameter("truckID", truck);
            query.setParameter("currentDate", currentDate);
            availabilityList = (List<Date>) query.getResultList();

            query = ges.getEntityManager().createQuery("SELECT a.startDate, a.endDate FROM SolicitedTruck a WHERE a.deleted = false AND a.truckAvailable = TRUE AND a.endDate >= :currentDate AND a.truckID = :truckID");

            query.setParameter("truckID", truck);
            query.setParameter("currentDate", currentDate);
            solicitedTruckList = query.getResultList().iterator();

            result.setMsg("good");
            ges.closeEm();

        } catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getUnavailabilityDatesByTruckID: Error: \n" + th.getMessage() + "\n" + string.toString());
            result.setMsg(th.getMessage() + "\n" + string.toString());
        }

        String availabilityDate = "";
        listResult = new ArrayList<>();

        if (availabilityList != null && !availabilityList.isEmpty()) {
            for (Date availability : availabilityList) {

                availabilityDate += "#" + availability.getTime();

            }

            availabilityDate = availabilityDate.substring(1);
        }

        listResult.add(availabilityDate);

        //logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getUnavailabilityDatesByTruckID: availabilityDate = " + availabilityDate);
        String solicitedTruckDate = "";
        tf = new TruckFunction();

        if (solicitedTruckList != null && solicitedTruckList.hasNext()) {
            while (solicitedTruckList.hasNext()) {
                Object[] objects = (Object[]) solicitedTruckList.next();

                Date startDate = (Date) objects[0];

                Date endDate = (Date) objects[1];
                //logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getUnavailabilityDatesByTruckID: objects[0] = " + objects[0] + "          objects[1] = " + objects[1]);
                solicitedTruckDate = tf.getAllDayOfDateRangeConcate((startDate.after(currentDate) ? startDate : currentDate), endDate, solicitedTruckDate);

            }

            solicitedTruckDate = solicitedTruckDate.substring(1);

        }

        //logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getUnavailabilityDatesByTruckID: solicitedTruckDate = " + solicitedTruckDate);
        listResult.add(solicitedTruckDate);

        listResult.add("");

        result.setMsg("good");
        result.setObjectList(listResult);
        return result;

    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getTruckDetailFrontEnd(int accountID, int truckID, int index, int nombreMaxResult) {

        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"getTruckDetailFrontEnd: truckID  = " + truckID + " ---> index = " + index + " ---> nombreMaxResult = " + nombreMaxResult);
        Result result = new Result();
        Truck truck;

        result.setMsg("good");
        try {

            ges.creatEntityManager();
            truck = (Truck) ges.getEntityManager().find(Truck.class, truckID);

            if (truck == null || truck.getDeleted()) {
                result.setMsg("InvaLidTruckID");
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
            
            Result resultat = this.getUnavailabilityDatesByTruckID(truck.getTruckID());
            String unavailableDates = resultat.getObjectList().get(0) + "#" + resultat.getObjectList().get(1) + "#" + resultat.getObjectList().get(2);

            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"Truck = " + truck + " ----  unavailableDates = " + unavailableDates);

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
                    + driverLicenses + ";"
                    + driverPictures + ";"
                    + unavailableDates + ";"
                    + "null";
            result.setObject(res);

        } catch (Throwable th) {
            System.err.println("getTruckDetailFrontEnd: Error  = " + th.getMessage());
            result.setMsg("" + th.getMessage());
        }

        result.afficherResult("getTruckDetailFrontEnd");
        return result;
    }


}
