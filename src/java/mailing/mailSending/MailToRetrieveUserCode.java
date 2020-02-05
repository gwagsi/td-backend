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

/**
 *
 * @author erman
 */
public class MailToRetrieveUserCode {
    
    public boolean sendMailToRetrieveUserCode (String receiver, String name, String firstName, String userCode){

        boolean res=true;
        
        String totalName = MailFunction.getHtmlTotalName(name, firstName);
        
        CustomMail custMail = getMailToRetrieveUserCodeMessage(totalName, userCode);

        String subject = "Retrieve of your validation code";
        MailSender mailSender = new MailSender();
        mailSender.sendMail(receiver, custMail, subject);
        
        return res;
    }
    
    
    /**
     *
     * @param totalName Nom complet du client si possible formatter
     * @param userCode code de validation des jobs de l'excavator
     * @return Le message deja formater
     */
    public CustomMail getMailToRetrieveUserCodeMessage(String totalName, String userCode) {
       
        
        String gwtUrl = MailFunction.getUrlWeb();
        
        HashMap hashValues = new HashMap();
        hashValues.put("###TOTALNAME", totalName);
        hashValues.put("###USERCODE", userCode);
        hashValues.put("###GWT_URL", gwtUrl);
        
        CustomMail ctMail = new CustomMail(hashValues, FileMessageContent.MAIL_RetrieveUserCodeMessageContent);
        ctMail.buildMail();
        System.out.println(" Message lu buff ........");
        
        return ctMail;

    }
    
}
