/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin.billingManagement.metier;

import admin.billingManagement.dao.IAdminBillingManagementDaoLocal;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import toolsAndTransversalFunctionnalities.Result;

/**
 *
 * @author Sorelus
 */
@WebService(serviceName = "AdminBillingManagementWs")
@Stateless()
public class AdminBillingManagementWs implements IAdminBillingManagementWs {

    @EJB
    private IAdminBillingManagementDaoLocal dao;
    
    final static Logger logger = LogManager.getLogger("dump1");
    
    @WebMethod(operationName = "getNumberUserBilling")
    @Override
    public Result getNumberUserBilling(boolean statusValue, long statDate, long endDate) {
        String res;
        Result resultat = new Result();

        try {

            resultat = dao.getNumberUserBilling(statusValue, statDate, endDate);

        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
            resultat.setMsg(res);
            logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getNumberUserBilling"+"\n"
                            +"Erreur   : "+"\n"
                            +res);
            return resultat;
        }

        return resultat;

    }

    @WebMethod(operationName = "getListUserBilling")
    @Override
    public Result getListUserBilling(boolean statusValue, long statDate, long endDate, int index, int nombreMaxResult) {
        String res;
        Result resultat = new Result();

        try {

            resultat = dao.getlistUserBilling(statusValue, statDate, endDate, index, nombreMaxResult);
        } catch (Throwable e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
            resultat.setMsg(res);
             logger.error("\n"+"Class ws- : "+getClass()+"\n"
                            +"Fct   ws- : "+"getListUserBilling"+"\n"
                            +"Erreur   : "+"\n"
                            +res);
            return resultat;
        }
        
                return resultat;

    }
}
