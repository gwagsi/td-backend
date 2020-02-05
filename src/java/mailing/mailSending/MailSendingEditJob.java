/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mailing.mailSending;

import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import jobManagement.dao.IJobManagementDaoLocal;
import mailing.mailContent.FileMessageContent;
import mailing.mailTool.CustomMail;
import mailing.mailTool.MailSender;
import util.exception.JobManagementException;
import util.exception.PushNotificationException;

/**
 *
 * @author erman
 */
public class MailSendingEditJob implements Runnable {

    private List<Object[]> userListInfo = null;
    private List<String> modifiedField = null;
    private final int jobID;
    private final String[] excavatorInfo;
    private final String[][] jobResponseInfos;
    private final float latitude;
    private final float longitude;
    
    
    @EJB
    IJobManagementDaoLocal jobDAO;

    public MailSendingEditJob(IJobManagementDaoLocal jobDAO, int jobID, String[][] jobResponseInfos, String[] excavatorInfo, float latitude, 
            float longitude, List<String> modifiedField) throws NullPointerException{
        if(excavatorInfo == null || jobResponseInfos == null || jobDAO == null) throw new NullPointerException();
        this.jobID = jobID;
        this.jobDAO = jobDAO;
        this.excavatorInfo = excavatorInfo;
        this.jobResponseInfos = jobResponseInfos;
        this.latitude = latitude;
        this.longitude = longitude;
        this.modifiedField = modifiedField;
    }

