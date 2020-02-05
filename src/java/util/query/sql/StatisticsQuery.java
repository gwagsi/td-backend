/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.query.sql;

import entities.User;
import javax.persistence.Query;
import toolsAndTransversalFunctionnalities.GestionnaireEntite;
import util.date.DateFunction;

/**
 *
 * @author erman
 */
public class StatisticsQuery {

    public static Query getQueryForSearchExcavatorStatistics(GestionnaireEntite ges, User user) {

        return ges.getEntityManager().createNativeQuery(""
                + "SELECT"
                + ""
                // + "	-- Job qui attendent la confirmation de l'excavator"
                + "	(SELECT COUNT(*) "
                + ""
                + "	FROM `job` j "
                + ""
                + "	INNER JOIN `validation` v "
                + "		ON j.`job_ID` = v.`job_ID` "
                + ""
                + "	INNER JOIN `job_response` jr"
                + "		ON jr.`job_response_ID` = v.`job_response_ID`"
                + ""
                + ""
                + "	WHERE v.`truck_owner_validation` = 1 AND v.`deleted` = FALSE AND v.`client_validation` = 0"
                + "		AND j.`deleted` = FALSE AND j.`excavator_ID` = ?1"
                + "		AND jr.`deleted` = FALSE AND j.`end_date` >= ?2"
                + "     ) AS TotalOfNonConfirmJobByExcavator,"
                + ""
                + ""
                // + "-- Liste des tickets non validé par l'excvator"
                + "	(SELECT COUNT(*) "
                + ""
                + "	FROM `daily_ticket` d"
                + ""
                + "	INNER JOIN `job_response` jr"
                + "		ON d.`job_response_ID` = jr.`job_response_ID`"
                + ""
                + "	INNER JOIN `job` j"
                + "		ON j.`job_ID` = jr.`job_ID`"
                + ""
                + "	WHERE d.`view_ticket` = TRUE AND d.`deleted` = FALSE"
                + "		AND d.`state` = FALSE AND j.`excavator_ID` = ?1"
                + "	) AS TotalOfUnvalidateTicketForExcavator,"
                + "     "
                // + " -- Liste des tickets qui n'ont pas encore été payé"
                + "	(SELECT COUNT(*) "
                + ""
                + "	FROM `weekly_ticket`  w"
                + ""
                + "	INNER JOIN `job_response` jr"
                + "		ON jr.`job_response_ID` = w.`job_response_ID`"
                + ""
                + "	INNER JOIN `job` j"
                + "		ON j.`job_ID` = jr.`job_ID`"
                + ""
                + "	WHERE w.`deleted` = FALSE AND w.`paid` = FALSE"
                + "		AND j.`excavator_ID` = ?1"
                + "		AND (w.`weekly_end_date` <= ?3 OR jr.`end_date` <= ?3)"
                + ""
                + ""
                + "	) AS TotalOfUnPaidTicketByExcavator"
        )
                .setParameter(1, user.getUserID())
                .setParameter(2, DateFunction.getGMTDate())
                .setParameter(3, DateFunction.getGMTDate());
        //a.weeklyEndDate <= :currentDate OR a.jobresponseID.endDate <= :currentDate

    }

    public static Query getQueryForSearchTruckOwnerStatistics(GestionnaireEntite ges, User user) {

        return ges.getEntityManager().createNativeQuery(""
                + "SELECT"
                //+ "	-- Job qui attendent la confirmation du truck Owner"
                + "	(SELECT COUNT(*) "
                + ""
                + "	FROM `job` j "
                + ""
                + "	INNER JOIN `validation` v "
                + ""
                + "		ON j.`job_ID` = v.`job_ID` "
                + ""
                + "	INNER JOIN `job_response` jr"
                + "		ON jr.`job_response_ID` = v.`job_response_ID`"
                + ""
                + ""
                + "	WHERE v.`truck_owner_validation` = 0 AND v.`deleted` = FALSE  AND v.`client_validation` = 0 "
                + "		AND j.`job_creation_type` = 1 AND j.`deleted` = FALSE AND j.`end_date` >= ?2"
                + "		AND jr.`deleted` = FALSE  AND jr.`truck_owner_ID` = ?1 "
                + "	"
                + "	) AS TotalOfNonConfirmJobByTruckOwner,"
                + ""
                //+ "-- Liste des tickets non envoyé par le truckowner"
                + "	(SELECT COUNT(*)"
                + ""
                + "	FROM `daily_ticket` d"
                + ""
                + "	INNER JOIN `job_response` jr"
                + "		ON d.`job_response_ID` = jr.`job_response_ID`"
                + ""
                + "	WHERE d.`view_ticket` = FALSE  AND d.`deleted` = FALSE"
                + "		 AND jr.`truck_owner_ID` = ?1 "
                + "	) AS ToTalOfUnSendTicketByTruckOwner,"
                + ""
                + ""
                //+ "-- Liste des camions attendant la confirmation du truckowner"
                + "	(SELECT COUNT(*) "
                + ""
                + "	FROM `solicited_truck` s"
                + ""
                + "	INNER JOIN `job_response` jr"
                + "		ON jr.`job_response_ID` = s.`job_response_ID`"
                + ""
                + "	INNER JOIN `job` j"
                + "		ON j.`job_ID` = jr.`job_ID`"
                + ""
                + "	INNER JOIN `validation` v "
                + "		ON v.`job_response_ID` = jr.`job_response_ID`"
                + ""
                + "	WHERE s.`deleted` = FALSE AND v.`truck_owner_validation` = 0 AND v.`deleted` = FALSE "
                + "		AND j.`job_creation_type` = 1 AND j.`deleted` = FALSE AND j.`end_date` >= ?2"
                + "		AND jr.`deleted` = FALSE AND jr.`truck_owner_ID` = ?1 "
                + "	) AS TotalOfNonConfirmTruckByTruckOwner"
                + ""
        )
                .setParameter(1, user.getUserID())
                .setParameter(2, DateFunction.getGMTDate());

    }

}
