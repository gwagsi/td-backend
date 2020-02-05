/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.query.sql;

import entities.Account_;
import entities.Job;
import entities.Job_;
import entities.MonthlyBill;
import entities.MonthlyBill_;
import entities.User_;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import toolsAndTransversalFunctionnalities.GestionnaireEntite;
import util.date.DateFunction;

/**
 *
 * @author erman
 */
public class AdministrationQuery {

    public static Query getQueryForSearchUserByCategories(String categorie, boolean categorieValue, int typeOfUser, long startDate, long endDate, GestionnaireEntite ges) {

        switch (categorie) {
            case "SUSPEND":
                return ges.getEntityManager().createQuery("SELECT u FROM User u WHERE u.accountID.deleted = false AND u.accountID.suspend = :suspend AND u.accountID.socialstatusID.socialstatusID = :socialstatusID")
                        .setParameter("suspend", categorieValue)
                        .setParameter("socialstatusID", typeOfUser);

            case "ACTIF":
                return ges.getEntityManager().createQuery("SELECT u FROM User u WHERE u.accountID.deleted = false AND u.accountID.suspend = false AND u.accountID.actif = :actif AND u.accountID.socialstatusID.socialstatusID = :socialstatusID")
                        .setParameter("actif", categorieValue)
                        .setParameter("socialstatusID", typeOfUser);

            case "CONNECTED":
                return ges.getEntityManager().createQuery("SELECT u FROM User u WHERE u.accountID.deleted = false AND u.accountID.connected = :connected AND u.accountID.socialstatusID.socialstatusID = :socialstatusID")
                        .setParameter("connected", categorieValue)
                        .setParameter("socialstatusID", typeOfUser);

            case "DELETED":
                return ges.getEntityManager().createQuery("SELECT u FROM User u WHERE u.accountID.deleted = :deleted AND u.accountID.socialstatusID.socialstatusID = :socialstatusID")
                        .setParameter("deleted", categorieValue)
                        .setParameter("socialstatusID", typeOfUser);

            case "PAID":
                if (startDate == 0 || endDate == 0) {
                    return ges.getEntityManager().createQuery("SELECT DISTINCT m.accountID, MAX(m.monthlyEndDate) FROM MonthlyBill m WHERE m.deleted = FALSE AND m.paid = :paid AND m.accountID.deleted = FALSE AND m.monthlyEndDate <= :currentDate GROUP BY m.accountID  ")
                            .setParameter("paid", categorieValue)
                            .setParameter("currentDate", DateFunction.getGMTDate());
                } else {
                    return ges.getEntityManager().createQuery("SELECT DISTINCT m.accountID, MAX(m.monthlyEndDate) FROM MonthlyBill m WHERE m.deleted = FALSE AND m.paid = :paid AND m.accountID.deleted = FALSE AND m.monthlyEndDate BETWEEN :monthlyStartDate AND :monthlyEndDate GROUP BY m.accountID  ")
                            .setParameter("paid", categorieValue)
                            .setParameter("monthlyStartDate", new Date(startDate))
                            .setParameter("monthlyEndDate", new Date(endDate));
                }

        }
        return null;
    }

    public static Query getQueryCurrentUsersStatistics(GestionnaireEntite ges, String categorie, boolean categorieValue) {

        String queryCategorie;
        String withDeletedUser = "";
        switch (categorie) {
            case "SUSPEND":
                queryCategorie = "  AND a.`deleted`= false AND a.`suspend` = " + categorieValue;
                break;

            case "ACTIF":
                queryCategorie = "  AND a.`deleted`= false AND a.`suspend` = false AND a.`actif` = " + categorieValue;
                break;

            case "CONNECTED":
                queryCategorie = "  AND a.`deleted`= false AND a.`suspend` = false AND a.`actif` = true AND a.`connected` = " + categorieValue;
                break;

            case "DELETED":
                queryCategorie = " AND a.`deleted` = " + categorieValue;
                withDeletedUser = "";
                break;
            default:
                queryCategorie = "";
        }

        System.out.println("getQueryCurrentUsersStatistics: queryCategorie = " + queryCategorie);
        return ges.getEntityManager().createNativeQuery(""
                + ""
                + "     SELECT s.`social_status_ID`, COUNT(DISTINCT u.`user_ID`) "
                + ""
                + "     FROM `user` u "
                + ""
                + "     INNER JOIN `account` a ON a.`account_ID` = u.`account_ID` " + queryCategorie
                + ""
                + "     RIGHT JOIN `social_status` s ON a.`social_status_ID` = s.`social_status_ID` " + withDeletedUser
                + ""
                + "     GROUP BY s.`social_status_ID` "
                + ""
                + "     ORDER BY s.`social_status_ID` ASC"
                + "");

    }

