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
import toolsAndTransversalFunctionnalities.Result;

/**
 *
 * @author erman
 */
public class MailSendingJobCancelTruckOwner implements Runnable {

    private Result result = null;

    public MailSendingJobCancelTruckOwner(Result result) {
        this.result = result;
    }

    public String sendTruckOwnerJobCancelMail() {

        String res = "envoyé";
        MailFunction mf = new MailFunction();
        String subject = "Trucksanddirt: Job Cancellation";

        String[] excavatorInfo = result.getObject().split("##")[0].split(";");
        String[] truckOwnerInfo = result.getObject().split("##")[1].split(";");
        String[] jobResponseInfo = result.getObject().split("##")[2].split(";");

        String truckOwnerEmail = truckOwnerInfo[0];
        String truckOwnerName = truckOwnerInfo[1];
        String truckOwnerSurname = truckOwnerInfo[2];
        String telephone = (truckOwnerInfo[3] == null || truckOwnerInfo[3].equals("") || truckOwnerInfo[3].split("#")[0].equals("") ? "Not set" : truckOwnerInfo[3].split("#")[0]);
        String cellPhone = (truckOwnerInfo[4] == null || truckOwnerInfo[4].equals("") || truckOwnerInfo[4].split("#")[0].equals("") ? "Not set" : truckOwnerInfo[4].split("#")[0]);

        String excavatorEmail = excavatorInfo[0];
        String excavatorName = excavatorInfo[1];
        String excavatorSurname = excavatorInfo[2];
        String truckOwnerTotalName = MailFunction.getHtmlTotalName(truckOwnerName, truckOwnerSurname);

        String jobNumber = jobResponseInfo[3];
        String jobLocation = jobResponseInfo[2];
        String jobStartDate = jobResponseInfo[0];
        String jobEndDate = jobResponseInfo[1];
        String hourPerDayRtn = jobResponseInfo[4];
        String numberOfTruckRtn = jobResponseInfo[5];
        String jobDescription = (jobResponseInfo[6] == null || jobResponseInfo[6].equals("") || jobResponseInfo[6].equals("null") ? "None" : jobResponseInfo[6]);
        String jobInstruction = (jobResponseInfo[7] == null || jobResponseInfo[7].equals("") || jobResponseInfo[7].equals("null") ? "None" : jobResponseInfo[7]);
        String dotNumber = (jobResponseInfo[8] == null || jobResponseInfo[8].equals("") || jobResponseInfo[8].equals("null") ? "None" : jobResponseInfo[8]);
        String generalLiability = (jobResponseInfo[9] == null || jobResponseInfo[9].equals("") || jobResponseInfo[9].equals("null") ? "None" : "$" + jobResponseInfo[9] + " min");
        String truckLiability = (jobResponseInfo[10] == null || jobResponseInfo[10].equals("") || jobResponseInfo[10].equals("null") ? "None" : "$" + jobResponseInfo[10] + " min");
        String proofOfTruckLiability = (jobResponseInfo[11] == null || jobResponseInfo[11].equals("") || jobResponseInfo[11].equals("null") ? "None" : jobResponseInfo[11]);
        String lenghtOfBed = (jobResponseInfo[12] == null || jobResponseInfo[12].equals("") || jobResponseInfo[12].equals("null") ? "None" : jobResponseInfo[12]);
        String truckAxle = (jobResponseInfo[13] == null || jobResponseInfo[13].equals("") || jobResponseInfo[13].equals("null") ? "None" : jobResponseInfo[13]);

        String excavatorTotalName = MailFunction.getHtmlTotalName(excavatorName, excavatorSurname);
        String gwtUrl = MailFunction.getUrlWeb();
        CustomMail custMail = getJobCancelTruckOwnerMessage(excavatorTotalName, jobNumber, jobLocation, jobStartDate, jobEndDate, truckOwnerTotalName, gwtUrl, hourPerDayRtn, numberOfTruckRtn, mf, jobDescription, jobInstruction, truckOwnerEmail, telephone, cellPhone, dotNumber, generalLiability, truckLiability, proofOfTruckLiability, lenghtOfBed, truckAxle);

        MailSender mailSender = new MailSender();
        String sendState = mailSender.sendMail(excavatorEmail, custMail, subject);

        System.out.println("sendTruckOwnerJobCancelMail : To excavatorEmail:  " + excavatorEmail + "  -   sendState: " + sendState);

        return res;
    }

    public CustomMail getJobCancelTruckOwnerMessage(String excavatorTotalName, String jobNumber, String jobLocation, String jobStartDate, String jobEndDate, String truckOwnerTotalName, String gwtUrl, String hourPerDayRtn, String numberOfTruckRtn, MailFunction mf, String jobDescription, String jobInstruction, String truckOwnerEmail, String telephone, String cellPhone, String dotNumber, String generalLiability, String truckLiability, String proofOfTruckLiability, String lenghtOfBed, String truckAxle) {
        HashMap hashValues = new HashMap();
        jobLocation = mf.replaceDotCommaCharReverse(jobLocation);
        hashValues.put("###TOTALNAME", excavatorTotalName);
        hashValues.put("###JOB_NUMBER", jobNumber);
        hashValues.put("###JOB_LOCATION", jobLocation);
        hashValues.put("###JOB_STARTDATE", jobStartDate);
        hashValues.put("###JOB_ENDDATE", jobEndDate);
        hashValues.put("###TOTALNAME2", truckOwnerTotalName);
        hashValues.put("###GWT_URL", gwtUrl);
        hashValues.put("###HOUR_PER_DAY", hourPerDayRtn);
        hashValues.put("###NUMBER_OF_TRUCK", numberOfTruckRtn);
        hashValues.put("###DESCRIPTION", mf.subString(mf.replaceDotCommaCharReverse(jobDescription), 0, mf.maxLengh));
        hashValues.put("###INSTRUCTION", mf.subString(mf.replaceDotCommaCharReverse(jobInstruction), 0, mf.maxLengh));
        hashValues.put("###EMAIL1", truckOwnerEmail);
        hashValues.put("###EMAIL2", truckOwnerEmail);
        hashValues.put("###PRIMARY_PHONE", telephone);
        hashValues.put("###CELLPHONE", cellPhone);
        hashValues.put("###DOT_NUMBER", dotNumber);
        hashValues.put("###GENERAL_LIABILITY", generalLiability);
        hashValues.put("###TRUCK_LIABILITY", truckLiability);
        hashValues.put("###PROOF_LIABILITY", proofOfTruckLiability);
        hashValues.put("###LENGTH_OF_BED", lenghtOfBed);
        hashValues.put("###TRUCK_TYPE", truckAxle);

        CustomMail ctMail = new CustomMail(hashValues, FileMessageContent.MAIL_JobCancelExcavatorMessageContent);
        ctMail.buildMail();
        return ctMail;

    }

    @Override
    public void run() {
        sendTruckOwnerJobCancelMail();
    }

}
