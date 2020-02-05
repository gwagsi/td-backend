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
import util.security.CodeDecodeAES;

/**
 *
 * @author erict
 */
public class MailSendingForAccountCreation {

    public boolean sendAccountCreationMsg(String receiver, String name, String firstName, 
            String login, String pwd, int id, int typeOfUser, String promotionCode, String promotionPercent){

        boolean res=true;
        
        String urlActive = MailFunction.getUrlWebActivate();
        urlActive += CodeDecodeAES.encrypt(String.valueOf(id));
        
        String totalName = MailFunction.getHtmlTotalName(name, firstName);
        
        CustomMail custMail = getAccountCreationMessage(totalName, login, pwd, promotionCode, promotionPercent, urlActive);

        String subject = "Welcome to Dump Truck Rent and Dirt Collect system";
        MailSender mailSender = new MailSender();
        mailSender.sendMail(receiver, custMail, subject);
         System.out.println("Envoie du mail ok");  
        return res;
    }
    
    
    /**
     *
     * @param totalName Nom complet du client si possible formatter
     * @param login Login du client
     * @param password mot de passe du client
     * @param validationCode code promotionnel du client
     * @param promotionalDescription pourcentage du code promotionel
     * @param activateAccount url d'activation du compte
     * @return Le message deja formater
     */
    public CustomMail getAccountCreationMessage(String totalName, String login, String password, String validationCode, String promotionalDescription, String activateAccount) {
       
        HashMap hashValues = new HashMap();
        hashValues.put("###TOTALNAME", "<b>"+totalName+"</b>");
        hashValues.put("###LOGIN", "<b>"+login+"</b>");
        hashValues.put("###PASSWORD", "<b>"+password+"</b>");
        hashValues.put("###PROMOTIONAL_DESCRIPTION", promotionalDescription);
        hashValues.put("###VALIDATIONCODE", validationCode);
        //activateAccount = activateAccount.replaceAll("%","truckanddirt");
        hashValues.put("###ACTIVATE_ACCOUNT", activateAccount);
        
        CustomMail ctMail = new CustomMail(hashValues, FileMessageContent.MAIL_AccountCreationMessageContent);
        ctMail.buildMail();
        System.out.println(" Message lu buff ........");
        
        return ctMail;

    }
    
}
