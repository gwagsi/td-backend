/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package truckBookingManagement.dao;

import entities.Truck;
import entities.User;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.timer.Timer;

/**
 *
 * @author erman
 */
public class TruckBookingFunction {
    
    private String dateFormat = "y MMM d";

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }
    
    
    public Date parseDate(String date){
         Date convert = null;
    try {
        convert = new SimpleDateFormat("y MMM d", new Locale("EN", "en")).parse(date);
    } catch (ParseException ex) {
        System.out.println("Unparsable date>>>>>>>>>>> "+ex.getMessage());
    }
         return convert;
    }
    
    
    public String parseDateToString2(Date date){
        
        SimpleDateFormat datFormat = new SimpleDateFormat("MMM d, y",  new Locale("EN", "en"));
        
        return datFormat.format(date);
    }
    
    public Date parseYearToDate(String stringYear){
        
        stringYear = stringYear.replace(" ", "");
        
        stringYear = "01-01-"+stringYear+" 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        Date year = null;
        try {
            year = sdf.parse(stringYear);
        } catch (ParseException ex) {
            Logger.getLogger(TruckBookingFunction.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return year;
    }
    
    
    public String parseDateToString(Date date){
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("y MMM d",  new Locale("EN", "en"));
        
        return dateFormat.format(date);
    }
    
    public Date addDay(Date date, int numberOfDay){
        return new Date(date.getTime() + numberOfDay*Timer.ONE_DAY);
    }

    
    public Date parseStringToDate(String date){
        Date convert = null;
        try {
            convert = new SimpleDateFormat(this.dateFormat, new Locale("EN", "en")).parse(date);
        } catch (ParseException ex) {
            System.out.println("Unparsable date>>>>>>>>>>> "+ex.getMessage());
        }
             return convert;
    }
    
    
    public List<String> getUnAvailabilityDate(String startDate, String endDate) {
        Date startDat = this.parseStringToDate(startDate);
        Date endDat = this.parseStringToDate(endDate);
        int year;
        
        List<String> unAvailabilityDateList = new ArrayList<>();
        
        String unAvailabilityDate;
        int i = 0;
        
        System.out.println("Contruction of a range of date between ....");
        System.out.println(" startDate = " + startDate + " And endDate = " + endDate + "...");
        
        Calendar c = Calendar.getInstance();
        c.setTime(startDat);
        year = c.get(Calendar.YEAR);
        unAvailabilityDate = ""+year;
        
        do{
            unAvailabilityDate += "#"+this.parseDateToString(startDat);
            System.out.println("startDate"+ (++i) +" = "+startDat);
            startDat = this.addDay(startDat, 1);
            c.setTime(startDat);
            if(year != c.get(Calendar.YEAR)){
                System.out.println("Année passée = "+ year +"  Nouvelle année =   "+ c.get(Calendar.YEAR) +"...");
                unAvailabilityDateList.add(unAvailabilityDate);
                year = c.get(Calendar.YEAR);
                unAvailabilityDate = ""+year;
            }
            
        }while(!startDat.after(endDat));
        
        //unAvailabilityDate = unAvailabilityDate.substring(1, unAvailabilityDate.length());
        unAvailabilityDateList.add(unAvailabilityDate);
        System.out.println("unAvailabilityDateList = "+unAvailabilityDateList);
        
        return unAvailabilityDateList;
    }
    
    
    public List<String> getUnAvailabilityDateByBookingDate(String bookingDate) {
        
        Date startDat;
        String[] bookingDateList = bookingDate.split("#");
        int year;
        
        
        List<String> unAvailabilityDateList = new ArrayList<>();
        String unAvailabilityDate;
        
        
        
        startDat = this.parseDate(bookingDateList[0]);
        System.out.println("Contruction of a range of date between ....");
        System.out.println(" bookingDate = " + bookingDate +"...");
        
        Calendar c = Calendar.getInstance();
        c.setTime(startDat);
        year = c.get(Calendar.YEAR);
        unAvailabilityDate = ""+year;
        int i = 0;
        for (String startDate : bookingDateList) {
            
            //System.out.println(" startDate"+ (++i) +" = "+ startDate );
            
            startDat = this.parseDate(startDate);
            c.setTime(startDat);
            if (year == c.get(Calendar.YEAR)) {
                unAvailabilityDate += "#" + this.parseDateToString(startDat);
            } else {
                System.out.println("Année passée = " + year + "  Nouvelle année =   " + c.get(Calendar.YEAR) + "...");
                unAvailabilityDateList.add(unAvailabilityDate);
                year = c.get(Calendar.YEAR);
                unAvailabilityDate = "" + year +"#" + this.parseDateToString(startDat);
            }

        }
        //unAvailabilityDate = unAvailabilityDate.substring(1, unAvailabilityDate.length());
        unAvailabilityDateList.add(unAvailabilityDate);
        System.out.println("unAvailabilityDateList = "+unAvailabilityDateList);
        
        return unAvailabilityDateList;
    }

    
    /**
     * Retourne la moyenne des rates du propriétaire du jobs
     * Cette fonction calcule le rating d'un excavator
     * On calcule a cet effet pour les jobs respectant les conditions suivante:
        <li>Il faudra que le job ne soit pas encore supprimer</li>
        <li>il faudra que le rate du job soit different de 0 ce qui signifiera que a ete note ce job.</li>
     * @param truckOwner
     * @return 
    */
    public float evaluateTruckOwnerRate(User truckOwner) {
        
        float rating = 0;
        int numberOfTruck = 0;
        List<Truck> truckList = truckOwner.getTruckList();
        
        System.out.println("evaluateTruckOwnerRate: Debut de calcul du rating de l'excavator ...");
        
        if(truckList != null && !truckList.isEmpty()){
            
            for(Truck truck: truckList){
                if (!truck.getDeleted() && truck.getRate() != 0) {
                    
                    rating += truck.getRate();
                    numberOfTruck++;
                    
                }
            }
            
        }
         
        //System.out.println("evaluateTruckOwnerRate: Rating du truckOwner " +truckOwner+ " = " + (numberOfTruck == 0? 0 :rating/numberOfTruck));
        //System.out.println("evaluateTruckOwnerRate: Fin de calcul du rating du truckOwner ...");
        
        return  (numberOfTruck == 0? 0 :rating/numberOfTruck) ;
    }

    

    public String getUnAvailabilityDate(Date startDate, Date endDate) {
        
        System.out.println("getUnAvailabilityDate: startDate = " + startDate + " And endDate = " + endDate + "");
        
        String unAvailabilityDate = "";
        
        do{
            
            unAvailabilityDate += "#" + startDate.getTime();
            
            startDate = addDay(startDate, 1);
            
        }while(!startDate.after(endDate));
        
        unAvailabilityDate = (unAvailabilityDate.equals("") ? unAvailabilityDate : unAvailabilityDate.substring(1));
        System.out.println("getUnAvailabilityDate: unAvailabilityDate = " + unAvailabilityDate);
        
        return unAvailabilityDate;
    }
    
    
    public String getUnAvailabilityDateByBookingDate(String bookingDate, Date currentDate, String bookingDates) {
        
        System.out.println("getUnAvailabilityDateByBookingDate: bookingDate = " + bookingDate + "   currentDate = " + currentDate + "  bookingDates = " + bookingDates + " ...");
        
        Date startDat;
        String unAvailabilityDate = "" + bookingDates;
        String[] bookingDateList = bookingDate.split("#");
        
        //Calendar c = Calendar.getInstance();
        
        int i = 0;
        for (String startDate : bookingDateList) {
            
            //System.out.println(" startDate"+ (++i) +" = "+ startDate );
            
            startDat = this.parseStringToDate(startDate);
            //c.setTime(startDat);
            if (!currentDate.after(startDat) && !unAvailabilityDate.contains(startDate)) {
                
                unAvailabilityDate += "#" + startDate;
                
            } 

        }
        //unAvailabilityDate = unAvailabilityDate.substring(1, unAvailabilityDate.length());
        //unAvailabilityDate = (unAvailabilityDate.equals("") ? unAvailabilityDate : unAvailabilityDate.substring(1, unAvailabilityDate.length()));
        
        System.out.println("getUnAvailabilityDateByBookingDate: unAvailabilityDate = "+unAvailabilityDate);
        
        return unAvailabilityDate;
    }

    
    public String getTotalName(String name, String firstName){
        String[] surname = firstName.split("#");
        String  totalName;
        if(surname.length <= 1){
            totalName = name+" "+surname[0];
        } 
       else{
            totalName = name+" "+surname[0]+" "+surname[1];
        }
        return totalName;
    }


    
}
