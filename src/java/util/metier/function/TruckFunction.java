/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.metier.function;

import entities.Truck;
import entities.TruckDocument;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author erman
 */
public class TruckFunction {

    public static String[] getDocumentForTruck(Truck truck) {

        final int PIC_PATH = 0;
        final int PIC_PATH_ID = 1;
        final int INS_PATH = 2;
        final int INS_PATH_ID = 3;

        String[] myTruckDocs = {"", "", "", ""};

        List<TruckDocument> tdList = truck.getTruckDocumentList();

        for (TruckDocument truckDocument : tdList) {

            if (truckDocument.getType().equals(TruckDocumentType.PICTURE) && !truckDocument.getDocumentID().getDeleted()) {
                myTruckDocs[PIC_PATH] += "#" + truckDocument.getDocumentID().getPathName();
                myTruckDocs[PIC_PATH_ID] += "#" + truckDocument.getDocumentID().getDocumentID();
            }

            if (truckDocument.getType().equals(TruckDocumentType.INSURANCE) && !truckDocument.getDocumentID().getDeleted()) {
                myTruckDocs[INS_PATH] += "#" + truckDocument.getDocumentID().getPathName();
                myTruckDocs[INS_PATH_ID] += "#" + truckDocument.getDocumentID().getDocumentID();
            }

        }
        
        // Alignment: Suppression du premier caractere
        myTruckDocs[PIC_PATH] = alignment(myTruckDocs[PIC_PATH]);
        myTruckDocs[PIC_PATH_ID] = alignment(myTruckDocs[PIC_PATH_ID]);
        myTruckDocs[INS_PATH] = alignment(myTruckDocs[INS_PATH]);
        myTruckDocs[INS_PATH_ID] = alignment(myTruckDocs[INS_PATH_ID]);
        
        return myTruckDocs;
    }

    private static String alignment(String s) {
        return (s == null || s.equals("") ? "" : s.substring(1));
    }
    
    
    public String replaceDotCommaCharReverse(String string){
		
		String content = string.replace("$#$#$", ";");
		content = content.replace("&lt;", "<");
		content = content.replace("&gt;", ">");
		content = content.replace("&amp;", "&");
		content = content.replace("&quot;", "\"");
		content = content.replace("&apos;", "\'");

		return content;
		
		//return string.replace("$#$#$", ";");
	}
    
    
    public static Date[] getDateForSearchTruck(long lStartDate, long lEndDate){
        
        Calendar c = Calendar.getInstance();
        
        Date startDate = (lStartDate == 0 ? new Date() : new Date(lStartDate));
        c.setTime(startDate);
        c.add(Calendar.YEAR, 100);
        
        Date endDate = (lStartDate == 0 ? c.getTime() : new Date(lEndDate));
        
        return new Date[]{startDate, endDate};
    }
    
    
    public static void main(String[] args){
        Date[] dates = getDateForSearchTruck(0, 0);
        System.out.println("startDate: " + dates[0]);
        System.out.println("endDate: " + dates[1]);
    }
    

}
