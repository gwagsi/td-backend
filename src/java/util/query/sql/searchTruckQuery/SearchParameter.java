/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util.query.sql.searchTruckQuery;

/**
 *
 * @author erman
 */
public class SearchParameter {
    
    
    public static String getSorterParam(int sortParam, int ORDER) {
        switch(sortParam){
            case 0:
                return "";
            case 1:
                return "tt.`rate` "+ (ORDER == 0 ? "ASC" : "DESC") + " ,"; 
            case 2:
                return "tt.`truck_axle_ID` "+ (ORDER == 0 ? "ASC" : "DESC") + " ,"; 
            case 3:
                return "tt.`lenght_of_bed_ID` "+ (ORDER == 0 ? "ASC" : "DESC") + " ,"; 
            default:
                return ""; 
        }
    }
    
    
}
