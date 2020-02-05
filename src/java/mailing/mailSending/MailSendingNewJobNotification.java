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
public class MailSendingNewJobNotification {

    private List<Object[]> userListInfo;
    private final String[] excavatorInfo;
    private final String[] jobResponseInfos;
    
    public MailSendingNewJobNotification(String[] excavatorInfo, String[] jobResponseInfos,
            List<Object[]> userListInfo) throws NullPointerException {
        if (excavatorInfo == null || jobResponseInfos == null || userListInfo == null) {
            throw new NullPointerException();
        }
        this.excavatorInfo = excavatorInfo;
        this.jobResponseInfos = jobResponseInfos;
        this.userListInfo = userListInfo;
    }

    public String sendMailNewJobNotification() {

        if (userListInfo == null) {
            return "NotExistingTruckOwner";
        }

        String res = "envoyé";
        MailFunction mf = new MailFunction();

        String clientEmail = excavatorInfo[0];
        String clientName = excavatorInfo[1];
        String clientSurname = excavatorInfo[2];
        String clientTotalName = MailFunction.getHtmlTotalName(clientName, clientSurname);
        String telephone = getNoneForNullValuePhone(excavatorInfo[3].split("#")[0]);
        String cellPhone = getNoneForNullValuePhone(excavatorInfo[4].split("#")[0]);

        
        String jobNumberRtn = jobResponseInfos[3];
        String jobLocationRtn = jobResponseInfos[2];
        String jobStartDate = jobResponseInfos[0];
        String jobEndDate = jobResponseInfos[1];
        String hourPerDayRtn = jobResponseInfos[4];
        String numberOfTruckRtn = jobResponseInfos[5];
        String jobDescriptionRtn = getNoneForNullValue(jobResponseInfos[6]);
        String jobInstructionRtn = getNoneForNullValue(jobResponseInfos[7]);
        String dotNumber = getNoneForNullValue(jobResponseInfos[8]);
        String generalLiability = getNoneForNullValueLiability(jobResponseInfos[9]);
        String truckLiability = getNoneForNullValueLiability(jobResponseInfos[10]);
        String proofOfTruckLiability = getNoneForNullValue(jobResponseInfos[11]);
        String lenghtOfBed = getNoneForNullValue(jobResponseInfos[12]);
        String truckAxle = getNoneForNullValue(jobResponseInfos[13]);

        String subject = "Trucksanddirt: New Job On TrucksAndDirt.com";

        for (Object[] truckOwnerInfo : userListInfo) {

            String truckOwnerEmail = String.valueOf(truckOwnerInfo[1]);
            
            String truckOwnerName = String.valueOf(truckOwnerInfo[2]);
            String truckOwnerSurname = String.valueOf(truckOwnerInfo[3]);
            //System.out.println("sendMailNewJobNotification: truckOwnerDistanceWithin: " + String.valueOf(truckOwnerInfo[6]));
            float truckOwnerDistanceWithin = 0;
            try {
                truckOwnerDistanceWithin = Float.parseFloat(String.valueOf(truckOwnerInfo[6]));
            } catch (NumberFormatException numberFormatException) {
                System.out.println("sendMailNewJobNotification: truckOwnerDistanceWithin: " + String.valueOf(truckOwnerInfo[6]));
                System.out.println("sendMailNewJobNotification: numberFormatException: " + numberFormatException.getMessage());
            }

            String truckOwnerTotalName = MailFunction.getHtmlTotalName(truckOwnerName, truckOwnerSurname);
            String gwtUrl = MailFunction.getUrlWeb();

            CustomMail custMail = getNewJobMailNotificationMessage(truckOwnerTotalName, jobNumberRtn, jobLocationRtn, jobStartDate,
                    jobEndDate, clientTotalName, gwtUrl, hourPerDayRtn, numberOfTruckRtn, mf, jobDescriptionRtn, jobInstructionRtn,
                    clientEmail, telephone, cellPhone, dotNumber, generalLiability, truckLiability, proofOfTruckLiability,
                    lenghtOfBed, truckAxle, truckOwnerDistanceWithin);

            MailSender mailSender = new MailSender();
            String sendState = mailSender.sendMail(truckOwnerEmail, custMail, subject);

            System.out.println("sendMailNewJobNotification : truckOwnerEmail:  " + truckOwnerEmail + "  -   sendState: " + sendState + "  -  truckOwnerTotalName : " + truckOwnerTotalName);

        }
        
        System.out.println("[MailSendingNewJobNotification] res: " + res);
        return res;
    }

