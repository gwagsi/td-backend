/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package employeeManagement.dao;

/**
 *
 * @author erman
 */
public final class EmployeeStatus {
    
    public static final String SUSPEND_EMP = "SUSPEND";
    public static final String ACTIF_EMP = "ACTIF";
    
    public static String getStatus(int statusID){
        switch(statusID){
            case 0: return ACTIF_EMP;
            case 1: return SUSPEND_EMP;
            default: return ACTIF_EMP;
        }
    }
    
    public static int getStatusID(String status){
        switch(status){
            case "ACTIF": return 0;
            case "SUSPEND": return 1;
            default: return 0;
        }
    }
}
