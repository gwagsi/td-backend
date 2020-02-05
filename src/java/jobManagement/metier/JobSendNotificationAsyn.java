/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jobManagement.metier;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import jobLogManagement.dao.IJobLogManagementDaoLocal;
import jobManagement.dao.IJobManagementDaoLocal;
import jobManagement.dao.JobNotificationDao;
import mailing.mailContent.FileMessageContent;
import mailing.mailSending.MailFunction;
import mailing.mailSending.MailSendingNewJobNotification;
import mailing.sendNotification.NotificationMessage;
import mailing.sendNotification.SendNotification;
import org.apache.logging.log4j.LogManager;
import toolsAndTransversalFunctionnalities.Result;
import util.date.DateFunction;

/**
 *
 * @author erman
 */
@Stateless
public class JobSendNotificationAsyn {

    @EJB
    IJobLogManagementDaoLocal jobLogDAO;

    @EJB
    IJobManagementDaoLocal jobDAO;

    @EJB
    JobNotificationDao jobNofif;

    final static org.apache.logging.log4j.Logger logger = LogManager.getLogger("dump1");
    
    @Asynchronous
    public void sendJobCreationNotificationMail(String[] excavatorInfo, String[] jobResponseInfos, int jobID, float latitude, float longitude) {

        List<String> registrationIDList = new ArrayList<>();
        Result result;
        String excavatorName = excavatorInfo[1];
        String excavatorSurname = excavatorInfo[2];
        String jobLocation = jobResponseInfos[2];
        
        //Chargement de notification
        NotificationMessage nMsg = new NotificationMessage(FileMessageContent.NOTIF_AddNewJobNotificationMessageContent);
        System.out.println(nMsg);
        //Recupération des informations pour les mails
        List<Object[]> userListInfo=null;
        try {
         userListInfo = jobDAO.searchTruckOwnerToNotify(latitude, longitude);
       } catch (Throwable e) {
        logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"editEmployeeInfo"+"\n"
                            +"Erreur   : "+"\n"
                            +e.toString());
       }
        if (userListInfo != null && !userListInfo.isEmpty()) {

            // Enregistrement des notifications en BD et retour des registrations ID
            for (Object[] userInfo : userListInfo) {
                
                int accountID = Integer.parseInt(String.valueOf(userInfo[0]));
                String emailTruckowner = String.valueOf(userInfo[1]);
                
                result = jobNofif.addNewJobNotification(accountID, jobID, nMsg.getMessageToSend());
                
                if (result != null && result.getMsg().contains("good") && result.getObjectList() != null && !result.getObjectList().isEmpty()) {
                    registrationIDList.addAll(result.getObjectList());
                }
                System.out.println("[sendJobCreationNotificationMail] accountID: " + accountID + " - emailTruckowner: " + emailTruckowner + " - result.getObjectList: " + result.getObjectList());
                
            }

            //Envoi des mails
            MailSendingNewJobNotification sendMsgAddNewJob = new MailSendingNewJobNotification(excavatorInfo, jobResponseInfos, userListInfo);
            String mailSendRes = sendMsgAddNewJob.sendMailNewJobNotification();
            System.out.println("[sendJobCreationNotificationMail] mailSendRes: " + mailSendRes );       

            //Envoi des notifications
            //registrationIDList.add(registID);
            System.out.println("[sendJobCreationNotificationMail] registrationIDList: " + registrationIDList );                
            if (!registrationIDList.isEmpty()) {
                
                SendNotification sendNotification = new SendNotification();
                nMsg.setExcavatorName(MailFunction.getTotalName(excavatorName, excavatorSurname));
                nMsg.setJobLocation(jobLocation);
                nMsg.setNotificationDate(DateFunction.getGMTDate());
                
                sendNotification.sendNotification(registrationIDList, nMsg);
            }
        }

    }
}