    /**
     * @param truckOwnerTotalName
     * @param jobNumberRtn
     * @param jobLocationRtn
     * @param jobStartDate
     * @param jobEndDate
     * @param clientTotalName
     * @param numberOfTruckRtn
     * @param mf
     * @param jobDescriptionRtn
     * @param jobInstructionRtn
     * @param telephone
     * @param cellPhone
     * @param hourPerDayRtn
     * @param clientEmail
     * @param proofOfTruckLiability
     * @param truckLiability
     * @param gwtUrl url de l'application
     * @param truckAxle
     * @param lenghtOfBed
     * @param generalLiability
     * @param dotNumber
     * @param truckOwnerDistanceWithin
     * @return Le message deja formater
     */
    public CustomMail getNewJobMailNotificationMessage(String truckOwnerTotalName, String jobNumberRtn, String jobLocationRtn, 
            String jobStartDate, String jobEndDate, String clientTotalName, String gwtUrl, String hourPerDayRtn, 
            String numberOfTruckRtn, MailFunction mf, String jobDescriptionRtn, String jobInstructionRtn, String clientEmail, 
            String telephone, String cellPhone, String dotNumber, String generalLiability, String truckLiability, 
            String proofOfTruckLiability, String lenghtOfBed, String truckAxle, float truckOwnerDistanceWithin) {

        HashMap hashValues = new HashMap();
        jobLocationRtn = MailFunction.replaceDotCommaCharReverse(jobLocationRtn);
        hashValues.put("###TOTALNAME", truckOwnerTotalName);
        hashValues.put("###JOB_NUMBER", jobNumberRtn);
        hashValues.put("###JOB_LOCATION", jobLocationRtn);
        hashValues.put("###JOB_STARTDATE", jobStartDate);
        hashValues.put("###JOB_ENDDATE", jobEndDate);
        hashValues.put("###TOTALNAME2", clientTotalName);
        hashValues.put("###GWT_URL", gwtUrl);
        hashValues.put("###HOUR_PER_DAY", hourPerDayRtn);
        hashValues.put("###NUMBER_OF_TRUCK", numberOfTruckRtn);
        hashValues.put("###DESCRIPTION", mf.subString(MailFunction.replaceDotCommaCharReverse(jobDescriptionRtn), 0, mf.maxLengh));
        hashValues.put("###INSTRUCTION", mf.subString(MailFunction.replaceDotCommaCharReverse(jobInstructionRtn), 0, mf.maxLengh));
        hashValues.put("###EMAIL1", clientEmail);
        hashValues.put("###EMAIL2", clientEmail);
        hashValues.put("###PRIMARY_PHONE", telephone);
        hashValues.put("###CELLPHONE", cellPhone);
        hashValues.put("###DOT_NUMBER", dotNumber);
        hashValues.put("###GENERAL_LIABILITY", generalLiability);
        hashValues.put("###TRUCK_LIABILITY", truckLiability);
        hashValues.put("###PROOF_LIABILITY", proofOfTruckLiability);
        hashValues.put("###LENGTH_OF_BED", lenghtOfBed);
        hashValues.put("###TRUCK_TYPE", truckAxle);
        hashValues.put("###DISTANCE_WITHIN", (truckOwnerDistanceWithin == -1 ? "Not defined" : (truckOwnerDistanceWithin >= 0 && truckOwnerDistanceWithin <= 1 ? "Close to you" : truckOwnerDistanceWithin)));

        CustomMail ctMail = new CustomMail(hashValues, FileMessageContent.MAIL_AddNewJobNotificationMessageContent);
        ctMail.buildMail();
        return ctMail;

    }

    private String getNoneForNullValue(String value){
        return (value == null || value.equals("") || value.equals("null") ? "None" : value);
    }

    private String getNoneForNullValueLiability(String value){
        return (value == null || value.equals("") || value.equals("null") ? "None" : "$" + value + " min");
    }

    private String getNoneForNullValuePhone(String value){
        return (value == null || value.equals("") || value.equals("null") ? "Not set" : value);
    }
    /*
    @Override
    public void run() {
        String res = sendMailNewJobNotification();
        System.out.println("sendMailNewJobNotification(run) ~ res : " + res);
    }
    */
}
