/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package employeeManagement.dao;

import entities.Account;
import entities.Employee;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import toolsAndTransversalFunctionnalities.CodeGenerate;
import toolsAndTransversalFunctionnalities.GestionnaireEntite;
import toolsAndTransversalFunctionnalities.Result;
import util.date.DateFunction;
import util.exception.DriverManagementException;
import util.exception.EmployeeManagementException;

/**
 *
 * @author erman
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class EmployeeManagementDao implements IEmployeeManagementDaoLocal, IEmployeeManagementDaoRemote {
    
    @EJB
    GestionnaireEntite ges;
     final static org.apache.logging.log4j.Logger logger = LogManager.getLogger("dump1");
    
    @Override
    public Result generateEmployeeCode (int excavatorID, String employeeName, String employeePhoneNumber, String employeeEmail, String description)  throws EmployeeManagementException{
        
        String res = "good";
        String employeeCode = "error";
        Account account;
        
        Employee employee;
        Result result = new Result();
       
        try{
            
            ges.creatEntityManager();
            
            account = (Account) ges.getEntityManager().find(Account.class, excavatorID);

            if (account == null) {
                ges.closeEm();
                result.setMsg("InvalidAccountID");
                result.afficherResult("generateEmployeeCode");
                return result;
            }
            
            employee = new Employee();
            employee.setExcavatorID(account.getUser());
            employee.setEmpEmail(employeeEmail);
            employee.setEmpName(employeeName);
            employee.setEmpCode(employeeCode);
            employee.setPhoneNumber(employeePhoneNumber);
            employee.setDescription(description);
            employee.setEmpStatus(EmployeeStatus.ACTIF_EMP);
            employee.setDeleted(false);
            employee.setCreationDate(DateFunction.getGMTDate());
            ges.getEntityManager().persist(employee);
            ges.closeEm();

            List<String> userCodeList = ges.getEntityManager().createQuery("SELECT e.empCode FROM Employee e WHERE e.deleted = FALSE AND e.excavatorID = :accountID AND e != :employee")
                    .setParameter("accountID", account.getUser())
                    .setParameter("employee", employee)
                    .getResultList();

            if (userCodeList == null) {
                userCodeList = new ArrayList<>();
            }
            userCodeList.add(account.getUser().getUserCode());

            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"generateEmployeeCode:  userCodeList = " + userCodeList);
            String userCode = CodeGenerate.generateCode(employee.getEmployeeID(), userCodeList);

            employee.setEmpCode(userCode);
            ges.getEntityManager().merge(employee);
            ges.closeEm();

            res += ";" + employee.getEmployeeID();

            String employeeInfo = "" + employee.getEmpName();//0
            employeeInfo += ";" + employee.getEmpCode();//1
            employeeInfo += ";" + employee.getEmpEmail();//2
            employeeInfo += ";" + employee.getPhoneNumber();//3
            employeeInfo += ";" + employee.getCreationDate();//4
            employeeInfo += ";" + employee.getExcavatorID().getAccountID().getEmail();//5
            employeeInfo += ";" + employee.getExcavatorID().getName();//6
            employeeInfo += ";" + employee.getExcavatorID().getSurname();//7
            
            result.setMsg(res);
            result.setObject(employeeInfo);

        } catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            result.setMsg(res + th.getMessage());
        }
        result.afficherResult("generateEmployeeCode");
        return result;
    }
    
    @Override
    public String editEmployeeInfo(int excavatorID, int employeesID, String employeeName, String employeePhoneNumber, String employeeEmail, String description, String employeeCode)  throws EmployeeManagementException{
        
        String res = "good";
        Account account;
        
        Employee employee;
        try{
            
            ges.creatEntityManager();
            
            account = (Account) ges.getEntityManager().find(Account.class, excavatorID);

            if (account == null) {
                ges.closeEm();
                return "InvalidAccountID";
            }
            employee = (Employee) ges.getEntityManager().find(Employee.class, employeesID);
            
            if (employee == null) {
                ges.closeEm();
                return "InvalidEmployeeID";
            }
            
            employee.setEmpEmail(employeeEmail);
            employee.setEmpName(employeeName);
            employee.setEmpCode(employeeCode);
            employee.setPhoneNumber(employeePhoneNumber);
            employee.setDescription(description);
            try {
                ges.getEntityManager().merge(employee);
                ges.closeEm();
            } catch (Exception e) {
                return "ExistingCode";
            }
            res += ";" + employee.getEmployeeID();

            String employeeInfo = "" + employee.getEmpName();
            employeeInfo += "##" + employee.getEmpCode();
            employeeInfo += "##" + employee.getEmpEmail();
            employeeInfo += "##" + employee.getPhoneNumber();
            employeeInfo += "##" + employee.getCreationDate();
            employeeInfo += "##" + employee.getExcavatorID().getName();
            employeeInfo += "##" + employee.getExcavatorID().getSurname();
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editEmployeeInfo: employeeInfo = " + employeeInfo);
        } catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            throw new EmployeeManagementException(getClass()+"","editEmployeeInfo",1,res,res);
        
        }
        
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editEmployeeInfo: res = " + res);
        return res;
    }

    @Override
    public Result getARangeOfEmployeeByExcavator(int excavatorID, int index, int nombreMaxResultat)  throws EmployeeManagementException{
        
        List<Employee> employeeList;
        Result result = new Result();
        List<String> listResult;
        int numberOfElts = 0;
        Account account;
        String res = "";
        
        try{
            ges.creatEntityManager();
            account = (Account) ges.getEntityManager().find(Account.class, excavatorID);
            
            if (account == null) {
                ges.closeEm();
                result.setMsg("InvalidAccountID");
                result.afficherResult("generateEmployeeCode");
                return result;
            }
            
            Query query =  ges.getEntityManager().createQuery("SELECT e FROM Employee e WHERE e.deleted = FALSE AND e.excavatorID = :accountID")
                    .setParameter("accountID", account.getUser());
            
            numberOfElts = query.getResultList().size();
            query.setFirstResult(index);
            query.setMaxResults(nombreMaxResultat);
            employeeList = (List<Employee>)query.getResultList();
            result.setMsg("good");

        } catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            result.setMsg(res);
            result.afficherResult("generateEmployeeCode");
            return result;
        }

        listResult = new ArrayList<>();

        for (Employee employee : employeeList) {
            res = "" + employee.getEmployeeID()+ ";"//0
                    + employee.getEmpName()+ ";"
                    + employee.getEmpEmail()+ ";"
                    + employee.getPhoneNumber()+ ";"
                    + employee.getDescription()+ ";"
                    + EmployeeStatus.getStatusID(employee.getEmpStatus()) + ";"
                    + employee.getEmpCode()+ ";"
                    + "null";
            listResult.add(res);
        }
        
        listResult.add("" + numberOfElts);
        result.setObjectList(listResult);
        result.afficherResult("generateEmployeeCode");
        return result;
    }

    
    @Override
    public String modifyEmployeeStatus(int excavatorID, int employeesID, String employeeStatus )  throws EmployeeManagementException{
        
        String res = "good";
        Account account;
        
        Employee employee;
        try{
            
            ges.creatEntityManager();
            account = (Account) ges.getEntityManager().find(Account.class, excavatorID);

            if (account == null) {
                ges.closeEm();
                return "InvalidAccountID";
            }
            
            employee = (Employee) ges.getEntityManager().find(Employee.class, employeesID);

            if (employee == null) {
                ges.closeEm();
                return "InvalidEmployeeID";
            }
            
            employee.setEmpStatus(employeeStatus);
            ges.getEntityManager().merge(employee);
            ges.closeEm();
            res += ";" + employee.getEmployeeID();

            String employeeInfo = "" + employee.getEmpName();
            employeeInfo += "##" + employee.getEmpCode();
            employeeInfo += "##" + employee.getEmpEmail();
            employeeInfo += "##" + employee.getPhoneNumber();
            employeeInfo += "##" + employee.getCreationDate();
            employeeInfo += "##" + employee.getExcavatorID().getName();
            employeeInfo += "##" + employee.getExcavatorID().getSurname();
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editEmployeeInfo: employeeInfo = " + employeeInfo);
        } catch (Throwable th) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            th.printStackTrace(str);
            res = string.toString();
            throw new EmployeeManagementException(getClass()+"","modifyEmployeeStatus",1,string.toString(),string.toString());
        
        }
        
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editEmployeeInfo: res = " + res);
        return res;
    }

    
    @Override
    public String editEmployeeCode(int excavatorID, int employeeID, String code, boolean generate)  throws EmployeeManagementException{
        
        Account account;
        Employee employee;
        
        try {

            ges.creatEntityManager();

            account = (Account) ges.getEntityManager().find(Account.class, excavatorID);
            employee = (Employee) ges.getEntityManager().find(Employee.class, employeeID);

            if (account == null) {
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editEmployeeCode: InvalidAccountID ");
                return "InvalidAccountID";
            }

            if (employee == null) {
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editEmployeeCode: InvalidEmployeeID ");
                return "InvalidEmployeeID";
            }

            if (!generate) {
                if (!code.equals(account.getUser().getUserCode())) {
                    try {
                        employee.setEmpCode(code);
                        ges.getEntityManager().merge(employee);
                        ges.closeEm();
                    } catch (Exception e) {
                        return "ExistingCode";
                    }
                } else {
                    return "ExistingCode";
                }
            } else {
                List<String> userCodeList = ges.getEntityManager().createQuery("SELECT e.empCode FROM Employee e WHERE e.deleted = FALSE AND e.excavatorID = :accountID AND e != :employee")
                        .setParameter("accountID", account.getUser())
                        .setParameter("employee", employee)
                        .getResultList();
                
                if(userCodeList == null){
                    userCodeList = new ArrayList<>();
                }
                userCodeList.add(account.getUser().getUserCode());
                
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editEmployeeCode:  userCodeList == " + userCodeList);
                String userCode = CodeGenerate.generateCode(employee.getEmployeeID(), userCodeList);

                employee.setEmpCode(userCode);
                ges.getEntityManager().merge(employee);
                ges.closeEm();
                logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editEmployeeCode:  userCode == " + userCode);
            }
            
        } catch (Throwable th) {
            logger.error("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editEmployeeCode: Error: " + th.getMessage());
            //return "" +  th.getMessage();
              throw new EmployeeManagementException(getClass()+"","editEmployeeCode",1,th.getMessage(),th.getMessage());
        
        
        }

        String res =  "good;" + employee.getEmpEmail()+ ";"
                + employee.getEmpName()+ ";" 
                + employee.getPhoneNumber()+ ";" 
                + employee.getDescription()+ ";" 
                + employee.getEmployeeID()+ ";"
                ;
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"editEmployeeCode: return: " + res);
        return res;
    }


}
