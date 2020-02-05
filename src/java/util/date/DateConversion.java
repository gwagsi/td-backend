/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util.date;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author erman
 */
public class DateConversion {
    
    /**
     * Permet de convertir une chaine de caractère String en Time(java.sql.Time) sur
     * le format HH:mm:ss avec ss egal à 00.
     *
     * @param sTime temps sous forme de chaine de caractère sous le HH:mm:ss ou HH:mm
     * @return retourne temps converti sous en typage Time.
     * @throws java.text.ParseException
     */
    public static Time convertStringToTime(String sTime) throws ParseException {

        String s = sTime;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        long ms = 0;

        ms = sdf.parse(s).getTime();

        Time time = new Time(ms);
        return time;

    }

    /**
     * Permet d'extraire le temps(java.sql.Time) dans une date donnée
     *
     * @param jdate correspond à la date dont on veut extraire le temps qu'il fait.
     * @return retourne un java.sql.Time.
     */
    public static Time getTimeOfDate(Date jdate) {
        return new Time(jdate.getTime());
    }

    /**
     * Permet d'extraire le temps(Format EN, Ex: 05:00 AM) dans une date données.
     *
     * @param dateToFormat la date a formatter.
     * @return retourne une chaine de caractère contenant l'heure au format anglais
     */
    public static String formatDateToTimeAMPM(Date dateToFormat) {

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", new Locale("EN", "en"));
        
        return sdf.format(dateToFormat);
    }
    
}
