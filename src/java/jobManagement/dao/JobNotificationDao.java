/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jobManagement.dao;

import entities.Account;
import entities.Job;
import entities.JobNotification;
import entities.JobNotificationType;
import entities.PhoneRegistration;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import toolsAndTransversalFunctionnalities.GestionnaireEntite;
import toolsAndTransversalFunctionnalities.Result;
import util.date.DateFunction;

/**
 *
 * @author erman
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class JobNotificationDao {

    @EJB
    GestionnaireEntite ges;

    public Result addNewJobNotification(int accountID, int jobID, String notificationValue) {

        String res = "good";
        Result result = new Result();
        List<String> registrationIDList;
        Account account;
        Job job;
        JobNotification jobNotification;
        JobNotificationType jobNotificationType;
                
        try {

            ges.creatEntityManager();
            account = (Account) ges.getEntityManager().find(Account.class, accountID);
            jobNotificationType = (JobNotificationType) ges.getEntityManager().find(JobNotificationType.class, 1);

            job = (Job) ges.getEntityManager().find(Job.class, jobID);

            if (account == null) {
                ges.closeEm();
                result.setMsg("InvalidAccountID");
                return result;
            }

            if (job == null) {
                ges.closeEm();
                result.setMsg("InvalidJobID");
                return result;
            }

            if (jobNotificationType == null) {
                ges.closeEm();
                result.setMsg("InvalidJobNotificationTypeID");
                return result;
            }

            jobNotification = new JobNotification();
            jobNotification.setJobID(job);
            jobNotification.setAccountID(account);
            jobNotification.setState(false);
            jobNotification.setDeleted(false);
            jobNotification.setValue(notificationValue);
            jobNotification.setCreationDate(DateFunction.getGMTDate());
            jobNotification.setJobnotificationtypeID(jobNotificationType);
            ges.getEntityManager().persist(jobNotification);
            ges.closeEm();

            List<PhoneRegistration> phoneRegistrationList = account.getPhoneRegistrationList();
            if (phoneRegistrationList != null && !phoneRegistrationList.isEmpty()) {
                registrationIDList = new ArrayList<>();
                for (PhoneRegistration phoneRegistration : phoneRegistrationList) {
                    if (!phoneRegistration.getRegisteredID().equals("0")) {
                        registrationIDList.add(phoneRegistration.getRegisteredID());
                    }
                }
                result.setObjectList(registrationIDList);
            }
            
            result.setObject(String.valueOf(jobNotification.getJobnotificationID()));

        } catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            result.setMsg(res);
            result.afficherResult("addNewJobNotification");
            return result;
        }

        result.setMsg(res);
        return result;
    }

}
