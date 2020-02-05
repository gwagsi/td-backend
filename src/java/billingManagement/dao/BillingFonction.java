/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package billingManagement.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import util.date.DateFunction;

/**
 *
 * @author erman
 */
public class BillingFonction {
    
    public Date parseStringToDate(String date){
        
        System.out.println("BillingFonction: parseStringToDate:  date = " + date);
        Date convert = null;
        try {
            convert = new SimpleDateFormat("y MMM d", new Locale("EN", "en")).parse(date);
        } catch (ParseException ex) {
            System.out.println("Unparsable date>>>>>>>>>>> "+ex.getMessage());
        }
             return convert;
    }
    
    
    public String parseDateToString(Date date){
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("y MMM d",  new Locale("EN", "en"));
        
        return dateFormat.format(date);
    }
    
    
    public String parseDateToString(){
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("y MMM d",  new Locale("EN", "en"));
        
        return dateFormat.format(DateFunction.getGMTDate());
    }
    
    
    public String parseDateToString2(Date date){
        
        SimpleDateFormat datFormat = new SimpleDateFormat("MMM d, y",  new Locale("EN", "en"));
        
        return datFormat.format(date);
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
