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
public class JobManagementQuery {
    
    public static Query getQueryForSearchJobByExacavator(String name, GestionnaireEntite ges, User user){
        switch(name){
            case "getARangeOfNotStartedJobByExcavator":
                return ges.getEntityManager().createQuery("SELECT a FROM Job a WHERE a.deleted = FALSE AND a.excavatorID = :excavatorID AND a.startDate > :currentDate  ORDER BY a.creationDate DESC")
                        .setParameter("excavatorID", user)
                        .setParameter("currentDate", DateFunction.getGMTDate());
                        
                //return "SELECT a FROM Job a WHERE a.deleted = FALSE AND a.excavatorID  = :excavatorID AND a.startDate > :currentDate  ORDER BY a.creationDate DESC";
            
            case "getARangeOfActifJobByExcavator":
                return ges.getEntityManager().createQuery("SELECT a FROM Job a WHERE a.deleted = FALSE AND a.excavatorID  = :excavatorID AND a.startDate <= :firstCurrentDate AND a.endDate  >= :lastCurrentDate ORDER BY a.creationDate DESC")
                        .setParameter("excavatorID", user)
                        .setParameter("firstCurrentDate", DateFunction.getGMTDate())
                        .setParameter("lastCurrentDate", DateFunction.getGMTDate());
                //return "SELECT a FROM Job a WHERE a.deleted = FALSE AND a.excavatorID  = :excavatorID AND a.startDate <= :firstCurrentDate AND a.endDate  >= :lastCurrentDate ORDER BY a.creationDate DESC";
            
            case "getARangeOfPassJobExcavator":
                return ges.getEntityManager().createQuery("SELECT DISTINCT a FROM  Job a WHERE a.deleted = FALSE AND a.excavatorID =:excavatorID AND a.endDate < :currentDate  ORDER BY a.creationDate DESC")
                        .setParameter("excavatorID", user)
                        .setParameter("currentDate", DateFunction.getGMTDate());
                //return "SELECT DISTINCT a FROM  Job a WHERE a.deleted = FALSE AND a.excavatorID =:excavatorID AND a.endDate < :currentDate  ORDER BY a.creationDate DESC";
            
            case "getARangeOfJobForBillByExcavator":
                return ges.getEntityManager().createQuery("SELECT DISTINCT v.jobID FROM Validation v WHERE v.deleted = FALSE AND v.clientValidation = 1 AND v.truckOwnerValidation = 1 AND v.jobID.deleted = FALSE AND v.jobID.excavatorID =:excavatorID AND v.jobID.startDate <= :currentDate  GROUP BY v.jobID.jobID ORDER BY v.creationDate DESC")
                        .setParameter("excavatorID", user)
                        .setParameter("currentDate", DateFunction.getGMTDate());
                //return "SELECT DISTINCT v.jobID FROM Validation v WHERE v.deleted = FALSE AND v.clientValidation = 1 AND v.truckOwnerValidation = 1 AND v.jobID.deleted = FALSE AND v.jobID.excavatorID =:excavatorID AND v.jobID.startDate <= :currentDate ORDER BY v.creationDate DESC";
        }
        return null;
    }
    
    
    public static String getQueryToFindUserToNotify(){
        
        return ""
                + " SELECT u.account_ID, a.email, u.`name`, u.surname, u.cell_phone, u.telephone, -1 AS distanceWithin, 0 AS raduis"
                + " FROM `user` u"
                + " INNER JOIN account a "
                + "         ON a.account_ID = u.account_ID "
                + " LEFT JOIN user_notification_params n"
                + "         ON n.user_ID = u.user_ID AND n.deleted = FALSE"
                + " WHERE (n.user_ID IS NULL AND a.social_status_ID = ?3)"
                + " "
                + " "
                + " UNION"
                + " "
                + " "
                + " SELECT u.account_ID, n.email, u.`name`, u.surname, u.cell_phone, u.telephone, round(getDistance(n.latitude , n.longitude, ?1, ?2), 2) as distanceWithin, n.raduis       "
                + " FROM user_notification_params n"
                + " LEFT JOIN `user` u"
                + "         ON u.user_ID = n.user_ID AND n.deleted = FALSE"
                + " INNER JOIN account a "
                + "         ON a.account_ID = u.account_ID "
                + " WHERE n.user_ID IS NOT NULL AND a.social_status_ID = ?3 "
                + " HAVING"
                + "    (distanceWithin <= n.raduis OR n.raduis = -1) "
                ;
        
    }
    
    public static String getQueryToFindUserForEditJob(){
        
        return "SELECT u.account_ID, u.`name`, u.surname, a.email, -1 AS distanceWithin, 0 AS myRaduis"
                + " FROM `user` u"
                + " INNER JOIN account a "
                + "         ON a.account_ID = u.account_ID "
                + " LEFT JOIN user_notification_params n"
                + "        ON n.user_ID = u.user_ID AND n.deleted = FALSE"
                + " INNER JOIN job_response jr"
                + "         ON jr.truck_owner_ID = u.user_ID AND jr.job_ID = ?4 "
                + " WHERE (n.user_ID IS NULL AND a.social_status_ID = ?3)"
                + " "
                + " "
                + " UNION "
                + " "
                + " "
                + " SELECT u.account_ID, u.`name`, u.surname, n.email, getDistance(n.latitude , n.longitude, ?1, ?2) as distanceWithin, n.raduis "
                + " FROM user_notification_params n"
                + " LEFT JOIN `user` u"
                + "         ON u.user_ID = n.user_ID AND n.deleted = FALSE"
                + " INNER JOIN account a "
                + "         ON a.account_ID = u.account_ID "
                + " INNER JOIN job_response jr"
                + "         ON jr.truck_owner_ID = u.user_ID AND jr.job_ID = ?4 "
                + " WHERE n.user_ID IS NOT NULL AND a.social_status_ID = ?3 "
                ;
        
    }
    

}
