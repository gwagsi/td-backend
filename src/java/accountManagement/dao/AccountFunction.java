/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package accountManagement.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author erman
 */
public class AccountFunction {
    
    public Calendar addMonth(Calendar c, double numberOfMonth){
        
        if(numberOfMonth == 0) return c;
        
        if(numberOfMonth > 0 && numberOfMonth < 1){
            Calendar cal = Calendar.getInstance();
            cal.setTime(c.getTime());
            cal.add(Calendar.MONTH, 1);
            long time = (long) (( cal.getTimeInMillis() - c.getTimeInMillis() )*numberOfMonth);
            Date d = c.getTime();
            d = new Date( d.getTime() + time );
            c.setTime(d);
            return c;
        }
        
        if(numberOfMonth >= 1){
            c.add(Calendar.MONTH, (int) numberOfMonth);
            return addMonth(c, numberOfMonth - (int) (numberOfMonth));
        }
        return c;
    }
    
    
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
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",  new Locale("EN", "en"));
        
        return dateFormat.format(date);
    }
    
    
}
