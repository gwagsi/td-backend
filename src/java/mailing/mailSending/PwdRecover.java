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
 * @author erict
 */
public class PwdRecover {

    public boolean sendMail(String receiver, String name, String firstName, String login, String pwd){

        boolean res=true;
        
        String url = MailFunction.getUrlWeb();

        String totalName = MailFunction.getHtmlTotalName(name, firstName);
        String subject = "Your account information on Dump Truck Rent and Dirt Collect system";
            String msg = "Dear <b>"+totalName+"</b>,Your account information on the Dump Truck Rent and Dirt Collect System: "
                    + "<br><br> your login is : <b>"+login+"</b><br> your password is : <b>"+pwd+"</b>"
                    + "<br><br>Good Work on our System.";
        CustomMail custMail = getPwdRecoverMessage(msg, url);
        
        MailSender mailSender = new MailSender();
        mailSender.sendMail(receiver, custMail, subject);
        return res;
    }
    
    
    /**
     * @param msgContent Nom complet du client si possible formaient
     * @param gwtUrl url de l'application
     * @return Le message deja formater
     */
    public CustomMail getPwdRecoverMessage(String msgContent, String gwtUrl) {
       
        HashMap hashValues = new HashMap();
        hashValues.put("###MESSAGECONTENT", msgContent);        
        hashValues.put("###GWT_URL", gwtUrl);        
        CustomMail ctMail = new CustomMail(hashValues, FileMessageContent.MAIL_DefaulMailContent);
        ctMail.buildMail();
        return ctMail;

    }
    
}
