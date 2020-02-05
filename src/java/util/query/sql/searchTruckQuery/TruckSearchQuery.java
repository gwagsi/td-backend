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
public class TruckSearchQuery {
    
    public static String getQueryForSearchAllAvailableTrucks2(int sortParam, boolean excludeWeekend, int dotNumber){
        
        return "SELECT tt.* FROM ("
                + "SELECT t.* FROM `truck` t "
                + "LEFT JOIN ("
                + "SELECT DISTINCT `truck_ID` "
                + "FROM `solicited_truck` st "
                + "WHERE ((`start_date` <= ?1 AND `end_date` >= ?2) OR DATEDIFF(?1, ?2) = (SELECT	"
                + "         SUM("
                + "             DATEDIFF("
                + "                GREATEST(`start_date`, ?1), "
                + "                LEAST(`end_date`, ?2)"
                + "             )"
                + "         ) NBJours "
                + " "
                + "FROM `solicited_truck` s "
                + ""
                + "WHERE DATEDIFF(GREATEST(`start_date`, ?1),LEAST(`end_date`, ?2))  > 0 AND s.`truck_ID` = st.`truck_ID` )) "
                + "AND `truck_available` = TRUE "
                + "AND `deleted` = FALSE "
                + ""
                + " ) s "
                + "ON t.`truck_ID` = s.`truck_ID` "
                + "WHERE  s.`truck_ID` is null "
                + "AND t.`distance` >= ?3 "
                + "AND t.`truck_zip_code` LIKE ?4 "
                + "AND t.`deleted` = FALSE "
                + ") tt "
                + "LEFT JOIN ("
                + "SELECT DISTINCT `truck_ID` "
                + "FROM `availability` av WHERE DATEDIFF(?6, ?5) <= (SELECT COUNT( DISTINCT TO_DAYS( `availbility_date` )) FROM `availability` a WHERE `availbility_date` BETWEEN ?5 AND ?6 "
                + "" + (excludeWeekend ? " AND DAYOFWEEK(`availbility_date`) != 1 AND DAYOFWEEK(`availbility_date`) != 7" : "") + ""
                + " AND `state` = 1"
                + " AND `deleted` = FALSE AND av.`truck_ID` = a.`truck_ID` )"
                + ") a "
                + "ON tt.`truck_ID` = a.`truck_ID` "
                + "WHERE  a.`truck_ID` is null "
                + (dotNumber == 0 ? "" : "AND tt.`d_o_t_number` IS NOT NULL ") 
                + "ORDER BY " + SearchParameter.getSorterParam(sortParam, 0) + " tt.`creation_date` "
                + ""
                ;
        
    }
    
    public static String getQueryForSearchAllAvailableTrucksWithFilter2(int sortParam, boolean excludeWeekend, int dotNumber){
        
        return "SELECT tt.* FROM ("
                + "SELECT t.* FROM `truck` t "
                + "LEFT JOIN ("
                + "SELECT DISTINCT `truck_ID` "
                + "FROM `solicited_truck` st "
                + "WHERE ((`start_date` <= ?1 AND `end_date` >= ?2) OR DATEDIFF(?1, ?2) = (SELECT	"
                + "         SUM("
                + "             DATEDIFF("
                + "                GREATEST(`start_date`, ?1), "
                + "                LEAST(`end_date`, ?2)"
                + "             )"
                + "         ) NBJours "
                + " "
                + "FROM `solicited_truck` s "
                + ""
                + "WHERE DATEDIFF(GREATEST(`start_date`, ?1),LEAST(`end_date`, ?2))  > 0 AND s.`truck_ID` = st.`truck_ID` )) "
                + "AND `truck_available` = TRUE "
                + "AND `deleted` = FALSE "
                + ""
                + " ) s "
                + "ON t.`truck_ID` = s.`truck_ID` "
                + "WHERE  s.`truck_ID` is null "
                + "AND t.`distance` >= ?3 "
                + "AND t.`truck_zip_code` LIKE ?4 "
                + "AND t.`deleted` = FALSE "
                + ") tt "
                + "LEFT JOIN ("
                + "SELECT DISTINCT `truck_ID` "
                + "FROM `availability` av WHERE DATEDIFF(?6, ?5) <= (SELECT COUNT( DISTINCT TO_DAYS( `availbility_date` )) FROM `availability` a WHERE `availbility_date` BETWEEN ?5 AND ?6 "
                + "" + (excludeWeekend ? " AND DAYOFWEEK(`availbility_date`) != 1 AND DAYOFWEEK(`availbility_date`) != 7" : "") + ""
                + " AND `state` = 1"
                + " AND `deleted` = FALSE AND av.`truck_ID` = a.`truck_ID` )"
                + ") a "
                + "ON tt.`truck_ID` = a.`truck_ID` "
                + "WHERE  a.`truck_ID` is null "
                + "AND tt.`truck_axle_ID` >= ?9 "
                + "AND tt.`general_liability` >= ?10 "
                + "AND tt.`insurance_liability` >= ?11 "
                + (dotNumber == 0 ? "" : "AND tt.`d_o_t_number` IS NOT NULL ") 
                + "ORDER BY " + SearchParameter.getSorterParam(sortParam, 0) + " tt.`creation_date` "
                + ""
                ;
        
    }
    
