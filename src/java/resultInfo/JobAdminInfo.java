/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resultInfo;

import java.util.Date;
import mailing.mailSending.MailFunction;
import util.date.DateFunction;

/**
 *
 * @author erman
 */
public class JobAdminInfo {

    protected int jobID;
    protected Date startDate;
    protected Date endDate;
    protected int hourPerDay;
    protected int numberOfTruck;// Total number of Truck
    protected int numberOfRemainTruck;
    protected String companyRate;
    protected float jobRate;
    protected int lenghtOfBed;
    protected int truckAxle;
    protected String jobLocation;
    protected String jobNumber;
    protected boolean closeStatus;
    protected String typeOfDirtLabel;
    protected String weight;
    protected String startTime;
    protected String dotNumber;
    protected int generalLiability;
    protected int truckLiability;
    protected String proofOfTruckLiability;
    protected String directDeposit;
    protected String timeSheet;
    protected String automatedBooking;
    protected String jobDescription;
    protected String jobInstruction;
    protected String jobDocumentsID;
    protected String jobDocumentsPATH;
    protected String excavatorTelephone;
    protected String timezoneID;
    protected int jobCreationType;
    protected String excavatorName;
    protected String excavatorSurname;

    public JobAdminInfo(int jobID, Date startDate, Date endDate, int hourPerDay, int numberOfTruck, int numberOfRemainTruck, 
            String companyRate, float jobRate, int lenghtOfBed, int truckAxle, String jobLocation, String jobNumber, 
            boolean closeStatus, String typeOfDirtLabel, String weight, String startTime, String dotNumber, int generalLiability, 
            int truckLiability, String proofOfTruckLiability, String directDeposit, String timeSheet, String automatedBooking, 
            String jobDescription, String jobInstruction, String jobDocumentsID, String jobDocumentsPATH, String excavatorTelephone,
            String timezoneID, int jobCreationType, String excavatorName, String excavatorSurname) {
        this.jobID = jobID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hourPerDay = hourPerDay;
        this.numberOfTruck = numberOfTruck;
        this.numberOfRemainTruck = numberOfRemainTruck;
        this.companyRate = companyRate;
        this.jobRate = jobRate;
        this.lenghtOfBed = lenghtOfBed;
        this.truckAxle = truckAxle;
        this.jobLocation = jobLocation;
        this.jobNumber = jobNumber;
        this.closeStatus = closeStatus;
        this.typeOfDirtLabel = typeOfDirtLabel;
        this.weight = weight;
        this.startTime = startTime;
        this.dotNumber = dotNumber;
        this.generalLiability = generalLiability;
        this.truckLiability = truckLiability;
        this.proofOfTruckLiability = proofOfTruckLiability;
        this.directDeposit = directDeposit;
        this.timeSheet = timeSheet;
        this.automatedBooking = automatedBooking;
        this.jobDescription = jobDescription;
        this.jobInstruction = jobInstruction;
        this.jobDocumentsID = jobDocumentsID;
        this.jobDocumentsPATH = jobDocumentsPATH;
        this.excavatorTelephone = excavatorTelephone;
        this.timezoneID = timezoneID;
        this.jobCreationType = jobCreationType;
        this.excavatorName = excavatorName;
        this.excavatorSurname = excavatorSurname;
    }
    
    @Override
    public String toString() {
        return getStringObj();
    }
    
    public String getStringObj() {
        return "" + jobID 
                + ";" + startDate.getTime()
                + ";" + endDate.getTime() 
                + ";" + hourPerDay
                + ";" + numberOfTruck 
                + ";" + numberOfRemainTruck 
                + ";" + companyRate 
                + ";" + jobRate 
                + ";" + lenghtOfBed 
                + ";" + truckAxle 
                + ";" + jobLocation 
                + ";" + jobNumber 
                + ";" + closeStatus 
                + ";" + typeOfDirtLabel 
                + ";" + weight 
                + ";" + startTime 
                + ";" + dotNumber 
                + ";" + generalLiability 
                + ";" + truckLiability 
                + ";" + proofOfTruckLiability 
                + ";" + directDeposit 
                + ";" + timeSheet 
                + ";" + automatedBooking 
                + ";" + jobDescription 
                + ";" + jobInstruction 
                + ";" + jobDocumentsID 
                + ";" + jobDocumentsPATH 
                + ";" + excavatorTelephone 
                + ";" + timezoneID 
                + ";" + jobCreationType 
                + ";" + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(startDate, timezoneID) 
                + ";" + DateFunction.cvtGmtAndParseToLocationWithTimeZoneID(endDate, timezoneID)
                + ";" + MailFunction.getTotalName(excavatorName, excavatorSurname)
                + ";" + "null" 
                ;
    }

    public String getStringObj(String res) {
        return res + ";" + getStringObj();
    }

}
