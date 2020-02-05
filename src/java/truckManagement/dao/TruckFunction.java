/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package truckManagement.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author erman
 */
public class TruckFunction {
    
    
    public Date parseDate(String date){
        Date convert = null;
        try {
            convert = new SimpleDateFormat("yy MMM d", new Locale("EN", "en")).parse(date);
        } catch (ParseException ex) {
            System.out.println("Unparsable date>>>>>>>>>>> "+ex.getMessage());
        }
             return convert;
    }
    
     
    public Date parseYearToDate(String stringYear){
        
        stringYear = stringYear.replace(" ", "");
        
        stringYear = "01-01-"+stringYear+" 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        Date year = null;
        try {
            year = sdf.parse(stringYear);
        } catch (ParseException ex) {
            Logger.getLogger(TruckFunction.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return year;
    }
    
    public int parseDateToYear(Date dateYear){
        
        Calendar c = Calendar.getInstance();
        c.setTime(dateYear);
        return c.get(Calendar.YEAR);
    }
    
    public int parseStringToInt(String value){
        int convert = 0;
        try {
            convert = Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            System.out.println("Unparsable Number>>>>>>>>>>> "+ex.getMessage());
        }
             return convert;
    }
    
    public String getAllDayOfDateRangeConcate(Date startDate, Date endDate, String solicitedTruckDate) {
        
        //System.out.println("getUnAvailabilityDate: startDate = " + startDate + " And endDate = " + endDate + "");
        //System.out.println("getUnAvailabilityDate: startDate.getTime() = " + startDate.getTime() + " And endDate.getTime() = " + endDate.getTime() + "");
        
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(startDate);
        c2.setTime(endDate);
        
        String unAvailabilityDate = solicitedTruckDate;
        
        do{
            
            if (!unAvailabilityDate.contains(""+c1.getTime())) {
                unAvailabilityDate += "#" + c1.getTime().getTime();
            }
            
            c1.add(Calendar.DATE, 1);
            
        }while(!c1.getTime().after(endDate) | c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR));
        
        //unAvailabilityDate = (unAvailabilityDate.equals("") ? unAvailabilityDate : unAvailabilityDate.substring(1));
        //System.out.println("getUnAvailabilityDate: unAvailabilityDate = " + unAvailabilityDate);
        
        return unAvailabilityDate;
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
    
    
}
