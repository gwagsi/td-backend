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
public class SearchAvailableTruck {
    
    
    public static String getQueryForSearchAllAvailableTrucks(int sortParam, boolean excludeWeekend, int dotNumber, int ORDER){
        
        return "SELECT tt.*, getDistance(tt.`latitude`,tt.`longitude`, ?4, ?5) as distanceWithin FROM ("
                + "		SELECT t.* FROM `truck` t  "
                + "		LEFT JOIN (			"
                + "			SELECT DISTINCT `truck_ID` "
                + "			FROM `solicited_truck` st "
                + "			WHERE ((`start_date` <= ?1 AND `end_date` >= ?2) "
                + "				OR DATEDIFF(?1, ?2) = ("
                + "					SELECT	SUM( DATEDIFF(GREATEST(`start_date`, ?1),  LEAST(`end_date`, ?2) ) ) NBJours  "
                + "					FROM `solicited_truck` s "
                + "					WHERE DATEDIFF(GREATEST(`start_date`, ?1), LEAST(`end_date`, ?2))  > 0 "
                + "					AND s.`truck_ID` = st.`truck_ID` )"
                + "				)"
                + "				AND `truck_available` = TRUE  AND `deleted` = FALSE "
                + "		) s  "
                + "			ON t.`truck_ID` = s.`truck_ID`  "
                + "		WHERE  s.`truck_ID` is null "
                + "			AND t.`deleted` = FALSE "
                + "	) tt "
                + "	LEFT JOIN ("
                + "		SELECT DISTINCT `truck_ID`  "
                + "		FROM `availability` av "
                + "		WHERE DATEDIFF(?2, ?1) <= ("
                + "			SELECT COUNT( DISTINCT TO_DAYS( `availbility_date` )) "
                + "			FROM `availability` a "
                + "			WHERE `availbility_date` BETWEEN ?1 AND ?2  "
                + "                         " + (excludeWeekend ? " AND DAYOFWEEK(`availbility_date`) != 1 AND DAYOFWEEK(`availbility_date`) != 7" : "") + ""
                + "				AND `state` = 1 "
                + "				AND `deleted` = FALSE AND av.`truck_ID` = a.`truck_ID` "
                + "		)"
                + "	) a "
                + "		ON tt.`truck_ID` = a.`truck_ID` "
                + "		"
                + "	WHERE  a.`truck_ID` is null "
                + "	   " + (dotNumber == 0 ? "" : "AND tt.`d_o_t_number` IS NOT NULL ") 
                + "		AND (tt.`latitude` >= ?6 AND tt.`latitude` <= ?7) "
                + "		AND (tt.`longitude` >= ?8 AND tt.`longitude` <= ?9)"
                + "		"
                + "	HAVING "
                + "         distanceWithin <= ( 1 + ?3)"
                + "	"
                + "	ORDER BY " + SearchParameter.getSorterParam(sortParam, ORDER) + " distanceWithin " + (ORDER == 0 ? "ASC" : "DESC") + ", tt.`creation_date` DESC"
                + ""
                ;
        
    }
    
    
    
    public static String getQueryForSearchTruckWithoutDistance(int sortParam, boolean excludeWeekend, int dotNumber, int ORDER){
        
        return "SELECT tt.*, tt.`distance` as distanceWithin FROM ( "
                + "		SELECT t.* FROM `truck` t  "
                + "		LEFT JOIN ("
                + "			SELECT DISTINCT `truck_ID` "
                + "			FROM `solicited_truck` st "
                + "			WHERE ((`start_date` <= ?1 AND `end_date` >= ?2) "
                + "				OR DATEDIFF(?1, ?2) = ("
                + "					SELECT	SUM( DATEDIFF(GREATEST(`start_date`, ?1),  LEAST(`end_date`, ?2) ) ) NBJours  "
                + "					FROM `solicited_truck` s "
                + "					WHERE DATEDIFF(GREATEST(`start_date`, ?1), LEAST(`end_date`, ?2))  > 0 "
                + "					AND s.`truck_ID` = st.`truck_ID` )"
                + "				)"
                + "				AND `truck_available` = TRUE  AND `deleted` = FALSE "
                + "		) s  "
                + "			ON t.`truck_ID` = s.`truck_ID`  "
                + "		WHERE  s.`truck_ID` is null "
                + "			AND t.`deleted` = FALSE  "
                + "	) tt "
                + "	LEFT JOIN ("
                + "		SELECT DISTINCT `truck_ID`  "
                + "		FROM `availability` av "
                + "		WHERE DATEDIFF(?2, ?1) <= ("
                + "			SELECT COUNT( DISTINCT TO_DAYS( `availbility_date` )) "
                + "			FROM `availability` a "
                + "			WHERE `availbility_date` BETWEEN ?1 AND ?2  "
                + "			   " + (excludeWeekend ? " AND DAYOFWEEK(`availbility_date`) != 1 AND DAYOFWEEK(`availbility_date`) != 7" : "") + ""
                + "				AND `state` = 1 "
                + "				AND `deleted` = FALSE AND av.`truck_ID` = a.`truck_ID` "
                + "		) "
                + "	) a  "
                + "		ON tt.`truck_ID` = a.`truck_ID` "
                + "		"
                + "	WHERE  a.`truck_ID` is null"
                + "		" + (dotNumber == 0 ? "" : "AND tt.`d_o_t_number` IS NOT NULL ") 
                + "	"
                + "	ORDER BY " + SearchParameter.getSorterParam(sortParam, ORDER)  + " distanceWithin " + (ORDER == 0 ? "ASC" : "DESC") + ", tt.`creation_date` DESC"
                + ""
                ;
        
    }
    
    
}
