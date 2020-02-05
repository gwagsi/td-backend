/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package employeeManagement.metier;

import toolsAndTransversalFunctionnalities.Result;

/**
 *
 * @author erman
 */
public interface IEmployeeManagementWs {
    
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
     */
    public String generateEmployeeCode(int excavatorID, String employeeName, String employeePhoneNumber, String employeeEmail, String description);
    
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
     */
    public String editEmployeeInfo(int excavatorID, int employeesID, String employeeName, String employeePhoneNumber, String employeeEmail, String description, String employeeCode);
    
    /**
     * Cette méthode recherche tous les employées d'un Excavator.
     *
     * @param excavatorID identifiant de l'excavator
     * @param index 
     * @param nombreMaxResultat
     * @return
     */
    public Result getARangeOfEmployeeByExcavator(int excavatorID, int index, int nombreMaxResultat);
    
    /**
     * Cette fonction permet à un Excavator de suspendre le code d'un de ses employés
     *
     * @param excavatorID l'identifiant de l'excavator
     * @param employeesID l'identifiant du truckowner
     * @return
     */
    public String suspendEmployee(int excavatorID, int employeesID );
    
    /**
     * Cette fonction permet à un Excavator de réactiver le code d'un employé suspendu
     *
     * @param excavatorID
     * @param employeesID
     * @return
     */
    public String activateEmployee(int excavatorID, int employeesID );
    
    /**
     *
     * @param excavatorID
     * @param employeeID
     * @param code
     * @param generate
     * @return
     */
    public String editEmployeeCode(int excavatorID, int employeeID, String code, boolean generate);
    
}
