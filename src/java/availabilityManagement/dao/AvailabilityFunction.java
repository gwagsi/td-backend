/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package availabilityManagement.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.timer.Timer;

/**
 *
 * @author erman
 */
public class AvailabilityFunction {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    public Date parseStringToDate(String date){
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
            Logger.getLogger(AvailabilityFunction.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return year;
    }
    
    public int parseDateToYear(Date dateYear){
        
        Calendar c = Calendar.getInstance();
        c.setTime(dateYear);
        return c.get(Calendar.YEAR);
    }
    
    
    public String parseDateToString(Date date){
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("y MMM d",  new Locale("EN", "en"));
        
        return dateFormat.format(date);
    }
    
    public Date addDay(Date date, int numberOfDay){
        return new Date(date.getTime() + numberOfDay*Timer.ONE_DAY);
    }

    public String getUnAvailabilityDate(String startDate, String endDate) {
        Date startDat = this.parseStringToDate(startDate);
        Date endDat = this.parseStringToDate(endDate);
        String unAvailabilityDate = "";
        int i = 0;
        
        
        System.out.println("Contruction of availability date ....");
        System.out.println(" startDate = " + startDate + " And endDate = " + endDate + "");
        
        
        do{
            unAvailabilityDate += "#"+this.parseDateToString(startDat);
            System.out.println("startDate"+ (++i) +" = "+startDat);
            startDat = this.addDay(startDat, 1);
            
        }while(!startDat.after(endDat));
        
        unAvailabilityDate = unAvailabilityDate.substring(1, unAvailabilityDate.length());
        System.out.println("unAvailabilityDate = "+unAvailabilityDate);
        
        return unAvailabilityDate;
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
        System.out.println("getUnAvailabilityDate: unAvailabilityDate = " + unAvailabilityDate);
        
        return unAvailabilityDate;
    }
    
    
    public String getUnAvailabilityDateByBookingDate(String bookingDate, int year, String bookingDates) {
        
        System.out.println("getUnAvailabilityDateByBookingDate: bookingDate = " + bookingDate + " year = " + year + "  bookingDates = " + bookingDates + " ...");
        
        Date startDat;
        String unAvailabilityDate = "" + bookingDates;
        String[] bookingDateList = bookingDate.split("#");
        
        Calendar c = Calendar.getInstance();
        
        int i = 0;
        for (String startDate : bookingDateList) {
            
            //System.out.println(" startDate"+ (++i) +" = "+ startDate );
            
            startDat = this.parseStringToDate(startDate);
            c.setTime(startDat);
            if (year == c.get(Calendar.YEAR) && !unAvailabilityDate.contains(startDate)) {
                
                unAvailabilityDate += "#" + startDate;
                
            } 

        }
        //unAvailabilityDate = unAvailabilityDate.substring(1, unAvailabilityDate.length());
        //unAvailabilityDate = (unAvailabilityDate.equals("") ? unAvailabilityDate : unAvailabilityDate.substring(1, unAvailabilityDate.length()));
        
        System.out.println("getUnAvailabilityDateByBookingDate: unAvailabilityDate = "+unAvailabilityDate);
        
        return unAvailabilityDate;
    }
    
    
    
}
