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
import toolsAndTransversalFunctionnalities.Result;

/**
 *
 * @author erman
 */
public class MailSendingNewJobBooking implements Runnable {
    
    private Result result = null;

    public MailSendingNewJobBooking(Result result) {
        this.result = result;
    }
    
    public String sendMailNewJobBooking(){

        String res = "envoyé";
        MailFunction mf = new MailFunction();
        
        String[] clientInfo = result.getObject().split(";");
        String clientEmail = clientInfo[0];
        String clientName = clientInfo[1];
        String clientSurname = clientInfo[2];
        String clientTotalName = MailFunction.getHtmlTotalName(clientName, clientSurname);
        String telephone = (clientInfo[3] == null || clientInfo[3].equals("") || clientInfo[3].split("#")[0].equals("") ? "Not set" : clientInfo[3].split("#")[0]);
        String cellPhone = (clientInfo[4] == null || clientInfo[4].equals("") || clientInfo[4].split("#")[0].equals("") ? "Not set" : clientInfo[4].split("#")[0]);
        
        List<String> list = result.getObjectList();
        String subject = "Trucksanddirt: New Job Reservation on Your Truck";
        
        for (String jtOwnerInfo : list) {
            
            String[] truckOwnerInfo = jtOwnerInfo.split("##")[0].split(";");
            String[] jobResponseInfo = jtOwnerInfo.split("##")[1].split(";");
            String truckNumber = jtOwnerInfo.split("##")[2];
            String truckOwnerEmail = truckOwnerInfo[0];
            String truckOwnerName = truckOwnerInfo[1];
            String truckOwnerSurname = truckOwnerInfo[2];
            
            String jobNumberRtn = jobResponseInfo[3];
            String jobLocationRtn = jobResponseInfo[2];
            String jobStartDate = jobResponseInfo[0];
            String jobEndDate = jobResponseInfo[1];
            String hourPerDayRtn = jobResponseInfo[4];
            String numberOfTruckRtn = jobResponseInfo[5];
            String jobDescriptionRtn = (jobResponseInfo[6] == null || jobResponseInfo[6].equals("") || jobResponseInfo[6].equals("null") ? "None" : jobResponseInfo[6]);
            String jobInstructionRtn = (jobResponseInfo[7] == null || jobResponseInfo[7].equals("") || jobResponseInfo[7].equals("null") ? "None" : jobResponseInfo[7]);
            String dotNumber = (jobResponseInfo[8] == null || jobResponseInfo[8].equals("") || jobResponseInfo[8].equals("null") ? "None" : jobResponseInfo[8]);
            String generalLiability = (jobResponseInfo[9] == null || jobResponseInfo[9].equals("") || jobResponseInfo[9].equals("null") ? "None" : "$" + jobResponseInfo[9] + " min");
            String truckLiability = (jobResponseInfo[10] == null || jobResponseInfo[10].equals("") || jobResponseInfo[10].equals("null") ? "None" : "$" + jobResponseInfo[10] + " min");
            String proofOfTruckLiability = (jobResponseInfo[11] == null || jobResponseInfo[11].equals("") || jobResponseInfo[11].equals("null") ? "None" : jobResponseInfo[11]);
            String lenghtOfBed = (jobResponseInfo[12] == null || jobResponseInfo[12].equals("") || jobResponseInfo[12].equals("null") ? "None" : jobResponseInfo[12]);
            String truckAxle = (jobResponseInfo[13] == null || jobResponseInfo[13].equals("") || jobResponseInfo[13].equals("null") ? "None" : jobResponseInfo[13]);
            
            String truckOwnerTotalName = MailFunction.getHtmlTotalName(truckOwnerName, truckOwnerSurname);
            String gwtUrl = MailFunction.getUrlWeb();
            
            CustomMail custMail = getAddNewJobBookingMailMessage (truckOwnerTotalName, jobNumberRtn, jobLocationRtn, jobStartDate, 
                    jobEndDate, clientTotalName, gwtUrl, hourPerDayRtn, numberOfTruckRtn, mf, jobDescriptionRtn, jobInstructionRtn,
                    clientEmail, telephone, cellPhone, dotNumber, generalLiability, truckLiability, proofOfTruckLiability, 
                    lenghtOfBed, truckAxle, truckNumber);
            
            MailSender mailSender = new MailSender();
            String sendState = mailSender.sendMail(truckOwnerEmail, custMail, subject);
            
            System.out.println("sendMailNewJobBooking : truckOwnerEmail:  " + truckOwnerEmail  + "  -   sendState: " + sendState);
            
        }
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
     * @param truckNumber
     * @param lenghtOfBed
     * @param generalLiability
     * @param dotNumber
     * @return Le message deja formater
     */
    public CustomMail getAddNewJobBookingMailMessage(String truckOwnerTotalName, String jobNumberRtn, String jobLocationRtn, String jobStartDate, String jobEndDate, String clientTotalName, String gwtUrl, String hourPerDayRtn, String numberOfTruckRtn, MailFunction mf, String jobDescriptionRtn, String jobInstructionRtn, String clientEmail, String telephone, String cellPhone, String dotNumber, String generalLiability, String truckLiability, String proofOfTruckLiability, String lenghtOfBed, String truckAxle, String truckNumber) {
       
        HashMap hashValues = new HashMap();
        jobLocationRtn = mf.replaceDotCommaCharReverse(jobLocationRtn);
        hashValues.put("###TOTALNAME", truckOwnerTotalName);
        hashValues.put("###JOB_NUMBER", jobNumberRtn);
        hashValues.put("###JOB_LOCATION", jobLocationRtn);
        hashValues.put("###JOB_STARTDATE", jobStartDate);
        hashValues.put("###JOB_ENDDATE", jobEndDate);
        hashValues.put("###TOTALNAME2", clientTotalName);
        hashValues.put("###GWT_URL", gwtUrl);
        hashValues.put("###HOUR_PER_DAY", hourPerDayRtn);
        hashValues.put("###NUMBER_OF_TRUCK", numberOfTruckRtn);
        hashValues.put("###DESCRIPTION", mf.subString(mf.replaceDotCommaCharReverse(jobDescriptionRtn), 0, mf.maxLengh));
        hashValues.put("###INSTRUCTION", mf.subString(mf.replaceDotCommaCharReverse(jobInstructionRtn), 0, mf.maxLengh));
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
        hashValues.put("###TRUCKS_NUMBER", truckNumber);
        
        CustomMail ctMail = new CustomMail(hashValues, FileMessageContent.MAIL_NewJobOnTruckMessageContent);
        ctMail.buildMail();
        return ctMail;

    }

    @Override
    public void run() {
        sendMailNewJobBooking();
    }
    
}
