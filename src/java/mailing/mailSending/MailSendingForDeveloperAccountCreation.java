/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mailing.mailSending;

import java.util.HashMap;
import mailing.mailContent.FileMessageContent;
import mailing.mailTool.CustomMail;
import mailing.mailTool.MailSender;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author erman
 */
public class MailSendingForDeveloperAccountCreation {
    
    final static org.apache.logging.log4j.Logger logger = LogManager.getLogger("dump1");
    
    public boolean sendAccountCreationMail(String[] developerMailInfo){

        boolean res = true;
        
        
        String name = developerMailInfo[1];
        String firstName = developerMailInfo[2];
        String login = developerMailInfo[4];
        String pwd = developerMailInfo[5];
        String receiver = developerMailInfo[3];
        
        String webURL = MailFunction.getUrlWeb();
        String adminUrl = MailFunction.getUrlAdmin();
        
        String totalName = MailFunction.getHtmlTotalName(name, firstName);
        
        CustomMail custMail = getDriverAccountCreationMessage(totalName, login, pwd, adminUrl, webURL);

        String subject = "New Developper Account";
        MailSender mailSender = new MailSender();
        mailSender.sendMail(receiver, custMail, subject);
        
        return res;
    }
    
    
    /**
     *
     * @param totalName Nom complet du client si possible formatter
     * @param login Login du client
     * @param password mot de passe du client
     * @param adminURL url d'activation du compte
     * @param webURL
     * @return Le message deja formater
     */
    public CustomMail getDriverAccountCreationMessage(String totalName, String login, String password,  String adminURL, String webURL) {
       
        HashMap hashValues = new HashMap();
        hashValues.put("###TOTALNAME", "<b>"+totalName+"</b>");
        hashValues.put("###LOGIN", "<b>"+login+"</b>");
        hashValues.put("###PASSWORD", "<b>"+password+"</b>");
        hashValues.put("###WEB_URL", adminURL);
        hashValues.put("###2WEB_URL", webURL);
        
        CustomMail ctMail = new CustomMail(hashValues, FileMessageContent.MAIL_DeveloperAccountCreationMessageContent);
        ctMail.buildMail();
        logger.info(" Message lu buff ");
        
        return ctMail;

    }
    
}
