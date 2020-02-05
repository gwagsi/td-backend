/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin.issueManagement.metier;

import java.util.Arrays;
import java.util.List;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import mailing.mailSending.MailSendingNewIssueNotification;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author erman
 */
@Stateless
public class IssueSendNotificationAsyn {

    
    final static org.apache.logging.log4j.Logger logger = LogManager.getLogger("dump1");
    
    @Asynchronous
    public void sendIssueNotificationMail(List<Object[]> resultArraysList) {

        if (resultArraysList != null && !resultArraysList.isEmpty()) {

//            String[] issueInfo = (String[]) resultArraysList.get(0);
//            String[] developperEmail = (String[]) resultArraysList.get(1);
            
            String[] issueInfo = Arrays.copyOf(resultArraysList.get(0), resultArraysList.get(0).length, String[].class);
            String[] developperEmail = Arrays.copyOf(resultArraysList.get(1), resultArraysList.get(1).length, String[].class);
           
            //Enregistrement des notifications en BD et retour des registrations ID
            
            //Envoi des mails
            MailSendingNewIssueNotification sendMsgAddNewJob = new MailSendingNewIssueNotification(issueInfo, developperEmail);
            String mailSendRes = sendMsgAddNewJob.sendMailNewJobNotification();
            System.out.println("[sendIssueNotificationMail] mailSendRes: " + mailSendRes );       
            
        }
    }
}
