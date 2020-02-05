/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package employeeManagement.dao;

import toolsAndTransversalFunctionnalities.Result;
import util.exception.EmployeeManagementException;


/**
 *
 * @author erman
 */
public interface IEmployeeManagementDao {
    
    /**
     * Cette méthode permet de créer un employé et lui générer le code de validation qui lui permettra de valider les 
     * jobs de l'excavator a qui il appartient. Ce code doit ếtre unique pour tous les employées d'un même excavator.
     *
     * @param excavatorID identifiant de l'excavator
     * @param employeeName Nom de l'employé
     * @param employeePhoneNumber le numero de téléphone de l'employé
     * @param employeeEmail l'email de l'employé
     * @param description un description de son excavator.
     * @return
     * @throws util.exception.EmployeeManagementException
     */
    public Result generateEmployeeCode(int excavatorID, String employeeName, String employeePhoneNumber, String employeeEmail, String description) throws EmployeeManagementException;
    
    /**
     * Cette méthode permet de d'éditer les informations d'un employé ainsi que son code de validation.
     * 
     * @param excavatorID identifiant de l'excavator
     * @param employeesID identifiant de l'employé
     * @param employeeName Nom de l'employé
     * @param employeePhoneNumber le numero de téléphone de l'employé
     * @param employeeEmail l'email de l'employé
     * @param description un description de son excavator.
     * @param employeeCode le nouveau code de validtion de l'employé
     * @return
     * @throws util.exception.EmployeeManagementException
     */
    public String editEmployeeInfo(int excavatorID, int employeesID, String employeeName, String employeePhoneNumber, String employeeEmail, String description, String employeeCode) throws EmployeeManagementException;
    
    /**
     * Cette méthode recherche tous les employées d'un Excavator.
     *
     * @param excavatorID identifiant de l'excavator
     * @param index 
     * @param nombreMaxResultat
     * @return
     * @throws util.exception.EmployeeManagementException
     */
    public Result getARangeOfEmployeeByExcavator(int excavatorID, int index, int nombreMaxResultat) throws EmployeeManagementException;
    
    /**
     * Cette fonction permet a un excavator de modifier le status d'un de ses employés.
     * Cette modification peut consister a suspendre("SUSPEND") le compte de son employé, le
     * réactiver("ACTIF"), ...
     *
     * @param excavatorID l'identifiant de l'excavator
     * @param employeesID l'identifiant de l'employé dont on desire modifier son status
     * @param employeeStatus la valeur du status: "SUSPEND", "ACTIF", ...
     * @return
     * @throws util.exception.EmployeeManagementException
     */
    public String modifyEmployeeStatus(int excavatorID, int employeesID, String employeeStatus )  throws EmployeeManagementException;
    
    /**
     *
     * @param excavatorID
     * @param employeeID
     * @param code
     * @param generate
     * @return
     * @throws util.exception.EmployeeManagementException
     */
    public String editEmployeeCode(int excavatorID, int employeeID, String code, boolean generate) throws EmployeeManagementException;
    
}
