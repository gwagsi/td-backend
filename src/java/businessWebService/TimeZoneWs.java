/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package businessWebService;

import httpRestClient.HttpRestClient;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;

/**
 *
 * @author erman
 */
@WebService(serviceName = "TimeZoneWs")
@Stateless()
public class TimeZoneWs {

    /**
     * This is web service operation that return the time zone ID of a locality
     * @param accountID
     * @param latitude the latitude of the place that we would like to have time zone ID.
     * @param longitude the longitude of the place that we would like to have time zone ID.
     * @return a string that correspond to the time zone ID
     */
    @WebMethod(operationName = "getTimeZoneIDByGPSCoordinate")
    public String getTimeZoneIDByGPSCoordinate(@WebParam(name = "accountID") int accountID, @WebParam(name = "latitude") double latitude, @WebParam(name = "longitude") double longitude) {
        
       System.out.println("getTimeZoneIDByGPSCoordinate ~ latitude: " + latitude + " - longitude: " + longitude);
       
       String timeZoneID = HttpRestClient.getTimeZoneIDUsingGPS(latitude, longitude);
        
        System.out.println("getTimeZoneIDByGPSCoordinate ~ timeZoneID: " + timeZoneID + " - latitude: " + latitude + " - longitude: " + longitude);
        
        return (timeZoneID == null ? "" : timeZoneID);
    }
}
