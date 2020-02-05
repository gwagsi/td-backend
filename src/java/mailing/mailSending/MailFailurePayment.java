/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mailing.mailSending;

import mailing.mailTool.CustomMail;
import mailing.IMessageBuilder;
import mailing.mailTool.MailSender;

/**
 *
 * @author erman
 */
public abstract class MailFailurePayment implements Runnable{
    
    private final String[] personnalData;
    private final String[] msgContentData;
    private final String subject;
    private String systemMsg;
    private final String systemReciever = "customer.care@truksanddirt.com";

    public MailFailurePayment(String[] personnalData, String[] msgContentData, String subject, String systemMsg) {
        this.personnalData = personnalData;
        this.msgContentData = msgContentData;
        this.subject = subject;
        this.systemMsg = systemMsg;
    }
        

    public MailFailurePayment(String[] personnalData, String[] msgContentData, String subject) {
        this(personnalData, msgContentData, subject, null);
    }
        
    
    public String sendMailToContactTruckOwner(){
        String res = "envoyé";
        
        String name = personnalData[0];
        String firstName = personnalData[1];
        String receiver = personnalData[2];
        String accountNumber = personnalData[3];
        String totalName = MailFunction.getHtmlTotalName(name, firstName);
        String url = MailFunction.getUrlWeb();
        systemMsg = systemMsg.replace("###TOTALNAME", totalName)
                .replace("###ACCOUNT_NUMBER", accountNumber);
        
        CustomMail custMail = getMailFailureMessage(totalName, msgContentData, url);
        MailSender mailSender = new MailSender();
        mailSender.sendMail(receiver, custMail, subject);
        mailSender.sendMail(systemReciever, new IMessageBuilder() {

            @Override
            public String getMessageContent() {
                return systemMsg; //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public String getMessageTitle() {
                return ""; //To change body of generated methods, choose Tools | Templates.
            }
        }, subject);
        return res;
    }
    
    
    /**
     * @param totalName
     * @param msgContentData Données de remplacement à fournir
     * @param gwtUrl url de l'application
     * @return Le message deja formater
     */
    public abstract CustomMail getMailFailureMessage(String totalName, String[] msgContentData, String gwtUrl);
    
    
    @Override
    public void run() {
        sendMailToContactTruckOwner();
    }
    
}
