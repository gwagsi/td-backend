/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util.metier.function;

import entities.Drive;
import entities.Driver;
import entities.Truck;

/**
 *
 * @author erman
 */
public class DriverFunction {
    
    public static String getDriverForTruck(Truck truck) {
        
        String driverInfo;
        if (truck.getDriveList() != null && !truck.getDriveList().isEmpty()) {
            Drive drive = truck.getDriveList().get(0);
            Driver driver = drive.getDriverID();
            if (!drive.getDeleted()) {
                driverInfo = "" + driver.getDriverID();
                driverInfo += ";" + driver.getName();
                driverInfo += ";" + driver.getSurname();
                driverInfo += ";" + driver.getIdCardNumber();
                driverInfo += ";" + driver.getLicense();
            } else {
                driverInfo = "-1";
            }
        } else {
            driverInfo = "-1";
        }
        return driverInfo;
    }

}
