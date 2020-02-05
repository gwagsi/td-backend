/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mailing.mailSending;

import java.util.HashMap;
import mailing.mailContent.FileMessageContent;
import mailing.mailTool.CustomMail;


public class MailFailurePaymentTruckOwner extends MailFailurePayment {

    public MailFailurePaymentTruckOwner(String[] personnalData, String[] msgContentData, String subject, String systemMsg) {
        super(personnalData, msgContentData, subject, systemMsg);
    }
    
    @Override
    public CustomMail getMailFailureMessage(String totalName, String[] msgContentData, String gwtUrl) {
       
        HashMap hashValues = new HashMap();     
        hashValues.put("###TOTALNAME", totalName);
        hashValues.put("###1COMPAGNY_NAME", msgContentData[0]);
        hashValues.put("###2COMPAGNY_NAME", msgContentData[0]);
        hashValues.put("###GWT_URL", gwtUrl);
        
        CustomMail ctMail = new CustomMail(hashValues, FileMessageContent.MAIL_FailurePaymentTruckOwner);
        ctMail.buildMail();
        return ctMail;

    }
    
}
