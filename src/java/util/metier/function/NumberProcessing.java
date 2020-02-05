/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util.metier.function;

import java.math.BigDecimal;

/**
 *
 * @author erman
 */
public class NumberProcessing {
     
    /**
     * Round to certain number of decimals
     * 
     * @param bd
     * @param decimalPlace
     * @return
     */
    public static float round(BigDecimal bd, int decimalPlace) {
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_UP);
        return bd.floatValue();
    }
    
    /**
     * Round to certain number of decimals
     * 
     * @param d
     * @param decimalPlace
     * @return
     */
    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        return round(bd, decimalPlace);
    }
    
    /**
     * Round to certain number of decimals
     * 
     * @param d
     * @param decimalPlace
     * @return
     */
    public static float round(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        return round(bd, decimalPlace);
    }
    
     /**
     * Round to certain number of decimals
     * 
     * @param fString
     * @param decimalPlace
     * @return
     */
    public static float round(String fString, int decimalPlace) {
        BigDecimal bd = new BigDecimal(fString);
        return round(bd, decimalPlace);
    }
}
