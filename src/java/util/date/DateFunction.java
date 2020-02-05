/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * @author erman
 */
public class DateFunction {

    static final String dateFormatMMMDY = "MMM d, y";
    static final String dateFormatYMMMD = "y MMM d";
    
    public static Date getGMTDate(){
        return cvtToGmt(new Date());
    }
    
    /**
     *
     * Cette fonction prend en paramètre une date et retourne date GMT correspondante toute en 
     * tenant compte de l'heure de l'été.
     * 
     * @param date Date à convertir.
     * @return retourne la date GMT correspondante.
     * 
     */
    public static Date cvtToGmt(Date date) {
        TimeZone tz = TimeZone.getDefault();
        Date ret = new Date(date.getTime() - tz.getRawOffset());

        // if we are now in DST, back off by the delta.  Note that we are checking the GMT date, this is the KEY.
        if (tz.inDaylightTime(ret)) {
            Date dstDate = new Date(ret.getTime() - tz.getDSTSavings());

        // check to make sure we have not crossed back into standard time
            // this happens when we are on the cusp of DST (7pm the day before the change for PDT)
            if (tz.inDaylightTime(dstDate)) {
                ret = dstDate;
            }
        }
        return ret;
    }
    
    /**
     *
     * Cette fonction permet de convertir une date GMT dans un fuseau horaire dont on connais
     * le TimeZoneID
     * 
     * @param date Date GMT à convertir.
     * @param timeZoneID l'identifiant du TimeZone a utilisé
     * @return retourne la date correspondante.
     * 
     */
    public static Date cvtGmtToLocationWithTimeZoneID(Date date, String timeZoneID) {
        TimeZone tz = TimeZone.getTimeZone((timeZoneID == null ? "" : timeZoneID));
        
        Date ret = new Date(date.getTime() + tz.getRawOffset());

        // if we are now in DST, back off by the delta.  Note that we are checking the GMT date, this is the KEY.
        if (tz.inDaylightTime(ret)) {
            Date dstDate = new Date(ret.getTime() + tz.getDSTSavings());

        // check to make sure we have not crossed back into standard time
            // this happens when we are on the cusp of DST (7pm the day before the change for PDT)
            if (tz.inDaylightTime(dstDate)) {
                ret = dstDate;
            }
        }
        return ret;
    }
    
    
    /**
     *
     * Cette fonction permet de convertir une date GMT dans un fuseau horaire dont on connais
     * le TimeZoneID
     * 
     * @param date Date GMT à convertir.
     * @param timeZoneID l'identifiant du TimeZone a utilisé
     * @return retourne la date correspondante.
     * 
     */
    public static String cvtGmtAndParseToLocationWithTimeZoneID(Date date, String timeZoneID) {
        
        return formatDateToStringYMMMD(cvtGmtToLocationWithTimeZoneID(date, timeZoneID));
    }
    

    /**
     * Permet de convertir une date de type long contenu dans une chaine de caractère en date
     *
     * @param date date à convertir
     * @return retourne la date dejà convertir
     */
    public static Date parseSLongToDate(String date){
        
        System.out.println("DateFunction: parseSLongToDate:  date = " + date);
        Date convert = null;
        convert = new Date(Long.parseLong(date));
        return convert;
    }

    /**
     * Permet de formatter une date sous le format MMM D, Y
     *
     * @param date
     * @return
     */
    public static String formatDateToStringMMMDY(Date date){
        
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatMMMDY,  new Locale("EN", "en"));
        
