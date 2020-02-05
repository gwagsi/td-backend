/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resultInfo;

import java.util.Date;
import util.date.DateConversion;

/**
 *
 * @author erman
 */
public class JobOfJobResponseInfo {

    protected String joblogCreateOwnerName;
    protected String joblogEditOwnerName;
    protected String joblogCreateOwnerSurname;
    protected String joblogEditOwnerSurname;
    protected int joblogID;
    protected String dateLog;
    protected String noteOnDay;
    protected int numberOfLoad;
    protected String timeLeft;
    protected String timeOnSite;
    protected Date creationDate;
    protected boolean submitted;
    protected boolean closed;
    protected Date startTime;
    protected Date endTime;
    protected String typeOfDirt;
    protected String fromWhere;
    protected String toWhere;
    protected int truckID;
    protected String truckNumber;
    protected boolean dailyTicketState;
    protected Date editionDate;

    public JobOfJobResponseInfo(String joblogCreateOwnerName, String joblogEditOwnerName, String joblogCreateOwnerSurname, String joblogEditOwnerSurname, int joblogID, String dateLog, String noteOnDay, int numberOfLoad, String timeLeft, String timeOnSite, Date creationDate, boolean submitted, boolean closed, Date startTime, Date endTime, String typeOfDirt, String fromWhere, String toWhere, int truckID, String truckNumber, boolean dailyTicketState, Date editionDate) {
        this.joblogCreateOwnerName = joblogCreateOwnerName;
        this.joblogCreateOwnerSurname = joblogCreateOwnerSurname;
        this.joblogEditOwnerName = joblogEditOwnerName;
        this.joblogEditOwnerSurname = joblogEditOwnerSurname;
        this.joblogID = joblogID;
        this.dateLog = dateLog;
        this.noteOnDay = noteOnDay;
        this.numberOfLoad = numberOfLoad;
        this.timeLeft = timeLeft;
        this.timeOnSite = timeOnSite;
        this.creationDate = creationDate;
        this.submitted = submitted;
        this.closed = closed;
        this.startTime = startTime;
        this.endTime = endTime;
        this.typeOfDirt = typeOfDirt;
        this.fromWhere = fromWhere;
        this.toWhere = toWhere;
        this.truckID = truckID;
        this.truckNumber = truckNumber;
        this.dailyTicketState = dailyTicketState;
        this.editionDate = editionDate;
    }
    
    
    
    public String getStringObj() {
        return "" + joblogID + "##"//0
                + dateLog + "##"
                + noteOnDay + "##"
                + numberOfLoad + "##"
                + timeLeft + "##"
                + timeOnSite + "##"//5
                + creationDate.getTime() + "##"
                + submitted + "##"
                + closed + "##"
                + DateConversion.getTimeOfDate(startTime) + "##"
                + DateConversion.getTimeOfDate(endTime) + "##"//10
                + typeOfDirt + "##"
                + fromWhere + "##"
                + toWhere + "##"
                + (truckID == 0 ? "" : truckID) + "##"
                + (truckNumber == null ? "" : truckNumber) + "##"//15
                + dailyTicketState + "##"//16
                + joblogCreateOwnerName + " " + joblogCreateOwnerSurname + "##"//17
                + joblogEditOwnerName + " " + joblogEditOwnerSurname + "##"//18
                + editionDate.getTime() + "##"//19
                + "null";
    }

    public String getStringObj(String res) {
        return res + ";" + getStringObj();
    }

}