    private String sendMailEditJob() {
        try{
        userListInfo = jobDAO.searchTruckOwnerToNotify(jobID, latitude, longitude);
          }
        catch( JobManagementException error){
           
        }
        
        if (userListInfo == null) {
            System.out.println("sendMailEditJob: res: NotExistingTruckOwner");
            return "NotExistingTruckOwner";
        }
        
        if (modifiedField == null || modifiedField.isEmpty()) {
            System.out.println("sendMailEditJob: res: NotNewInformation");
            return "NotNewInformation";
        }

        String res = "envoyé";
        MailFunction mf = new MailFunction();

        String clientEmail = excavatorInfo[0];
        String clientName = excavatorInfo[1];
        String clientSurname = excavatorInfo[2];
        String clientTotalName = MailFunction.getHtmlTotalName(clientName, clientSurname);
        String telephone = getNoneForNullValuePhone(excavatorInfo[3].split("#")[0]);
        String cellPhone = getNoneForNullValuePhone(excavatorInfo[4].split("#")[0]);

        String[] jobNumberRtn = jobResponseInfos[3];
        String[] jobLocationRtn = jobResponseInfos[2];
        String[] jobStartDate = jobResponseInfos[0];
        String[] jobEndDate = jobResponseInfos[1];
        String[] hourPerDayRtn = jobResponseInfos[4];
        String[] numberOfTruckRtn = jobResponseInfos[5];
        String[] jobDescriptionRtn = jobResponseInfos[6];
        String[] jobInstructionRtn = jobResponseInfos[7];
        String[] dotNumber = jobResponseInfos[8];
        String[] generalLiability = jobResponseInfos[9];
        String[] truckLiability = jobResponseInfos[10];
        String[] proofOfTruckLiability = jobResponseInfos[11];
        String[] lenghtOfBed = jobResponseInfos[12];
        String[] truckAxle = jobResponseInfos[13];

        String subject = "Trucksanddirt: Updating Of Job Informations";

        for (Object[] truckOwnerInfo : userListInfo) {

            String truckOwnerEmail = String.valueOf(truckOwnerInfo[3]);
            
            String truckOwnerName = String.valueOf(truckOwnerInfo[1]);
            String truckOwnerSurname = String.valueOf(truckOwnerInfo[2]);
            System.out.println("sendMailNewJobNotification: truckOwnerDistanceWithin: " + String.valueOf(truckOwnerInfo[4]));
            float truckOwnerDistanceWithin = 0;
            try {
                truckOwnerDistanceWithin = Float.parseFloat(String.valueOf(truckOwnerInfo[4]));
            } catch (NumberFormatException numberFormatException) {
                System.out.println("sendMailNewJobNotification: truckOwnerDistanceWithin: " + String.valueOf(truckOwnerInfo[4]));
                System.out.println("sendMailNewJobNotification: numberFormatException: " + numberFormatException.getMessage());
            }

            String truckOwnerTotalName = MailFunction.getHtmlTotalName(truckOwnerName, truckOwnerSurname);
            String gwtUrl = MailFunction.getUrlWeb();

            CustomMail custMail = getEditJobMailMessage(truckOwnerTotalName, jobNumberRtn, jobLocationRtn, jobStartDate,
                    jobEndDate, clientTotalName, gwtUrl, hourPerDayRtn, numberOfTruckRtn, mf, jobDescriptionRtn, jobInstructionRtn,
                    clientEmail, telephone, cellPhone, dotNumber, generalLiability, truckLiability, proofOfTruckLiability,
                    lenghtOfBed, truckAxle, truckOwnerDistanceWithin);

            MailSender mailSender = new MailSender();
            String sendState = mailSender.sendMail(truckOwnerEmail, custMail, subject);

            System.out.println("sendMailNewJobNotification : truckOwnerEmail:  " + truckOwnerEmail + "  -   sendState: " + sendState + "  -  truckOwnerTotalName : " + truckOwnerTotalName);

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
     * @param lenghtOfBed
     * @param generalLiability
     * @param dotNumber
     * @param truckOwnerDistanceWithin
     * @return Le message deja formater
     */
    public CustomMail getEditJobMailMessage(String truckOwnerTotalName, String[] jobNumberRtn, String[] jobLocationRtn, 
            String[] jobStartDate, String[] jobEndDate, String clientTotalName, String gwtUrl, String[] hourPerDayRtn, 
            String[] numberOfTruckRtn, MailFunction mf, String[] jobDescriptionRtn, String[] jobInstructionRtn, 
            String clientEmail, String telephone, String cellPhone, String[] dotNumber, String[] generalLiability, 
            String[] truckLiability, String[] proofOfTruckLiability, String[] lenghtOfBed, String[] truckAxle, 
            float truckOwnerDistanceWithin) {

        jobLocationRtn[0] = mf.replaceDotCommaCharReverse(jobLocationRtn[0]);
        jobLocationRtn[1] = mf.replaceDotCommaCharReverse(jobLocationRtn[1]);
        HashMap hashValues = new HashMap();
        hashValues.put("###TOTALNAME", truckOwnerTotalName);
        hashValues.put("###2JOB_NUMBER", jobNumberRtn[0]);
        hashValues.put("###JOB_NUMBER", jobNumberRtn[0]);
        hashValues.put("###JOB_LOCATION", getHTMLInfoChanged(jobLocationRtn, keyJobLocation));
        hashValues.put("###JOB_STARTDATE", getHTMLInfoChanged(jobStartDate, keyJobStartDate));
        hashValues.put("###JOB_ENDDATE", getHTMLInfoChanged(jobEndDate, keyJobEndDate));
        hashValues.put("###2TOTALNAME", clientTotalName);
        hashValues.put("###GWT_URL", gwtUrl);
        hashValues.put("###HOUR_PER_DAY", hourPerDayRtn[0]);
        hashValues.put("###NUMBER_OF_TRUCK", getHTMLInfoChanged(numberOfTruckRtn, keyNbTruck));
        hashValues.put("###DESCRIPTION", getHTMLInfosChangedBigStr(mf.subString(mf.replaceDotCommaCharReverse(jobDescriptionRtn[0]), 0, mf.maxLengh), keyJobDescription));
        hashValues.put("###INSTRUCTION", getHTMLInfosChangedBigStr(mf.subString(mf.replaceDotCommaCharReverse(jobInstructionRtn[0]), 0, mf.maxLengh), keyJobInstruction));
        hashValues.put("###EMAIL1", clientEmail);
        hashValues.put("###EMAIL2", clientEmail);
        hashValues.put("###PRIMARY_PHONE", telephone);
        hashValues.put("###CELLPHONE", cellPhone);
        hashValues.put("###DOT_NUMBER", getHTMLInfoChanged(dotNumber, keyDOT_Number));
        hashValues.put("###GENERAL_LIABILITY", getHTMLInfoLiability(generalLiability, keyGeneralLiability));
        hashValues.put("###TRUCK_LIABILITY", getHTMLInfoLiability(truckLiability, keyTruckLiability));
        hashValues.put("###PROOF_LIABILITY", getHTMLInfoChanged(proofOfTruckLiability, keyProofOfTruckLiability));
        hashValues.put("###LENGTH_OF_BED", getHTMLInfoChanged(lenghtOfBed, keyLenghtOfBed));
        hashValues.put("###TRUCK_TYPE", getHTMLInfoChanged(truckAxle, keyTruckAxle));
        hashValues.put("###DISTANCE_WITHIN", getHTMLInfosChangedDistane((truckOwnerDistanceWithin == -1 ? "Not defined" : (truckOwnerDistanceWithin >= 0 && truckOwnerDistanceWithin <= 1 ? "Close to you" : truckOwnerDistanceWithin))));

        CustomMail ctMail = new CustomMail(hashValues, FileMessageContent.MAIL_EditJobMessageContent);
        ctMail.buildMail();
        return ctMail;

    }

    private String getHTMLInfoChanged(String[] informations, String keyInfo) {
        return (modifiedField.contains(keyInfo)
                ? "<span style=\"background-color: #FFE7E7;\"><s>" + getNoneForNullValue(informations[1]) + "</s></span>&nbsp;"
                + "<span style=\"background-color:#ddfade;\"><b>" + getNoneForNullValue(informations[0]) + "</b></span>"
                : "<b>" + getNoneForNullValue(informations[0]) + "</b>");
        
    }

    private String getHTMLInfoLiability(String[] informations, String keyInfo) {
        return (modifiedField.contains(keyInfo)
                ? "<span style=\"background-color: #FFE7E7;\"><s>$" + getNoneForNullValue(informations[1]) + " min</s></span>&nbsp;"
                + "<span style=\"background-color:#ddfade;\"><b>$" + getNoneForNullValue(informations[0]) + " min</b></span>"
                : "<b>$" + getNoneForNullValue(informations[0]) + " min</b>");
        
    }

    private String getHTMLInfosChangedBigStr(String information,  String keyInfo){
        return (modifiedField.contains(keyInfo) ? 
                "<span style=\"background-color:#ddfade;\">" + getNoneForNullValue(information) + "</span>" : getNoneForNullValue(information));
    }
    
    private String getHTMLInfosChangedDistane(Object information){
        return (modifiedField.contains(keyJobLocation) ? 
                "<span style=\"background-color:#ddfade;\"><b>" + String.valueOf(information) + "</b></span>" : "<b>" + String.valueOf(information) + "</b>");
    }
    
    private String getNoneForNullValue(String string){
        return (string == null || string.equals("") || string.equals("null") ? "None" : string);
    }

    private String getNoneForNullValuePhone(String value){
        return (value == null || value.equals("") || value.equals("null") ? "Not set" : value);
    }
    
    @Override
    public void run() {
        String res = sendMailEditJob();
        System.out.println("MailSendingEditJob(run) ~ res : " + res);
    }

    private final static String keyJobLocation = "JOB_LOCATION";
    private final static String keyJobStartDate = "START_DATE";
    private final static String keyJobEndDate = "END_DATE";
    private final static String keyNbTruck = "NB_TRUCK";
    private final static String keyJobDescription = "DESCRIPTION";
    private final static String keyJobInstruction = "INSTRUCTION";
    private final static String keyDOT_Number = "DOT_NUMBER";
    private final static String keyGeneralLiability = "GENERAL_LIABILITY";
    private final static String keyTruckLiability = "TRUCK_LIABILITY";
    private final static String keyLenghtOfBed = "LENGHT_OF_BED";
    private final static String keyTruckAxle = "TRUCK_AXLE";
    private final static String keyProofOfTruckLiability = "PROOF_OF_TRUCK_LIABILITY";

}
