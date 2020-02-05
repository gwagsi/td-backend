/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package accountManagement.dao;

/**
 *
 * @author erman
 */
public final class TypeOfUser {
    
    public static final int SOCIAL_STATUS_EXCAVATOR_ID = 1;
    public static final int SOCIAL_STATUS_TRUCK_OWNER_ID = 2;
    public static final int SOCIAL_STATUS_GOV_ID = 3;
    public static final int SOCIAL_STATUS_SIMPLE_USER_ID = 4;
    public static final int SOCIAL_STATUS_DRIVER_ID = 5;
    public static String getStringValue(int socialStatusID){
        switch(socialStatusID){
            case SOCIAL_STATUS_EXCAVATOR_ID : return "Excavator";
            case SOCIAL_STATUS_TRUCK_OWNER_ID : return "Truck_Owner";
            case SOCIAL_STATUS_GOV_ID : return "Govment";
            case SOCIAL_STATUS_SIMPLE_USER_ID : return "Simple User";
            case SOCIAL_STATUS_DRIVER_ID : return "Driver";
            default: return "USER";
        
        }
    
    }
    
    
}