        return dateFormat.format(date);
    }
    

    /**
     * Permet de formatter une date sous le format Y MMM d
     *
     * @param date
     * @return
     */
    public static String formatDateToStringYMMMD(Date date){
        
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatYMMMD,  new Locale("EN", "en"));
        
        return dateFormat.format(date);
    }
    
    
    public static Date parseStringToDate(String date){
        
        System.out.println("DateFunction: parseStringToDate:  date = " + date);
        Date convert = null;
        try {
            convert = new SimpleDateFormat("y MMM d", new Locale("EN", "en")).parse(date);
        } catch (ParseException ex) {
            System.out.println("Unparsable date>>>>>>>>>>> "+ex.getMessage());
        }
             return convert;
    }
    
    
    /**
     * Cette fonction prend en paramètre une date et retourne le premier et le dernier jour de la
     * semaine a laquelle celle-ci appartient.Ici on considère que le premier jour de la semaine est
     * un dimanche et dernier un samedi
     *

     * @param date date dont l'on souhaite determiner le premier et dernier jour de la semaine à laquelle elle apartient.
     * @return retourne un tableau de deux elements où la première case du tableau contient le premièr
     *          jour de la semaine et le deuxieme case contient le dernier jour de la semaine.
     */
    public static Date[] getDateRangeOfWeek(Date date){
        
        System.out.println("getDateRangeOfWeek: date = "+date);
        Date[] dates = new Date[2];
        
        GregorianCalendar dt = new GregorianCalendar();
        GregorianCalendar dt2 = new GregorianCalendar();
        
        dt.setTime(date);
        dt2.setTime(date);
        
        switch(dt.get(Calendar.DAY_OF_WEEK)){
            case Calendar.MONDAY: 
                dt.add(Calendar.DATE, -1);
                dt2.add(Calendar.DATE, 5);
                break;
            case Calendar.TUESDAY:
                dt.add(Calendar.DATE, -2);
                dt2.add(Calendar.DATE, 4);
                break;
            case Calendar.WEDNESDAY:
                dt.add(Calendar.DATE, -3);
                dt2.add(Calendar.DATE, 3);
                break;
            case Calendar.THURSDAY:
                dt.add(Calendar.DATE, -4);
                dt2.add(Calendar.DATE, 2);
                break;
            case Calendar.FRIDAY:
                dt.add(Calendar.DATE, -5);
                dt2.add(Calendar.DATE, 1);
                break;
            case Calendar.SATURDAY: 
                dt.add(Calendar.DATE, -6);
                dt2.add(Calendar.DATE, 0);
                break;
            case Calendar.SUNDAY:
                dt.add(Calendar.DATE, 0);
                dt2.add(Calendar.DATE, 6);
                break;
        }
        

        dates[0] = dt.getTime();
        dates[1] = dt2.getTime();
        System.out.println("getDateRangeOfWeek: dates = "+ (Arrays.asList(dates)));
        
        return dates;
    }
    
    public static Date[] getDateRangeOfWeekLarge(Date date){
        Date[] dates = getDateRangeOfWeek(date);
        dates[0] = getDateMidnight(dates[0]);
        dates[1] = getLastDateOfDay(dates[1]);
        return dates;
    }
    
    /**
     * Cette fonction retourne le premier et le dernier jour de la
     * semaine courante.Ici on considère que le premier jour de la semaine est
     * un dimanche et dernier un samedi
     * 
     * @return retourne un tableau de deux elements où la première case du tableau contient le premièr
     *          jour de la semaine et le deuxieme case contient le dernier jour de la semaine.
     */
    public static Date[] getDateRangeOfWeek(){
        return getDateRangeOfWeek(getGMTDate());
    }
    
    public static Date[] getDateRangeOfWeekLarge(){
        return getDateRangeOfWeekLarge(getGMTDate());
    }

    /**
     * Cette fonction prend en paramètre une date et retourne le premier et le dernier jour de l'année
     * à laquelle celle-ci appartient.
     *
     * @param date date dont l'on souhaite determiner le premier et le dernier jour de l'année à laquelle elle apartient.
     * @return retourne un tableau de deux elements dont le premièr element du tableau contient le premièr
     *          jour de l'année et le deuxieme element contient le dernier jour de l'année.
     */
    public static Date[] getDateRangeOfYear(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        
        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMinimum(Calendar.DAY_OF_YEAR));
        
        Date firstDateOfYear = getDateMidnight(calendar.getTime());
        
        
        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
        
        Date endDateOfYear = getLastDateOfDay(calendar.getTime());
        
        return (new Date[]{firstDateOfYear, endDateOfYear});

    }
    
    /**
     * Cette fonction retourne le premier et le dernier jour de l'année courante.
     * 
     * @return retourne un tableau de deux elements où la première case du tableau contient le premièr
     *          jour de la semaine et le deuxieme case contient le dernier jour de la semaine.
     */
    public static Date[] getDateRangeOfYear(){
        return getDateRangeOfYear(getGMTDate());
    }
    
    /**
     * Cette fonction determine une de l'année passé en paramètre.
     * 
     * @param year c'est la donc on aimerai avoir une date lui appartenant
     * @return retourne la date correspondante a l'année qui lui ai passé en paramètre.
     */
    public static Date getADateForYear(int year){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        return c.getTime();
    }
    
    /**
     * Cette fonction permet de determiner une date minuit associé a celle passé en paramètre.
     * 
     * @param date 
     * @return retourne une date.
     */
    public static Date getGMTDateMidnight(Date date){
        return getDateMidnight(cvtToGmt(date));
    }
    
    
    public static Date getGMTDateMidnight(){
        return getGMTDateMidnight(new Date());
    }
    
    public static Date getGMTLastDateOfDay(Date date){
        return new Date(getGMTDateMidnight(date).getTime() + 24 * 60 * 60 * 1000 - 1);
    }
    
    public static Date getGMTLastDateOfDay(){
        return getGMTLastDateOfDay(new Date());
    }
    
    public static Date getDateMidnight(Date date){
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return calendar.getTime();
    }
    
    public static Date getLastDateOfDay(Date date){
        return new Date(getDateMidnight(date).getTime() + 24 * 60 * 60 * 1000 - 1);
    }
    
    
    public static int getSessionTimeOut() {
        //Si l'utilisateur demande à se connecté avant qu'on met à jour les informations de deployment
        Calendar cal = Calendar.getInstance();
        long sessionTimeOut = (int) cal.getTimeInMillis();
        System.out.println("Time: " + cal.getTimeInMillis());
        cal.add(Calendar.DAY_OF_MONTH, 3);
        sessionTimeOut = cal.getTimeInMillis() - sessionTimeOut;
        return (int) sessionTimeOut;
    }

}
