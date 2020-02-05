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
public class MailToContactTruckOwner {
    
    public String sendMailToContactTruckOwner(String receiver, String name, String firstName, String msg, String subject, boolean isCopy, String ccEmail){

        String res = "envoyé";
        
        String totalName = MailFunction.getHtmlTotalName(name, firstName);
        String url = MailFunction.getUrlWeb();
        
        CustomMail custMail = getMailToContactTruckOwnerMessage("Dear "+totalName+",<br>" + msg, url);
        
        MailSender mailSender = new MailSender();
        if (isCopy) {
            mailSender.sendMail(receiver, custMail, subject, ccEmail);
        } else {
            mailSender.sendMail(receiver, custMail, subject);
        }
        return res;
    }
    
    
    /**
     * @param msgContent Nom complet du client si possible formaient
     * @param gwtUrl url de l'application
     * @return Le message deja formater
     */
    public CustomMail getMailToContactTruckOwnerMessage(String msgContent, String gwtUrl) {
       
        HashMap hashValues = new HashMap();
        hashValues.put("###MESSAGECONTENT", msgContent);        
        hashValues.put("###GWT_URL", gwtUrl);        
        CustomMail ctMail = new CustomMail(hashValues, FileMessageContent.MAIL_DefaulMailContent);
        ctMail.buildMail();
        return ctMail;

    }
    
}
