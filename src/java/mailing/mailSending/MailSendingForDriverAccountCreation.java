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
public class MailSendingForDriverAccountCreation {
    
    public boolean sendAccountCreationMail(String[] driverMailInfo){

        boolean res=true;
        
        int id = Integer.parseInt(driverMailInfo[0]);
        //int typeOfUser = Integer.parseInt(driverMailInfo[12]);
        String name = driverMailInfo[1];
        String firstName = driverMailInfo[2];
        String login = driverMailInfo[6];
        String pwd = driverMailInfo[7];
        String receiver = driverMailInfo[5];
        String ccEmail = driverMailInfo[8];
        String truckOwnerName = driverMailInfo[9];
        String truckOwnerSurname = driverMailInfo[10];
        
        
        String webURL;
        webURL = MailFunction.getUrlWeb();
        
        
        String totalName = MailFunction.getHtmlTotalName(name, firstName);
        String truckOwnerTotalName = MailFunction.getHtmlTotalName(truckOwnerName, truckOwnerSurname);
        
        CustomMail custMail = getDriverAccountCreationMessage(totalName, login, pwd, truckOwnerTotalName, webURL);

        String subject = "Welcome to Dump Truck Rent and Dirt Collect system";
        MailSender mailSender = new MailSender();
        mailSender.sendMail(receiver, custMail, subject, ccEmail);
        
        return res;
    }
    
    
    /**
     *
     * @param totalName Nom complet du client si possible formatter
     * @param login Login du client
     * @param password mot de passe du client
     * @param truckOwnerTotalName nom complet du TruckOwner
     * @param webURL url d'activation du compte
     * @return Le message deja formater
     */
    public CustomMail getDriverAccountCreationMessage(String totalName, String login, String password, String truckOwnerTotalName,  String webURL) {
       
        HashMap hashValues = new HashMap();
        hashValues.put("###TOTALNAME", "<b>"+totalName+"</b>");
        hashValues.put("###2TOTALNAME", "<b>"+truckOwnerTotalName+"</b>");
        hashValues.put("###LOGIN", "<b>"+login+"</b>");
        hashValues.put("###PASSWORD", "<b>"+password+"</b>");
        hashValues.put("###WEB_URL", webURL);
        
        CustomMail ctMail = new CustomMail(hashValues, FileMessageContent.MAIL_DriverAccountCreationMessageContent);
        ctMail.buildMail();
        System.out.println(" Message lu buff ");
        
        return ctMail;

    }
    
}
