/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package admin.billingManagement.dao;

import entities.Account;
import entities.User;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import resultInfo.BillingAdminInfo;
import toolsAndTransversalFunctionnalities.GestionnaireEntite;
import toolsAndTransversalFunctionnalities.Result;
import util.query.sql.AdministrationQuery;

/**
 *
 * @author Sorelus
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class AdminBillingManagementDao implements IAdminBillingManagementDaoRemote , IAdminBillingManagementDaoLocal {
     
    @EJB
    GestionnaireEntite ges;

    final static Logger logger = LogManager.getLogger("dump1");

/*
    @Override
    public String applyActionToUser(int accountID, String admin, int actionID, String message) {
        
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
             System.out.println("" + res);
             return res;
        }
        return res;
    }

    @Override
    public Result getARangeOfCategorieJobs(String categorie, boolean categorieValue, int index, int nombreMaxResult) {
        
        Result result = new Result();
        String res;
        List<Job> jobsList = null;
        List<String> listResult = new ArrayList<>();
        int nombreResult = 0;
                
        try{
            
            ges.creatEntityManager();
            
            Query query = AdministrationQuery.getQueryForSearchJobsByCategories(categorie, categorieValue, 0, 0, ges);
            nombreResult = query.getResultList().size();
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResult);
           
            jobsList = (List<Job>)query.getResultList();
            System.out.println("usersList = " + jobsList);
            ges.closeEm();
        }catch(Throwable th){
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            result.setMsg(res);
            result.afficherResult("getARangeOfCategorieJobs");
            return result;
        }
        
        //System.out.println("[getARangeOfCategorieJobs] jobsList = " + jobsList);
        
        for (Job job : jobsList) {
            
            //System.out.println("[getARangeOfCategorieJobs] job = " + job);
            int jobID = job.getJobID();
            Date startDate = job.getStartDate();
            Date endDate = job.getEndDate();
            int hourPerDay = job.getHourPerDay();
            int numberOfTruck = job.getNumberOfTruck();// Total number of Truck
            int numberOfRemainTruck = 0;
            String companyRate = job.getCompanyRate();
            float jobRate = job.getExcavatorID().getRate();
            int lenghtOfBed = (job.getLenghtOfBed() == null ? 0 : job.getLenghtOfBed().getLenghtofbedID());
            int truckAxle = (job.getTruckAxle() == null ? 0 : job.getTruckAxle().getTruckaxleID());
            String jobLocation = job.getJobLocation();
            String jobNumber = job.getJobNumber();
            boolean closeStatus = job.getClose();
            String typeOfDirtLabel = (job.getTypeofdirtID() == null ? "" : job.getTypeofdirtID().getLabel());
            String weight = job.getWeight();
            String startTime = job.getStartTime();
            String dotNumber = job.getDotNumber();
            int generalLiability = job.getGeneralLiability();
            int truckLiability = job.getTruckLiability();
            String proofOfTruckLiability = job.getProofOfTruckLiability();
            String directDeposit = job.getDirectDeposit();
            String timeSheet = job.getTimeSheet();
            String automatedBooking = job.getAutomatedBooking();
            String jobDescription = job.getJobDescription();
            String jobInstruction = job.getJobInstruction();
            String jobDocumentsID = "";
            String jobDocumentsPATH = "";
            String excavatorTelephone = job.getExcavatorID().getTelephone();
            String timezoneID = job.getTimezoneID();
            int jobCreationType = job.getJobCreationType();
            String excavatorName = job.getExcavatorID().getName();
            String excavatorSurname = job.getExcavatorID().getSurname();

            JobAdminInfo jobAdminInfo = new JobAdminInfo(jobID, startDate, endDate, hourPerDay, numberOfTruck, numberOfRemainTruck, companyRate, jobRate, lenghtOfBed, truckAxle, jobLocation, jobNumber, closeStatus, typeOfDirtLabel, weight, startTime, dotNumber, generalLiability, truckLiability, proofOfTruckLiability, directDeposit, timeSheet, automatedBooking, jobDescription, jobInstruction, jobDocumentsID, jobDocumentsPATH, excavatorTelephone, timezoneID, jobCreationType, excavatorName, excavatorSurname);
            res = jobAdminInfo.getStringObj();
            listResult.add(res);

        }

        result.setMsg("good");
        listResult.add("" + nombreResult);
        result.setObjectList(listResult);
        //result.afficherResult("getARangeOfCategorieJobs");
        return result;
    }

   @Override
    public String getJobsStatisticsInDateRange(long statDate, long endDate) {

        // posted, closed, deleted, started et all (nombre total de job en bd)
        long numberOfPostedJob = 0;
        long numberOfClosedJob = 0;
        long numberOfDeletedJob = 0;
        long numberOfStartedJob = 0;
        long numberOfPassedJob = 0;
        long numberOfAllJob = 0;
        
        Query query = AdministrationQuery.getQueryJobsByStartitics(statDate, endDate, ges);
        
        Iterator statictics = query.getResultList().iterator();
        
        if (statictics.hasNext()) {
            Object[] object = (Object[]) statictics.next();
            
            System.out.println("[getJobsStatisticsInDateRange] object.length: " + object.length);
            numberOfPostedJob = object[0] == null ? 0 : Long.parseLong(String.valueOf(object[0]));
            numberOfClosedJob = object[1] == null ? 0 : Long.parseLong(String.valueOf(object[1]));
            numberOfDeletedJob = object[2] == null ? 0 : Long.parseLong(String.valueOf(object[2]));
            numberOfStartedJob = object[3] == null ? 0 : Long.parseLong(String.valueOf(object[3]));
            numberOfPassedJob = object[4] == null ? 0 : Long.parseLong(String.valueOf(object[4]));
            numberOfAllJob = object[5] == null ? 0 : Long.parseLong(String.valueOf(object[5]));
            System.out.println("[getJobsStatisticsInDateRange] Return Order: NBPostedJob ## NBClosedJob ## NBDeletedJob ## NBActifJob ## NBPassedJob ## NBAllJob");
            System.out.println("[getJobsStatisticsInDateRange] data: " + numberOfPostedJob + "##" + numberOfClosedJob + "##" + numberOfDeletedJob + "##" + numberOfStartedJob + "##" + numberOfPassedJob  + "##" + numberOfAllJob);
        }

        return numberOfPostedJob + "##" + numberOfClosedJob + "##" + numberOfDeletedJob + "##" + numberOfStartedJob + "##" + numberOfPassedJob  + "##" + numberOfAllJob;
    }
    
    @Override
    public String getCurrentJobsStatistics(String categorie, boolean categorieValue) {

        long numberUser = 0;
        String user;
        String result = "";

        Query query = AdministrationQuery.getQueryCurrentUsersStatistics(ges, categorie, categorieValue);

        
        Iterator statictics = query.getResultList().iterator();
        System.out.println( "getCurrentUsersStatistics: statictics = " + statictics);

        while (statictics.hasNext()) {
            Object[] object = (Object[]) statictics.next();
            numberUser = (long)object[1];
            user =  String.valueOf(object[0]);
            result += numberUser+"##";
            System.out.println("getCurrentUsersStatistics: " + user + "  Nombre: " + numberUser );
        }

        return result;
    }
    
    @Override
    public Result getARangeOfUserInDateRange(long statDate, long endDate, boolean isPaid, int index, int nombreMaxResult) {

        System.out.println("getARangeOfUserInDateRange:  statDate = " + statDate + " -- endDate = " + endDate + " -- isPaid = " + isPaid + " -- index = " + index + " -- nombreMaxResult = " + nombreMaxResult);
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
        
        System.out.println("usersList = " + usersList);
        
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

    
*/
        @Override
    public Result getNumberUserBilling(boolean statusValue, long statDate, long endDate) {

        //System.out.println("getNumberUserBilling: statusvalue= " + statusValue + ", statDate = " + new Date(statDate) + ", endDate = " + new Date(endDate));
        
         logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"getNumberUserBilling"+"\n"
                            +"info      : "+ "getNumberUserBilling: statusvalue= " + statusValue + ", statDate = " + new Date(statDate) + ", endDate = " + new Date(endDate));
        
        Result result = new Result();
        String res = "good";
        List<Object[]> usersList;
        int nbre = 0;
        
        try {
            ges.creatEntityManager();
            Query query;

            query = AdministrationQuery.getQueryForAdminCountUserByStatus(statusValue, statDate, endDate, ges);
            usersList = query.getResultList();

            nbre = Integer.parseInt(String.valueOf(usersList.get(0)));
            ges.closeEm();

        } catch (NumberFormatException th) {
            res = th.getMessage();
            result.setMsg(res);
            result.afficherResult("getNumberUserBilling");
        }
        result.setMsg("good");
        //listResult.add("" + nombreResult);
        result.setObject(String.valueOf(nbre));
        result.afficherResult("getNumberUserBilling");
        return result;
    }

    @Override
    public Result getlistUserBilling(boolean statusValue, long statDate, long endDate, int index, int nombreMaxResult) {

        //System.out.println("getlistUserBilling: Type= " + "LIST" + ", statusvalue= " + statusValue + ", statDate= " + new Date(statDate) + ", endDate= " + new Date(endDate) + " , index = " + index + ", Max= " + nombreMaxResult);
         logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"getlistUserBilling"+"\n"
                            +"info      : "+ "getlistUserBilling: Type= " + "LIST" + ", statusvalue= " + statusValue + ", statDate= " + new Date(statDate) + ", endDate= " + new Date(endDate) + " , index = " + index + ", Max= " + nombreMaxResult);
        
        
        Result result = new Result();
        String res = "good";
        List<Account> usersList = null;
        List<String> listResult = new ArrayList<>();
        int nombreResult = 0;
        try {
            ges.creatEntityManager();
            Query query;

            query = AdministrationQuery.getQueryForAdminSearchUserByStatus(statusValue, statDate, endDate, ges);
            nombreResult = query.getResultList().size();
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResult);
            usersList = query.getResultList();
            
            ges.closeEm();

        } catch (Throwable th) {
            res =th.getMessage();
            result.setMsg(res);
            result.afficherResult("getListUserBilling");
        }
       
        
       // System.out.println("[getListUserBilling] usersList: " + usersList);

        logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"getlistUserBilling"+"\n"
                            +"info      : "+ "[getListUserBilling] usersList: " + usersList);
        
        
        for (Account account : usersList) {

            User user = account.getUser();
            if (user != null) {
                BillingAdminInfo bInfo;

                BigDecimal monthlyCost = new BigDecimal(0);
                long endOfBillingCycleTime = 0;
                if (account.getBillingreceiverID() != null) {
                    monthlyCost = account.getBillingreceiverID().getMonthlyCost();
                    endOfBillingCycleTime = account.getBillingreceiverID().getEndOfBillingCycle().getTime();

                }

                bInfo = new BillingAdminInfo(account.getAccountID(), user.getName(), user.getSurname(),
                        account.getSocialstatusID().getSocialstatusID(), account.getEmail(), user.getTelephone(), endOfBillingCycleTime, monthlyCost);
                listResult.add(bInfo.getStringObj());
            } else {
                //System.out.println("[getListUserBilling] May be Driver Account: " + account + " -- SocialstatusID:" + account.getSocialstatusID());
                  logger.info("\n"+"Class : "+getClass()+"\n"
                            +"Fct       : "+"getlistUserBilling"+"\n"
                            +"info      : "+ "[getListUserBilling] May be Driver Account: " + account + " -- SocialstatusID:" + account.getSocialstatusID());
        
        
            
            }

        }

        result.setMsg("good");
        listResult.add("" + nombreResult);
        result.setObjectList(listResult);
        result.afficherResult("getListUserBilling");
        return result;
    }

}