    private static List<Predicate> getPostedJobPredicate(CriteriaBuilder criteriaBuilder, Root job, boolean categorieValue) {

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(criteriaBuilder.equal(job.get(Job_.deleted), false));

        if (categorieValue) {
            predicates.add(criteriaBuilder.greaterThan(job.get(Job_.startDate), criteriaBuilder.parameter(Date.class, "currentDate")));
        } else {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(job.get(Job_.startDate), criteriaBuilder.parameter(Date.class, "currentDate")));
        }
        return predicates;
    }

    private static List<Predicate> getListInfosBillingPredicates(CriteriaBuilder criteriaBuilder, Root monthlyRoot, Join accountMonth, long statDate, long endDate) {

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(monthlyRoot.get(MonthlyBill_.deleted), false));
        // predicates.add(criteriaBuilder.equal(billing.get(Account_.deleted), false));
        predicates.add(criteriaBuilder.equal(accountMonth.get(Account_.deleted), false));
        
        predicates.add(criteriaBuilder.equal(accountMonth.get(Account_.accountID), accountMonth.get(User_.accountID)));
        
        if (statDate > 0) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(monthlyRoot.get(MonthlyBill_.monthlyStartDate), new Date(statDate)));
        }
        if (endDate > 0) {
            predicates.add(criteriaBuilder.lessThan(monthlyRoot.get(MonthlyBill_.monthlyStartDate), new Date(endDate)));
        }

        predicates.add(criteriaBuilder.equal(monthlyRoot.get(MonthlyBill_.paid), criteriaBuilder.parameter(Date.class, "paid")));

        return predicates;
    }
    
  
    

    private static List<Predicate> getActifJobPredicate(CriteriaBuilder criteriaBuilder, Root job, boolean categorieValue) {

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(criteriaBuilder.equal(job.get(Job_.deleted), false));

        if (categorieValue) {
            predicates.add(criteriaBuilder.between(criteriaBuilder.parameter(Date.class, "currentDate"), job.get(Job_.startDate), job.get(Job_.endDate)));
        } else {
            predicates.add(criteriaBuilder.not(criteriaBuilder.between(criteriaBuilder.parameter(Date.class, "currentDate"), job.get(Job_.startDate), job.get(Job_.endDate))));
        }
        return predicates;
    }

    private static List<Predicate> getClosedJobPredicate(CriteriaBuilder criteriaBuilder, Root job, boolean categorieValue) {

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(criteriaBuilder.equal(job.get(Job_.deleted), false));

        predicates.add(criteriaBuilder.equal(job.get(Job_.close), categorieValue));

        return predicates;
    }

    private static List<Predicate> getDeletedJobPredicate(CriteriaBuilder criteriaBuilder, Root job, boolean categorieValue) {

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(criteriaBuilder.equal(job.get(Job_.deleted), categorieValue));

        return predicates;
    }

    private static List<Predicate> getPassedJobPredicate(CriteriaBuilder criteriaBuilder, Root job, boolean categorieValue) {

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(criteriaBuilder.equal(job.get(Job_.deleted), false));

        if (categorieValue) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(job.get(Job_.endDate), criteriaBuilder.parameter(Date.class, "currentDate")));
        } else {
            predicates.add(criteriaBuilder.greaterThan(job.get(Job_.endDate), criteriaBuilder.parameter(Date.class, "currentDate")));
        }
        return predicates;
    }

    public static Query getQueryForSearchJobsByCategories(String categorie, boolean categorieValue, long startDate, long endDate, GestionnaireEntite ges) {

        CriteriaBuilder criteriaBuilder = ges.getEntityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Job.class);
        Root jobRoot = criteriaQuery.from(Job.class);
        Query query;

        switch (categorie) {
            case "POSTED":
                criteriaQuery.where(criteriaBuilder.and(getPostedJobPredicate(criteriaBuilder, jobRoot, categorieValue).toArray(new Predicate[]{})));
                criteriaQuery.orderBy(criteriaBuilder.desc(jobRoot.get(Job_.creationDate)));
                return ges.getEntityManager().createQuery(criteriaQuery)
                        .setParameter("currentDate", DateFunction.getGMTDate());

            case "ACTIF":
                criteriaQuery.where(criteriaBuilder.and(getActifJobPredicate(criteriaBuilder, jobRoot, categorieValue).toArray(new Predicate[]{})));
                criteriaQuery.orderBy(criteriaBuilder.desc(jobRoot.get(Job_.creationDate)));
                return ges.getEntityManager().createQuery(criteriaQuery)
                        .setParameter("currentDate", DateFunction.getGMTDate());

            case "CLOSED":
                criteriaQuery.where(criteriaBuilder.and(getClosedJobPredicate(criteriaBuilder, jobRoot, categorieValue).toArray(new Predicate[]{})));
                criteriaQuery.orderBy(criteriaBuilder.desc(jobRoot.get(Job_.creationDate)));
                return ges.getEntityManager().createQuery(criteriaQuery);

            case "DELETED":
                criteriaQuery.where(criteriaBuilder.and(getDeletedJobPredicate(criteriaBuilder, jobRoot, categorieValue).toArray(new Predicate[]{})));
                criteriaQuery.orderBy(criteriaBuilder.desc(jobRoot.get(Job_.creationDate)));
                return ges.getEntityManager().createQuery(criteriaQuery);

            case "PASSED":
                criteriaQuery.where(criteriaBuilder.and(getPassedJobPredicate(criteriaBuilder, jobRoot, categorieValue).toArray(new Predicate[]{})));
                criteriaQuery.orderBy(criteriaBuilder.desc(jobRoot.get(Job_.creationDate)));
                return ges.getEntityManager().createQuery(criteriaQuery)
                        .setParameter("currentDate", DateFunction.getGMTDate());

            case "ALL":
                criteriaQuery.orderBy(criteriaBuilder.desc(jobRoot.get(Job_.creationDate)));
                return ges.getEntityManager().createQuery(criteriaQuery);

        }

        System.out.println("[getQueryForSearchJobsByCategories] Using of default criteria query");
        query = ges.getEntityManager().createQuery(criteriaQuery);
        return query;
    }

    public static Query getQueryJobsByStartitics(long startDate, long endDate, GestionnaireEntite ges) {

        CriteriaBuilder criteriaBuilder = ges.getEntityManager().getCriteriaBuilder();
        //CriteriaQuery<Tuple> cq= cb.createTupleQuery();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
        Root jobRoot = criteriaQuery.from(Job.class);

        List statData = new ArrayList();
        Predicate[] postedJobPredicate = getPostedJobPredicate(criteriaBuilder, jobRoot, true).toArray(new Predicate[]{});
        Predicate[] closedJobPredicate = getClosedJobPredicate(criteriaBuilder, jobRoot, true).toArray(new Predicate[]{});
        Predicate[] deletedJobPredicate = getDeletedJobPredicate(criteriaBuilder, jobRoot, true).toArray(new Predicate[]{});
        Predicate[] actifJobPredicate = getActifJobPredicate(criteriaBuilder, jobRoot, true).toArray(new Predicate[]{});
        Predicate[] passedJobPredicate = getPassedJobPredicate(criteriaBuilder, jobRoot, true).toArray(new Predicate[]{});
        Predicate allJobPredicate = criteriaBuilder.isTrue(criteriaBuilder.literal(true));// True predicate

        statData.add(criteriaBuilder.sum((criteriaBuilder.<Number>selectCase().when(criteriaBuilder.and(postedJobPredicate), 1).otherwise(0))).alias("NBPostedJob"));
        statData.add(criteriaBuilder.sum((criteriaBuilder.<Number>selectCase().when(criteriaBuilder.and(closedJobPredicate), 1).otherwise(0))).alias("NBClosedJob"));
        statData.add(criteriaBuilder.sum((criteriaBuilder.<Number>selectCase().when(criteriaBuilder.and(deletedJobPredicate), 1).otherwise(0))).alias("NBDeletedJob"));
        statData.add(criteriaBuilder.sum((criteriaBuilder.<Number>selectCase().when(criteriaBuilder.and(actifJobPredicate), 1).otherwise(0))).alias("NBActifJob"));
        statData.add(criteriaBuilder.sum((criteriaBuilder.<Number>selectCase().when(criteriaBuilder.and(passedJobPredicate), 1).otherwise(0))).alias("NBPassedJob"));
        statData.add(criteriaBuilder.sum((criteriaBuilder.<Number>selectCase().when(criteriaBuilder.and(allJobPredicate), 1).otherwise(1))).alias("NBAllJob"));

        List<Predicate> dateRangePredicate = new ArrayList<>();
        if (startDate > 0) {
            dateRangePredicate.add(criteriaBuilder.greaterThan(jobRoot.get(Job_.creationDate), new Date(startDate)));
        }

        if (endDate > 0) {
            dateRangePredicate.add(criteriaBuilder.lessThan(jobRoot.get(Job_.creationDate), new Date(endDate)));
        }

        criteriaQuery.where(dateRangePredicate.toArray(new Predicate[]{}));
        criteriaQuery.multiselect(statData);
        return ges.getEntityManager()
                .createQuery(criteriaQuery)
                .setParameter("currentDate", DateFunction.getGMTDate());
    }

    public static Query getQueryForAdminCountUserByStatus(boolean statusValue, long statDate, long endDate, GestionnaireEntite ges) {

        CriteriaBuilder criteriaBuilder = ges.getEntityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root monthlyRoot = criteriaQuery.from(MonthlyBill.class);
        criteriaQuery.distinct(true);
        Join accountMonth = monthlyRoot.join(MonthlyBill_.accountID);
        
        criteriaQuery.select(criteriaBuilder.countDistinct(monthlyRoot.get(MonthlyBill_.accountID)));
        criteriaQuery.where(criteriaBuilder.and(getListInfosBillingPredicates(criteriaBuilder, monthlyRoot, accountMonth, statDate, endDate).toArray(new Predicate[]{})));
        return ges.getEntityManager().createQuery(criteriaQuery)
                .setParameter("paid", statusValue)
                ;

    }

    public static Query getQueryForAdminSearchUserByStatus(boolean statusValue, long statDate, long endDate, GestionnaireEntite ges) {

        /* ceci fonctionne tres bien mais j'ai un problème pr atteindre account.delete
         CriteriaBuilder criteriaBuilder = ges.getEntityManager().getCriteriaBuilder();
         CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(MonthlyBill.class);
        
         Root jobRoot = criteriaQuery.from(MonthlyBill.class);
         criteriaQuery.distinct(true);
         criteriaQuery.select(jobRoot.get(MonthlyBill_.accountID)).distinct(true);
         */
        CriteriaBuilder criteriaBuilder = ges.getEntityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(MonthlyBill.class);
        Root monthlyRoot = criteriaQuery.from(MonthlyBill.class);
        //Join p = monthRoot.join(MonthlyBill_.accountID).join(Account_.user);
        Join accountMonth = monthlyRoot.join(MonthlyBill_.accountID);
        
        criteriaQuery.select(monthlyRoot.get(MonthlyBill_.accountID)).distinct(true);
        //criteriaQuery.select(accountMonth.get(MonthlyBill_.accountID)).distinct(true);

        criteriaQuery.where(criteriaBuilder.and(getListInfosBillingPredicates(criteriaBuilder, monthlyRoot, accountMonth, statDate, endDate).toArray(new Predicate[]{})));

        Query query = ges.getEntityManager().createQuery(criteriaQuery)
                .setParameter("paid", statusValue)
                ;
        
        return query;

    }
}
