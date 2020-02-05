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
public class MailToContactUs {
    
    public String sendMailToContactUs(String receiver, String msg, String subject){

        String res = "envoyé";
        
        String url = MailFunction.getUrlWeb();
        
        CustomMail custMail = getContactUsMessage(msg, url);
        
        MailSender mailSender = new MailSender();
        mailSender.sendMail(receiver, custMail, subject);
        return res;
    }
    
    
    /**
     * @param msgContent Nom complet du client si possible formaient
     * @param gwtUrl url de l'application
     * @return Le message deja formater
     */
    public CustomMail getContactUsMessage(String msgContent, String gwtUrl) {
       
        HashMap hashValues = new HashMap();
        hashValues.put("###MESSAGECONTENT", msgContent);        
        hashValues.put("###GWT_URL", gwtUrl);        
        CustomMail ctMail = new CustomMail(hashValues, FileMessageContent.MAIL_DefaulMailContent);
        ctMail.buildMail();
        return ctMail;

    }
    
}
