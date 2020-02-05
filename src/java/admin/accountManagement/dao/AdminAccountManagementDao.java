/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package admin.accountManagement.dao;

import entities.Account;
import entities.AccountAdministration;
import entities.AccountDeleted;
import entities.AdminAction;
import entities.User;
import entities.Users;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import toolsAndTransversalFunctionnalities.GestionnaireEntite;
import toolsAndTransversalFunctionnalities.Result;
import util.date.DateFunction;
import util.exception.AdminAccountManagementException;
import util.query.sql.AdministrationQuery;

/**
 *
 * @author erman
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class AdminAccountManagementDao implements IAdminAccountManagementDaoRemote , IAdminAccountManagementDaoLocal {
     
    @EJB
    GestionnaireEntite ges;
final static org.apache.logging.log4j.Logger logger = LogManager.getLogger("dump1");
    
    @Override
    public String applyActionToUser(int accountID, String admin, int actionID, String message) throws AdminAccountManagementException{
        
        String res = "good";
        Account account;
        Users user;
        AdminAction action;
        List<AccountAdministration> accountAdmin;
        
        try{
            ges.creatEntityManager();
            account = (Account)ges.getEntityManager().find(Account.class, accountID);
            user = (Users)ges.getEntityManager().find(Users.class, admin);
            action = (AdminAction)ges.getEntityManager().find(AdminAction.class, actionID);
            
            if(account == null){
                return "invalidAccountID";
            }
            
            if(user == null){
                return "invalidUser";
            }
            
            if(action == null){
                return "invalidActionID";
            }

            switch (action.getAdminactionID()) {
                case Action.SUSPEND_ACTION:
                    if (account.getSuspend()) {
                        return "AlreadySuspended";
                    }
                    account.setSuspend(true);
                    break;
                case Action.ACTIVATE_ACTION:
                    account.setActif(true);
                    account.setDeleted(false);
                    account.setSuspend(false);
                    break;
                case Action.DELETED_ACTION:
                    account.setDeleted(true);
                    
                     //j'ai ajouté ceci pour la gestion du backup
                    AccountDeleted accountDeleted=new AccountDeleted();
                    accountDeleted.setAccountID(account);
                    accountDeleted.setEmail(account.getEmail());
                    accountDeleted.setLogin(account.getLogin());
                    ges.getEntityManager().persist(accountDeleted);
                    
                    account.setEmail(String.valueOf(UUID.randomUUID()));
                    account.setLogin(String.valueOf(UUID.randomUUID()));
                    break;
            }

            AccountAdministration manage = new AccountAdministration();
            manage.setAccountID(account);
            manage.setAdmin(user);
            manage.setMessage(message);
            manage.setAction(action);
            manage.setActionDate(DateFunction.getGMTDate());
                    
            accountAdmin = account.getAccountAdministrationList();
            accountAdmin.add(manage);
            
            ges.getEntityManager().merge(account);
            ges.getEntityManager().merge(account.getUser());
            
            ges.closeEm();
        }catch(Throwable th){StringWriter string = new StringWriter();
             PrintWriter str = new PrintWriter(string);
             th.printStackTrace(str);
             res = string.toString();
            // System.out.println("" + res);
              throw new AdminAccountManagementException("AdminAccountManagementDao","applyActionToUser",1,res,res);
           
             //return res;
        }
        return res;
    }

    @Override
    public Result getARangeOfCategorieUser(String categorie, boolean categorieValue, int typeOfUser, int index, int nombreMaxResult) throws AdminAccountManagementException{
        
        Result result = new Result();
        String res = "good";
        Account account = null;
        List<User> usersList = null;
        String administrations ="";
        List<String> listResult = new ArrayList<>();
        int nombreResult = 0;
                
        try{
            
            ges.creatEntityManager();
            
            Query query = AdministrationQuery.getQueryForSearchUserByCategories(categorie, categorieValue, typeOfUser, 0, 0, ges);
            nombreResult = query.getResultList().size();
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResult);
           
            usersList = (List<User>)query.getResultList();
           // System.out.println("usersList = " + usersList);
            logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"getARangeOfCategorieUser"+"\n"
                            +"info      : "+ "usersList = "+usersList);
            ges.closeEm();
        }catch(Throwable th){
            res += ""+th.getMessage();
        }
        
         // System.out.println("[getARangeOfCategorieUser] usersList = " + usersList);
        
        logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"getARangeOfCategorieUser"+"\n"
                            +"info      : "+ "[getARangeOfCategorieUser] usersList ="+usersList);
        for (User user : usersList) {
            account = user.getAccountID();
            /*
            List<AccountAdministration> acc_administrations = account.getAccountAdministrationList();
            administrations = "";
            for(AccountAdministration admins : acc_administrations){
                administrations+=" <b> - Date</b>: " + admins.getActionDate() + ", <b> - Message or Reason</b>: " + admins.getMessage() + "<br>";
            }
             */   
            res = account.getAccountID() + ";"//0
                    + user.getName() + ";"
                    + user.getSurname() + ";"
                    + user.getAddress() + ";"
                    + user.getAccountID().getEmail() + ";"
                    + user.getTelephone() + ";"
                    + user.getCellPhone() + ";"
                    + user.getGpsCoordinate() + ";"
                    + user.getCardNumber() + ";"
                    + account.getLogin() + ";"
                    + account.getPassword() + ";"//10
                    + account.getSocialstatusID().getSocialstatusID() + ";"
                    + account.getSocialstatusID().getTypeName() + ";"
                    + account.getUser().getCompagnyName() + ";"
                    + account.getAccountNumber() + ";"
                    + administrations + ";"
                    
                    + "null";
            listResult.add(res);
            
        }
        
        result.setMsg("good");
        listResult.add("" + nombreResult);
        result.setObjectList(listResult);
        result.afficherResult("getARangeOfCategorieUser");
        return result;
    }

   @Override
    public String getUsersStatisticsInDateRange(long statDate, long endDate)throws AdminAccountManagementException {

        long numberOfAccount = 0;
        long numberOfJob = 0;
        long numberOfTruck = 0;

        //Date[] dates = DateFunction.getDateRangeOfWeekLarge();
        Query query = ges.getEntityManager().createQuery("SELECT COUNT(DISTINCT a), COUNT(DISTINCT b), COUNT(DISTINCT c) FROM Account a , Job b , Truck c"
                                                            + " WHERE (a.creationDate BETWEEN :firstDayOfWeek AND :lastDayOfWeek) "
                                                            + "AND (b.creationDate BETWEEN :firstDayOfWeek AND :lastDayOfWeek) "
                                                            + "AND (c.creationDate BETWEEN :firstDayOfWeek AND :lastDayOfWeek) "
                                                            + "AND a.deleted = FALSE AND b.deleted = FALSE AND c.deleted = FALSE");

        query.setParameter("firstDayOfWeek", new Date(statDate));
        query.setParameter("lastDayOfWeek", new Date(endDate));

        Iterator statictics = query.getResultList().iterator();
        
        if (statictics.hasNext()) {
            Object[] object = (Object[]) statictics.next();
            numberOfAccount = (long) object[0];
            numberOfJob = (long) object[1];
            numberOfTruck = (long) object[2];
            
            logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"getUsersStatisticsInDateRange"+"\n"
                            +"info      : "+ "getUsersStatisticsInDateRange: "+ numberOfAccount + "##" + numberOfTruck + "##" + numberOfJob);
            
          //  System.out.println("getUsersStatisticsInDateRange: " + numberOfAccount + "##" + numberOfTruck + "##" + numberOfJob);
        }

        return numberOfAccount + "##" + numberOfTruck + "##" + numberOfJob;
    }

    @Override
    public String getCurrentUsersStatistics(String categorie, boolean categorieValue) throws AdminAccountManagementException{

        long numberUser = 0;
        String user;
        String result = "";

       /* Query query = ges.getEntityManager().createQuery("SELECT COUNT(DISTINCT a), COUNT(DISTINCT b), COUNT(DISTINCT c), COUNT(DISTINCT d) FROM User a , User b , User c, User d"
                                                            + " WHERE (a.socialstatusID = 1) "
                                                            + "AND (b.socialstatusID = 2) "
                                                            + "AND (c.socialstatusID = 3) "
                                                            + "AND (d.socialstatusID = 4) "
                                                            + "AND a.deleted = FALSE AND b.deleted = FALSE AND c.deleted = FALSE AND d.deleted = FALSE");
        */
        Query query = AdministrationQuery.getQueryCurrentUsersStatistics(ges, categorie, categorieValue);

        
        Iterator statictics = query.getResultList().iterator();
       // System.out.println( "getCurrentUsersStatistics: statictics = " + statictics);

         logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"getCurrentUsersStatistics"+"\n"
                            +"info      : "+ "getCurrentUsersStatistics: statictics = " + statictics);
        
        while (statictics.hasNext()) {
            Object[] object = (Object[]) statictics.next();
            numberUser = (long)object[1];
            user =  String.valueOf(object[0]);
            result += numberUser+"##";
             logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"getCurrentUsersStatistics"+"\n"
                            +"info      : "+ "getCurrentUsersStatistics: statictics = " + statictics);
        
           // System.out.println("getCurrentUsersStatistics: " + user + "  Nombre: " + numberUser );
        }

        return result;
    }
    

    @Override
    public Result getARangeOfUserInDateRange(long statDate, long endDate, boolean isPaid, int index, int nombreMaxResult) throws AdminAccountManagementException{

        //System.out.println("getARangeOfUserInDateRange:  statDate = " + statDate + " -- endDate = " + endDate + " -- isPaid = " + isPaid + " -- index = " + index + " -- nombreMaxResult = " + nombreMaxResult);
        
         logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"getARangeOfUserInDateRange"+"\n"
                            +"info      : "+ "getARangeOfUserInDateRange:  statDate = " + statDate + " -- endDate = " + endDate + " -- isPaid = " + isPaid + " -- index = " + index + " -- nombreMaxResult = " + nombreMaxResult);
        
        Result result = new Result();
        String res = "good";
        List<Object[]> usersList = null;
        List<String> listResult = new ArrayList<>();
        int nombreResult = 0;
                
        try{
         
            ges.creatEntityManager();
            Query query = null;
            
            query = AdministrationQuery.getQueryForSearchUserByCategories("PAID", isPaid, 0, statDate, endDate, ges);
            //nombreResult = query.getResultList().size();
            
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResult);
             //usersList = query.getResultList();
            nombreResult = query.getResultList().size();

            query.setFirstResult(index);
            
            query.setMaxResults(nombreMaxResult);
           
            usersList = (List<Object[]>)query.getResultList();
            //System.out.println("usersList = " + usersList);
            ges.closeEm();
        }catch(Throwable th){
            res += ""+th.getMessage();
        }
        
        //System.out.println("usersList = " + usersList);
        
         logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"getARangeOfUserInDateRange"+"\n"
                            +"info      : "+ "usersList = " + usersList);
        
        for (Object[] object : usersList) {
            
            Account account = (Account) object[0];
            long monthDate = (object[1] == null ? 0 : ((Date) object[1]).getTime());
            User user = account.getUser();
            res = account.getAccountID() + ";"//0
                    + user.getName() + ";"
                    + user.getSurname() + ";"
                    + user.getAddress() + ";"
                    + user.getAccountID().getEmail() + ";"
                    + user.getTelephone() + ";"
                    + user.getCellPhone() + ";"
                    + user.getGpsCoordinate() + ";"
                    + user.getCardNumber() + ";"
                    + account.getLogin() + ";"
                    + account.getPassword() + ";"//10
                    + account.getSocialstatusID().getSocialstatusID() + ";"
                    + account.getSocialstatusID().getTypeName() + ";"
                    + account.getUser().getCompagnyName() + ";"
                    + account.getAccountNumber() + ";"
                    + monthDate + ";"
                    + "null";
            listResult.add(res);
            
        }
        
        result.setMsg("good");
        listResult.add("" + nombreResult);
        result.setObjectList(listResult);
        result.afficherResult("getARangeOfUserInDateRange");
        return result;
    }
    
}
