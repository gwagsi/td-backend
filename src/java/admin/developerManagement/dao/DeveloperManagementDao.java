/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package admin.developerManagement.dao;

import entities.Developer;
import entities.Users;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import toolsAndTransversalFunctionnalities.GestionnaireEntite;
import toolsAndTransversalFunctionnalities.Result;
import toolsAndTransversalFunctionnalities.ResultBackend;
import util.date.DateFunction;
import util.exception.DeveloperManagementException;

/**
 *
 * @author erman
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class DeveloperManagementDao implements IDeveloperManagementDaoLocal, IDeveloperManagementDaoRemote{
    
    @EJB
    GestionnaireEntite ges;
    final static org.apache.logging.log4j.Logger logger = LogManager.getLogger("dump1");

    @Override
    public ResultBackend addNewDeveloper(String firstName, String lastName, String login, String password, String email, String timezoneID) throws DeveloperManagementException {
    
        String res = "";
        Users accountUser;
        Developer developer;
                
        ResultBackend result = new ResultBackend();
       
        try{
            
            ges.creatEntityManager();
            
            accountUser = (Users) ges.getEntityManager().find(Users.class, login);
            
            if (accountUser != null) {
                ges.closeEm();
                result.setMsg("ExistingUsername");
                logger.warn("addNewDeveloper: ExistingUsername");
                return result;
            }
            
            try {
                
                accountUser = (Users) ges.getEntityManager().createQuery("SELECT a FROM Users a WHERE a.email = :email")
                        .setParameter("email", email).getSingleResult();
            } catch (Exception e) {
            }

            if (accountUser != null) {
                //ges.closeEm();
                result.setMsg("ExistingEmail");
                logger.warn("addNewDeveloper: ExistingEmail");
                return result;
            }

            accountUser = new Users();
            accountUser.setUsername(login);
            accountUser.setPassword(password);
            accountUser.setEnabled((short) 1);
            accountUser.setEmail(email);
            
            ges.getEntityManager().persist(accountUser);

            developer = new Developer();
            developer.setUsersID(accountUser);
            developer.setFirstName(firstName);
            developer.setLastName(lastName);
            developer.setDeleted(false);
            developer.setCreationDate(DateFunction.getGMTDate());
            developer.setTimezoneID(timezoneID);
            
            ges.getEntityManager().persist(developer);
            ges.closeEm();
            
            String developerInfo = "" + developer.getDeveloperID() + ";"  + developer.getFirstName();
            developerInfo += ";" + developer.getLastName();
            developerInfo += ";" + developer.getUsersID().getEmail();
            developerInfo += ";" + developer.getUsersID();
//            developerInfo += ";" + developer.getLicense();
//            developerInfo += ";" + developer.getUserID().getName();
//            developerInfo += ";" + developer.getUserID().getSurname();
            
            result.setObject(developerInfo);
            
            logger.info("addNewDeveloper: " + res + ", developperID: " + developer.getDeveloperID() + ", developerInfo: " + developerInfo);
            
            List<Object[]> listResult;

            String[] developerMailInfo = {
                developer.getUsersID().getUsername(),//00
                developer.getFirstName(),//01
                developer.getLastName(),
                developer.getUsersID().getEmail(),//03
                developer.getUsersID().getUsername(),//04
                developer.getUsersID().getPassword(),//05
            };
            
            listResult = new ArrayList<>();
            listResult.add(developerMailInfo);
            result.setResultArraysList(listResult);
            
            result.setMsg("good");

        } catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            logger.error(res);
//            result.setMsg(res + th.getMessage());
            throw new DeveloperManagementException("DeveloperManagementDao", "addNewDeveloper", 1, res,res);
//            result.afficherResult("addNewDeveloper");
//            return result;
        }
        return result;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfDeveloper(int index, int nombreMaxResultat) {

        logger.info("getARangeOfDeveloper(info): " + index + ", nombreMaxResultat: " + nombreMaxResultat);
        logger.trace("getARangeOfDeveloper(trace): " + index + ", nombreMaxResultat: " + nombreMaxResultat);
        logger.debug("getARangeOfDeveloper(debug): " + index + ", nombreMaxResultat: " + nombreMaxResultat);
        logger.error("getARangeOfDeveloper(error): " + index + ", nombreMaxResultat: " + nombreMaxResultat);
        List<Developer> developerList;
        Result result = new Result();
        result.setMsg("good");
        List<String> listResult;
        int numberOfElts;

        try {

            ges.creatEntityManager();
            
            Query query = ges.getEntityManager().createQuery("SELECT d FROM Developer d WHERE d.deleted = FALSE ORDER BY d.creationDate DESC");

            developerList = (List<Developer>) query.getResultList();
            numberOfElts = developerList.size();

            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResultat);

            developerList = (List<Developer>) query.getResultList();

            ges.closeEm();

        } catch (Throwable th) {
            logger.warn("getARangeOfDeveloper: InternalError");
            result.setMsg("InternalError");
            return result;
        }

        if (developerList != null && !developerList.isEmpty()) {
            String res;
            listResult = new ArrayList<>();
            for (Developer developer : developerList) {
                
                res = developer.getDeveloperID()+ ";"//00
                        + developer.getFirstName()+ ";"
                        + developer.getLastName()+ ";"
                        + (developer.getUsersID() == null ? "" : developer.getUsersID().getEmail()) + ";"//03
                        + developer.getCreationDate().getTime() + ";"//04
                        + developer.getTimezoneID() + ";"//05
                        + "null";
                logger.debug("getARangeOfDeveloper: " + res);
                listResult.add(res);
            }
            listResult.add("" + numberOfElts);
            result.setObjectList(listResult);
        }

        return result;
    }
    
    @Override
    public ResultBackend editDeveloper(int developerID, String firstName, String lastName, String email, String timezoneID) throws DeveloperManagementException {

        String res = "good";
        Developer developper;
        ResultBackend result = new ResultBackend();
        
        try {

            ges.creatEntityManager();

            // Rechercher le Developer en BD
            developper = (Developer) ges.getEntityManager().find(Developer.class, developerID);

            if (developper == null || developper.getDeleted()) {
                ges.closeEm();
                logger.warn("editDeveloper: NotExistingDeveloper");
                result.setMsg("NotExistingDeveloper");
                return result;
            }

            // Si le driver ne possède pas de compte, il faudra en créer
            if (developper.getUsersID() != null) {
                developper.getUsersID().setEmail(email);
            }
            
            developper.setFirstName(firstName);
            developper.setLastName(lastName);
            developper.setTimezoneID(timezoneID);
            
            ges.getEntityManager().merge(developper);
            ges.closeEm();

        }catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            logger.error(res);
//            result.setMsg(res + th.getMessage());
            throw new DeveloperManagementException("DeveloperManagementDao", "editDeveloper", 2, res,res);
//            result.afficherResult("editDeveloper");
//            return result;
        }

        result.setMsg(res);
//        result.afficherResult("editDeveloper");
        return result;
    }

    @Override
    public String deleteDeveloper(int developerID) throws DeveloperManagementException {

        String res = "good";
        Developer developer;
        
        try {
            ges.creatEntityManager();
            developer = (Developer) ges.getEntityManager().find(Developer.class, developerID);
            
            if (developer == null || developer.getDeleted()) {
                ges.closeEm();
                logger.warn("deleteDeloper: NotExistingDeveloper");
                return "NotExistingDeveloper";
            }
            
            developer.setDeleted(true);
            
            ges.getEntityManager().merge(developer);
            
            ges.closeEm();

        }catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            logger.error(res);
//            result.setMsg(res + th.getMessage());
            throw new DeveloperManagementException("DeveloperManagementDao", "deleteDeveloper", 3, res,res);
//            result.afficherResult("editDeveloper");
//            return result;
        }

        return res;
    }
    
}