    public static String getQueryForSearchAllAvailableTrucksINDateRange(String truckListID){
        
        return "SELECT tt.* "
                + "FROM ("
                + "	SELECT t.*"
                + "	FROM `truck` t"
                + ""
                + "	LEFT JOIN ("
                + "		SELECT DISTINCT `truck_ID`"
                + "		FROM `solicited_truck` "
                + "		WHERE  (`start_date` <= ?2 AND `end_date` >= ?1 ) "
                + "				AND `truck_available` = TRUE "
                + "				AND `deleted` = FALSE"
                + "		) s "
                + ""
                + "	ON t.`truck_ID` = s.`truck_ID`"
                + ""
                + "	WHERE  s.`truck_ID` is null "
                + "		AND t.`deleted` = FALSE "
                + "		AND t.`truck_ID` IN " + truckListID
                + ") tt "
                + ""
                + "LEFT JOIN ( "
                + "		SELECT DISTINCT `truck_ID` "
                + "		FROM `availability` WHERE `availbility_date` "
                + "		BETWEEN ?1 AND ?2 "
                + "				AND `state` = 1 "
                + "				AND `deleted` = FALSE "
                + "      ) a "
                + ""
                + "ON tt.`truck_ID` = a.`truck_ID`"
                + ""
                + "WHERE  a.`truck_ID` is null AND tt.`truck_ID` IN " + truckListID
                ;
        
    }

    /**
     * Permet de rechercher tous les trucks sollicités d'un jobResponse qui ne sont plus disponible.
     * TruckOccup correspond aux Camions reservés et occupés pour les jobResponses autre que le  'j'"
     * s correspond aux camions reservé au compte du jobResponse 'j'
     * @return retourne une requête sql native sous forme de chaine de caractère
     */
    public static String getQueryForSearchTakenTrucksOfJobResponse(){
        
        return "SELECT s.*"
                + "	FROM `solicited_truck` s "
                + "	INNER JOIN `solicited_truck`  AS TruckOccup "
                + "	ON s.`truck_ID` = TruckOccup.`truck_ID`	"
                + "		AND !(s.`start_date` > TruckOccup.`end_date` OR s.`end_date` < TruckOccup.`start_date`)"
                + "		AND TruckOccup.`deleted` = FALSE"
                + "		AND TruckOccup.`truck_available` = 1"
                + "		AND TruckOccup.`job_response_ID` != ?1 "
                + ""
                + "	LEFT JOIN `availability` A"
                + "	ON s.`truck_ID` = A.`truck_ID`"
                + "		AND A.`availbility_date` BETWEEN s.`start_date` AND s.`end_date`"
                + "		AND A.`deleted` = FALSE"
                + ""
                + "	WHERE s.`job_response_ID` = ?1 "
                + "		AND s.`deleted` = FALSE"
                ;
        
    }
    
}
