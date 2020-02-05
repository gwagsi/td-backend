/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util.metier.function;

import entities.Job;
import java.util.Date;
import javax.management.timer.Timer;
import toolsAndTransversalFunctionnalities.GestionnaireEntite;

/**
 *
 * @author erman
 */

public class JobFonction {
    

    public long jobRemainingTruck(Job job, GestionnaireEntite ges) {
        
        return job.getNumberOfTruck() - (long) ges.getEntityManager().createQuery("SELECT COUNT(DISTINCT s.truckID) FROM SolicitedTruck s WHERE s.deleted = FALSE AND s.truckAvailable = TRUE AND s.jobresponseID.jobID = :jobID")
                    .setParameter("jobID", job)
                    .getSingleResult();
    }
    
    private Date addDay(Date date, int numberOfDay){
        return new Date(date.getTime() + numberOfDay*Timer.ONE_DAY);
    }

}
