/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package admin.issueManagement.dao;

import entities.Account;
import entities.Developer;
import entities.Issue;
import entities.ResolveIssue;
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
import util.exception.IssueManagementException;

/**
 *
 * @author erman
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class IssueManagementDao implements IIssueManagementDaoLocal, IIssueManagementDaoRemote{
    
    @EJB
    GestionnaireEntite ges;
    final static org.apache.logging.log4j.Logger logger = LogManager.getLogger("dump1");

    @Override
    public ResultBackend addNewIssue(int accountID, String category, String subcategory, String description) throws IssueManagementException {
    
        String res = "";
        Account account;
        Issue issue;
        
        List<Developer> developerList;
        
        ResultBackend result = new ResultBackend();
       
        try{
            
            ges.creatEntityManager();
            
            account = (Account) ges.getEntityManager().find(Account.class, accountID);
            
            if (account == null) {
                result.setMsg("InvalidAccountID");
                logger.warn("addNewIssue: InvalidAccountID");
                return result;
            }
            
            issue = new Issue();
            issue.setAccountID(account);
            issue.setCategory(category);
            issue.setSubcategory(subcategory);
            issue.setDeleted(false);
            issue.setStatut(false); // Set to true if the issue is solve
            issue.setCreationDate(DateFunction.getGMTDate());
            issue.setEditionDate(DateFunction.getGMTDate());
            issue.setDescription(description);
            
            ges.getEntityManager().persist(issue);
            ges.closeEm();
            
            developerList = (List<Developer>) ges.getEntityManager().createQuery("SELECT d FROM Developer d WHERE d.deleted = FALSE")
                    .getResultList();
            
            List<Object[]> listResult = new ArrayList<>();
            
            String[] issueInfo = {
                issue.getAccountID().getEmail(),//00
                issue.getAccountID().getUser().getName(),//01
                issue.getAccountID().getUser().getSurname(),//02
                issue.getCategory(),//03
                issue.getSubcategory(),//04
                DateFunction.formatDateToStringYMMMD(issue.getCreationDate()),//05
                String.valueOf(issue.getStatut()),//06
                issue.getDescription()//07
            };

            listResult.add(issueInfo);
            
            logger.info("addNewIssue: " + res + ", getAccountID: " + issue.getAccountID() + ", issueDescription: " + issue.getDescription());
            
            List<String> developerMailInfo = new ArrayList<>();
            for (Developer developer : developerList) {
                developerMailInfo.add(developer.getUsersID().getEmail());
            }
            
            listResult.add(developerMailInfo.toArray());
            result.setResultArraysList(listResult);

            result.setMsg("good");

        } catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            logger.error(res);
//            result.setMsg(res + th.getMessage());
            throw new IssueManagementException("IssueManagementDao", "addNewIssue", 1, "InternalError",res);
//            result.afficherResult("addNewIssue");
//            return result;
        }
        return result;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfIssue(int index, int nombreMaxResultat) {

        logger.info("getARangeOfIssue(info): " + index + ", nombreMaxResultat: " + nombreMaxResultat);
        logger.trace("getARangeOfIssue(trace): " + index + ", nombreMaxResultat: " + nombreMaxResultat);
        logger.debug("getARangeOfIssue(debug): " + index + ", nombreMaxResultat: " + nombreMaxResultat);
        logger.error("getARangeOfIssue(error): " + index + ", nombreMaxResultat: " + nombreMaxResultat);
        List<Issue> issueList;
        Result result = new Result();
        result.setMsg("good");
        List<String> listResult;
        int numberOfElts;

        try {

            ges.creatEntityManager();
            
            Query query = ges.getEntityManager().createQuery("SELECT i FROM Issue i WHERE i.deleted = FALSE ORDER BY i.creationDate DESC");

            issueList = (List<Issue>) query.getResultList();
            numberOfElts = issueList.size();

            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResultat);

            issueList = (List<Issue>) query.getResultList();

            ges.closeEm();

        } catch (Throwable th) {
            logger.warn("getARangeOfIssue: InternalError");
            result.setMsg("InternalError");
            return result;
        }

        if (issueList != null && !issueList.isEmpty()) {
            String res;
            listResult = new ArrayList<>();
            for (Issue issue : issueList) {
                
                res = issue.getIssueID() + ";"//00
                        + issue.getCategory()+ ";"
                        + issue.getSubcategory()+ ";"
                        + issue.getDescription() + ";"//03
                        + issue.getCreationDate().getTime() + ";"//04
                        + issue.getStatut() + ";"//05
                        + issue.getAccountID().getEmail() + ";"//06
                        + issue.getAccountID().getUser().getName() + ";"//07
                        + issue.getAccountID().getUser().getSurname() + ";"//08
                        + issue.getEditionDate().getTime() + ";"//09
                        + "null";
                logger.debug("getARangeOfIssue: " + res);
                listResult.add(res);
            }
            listResult.add("" + numberOfElts);
            result.setObjectList(listResult);
        }

        return result;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getARangeOfIssueByAccountID(int accountID, int index, int nombreMaxResultat) {

        logger.info("getARangeOfIssueByAccountID(info): " + index + ", nombreMaxResultat: " + nombreMaxResultat);
        logger.trace("getARangeOfIssueByAccountID(trace): " + index + ", nombreMaxResultat: " + nombreMaxResultat);
        logger.debug("getARangeOfIssueByAccountID(debug): " + index + ", nombreMaxResultat: " + nombreMaxResultat);
        logger.error("getARangeOfIssueByAccountID(error): " + index + ", nombreMaxResultat: " + nombreMaxResultat);
        List<Issue> issueList;
        Result result = new Result();
        result.setMsg("good");
        List<String> listResult;
        int numberOfElts;
        Account account;

        try {

            ges.creatEntityManager();
            
            account = (Account) ges.getEntityManager().find(Account.class, accountID);
            
            if (account == null) {
                result.setMsg("InvalidAccountID");
                logger.warn("getARangeOfIssueByAccountID: InvalidAccountID");
                return result;
            }
            
            Query query = ges.getEntityManager().createQuery("SELECT i FROM Issue i WHERE i.deleted = FALSE AND i.accountID = :accountID ORDER BY i.creationDate DESC")
                    .setParameter("accountID", account);

            issueList = (List<Issue>) query.getResultList();
            numberOfElts = issueList.size();

            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResultat);

            issueList = (List<Issue>) query.getResultList();

            ges.closeEm();

        } catch (Throwable th) {
            logger.warn("getARangeOfIssueByAccountID: InternalError");
            result.setMsg("InternalError");
            return result;
        }

        if (issueList != null && !issueList.isEmpty()) {
            String res;
            listResult = new ArrayList<>();
            for (Issue issue : issueList) {
                
                res = issue.getIssueID() + ";"//00
                        + issue.getCategory()+ ";"
                        + issue.getSubcategory()+ ";"
                        + issue.getDescription() + ";"//03
                        + issue.getCreationDate().getTime() + ";"//04
                        + issue.getStatut() + ";"//05
                        + issue.getAccountID().getEmail() + ";"//06
                        + issue.getAccountID().getUser().getName() + ";"//07
                        + issue.getAccountID().getUser().getSurname() + ";"//08
                        + issue.getEditionDate().getTime() + ";"//09
                        + "null";
                logger.debug("getARangeOfIssueByAccountID: " + res);
                listResult.add(res);
            }
            listResult.add("" + numberOfElts);
            result.setObjectList(listResult);
        }

        return result;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Result getIssueByID(int accountID, int issueID) {

        logger.info ("getIssueByID(infos): " + accountID + ", issueID: " + issueID);
        logger.trace("getIssueByID(trace): " + accountID + ", issueID: " + issueID);
        logger.debug("getIssueByID(debug): " + accountID + ", issueID: " + issueID);
        logger.error("getIssueByID(error): " + accountID + ", issueID: " + issueID);
        Issue issue;
        Result result = new Result();
        result.setMsg("good");
        
        Account account;

        try {

            ges.creatEntityManager();
            
            account = (Account) ges.getEntityManager().find(Account.class, accountID);
            
            if (account == null) {
                result.setMsg("InvalidAccountID");
                logger.warn("getIssueByID: InvalidAccountID");
                return result;
            }
            
            issue = (Issue) ges.getEntityManager().find(Issue.class, issueID);
            if (issue == null) {
                result.setMsg("InvalidIssueID");
                logger.warn("getIssueByID: InvalidIssueID");
                return result;
            }

            ges.closeEm();

        } catch (Throwable th) {
            logger.warn("getIssueByID: InternalError");
            result.setMsg("InternalError");
            return result;
        }
        
        String res = issue.getIssueID() + ";"//00
                        + issue.getCategory()+ ";"
                        + issue.getSubcategory()+ ";"
                        + issue.getDescription() + ";"//03
                        + issue.getCreationDate().getTime() + ";"//04
                        + issue.getStatut() + ";"//05
                        + issue.getAccountID().getEmail() + ";"//06
                        + issue.getAccountID().getUser().getName() + ";"//07
                        + issue.getAccountID().getUser().getSurname() + ";"//08
                        + issue.getEditionDate().getTime() + ";"//09
                        + issue.getDeleted() + ";"//10
                        + "null";
                logger.debug("getIssueByID: " + res);
                
            result.setObject(res);
            
        return result;
    }
    
    @Override
    public ResultBackend editIssue(int accountID, int issueID, String category, String subcategory, String description) throws IssueManagementException {

        String res = "good";
        Issue issue;
        ResultBackend result = new ResultBackend();
        
        try {

            ges.creatEntityManager();

            // Rechercher le Developer en BD
            issue = (Issue) ges.getEntityManager().find(Issue.class, issueID);

            if (issue == null || issue.getDeleted()) {
                ges.closeEm();
                logger.warn("editIssue: InvalidIssueID");
                result.setMsg("InvalidIssueID");
                return result;
            }

            
            issue.setCategory(category);
            issue.setSubcategory(subcategory);
            issue.setDescription(description);
            issue.setEditionDate(DateFunction.getGMTDate());
            
            ges.getEntityManager().merge(issue);
            ges.closeEm();

        }catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            logger.error(res);
//            result.setMsg(res + th.getMessage());
            throw new IssueManagementException("IssueManagementDao", "editDeveloper", 2, res,res);
//            result.afficherResult("editDeveloper");
//            return result;
        }

        result.setMsg(res);
//        result.afficherResult("editDeveloper");
        return result;
    }

    @Override
    public String deleteIssue(int accountID, int issueID) throws IssueManagementException {

        String res = "good";
        Issue issue;
        
        try {
            ges.creatEntityManager();
            issue = (Issue) ges.getEntityManager().find(Issue.class, issueID);
            
            if (issue == null || issue.getDeleted()) {
                ges.closeEm();
                logger.warn("deleteIssue: InvalidIssueID");
                return "InvalidIssueID";
            }
            
            issue.setDeleted(true);
            
            ges.getEntityManager().merge(issue);
            
            ges.closeEm();

        }catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            logger.error(res);
//            result.setMsg(res + th.getMessage());
            throw new IssueManagementException("IssueManagementDao", "deleteIssue", 3, res,res);
//            result.afficherResult("editDeveloper");
//            return result;
        }

        return res;
    }

    @Override
    public String resolveIssue(String usernameID, int issueID, String solution) throws IssueManagementException {

        String res = "good";
        Users users;
        Issue issue;
        Developer developper;
        ResolveIssue resolveIssue;
        
        try {
            ges.creatEntityManager();
            users = (Users) ges.getEntityManager().find(Users.class, usernameID);
            
            if (users == null || users.getDeveloperList() == null || users.getDeveloperList().isEmpty()) {
                ges.closeEm();
                logger.warn("resolveIssue: InvalidUsernameID");
                return "InvalidUsernameID";
            }
            
            developper = users.getDeveloperList().get(0);
            
            issue = (Issue) ges.getEntityManager().find(Issue.class, issueID);
            
            if (issue == null || issue.getDeleted()) {
                ges.closeEm();
                logger.warn("resolveIssue: InvalidIssueID");
                return "InvalidIssueID";
            }
            
            resolveIssue = new ResolveIssue();
            resolveIssue.setDeleted(false);
            resolveIssue.setCreationDate(DateFunction.getGMTDate());
            resolveIssue.setSolutionDate(DateFunction.getGMTDate());
            resolveIssue.setEditionDate(DateFunction.getGMTDate());
            resolveIssue.setIssueID(issue);
            resolveIssue.setDeveloperID(developper);
            resolveIssue.setSolution(solution);
            
            ges.getEntityManager().persist(resolveIssue);
            
            issue.setStatut(true);
            
            ges.getEntityManager().merge(issue);
            
            ges.closeEm();

        }catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            logger.error(res);
//            result.setMsg(res + th.getMessage());
            throw new IssueManagementException("IssueManagementDao", "resolveIssue", 4, res,res);
//            result.afficherResult("editDeveloper");
//            return result;
        }

        return res;
    }
    
}
