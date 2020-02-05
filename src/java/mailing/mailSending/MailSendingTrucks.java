/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mailing.mailSending;

import java.util.HashMap;
import java.util.List;
import mailing.mailContent.FileMessageContent;
import mailing.mailTool.CustomMail;
import mailing.mailTool.MailSender;

/**
 *
 * @author erman
 */
public class MailSendingTrucks {
    
    public String sendMail(List<String> receivers, String msg) {

        String res = " envoyé";
        String url = MailFunction.getUrlWeb();

        String subject = "Dump Truck Rent and Dirt Collect system";
        String msgContent = ("<FONT size=\"4\">" + msg + "<br>Please visit our platform to search the truck you need"
                + "<a href=\"" + url + "\">" + url + "</a>"
                + "<br>Good Work on our System.<br><br></FONT>");

        CustomMail custMail = getAddNewTruckMessage(msgContent, url);

        MailSender mailSender = new MailSender();
        mailSender.sendMail(receivers.toArray(), custMail, subject);
        return res;
    }


    
    /**
     * @param msgContent Nom complet du client si possible formaient
     * @param gwtUrl url de l'application
     * @return Le message deja formater
     */
    public CustomMail getAddNewTruckMessage(String msgContent, String gwtUrl) {
       
        HashMap hashValues = new HashMap();
        hashValues.put("###MESSAGECONTENT", msgContent);        
        hashValues.put("###GWT_URL", gwtUrl);        
        CustomMail ctMail = new CustomMail(hashValues, FileMessageContent.MAIL_DefaulMailContent);
        ctMail.buildMail();
        return ctMail;

    }
    
    
}
