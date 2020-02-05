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
public class MailSendingEmployeCode {

    
    public String sendMailToContactTruckOwner(String receiver, String name, String firstName, String empName, String empEmail, String empPhone, String empCode){

        String res = "envoyé";
        
        String subject = "New employee created";
        
        String excavatorTotalName = MailFunction.getHtmlTotalName(name, firstName);
        String gwtUrl = MailFunction.getUrlWeb();
        
        CustomMail custMail = getMailToContactTruckOwnerMessage(excavatorTotalName, empName, empEmail, empPhone, empCode, gwtUrl);
        
        MailSender mailSender = new MailSender();
        mailSender.sendMail(receiver, custMail, subject);
        return res;
    }
    
    
    /**
     * @param excavatorTotalName
     * @param empName
     * @param empEmail
     * @param empPhone
     * @param empCode
     * @param gwtUrl url de l'application
     * @return Le message deja formater
     */
    public CustomMail getMailToContactTruckOwnerMessage(String excavatorTotalName, String empName, String empEmail, String empPhone, String empCode, String gwtUrl) {
       
        HashMap hashValues = new HashMap();
        
        hashValues.put("###TOTALNAME", excavatorTotalName);
        hashValues.put("###EMP_NAME", empName.equals("null") ? "None" : empName);
        hashValues.put("###EMP_EMAIL", empEmail.equals("null") ? "None" : empEmail);
        hashValues.put("###EMP_PHONE", empPhone.equals("null") ? "None" : empPhone);
        hashValues.put("###EMP_CODE", empCode);
        hashValues.put("###GWT_URL", gwtUrl);
        CustomMail ctMail = new CustomMail(hashValues, FileMessageContent.MAIL_SendUserCodeMessageContent);
        ctMail.buildMail();
        return ctMail;

    }
    
}
